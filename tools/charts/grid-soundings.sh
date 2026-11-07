#!/usr/bin/env bash
# Grids depth soundings from CSV files and lays them over a base depth grid.
#
# Usage: grid-soundings.sh BASE OUT "west south east north" CELL FILL FILE...
#
#   BASE   the depth grid to fill in around the soundings: elevation in metres, negative below
#          the surface, in EPSG:4326 (the EMODnet grid of build-chart.sh). "none" keeps only the
#          soundings: cells too far from any sounding stay empty (nodata -99999).
#   OUT    the merged grid, a GeoTIFF on a CELL x CELL degree grid over the area
#   CELL   grid cell in degrees, for example 0.0002 (about 22 m north-south, 16 m east-west)
#   FILL   how many cells a gap between soundings is filled across; wider gaps keep the base grid
#   FILE   CSV files with a header line and the columns lon,lat,depth in that order: WGS84 decimal
#          degrees and metres. Further columns are ignored.
#
# SOUNDINGS_SIGN=depth (default) reads depth as positive below the surface; elevation reads it as
# negative below the surface, as EMODnet stores it.
#
# Every cell takes the mean of the soundings in it. Soundings outside the area, and lines whose
# values are not numbers, are skipped.
set -euo pipefail

if [[ $# -lt 6 ]]; then
    echo "usage: grid-soundings.sh BASE OUT \"west south east north\" CELL FILL FILE..." >&2
    exit 2
fi
BASE=$1; OUT=$2; read -r XMIN YMIN XMAX YMAX <<< "$3"; CELL=$4; FILL=$5
shift 5
SIGN=${SOUNDINGS_SIGN:-depth}
case "$SIGN" in
    depth|elevation) ;;
    *) echo "SOUNDINGS_SIGN must be depth or elevation" >&2; exit 2 ;;
esac

WORK=$(dirname "$OUT")/soundings
mkdir -p "$WORK"
NODATA=-99999

echo "== soundings: binning $# file(s) into $CELL degree cells"
# One mean per cell, written as the cell's centre. The cells line up with the raster below, whose
# origin is the area's top left corner.
for file in "$@"; do tail -n +2 "$file"; done | tr -d '\r' | awk -F, -v OFS=, \
    -v w="$XMIN" -v s="$YMIN" -v e="$XMAX" -v n="$YMAX" -v cell="$CELL" -v sign="$SIGN" '
    BEGIN { print "lon,lat,elev" }
    $1 ~ /^-?[0-9.]+$/ && $2 ~ /^-?[0-9.]+$/ && $3 ~ /^-?[0-9.]+$/ {
        lon = $1 + 0; lat = $2 + 0
        if (lon < w || lon >= e || lat <= s || lat > n) next
        key = int((lon - w) / cell) "," int((n - lat) / cell)
        sum[key] += (sign == "depth" ? -$3 : $3); count[key]++
        used++
    }
    END {
        for (key in sum) {
            split(key, k, ",")
            printf "%.8f,%.8f,%.2f\n", w + (k[1] + 0.5) * cell, n - (k[2] + 0.5) * cell, sum[key] / count[key]
        }
        printf "%d soundings in %d cells\n", used, length(sum) > "/dev/stderr"
    }' > "$WORK/cells.csv"

if [[ $(wc -l < "$WORK/cells.csv") -le 1 ]]; then
    echo "no soundings inside $XMIN $YMIN $XMAX $YMAX" >&2
    exit 1
fi

printf '"Real","Real","Real"\n' > "$WORK/cells.csvt"
cat > "$WORK/cells.vrt" <<VRT
<OGRVRTDataSource>
  <OGRVRTLayer name="cells">
    <SrcDataSource>$WORK/cells.csv</SrcDataSource>
    <SrcLayer>cells</SrcLayer>
    <GeometryType>wkbPoint</GeometryType>
    <LayerSRS>EPSG:4326</LayerSRS>
    <GeometryField encoding="PointFromColumns" x="lon" y="lat"/>
  </OGRVRTLayer>
</OGRVRTDataSource>
VRT

echo "== soundings: grid"
rm -f "$WORK/cells.tif" "$WORK/filled.tif" "$WORK/base.tif" "$OUT"
gdal_rasterize -q -a elev -te "$XMIN" "$YMIN" "$XMAX" "$YMAX" -tr "$CELL" "$CELL" \
    -init $NODATA -a_nodata $NODATA -ot Float32 -co TILED=YES -co COMPRESS=DEFLATE \
    "$WORK/cells.vrt" "$WORK/cells.tif"
# Closes the gaps between neighbouring tracks; anything wider stays empty and takes the base grid.
if [[ "$FILL" -gt 0 ]]; then
    gdal_fillnodata.py -q -md "$FILL" -si 0 -co TILED=YES -co COMPRESS=DEFLATE "$WORK/cells.tif" "$WORK/filled.tif"
else
    cp "$WORK/cells.tif" "$WORK/filled.tif"
fi

if [[ "$BASE" == none ]]; then
    mv "$WORK/filled.tif" "$OUT"
    exit 0
fi

echo "== soundings: merge over the base grid"
gdalwarp -q -overwrite -te "$XMIN" "$YMIN" "$XMAX" "$YMAX" -tr "$CELL" "$CELL" -r bilinear \
    -ot Float32 -co TILED=YES -co COMPRESS=DEFLATE "$BASE" "$WORK/base.tif"
gdal_calc.py --quiet --hideNoData -A "$WORK/filled.tif" -B "$WORK/base.tif" --outfile="$OUT" \
    --calc="numpy.where(A == $NODATA, B, A)" --type=Float32 --NoDataValue=none \
    --co=TILED=YES --co=COMPRESS=DEFLATE --co=BIGTIFF=IF_SAFER
