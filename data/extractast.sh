#!/usr/bin/env bash


# create array with files
filesName=$(find $1 -type f -name "*.js")

# get the 1st element of the array
first=${filesName[0]}

# remove the 1st element of from array
files=("${filesName[@]:1}")


acorn first --allow-hash-bang --locations > "first.json"

while read -r line; do
    acorn line --allow-hash-bang --locations  > "result.json"
done <<< "$files"