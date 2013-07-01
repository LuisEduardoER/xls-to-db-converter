#!/bin/bash

TOMCAT_DIR="/opt/tomcat-7/"
WEBAPPS_DIR="${TOMCAT_DIR}webapps/"
WAR_FILE="${WEBAPPS_DIR}converter_web.war"
WAR_DIR="${WEBAPPS_DIR}converter_web/"

sudo sh /opt/apache-tomcat-7.0.11/bin/shutdown.sh
echo

if [ -f $WAR_FILE ]; then
sudo rm $WAR_FILE
echo $WAR_FILE removed
fi

if [ -d $WAR_DIR ]; then
sudo rm -R $WAR_DIR
echo $WAR_DIR removed
fi

echo

sudo cp ./target/converter_web.war $WEBAPPS_DIR
sudo sh ${TOMCAT_DIR}bin/startup.sh
echo
echo Tomcat was started
