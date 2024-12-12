package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AppleBunch {
    private final List<int[]> walls;
    private final int unitSize;
    private final int screenWidth;
    private final int screenHeight;
    private final Random random;

    public AppleBunch(int unitSize, int screenWidth, int screenHeight) {
        this.unitSize = unitSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.random = new Random();
        this.walls = new ArrayList<>();
        generateWalls();
    }

    public void generateWalls() {
        walls.clear();
        int numberOfWalls = random.nextInt(10) + 5; // Random number of walls between 5 and 15
        int startX = random.nextInt(screenWidth / unitSize) * unitSize;
        int startY = random.nextInt(screenHeight / unitSize) * unitSize;
        boolean horizontal = random.nextBoolean();

        for (int i = 0; i < numberOfWalls; i++) {
            if (horizontal) {
                walls.add(new int[]{startX + i * unitSize, startY});
            } else {
                walls.add(new int[]{startX, startY + i * unitSize});
            }
        }
    }

    public List<int[]> getWalls() {
        return walls;
    }
}