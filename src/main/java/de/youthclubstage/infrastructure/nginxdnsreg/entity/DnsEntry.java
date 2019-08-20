package de.youthclubstage.infrastructure.nginxdnsreg.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

@Data
public class DnsEntry {

    @Id
    private UUID id;

    @Indexed
    private String source;

    private String target;

    private Boolean valid;


}
