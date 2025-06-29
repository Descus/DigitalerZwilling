package de.frauas.objects.trace.debug;

import de.frauas.objects.Scene;
import de.frauas.objects.datastructures.Line3D;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.trace.Trace;
import de.frauas.objects.trace.TraceType;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public TraceType getType() {
        return TraceType.DEBUG;
    }

    @Override
    public void drawLines(Graphics g){
        lines.forEach(l -> l.draw(g));
    }

    @Override
    public void createLines(){
        lines.clear();
        for (int i = 0; i < points.size() - 1; i++) {
            lines.add(new Line3D(this, points.get(i), points.get(i + 1)));
        }
    }
}
