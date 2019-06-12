FROM arm32v7/openjdk:8-jre-stretch
MAINTAINER Sascha Deeg <sascha.deeg@gmail.com>
USER root
COPY ./docker-scripts/ngnix.sh /root/ngnix.sh
RUN chmod +x /root/ngnix.sh
RUN /root/ngnix.sh
COPY ./docker-scripts/ngnix.conf /etc/nginx/nginx.conf

RUN mkdir -pv \
             /etc/ngnix-config/app \
             /etc/ngnix-config/config/sites \
             /etc/ngnix-config/config/template
RUN chown -R www-data:www-data /etc/ngnix-config
RUN chmod -R 770 /etc/ngnix-config
COPY ./target/ngnix-dns-reg-*.jar /etc/ngnix-config/app/ngnix-dns-reg.jar
COPY ./docker-scripts/start.sh /etc/ngnix-config/app/start.sh
RUN chmod +x /etc/ngnix-config/app/start.sh
USER www-data
EXPOSE 80 443 8080
CMD /etc/ngnix-config/app/start.sh