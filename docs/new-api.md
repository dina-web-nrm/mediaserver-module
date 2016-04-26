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
http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?content=metadata**<p>

## @Get media file : returns an image (?format=image/jpeg)
**URI:** http://localhost:8080/MediaServerResteasy/media/v1/<uuid>?format=image/jpeg <p>
http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg**<p>

## @Get image : returns an image with height xxx (?format=image/jpeg&height=xxx)
**URI:** http://localhost:8080/MediaServerResteasy/media/image/v1/<uuid>?format=image/jpeg&height=xxx <p>
for instance height=150 <br>
http://localhost:8080/MediaServerResteasy/media/image/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg&height=150**

## @Post a base64-encoded file
**URI:** http://127.0.0.1:8080/MediaServerResteasy/media<p>

### posting an image with 5 metadata-attributes ( owner+access+licenseType+legend+fileName )
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"owner":"dina","access":"public","licenseType":"CC BY","legend":"this is chess","fileName":"chess.png","fileDataBase64":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQMAAABIeJ9nAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AQZCR0TdgIZugAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY3BgaAAAAUQAwetZAwkAAAAASUVORK5CYII="}' http://localhost:8080/MediaServerResteasy/media <p>

### posting an image with 6 metadata-attributes ( owner+access+licenseType+legend+fileName+taggar )
adding tags, *"taggar": ["where:Reykjavik", "sport:chess"]* <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"owner":"dina","access":"public","licenseType":"CC BY","legend":"this is chess","taggar": ["where:Reykjavik", "sport:chess"], "fileName":"chess.png","fileDataBase64":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQMAAABIeJ9nAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AQZCR0TdgIZugAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY3BgaAAAAUQAwetZAwkAAAAASUVORK5CYII="}' http://localhost:8080/MediaServerResteasy/media

### posting all content in a file
when the content (metadata + base64-encoded file) is packaged in the file named i.e '@meta_and_image_corvux-corax.json' <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @meta_and_image_corvux-corax.json http://127.0.0.1:8080/MediaServerResteasy/media <p>
location of this [testfile](https://github.com/DINA-Web/mediaserver-module/tree/master/docs/example-files)<p>


## @Get a media file(s) -filtering  on the tags 
### ex :  filter on the  tag 'view:sitting'
http://localhost:8080/MediaServerResteasy/media/v1/search?view=sitting

### ex :  filter on the  tag 'where:Reykjavik'
http://localhost:8080/MediaServerResteasy/media/v1/search?where=Reykjavik

## @Put 
**URI:** http://127.0.0.1:8080/MediaServerResteasy/media<p>
* **must have**: key:value => mediaUUID:<UUID>  . ex.  "mediaUUID":"cf170678-7fc1-42e5-b7c2-cadac44250e2"
*In this example: changing 'access' ='public' to 'access' ='private'*<p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X PUT -d  '{"mediaUUID":"863ec044-17cf-4c87-81cc-783ab13230ae","access":"public"}' http://127.0.0.1:8080/MediaServerResteasy/media

## @Delete
**URI:**  http://localhost:8080/MediaServerResteasy/media/<uuid>
curl -i -H "Accept: application/json" -X DELETE   http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae

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
