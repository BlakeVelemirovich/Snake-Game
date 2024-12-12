
package org.example;

import java.awt.Color;
import java.awt.Graphics;

public class Player {
    private final int[] x;
    private final int[] y;
    private int bodyParts;
    private final int unitSize;
    private char direction;
    private boolean running;

    public Player(int gameUnits, int unitSize, int initialBodyParts, char initialDirection, int startX, int startY) {
        this.x = new int[gameUnits];
        this.y = new int[gameUnits];
        this.bodyParts = initialBodyParts;
        this.unitSize = unitSize;
        this.direction = initialDirection;
        this.running = true;
        this.x[0] = startX;
        this.y[0] = startY;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
                break;
        }
    }

    public void draw(Graphics g, Color headColor, Color bodyColor) {
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(headColor);
                g.fillRect(x[i], y[i], unitSize, unitSize);
            } else {
                g.setColor(bodyColor);
                g.fillRect(x[i], y[i], unitSize, unitSize);
            }
        }
    }

    public int getX(int index) {
        return x[index];
    }

    public int getY(int index) {
        return y[index];
    }

    public int getBodyParts() {
        return bodyParts;
    }

    public void grow() {
        bodyParts++;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public char getDirection() {
        return direction;
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }
}