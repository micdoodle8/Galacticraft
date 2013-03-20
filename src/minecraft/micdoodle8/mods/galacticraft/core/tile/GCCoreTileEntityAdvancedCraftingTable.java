package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelAssemblyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.IMultiBlock;
import universalelectricity.prefab.multiblock.TileEntityMulti;

public class GCCoreTileEntityAdvancedCraftingTable extends TileEntityMulti implements IMultiBlock
{
    public GCCoreModelAssemblyTable model = new GCCoreModelAssemblyTable();

    @Override
  	public void validate()
  	{
   		super.validate();

   		if (!this.isInvalid() && this.worldObj != null)
      	{
   		   	this.model = new GCCoreModelAssemblyTable();
      	}
  	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer) 
	{
		entityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		return true;
	}

	@Override
	public void onCreate(Vector3 placedPosition) 
	{
		this.mainBlockPosition = placedPosition;
		
		for (int x = -1; x < 2; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					Vector3 vecToAdd = Vector3.add(placedPosition, new Vector3(x, y, z));
					
					if (!vecToAdd.isEqual(placedPosition))
					{
						if ((Math.abs(x) != 1 || Math.abs(z) != 1))
						{
							if ((y == 0 || y == 3) && x == 0 && z == 0)
							{
								GCCoreBlocks.dummyBlock.makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
							}
							else if (y != 0 && y != 3)
							{
								GCCoreBlocks.dummyBlock.makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock) 
	{
		Vector3 thisBlock = new Vector3(this);
		
		for (int x = -1; x < 2; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					if ((Math.abs(x) != 1 || Math.abs(z) != 1))
					{
						if ((y == 0 || y == 3) && x == 0 && z == 0)
						{
							if (this.worldObj.rand.nextDouble() < 0.05D)
								FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCCoreBlocks.rocketBench.blockID & 4095, GCCoreBlocks.rocketBench.blockID >> 12 & 255);
							this.worldObj.setBlockAndMetadataWithNotify(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, 0, 0, 3);
						}
						else if (y != 0 && y != 3)
						{
							if (this.worldObj.rand.nextDouble() < 0.05D)
								FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCCoreBlocks.rocketBench.blockID & 4095, GCCoreBlocks.rocketBench.blockID >> 12 & 255);
							this.worldObj.setBlockAndMetadataWithNotify(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, 0, 0, 3);
						}
					}
				}
			}
		}
	}
}
