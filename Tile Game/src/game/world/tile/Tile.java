package game.world.tile;

import java.awt.image.BufferedImage;

import game.world.World;
import main.Canvas;

public class Tile {
	
	private int i;
	private int j;
	
	private boolean left_overlay;
	private boolean top_overlay;
	private boolean right_overlay;
	private boolean bottom_overlay;
	
	private TileData tileData;
	
	public Tile(int id_major, int id_minor, int i, int j) {
		this.i = i;
		this.j = j;
		
		this.tileData = TileManager.getTileData(id_major, id_minor);
	}
	
	public Tile(TileData td, int i, int j) {
		this.i = i;
		this.j = j;
		
		this.tileData = td;
	}
	
	public void update(double deltaSeconds) {
		
	}
	
	public void render(Canvas c) {
		BufferedImage sprite = tileData.getSprite();
		if (left_overlay) {
			sprite = TileManager.overlay(sprite, TileManager.getTileData(-1, 0).getSprite());
		}
		if (top_overlay) {
			sprite = TileManager.overlay(sprite, TileManager.getTileData(-1, 1).getSprite());
		}
		if (right_overlay) {
			sprite = TileManager.overlay(sprite, TileManager.getTileData(-1, 2).getSprite());
		}
		if (bottom_overlay) {
			sprite = TileManager.overlay(sprite, TileManager.getTileData(-1, 3).getSprite());
		}
		
		int w = World.SIZE;
		int h = World.SIZE / sprite.getWidth() * sprite.getHeight();
		c.drawImage(sprite, World.SIZE * i, World.SIZE * j - (h - World.SIZE), w, h, tileData.getLayer());
	}
	
	public void setID(int id_major, int id_minor) {
		setData(TileManager.getTileData(id_major, id_minor));
	}
	
	public void setData(TileData td) {
		this.tileData = td;
		if (!TileManager.isOverlayable(tileData.getIDMajor())) {
			setLeftOverlay(false);
			setTopOverlay(false);
			setRightOverlay(false);
			setBottomOverlay(false);
		}
	}
	
	public int getIDMajor() {
		return tileData.getIDMajor();
	}
	
	public int getIDMinor() {
		return tileData.getIDMinor();
	}

	public String getName() {
		return tileData.getName();
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
	
	public void setLeftOverlay(boolean left_overlay) {
		this.left_overlay = left_overlay;
	}

	public void setTopOverlay(boolean top_overlay) {
		this.top_overlay = top_overlay;
	}

	public void setRightOverlay(boolean right_overlay) {
		this.right_overlay = right_overlay;
	}

	public void setBottomOverlay(boolean bottom_overlay) {
		this.bottom_overlay = bottom_overlay;
	}
}
