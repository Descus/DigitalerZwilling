package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * {@code PathInfoPanel} is a GUI component that displays pairwise segment distances
 * between consecutive points on the car's path (trace).
 * It helps visualize and understand the route the vehicle will take.
 *
 * @author GUI-Group
 */
public class PathInfoPanel extends TitledRoundedPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    /**
     * Constructs a new PathInfoPanel and initializes it using data
     * from the provided SimulationModel.
     *
     * @param model the simulation model containing the path (trace) data
     */
    public PathInfoPanel(SimulationModel model) {
        super("Path Information", Color.BLACK);

        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init(model);
    }

    /**
     * Populates the list with distances between each consecutive pair of trace points
     * from the simulation model. Each entry shows two points and the distance between them.
     *
     * @param model the simulation model providing the trace (path) data
     */
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
