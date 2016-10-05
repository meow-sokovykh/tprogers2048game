/*
 * @GraphicsModule.java
 *
 * Version 1.0 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 */
package ru.tproger.game2048.graphics;

import ru.tproger.game2048.main.GameField;

/**
 * Определяет действия, которые должен производить графический модуль игры.
 *
 * @author DoKel
 * @version 1.0
 */
public interface GraphicsModule {

    /**
     * Отрисовывает переданное игровое поле
     *
     * @param field Игровое поле, которое необходимо отрисовать
     */
    void draw(GameField field);

    /**
     * @return Возвращает true, если в окне нажат "крестик"
     */
    boolean isCloseRequested();

    /**
     * Заключительные действия, на случай, если модулю нужно подчистить за собой.
     */
    void destroy();
}
