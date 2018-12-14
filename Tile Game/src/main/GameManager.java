package main;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import game.Camera;
import game.Vector2f;
import game.entity.Player;
import game.world.MiniMap;
import game.world.World;
import game.world.WorldEditor;
import game.world.WorldManager;
import game.world.structure.StructureManager;
import game.world.tile.TileManager;

public class GameManager {
	
	private World world;
	private WorldEditor worldEditor;
	
	private Camera cam;
	private MiniMap map;
	
	private Player player;
	
	public GameManager(int windowWidth, int windowHeight) {
		
		String file = "1.world";
		
		//this.world = WorldManager.fromFile(file);
		int width = 200;
		int height = 200;
		this.world = WorldManager.GenerateFromSimplex(width, height);
		
		for (int i = 0; i < 10; i ++) {
			StructureManager.Generate(world, "house");
		}
		
		
		this.cam = new Camera(new Vector2f(0, 0));
		cam.setBounds(0, 0, world.getWidth() * World.SIZE - windowWidth, world.getHeight() * World.SIZE - windowHeight);
		
		this.worldEditor = new WorldEditor(world, cam, file);
		
		map = new MiniMap(world, 10, 10, width, height);
		
		int x;
		int y;
		do {
			x = (int) (Math.random() * width);
			y = (int) (Math.random() * height);
		} while (!TileManager.isGround(world.getTile(x, y).getIDMajor()));
		player = new Player(0, world, new Vector2f(x * World.SIZE, y * World.SIZE));
		
		world.addEntity(player);
		
		//AudioManager.Play("overworld", 0.6f, true);
	}
	
	void update(double deltaSeconds) {
		world.update(deltaSeconds);
		worldEditor.update();
		
		this.cam.setPosition(player.getPosition().sub(new Vector2f(Game.windowWidth/2, Game.windowHeight/2)));
		
		if (InputManager.isKeyPressed(KeyEvent.VK_P)) {
			AudioManager.Play("overworld", 0.5f, true);
		}
		if (InputManager.isKeyPressed(KeyEvent.VK_O)) {
			AudioManager.Stop("overworld");
		}
		
		if (InputManager.isKeyPressed(KeyEvent.VK_M)) {
			map.toggleVisible();
		}
	}
	
	void render(Graphics2D[] g) {
		Canvas c = new Canvas(g, cam);
		
		world.render(c);
		worldEditor.render(c);
		
		map.render(c);
	}

}
