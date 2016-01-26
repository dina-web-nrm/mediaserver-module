# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.vm.box = "ubuntu/trusty64"
  config.vm.box_url = "https://atlas.hashicorp.com/ubuntu/boxes/trusty64"

  # config.vm.network "public_network", :public_network => "eth0"
  # config.vm.network "public_network"
  # config.vm.network "public_network", ip: "172.16.23.75", :public_network => "eth1"
  # config.vm.network :bridged, bridge: "em1", :use_dhcp_assigned_default_route => true

# use for host only networking with port mapping
  config.vm.network :forwarded_port, guest: 80, host: 8080, auto_correct: true
  config.vm.network :forwarded_port, guest: 443, host: 8443, auto_correct: true

#  config.vm.synced_folder "../datasets", "/vagrant_data"

  config.vm.provider :virtualbox do |vb|
#    vb.gui = true
    vb.customize ["modifyvm", :id, "--memory", "1024"]
  end

  config.vm.provision :shell, :path => "bootstrap.sh"

end
