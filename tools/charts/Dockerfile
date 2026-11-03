# GDAL for the contours and the coastline clip, tippecanoe for the PMTiles file.
FROM ghcr.io/osgeo/gdal:ubuntu-full-3.11.4

# The image ships an Apache Arrow apt source whose key has expired; GDAL does not need it.
RUN rm -f /etc/apt/sources.list.d/*arrow* \
    && apt-get update \
    && apt-get install -y --no-install-recommends build-essential libsqlite3-dev zlib1g-dev git curl ca-certificates unzip \
    && git clone --depth 1 --branch 2.79.0 https://github.com/felt/tippecanoe.git /tmp/tippecanoe \
    && make -C /tmp/tippecanoe -j4 \
    && make -C /tmp/tippecanoe install \
    && rm -rf /tmp/tippecanoe /var/lib/apt/lists/*

COPY build-chart.sh /usr/local/bin/build-chart.sh
RUN chmod +x /usr/local/bin/build-chart.sh

ENTRYPOINT ["/usr/local/bin/build-chart.sh"]
