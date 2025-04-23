package de.frauas.scenario.components;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import de.frauas.scenario.primitives.Line2;
import de.frauas.scenario.primitives.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "Trace")
public class RoadTrace implements Drawable{
    private final List<Vec2<Integer>> points = new ArrayList<>();
    private final List<Line2<Integer>> lines = new ArrayList<>();
    
    public void add(Vec2<Integer> point) {
        points.add(point);
        createLines();
    }
    
    public void remove(Vec2<Integer> point) {
        points.remove(point);
        createLines();
    }
    
    public List<Vec2<Integer>> getPoints() {
        return new ArrayList<>(points);
    }
    
    @Override
    public void Draw(Graphics2D g, Vec2<Float> scale) {
        Graphics2D g2d = (Graphics2D) g.create();
        for (Line2<Integer> line : lines) {
            line.Draw(g2d, scale);
        }
        g2d.dispose();
    }
    
    private void createLines(){
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line2<>(points.get(i), points.get(i + 1)));
        }
    }
}
