package game.world;

import java.util.ArrayList;

import game.Vector2i;
import game.entity.Entity;
import game.world.tile.Tile;
import game.world.tile.TileData;
import game.world.tile.TileManager;
import main.Canvas;

public class World {

	public static final int SIZE = 32;
	
	private Tile[][] tiles;
	
	private ArrayList<Entity> entities;
	
	private int width;
	private int height;
	
	public World(int width, int height, int id_major, int id_minor) {
		this.tiles = new Tile[width][height];
		
		this.width = width;
		this.height = height;
		
		this.entities = new ArrayList<>();
		
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				setTile(i, j, id_major, id_minor);
			}
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void update(double deltaSeconds) {
		for (int i = 0; i < tiles.length; i ++) {
			for (int j = 0; j < tiles[i].length; j ++) {
				tiles[i][j].update(deltaSeconds);
			}
		}
		
		for (Entity e : entities) {
			e.update(deltaSeconds);
		}
		updateOverlays();
	}
	
	public void render(Canvas c) {
		Vector2i min = c.getMin();
		Vector2i max = c.getMax();
		int mx = Math.max(min.getX()/SIZE, 0);
		int my = Math.max(min.getY()/SIZE, 0);
		int Mx = Math.min(max.getX()/SIZE + 1, tiles.length);
		int My = Math.min(max.getY()/SIZE + 2, tiles[0].length);
		
		ArrayList<ArrayList<Entity>> layered_entities = new ArrayList<>();
		for (int i = 0; i < My - my; i ++) {
			layered_entities.add(new ArrayList<>());
		}
		
		for (Entity e : entities) {
			int discrete_y = e.getY() / World.SIZE;
			
			layered_entities.get(discrete_y - my).add(e);
		}
		
		for (int j = my; j < My; j ++) {			
			for (int i = mx; i < Mx; i ++) {
				tiles[i][j].render(c);
			}
			
			for (Entity e : layered_entities.get(j-my)) {
				e.render(c);
			}
		}
	}
	

	public void setTile(int x, int y, TileData tileData) {
		if (x < 0 || x > tiles.length || y < 0 || y > tiles[0].length) {
			return;
		}
		if (tiles[x][y] == null) {
			tiles[x][y] = new Tile(tileData, x, y);
			return;
		}
		tiles[x][y].setData(tileData);
	}
	public void setTile(int x, int y, int id_major, int id_minor) {
		setTile(x, y, TileManager.getTileData(id_major, id_minor));
	}
	
	public void updateOverlays() {
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				updateOverlay(i, j);
			}
		}
	}
	
	public void updateOverlay(int i, int j) {
		Tile t = getTile(i, j);
		if (!TileManager.isOverlayable(t.getIDMajor())) {
			return;
		}
		
		if (i > 0) {
			t.setLeftOverlay(TileManager.isLiquid(getTile(i-1, j).getIDMajor()));
		}
		if (j > 0) {
			t.setTopOverlay(TileManager.isLiquid(getTile(i, j-1).getIDMajor()));
		}
		if (i < width-1) {
			t.setRightOverlay(TileManager.isLiquid(getTile(i+1, j).getIDMajor()));
		}
		if (j < height-1) {
			t.setBottomOverlay(TileManager.isLiquid(getTile(i, j+1).getIDMajor()));
		}
	}
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
			return null;
		}
		
		return tiles[x][y];
	}
	
	public Tile getTile(Vector2i pos) {
		return getTile(pos.getX(), pos.getY());
	}
	
	public int getWidth() {
		return tiles.length;
	}
	
	public int getHeight() {
		return tiles[0].length;
	}
}
