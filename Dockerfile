FROM arm32v7/openjdk:8-jre-stretch
MAINTAINER Sascha Deeg <sascha.deeg@gmail.com>
USER root
COPY ./docker-scripts/nginx.sh /root/nginx.sh
RUN chmod +x /root/nginx.sh
RUN /root/nginx.sh
COPY ./docker-scripts/nginx.conf /etc/nginx/nginx.conf

RUN mkdir -pv \
             /etc/nginx-config/app \
             /etc/nginx-config/config/sites \
             /etc/nginx-config/config/template
RUN chown -R www-data:www-data /etc/nginx-config
RUN chmod -R 770 /etc/nginx-config
COPY ./target/nginx-dns-reg-*.jar /etc/nginx-config/app/nginx-dns-reg.jar
COPY ./docker-scripts/start.sh /etc/nginx-config/app/start.sh
RUN chmod +x /etc/nginx-config/app/start.sh
USER www-data
EXPOSE 80 443 8080
CMD /etc/nginx-config/app/start.sh