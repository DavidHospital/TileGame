package game.world.structure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import game.world.World;
import game.world.tile.TileData;
import game.world.tile.TileManager;

public class StructureManager {

	private static HashMap<String, StructureData> structureData;
	
	public static void init() {
		loadStructures();
	}
	
	private static void loadStructures() {
		structureData = new HashMap<>();
		
		File f = new File ("assets/structures");	
		//for all files within f
		for (File struct : f.listFiles()) {											
			try {								
				Scanner r = new Scanner(struct);
				
				ArrayList<TileData> tileData = new ArrayList<>();
				
				String line;
				StringTokenizer st = new StringTokenizer(struct.getName(), ".");
				String name = st.nextToken();
				
				// Get the tile types to use in this structure
				while (r.hasNextLine()) {
					line = r.nextLine();
					if (line.isEmpty()) {
						break;
					}
					
					st = new StringTokenizer(line, ":", false);
					
					int id_major = Integer.parseInt(st.nextToken());
					int id_minor = Integer.parseInt(st.nextToken());
					
					tileData.add(TileManager.getTileData(id_major, id_minor));
				}
				
				st = new StringTokenizer(line = r.nextLine(), "x", false);
				
				int w = Integer.parseInt(st.nextToken());
				int h = Integer.parseInt(st.nextToken());
				
				TileData[][] blueprint = new TileData[w][h];
				
				
				for (int j = 0; r.hasNextLine(); j++) {
					line = r.nextLine();
					st = new StringTokenizer(line);
					
					for (int i = 0; st.hasMoreTokens(); i ++) {
						String token = st.nextToken();
						if (token.equals("-")) {
							blueprint[i][j] = TileData.SKIP;
						} else {
							blueprint[i][j] = tileData.get(Integer.parseInt(token));
						}
					}
				}
				
				structureData.put(name, new StructureData(name, blueprint));
				
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void Generate(World world, String name) {
		if (!structureData.containsKey(name)) {
			return;
		}
		
		structureData.get(name).generate(world);
	}
}
