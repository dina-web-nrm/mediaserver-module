echo "Start: mdkir for mediaserver"
mkdir -p /home/dina/artifact

myMediaDir=/home/dina/artifact/media-files/
mkdir -p $myMediaDir

echo "git clone mediaserver"
cd /home/dina/artifact
git clone https://github.com/DINA-Web/mediaserver-module.git

tar -xf /home/dina/artifact/mediaserver-module/docs/images.tar -C $myMediaDir

myProject=mediaserver-module
cd /home/dina/artifact/$myProject/db
echo "build db"
mvn clean package
sleep 2
mysql -umediaserver -pmediaserver -e "update nrm_media.admin_config set admin_value='$myMediaDir' where admin_key='path_to_files';"

echo "build project"
cd /home/dina/artifact/$myProject
mvn clean package

#deploy
cd ~/bitnami/wildfly/wildfly/bin
./jboss-cli.sh --connect --command="deploy ~/artifact/$myProject/ear/target/mediaserver-ear.ear"
