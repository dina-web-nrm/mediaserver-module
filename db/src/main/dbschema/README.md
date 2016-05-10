# pre-requirements

## Check out the docker-machine at @ToDO else do the steps
1. Create a database and a user on your machine.
2. for instance : db = 'media', user='mediaserver'/ psw='mediaserver'
3. Update the **liquibase.properties**-file,  use the above db + user/psw

### log in as root to MySQL 

1. mysql> CREATE database media;
2. mysql> CREATE USER 'mediaserver'@'localhost' IDENTIFIED BY 'mediaserver';
3. mysql> GRANT ALL PRIVILEGES ON media . * TO 'mediaserver'@'localhost';

###  Execute the 'db-project by running :
1. $mvn clean install

## If deploying in Glassfish 
1. update the glassfish-resources.xml
2. set URL to database

##  If deploying to Wildfly.
1. Create a JNDI-connection according to the following.
[Wildfly] (https://gist.github.com/Inkimar/d81639a9cd41e96903bfbaa9d07decff)
