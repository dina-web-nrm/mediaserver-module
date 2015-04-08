#Versionhistory
Date | Version | Author | Description
------------- | ------------- | ------------- | -------------
2014-01-21  |  1  | ingimar.erlingsson@nrm.se  | original
2015-04-08  |  1  | ingimar.erlingsson@nrm.se  | updated
<date>  | <version>  | <email>  | <desc>

# Introduction
The content of this document will be moved to an issue-tracking system in the future<p>
This document is an extract from the comments made in the mediaserver-environment.odt document<p>

## feedback
1. Suggestion : Using SHA1 of image for unique ID instead of UUID. <p>
2. Suggestion : of 6 levels of directories instead of 3. <p>
3. Suggestion : of using ImageJ-Library for all image manipulation ( http://rsbweb.nih.gov/ij/ )  <p>
3.1 for 'rotate' , for 'quality' , for 'image re-format' <p>
3.2 there is no requirement for this feature in the orignal mediaserver requirments<p>
4. use of correct RESTful - URI:s according to http://dina-project.net/wiki/DINA_Web_API_Standard_-_version_1.0_DRAFT#DINA_REST_API_standard_-_Specification <p>
4.1 page 12 : below the statement 'Also for the above, tags should be encoded something like:' <p>
tags and locale. <p>
4.2 page 13 : on transformed media files, error in the original document, only height should be supported  <p>
4.3 page 14 : when @POST, use base64-encoding, return 'success': 204 created and 'failure' :various <p>
4.4 page 16 : when @PUT, use base64-encoding return 'success': 200 ok and 'failure' :various<p>
4.5 page 17 : when @DELETE,return : 204 or 404 <p>
4.5.1 page 17: should be @Path("/media/{UUID}")  <p>
4.6 obs. use of correct 'return values' <p>
5. page 22: obs. handling of 'batch'-uploads <p>
6. page 18-25 : comment on 'coupling to external system' <p>
6.1 conclusion : could be removed to another document <p>
6.2 check the RESTful-URI:s <p>


