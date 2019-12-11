# test to submit three images with metadata to the server
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @corvuxcorax.b64 http://api.nrm.se/MediaServerResteasy/media | json_pp
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica.b64 http://api.nrm.se/MediaServerResteasy/media | json_pp
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica-fly.b64 http://api.nrm.se/MediaServerResteasy/media | json_pp
