package ru.tproger.game2048.graphics.lwjglmodule;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;

/**
 * Загружает и хранит все доступные в игре текстуры
 */
public enum LwjglSprite {
    CELL2(2), CELL4(4), CELL8(8), CELL16(16), CELL32(32), CELL64(64),
    CELL128(128), CELL256(256), CELL512(512), CELL1024(1024), CELL2048(2048), EMPTY("empty");

    private Texture texture;
    private Integer spriteNumber;

    LwjglSprite(String textureName) {
        this(textureName, null);
    }

    LwjglSprite(int spriteNumber) {
        this(String.valueOf(spriteNumber), spriteNumber);
    }

    LwjglSprite(String texturename, Integer spriteNumber) {
        this.spriteNumber = spriteNumber;
        try {
            texture = TextureLoader.getTexture(
                "PNG",
                getClass().getClassLoader().getResourceAsStream(String.format("texture/%s.png", texturename))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Texture getTexture() {
        return this.texture;
    }

    Integer getSpriteNumber() {
        return spriteNumber;
    }
}
