mkdir .ssh
chmod 700 .ssh
touch .ssh/authorized_keys
chmod 600 .ssh/authorized_keys
cat /vagrant/dina-admin.pub >> .ssh/authorized_keys
cp /vagrant/.bitnami-pass .
chmod 600 .bitnami-pass