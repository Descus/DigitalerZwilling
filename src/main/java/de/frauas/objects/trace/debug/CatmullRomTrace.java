package de.frauas.objects.trace.debug;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;

import java.util.ArrayList;

import static de.frauas.Settings.SPLINE_INTERPOLATION_SIZE;

public class CatmullRomTrace extends RoadTrace {
    
    public CatmullRomTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        createLines();
    }

    public CatmullRomTrace(Scene parent) {
        super(parent);
        createLines();
    }

    @Override
    public void createLines() {
        lines.clear();

        if (points.size() < 2) {
            return;
        }

        Vec3D firstControl = points.get(1).reflect(points.getFirst());
        Vec3D lastControl = points.get(points.size()-2).reflect(points.getLast());

        for (int i = 0; i <= points.size() - 2; i++) {
            //Catmull Rom Algorithm needs 2 points
            // in addition it needs the point before and after the distance of 2 points
            Vec3D p0 = i > 0 ? points.get(i - 1) : firstControl;
            Vec3D p1 = points.get(i);
            Vec3D p2 =  points.get(i + 1);
            Vec3D p3 = i + 2 < points.size() ? points.get(i + 2) : lastControl;

            Vec3D prevPoint = p1;

            for (int j = 1; j <= SPLINE_INTERPOLATION_SIZE; j++) {
                double t = j / (double) SPLINE_INTERPOLATION_SIZE;
                double t2 = t * t;
                double t3 = t2 * t;

                // Catmull-Rom formula for x coordinate
                double x = 0.5f * ((2 * p1.getX())
                        + (-p0.getX() + p2.getX()) * t
                        + (2 * p0.getX() - 5 * p1.getX() + 4 * p2.getX() - p3.getX()) * t2
                        + (-p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX()) * t3);

                // Catmull-Rom formula for y coordinate
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