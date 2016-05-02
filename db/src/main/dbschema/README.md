# pre-requirements

## Check out the docker-machine at @ToDO else do the steps
1. Create a database and a user on your machine.
1.1 for instance : db = 'media', user='mediaserver'/ psw='mediaserver'
2. Update the liquibase.properties-file,  use the above db + user/psw

## log in as root to MySQL 

1. mysql> CREATE database media;
2. mysql> GRANT ALL PRIVILEGES ON nrm_demo_media.* To 'media'@'localhost' IDENTIFIED BY 'mediaserver';

4)   Execute the 'db-project by running :
4.1) $mvn clean install

### If deploying in Glassfish e 
1) update thglassfish-resources.xml (in module xyz )
1.1) set URL to database
1.2) <property name="URL" value="jdbc:mysql://localhost:<port>/nrm_demo_media"/>
###  If deploying to Wildfly.
1) Create a JNDI-connection
[Wildfly] (https://gist.github.com/Inkimar/d81639a9cd41e96903bfbaa9d07decff)
