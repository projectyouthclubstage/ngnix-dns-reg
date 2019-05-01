package de.youthclubstage.infrastructure.ngnixdnsreg.service.mapping;

import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.entity.DnsEntry;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DnsMapper {
    DnsEntry toEntity(DnsCreateUpdateDto source);

    List<DnsDto> toDto(List<DnsEntry> source);
}
