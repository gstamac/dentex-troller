<#
.SYNOPSIS
Builds the offline chart with depths from CSV soundings only, and land and shoreline from OSM.

.DESCRIPTION
Runs build-chart.sh in the dentex/chart-builder image with DEPTH_SOURCE=soundings. Every *.csv
in the soundings folder is read: a header line, then lon,lat,depth (WGS84 degrees, metres below
the surface). Water further than Fill cells from a sounding gets no depth. The .pmtiles files
are written to tools/charts/build (or -Out).

.EXAMPLE
.\tools\charts\build-from-csv.ps1
.\tools\charts\build-from-csv.ps1 -Cell 0.00005 -Fill 4 -Zoom 15 -Bbox "14.68 44.14 14.91 44.30"
#>
param(
    # Folder with the CSV files.
    [string]$Soundings = "docs/csv",
    # Grid cell in degrees: 0.00002 is about 2 m, 0.0002 about 20 m.
    [string]$Cell = "0.00002",
    # Gaps up to this many cells between tracks are filled.
    [int]$Fill = 8,
    # Deepest zoom of the rainbow tiles; 16 suits 2 m cells, 13 suits 20 m.
    [int]$Zoom = 16,
    # "west south east north"; empty takes the soundings' extent plus about 500 m.
    [string]$Bbox = "",
    # Credit shown on the chart for the soundings.
    [string]$Attribution = "Own sonar logs",
    # Land polygon file inside the data folder, for when osmdata.openstreetmap.de is down.
    [string]$Land = "",
    # Output folder for the .pmtiles files.
    [string]$Out = "tools/charts/build"
)
$ErrorActionPreference = "Stop"
$repo = Resolve-Path (Join-Path $PSScriptRoot "../..")
Push-Location $repo
try {
    $csv = Resolve-Path $Soundings
    if (-not (Get-ChildItem $csv -Filter *.csv)) { throw "no .csv files in $csv" }
    New-Item -ItemType Directory -Force $Out, "tools/charts/build/data" | Out-Null
    $outDir = Resolve-Path $Out
    $data = Resolve-Path "tools/charts/build/data"

    # The build logs to stderr, which Windows PowerShell would turn into errors; the exit codes
    # tell whether docker failed.
    $ErrorActionPreference = "Continue"
    docker build -q -t dentex/chart-builder tools/charts | Out-Null
    if ($LASTEXITCODE -ne 0) { throw "docker build failed" }

    $run = @(
        "run", "--rm",
        "-v", "${data}:/data", "-v", "${outDir}:/out", "-v", "${csv}:/soundings:ro",
        "-e", "DEPTH_SOURCE=soundings", "-e", "SOUNDINGS=/soundings/*.csv",
        "-e", "SOUNDINGS_CELL=$Cell", "-e", "SOUNDINGS_FILL=$Fill", "-e", "RAINBOW_ZOOM=$Zoom",
        "-e", "SOUNDINGS_ATTRIBUTION=$Attribution"
    )
    if ($Bbox) { $run += "-e", "BBOX=$Bbox" }
    if ($Land) { $run += "-e", "LAND=/data/$Land" }
    docker @run dentex/chart-builder
    if ($LASTEXITCODE -ne 0) { throw "chart build failed" }
    Write-Host "Chart files in $outDir"
}
finally {
    Pop-Location
}
