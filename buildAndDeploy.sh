#!/bin/bash
# Tested with Wildfly 8.2.0.Final,make sure that the server is started and has the JNDI 'java:/MediaDS'
mvn clean package wildfly:deploy
