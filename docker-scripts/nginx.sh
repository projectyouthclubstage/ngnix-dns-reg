#!/bin/sh
apt-get update
apt-get upgrade -y
apt-get install nginx -y
echo "" > /var/run/nginx.pid
chown www-data:www-data /var/run/nginx.pid