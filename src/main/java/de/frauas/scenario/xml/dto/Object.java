package de.frauas.scenario.xml.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Object {
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    @JacksonXmlProperty
    int xPositionStart;
    @JacksonXmlProperty
    int yPositionStart;
    @JacksonXmlProperty
    int xPositionEnd;
    @JacksonXmlProperty
    int yPositionEnd;
    @JacksonXmlProperty
    int height;
}
