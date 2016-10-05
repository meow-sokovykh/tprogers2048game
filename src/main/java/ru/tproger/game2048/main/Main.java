/*
 * @Main.java
 *
 * Version 1.1 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 *
 * Спасибо Yury Megumin (https://vk.com/feepro) за идею написать этот клон //DoKel.
 * Спасибо Анастасии В. за вдохновение для написания этого кода <3 //DoKel
 */
package ru.tproger.game2048.main;

import ru.tproger.game2048.graphics.GraphicsModule;
import ru.tproger.game2048.graphics.lwjglmodule.LwjglGraphicsModule;
import ru.tproger.game2048.keyboard.KeyboardHandleModule;
import ru.tproger.game2048.keyboard.lwjglmodule.LwjglKeyboardHandleModule;

import java.util.Random;

import static ru.tproger.game2048.main.Constants.*;

/**
 *
 * Главный управляющий класс.
 * Считывает действия пользователя, передаёт данные в graphicsModule, затем обрабатывает результат.
 *
 * @version 1.1
 * @author DoKel
 *
 */

public class Main {


    /**
     * Результат работы метода сдвига shiftRow().
     * Содержит изменённую строку и информацию о том, эквивалентна ли она начальной.
     */
    private static class ShiftRowResult{
        boolean didAnythingMove;
        int[] shiftedRow;
    }

    private static int score; //Сумма всех чисел на поле
    private static boolean endOfGame; //Флаг для завершения основного цикла программы
    private static boolean isThere2048; //Хранит информацию о том, удалось ли игроку создать плитку 2048 (флаг победы)
    private static Direction direction; //Направление, в котором требуется сдвиг клеток поля.
    private static GraphicsModule graphicsModule;
    private static KeyboardHandleModule keyboardModule;
    private static GameField gameField;

    /**
     * Точка входа. Содержит все необходимые действия для одного игрового цикла.
     */
    public static void main(String[] args) {
        initFields();
        createInitialCells();

        while(!endOfGame){

            input();
            logic();

            graphicsModule.draw(gameField);
        }

        graphicsModule.destroy();

        printGameResult();
    }

    /**
     * Задаёт значения полей для начала игры
     */
    private static void initFields() {
        score = 0;
        endOfGame = false;
        isThere2048 = false;
        direction = Direction.AWAITING;
        graphicsModule = new LwjglGraphicsModule();
        keyboardModule = new LwjglKeyboardHandleModule();
        gameField = new GameField();
    }

    /**
     * Создаёт на поле начальные ячейки
     */
    private static void createInitialCells() {
        for(int i = 0; i < COUNT_INITITAL_CELLS; i++){
            generateNewCell();
        }
    }

    /**
     * Считывает пользовательский ввод.
     * Изменяет Main.direction и endOfGame;
     */
    private static void input() {
        keyboardModule.update();

        direction = keyboardModule.lastDirectionKeyPressed();

        endOfGame = endOfGame || graphicsModule.isCloseRequested() || keyboardModule.wasEscPressed();
    }

    /**
     * Основная логика игры.
     *
     * Если пользователь определил направление, вызывает метод сдвига.
     * Если сдвиг удался, создаёт новую плитку.
     */
    private static void logic() {
        if(direction!=Direction.AWAITING){
            if(shift(direction)) generateNewCell();

            direction=Direction.AWAITING;
        }
    }

    /**
     * Описывает действия в случае победы пользователя (если пользователь создал плитку 2048).
     *
     * Сейчас: устанавливает флаг победы на true, завершает игру.
     */
    private static void merged2048() {
        endOfGame = true;
        isThere2048 = true;
    }

    /**
     * Изменяет gameField, сдвигая все ячейки в указанном направлении,
     * вызывая shiftRow() для каждой строки/столбца (в зависимости от направления)
     *
     * @param direction Направление, в котором необходимо совершить сдвиг
     * @return Возвращает true, если сдвиг прошёл успешно (поле изменилось)
     */
    private static boolean shift(Direction direction) {
        boolean ret = false;

        switch(direction) {
            case UP:
            case DOWN:

                /*По очереди сдвигаем числа всех столбцов в нужном направлении*/
                for(int i = 0; i< Constants.COUNT_CELLS_X; i++){
                    /*Запрашиваем очередной столбец*/
                    int[] arg =  gameField.getColumn(i);

                    /*В зависимости от направления сдвига, меняем или не меняем порядок чисел на противоположный*/
                    if(direction==Direction.UP){
                        int[] tmp = new int[arg.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = arg[tmp.length-e-1];
                        }
                        arg = tmp;
                    }

                    /*Пытаемся сдвинуть числа в этом столбце*/
                    ShiftRowResult result = shiftRow (arg);

                    /*Возвращаем линию в исходный порядок*/
                    if(direction==Direction.UP){
                        int[] tmp = new int[result.shiftedRow.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = result.shiftedRow[tmp.length-e-1];
                        }
                        result.shiftedRow = tmp;
                    }

                    /*Записываем изменённый столбец*/
                    gameField.setColumn(i, result.shiftedRow);

                    /*Если хоть одна линия была изменена, значит было изменено всё поле*/
                    ret = ret || result.didAnythingMove;
                }
                break;
            case LEFT:
            case RIGHT:

                /*По очереди сдвигаем числа всех строк в нужном направлении*/
                for(int i = 0; i< Constants.COUNT_CELLS_Y; i++){
                    /*Запрашиваем очередную строку*/
                    int[] arg = gameField.getLine(i);

                    /*В зависимости от направления сдвига, меняем или не меняем порядок чисел на противоположный*/
                    if(direction==Direction.RIGHT){
                        int[] tmp = new int[arg.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = arg[tmp.length-e-1];
                        }
                        arg = tmp;
                    }

                    /*Пытаемся сдвинуть числа в этом столбце*/
                    ShiftRowResult result = shiftRow (arg);

                    /*Возвращаем линию в исходный порядок*/
                    if(direction==Direction.RIGHT){
                        int[] tmp = new int[result.shiftedRow.length];
                        for(int e = 0; e < tmp.length; e++){
                            tmp[e] = result.shiftedRow[tmp.length-e-1];
                        }
                        result.shiftedRow = tmp;
                    }

                    /*Записываем изменённую строку*/
                    gameField.setLine(i, result.shiftedRow);

                     /*Если хоть одна линия была изменена, значит было изменено всё поле*/
                    ret = ret || result.didAnythingMove;
                }

                break;
            default:
                ErrorCatcher.shiftFailureWrongParam();
                break;
        }

        return ret;
    }

    /**
     * Сдвигает и совмещает числа в линии по следующим правилам:
     * 1) Если в ряде есть нули, они из ряда удаляются;
     * 2) Если любые два соседних числа равны, то вместо них должно остаться одно число,
     * равное сумме этих двух чисел;
     * 3) Если число получено через пункт (2), оно не может совмещаться с другими числами.
     * 4) Проверка чисел на равенство и их совмещение происходит слева направо,
     * т.е. от 0-го элемента, к последнему.
     *
     * Если в результате сдвига получилось число 2048 (ровно) вызывает merged2048().
     *
     * Автор: Darth (https://tproger.ru/author/alkurmtl/)
     *
     * @param oldRow линия, члены которой необходимо сдвинуть и совместить
     * @return Возвращает true, если сдвиг прошёл успешно (поле изменилось)
     */
    private static ShiftRowResult shiftRow (int[] oldRow) {
        ShiftRowResult ret = new ShiftRowResult();

        int[] oldRowWithoutZeroes = new int[oldRow.length];
        {
            int q = 0;

            for (int i = 0; i < oldRow.length; i++) {
                if(oldRow[i] != 0){
                    if(q != i){
                        /*
                         * Это значит, что мы передвинули ячейку
                         * на место какого-то нуля (пустой плитки)
                         */
                        ret.didAnythingMove = true;
                    }

                    oldRowWithoutZeroes[q] = oldRow[i];
                    q++;
                }
            }

            /* Чтобы избежать null'ов в конце массива */
            for(int i = q; i < oldRowWithoutZeroes.length; i++) {
                oldRowWithoutZeroes[i] = 0;
            }
        }

        ret.shiftedRow = new int[oldRowWithoutZeroes.length];

        {
            int q = 0;

            {
                int i = 0;


                while (i < oldRowWithoutZeroes.length) {
                    if((i+1 < oldRowWithoutZeroes.length) && (oldRowWithoutZeroes[i] == oldRowWithoutZeroes[i + 1])
                            && oldRowWithoutZeroes[i]!=0) {
                        ret.didAnythingMove = true;
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i] * 2;
                        if(ret.shiftedRow[q] == 2048) merged2048();
                        i++;
                    } else {
                        ret.shiftedRow[q] = oldRowWithoutZeroes[i];
                    }

                    q++;
                    i++;
                }

            }
            //Чтобы избежать null'ов в конце массива
            for(int j = q; j < ret.shiftedRow.length; j++) {
                ret.shiftedRow[j] = 0;
            }
        }

        return ret;
    }

    /**
     * Создаёт в случайной пустой клетке игрового поля плитку (с ненулевым состоянием).
     */
    private static void generateNewCell() {
        int state = (new Random().nextInt(100) <= Constants.CHANCE_OF_LUCKY_SPAWN)
                ? LUCKY_INITIAL_CELL_STATE
                : INITIAL_CELL_STATE;

        int randomX, randomY;

        randomX = new Random().nextInt(Constants.COUNT_CELLS_X);
        int currentX = randomX;

        randomY = new Random().nextInt(Constants.COUNT_CELLS_Y);
        int currentY = randomY;



        boolean placed = false;
        while(!placed){
            if(gameField.getState(currentX, currentY) == 0) {
                gameField.setState(currentX, currentY, state);
                placed = true;
            }else{
                if(currentX+1 < Constants.COUNT_CELLS_X) {
                    currentX++;
                }else{
                    currentX = 0;
                    if(currentY+1 < Constants.COUNT_CELLS_Y) {
                        currentY++;
                    }else{
                        currentY = 0;
                    }
                }

                if ((currentX == randomX) && (currentY==randomY) ) {  //No place -> Something went wrong
                    ErrorCatcher.cellCreationFailure();
                }
            }
        }

        score += state;
    }

    /**
     * Выводит на экран результат игры пользователя -- победа или поражение, очки.
     */
    private static void printGameResult() {
        System.out.println("You " + (isThere2048 ? "won :)" : "lost :(") );
        System.out.println("Your score is " + score);
    }

}
