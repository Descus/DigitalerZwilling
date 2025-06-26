package de.frauas.objects.car.parts;

import de.frauas.IDrawable;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.trace.ShiftedTrace;

import java.awt.*;


public class InfraredSensor extends Transformable implements IInfraredSensor, IDrawable {

    public InfraredSensor(Transformable parent, Vec3D positionOffset) {
        this.parent = parent;
        transform.setTranslation(positionOffset);
    }

    @Override
    public boolean isOnTrack(ShiftedTrace trace) {
        Vec3D p = getWorldPosition();
        return trace.isPointBetweenLines(p);
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.transform(transform.toAffineTransform());
        g2d.setColor(Color.GREEN);
        g2d.fillOval(-3, -3, 6, 6);
        g2d.dispose();
    }
}
