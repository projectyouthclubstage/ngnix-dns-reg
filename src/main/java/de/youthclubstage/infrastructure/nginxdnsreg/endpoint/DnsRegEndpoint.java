package de.youthclubstage.infrastructure.nginxdnsreg.endpoint;


import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.nginxdnsreg.service.DnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1")
public class DnsRegEndpoint {

    private final DnsService dnsService;

    @Autowired
    public DnsRegEndpoint(DnsService dnsService){
        this.dnsService = dnsService;
    }

    @GetMapping(value = "dns", produces = "application/json")
    ResponseEntity<List<DnsDto>> getall(){
       return ResponseEntity.ok().body(dnsService.getAll());
    }

    @PostMapping( value = "dns", consumes = "application/json")
    ResponseEntity<DnsDto> create(@RequestBody DnsCreateUpdateDto createUpdateDto){
        DnsDto dnsDto = dnsService.createDns(createUpdateDto);
        return ResponseEntity.ok(dnsDto);
    }

    @DeleteMapping( value = "dns/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        dnsService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
