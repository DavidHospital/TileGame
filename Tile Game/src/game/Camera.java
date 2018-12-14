package game;

public class Camera {

	private Vector2f pos;
	
	private Vector2f boundTL;
	private Vector2f boundBR;
	
	public Camera(Vector2f pos) {
		this.pos = pos;
		
		boundTL = null;
		boundBR = null;
	}
	
	public void translate(Vector2f dpos) {
		setPosition(pos.add(dpos));
	}
	
	public void removeBounds() {
		boundTL = null;
		boundBR = null;
	}
	
	public void setBounds(int left, int top, int right, int bottom) {
		boundTL = new Vector2f(left, top);
		boundBR = new Vector2f(right, bottom);
	}
	
	/**
	 * @brief Transforms a point in world space to a position on the screen.
	 * @detail (Point - cam.pos)
	 * @param point world position
	 * @return screen position
	 */
	public Vector2i transform(Vector2i point) {
		return point.sub(pos.i());
	}
	
	/**
	 * @brief Transforms a point in screen space to a position in the world.
	 * @detail (Point + cam.pos)
	 * @param point screen position
	 * @return world position
	 */
	public Vector2i reverseTransform(Vector2i point) {
		return pos.i().add(point);
	}

	public void setPosition(Vector2f position) {
		this.pos = position;
		this.pos = Vector2f.max(boundTL, Vector2f.min(pos, boundBR));
	}
	
}
