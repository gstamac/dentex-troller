#!/usr/bin/env bash
# Builds the offline Adriatic chart (adriatic.pmtiles) from open data.
#
#   Depths:    EMODnet Bathymetry DTM 2024, CC BY 4.0 (about 115 m grid, not for navigation)
#   Shoreline: OpenStreetMap land polygons, (c) OpenStreetMap contributors, ODbL
#
# Runs inside the image built from tools/charts/Dockerfile:
#   /data  downloads (kept between runs; delete a file to fetch it again)
#   /out   adriatic.pmtiles and the intermediate files in /out/work
set -euo pipefail

DATA=/data
OUT=/out
WORK=$OUT/work
mkdir -p "$DATA" "$WORK"

# Adriatic from the Strait of Otranto to Trieste.
XMIN=12.0; YMIN=39.75; XMAX=20.05; YMAX=45.85

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
            "$ERDDAP?elevation%5B($south):1:($north)%5D%5B($XMIN):1:($XMAX)%5D"
    fi
done
LAND_ZIP="$DATA/land-polygons-split-4326.zip"
if [[ ! -s "$LAND_ZIP" ]]; then
    curl -sS -f --retry 3 -o "$LAND_ZIP" https://osmdata.openstreetmap.de/download/land-polygons-split-4326.zip
fi

echo "== depth grid"
strips=()
for file in "$DATA"/emodnet_*.nc; do strips+=("NETCDF:\"$file\":elevation"); done
gdalbuildvrt -q -overwrite -a_srs EPSG:4326 "$WORK/depth.vrt" "${strips[@]}"

echo "== depth bands"
rm -f "$WORK/bands.gpkg" "$WORK/contours.gpkg"
# shellcheck disable=SC2086
gdal_contour -q -p -amin elev_min -amax elev_max -fl $LEVELS -f GPKG "$WORK/depth.vrt" "$WORK/bands.gpkg"
ogr2ogr -f GeoJSONSeq "$WORK/depth_area.geojsons" "$WORK/bands.gpkg" -dialect SQLite \
    -sql "SELECT CAST(-elev_max AS INTEGER) AS depth, geom FROM contour WHERE elev_max <= 0"

echo "== contour lines"
# shellcheck disable=SC2086
gdal_contour -q -a elev -fl $LEVELS -f GPKG "$WORK/depth.vrt" "$WORK/contours.gpkg"
ogr2ogr -f GeoJSONSeq "$WORK/depth_contour.geojsons" "$WORK/contours.gpkg" -dialect SQLite \
    -sql "SELECT CAST(-elev AS INTEGER) AS depth, geom FROM contour WHERE elev < 0"

echo "== shoreline"
ogr2ogr -f GeoJSONSeq "$WORK/land.geojsons" \
    "/vsizip/$LAND_ZIP/land-polygons-split-4326/land_polygons.shp" \
    -spat $XMIN $YMIN $XMAX $YMAX -clipsrc spat_extent -select ""

echo "== tiles"
# Minor contours only appear from zoom 11; the major ones are kept from the start.
tippecanoe -q -o "$OUT/adriatic.pmtiles" --force -Z5 -z14 \
    --name "Adriatic depths" \
    --attribution "EMODnet Bathymetry DTM 2024 (CC BY 4.0); OpenStreetMap contributors (ODbL)" \
    --detect-shared-borders --coalesce-densest-as-needed --extend-zooms-if-still-dropping \
    -j '{"depth_contour":["any",[">=","$zoom",11],["in","depth",10,20,50,100,200,500,1000]]}' \
    -L "land:$WORK/land.geojsons" \
    -L "depth_area:$WORK/depth_area.geojsons" \
    -L "depth_contour:$WORK/depth_contour.geojsons"

ls -l "$OUT/adriatic.pmtiles"
