package gregtechmod.api.world;

import java.util.Collection;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class GT_Worldgen_Ore_SingleBlock_UnderLava extends GT_Worldgen_Ore {
	public GT_Worldgen_Ore_SingleBlock_UnderLava(String aName, boolean aDefault, int aBlockID, int aBlockMeta, int aDimensionType, int aAmount, int aSize, int aProbability, int aMinY, int aMaxY, Collection<String> aBiomeList, boolean aAllowToGenerateinVoid) {
		super(aName, aDefault, aBlockID, aBlockMeta, aDimensionType, aAmount, aSize, aProbability, aMinY, aMaxY, aBiomeList, aAllowToGenerateinVoid);
	}
	
	@Override
	public boolean executeCavegen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		if (aDimensionType == mDimensionType && (mBiomeList.isEmpty() || mBiomeList.contains(aBiome)) && (mProbability <= 1 || aRandom.nextInt(mProbability) == 0)) {
			for (int i = 0; i < mAmount; i++) {
				int tX = aChunkX + aRandom.nextInt(16), tY = mMinY + aRandom.nextInt(mMaxY - mMinY), tZ = aChunkZ + aRandom.nextInt(16);
				Block tBlock = Block.blocksList[aWorld.getBlockId(tX, tY, tZ)];
		        if (((mAllowToGenerateinVoid&&aWorld.getBlockId(tX, tY, tZ)==0) || (tBlock != null && (tBlock.isGenMineableReplaceable(aWorld, tX, tY, tZ, Block.stone.blockID) || tBlock.isGenMineableReplaceable(aWorld, tX, tY, tZ, Block.whiteStone.blockID) || tBlock.isGenMineableReplaceable(aWorld, tX, tY, tZ, Block.netherrack.blockID))))) {
		        	if (aWorld.getBlockId(tX, tY+1, tZ) == Block.lavaStill.blockID || aWorld.getBlockId(tX, tY, tZ) == Block.lavaMoving.blockID) aWorld.setBlock(tX, tY, tZ, mBlockID, mBlockMeta, 0);
				}
	        }
			return true;
		}
        return false;
	}
}