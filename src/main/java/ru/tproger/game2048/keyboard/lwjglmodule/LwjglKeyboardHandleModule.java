/*
 * @LwjglKeyboardHandleModule.java
 *
 * Version 1.0 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 */
package ru.tproger.game2048.keyboard.lwjglmodule;

import org.lwjgl.input.Keyboard;
import ru.tproger.game2048.keyboard.KeyboardHandleModule;
import ru.tproger.game2048.main.Direction;

/**
 * Реализует считывание необходимых игре параметров с клавиатуры средствами LWJGL
 *
 * @author DoKel
 * @version 1.0
 */
public class LwjglKeyboardHandleModule implements KeyboardHandleModule {

    /* Данные о вводе за последнюю итераци. */
    private boolean wasEscPressed;
    private Direction lastDirectionKeyPressed;

    /**
     * Считывание последних данных из стека событий
     */
    @Override
    public void update() {
        resetValues();
        lastDirectionKeyPressed = Direction.AWAITING;

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch(Keyboard.getEventKey()){
                    case Keyboard.KEY_ESCAPE:
                        wasEscPressed = true;
                        break;
                    case Keyboard.KEY_UP:
                        lastDirectionKeyPressed = Direction.UP;
                        break;
                    case Keyboard.KEY_RIGHT:
                        lastDirectionKeyPressed = Direction.RIGHT;
                        break;
                    case Keyboard.KEY_DOWN:
                        lastDirectionKeyPressed = Direction.DOWN;
                        break;
                    case Keyboard.KEY_LEFT:
                        lastDirectionKeyPressed = Direction.LEFT;
                        break;
                }
            }
        }
    }

    /**
     * Обнуление данных, полученых в при предыдущих запросах
     */
    private void resetValues() {
        lastDirectionKeyPressed = Direction.AWAITING;
        wasEscPressed = false;
    }

    /**
     * @return Возвращает направление последней нажатой "стрелочки",
     * либо AWAITING, если не было нажато ни одной
     */
    @Override
    public Direction lastDirectionKeyPressed() {
        return lastDirectionKeyPressed;
    }

    /**
     * @return Возвращает информацию о том, был ли нажат ESCAPE за последнюю итерацию
     */
    @Override
    public boolean wasEscPressed() {
        return wasEscPressed;
    }


}
