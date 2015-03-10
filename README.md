# mediaserver-module
Mediaserver core module

![alt mediaserver in fokus](docs/Mediaserver-module(1-2).png)

#Background & Purpose
The Mediaserver (aka. 'attachment server')  handles mediafiles and their metadata.<p>
Guiding principle is 'ease of installation' and 'ease of management'.

1. The Mediaserver can be used as a standalone server (no coupling to an external system).
2. The Mediaserver can be used as an integrated part of your system, coupled.

The Mediaservers has the basic CRUD responsibilities:

1. Storing :
Storing binaries to the filesystem and Storing the metadata to a database.
2. Retrieving:
Retrieving the binaries and retrieving the metadata for the binaries.
3. update.
4. delete.

##Constraints
1. Stores the binary-files to the local filessystem, the filesystem where the Mediaserver is installed.

#Functional Requirements

The Mediaserver should provide services for other systems.

1. To store mediafiles (Create)
2. To edit mediafiles (Update)
3. To search on metadata, fetch mediafile(s) (Retrieve) 
4. To delete mediafiles (Delete)

##Storing the mediafiles 
The mediafiles are stored in the filesystem with the depth of 3 layers.
Only one mediafile is stored, no derivates for images are created at the same time. <p>
All medifiles are stored using UUID as names. <p>
UUID, " Universally unique identifier, 128-bit number." ( http://tools.ietf.org/html/rfc4122) <p>

### directory-structure
The structure is that there are directories ranging from '0' to 'F' (hexadecimal) with the depth of 3 layers, this gives 4096 directories ( 16^3 ).
Ex. Say that we have 10 000 000 ( ten millions ) mediafiles, with an even spread this would give 2441 mediafiles per directory. 

All mediafiles are processed in the same way - they are streamed to the filesystem and streamed back to their client.<p>
The mediafile will hold their own 'media-type'/'mime-type' ( stored in the database )

- The principle is the following : 
- - if the generated UUID is **'ab30899c-58a0-4305-85a6-bbfa14f89b92'**
- Then the file is stored in the following directory ( subdirectory is made up by the first 3 chars ):
- - directory = **/opt/data/mediaserver/newmedia/a/b/3**
- - directory and file = /opt/data/mediaserver/newmedia/a/b/3/**ab30899c-58a0-4305-85a6-bbfa14f89b92**

### Metadata
Terminology, there is distinguish between metadata and tags.<p>
1. 'metadata' is regarded as immuatable, metadata about the file.
2. 'tag(s)' is regarded as mutable, metadata about the content.

#### 'metadata':

1. original filename
2. mime type
3. owner
4. visibility
- i.e  'private' or 'public'
5. md5hash
- Saving the md5hash for every mediafile (facilitates finding duplicates)
6. Exif
To store Exif-metadata for Mediafiles of type images.<p>
- Exif-metadata is stored in a table of its own.<p>
- configurable parameter, set flag to 'true' or 'false' in database.<p>

#### 'tag(s)':
The Mediaserver sets no constraint on the keys that are used.  <p>
This gives an external module using the Mediaserver freedom to define its own keys and constrain others keys.

Generics tags are supported and saved as a text-string in the database. <p>
- ':' is used as the delimiter between key and its value.
- '&' is used as the delimiter between key/value-pairs  <p>

<b>i.e setting key='country' with value='sweden' and key='view' with value='dorsal'. </b><p>
<b>is saved as -> 'country:sweden&value=dorsal'</b> <p>

#User Guide

##How to install
see the 'turn-key vagrant'-project at [dw-media](https://github.com/DINA-Web/dw-media) 

If not using the vagrant-project, the basics steps are the following.

1. download and build the source code, maven
2. install and populate the chosen database-engine
3. set up the Application server ( ex. Wildfly 8.x )
set up a datasource/datapool/JNDI-handle
4. deploy the service
5. set up the filesystem-path for the mediafiles

##How to connect to an external system
A link-table in the database that maps the ID from the external-system to one or many media-files.

##How to add supported licenses
Licences will be stored in a licence-table. <p>
This gives the administrator full control of what licenses are permitted in the system.<p>

##Maintenability , How to configure

The database table ADMIN_CONFIG
A table ( key/value) in the schema is used for managing settables :

For instance the below ( key/value ) is set in the database.
1. is_exif = [true||false]
2. path_to_files = '/opt/data/mediaserver/newmedia'

![alt Admin-table](docs/admin-table.png)