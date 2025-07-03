package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class Point {
    @JacksonXmlProperty(isAttribute = true, localName = "Number")
    int number;
    @JacksonXmlProperty(localName = "xPosition")
    int x;
    @JacksonXmlProperty(localName = "yPosition")
    int y;
}
