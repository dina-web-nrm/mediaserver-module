1) Create a database and a user on your machine.
1.1) for instance : db = 'nrm_demo_media' and user='ingimar'
2) liquibase.properties-file, use the same user&database

3) log in as root to mysql
3.1) mysql> CREATE database nrm_demo_media;
3.2) mysql> GRANT ALL PRIVILEGES ON nrm_demo_media.* To 'media'@'localhost' IDENTIFIED BY 'ingimar';

4)   Execute the 'database project by running :
4.1) $mvn clean install

If deploying in Glassfish 
1) check the file glassfish-resources.xml (in module xyz )
1.1) set URL to database
1.2) <property name="URL" value="jdbc:mysql://localhost:3306/nrm_demo_media"/>

Check the 'GET', ( MediaServer-Standalone )  : Skata :
http://localhost:8080/MediaServerResteasy/media/metadata/863ec044-17cf-4c87-81cc-783ab13230ae
http://localhost:8080/MediaServerResteasy/media/stream/863ec044-17cf-4c87-81cc-783ab13230ae

