package game;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Cell implements Serializable{
    private int row;
    private int col;
    transient private BufferedImage image;
    public Cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public String toString(){
        return "("+row+","+col+")";
    }
    public void drop(){
        row++;
    }
    public void moveLeft(){
        col--;
    }
    public void moveRight(){
        col++;
    }

}


