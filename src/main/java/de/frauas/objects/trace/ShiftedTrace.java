package de.frauas.objects.trace;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.datastructures.Line3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Scenario-Group
 * Represents a trace defined by two parallel lines (upper and lower) shifted
 * from a central path of points. This class is used to create road-like
 * structures with a specific width. It calculates the geometry of these parallel
 * lines based on tangents and normals at each point of the central path.
 * It also provides methods for drawing the trace and checking if a point
 * lies within its boundaries.
 */

@Getter
public class ShiftedTrace extends Trace {

    //The list of line segments forming the upper boundary of the trace.
    public final List<Line3D> upperLine;
     //The list of line segments forming the lower boundary of the trace.
    public final List<Line3D> lowerLine;

    public ShiftedTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        upperLine = new ArrayList<>();
        lowerLine = new ArrayList<>();
        createLines();
    }

    public ShiftedTrace(Scene parent) {
        super(parent);
        upperLine = new ArrayList<>();
        lowerLine = new ArrayList<>();
        createLines();
    }


    /**
     * Generates the upper and lower line segments based on the central path points.
     * It calculates a normal for each point on the path and shifts points along this
     * normal to create the parallel boundaries.
     */
    @Override
    public void createLines() {
        upperLine.clear();
        lowerLine.clear();

        if (points.size() < 2) return;

        // Lists to hold the calculated points for the upper and lower trace boundaries.
        ArrayList<Vec3D> shiftedPointsUp = new ArrayList<>();
        ArrayList<Vec3D> shiftedPointsDown = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            Vec3D prev = i > 0 ? points.get(i - 1) : points.get(i);
            Vec3D next = i < points.size() - 1 ? points.get(i + 1) : points.get(i);


            // Calculate the tangent, which represents the direction of the path.
            Vec3D tangent = next.subtract(prev).normalize();

            // The normal is a vector perpendicular to the tangent.
            Vec3D normal = tangent.perpendicular();

            // Shift the original point along the normal for the upper and lower lines.
            Vec3D shiftedUp = points.get(i).add(normal.scale(Settings.SCENE.TRACE.LINE_WIDTH / 2));
            Vec3D shiftedDown = points.get(i).add(normal.scale(-Settings.SCENE.TRACE.LINE_WIDTH / 2));

            shiftedPointsUp.add(shiftedUp);
            shiftedPointsDown.add(shiftedDown);
        }

        // Create the line segments from the shifted points.
        for (int i = 0; i < shiftedPointsUp.size() - 1; i++) {
            upperLine.add(new Line3D(this, shiftedPointsUp.get(i), shiftedPointsUp.get(i + 1)));
            lowerLine.add(new Line3D(this, shiftedPointsDown.get(i), shiftedPointsDown.get(i + 1)));
        }
    }

    /**
     * @author Infrared-Team,
     * Checks if the given point lies between the upper, lower and left, right lines
     * of any segment in the trace.
     * These checks are performed using the method rightOfLine(), which evaluates
     * whether a point lies to the right of a directed line segment. If all conditions are met
     * for any segment, the point is considered to be within the trace bounds
     *
     * @param globalSpacePoint the point to check
     * @return true if the point is within the trace bounds, false otherwise
     */
    public boolean isPointBetweenLines(Vec3D globalSpacePoint){
        if (points.size() < 2) return false;

        Vec3D localSpacePoint = toLocalSpace(globalSpacePoint);

        for (int i = 0; i < upperLine.size(); i++) {
            Line3D upper = upperLine.get(i);
            Line3D lower = lowerLine.get(i);

            Line3D leftBound = new Line3D(this, lower.getEnd(), upper.getEnd());
            Line3D rightBound = new Line3D(this, lower.getStart(), upper.getStart());

            if (lower.rightOfLine(localSpacePoint)
                    && !upper.rightOfLine(localSpacePoint)
                    && leftBound.rightOfLine(localSpacePoint)
                    && !rightBound.rightOfLine(localSpacePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Draws the upper and lower lines of the trace on the given Graphics context.
     *
     * @param g The Graphics context on which to draw the lines.
     */

    @Override
    public void drawLines(Graphics g) {
        upperLine.forEach(l -> l.draw(g));
        lowerLine.forEach(l -> l.draw(g));
    }

    /**
    * Returns the type of this trace.
    *
    */
    @Override
        public TraceType getType() {
            return TraceType.WORKING;
        }
    }
