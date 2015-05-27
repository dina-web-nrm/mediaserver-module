Bitnami-stack <p>
apache 	on port 8080 <p>
wildfly on port 8090 <p>
mysql   on port 3306 <p>

redirection in the bootstrap.sh-file <p>
tcp --dport  80 -j REDIRECT --to-ports 8080 <p>
tcp --dport 443 -j REDIRECT --to-ports 8443 <p>

login to the newly created machine: <p>
'host'>vagrant ssh <p>

dina@dina-web-vm:~$ sudo su dina <p>
dina@dina-web-vm:~$ cd ../dina/ <p>
dina@dina-web-vm:~$ ls bitnami/wildfly/ <p>
<drwxr-xr-x> apache2 		(directory) <p>
<-rwxr-xr-x> ctlscript.sh	(startscript for services) <p>
<drwxr-xr-x> mysql		(directory) <p>
<drwxr-xr-x> wildfly		(directory)<p> 

dina@dina-web-vm:~$ cd bitnami/wildfly/wildfly/<p>
-> jboss-cli.sh is a command line management tool for wildfly.<p>
dina@dina-web-vm:~$ bin/jboss-cli.sh<p>
[disconnected /] connect<p>
-check that all is deployed<p>
[standalone@localhost:9990 /] ls deployment<p>
mediaserver-ear.ear                  mysql-connector-java-5.1.34.jar     <p> 
mysql-connector-java-5.1.12-bin.jar  postgresql-9.1-903.jdbc4.jar<p>
-leave the managment tool         <p>
[standalone@localhost:9990 /] quit<p>

