package de.frauas.objects.datastructures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Mat3x3DTest {

    @Test
    void add() {
        // Wir testen: IDENTITY + ONE
        Mat3x3D a = Mat3x3D.IDENTITY; // Identitätsmatrix: Diagonale = 1
        Mat3x3D b = Mat3x3D.ONE;      // Alle Werte = 1
        Mat3x3D result = a.add(b);  // Ergebnis sollte Diagonale = 2, Rest = 1

        // Erwartete Werte überprüfen
        assertEquals(2, result.getM00()); // 1 + 1
        assertEquals(1, result.getM01()); // 0 + 1
        assertEquals(1, result.getM02());
        assertEquals(1, result.getM10());
        assertEquals(2, result.getM11()); // 1 + 1
        assertEquals(1, result.getM12());
        assertEquals(1, result.getM20());
        assertEquals(1, result.getM21());
        assertEquals(2, result.getM22()); // 1 + 1
    }

    @Test
    void sub() {
        // Wir testen: ONE - IDENTITY
        Mat3x3D a = Mat3x3D.ONE;         // Alle Werte = 1
        Mat3x3D b = Mat3x3D.IDENTITY;    // Diagonale = 1, Rest = 0
        Mat3x3D result = a.sub(b);     // Ergebnis: Diagonale = 0, Rest = 1

        // Erwartete Werte überprüfen
        assertEquals(0, result.getM00()); // 1 - 1
        assertEquals(1, result.getM01()); // 1 - 0
        assertEquals(1, result.getM02());
        assertEquals(1, result.getM10());
        assertEquals(0, result.getM11()); // 1 - 1
        assertEquals(1, result.getM12());
        assertEquals(1, result.getM20());
        assertEquals(1, result.getM21());
        assertEquals(0, result.getM22()); // 1 - 1
    }

    @Test
    void mult() {
        // Matrix-Vektor-Multiplikation mit Identitätsmatrix
        Mat3x3D mat = Mat3x3D.IDENTITY;
        Vec3D vec = new Vec3D(1, 2, 3);  // Testvektor
        Vec3D result = mat.mult(vec);   // Sollte gleich bleiben

        // Erwartung: Ergebnis = Eingabevektor
        assertEquals(1, result.getX());
        assertEquals(2, result.getY());
        assertEquals(3, result.getZ());
    }

    @Test
    void rotationMatrix() {
        // Test: Rotation um 90 Grad (gegen Uhrzeigersinn) um die Z-Achse
        Mat3x3D rot = Mat3x3D.RotationMatrix(90); // Rotationsmatrix für 90°
        Vec3D vec = new Vec3D(1, 0, 0);       // Vektor auf der X-Achse
        Vec3D rotated = rot.mult(vec);       // Ergebnis sollte (0,1,0) sein
        Mat3x3D rotM = Mat3x3D.RotationMatrix(60);

        // Mit Toleranz wegen Gleitkommazahlen
        assertEquals(0, rotated.getX(), 0.001);
        assertEquals(1, rotated.getY(), 0.001);
        assertEquals(0, rotated.getZ(), 0.001);
    }

    @Test
    void scaleMatrix() {
        // Test: Skalierung mit Vektor (2, 3, 4)
        Vec3D scale = new Vec3D(2, 3, 4);      // Skalierungsfaktor
        Mat3x3D scaleMatrix = Mat3x3D.ScaleMatrix(scale);
        Vec3D vec = new Vec3D(1, 1, 1);        // Testvektor
        Vec3D result = scaleMatrix.mult(vec); // Sollte (2,3,4) ergeben

        assertEquals(2, result.getX());
        assertEquals(3, result.getY());
        assertEquals(4, result.getZ());
    }

    @Test
    void translationMatrix() {
        // Test: 2D-Translation (z wird ignoriert, homogener Vektor = (x,y,1))
        Vec3D translation = new Vec3D(5, 7, 0);    // Verschiebung um (5,7)
        Mat3x3D transMat = Mat3x3D.TranslationMatrix(translation);

        Vec3D point = new Vec3D(1, 2, 1); // Homogener Punkt (x,y,1)
        Vec3D result = transMat.mult(point); // Erwartet: (1+5, 2+7, 1)

        assertEquals(6, result.getX()); // 1 + 5
        assertEquals(9, result.getY()); // 2 + 7
        assertEquals(1, result.getZ()); // bleibt 1
    }
}