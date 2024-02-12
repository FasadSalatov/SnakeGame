
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

class SnakeGame extends JPanel implements KeyListener {
    private final int BOARD_SIZE = 800;
    private final int DOT_SIZE = 20;
    private final int ALL_DOTS = (BOARD_SIZE * BOARD_SIZE) / (DOT_SIZE * DOT_SIZE);
    private final int RAND_POS = (BOARD_SIZE / DOT_SIZE) * (BOARD_SIZE / DOT_SIZE);
    private final int DELAY = 140;

    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];

    private int dots;
    private int appleX;
    private int appleY;

    private Timer timer;
    private boolean gameOver;
    private boolean leftDirection;
    private boolean rightDirection;
    private boolean upDirection;
    private boolean downDirection;

    public SnakeGame() {
        initBoard();
    }

    private void initBoard() {
        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();
    }

    private void initGame() {
        dots = 3;
        gameOver = false;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;

        for (int i = 0; i < dots; i++) {
            x[i] = 60 - i * DOT_SIZE;
            y[i] = 60;
        }

        generateApple();

        timer = new Timer(DELAY, e -> {
            if (!gameOver) {
                move();
                checkApple();
                checkCollision();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            drawSnake(g);
            drawApple(g);
        } else {
            gameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < dots; i++) {
            g.setColor(Color.GREEN);
            g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, DOT_SIZE, DOT_SIZE);
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(message, (BOARD_SIZE - metrics.stringWidth(message)) / 2, BOARD_SIZE / 2);
    }

    private void generateApple() {
        int r = (int) (Math.random() * RAND_POS);
        appleX = r * DOT_SIZE;

        r = (int) (Math.random() * RAND_POS);
        appleY = r * DOT_SIZE;
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            generateApple();
        }
    }

    private void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                gameOver = true;
            }
        }

        if (x[0] >= BOARD_SIZE || x[0] < 0 || y[0] >= BOARD_SIZE || y[0] < 0) {
            gameOver = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !rightDirection) {
            leftDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !leftDirection) {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
        }

        if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !downDirection) {
            upDirection = true;
            rightDirection = false;
            leftDirection = false;
        }

        if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !upDirection) {
            downDirection = true;
            rightDirection = false;
            leftDirection = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
