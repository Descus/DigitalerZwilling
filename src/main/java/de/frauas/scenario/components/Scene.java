package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;
import de.frauas.scenario.primitives.Vec2F;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scene extends JPanel{
    private final Vec2 size;
    private final List<Drawable> drawStack = new ArrayList<>();
    private long last_update = System.nanoTime();
    
    public Scene(Vec2 size, List<Drawable> drawStack){
        this.size = size;
        this.drawStack.addAll(drawStack);
    }
    public Scene(Vec2 size){
        this.size = size;
    }
    
    public void addDrawable(Drawable d){
        drawStack.add(d);
    }

    public void addDrawables(Collection<Drawable> d){
        drawStack.addAll(d);
    }
    
    public void removeDrawable(Drawable d){
        drawStack.remove(d);
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        float dt = calcDeltaTime();
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawRect(getLocation().x, getLocation().y, getWidth(), getHeight());
        //set pivot bottom left
        g2d.translate(getLocation().x, getLocation().y + getHeight());
        //update FPS counter
        g2d.drawString("" + 1/ dt, 0,0);
        //update draw stack
        for (Drawable d : drawStack) {
            d.draw(g2d, getCoordinateSystemScaleFactor(), dt);
        }
        g2d.dispose();
    }
    
    private float calcDeltaTime(){
        long time = System.nanoTime();
        float dt = (time - last_update) / 1000000000f;
        last_update = time;
        return dt;
    }
    
    private Vec2F getCoordinateSystemScaleFactor(){
        return new Vec2F((float) getWidth() / size.x(), (float) getHeight() / size.y());
    }
    
}
