package gregtechmod.api.world;

import gregtechmod.api.GregTech_API;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public abstract class GT_Worldgen {
	
	public final String mWorldGenName;
	
	public GT_Worldgen(String aName, boolean aDefault) {
		mWorldGenName = aName;
		if (GregTech_API.sWorldgenFile.add("worldgen", mWorldGenName, aDefault)) {
			GregTech_API.sWorldgenList.add(this);
		}
	}

	/**
	 * @param aWorld The World Object
	 * @param aRandom The Random Generator to use
	 * @param aBiome The Name of the Biome (always != null)
	 * @param aDimensionType The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
	 * @param aChunkX xCoord of the Chunk
	 * @param aChunkZ zCoord of the Chunk
	 * @return if the Worldgeneration has been successfully completed
	 */
	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		return false;
	}
	
	/**
	 * @param aWorld The World Object
	 * @param aRandom The Random Generator to use
	 * @param aBiome The Name of the Biome (always != null)
	 * @param aDimensionType The Type of Worldgeneration to add. -1 = Nether, 0 = Overworld, +1 = End
	 * @param aChunkX xCoord of the Chunk
	 * @param aChunkZ zCoord of the Chunk
	 * @return if the Worldgeneration has been successfully completed
	 */
	public boolean executeCavegen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		return false;
	}
}
