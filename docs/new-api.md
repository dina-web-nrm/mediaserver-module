---
output: html_document
---
# Demo-Standalone, the RESTful-API :
The 3 images are packaged with the mediaserver :<p>
* 863ec044-17cf-4c87-81cc-783ab13230ae (sitting 'pica pica')
* c41bd445-8796-4421-9b77-fd1e65b14974 (flying 'pica pica')
* e4a3cf7d-add4-4949-a6ce-0f5594e61970 (sitting 'Corvus corax')

NB: inconsistent, version on @GET but not on @POST/@PUT/@DELETE <br>

## @GET metadata : returns metadata (?content=metadata)
**URI:** http://localhost:8080/MediaServerResteasy/media/v1/<uuid>?content=metadata <p>
piping to JSON pretty-print (json_pp) <p>
curl -v -H "Accept: application/json" http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae?content=metadata || json_pp <p>

http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?content=metadata**<p>

## @Get media file : returns an image (?format=image/jpeg)
**URI:** http://localhost:8080/MediaServerResteasy/media/v1/<uuid>?format=image/jpeg <p>
http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg**<p>

## @Get image : returns an image with height xxx (?format=image/jpeg&height=xxx)
**URI:** http://localhost:8080/MediaServerResteasy/media/image/v1/<uuid>?format=image/jpeg&height=xxx <p>
for instance height=150 <br>
http://localhost:8080/MediaServerResteasy/media/image/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg&height=150**

## @Get the base64-encoded value
**URI:** http://localhost:8080/MediaServerResteasy/media/base64/<uuid> <p>
curl -v  http://localhost:8080/MediaServerResteasy/media/base64/55df001f-646b-47cf-a0c4-a3340a815615

## @Post a base64-encoded file
**URI:** http://127.0.0.1:8080/MediaServerResteasy/media<p>

### posting an image with 5 metadata-attributes ( owner+access+licenseType+legend+fileName )
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"owner":"dina","access":"public","licenseType":"CC BY","legend":"this is chess","fileName":"chess.png","fileDataBase64":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQMAAABIeJ9nAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AQZCR0TdgIZugAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY3BgaAAAAUQAwetZAwkAAAAASUVORK5CYII="}' http://localhost:8080/MediaServerResteasy/media <p>

### posting an image with 6 metadata-attributes ( owner+access+licenseType+legend+fileName+taggar )
With tags: **"taggar": ["where:Reykjavik", "sport:chess"]** <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"owner":"dina","access":"public","licenseType":"CC BY","legend":"this is chess","taggar": ["where:Reykjavik", "sport:chess"], "fileName":"chess.png","fileDataBase64":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQMAAABIeJ9nAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AQZCR0TdgIZugAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY3BgaAAAAUQAwetZAwkAAAAASUVORK5CYII="}' http://localhost:8080/MediaServerResteasy/media

## @Post a base64-encoded file ( metadata + base64 in same file)
post 3 images with <p>
location of  [testfile](https://github.com/DINA-Web/mediaserver-module/tree/master/docs/example-files)<p>

### posting @corvuxcorax.b64
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @corvuxcorax.b64 http://127.0.0.1:8080/MediaServerResteasy/media


### posting @picapica.b64
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica.b64 http://127.0.0.1:18080/MediaServerResteasy/media

### posting @picapica-fly.b64
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @picapica.b64 http://127.0.0.1:18080/MediaServerResteasy/media


## @Get a media file(s) -filtering  on the tags 
### ex :  filter on the  tag 'view:sitting'
http://localhost:8080/MediaServerResteasy/media/v1/search?view=sitting

### ex :  filter on the  tag 'where:Reykjavik'
http://localhost:8080/MediaServerResteasy/media/v1/search?where=Reykjavik

## @Put 
**URI:** http://127.0.0.1:8080/MediaServerResteasy/media<p>
updating a
* **must have**: key:value => mediaUUID:<UUID>  . i.e.  "mediaUUID":"cf170678-7fc1-42e5-b7c2-cadac44250e2" <p>
*In this example: changing 'access' ='public' to 'access' ='private'* <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X PUT -d  '{"mediaUUID":"863ec044-17cf-4c87-81cc-783ab13230ae","access":"public"}' http://127.0.0.1:8080/MediaServerResteasy/media

## @Delete
**URI:**  http://localhost:8080/MediaServerResteasy/media/<uuid>
curl -i -H "Accept: application/json" -X DELETE   http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae

## @Get range
where <type> can be 'media','images','sounds','videos','attachments' <p>
if parameters are not given then minid is set to 0 and  maxid is limited to 15 <p>
**URI:** http://localhost:8080/MediaServerResteasy/media/v1/range/'type' <p>
**URI:** http://localhost:8080/MediaServerResteasy/media/v1/range/'type'?minid=0&maxid=14 <p>

i.e  http://localhost:8080/MediaServerResteasy/media/v1/range/media?minid=0&maxid=2 <p>
i.e  http://localhost:8080/MediaServerResteasy/media/v1/range/images?minid=0&maxid=6 <p>

## @Get count
where type can be 'media','images','sounds','videos','attachments' <p>
**URI:** http://localhost:8080/MediaServerResteasy/media/'type'/v1/count <p>

i.e  http://localhost:8080/MediaServerResteasy/media/images/v1/count <p>

## Admin-stuff :
@GET licenses<p>
To see all the licenses that are available, only @GET <br>
* localhost:8080/MediaServerResteasy/media/admin/licenses



@Todo
-with lang:
<tags>
  <tag lang=”sv_SE””>
    <name>view</name>
    <value>sitting</value>
  </tag>
<tag lang=”sv_SE””>
    <name>country</name>
    <value>Sweden</value>
