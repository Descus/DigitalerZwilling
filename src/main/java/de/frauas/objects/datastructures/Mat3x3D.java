package de.frauas.objects.datastructures;

import lombok.Getter;

/**
 * A class representing a 3x3 matrix of doubles, primarily designed for
 * mathematical operations involving linear transformations in 2D or 3D space.
 * This class provides utility methods for common matrix operations such as
 * addition, subtraction, multiplication with vectors, and generating specific
 * transformation matrices like rotation, scaling, and translation matrices.
 *
 * @author Scenario
 */
@Getter
public class Mat3x3D {
    private final double m00, m01, m02, m10, m11, m12, m20, m21, m22;

    /**
     * Constructs a 3x3 matrix with the specified elements.
     * <p>
     * @param m00 the element at the first row and first column of the matrix
     * @param m01 the element at the first row and second column of the matrix
     * @param m02 the element at the first row and third column of the matrix
     * @param m10 the element at the second row and first column of the matrix
     * @param m11 the element at the second row and second column of the matrix
     * @param m12 the element at the second row and third column of the matrix
     * @param m20 the element at the third row and first column of the matrix
     * @param m21 the element at the third row and second column of the matrix
     * @param m22 the element at the third row and third column of the matrix
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
     * A constant representing the 3x3 identity matrix.
     * The identity matrix is a square matrix in which all elements of the principal diagonal are ones and
     * all other elements are zeros. It acts as the multiplicative identity in matrix operations.
     */
    public static final Mat3x3D IDENTITY =
            new Mat3x3D(
                    1, 0, 0,  
                    0, 1, 0, 
                    0, 0, 1);
    
    /**
     * A constant representing a 3x3 zero matrix, where all elements are set to 0.
     * This matrix is commonly used as an additive identity in matrix operations.
     * All values in the matrix, from the first row to the last, are set to zero:
     * <p>
     * m00 = 0, m01 = 0, m02 = 0,
     * m10 = 0, m11 = 0, m12 = 0,
     * m20 = 0, m21 = 0, m22 = 0.
     */
    public static final Mat3x3D ZERO =
            new Mat3x3D(
                    0, 0, 0, 
                    0, 0, 0,
                    0,0, 0);

    /**
     * A constant 3x3 matrix where all elements are set to 1.
     * Represents a matrix with uniform values in all positions.
     * This can be used as a predefined matrix for mathematical or graphical operations.
     */
    public static final Mat3x3D ONE =
            new Mat3x3D(
                    1, 1, 1, 
                    1, 1, 1,
                    1, 1, 1);

    /**
     * Adds the elements of the given matrix to this matrix and returns the resulting matrix.
     * <p>
     * @param other the matrix to be added to this matrix
     * @return a new Mat3x3D instance representing the result of the addition
     */
    public Mat3x3D add(Mat3x3D other){
        return new Mat3x3D(
                m00 + other.m00, m01 + other.m01, m02 + other.m02, 
                m10 + other.m10, m11 + other.m11, m12 + other.m12, 
                m20 + other.m20, m21 + other.m21, m22 + other.m22);
    }

    /**
     * Subtracts the elements of the given matrix from this matrix and returns the resulting matrix.
     * <p>
     * @param other the matrix to be subtracted from this matrix
     * @return a new Mat3x3D instance representing the result of the subtraction
     */
    public Mat3x3D sub(Mat3x3D other){
        return new Mat3x3D(
                m00 - other.m00, m01 - other.m01, m02 - other.m02,
                m10 - other.m10, m11 - other.m11, m12 - other.m12,
                m20 - other.m20, m21 - other.m21, m22 - other.m22);
    }

    /**
     * Multiplies this 3x3 matrix with a 3D vector and returns the resulting vector.
     * <p>
     * @param other the 3D vector to be multiplied with this matrix
     * @return a new Vec3D instance representing the result of the multiplication
     */
    public Vec3D mult(Vec3D other){
        return new Vec3D(
                m00 * other.getX() + m01 * other.getY() + m02 * other.getZ(),
                m10 * other.getX() + m11 * other.getY() + m12 * other.getZ(),
                m20 * other.getX() + m21 * other.getY() + m22 * other.getZ());
    }

    /**
     * Returns a rotation matrix for the given angle in degrees around the z-axis.
     * 
     * x/y-rotations are not needed for our usecase
     * @param angleDeg
     * @return
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
     * Returns a Scale matrix for the Given vector.
     * @param scale
     * @return
     */
    public static Mat3x3D ScaleMatrix(Vec3D scale){
        return new Mat3x3D(
                scale.getX(), 0, 0, 
                0, scale.getY(), 0, 
                0, 0, 1);
    }

    /**
     * Returns a Translation matrix for the Given vector.
     * This is 2D only, so the z-component of the vector is ignored.
     * @param translation
     * @return
     */
    public static Mat3x3D TranslationMatrix(Vec3D translation){
        return new Mat3x3D(
                1, 0, translation.getX(),
                0, 1, translation.getY(),
                0, 0, 1);
    }
    
    /**
     * Computes and returns the inverse of this 3x3 matrix. If the matrix
     * is not invertible (i.e., its determinant is zero), an
     * {@link ArithmeticException} is thrown.
     * <p>
     * @return a new Mat3x3D instance representing the inverse of this matrix
     * @throws ArithmeticException if the matrix is not invertible
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
