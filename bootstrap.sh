#!/usr/bin/env bash

export DEBIAN_FRONTEND=noninteractive

echo "Setting hostname to dina-web-vm"
hostname dina-web-vm

echo "Install OS system requirements and security updates"
apt-get update > /dev/null
echo "Install git"
apt-get install -y unattended-upgrades git > /dev/null
echo "Install wget"
apt-get install -y unattended-upgrades wget > /dev/null
echo "Install maven"
apt-get install -y unattended-upgrades maven > /dev/null
echo "Install curl"
apt-get install -y unattended-upgrades curl > /dev/null
echo "Install rdate"
apt-get install -y unattended-upgrades rdate > /dev/null

#apt-get install -y unattended-upgrades git wget maven > /dev/null
echo "apt upgrade"
apt-get upgrade > /dev/null
echo "dpkg "
dpkg-reconfigure -plow unattended-upgrades

echo "Synching time"
rdate -n dc1.nrm.se

echo "Setting up utf-8 locales"
locale-gen UTF-8

echo "Setting up swap file"
dd if=/dev/zero of=/var/my_swap bs=1M count=4096
mkswap -f /var/my_swap
swapon /var/my_swap

echo " -start- Adding DINA-WEB system user called dina"
useradd -p $(openssl passwd -1 $(cat /vagrant/.bitnami-pass))  -m -d /home/dina dina
addgroup dina vagrant 
addgroup dina sudo

echo "Generating and installing keypair for system user"
su dina -l -c /vagrant/dina_admin_generate_key.sh
su dina -l -c /vagrant/dina_admin_setup_key.sh

chmod g+r /vagrant/.bitnami-pass

echo "1(4) Installing bitnami open source server sw stacks"
su dina -l -c /vagrant/bitnami-sw.sh

sleep 1s

# Läser automatiskt in environment för dina-användaren
su dina -l -c "sed -e 's/exec bash ..rcfile/source/g' ~/bitnami/wildfly/use_wildfly  | grep -v '#!' | grep -v 'exec' > .bash_profile"

echo "2(4) bootstrap:: setting up mysql"
su dina -l -c /vagrant/mysql-setup.sh

# 2015-03-09, not used right now.
echo "3(4) bootstrap:: setting up JDK-7"
#su dina -l -c /vagrant/setup-oracle-jdk7.sh
sudo /vagrant/setup-oracle-jdk7.sh

echo "4(4) bootstrap:: setting up mediaserver"
su dina -l -c /vagrant/setup-mediaserver.sh

#echo "Install SSL certificates"
#echo "...TODO..."
sleep 1s

#echo "Installing firewall rules"
#iptables -t mangle -A PREROUTING -p tcp --dport 8080 -j MARK --set-mark 1
#iptables -t mangle -A PREROUTING -p tcp --dport 8443 -j MARK --set-mark 1
#iptables -t mangle -A PREROUTING -p tcp --dport 8090 -j MARK --set-mark 1
#iptables -A INPUT -p TCP -m mark --mark 1 -j REJECT --reject-with tcp-reset

iptables -t nat -I PREROUTING -i eth0 -p tcp --dport 80 -j REDIRECT --to-ports 8080
iptables -t nat -I PREROUTING -i eth0 -p tcp --dport 443 -j REDIRECT --to-ports 8443
iptables -t nat -A OUTPUT -o lo -p tcp --dport 80 -j REDIRECT --to-port 8080
iptables -t nat -A OUTPUT -o lo -p tcp --dport 443 -j REDIRECT --to-port 8443
 sleep 1s

#echo "Configuring automatic startup of services"
#echo "... use rc.local approach for portability..."
# cp /vagrant/etc/rc.local /etc/rc.local

#echo "Configuring backups"
#echo "Install and setup iRODS-commands deb"
#echo "... TODO schedule w crontab calls to backup scripts under ~/bin"

echo "Done with bootstrap!"


