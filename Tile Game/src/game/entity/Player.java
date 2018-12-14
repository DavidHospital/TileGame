package game.entity;

import java.awt.event.KeyEvent;

import game.Vector2f;
import game.world.World;
import game.world.tile.Tile;
import game.world.tile.TileManager;
import main.Canvas;
import main.InputManager;

public class Player extends Entity {

	private float speed = 4.2f;
	private final float animation_speed = 1 / speed;
	private final int FRAMES = 4;
	private int frame = 1;
	private int direction = 0;
	
	private double lastChange = 0.;
	
	public Player(int id_minor, World world, Vector2f position) {
		super(0, id_minor, world, position, World.SIZE/2 - 7);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(double deltaSeconds) {
		
		// Get WASD inputs and apply them to translate vector
		Vector2f translate = Vector2f.Zero();
		if (InputManager.isKeyDown(KeyEvent.VK_W)) {
			translate = translate.add(new Vector2f(0, -1));
		}
		if (InputManager.isKeyDown(KeyEvent.VK_S)) {
			translate = translate.add(new Vector2f(0, 1));
		}
		if (InputManager.isKeyDown(KeyEvent.VK_D)) {
			translate = translate.add(new Vector2f(1, 0));
		}
		if (InputManager.isKeyDown(KeyEvent.VK_A)) {
			translate = translate.add(new Vector2f(-1, 0));
		}
		
		if (translate.magnitude() == 0) {
			lastChange = animation_speed;
			frame = 1;
		} else {
			lastChange += deltaSeconds;
			if (lastChange >= animation_speed) {
				lastChange -= animation_speed;
				frame = (frame + 1) % FRAMES;
			}
		}
		
		// Calculate direction
		if (translate.getY() > 0) {
			direction = 0;
		} 
		if (translate.getY() < 0) {
			direction = 3;
		}
		if (translate.getX() > 0) {
			direction = 2;
		} 
		if (translate.getX() < 0) {
			direction = 1;
		}
		float distance = (float)(deltaSeconds * speed * World.SIZE);
		
		// normalize vector so that diagonal movement is not faster
		move(translate.normalized().mult(distance));
	}

	@Override
	public void render(Canvas c) {
		// Outline tile player is currently standing on
//		Tile current = world.getTile(position
//				.add(new Vector2f(World.SIZE/2, World.SIZE/2)).mult(1.f/World.SIZE).i());
//		if (current != null) {
//			int x = current.getI();
//			int y = current.getJ();
//			c.getGraphics().setColor(Color.black);
//			c.drawRect(x * World.SIZE, y * World.SIZE, World.SIZE, World.SIZE);
//		}
		
		
		
		// Draw player
		c.drawImage(entityData.getSprites()[direction * FRAMES + frame], position.i().getX(), position.i().getY(), World.SIZE, World.SIZE, 1);
	}

	@Override
	public void move(Vector2f delta) {
		// Check tile that player is moving to
		Vector2f horizontal = delta.h_component();
		Vector2f vertical = delta.v_component();
		Vector2f x_pos = position.add(horizontal.normalized().mult(radius));
		Vector2f y_pos = position.add(vertical.normalized().mult(radius));
		Tile next_h1 = world.getTile(x_pos.add(horizontal).add(new Vector2f(0, radius + 8)).add(new Vector2f(World.SIZE/2, World.SIZE/2)).mult(1.f/World.SIZE).i());
		Tile next_h2 = world.getTile(x_pos.add(horizontal).add(new Vector2f(0, -radius + 8)).add(new Vector2f(World.SIZE/2, World.SIZE/2)).mult(1.f/World.SIZE).i());
		Tile next_v1 = world.getTile(y_pos.add(vertical).add(new Vector2f(radius, 8)).add(new Vector2f(World.SIZE/2, World.SIZE/2)).mult(1.f/World.SIZE).i());
		Tile next_v2 = world.getTile(y_pos.add(vertical).add(new Vector2f(-radius, 8)).add(new Vector2f(World.SIZE/2, World.SIZE/2)).mult(1.f/World.SIZE).i());
		if (next_h1 != null && TileManager.isGround(next_h1.getIDMajor())
		  &&next_h2 != null && TileManager.isGround(next_h2.getIDMajor())) {
			position = position.add(horizontal);	
		}
		if (next_v1 != null && TileManager.isGround(next_v1.getIDMajor())
		  &&next_v2 != null && TileManager.isGround(next_v2.getIDMajor())) {
			position = position.add(vertical);
		}
	}

	public Vector2f getPosition() {
		return position;
	}

	@Override
	public int getX() {
		return (int) position.getX();
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return (int) position.getY();
	}

	
}
