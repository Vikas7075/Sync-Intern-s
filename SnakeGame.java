import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    public static final int SCALE = 10;
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;
    public static final int SPEED = 150;

    private ArrayList<Point> snake = new ArrayList<Point>();
    private int direction = KeyEvent.VK_RIGHT;
    private Point food;
    private Timer timer = new Timer(SPEED, this);
    private boolean gameOver = false;
    private int score = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
        timer.start();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 6));
        generateFood();
        score = 0;
        direction = KeyEvent.VK_RIGHT;
        gameOver = false;
    }

    private void generateFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = head;
        switch (direction) {
        case KeyEvent.VK_UP:
            newHead = new Point(head.x, head.y - 1);
            break;
        case KeyEvent.VK_DOWN:
            newHead = new Point(head.x, head.y + 1);
            break;
        case KeyEvent.VK_LEFT:
            newHead = new Point(head.x - 1, head.y);
            break;
        case KeyEvent.VK_RIGHT:
            newHead = new Point(head.x + 1, head.y);
            break;
        }
        if (newHead.equals(food)) {
            snake.add(0, newHead);
            generateFood();
            score += 5;
        } else {
            snake.remove(snake.size() - 1);
            if (snake.contains(newHead)) {
                gameOver = true;
            }
            snake.add(0, newHead);
        }
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            timer.stop();
            int option = JOptionPane.showConfirmDialog(this,
                    "TOUGH LUCK!\n YOUR FINAL SCORE IS " + 
                    score + 
                    "\nWOULD YOU LIKE TO PLAY AGAIN?\nCLICK NO TO EXIT", 
                    "GAME OVER!",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                initGame();
                timer.start();
            } else {
                System.exit(0);
            }
        }
    }


@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * SCALE, p.y * SCALE, SCALE, SCALE);
        }
        g.setColor(Color.GREEN);
        g.fillRect(food.x * SCALE, food.y * SCALE, SCALE, SCALE);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Score: " + score, 5, 20);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
                && Math.abs(key - direction) != 2) {
            direction = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SNAKE GAME");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}