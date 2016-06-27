#!/bin/bash 
echo "Submits 3 images with metadata to the server"
BASE_URL="http://localhost:8080/MediaServerResteasy/media"
echo "Base URL is " $BASE_URL

curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @corvuxcorax.b64 $BASE_URL | json_pp
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica.b64 $BASE_URL | json_pp
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica-fly.b64 $BASE_URL | json_pp
