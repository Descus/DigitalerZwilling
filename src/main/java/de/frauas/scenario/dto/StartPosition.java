package de.frauas.scenario.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class StartPosition {
    @JacksonXmlProperty(localName = "xPosition")
    int x;
    @JacksonXmlProperty(localName = "yPosition")
    int y;
    @JacksonXmlProperty
    int heading;
}
