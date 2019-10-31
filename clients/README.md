Ingimars Machine, Short on how I test a mediserver-ear-file. <p> 
running db in docker (same version and same version of jdbc)  and a local wildfly instance on port 8080

1. cd /home/ingimar/repos/naturhistoriska-github/vegadare-docker
2. git branch = 'local'
3. Starta endast databas   -> docker-compose -f docker-compose.all.yml up -d db.media
4.1 verifiera ->  mysql -u mediaserver -pmediaserver -h 127.0.0.1 media 
4.2 verifiera -> show tables;
5. Check wildly , localhost:8080 (admin/adminadmin)
6. check datasources, JNDI=java:MediaDS
6.1 db connection -> Test -> OK?
6.2 deploy ear-file, enable.
7. Posta bild via interface: http://localhost:8080/MediaServerResteasy/
7.1 Test -> OK ?
8. fetch image via browser:
8.1 check UUID , here = 2b041f01-250c-4a3a-b264-76c67dac2ffc 
8.2 http://127.0.0.1:8080/MediaServerResteasy/media/v2/<UUID>?format=image/jpeg 
8.3 http://127.0.0.1:8080/MediaServerResteasy/media/v2/2b041f01-250c-4a3a-b264-76c67dac2ffc?format=image/jpeg 

the runnable jar-file:see pom.xml ->  Main-file =  ClientPostEncodedHardcodedString
1. java -jar target/mediaClient.jar http://127.0.0.1:8080 

Testing ClientPostEncodedFiles, hardcoded file right now.:
touch /media/real-vegadare/vegadare-share/zipfiles/Testing.zip
eller -> cp ~/Pictures/TESTBILD-1-988x556.jpg Testing.zip 


# linux, base64 encode and base64 encode
> base64 --decode image.b64 > image.jpg  

## linux, create large files for testing
fallocate -l 1GB 1gb.zip
