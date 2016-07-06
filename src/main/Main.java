package main;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import java.util.Random;

import static main.Constants.CELLS_COUNT_X;
import static main.Constants.CELLS_COUNT_Y;
import static main.Constants.CHANCE_OF_FOUR_SPAWN;

/*
Спасибо Yury Megumin (https://vk.com/feepro) за идею написать этот клон.
Спасибо Анастасии В. за вдохновение для написания этого кода <3
 */

public class Main {

    private static int score;
    private static boolean end_of_game;
    private static boolean isThere2048;

    enum Direction{
        AWAITING, UP, DOWN, LEFT, RIGHT
    }
    private static Direction direction = Direction.AWAITING;

    public static void main(String[] args) {
        score=0;
        end_of_game=false;
        isThere2048=false;

        GUI.init();
        generate_new_cell();
        generate_new_cell();

        while(!end_of_game){

            input();

            logic();

            GUI.draw();
            GUI.update();
        }

        Display.destroy();

        System.out.println("You "+(isThere2048?"won :)":"lost :("));
        System.out.println("Your score is "+score);
    }

    private static void logic(){
        if(!canMove()){
            end_of_game = true;
        }

        if(direction!=Direction.AWAITING){
            if(GUI.shift(direction)) generate_new_cell();

            direction=Direction.AWAITING;
        }
    }

    private static boolean canMove(){
        return GUI.canSomethingBeMeged();
    }

    static void merged2048(){
        end_of_game=true;
        isThere2048=true;
    }

    private static void generate_new_cell() {
        int state = new Random().nextInt(100)<=CHANCE_OF_FOUR_SPAWN?4:2;

        int random_x, random_y;

        int current_x = random_x = new Random().nextInt(CELLS_COUNT_X);
        int current_y = random_y = new Random().nextInt(CELLS_COUNT_Y);



        boolean placed = false;
        while(!placed){
            if(GUI.getState(current_x, current_y)==0){
                GUI.setState(current_x, current_y, state);
                placed = true;
            }else{
                if(current_x+1<CELLS_COUNT_X){
                    current_x++;
                }else{
                    current_x = 0;
                    if(current_y+1<CELLS_COUNT_Y) {
                        current_y++;
                    }else{
                        current_y=0;
                    }
                }

                if(current_x==random_x&&current_y==random_y){
                    //No place -> Something went wrong
                    System.err.println("ERROR WHILE CREATING NEW CELL -- NO PLACE FOUND");
                    System.exit(-1);
                    break;
                }
            }
        }

        score+=state;

    }

    private static void input() {

            ///Если произошли какие-то события с клавиатурой,
            ///перебираем их по очереди
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    switch(Keyboard.getEventKey()){
                        case Keyboard.KEY_ESCAPE:
                            end_of_game = true;
                            break;
                        case Keyboard.KEY_UP:
                            direction = Direction.UP;
                            break;
                        case Keyboard.KEY_RIGHT:
                            direction = Direction.RIGHT;
                            break;
                        case Keyboard.KEY_DOWN:
                            direction = Direction.DOWN;
                            break;
                        case Keyboard.KEY_LEFT:
                            direction = Direction.LEFT;
                            break;
                    }
                }
            }

        ///Обрабатываем клик по кнопке "закрыть" окна
        end_of_game = end_of_game || Display.isCloseRequested();
    }

}
