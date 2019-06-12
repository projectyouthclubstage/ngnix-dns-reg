package de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model;

import lombok.Data;


@Data
public class DnsCreateUpdateDto {

    private String source;

    private String target;
}
