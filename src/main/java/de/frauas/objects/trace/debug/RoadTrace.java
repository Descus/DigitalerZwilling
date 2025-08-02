package de.frauas.objects.trace.debug;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.trace.Trace;
import de.frauas.objects.trace.TraceType;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple trace made of straight line segments connecting a series of points.
 * This class is intended for debugging purposes, providing a visual representation of a path.
 */
@Getter
public class RoadTrace extends Trace {

    /**
     * The list of 3D line segments that make up the trace.
     */
    protected final List<Line3D> lines = new ArrayList<>();

    /**
     * Constructs a RoadTrace with a parent scene and an initial list of points.
     * The lines are created immediately upon construction.
     *
     * @param parent The scene this trace belongs to.
     * @param points The initial list of points defining the trace.
     */
    public RoadTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        createLines();
    }

    /**
     * Constructs an empty RoadTrace with a parent scene.
     *
     * @param parent The scene this trace belongs to.
     */
    public RoadTrace(Scene parent) {
        super(parent);
        createLines();
    }

    /**
     * Returns the type of this trace, which is always {@link TraceType#DEBUG}.
     *
     * @return The debug trace type.
     */
    @Override
    public TraceType getType() {
        return TraceType.DEBUG;
    }

    /**
     * Draws all the line segments of the trace on the given Graphics context.
     *
     * @param g The Graphics context on which to draw the lines.
     */
    @Override
    public void drawLines(Graphics g){
        lines.forEach(l -> l.draw(g));
    }

    /**
     * Creates the straight line segments that connect the points of the trace.
     * If there are existing lines, they are cleared first.
     */
    @Override
    public void createLines(){
        lines.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line3D(this, points.get(i), points.get(i + 1)));
        }
    }
}