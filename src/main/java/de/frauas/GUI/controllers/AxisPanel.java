package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.observer.SimulationObserver;
import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.car.CarStatus;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.scenario.dto.Scenario;
import lombok.Getter;


import javax.swing.*;
import java.awt.*;


public class AxisPanel extends JPanel implements SimulationObserver {
    @Getter
    private Scene scene;
    private long lastTime = System.currentTimeMillis();

    public AxisPanel(SimulationModel model, Scene scene){
        this.scene = scene;
        model.addObserver(this);
        setOpaque(false);
    }

    @Override
    public void onSimulationUpdate(){
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.BLUE); // oder eine andere Farbe deiner Wahl

        // Define the dimension of the frame
        // Uniform scale between X and Y
        // Padding around the drawable area so axis labels don’t get cut off
        int width = getWidth();
        int height = getHeight();
        int MARGIN = 60;
        int NUM_X_TICKS = 20;
        int NUM_Y_TICKS = 10;
        int LABEL_MARGIN = 25;
        int TICK_Size = 5;


        // Calculates scaling to convert real-world mm into pixels.
        // Data range in millimeters
        // Define the physical size of the virtual environment (in mm).
        double drawableWidth = width - 2 * MARGIN;
        double drawableHeight = height - 2 * MARGIN;
        double x_MAX = Settings.SCENE_CANVAS.getX();
        double y_MAX = Settings.SCENE_CANVAS.getY();
        double scale = Math.min(drawableWidth / x_MAX, drawableHeight / y_MAX);



        // Define drawing corners in coords
        int y0 = height - MARGIN;
        int x0 = MARGIN;
        int x1 = MARGIN + (int) (x_MAX * scale); // Endpoint of each axis depend on the scale
        int y1 = y0 - (int) (y_MAX * scale);

        scene.getTransform().setScale(new Vec3D(scale, scale, 1));

        // Draw axes
        g2.drawLine(x0, y0, x1, y0); // X-axis
        g2.drawLine(x0, y0, x0, y1); // Y-axis
        // Draw support axes
        g2.drawLine(x1, y0, x1, y1); // support Y-axis
        g2.drawLine(x0, y1, x1, y1); // support X-axis

        // X-axis ticks & labels
        // X axis with ticks
        for (int i = 0; i <= NUM_X_TICKS; i++) {
            double value = i * (x_MAX / NUM_X_TICKS);
            int x = x0 + (int) (value * scale);
            // tick mark
            if (i != 0 && i != NUM_X_TICKS) {
                g2.drawLine(x, y0, x, y0 + TICK_Size);
            }
            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x - labelWidth / 2, y0 + LABEL_MARGIN);
        }

        // Y-axis ticks & labels
        // Y axis with ticks
        for (int i = 0; i <= NUM_Y_TICKS; i++) {
            double value = i * (y_MAX / NUM_Y_TICKS);
            int y = y0 - (int) (value * scale);
            // tick mark
            if (i != 0 && i != NUM_Y_TICKS) {
                g2.drawLine(x0 - TICK_Size, y, MARGIN, y);
            }
            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x0 - LABEL_MARGIN - labelWidth, y + labelWidth / 2);
        }

            Graphics2D g3 = (Graphics2D) g2.create();
            {
                g3.translate(x0, y0);
                g3.scale(1, -1);
                scene.draw(g3);
            }
            g3.dispose();
        }
        g2.dispose();
    }

    public void populate(Scenario scenario) {
        scene = new Scene(scenario);

        timer = new Timer(200, _ -> {
            scene.update();
            repaint();
        });
        timer.start();
    }
}