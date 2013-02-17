package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class GCCoreEntityParaChest extends Entity
{
	public ItemStack[] cargo;
	
	private boolean placedChest;
	
	public GCCoreEntityParaChest(World world, ItemStack[] cargo)
	{
		this(world);
		this.cargo = cargo;
		this.placedChest = false;
	}
	
	public GCCoreEntityParaChest(World world)
	{
		super(world);
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.cargo = new ItemStack[27];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.cargo.length)
            {
                this.cargo[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        
        this.placedChest = par1NBTTagCompound.getBoolean("placedChest");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
        final NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.cargo.length; ++var3)
        {
            if (this.cargo[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.cargo[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
        par1NBTTagCompound.setBoolean("placedChest", this.placedChest);
	}
	
	@Override
	public void onUpdate()
	{
		if (!this.placedChest)
		{
			if (this.onGround && !this.worldObj.isRemote)
			{
				for (int i = 0; i < 100; i++)
				{
					final int x = MathHelper.floor_double(this.posX);
					final int y = MathHelper.floor_double(this.posY);
					final int z = MathHelper.floor_double(this.posZ);
					
					final int id = this.worldObj.getBlockId(x, y + i, z);
					
					if (id == 0)
					{
						if (this.placeChest(x, y + i, z))
						{
							this.setDead();
							return;
						}
						else if (this.cargo != null)
						{
							for (final ItemStack stack : this.cargo)
							{
								final EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
								this.worldObj.spawnEntityInWorld(e);
							}
							
							return;
						}
					}
				}

				if (this.cargo != null)
				{
					for (final ItemStack stack : this.cargo)
					{
						final EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
						this.worldObj.spawnEntityInWorld(e);
					}
				}
			}
			else
			{
				this.motionY = -0.25;
			}
			
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}
	}
	
	private boolean placeChest(int x, int y, int z)
	{
		this.worldObj.setBlock(x, y, z, Block.chest.blockID);
		final TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof TileEntityChest && this.cargo != null)
		{
			final TileEntityChest chest = (TileEntityChest) te;
			
			for (int i = 0; i < this.cargo.length; i++)
			{
				chest.setInventorySlotContents(i, this.cargo[i]);
			}
			
			return true;
		}
		
		this.placedChest = true;
		
		return true;
	}
}
