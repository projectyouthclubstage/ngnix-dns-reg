package de.youthclubstage.infrastructure.ngnixdnsreg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NgnixDnsRegApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgnixDnsRegApplication.class, args);
	}


}
