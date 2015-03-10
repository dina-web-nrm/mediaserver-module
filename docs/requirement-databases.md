DINA-WEB - MediaServer Module

================
Installing : MySQL on Ubuntu
================

sudo apt-get install mysql-server <br/>
- add the root password when installing <br/>

================
Setting up a mysql-user 
================

log in as root. <br/>
mysql -u root -p <br/>
mysql> CREATE USER ‘ingimar’@'localhost' IDENTIFIED BY 'ingimar'; <br/>
mysql>  ALL PRIVILEGES ON * . * TO 'ingimar'@'localhost'; <br/>
mysql> FLUSH PRIVILEGES; <br/>
mysql> exit <br/>

================
Setting up: Database for the Mediaserver
================
log in as ingimar  <br/>
mysql -u ingimar -p  <br/>
mysql>  create database nrm_demo_media;  <br/>

<b>reflect the mysq-user&password in the liquibase.properties-file</b>

================
Setting up: Database for the 'SimpleTaxonMock-server'
================

log in as ingimar  <br/>
mysql -u ingimar -p  <br/>
mysql>  create database TaxonMock;  <br/>

<b>reflect the mysq-user&password in the liquibase.properties-file</b>
