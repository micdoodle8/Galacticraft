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
import cpw.mods.fml.common.FMLLog;

public class GCCoreEntityParaChest extends Entity
{
	public ItemStack[] cargo;
	
	private boolean placedChest;
	
	public GCCoreEntityParaChest(World world, ItemStack[] cargo)
	{
		this(world);
		this.cargo = cargo;
		placedChest = false;
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
        NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.cargo = new ItemStack[27];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

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
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.cargo.length; ++var3)
        {
            if (this.cargo[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.cargo[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", var2);
        par1NBTTagCompound.setBoolean("placedChest", this.placedChest);
	}
	
	public void onUpdate()
	{
		if (!placedChest)
		{
			if (this.onGround && !this.worldObj.isRemote)
			{
				for (int i = 0; i < 100; i++)
				{
					int x = MathHelper.floor_double(this.posX);
					int y = MathHelper.floor_double(this.posY);
					int z = MathHelper.floor_double(this.posZ);
					
					int id = this.worldObj.getBlockId(x, y + i, z);
					
					if (id == 0)
					{
						if (this.placeChest(x, y + i, z))
						{
							this.setDead();
							return;
						}
						else if (cargo != null)
						{
							for (ItemStack stack : this.cargo)
							{
								EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
								this.worldObj.spawnEntityInWorld(e);
							}
							
							return;
						}
					}
				}

				if (cargo != null)
				{
					for (ItemStack stack : this.cargo)
					{
						EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
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
		TileEntity te = this.worldObj.getBlockTileEntity(x, y, z);
		
		if (te instanceof TileEntityChest && cargo != null)
		{
			TileEntityChest chest = (TileEntityChest) te;
			
			for (int i = 0; i < this.cargo.length; i++)
			{
				chest.setInventorySlotContents(i, cargo[i]);
			}
			
			return true;
		}
		
		this.placedChest = true;
		
		return true;
	}
}
