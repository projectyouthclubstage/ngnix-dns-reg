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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
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
    public DnsService(DnsRepository dnsRepository, DnsMapper dnsMapper,
                      TemplateService templateService) {
        this.dnsRepository = dnsRepository;
        this.dnsMapper = dnsMapper;
        this.templateService = templateService;
    }

    public boolean checkTarget(DnsEntry dnsEntry) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            final String response = restTemplate.getForObject("http://" + dnsEntry.getTarget(), String.class);
        } catch (UnknownHttpStatusCodeException | HttpServerErrorException|UnknownHostException ex) {
            return false;
        } catch (HttpClientErrorException ex) {
            return true;
        }
        return true;
    }

    public void createDns(DnsCreateUpdateDto dnsCreateUpdateDto) {
        List<DnsEntry> current = dnsRepository.findAllBySource(dnsCreateUpdateDto.getSource());
        if (current.size() != 0) {
            dnsRepository.deleteAll(current);
        }
        DnsEntry dnsEntry = dnsMapper.toEntity(dnsCreateUpdateDto);
        if (checkTarget(dnsEntry)) {
            dnsEntry.setValid(true);
            templateService.writeTemplate(dnsCreateUpdateDto.getSource(), dnsCreateUpdateDto.getTarget());
        }
        else
        {
            dnsEntry.setValid(false);
        }
        dnsRepository.save(dnsEntry);
        reloadAndClean();
    }

    public List<DnsDto> getAll() {
        List<DnsEntry> entries = new ArrayList<>(UtilClass.makeCollection(dnsRepository.findAll()));
        return dnsMapper.toDto(entries);

    }

    public void cleanUpInvalidEntries()
    {
        List<DnsEntry> entries = new ArrayList<>(UtilClass.makeCollection(dnsRepository.findAll()));
        for (DnsEntry dnsEntry : entries) {
            if (dnsEntry != null) {
                if (!checkTarget(dnsEntry))
                {
                    templateService.delete(dnsEntry.getSource());
                    dnsEntry.setValid(false);
                }else {
                    if(dnsEntry.getValid() == null){
                        dnsEntry.setValid(true);
                    }
                    if(!dnsEntry.getValid()){
                        templateService.writeTemplate(dnsEntry.getSource(), dnsEntry.getTarget());
                    }
                }

            }
        }

    }


    @EventListener(ApplicationReadyEvent.class)
    public void writeAllEntries() {
        if (activeProfile.equalsIgnoreCase("docker")) {
            List<DnsEntry> entries = new ArrayList<>(UtilClass.makeCollection(dnsRepository.findAll()));
            for (DnsEntry dnsEntry : entries) {
                if (dnsEntry != null) {
                    if (checkTarget(dnsEntry)) {
                        dnsEntry.setValid(true);
                        templateService.writeTemplate(dnsEntry.getSource(), dnsEntry.getTarget());
                    }
                    else
                    {
                        dnsEntry.setValid(false);
                    }

                }
                dnsRepository.save(dnsEntry);
            }
            reloadnginx();
        }
    }

    private void reloadAndClean(){
        cleanUpInvalidEntries();
        reloadnginx();
    }


    private void reloadnginx() {

        executeBashCommand("kill -s HUP $(cat /var/run/nginx.pid)");
    }

    public void delete(UUID id) {
        Optional<DnsEntry> dnsEntry = dnsRepository.findById(id);
        if (!dnsEntry.isPresent()) {
            throw new RuntimeException("Not Found");
        }
        templateService.delete(dnsEntry.get().getSource());
        dnsRepository.deleteById(id);
        reloadAndClean();
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
