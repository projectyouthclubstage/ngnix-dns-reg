package de.youthclubstage.infrastructure.ngnixdnsreg.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Data
@RedisHash(value = "DnsEntry")
public class DnsEntry {

    @Id
    private UUID id;

    @Indexed
    private String source;

    private String target;


}
