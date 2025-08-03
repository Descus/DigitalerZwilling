package de.frauas.objects.obstacle;


import de.frauas.IDrawable;
import de.frauas.objects.Scene;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

import java.awt.*;

/**
 * Represents an obstacle within a 3D scene. The obstacle is defined by its start and end points,
 * dimensions, and height. It is transformable, drawable, and supports computation of Signed
 * Distance Field (SDF) values.
 * <p>
 * This class extends the {@code Transformable} class, inheriting properties and methods for
 * transforming its position, scale, and rotation within a hierarchical coordinate space.
 * Additionally, it implements the {@code ISdf} and {@code IDrawable} interfaces, enabling
 * interaction with SDF-based methods and rendering in a graphical context.
 *
 * @author Scenario
 */
@Getter
public class Obstacle extends Transformable implements ISdf, IDrawable {
    /**
     * Represents the starting point of the obstacle in 3D space.
     * This point is used to define the initial position of an obstacle, generally
     * in conjunction with its ending point. The coordinates of the starting point
     * are stored as a 3D vector, encapsulated by the {@link Vec3D} class.
     * <p>
     * The starting point is immutable and provides a reference for geometric
     * calculations such as the obstacle's bounds or spatial relationships within
     * a scene.
     */
    private final Vec3D startPoint;
    /**
     * Represents the ending point of the obstacle in 3D space.
     * This field is a fixed 3D vector defining one endpoint of the obstacle's spatial representation.
     */
    private final Vec3D endPoint;
    /**
     * Represents the dimensions of the obstacle in 3D space. This vector defines
     * the width, depth, and other spatial properties of the obstacle, relevant for
     * its representation and interaction within the scene.
     */
    private final Vec3D dimension;
    /**
     * Represents the height of the obstacle in a 3D space.
     * Specifies the vertical extent of the obstacle, contributing
     * to its overall dimensional attributes.
     */
    private final int height;

    /**
     * Constructs an instance of the Obstacle class, representing a 3D obstacle within a scene.
     * The obstacle is defined by its starting and ending coordinates in 3D space and its height.
     * <p>
     * @param parent The parent scene to which this obstacle belongs.
     * @param xStart The x-coordinate of the starting point of the obstacle.
     * @param yStart The y-coordinate of the starting point of the obstacle.
     * @param xEnd The x-coordinate of the ending point of the obstacle.
     * @param yEnd The y-coordinate of the ending point of the obstacle.
     * @param height The height of the obstacle.
     */
    public Obstacle(Scene parent, int xStart, int yStart, int xEnd, int yEnd, int height) {
        this.parent = parent;
        this.startPoint = new Vec3D(xStart, yStart, 0);
        this.endPoint = new Vec3D(xEnd, yEnd, 0);
        this.dimension = endPoint.subtract(startPoint).abs();
        this.height = height;
    }

    /**
     * Constructs an Obstacle instance representing a 3D obstacle within a scene.
     * The obstacle is defined by its starting and ending points in 3D space, and its height.
     * <p>
     * @param parent The parent scene to which this obstacle belongs.
     * @param startPoint The starting point of the obstacle as a 3D vector.
     * @param endPoint The ending point of the obstacle as a 3D vector.
     * @param height The height of the obstacle.
     */
    public Obstacle(Scene parent, Vec3D startPoint, Vec3D endPoint, int height) {
        this.parent = parent;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.dimension = endPoint.subtract(startPoint).abs();
        this.height = height;
    }

    /**
     * Draws the obstacle onto the provided Graphics context. This method uses a transformation
     * to position and orient the obstacle and renders a rectangular representation of it.
     * <p>
     * @param g The Graphics context on which the obstacle will be drawn.
     */
    @Override
    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g.create();
        {
            g2.transform(transform.toAffineTransform());
            g2.drawRect((int) startPoint.getX(), (int) startPoint.getY(), (int) dimension.getX(), (int) dimension.getY());
        }
        g2.dispose();
    }

    /**
     * Draws the obstacle onto the given Graphics context within the scene.
     * This method handles the rendering with respect to the scene's transformations
     * and ensures the obstacle is depicted at the correct position and scale.
     * <p>
     * @param g the Graphics context used to draw the obstacle within the scene.
     */
    @Override
    public void drawInScene(Graphics g) {
        draw(g);
    }

    /**
     * Computes the Signed Distance Field (SDF) value for a given 3D position relative to this obstacle.
     * The SDF value represents the shortest distance from the provided position to the nearest surface
     * of the obstacle, with negative values indicating positions inside the obstacle
     * and positive values indicating positions outside.
     * <p>
     * @param otherPosition The 3D position for which the SDF value is calculated.
     * @return The computed SDF value, where negative indicates inside the obstacle, and positive indicates outside.
     */
    @Override
    public double getSDF(Vec3D otherPosition) {
        Vec3D halfDim = dimension.scale(0.5);
        Vec3D translatedPosition = otherPosition.subtract(startPoint.add(halfDim));
        Vec3D d = translatedPosition.abs().subtract(halfDim);
        d = new Vec3D(d.getX(), d.getY(), -Double.MAX_VALUE);
        return d.max(Vec3D.zero).length() + Math.min(d.maxComponent(), 0);
    }
}
