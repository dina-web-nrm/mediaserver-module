echo "Start : Fetch JDK 1.7u75"
wget --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u75-b13/jdk-7u75-linux-x64.tar.gz -O  /home/dina/dl/jdk.tgz

# install Java 7 JDK system-wide
sudo mkdir -p /opt/jdk
sudo tar -zxf /home/dina/dl/jdk.tgz -C /opt/jdk

# set oracle jdk as default java
sudo update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.7.0_75/bin/java 100
sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk1.7.0_75/bin/javac 100

# add symlink to be used by bitnami sw
sudo mkdir /opt/java
sudo ln -s /opt/jdk/jdk1.7.0_75 /opt/java/jdk

echo "Done: Setting up JDK 1.7u75 "

