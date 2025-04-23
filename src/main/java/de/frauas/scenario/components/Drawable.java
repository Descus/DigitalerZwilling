package de.frauas.scenario.components;

import de.frauas.scenario.primitives.Vec2F;

import java.awt.*;

public interface Drawable {
    void draw(Graphics2D g, Vec2F scale, float deltaTime);
}
