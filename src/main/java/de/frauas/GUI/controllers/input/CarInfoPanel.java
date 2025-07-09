package de.frauas.GUI.controllers.input;

import de.frauas.GUI.controllers.AxisPanel;
import de.frauas.Settings;
import de.frauas.objects.car.Car;
import javax.swing.*;
import java.awt.*;

public class CarInfoPanel extends JPanel{
    private final AxisPanel axisPanel;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public CarInfoPanel(AxisPanel axisPanel) {
        this.axisPanel = axisPanel;
        setOpaque(false);
        setLayout(new BorderLayout(5, 5));


        // List in center
        JList<String> infoList = new JList<>(listModel);
        add(new JScrollPane(infoList), BorderLayout.CENTER);

        // Initial population
        init();
    }

    private void init() {
        listModel.clear();
        Car car = axisPanel.getScene().getCar();
        String carLength = String.format(
                "Car length: %.2f mm" , Settings.CAR.SIZE.getY()
        );
        listModel.addElement(carLength);
        String carWidth = String.format(
                "Car width: %.2f mm", Settings.CAR.SIZE.getX()
        );
        listModel.addElement(carWidth);

        String carRad = String.format(
                "Car initial heading to %s degree ", car.getTransform().getRotation()
        );
        listModel.addElement(carRad);
    }

}
