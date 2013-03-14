package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSpaceship;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;

public class GCCoreTileEntityLandingPad extends TileEntityMulti implements IPacketReceiver
{
	protected long ticks = 0;
	private EntitySpaceshipBase spaceshipOnPad;
	
	// I'll be back in a sec :D
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 6);
			}
		}

		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;
	}

	@Override
	public int getBlockMetadata()
	{
		if (this.blockMetadata == -1)
		{
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		}

		return this.blockMetadata;
	}

	@Override
	public Block getBlockType()
	{
		if (this.blockType == null)
		{
			this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
		}

		return this.blockType;
	}

	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		super.onBlockActivated(par1World, x, y, z, par5EntityPlayer);
		
		if (this.mainBlockPosition != null && par5EntityPlayer.inventory.getCurrentItem() != null && par5EntityPlayer.inventory.getCurrentItem().getItem() instanceof GCCoreItemSpaceship)
		{
	    	int amountOfCorrectBlocks = 0;

	    	final GCCoreEntitySpaceship spaceship = new GCCoreEntitySpaceship(par1World, this.mainBlockPosition.x + 0.5F, this.mainBlockPosition.y - 1.5F, this.mainBlockPosition.z + 0.5F, par5EntityPlayer.inventory.getCurrentItem().getItemDamage());

	    	if (this.worldObj.isRemote)
	    	{
	    		return false;
	    	}
	    	else
	    	{
	    		par1World.spawnEntityInWorld(spaceship);
	    		
	    		if (!par5EntityPlayer.capabilities.isCreativeMode)
	    		{
	    			par5EntityPlayer.inventory.consumeInventoryItem(par5EntityPlayer.inventory.getCurrentItem().getItem().itemID);
	    		}
	    		
	    		spaceship.setSpaceshipType(par5EntityPlayer.inventory.getCurrentItem().getItemDamage());
	    		spaceship.setLandingPad(this);
	    		this.spaceshipOnPad = spaceship;
	    	}
	        return true;
		}
		
		return false;
	}
}
