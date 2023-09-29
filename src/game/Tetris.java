package game;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris extends JPanel implements Serializable {
    private int state;
    public static final int PLAYING = 0;
    public static final int PAUSE = 1;
    public static final int GAME_OVER = 2;
    private static final String[] STATE= {"[P]Pause", "[C]Continue", "[S]Restart"};
    int index = 0;
    int speed = 8;
    private int score;
    private int lines;
    public static final int ROWS = 20;
    public static final int COLS = 10;
    private Cell[][] wall=new Cell[ROWS][COLS];
    private Tetromino currentTetromino;
    private Tetromino nextTetromino;
    public static BufferedImage background;
    public static BufferedImage gameOverImage;
    public static BufferedImage T;
    public static BufferedImage S;
    public static BufferedImage Z;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage I;
    public static BufferedImage O;

    static {
        try{
            background=ImageIO.read(
                    Tetris.class.getResource("tetris.png"));
            gameOverImage=ImageIO.read(
                    Tetris.class.getResource("game-over.png"));
            T=ImageIO.read(Tetris.class.getResource("T.png"));
            S=ImageIO.read(Tetris.class.getResource("S.png"));
            Z=ImageIO.read(Tetris.class.getResource("Z.png"));
            J=ImageIO.read(Tetris.class.getResource("J.png"));
            L=ImageIO.read(Tetris.class.getResource("L.png"));
            O=ImageIO.read(Tetris.class.getResource("O.png"));
            I=ImageIO.read(Tetris.class.getResource("I.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void paint(Graphics g){
        Font font = new Font(
                Font.SANS_SERIF, Font.BOLD, 30);
        g.setFont(font);
        g.setColor(new Color(0x667799));
        g.drawImage(background, 0, 0, null);
        g.translate(15, 15);
        paintWall(g);
        paintTetromino(g);
        paintnextTetromino(g);
        paintScore(g);
        paintState(g);
        g.translate(-15, -15);
    }
    private void paintState(Graphics g) {
        g.drawString(STATE[state], 309, 287);
        if(state == GAME_OVER){
            g.drawImage(gameOverImage,0,0,null);
        }
    }
    private void paintScore(Graphics g){
        int x = 293;
        int y = 162;

        g.drawString("SCORE:"+score, x, y);
        y+=56;
        g.drawString("LINES:"+lines, x, y);
    }
    public void paintnextTetromino(Graphics g){
        Cell[] cells = nextTetromino.cells;
        for(int i=0;i<cells.length; i++){
            Cell cell = cells[i];
            int x = CELL_SIZE*(cell.getCol()+10);
            int y = CELL_SIZE*(cell.getRow()+1);
            g.drawImage(cell.getImage(), x, y, null);
        }
    }
    public void paintTetromino(Graphics g){
        Cell[] cells = currentTetromino.cells;
        for(int i=0;i<cells.length; i++){
            Cell cell = cells[i];
            int x = CELL_SIZE*cell.getCol();
            int y = CELL_SIZE*cell.getRow();
            g.drawImage(cell.getImage(), x, y, null);
        }
    }
    public static final int CELL_SIZE=26;
    private void paintWall(Graphics g){
        for(int row=0; row<ROWS; row++){
            for(int col=0; col<COLS; col++){
                Cell cell = wall[row][col];
                int x=CELL_SIZE * col;
                int y=CELL_SIZE * row;
                if(cell!=null){
                    g.drawImage(cell.getImage(), x,y,null);
                }
            }
        }
    }

    public void action(Tetris panel){
        state = PLAYING;
        currentTetromino = Tetromino.randomOne();
        nextTetromino = Tetromino.randomOne();
        repaint();
        KeyListener l=new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if(key==KeyEvent.VK_Q){

                    System.exit(0);
                }
                switch(state){
                    case GAME_OVER:
                        if(key==KeyEvent.VK_S){
                            restartAction();
                        }
                        return;
                    case PAUSE:
                        if(key==KeyEvent.VK_C){
                            state=PLAYING;
                        }
                        return;
                }
                switch(key){
                    case KeyEvent.VK_DOWN:
                        softDropAction();break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();	break;
                    case KeyEvent.VK_SPACE:
                        rotateRightAction(); break;
                    case KeyEvent.VK_Z:
                        rotateLeftAction(); break;
                    case KeyEvent.VK_UP:
                        hardDropAction();	break;
                    case KeyEvent.VK_P:
                        state = PAUSE; break;
                }
                repaint();
            }
        };
        this.addKeyListener(l);
        this.requestFocus();
        //主控程序
        for(;;){
            if(state==PLAYING){
                index ++;
                if(index % speed==0){
                    softDropAction();
                }
            }
            repaint();
            try{
                Thread.sleep(1000/10);
            }catch(Exception e){
            }
        }

    }
    protected void restartAction() {
        score = 0;
        lines = 0;
        wall = new Cell[ROWS][COLS];
        currentTetromino = Tetromino.randomOne();
        nextTetromino = Tetromino.randomOne();
        speed = 8;
        index = 0;
        state = PLAYING;
    }

    private void hardDropAction(){
        while(canDrop()){
            currentTetromino.softDrop();
        }
        landIntoWall();
        destroyLines();
        if(!isGameOver()){
            currentTetromino = nextTetromino;
            nextTetromino = Tetromino.randomOne();
        }else{
            state = GAME_OVER;
        }
    }

    private void softDropAction(){
        boolean drop = canDrop();
        if(drop){
            currentTetromino.softDrop();
        }else{
            landIntoWall();
            destroyLines();
            if(!isGameOver()){
                currentTetromino=nextTetromino;
                nextTetromino=Tetromino.randomOne();
            }else{
                state = GAME_OVER;
            }
        }
    }

    private boolean isGameOver() {
        Cell[] cells = nextTetromino.cells;
        for(int i=0; i<cells.length; i++){
            Cell cell = cells[i];
            int row=cell.getRow();
            int col=cell.getCol();
            if(wall[row][col]!=null){
                return true;
            }
        }
        return false;
    }

    private int[] scoreTable = {0,1,5,20,100};
    private void destroyLines() {
        int lines = 0;
        for(int row=0; row<ROWS; row++){
            if(fullCells(row)){
                deleteRow(row);
                lines++;
            }
        }
        this.score += scoreTable[lines];
        speed = Math.max(8-score/1000, 1);
        this.lines += lines;
    }
    private boolean fullCells(int row) {
        Cell[] line = wall[row];
        for (int i = 0; i < line.length; i++) {
            Cell cell = line[i];
            if (cell == null) {
                return false;
            }
        }
        return true;
    }
    private void deleteRow(int row) {
        for(int i=row; i>=1; i--){
            System.arraycopy(wall[i-1], 0,
                    wall[i], 0, COLS);
        }
        Arrays.fill(wall[0], null);
    }
    private void landIntoWall() {
        Cell[] cells = currentTetromino.cells;
        for(int i=0; i<cells.length; i++){
            Cell cell = cells[i];
            int row = cell.getRow();
            int col = cell.getCol();
            wall[row][col]=cell;
        }
    }
    private boolean canDrop(){
        Cell[] cells = currentTetromino.cells;
        for(int i=0; i<cells.length; i++){
            Cell cell = cells[i];
            if(cell.getRow() == ROWS-1){
                return false;
            }
        }

        for(int i=0; i<cells.length; i++){
            Cell cell = cells[i];
            int row = cell.getRow();
            int col = cell.getCol();
            if(wall[row+1][col]!=null){
                //System.out.println(cell+"的下方有方块");
                return false;
            }
        }
        return true;
    }

    /** Tetris 类中添加向右移动流程控制方法 */
    private void moveRightAction(){
        currentTetromino.moveRight();
        if(outOfBounds() || concide()){
            currentTetromino.moveLeft();
        }
    }
    private void moveLeftAction(){
        currentTetromino.moveLeft();
        if(outOfBounds() || concide()){
            currentTetromino.moveRight();
        }
    }
    private boolean outOfBounds(){
        Cell[] cells = currentTetromino.cells;
        for(int i=0; i<cells.length; i++){
            Cell cell = cells[i];
            int row = cell.getRow();
            int col = cell.getCol();
            if((row<0||row>=ROWS) ||
                    (col<0||col>=COLS)){
                return true;
            }
        }
        return false;
    }
    private boolean concide(){
        Cell[] cells = currentTetromino.cells;
        for(Cell cell: cells){
            int row = cell.getRow();
            int col = cell.getCol();
            if(wall[row][col]!=null){
                return true;
            }
        }
        return false;
    }
    public void rotateRightAction(){
        currentTetromino.rotateRight();
        if(outOfBounds() || concide()){
            currentTetromino.rotateLeft();
        }
    }
    public void rotateLeftAction(){
        currentTetromino.rotateLeft();
        if(outOfBounds() || concide()){
            currentTetromino.rotateRight();
        }
    }

    public static void main(String[] args) {
        ObjectInputStream ois=null;
        Tetris saveData = null;


        //Frame 框，相框，代表窗口框
        JFrame frame = new JFrame();
        //panel 代表面板
        Tetris panel = new Tetris();
        //Background 背景
        panel.setBackground(Color.YELLOW);
        //窗口中添加面板
        frame.add(panel);
        frame.setSize(535, 580);
        //居中
        frame.setLocationRelativeTo(null);
        //点击X时候同时关闭程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //显示窗口框
        frame.setVisible(true);
        //尽快的调用 paint()方法绘制显示界面
        //显示窗口以后，执行 启动方法action
        panel.action(panel);

    }
}





