package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import java.lang.ref.WeakReference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockMinerBase;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityAstroMiner;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityMinerBase extends TileBaseElectricBlockWithInventory implements IMultiBlock
{
    private ItemStack[] containingItems = new ItemStack[72];
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean isMaster = false;
    @NetworkedField(targetSide = Side.CLIENT)
	public int facing;
    private WeakReference<TileEntityMinerBase> masterTile = null;
    private BlockVec3 mainBlockPosition;

    /**
     * The number of players currently using this chest
     */
    public int numUsingPlayers;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;

    private boolean spawnedMiner = false;

	public EntityAstroMiner linkedMiner;

    public TileEntityMinerBase()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 150 : 90);
    }

    @Override
    public void updateEntity()
    {
		super.updateEntity();
    	if (this.isMaster)
    	{
    		if (!this.spawnedMiner && this.linkedMiner == null && this.hasEnoughEnergyToRun)
	    	{
	    		this.spawnedMiner = true;
	        	EntityAstroMiner.spawnMinerAtBase(this.worldObj, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1, (this.facing + 2) ^ 1, new BlockVec3(this));
	    	}    	
    	}
    	else
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
    }
        
    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be
     * 64, possibly will be extended. *Isn't this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes
     * with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    /**
     * Causes the TileEntity to reset all it's cached values for it's container
     * block, blockID, metaData and in the case of chests, the adjcacent chest
     * check
     */
    @Override
    public void updateContainingBlockInfo()
    {
        super.updateContainingBlockInfo();
    }

    /**
     * Called when a client event is received with the event number and
     * argument, see World.sendClientEvent
     */
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
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
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

	public void linkMiner(EntityAstroMiner entityAstroMiner)
	{
		this.linkedMiner = entityAstroMiner;
	}

	public void setMaster()
	{
		this.isMaster = true;
	}

	public void setSlave(TileEntityMinerBase master)
	{
		this.isMaster = false;
		this.masterTile = new WeakReference<TileEntityMinerBase>(master);
		this.mainBlockPosition = new BlockVec3(master);
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
		// TODO Auto-generated method stub
		
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
    	if (this.isMaster)
    	{
	    	System.out.println("Wrench master "+this.facing);
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
	    	System.out.println("Wrench client to ->");
            TileEntityMinerBase master = this.getMaster();
            if (master != null) master.updateFacing();
    	}
    }
    
    
    @Override
    public ForgeDirection getElectricInputDirection()
    {
        if (this.isMaster)
        {
        	return ForgeDirection.getOrientation(this.facing + 2);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null) return master.getElectricInputDirection();
        return ForgeDirection.UNKNOWN;
    }
}