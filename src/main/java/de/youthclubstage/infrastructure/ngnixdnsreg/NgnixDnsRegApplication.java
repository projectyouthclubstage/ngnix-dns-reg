package de.youthclubstage.infrastructure.ngnixdnsreg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
public class NgnixDnsRegApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgnixDnsRegApplication.class, args);
	}

}
