#!/bin/sh -x

# Run this script from the base katalon-mobile-util directory.
mkdir lib

version="1.0.0.201906110939"

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core_${version}.jar" \
  -DgroupId=com.kms.katalon \
  -DartifactId=core \
  -Dversion=${version} \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.mobile_${version}.jar" \
  -DgroupId=com.kms.katalon.core \
  -DartifactId=mobile \
  -Dversion=${version} \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib

mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file \
  -Dfile="/Applications/Katalon Studio.app/Contents/Eclipse/plugins/com.kms.katalon.core.webui_${version}.jar" \
  -DgroupId=com.kms.katalon.core \
  -DartifactId=webui \
  -Dversion=${version} \
  -Dpackaging=jar \
  -DlocalRepositoryPath=lib

mvn package -U

mvn dependency:resolve
