package de.frauas.objects.obstacle;

import de.frauas.objects.datastructures.Vec3D;

/**
 * The ISdf interface defines a method to compute the Signed Distance Field (SDF) value
 * for a given position in 3D space. The SDF value represents the shortest distance from
 * the given position to the nearest surface or boundary, with negative values indicating
 * positions inside the boundary and positive values indicating positions outside.
 * <p>
 * @author Scenario
 */
public interface ISdf {
    double getSDF(Vec3D otherPosition);
}
