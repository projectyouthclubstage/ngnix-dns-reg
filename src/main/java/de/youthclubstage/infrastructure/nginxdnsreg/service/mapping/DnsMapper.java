package de.youthclubstage.infrastructure.nginxdnsreg.service.mapping;

import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.nginxdnsreg.entity.DnsEntry;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DnsMapper {
    DnsEntry toEntity(DnsCreateUpdateDto source);

    List<DnsDto> toDto(List<DnsEntry> source);
}
