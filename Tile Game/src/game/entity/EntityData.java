package game.entity;

import java.awt.image.BufferedImage;

public class EntityData {

	private int id_major;
	private int id_minor;
	private String name;
	private BufferedImage[] sprites;
	
	public EntityData(int id_major, int id_minor, String name, BufferedImage[] sprites) {
		this.id_major = id_major;
		this.id_minor = id_minor;
		this.name = name;
		this.sprites = sprites;
	}

	public int getIDMajor() {
		return id_major;
	}
	
	public int getIDMinor() {
		return id_minor;
	}

	public String getName() {
		return name;
	}

	public BufferedImage[] getSprites() {
		return sprites;
	}
	
	
}
