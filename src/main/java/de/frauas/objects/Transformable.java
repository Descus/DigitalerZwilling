package de.frauas.objects;

import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

public abstract class Transformable {
    protected Transformable parent = null;
    @Getter
    protected Transform2D transform = new Transform2D();
    
    public Vec3D toGlobalSpace(Vec3D point){
        Vec3D transformedPoint = transform.transformPoint(point);
        return parent == null
                ? point
                : parent.toGlobalSpace(transformedPoint);
    }

    public Vec3D toLocalSpace(Vec3D point) {
        if (parent != null) {
            point = parent.toLocalSpace(point);
            return transform.transformPointReverse(point);
        }
        return point;
    }

    public Vec3D getWorldPosition(){
        return toGlobalSpace(Vec3D.identity);
    }

    public Vec3D forward(){
        Vec3D localForward = transform.forward();
        return parent == null
                ? localForward
                : parent.toGlobalSpace(localForward);
    }

    public Vec3D getWorldScale(){
        if (parent == null)
            return transform.scalePoint(Vec3D.one);
        return parent.transform.scalePoint(transform.scalePoint(Vec3D.one));
    }
}
