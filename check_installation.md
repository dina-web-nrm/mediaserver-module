Bitnami-stack
apache 	on port 8080
wildfly on port 8090
mysql   on port 3306

redirection in the bootstrap.sh-file
tcp --dport  80 -j REDIRECT --to-ports 8080
tcp --dport 443 -j REDIRECT --to-ports 8443

login to the machine:
'host'>ssh 

dina@dina-web-vm:~$ sudo su dina
dina@dina-web-vm:~$ cd ../dina/
dina@dina-web-vm:~$ ls bitnami/wildfly/
<drwxr-xr-x> apache2 		(directory)
<-rwxr-xr-x> ctlscript.sh	(startscript for services)
<drwxr-xr-x> mysql		(directory)
<drwxr-xr-x> wildfly		(directory)

dina@dina-web-vm:~$ cd bitnami/wildfly/wildfly/
-> jboss-cli.sh is a command line management tool for wildfly.
dina@dina-web-vm:~$ bin/jboss-cli.sh
[disconnected /] connect
-check that all is deployed
[standalone@localhost:9990 /] ls deployment
mediaserver-ear.ear                  mysql-connector-java-5.1.34.jar      
mysql-connector-java-5.1.12-bin.jar  postgresql-9.1-903.jdbc4.jar         
[standalone@localhost:9990 /]

