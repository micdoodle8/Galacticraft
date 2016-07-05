package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids.EnumSimplePacketAsteroids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class TileEntityMinerBase extends TileBaseElectricBlockWithInventory implements ISidedInventory, IMultiBlock
{
    public static final int HOLDSIZE = 72;
	private ItemStack[] containingItems = new ItemStack[HOLDSIZE + 1];
    private int[] slotArray;
    public boolean isMaster = false;
	public EnumFacing facing = EnumFacing.DOWN;
    private BlockPos mainBlockPosition;
    private LinkedList<BlockVec3> targetPoints = new LinkedList();
    private WeakReference<TileEntityMinerBase> masterTile = null;
	public boolean updateClientFlag;
	public boolean findTargetPointsFlag;
	public int linkCountDown = 0;
	
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
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 20 : 12);
        this.slotArray = new int[HOLDSIZE];
        for (int i = 0; i < HOLDSIZE; i++)
        	this.slotArray[i] = i + 1;
    }

    @Override
    public void update()
    {
		super.update();
		
        if (this.updateClientFlag)
        {
        	this.updateClient();
        	this.updateClientFlag = false;
        }
        
        if (this.findTargetPointsFlag)
        {
        	if (this.isMaster && this.linkedMiner != null) this.findTargetPoints();
        	this.findTargetPointsFlag = false;
        }
        
        //TODO: Find linkedminer by UUID and update it if not chunkloaded?

    	if (!this.isMaster)
    	{
            TileEntityMinerBase master = this.getMaster();

            if (master != null)
            {
                float energyLimit = master.storage.getCapacityGC() - master.storage.getEnergyStoredGC();
                if (energyLimit < 0F) energyLimit = 0F;
            	this.storage.setCapacity(energyLimit);
                this.storage.setMaxExtract(energyLimit);
                this.storage.setMaxReceive(energyLimit);
                float hasEnergy = this.getEnergyStoredGC();
                if (hasEnergy > 0F)
                {	
                	this.extractEnergyGC(null, master.receiveEnergyGC(null, hasEnergy, false), false);
                }
            }
    	}

    	//Used for refreshing client with linked miner position data
		if (this.linkCountDown > 0)
			this.linkCountDown--;
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
			if (this.linkedMinerID == null)
	    	{
	        	if (EntityAstroMiner.spawnMinerAtBase(this.worldObj, this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1, EnumFacing.getHorizontal((this.facing.getHorizontalIndex() + 2) ^ 1), new BlockVec3(this), player))
	        	{
	        		this.findTargetPoints();
	        		return true;
	        	}
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
    
    public TileEntityMinerBase getMaster()
    {
        if (this.mainBlockPosition == null)
        {
            return null;
        }

        if (masterTile == null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

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
            this.worldObj.setBlockToAir(this.mainBlockPosition);
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
                this.worldObj.removeTileEntity(this.getPos());
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
            NBTTagCompound tagCompound = nbt.getCompoundTag("masterpos");
            this.mainBlockPosition = new BlockPos(tagCompound.getInteger("x"), tagCompound.getInteger("y"), tagCompound.getInteger("z"));
        }
        this.facing = EnumFacing.getHorizontal(nbt.getInteger("facing"));
        this.updateClientFlag = true;
        if (nbt.hasKey("LinkedUUIDMost", 4) && nbt.hasKey("LinkedUUIDLeast", 4))
        {
            this.linkedMinerID = new UUID(nbt.getLong("LinkedUUIDMost"), nbt.getLong("LinkedUUIDLeast"));
        }
        else this.linkedMinerID = null;
        if (nbt.hasKey("TargetPoints"))
        {
        	this.targetPoints.clear();
        	final NBTTagList mpList = nbt.getTagList("TargetPoints", 10);
        	for (int j = 0; j < mpList.tagCount(); j++)
        	{
	            NBTTagCompound bvTag = mpList.getCompoundTagAt(j);
	            this.targetPoints.add(BlockVec3.readFromNBT(bvTag));
        	}
        } else
        	this.findTargetPointsFlag = this.isMaster;
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
            masterTag.setInteger("x", this.mainBlockPosition.getX());
            masterTag.setInteger("y", this.mainBlockPosition.getY());
            masterTag.setInteger("z", this.mainBlockPosition.getZ());
        	nbt.setTag("masterpos", masterTag);
        }
        nbt.setInteger("facing", this.facing.getHorizontalIndex());
        if (this.isMaster && this.linkedMinerID != null)
        {
	        nbt.setLong("LinkedUUIDMost", this.linkedMinerID.getMostSignificantBits());
	        nbt.setLong("LinkedUUIDLeast", this.linkedMinerID.getLeastSignificantBits());
        }
       	NBTTagList mpList = new NBTTagList();
    	for (int j = 0; j < this.targetPoints.size(); j++)
    	{
            mpList.appendTag(this.targetPoints.get(j).writeToNBT(new NBTTagCompound()));
    	}
    	nbt.setTag("TargetPoints", mpList);
    }
        
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

	public boolean addToInventory(ItemStack itemstack)
	{
		//TODO - add test for is container open and if so use Container.mergeItemStack
		
		boolean flag1 = false;
        int k = 1;
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
            k = 1;

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
	   super.validate(); 
	   if (this.worldObj.isRemote)
	    {
	    	GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleAsteroids(EnumSimplePacketAsteroids.S_REQUEST_MINERBASE_FACING, new Object[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() } ));
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
    public String getName()
    {
        return GCCoreUtil.translate("tile.minerBase.name");
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
		if (this.isMaster)
		{
			return this.containingItems;
		}
		TileEntityMinerBase master = this.getMaster();
		if (master != null)
		{
			return master.getContainingItems();
		}

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
		if (this.getPos().getX() == x && this.getPos().getY() == y && this.getPos().getZ() == z)
		{
			this.isMaster = true;
			this.mainBlockPosition = null;
			return;
		}
		this.isMaster = false;
		this.mainBlockPosition = new BlockPos(x, y, z);
		this.markDirty();
	}

    public void onBlockRemoval()
    {
        if (this.isMaster)
        {
        	this.invalidate();
        	this.onDestroy(this);
        	return;
        }
        
    	TileEntityMinerBase master = this.getMaster();

        if (master != null && !master.isInvalid())
        {
        	this.worldObj.destroyBlock(master.getPos(), false);
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
        	
        	entityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_ASTEROIDS, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        	return true;
        }
        else
        {
			TileEntityMinerBase master = this.getMaster();
	        return master != null && master.onActivated(entityPlayer);
        }
	}

	@Override
    public void onCreate(World world, BlockPos placedPosition)
	{
        this.mainBlockPosition = placedPosition;
        this.markDirty();

        for (int x = 0; x < 2; x++)
        {
            for (int y = 0; y < 2; y++)
            {
                for (int z = 0; z < 2; z++)
                {
                    final BlockPos vecToAdd = new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z);

                    if (!vecToAdd.equals(placedPosition))
                    {
                        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecToAdd, placedPosition, 7);
                    }
                }
            }
        }
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
                    this.worldObj.destroyBlock(this.getPos().add(x, y, z), false);
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
    	return AxisAlignedBB.fromBounds(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 2, getPos().getY() + 2, getPos().getZ() + 2);
    }

    public void updateFacing()
    {
    	if (this.isMaster && this.linkedMinerID == null)
    	{
    		// Re-orient the block
	        switch (this.facing)
	        {
	        case SOUTH:
	        	this.facing = EnumFacing.EAST;
	            break;
	        case EAST:
	        	this.facing = EnumFacing.WEST;
	            break;
	        case WEST:
	        	this.facing = EnumFacing.NORTH;
	            break;
	        case NORTH:
	        	this.facing = EnumFacing.SOUTH;
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
    	
		this.markDirty();
    }

    private void updateClient()
    {
    	int x = this.getPos().getX();
    	int y = this.getPos().getY();
    	int z = this.getPos().getZ();
    	if (this.mainBlockPosition != null)
    	{
    		x = this.mainBlockPosition.getX();
    		y = this.mainBlockPosition.getY();
    		z = this.mainBlockPosition.getZ();
    	}
    	int link = (this.linkedMinerID != null) ? 1:0;
        System.err.println(x + " " + y + " " + z + " " + this.mainBlockPosition);
    	GalacticraftCore.packetPipeline.sendToDimension(new PacketSimpleAsteroids(EnumSimplePacketAsteroids.C_UPDATE_MINERBASE_FACING, new Object[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.facing, x, y, z, link} ), this.worldObj.provider.getDimensionId());
    }
    
    @Override
    public EnumFacing getElectricInputDirection()
    {
        if (this.isMaster)
        {
        	return EnumFacing.getFront(this.facing.getHorizontalIndex() + 2);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
        	return EnumFacing.getFront(master.facing.getHorizontalIndex() + 2);
        }
        return null;
    }
    
	public void linkMiner(EntityAstroMiner entityAstroMiner)
	{
		this.linkedMiner = entityAstroMiner;
        this.linkedMinerID = this.linkedMiner.getUniqueID();
		this.updateClientFlag = true;
		this.markDirty();
	}

	public void unlinkMiner()
	{
		this.linkedMiner = null;
		this.linkedMinerID = null;
		this.updateClientFlag = true;
		this.markDirty();
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
   public int[] getSlotsForFace(EnumFacing side)
   {
       if (this.isMaster)
       {
    	   return side != EnumFacing.getFront(this.facing.getHorizontalIndex() + 2) ? slotArray : new int[] { };
       }

       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.getSlotsForFace(side);
       }

       return new int[] { };
}

   @Override
   public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
   {
       return false;
   }

   @Override
   public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
   {
       if (this.isMaster)
       {
           if (side != EnumFacing.getHorizontal(this.facing.getHorizontalIndex() + 2))
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

   @Override
   public ItemStack getStackInSlot(int par1)
   {
       if (this.isMaster)
    	   return super.getStackInSlot(par1);
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.getStackInSlot(par1);
       }

       return null;    	   
   }

   @Override
   public ItemStack decrStackSize(int par1, int par2)
   {
       if (this.isMaster)
    	   return super.decrStackSize(par1, par2);
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.decrStackSize(par1, par2);
       }

       return null;    	   
   }

   @Override
   public ItemStack removeStackFromSlot(int par1)
   {
       if (this.isMaster)
    	   return super.removeStackFromSlot(par1);
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   return master.removeStackFromSlot(par1);
       }

       return null;    	   
   }

   @Override
   public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
   {
       if (this.isMaster)
       {
    	   	super.setInventorySlotContents(par1, par2ItemStack);
   			this.markDirty();
   			return;
       }
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   master.setInventorySlotContents(par1, par2ItemStack);
       }

       return;    	   
   }

   @Override
   public ItemStack getBatteryInSlot()
   {
       if (this.isMaster)
       {
    	   return this.getStackInSlot(0);
       }
       TileEntityMinerBase master = this.getMaster();
       if (master != null)
       {
    	   master.getBatteryInSlot();
       }

       return null;
   }

   @Override
   public int getSizeInventory()
   {
	   return HOLDSIZE + 1;
   }
   
  public BlockVec3 findNextTarget()
   {
	   if (!this.targetPoints.isEmpty())
	   {	
		   BlockVec3 pos = this.targetPoints.removeFirst();
			this.markDirty();
		   if (pos != null)
	       		return pos.clone();
	   }

	   //No more mining targets, the whole area is mined
	   return null;
   }

  private void findTargetPoints()
	{
		this.targetPoints.clear();
		int baseFacing = (this.facing.getHorizontalIndex() + 2) ^ 1;
		BlockVec3 posnTarget = new BlockVec3(this);
		
		if (this.worldObj.provider instanceof WorldProviderAsteroids)
		{
			ArrayList<BlockVec3> roids = ((WorldProviderAsteroids)this.worldObj.provider).getClosestAsteroidsXZ(posnTarget.x, posnTarget.y, posnTarget.z, baseFacing, 100);
			if (roids != null && roids.size() > 0)
			{	
				this.targetPoints.addAll(roids);
				return;
			}
		}
		
		posnTarget.modifyPositionFromSide(EnumFacing.getFront(baseFacing), this.worldObj.rand.nextInt(16) + 32);
		int miny = Math.min(this.getPos().getY() * 2 - 90, this.getPos().getY() - 22);
		if (miny < 5) miny = 5;
		posnTarget.y = miny + 5 + this.worldObj.rand.nextInt(4);

		this.targetPoints.add(posnTarget);

		EnumFacing lateral = EnumFacing.NORTH;
		EnumFacing inLine = EnumFacing.getFront(baseFacing);
		if ((baseFacing & 6) == 2)
			lateral = EnumFacing.WEST;
			
		this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13));
		this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13));
		if (posnTarget.y > 17)
		{
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(EnumFacing.DOWN, 11));
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(EnumFacing.DOWN, 11));
		}
		else
		{
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 26));
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -26));
		}
		this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(EnumFacing.UP, 11));
		this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(EnumFacing.UP, 11));
		if (posnTarget.y < this.getPos().getY() - 38)
		{
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13).modifyPositionFromSide(EnumFacing.UP, 22));
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(EnumFacing.UP, 22));
			this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13).modifyPositionFromSide(EnumFacing.UP, 22));
		}

		int s = this.targetPoints.size();
		for (int i = 0; i < s; i++)
		{
			this.targetPoints.add(this.targetPoints.get(i).clone().modifyPositionFromSide(inLine,EntityAstroMiner.MINE_LENGTH + 6));
		}
		
		this.markDirty();
		return;
	}
  
  	@Override
  	public void setDisabled(int index, boolean disabled)
  	{
  		//Used to recall miner
  		TileEntityMinerBase master;
    	if (!this.isMaster)
    	{
            master = this.getMaster();
            if (master == null)
            	return;
    	} else
    		master = this;
  		if (master.linkedMiner != null) master.linkedMiner.recall();
  	}

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}

