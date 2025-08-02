package de.frauas.objects.datastructures;

import de.frauas.IDrawable;
import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;

/**
 * Represents a 3D vector with x, y, and z components. This class provides a variety
 * of utility methods for vector arithmetic, transformations, and operations such
 * as addition, subtraction, scaling, normalization, dot product, and more. It also
 * implements the IDrawable interface, allowing the vector object to be visually
 * represented in a graphical context.
 *
 * @author Scenario
 */
@Getter
public class Vec3D implements IDrawable {

    /**
     * Represents a zero vector in 3D space with all components (x, y, z) set to 0.
     * This is a constant value and can be used as a reference for calculations
     * involving a vector with no magnitude or direction.
     */
    public static final Vec3D zero = new Vec3D(0, 0, 0);
    /**
     * Represents the identity vector for the Vec3D class.
     * This vector has components (0, 0, 1) and is commonly used as the default
     * direction in 3D space along the z-axis.
     */
    public static final Vec3D identity = new Vec3D(0, 0, 1);
    /**
     * A constant vector in 3D space with all components set to 1.
     * Represents a unit vector where x, y, and z are equal to 1.
     * Often used to initialize or represent uniform scaling or as a basis for certain vector operations.
     */
    public static final Vec3D one = new Vec3D(1, 1, 1);
    /**
     * A constant vector representing the positive X-axis direction in a 3D space.
     * This can be used as a basis vector or reference for operations requiring
     * a unit vector in the rightward (positive X) direction.
     */
    public static final Vec3D right = new Vec3D(1, 0, 0);
    /**
     * A constant vector representing the left direction in 3D space.
     * This vector has a value of (-1, 0, 0), indicating movement in the negative X-axis direction.
     * It is commonly used to denote or perform transformations towards the left in 3D space.
     */
    public static final Vec3D left = new Vec3D(-1, 0, 0);
    /**
     * A constant vector representing the upward direction in 3D space.
     * The vector has coordinates (0, 1, 0), indicating no movement along the x-axis
     * or z-axis and a unit movement along the y-axis.
     */
    public static final Vec3D up = new Vec3D(0, 1, 0);
    /**
     * A constant vector representing the downward direction in 3D space.
     * This vector has components (0, -1, 0), indicating a unit vector
     * pointing in the negative y-axis direction.
     */
    public static final Vec3D down = new Vec3D(0, -1, 0);

    /**
     * Represents the x-coordinate or x-component of a 3D vector.
     * This is a constant value that cannot be modified after initialization.
     * It is typically used to define the position or direction of a vector
     * along the x-axis in a 3D Cartesian coordinate system.
     */
    private final double x; /**
     * Represents the Y-component of a 3D vector in the Vec3D class.
     * This value is used to define the vertical position of the vector
     * or its movement along the Y-axis in a 3-dimensional space.
     */
    private final double y; /**
     * The z-component of a 3D vector.
     * Represents the third coordinate in a three-dimensional Cartesian coordinate system.
     * Typically used in vector operations such as addition, scaling, or dot product calculations.
     */
    private final double z;

    /**
     * Constructs a new 3D vector with the provided x, y, and z components.
     * <p>
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @param z the z-component of the vector
     */
    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds the components of the given vector to the components of this vector, producing a new vector.
     * <p>
     * @param other the vector to add to this vector
     * @return a new Vec3D instance representing the result of the addition
     */
    public Vec3D add(Vec3D other) {
        return new Vec3D(this.x + other.x , this.y + other.y, this.z + other.z);
    }

    /**
     * Subtracts the components of the given vector from this vector, producing a new vector.
     * <p>
     * @param other the vector to subtract from this vector
     * @return a new Vec3D instance representing the result of the subtraction
     */
    public Vec3D subtract(Vec3D other){
        return new Vec3D(this.x - other.x , this.y - other.y, this.z - other.z);
    }

    /**
     * Scales this vector by the provided factor, returning a new vector with each
     * component multiplied by the factor.
     * <p>
     * @param factor the scalar value by which to scale the components of this vector
     * @return a new Vec3D instance representing the scaled vector
     */
    public Vec3D scale(double factor) {
        return new Vec3D(this.x * factor, this.y * factor, this.z * factor);
    }

    /**
     * Computes the dot product of this vector and another vector.
     * The dot product is defined as the sum of the products
     * of the corresponding components of the two vectors.
     * <p>
     * @param other the vector to compute the dot product with
     * @return the dot product of the two vectors as a double value
     */
    public double dotProd(Vec3D other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    /**
     * Computes the length (magnitude) of the 3D vector. The length is calculated
     * as the square root of the sum of squares of the vector components.
     * <p>
     * @return the length of the vector as a double value
     */
    public double length() {
        return Math.sqrt(lengthSq());
    }

    /**
     * Computes the squared length (magnitude) of this 3D vector.
     * The squared length is obtained by summing the squares of
     * the x, y, and z components of the vector.
     * <p>
     * @return the squared length of the vector as a double value
     */
    public double lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Returns a new vector where each component is the absolute value
     * of the respective component of this*/
    public Vec3D abs() {
        return new Vec3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    /**
     * Computes a new vector where each component is the maximum of the corresponding
     * components of this vector and the specified vector.
     * <p>
     * @param other the vector to compare with this vector for the maximum components
     * @return a new Vec3D instance containing the maximum components from this
     *         vector and the other vector
     */
    public Vec3D max(Vec3D other) {
        return new Vec3D(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    /**
     * Computes a new vector where each component is the minimum of the corresponding
     * components of this vector and the specified vector.
     * <p>
     * @param other the vector to compare with this vector for the minimum components
     * @return a new Vec3D instance containing the minimum components from this
     *         vector and the other vector
     */
    public Vec3D min(Vec3D other) {
        return new Vec3D(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    /**
     * Returns the maximum value among the x, y, and z components of this 3D vector.
     * <p>
     * @return the largest component value as a double
     */
    public double maxComponent() {
        return Math.max(Math.max(this.x, this.y), z);
    }

    /**
     * Returns the smallest value among the x, y, and z components of this 3D vector.
     * <p>
     * @return the smallest component value as a double
     */
    public double minComponent() {
        return Math.min(Math.min(this.x, this.y), z);
    }

    /**
     * Rotates this 3D vector around the Z-axis by the specified angle in degrees.
     * The rotation is performed in 2D (x and y components are rotated),
     * while the z-component remains unchanged.
     * <p>
     * @param angleDeg the angle of rotation in degrees
     * @return a new Vec3D instance representing the rotated vector
     */
    public Vec3D rotate(double angleDeg) {
        double angle = Math.toRadians(angleDeg);
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        return new Vec3D( this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA, this.z);
    }

    /**
     * Normalizes the vector to have a length of 1 while maintaining its direction.
     * This is achieved by dividing each component of the vector by its current length.
     * If the vector's length is 0, normalization is undefined and may result in an exception.
     * <p>
     * @return a new Vec3D instance representing the normalized vector with a unit length
     */
    public Vec3D normalize() {
        double length = length();
        return new Vec3D(this.x / length, this.y / length, this.z/length);
    }

    /**
     * Creates a new vector that is the negation (inversion) of this vector.
     * Each component of the vector is multiplied by -1, effectively reversing its direction.
     * <p>
     * @return a new Vec3D instance representing the negation of this vector
     */
    public Vec3D negate() {
        return new Vec3D(this.x * -1, this.y * -1, this.z * -1);
    }

    /**
     * Computes a new 3D vector that is perpendicular to the current vector in the XY plane.
     * The perpendicular vector is calculated by swapping the x and y components of this vector
     * and negating the new x-component. The resulting vector's z-component is set to 0.
     * <p>
     * @return a new Vec3D instance representing the vector perpendicular
     *         to this vector in the XY plane
     */
    public Vec3D perpendicular(){
        //noinspection SuspiciousNameCombination
        return new Vec3D(this.y, this.x * -1, 0);
    }

    /**
     * Draws the Vec3D instance on the provided graphics context. The point is
     * represented as an oval with a predefined radius, transformed by its
     * internal coordinates and settings.
     * <p>
     * @param g the Graphics context where the vector point will be drawn
     */
    @Override
    public void draw(Graphics g){
        Vec3D transformedPoint = this.subtract(new Vec3D((double) Settings.DEBUG.POINT_RADIUS / 2, (double) Settings.DEBUG.POINT_RADIUS / 2, 1));
        g.fillOval((int) transformedPoint.getX(), (int) transformedPoint.getY(), Settings.DEBUG.POINT_RADIUS, Settings.DEBUG.POINT_RADIUS);
    }

    /**
     * Draws the object in the specified graphical scene. The method utilizes the
     * given Graphics context to render the object based on its internal properties
     * and transformations.
     * <p>
     */
    @Override
    public void drawInScene(Graphics g) {
        draw(g);
    }

    /**
     * Compares this vector with another vector to determine whether they are equal.
     * Two vectors are considered equal if their corresponding x, y, and z components
     * are the same.
     * <p>
     * @param other the vector to be compared with this vector
     * @return true if the vectors have the same components, false otherwise
     */
    public boolean equals(Vec3D other){
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    /**
     * Computes the reflection of the given vector relative to this vector.
     * The reflection is calculated by scaling the given vector by 2 and then subtracting this vector.
     * <p>
     * @param p the vector to be reflected
     * @return a new Vec3D instance representing the reflected vector
     */
    public Vec3D reflect(Vec3D p){
        return p.scale(2).subtract(this);
    }
    
    /**
     * Returns a string representation of the Vec3D object.
     * The string is formatted as "(x, y, z)", where x, y, and z are
     * the components of the vector rounded to one decimal place.
     * <p>
     * @return a string representation of this vector
     */
    public String toString(){
        return String.format("(%.1f, %.1f, %.1f)", x, y, z);
    }

}
