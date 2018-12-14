package game.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import main.ResourceManager;

public class EntityManager {

	private static HashMap<Integer, HashMap<Integer, EntityData>> entityData;
	
	public static void init() {
		entityData = new HashMap<>();
		loadEntitiesFromFile("character.entity");
	}
	
	private static void loadEntitiesFromFile(String fileName) {
		String path = "assets/entities/";
		File f = new File(path + fileName);
		
		try {
			Scanner r = new Scanner(f);
			
			while (r.hasNextLine()) {
				String line;
				do {
					line = r.nextLine().trim();
				} while (line.equals("\n"));
				
				StringTokenizer st = new StringTokenizer(line, ":", false);
				int id_major = Integer.parseInt(st.nextToken());
				int id_minor = Integer.parseInt(st.nextToken());
				String name  = r.nextLine().trim();
				
				ArrayList<BufferedImage> sprites = new ArrayList<>();
				
				while (!(line = r.nextLine().trim()).equals("")) {
					st = new StringTokenizer(line);
					String spritesheet = st.nextToken();
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					BufferedImage sprite = ResourceManager.getSSImg(spritesheet, x, y);
					sprites.add(sprite);
				}
				
				BufferedImage[] spritesarr = sprites.toArray(new BufferedImage[sprites.size()]);
				
				EntityData ed = new EntityData(id_major, id_minor, name, spritesarr);
				
				if (!entityData.containsKey(id_major)) {
					entityData.put(id_major, new HashMap<>());
				}
				entityData.get(id_major).put(id_minor, ed);
				
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

	public static EntityData getEntityData(int id_major, int id_minor) {
		if (entityData.containsKey(id_major)) {
			if (entityData.get(id_major).containsKey(id_minor)) {
				return entityData.get(id_major).get(id_minor);
			}
		}
		return null;
	}
}
