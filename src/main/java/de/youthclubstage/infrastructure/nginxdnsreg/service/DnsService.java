package de.youthclubstage.infrastructure.nginxdnsreg.service;

import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsCreateUpdateDto;
import de.youthclubstage.infrastructure.nginxdnsreg.endpoint.model.DnsDto;
import de.youthclubstage.infrastructure.nginxdnsreg.entity.DnsEntry;
import de.youthclubstage.infrastructure.nginxdnsreg.repository.DnsRepository;
import de.youthclubstage.infrastructure.nginxdnsreg.service.mapping.DnsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DnsService {
    private final DnsRepository dnsRepository;
    private final DnsMapper dnsMapper;
    private final TemplateService templateService;

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;


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
        reloadnginx();
    }

    public List<DnsDto> getAll(){
        return dnsMapper.toDto(new ArrayList<>(UtilClass.makeCollection(dnsRepository.findAll())));

    }


    @EventListener(ApplicationReadyEvent.class)
    public void writeAllEntries(){
        if(activeProfile.equalsIgnoreCase("docker")) {
            List<DnsDto> all = getAll();
            for (DnsDto dnsDto : all) {
               if(dnsDto != null) {
                   templateService.writeTemplate(dnsDto.getSource(), dnsDto.getTarget());
               }
            }
            reloadnginx();
        }
    }


    private void reloadnginx(){
        executeBashCommand("kill -s HUP $(ps aux | grep 'nginx' | awk '{print $2}')");
    }

    public void delete(UUID id){
        Optional<DnsEntry> dnsEntry =  dnsRepository.findById(id);
        if(!dnsEntry.isPresent()){
            throw new RuntimeException("Not Found");
        }
        templateService.delete(dnsEntry.get().getSource());
        dnsRepository.deleteById(id);
        reloadnginx();
    }

    public static boolean executeBashCommand(String command) {
        boolean success = false;
        System.out.println("Executing BASH command:\n   " + command);
        Runtime r = Runtime.getRuntime();
        // Use bash -c so we can handle things like multi commands separated by ; and
        // things like quotes, $, |, and \. My tests show that command comes as
        // one argument to bash, so we do not need to quote it to make it one thing.
        // Also, exec may object if it does not have an executable file as the first thing,
        // so having bash here makes it happy provided bash is installed and in path.
        String[] commands = {"bash", "-c", command};
        try {
            Process p = r.exec(commands);

            p.waitFor();
            BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = b.readLine()) != null) {
                System.out.println(line);
            }

            b.close();
            success = true;
        } catch (Exception e) {
            System.err.println("Failed to execute bash with command: " + command);
            e.printStackTrace();
        }
        return success;
    }




}
