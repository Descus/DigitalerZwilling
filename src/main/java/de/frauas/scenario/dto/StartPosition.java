package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

/**
 * Represents the starting position and orientation of an entity in a two-dimensional space.
 * <p>
 * The class defines attributes for specifying horizontal and vertical positions,
 * as well as the initial heading (orientation) of an entity within a scenario.
 * It is intended to be used for tasks such as defining the start state of objects
 * or entities in a simulation or model.
 * <p>
 * Attributes:
 * - `x`: The x-coordinate of the starting position.
 * - `y`: The y-coordinate of the starting position.
 * - `heading`: The initial orientation or direction at the starting position.
 * <p>
 * This class is designed for serialization and deserialization using the Jackson XML library,
 * allowing it to be represented in XML format for input and output operations.
 * <p>
 * @author Scenario
 */
@Getter
public class StartPosition {
    /**
     * Represents the x-coordinate of the starting position in a two-dimensional space.
     * <p>
     * This attribute is used to define the horizontal position of an entity
     * within a simulation or scenario initialization. It is primarily designed
     * to specify the starting state of an object or entity.
     * <p>
     * It is serialized as an XML element with the local name "xPosition".
     */
    @JacksonXmlProperty(localName = "xPosition")
    int x;
    /**
     * Represents the y-coordinate of the starting position in a two-dimensional space.
     * <p>
     * This attribute is used to define the vertical position of an entity
     * within a simulation or scenario initialization. It is primarily designed
     * to specify the starting state of an object or entity.
     * <p>
     * It is serialized as an XML element with the local name "yPosition".
     */
    @JacksonXmlProperty(localName = "yPosition")
    int y;
    /**
     * Represents the initial orientation or direction of an entity in a two-dimensional space.
     * <p>
     * This attribute defines the heading or angle (typically in degrees)
     * at which the entity is oriented at the starting position. It is mainly
     * used to describe the rotational state of an object within a
     * simulation or scenario.
     * <p>
     * This field is serialized and deserialized as an XML element
     * during input and output operations.
     */
    @JacksonXmlProperty
    int heading;
}
