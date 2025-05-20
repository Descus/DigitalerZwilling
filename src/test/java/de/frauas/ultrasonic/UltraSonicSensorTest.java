package de.frauas.ultrasonic;

import de.frauas.scenario.components.Obstacle;
import de.frauas.scenario.primitives.SDF;
import de.frauas.scenario.primitives.Vec2F;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UltraSonicSensorTest {
    Vec2F position;
    Vec2F forward;
    UltraSonicSensor sensor;
    List<SDF> sdfs;
    
    @BeforeEach
    public void init(){
        position = new Vec2F(0, 50);
        forward = new Vec2F(1, 0);
        sensor = new UltraSonicSensor(new Vec2F(0, 0), forward);
        sdfs = new ArrayList<>();
    }
    
    @Test
    public void obstacleInRangeTest_ObstacleInFront_ShouldReturnTrue() {
        sdfs.add(new Obstacle(new Vec2F(100, 0), new Vec2F(200, 100), 100));
        assertTrue(sensor.isObstacleInRange(position, sdfs));
    }

    @Test
    public void obstacleInRangeTest_ObstacleInRange_ShouldReturnTrue() {
        sdfs.add(new Obstacle(new Vec2F(100, 60), new Vec2F(200, 100), 100));
        assertTrue(sensor.isObstacleInRange(position, sdfs));
    }

    @Test
    public void obstacleInRangeTest_ObstacleMissed_ShouldReturnFalse() {
        sdfs.add(new Obstacle(new Vec2F(100, 200), new Vec2F(200, 300), 100));
        assertFalse(sensor.isObstacleInRange(position, sdfs));
    }
}