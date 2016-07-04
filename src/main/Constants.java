package main;

import java.util.HashMap;

class Constants {

	///Размер одной плитки
	static final int CELL_SIZE = 64;

	///Количество ячеек на экране по горизонтали и вертикали
	static final int CELLS_COUNT_X = 4;
	static final int CELLS_COUNT_Y = 4;

	///Параметры окна
	static final int SCREEN_WIDTH =CELLS_COUNT_X*CELL_SIZE;
	static final int SCREEN_HEIGHT = CELLS_COUNT_Y*CELL_SIZE;
	static final String SCREEN_NAME = "Tproger's 2048";

	///В оригинальной игре есть небольшой шанс, что появится плитка со значением не 2, а 4
	///Этот шанс (в процентах) определяется здесь
	static final int CHANCE_OF_FOUR_SPAWN = 17; //%


	///Для удобства поместим все наши текстуры (спрайты) в HashMap,
	///поставив в качестве ключа им то число, которое на них отображено

	///Чтобы избежать модификации этой HashMap, сделаем её приватной и напишем метод,
	///через который текстуры будут запрашиваться

	private static HashMap<Integer, Sprite> sprite_by_number = new HashMap<>();

	static{
		sprite_by_number.put(2, Sprite.CELL2);
		sprite_by_number.put(4, Sprite.CELL4);
		sprite_by_number.put(8, Sprite.CELL8);
		sprite_by_number.put(16, Sprite.CELL16);
		sprite_by_number.put(32, Sprite.CELL32);
		sprite_by_number.put(64, Sprite.CELL64);
		sprite_by_number.put(128, Sprite.CELL128);
		sprite_by_number.put(256, Sprite.CELL256);
		sprite_by_number.put(512, Sprite.CELL512);
		sprite_by_number.put(1024, Sprite.CELL1024);
		sprite_by_number.put(2048, Sprite.CELL2048);
	}

	static Sprite getSpriteByNumber(int n){
		if(sprite_by_number.containsKey(n)){
			return sprite_by_number.get(n);
		}

		return Sprite.EMPTY;
	}

}
