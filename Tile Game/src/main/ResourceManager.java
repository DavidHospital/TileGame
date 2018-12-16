package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ResourceManager {
	
	private static HashMap<String, BufferedImage> sprites;
	private static HashMap<String, SpriteSheet> spriteSheets;
	

	public static void init() {
		loadImages();
	}
	
	/**
	 * load all images from the "assets/sprites" folder and store them in 'images'
	 */
	private static void loadImages() {
		
		sprites = new HashMap<>();
		spriteSheets = new HashMap<>();
		
		File f = new File ("assets/sprites");	
		for (File image : f.listFiles()) {											//for all files within f
			try {								
				StringTokenizer st = new StringTokenizer (image.getName(), ".");	
				String name = st.nextToken();
				
				/*	put the name of the image (located before the '.' in the file extension)
				 * 	into 'images' along with the read file
				 */
				if (name.charAt(0) == '~') {
					String s = "";
					int i = 1;
					while (name.charAt(i++) != '~') {
						s += name.charAt(i-1);
					}
					int size = Integer.parseInt(s);
					SpriteSheet ss = new SpriteSheet(ImageIO.read(image), size);
					spriteSheets.put(name.substring(i), ss);
				}
				sprites.put (name, ImageIO.read(image));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static BufferedImage getImg(String key) {
		if (sprites.containsKey(key)) {
			return deepCopy(sprites.get(key));
		}
		return null;
	}
	
	public static BufferedImage getSSImg(String key, int x, int y) {
		if (spriteSheets.containsKey(key)) {
			return deepCopy(spriteSheets.get(key).getImg(x, y));
		}
		return null;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}

class SpriteSheet {
	
	private final int width;
	private final int height;
	
	private BufferedImage[][] imgs;
	
	protected SpriteSheet(BufferedImage spriteSheet, int size) {

		this.width = spriteSheet.getWidth() / size;
		this.height = spriteSheet.getHeight() / size;
		
		imgs = new BufferedImage[width][height];
		
		for (int i = 0; i < width; i ++) {
			for (int j = 0; j < height; j ++) {
				imgs[i][j] = new BufferedImage(size, size, spriteSheet.getType());
				Graphics2D g = imgs[i][j].createGraphics();
				g.drawImage(spriteSheet.getSubimage(i * size, j * size, size, size), 0, 0, null);
			}
		}
	}
	
	protected BufferedImage getImg(int x, int y) {
		if (x < 0 || x > width || y < 0 || y > height) {
			return null;
		}
		return imgs[x][y];
	}
	
	
}











