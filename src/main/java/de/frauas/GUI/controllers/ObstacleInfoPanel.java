package de.frauas.GUI.controllers;

import de.frauas.objects.Obstacle;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ObstacleInfoPanel extends JPanel {
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public ObstacleInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(5, 5));

        // Title
        JLabel title = new JLabel("Obstacles Info");
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
        List<Obstacle> obstacles = axisPanel.getObstacles(); //TODO Let Scene return info about all obstacles 
        for (Obstacle o : obstacles) {
            Vec3D startPoint = o.getStartPoint();
            Vec3D endPoint = o.getEndPoint();
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
