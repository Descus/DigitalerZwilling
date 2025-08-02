package de.frauas.objects.datastructures;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.AffineTransform;

/**
 * @author Scenario-Group
 * Represents a 2D transformation, including translation, rotation, and scaling.
 * This class encapsulates the transformation parameters and provides methods
 * to manipulate them and apply the transformation to points. It uses 3x3 matrices
 * for the underlying calculations to work with homogeneous coordinates.
 */
@Getter @Setter
public class Transform2D {
    /**
     * The translation component of the transformation.
     */
    private Vec3D translation = Vec3D.zero;
    /**
     * The rotation component of the transformation, in degrees.
     */
    private double rotation = 0;
    /**
     * The scale component of the transformation.
     */
    private Vec3D scale = Vec3D.one;

    /**
     * The matrix representing the translation transformation.
     */
    private Mat3x3D translationMatrix = Mat3x3D.IDENTITY;
    /**
     * The matrix representing the scaling transformation.
     */
    private Mat3x3D scaleMatrix = Mat3x3D.IDENTITY;
    /**
     * The matrix representing the rotation transformation.
     */
    private Mat3x3D rotationMatrix = Mat3x3D.IDENTITY;

    /**
     * The inverse of the translation matrix.
     */
    private Mat3x3D translationMatrixInverse = Mat3x3D.IDENTITY;
    /**
     * The inverse of the scaling matrix.
     */
    private Mat3x3D scaleMatrixInverse = Mat3x3D.IDENTITY;
    /**
     * The inverse of the rotation matrix.
     */
    private Mat3x3D rotationMatrixInverse = Mat3x3D.IDENTITY;

    /**
     * Default constructor. Initializes the transform with no translation, rotation, or scaling.
     */
    public Transform2D() {}

    /**
     * Constructs a new transform with a specified translation.
     *
     * @param translation The initial translation vector.
     */
    public Transform2D(Vec3D translation) {
        setTranslation(translation);
    }

    /**
     * Constructs a new transform with a specified translation and rotation.
     *
     * @param translation The initial translation vector.
     * @param rotation    The initial rotation angle in degrees.
     */
    public Transform2D(Vec3D translation, double rotation) {
        setTranslation(translation);
        setRotation(rotation);
    }

    /**
     * Constructs a new transform with specified translation, scaling, and rotation.
     *
     * @param translation The initial translation vector.
     * @param scale       The initial scale vector.
     * @param rotation    The initial rotation angle in degrees.
     */
    public Transform2D(Vec3D translation, Vec3D scale, double rotation) {
        setScale(scale);
        setRotation(rotation);
        setTranslation(translation);
    }

    /**
     * Sets the rotation of the transform and updates the corresponding matrices.
     *
     * @param rotation The new rotation angle in degrees.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.rotationMatrix = Mat3x3D.RotationMatrix(rotation);
        this.rotationMatrixInverse = rotationMatrix.inverse();
    }

    /**
     * Sets the translation of the transform and updates the corresponding matrices.
     *
     * @param translation The new translation vector.
     */
    public void setTranslation(Vec3D translation) {
        this.translation = translation;
        this.translationMatrix = Mat3x3D.TranslationMatrix(translation);
        this.translationMatrixInverse = translationMatrix.inverse();
    }

    /**
     * Sets the scale of the transform and updates the corresponding matrices.
     *
     * @param scale The new scale vector.
     */
    public void setScale(Vec3D scale) {
        this.scale = scale;
        this.scaleMatrix = Mat3x3D.ScaleMatrix(scale);
        this.scaleMatrixInverse = scaleMatrix.inverse();
    }

    /**
     * Calculates the forward direction vector based on the current rotation.
     *
     * @return A {@code Vec3D} representing the forward direction (rotated 'up' vector).
     */
    public Vec3D forward(){
        return rotationMatrix.mult(Vec3D.up);
    }

    /**
     * Calculates the right-hand direction vector, perpendicular to the forward vector.
     *
     * @return A {@code Vec3D} representing the right direction.
     */
    public Vec3D right(){
        return forward().perpendicular();
    }

    /**
     * Applies an additional translation to the current transform.
     *
     * @param translation The vector to add to the current translation.
     */
    public void translate(Vec3D translation){
        setTranslation(this.translation.add(translation));
    }

    /**
     * Applies an additional translation to the current transform.
     *
     * @param x The distance to translate along the x-axis.
     * @param y The distance to translate along the y-axis.
     */
    public void translate(double x, double y){
        translate(new Vec3D(x, y, 0));
    }

    /**
     * Applies an additional rotation to the current transform. The angle is wrapped to stay within 360 degrees.
     *
     * @param angleDeg The angle in degrees to add to the current rotation.
     */
    public void rotate(double angleDeg){
        this.rotation += angleDeg;
        setRotation(this.rotation %= 360);
    }

    /**
     * Transforms a point from local to world space by applying scaling, then rotation, then translation.
     *
     * @param point The point in local space.
     * @return The transformed point in world space.
     */
    public Vec3D transformPoint(Vec3D point){
        return translatePoint(rotatePoint(scalePoint(point)));
    }

    /**
     * Transforms a point from world to local space by applying the inverse of the transformation.
     *
     * @param point The point in world space.
     * @return The transformed point in local space.
     */
    public Vec3D transformPointReverse(Vec3D point){
        return scalePointReverse(rotatePointReverse(translatePointReverse(point)));
    }

    /**
     * Applies only the scaling component of the transform to a point.
     *
     * @param point The point to scale.
     * @return The scaled point.
     */
    public Vec3D scalePoint(Vec3D point){
        return scaleMatrix.mult(point);
    }

    /**
     * Applies only the translation component of the transform to a point.
     *
     * @param point The point to translate.
     * @return The translated point.
     */
    public Vec3D translatePoint(Vec3D point){
        return translationMatrix.mult(point);
    }

    /**
     * Applies only the rotation component of the transform to a point.
     *
     * @param point The point to rotate.
     * @return The rotated point.
     */
    public Vec3D rotatePoint(Vec3D point){
        return rotationMatrix.mult(point);
    }

    /**
     * Applies the inverse scaling component of the transform to a point.
     *
     * @param point The point to transform.
     * @return The inversely scaled point.
     */
    public Vec3D scalePointReverse(Vec3D point){
        return scaleMatrixInverse.mult(point);
    }

    /**
     * Applies the inverse translation component of the transform to a point.
     *
     * @param point The point to transform.
     * @return The inversely translated point.
     */
    public Vec3D translatePointReverse(Vec3D point){
        return translationMatrixInverse.mult(point);
    }

    /**
     * Applies the inverse rotation component of the transform to a point.
     *
     * @param point The point to transform.
     * @return The inversely rotated point.
     */
    public Vec3D rotatePointReverse(Vec3D point){
        return rotationMatrixInverse.mult(point);
    }

    /**
     * Converts this {@code Transform2D} into a {@code java.awt.geom.AffineTransform}.
     * This is useful for rendering with Java's 2D graphics API.
     *
     * @return An {@code AffineTransform} that represents this transformation.
     */
    public AffineTransform toAffineTransform(){
        AffineTransform at = new AffineTransform();
        at.translate(translation.getX(), translation.getY());
        at.rotate(Math.toRadians(rotation));
        at.scale(scale.getX(), scale.getY());
        return at;
    }
}