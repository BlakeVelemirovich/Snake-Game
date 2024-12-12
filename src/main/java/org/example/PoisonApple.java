package org.example;

public class PoisonApple extends AppleSeed {
    public PoisonApple(int unitSize, int screenWidth, int screenHeight) {
        super(unitSize, screenWidth, screenHeight);
        generateNewPosition();
    }
}
