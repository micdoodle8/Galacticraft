package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class EntityParachest extends Entity
{
	public ItemStack[] cargo;

	public int fuelLevel;

	private boolean placedChest;

	public EntityParachest(World world, ItemStack[] cargo, int fuelLevel)
	{
		this(world);
		this.cargo = cargo.clone();
		this.placedChest = false;
		this.fuelLevel = fuelLevel;
		if ((cargo.length - 2) % 18 != 0)
		{
			System.out.println("Strange EntityParachest inventory size " + cargo.length);
			this.fuelLevel = 1 / 0;
		}
	}

	public EntityParachest(World world)
	{
		super(world);
	}

	@Override
	protected void entityInit()
	{
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		final NBTTagList var2 = nbt.getTagList("Items", 10);
		this.cargo = new ItemStack[27];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.cargo.length)
			{
				this.cargo[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		this.placedChest = nbt.getBoolean("placedChest");
		this.fuelLevel = nbt.getInteger("FuelLevel");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.cargo.length; ++var3)
		{
			if (this.cargo[var3] != null)
			{
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.cargo[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		nbt.setTag("Items", var2);
		nbt.setBoolean("placedChest", this.placedChest);
		nbt.setInteger("FuelLevel", this.fuelLevel);
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

					Block block = this.worldObj.getBlock(x, y + i, z);

					if (block.getMaterial().isReplaceable())
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

			this.moveEntity(0, this.motionY, 0);
		}
	}

	private boolean placeChest(int x, int y, int z)
	{
		this.worldObj.setBlock(x, y, z, GCBlocks.parachest, 0, 3);
		final TileEntity te = this.worldObj.getTileEntity(x, y, z);

		if (te instanceof TileEntityParaChest && this.cargo != null)
		{
			final TileEntityParaChest chest = (TileEntityParaChest) te;

			chest.chestContents = new ItemStack[this.cargo.length + 1];

			for (int i = 0; i < this.cargo.length; i++)
			{
				chest.chestContents[i] = this.cargo[i];
			}

			chest.fuelTank.fill(FluidRegistry.getFluidStack(GalacticraftCore.fluidFuel.getName().toLowerCase(), this.fuelLevel), true);

			return true;
		}

		this.placedChest = true;

		return true;
	}
}
