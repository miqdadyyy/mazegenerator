package mazegenerator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author myf
 */
public class Game extends Canvas implements Runnable {

    /*------------------------------*/
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int SCALE = 4;
    private static final double FPS = 15D;
    private JFrame frame;
    private boolean isRunning = false;
    private BufferedImage bimg = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) bimg.getRaster().getDataBuffer()).getData();

    /*------------------------------*/
    private int cols, rows;
    private int size = 50;
    public Cell[] grid;
    private Cell currentCell;
    public Stack<Cell> stack;

    public Game() {
//        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
//        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        init();

        frame = new JFrame("Maze Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        this.start();
    }

    public void init() {
        cols = WIDTH / size;
        rows = HEIGHT / size;
        System.out.println(cols + " " + rows);
        stack = new Stack<Cell>();
        grid = new Cell[rows * cols];
        int index = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                grid[index++] = new Cell(x, y, size, rows, cols);
            }
        }
        currentCell = grid[0];
        currentCell.visited = true;
    }

    public void update() {
        Cell next = currentCell.checkNeighbours(grid);
        if (next != null) {
            next.visited = true;
            stack.push(currentCell);
            removeWall(currentCell, next);
            System.out.println(next.getIndex(next.x / size, next.y / size));
            currentCell = next;
        }
        else if(!stack.isEmpty()){
            currentCell = stack.pop();
        }
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
//        Graphics g = bs.getDrawGraphics();

        /*- Draw Here -*/
        g.setColor(Color.red);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (Cell c : grid) {
            c.draw(g);
        }
        currentCell.pointThis(g);

        /*- End Here -*/
        g.dispose();
        bs.show();
    }
    
    public void removeWall(Cell a, Cell b){
        int x = a.x/size - b.x/size;
        if(x == 1){
            a.walls[3] = false;
            b.walls[1] = false;
        }
        if(x == -1){
            a.walls[1] = false;
            b.walls[3] = false;
        }
        
        int y = a.y/size - b.y/size;
        if(y == -1){
            a.walls[2] = false;
            b.walls[0] = false;
        }
        if(y == 1){
            a.walls[0] = false;
            b.walls[2] = false;
        }
    }

    @Override
    public void run() {
        final double nsPerUpdate = 1000000000D / FPS;

        long lastTime = System.nanoTime();
        long frameCounter = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;
        double unprocessedTime = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();
            long passTime = currentTime - lastTime;
            lastTime = currentTime;
            unprocessedTime += passTime;

            if (unprocessedTime >= nsPerUpdate) {
                unprocessedTime = 0;
                update();
                updates++;
            }

            try {
                Thread.sleep(2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            render();
            frames++;

            if (System.currentTimeMillis() - frameCounter >= 1000) {
                System.out.println("FPS : " + frames + " Updates : " + updates);
                frameCounter += 1000;
                frames = 0;
                updates = 0;
            }
        }
        dispose();
    }

    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        new Thread(this).run();
    }

    public void stop() {
        if (!this.isRunning) {
            return;
        }
        this.isRunning = false;
    }

    public void dispose() {
        System.exit(0);
    }
}
