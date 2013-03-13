package micdoodle8.mods.galacticraft.core.tile;

import mekanism.api.EnumGas;
import mekanism.api.IGasAcceptor;
import mekanism.api.IGasStorage;
import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenNetwork;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
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

import cpw.mods.fml.common.registry.LanguageRegistry;

public class GCCoreTileEntityOxygenCollector extends TileEntityElectricityRunnable implements IGasStorage, ITubeConnection, IInventory, IPacketReceiver
{
    public boolean active;
   	
	public static final double WATTS_PER_TICK = 200;

	private int playersUsing = 0;
	
	public int storedOxygen;
	
	public int MAX_OXYGEN = 180;
	
	public int outputSpeed = 16;
    
	private ItemStack[] containingItems = new ItemStack[1];

	@Override
	public void updateEntity() 
	{
		super.updateEntity();

		this.wattsReceived += ElectricItemHelper.dechargeItem(this.containingItems[0], WATTS_PER_TICK, this.getVoltage());
		
		if (!this.worldObj.isRemote)
		{
			this.wattsReceived = Math.max(this.wattsReceived - WATTS_PER_TICK / 4, 0);
			
			double power = 0;

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
								this.worldObj.func_94571_i(x, y, z);
							}

							power++;
						}
					}
				}
			}
			
			this.setGas(EnumGas.OXYGEN, MathHelper.floor_double(power / 5));

			if (this.ticks % 3 == 0)
			{
				Packet packet = this.getDescriptionPacket();
				
				if (packet != null)
				{
					PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 6);
				}
			}
			
			if(this.storedOxygen > this.MAX_OXYGEN)
			{
				this.storedOxygen = this.MAX_OXYGEN;
			}
			
			if (this.wattsReceived >= 0.05)
			{
				this.wattsReceived -= 0.05;
			}
			
			if(this.getGas(EnumGas.OXYGEN) > 0 && !this.worldObj.isRemote)
			{
		    	for(ForgeDirection orientation : ForgeDirection.values())
		    	{
		    		if(orientation != ForgeDirection.UNKNOWN)
		    		{
		    			if (orientation == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite())
		    			{
			    			this.setGas(EnumGas.OXYGEN, this.getGas(EnumGas.OXYGEN) - (Math.min(this.getGas(EnumGas.OXYGEN), this.outputSpeed) - OxygenNetwork.emitGasToNetwork(EnumGas.OXYGEN, Math.min(this.getGas(EnumGas.OXYGEN), this.outputSpeed), this, orientation)));

			    			TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), orientation);

			    			if(tileEntity instanceof IGasAcceptor)
			    			{
			    				if(((IGasAcceptor)tileEntity).canReceiveGas(orientation.getOpposite(), EnumGas.OXYGEN))
			    				{
			    					int sendingGas = 0;
			    					
			    					if(this.getGas(EnumGas.OXYGEN) >= this.outputSpeed)
			    					{
			    						sendingGas = this.outputSpeed;
			    					}
			    					else if(this.getGas(EnumGas.OXYGEN) < this.outputSpeed)
			    					{
			    						sendingGas = this.getGas(EnumGas.OXYGEN);
			    					}

			    					int rejects = ((IGasAcceptor)tileEntity).transferGasToAcceptor(sendingGas, EnumGas.OXYGEN);

			    					this.setGas(EnumGas.OXYGEN, this.getGas(EnumGas.OXYGEN) - (sendingGas - rejects));
			    				}
			    			}
		    			}
		    		}
		    	}
			}
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		int power = this.getGas(EnumGas.OXYGEN);

		if (power != 0)
		{
			Packet p = PacketManager.getPacket(BasicComponents.CHANNEL, this, power, this.wattsReceived, this.disabledTicks);
			return p;
		}
		
		return null;
	}

	@Override
	public void handlePacketData(INetworkManager network, int type, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.storedOxygen = dataStream.readInt();
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
	public ElectricityPack getRequest()
	{
		if (this.getGas(EnumGas.OXYGEN) > 0)
		{
			return new ElectricityPack(WATTS_PER_TICK / this.getVoltage(), this.getVoltage());
		}
		else
		{
			return new ElectricityPack();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.storedOxygen = par1NBTTagCompound.getInteger("storedOxygen");

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
		par1NBTTagCompound.setInteger("storedOxygen", this.storedOxygen);

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
	public int getGas(EnumGas type) 
	{
		if (type == EnumGas.OXYGEN)
		{
			return this.storedOxygen;
		}
		
		return 0;
	}

	@Override
	public void setGas(EnumGas type, int amount) 
	{
		if (type == EnumGas.OXYGEN)
		{
			this.storedOxygen = Math.max(Math.min(amount, this.MAX_OXYGEN), 0);
		}
	}

	@Override
	public boolean canTubeConnect(ForgeDirection direction) 
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2).getOpposite();
	}

	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		return direction == ForgeDirection.getOrientation(this.getBlockMetadata() + 2);
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
	public boolean func_94042_c()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_94041_b(int i, ItemStack itemstack)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openChest() 
	{
		
	}

	@Override
	public void closeChest() 
	{
		
	}
}
