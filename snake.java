import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int NUM_TILES_X = WIDTH / TILE_SIZE;
    private static final int NUM_TILES_Y = HEIGHT / TILE_SIZE;
    
    private LinkedList<Point> snake;
    private Point food;
    private String direction = "RIGHT";
    private boolean gameOver = false;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snake = new LinkedList<>();
        snake.add(new Point(NUM_TILES_X / 2, NUM_TILES_Y / 2)); // Initial snake position
        spawnFood();

        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveSnake();
                    checkCollisions();
                    repaint();
                }
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) {
                        direction = "UP";
                    } else if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) {
                        direction = "DOWN";
                    } else if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) {
                        direction = "LEFT";
                    } else if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) {
                        direction = "RIGHT";
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartGame();
                }
            }
        });
        setFocusable(true);
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = null;

        switch (direction) {
            case "UP":
                newHead = new Point(head.x, head.y - 1);
                break;
            case "DOWN":
                newHead = new Point(head.x, head.y + 1);
                break;
            case "LEFT":
                newHead = new Point(head.x - 1, head.y);
                break;
            case "RIGHT":
                newHead = new Point(head.x + 1, head.y);
                break;
        }

        // Add the new head to the front of the snake
        snake.addFirst(newHead);

        // If the snake eats food, don't remove the tail (snake grows)
        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkCollisions() {
        Point head = snake.getFirst();

        // Check if the snake hits the walls
        if (head.x < 0 || head.x >= NUM_TILES_X || head.y < 0 || head.y >= NUM_TILES_Y) {
            gameOver = true;
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
            }
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(NUM_TILES_X);
        int y = rand.nextInt(NUM_TILES_Y);

        // Ensure food doesn't spawn on the snake
        for (Point p : snake) {
            if (p.x == x && p.y == y) {
                spawnFood();  // Try again if food spawns on the snake
                return;
            }
        }

        food = new Point(x, y);
    }

    private void restartGame() {
        snake.clear();
        snake.add(new Point(NUM_TILES_X / 2, NUM_TILES_Y / 2));
        spawnFood();
        direction = "RIGHT";
        gameOver = false;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw the game area
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw the snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw the game over message
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! Press R to Restart", 100, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}
