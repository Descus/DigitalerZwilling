package de.frauas.objects.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vec3DTest {

    @Test
    void add() {
        Vec3D vec1 = new Vec3D(1, 1, 1);
        Vec3D vec2 = new Vec3D(2, 3, 1);
        Vec3D result = vec1.add(vec2);
        assertEquals(3, result.getX());
        assertEquals(4, result.getY());
        assertEquals(2, result.getY());
        
    }

    @Test
    void subtract() {
        Vec3D vec1 = new Vec3D(2, 2, 1);
        Vec3D vec2 = new Vec3D(1, 3, 5);
        Vec3D result = vec1.subtract(vec2);
        assertEquals(1, result.getX());
        assertEquals(-1, result.getY());
        assertEquals(-4, result.getZ());
    }

    @Test
    void scale() {
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.scale(2);
        assertEquals(2, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void length() {
        Vec3D vec1 = new Vec3D(1, 1);
        double result = vec1.length();
        assertEquals(Math.sqrt(2), result);
    }

    @Test
    void lengthSq() {
        Vec3D vec1 = new Vec3D(1, 1);
        double result = vec1.lengthSq();
        assertEquals(2, result);
    }

    @Test
    void rotate() {
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.rotate(-90);
        assertEquals(1, result.getX());
        assertEquals(-1, result.getY(), 0.001);
    }

    @Test
    void normalize() {
        Vec3D vec1 = new Vec3D(0, 4);
        Vec3D result = vec1.normalize();
        assertEquals(0, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void dotProd_OrthogonalVectors() {
        Vec3D vec1 = new Vec3D(1, 0);
        Vec3D vec2 = new Vec3D(0, 1);
        double result = vec1.dotProd(vec2);
        assertEquals(0, result);
    }

    @Test
    void dotProd_UnorthogonalVectors() {
        Vec3D vec1 = new Vec3D(2, 2);
        Vec3D vec2 = new Vec3D(1, 1);
        double result = vec1.dotProd(vec2);
        assertEquals(4, result);
    }
    
    @Test
    void perpendicular() {
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.perpendicular();
        assertEquals(1, result.getX());
        assertEquals(-1, result.getY());
    }
    
    @Test
    void negate(){
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.negate();
        assertEquals(-1, result.getX());
        assertEquals(-1, result.getY());
    }
}