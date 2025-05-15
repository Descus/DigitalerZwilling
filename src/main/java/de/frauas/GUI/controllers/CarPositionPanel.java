package de.frauas.GUI.controllers;

import de.frauas.GUI.objects.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CarPositionPanel extends JPanel {
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(model);

    public CarPositionPanel(AxisPanel axisPanel, Car car) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Car Position"));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
        add(new JScrollPane(infoList),BorderLayout.CENTER);
        model.addElement("[Example] 03.040 - Moving - (300,530)");
    }
}