package game.world;

import java.awt.Color;
import java.awt.image.BufferedImage;

import game.Vector2i;
import game.world.tile.Tile;
import main.Canvas;
import main.Game;

public class MiniMap {

	private BufferedImage map;
	private World world;
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	private boolean visible;
	
	public MiniMap(World world, int x, int y, int width, int height) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		init_map();
		
		visible = true;
	}
	
	public void init_map() {
		map = new BufferedImage(world.getWidth(), world.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				Vector2i worldCoords = new Vector2i(i, j);
				
				Tile t = world.getTile(worldCoords);
				if (t != null) {
					switch (t.getIDMajor()) {
					case 0:
						map.setRGB(i, j, 0x7cfc00);
						break;
					case 1:
						map.setRGB(i, j, 0x0077be);
						break;
					case 2:
						map.setRGB(i, j, 0xc2b280);
						break;
					case 3:
						map.setRGB(i, j, 0xcf1020);
						break;
					case 4:
						map.setRGB(i, j, 0xcb4154);
						break;
					default:
						map.setRGB(i, j, 0x000000);
						break;
					}
				}
			}
		}
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void toggleVisible() {
		visible = !visible;
	}
	
	public void render(Canvas c) {
		
		if (!visible) {
			return;
		}
		
		c.getGraphics().drawImage(map, x, y, null);
		Vector2i min = c.getCam().reverseTransform(new Vector2i(0, 0)).div(World.SIZE);
		Vector2i max = c.getCam().reverseTransform(new Vector2i(Game.windowWidth, Game.windowHeight)).div(World.SIZE);
		int rx = min.getX();
		int ry = min.getY();
		int rw = max.getX() - rx;
		int rh = max.getY() - ry;
		c.getGraphics().setColor(Color.WHITE);
		c.getGraphics().drawRect(rx + x, ry + y, rw, rh);
		c.getGraphics().drawRect(x, y, width, height);
	}
}
