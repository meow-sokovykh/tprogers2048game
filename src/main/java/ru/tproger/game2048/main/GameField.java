/*
 * @GameField.java
 *
 * Version 1.0 (7.07.2016)
 *
 * Распространяется под копилефтной лицензией GNU GPL v3
 */
package ru.tproger.game2048.main;

import static ru.tproger.game2048.main.Constants.COUNT_CELLS_X;

/**
 * Хранит и изменяет состояние игрового поля
 *
 * @author DoKel
 * @version 1.0
 */
public class GameField {
    /**
     * Состояние всех ячеек поля.
     */
    private int[][] theField;

    /**
     * Инициализирует поле и заполняет его нулями
     */
    public GameField(){
        theField = new int[COUNT_CELLS_X][Constants.COUNT_CELLS_Y];

        for(int i=0; i<theField.length;i++){
            for(int j=0; j<theField[i].length; j++){
                theField[i][j]=0;
            }
        }
    }

    /**
     * Возвращает состояние ячейки поля по координатам
     *
     * @param x Координата ячейки X
     * @param y Координата ячейки Y
     * @return Состояние выбранной ячейки
     */
    public int getState(int x, int y){
        return theField[x][y];
    }

    /**
     * Изменяет состояние ячейки поля по координатам
     *
     * @param x Координата ячейки X
     * @param y Координата ячейки Y
     * @param state Новое состояние для этой ячейки
     */
    public void setState(int x, int y, int state){
        //TODO check input maybe?

        theField[x][y] = state;
    }

    /**
     * Изменяет столбец под номером i
     *
     * @param i Номер изменяемого столбца
     * @param newColumn Массив новых состояний ячеек столбца
     */
    public void setColumn(int i, int[] newColumn) {
        theField[i] = newColumn;
    }

    /**
     * Возвращает массив состояний ячеек столбца под номером i
     *
     * @param i Номер запрашиваемого столбца
     * @return Массив состояний ячеек столбца
     */
    public int[] getColumn(int i) {
        return theField[i];
    }

    /**
     * Изменяет строку под номером i
     *
     * @param i Номер изменяемой строки
     * @param newLine Массив новых состояний ячеек строки
     */
    public void setLine(int i, int[] newLine) {
        for(int j = 0; j< COUNT_CELLS_X; j++){
            theField[j][i] = newLine[j];
        }
    }

    /**
     * Возвращает массив состояний ячеек строки под номером i
     *
     * @param i Номер запрашиваемой строки
     * @return Массив состояний ячеек строки
     */
    public int[] getLine(int i) {
        int[] ret = new int[COUNT_CELLS_X];

        for(int j = 0; j< COUNT_CELLS_X; j++){
            ret[j] = theField[j][i];
        }

        return ret;
    }

}
