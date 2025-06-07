package de.frauas.objects.parts;

import de.frauas.objects.ISdf;
import de.frauas.objects.Transformable;
import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.interfaces.IUltrasonicSensor;


public class UltrasonicSensor extends Transformable implements IUltrasonicSensor {
    
    public static final int MAX_DISTANCE = 300;
    public static final int MAX_ANGLE = 15;
    public double stepSize = 0.1f;

    public UltrasonicSensor(Transformable parent, Vec3D positionOffset, double orientationAngle) {
        this.parent = parent;
        transform.setTranslation(positionOffset);
        transform.setRotation(orientationAngle);
    }

    @Override
    public boolean isObstacleInRange(ISdf ISDF){
        for (double angle = -MAX_ANGLE; angle < MAX_ANGLE; angle += stepSize) {
            if(castRay(transform.forward().rotate(angle), ISDF)) {
                return true;
            }
        }
        return false;
    }

    private boolean castRay(Vec3D direction, ISdf ISDF) {
        double travelDistance = 0;
        Vec3D currentPosition = transformPoint(Vec3D.zero);

        while (travelDistance < MAX_DISTANCE){
            double smallestDistance = MAX_DISTANCE;
            double currentSdf = ISDF.getSDF(currentPosition);
            if (currentSdf <= 0)
                return true;
            if (currentSdf < smallestDistance){
                smallestDistance = currentSdf;
            }
            travelDistance += smallestDistance;
            currentPosition = currentPosition.add(direction.scale(travelDistance));
        }
        return false;
    }
}
