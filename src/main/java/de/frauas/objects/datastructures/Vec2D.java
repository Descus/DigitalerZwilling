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
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D subtract(Vec2D other){
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D scale(double factor) {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public double dotProd(Vec2D other){
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public double length() {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public double lengthSq(){
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D rotate(double angle) {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D normalize() {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D negate() {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Vec2D perpendicuar(){
        //TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
