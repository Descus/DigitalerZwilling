package de.frauas.scenario.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.scenario.dto.Scenario;

import java.io.IOException;
import java.io.InputStream;

/**
 * A utility class for loading and parsing scenario definitions from XML files.
 * This class provides a static method to deserialize an XML resource file
 * into a {@link Scenario} object.
 * @author Scenario
 */
public class ScenarioLoader {
    /**
     * Loads a scenario from a given resource path using the class loader.
     * The method reads the XML file, maps it to the {@link Scenario} class,
     * and returns the resulting object.
     *
     * @param resourcePath The path to the XML resource file.
     * @return The deserialized {@link Scenario} object.
     * @throws IOException If the specified resource cannot be found or read.
     */
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