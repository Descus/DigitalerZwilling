package de.frauas.objects.datastructures;

import de.frauas.IDrawable;
import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;

/**
 * @author Scenario-Group
 * Represents an immutable 3D vector with x, y, and z components.
 * This class provides a comprehensive set of vector operations, including
 * arithmetic, normalization, and geometric calculations. It is also drawable
 * for debugging purposes.
 */
@Getter
public class Vec3D implements IDrawable {

    /** A vector with all components set to zero. */
    public static final Vec3D zero = new Vec3D(0, 0, 0);
    /** A vector representing the identity element for homogeneous coordinates (0, 0, 1). */
    public static final Vec3D identity = new Vec3D(0, 0, 1);
    /** A vector with all components set to one. */
    public static final Vec3D one = new Vec3D(1, 1, 1);
    /** A unit vector pointing along the positive x-axis. */
    public static final Vec3D right = new Vec3D(1, 0, 0);
    /** A unit vector pointing along the negative x-axis. */
    public static final Vec3D left = new Vec3D(-1, 0, 0);
    /** A unit vector pointing along the positive y-axis. */
    public static final Vec3D up = new Vec3D(0, 1, 0);
    /** A unit vector pointing along the negative y-axis. */
    public static final Vec3D down = new Vec3D(0, -1, 0);

    /** The x-component of the vector. */
    private final double x;
    /** The y-component of the vector. */
    private final double y;
    /** The z-component of the vector. */
    private final double z;

    /**
     * Constructs a new Vec3D with the specified x, y, and z components.
     *
     * @param x The x-component.
     * @param y The y-component.
     * @param z The z-component.
     */
    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other The vector to add.
     * @return A new {@code Vec3D} instance representing the sum.
     */
    public Vec3D add(Vec3D other) {
        return new Vec3D(this.x + other.x , this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts another vector from this vector.
     *
     * @param other The vector to subtract.
     * @return A new {@code Vec3D} instance representing the difference.
     */
    public Vec3D subtract(Vec3D other){
        return new Vec3D(this.x - other.x , this.y - other.y, this.z - other.z);
    }

    /**
     * Scales this vector by a scalar factor.
     *
     * @param factor The scaling factor.
     * @return A new {@code Vec3D} instance representing the scaled vector.
     */
    public Vec3D scale(double factor) {
        return new Vec3D(this.x * factor, this.y * factor, this.z * factor);
    }

    /**
     * Calculates the dot product of this vector with another.
     *
     * @param other The other vector.
     * @return The dot product.
     */
    public double dotProd(Vec3D other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Calculates the magnitude (length) of this vector.
     *
     * @return The length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSq());
    }

    /**
     * Calculates the squared magnitude (length) of this vector.
     * This is more efficient than {@code length()} if only comparing magnitudes.
     *
     * @return The squared length of the vector.
     */
    public double lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Creates a new vector with the absolute values of this vector's components.
     *
     * @return A new {@code Vec3D} with absolute components.
     */
    public Vec3D abs() {
        return new Vec3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    /**
     * Creates a new vector by taking the component-wise maximum of this and another vector.
     *
     * @param other The other vector to compare with.
     * @return A new {@code Vec3D} with the maximum components.
     */
    public Vec3D max(Vec3D other) {
        return new Vec3D(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    /**
     * Creates a new vector by taking the component-wise minimum of this and another vector.
     *
     * @param other The other vector to compare with.
     * @return A new {@code Vec3D} with the minimum components.
     */
    public Vec3D min(Vec3D other) {
        return new Vec3D(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    /**
     * Returns the largest component (x, y, or z) of this vector.
     *
     * @return The value of the largest component.
     */
    public double maxComponent() {
        return Math.max(Math.max(this.x, this.y), z);
    }

    /**
     * Returns the smallest component (x, y, or z) of this vector.
     *
     * @return The value of the smallest component.
     */
    public double minComponent() {
        return Math.min(Math.min(this.x, this.y), z);
    }

    /**
     * Rotates this vector around the z-axis by a given angle.
     *
     * @param angleDeg The angle of rotation in degrees.
     * @return A new {@code Vec3D} instance representing the rotated vector.
     */
    public Vec3D rotate(double angleDeg) {
        double angle = Math.toRadians(angleDeg);
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        return new Vec3D( this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA, this.z);
    }

    /**
     * Creates a normalized version of this vector (a unit vector).
     *
     * @return A new {@code Vec3D} with length 1.
     */
    public Vec3D normalize() {
        double length = length();
        return new Vec3D(this.x / length, this.y / length, this.z/length);
    }

    /**
     * Negates all components of this vector.
     *
     * @return A new {@code Vec3D} instance with all components inverted.
     */
    public Vec3D negate() {
        return new Vec3D(this.x * -1, this.y * -1, this.z * -1);
    }

    /**
     * Calculates the perpendicular vector in the 2D plane (XY).
     * The z-component of the resulting vector is set to 0.
     *
     * @return A new {@code Vec3D} that is perpendicular to this vector's XY components.
     */
    public Vec3D perpendicular(){
        //noinspection SuspiciousNameCombination
        return new Vec3D(this.y, this.x * -1, 0);
    }

    /**
     * Draws a representation of this vector as a point on a graphics context.
     * The drawing is offset by the point radius for centering.
     *
     * @param g The Graphics context to draw on.
     */
    @Override
    public void draw(Graphics g){
        Vec3D transformedPoint = this.subtract(new Vec3D((double) Settings.DEBUG.POINT_RADIUS / 2, (double) Settings.DEBUG.POINT_RADIUS / 2, 1));
        g.fillOval((int) transformedPoint.getX(), (int) transformedPoint.getY(), Settings.DEBUG.POINT_RADIUS, Settings.DEBUG.POINT_RADIUS);
    }

    /**
     * Draws the vector in the scene, effectively calling the {@code draw} method.
     *
     * @param g The Graphics context to draw on.
     */
    @Override
    public void drawInScene(Graphics g) {
        draw(g);
    }

    /**
     * Checks for equality between this vector and another.
     *
     * @param other The vector to compare with.
     * @return {@code true} if all components are equal, {@code false} otherwise.
     */
    public boolean equals(Vec3D other){
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    /**
     * Reflects this vector across another point (p).
     *
     * @param p The point to reflect across.
     * @return A new {@code Vec3D} representing the reflected vector.
     */
    public Vec3D reflect(Vec3D p){
        return p.scale(2).subtract(this);
    }

    /**
     * Returns a string representation of the vector.
     *
     * @return A formatted string like "(x.x, y.y, z.z)".
     */
    public String toString(){
        return String.format("(%.1f, %.1f, %.1f)", x, y, z);
    }
}