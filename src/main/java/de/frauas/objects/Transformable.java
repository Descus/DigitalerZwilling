package de.frauas.objects;

import de.frauas.objects.datastructures.Transform2D;
import de.frauas.objects.datastructures.Vec3D;
import lombok.Getter;

/**
 * An abstract class that represents a transformable object in a 2D or 3D space.
 * Each Transformable instance can have a parent Transformable, forming a transformation hierarchy.
 * Provides methods to transform points and vectors between local and global coordinate spaces.
 *
 * @author Scenario
 */
public abstract class Transformable {
    /**
     * Represents the parent in the transformation hierarchy for this {@code Transformable} object.
     * The parent is another {@code Transformable} instance or {@code null} if this object is the root of the hierarchy.
     * It is used to define relationships between transformable objects, enabling coordinated transformations
     * in local and global coordinate spaces.
     */
    protected Transformable parent = null;
    /**
     * Represents the local 2D transformation for this object.
     * The transformation includes translation, rotation, and scale components
     * and encapsulates methods to manipulate and query its properties.
     * Used to perform transformations in local 2D coordinate space and coordinate
     * conversions to and from global space in combination with hierarchical relationships.
     */
    @Getter
    protected Transform2D transform = new Transform2D();
    
    /**
     * Transforms a point from the local coordinate space of this object
     * to the global coordinate space, taking into account the transformations
     * of all parent objects in the hierarchy.
     *
     * @param point the point in the local coordinate space to be transformed to global space
     * @return the transformed point in the global coordinate space
     */
    public Vec3D toGlobalSpace(Vec3D point){
        Vec3D transformedPoint = transform.transformPoint(point);
        return parent == null
                ? point
                : parent.toGlobalSpace(transformedPoint);
    }

    /**
     * Transforms a point from the global coordinate space to the local coordinate space
     * of this object, taking into account the transformations of all parent objects
     * in the hierarchy.
     *
     * @param point the point in the global coordinate space to be transformed to local space
     * @return the transformed point in the local coordinate space
     */
    public Vec3D toLocalSpace(Vec3D point) {
        if (parent != null) {
            point = parent.toLocalSpace(point);
            return transform.transformPointReverse(point);
        }
        return point;
    }

    /**
     * Computes the global position of the current {@code Transformable} object by transforming
     * the local origin point ({@code Vec3D.identity}) to the global coordinate space,
     * taking into account the transformations of all parent objects in the hierarchy.
     *
     * @return the global position as a {@code Vec3D} object, representing the position
     *         of this object in the global coordinate space.
     */
    public Vec3D getWorldPosition(){
        return toGlobalSpace(Vec3D.identity);
    }

    /**
     * Computes the forward vector of the current object in the local or global coordinate space.
     * If the object has a parent, the forward vector is transformed to the global coordinate
     * space based on the parent's transformations. Otherwise, the forward vector is returned
     * in the local coordinate space.
     *
     * @return the forward vector of the object, either in the global or local coordinate space
     */
    public Vec3D forwardVector(){
        Vec3D localForward = transform.forward();
        return parent == null
                ? localForward
                : parent.toGlobalSpace(localForward);
    }

    /**
     * Computes the global scale of the current {@code Transformable} object by
     * recursively applying scaling transformations of its parent objects, if any.
     * If the object does not have a parent, the local scale is returned as the global scale.
     *
     * @return the global scale as a {@code Vec3D} object, representing the scale
     *         of this object in the global coordinate space.
     */
    public Vec3D getWorldScale(){
        if (parent == null)
            return transform.scalePoint(Vec3D.one);
        return parent.transform.scalePoint(transform.scalePoint(Vec3D.one));
    }
}
