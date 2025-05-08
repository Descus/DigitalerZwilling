package de.frauas.GUI.objects;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.ArrayList;

public class AxisPanel extends JPanel {
    private static int MARGIN = 60;
    private static int LABEL_MARGIN = 25;
    private static int TICK_Size = 5;
    private static int NUM_X_TICKS = 20;
    private static int NUM_Y_TICKS = 10;

    // Data range in millimeters
    private static double X_MAX = 1000;
    private static double Y_MAX = 500;

    // points in data coords (mm):
    private final List<Point2D.Double> points = new ArrayList<>();
    private Obstacle obstacle;
    private static final int POINT_RADIUS = 10;


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Define the dimension of the frame
        int width = getWidth();
        int height = getHeight();

        // Uniform scale between X and Y
        double drawableWidth = width - 2 * MARGIN;
        double drawableHeight = height - 2 * MARGIN;
        double scale = Math.min(drawableWidth / X_MAX, drawableHeight / Y_MAX);

        // Define drawing corners in coords
        int x0 = MARGIN;
        int y0 = height - MARGIN;
        // Endpoint of each axis depend on the scale
        int x1 = x0 + (int)( X_MAX* scale);
        int y1 = y0 - (int)(Y_MAX * scale);

        // Draw axes
        g2.drawLine(x0, y0, x1, y0); // X-axis
        g2.drawLine(x0, y0, x0, y1); // Y-axis
        // Draw support axes
        g2.drawLine(x1, y0, x1, y1); // support Y-axis
        g2.drawLine(x0, y1, x1, y1); // support X-axis

        // X-axis ticks & labels
        for (int i = 0; i <= NUM_X_TICKS; i++) {
            double value = i * ( X_MAX/ NUM_X_TICKS);
            int x = x0 + (int)(value * scale);

            // tick mark
            if (i != 0 && i != NUM_X_TICKS) {
                g2.drawLine(x, y0 , x, y0 + TICK_Size);
            }

            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x - labelWidth/2 , y0 + LABEL_MARGIN);
        }

        // Y-axis ticks & labels
        for (int i = 0; i <= NUM_Y_TICKS; i++) {
            double value = i * (Y_MAX / NUM_Y_TICKS);
            int y = y0 - (int)(value * scale);

            // tick mark
            if (i != 0 && i != NUM_Y_TICKS) {
                g2.drawLine(x0 - TICK_Size, y, x0 , y);
            }

            // label
            String label = String.format("%.0f", value);
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x0 - LABEL_MARGIN - labelWidth, y + labelWidth/2);
        }

        // draw points
        g2.setColor(Color.BLACK);
        for (Point2D.Double p : points) {
            int px = x0 + (int)(p.x * scale);
            int py = y0 - (int)(p.y * scale);
            g2.fillOval(
                    px - POINT_RADIUS/2,
                    py - POINT_RADIUS/2,
                    POINT_RADIUS,
                    POINT_RADIUS
            );
        }

        // draw lines between consecutive points
        g2.setColor(Color.RED);
        if (points.size() >= 2) {
            for (int i = 1; i < points.size(); i++) {
                Point2D.Double p0 = points.get(i - 1);
                Point2D.Double p1 = points.get(i);
                int x0p = x0 + (int) (p0.x * scale);
                int y0p = y0 - (int) (p0.y * scale);
                int x1p = x0 + (int) (p1.x * scale);
                int y1p = y0 - (int) (p1.y * scale);
                g2.drawLine(x0p, y0p, x1p, y1p);
            }
        }

        // draw Obstacle
        g2.setColor(Color.BLACK);
        int xs= x0 + (int) (200 *scale);
        int ys= y0 - (int) (50*scale);
        int xe= x0 +(int) (400*scale);
        int ye= y0- (int) (100*scale);
        g2.drawLine(xs,ys,xs,ye);
        g2.drawLine(xs,ye,xe,ye);




        g2.dispose();
    }

    // Add a point
    public void addPoint(Point point) {
        points.add(new Point2D.Double(point.x, point.y));
        repaint();
    }


}
