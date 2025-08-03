package de.frauas.objects.datastructures;


import de.frauas.IDrawable;
import de.frauas.objects.Transformable;
import lombok.Getter;

import java.awt.*;

/**
 * Represents a directed line segment in 3D space defined by a start and end point.
 * The class provides methods for computing properties of the line, such as its length,
 * and determining spatial relationships between the line and other points in 3D space.
 * This class extends Transformable, allowing it to inherit transformations
 * (e.g., rotation, translation, scaling) from a parent Transformable object.
 * It also implements the IDrawable interface, enabling visual rendering
 * of the line in a 2D graphical context.
 *
 * @author Scenario
 */
@Getter
public class Line3D extends Transformable implements IDrawable {
    /**
     * Represents the starting point of the directed line segment in a 3D space.
     * This field is immutable and defines one of the two endpoints that make up the line.
     * It serves as the origin vector for calculations involving the line's direction,
     * length, or spatial relationships.
     */
    private final Vec3D start;

    /**
     * Represents the ending point of the 3D line segment defined by this Line3D object.
     * The field is a 3D vector indicating the line's terminal position in the 3D space.
     * It is initialized during the creation of the Line3D instance and remains immutable.
     */
    private final Vec3D end;

    /**
     * Constructs a new Line3D object representing a directed line segment in 3D space.
     *
     * @param parent the parent object in the transformation hierarchy. It can be used to apply transformations
     *               and establish relationships between the line and other objects. Must not be null.
     * @param start  the starting point of the line segment as a 3D vector. Must not be null.
     * @param end    the ending point of the line segment as a 3D vector. Must not be null.
     */
    public Line3D(Transformable parent, Vec3D start, Vec3D end) {
        this.parent = parent;
        this.start = start;
        this.end = end;
    }

    /**
     * Calculates the length of the directed line segment represented by this object.
     * The length is computed as the Euclidean distance between the start and end points in 3D space.
     *
     * @return the length of the line segment as a double
     */
    public double length(){
        return end.subtract(start).length();
    }

    /**
     * Determines whether a given point lies to the right of the directed line
     * segment defined by this line's start and end points.
     * The method uses the dot product between the vector from the start to the point
     * and a perpendicular vector to the line direction.
     *
     * @param point the point to check
     * @return true if the point is to the right of the line; false otherwise
     * @author Infrared-Team
     */
    public boolean rightOfLine(Vec3D point) {
        Vec3D ap = point.subtract(start);
        Vec3D abPerp = end.subtract(start).perpendicular();
        return ap.dotProd(abPerp) >= 0;
    }
    
    /**
     * Returns a string representation of the Line3D object.
     * The string is formatted as "start -> end", where "start" and "end"
     * are the string representations of the start and end points of the line.
     *
     * @return a string representation of this Line3D object
     */
    public String toString(){
        return String.format("%s -> %s", start.toString(), end.toString());
    }

    /**
     * Renders the directed line segment defined by the start and end points onto the provided
     * graphical context. The method applies the current transformation to the graphical context
     * before rendering the line and ensures any temporary resources created are disposed of properly.
     * <p>
     * @param g the graphical context used for rendering the line. It must not be null.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g.drawLine(
                    (int) this.start.getX(),
                    (int) this.start.getY(),
                    (int) this.end.getX(),
                    (int) this.end.getY());
        }
        g2.dispose();
    }

    /**
     * Renders the directed line segment defined by the start and end points
     * of this Line3D object within the given scene. The method utilizes the
     * specified graphical context to draw a straight line between the start
     * and end points after applying necessary transformations.
     *
     * @param g the graphical context used for rendering the line. It must not be null.
     */
    @Override
    public void drawInScene(Graphics g) {
        g.drawLine(
                (int) this.start.getX(),
                (int) this.start.getY(),
                (int) this.end.getX(),
                (int) this.end.getY());
    }
}
