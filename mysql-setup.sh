echo "Start: mysql-setup.sh"
pass=$(cat ~/.bitnami-pass)

# rättigheter?
mysql -uroot -e "create user 'mediaserver'@'localhost' identified by 'mediaserver';"
mysql -uroot -e "grant all on *.* to 'mediaserver'@'localhost';"
#mysql -uroot -e "grant all on *.* to 'mediaserver'@'%';"
mysql -uroot  -e "flush privileges;"

# create databases to be used by all DINA modules
mysql -umediaserver -pmediaserver -e "create database nrm_media;"

# wildfly data source configuration
wget http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.34/mysql-connector-java-5.1.34.jar -O ~/dl/mysql-connector-java-5.1.34.jar

# deploy the mysql driver & connect datasource to wildfly
cd ~/bitnami/wildfly/wildfly/bin
./jboss-cli.sh --connect --command="deploy ~/dl/mysql-connector-java-5.1.34.jar"
./jboss-cli.sh --connect --command="data-source add --name=MediaDS --jndi-name=java:/MediaDS  --driver-name=mysql-connector-java-5.1.34.jar_com.mysql.jdbc.Driver_5_1 --connection-url=jdbc:mysql://localhost:3306/nrm_media --user-name=mediaserver --password=mediaserver"

echo "done with mysql-setup.sh"
