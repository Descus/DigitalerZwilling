package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Line2F;
import de.frauas.scenario.primitives.Vec2F;
import static de.frauas.Settings.ROAD_TRACE_BEZIER;

import java.util.ArrayList;
import java.util.List;

public class BezierRoadTrace extends RoadTrace implements Drawable {
    private List<Vec2F> CatmullRom(List<Vec2F> originalPoints) {
        List<Vec2F> newPoints = new ArrayList<>();

        for (int i = 1; i < originalPoints.size() - 2; i++) {
            //Catmull Rom Algorithm needs 2 points
            // in addition it needs the point before and after the distance of 2 points
            Vec2F p0 = originalPoints.get(i - 1);
            Vec2F p1 = originalPoints.get(i);
            Vec2F p2 = originalPoints.get(i + 1);
            Vec2F p3 = originalPoints.get(i + 2);

            for (int j = 0; j <= ROAD_TRACE_BEZIER; j++) {
                // t defines the position of the trace
                float t = j / (float) ROAD_TRACE_BEZIER;
                // t2 defines the curvature of the trace
                float t2 = t * t;
                // t3 defines the softness of the trace
                float t3 = t2 * t;

                // Catmull-Rom formula for x coordinate
                float x = 0.5f * ((2 * p1.x())
                        + (-p0.x() + p2.x()) * t
                        + (2 * p0.x() - 5 * p1.x() + 4 * p2.x() - p3.x()) * t2
                        + (-p0.x() + 3 * p1.x() - 3 * p2.x() + p3.x()) * t3);

                // Catmull-Rom formula for y coordinate
                float y = 0.5f * ((2 * p1.y())
                        + (-p0.y() + p2.y()) * t
                        + (2 * p0.y() - 5 * p1.y() + 4 * p2.y() - p3.y()) * t2
                        + (-p0.y() + 3 * p1.y() - 3 * p2.y() + p3.y()) * t3);

                newPoints.add(new Vec2F(x, y));
            }
        }
        newPoints.add(originalPoints.getLast());
        return newPoints;
    }

    @Override
    public void createLines() {
        lines.clear();
        lowerLines.clear();
        upperLines.clear();

        List<Vec2F> interpolatedPoints = CatmullRom(points);
        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
            lines.add(new Line2F(interpolatedPoints.get(i), interpolatedPoints.get(i + 1)));
            upperLines.add(new Line2F(interpolatedPoints.get(i), interpolatedPoints.get(i + 1)));
            lowerLines.add(new Line2F(interpolatedPoints.get(i), interpolatedPoints.get(i + 1)));
        }
    }

    public List<Line2F> getLines() {
        return this.lines;
    }
}
