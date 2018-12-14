package game.world.structure;

import game.world.World;
import game.world.tile.TileData;
import game.world.tile.TileManager;

public class StructureData {
	
	private final String name;
	
	private final TileData[][] blueprint;
	
	public StructureData(String name, TileData[][] blueprint) {
		this.name = name;
		this.blueprint = blueprint;
	}
	
	void generate(World world) {
		
		int x;
		int y;
		
		final int w = blueprint.length;
		final int h = blueprint[0].length;
		
		int count;
		
		do {
			count = 0;
			
			x = (int) (Math.random() * (world.getWidth() - w));
			y = (int) (Math.random() * (world.getHeight() - h));
			
			for (int i = 0; i < w; i ++) {
				for (int j = 0; j < h; j ++) {
					count += TileManager.isGround(world.getTile(x + i, y + j).getIDMajor()) ? 1 : 0;
				}
			}
			
		} while(count / (float) (w * h) < 0.8);
		
		for (int i = 0; i < w; i ++) {
			for (int j = 0; j < h; j ++) {
				if (blueprint[i][j] != TileData.SKIP) {
					world.setTile(x + i, y + j, blueprint[i][j]);
				}
			}
		}
	}
	
	public String getName() {
		return name;
	}
}
