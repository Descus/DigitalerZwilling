package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene extends JPanel{
    public final Vec2<Integer> size;
    private final List<Drawable> drawStack = new ArrayList<>();
    
    public Scene(Vec2<Integer> size, List<Drawable> drawStack){
        this.size = size;
        this.drawStack.addAll(drawStack);
    }
    public Scene(Vec2<Integer> size){
        this.size = size;
    }
    
    public void Add(Drawable d){
        drawStack.add(d);
    }
    
    public void Remove(Drawable d){
        drawStack.remove(d);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawRect(getLocation().x, getLocation().y, getWidth(), getHeight());
        g2d.translate(getLocation().x, getLocation().y + getHeight());
        for (Drawable d : drawStack) {
            d.Draw(g2d, new Vec2<>((float) getWidth() / size.x, (float)  getHeight() / size.y));
        }
        g2d.dispose();
    }
}
