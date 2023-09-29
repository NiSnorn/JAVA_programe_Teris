/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.Serializable;
import java.util.Random;

public class Tetromino implements Serializable {

    protected Cell[] cells=new Cell[4];
    public static Tetromino randomOne(){
        Random random=new Random();
        int type=random.nextInt(7);
        switch(type){
            case 0:return new Tetromino().new T();
            case 1:return new Tetromino().new L();
            case 2:return new Tetromino().new J();
            case 3:return new Tetromino().new S();
            case 4:return new Tetromino().new Z();
            case 5:return new Tetromino().new O();
            case 6:return new Tetromino().new I();
        }
        return null;
    }
    public void softDrop(){
        for(int i=0;i<cells.length;i++){
            cells[i].drop();
        }
    }
    public void moveLeft(){
        for(int i=0;i<cells.length;i++){
            cells[i].moveLeft();
        }
    }
    public void moveRight(){
        for(int i=0;i<cells.length;i++){
            cells[i].moveRight();
        }
    }
    private int index=1000;
    protected class State implements Serializable{
        int row0,col0,row1,col1,row2,col2,row3,col3;
        public State(int row0, int col0, int row1, int col1,
                     int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }

    }
    protected State[] states;
    public void rotateRight(){
        index++;
        State state=states[index%4];
        cells[1].setRow(cells[1].getRow()+state.row1);
        cells[1].setCol(cells[1].getCol()+state.col1);
        cells[2].setRow(cells[2].getRow()+state.row2);
        cells[2].setCol(cells[2].getCol()+state.col2);
        cells[3].setRow(cells[3].getRow()+state.row3);
        cells[3].setCol(cells[3].getCol()+state.col3);
    }

    public void rotateLeft(){
        State state=states[index%4];
        cells[1].setRow(cells[1].getRow()-state.row1);
        cells[1].setCol(cells[1].getCol()-state.col1);
        cells[2].setRow(cells[2].getRow()-state.row2);
        cells[2].setCol(cells[2].getCol()-state.col2);
        cells[3].setRow(cells[3].getRow()-state.row3);
        cells[3].setCol(cells[3].getCol()-state.col3);
        index--;
    }



    class T extends Tetromino{
        public T(){
            cells[0]=new Cell(0,4,Tetris.T);
            cells[1]=new Cell(0,3,Tetris.T);
            cells[2]=new Cell(0,5,Tetris.T);
            cells[3]=new Cell(1,4,Tetris.T);
            states=new State[]{
                    new State(0,0,-1,-1, 1, 1, 1,-1),
                    new State(0,0,-1, 1, 1,-1,-1,-1),
                    new State(0,0, 1, 1,-1,-1,-1, 1),
                    new State(0,0, 1,-1,-1, 1, 1, 1)
            };
        }
    }

    class L extends Tetromino{
        public L(){
            cells[0]=new Cell(0,4,Tetris.L);
            cells[1]=new Cell(0,3,Tetris.L);
            cells[2]=new Cell(0,5,Tetris.L);
            cells[3]=new Cell(1,3,Tetris.L);
            states=new State[]{
                    new State(0,0,-1,-1, 1, 1, 0,-2),
                    new State(0,0,-1, 1, 1,-1,-2, 0),
                    new State(0,0, 1, 1,-1,-1, 0, 2),
                    new State(0,0, 1,-1,-1, 1, 2, 0)
            };
        }
    }

    class J extends Tetromino{
        public J(){
            cells[0]=new Cell(0,4,Tetris.J);
            cells[1]=new Cell(0,3,Tetris.J);
            cells[2]=new Cell(0,5,Tetris.J);
            cells[3]=new Cell(1,5,Tetris.J);
            states=new State[]{
                    new State(0,0,-1,-1, 1, 1, 2, 0),
                    new State(0,0,-1, 1, 1,-1, 0,-2),
                    new State(0,0, 1, 1,-1,-1,-2, 0),
                    new State(0,0, 1,-1,-1, 1, 0, 2)
            };
        }
    }

    class S extends Tetromino{
        public S(){
            cells[0]=new Cell(0,4,Tetris.S);
            cells[1]=new Cell(0,5,Tetris.S);
            cells[2]=new Cell(1,3,Tetris.S);
            cells[3]=new Cell(1,4,Tetris.S);
            states=new State[]{
                    new State(0,0, 1, 1, 0,-2, 1,-1),
                    new State(0,0,-1,-1, 0, 2,-1, 1),
                    new State(0,0, 1, 1, 0,-2, 1,-1),
                    new State(0,0,-1,-1, 0, 2,-1, 1)
            };
        }
    }

    class Z extends Tetromino{
        public Z(){
            cells[0]=new Cell(1,4,Tetris.Z);
            cells[1]=new Cell(0,3,Tetris.Z);
            cells[2]=new Cell(0,4,Tetris.Z);
            cells[3]=new Cell(1,5,Tetris.Z);
            states=new State[]{
                    new State(0,0, 0,-2,-1,-1,-1, 1),
                    new State(0,0, 0, 2, 1, 1, 1,-1),
                    new State(0,0, 0,-2,-1,-1,-1, 1),
                    new State(0,0, 0, 2, 1, 1, 1,-1)
            };
        }
    }

    class O extends Tetromino{
        public O(){
            cells[0]=new Cell(0,4,Tetris.O);
            cells[1]=new Cell(0,5,Tetris.O);
            cells[2]=new Cell(1,4,Tetris.O);
            cells[3]=new Cell(1,5,Tetris.O);
            states=new State[]{
                    new State(0,0,0,0,0,0,0,0),
                    new State(0,0,0,0,0,0,0,0),
                    new State(0,0,0,0,0,0,0,0),
                    new State(0,0,0,0,0,0,0,0)
            };
        }
    }

    class I extends Tetromino{
        public I(){
            cells[0]=new Cell(0,4,Tetris.I);
            cells[1]=new Cell(0,3,Tetris.I);
            cells[2]=new Cell(0,5,Tetris.I);
            cells[3]=new Cell(0,6,Tetris.I);
            states=new State[]{
                    new State(0,0, 1,-1,-1, 1,-2, 2),
                    new State(0,0,-1, 1, 1,-1, 2,-2),
                    new State(0,0, 1,-1,-1, 1,-2, 2),
                    new State(0,0,-1, 1, 1,-1, 2,-2)
            };
        }
    }
}



