package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PathInfoPanel extends JPanel{
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public PathInfoPanel(SimulationModel model) {

        setLayout(new BorderLayout(10,10));

        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init(model);
    }

    private void init(SimulationModel model) {
        listModel.clear();
        List<Vec3D> points = model.getScene().getTrace().getPoints(); 
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
