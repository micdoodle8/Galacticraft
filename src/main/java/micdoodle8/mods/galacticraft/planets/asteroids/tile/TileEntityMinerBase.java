package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import java.lang.ref.WeakReference;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids.EnumSimplePacketAsteroids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMinerBase extends TileBaseElectricBlockWithInventory implements ISidedInventory, IMultiBlock
{
    public static final int HOLDSIZE = 72;
	private ItemStack[] containingItems = new ItemStack[HOLDSIZE + 1];
    private int[] slotArray;
    public boolean isMaster = false;
	public int facing;
    private BlockVec3 mainBlockPosition;
    private WeakReference<TileEntityMinerBase> masterTile = null;
	public boolean updateClientFlag;
	
    /**
     * The number of players currently using this chest
     */
    public int numUsingPlayers;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;

    private boolean spawnedMiner = false;

	public EntityAstroMiner linkedMiner = null;
	public UUID linkedMinerID = null;

    public TileEntityMinerBase()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 150 : 90);
        this.slotArray = new int[HOLDSIZE];
        for (int i = 0; i < HOLDSIZE; i++)
        	this.slotArray[i] = i + 1;
    }

    @Override
    public void updateEntity()
    {
		super.updateEntity();
        if (this.updateClientFlag)
        {
        	this.updateClient();
        	this.updateClientFlag = false;
        }
        
        //TODO: Find linkedminer by UUID and chunkload it?

    	if (!this.isMaster)
    	{
            TileEntityMinerBase master = this.getMaster();

            if (master != null)
            {
                this.storage.setCapacity(master.storage.getCapacityGC());
                this.storage.setMaxExtract(master.storage.getMaxExtract());
                this.storage.setMaxReceive(master.storage.getMaxReceive());
                this.extractEnergyGC(null, master.receiveEnergyGC(null, this.getEnergyStoredGC(), false), false);
            }
    	}
    }

    public boolean spawnMiner(EntityPlayerMP player)
    {
		if (this.isMaster)
		{
			if (this.linkedMiner != null)
			{
				if (this.linkedMiner.isDead)
					this.unlinkMiner();
					
			}
			if (this.linkedMiner == null)
	    	{
	        	if (EntityAstroMiner.spawnMinerAtBase(this.worldObj, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1, (this.facing + 2) ^ 1, new BlockVec3(this), player))
	        		return true;
	    	}
			return false;
		}
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
        	return master.spawnMiner(player);
        }
        return false;
    }
    
    private TileEntityMinerBase getMaster()
    {
        if (this.mainBlockPosition == null)
        {
            return null;
        }

        if (masterTile == null)
        {
            TileEntity tileEntity = this.mainBlockPosition.getTileEntity(this.worldObj);

            if (tileEntity != null)
            {
                if (tileEntity instanceof TileEntityMinerBase)
                {
                    masterTile = new WeakReference<TileEntityMinerBase>(((TileEntityMinerBase) tileEntity));
                }
            }
        }

        if (masterTile == null)
        {
            this.worldObj.setBlockToAir(this.mainBlockPosition.x, this.mainBlockPosition.y, this.mainBlockPosition.z);
        }
        else
        {
            TileEntityMinerBase master = this.masterTile.get();

            if (master != null)
            {
                return master;
            }
            else
            {
                this.worldObj.removeTileEntity(this.xCoord, this.yCoord, this.zCoord);
            }
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.containingItems = this.readStandardItemsFromNBT(nbt);
        this.isMaster = nbt.getBoolean("isMaster");
        if (!this.isMaster)
        {
        	this.mainBlockPosition = BlockVec3.readFromNBT(nbt.getCompoundTag("masterpos"));
        }
        this.facing = nbt.getInteger("facing");
        this.updateClientFlag = true;
        if (nbt.hasKey("LinkedUUIDMost", 4) && nbt.hasKey("LinkedUUIDLeast", 4))
        {
            this.linkedMinerID = new UUID(nbt.getLong("LinkedUUIDMost"), nbt.getLong("LinkedUUIDLeast"));
        }
        else this.linkedMinerID = null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);
        nbt.setBoolean("isMaster", this.isMaster);
        if (!this.isMaster && this.mainBlockPosition != null)
        {
        	NBTTagCompound masterTag = new NBTTagCompound();
        	this.mainBlockPosition.writeToNBT(masterTag);
        	nbt.setTag("masterpos", masterTag);
        }
        nbt.setInteger("facing", this.facing);
        if (this.isMaster && this.linkedMinerID != null)
        {
	        nbt.setLong("LinkedUUIDMost", this.linkedMinerID.getMostSignificantBits());
	        nbt.setLong("LinkedUUIDLeast", this.linkedMinerID.getLeastSignificantBits());
        }
    }
        
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
    }

    @Override
    public boolean receiveClientEvent(int par1, int par2)
    {
        if (par1 == 1)
        {
            this.numUsingPlayers = par2;
            return true;
        }
        else
        {
            return super.receiveClientEvent(par1, par2);
        }
    }

    @Override
    public void openInventory()
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numUsingPlayers);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
    }

    @Override
    public void closeInventory()
    {
        if (this.getBlockType() != null && this.getBlockType() instanceof BlockMinerBase)
        {
            --this.numUsingPlayers;
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numUsingPlayers);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        }
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

	public boolean addToInventory(ItemStack itemstack)
	{
		//TODO - add test for is container open and if so use Container.mergeItemStack
		
		boolean flag1 = false;
        int k = 0;
        int invSize = this.getSizeInventory();

        ItemStack existingStack;

        if (itemstack.isStackable())
        {
            while (itemstack.stackSize > 0 && k < invSize )
            {
                existingStack = this.containingItems[k];

                if (existingStack != null && existingStack.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, existingStack))
                {
                    int combined = existingStack.stackSize + itemstack.stackSize;

                    if (combined <= itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize = 0;
                        existingStack.stackSize = combined;
                        flag1 = true;
                    }
                    else if (existingStack.stackSize < itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize -= itemstack.getMaxStackSize() - existingStack.stackSize;
                        existingStack.stackSize = itemstack.getMaxStackSize();
                        flag1 = true;
                    }
                }

                ++k;
            }
        }

        if (itemstack.stackSize > 0)
        {
            k = 0;

            while (k < invSize)
            {
                existingStack = this.containingItems[k];

                if (existingStack == null)
                {
                    this.containingItems[k] = itemstack.copy();
                    itemstack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                ++k;
            }
        }

		this.markDirty();
        return flag1;
	}

   @Override
    public void validate()
    {
	    if (this.worldObj.isRemote)
	    {
	    	GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(EnumSimplePacketAsteroids.S_REQUEST_MINERBASE_FACING, new Object[] { this.xCoord, this.yCoord, this.zCoord } ));
	    }
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.astrominerbase.name");
    }

    @Override
    public double getPacketRange()
    {
        return 20.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

	@Override
	protected ItemStack[] getContainingItems()
	{
        return this.containingItems;
	}

	@Override
	public boolean shouldUseEnergy()
	{
		return false;
	}

	public void setMainBlockPos(int x, int y, int z)
	{
		this.masterTile = null;
		if (this.xCoord == x && this.yCoord == y && this.zCoord == z)
		{
			this.isMaster = true;
			this.mainBlockPosition = null;
			return;
		}
		this.isMaster = false;
		this.mainBlockPosition = new BlockVec3(x, y, z);
	}

    public void onBlockRemoval()
    {
        if (this.isMaster)
        {
        	this.onDestroy(this);
        	return;
        }
        
    	TileEntityMinerBase master = this.getMaster();

        if (master != null)
        {
            master.onDestroy(this);
        }
    }

	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
        if (this.isMaster)
        {
        	ItemStack holding = entityPlayer.getCurrentEquippedItem(); 
        	if (holding != null && holding.getItem() == AsteroidsItems.astroMiner)
        		return false;
        	
        	//TODO = open GUI
        	return true;
        }
        else
        {
			TileEntityMinerBase master = this.getMaster();
	        return master != null && master.onActivated(entityPlayer);
        }
	}

	@Override
	public void onCreate(BlockVec3 placedPosition)
	{
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
	            for (int z = 0; z < 2; z++)
                {
                    this.worldObj.func_147480_a(this.xCoord + x, this.yCoord + y, this.zCoord + z, x + y + z == 0);
                }
            }
        }
	}
	
	//TODO
	//maybe 2 electrical inputs are needed?
	//chest goes above (could be 2 chests or other mods storage)

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
    	return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 2, yCoord + 2, zCoord + 2);
    }

    public void updateFacing()
    {
    	if (this.isMaster && this.linkedMinerID == null)
    	{
    		// Re-orient the block
	        switch (this.facing)
	        {
	        case 0:
	        	this.facing = 3;
	            break;
	        case 3:
	        	this.facing = 1;
	            break;
	        case 1:
	        	this.facing = 2;
	            break;
	        case 2:
	        	this.facing = 0;
	            break;
	        }
	
	    	super.updateFacing();
    	}
    	else
    	{
            TileEntityMinerBase master = this.getMaster();
            if (master != null) master.updateFacing();
    	}
    	
    	if (!this.worldObj.isRemote)
    		this.updateClient();
    }

    private void updateClient()
    {
    	int x = this.xCoord;
    	int y = this.yCoord;
    	int z = this.zCoord;
    	if (this.mainBlockPosition != null)
    	{
    		x = this.mainBlockPosition.x;
    		y = this.mainBlockPosition.y;
    		z = this.mainBlockPosition.z;
    	}
    	GalacticraftCore.packetPipeline.sendToDimension(new PacketSimpleAsteroids(EnumSimplePacketAsteroids.C_UPDATE_MINERBASE_FACING, new Object[] { this.xCoord, this.yCoord, this.zCoord, this.facing, x, y, z} ), this.worldObj.provider.dimensionId);
    }
    
    @Override
    public ForgeDirection getElectricInputDirection()
    {
        if (this.isMaster)
        {
        	return ForgeDirection.getOrientation(this.facing + 2);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
        	return ForgeDirection.getOrientation(master.facing + 2);
        }
        return ForgeDirection.UNKNOWN;
    }
    
	public void linkMiner(EntityAstroMiner entityAstroMiner)
	{
		this.linkedMiner = entityAstroMiner;
        this.linkedMinerID = this.linkedMiner.getUniqueID();
	}

	public void unlinkMiner()
	{
		this.linkedMiner = null;
		this.linkedMinerID = null;
	}

   public UUID getLinkedMiner()
    {
        if (this.isMaster)
        {
        	return this.linkedMinerID;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
        	return master.linkedMinerID;
        }
        return null;
    }
   
   @Override
   public int[] getAccessibleSlotsFromSide(int side)
   {
       if (this.isMaster)
       {
    	   return side != this.facing + 2 ? slotArray : new int[] { };
       }

       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.getAccessibleSlotsFromSide(side);
       }

       return new int[] { };
}

   @Override
   public boolean canInsertItem(int slotID, ItemStack itemstack, int side)
   {
       return false;
   }

   @Override
   public boolean canExtractItem(int slotID, ItemStack itemstack, int side)
   {
       if (this.isMaster)
       {
           if (side != this.facing + 2)
           {
               return slotID > 0 || ItemElectricBase.isElectricItemEmpty(itemstack);
           }

           return false;
       }
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.canExtractItem(slotID, itemstack, side);
       }

       return false;
   }

   @Override
   public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
   {
       if (this.isMaster)
    	   return slotID > 0 || ItemElectricBase.isElectricItem(itemstack.getItem());
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.isItemValidForSlot(slotID, itemstack);
       }

       return false;    	   
   }
}