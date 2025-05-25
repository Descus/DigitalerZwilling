package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Line2F;
import de.frauas.scenario.primitives.Vec2F;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class RoadTrace implements Drawable {
    protected final List<Vec2F> points = new ArrayList<>();
    protected final List<Line2F> lines = new ArrayList<>();
    protected final List<Line2F> upperLines = new ArrayList<>();
    protected final List<Line2F> lowerLines = new ArrayList<>();

    public void addPoint(Vec2F point) {
        points.add(point);
        createLines();
        createUpperLines();
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
        Graphics2D g2dUpperLine = (Graphics2D) g2d.create();
        Graphics2D g2dLowerLine = (Graphics2D) g2d.create();
        for (Line2F line : lines) {
            line.draw(g2d, scale, deltaTime);
        }
        for (Line2F upperLine : upperLines) {
            upperLine.draw(g2dUpperLine, scale, deltaTime);
        }
        for (Line2F lowerLine : lowerLines) {
            lowerLine.draw(g2dLowerLine, scale, deltaTime);
        }
        g2d.dispose();
        g2dUpperLine.dispose();
        g2dLowerLine.dispose();

    }

    public void createLines() {
        lines.clear();

        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line2F(points.get(i), points.get(i + 1)));
        }
    }
    public void createUpperLines() {
        upperLines.clear();
        lowerLines.clear(); // Falls du auch eine untere Linie erstellen möchtest

        float offset = 10.0f; // Verschiebungslänge in Einheiten oder Pixeln

        for (int i = 0; i < points.size() - 1; i++) {
            Vec2F a = points.get(i);
            Vec2F b = points.get(i + 1);


            float dx = b.x() - a.x();
            float dy = b.y() - a.y();


            float normalX = -dy;
            float normalY = dx;


            float length = (float) Math.sqrt(normalX * normalX + normalY * normalY);
            float unitX = normalX / length;
            float unitY = normalY / length;


            Vec2F shiftedA = new Vec2F(a.x() + unitX * offset, a.y() + unitY * offset);
            Vec2F shiftedB = new Vec2F(b.x() + unitX * offset, b.y() + unitY * offset);


            upperLines.add(new Line2F(shiftedA, shiftedB));


//            Vec2F shiftedALower = new Vec2F(a.x() - unitX * offset, a.y() - unitY * offset);
//            Vec2F shiftedBLower = new Vec2F(b.x() - unitX * offset, b.y() - unitY * offset);
//            lowerLines.add(new Line2F(shiftedALower, shiftedBLower));
        }
    }



//        for (int i = 0; i < points.size() - 1; i++) {
//            Vec2F a = points.get(i);
//            Vec2F b = points.get(i + 1);
//
//            Vec2F distance = b.subtract(a);
//
//            Vec2F normal = new Vec2F(-distance.y(), distance.x());
//
//            float len = (float) Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y());
//            Vec2F unitNormal = new Vec2F(normal.x() / len, normal.y() / len);
//            Vec2F shift = new Vec2F(unitNormal.x() * offsetLower, unitNormal.y() * offsetLower);
//
//            Vec2F shiftedA = a.add(shift);
//            Vec2F shiftedB = b.add(shift);
//
//            lowerLines.add(new Line2F(shiftedA, shiftedB));
//        }
    }
