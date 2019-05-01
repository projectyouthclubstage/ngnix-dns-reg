package de.youthclubstage.infrastructure.ngnixdnsreg.endpoint;


import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.ngnixdnsreg.service.DnsService;
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
    ResponseEntity<Void> create(@RequestBody DnsCreateUpdateDto createUpdateDto){
        dnsService.createDns(createUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping( value = "dns/{id}", consumes = "application/json")
    ResponseEntity<Void> delete(@PathVariable("id") UUID id){
        dnsService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
