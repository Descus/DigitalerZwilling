package de.frauas.objects.datastructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vec3DTest {

    @Test
    void add() {
        // Test für Vektoraddition: (1,1) + (2,2) = (3,3)
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D vec2 = new Vec3D(2, 2);
        Vec3D result = vec1.add(vec2);
        assertEquals(3, result.getX());
        assertEquals(3, result.getY());
    }

    @Test
    void subtract() {
        // Test für Vektorsubtraktion: (2,2) - (1,1) = (1,1)
        Vec3D vec1 = new Vec3D(2, 2);
        Vec3D vec2 = new Vec3D(1, 1);
        Vec3D result = vec1.subtract(vec2);
        assertEquals(1, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void scale() {
        // Test für Skalierung: (1,1) * 2 = (2,2)
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.scale(2);
        assertEquals(2, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void length() {
        // Test für Vektorlänge: √(1² + 1²) = √2
        Vec3D vec1 = new Vec3D(1, 1);
        double result = vec1.length();
        assertEquals(Math.sqrt(2), result);
    }

    @Test
    void lengthSq() {
        // Test für quadratische Länge: 1² + 1² = 2
        Vec3D vec1 = new Vec3D(1, 1);
        double result = vec1.lengthSq();
        assertEquals(2, result);
    }

    @Test
    void rotate() {
        // Test für Rotation: (1,1) um -90 Grad drehen → (1,-1)
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.rotate(-90);
        assertEquals(1, result.getX());
        assertEquals(-1, result.getY(), 0.001); // Mit Toleranz wegen Double-Wert
    }

    @Test
    void normalize() {
        // Test für Normierung: (0,4) → (0,1)
        Vec3D vec1 = new Vec3D(0, 4);
        Vec3D result = vec1.normalize();
        assertEquals(0, result.getX());
        assertEquals(1, result.getY());
    }

    @Test
    void dotProd_OrthogonalVectors() {
        // Test für Skalarprodukt: (1,0) · (0,1) = 0 (orthogonal)
        Vec3D vec1 = new Vec3D(1, 0);
        Vec3D vec2 = new Vec3D(0, 1);
        double result = vec1.dotProd(vec2);
        assertEquals(0, result);
    }

    @Test
    void dotProd_UnorthogonalVectors() {
        // Test für Skalarprodukt: (2,2) · (1,1) = 2*1 + 2*1 = 4
        Vec3D vec1 = new Vec3D(2, 2);
        Vec3D vec2 = new Vec3D(1, 1);
        double result = vec1.dotProd(vec2);
        assertEquals(4, result);
    }

    @Test
    void perpendicular() {
        // Test für senkrechten Vektor: (1,1) ⟶ (1,-1)
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.perpendicular();
        assertEquals(1, result.getX());
        assertEquals(-1, result.getY());
    }

    @Test
    void negate(){
        // Test für Vorzeichenumkehrung: (1,1) ⟶ (-1,-1)
        Vec3D vec1 = new Vec3D(1, 1);
        Vec3D result = vec1.negate();
        assertEquals(-1, result.getX());
        assertEquals(-1, result.getY());
    }
}
