package de.frauas.objects.datastructures;

import lombok.Getter;

@Getter
public class Mat3x3D {
    private final double m00, m01, m02, m10, m11, m12, m20, m21, m22;

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

    public static final Mat3x3D IDENTITY =
            new Mat3x3D(
                    1, 0, 0,  
                    0, 1, 0, 
                    0, 0, 1);
    
    public static final Mat3x3D ZERO =
            new Mat3x3D(
                    0, 0, 0, 
                    0, 0, 0,
                    0,0, 0);
    
    public static final Mat3x3D ONE =
            new Mat3x3D(
                    1, 1, 1, 
                    1, 1, 1,
                    1, 1, 1);

    public Mat3x3D add(Mat3x3D other){
        return new Mat3x3D(
                m00 + other.m00, m01 + other.m01, m02 + other.m02, 
                m10 + other.m10, m11 + other.m11, m12 + other.m12, 
                m20 + other.m20, m21 + other.m21, m22 + other.m22);
    }

    public Mat3x3D sub(Mat3x3D other){
        return new Mat3x3D(
                m00 - other.m00, m01 - other.m01, m02 + other.m02,
                m10 - other.m10, m11 - other.m11, m12 + other.m12,
                m20 - other.m20, m21 - other.m21, m22 + other.m22);
    }

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
                0, 0, scale.getZ());
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
