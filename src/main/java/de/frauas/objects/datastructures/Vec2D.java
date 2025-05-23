package de.frauas.objects.datastructures;

import lombok.Getter;

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
        double result = this.x * other.x + this.y * other.y;
        return result;

    }

    public double length() {
            double result = Math.sqrt(this.x * this.x + this.y * this.y);
            return result;
    }

    public double lengthSq(){
        double result = this.x * this.x + this.y * this.y;
        return result;
    }

    public Vec2D rotate(double angle) {
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        double newX = this.x * cosA - this.y * sinA;
        double newY = this.x * sinA + this.y * cosA;
        Vec2D result = new Vec2D(newX, newY);
        return result;
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
}
