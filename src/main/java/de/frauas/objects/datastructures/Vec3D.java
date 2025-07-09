package de.frauas.objects.datastructures;

import de.frauas.IDrawable;
import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;

@Getter
public class Vec3D implements IDrawable {

    public static final Vec3D zero = new Vec3D(0, 0, 0);
    public static final Vec3D identity = new Vec3D(0, 0, 1);
    public static final Vec3D one = new Vec3D(1, 1, 1);
    public static final Vec3D right = new Vec3D(1, 0, 0);
    public static final Vec3D left = new Vec3D(-1, 0, 0);
    public static final Vec3D up = new Vec3D(0, 1, 0);
    public static final Vec3D down = new Vec3D(0, -1, 0);

    private final double x, y, z;

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D add(Vec3D other) {
        return new Vec3D(this.x + other.x , this.y + other.y, this.z + other.z);
    }

    public Vec3D subtract(Vec3D other){
        return new Vec3D(this.x - other.x , this.y - other.y, this.z - other.z);
    }

    public Vec3D scale(double factor) {
        return new Vec3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public double dotProd(Vec3D other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double length() {
        return Math.sqrt(lengthSq());
    }

    public double lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vec3D abs() {
        return new Vec3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    public Vec3D max(Vec3D other) {
        return new Vec3D(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    public Vec3D min(Vec3D other) {
        return new Vec3D(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    public double maxComponent() {
        return Math.max(Math.max(this.x, this.y), z);
    }

    public double minComponent() {
        return Math.min(Math.min(this.x, this.y), z);
    }

    public Vec3D rotate(double angleDeg) {
        double angle = Math.toRadians(angleDeg);
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        return new Vec3D( this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA, this.z);
    }

    public Vec3D normalize() {
        double length = length();
        return new Vec3D(this.x / length, this.y / length, this.z/length);
    }

    public Vec3D negate() {
        return new Vec3D(this.x * -1, this.y * -1, this.z * -1);
    }

    /**
     * Returns the perpendicular vector in 2 Dimensional Space.
     * @return 
     */
    public Vec3D perpendicular(){
        //noinspection SuspiciousNameCombination
        return new Vec3D(this.y, this.x * -1, 0);
    }

    @Override
    public void draw(Graphics g){
        Vec3D transformedPoint = this.subtract(new Vec3D((double) Settings.DEBUG.POINT_RADIUS / 2, (double) Settings.DEBUG.POINT_RADIUS / 2, 1));
        g.fillOval((int) transformedPoint.getX(), (int) transformedPoint.getY(), Settings.DEBUG.POINT_RADIUS, Settings.DEBUG.POINT_RADIUS);
    }

    @Override
    public void drawInScene(Graphics g) {
        draw(g);
    }

    public boolean equals(Vec3D other){
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    public Vec3D reflect(Vec3D p){
        return p.scale(2).subtract(this);
    }
    
    public String toString(){
        return String.format("(%.1f, %.1f, %.1f)", x, y, z);
    }

}
