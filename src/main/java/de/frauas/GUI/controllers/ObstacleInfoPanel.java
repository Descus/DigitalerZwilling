package de.frauas.GUI.controllers;

import de.frauas.objects.Obstacle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;


public class ObstacleInfoPanel extends JPanel {
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> infoList = new JList<>(listModel);

    public ObstacleInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(5, 5));

        // Title
        JLabel title = new JLabel("Obstacles Info");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // List in center
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init();
    }

    private void init() {
        listModel.clear();
        List<Obstacle> obstacles = axisPanel.getObstacles();
        for (Obstacle o : obstacles) {
            Point2D.Double startPoint = o.getStartPoint();
            Point2D.Double endPoint = o.getEndPoint();
            String info = String.format(
                    "Start=(%.1f, %.1f), End=(%.1f, %.1f), Height=%d mm",
                    startPoint.getX(), startPoint.getY(),
                    endPoint.getX(),   endPoint.getY(),
                    o.getHeight()
            );
            listModel.addElement(info);
        }
    }
}
