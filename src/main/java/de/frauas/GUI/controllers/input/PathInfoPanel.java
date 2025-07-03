package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PathInfoPanel extends JPanel{
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public PathInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setLayout(new BorderLayout(10,10));

        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init();
    }

    private void init() {
        listModel.clear();
        List<Vec3D> points = axisPanel.getScene().getTrace().getPoints(); //TODO Let Scene return info about the trace
        for (int i = 0; i < points.size()-1; i++) {
            Vec3D point = points.get(i);
            Vec3D nextPoint = points.get(i+1);

            double distance = nextPoint.subtract(point).length();
            String info = String.format(
                    "Point %d (%.1f,%.1f) - Point %d (%.1f,%.1f) : %.2f mm",
                    i, point.getX(), point.getY(), i+1, nextPoint.getX(), nextPoint.getY(), distance
            );
            listModel.addElement(info);
        }
    }
}
