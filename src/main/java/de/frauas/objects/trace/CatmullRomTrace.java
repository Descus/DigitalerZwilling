package de.frauas.objects.trace;

import de.frauas.objects.datastructures.Line2D;
import de.frauas.objects.datastructures.Vec2D;

import java.util.ArrayList;

import static de.frauas.Settings.SPLINE_INTERPOLATION_SIZE;

public class CatmullRomTrace extends RoadTrace {
    
    public CatmullRomTrace(ArrayList<Vec2D> points) {
        super(points);
        createLines();
    }

    public CatmullRomTrace() {
        super();
        createLines();
    }

    @Override
    public void createLines() {
        lines.clear();

        if (points.size() < 2) {
            return;
        }

        Vec2D firstControl = points.get(1).reflect(points.getFirst());
        Vec2D lastControl = points.get(points.size()-2).reflect(points.getLast());

        for (int i = 0; i <= points.size() - 2; i++) {
            //Catmull Rom Algorithm needs 2 points
            // in addition it needs the point before and after the distance of 2 points
            Vec2D p0 = i > 0 ? points.get(i - 1) : firstControl;
            Vec2D p1 = points.get(i);
            Vec2D p2 =  points.get(i + 1);
            Vec2D p3 = i + 2 < points.size() ? points.get(i + 2) : lastControl;

            Vec2D prevPoint = p1;

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

                Vec2D point = new Vec2D(x, y);
                lines.add(new Line2D(prevPoint, point));
                prevPoint = point;
            }
        }
    }
}