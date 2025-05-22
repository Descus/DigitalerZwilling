package de.frauas.GUI.controllers;

import de.frauas.objects.datastructures.Vec2D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PathInfoPanel extends JPanel{
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public PathInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(10,10));

        // Title
        JLabel title = new JLabel("Path Info");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init();
    }

    private void init() {
        listModel.clear();
        List<Vec2D> points = axisPanel.getRoadTrace().getPoints();
        System.out.println(points.size());
        for (int i = 0; i < points.size()-1; i++) {
            Vec2D point = points.get(i);
            Vec2D nextPoint = points.get(i+1);

            double distance = Math.sqrt(Math.pow(point.getX() - nextPoint.getX(), 2) + Math.pow(point.getX() - nextPoint.getY(), 2));
            String info = String.format(
                    "Point %d (%.1f,%.1f) - Point %d (%.1f,%.1f) : %.2f mm",
                    i, point.getX(), point.getY(), i+1, nextPoint.getX(), nextPoint.getY(), distance
            );
            listModel.addElement(info);
        }

    }
}
