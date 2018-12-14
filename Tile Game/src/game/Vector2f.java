package game;

public class Vector2f {

	private final float x;
	private final float y;
	
	public Vector2f(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2f Zero() {
		return new Vector2f(0, 0);
	}
	
	public Vector2f add(Vector2f other) {
		return new Vector2f(this.x + other.x, this.y + other.y);
	}
	
	public Vector2f sub(Vector2f other) {
		return new Vector2f(this.x - other.x, this.y - other.y);
	}
	
	public Vector2f mult(float k) {
		return new Vector2f(this.x * k, this.y * k);
	}
	
	public float dot(Vector2f other) {
		return this.x * other.x + this.y * other.y;
	}
	
	public static Vector2f min(Vector2f u, Vector2f v) {
		return new Vector2f(Math.min(u.x, v.x), Math.min(u.y, v.y));
	}
	
	public static Vector2f max(Vector2f u, Vector2f v) {
		return new Vector2f(Math.max(u.x, v.x), Math.max(u.y, v.y));
	}
	
	public float magnitude() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	public Vector2f normalized() {
		float mag = this.magnitude();
		if (mag == 0) {
			return this.copy();
		}
		return new Vector2f(x / mag, y / mag);
	}
	
	public Vector2f h_component() {
		return new Vector2f (x, 0);
	}
	
	public Vector2f v_component() {
		return new Vector2f (0, y);
	}
	
	public Vector2f copy() {
		return new Vector2f(this.x, this.y);
	}
	
	public Vector2i i() {
		return new Vector2i((int) x, (int) y);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Vector2f [x=" + x + ", y=" + y + "]";
	}

}
