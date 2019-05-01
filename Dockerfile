FROM armv7/armhf-java8:latest
MAINTAINER Sascha Deeg <sascha.deeg@gmail.com>
USER root
RUN mkdir -pv \
             /etc/ngnix-config/app \
             /etc/ngnix-config/config/sites \
             /etc/ngnix-config/config/template
RUN chown -R ww-data:www-data /etc/ngnix-config
RUN chmod -R 770 /etc/ngnix-config
COPY ./target/ngnix-dns-reg-*.jar /etc/ngnix-config/app/ngnix-dns-reg.jar
USER www-data
EXPOSE 8080
CMD java -jar /etc/ngnix-config/app/ngnix-dns-reg.jar