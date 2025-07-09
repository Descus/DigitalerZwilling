package de.frauas.scenario.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.dto.Scenario;

import java.io.File;
import java.io.IOException;

public class ScenarioLoader {
    public static Scenario loadFromFile(String filepath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        File xmlFile = new File(filepath);
        return xmlMapper.readValue(xmlFile, Scenario.class);
    }
}