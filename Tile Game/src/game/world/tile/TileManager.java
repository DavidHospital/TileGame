package game.world.tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import main.Game;
import main.ResourceManager;

public class TileManager {

	private static HashMap<Integer, HashMap<Integer, TileData>> tileData;
	
	public static void init() {
		tileData = new HashMap<>();
		loadTilesFromFile("basic.tile");
	}
	
	private static void loadTilesFromFile(String fileName) {
		String path = "assets/tiles/";
		File f = new File(path + fileName);
		
		try {
			Scanner r = new Scanner(f);
			
			while (r.hasNextLine()) {
				String line;
				do {
					line = r.nextLine().trim();
				} while (line.equals("\n"));
				
				StringTokenizer st = new StringTokenizer(line, ":L", false);
				int id_major = Integer.parseInt(st.nextToken());
				int id_minor = Integer.parseInt(st.nextToken());
				int layer = 0;
				if (st.hasMoreTokens()) {
					layer = Integer.parseInt(st.nextToken());
				}
				String name  = r.nextLine().trim();
				
				ArrayList<BufferedImage> sprites = new ArrayList<>();
				
				while (!(line = r.nextLine().trim()).equals("")) {
					st = new StringTokenizer(line);
					String spritesheet = st.nextToken();
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					BufferedImage sprite = ResourceManager.getSSImg(spritesheet, x, y);
					if (st.hasMoreTokens()) {
						if (st.nextToken().equals("/")) {
							boolean overlay = false;
							spritesheet = st.nextToken();
							x = Integer.parseInt(st.nextToken());
							y = Integer.parseInt(st.nextToken());
							if (st.hasMoreTokens() && st.nextToken().equals("/")) {
								overlay = true;
							}
							sprite = overlay(sprite, ResourceManager.getSSImg(spritesheet, x, y), overlay);
						}
					}
					sprites.add(sprite);
				}
				
				while(sprites.size() < Game.ANIMATION_FRAMES) {
					sprites.add(sprites.get(0));
				}
				
				BufferedImage[] spritesarr = sprites.toArray(new BufferedImage[sprites.size()]);
				
				TileData td = new TileData(id_major, id_minor, name, spritesarr, layer);
				
				if (!tileData.containsKey(id_major)) {
					tileData.put(id_major, new HashMap<>());
				}
				tileData.get(id_major).put(id_minor, td);
				
//				for (Integer i : tileData.keySet()) {
//					for (Integer j : tileData.get(i).keySet()) {
//						System.out.println(tileData.get(i).get(j));
//					}
//				}
			}
			
			r.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static TileData getTileData(int id_major, int id_minor) {
		if (tileData.containsKey(id_major)) {
			if (tileData.get(id_major).containsKey(id_minor)) {
				return tileData.get(id_major).get(id_minor);
			}
		}
		return null;
	}
	
	public static BufferedImage overlay(BufferedImage back, BufferedImage front) {
		BufferedImage img = ResourceManager.deepCopy(back);
		Graphics2D g = img.createGraphics();
		g.drawImage(front, 0, 0, null);
		return img;
	}
	
	public static BufferedImage overlay(BufferedImage back, BufferedImage front, boolean offset) {
		if (!offset) {
			return overlay(back, front);
		}
		
		final int num_overlays = 1;
		
		BufferedImage img = new BufferedImage(back.getWidth(), (num_overlays + 2) * (int) (back.getHeight() * 0.5), back.getType());
		Graphics2D g = img.createGraphics();
		
		for (int i = 0; i < num_overlays; i ++) {
			g.drawImage(back, 0, (i + 1) * (int) (back.getHeight() * 0.5), null);
		}

		g.drawImage(front, 0, 0, null);
		return img;
	}

	public static boolean isOverlayable(int id_major) {
		switch (id_major) {
		case 0:
		case 2:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isLiquid(int id_major) {
		switch (id_major) {
		case 1:
		case 3:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isGround(int id_major) {
		switch (id_major) {
		case 0:
		case 2:
		case 5:
			return true;
		default:
			return false;
		}
	}
}
