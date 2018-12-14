package game.entity;

import game.Vector2f;
import game.world.World;
import main.Canvas;

public abstract class Entity {

	protected Vector2f position;
	protected World world;
	
	protected EntityData entityData;
	
	protected float radius;
	
	public Entity(int id_major, int id_minor, World world, Vector2f position, float radius) {
		this.world = world;
		this.position = position;
		
		this.radius = radius;
		
		this.entityData = EntityManager.getEntityData(id_major, id_minor);
	}
	
	public abstract void update(double deltaSeconds);
	public abstract void render(Canvas c);
	
	public abstract void move(Vector2f delta);
	
	public abstract int getX();
	public abstract int getY();
}
