package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.Camera;
import game.Vector2f;
import game.Vector2i;

public class Canvas {

	private Camera cam;
	
	private Graphics2D[] g;
	
	protected Canvas (Graphics2D[] g, Camera cam) {
		this.g = g;
		this.cam = cam;
	}
	
	public Graphics2D getGraphics(int depth) {
		return g[0];
	}
	
	public Graphics2D getGraphics() {
		return g[g.length-1];
	}
	
	public Camera getCam() {
		return cam;
	}

	public void drawImage(BufferedImage image, int x, int y, int w, int h, int layer) {
		Vector2i pos = new Vector2i(x, y);
		Vector2i size = new Vector2i(w, h);
		Vector2i spos = this.cam.transform(pos);
		Vector2i ssize = this.cam.transform(pos.add(size).sub(spos));
		
		g[layer].drawImage(image, (int) spos.getX(), (int) spos.getY(), (int) ssize.getX(), (int) ssize.getY(), null);
	}
	public void drawImage(BufferedImage image, int x, int y, int w, int h) {
		drawImage(image, x, y, w, h, 0);
	}
	public void drawImage(BufferedImage image, Vector2i position) {
		drawImage(image, position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}
	public void drawImage(BufferedImage image, Vector2f position) {
		drawImage(image, position.i());
	}
	
	public void drawRect(int x, int y, int w, int h) {
		Vector2i pos = new Vector2i(x, y);
		Vector2i size = new Vector2i(w, h);
		Vector2i spos = this.cam.transform(pos);
		Vector2i ssize = this.cam.transform(pos.add(size).sub(spos));
		
		g[0].drawRect((int) spos.getX(), (int) spos.getY(), (int) ssize.getX(), (int) ssize.getY());
	}
	
	public void fillRect(int x, int y, int w, int h) {
		Vector2i pos = new Vector2i(x, y);
		Vector2i size = new Vector2i(w, h);
		Vector2i spos = this.cam.transform(pos);
		Vector2i ssize = this.cam.transform(pos.add(size).sub(spos));
		
		g[0].fillRect((int) spos.getX(), (int) spos.getY(), (int) ssize.getX(), (int) ssize.getY());
	}

	public Vector2i getMin() {
		return cam.reverseTransform(new Vector2i(0, 0));
	}

	public Vector2i getMax() {
		return cam.reverseTransform(new Vector2i(Game.windowWidth, Game.windowHeight));
	}

}
