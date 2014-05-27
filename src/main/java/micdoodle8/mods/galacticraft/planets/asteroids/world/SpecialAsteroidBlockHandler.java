package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;

public class SpecialAsteroidBlockHandler {
	
	ArrayList<SpecialAsteroidBlock> asteroidBlocks;
	
	public SpecialAsteroidBlockHandler(SpecialAsteroidBlock...  asteroidBlocks) {
		this.asteroidBlocks = new ArrayList<SpecialAsteroidBlock>();
		for(SpecialAsteroidBlock asteroidBlock : this.asteroidBlocks) {
			for(int i = 0; i < asteroidBlock.probability; i++) {
				this.asteroidBlocks.add(asteroidBlock);
			}
		}
	}
	
	public SpecialAsteroidBlockHandler() {
		this.asteroidBlocks = new ArrayList<SpecialAsteroidBlock>();
	}
	
	public void addBlock(SpecialAsteroidBlock asteroidBlock) {
		for(int i = 0; i < asteroidBlock.probability; i++) {
			this.asteroidBlocks.add(asteroidBlock);
		}
	}
	
	public SpecialAsteroidBlock getBlock(Random rand) {
		return asteroidBlocks.get(rand.nextInt(asteroidBlocks.size()));
	}
	
}
