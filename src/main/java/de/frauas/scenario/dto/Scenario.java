package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents a scenario containing details about specific events, positions,
 * and objects within a defined context. The Scenario class is designed for
 * serialization and deserialization from XML data using Jackson XML annotations.
 * <p>
 * This class encapsulates key attributes that describe a scenario:
 * - A unique identifier (`id`) for the scenario.
 * - The date associated with the scenario.
 * - The version of the scenario definition.
 * - The starting position (`startPosition`), which specifies the initial point and heading.
 * - A series of trace points (`trace`), each representing coordinates in a two-dimensional space.
 * - A list of objects (`objects`) representing entities with spatial and dimensional properties.
 *
 * @author Scenario
 */
@Getter
@JacksonXmlRootElement(localName = "Scenario")
public class Scenario {
    /**
     * Represents the unique identifier for a scenario within a given dataset or system context.
     * This attribute is serialized as an XML attribute with the local name "Scenario_ID".
     * <p>
     * The `id` serves as a primary reference for identifying and differentiating between multiple scenarios
     * during data processing, serialization, and deserialization operations.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Scenario_ID")
    String id;
    /**
     * Represents the date associated with the scenario.
     * <p>
     * This attribute is serialized as an XML attribute with the local name "Date".
     * It is intended to store the date information relevant to the scenario,
     * typically formatted as a string.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Date")
    String date;
    /**
     * Represents the version number associated with the scenario definition.
     * <p>
     * This attribute is serialized as an XML attribute with the local name "Version".
     * It is used to indicate the revision or iteration of the scenario specification
     * and provides a reference for version control in systems that use this data.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "Version")
    String version;
    /**
     * Represents the starting position within a scenario, which defines
     * the initial spatial coordinates and heading orientation of a scenario's context.
     * <p>
     * This attribute is serialized using Jackson XML annotations and includes:
     * - `x`: The x-coordinate of the initial position.
     * - `y`: The y-coordinate of the initial position.
     * - `heading`: The directional orientation (in degrees) of the scenario's start point.
     * <p>
     * The `startPosition` is an essential component of the scenario data, providing
     * the reference point for interpreting subsequent movements or operations in the scenario.
     */
    @JacksonXmlProperty(localName = "StartPosition")
    StartPosition startPosition;
    /**
     * Represents a collection of {@link Point} objects that together form a trace path.
     * <p>
     * This variable is a part of a {@code Scenario} and is used to define the spatial
     * representation or movement trajectory within a scenario in a two-dimensional space.
     * <p>
     * Each element of the list is a {@link Point} object, containing x and y coordinates
     * along with a unique number identifier. The trace path is utilized to represent a series
     * of connected points, often corresponding to movement patterns or other coordinate-based paths.
     * <p>
     * This variable is annotated with {@link JacksonXmlProperty} for XML serialization
     * and deserialization, where it appears in the generated XML with the local name "Trace".
     */
    @JacksonXmlProperty(localName = "Trace")
    ArrayList<Point> trace;
    /**
     * A list of objects with spatial and geometric properties within a scenario.
     * <p>
     * This variable is used to store a collection of objects, defined by their positions, dimensions,
     * and other related attributes, as described by the Object class. It is a core component of the
     * Scenario structure, representing multiple objects involved in the scenario.
     * <p>
     * Each object in the list contains information about coordinates, height, and identifiers,
     * enabling its use in spatial and geometric modeling or analysis.
     * <p>
     * Serialized as an XML element with the local name "Objects" using Jackson XML annotations.
     */
    @JacksonXmlProperty(localName = "Objects")
    ArrayList<Object> objects;
}
