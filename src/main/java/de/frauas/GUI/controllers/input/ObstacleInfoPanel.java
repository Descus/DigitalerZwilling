package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.TitledRoundedPanel;
import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.objects.obstacle.Obstacle;
import de.frauas.objects.datastructures.Vec3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * ObstacleInfoPanel is a GUI component that displays detailed information
 * about all obstacles present in the simulation scene. It shows start and end coordinates
 * as well as the height of each obstacle.
 *
 * @author GUI-Group
 */
public class ObstacleInfoPanel extends TitledRoundedPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    /**
     * Constructs a new ObstacleInfoPanel using the provided simulation model.
     * Retrieves and displays obstacle information from the current scene.
     *
     * @param model the simulation model containing the scene and obstacle data
     */
    public ObstacleInfoPanel(SimulationModel model) {
        super("Obstacle Information", Color.BLACK);

        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init(model);
    }

    /**
     * Populates the list with obstacle information including start and end positions,
     * and obstacle height, extracted from the SimulationModel's scene.
     *
     * @param model the simulation model providing access to the obstacle list
     */
    private void init(SimulationModel model) {
        listModel.clear();
        List<Obstacle> obstacles = model.getScene().getObstacles();
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
