package de.frauas.objects.trace;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents an abstract trace defined by a sequence of points.
 * This class serves as a base for different types of traces (e.g., straight, curved)
 * and provides fundamental functionalities like storing points, calculating length,
 * and drawing the trace.
 *
 * @author Scenario-Group
 */
@Getter
public abstract class Trace extends Transformable implements IDrawable {

    protected final List<Vec3D> points = new ArrayList<>();
    protected double length = 0;

    /**
     * Constructs a Trace with a parent scene and an initial list of points.
     *
     * @param parent The scene this trace belongs to.
     * @param points The initial list of points defining the trace.
     */
    public Trace(Scene parent, List<Vec3D> points) {
        this.parent = parent;
        this.points.addAll(points);
        calculateLength();
    }

    /**
     * Constructs an empty Trace with a parent scene.
     *
     * @param parent The scene this trace belongs to.
     */
    public Trace(Scene parent) {
        this.parent = parent;
    }

    /**
     * Adds a new point to the trace and recalculates its geometry and length.
     *
     * @param point The point to add to the trace.
     */
    public void addPoint(Vec3D point) {
        points.add(point);
        createLines();
        calculateLength();
    }

    /**
     * Calculates the total length of the trace by summing the distances
     * between consecutive points.
     */
    private void calculateLength(){
        length = 0;
        for (int i = 0; i < points.size() - 2; i++) {
            length += points.get(i).subtract(points.get(i + 1)).length();
        }
    }

    /**
     * Gets the first point of the trace.
     */
    public Vec3D first(){
        return points.getFirst();
    }

    /**
     * Gets the last point of the trace.
     */
    public Vec3D last(){
        return points.getLast();
    }

    /**
     * Abstract method to be implemented by subclasses to create the geometric lines
     * that represent the trace based on its points.
     */
    protected abstract void createLines();

    /**
     * Abstract method for subclasses to implement the drawing of the trace's lines.
     *
     * @param g The Graphics context on which to draw.
     */
    public abstract void drawLines(Graphics g);

    /**
     * Abstract method to get the specific type of the trace.
     *
     * @return The {@link TraceType} of the trace.
     */
    public abstract TraceType getType();

    /**
     * Draws the individual points of the trace if debug mode is enabled.
     *
     * @param g The Graphics context on which to draw the points.
     */
    public void drawPoints(Graphics g) {
            points.forEach(p -> p.draw(g));
    }

    /**
     * Draws the entire trace, including its lines and, if enabled, its points.
     * It applies the trace's transformation to the Graphics context.
     *
     * @param g The Graphics context to draw on.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g2.setColor(Color.black);
            drawLines(g2);

            if (Settings.DEBUG.ENABLED) {
                Graphics2D g2Debug = (Graphics2D) g.create();
                g2Debug.setColor(Color.RED);
                drawPoints(g2Debug);
                g2Debug.dispose();
            }
        }
        g2.dispose();
    }
    
    @Override
    public void drawInScene(Graphics g) {
        
    }
}
