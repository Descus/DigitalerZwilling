package de.frauas.scenario.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import de.frauas.GUI.controllers.NotificationHelper;
import de.frauas.Settings;
import de.frauas.scenario.dto.Scenario;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * A utility class for handling scenario definition files in XML format.
 * It provides methods to create an object representation from a file path
 * and to load and parse the XML file into a {@link Scenario} data transfer object (DTO).
 * @author Scenario
 */
public class ScenarioXmlFile {
    /**
     * The file path of the scenario XML file.
     * This variable stores the location of the XML file that is used to define or load
     * a {@link Scenario} instance. It is initialized during the creation of a
     * {@link ScenarioXmlFile} instance and remains constant throughout its lifecycle.
     * The file path must not be null or empty to ensure proper functionality.
     */
    private final String filePath;

    /**
     * Private constructor to initialize with a file path.
     * Use the static factory methods {@code fromPath} or {@code fromExample} to create instances.
     * <p>
     * @param filePath The path to the scenario XML file.
     */
    private ScenarioXmlFile(String filePath){
        this.filePath = filePath;
    }

    /**
     * Creates a {@code ScenarioXmlFile} instance from the provided file path.
     * Validates the input to ensure the file path is neither null nor empty.
     * Throws an {@code IllegalArgumentException} if the validation fails.
     *
     * @param filePath the path to the scenario XML file
     * @return a new instance of {@code ScenarioXmlFile} initialized with the provided file path
     * @throws IllegalArgumentException if the provided file path is null or empty
     */
    public static ScenarioXmlFile fromPath(String filePath) throws IllegalArgumentException{
        if(filePath == null || filePath.isEmpty()){
            // Show an error message to the user and throw an exception for the developer.
            NotificationHelper.showError("File path must not be null or empty");
            throw new IllegalArgumentException("File path must not be null or empty");
        }
        return new ScenarioXmlFile(filePath);
    }

    /**
     * Creates a {ScenarioXmlFile} instance from a default example scenario file defined in the settings.
     *
     * @return A new instance of {@link ScenarioXmlFile} pointing to the example file.
     * @throws IllegalArgumentException if the default scenario file cannot be found in the resources.
     */
    public static ScenarioXmlFile fromExample(){
        // Get the URL of the resource from the classpath.
        URL resource = ScenarioXmlFile.class.getClassLoader().getResource(Settings.SCENE.DEFAULT_SCENARIO_FILE);
        if (resource == null) {
            // Handle cases where the example file is missing.
            NotificationHelper.showError("File not found!");
            throw new IllegalArgumentException("File not found!");
        }
        // Create an instance using the found file's path.
        return fromPath(resource.getFile());
    }
    /**
     * Reads the scenario XML file from the specified file path and deserializes its content
     * into a {@link Scenario} object. The method uses Jackson's {@link XmlMapper} to map
     * the XML structure of the file to the {@code Scenario} class.
     *
     * @return The parsed {@link Scenario} object representing the content of the XML file.
     * @throws IOException If an I/O error occurs while reading the XML file.
     */
    public Scenario read() throws IOException {
        File file = new File(filePath);
        // Use Jackson's XmlMapper to map the XML content to the Scenario class.
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(file, Scenario.class);
    }
}