package de.frauas.ultrasonic;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class SonicCalculation {

    @Getter
    private static Set<SonicObject> objects = new HashSet<>();

    public static void addObject(SonicObject object) {
        objects.add(object);
    }

    public static void clearObjects() {
        objects.clear();
    }

    public static SonicObject getNearestObject(SonicObject current, double headingDegrees) {

        Set<SonicObject> visible = new HashSet<>();
        for (SonicObject object : objects) {
            if (isVisible(current, object, headingDegrees)) {
                visible.add(object);
            }
        }

        SonicObject toReturn = null;
        double min = Double.MAX_VALUE;
        for (SonicObject object : visible) {
            if (object.distanceTo(current) < min) {
                min = object.distanceTo(current);
                toReturn = object;
            }
        }
        return toReturn;
    }

    public static boolean isVisible(SonicObject current, SonicObject target, double headingDegrees) {
        double dx = target.x - current.x;
        double dy = target.y - current.y;
        double dz = target.z - current.z;

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance > 10) return false;

        // Richtungsvektor aus Heading (2D-Sicht, Z wird ignoriert)
        double headingRad = Math.toRadians(headingDegrees);
        double viewX = Math.cos(headingRad);
        double viewY = Math.sin(headingRad);

        // Zielvektor normalisieren
        double targetVecX = dx / distance;
        double targetVecY = dy / distance;

        // Skalarprodukt zur Winkelbestimmung
        double dot = viewX * targetVecX + viewY * targetVecY;
        double angleRad = Math.acos(dot);
        double angleDeg = Math.toDegrees(angleRad);

        return angleDeg <= 15;
    }
}
