package de.frauas.ultrasonic;

import de.frauas.scenario.components.Obstacle;
import de.frauas.scenario.primitives.Vec2;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class SonicCalculation {

    @Getter
    private static Set<Obstacle> objects = new HashSet<>();


    /**
     * This method has to be used to add all obstacles to a list when loading the scenario
     *
     * @param object Obstacle
     **/
    public static void addObject(Obstacle object) {
        objects.add(object);
    }

    /**
     * Optional: Security feature to clear the list before inserting new obstacles from new scenario
     **/
    public static void clearObjects() {
        objects.clear();
    }

    /**
     * This method can be used to check whether there are any obstacles too close to the car and sends a boolen
     * if the car needs to stop
     *
     * @param car Vec2 of the current location of the car
     * @param headingDegrees Heading degree of the car
     * @return Boolean whether the car needs to stop
     */
    public static boolean stop(Vec2 car, double headingDegrees) {

        // Check all obstacles if they are too close
        for (Obstacle obstacles : objects) {
            double dx = obstacles.position.x() - car.x();
            double dy = obstacles.position.y() - car.y();

            double distance = Math.sqrt(dx * dx + dy * dy);

            // If obstacle is too far away ignore the rest
            if (distance > 3) return false;

            // Analzye the heading direction of the heading degree
            double headingRad = Math.toRadians(headingDegrees);
            double viewX = Math.cos(headingRad);
            double viewY = Math.sin(headingRad);

            // Normalize
            double targetVecX = dx / distance;
            double targetVecY = dy / distance;

            // Skalarproduct for determine the angle
            double dot = viewX * targetVecX + viewY * targetVecY;
            double angleRad = Math.acos(dot);
            double angleDeg = Math.toDegrees(angleRad);

            return angleDeg <= 15;
        }

        return false;
    }
}
