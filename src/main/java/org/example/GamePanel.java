package org.example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    int applesEaten;
    boolean running = false;
    Timer timer;
    AppleSeed apple;
    AppleSeed poisonApple;
    AppleBunch appleBunch;
    Player player1;
    Player player2;
    boolean twoPlayerMode;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        showPlayerOptionDialog();
        startGame();
    }

    private void showPlayerOptionDialog() {
        String[] options = {"One Player", "Two Players"};
        int choice = JOptionPane.showOptionDialog(this, "Choose game mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        twoPlayerMode = (choice == 1);
    }

    public void startGame() {
        apple = new Apple(UNIT_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
        poisonApple = new PoisonApple(UNIT_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
        appleBunch = new AppleBunch(UNIT_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT);
        player1 = new Player(GAME_UNITS, UNIT_SIZE, 6, 'R', UNIT_SIZE, UNIT_SIZE);
        if (twoPlayerMode) {
            player2 = new Player(GAME_UNITS, UNIT_SIZE, 6, 'L', SCREEN_WIDTH - UNIT_SIZE * 2, SCREEN_HEIGHT - UNIT_SIZE * 2);
        }
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(apple.getX(), apple.getY(), UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.blue);
            g.fillOval(poisonApple.getX(), poisonApple.getY(), UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.gray);
            for (int[] wall : appleBunch.getWalls()) {
                g.fillRect(wall[0], wall[1], UNIT_SIZE, UNIT_SIZE);
            }

            player1.draw(g, Color.green, new Color(45, 180, 0));
            if (twoPlayerMode) {
                player2.draw(g, Color.yellow, new Color(180, 180, 0));
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void move() {
        player1.move();
        if (twoPlayerMode) {
            player2.move();
        }
    }

    public void checkApple() {
        if ((player1.getX(0) == apple.getX()) && (player1.getY(0) == apple.getY())) {
            player1.grow();
            applesEaten++;
            apple.generateNewPosition();
            poisonApple.generateNewPosition();
            appleBunch.generateWalls();
        }
        if (twoPlayerMode && (player2.getX(0) == apple.getX()) && (player2.getY(0) == apple.getY())) {
            player2.grow();
            applesEaten++;
            apple.generateNewPosition();
            poisonApple.generateNewPosition();
            appleBunch.generateWalls();
        }
    }

    public void checkPoisonApple() {
        if ((player1.getX(0) == poisonApple.getX()) && (player1.getY(0) == poisonApple.getY())) {
            player1.stop();
        }
        if (twoPlayerMode && (player2.getX(0) == poisonApple.getX()) && (player2.getY(0) == poisonApple.getY())) {
            player2.stop();
        }
    }

    public void checkCollisions() {
        // Check collisions for player1
        for (int i = player1.getBodyParts(); i > 0; i--) {
            if ((player1.getX(0) == player1.getX(i)) && (player1.getY(0) == player1.getY(i))) {
                player1.stop();
            }
        }

        // Check collisions for player2
        if (twoPlayerMode) {
            for (int i = player2.getBodyParts(); i > 0; i--) {
                if ((player2.getX(0) == player2.getX(i)) && (player2.getY(0) == player2.getY(i))) {
                    player2.stop();
                }
            }
        }

        // Check if head touches borders for player1
        if (player1.getX(0) < 0 || player1.getX(0) > SCREEN_WIDTH || player1.getY(0) < 0 || player1.getY(0) > SCREEN_HEIGHT) {
            player1.stop();
        }

        // Check if head touches borders for player2
        if (twoPlayerMode && (player2.getX(0) < 0 || player2.getX(0) > SCREEN_WIDTH || player2.getY(0) < 0 || player2.getY(0) > SCREEN_HEIGHT)) {
            player2.stop();
        }

        // Check if head collides with walls for player1
        for (int[] wall : appleBunch.getWalls()) {
            if (player1.getX(0) == wall[0] && player1.getY(0) == wall[1]) {
                player1.stop();
            }
        }

        // Check if head collides with walls for player2
        if (twoPlayerMode) {
            for (int[] wall : appleBunch.getWalls()) {
                if (player2.getX(0) == wall[0] && player2.getY(0) == wall[1]) {
                    player2.stop();
                }
            }
        }

        // Check if player1 collides with player2
        if (twoPlayerMode) {
            for (int i = player2.getBodyParts(); i > 0; i--) {
                if ((player1.getX(0) == player2.getX(i)) && (player1.getY(0) == player2.getY(i))) {
                    player1.stop();
                }
            }
            for (int i = player1.getBodyParts(); i > 0; i--) {
                if ((player2.getX(0) == player1.getX(i)) && (player2.getY(0) == player1.getY(i))) {
                    player2.stop();
                }
            }
        }

        if (!player1.isRunning() && (!twoPlayerMode || !player2.isRunning())) {
            running = false;
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkPoisonApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (player1.getDirection() != 'R') {
                        player1.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (player1.getDirection() != 'L') {
                        player1.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (player1.getDirection() != 'D') {
                        player1.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (player1.getDirection() != 'U') {
                        player1.setDirection('D');
                    }
                    break;
                case KeyEvent.VK_A:
                    if (twoPlayerMode && player2.getDirection() != 'R') {
                        player2.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_D:
                    if (twoPlayerMode && player2.getDirection() != 'L') {
                        player2.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_W:
                    if (twoPlayerMode && player2.getDirection() != 'D') {
                        player2.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_S:
                    if (twoPlayerMode && player2.getDirection() != 'U') {
                        player2.setDirection('D');
                    }
                    break;
            }
        }
    }
}