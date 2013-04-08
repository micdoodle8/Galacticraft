package micdoodle8.mods.galacticraft.core.tile;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergySink;
import micdoodle8.mods.galacticraft.API.IDisableableMachine;
import micdoodle8.mods.galacticraft.API.IRefinableItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
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
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityRefinery extends TileEntityElectricityRunnable implements IInventory, ISidedInventory, IPacketReceiver, IEnergySink, IDisableableMachine
{
	private int tankCapacity = 24000;
	
	public LiquidTank oilTank = new LiquidTank(tankCapacity);
	public LiquidTank fuelTank = new LiquidTank(tankCapacity);
	
	public boolean disabled = true;
	public boolean lastDisabled = true;
	
	public static final double WATTS_PER_TICK = 600;
	public static final int PROCESS_TIME_REQUIRED = 1000;
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[3];
	private int playersUsing = 0;
	
	public double ic2WattsReceived = 0;
	private boolean initialized = false;
	
	private int canisterToTankRatio = tankCapacity / GCCoreItems.fuelCanister.getMaxDamage();
	private int canisterToLiquidStackRatio = GalacticraftCore.fuelStack.amount / GCCoreItems.fuelCanister.getMaxDamage();

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

		this.wattsReceived += ElectricItemHelper.dechargeItem(this.containingItems[0], GCCoreTileEntityRefinery.WATTS_PER_TICK, this.getVoltage());

		if (!this.worldObj.isRemote)
		{
			if (this.containingItems[1] != null)
			{
				LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(this.containingItems[1]);

				if (liquid != null && LiquidDictionary.findLiquidName(liquid).equals("Oil"))
				{
					if (this.oilTank.getLiquid() == null || this.oilTank.getLiquid().amount + liquid.amount <= this.oilTank.getCapacity())
					{
						this.oilTank.fill(liquid, true);
						
						if(liquid.itemID == GCCoreBlocks.crudeOilStill.blockID)
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

			if (this.containingItems[2] != null && LiquidContainerRegistry.isContainer(this.containingItems[2]))
			{
				LiquidStack liquid = this.fuelTank.getLiquid();

				if (liquid != null && this.fuelTank.getLiquidName().equals("Fuel"))
				{
					if (this.containingItems[2].isItemEqual(new ItemStack(GCCoreItems.oilCanister, 1, GCCoreItems.oilCanister.getMaxDamage())))
					{
						int amountToFill = Math.min(GCCoreItems.fuelCanister.getMaxDamage() - 1, MathHelper.floor_double(liquid.amount / this.canisterToLiquidStackRatio));
						
						this.containingItems[2] = new ItemStack(GCCoreItems.fuelCanister, 1, GCCoreItems.fuelCanister.getMaxDamage() - amountToFill);
						
						this.fuelTank.drain(amountToFill * canisterToLiquidStackRatio, true);
					}
				}
			}
			
			if (this.canProcess())
			{
				if (this.wattsReceived >= this.WATTS_PER_TICK || this.ic2WattsReceived >= this.WATTS_PER_TICK)
				{
					if (this.processTicks == 0)
					{
						this.processTicks = this.PROCESS_TIME_REQUIRED;
					}
					else if (this.processTicks > 0)
					{
						this.processTicks--;
						
						if (this.processTicks < 1)
						{
							this.smeltItem();
							this.processTicks = 0;
						}
					}
					else
					{
						this.processTicks = 0;
					}
				}
				else
				{
					this.processTicks = 0;
				}
			}
			else
			{
				this.processTicks = 0;
			}
			
			this.wattsReceived = Math.max(this.wattsReceived - GCCoreTileEntityRefinery.WATTS_PER_TICK / 4, 0);
			this.ic2WattsReceived = Math.max(this.ic2WattsReceived - GCCoreTileEntityRefinery.WATTS_PER_TICK / 4, 0);

			if (this.ticks % 3 == 0 && this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	public int getScaledOilLevel(int i)
	{
		return this.oilTank.getLiquid() != null ? this.oilTank.getLiquid().amount * i / this.oilTank.getCapacity() : 0;
	}

	public int getScaledFuelLevel(int i)
	{
		return this.fuelTank.getLiquid() != null ? this.fuelTank.getLiquid().amount * i / this.fuelTank.getCapacity() : 0;
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.canProcess() || this.wattsReceived <= this.WATTS_PER_TICK)
		{
			return new ElectricityPack(GCCoreTileEntityRefinery.WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(BasicComponents.CHANNEL, this, this.wattsReceived, this.processTicks, this.disabledTicks, this.ic2WattsReceived, this.oilTank.getLiquid() == null ? 0 : this.oilTank.getLiquid().amount, this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount, this.disabled);
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.wattsReceived = dataStream.readDouble();
			this.processTicks = dataStream.readInt();
			this.disabledTicks = dataStream.readInt();
			this.ic2WattsReceived = dataStream.readDouble();
			
			int amount = dataStream.readInt();
			this.oilTank.setLiquid(new LiquidStack(GCCoreBlocks.crudeOilStill.blockID, amount, 0));
			
			int amount2 = dataStream.readInt();
			this.fuelTank.setLiquid(new LiquidStack(GCCoreItems.fuel.itemID, amount2, 0));
			
			this.disabled = dataStream.readBoolean();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void openChest()
	{
		if (!this.worldObj.isRemote)
		{
			PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 15);
		}
		this.playersUsing++;
	}

	@Override
	public void closeChest()
	{
		this.playersUsing--;
	}

	public boolean canProcess()
	{
		if (this.oilTank.getLiquid() == null || this.oilTank.getLiquid().amount <= 0)
		{
			return false;
		}
		
		if (this.disabled)
		{
			return false;
		}

		return true;
	}

	public void smeltItem()
	{
		if (this.canProcess())
		{
			int oilAmount = this.oilTank.getLiquid().amount;
			int fuelSpace = this.fuelTank.getCapacity() - (this.fuelTank.getLiquid() == null ? 0 : this.fuelTank.getLiquid().amount);
			
			int amountToDrain = Math.min(oilAmount, fuelSpace);
			
			this.oilTank.drain(amountToDrain, true);
			this.fuelTank.fill(new LiquidStack(GCCoreItems.fuel, amountToDrain), true);
			
			if (!this.disabled)
			{
				this.disabled = true;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.processTicks = par1NBTTagCompound.getInteger("smeltingTicks");
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			byte var5 = var4.getByte("Slot");

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
		par1NBTTagCompound.setInteger("smeltingTicks", this.processTicks);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3)
		{
			if (this.containingItems[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

//	@Override
//	public int getStartInventorySide(ForgeDirection side)
//	{
//		if (side == side.DOWN || side == side.UP)
//		{
//			return side.ordinal();
//		}
//
//		return 2;
//	}
//
//	@Override
//	public int getSizeInventorySide(ForgeDirection side)
//	{
//		return 1;
//	}

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
			ItemStack var2 = this.containingItems[par1];
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
		return "Refinery";
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
	public boolean isInvNameLocalized() 
	{
		return true;
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemstack) 
	{
		return slotID == 1 ? itemstack.getItem() instanceof IRefinableItem && ((IRefinableItem) itemstack.getItem()).canSmeltItem(itemstack) : (slotID == 0 ? itemstack.getItem() instanceof IItemElectric : (slotID == 2 ? true : false));
	}
	
	// ISidedInventory Implementation:

	@Override
	public int[] getSizeInventorySide(int side) 
	{
		return side == 1 ? new int[] {1} : side == 0 ? new int[] {0} : new int[] {2};
	}

	@Override
	public boolean func_102007_a(int slotID, ItemStack itemstack, int side) 
	{
		return isStackValidForSlot(slotID, itemstack);
	}

	@Override
	public boolean func_102008_b(int slotID, ItemStack itemstack, int side) 
	{
		return slotID == 2;
	}
	
	// Industrial Craft 2 Implementation:

	@Override
	public int demandsEnergy()
	{
		return this.canProcess() ? (int) ((GCCoreTileEntityFuelLoader.WATTS_PER_TICK / this.getVoltage()) * GalacticraftCore.IC2EnergyScalar) : 0;
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
