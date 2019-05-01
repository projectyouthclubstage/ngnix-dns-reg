package de.youthclubstage.infrastructure.ngnixdnsreg.service;

import de.youthclubstage.infrastructure.ngnixdnsreg.configuration.TemplateProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE;

@Service
public class TemplateService {
    private final TemplateProperties templateProperties;

    @Autowired
    public TemplateService(TemplateProperties templateProperties){
        this.templateProperties = templateProperties;
    }

    public void writeTemplate(String source, String target) {
        String template = getTemplate(source);

        String templateTarget = template.replace("{var.target}",target);
        templateTarget = templateTarget.replace("{var.source}",source);
        templateTarget = templateTarget.replace("{var.certpath}",templateProperties.getCertsPath());

        try {
            writeFile(templateProperties.getSitesPath()+"/"+source,templateTarget, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben",e);
        }


    }

    private String getTemplate(String source) {
        File file = new File(templateProperties.getTemplatePath()+"/"+source);
        if(!(file.exists() && !file.isDirectory()))
        {
            file = new File(templateProperties.getTemplatePath()+"/default.template");
        }

        if(!(file.exists() && !file.isDirectory()))
        {
           throw new RuntimeException("template nicht auffindbar!");
        }

        String template;
        try {
            template = readFile(file.getAbsolutePath(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("template nicht lesbar!",e);
        }
        return template;
    }

    private String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private void writeFile(String path,String data, Charset encoding)
            throws IOException
    {
        deleteFile(path);
        Files.write(Paths.get(path),data.getBytes(encoding), CREATE);
    }

    public void delete(String source){
        deleteFile(templateProperties.getSitesPath()+"/"+source);
    }

    private void deleteFile(String path) {
        File file = new File(path);
        if(file.exists() && !file.isDirectory()){
            file.delete();
        }
    }


}
