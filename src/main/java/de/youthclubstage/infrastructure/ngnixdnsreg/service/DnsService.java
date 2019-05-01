package de.youthclubstage.infrastructure.ngnixdnsreg.service;

import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.entity.DnsEntry;
import de.youthclubstage.infrastructure.ngnixdnsreg.repository.DnsRepository;
import de.youthclubstage.infrastructure.ngnixdnsreg.service.mapping.DnsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DnsService {
    private final DnsRepository dnsRepository;
    private final DnsMapper dnsMapper;
    private final TemplateService templateService;


    @Autowired
    public DnsService(DnsRepository dnsRepository,DnsMapper dnsMapper,
                      TemplateService templateService){
        this.dnsRepository = dnsRepository;
        this.dnsMapper = dnsMapper;
        this.templateService = templateService;
    }

    public void createDns(DnsCreateUpdateDto dnsCreateUpdateDto){
        List<DnsEntry> current = dnsRepository.findAllBySource(dnsCreateUpdateDto.getSource());
        if(current.size() != 0){
            dnsRepository.deleteAll(current);
        }
        templateService.writeTemplate(dnsCreateUpdateDto.getSource(),dnsCreateUpdateDto.getTarget());
        dnsRepository.save(dnsMapper.toEntity(dnsCreateUpdateDto));
    }

    public List<DnsDto> getAll(){
        return dnsMapper.toDto(new ArrayList<>(UtilClass.makeCollection(dnsRepository.findAll())));

    }

    public void delete(UUID id){
        Optional<DnsEntry> dnsEntry =  dnsRepository.findById(id);
        if(!dnsEntry.isPresent()){
            throw new RuntimeException("Not Found");
        }
        templateService.delete(dnsEntry.get().getSource());
        dnsRepository.deleteById(id);
    }




}
