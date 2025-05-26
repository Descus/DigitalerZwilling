package de.frauas.objects;

import de.frauas.objects.datastructures.Vec2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleSDFTest {

    @Test
    void SDFTest_PointOutsideObstacle() {
        SDF obstacle = new Obstacle(50, 50, 100, 100, 10);
        double sdf = obstacle.getSDF(new Vec2D(40,  60));
        assertEquals(10, sdf);
    }

    @Test
    void SDFTest_PointInsideObstacle() {
        SDF obstacle = new Obstacle(50, 50, 100, 100, 10);
        double sdf = obstacle.getSDF(new Vec2D(70, 75));
        assertEquals(-20, sdf);
    }

    @Test
    void SDFTest_MultiplePointsInsideObstacle() {
        SDF obstacle = new Obstacle(60, 50, 100, 100, 10);
        List<Double> sdfList = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            for (int j = 0; j < 110; j++) {
                if((j < 100 && j > 50) && (i > 60 && i < 100)){
                    sdfList.add(obstacle.getSDF(new Vec2D(i, j)));
                }
            }
        }
        assertTrue(sdfList.stream().allMatch(d -> d < 0));
    }

    @Test
    void SDFTest_MultiplePointsOnObstacle() {
        SDF obstacle = new Obstacle(60, 50, 100, 100, 10);
        List<Double> sdfList = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            for (int j = 0; j < 110; j++) {
                if((j == 50 || j == 100) && (i >= 60 && i <= 100)){
                    sdfList.add(obstacle.getSDF(new Vec2D(i, j)));
                }
                if ((i == 60 || i == 100) && (j >= 50 && j <= 100)) {
                    sdfList.add(obstacle.getSDF(new Vec2D(i, j)));
                }
            }
        }
        assertTrue(sdfList.stream().allMatch(d -> d == 0));
    }
}