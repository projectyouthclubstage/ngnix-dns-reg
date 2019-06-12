#!/bin/sh
rm  -f /etc/nginx/conf.d/*
java -Dspring.profiles.active=docker -jar /etc/nginx-config/app/nginx-dns-reg.jar && nginx -g daemon off;