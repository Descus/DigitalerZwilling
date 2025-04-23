package de.frauas.scenario.xml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.xml.dto.Scenario;

import java.io.File;
import java.io.IOException;

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
        try {
            return fromPath(ClassLoader.getSystemResource("Scenario/example.xml").getFile());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    
    Scenario read() throws IOException {
        File file = new File(filePath);
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(file, Scenario.class);
    }

    public static void main(String[] args) throws IOException {
        Scenario scenario = ScenarioXmlFile.fromExample().read();
    }

}
