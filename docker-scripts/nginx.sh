#!/bin/sh
apt-get update
apt-get upgrade -y
apt-get install nginx -y
echo "" > /var/run/nginx.pid
chown nginx:nginx /var/run/nginx.pid
chown -R nginx:nginx /etc/nginx/conf.d/