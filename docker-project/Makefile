# all: init db build up

.PHONY: all


up: database
	docker-compose up -d app 

database:
	docker-compose up -d db.media

server: database
	docker-compose up -d app

test-https:
	cd testing; ./post-SSL-1-image.sh

stop:
	docker-compose stop

init:
	@echo "Pulling the DINA mediaserver-module release"
	wget https://archive.org/download/mediaserver/mediaserver.ear -O srv/releases/mediaserver.ear

build: 
	docker build -t inkimar/vega-server:v4.0 ./wildfly-custom/
