#!/bin/sh
rm  -f /etc/nginx/conf.d/*
su - www-data -s /bin/bash -c 'java -Dspring.profiles.active=docker -jar /etc/nginx-config/app/nginx-dns-reg.jar' &
nginx -g 'daemon off;'

