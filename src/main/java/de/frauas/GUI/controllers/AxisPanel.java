package de.frauas.GUI.controllers;

import de.frauas.GUI.controllers.observer.SimulationModel;
import de.frauas.GUI.controllers.observer.ISimulationObserver;
import de.frauas.Settings;
import de.frauas.objects.datastructures.Vec3D;
import java.awt.*;

/**
 * AxisPanel is a  panel used to visualize a coordinate system (axes)
 * along with the current simulation scene. It reacts to simulation updates
 * and redraws the scene accordingly.
 * <p>
 * This panel extends TitledRoundedPanel and implements SimulationObserver
 * to enable real-time updates.
 */
public class AxisPanel extends TitledRoundedPanel implements ISimulationObserver {
    private final SimulationModel model;

    /**
     * Constructor: Initializes the panel according to the simulation model.
     * It registers itself as an observer to receive update notifications.
     * <p>
     * @param model the simulation model providing data and transformation settings
     */
    public AxisPanel(SimulationModel model){
        super("Axis Panel",Color.RED);
        this.model = model;
        model.addObserver(this);     //register as an observer for updating data
    }

    /**
     * when the simulation is update, triggering repainting the panel.
     */
    @Override
    public void onSimulationUpdate(){
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(Color.BLACK);   // Axis and tick color

        //Settings and constants
        int width = getWidth();
        int height = getHeight();
        int MARGIN = 60;            // Padding around axes
        int NUM_X_TICKS = 20;
        int NUM_Y_TICKS = 10;
        int LABEL_MARGIN = 25;      // Space for axis labels
        int TICK_SIZE = 5;

        // Calculates scaling to convert real-world mm into pixels.
        double drawableWidth = width - 2.0 * MARGIN;
        double drawableHeight = height - 2.0 * MARGIN;
        double x_MAX = Settings.SCENE.CANVAS.getX();
        double y_MAX = Settings.SCENE.CANVAS.getY();
        double scale = Math.min(drawableWidth / x_MAX, drawableHeight / y_MAX);

        // Define axis base positions in pixel coordinates
        int x0 = MARGIN;
        int y0 = height - MARGIN;
        int x1 = MARGIN + (int) (x_MAX * scale);
        int y1 = y0 - (int) (y_MAX * scale);

        // Store the calculated scale to the simulation's transform object
        model.getScene().getTransform().setScale(new Vec3D(scale, scale, 1));

        // Draw axes and border
        g2.drawLine(x0, y0, x1, y0); // X-axis
        g2.drawLine(x0, y0, x0, y1); // Y-axis
        g2.drawLine(x1, y0, x1, y1); // support Y-axis
        g2.drawLine(x0, y1, x1, y1); // support X-axis

        FontMetrics fm = g2.getFontMetrics();

        // X-axis ticks & labels
        for (int i = 0; i <= NUM_X_TICKS; i++) {
            double value = i * (x_MAX / NUM_X_TICKS);
            int x = x0 + (int) (value * scale);
            if (i != 0 && i != NUM_X_TICKS) {
                g2.drawLine(x, y0, x, y0 + TICK_SIZE);                      // tick mark
            }
            String label = String.format("%.0f", value);
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, x - labelWidth / 2, y0 + LABEL_MARGIN);   // label
        }

        // Y-axis ticks & labels
        for (int i = 0; i <= NUM_Y_TICKS; i++) {
            double value = i * (y_MAX / NUM_Y_TICKS);
            int y = y0 - (int) (value * scale);
            if (i != 0 && i != NUM_Y_TICKS) {
                g2.drawLine(x0 - TICK_SIZE, y, MARGIN, y);                  // tick mark
            }
            String label = String.format("%.0f", value);
            int labelHeight = fm.getHeight();
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, (int) (x0 - LABEL_MARGIN - labelWidth/1.5), y + labelHeight / 3);  // label
        }

        //draw the simulation scene
        Graphics2D g3 = (Graphics2D) g2.create();
        g3.translate(x0, y0);
        g3.scale(1, -1);
        model.getScene().draw(g3);
        g3.dispose();

        g2.dispose();
    }
}