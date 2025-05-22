package de.frauas.scenario.xml.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class Point {
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    @JacksonXmlProperty
    int xPosition;
    @JacksonXmlProperty
    int yPosition;
}
