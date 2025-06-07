package de.frauas.objects;

import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;

public abstract class Transformable {
    protected Transformable parent = null;
    protected Transform2D transform = new Transform2D();
    
    public Vec3D transformPoint(Vec3D point){
        if (parent == null)
            return transform.transform(point);
        return parent.transformPoint(transform.transform(point));
    }
}
