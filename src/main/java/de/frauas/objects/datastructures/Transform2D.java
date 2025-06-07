package de.frauas.objects.datastructures;

import lombok.Getter;

@Getter
public class Transform2D {
    private Vec3D translation = Vec3D.zero;
    private double rotation = 0;
    private Vec3D scale = Vec3D.one;
    
    private Mat3D translationMatrix = Mat3D.IDENTITY;
    private Mat3D scaleMatrix = Mat3D.IDENTITY;
    private Mat3D rotationMatrix = Mat3D.IDENTITY;
    
    public Transform2D() {}

    public Transform2D(Vec3D translation) {
        setTranslation(translation);
    }
    
    public Transform2D(Vec3D translation, double rotation) {
        setTranslation(translation);
        setRotation(rotation);
    }

    public Transform2D(Vec3D translation, Vec3D scale, double rotation) {
        setScale(scale);
        setRotation(rotation);
        setTranslation(translation);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
        this.rotationMatrix = Mat3D.RotationMatrix(rotation);
    }

    public void setTranslation(Vec3D translation) {
        this.translation = translation;
        this.translationMatrix = Mat3D.TranslationMatrix(translation);
    }
    
    public void setScale(Vec3D scale) {
        this.scale = scale;
        this.scaleMatrix = Mat3D.ScaleMatrix(scale);
    }

    public Vec3D forward(){
        return rotationMatrix.mult(Vec3D.right);
    }
    
    public Vec3D right(){
        return forward().perpendicular();
    }
    
    public void translate(Vec3D translation){
        setTranslation(this.translation.add(translation));
    }
    
    public void translate(double x, double y){
        translate(new Vec3D(x, y, 0));
    }

    public void rotate(double angleDeg){
        this.rotation += angleDeg;
        setRotation(this.rotation %= 360);
    }
    
    public Vec3D transform(Vec3D point){
        return scaleMatrix.mult(rotationMatrix.mult(translationMatrix.mult(point)));
    }
}
