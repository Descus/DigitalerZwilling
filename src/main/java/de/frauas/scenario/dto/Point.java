package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

/**
 * Represents a point in a two-dimensional space, used for defining spatial positions
 * such as trace paths or other coordinate-based attributes within a scenario.
 * <p>
 * This class provides the coordinates (x, y) along with a unique number identifier
 * to represent the point in a dataset or scenario.
 * <p>
 * The class is designed for serialization and deserialization from XML data using
 * Jackson XML annotations.
 *
 * @author Scenario
 */
@Getter
public class Point {
    /**
     * Represents a unique identifier or sequence number for a point in a dataset or scenario.
     * It is used to distinguish one point from another within a trace path
     * or other spatial contexts in a two-dimensional space.
     * <p>
     * This attribute is serialized as an XML attribute with the local name "Number".
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    /**
     * Represents the x-coordinate of a point in a two-dimensional space.
     * This attribute is used to define the horizontal position of the point.
     * <p>
     * It is serialized as an XML element with the local name "xPosition"
     * and is utilized in scenarios such as trace paths or spatial representations.
     */
    @JacksonXmlProperty(localName = "xPosition")
    int x;
    /**
     * Represents the y-coordinate of a point in a two-dimensional space.
     * This attribute defines the vertical position of the point and is utilized
     * in spatial contexts such as trace paths or coordinate-based scenarios.
     * <p>
     * It is serialized as an XML element with the local name "yPosition".
     */
    @JacksonXmlProperty(localName = "yPosition")
    int y;
}
