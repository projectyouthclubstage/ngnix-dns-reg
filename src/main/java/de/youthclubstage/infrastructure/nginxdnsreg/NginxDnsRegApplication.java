package de.youthclubstage.infrastructure.nginxdnsreg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NginxDnsRegApplication {

	public static void main(String[] args) {
		SpringApplication.run(NginxDnsRegApplication.class, args);
	}


}
