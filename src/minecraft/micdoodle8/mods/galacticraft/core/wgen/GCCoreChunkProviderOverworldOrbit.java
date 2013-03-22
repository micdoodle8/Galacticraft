package micdoodle8.mods.galacticraft.core.wgen;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IMultiBlock;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
public class GCCoreChunkProviderOverworldOrbit extends ChunkProviderGenerate
{
	private final Random rand;
	
	private final World worldObj;

	public GCCoreChunkProviderOverworldOrbit(World par1World, long par2, boolean par4)
	{
		super(par1World, par2, par4);
		this.rand = new Random(par2);
		this.worldObj = par1World;
	}

	@Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

	@Override
    public int getLoadedChunkCount()
    {
        return 0;
    }
	
	@Override
    public boolean saveChunks(boolean var1, IProgressUpdate var2)
    {
        return true;
    }

	@Override
    public boolean canSave()
    {
        return true;
    }

	@Override
	public Chunk provideChunk(int par1, int par2)
	{
		this.rand.setSeed(par1 * 341873128712L + par2 * 132897987541L);
		final int[] ids = new int[32768];
		final int[] meta = new int[32768];

		final Chunk var4 = new GCCoreChunk(this.worldObj, ids, meta, par1, par2);

		var4.generateSkylightMap();
		return var4;
	}
	
	@Override
	public boolean chunkExists(int par1, int par2)
	{
		return true;
	}

	@Override
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
	{
		BlockSand.fallInstantly = true;
        int k = par2 * 16;
        int l = par3 * 16;
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(par2 * i1 + par3 * j1 ^ this.worldObj.getSeed());
        if (k == 0 && l == 0)
        {
            this.worldObj.setBlockAndMetadataWithNotify(k, 64, l, GCCoreBlocks.spaceStationBase.blockID, 0, 3);
//            this.worldObj.setBlockTileEntity(k, 64, l, new GCCoreTileEntitySpaceStationBase());

            TileEntity var8 = this.worldObj.getBlockTileEntity(k, 64, l);
            
            if (var8 instanceof IMultiBlock)
            {
                ((IMultiBlock)var8).onCreate(new Vector3(k, 64, l));
            }
            
            new GCCoreWorldGenSpaceStation().generate(this.worldObj, this.rand, k - 10, 62, l - 3);
//            for (int x = -3; x < 4; x++)
//            {
//            	for (int y = 0; y < 6; y++)
//            	{
//                    for (int z = -3; z < 4; z++)
//                    {
//                    	if (Math.abs(x) == 3 || Math.abs(y) == 0 || Math.abs(y) == 5 || Math.abs(z) == 3)
//                    	{
//                    		if (y == 2 && !(Math.abs(x) == 3 && Math.abs(z) == 3))
//                    		{
//                    			this.worldObj.setBlockAndMetadataWithNotify(k + x, 63 + y, l + z, Block.thinGlass.blockID, 0, 3);
//                    		}
//                    		else
//                    		{
//                    			this.worldObj.setBlockAndMetadataWithNotify(k + x, 63 + y, l + z, GCCoreBlocks.decorationBlocks.blockID, 4, 3);
//                    		}
//                    	}
//                    }
//            	}
//            }

//            for (int x = -2; x < 3; x++)
//            {
//            	for (int y = 0; y < 6; y++)
//            	{
//                    for (int z = -2; z < 3; z++)
//                    {
//                    	this.worldObj.setBlockAndMetadataWithNotify(k + x, 63, l + z, GCCoreBlocks.decorationBlocks.blockID, 4, 3);
//                    }
//            	}
//            }
        }
		BlockSand.fallInstantly = false;
	}

	@Override
	public String makeString()
	{
		return "OrbitLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType,	int i, int j, int k)
	{
		return null;
	}
}
