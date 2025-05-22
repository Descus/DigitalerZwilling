package de.frauas.objects.datastructures;

import lombok.Getter;

@Getter
public class Mat2D {
    private final double m00, m01, m10, m11;

    public Mat2D(double m00, double m01, double m10, double m11) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
    }

    public static final Mat2D IDENTITY = new Mat2D(1, 0, 0, 1);
    public static final Mat2D ZERO = new Mat2D(0, 0, 0, 0);

    public Mat2D add(Mat2D other){
        return new Mat2D(m00 + other.m00, m01 + other.m01, m10 + other.m10, m11 + other.m11);
    }

    public Mat2D sub(Mat2D other){
        return new Mat2D(m00 - other.m00, m01 - other.m01, m10 - other.m10, m11 - other.m11);
    }

    public Vec2D mult(Vec2D other){
        return new Vec2D(m00 * other.getX() + m01 * other.getY(), m10 * other.getY() + m11 * other.getY());
    }
}
