package gregtechmod.api.world;

import java.util.Collection;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class GT_Worldgen_Stone_Ore_SingleBlock extends GT_Worldgen_Ore {
	
	private final int[] mOreIDs, mOreMetas, mOreChances;
	
	public GT_Worldgen_Stone_Ore_SingleBlock(String aName, boolean aDefault, int aBlockID, int aBlockMeta, int aDimensionType, int aAmount, int aSize, int aProbability, int aMinY, int aMaxY, Collection<String> aBiomeList, boolean aAllowToGenerateinVoid, int[] aOreIDs, int[] aOreMetas, int[] aOreChances) {
		super(aName, aDefault, aBlockID, aBlockMeta, aDimensionType, aAmount, aSize, aProbability, aMinY, aMaxY, aBiomeList, aAllowToGenerateinVoid);
		mOreIDs = aOreIDs;
		mOreMetas = aOreMetas;
		mOreChances = aOreChances;
	}
	
	@Override
	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		if (aDimensionType == mDimensionType && (mBiomeList.isEmpty() || mBiomeList.contains(aBiome)) && (mProbability <= 1 || aRandom.nextInt(mProbability) == 0)) {
			for (int i = 0; i < mAmount; i++) {
				int tX = aChunkX + aRandom.nextInt(16), tY = mMinY + aRandom.nextInt(mMaxY - mMinY), tZ = aChunkZ + aRandom.nextInt(16);
				if (mAllowToGenerateinVoid || aWorld.getBlockId(tX, tY, tZ) != 0) {
					float var6 = aRandom.nextFloat() * (float)Math.PI;
			        double var7 = ((tX + 8) + MathHelper.sin(var6) * mSize / 8.0F);
			        double var9 = ((tX + 8) - MathHelper.sin(var6) * mSize / 8.0F);
			        double var11 = ((tZ + 8) + MathHelper.cos(var6) * mSize / 8.0F);
			        double var13 = ((tZ + 8) - MathHelper.cos(var6) * mSize / 8.0F);
			        double var15 = (tY + aRandom.nextInt(3) - 2);
			        double var17 = (tY + aRandom.nextInt(3) - 2);
			        
			        for (int var19 = 0; var19 <= mSize; ++var19) {
			            double var20 = var7 + (var9 - var7) * var19 / mSize;
			            double var22 = var15 + (var17 - var15) * var19 / mSize;
			            double var24 = var11 + (var13 - var11) * var19 / mSize;
			            double var26 = aRandom.nextDouble() * mSize / 16.0D;
			            double var28 = (MathHelper.sin(var19 * (float)Math.PI / mSize) + 1.0F) * var26 + 1.0D;
			            double var30 = (MathHelper.sin(var19 * (float)Math.PI / mSize) + 1.0F) * var26 + 1.0D;
			            int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
			            int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
			            int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
			            int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
			            int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
			            int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);
			            
			            for (int var38 = var32; var38 <= var35; ++var38) {
			                double var39 = (var38 + 0.5D - var20) / (var28 / 2.0D);
			                if (var39 * var39 < 1.0D) {
			                    for (int var41 = var33; var41 <= var36; ++var41) {
			                        double var42 = (var41 + 0.5D - var22) / (var30 / 2.0D);
			                        if (var39 * var39 + var42 * var42 < 1.0D) {
			                            for (int var44 = var34; var44 <= var37; ++var44) {
			                                double var45 = (var44 + 0.5D - var24) / (var28 / 2.0D);
			                                Block block = Block.blocksList[aWorld.getBlockId(var38, var41, var44)];
			                                int tBlockID = mBlockID;
			                                int tBlockMeta = mBlockMeta;
			                                for (byte x = 0; x < mOreChances.length; x++) if (aRandom.nextInt(mOreChances[x]) == 0) {
			                                	tBlockID = mOreIDs[x];
			                                	tBlockMeta = mOreMetas[x];
			                                	break;
			                                }
			                                if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0D && ((mAllowToGenerateinVoid&&aWorld.getBlockId(var38, var41, var44)==0) || (block != null && (block.isGenMineableReplaceable(aWorld, var38, var41, var44, Block.stone.blockID) || block.isGenMineableReplaceable(aWorld, var38, var41, var44, Block.whiteStone.blockID) || block.isGenMineableReplaceable(aWorld, var38, var41, var44, Block.netherrack.blockID))))) {
			                                    aWorld.setBlock(var38, var41, var44, tBlockID, tBlockMeta, 0);
			                                }
			                            }
			                        }
			                    }
			                }
			            }
			        }
				}
	        }
			return true;
		}
        return false;
	}
}