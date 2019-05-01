package de.youthclubstage.infrastructure.ngnixdnsreg.repository;

import de.youthclubstage.infrastructure.ngnixdnsreg.entity.DnsEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DnsRepository extends CrudRepository<DnsEntry,UUID> {
    List<DnsEntry> findAllBySource(String source);
}
