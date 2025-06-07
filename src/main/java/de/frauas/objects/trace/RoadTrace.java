package de.frauas.objects.trace;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class RoadTrace extends Trace {

    protected final List<Line3D> lines = new ArrayList<>();

    public RoadTrace(Scene parent, ArrayList<Vec3D> points) {
        super(parent, points);
        createLines();
    }

    public RoadTrace(Scene parent) {
        super(parent);
        createLines();
    }

    public Vec3D lerp(double t){
        int segment = (int) t;
        if(t > lines.size() - 1)
            return last();
        return lines.get(segment).lerp(t - segment);
    }

    @Override
    public void drawLines(Graphics g){
        lines.forEach(l -> l.draw(g));
    }

    @Override
    protected void createLines(){
        lines.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line3D(points.get(i), points.get(i + 1)));
        }
    }
}
