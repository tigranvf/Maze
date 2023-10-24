import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener, KeyListener {
    int width;
    int height;
    int mazeWidth;
    int mazeHeight;
    int realMazeWidth;
    int realMazeHeight;
    int tileSize;

    boolean[][] maze;
    Random random;
    Timer gameLoop;

    Game(int width, int height, int mazeWidth, int mazeHeight) {
        this.width = width;
        this.height = height;
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;

        setPreferredSize(new Dimension(this.width, this.height));
        setBackground(new Color(30, 30, 30));
        addKeyListener(this);
        setFocusable(true);

        this.random = new Random();


        this.realMazeWidth = mazeWidth*2+1;
        this.realMazeHeight = mazeHeight*2+1;
        this.tileSize = width / realMazeWidth;
        this.maze = generateMaze();

        gameLoop = new Timer(400, this);
        gameLoop.start();
    }

    public static class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean[][] generateMaze() {
        boolean[][] maze = new boolean[realMazeWidth][realMazeHeight];

        for (int x = 0; x < realMazeWidth; x++) {
            for (int y = 0; y < realMazeHeight; y++) {
                maze[x][y] = true;
            }
        }

        ArrayList<Tile> stack = new ArrayList<Tile>();
        Tile position = new Tile(1, 1);

        maze[position.x][position.y] = false;

        while (true) {
            // Get Neighborhoods
            ArrayList<Tile> neighborhoods = new ArrayList<>();

            if (0 <= position.x - 2) {
                if (maze[position.x - 2][position.y]) {
                    neighborhoods.add(new Tile(-1, 0));
                }
            }
            if (position.x + 2 < realMazeWidth) {
                if (maze[position.x + 2][position.y]) {
                    neighborhoods.add(new Tile(1, 0));
                }
            }
            if (0 <= position.y - 2) {
                if (maze[position.x][position.y - 2]) {
                    neighborhoods.add(new Tile(0, -1));
                }
            }
            if (position.y + 2 < realMazeHeight) {
                if (maze[position.x][position.y + 2]) {
                    neighborhoods.add(new Tile(0, 1));
                }
            }

            // Choose neighborhood
            if (!neighborhoods.isEmpty()) {
                Tile neighborhood = neighborhoods.get(random.nextInt(neighborhoods.size()));

                stack.add(new Tile(position.x, position.y));
                maze[position.x+neighborhood.x][position.y+neighborhood.y] = false;

                position.x += neighborhood.x*2;
                position.y += neighborhood.y*2;

                maze[position.x][position.y] = false;
            } else {
                if (!stack.isEmpty()) {
                    position = stack.get(stack.size()-1);
                    stack.removeLast();
                } else {
                    break;
                }
            }
        }

        // Print maze into console
        for (int y = 0; y < realMazeHeight; y++) {
            for (int x = 0; x < realMazeWidth; x++) {
                if (maze[x][y]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }

        return maze;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(150, 150, 150));
        for (int x = 0; x < realMazeWidth; x++) {
            for (int y = 0; y < realMazeHeight; y++) {
                if (maze[x][y]) {
                    g.fillRect(x*tileSize, y*tileSize, tileSize, tileSize);
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
