package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.Arrays;
import java.util.Collections;

import static main.Constants.*;
import static org.lwjgl.opengl.GL11.*;


 class GUI {

    private static Cell[][] cells = new Cell [CELLS_COUNT_X][CELLS_COUNT_Y];

    static int getState(int x, int y){
        return cells[x][y].getState();
    }

    static void setState(int x, int y, int state){
        cells[x][y].setState(state);
    }

    static void init(){
        initializeOpenGL();

        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[i].length; j++){
                cells[i][j] = new Cell(0);
            }
        }
    }

    static void draw(){
        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[i].length; j++){
                drawElement(cells[i][j].getSprite(), i*CELL_SIZE, j*CELL_SIZE);
            }
        }
    }

    private static void drawElement(Sprite sprite, int x, int y){
        sprite.getTexture().bind();

        glBegin(GL_QUADS);
        glTexCoord2f(0,0);
        glVertex2f(x,y+CELL_SIZE);
        glTexCoord2f(1,0);
        glVertex2f(x+CELL_SIZE,y+CELL_SIZE);
        glTexCoord2f(1,1);
        glVertex2f(x+CELL_SIZE, y);
        glTexCoord2f(0,1);
        glVertex2f(x, y);
        glEnd();
    }

    private static void initializeOpenGL(){
        try {
            //Задаём размер будущего окна
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));

            //Задаём имя будущего окна
            Display.setTitle(SCREEN_NAME);

            //Создаём окно
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glOrtho(0,SCREEN_WIDTH,0,SCREEN_HEIGHT,1,-1);
        glMatrixMode(GL_MODELVIEW);

		/*
		 * Для поддержки текстур
		 */
        glEnable(GL_TEXTURE_2D);

		/*
		 * Для поддержки прозрачности
		 */
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


		/*
		 * Белый фоновый цвет
		 */
        glClearColor(1,1,1,1);
    }

    ///Этот метод будет вызываться извне
    static void update() {
        updateOpenGL();
    }

    static boolean shift(Main.Direction direction) {
        boolean ret = false;

        switch(direction) {
            case UP:
            case DOWN:
                for(int i=0; i<CELLS_COUNT_X; i++){
                    Cell[] arg =  cells[i];

                    if(direction==Main.Direction.UP) Collections.reverse(Arrays.asList(arg));

                    ShiftRowResult result = shiftRow (arg);

                    if(direction==Main.Direction.UP) Collections.reverse(Arrays.asList(result.shiftedRow));

                    cells[i] = result.shiftedRow;
                    ret = ret || result.didAnythingMove;
                }
                break;
            case LEFT:
            case RIGHT:
                for(int i=0; i<CELLS_COUNT_Y; i++){
                    Cell[] arg =  new Cell[CELLS_COUNT_X];

                    for(int j=0; j<CELLS_COUNT_X; j++){
                        arg[j] = cells[j][i];
                    }

                    if(direction==Main.Direction.RIGHT) Collections.reverse(Arrays.asList(arg));

                    ShiftRowResult result = shiftRow (arg);

                    if(direction==Main.Direction.RIGHT) Collections.reverse(Arrays.asList(result.shiftedRow));


                    for(int j=0; j<CELLS_COUNT_X; j++){
                        cells[j][i] = result.shiftedRow[j];
                    }
                    ret = ret || result.didAnythingMove;
                }
        }

        return ret;
    }

    private static class ShiftRowResult{
        boolean didAnythingMove;
        Cell[] shiftedRow;
    }

     /**
      * Автор: Darth (https://tproger.ru/author/alkurmtl/)
      */
    private static ShiftRowResult shiftRow (Cell[] oldRow){
        ShiftRowResult ret = new ShiftRowResult();

        Cell[] oldRowWithoutZeroes = new Cell[oldRow.length];
        {
            int q = 0;

            for (int i = 0; i < oldRow.length; i++){
                if(oldRow[i].getState()!=0){
                   if(q!=i){
                       /// Это значит, что мы передвинули ячейку
                       /// на место какого-то нуля (пустой плитки)
                       ret.didAnythingMove = true;
                   }

                    oldRowWithoutZeroes[q] = oldRow[i];
                    q++;
                }
            }

            //Чтобы избежать null'ов в конце массива
            for(int i=q; i<oldRowWithoutZeroes.length; i++){
               oldRowWithoutZeroes[i] = new Cell(0);
            }
        }

        ret.shiftedRow = new Cell[oldRowWithoutZeroes.length];

        {
            int q = 0;

            {
                int i = 0;


                while (i < oldRowWithoutZeroes.length) {
                    if ((i+1)<oldRowWithoutZeroes.length && oldRowWithoutZeroes[i].getState() == oldRowWithoutZeroes[i + 1].getState()) {
                        ret.didAnythingMove = true;
                        ret.shiftedRow[q] = new Cell(oldRowWithoutZeroes[i].getState() * 2);
                        i++;
                    } else {
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i];
                    }

                    q++;
                    i++;
                }

            }
            //Чтобы избежать null'ов в конце массива
            for(int j=q; j<ret.shiftedRow.length; j++){
                ret.shiftedRow[j] = new Cell(0);
            }
        }

        return ret;
    }


    ///А этот метод будет использоваться только локально,
    ///т.к.  другие классы должны работать на более высоком уровне
    private static void updateOpenGL() {
        Display.update();
        Display.sync(60);
    }

    static boolean canSomethingBeMeged() {
        for(int i=0; i<cells.length; i++){
            for(int j=0; j<cells[i].length; j++){
                if (canBeMerged(i,j)) return true;
            }
        }

        return false;
    }

    private static boolean canBeMerged(int i, int j) {
        int current_state = cells[i][j].getState();

        if(current_state==0) return true;

        if(i+1<CELLS_COUNT_X){
            if(cells[i+1][j].getState()==current_state) return true;
        }

        if(j+1<CELLS_COUNT_Y){
            if(cells[i][j+1].getState()==current_state) return true;
        }

        return false;
    }
}
