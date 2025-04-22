package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2;

import java.awt.*;

public interface Drawable {
    void Draw(Graphics2D g, Vec2<Float> scale);
}
