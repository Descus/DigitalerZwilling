package de.frauas.objects;

import de.frauas.Settings;
import de.frauas.objects.datastructures.Vec2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static de.frauas.Settings.CATMULL_ROM_INTERPOLATION_SIZE;

public class CatmullRom extends RoadTrace {

    public CatmullRom(List<Vec2D> points) {
        super(points);
    }

    @Override
    public void createLines(){
        List<Vec2D> calcPoints = new ArrayList<>();

        for (int i = 1; i < points.size() - 2; i++) {

            //Catmull Rom Algorithm needs 2 points
            // in addition it needs the point before and after the distance of 2 points
            Vec2D p0 = points.get(i - 1);
            Vec2D p1 = points.get(i);
            Vec2D p2 = points.get(i + 1);
            Vec2D p3 = points.get(i + 2);

            for (int j = 0; j < CATMULL_ROM_INTERPOLATION_SIZE; j++) {
                float t1 = j / (float) CATMULL_ROM_INTERPOLATION_SIZE;
                float t2 = t1 * t1;
                float t3 = t2 * t1;

                // Catmull-Rom formula for x coordinate
                double x = 0.5f * ((2 * p1.getX())
                        + (-p0.getX() + p2.getX()) * t1
                        + (2 * p0.getX() - 5 * p1.getX() + 4 * p2.getY() - p3.getX()) * t2
                        + (-p0.getX() + 3 * p1.getX() - 3 * p2.getX() + p3.getX()) * t3);

                // Catmull-Rom formula for y coordinate
                double y = 0.5f * ((2 * p1.getY())
                        + (-p0.getY() + p2.getY()) * t1
                        + (2 * p0.getY() - 5 * p1.getY() + 4 * p2.getY() - p3.getY()) * t2
                        + (-p0.getY() + 3 * p1.getY() - 3 * p2.getY() + p3.getY()) * t3);

                calcPoints.add(new Vec2D(x, y));
                calcPoints.add(calcPoints.getLast());
            }
        }
        //return calcPoints;
    }
}