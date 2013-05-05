package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.GasTransmission;
import mekanism.api.IGasAcceptor;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityOxygenCollector extends GCCoreTileEntityElectric implements ITubeConnection, IInventory, ISidedInventory
{
	public boolean active;

	public static final double WATTS_PER_TICK = 200;

	private final int playersUsing = 0;

	public double power;

	public int MAX_POWER = 180;

	public int outputSpeed = 16;

	private ItemStack[] containingItems = new ItemStack[1];

    public GCCoreTileEntityOxygenCollector() 
    {
		super(200, 130, 1, 0.75D);
	}
    
	public int getCappedScaledOxygenLevel(int scale)
	{
		return (int) Math.max(Math.min((Math.floor((double)this.power / (double)this.MAX_POWER * scale)), scale), 0);
	}
    
	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if(this.getPower() > 0 && !this.worldObj.isRemote && (this.ic2Energy > 0 || this.wattsReceived > 0 || this.bcEnergy > 0))
			{
		    	for(final ForgeDirection orientation : ForgeDirection.values())
		    	{
		    		if(orientation != ForgeDirection.UNKNOWN)
		    		{
		    			if (orientation == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
		    			{
			    			this.setPower(this.getPower() - (Math.min(this.getPower(), this.outputSpeed) - GasTransmission.emitGasToNetwork(EnumGas.OXYGEN, (int)Math.min(this.getPower(), this.outputSpeed), this, orientation)));

			    			final TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), orientation);

			    			if(tileEntity instanceof IGasAcceptor)
			    			{
			    				if(((IGasAcceptor)tileEntity).canReceiveGas(orientation.getOpposite(), EnumGas.OXYGEN))
			    				{
			    					double sendingGas = 0;

			    					if(this.getPower() >= this.outputSpeed)
			    					{
			    						sendingGas = this.outputSpeed;
			    					}
			    					else if(this.getPower() < this.outputSpeed)
			    					{
			    						sendingGas = this.getPower();
			    					}

			    					final int rejects = ((IGasAcceptor)tileEntity).transferGasToAcceptor(MathHelper.floor_double(sendingGas), EnumGas.OXYGEN);

			    					this.setPower(this.getPower() - (sendingGas - rejects));
			    				}
			    			}
		    			}
		    		}
		    	}
			}
			
			double power = 0;

			if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
			{
				for (int y = this.yCoord - 5; y <= this.yCoord + 5; y++)
				{
					for (int x = this.xCoord - 5; x <= this.xCoord + 5; x++)
					{
						for (int z = this.zCoord - 5; z <= this.zCoord + 5; z++)
						{
							final Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];

							if (block != null && block instanceof BlockLeaves)
							{
								if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(100000) == 0 && !GCCoreConfigManager.disableLeafDecay)
								{
									this.worldObj.setBlockToAir(x, y, z);
								}

								power++;
							}
						}
					}
				}
				
				this.setPower(power / 1.2D);
			}
			else
			{
				this.setPower(this.MAX_POWER);
			}

			if(this.getPower() > this.MAX_POWER)
			{
				this.setPower(this.MAX_POWER);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.setPower(par1NBTTagCompound.getDouble("storedOxygenD"));

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            final byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setDouble("storedOxygenD", this.getPower());

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        par1NBTTagCompound.setTag("Items", list);
	}

	public double getPower()
	{
		return this.power;
	}

	public void setPower(double power)
	{
		this.power = power;
	}

	@Override
	public boolean canTubeConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	@Override
	public int getSizeInventory()
	{
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.containingItems[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.containingItems[par1] != null)
		{
			ItemStack var3;

			if (this.containingItems[par1].stackSize <= par2)
			{
				var3 = this.containingItems[par1];
				this.containingItems[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.containingItems[par1].splitStack(par2);

				if (this.containingItems[par1].stackSize == 0)
				{
					this.containingItems[par1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.containingItems[par1] != null)
		{
			final ItemStack var2 = this.containingItems[par1];
			this.containingItems[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.containingItems[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return LanguageRegistry.instance().getStringLocalization("tile.bcMachine.2.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openChest()
	{

	}

	@Override
	public void closeChest()
	{

	}

	// ISidedInventory Implementation:

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return new int[] {0};
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
	{
		return this.isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
	{
		return slotID == 0;
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemstack)
	{
		return slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false;
	}

	@Override
	public boolean shouldPullEnergy() 
	{
		return this.getPower() > 0;
	}

	@Override
	public void readPacket(ByteArrayDataInput data)
	{
		if (this.worldObj.isRemote)
		{
			this.setPower(data.readDouble());
			this.wattsReceived = data.readDouble();
			this.ic2Energy = data.readDouble();
			this.disabled = data.readBoolean();
			this.bcEnergy = data.readDouble();
		}
	}

	@Override
	public Packet getPacket() 
	{
		return PacketManager.getPacket(GalacticraftCore.CHANNEL, this, this.power, this.wattsReceived, this.ic2Energy, this.disabled, this.bcEnergy);
	}

	@Override
	public ForgeDirection getElectricInputDirection() 
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ItemStack getBatteryInSlot() 
	{
		return this.getStackInSlot(0);
	}
}
