package de.frauas.objects.datastructures;

import lombok.Getter;

import java.awt.geom.AffineTransform;

@Getter
public class Transform2D {
    private Vec3D translation = Vec3D.zero;
    private double rotation = 0;
    private Vec3D scale = Vec3D.one;
    
    private Mat3x3D translationMatrix = Mat3x3D.IDENTITY;
    private Mat3x3D scaleMatrix = Mat3x3D.IDENTITY;
    private Mat3x3D rotationMatrix = Mat3x3D.IDENTITY;

    private Mat3x3D translationMatrixInverse = Mat3x3D.IDENTITY;
    private Mat3x3D scaleMatrixInverse = Mat3x3D.IDENTITY;
    private Mat3x3D rotationMatrixInverse = Mat3x3D.IDENTITY;

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
        this.rotationMatrix = Mat3x3D.RotationMatrix(rotation);
        this.rotationMatrixInverse = rotationMatrix.inverse();
    }

    public void setTranslation(Vec3D translation) {
        this.translation = translation;
        this.translationMatrix = Mat3x3D.TranslationMatrix(translation);
        this.translationMatrixInverse = translationMatrix.inverse();
    }
    
    public void setScale(Vec3D scale) {
        this.scale = scale;
        this.scaleMatrix = Mat3x3D.ScaleMatrix(scale);
        this.scaleMatrixInverse = scaleMatrix.inverse();
    }

    public Vec3D forward(){
        return rotationMatrix.mult(Vec3D.up);
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
    
    public Vec3D transformPoint(Vec3D point){
        return translatePoint(rotatePoint(scalePoint(point)));
    }

    public Vec3D transformPointReverse(Vec3D point){
        return scalePointReverse(rotatePointReverse(translatePointReverse(point)));
    }

    public Vec3D scalePoint(Vec3D point){
        return scaleMatrix.mult(point);
    }

    public Vec3D translatePoint(Vec3D point){
        return translationMatrix.mult(point);
    }

    public Vec3D rotatePoint(Vec3D point){
        return rotationMatrix.mult(point);
    }

    public Vec3D scalePointReverse(Vec3D point){
        return scaleMatrixInverse.mult(point);
    }

    public Vec3D translatePointReverse(Vec3D point){
        return translationMatrixInverse.mult(point);
    }

    public Vec3D rotatePointReverse(Vec3D point){
        return rotationMatrixInverse.mult(point);
    }

    public AffineTransform toAffineTransform(){
        AffineTransform at = new AffineTransform();
        at.translate(translation.getX(), translation.getY());
        at.rotate(Math.toRadians(rotation));
        at.scale(scale.getX(), scale.getY());
        return at;
    }
}
