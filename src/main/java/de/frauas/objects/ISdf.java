package de.frauas.objects;

import de.frauas.objects.datastructures.Vec3D;

public interface ISdf {
    double getSDF(Vec3D otherPosition);
}
