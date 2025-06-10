package de.frauas.objects.trace;

import de.frauas.IDrawable;
import de.frauas.Settings;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public abstract class Trace extends Transformable implements IDrawable {

    protected final List<Vec3D> points = new ArrayList<>();

    public Trace(Scene parent, List<Vec3D> points) {
        this.parent = parent;
        this.points.addAll(points);
    }

    public Trace(Scene parent) {
        this.parent = parent;
    }

    public void addPoint(Vec3D point) {
        points.add(point);
        createLines();
    }

    public Vec3D first(){
        return points.getFirst();
    }

    public Vec3D last(){
        return points.getLast();
    }
    
    protected abstract void createLines();
    
    public abstract void drawLines(Graphics g);
    
    public void drawPoints(Graphics g) {
            points.forEach(p -> p.draw(g));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g2.setColor(Color.black);
            drawLines(g2);

            if (Settings.DEBUG) {
                Graphics2D g2Debug = (Graphics2D) g.create();
                g2Debug.setColor(Color.RED);
                drawPoints(g2Debug);
                g2Debug.dispose();
            }
        }
        g2.dispose();
    }
}
