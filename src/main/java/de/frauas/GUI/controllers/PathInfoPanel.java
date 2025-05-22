package de.frauas.GUI.controllers;

import de.frauas.GUI.objects.AxisPanel;
import de.frauas.GUI.objects.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class PathInfoPanel extends JPanel{
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(listModel);

    public PathInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(10,10));

        // Title
        JLabel title = new JLabel("Path Info");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // List in center
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init();
    }

    private void init() {
        listModel.clear();
        List<Point2D.Double> points = axisPanel.getPoints();
        System.out.println(points.size());;
        for (int i = 0; i < points.size()-1; i++) {
            Point2D.Double point = points.get(i);
            Point2D.Double nextPoint = points.get(i+1);

            double distance = Math.sqrt(Math.pow(point.x - nextPoint.x, 2) + Math.pow(point.y - nextPoint.y, 2));
            String info = String.format(
                    "Point %d (%.1f,%.1f) - Point %d (%.1f,%.1f) : %.2f mm",
                    i, point.x, point.y, i+1, nextPoint.x, nextPoint.y, distance
            );
            listModel.addElement(info);
        }

    }
}
