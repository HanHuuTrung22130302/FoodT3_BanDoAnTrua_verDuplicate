#!/bin/bash

# Local build
echo "Building WAR file with Maven..."
mvn clean package

# Check if WAR was built successfully
if [ ! -f target/ROOT.war ]; then
  echo "❌ Build failed: ROOT.war not found."
  exit 1
fi

SERVER=root@160.187.229.8
REMOTE_DIR=/root/tomcat-app
LOCAL_WAR=target/ROOT.war
CONTAINER_NAME=tomcat10
MYSQL_CONTAINER=mysql-db


echo "Deleting old WAR and ROOT dir on server..."
ssh -t $SERVER "rm -rf $REMOTE_DIR/ROOT.war $REMOTE_DIR/ROOT"

echo "Uploading WAR file..."
scp $LOCAL_WAR $SERVER:$REMOTE_DIR

echo "Restarting Tomcat Docker container..."
ssh -t $SERVER << EOF
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true
docker run -d --name $CONTAINER_NAME -v $REMOTE_DIR:/usr/local/tomcat/webapps -p 80:8080 tomcat:10
EOF

echo "Deploy done!"
