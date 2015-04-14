#Versionhistory
Date | Version | Author | Description
------------- | ------------- | ------------- | -------------
2014-01-21  |  1  | ingimar.erlingsson@nrm.se  | original
2015-04-08  |  2  | ingimar.erlingsson@nrm.se  | updated
2015-04-14  |  3  | ingimar.erlingsson@nrm.se  | looking more closely at tags&languages (see 4.1)
<date>  | <version>  | <email>  | <desc>

# Introduction
The content of this document will be moved to an issue-tracking system in the future<p>
This document is an extract from the comments made in the mediaserver-environment.odt document<p>

## feedback
1. Suggestion : Using SHA1 of image for unique ID instead of UUID. <p>
2. Suggestion : of 6 levels of directories instead of 3. <p>
3. Suggestion : of using ImageJ-Library for all image manipulation ( http://rsbweb.nih.gov/ij/ )  <p>
3.1 for 'rotate' , for 'quality' , for 'image re-format' <p>
3.2 comment : not a part of the orignal mediaserver-requirments, is there an immeadiate need for this function ?<p>
4. use of correct RESTful - URI:s according to http://dina-project.net/wiki/DINA_Web_API_Standard_-_version_1.0_DRAFT#DINA_REST_API_standard_-_Specification <p>
4.1 page 11 : below the statement 'Also for the above, tags should be encoded something like:' <p>
tags and locale. <p>
```
<tags>
  <tag lang=”sv_SE””>
    <name>view</name>
    <value>sitting</value>
  </tag>
<tag lang=”sv_SE””>
    <name>country</name>
    <value>Sweden</value>
  </tag>
</tags>
```
4.1.1 comment: language on tags : not a part of the orignal mediaserver-requirments - affecting :  'post' , 'search', database  <p>

4.3 page 13 : on transformed media files, error in the original document, only height should be supported  <p>
4.4 page 14 : when @POST, use base64-encoding, return 'success': 204 created and 'failure' :various <p>
4.5 page 16 : when @PUT, use base64-encoding return 'success': 200 ok and 'failure' :various<p>
4.6 page 17 : when @DELETE,return : 204 or 404 <p>
4.6.1 page 17: should be @Path("/media/{UUID}")  <p>
4.7 obs. use of correct 'return values' <p>
5. page 22: obs. handling of 'batch'-uploads <p>
6. page 18-25 : comment on 'coupling to external system' <p>
6.1 conclusion : could be removed to another document <p>
6.2 check the RESTful-URI:s <p>
7 


