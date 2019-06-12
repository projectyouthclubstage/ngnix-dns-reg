FROM arm32v7/openjdk:8-jre-slim-stretch
MAINTAINER Sascha Deeg <sascha.deeg@gmail.com>
USER root
RUN mkdir -pv \
             /etc/ngnix-config/app \
             /etc/ngnix-config/config/sites \
             /etc/ngnix-config/config/template
RUN chown -R www-data:www-data /etc/ngnix-config
RUN chmod -R 770 /etc/ngnix-config
COPY ./target/ngnix-dns-reg-*.jar /etc/ngnix-config/app/ngnix-dns-reg.jar
COPY ./start.sh /etc/ngnix-config/app/start.sh
RUN chmod +x /etc/ngnix-config/app/start.sh
USER www-data
EXPOSE 8080
CMD /etc/ngnix-config/app/start.sh