package de.frauas.scenario.xml.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@JacksonXmlRootElement(localName = "Scenario")
public class Scenario {
    @JacksonXmlProperty(isAttribute = true, localName = "Scenario_ID")
    String id;
    @JacksonXmlProperty(isAttribute = true, localName = "Date")
    String date;
    @JacksonXmlProperty(isAttribute = true, localName = "Version")
    String version;
    @JacksonXmlProperty(localName = "StartPosition")
    StartPosition startPosition;
    @JacksonXmlProperty(localName = "Trace")
    ArrayList<Point> trace;
    @JacksonXmlProperty(localName = "Objects")
    ArrayList<Object> objects;

}
