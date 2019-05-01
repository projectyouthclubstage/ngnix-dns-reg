package de.youthclubstage.infrastructure.ngnixdnsreg.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@Data
@ConfigurationProperties(value = "template")
public class TemplateProperties {
    private String sitesPath;
    private String templatePath;
    private String certsPath;
}
