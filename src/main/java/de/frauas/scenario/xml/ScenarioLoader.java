package de.frauas.scenario.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.dto.Scenario;

import java.io.IOException;
import java.io.InputStream;

public class ScenarioLoader {
    public static Scenario loadFromFile(String resourcePath) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        try (InputStream inputStream = ScenarioLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Cannot find resource: " + resourcePath);
            }
            return xmlMapper.readValue(inputStream, Scenario.class);
        }
    }
}