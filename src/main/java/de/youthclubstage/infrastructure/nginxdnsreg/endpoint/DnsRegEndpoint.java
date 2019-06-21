package de.youthclubstage.infrastructure.nginxdnsreg.endpoint;


import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.nginxdnsreg.service.DnsService;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1")
public class DnsRegEndpoint {

    private final DnsService dnsService;

    private static Logger log = LogManager.getLogger(DnsRegEndpoint.class)

    @Autowired
    public DnsRegEndpoint(DnsService dnsService){
        this.dnsService = dnsService;
    }

    @GetMapping(value = "dns", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<List<DnsDto>> getall(){
       return ResponseEntity.ok().body(dnsService.getAll());
    }

    @PostMapping( value = "dns", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseEntity<DnsDto> create(@RequestBody DnsCreateUpdateDto createUpdateDto){
        log.error(createUpdateDto.toString());
        DnsDto dnsDto = dnsService.createDns(createUpdateDto);
        return ResponseEntity.ok(dnsDto);
    }

    @DeleteMapping( value = "dns/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        dnsService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
