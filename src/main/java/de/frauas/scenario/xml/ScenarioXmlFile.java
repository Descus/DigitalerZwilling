package de.frauas.scenario.xml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.frauas.scenario.components.Car;

import java.io.File;
import java.io.FileInputStream;
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
        Car car = xmlMapper.readValue(file, Car.class);
        System.out.println(car);
    }

    public static void main(String[] args) throws IOException {
        File file = new File(ClassLoader.getSystemResource("Scenario/example.xml").getFile());
        XmlMapper xmlMapper = new XmlMapper();
        Car car = xmlMapper.readValue(file, Car.class);
        System.out.println(car);

    }

}
