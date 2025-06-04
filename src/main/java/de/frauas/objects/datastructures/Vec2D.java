package de.frauas.objects.datastructures;

import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;

@Getter
public class Vec2D {

    public static final Vec2D zero = new Vec2D(0, 0);
    public static final Vec2D one = new Vec2D(1, 1);
    public static final Vec2D right = new Vec2D(1, 0);
    public static final Vec2D left = new Vec2D(-1, 0);
    public static final Vec2D up = new Vec2D(0, 1);
    public static final Vec2D down = new Vec2D(0, -1);

    private final double x, y;

    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2D add(Vec2D other) {
        return new Vec2D(this.x + other.x , this.y + other.y);
    }

    public Vec2D subtract(Vec2D other){
        return new Vec2D(this.x - other.x , this.y - other.y);
    }

    public Vec2D scale(double factor) {
        return new Vec2D(this.x * factor, this.y * factor);
    }

    public double dotProd(Vec2D other){
        return this.x * other.x + this.y * other.y;
    }

    public double length() {
        return Math.sqrt(lengthSq());
    }

    public double lengthSq(){
        return this.x * this.x + this.y * this.y;
    }

    public Vec2D abs() {
        return new Vec2D(Math.abs(this.x), Math.abs(this.y));
    }

    public Vec2D max(Vec2D other) {
        return new Vec2D(Math.max(this.x, other.x), Math.max(this.y, other.y));
    }

    public Vec2D min(Vec2D other) {
        return new Vec2D(Math.min(this.x, other.x), Math.min(this.y, other.y));
    }

    public double maxComponent() {
        return Math.max(this.x, this.y);
    }

    public double minComponent() {
        return Math.min(this.x, this.y);
    }

    public Vec2D rotate(double angleDeg) {
        double angle = Math.toRadians(angleDeg);
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        return new Vec2D( this.x * cosA - this.y * sinA, this.x * sinA + this.y * cosA);
    }

    public Vec2D normalize() {
        double length = length();
        return new Vec2D(this.x / length, this.y / length);
    }

    public Vec2D negate() {
        return new Vec2D(this.x * -1, this.y * -1);
    }
    
    public Vec2D perpendicular(){
        return new Vec2D(this.y, this.x * -1);
    }

    public void draw(Graphics g, Function<Vec2D, Vec2D> transformFunction){
        Vec2D transformedPoint = transformFunction.apply(this);
        transformedPoint = transformedPoint.subtract(new Vec2D((double) Settings.POINT_DEBUG_RADIUS / 2, (double) Settings.POINT_DEBUG_RADIUS / 2));
        g.fillOval((int) transformedPoint.getX(), (int) transformedPoint.getY(), Settings.POINT_DEBUG_RADIUS, Settings.POINT_DEBUG_RADIUS);
    }

    public boolean equals(Vec2D other){
        return this.x == other.x && this.y == other.y;
    }

    public Vec2D reflect(Vec2D p){
        return p.scale(2).subtract(this);
    }

    public static Vec2D select(Vec2D a, Vec2D b, boolean isA){
        return isA ? a : b;
    }
    
    public String toString(){
        return String.format("(%.1f, %.1f)", x, y);
    }
}
