package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class Object {
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    @JacksonXmlProperty(localName = "xPositionStart")
    int xStart;
    @JacksonXmlProperty(localName = "yPositionStart")
    int yStart;
    @JacksonXmlProperty(localName = "xPositionEnd")
    int xEnd;
    @JacksonXmlProperty(localName = "yPositionEnd")
    int yEnd;
    @JacksonXmlProperty
    int height;
}
