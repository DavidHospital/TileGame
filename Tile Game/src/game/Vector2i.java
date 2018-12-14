package game;

public class Vector2i {

	private final int x;
	private final int y;
	
	public Vector2i(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i other) {
		return new Vector2i(this.x + other.x, this.y + other.y);
	}
	
	public Vector2i sub(Vector2i other) {
		return new Vector2i(this.x - other.x, this.y - other.y);
	}
	
	public Vector2i mult(int k) {
		return new Vector2i(this.x * k, this.y * k);
	}
	
	public Vector2i div(double k) {
		if (k == 0) {
			return this;
		}
		return new Vector2i((int) (this.x / k), (int) (this.y / k));
	}
	
	public int dot(Vector2i other) {
		return this.x * other.x + this.y * other.y;
	}
	
	public float magnitude() {
		return (int) Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public Vector2f normalized() {
		float mag = this.magnitude();
		return new Vector2f(this.x / mag, this.y / mag);
	}
	
	public Vector2i copy() {
		return new Vector2i(this.x, this.y);
	}
	
	public Vector2f f() {
		return new Vector2f(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return x + ", " + y;
	}
}
