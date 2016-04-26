---
output: html_document
---
# Demo-Standalone, the RESTful-API :
The 3 images are packaged with the mediaserver :<p>
* 863ec044-17cf-4c87-81cc-783ab13230ae (sitting 'pica pica')
* c41bd445-8796-4421-9b77-fd1e65b14974 (flying 'pica pica')
* e4a3cf7d-add4-4949-a6ce-0f5594e61970 (sitting 'Corvus corax')

## @GET metadata : returns metadata (?content=metadata)
http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?content=metadata**<p>
curl -v -H "Accept: application/json" http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?content=metadata** <p>

## @Get media file : returns an image (?format=image/jpeg)
http://localhost:8080/MediaServerResteasy/media/v1/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg**<p>

## @Get image : returns an image with height 150 (?format=image/jpeg&height=150)
http://localhost:8080/MediaServerResteasy/media/v1/image/863ec044-17cf-4c87-81cc-783ab13230ae**?format=image/jpeg&height=150**

## @Get a media file(s) -filtering  on the tags  'view=sitting'
http://localhost:8080/MediaServerResteasy/media/v1/search**?view=sitting**

## @Post base64-encoding file
http://127.0.0.1:8080/MediaServerResteasy/media<p>
* **observe on how to add the tags** : "taggar": ["view:facial", "music:reggea"] <p>
(1) one-liner <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"owner":"dina","access":"public","licenseType":"CC BY","legend":"this is chess","fileName":"chess.png","fileDataBase64":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAACAQMAAABIeJ9nAAAABlBMVEUAAAD///+l2Z/dAAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH4AQZCR0TdgIZugAAABl0RVh0Q29tbWVudABDcmVhdGVkIHdpdGggR0lNUFeBDhcAAAAMSURBVAjXY3BgaAAAAUQAwetZAwkAAAAASUVORK5CYII="}' http://localhost:8080/MediaServerResteasy/media <p>

(2) when the content (metadata + base64-encoded file) is packaged in the file named i.e '@meta_and_image_corvux-corax.json' <p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @meta_and_image_corvux-corax.json http://127.0.0.1:8080/MediaServerResteasy/media <p>
location of the file '/docs/example-files/meta_and_image_corvux-corax.json'<p>


## @Put base64-encoding 
http://127.0.0.1:8080/MediaServerResteasy/media<p>
* **must have**: key:value => mediaUUID:<UUID>  . ex.  "mediaUUID":"cf170678-7fc1-42e5-b7c2-cadac44250e2"
* key:value => "fileDataBase64":"/9j/4AAQSkZJ /9k="  (where '/9j/4AAQSkZJ /9k=' is not a valid example of a file)<p>
*This example: changing 'access' from 'public' to 'private'*<p>
curl -v -H "Accept: application/json" -H "Content-type: application/json" -X PUT -d  '{"mediaUUID":"863ec044-17cf-4c87-81cc-783ab13230ae","access":"public"}' http://127.0.0.1:8080/MediaServerResteasy/media

## @Delete
**obs:** check the URI, not up to standard right now <p>
curl -i -H "Accept: application/json" -X DELETE   http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae

## Admin-stuff :
@GET licenses<p>
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
