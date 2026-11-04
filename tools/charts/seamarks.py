#!/usr/bin/env python3
"""Turns OpenStreetMap seamarks into point features for the chart's `seamark` layer.

Reads the GeoJSON sequence written by `osmium export` (any number of files) and writes one
GeoJSON feature per line for tippecanoe. Each feature is a point with these properties:

    kind    harbour, marina, slipway, fuel, light, buoy, beacon, rock, wreck, obstruction,
            anchorage or mooring (ChartStyle.SEAMARK_KINDS in the app)
    name    the name, when the object has one
    colour  the first colour of a buoy or beacon, for example "red"
    light   the light characteristic, for example "Fl(2) WR 5s"

Areas and lines become a single point on them. An object that appears in two country extracts
is written once.

Usage: seamarks.py OUTPUT INPUT...
"""
import json
import sys

from osgeo import ogr

# Zoom from which each kind appears; hazards come in before the buoys that mark them.
MIN_ZOOM = {
    "harbour": 9, "marina": 9, "light": 10, "rock": 11, "wreck": 11, "obstruction": 11,
    "buoy": 12, "beacon": 12, "slipway": 12, "fuel": 12, "anchorage": 12, "mooring": 14,
}

COLOUR_LETTERS = {"white": "W", "red": "R", "green": "G", "yellow": "Y", "blue": "Bu", "orange": "Or", "violet": "Vi"}


def kind_of(tags):
    seamark = tags.get("seamark:type", "")
    facility = tags.get("seamark:small_craft_facility:category", "")
    if seamark.startswith("buoy_"):
        return "buoy"
    if seamark.startswith("beacon_"):
        return "beacon"
    if seamark.startswith("light") or (seamark == "landmark" and light_of(tags)):
        return "light"
    if seamark in ("rock", "wreck", "obstruction", "mooring"):
        return seamark
    if seamark in ("anchorage", "anchor_berth"):
        return "anchorage"
    if seamark == "harbour":
        return "marina" if "marina" in tags.get("seamark:harbour:category", "") else "harbour"
    if tags.get("leisure") == "marina":
        return "marina"
    if tags.get("leisure") == "slipway" or "slipway" in facility:
        return "slipway"
    if tags.get("waterway") == "fuel" or "fuel_station" in facility:
        return "fuel"
    if "harbour" in tags and tags["harbour"] != "no":
        return "harbour"
    return None


def light_of(tags):
    """The characteristic of the light, for example "Fl(2) WR 5s", or None."""
    def tag(name):
        return tags.get(f"seamark:light:{name}") or tags.get(f"seamark:light:1:{name}")

    character = tag("character")
    if not character:
        return None
    group = tag("group")
    text = f"{character}({group})" if group else character

    # Sector lights carry one colour per sector: seamark:light:1:colour, seamark:light:2:colour...
    colours = []
    for key, value in tags.items():
        if key.startswith("seamark:light:") and key.endswith(":colour"):
            for colour in value.split(";"):
                letter = COLOUR_LETTERS.get(colour.strip())
                if letter and letter not in colours:
                    colours.append(letter)
    order = list(COLOUR_LETTERS.values())
    colours.sort(key=order.index)
    if colours:
        text += " " + "".join(colours)

    period = tag("period")
    if period:
        text += f" {period}s"
    return text


def point_of(geometry):
    """A single point on the geometry: the point itself, the middle of a line, or a point inside an area."""
    shape = ogr.CreateGeometryFromJson(json.dumps(geometry))
    if shape is None:
        return None
    name = shape.GetGeometryName()
    if name == "POINT":
        point = shape
    elif name == "LINESTRING":
        point = shape.Value(shape.Length() / 2)
    else:
        point = shape.PointOnSurface()
    if point is None or point.IsEmpty():
        return None
    return {"type": "Point", "coordinates": [round(point.GetX(), 7), round(point.GetY(), 7)]}


def main():
    if len(sys.argv) < 3:
        sys.exit(__doc__)
    seen = set()
    counts = {}
    with open(sys.argv[1], "w", encoding="utf-8") as output:
        for path in sys.argv[2:]:
            with open(path, encoding="utf-8") as source:
                for line in source:
                    line = line.strip().lstrip("\x1e")
                    if not line:
                        continue
                    feature = json.loads(line)
                    identifier = feature.get("id")
                    if identifier in seen:
                        continue
                    seen.add(identifier)

                    tags = feature.get("properties") or {}
                    kind = kind_of(tags)
                    if kind is None:
                        continue
                    point = point_of(feature["geometry"])
                    if point is None:
                        continue

                    properties = {"kind": kind}
                    name = tags.get("name") or tags.get("seamark:name")
                    if name:
                        properties["name"] = name
                    seamark = tags.get("seamark:type", "")
                    colour = tags.get(f"seamark:{seamark}:colour", "")
                    if colour:
                        properties["colour"] = colour.split(";")[0].strip()
                    light = light_of(tags)
                    if light:
                        properties["light"] = light

                    output.write(json.dumps({
                        "type": "Feature",
                        "tippecanoe": {"minzoom": MIN_ZOOM[kind]},
                        "properties": properties,
                        "geometry": point,
                    }, ensure_ascii=False) + "\n")
                    counts[kind] = counts.get(kind, 0) + 1
    print("seamarks: " + ", ".join(f"{k} {v}" for k, v in sorted(counts.items())))


if __name__ == "__main__":
    main()
