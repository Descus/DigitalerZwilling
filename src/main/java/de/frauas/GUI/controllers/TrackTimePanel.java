package de.frauas.GUI.controllers;

import javax.swing.*;
import java.awt.*;

public class TrackTimePanel extends JPanel{
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(model);

    public TrackTimePanel() {
        setLayout(new BorderLayout(5,5));
        JLabel title = new JLabel("Track Time");
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(infoList), BorderLayout.CENTER);
        model.addElement("[Example] P1 , time, delta, distance");
    }
}
