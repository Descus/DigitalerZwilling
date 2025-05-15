package de.frauas.ultrasonic;

import de.frauas.scenario.primitives.SDF;
import de.frauas.scenario.primitives.Vec2F;

import java.util.List;

public class UltraSonicSensor {
    
    public static final int MAX_DISTANCE = 300;
    public static final int MAX_ANGLE = 15;
    public float stepSize = 0.1f;
    private final Vec2F positionOffset;
    private final Vec2F forward;

    public UltraSonicSensor(Vec2F positionOffset, Vec2F forward) {
        this.positionOffset = positionOffset;
        this.forward = forward;
    }
    
    public boolean isObstacleInRange(Vec2F carPosition, List<SDF> signedDistanceFields){
        for (float angle = -MAX_ANGLE; angle < 2*MAX_ANGLE ; angle += stepSize) {
            if(castRay(carPosition, forward.rotate(angle), signedDistanceFields)) {
                return true;
            }
        }
        return false;
    }

    private boolean castRay(Vec2F carPosition, Vec2F direction, List<SDF> signedDistanceFields) {
        float travelDistance = 0;
        Vec2F currentPosition = carPosition.add(positionOffset);

        while (travelDistance < MAX_DISTANCE){
            float smallestDistance = MAX_DISTANCE;
            for (SDF sdf : signedDistanceFields) {
                float currentSdf = sdf.getSDF(currentPosition);
                if (currentSdf <= 0)
                    return true;
                if (currentSdf < smallestDistance){
                    smallestDistance = currentSdf;
                }
            }
            travelDistance += smallestDistance;
            currentPosition = currentPosition.add(direction.scale(travelDistance));
        }
        return false;
    }
}
