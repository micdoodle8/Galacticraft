package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.components.common.BasicComponents;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.item.ElectricItemHelper;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityElectricityRunnable;

import com.google.common.io.ByteArrayDataInput;

public class GCCoreTileEntityFuelLoader extends TileEntityElectricityRunnable implements IInventory, IPacketReceiver
{
	private ItemStack[] containingItems = new ItemStack[2];
	public static final double WATTS_PER_TICK = 300;
	private int playersUsing = 0;
	private GCCoreTileEntityLandingPad attachedLandingPad;
	
	@Override
	public boolean canConnect(ForgeDirection direction) 
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
	}

	@Override
	public ElectricityPack getRequest()
	{
		if (this.getStackInSlot(1) != null)
		{
			return new ElectricityPack(WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}
	
	public void transferFuelToSpaceship(EntitySpaceshipBase spaceship)
	{
		if (!this.worldObj.isRemote && this.getStackInSlot(1) != null && (this.getStackInSlot(1).getMaxDamage() - this.getStackInSlot(1).getItemDamage() != 0) && this.getStackInSlot(1).getItemDamage() < this.getStackInSlot(1).getMaxDamage())
		{
			spaceship.fuel += 1F;
			this.getStackInSlot(1).setItemDamage(this.getStackInSlot(1).getItemDamage() + 1);
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.wattsReceived += ElectricItemHelper.dechargeItem(this.getStackInSlot(0), WATTS_PER_TICK, this.getVoltage());
		
		if (!this.worldObj.isRemote)
		{
			this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_TICK / 4, 0);
			
			TileEntity pad = VectorHelper.getTileEntityFromSide(worldObj, new Vector3(this), ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite());
			
			if (pad != null && pad instanceof GCCoreTileEntityLandingPad)
			{
				this.attachedLandingPad = (GCCoreTileEntityLandingPad) pad;
			}
			else
			{
				this.attachedLandingPad = null;
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
		Packet p = PacketManager.getPacket(BasicComponents.CHANNEL, this, this.wattsReceived, this.disabledTicks);
		return p;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream) 
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.wattsReceived = dataStream.readInt();
				this.disabledTicks = dataStream.readInt();
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

	@Override
	public boolean func_94042_c() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}

}
