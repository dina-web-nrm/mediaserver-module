<%@ page contentType="text/html; charset=UTF-8" %> 
<html>
    <head>
        <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
        <title>Demo, upload file [returns a UUID]</title>
    </head>
    <body>
        <h1>Image - JAX-RS File Upload Example [returns a UUID]</h1>
        <input type="hidden" name="branch" id="branch" value="development-liquibase-mysql">
        <input type="hidden" name="version.date" id="version.date" value="latest: 2015-03-07-16:44, now: 2015-06-08-14:06">
        <form action="media/load" method="post" enctype="multipart/form-data"  accept-charset="utf-8" >
            <p>
                Owner : <input type="text" name="owner" /></br>
            </p>
            <p>
                private/public ( as in visibility ) : <input type="text" name="access" /></br>
            </p>
            <p>
                tags ( ?? such as 'view:dorsal'&'country:sweden' : <input type="text" name="tags" /></br>
            </p>
            <p>
                legend : <input type="text" name="legend" /></br>
            </p>
            <p>
                comment ( not public ) : <input type="text" name="comment" /></br>
            </p>
            <p>
                language ( such as 'sv_SE' ) : <input type="text" name="legendLanguage" /></br>
            </p>
            <p>
                License type ( [no drop-down-list], CC BY > CC BY-NC > CC BY-NC-ND > CC BY-NC-SA > CC BY-ND > CC BY-SA ) : <input type="text" name="licenseType" /></br>
            </p>
            <p>
                Stream: startTime ( in seconds ) : <input type="text" name="startTime" /></br>
            </p>
            <p>
                Stream: endTime  ( in seconds ) : <input type="text" name="endTime" /></br>
            </p>
            <p>
                Choose the file : <input type="file" name="selectedFile" />
            </p>
            <p>
                File name (: <input type="text" name="fileName" />
            </p>
            <input type="submit" value="Upload" />
        </form>

    </body>
</html>