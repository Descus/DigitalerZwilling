package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Line2F;
import de.frauas.scenario.primitives.Vec2F;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RoadTrace implements Drawable {
    protected final List<Vec2F> points = new ArrayList<>();
    protected final List<Line2F> lines = new ArrayList<>();

    public void addPoint(Vec2F point) {
        points.add(point);
        createLines();
    }

    public void removePoint(Vec2F point) {
        points.remove(point);
        createLines();
    }

    public List<Vec2F> getPoints() {
        return new ArrayList<>(points);
    }

    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (Line2F line : lines) {
            line.draw(g2d, scale, deltaTime);
        }
        g2d.dispose();
    }

    public void createLines() {
        lines.clear();

        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line2F(points.get(i), points.get(i + 1)));
        }
    }
}


//    private void createBezierLines() {
//
//        if (points.size() < 4) {
//            for (int i = 0; i < points.size() - 1; i++) {
//                lines.add(new Line2F(points.get(i), points.get(i + 1)));
//            }
//            return;
//        }
//
//        int sample = 4;
//        List<Vec2F> interpolatedPoints = CatmullRom(points);
//
//        for (int i = 0; i < interpolatedPoints.size() - 1; i++) {
//            lines.add(new Line2F(interpolatedPoints.get(i), interpolatedPoints.get(i + 1)));
//        }
//    }
//
//    private List<Vec2F> CatmullRom(List<Vec2F> originalPoints) {
//        List<Vec2F> newPoints = new ArrayList<>();
//
//        for (int i = 1; i < originalPoints.size() - 2; i++) {
//            Vec2F p0 = originalPoints.get(i - 1);
//            Vec2F p1 = originalPoints.get(i);
//            Vec2F p2 = originalPoints.get(i + 1);
//            Vec2F p3 = originalPoints.get(i + 2);
//
//            for (int j = 0; j <= ROAD_TRACE_BEZIER; j++) {
//                float t = j / (float) ROAD_TRACE_BEZIER;
//                float t2 = t * t;
//                float t3 = t2 * t;
//
//                float x = 0.5f * ((2 * p1.x())
//                        + (-p0.x() + p2.x()) * t
//                        + (2 * p0.x() - 5 * p1.x() + 4 * p2.x() - p3.x()) * t2
//                        + (-p0.x() + 3 * p1.x() - 3 * p2.x() + p3.x()) * t3);
//
//                float y = 0.5f * ((2 * p1.y())
//                        + (-p0.y() + p2.y()) * t
//                        + (2 * p0.y() - 5 * p1.y() + 4 * p2.y() - p3.y()) * t2
//                        + (-p0.y() + 3 * p1.y() - 3 * p2.y() + p3.y()) * t3);
//
//                newPoints.add(new Vec2F(x, y));
//            }
//        }
//        return newPoints;
//    }
//}