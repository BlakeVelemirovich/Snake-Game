package org.example;

public class Apple extends AppleSeed {
    public Apple(int unitSize, int screenWidth, int screenHeight) {
        super(unitSize, screenWidth, screenHeight);
        generateNewPosition();
    }
}
