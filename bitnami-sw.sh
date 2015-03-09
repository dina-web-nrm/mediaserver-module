echo "Setting up bitnami services password"
cp /vagrant/.bitnami-pass .
chmod 600 .bitnami-pass

echo "creating directories"
mkdir -p ~/dl ~/bitnami

echo "Getting and installing bitnami open source server sw"
wget https://bitnami.com/redirect/to/47719/bitnami-wildfly-8.2.0-1-linux-x64-installer.run \
-O ~/dl/wildfly-8.2.run

chmod +x ~/dl/wildfly-8.2.run

echo "running file"
~/dl/wildfly-8.2.run \
--mode unattended \
--prefix ~/bitnami/wildfly \
--apache_server_port 8080 \
--apache_server_ssl_port 8443 \
--jboss_manager_username manager \
--jboss_manager_password manager

cp /vagrant/wildfly-specifics/module.xml        ~/bitnami/wildfly/wildfly/modules/system/layers/base/org/eclipse/persistence/main
wget http://central.maven.org/maven2/org/eclipse/persistence/eclipselink/2.5.2/eclipselink-2.5.2.jar -O ~/dl/eclipselink-2.5.2.jar
cp /home/dina/dl/eclipselink-2.5.2.jar ~/bitnami/wildfly/wildfly/modules/system/layers/base/org/eclipse/persistence/main

echo "done with bitnami-sw.sh"

