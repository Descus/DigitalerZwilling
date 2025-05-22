package de.frauas.scenario.primitives;

public record Mat2F(float m00, float m01, float m10, float m11) {
    public static final Mat2F IDENTITY = new Mat2F(1, 0, 0, 1);
    public static final Mat2F ZERO = new Mat2F(0, 0, 0, 0);
    
    public Mat2F add(Mat2F other){
        return new Mat2F(m00 + other.m00, m01 + other.m01, m10 + other.m10, m11 + other.m11);
    }
    
    public Mat2F sub(Mat2F other){
        return new Mat2F(m00 - other.m00, m01 - other.m01, m10 - other.m10, m11 - other.m11);
    }
    
    public Vec2F mult(Vec2F other){
        return new Vec2F(m00 * other.x() + m01 * other.y(), m10 * other.x() + m11 * other.y());
    }
}
