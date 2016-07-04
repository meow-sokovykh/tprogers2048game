package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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
                    ret = shift_column(i, direction, direction==Main.Direction.UP?CELLS_COUNT_Y-1:0) || ret;
                }
                break;
            case LEFT:
            case RIGHT:
                for(int i=0; i<CELLS_COUNT_Y; i++){
                    ret = shift_line(i, direction, direction==Main.Direction.RIGHT?CELLS_COUNT_X-1:0) || ret;
                }
                break;
        }

        return ret;
    }

   private static boolean shift_column(int n, Main.Direction direction, int curr_y) {
        boolean ret = false;
        int shift1 = direction== Main.Direction.UP?-1:1;
        int shift_now = shift1;

        MergeState tmp_state;

        while ((tmp_state=merge_cells(n, curr_y, n, curr_y+shift_now))!=MergeState.COMBINED && tmp_state!=MergeState.CANT_MERGE){
            shift_now+=shift1;
            ret = ret || (tmp_state==MergeState.MOVED_TO_EMPTY);
            if(curr_y+shift_now<0 || curr_y+shift_now>=CELLS_COUNT_Y) return ret;
        }
        ret = ret || (tmp_state==MergeState.COMBINED);

        if(curr_y+2*shift1>=0 && curr_y+2*shift1<CELLS_COUNT_Y)
            return shift_column(n, direction, curr_y+shift1) || ret;
        return ret;
    }

    private static boolean shift_line(int n, Main.Direction direction, int curr_x) {
        boolean ret = false;
        int shift1 = direction== Main.Direction.RIGHT?-1:1;
        int shift_now = shift1;

        MergeState tmp_state;

        while ( (tmp_state=merge_cells(curr_x, n, curr_x+shift_now, n))!=MergeState.COMBINED
                && tmp_state!=MergeState.CANT_MERGE){

                shift_now+=shift1;
                ret = ret || (tmp_state==MergeState.MOVED_TO_EMPTY);
                if(curr_x+shift_now<0 || curr_x+shift_now>=CELLS_COUNT_X) return ret;
        }
        ret = ret || (tmp_state==MergeState.COMBINED);

        if(curr_x+2*shift1>=0 && curr_x+2*shift1<CELLS_COUNT_X)
            return shift_line(n, direction, curr_x+shift1) || ret;
        return ret;
    }

    private enum MergeState{ MOVED_TO_EMPTY, COMBINED, DID_NOTHING, CANT_MERGE}

    private static MergeState merge_cells(int x, int y, int x2, int y2){

        if( cells[x2][y2].getState()==0) return MergeState.DID_NOTHING;

        if(cells[x][y].getState()==0){
            cells[x][y].setState(cells[x2][y2].getState());
            cells[x2][y2].setState(0);
            return MergeState.MOVED_TO_EMPTY;
        }

        if(cells[x][y].getState()==cells[x2][y2].getState()){
            cells[x][y].setState(cells[x][y].getState() + cells[x2][y2].getState());
            cells[x2][y2].setState(0);
            if(cells[x][y].getState()==2048) Main.merged2048();
            return MergeState.COMBINED;
        }

        return MergeState.CANT_MERGE;
    } //*/

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
