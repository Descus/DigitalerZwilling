package de.frauas.scenario.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.dto.Scenario;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static de.frauas.Settings.SCENARIO_FILE;


public class ScenarioXmlFile {
    private final String filePath;

    private ScenarioXmlFile(String filePath){
        this.filePath = filePath;
    }


    
    public static ScenarioXmlFile fromPath(String filePath) throws IllegalArgumentException{
        if(filePath == null || filePath.isEmpty()){
            throw new IllegalArgumentException("File path must not be null or empty");
        }
        return new ScenarioXmlFile(filePath);
    }
    
    public static ScenarioXmlFile fromExample(){
        URL resource = ScenarioXmlFile.class.getClassLoader().getResource(SCENARIO_FILE);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        }
        return fromPath(resource.getFile());
    }
    
    public Scenario read() throws IOException {
        File file = new File(filePath);
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(file, Scenario.class);
    }
}
