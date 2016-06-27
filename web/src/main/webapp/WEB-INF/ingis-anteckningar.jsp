<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        https://github.com/DINA-Web/mediaserver-module
    </head>
    <body>
        2016-05-17 : 
        får rawTypes i json.
        http://stackoverflow.com/questions/14985435/not-getting-right-json-of-the-when-returning-list
        2015-06-15 : Integration med Naturforskaren
        - use nrm_media_test_1; 
        - bilder ligger i tar-fil
        
        
        Turn on debug on wildfly : /subsystem=logging/root-logger=ROOT:write-attribute(name=level,value=DEBUG)
        
        http://localhost:9990/ -> admin/admin
        http://localhost:8080/MediaServerResteasy/
        
        https://titanpad.com/ylPQeByKim
        mvn clean package wildfly:deploy 
        
        Apiary : https://app.apiary.io/media8/editor ( log in as 'inkimar@gmail.com' )
        
        http://www.liquibase.org/sqlite.html
        
        Gjort:
        1. använder web.xml
        2. använder wildfly 8.2 ( ej testat 8.1)

        2. tomma beans.xml -> CDI -> se jpassion.com

        obs 2: tagit bort från  from Media-klassen tills vidare : Såg felet när jag körde NewClass -
        obs 1:  Jag hade heller inte med 'exif'-kolumnen i Image-tabellen 

        @OneToMany(cascade = CascadeType.ALL, mappedBy = "media",targetEntity = MediaText.class, fetch = FetchType.EAGER)
        @XmlElementWrapper(name = "descriptions")
        @XmlElement(name = "description", required = true)
        private Set<MediaText> texts = new HashSet<>(0);

        - nu funkar multipart ( web.xml införd igen )

        Ska göras:
        - lyft in jsp-sida för post
        - posta, funkar eller inte ?
        - Kolla mediatext
        -

        Intro 
        Få in bild till servlet, göra om till 'base64'


        Ny klasser :
        (0) NewMediaResource - for GET
        (1) NewMediaResourceForm - for POST
        (2)

        @GET-UUID - Sök på 'Skata'
        - - måste ha ett 'externt' system såsom Naturforskaren

        @GET-file
        /MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?format=image/jpeg
        ex: http://localhost:8080/MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?format=image/jpeg

        @GET-file-derivative (only height, keeping the ratio)
        /MediaServerResteasy/media/image/863ec044-17cf-4c87-81cc-783ab13230ae?format=raw&height=200
        ex: http://localhost:8080/MediaServerResteasy/media/image/863ec044-17cf-4c87-81cc-783ab13230ae?format=raw&height=200

        @GET-metadata
        /MediaServerResteasy/media/863ec044-17cf-4c87-81cc-783ab13230ae?content=metadata

        @FILTER on TAGS
        Ett par
        http://localhost:8080/MediaServerResteasy/media/search/view:flying
        (should be : http://localhost:8080/MediaServerResteasy/media/search?view:flying) ?
        Två par:
        http://localhost:8080/MediaServerResteasy/media/search/view=flying&country=sweden

        @Delete :
        Enligt ottawa : - inte säker på deras 'return'
        @Path(“/images/{UUID}”)
        204 No Content
        This deletes the image with UUID={UUID}
        See https://stackoverflow.com/questions/6439416/deleting-a-resource-using-http-delete

        curl -X DELETE "http://localhost:8080/MediaServerResteasy/media/v1/c41bd445-8796-4421-9b77-fd1e65b14974"

        @POST ,med base64-encoded   (referens till fil (base64)) - http://127.0.0.1:8080/MediaServerResteasy/media eller http://127.0.0.1:8080/MediaServerResteasy/media/post
        curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d @meta_and_image.json http://127.0.0.1:8080/MediaServerResteasy/media

        @POST, med base64-encoded
        curl -v -H "Accept: application/json" -H "Content-type: application/json" -X POST -d  '{"owner":"ingimar","access":"public","licenseType":"CC BY","legend":"this is bob marley","fileName":"bob-marley20150407.jpg","fileDataBase64":"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"}' http://127.0.0.1:8080/MediaServerResteasy/media

        @PUT
    </body>

</html>
