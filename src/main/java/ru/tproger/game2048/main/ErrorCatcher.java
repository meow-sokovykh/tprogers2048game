/*
 * @ErrorCatcher.java
 *
 * Version 1.0 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 *
 */

package ru.tproger.game2048.main;

/**
 * Класс предназначен для обработки возможных ошибок и/или вывода их на экран
 *
 * @author DoKel
 * @version 1.0
 */
public class ErrorCatcher {

    /**
     * Ошибка создания новой ячейки
     */
    public static void cellCreationFailure() {
        System.err.println("Main class failed to create new cell.");
        System.exit(-1);
    }

    /**
     * Передача неверного параметра Direction в метод сдвига
     */
    public static void shiftFailureWrongParam() {
        System.err.println("Main class failed to shift cells on field. Wrong parameter.");
        System.exit(-2);
    }

    /**
     * Внутренняя ошибка графического модуля
     *
     * @param e Выброшенное исключение
     */
    public static void graphicsFailure(Exception e) {
        System.err.println("GraphicsModule failed.");
        e.printStackTrace();
        System.exit(-3);
    }
}
