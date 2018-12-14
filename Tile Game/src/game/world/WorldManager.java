package game.world;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

import game.world.generate.SimplexNoise;
import game.world.tile.TileData;
import game.world.tile.TileManager;

public class WorldManager {

	
	public static World GenerateFromSimplex(int width, int height) {
		int id_major_grass = 0;
		int id_minor_grass = 0;
		
		int id_major_sand = 2;
		int id_minor_sand = 0;
		
		int id_major_water = 1;
		int id_minor_water = 0;
		
		double simplex_factor_1 = 0.03;
		double simplex_factor_2 = 0.01;
		
		double simplex_offset_x = Math.random() * 100;
		double simplex_offset_y = Math.random() * 100;
		
		
		World world = new World(width, height, id_major_water, id_minor_water);
		
		// Simplex noise land generation
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				double noise1 = SimplexNoise.noise(x * simplex_factor_1 + simplex_offset_x, y * simplex_factor_1 + simplex_offset_y);
				double noise2 = SimplexNoise.noise(x * simplex_factor_2, y * simplex_factor_2);
				if (noise1 > 0.0) {
					if (noise1 < 0.2 && noise2 > 0.3) {
						world.setTile(x, y, id_major_sand, id_minor_sand);
					} else {
						world.setTile(x, y, id_major_grass, id_minor_grass);
					}
				}
			}
		}
		
		double foliage_chance = 0.3;
		double[] foliage_weights = new double[4];
		foliage_weights[0] = 3;	// short grass
		foliage_weights[1] = 1;	// long grass
		foliage_weights[2] = 1;	// mushroom
		foliage_weights[3] = 2;	// flowers
		
		TileData[] tileData = new TileData[4];
		tileData[0] = TileManager.getTileData(0, 1);	// short grass
		tileData[1] = TileManager.getTileData(0, 2);	// long grass
		tileData[2] = TileManager.getTileData(0, 3);	// mushroom
		tileData[3] = TileManager.getTileData(0, 4);	// flowers
		
		double sum = 0;
		for (int i = 0; i < foliage_weights.length; i ++) {
			foliage_weights[0] = foliage_weights[0] + sum;
			sum += foliage_weights[0];
		}
		
		// Populate world with foliage
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (world.getTile(x, y).getIDMajor() == id_major_grass) {
					if (Math.random() < foliage_chance) {
						for (int i = 0; i < foliage_weights.length; i++) {
							if (Math.random() * sum < foliage_weights[i]) {
								world.setTile(x, y, tileData[i]);
							}
						}
					}
				}
			}
		}
		
		return world;
	}
	
	// Test generate a water world
	public static World GenerateWorld(int width, int height) {
		
		int id_major_land = 0;
		int id_minor_land = 0;
		int id_minor_land_2 = 1;
		int id_minor_land_3 = 2;
		
		int id_major_water = 1;
		int id_minor_water = 0;
		
		World world = new World(width, height, id_major_water, id_minor_water);
		
		final int area = width * height;
		
		final int num_points = 10;
		final int num_walks = (int) (area * 0.3);
		final double minor_chance = 0.05;
		
		// Place initial points
		for (int i = 0; i < num_points; i ++) {
			int x = (int) (width * Math.random());
			int y = (int) (height * Math.random());
			
			world.setTile(x, y, id_major_land, id_minor_land);
		}
		
		// Do random walks to generate islands
		for (int i = 0; i < num_walks; i ++) {
			int x;
			int y;
			
			boolean placed = false;
			do {
				x = (int) (width * Math.random());
				y = (int) (height * Math.random());
				
				// check adjacent tiles
				if ((x > 0 && world.getTile(x-1, y).getIDMajor() == id_major_land)
				  ||(y > 0 && world.getTile(x, y-1).getIDMajor() == id_major_land)
				  ||(x < world.getWidth()-1 && world.getTile(x+1, y).getIDMajor() == id_major_land)
				  ||(y < world.getHeight()-1 && world.getTile(x, y+1).getIDMajor() == id_major_land)) {
					
					
					// One of the adjacent tiles is land
					if (Math.random() < minor_chance) {
						if (Math.random() < minor_chance) {
							world.setTile(x, y, id_major_land, id_minor_land_3);
						} else {
							world.setTile(x, y, id_major_land, id_minor_land_2);
						}
					} else {
						world.setTile(x, y, id_major_land, id_minor_land);
					}
					
					placed = true;
				}
				
				
			} while (!placed);
		}
		
		// Smooth out land
		
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (world.getTile(x, y).getIDMajor() == id_major_water) {
					int count_adjacent = 0;
					if (x > 0 && world.getTile(x-1, y).getIDMajor() == id_major_land) {
						count_adjacent ++;
					}
					if (y > 0 && world.getTile(x, y-1).getIDMajor() == id_major_land) {
						count_adjacent ++;
					}
					if (x < world.getWidth()-1 && world.getTile(x+1, y).getIDMajor() == id_major_land) {
						count_adjacent ++;
					}
					if (y < world.getHeight()-1 && world.getTile(x, y+1).getIDMajor() == id_major_land) {
						count_adjacent ++;
					}
					
					if (count_adjacent >= 3) {
						world.setTile(x, y, id_major_land, id_minor_land);
					}
				}
			}
		}
		
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				if (world.getTile(x, y).getIDMajor() == id_major_land) {
					int count_adjacent = 0;
					if (x > 0 && world.getTile(x-1, y).getIDMajor() == id_major_water) {
						count_adjacent ++;
					}
					if (y > 0 && world.getTile(x, y-1).getIDMajor() == id_major_water) {
						count_adjacent ++;
					}
					if (x < world.getWidth()-1 && world.getTile(x+1, y).getIDMajor() == id_major_water) {
						count_adjacent ++;
					}
					if (y < world.getHeight()-1 && world.getTile(x, y+1).getIDMajor() == id_major_water) {
						count_adjacent ++;
					}
					
					if (count_adjacent >= 3) {
						world.setTile(x, y, id_major_land, id_minor_land);
					}
				}
			}
		}
		
		// Add some docks
		for (int i = 0; i < num_points; i ++) {

			boolean placed = false;
			
			int x;
			int y;
			
			do {
				x = (int) (width * Math.random());
				y = (int) (height * Math.random());
				
				if (world.getTile(x, y).getIDMajor() == id_major_water) {
					// check adjacent tiles
					
					boolean hor = false;
					boolean vert = false;
					
					if (x > 0 && world.getTile(x-1, y).getIDMajor() == id_major_land) {
						hor = !hor;
					}
					if (y > 0 && world.getTile(x, y-1).getIDMajor() == id_major_land) {
						vert = !vert;
					}
					if (x < world.getWidth()-1 && world.getTile(x+1, y).getIDMajor() == id_major_land) {
						hor = !hor;
					}
					if (y < world.getHeight()-1 && world.getTile(x, y+1).getIDMajor() == id_major_land) {
						vert = !vert;
					}
					
					if (hor && !vert) {
						world.setTile(x, y, 5, 1);
						placed = true;
					} else if (vert && !hor) {
						world.setTile(x, y, 5, 0);
						placed = true;
					}
				}
				
				
			} while (!placed);
		}
		
		
		return world;
	}
	
	
	public static World fromFile(String fileName) {
		File f = new File("assets/worlds/" + fileName);
		Scanner r;
		try {
			r = new Scanner(f);
			
			String line = r.nextLine();
			StringTokenizer st = new StringTokenizer(line);
			int width = Integer.parseInt(st.nextToken());
			int height = Integer.parseInt(st.nextToken());
			int d_id_major = 0;
			int d_id_minor = 0;
			if (st.hasMoreTokens()) {
				d_id_major = Integer.parseInt(st.nextToken());
				if (st.hasMoreTokens()) {
					d_id_minor = Integer.parseInt(st.nextToken());
				}
			}
			
			World world = new World(width, height, d_id_major, d_id_minor);
			
			while(r.hasNextLine()) {
				line = r.nextLine();
				st = new StringTokenizer(line);
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				int id_major = Integer.parseInt(st.nextToken());
				int id_minor = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : 0;
				
				world.setTile(x, y, id_major, id_minor);
			}
			
			r.close();
			
			return world;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void saveToFile(World world, String fileName) {
		File f = new File("assets/worlds/" + fileName);
		
		try {
			FileWriter fr = new FileWriter(f);
			
			fr.write(String.format("%d %d\n", world.getWidth(), world.getHeight()));
			
			for (int i = 0; i < world.getWidth(); i ++) {
				for (int j = 0; j < world.getHeight(); j ++) {
					fr.write(String.format("%d %d %d %d\n", i, j, world.getTile(i, j).getIDMajor(), world.getTile(i, j).getIDMinor()));
				}
			}
			
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
