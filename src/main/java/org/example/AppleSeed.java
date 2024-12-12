package org.example;

import java.util.Random;

public abstract class AppleSeed {
    protected int x;
    protected int y;
    protected final int unitSize;
    protected final int screenWidth;
    protected final int screenHeight;
    protected final Random random;

    public AppleSeed(int unitSize, int screenWidth, int screenHeight) {
        this.unitSize = unitSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.random = new Random();
    }

    public void generateNewPosition() {
        x = random.nextInt(screenWidth / unitSize) * unitSize;
        y = random.nextInt(screenHeight / unitSize) * unitSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
