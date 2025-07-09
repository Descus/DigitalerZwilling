package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.Settings;
import javax.swing.*;
import java.awt.*;

public class CarInfoPanel extends JPanel{
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public CarInfoPanel(SimulationModel model) {
        setOpaque(false);
        setLayout(new BorderLayout(5, 5));


        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init(model);
    }

    private void init(SimulationModel model) {
        listModel.clear();
        String carLength = String.format(
                "Car length: %.2f mm" , Settings.CAR.SIZE.getY()
        );
        listModel.addElement(carLength);
        String carWidth = String.format(
                "Car width: %.2f mm", Settings.CAR.SIZE.getX()
        );
        listModel.addElement(carWidth);
        String carPos = String.format(
                "Car length: %s mm" , model.getScene().getStartPosition().toString()
        );
        listModel.addElement(carPos);
        String carRad = String.format(
                "Car initial heading to %s degree ", model.getScene().getStartHeading()
        );
        listModel.addElement(carRad);
    }

}
