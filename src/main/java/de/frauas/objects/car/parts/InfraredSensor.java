package de.frauas.objects.car.parts;

import de.frauas.IDrawable;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IInfraredSensor;
import de.frauas.objects.trace.ShiftedTrace;

import java.awt.*;

/**
 * Represents a single infrared sensor that is attached to a car.
 * The sensor is positioned relative to its parent (the car)
 * and is used to determine whether it is currently located on the track.
 * <p>
 * @author Infrared
 */
public class InfraredSensor extends Transformable implements IInfraredSensor, IDrawable {

    /**
     * Creates a new infrared sensor with a position offset relative to its parent.
     * <p>
     * @param parent          the parent transformable object (the car)
     * @param positionOffset  the sensor's position relative to the parent's origin
     */
    public InfraredSensor(Transformable parent, Vec3D positionOffset) {
        this.parent = parent;
        transform.setTranslation(positionOffset);
    }

    /**
     * Determines whether the infrared sensor is currently positioned
     * on the track by checking if its world position lies between
     * the trace boundaries using isPointBetweenLines().
     * <p>
     * @param trace the shifted trace representing the visual track
     * @return true if the sensor is on the track, false otherwise
     */
    @Override
    public boolean isOnTrack(ShiftedTrace trace) {
        Vec3D p = getWorldPosition();
        return trace.isPointBetweenLines(p);
    }

    /**
     * Draws the infrared sensor onto the provided graphics context.
     * The method applies the sensor's transformation settings to position
     * and orient the sensor correctly, sets its color to green, and renders
     * it as a filled oval shape at its transformed position.
     * <p>
     * @param g the Graphics object used to perform drawing operations
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        {
            g2d.transform(transform.toAffineTransform());
            g2d.setColor(Color.GREEN);
            g2d.fillOval(-3, -3, 6, 6);
        }
        g2d.dispose();
    }

    @Override
    public void drawInScene(Graphics g) {
        
    }
}
