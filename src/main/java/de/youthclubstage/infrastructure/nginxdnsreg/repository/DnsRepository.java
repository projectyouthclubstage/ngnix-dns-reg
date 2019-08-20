package de.youthclubstage.infrastructure.nginxdnsreg.repository;

import de.youthclubstage.infrastructure.nginxdnsreg.entity.DnsEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DnsRepository extends MongoRepository<DnsEntry,UUID> {
    List<DnsEntry> findAllBySource(String source);
}
