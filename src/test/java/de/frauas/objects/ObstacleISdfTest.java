package de.frauas.objects;

import de.frauas.objects.datastructures.Vec3D;
import de.frauas.objects.obstacle.ISdf;
import de.frauas.objects.obstacle.Obstacle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleISdfTest {

    @Test
    void SDFTest_PointOutsideObstacle() {
        ISdf obstacle = new Obstacle(null, 50, 50, 100, 100, 10);
        double sdf = obstacle.getSDF(new Vec3D(40,  60, 0));
        assertEquals(10, sdf, 0.05);
    }

    @Test
    void SDFTest_PointInsideObstacle() {
        ISdf obstacle = new Obstacle(null, 50, 50, 100, 100, 10);
        double sdf = obstacle.getSDF(new Vec3D(70, 75, 0));
        assertEquals(-20, sdf);
    }

    @Test
    void SDFTest_MultiplePointsInsideObstacle() {
        ISdf obstacle = new Obstacle(null, 60, 50, 100, 100, 10);
        List<Double> sdfList = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            for (int j = 0; j < 110; j++) {
                if((j < 100 && j > 50) && (i > 60 && i < 100)){
                    sdfList.add(obstacle.getSDF(new Vec3D(i, j, 0)));
                }
            }
        }
        assertTrue(sdfList.stream().allMatch(d -> d < 0));
    }

    @Test
    void SDFTest_MultiplePointsOnObstacle() {
        ISdf obstacle = new Obstacle(null, 60, 50, 100, 100, 10);
        List<Double> sdfList = new ArrayList<>();
        for (int i = 0; i < 110; i++) {
            for (int j = 0; j < 110; j++) {
                if((j == 50 || j == 100) && (i >= 60 && i <= 100)){
                    sdfList.add(obstacle.getSDF(new Vec3D(i, j, 0)));
                }
                if ((i == 60 || i == 100) && (j >= 50 && j <= 100)) {
                    sdfList.add(obstacle.getSDF(new Vec3D(i, j, 0)));
                }
            }
        }
        assertTrue(sdfList.stream().allMatch(d -> d == 0));
    }
}