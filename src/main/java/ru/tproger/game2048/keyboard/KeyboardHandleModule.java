/*
 * @KeyboardHandleModule.java
 *
 * Version 1.0 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 *
 */
package ru.tproger.game2048.keyboard;

/**
 * Определяет параметры, которые игре необходимо считывать с клавиатуры.
 *
 * @author DoKel
 * @version 1.0
 */
public interface KeyboardHandleModule {

    /**
     * Считывание последних данных из стека событий, если можулю это необходимо
     */
    void update();

    /**
     * @return Возвращает направление последней нажатой "стрелочки",
     * либо AWAITING, если не было нажато ни одной
     */
    ru.tproger.game2048.main.Direction lastDirectionKeyPressed();

    /**
     * @return Возвращает информацию о том, был ли нажат ESCAPE за последнюю итерацию
     */
    boolean wasEscPressed();

}
