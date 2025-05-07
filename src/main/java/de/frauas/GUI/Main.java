package de.frauas.GUI;

import de.frauas.GUI.objects.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200,800);

        AxisPanel axisPanel = new AxisPanel();
        axisPanel.addPoint(new  Point(200,370));
        axisPanel.addPoint(new Point(894,256));
        frame.add(axisPanel);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
