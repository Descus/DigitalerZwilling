package de.frauas.scenario.xml.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class StartPosition {
    @JacksonXmlProperty
    int xPosition;
    @JacksonXmlProperty
    int yPosition;
    @JacksonXmlProperty
    int heading;
}
