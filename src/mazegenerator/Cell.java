/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazegenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 * @author myf
 */
public class Cell {

    int x, y;
    int size, rows, cols;
    boolean walls[] = {true, true, true, true}; //top, right, bottom, left
    boolean visited;

    public Cell(int x, int y, int size, int rows, int cols) {
        this.x = x * size;
        this.y = y * size;
        this.size = size;
        this.visited = false;
        this.rows = rows;
        this.cols = cols;
    }

    public void draw(Graphics2D g) {
        if (visited) {
            g.setColor(Color.yellow);
            g.fillRect( x,  y, size, size);
        }
        
        g.setColor(Color.black);

        if (walls[0]) {
            g.drawLine(x, y, x + size, y);
        }
        if (walls[1]) {
            g.drawLine(x + size, y, x + size, y + size);
        }
        if (walls[2]) {
            g.drawLine(x, y + size, x + size, y + size);
        }
        if (walls[3]) {
            g.drawLine(x, y, x, y + size);
        }

        

//        g.drawRect(size * x, size * y, size, size);
    }

    public Cell checkNeighbours(Cell[] grid) {
        int ind;
        ArrayList<Cell> neighbours = new ArrayList<Cell>();

        ind = getIndex(x/size, y/size - 1);
        Cell top = ind == -1 ? null : grid[ind];
        ind = getIndex(x/size, y/size + 1);
        Cell bottom = ind == -1 ? null : grid[ind];
        ind = getIndex(x/size + 1, y/size);
        Cell right = ind == -1 ? null : grid[ind];
        ind = getIndex(x/size - 1, y/size);
        Cell left = ind == -1 ? null : grid[ind];

        if (top != null) {
            if (!top.visited) {
                neighbours.add(top);
            }
        }
        if (bottom != null) {
            if (!bottom.visited) {
                neighbours.add(bottom);
            }
        }
        if (right != null) {
            if (!right.visited) {
                neighbours.add(right);
            }
        }
        if (left != null) {
            if (!left.visited) {
                neighbours.add(left);
            }
        }
        if (neighbours.size() > 0) {
            int index = (int) (Math.random() * neighbours.size());
            return neighbours.get(index);
        }
        return null;
    }
    
    public void pointThis(Graphics2D g){
        g.setColor(Color.green);
        g.fillRect(x, y, size, size);
    }

    public int getIndex(int i, int j) {
        if (i < 0 || j < 0 || i >= cols || j >= rows) {
            return -1;
        }
        return i + j * cols;
    }
}
