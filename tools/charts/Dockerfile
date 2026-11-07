# GDAL for the contours, the coastline clip and the rainbow raster; tippecanoe for the vector
# tiles; go-pmtiles to turn the raster MBTiles into PMTiles; osmium for the seamarks.
FROM ghcr.io/osgeo/gdal:ubuntu-full-3.11.4

# The image ships an Apache Arrow apt source whose key has expired; GDAL does not need it.
RUN rm -f /etc/apt/sources.list.d/*arrow* \
    && apt-get update \
    && apt-get install -y --no-install-recommends build-essential libsqlite3-dev zlib1g-dev git curl ca-certificates unzip \
    && git clone --depth 1 --branch 2.79.0 https://github.com/felt/tippecanoe.git /tmp/tippecanoe \
    && make -C /tmp/tippecanoe -j4 \
    && make -C /tmp/tippecanoe install \
    && rm -rf /tmp/tippecanoe /var/lib/apt/lists/* \
    && curl -sSfL https://github.com/protomaps/go-pmtiles/releases/download/v1.31.2/go-pmtiles_1.31.2_Linux_x86_64.tar.gz \
        | tar -xz -C /usr/local/bin pmtiles

# osmium cuts the seamarks out of the OpenStreetMap extracts.
RUN rm -f /etc/apt/sources.list.d/*arrow* \
    && apt-get update \
    && apt-get install -y --no-install-recommends osmium-tool \
    && rm -rf /var/lib/apt/lists/*

COPY build-chart.sh grid-soundings.sh seamarks.py /usr/local/bin/
RUN chmod +x /usr/local/bin/build-chart.sh /usr/local/bin/grid-soundings.sh /usr/local/bin/seamarks.py

ENTRYPOINT ["/usr/local/bin/build-chart.sh"]
