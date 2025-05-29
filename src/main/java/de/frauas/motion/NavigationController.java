package de.frauas.motion;

import de.frauas.scenario.primitives.Vec2F;

public class NavigationController {

    private InfraredSensorSimulator sensorSim;
    private Vec2F currentPosition;
    private double currentRotationDeg;

    private static final double STEP_DISTANCE_CM = 2.8;
    //28 wäre richtig vermutlich da 28mm -> 2.8cm
    private static final double STEP_DISTANCE_TURN_CM = 1.4;
    private static final double ROTATION_LEFT_DEG = 0.7;
    private static final double ROTATION_RIGHT_DEG = 0.7;

    public NavigationController(InfraredSensorSimulator simulator, Vec2F startPosition, double startRotationDeg) {
        this.sensorSim = simulator;
        this.currentPosition = startPosition;
        this.currentRotationDeg = startRotationDeg;
    }

    public void step() {
        sensorSim.updateCarState(currentPosition, currentRotationDeg);
        int[] s = sensorSim.getSensorStates();
        System.out.println("Alte position: -> " + currentPosition);
        System.out.printf("Sensor: %d%d%d -> ", s[0], s[1], s[2]);

        if (s[0] == 1 && s[1] == 0 && s[2] == 0) {
            System.out.println("LEFT");
            rotateLeft();
        } else if (s[0] == 1 && s[1] == 1 && s[2] == 0) {
            System.out.println("LEFT");
            rotateLeft();
        } else if (s[0] == 0 && s[1] == 1 && s[2] == 0) {
            System.out.println("STRAIGHT");
            moveForward();
        } else if (s[0] == 0 && s[1] == 1 && s[2] == 1) {
            System.out.println("RIGHT");
            rotateRight();
        } else if (s[0] == 0 && s[1] == 0 && s[2] == 1) {
            System.out.println("RIGHT");
            rotateRight();
        } else if (s[0] == 1 && s[1] == 1 && s[2] == 1) {
            System.out.println("STRAIGHT");
            moveForward();
        } else if (s[0] == 0 && s[1] == 0 && s[2] == 0) {
            System.out.println("STOP");
            // nichts tun
        } else {
            System.out.println("STOP");
        }
    }

    private void moveForward() {
        double angleRad = Math.toRadians(currentRotationDeg);
        float dx = (float)(Math.sin(angleRad) * STEP_DISTANCE_CM);
        float dy = (float)(Math.cos(angleRad) * STEP_DISTANCE_CM);
        currentPosition = new Vec2F(currentPosition.x() + dx, currentPosition.y() + dy);
    }

    private void rotateLeft() {
        currentRotationDeg = (currentRotationDeg - ROTATION_LEFT_DEG) % 360;
        //double angleRad = Math.toRadians(currentRotationDeg);
        //float dx = (float)(Math.sin(angleRad) * STEP_DISTANCE_TURN_CM);
        //float dy = (float)(Math.cos(angleRad) * STEP_DISTANCE_TURN_CM);
        //currentPosition = new Vec2F(currentPosition.x() + dx, currentPosition.y() + dy);
    }

    private void rotateRight() {
        currentRotationDeg = (currentRotationDeg + ROTATION_RIGHT_DEG + 360) % 360;
        //double angleRad = Math.toRadians(currentRotationDeg);
        //float dx = (float)(Math.sin(angleRad) * STEP_DISTANCE_TURN_CM);
        //float dy = (float)(Math.cos(angleRad) * STEP_DISTANCE_TURN_CM);
        //currentPosition = new Vec2F(currentPosition.x() + dx, currentPosition.y() + dy);
    }

    public Vec2F getCurrentPosition() {
        return currentPosition;
    }

    public double getCurrentRotationDeg() {
        return currentRotationDeg;
    }
}
