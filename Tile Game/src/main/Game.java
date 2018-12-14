package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import game.entity.EntityManager;
import game.world.structure.StructureManager;
import game.world.tile.TileManager;

@SuppressWarnings("serial")
public class Game extends JFrame implements KeyListener, MouseListener {
	
	private boolean isRunning = true;

	public static final double ANIMATION_CHANGE = 0.6;
	public static final int ANIMATION_FRAMES = 2;
	private static int animationFrame;
	private static double lastAnimationFrame;
	
	public static int windowWidth = 800;
	public static int windowHeight = 608;
	
	private static final int layers = 2;
	
	private BufferedImage backBuffer;
	private BufferedImage[] backbuffer_layers;
	private Insets insets;
	
	private GameManager gm;
	
	private Game() {
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
	}

	private void run() {
		initialize();
		
		double lastFrame = System.currentTimeMillis();
		
		int frames = 0;
		double frameTime = 0.;
		
		// Animation
		lastAnimationFrame = 0.f;
		animationFrame = 0;
		
		while(isRunning) 
        { 
//            long time = System.currentTimeMillis(); 

			double deltaTime;
			frames ++;
            update(deltaTime = (System.currentTimeMillis() - lastFrame) / 1000.f); 
			lastFrame = System.currentTimeMillis();
			frameTime += deltaTime;
			
			if (frameTime >= 1.) {
				frameTime -= 1.;
				setTitle("FPS: " + frames);
				frames = 0;
			}
			
			// Animation
			if ((lastAnimationFrame += deltaTime) > ANIMATION_CHANGE) {
				lastAnimationFrame -= ANIMATION_CHANGE;
				animationFrame = (animationFrame + 1) % ANIMATION_FRAMES;
			}
			
            render();
        } 

        setVisible(false); 
	}
	
	private void initialize() {
		setTitle("Tile Game");
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		insets = getInsets();
		setSize(insets.left + windowWidth + insets.right,
				insets.top + windowHeight + insets.bottom);
		
		backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
		backbuffer_layers = new BufferedImage[layers];
		for (int i = 0; i < layers; i ++) {
			backbuffer_layers[i] = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
		}
		
		
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/")));
		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		
		ResourceManager.init();
		AudioManager.init();
		TileManager.init();
		EntityManager.init();
		StructureManager.init();
		
		gm = new GameManager(windowWidth, windowHeight);
	}
	
	private void update(double deltaSeconds) {
		InputManager.update(this);
		gm.update(deltaSeconds);
	}
	
	private void render() {		
		Graphics2D g = (Graphics2D) getGraphics();
		
		Graphics2D[] bggs = new Graphics2D[layers];
		for (int i = 0; i < layers; i ++) {
			backbuffer_layers[i] = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
			bggs[i] = backbuffer_layers[i].createGraphics();
			
			bggs[i].setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				    RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			
			bggs[i].setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		}
		
		
		gm.render(bggs);
		
		Graphics2D bgg = backBuffer.createGraphics();
		
		for (int i = 0; i < layers; i ++) {
			bgg.drawImage(backbuffer_layers[i], 
					0, 
					0, 
					null);
		}
		
		g.drawImage(backBuffer, insets.left, insets.top, null);
	}
	
	public static int Frame() {
		return animationFrame;
	}
	
	public static void main (String[] args) {
		// Initialize Window
		
		Game game = new Game();
		game.run();
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		InputManager.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		InputManager.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		InputManager.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		InputManager.mouseReleased(e);
	}
}
