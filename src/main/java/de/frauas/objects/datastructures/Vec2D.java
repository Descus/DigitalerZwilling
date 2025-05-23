package de.frauas.objects.datastructures;

import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;

@Getter
public class Vec2D {
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
        return new Vec2D(this.x * factor , this.y * factor);
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
        g.fillOval((int) transformedPoint.getX(), (int) transformedPoint.getY(), Settings.POINT_DEBUG_RADIUS, Settings.POINT_DEBUG_RADIUS);
    }
}
