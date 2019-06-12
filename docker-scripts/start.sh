#!/bin/sh
rm  -f /etc/nginx/conf.d/*
java -Dspring.profiles.active=docker -jar /etc/ngnix-config/app/ngnix-dns-reg.jar && nginx -g daemon off;