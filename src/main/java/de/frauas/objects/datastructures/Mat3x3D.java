package de.frauas.objects.datastructures;

import lombok.Getter;

/**
 * @author Scenario-Group
 * Represents an immutable 3x3 matrix, primarily used for 2D affine transformations
 * using homogeneous coordinates. This class provides functionalities for basic matrix
 * operations and the creation of transformation matrices (rotation, scaling, translation).
 */
@Getter
public class Mat3x3D {
    /**
     * The elements of the matrix, stored in row-major order.
     */
    private final double m00, m01, m02, m10, m11, m12, m20, m21, m22;

    /**
     * Constructs a new 3x3 matrix with the specified elements.
     *
     * @param m00 Element at row 0, column 0.
     * @param m01 Element at row 0, column 1.
     * @param m02 Element at row 0, column 2.
     * @param m10 Element at row 1, column 0.
     * @param m11 Element at row 1, column 1.
     * @param m12 Element at row 1, column 2.
     * @param m20 Element at row 2, column 0.
     * @param m21 Element at row 2, column 1.
     * @param m22 Element at row 2, column 2.
     */
    public Mat3x3D(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
    }

    /**
     * A constant for the 3x3 identity matrix.
     */
    public static final Mat3x3D IDENTITY =
            new Mat3x3D(
                    1, 0, 0,  
                    0, 1, 0, 
                    0, 0, 1);
    
    /**
     * A constant for a 3x3 matrix with all elements set to zero.
     */
    public static final Mat3x3D ZERO =
            new Mat3x3D(
                    0, 0, 0, 
                    0, 0, 0,
                    0,0, 0);
    
    /**
     * A constant for a 3x3 matrix with all elements set to one.
     */
    public static final Mat3x3D ONE =
            new Mat3x3D(
                    1, 1, 1, 
                    1, 1, 1,
                    1, 1, 1);

    /**
     * Performs element-wise addition of this matrix with another.
     *
     * @param other The matrix to add to this one.
     * @return A new {@code Mat3x3D} instance representing the sum.
     */
    public Mat3x3D add(Mat3x3D other){
        return new Mat3x3D(
                m00 + other.m00, m01 + other.m01, m02 + other.m02, 
                m10 + other.m10, m11 + other.m11, m12 + other.m12, 
                m20 + other.m20, m21 + other.m21, m22 + other.m22);
    }

    /**
     * Performs element-wise subtraction of another matrix from this one.
     *
     * @param other The matrix to subtract.
     * @return A new {@code Mat3x3D} instance representing the difference.
     */
    public Mat3x3D sub(Mat3x3D other){
        return new Mat3x3D(
                m00 - other.m00, m01 - other.m01, m02 - other.m02,
                m10 - other.m10, m11 - other.m11, m12 - other.m12,
                m20 - other.m20, m21 - other.m21, m22 - other.m22);
    }

    /**
     * Multiplies this matrix by a column vector.
     *
     * @param other The {@code Vec3D} to multiply with.
     * @return A new {@code Vec3D} instance representing the transformed vector.
     */
    public Vec3D mult(Vec3D other){
        return new Vec3D(
                m00 * other.getX() + m01 * other.getY() + m02 * other.getZ(),
                m10 * other.getX() + m11 * other.getY() + m12 * other.getZ(),
                m20 * other.getX() + m21 * other.getY() + m22 * other.getZ());
    }

    /**
     * Creates a 2D rotation matrix for a given angle in degrees around the z-axis.
     * <p>
     * Note: Rotations around the x and y axes are not implemented as they are not
     * required for the 2D scope of this project.
     *
     * @param angleDeg The rotation angle in degrees.
     * @return A new {@code Mat3x3D} representing the z-rotation.
     */
    public static Mat3x3D RotationMatrix(double angleDeg){
        double angleRad = Math.toRadians(angleDeg);
        double cos = Math.cos(angleRad);
        double sin = Math.sin(angleRad);
        return new Mat3x3D(
                cos, -sin, 0,
                sin, cos, 0,
                0, 0, 1);
    }

    /**
     * Creates a 2D scale matrix from a given vector.
     *
     * @param scale A {@code Vec3D} containing the scale factors for the x and y axes.
     *              The z-component of the vector is ignored.
     * @return A new {@code Mat3x3D} representing the scaling transformation.
     */
    public static Mat3x3D ScaleMatrix(Vec3D scale){
        return new Mat3x3D(
                scale.getX(), 0, 0, 
                0, scale.getY(), 0, 
                0, 0, 1);
    }

    /**
     * Creates a 2D translation matrix from a given vector.
     * The z-component of the vector is ignored.
     *
     * @param translation A {@code Vec3D} containing the translation distances for the x and y axes.
     * @return A new {@code Mat3x3D} representing the translation.
     */
    public static Mat3x3D TranslationMatrix(Vec3D translation){
        return new Mat3x3D(
                1, 0, translation.getX(),
                0, 1, translation.getY(),
                0, 0, 1);
    }

    /**
     * Calculates and returns the inverse of this matrix.
     *
     * @return A new {@code Mat3x3D} instance that is the inverse of this matrix.
     * @throws ArithmeticException if the matrix is singular and cannot be inverted (i.e., its determinant is close to zero).
     */
    public Mat3x3D inverse(){
        double det = m00 * (m11 * m22 - m12 * m21)
                - m01 * (m10 * m22 - m12 * m20)
                + m02 * (m10 * m21 - m11 * m20);

        if (Math.abs(det) < 1e-10) {
            throw new ArithmeticException("Matrix is not invertible");
        }

        double invDet = 1.0 / det;

        return new Mat3x3D(
                (m11 * m22 - m12 * m21) * invDet,
                (m02 * m21 - m01 * m22) * invDet,
                (m01 * m12 - m02 * m11) * invDet,
                (m12 * m20 - m10 * m22) * invDet,
                (m00 * m22 - m02 * m20) * invDet,
                (m02 * m10 - m00 * m12) * invDet,
                (m10 * m21 - m11 * m20) * invDet,
                (m01 * m20 - m00 * m21) * invDet,
                (m00 * m11 - m01 * m10) * invDet
        );
    }
}