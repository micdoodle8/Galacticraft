package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySink;
import micdoodle8.mods.galacticraft.API.IDisableableMachine;
import micdoodle8.mods.galacticraft.API.IFuelTank;
import micdoodle8.mods.galacticraft.API.IFuelable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.multiblock.TileEntityMulti;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityFuelLoader extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IEnergySink, IDisableableMachine
{
	private int tankCapacity = 24000;
	public LiquidTank fuelTank = new LiquidTank(tankCapacity);
	
	private ItemStack[] containingItems = new ItemStack[2];
	public static final double WATTS_PER_TICK = 300;
	private final int playersUsing = 0;
	public IFuelable attachedFuelable;
	public double ic2WattsReceived = 0;
	private boolean initialized = false;
	private boolean disabled = true;
	
	public int getScaledFuelLevel(int i)
	{
		double fuelLevel = this.fuelTank.getLiquid() == null ? 0 : (this.fuelTank.getLiquid().amount);
		
		return (int) (fuelLevel * i / 2000);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (!this.initialized && this.worldObj != null)
		{
			if(GalacticraftCore.modIC2Loaded)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			}
			
			initialized = true;
		}
		
		if (this.fuelTank.getLiquid() != null && this.fuelTank.getLiquid().amount > 0)
		{
			this.wattsReceived += ElectricItemHelper.dechargeItem(this.getStackInSlot(0), GCCoreTileEntityFuelLoader.WATTS_PER_TICK, this.getVoltage());
		}
		
		if (!this.worldObj.isRemote)
		{
			this.wattsReceived = Math.max(this.wattsReceived - GCCoreTileEntityFuelLoader.WATTS_PER_TICK / 4, 0);

			if (this.containingItems[1] != null)
			{
				LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(this.containingItems[1]);

				if (liquid != null && LiquidDictionary.findLiquidName(liquid).equals("Fuel"))
				{
					if (this.fuelTank.getLiquid() == null || this.fuelTank.getLiquid().amount + liquid.amount <= this.fuelTank.getCapacity())
					{
						this.fuelTank.fill(liquid, true);
						
						if(liquid.itemID == GCCoreItems.fuel.itemID)
						{
							this.containingItems[1] = new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage());
						}
						else 
						{
							this.containingItems[1].stackSize--;

							if(this.containingItems[1].stackSize == 0)
							{
								this.containingItems[1] = null;
							}
						}
					}
				}
			}
			
			if (this.ticks % 100 == 0)
			{
				boolean foundFuelable = false;
				
				for (ForgeDirection dir : ForgeDirection.values())
				{
					if (dir != ForgeDirection.UNKNOWN)
					{
						Vector3 vecAt = new Vector3(this);
						vecAt = vecAt.modifyPositionFromSide(dir);
						
						TileEntity pad = vecAt.getTileEntity(this.worldObj);

						if (pad != null && pad instanceof TileEntityMulti)
						{
							TileEntity mainTile = ((TileEntityMulti) pad).mainBlockPosition.getTileEntity(this.worldObj);
							
							if (mainTile != null && mainTile instanceof IFuelable)
							{
								this.attachedFuelable = (IFuelable) mainTile;
								foundFuelable = true;
								break;
							}
						}
						else if (pad != null && pad instanceof IFuelable)
						{
							this.attachedFuelable = (IFuelable) pad;
							foundFuelable = true;
							break;
						}
					}
				}
				
				if (!foundFuelable)
				{
					this.attachedFuelable = null;
				}
			}
			
			if (this.attachedFuelable != null && (this.ic2WattsReceived > 0 || this.wattsReceived > 0) && !this.disabled)
			{
				LiquidStack liquid = LiquidDictionary.getLiquid("Fuel", 1);
				
				if (liquid != null)
				{
					this.fuelTank.drain(this.attachedFuelable.addFuel(liquid, 1), true);
				}
			}

			if (this.ticks % 3 == 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 6);
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(BasicComponents.CHANNEL, this, this.wattsReceived, this.disabledTicks, this.ic2WattsReceived, this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount, this.disabled);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.wattsReceived = dataStream.readDouble();
				this.disabledTicks = dataStream.readInt();
				this.ic2WattsReceived = dataStream.readDouble();
				int amount = dataStream.readInt();
				this.fuelTank.setLiquid(new LiquidStack(GCCoreItems.fuel.itemID, amount, 0));
				this.disabled = dataStream.readBoolean();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

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

	@Override
	public String getInvName()
	{
		return "Fuel Loader";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}
	
	private boolean validStackInSlot()
	{
		return this.getStackInSlot(1) != null && this.getStackInSlot(1).getItem() instanceof IFuelTank && this.getStackInSlot(1).getMaxDamage() - this.getStackInSlot(1).getItemDamage() != 0 && this.getStackInSlot(1).getItemDamage() < this.getStackInSlot(1).getMaxDamage();
	}
	
	// Universal Electricity Implementation:
	
	@Override
	public boolean canConnect(ForgeDirection direction) 
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.fuelTank.getLiquid() != null && this.fuelTank.getLiquid().amount > 0)
		{
			return new ElectricityPack(GCCoreTileEntityFuelLoader.WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}
	
	// Industrial Craft 2 Implementation:

	@Override
	public int demandsEnergy()
	{
		return validStackInSlot() ? (int) ((GCCoreTileEntityFuelLoader.WATTS_PER_TICK / this.getVoltage()) * GalacticraftCore.IC2EnergyScalar) : 0;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) 
	{
		double rejects = 0;
    	double neededEnergy = ((GCCoreTileEntityFuelLoader.WATTS_PER_TICK / this.getVoltage()) * GalacticraftCore.IC2EnergyScalar);
    	
    	if(amount <= neededEnergy)
    	{
    		ic2WattsReceived += amount;
    	}
    	else if(amount > neededEnergy)
    	{
    		ic2WattsReceived += neededEnergy;
    		rejects = amount - neededEnergy;
    	}
    	
    	return (int) (rejects * GalacticraftCore.IC2EnergyScalar);
	}

	@Override
	public int getMaxSafeInput() 
	{
		return 2048;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) 
	{
		return direction.toForgeDirection() == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public boolean isAddedToEnergyNet() 
	{
		return this.initialized;
	}
	
	// ISidedInventory Implementation:

	@Override
	public int[] getSizeInventorySide(int side) 
	{
		return side == 1 || side == 0 ? new int[] {0} : new int[] {1};
	}

	@Override
	public boolean func_102007_a(int slotID, ItemStack itemstack, int side) 
	{
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean func_102008_b(int slotID, ItemStack itemstack, int side) 
	{
		return slotID == 1;
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		return true;
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemstack) 
	{
		return slotID == 1 ? itemstack.getItem() instanceof IFuelTank && itemstack.getMaxDamage() - itemstack.getItemDamage() != 0 && itemstack.getItemDamage() < itemstack.getMaxDamage() 
				: (slotID == 0 ? itemstack.getItem() instanceof IItemElectric : false);
	}

	@Override
	public void setDisabled(boolean disabled) 
	{
		this.disabled = disabled;
	}

	@Override
	public boolean getDisabled() 
	{
		return this.disabled;
	}
}
