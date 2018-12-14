package game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import game.Camera;
import game.Vector2i;
import game.world.tile.Tile;
import game.world.tile.TileManager;
import main.Canvas;
import main.Game;
import main.InputManager;

public class WorldEditor {

	private World world;
	
	private String file;
	
	private Tile selectedTile;
	private int x;
	private int y;
	
	private int left_id_major;
	private int left_id_minor;
	
	private int right_id_major;
	private int right_id_minor;
	
	private Camera camera;
	
	public WorldEditor(World world, Camera camera, String file) {
		this.world = world;
		this.file = file;
		
		selectedTile = null;
		
		left_id_major = 0;
		left_id_minor = 0;
		right_id_major = 1;
		right_id_minor = 0;
		
		this.camera = camera;
	}
	
	public void update() {
		
		int mx = InputManager.getMouseX();
		int my = InputManager.getMouseY();
		
		if (mx == -1 || my == -1) {
			selectedTile = null;
		} else {
			Vector2i worldPos = camera.reverseTransform(new Vector2i(mx, my));
			x = worldPos.getX() / World.SIZE;
			y = worldPos.getY() / World.SIZE;
			
			selectedTile = world.getTile(x, y);
			
			if (selectedTile != null) {
				// Edit with mouse
				if (InputManager.isButtonDown(MouseEvent.BUTTON1)) {
					selectedTile.setID(left_id_major, left_id_minor);
				} else if (InputManager.isButtonDown(MouseEvent.BUTTON3)) {
					selectedTile.setID(right_id_major, right_id_minor);
				}
			}
		}
		
		
		
		// Save
		if (InputManager.isKeyPressed(KeyEvent.VK_Q)) {
			WorldManager.saveToFile(world, file);
		}
	}
	
	public void render(Canvas c) {
		
		Graphics2D g = c.getGraphics();
		
		if (selectedTile != null) {
			g.setColor(Color.black);
			c.drawRect(x * World.SIZE, y * World.SIZE, World.SIZE, World.SIZE);
			
			Tile t = world.getTile(x, y);
			g.setColor(Color.white);
			g.drawString(String.format("(%d, %d)   ID: %d:%d; %s", 
						x, 
						y, 
						t.getIDMajor(), 
						t.getIDMinor(), 
						t.getName()),
				20, 20);
		}
		
		g.setColor(Color.white);
		g.drawString(String.format("Left click: %s,    Right click: %s", 
					TileManager.getTileData(left_id_major, left_id_minor).getName(),
					TileManager.getTileData(right_id_major, right_id_minor).getName()),
			20, Game.windowHeight - 20);
		
		String str = "Press \'q\' to save";
		g.drawString(str, Game.windowWidth - g.getFontMetrics().stringWidth(str) - 20, Game.windowHeight - 20);
	}
}
