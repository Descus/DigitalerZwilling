package de.frauas.objects.trace.debug;

import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;

import java.util.ArrayList;

/**
 * Represents a debug trace created using Catmull-Rom spline interpolation.
 * This class generates a smooth curve that passes through a given set of control points,
 * making it useful for visualizing complex paths for debugging purposes.
 * <p>
 * @author Scenario
 */
public class CatmullRomTrace extends RoadTrace {

    /**
     * Constructs a CatmullRomTrace with a parent scene and an initial list of control points.
     * The spline curve is generated immediately upon construction.
     * <p>
     * @param parent The scene this trace belongs to.
     * @param points The list of control points used to generate the spline.
     */
    public CatmullRomTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        createLines();
    }

    /**
     * Constructs an empty CatmullRomTrace with a parent scene.
     * Points can be added later, and {@link #createLines()} must be called to generate the trace.
     * <p>
     * @param parent The scene this trace belongs to.
     */
    public CatmullRomTrace(Scene parent) {
        super(parent);
        createLines();
    }

    /**
     * Generates the line segments that form the Catmull-Rom spline.
     * The method clears any existing lines and then interpolates a smooth curve through the control points.
     * <p>
     * The algorithm requires four points to define each segment of the curve (p0, p1, p2, p3).
     * To handle the start and end of the spline, it creates "virtual" control points by reflecting
     * the second point over the first, and the second-to-last point over the last.
     * <p>
     * For each segment between two consecutive points, it calculates a series of intermediate points
     * using the Catmull-Rom formula. The number of intermediate points is determined by
     * {@link Settings.SCENE.TRACE#SPLINE_INTERPOLATION_STEPS}. These points are then connected
     * to form the final, smooth trace.
     */
    @Override
    public void createLines() {
        lines.clear();

        if (points.size() < 2) {
            return;
        }

        Vec3D firstControl = points.get(1).reflect(points.getFirst());
        Vec3D lastControl = points.get(points.size() - 2).reflect(points.getLast());

        for (int i = 0; i <= points.size() - 2; i++) {
            Vec3D p0 = i > 0 ? points.get(i - 1) : firstControl;
            Vec3D p1 = points.get(i);
            Vec3D p2 = points.get(i + 1);
            Vec3D p3 = i + 2 < points.size() ? points.get(i + 2) : lastControl;

            Vec3D prevPoint = p1;

            for (int j = 1; j <= Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS; j++) {
                double t = j / (double) Settings.SCENE.TRACE.SPLINE_INTERPOLATION_STEPS;
                double t2 = t * t;
                double t3 = t2 * t;
                
                double x = 0.5f * ((2 * p1.getX())
                        + (-p0.getX() + p2.getX()) * t
                        + (2 * p0.getX() - 5 * p1.getX() + 4 * p2.getX() - p3.getX()) * t2
                        + (-p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX()) * t3);

                double y = 0.5f * ((2 * p1.getY())
                        + (-p0.getY() + p2.getY()) * t
                        + (2 * p0.getY() - 5 * p1.getY() + 4 * p2.getY() - p3.getY()) * t2
                        + (-p0.getY() + 3 * p1.getY() - 3 * p2.getY() + p3.getY()) * t3);

                Vec3D point = new Vec3D(x, y, 1);
                lines.add(new Line3D(this, prevPoint, point));
                prevPoint = point;
            }
        }
    }
}