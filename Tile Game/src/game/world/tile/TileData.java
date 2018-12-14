package game.world.tile;

import java.awt.image.BufferedImage;

import main.Game;

public class TileData {

	public static final TileData SKIP = null;
	
	private int id_major;
	private int id_minor;
	private String name;
	private BufferedImage[] sprites;
	
	private int layer;
	
	public TileData(int id_major, int id_minor, String name, BufferedImage[] sprites, int layer) {
		this.id_major = id_major;
		this.id_minor = id_minor;
		this.name = name;
		this.sprites = sprites;
		this.layer = layer;
	}
	
	public TileData(int id_major, int id_minor, String name, BufferedImage[] sprites) {
		this(id_major, id_minor, name, sprites, 0);
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
	
	public int getLayer() {
		return layer;
	}
	
	public BufferedImage getSprite() {
		return sprites[Game.Frame()];
	}
	
	@Override
	public String toString() {
		return "ID: " + id_major + ":" + id_minor + "\tName: " + name;
	}
}
