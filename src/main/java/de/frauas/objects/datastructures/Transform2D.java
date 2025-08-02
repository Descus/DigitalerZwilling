package de.frauas.objects.datastructures;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.AffineTransform;

/**
 * Represents a 2D transformation that includes translation, rotation, and scaling.
 * Allows for manipulation and computation of 2D transformations using matrices.
 *
 * @author Scenario
 */
@Getter @Setter
public class Transform2D {
    /**
     * Represents the translation component of the 2D transformation.
     * This vector is used to adjust the position of an object within a 2D space.
     * By default, it is initialized to zero, indicating no translation.
     */
    private Vec3D translation = Vec3D.zero;
    /**
     * Represents the rotational component of the transformation in degrees.
     * It defines the rotation applied to a 2D transformation and is used in
     * various rotation-related operations within the Transform2D class.
     */
    private double rotation = 0;
    /**
     * Represents the scaling factor of the Transform2D object.
     * This variable determines the scale to be applied along the x, y, and possibly z axes
     * when performing transformations. It defaults to a scale of 1 (no scaling).
     */
    private Vec3D scale = Vec3D.one;
    
    /**
     * Represents the translation component of a 2D transformation as a 3x3 matrix.
     * This matrix is used to perform translation transformations on points in a 2D
     * space by multiplying it with a vector. By default, it is initialized to the
     * identity matrix.
     * <p>
     * The translation matrix can be updated to represent specific translation
     * transformations by applying a translation vector.
     */
    private Mat3x3D translationMatrix = Mat3x3D.IDENTITY;
    /**
     * Represents a 3x3 transformation matrix specifically used to handle scaling operations
     * in 2D space. This matrix is initialized to the identity matrix, which implies no scaling
     * transformation is applied by default.
     * <p>
     * The scaleMatrix is primarily used within the Transform2D class to compute and apply
     * scaling transformations to objects or points in 2D space using homogeneous coordinates.
     */
    private Mat3x3D scaleMatrix = Mat3x3D.IDENTITY;
    /**
     * Represents the 2D rotation component of this transform as a 3x3 matrix.
     * The matrix is initialized to the identity matrix, indicating no rotation.
     * This matrix is used to perform rotation transformations in 2D space.
     */
    private Mat3x3D rotationMatrix = Mat3x3D.IDENTITY;

    /**
     * Represents the inverse of the translation matrix for a 2D transformation.
     * This matrix is used to reverse the effects of translation operations by
     * applying the inverse translation.
     * <p>
     * By default, this variable is initialized to {@code Mat3x3D.IDENTITY},
     * representing no inverse translation (identity transformation).
     */
    private Mat3x3D translationMatrixInverse = Mat3x3D.IDENTITY;
    /**
     * Represents the inverse of the scale matrix used in 2D transformations.
     * This matrix is initialized to the identity matrix {@link Mat3x3D#IDENTITY},
     * meaning no scaling is applied by default.
     * <p>
     * This matrix is used to reverse any scaling transformations applied via the
     * scale matrix in the {@code Transform2D} class.
     */
    private Mat3x3D scaleMatrixInverse = Mat3x3D.IDENTITY;
    /**
     * Stores the inverse of the rotation matrix used for 2D transformations.
     * This matrix is initialized with the identity matrix and represents the inverse
     * of the transformation's current*/
    private Mat3x3D rotationMatrixInverse = Mat3x3D.IDENTITY;

    /**
     * Default constructor for the Transform2D class.
     * Initializes a new 2D transformation with default values.
     * By default, no translation, scaling, or rotation is applied to the transformation.
     */
    public Transform2D() {}

    /**
     * Constructs a Transform2D object with a given translation vector.
     * <p>
     * @param translation The translation vector to initialize the transformation with.
     *                    It represents the amount of movement along the x, y, and z axes.
     */
    public Transform2D(Vec3D translation) {
        setTranslation(translation);
    }
    
    /**
     * Constructs a Transform2D object with a given translation vector and rotation angle.
     * <p>
     * @param translation The translation vector to initialize the transformation with. Represents
     *                    the amount of movement along the x, y, and z axes.
     * @param rotation    The rotation angle in degrees to initialize the transformation with.
     *                    Positive values rotate counterclockwise, while negative values
     *                    rotate clockwise.
     */
    public Transform2D(Vec3D translation, double rotation) {
        setTranslation(translation);
        setRotation(rotation);
    }

    /**
     * Constructs a Transform2D object with a given translation vector, scale vector,
     * and rotation angle. This constructor initializes the transformation properties
     * and applies the specified operations in order.
     * <p>
     * @param translation The translation vector to initialize the transformation with.
     *                    Represents the amount of movement along the x, y, and z axes.
     * @param scale       The scaling factors to initialize the transformation with.
     *                    Represents the scaling to be applied along the x, y, and z axes.
     * @param rotation    The rotation angle in degrees to initialize the transformation with.
     *                    Positive values rotate counterclockwise, while negative values
     *                    rotate clockwise.
     */
    public Transform2D(Vec3D translation, Vec3D scale, double rotation) {
        setScale(scale);
        setRotation(rotation);
        setTranslation(translation);
    }

    /**
     * Sets the rotation angle for the 2D transformation. Updates the rotation matrix
     * and its inverse to reflect the specified rotation angle.
     * <p>
     * @param rotation The new rotation angle in degrees. Positive values represent
     *                 counterclockwise rotation, while negative values represent
     *                 clockwise rotation.
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.rotationMatrix = Mat3x3D.RotationMatrix(rotation);
        this.rotationMatrixInverse = rotationMatrix.inverse();
    }

    /**
     * Sets the translation vector for the 2D transformation. Updates the internal
     * translation matrix and its inverse to reflect the specified translation.
     * <p>
     * @param translation The new translation vector. It represents the amount of
     *                    movement along the x, y, and z axes.
     */
    public void setTranslation(Vec3D translation) {
        this.translation = translation;
        this.translationMatrix = Mat3x3D.TranslationMatrix(translation);
        this.translationMatrixInverse = translationMatrix.inverse();
    }
    
    /**
     * Sets the scaling factors for the 2D transformation. Updates the scale matrix
     * and its inverse to reflect the specified scaling.
     * <p>
     * @param scale The new scaling factors to be applied. It represents the scaling
     *              along the x, y, and z axes.
     */
    public void setScale(Vec3D scale) {
        this.scale = scale;
        this.scaleMatrix = Mat3x3D.ScaleMatrix(scale);
        this.scaleMatrixInverse = scaleMatrix.inverse();
    }

    /**
     * Computes the forward direction vector for the current transformation.
     * The forward direction is determined by applying the transformation's
     * rotation matrix to the unit vector pointing up, represented by Vec3D.up.
     * <p>
     * @return The forward direction as a vector after applying the current rotation.
     */
    public Vec3D forward(){
        return rotationMatrix.mult(Vec3D.up);
    }
    
    /**
     * Calculates the right direction vector for the current transformation.
     * The right direction is derived by finding a vector perpendicular
     * to the forward direction vector.
     * <p>
     * @return The right direction vector as a Vec3D.
     */
    public Vec3D right(){
        return forward().perpendicular();
    }
    
    /**
     * Applies a translation to the current transformation by adding the specified
     * translation vector to the existing translation vector.
     * <p>
     * @param translation The vector by which to translate. It represents the amount
     *                    of movement along the x, y, and z axes.
     */
    public void translate(Vec3D translation){
        setTranslation(this.translation.add(translation));
    }
    
    /**
     * Translates the current transformation by the specified x and y components.
     * This method creates a 3D translation vector with the given x and y values
     * and a z value of 0, and applies it to the transformation.
     * <p>
     * @param x The amount of translation along the x-axis.
     * @param y The amount of translation along the y-axis.
     */
    public void translate(double x, double y){
        translate(new Vec3D(x, y, 0));
    }

    /**
     * Rotates the transformation by the specified angle in degrees and updates
     * the rotation property. The resulting rotation angle is kept within the range
     * of [0, 360) degrees.
     * <p>
     * @param angleDeg The angle to rotate by, in degrees. Positive values rotate
     *                 counterclockwise, while negative values rotate clockwise.
     */
    public void rotate(double angleDeg){
        this.rotation += angleDeg;
        setRotation(this.rotation %= 360);
    }
    
    /**
     * Transforms a given 3D point by applying scaling, rotation, and translation
     * in sequence. This method combines the individual transformations into one
     * cohesive operation and returns the transformed point.
     * <p>
     * @param point The 3D point to be transformed. It represents the position
     *              in space to which the transformations (scaling, rotation,
     *              and translation) are applied.
     * @return The transformed 3D point after applying the scaling, rotation,
     *         and translation operations in sequence.
     */
    public Vec3D transformPoint(Vec3D point){
        return translatePoint(rotatePoint(scalePoint(point)));
    }

    /**
     * Transforms the given 3D point by applying the inverse translation,
     * rotation, and scaling operations in sequence. This method reverses
     * the forward transformation process, effectively mapping the point
     * back to its original position in the untransformed space.
     * <p>
     * @param point The 3D point to be transformed in reverse. It represents the
     *              position in space where the inverse operations (inverse
     *              translation, rotation, and scaling) are applied.
     * @return The transformed 3D point after applying the inverse transformations
     *         (inverse scaling, rotation, and translation) in sequence.
     */
    public Vec3D transformPointReverse(Vec3D point){
        return scalePointReverse(rotatePointReverse(translatePointReverse(point)));
    }

    /**
     * Scales the given 3D point by applying the current scale transformation.
     * This method uses the scale matrix to multiply the input point,
     * effectively altering its size according to the defined scaling factors.
     * <p>
     * @param point The 3D point to be scaled. It represents the position in space
     *              where the scaling transformation is applied.
     * @return The scaled 3D point after applying the scaling transformation.
     */
    public Vec3D scalePoint(Vec3D point){
        return scaleMatrix.mult(point);
    }

    /**
     * Translates a given 3D point using the current translation matrix.
     * The translation is applied by multiplying the input point with the translation matrix.
     * <p>
     * @param point The 3D point to be translated. It represents the position in space
     *              to which the current translation transformation is applied.
     * @return The translated 3D point after applying the translation transformation.
     */
    public Vec3D translatePoint(Vec3D point){
        return translationMatrix.mult(point);
    }

    /**
     * Rotates the given 3D point using the current rotation matrix.
     * <p>
     * @param point The 3D point to be rotated. It represents the position in space
     *              where the rotation transformation is applied.
     * @return The rotated 3D point after applying the rotation transformation.
     */
    public Vec3D rotatePoint(Vec3D point){
        return rotationMatrix.mult(point);
    }

    /**
     * Scales a given 3D point by applying the inverse of the current scaling transformation.
     * This method uses the inverse of the scale matrix to multiply the input point,
     * effectively reversing the scaling applied to the point.
     * <p>
     * @param point The 3D point to be scaled in reverse. It represents the position in space
     *              where the inverse scaling transformation is applied.
     * @return The scaled 3D point after applying the inverse scaling transformation.
     */
    public Vec3D scalePointReverse(Vec3D point){
        return scaleMatrixInverse.mult(point);
    }

    /**
     * Applies the inverse translation transformation to the given 3D point.
     * The method multiplies the provided point by the inverse of the translation matrix,
     * effectively reversing any translation transformations applied to the point.
     * <p>
     * @param point The 3D point to be reverse-translated. It represents the position in space
     *              where the inverse translation transformation is applied.
     * @return The translated 3D point after applying the inverse translation transformation.
     */
    public Vec3D translatePointReverse(Vec3D point){
        return translationMatrixInverse.mult(point);
    }

    /**
     * Rotates the given 3D point using the inverse of the current rotation matrix.
     * This operation reverses the rotation applied to the point, effectively
     * transforming it back to its original orientation relative to the current
     * rotation.
     * <p>
     * @param point The 3D point to be rotated in reverse. It represents the
     *              position in space where the inverse rotation transformation
     *              is applied.
     * @return The rotated 3D point after applying the inverse rotation
     *         transformation.
     */
    public Vec3D rotatePointReverse(Vec3D point){
        return rotationMatrixInverse.mult(point);
    }

    /**
     * Converts the current 2D transformation, which includes translation, rotation,
     * and scaling operations, into an AffineTransform object. The resulting
     * AffineTransform represents the combination of all transformations
     * applied in the order they occur.
     * <p>
     * @return An AffineTransform object that combines the translation,
     * rotation, and scaling properties of the current Transform2D.
     */
    public AffineTransform toAffineTransform(){
        AffineTransform at = new AffineTransform();
        at.translate(translation.getX(), translation.getY());
        at.rotate(Math.toRadians(rotation));
        at.scale(scale.getX(), scale.getY());
        return at;
    }
}
