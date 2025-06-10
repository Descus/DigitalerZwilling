package de.frauas.objects.datastructures;

import de.frauas.Settings;
import lombok.Getter;

import java.awt.*;
import java.util.function.Function;

@Getter // Lombok generiert Getter für x, y, z
public class Vec3D {

    // Vordefinierte Standard-Vektoren
    public static final Vec3D zero = new Vec3D(0, 0, 0);     // Nullvektor
    public static final Vec3D one = new Vec3D(1, 1, 0);       // Vektor (1,1,0)
    public static final Vec3D right = new Vec3D(1, 0, 0);     // X-Richtung
    public static final Vec3D left = new Vec3D(-1, 0, 0);     // -X-Richtung
    public static final Vec3D up = new Vec3D(0, 1, 0);        // Y-Richtung
    public static final Vec3D down = new Vec3D(0, -1, 0);     // -Y-Richtung

    // Komponenten des Vektors
    private final double x, y, z;

    // Konstruktor
    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Vektoraddition
    public Vec3D add(Vec3D other) {
        return new Vec3D(this.x + other.x , this.y + other.y, this.z + other.z);
    }

    // Vektorsubtraktion
    public Vec3D subtract(Vec3D other){
        return new Vec3D(this.x - other.x , this.y - other.y, this.z - other.z);
    }

    // Skalierung des Vektors mit einem Faktor
    public Vec3D scale(double factor) {
        return new Vec3D(this.x * factor, this.y * factor, this.z * factor);
    }

    // Skalarprodukt (dot product) zweier Vektoren
    public double dotProd(Vec3D other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    // Länge des Vektors (euklidische Norm)
    public double length() {
        return Math.sqrt(lengthSq());
    }

    // Quadrat der Länge (schneller als `length()`, da ohne Wurzel)
    public double lengthSq(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    // Betrag (absolute Werte) jedes Vektorelements
    public Vec3D abs() {
        return new Vec3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }

    // Elementweises Maximum zwischen zwei Vektoren
    public Vec3D max(Vec3D other) {
        return new Vec3D(Math.max(this.x, other.x), Math.max(this.y, other.y), Math.max(this.z, other.z));
    }

    // Elementweises Minimum zwischen zwei Vektoren
    public Vec3D min(Vec3D other) {
        return new Vec3D(Math.min(this.x, other.x), Math.min(this.y, other.y), Math.min(this.z, other.z));
    }

    // Größter Wert der drei Komponenten (x, y, z)
    public double maxComponent() {
        return Math.max(Math.max(this.x, this.y), z);
    }

    // Kleinster Wert der drei Komponenten
    public double minComponent() {
        return Math.min(Math.min(this.x, this.y), z);
    }

    // 2D-Rotation um die Z-Achse (z bleibt unverändert)
    public Vec3D rotate(double angleDeg) {
        double angle = Math.toRadians(angleDeg); // Umrechnung in Bogenmaß
        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);
        return new Vec3D(
                this.x * cosA - this.y * sinA,
                this.x * sinA + this.y * cosA,
                this.z
        );
    }

    // Rückgabe eines normierten Vektors (Länge = 1)
    public Vec3D normalize() {
        double length = length();
        return new Vec3D(this.x / length, this.y / length, this.z / length);
    }

    // Umkehrung aller Vorzeichen (Negation)
    public Vec3D negate() {
        return new Vec3D(-this.x, -this.y, -this.z);
    }

    /**
     * Gibt einen senkrechten Vektor im 2D-Raum zurück.
     * Beispiel: (1,0) → (0,-1)
     */
    public Vec3D perpendicular(){
        // x und y werden vertauscht, y bekommt ein negatives Vorzeichen
        return new Vec3D(this.y, -this.x, 0);
    }

    /**
     * Zeichnet den Punkt als Kreis im GUI.
     * Die Position wird um den halben Radius verschoben, um den Kreis zu zentrieren.
     */
    public void draw(Graphics g){
        Vec3D transformedPoint = this.subtract(new Vec3D(
                (double) Settings.POINT_DEBUG_RADIUS / 2,
                (double) Settings.POINT_DEBUG_RADIUS / 2,
                0));
        g.fillOval(
                (int) transformedPoint.getX(),
                (int) transformedPoint.getY(),
                Settings.POINT_DEBUG_RADIUS,
                Settings.POINT_DEBUG_RADIUS
        );
    }

    // Vergleich zweier Vektoren auf Gleichheit (alle Komponenten gleich)
    public boolean equals(Vec3D other){
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    // Spiegelung: Gibt einen reflektierten Vektor p bezogen auf this zurück
    public Vec3D reflect(Vec3D p){
        return p.scale(2).subtract(this);
    }

    // Rückgabe als String zur Anzeige oder Debugging
    public String toString(){
        return String.format("(%.1f, %.1f, %.1f)", x, y, z);
    }
}
