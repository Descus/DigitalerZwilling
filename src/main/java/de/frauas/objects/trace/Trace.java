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

@Getter
public abstract class Trace extends Transformable implements IDrawable {

    protected final List<Vec3D> points = new ArrayList<>();
    protected double length = 0;

    public Trace(Scene parent, List<Vec3D> points) {
        this.parent = parent;
        this.points.addAll(points);
        calculateLength();
    }

    public Trace(Scene parent) {
        this.parent = parent;
    }

    public void addPoint(Vec3D point) {
        points.add(point);
        createLines();
        calculateLength();
    }

    private void calculateLength(){
        length = 0;
        for (int i = 0; i < points.size() - 2; i++) {
            length += points.get(i).subtract(points.get(i + 1)).length();
        }
    }

    public Vec3D first(){
        return points.getFirst();
    }

    public Vec3D last(){
        return points.getLast();
    }
    
    protected abstract void createLines();
    
    public abstract void drawLines(Graphics g);

    public abstract TraceType getType();
    
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

            if (Settings.DEBUG.ENABLED) {
                Graphics2D g2Debug = (Graphics2D) g.create();
                g2Debug.setColor(Color.RED);
                drawPoints(g2Debug);
                g2Debug.dispose();
            }
        }
        g2.dispose();
    }
    
    @Override
    public void drawInScene(Graphics g) {
        
    }
}
