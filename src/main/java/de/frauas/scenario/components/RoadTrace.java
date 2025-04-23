package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Line2;
import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.primitives.Vec2F;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoadTrace implements Drawable{
    private final List<Vec2> points = new ArrayList<>();
    private final List<Line2> lines = new ArrayList<>();
    
    public void addPoint(Vec2 point) {
        points.add(point);
        createLines();
    }
    
    public void removePoint(Vec2 point) {
        points.remove(point);
        createLines();
    }
    
    public List<Vec2> GetPoints() {
        return new ArrayList<>(points);
    }
    
    @Override
    public void draw(Graphics2D g, Vec2F scale, float deltaTime) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (Line2 line : lines) {
            line.draw(g2d, scale, deltaTime);
        }
        g2d.dispose();
    }
    
    private void createLines(){
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line2(points.get(i), points.get(i + 1)));
        }
    }
}
