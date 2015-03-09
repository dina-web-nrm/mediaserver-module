# dw-media
Integration project for media module

Here we use `Vagrant` ( version 1.7.2 ) to install and setup the mediaserver module in a way compatile with the DINA-Web system


## Usage instructions

To use this approach, install `vagrant` on your system. Also install `git` if you haven't already, and `VirtualBox`. 

Then use these steps to build the server, start it in VirtualBox and connect to the server using ssh.

```console
mkdir -p ~/repos/dina-web
cd ~/repos/dina-web

# get this integration project
git clone https://github.com/DINA-Web/dw-media.git

# remember to not use the default password
# change it to using the approach below
cd server-vm
echo "passw0rd12" > .bitnami-pass

# build the server and connect to it
vagrant up
vagrant ssh
```

## What is a Vagrantfile?

This file outlines all configuration steps starting with a base linux OS. It calls a bootstrap script which installs all required OS dependencies along with all other required software and makes settings.

## Building the server from scratch

We make use of open source software server software stacks from bitnami to get required software such as web server (apache), application server (django), databases (mysql, psql) in place.

Then we configure the system so it is becomes turn-key ready. And load data.

