package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

/**
 * Represents an object with spatial and geometric properties,
 * which can be associated with a Scenario to define its characteristics.
 * <p>
 * The class defines attributes for determining the position and size of an object.
 * It is primarily intended to represent objects with a start and end position
 * in two-dimensional space (x and y coordinates) and a height.
 * <p>
 * Attributes:
 * - `number`: A unique identifier or sequence number for the object.
 * - `xStart`: The x-coordinate of the object's starting position.
 * - `yStart`: The y-coordinate of the object's starting position.
 * - `xEnd`: The x-coordinate of the object's ending position.
 * - `yEnd`: The y-coordinate of the object's ending position.
 * - `height`: The height of the object.
 * <p>
 * The class is designed for serialization and deserialization from XML data using annotations
 * from the Jackson XML library.
 * <p>
 * @author Scenario
 */
@Getter
public class Object {
    /**
     * Represents a unique identifier or sequence number for the object.
     * This attribute is used to distinguish one object from another
     * within the context of a scenario or dataset.
     * <p>
     * It is serialized as an XML attribute with the local name "Number".
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    /**
     * Represents the x-coordinate of the object's starting position in a two-dimensional space.
     * This attribute is used to define where the object begins along the horizontal axis.
     * <p>
     * It is serialized as an XML element with the local name "xPositionStart".
     */
    @JacksonXmlProperty(localName = "xPositionStart")
    int xStart;
    /**
     * Represents the y-coordinate of the object's starting position in a two-dimensional space.
     * This attribute is used to define where the object begins along the vertical axis.
     * <p>
     * It is serialized as an XML element with the local name "yPositionStart".
     */
    @JacksonXmlProperty(localName = "yPositionStart")
    int yStart;
    /**
     * Represents the x-coordinate of the object's ending position in a two-dimensional space.
     * This attribute is used to define where the object ends along the horizontal axis.
     * <p>
     * It is serialized as an XML element with the local name "xPositionEnd".
     */
    @JacksonXmlProperty(localName = "xPositionEnd")
    int xEnd;
    /**
     * Represents the y-coordinate of the object's ending position in a two-dimensional space.
     * This attribute is used to define where the object ends along the vertical axis.
     * <p>
     * It is serialized as an XML element with the local name "yPositionEnd".
     */
    @JacksonXmlProperty(localName = "yPositionEnd")
    int yEnd;
    /**
     * Represents the height of an object in a three-dimensional space.
     * This attribute defines the vertical dimension of the object,
     * which complements its spatial extent defined by start and end positions.
     * <p>
     * This property is serialized as an XML element and is used
     * during the serialization and deserialization of object data.
     */
    @JacksonXmlProperty
    int height;
}
