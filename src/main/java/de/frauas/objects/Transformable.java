package de.frauas.objects;

import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

public abstract class Transformable {
    protected Transformable parent = null;
    @Getter
    protected Transform2D transform = new Transform2D();
    
    public Vec3D transformPoint(Vec3D point){
        Vec3D transformedPoint = transform.transformPoint(point);
        return parent == null
                ? transformedPoint
                : parent.transformPoint(transformedPoint);
    }

    public Vec3D getWorldPosition(){
        return transformPoint(Vec3D.identity);
    }

    public Vec3D getWorldScale(){
        if (parent == null)
            return transform.scalePoint(Vec3D.one);
        return parent.transform.scalePoint(transform.scalePoint(Vec3D.one));
    }
}
