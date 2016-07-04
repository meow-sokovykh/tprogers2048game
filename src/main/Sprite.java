package main;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

enum Sprite {
	CELL2("2"),CELL4("4"),CELL8("8"),CELL16("16"),CELL32("32"),CELL64("64"),
	CELL128("128"),CELL256("256"),CELL512("512"),CELL1024("1024"),CELL2048("2048"), EMPTY("empty");

	private Texture texture;
	 
	Sprite(String texturename){
	try {
	        this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/"+texturename+".png")));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	 
	}
	
	public Texture getTexture(){
	     return this.texture;
	}
}
