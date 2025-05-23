package de.frauas.objects.datastructures;

import lombok.Getter;

@Getter
public class Vec2D {
    private double x, y;
    
    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2D add(Vec2D other) {
        //TODO
    }
    
    public Vec2D subtract(Vec2D other){
        //TODO
    }
    
    public Vec2D scale(double factor) {
        //TODO
    }
    
    public double dotProd(Vec2D other){
        //TODO
    }
    
    public double length() {
        //TODO
    }
    
    public double lengthSq(){
        //TODO
    }
    
    public Vec2D rotate(double angle) {
        //TODO
    }
    
    public Vec2D normalize() {
        //TODO
    }
    
    public Vec2D negate() {
        //TODO
    }
    
    public Vec2D perpendicular(){
        //TODO
    }
}
