package main;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import javax.swing.JFrame;

public class InputManager {
	
	// Holds the current keys and mouse buttons being pressed
	private static HashSet<Integer> keyDowns;
	private static HashSet<Integer> mouseDowns;
	
	// New keys and buttons
	private static HashSet<Integer> newKeyDowns;
	private static HashSet<Integer> newMouseDowns;
	
	// Old keys and buttons
	private static HashSet<Integer> oldKeyDowns;
	private static HashSet<Integer> oldMouseDowns;
	
	private static int mouseX;
	private static int mouseY;
	
	static {
		
		// Initialize the key and mouse lists
		keyDowns = new HashSet<>();
		mouseDowns = new HashSet<>();
		
		newKeyDowns = new HashSet<>();
		newMouseDowns = new HashSet<>();
		
		oldKeyDowns = new HashSet<>();
		oldMouseDowns = new HashSet<>();
		
	}
	
	@SuppressWarnings("unchecked")
	protected static void update(JFrame context) {
		oldKeyDowns = (HashSet<Integer>) keyDowns.clone();
		keyDowns = (HashSet<Integer>) newKeyDowns.clone();
		
		oldMouseDowns = (HashSet<Integer>) mouseDowns.clone();
		mouseDowns = (HashSet<Integer>) newMouseDowns.clone();
		
		mouseX = MouseInfo.getPointerInfo().getLocation().x - context.getLocation().x - context.getInsets().left;
		mouseY = MouseInfo.getPointerInfo().getLocation().y - context.getLocation().y - context.getInsets().top;
		if (mouseX < 0 || mouseX > Game.windowWidth -1) {
			mouseX = -1;
		}
		if (mouseY < 0 || mouseY > Game.windowHeight -1) {
			mouseY = -1;
		}
	}
	
	/*
	 * Add key and mouse presses and releases
	 */
	protected static void keyPressed(KeyEvent e) {
		newKeyDowns.add(e.getKeyCode());
	}
	
	protected static void keyReleased(KeyEvent e) {
		newKeyDowns.remove(e.getKeyCode());
	}
	
	protected static void mousePressed(MouseEvent e) {
		newMouseDowns.add(e.getButton());
	}
	
	protected static void mouseReleased(MouseEvent e) {
		newMouseDowns.remove(e.getButton());
	}
	
	
	/*
	 * Check if key or mouse is down
	 */
	public static boolean isKeyDown(int keyCode) {
		return keyDowns.contains(keyCode);
	}
	
	public static boolean isButtonDown(int button) {
		return mouseDowns.contains(button);
	}
	
	/*
	 * Check if key or mouse is pressed
	 */
	public static boolean isKeyPressed(int keyCode) {
		return keyDowns.contains(keyCode) && !oldKeyDowns.contains(keyCode);
	}
	
	public static boolean isButtonPressed(int button) {
		return mouseDowns.contains(button) && !oldMouseDowns.contains(button);
	}
	
	/*
	 * Check if key or mouse is released
	 */
	public static boolean isKeyReleased(int keyCode) {
		return !keyDowns.contains(keyCode) && oldKeyDowns.contains(keyCode);
	}
	
	public static boolean isButtonReleased(int button) {
		return !mouseDowns.contains(button) && oldMouseDowns.contains(button);
	}
	
	/*
	 * Get Mouse location on screen
	 */
	public static int getMouseX() {
		return mouseX;
	}
	
	public static int getMouseY() {
		return mouseY;
	}
}
