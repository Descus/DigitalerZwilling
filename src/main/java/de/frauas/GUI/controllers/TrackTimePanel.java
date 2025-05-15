package de.frauas.GUI.controllers;

import javax.swing.*;
import java.awt.*;

public class TrackTimePanel extends JPanel{
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(model);

    public TrackTimePanel() {
        setLayout(new BorderLayout(5,5));
        setBorder(BorderFactory.createTitledBorder("Arrival Times"));
        add(new JLabel("Point → Arrival", SwingConstants.CENTER),
                BorderLayout.NORTH);
        add(new JScrollPane(infoList), BorderLayout.CENTER);
        model.addElement("[Example] P1 , time, delta, distance");
    }
}
