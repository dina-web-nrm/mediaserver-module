# dw-media
Integration project for the mediaserver module
Here we use `Vagrant` ( version 1.7.2 ) to install and setup the mediaserver module in a way compatile with the DINA-Web system

the file 'setup-mediaserver.sh' holds database-name + filepath to mediafiles.
the default filepath is set in the db.
the default filepath is '/home/dina/artifact/media-files'


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
cd 'project'
echo "passw0rd12" > .bitnami-pass

# build the server and connect to it
vagrant up
vagrant ssh
```

## What is a Vagrantfile?

This file outlines all configuration steps starting with a base linux OS. It calls a bootstrap script which installs all required OS dependencies along with all other required software and makes settings.

# Handy info

When you stop developing you have few options on how to close the virtual machine

## Destroys all the traces of the virtual machine
vagrant destroy

## Shutdowns the virtual machine
vagrant halt

## Saves the current running state to hd
vagrant suspend

Use one of the above. When ever you return to developing you just use the command

vagrant up

