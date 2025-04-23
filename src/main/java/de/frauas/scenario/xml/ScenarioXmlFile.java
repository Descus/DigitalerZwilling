package de.frauas.scenario.xml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.frauas.scenario.components.Car;
import de.frauas.scenario.xml.dto.Scenario;

import java.io.File;
import java.io.IOException;

public class ScenarioXmlFile {
    private final String name;
    private final String filePath;
    
    public ScenarioXmlFile(String filePath){
        this.filePath = filePath;
        this.name = filePath.substring(filePath.lastIndexOf("/") + 1);
    }
    
    void read() throws IOException {
        File file = new File(ClassLoader.getSystemResource("Scenario/example.xml").getFile());

        XmlMapper xmlMapper = new XmlMapper();
        Scenario scenario = xmlMapper.readValue(file, Scenario.class);
        System.out.println(scenario);
    }

    public static void main(String[] args) throws IOException {
        File file = new File(ClassLoader.getSystemResource("Scenario/example.xml").getFile());
        XmlMapper xmlMapper = new XmlMapper();
        Scenario scenario = xmlMapper.readValue(file, Scenario.class);
        System.out.println(scenario);

    }

}
