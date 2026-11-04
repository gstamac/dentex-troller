#!/usr/bin/env bash
# Builds the offline Adriatic chart from open data:
#
#   adriatic.pmtiles          vector tiles: land, depth bands, contours and seamarks
#   adriatic-rainbow.pmtiles  raster tiles: depths in EMODnet's "rainbow colour" ramp
#
#   Depths:    EMODnet Bathymetry DTM 2024, CC BY 4.0 (about 115 m grid, not for navigation)
#   Shoreline: OpenStreetMap land polygons, (c) OpenStreetMap contributors, ODbL
#   Seamarks:  OpenStreetMap / OpenSeaMap via Geofabrik extracts, (c) OpenStreetMap contributors, ODbL
#
# Runs inside the image built from tools/charts/Dockerfile:
#   /data  downloads (kept between runs; delete a file to fetch it again)
#   /out   the .pmtiles files and the intermediate files in /out/work
#
# Usage: build-chart.sh [all|chart|rainbow]   (default: all)
set -euo pipefail

TARGET=${1:-all}
case "$TARGET" in
    all|chart|rainbow) ;;
    *) echo "usage: build-chart.sh [all|chart|rainbow]" >&2; exit 2 ;;
esac

DATA=/data
OUT=/out
WORK=$OUT/work
mkdir -p "$DATA" "$WORK"

# Adriatic from the Strait of Otranto to Trieste. The downloads always cover all of it.
XMIN=12.0; YMIN=39.75; XMAX=20.05; YMAX=45.85
DL_XMIN=$XMIN; DL_XMAX=$XMAX
# BBOX="west south east north" builds only that part, for quick test builds, for example
#   docker run -e BBOX="14.68 44.14 14.91 44.30" ...   (about 18 x 18 km)
if [[ -n "${BBOX:-}" ]]; then
    read -r XMIN YMIN XMAX YMAX <<< "$BBOX"
    echo "== area $XMIN $YMIN $XMAX $YMAX"
fi

# Contour depths in metres. Each band in depth_area starts at one of these depths.
DEPTHS="0 2 5 10 15 20 25 30 40 50 60 70 80 90 100 125 150 200 300 500 750 1000 1250"
# gdal_contour wants elevations in increasing order, so the deepest level comes first.
LEVELS=$(for d in $DEPTHS; do echo "$d"; done | sort -rn | while read -r d; do printf -- '%s ' "$((-d))"; done)

# NetCDF keeps the float depths; the ERDDAP GeoTIFF output is scaled to 8 bits.
ERDDAP="https://erddap.emodnet.eu/erddap/griddap/bathymetry_dtm_2024.nc"
STRIPS=("39.75 41.30" "41.30 42.85" "42.85 44.40" "44.40 45.85")

echo "== downloads"
for i in "${!STRIPS[@]}"; do
    file="$DATA/emodnet_$((i + 1)).nc"
    if [[ ! -s "$file" ]]; then
        read -r south north <<< "${STRIPS[$i]}"
        curl -sS -f --retry 3 --max-time 1800 -o "$file" \
            "$ERDDAP?elevation%5B($south):1:($north)%5D%5B($DL_XMIN):1:($DL_XMAX)%5D"
    fi
done
LAND_ZIP="$DATA/land-polygons-split-4326.zip"
if [[ ! -s "$LAND_ZIP" ]]; then
    curl -sS -f --retry 3 -o "$LAND_ZIP" https://osmdata.openstreetmap.de/download/land-polygons-split-4326.zip
fi

echo "== depth grid"
strips=()
for file in "$DATA"/emodnet_*.nc; do strips+=("NETCDF:\"$file\":elevation"); done
gdalbuildvrt -q -overwrite -a_srs EPSG:4326 -te $XMIN $YMIN $XMAX $YMAX "$WORK/depth.vrt" "${strips[@]}"
# EMODnet leaves land cells empty (NaN). Contouring around empty cells cuts square holes, one
# grid cell each, into the depth bands along the shore. Giving land a height of 1 m lets the 0 m
# line run smoothly between the last water cell and the first land cell; the OSM shoreline is
# drawn on top of it.
rm -f "$WORK/depth_filled.tif"
gdal_calc.py --quiet --hideNoData -A "$WORK/depth.vrt" --outfile="$WORK/depth_filled.tif" \
    --calc="numpy.where(numpy.isnan(A), 1.0, A)" --type=Float32 --NoDataValue=none \
    --co=TILED=YES --co=COMPRESS=DEFLATE --co=BIGTIFF=IF_SAFER
DEPTH="$WORK/depth_filled.tif"

if [[ "$TARGET" != rainbow ]]; then
echo "== depth bands"
rm -f "$WORK/bands.gpkg" "$WORK/contours.gpkg"
# shellcheck disable=SC2086
gdal_contour -q -p -amin elev_min -amax elev_max -fl $LEVELS -f GPKG "$DEPTH" "$WORK/bands.gpkg"
ogr2ogr -f GeoJSONSeq "$WORK/depth_area.geojsons" "$WORK/bands.gpkg" -dialect SQLite \
    -sql "SELECT CAST(-elev_max AS INTEGER) AS depth, geom FROM contour WHERE elev_max <= 0"

echo "== contour lines"
# shellcheck disable=SC2086
gdal_contour -q -a elev -fl $LEVELS -f GPKG "$DEPTH" "$WORK/contours.gpkg"
ogr2ogr -f GeoJSONSeq "$WORK/depth_contour.geojsons" "$WORK/contours.gpkg" -dialect SQLite \
    -sql "SELECT CAST(-elev AS INTEGER) AS depth, geom FROM contour WHERE elev < 0"

echo "== shoreline"
ogr2ogr -f GeoJSONSeq "$WORK/land.geojsons" \
    "/vsizip/$LAND_ZIP/land-polygons-split-4326/land_polygons.shp" \
    -spat $XMIN $YMIN $XMAX $YMAX -clipsrc spat_extent -select ""

echo "== seamarks"
# Geofabrik extracts of the countries on the Adriatic, with a rough box each (west south east
# north). Only the extracts that overlap the build area are downloaded: about 190 MB for a test
# area in Croatia, about 2.1 GB for the whole Adriatic.
EXTRACTS=(
    "europe/croatia 13.4 42.3 19.5 46.6"
    "europe/slovenia 13.3 45.4 16.7 46.9"
    "europe/bosnia-herzegovina 15.7 42.5 19.7 45.3"
    "europe/montenegro 18.4 41.8 20.4 43.6"
    "europe/albania 19.2 39.6 21.1 42.7"
    "europe/italy/nord-est 10.4 43.7 13.95 47.1"
    "europe/italy/centro 9.6 41.2 14.0 44.0"
    "europe/italy/sud 13.0 37.9 18.6 42.9"
)
mkdir -p "$DATA/osm"
rm -f "$WORK"/seamarks_*.geojsons
for extract in "${EXTRACTS[@]}"; do
    read -r region west south east north <<< "$extract"
    overlaps=$(awk -v a="$west" -v b="$south" -v c="$east" -v d="$north" \
        -v w="$XMIN" -v s="$YMIN" -v e="$XMAX" -v n="$YMAX" \
        'BEGIN { print (a < e && c > w && b < n && d > s) ? 1 : 0 }')
    [[ "$overlaps" == 1 ]] || continue
    name=${region//\//_}
    pbf="$DATA/osm/$name.osm.pbf"
    if [[ ! -s "$pbf" ]]; then
        curl -sS -f -L --retry 3 -o "$pbf.part" "https://download.geofabrik.de/$region-latest.osm.pbf"
        mv "$pbf.part" "$pbf"
    fi
    osmium extract --overwrite -s smart -b "$XMIN,$YMIN,$XMAX,$YMAX" -o "$WORK/area.osm.pbf" "$pbf"
    osmium tags-filter --overwrite -o "$WORK/seamarks.osm.pbf" "$WORK/area.osm.pbf" \
        nwr/seamark:type nwr/leisure=marina,slipway nwr/harbour nwr/waterway=fuel
    osmium export --overwrite -f geojsonseq --add-unique-id=type_id \
        --geometry-types=point,linestring,polygon -o "$WORK/seamarks_$name.geojsons" "$WORK/seamarks.osm.pbf"
done
seamarks.py "$WORK/seamark.geojsons" "$WORK"/seamarks_*.geojsons

echo "== tiles"
# Minor contours only appear from zoom 11; the major ones are kept from the start. -r1 keeps
# every seamark from the zoom its feature asks for, instead of thinning points at low zooms.
tippecanoe -q -o "$OUT/adriatic.pmtiles" --force -Z5 -z14 -r1 \
    --name "Adriatic depths" \
    --attribution "EMODnet Bathymetry DTM 2024 (CC BY 4.0); OpenStreetMap contributors (ODbL)" \
    --detect-shared-borders --coalesce-densest-as-needed --extend-zooms-if-still-dropping \
    -j '{"depth_contour":["any",[">=","$zoom",11],["in","depth",10,20,50,100,200,500,1000]]}' \
    -L "land:$WORK/land.geojsons" \
    -L "depth_area:$WORK/depth_area.geojsons" \
    -L "depth_contour:$WORK/depth_contour.geojsons" \
    -L "seamark:$WORK/seamark.geojsons"

ls -l "$OUT/adriatic.pmtiles"
fi

if [[ "$TARGET" != chart ]]; then
echo "== rainbow depths"
# EMODnet's "rainbowcolour" style of the emodnet:mean WMS layer, with land left transparent like
# the "no land" variant. Colours are interpolated linearly between the stops, as in the WMS.
# The legend in the app (ChartStyle.RAINBOW) uses the same stops.
cat > "$WORK/rainbow.txt" <<'COLORS'
nv      0   0   0   0
-6000 182   0 208 255
-5500 120  24 207 255
-4800  18   0 145 255
-3000   0  87 217 255
-500    0 199 220 255
-200    2 228  17 255
-40   229 235   2 255
-5    255   8  30 255
0     206   0  16 255
0.01    0   0   0   0
9000    0   0   0   0
COLORS
# Pixels sit exactly on the zoom 11 tile grid (156543.03392804097 / 2^11 m), so the tiles need no
# second resampling. The grid is about 115 m, so zoom 11 keeps all of its detail.
RES=76.43702828517625
gdalwarp -q -overwrite -multi -wm 1024 -te $XMIN $YMIN $XMAX $YMAX -te_srs EPSG:4326 -t_srs EPSG:3857 \
    -tr $RES $RES -tap -r bilinear -ot Float32 -dstnodata nan \
    -co TILED=YES -co COMPRESS=DEFLATE -co BIGTIFF=IF_SAFER "$DEPTH" "$WORK/depth_3857.tif"
gdaldem color-relief -q -alpha -co TILED=YES -co COMPRESS=DEFLATE -co BIGTIFF=IF_SAFER \
    "$WORK/depth_3857.tif" "$WORK/rainbow.txt" "$WORK/rainbow_rgba.tif"
rm -f "$WORK/rainbow.mbtiles"
gdal_translate -q -of MBTiles -co TILE_FORMAT=WEBP -co QUALITY=80 \
    -co NAME="Adriatic rainbow depths" "$WORK/rainbow_rgba.tif" "$WORK/rainbow.mbtiles"
# Zooms 10 down to 5.
gdaladdo -q -r average "$WORK/rainbow.mbtiles" 2 4 8 16 32 64
pmtiles convert "$WORK/rainbow.mbtiles" "$OUT/adriatic-rainbow.pmtiles"
ls -l "$OUT/adriatic-rainbow.pmtiles"
fi
