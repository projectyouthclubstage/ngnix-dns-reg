package de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model;

import lombok.Data;

import java.util.UUID;

@Data
public class DnsDto {
    private UUID id;

    private String source;

    private String target;
}
