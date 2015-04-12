package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockTuple;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.IFluidBlock;

public class EntityAstroMiner extends Entity implements IInventory, IPacketReceiver
{
	private static final int MINE_LENGTH = 24;
    private static final int MAXENERGY = 10000;
    private static final int RETURNENERGY = 1000;
    private static final int RETURNDROPS = 10;
    private static final int INV_SIZE = 27;
    private static final float cLENGTH = 3.6F;
    private static final float cWIDTH = 1.8F;
    private static final float cHEIGHT = 1.7F;
    private static final double SPEEDUP = 2.5D;
    
    public static final int AISTATE_OFFLINE = -1;
    public static final int AISTATE_STUCK = 0;
    public static final int AISTATE_ATBASE = 1;
    public static final int AISTATE_TRAVELLING = 2;
    public static final int AISTATE_MINING = 3;
    public static final int AISTATE_RETURNING = 4;
    public static final int AISTATE_DOCKING = 5;
    
    private boolean TEMPDEBUG = false;

	public ItemStack[] cargoItems;

    public int energyLevel;
    public int mineCount = 0;
    public float targetYaw;
    public float targetPitch;
    
    public int AIstate;
    public int timeInCurrentState = 0;
	private EntityPlayerMP playerMP = null;
	private UUID playerUUID;
	private BlockVec3 posTarget;
    private BlockVec3 posBase;
    private BlockVec3 waypointBase;
    private LinkedList<BlockVec3> wayPoints = new LinkedList();
    private LinkedList<BlockVec3> minePoints = new LinkedList();
    private BlockVec3 minePointCurrent = null;
    private int baseFacing;
    public int facing;
    private int facingAI;
    private int lastFacing;
    private static BlockVec3[] headings = {
    	new BlockVec3(0, -1, 0),
    	new BlockVec3(0, 1, 0),
    	new BlockVec3(0, 0, -1),
    	new BlockVec3(0, 0, 1),
    	new BlockVec3(-1, 0, 0),
    	new BlockVec3(1, 0, 0)    };
    private static BlockVec3[] headings2 = {
    	new BlockVec3(0, -3, 0),
    	new BlockVec3(0, 2, 0),
    	new BlockVec3(0, 0, -3),
    	new BlockVec3(0, 0, 2),
    	new BlockVec3(-3, 0, 0),
    	new BlockVec3(2, 0, 0)    };

    private final int baseSafeRadius = 32;
    private final double speed = 0.02D;
    private final float rotSpeed = 1.5F;
    private boolean noSpeedup = false;  //This stops the miner getting stuck at turning points
    public float shipDamage;
    public int currentDamage;
    public int timeSinceHit;
    private boolean flagLink = false;

    //To do:
    //   break the entity drops it as an item

    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;

	private int tryBlockLimit;
	private int inventoryDrops;
	public boolean stopForTurn;

    private static ArrayList<Block> noMineList = new ArrayList();
    public static BlockTuple blockingBlock = new BlockTuple(Blocks.air, 0);
    
    static
    {
		//Avoid:
		// Overworld: avoid lava source blocks, mossy cobble, End Portal and Fortress blocks
		// railtrack, levers, redstone dust, GC walkways, 
    	//Anything with a tileEntity will also be avoided:
		// spawners, chests, oxygen pipes, hydrogen pipes, wires
		noMineList.add(Blocks.bedrock);
		noMineList.add(Blocks.lava);
		noMineList.add(Blocks.mossy_cobblestone);
		noMineList.add(Blocks.end_portal);
		noMineList.add(Blocks.end_portal_frame);
		noMineList.add(Blocks.stonebrick);
		noMineList.add(Blocks.farmland);
		noMineList.add(Blocks.rail);
		noMineList.add(Blocks.lever);
		noMineList.add(Blocks.redstone_wire);
		noMineList.add(AsteroidBlocks.blockWalkway);
		//TODO:
		//Add configurable blacklist
    }

    public EntityAstroMiner(World world, ItemStack[] cargo, int energy)
    {
        this(world);
        this.cargoItems = cargo.clone();
        this.energyLevel = energy;
    }

    public EntityAstroMiner(World world)
    {
        super(world);
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
        this.renderDistanceWeight = 5.0D;
        this.width = cLENGTH;
        this.height = cWIDTH;
        this.setSize(cLENGTH, cWIDTH);
        this.yOffset = 0;
        this.myEntitySize = Entity.EnumEntitySize.SIZE_6;
//        this.dataWatcher.addObject(this.currentDamage, new Integer(0));
//        this.dataWatcher.addObject(this.timeSinceHit, new Integer(0));
        this.isImmuneToFire = true;
        this.noClip = true;
    }

    @Override
    protected void entityInit()
    {
        this.dataWatcher.addObject(19, new Float(0.0F));
    }

    @Override
    public int getSizeInventory()
    {
        return this.cargoItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return this.cargoItems[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        if (this.cargoItems[var1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[var1].stackSize <= var2)
            {
                var3 = this.cargoItems[var1];
                this.cargoItems[var1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[var1].splitStack(var2);

                if (this.cargoItems[var1].stackSize == 0)
                {
                    this.cargoItems[var1] = null;
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
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        if (this.cargoItems[var1] != null)
        {
            final ItemStack var2 = this.cargoItems[var1];
            this.cargoItems[var1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2)
    {
        this.cargoItems[var1] = var2;

        if (var2 != null && var2.stackSize > this.getInventoryStackLimit())
        {
            var2.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName()
    {
        return "AstroMiner";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return !this.isDead && var1.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }
    
	private void emptyInventory(TileEntityMinerBase minerBase)
	{
		for (int i = 0; i < this.cargoItems.length; i++)
		{
			ItemStack stack = this.cargoItems[i];
			if (stack == null)
				continue;
			if (stack.stackSize == 0)
			{
				this.cargoItems[i] = null;
				continue;
			}
			minerBase.addToInventory(stack);
			if (stack == null || stack.stackSize == 0)
			{
				this.cargoItems[i] = null;
			}
			else
				this.cargoItems[i] = stack;
		}
		this.markDirty();
	}

    @Override
    public void onUpdate()
    {
	    if (this.posY < -64.0D)
	    {
	        this.kill();
	        return;
	    }
   	
        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

		stopForTurn = !this.checkRotation();
        this.facing = this.getFacingFromRotation();
    	this.setBoundingBoxForFacing();

    	if (this.worldObj.isRemote)
        {
            if (this.turnProgress == 0)
            {
	            this.turnProgress++;
	            double diffX = this.minecartX - this.posX;
	            double diffY = this.minecartY - this.posY;
	            double diffZ = this.minecartZ - this.posZ;
            	if (Math.abs(diffX) > 1.0D || Math.abs(diffY) > 1.0D || Math.abs(diffZ) > 1.0D)
            	{
            		this.posX = this.minecartX;
            		this.posY = this.minecartY;
            		this.posZ = this.minecartZ;
            	}
            	else
            	{
		            if (Math.abs(diffX) > Math.abs(this.motionX))
		            		this.motionX += diffX / 10D;
		            if (Math.abs(diffY) > Math.abs(this.motionY))
		            		this.motionY += diffY / 10D;
		            if (Math.abs(diffZ) > Math.abs(this.motionZ))
		            		this.motionZ += diffZ / 10D;
            	}
            }
            this.posX += this.motionX;
            this.boundingBox.minX += this.motionX;
            this.boundingBox.maxX += this.motionX;
            this.posY += this.motionY;
            this.boundingBox.minY += this.motionY;
            this.boundingBox.maxY += this.motionY;
            this.posZ += this.motionZ;
            this.boundingBox.minZ += this.motionZ;
            this.boundingBox.maxZ += this.motionZ;
            this.setRotation(this.rotationYaw, this.rotationPitch);
        	return;
        }
        
    	if (this.ticksExisted % 10 == 0)
    	{
    		if (this.playerMP == null) 
	    	{
    			 if (this.playerUUID != null) this.playerMP = PlayerUtil.getPlayerByUUID(this.playerUUID);
	    	}
	    	else
	    	{
	    		if (!PlayerUtil.isPlayerOnline(this.playerMP)) this.playerMP = null;
	    	}
    	}
    	
        if (flagLink)
        {
	    	TileEntity tileEntity = posBase.getTileEntity(this.worldObj);
			if (tileEntity instanceof TileEntityMinerBase)
			{
				((TileEntityMinerBase) tileEntity).linkMiner(this);
			}
        	flagLink = false;
        }
        
        if (this.playerMP == null)
        {
            //Go into dormant state if player is offline
        	this.motionX = 0;
        	this.motionY = 0;
        	this.motionZ = 0;
        	GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.dimensionId);
            return;
        }
        
    	if (this.lastFacing != this.facingAI)
    	{
    		this.lastFacing = this.facingAI;
    		this.prepareMove(12, 0);
    		this.prepareMove(12, 1);
    		this.prepareMove(12, 2);
    	}

    	this.lastTickPosX = this.posX;
    	this.lastTickPosY = this.posY;
    	this.lastTickPosZ = this.posZ;
    	this.prevPosX = this.posX;
    	this.prevPosY = this.posY;
    	this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

    	if (this.energyLevel <= 0)
    	{
    		if (this.AIstate > AISTATE_ATBASE)
    			this.AIstate = AISTATE_STUCK;
    	}
    	else if (this.ticksExisted % 2 == 0) this.energyLevel--;
    	
    	switch (this.AIstate)
    	{
    	case AISTATE_STUCK:
    		//TODO blinking distress light or something
    		//TODO: check close to base and if so, reverse in slowly
    		break;
    	case AISTATE_ATBASE:
    		this.atBase();
    		break;
    	case AISTATE_TRAVELLING:
    		if (!this.moveToTarget())
    			this.prepareMove(2, 2);
    		break;
    	case AISTATE_MINING:
    		if (!this.doMining() && this.ticksExisted % 2 == 0)
    		{
    			this.energyLevel--;
    			this.prepareMove(1, 2);
    		}
    		break;
    	case AISTATE_RETURNING:
    		this.moveToBase();
        	this.prepareMove(4, 1);
    		break;
    	case AISTATE_DOCKING:
    		if (this.waypointBase != null)
    		{	
    			if (this.moveToPos(this.waypointBase, true))
    			{
    				this.AIstate = AISTATE_ATBASE;
    				this.motionX = 0;
    				this.motionY = 0;
    				this.motionZ = 0;
    			}
    		}
    		else
    			this.AIstate = AISTATE_STUCK;
    		break;
    	}

        GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.worldObj.provider.dimensionId);
    	
        this.posX += this.motionX;
        this.boundingBox.minX += this.motionX;
        this.boundingBox.maxX += this.motionX;
        this.posY += this.motionY;
        this.boundingBox.minY += this.motionY;
        this.boundingBox.maxY += this.motionY;
        this.posZ += this.motionZ;
        this.boundingBox.minZ += this.motionZ;
        this.boundingBox.maxZ += this.motionZ;
/*        if (this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) > 0)
        {
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) - 1));
        }

        if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 0)
        {
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) - 1));
        }
        
*/    
    }

	//packet with AIstate, energy, rotationP + Y, mining data count
    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.AIstate = buffer.readInt();
        this.energyLevel = buffer.readInt();
        this.targetPitch = buffer.readFloat();
        this.targetYaw = buffer.readFloat();
        this.mineCount = buffer.readInt();
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        list.add(this.playerMP == null ? AISTATE_OFFLINE : this.AIstate);
        list.add(this.energyLevel);
        list.add(this.targetPitch);
        list.add(this.targetYaw);
        list.add(this.mineCount);
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {
    }

	private int getFacingFromRotation()
	{
		if (this.rotationPitch > 45F)
			return 1;
		if (this.rotationPitch < -45F)
			return 0;
		float rY = this.rotationYaw % 360F;
		//rotationYaw 5 90 4 270 2 180 3 0
		if (rY < 45F || rY > 315F)
			return 3;
		if (rY < 135F)
			return 5;
		if (rY < 225F)
			return 2;
		return 4;
	}

	private void atBase()
	{
		TileEntity tileEntity = posBase.getTileEntity(this.worldObj);
		
		if (!(tileEntity instanceof TileEntityMinerBase) || tileEntity.isInvalid())
		{
			System.out.println("Problem with Astro Miner's base");
			this.AIstate = AISTATE_STUCK;
			return;
			//TODO notify owner in chat that miner can't find base
		}
		
		TileEntityMinerBase minerBase = (TileEntityMinerBase) tileEntity;
		this.wayPoints.clear();
		
		this.emptyInventory(minerBase);
		this.inventoryDrops = 0;
		
		// Recharge
		if (minerBase.hasEnoughEnergyToRun && this.energyLevel < MAXENERGY)
		{
			this.energyLevel += 100;
			minerBase.storage.extractEnergyGC(minerBase.storage.getMaxExtract(), false);
		}
		
		// When fully charged, set off again
		if (this.energyLevel >= MAXENERGY)
		{
			this.energyLevel = MAXENERGY;
			if (this.findNextTarget())
			{
				this.AIstate = AISTATE_TRAVELLING;
				this.wayPoints.add(this.waypointBase.clone());
			}
		}
	}

	private boolean findNextTarget()
	{
		if (!this.minePoints.isEmpty())
		{	
			this.posTarget = this.minePoints.getFirst().clone();
			return true;
		}
		this.posTarget = this.posBase.clone().modifyPositionFromSide(ForgeDirection.getOrientation(this.baseFacing), this.worldObj.rand.nextInt(40) + 10);
		if ((this.baseFacing & 6) == 2)
		{
			this.posTarget.modifyPositionFromSide(ForgeDirection.WEST, this.worldObj.rand.nextInt(40) - 20);
		}
		else this.posTarget.modifyPositionFromSide(ForgeDirection.NORTH, this.worldObj.rand.nextInt(40) - 20);
		this.posTarget.modifyPositionFromSide(ForgeDirection.DOWN, 3 + this.worldObj.rand.nextInt(10));
		if (ConfigManagerCore.enableDebug) System.out.println("Miner target: "+this.posTarget.toString());
		return true;
		// TODO Is target completely mined?  If so, change target	
		// return false;
	}

	/**
	 * 
	 * @return  True if reached a turning point
	 */
	private boolean moveToTarget()
	{
		if (this.energyLevel < this.RETURNENERGY || this.inventoryDrops > this.RETURNDROPS)
		{
			AIstate = AISTATE_RETURNING;
			return true;		
		}
		
		if (this.posTarget == null)
		{
			AIstate = AISTATE_STUCK;
			return true;
		}
		
		if (this.moveToPos(this.posTarget, false))
		{
			AIstate = AISTATE_MINING;
			wayPoints.add(this.posTarget.clone());
			this.setMinePoints();
			return true;
		}
		
		return false;
		// TODO  marker beacons for things to avoid
		// Overworld: avoid lava source blocks, spawners, chests, mossy cobble, End Portal and Fortress blocks
		// railtrack, levers, redstone dust
		// GC walkways, oxygen pipes, hydrogen pipes, wires
		
		// TODO
		//- move in straight lines (basedir first) until [12?] blocks from target
		//- not allowed to move less than 16 blocks closer to base in basedir (to protect own base from being mined by accident - hopefully!)
	}

	private void moveToBase()
	{
		if (this.wayPoints.size() == 0)
		{
			//When it gets there: stop and reverse in!
			AIstate = AISTATE_DOCKING;
			if (this.waypointBase != null)
			{
				//Teleport back to base in case of any serious problem
				this.setPosition(this.waypointBase.x, this.waypointBase.y, this.waypointBase.z);
		        this.facingAI = this.baseFacing;
			}
			return;
		}
		
		if (this.moveToPos(this.wayPoints.getLast(), true))
		{
			this.wayPoints.removeLast();
		}
		
		// TODO
		// If obstructed: either mine it (v1) or will need AI pathfinding (v2 ...)	
	}

	private void setMinePoints()
	{
		//Still some areas left to mine from last visit (maybe it was full or out of power?)
		if (this.minePoints.size() > 0) return;
		
		BlockVec3 inFront = new BlockVec3(MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 1.5D), MathHelper.floor_double(this.posZ + 0.5D));
		int otherEnd = this.MINE_LENGTH;
		if (this.baseFacing == 2 || this.baseFacing == 4) otherEnd = -otherEnd;
		switch (this.baseFacing)
		{
		case 2:
		case 3:
			this.minePoints.add(inFront.clone().translate(0, 0, otherEnd));
			this.minePoints.add(inFront.clone().translate(4, 0, otherEnd));
			this.minePoints.add(inFront.clone().translate(4, 0, 0));
			this.minePoints.add(inFront.clone().translate(2, 3, 0));
			this.minePoints.add(inFront.clone().translate(2, 3, otherEnd));
			this.minePoints.add(inFront.clone().translate(-2, 3, otherEnd));
			this.minePoints.add(inFront.clone().translate(-2, 3, 0));
			this.minePoints.add(inFront.clone().translate(-4, 0, 0));
			this.minePoints.add(inFront.clone().translate(-4, 0, otherEnd));
			this.minePoints.add(inFront.clone().translate(-2, -3, otherEnd));
			this.minePoints.add(inFront.clone().translate(-2, -3, 0));
			this.minePoints.add(inFront.clone().translate(2, -3, 0));
			this.minePoints.add(inFront.clone().translate(2, -3, otherEnd));
			this.minePoints.add(inFront.clone().translate(0, 0, otherEnd));
			break;
		case 4:
		case 5:
			this.minePoints.add(inFront.clone().translate(otherEnd, 0, 0));
			this.minePoints.add(inFront.clone().translate(otherEnd, 0, 4));
			this.minePoints.add(inFront.clone().translate(0, 0, 4));
			this.minePoints.add(inFront.clone().translate(0, 3, 2));
			this.minePoints.add(inFront.clone().translate(otherEnd, 3, 2));
			this.minePoints.add(inFront.clone().translate(otherEnd, 3, -2));
			this.minePoints.add(inFront.clone().translate(0, 3, -2));
			this.minePoints.add(inFront.clone().translate(0, 0, -4));
			this.minePoints.add(inFront.clone().translate(otherEnd, 0, -4));
			this.minePoints.add(inFront.clone().translate(otherEnd, -3, -2));
			this.minePoints.add(inFront.clone().translate(0, -3, -2));
			this.minePoints.add(inFront.clone().translate(0, -3, 2));
			this.minePoints.add(inFront.clone().translate(otherEnd, -3, 2));
			this.minePoints.add(inFront.clone().translate(otherEnd, 0, 0));
			break;
		}
	}

	/**
	 * 
	 * @return  True if reached a turning point
	 */
	private boolean doMining()
	{
		if (this.energyLevel < this.RETURNENERGY || this.inventoryDrops > this.RETURNDROPS || this.minePoints.size() == 0)
		{
			if (this.minePoints.size() > 0 && this.minePointCurrent != null)
			{
				this.minePoints.addFirst(this.minePointCurrent);
			}
			AIstate = AISTATE_RETURNING;
			if (ConfigManagerCore.enableDebug) System.out.println("Miner going home: "+this.posBase.toString());
			return true;		
		}

		if (this.moveToPos(this.minePoints.getFirst(), false))
		{
			this.minePointCurrent = this.minePoints.getFirst();
			this.minePoints.removeFirst();
			return true;
		}
		return false;
	}

	private void tryBackIn()
	{
		if (this.waypointBase.distanceSquared(new BlockVec3(this)) <= 9.1D)
		{
			this.AIstate = AISTATE_DOCKING;
	        switch (this.baseFacing)
	        {
	        case 2: 
	            this.targetYaw = 180;
	            break;
	        case 3: 
	            this.targetYaw = 0;
	            break;
	        case 4: 
	            this.targetYaw = 270;
	            break;
	        case 5: 
	            this.targetYaw = 90;
	            break;
	        }
		}
		else this.AIstate = AISTATE_STUCK;
	}


	/**
	 * Mine out the area in front of the miner (dist blocks from miner centre)
	 * @param limit   Maximum block count to be mined this tick 
	 * @param dist
	 * @return  True if the mining failed (meaning the miner's path is blocked)
	 */
	private boolean prepareMove(int limit, int dist)
	{
		BlockVec3 inFront = new BlockVec3(MathHelper.floor_double(this.posX + 0.5D), MathHelper.floor_double(this.posY + 1.5D), MathHelper.floor_double(this.posZ + 0.5D));
		if (dist == 2) inFront.add(headings2[this.facingAI]);
		else
		{	
			if ((this.facingAI & 1) == 0) dist++; 
			if (dist > 0) inFront.add(headings[this.facingAI].clone().scale(dist));
		}
		int x = inFront.x;
		int y = inFront.y;
		int z = inFront.z;
		
		//Test not trying to mine own dock!
		if (y == this.waypointBase.y && x == this.waypointBase.x - ((this.baseFacing == 5) ? 1 : 0) && z == this.waypointBase.z - ((this.baseFacing == 3) ? 1 : 0))
		{
			this.tryBackIn();
			return false;
		}
		boolean wayBarred = false;
		this.tryBlockLimit = limit;

		//Check not obstructed by something immovable e.g. bedrock
		//Mine out the 12 blocks in front of it in direction of travel when getting close
		//There are 12 blocks around ... and 12 in front.  One block per tick?  
		//(That means can move at 5/6 block per second when mining, and 1.67 bps when traveling)
		switch (this.facingAI & 6)
		{
		case 0:
			if (tryBlock(x, y, z)) wayBarred = true;
			if (tryBlock(x + 1, y, z)) wayBarred = true;
			if (tryBlock(x + 1, y, z - 1)) wayBarred = true;
			if (tryBlock(x, y, z - 1)) wayBarred = true;
			if (tryBlock(x, y, z - 2)) wayBarred = true;
			if (tryBlock(x - 1, y, z - 2)) wayBarred = true;
			if (tryBlock(x - 1, y, z - 1)) wayBarred = true;
			if (tryBlock(x - 2, y, z - 1)) wayBarred = true;
			if (tryBlock(x - 2, y, z)) wayBarred = true;
			if (tryBlock(x - 1, y, z)) wayBarred = true;
			if (tryBlock(x - 1, y, z + 1)) wayBarred = true;
			if (tryBlock(x, y, z + 1)) wayBarred = true;
			break;
		case 2:
			if (tryBlock(x, y - 2, z)) wayBarred = true;
			if (tryBlock(x - 1, y - 2, z)) wayBarred = true;
			if (tryBlock(x, y - 1, z)) wayBarred = true;
			if (tryBlock(x - 1, y - 1, z)) wayBarred = true;
			if (tryBlock(x + 1, y - 1, z)) wayBarred = true;
			if (tryBlock(x - 2, y - 1, z)) wayBarred = true;
			if (tryBlock(x + 1, y, z)) wayBarred = true;
			if (tryBlock(x - 2, y, z)) wayBarred = true;
			if (tryBlock(x, y, z)) wayBarred = true;
			if (tryBlock(x - 1, y, z)) wayBarred = true;
			if (tryBlock(x, y + 1, z)) wayBarred = true;
			if (tryBlock(x - 1, y + 1, z)) wayBarred = true;
			break;
		case 4:
			if (tryBlock(x, y - 2, z - 1)) wayBarred = true;
			if (tryBlock(x, y - 1, z)) wayBarred = true;
			if (tryBlock(x, y - 1, z - 1)) wayBarred = true;
			if (tryBlock(x, y - 1, z + 1)) wayBarred = true;
			if (tryBlock(x, y - 1, z - 2)) wayBarred = true;
			if (tryBlock(x, y, z + 1)) wayBarred = true;
			if (tryBlock(x, y, z - 2)) wayBarred = true;
			if (tryBlock(x, y, z - 1)) wayBarred = true;
			if (tryBlock(x, y - 2, z)) wayBarred = true;
			if (tryBlock(x, y + 1, z - 1)) wayBarred = true;
			if (tryBlock(x, y, z)) wayBarred = true;
			if (tryBlock(x, y + 1, z)) wayBarred = true;
			break;
		}
		
		// TODO
		//[if it is obstructed, figure out what to do ... e.g. return to base, or turn 90 degrees?]
		if (wayBarred)
		{
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			this.tryBlockLimit = 0;
			if (this.AIstate == AISTATE_TRAVELLING || this.AIstate == AISTATE_MINING) this.AIstate = AISTATE_RETURNING;
			else if (this.AIstate == AISTATE_RETURNING)
				this.tryBackIn();
			else this.AIstate = AISTATE_STUCK;
		}
		
		if (this.tryBlockLimit == limit && !this.noSpeedup)
		{
			this.motionX *= SPEEDUP;
			this.motionY *= SPEEDUP;
			this.motionZ *= SPEEDUP;
		}
		
		return wayBarred;

		//TODO
		//But no mining out in protected zone close to base (may need to do pathfinding if blocks were changed?)		
	}

	private boolean tryBlock(int x, int y, int z)
	{
		//Check things to avoid in front of it (see static list for list) including base type things
		//Can move through liquids including flowing lava
		Block b = this.worldObj.getBlock(x, y, z);
		if (b.getMaterial() == Material.air) return false;
		if (noMineList.contains(b))
		{
			blockingBlock.block = b;
			blockingBlock.meta = this.worldObj.getBlockMetadata(x, y, z);
			return true;
		}
		if (b instanceof BlockLiquid) return false;
		if (b instanceof IFluidBlock) return false;
		if (b instanceof IPlantable)
		{
			blockingBlock.block = b;
			blockingBlock.meta = this.worldObj.getBlockMetadata(x, y, z);
			return true;
		}
		int meta = this.worldObj.getBlockMetadata(x, y, z);
		if (b.hasTileEntity(meta) || b.getBlockHardness(this.worldObj,  x,  y,  z) < 0)
		{
			blockingBlock.block = b;
			blockingBlock.meta = meta;
			return true;
		}
		
		if (this.tryBlockLimit == 0) return false;
        BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(this.worldObj, this.playerMP.theItemInWorldManager.getGameType(), this.playerMP, x, y, z);
        if (event.isCanceled()) return false;

		this.tryBlockLimit--;
		
		ItemStack drops = getPickBlock(this.worldObj, x, y, z, b);
		if (drops != null && !this.addToInventory(drops))
		{
			//drop itemstack if AstroMiner can't hold it
            float f = 0.7F;
            double d0 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double d1 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            double d2 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(this.worldObj, x + d0, y + d1, z + d2, drops);
            entityitem.delayBeforeCanPickup = 10;
            this.worldObj.spawnEntityInWorld(entityitem);
            this.inventoryDrops++;
		}
		
		this.worldObj.setBlock(x, y, z, Blocks.air, 0, 3);
		return false;
	}

	private ItemStack getPickBlock(World world, int x, int y, int z, Block b)
	{
        Item item = Item.getItemById(Block.getIdFromBlock(b));
        
		if (item == null)
        {
            return null;
        }

        Block block = item instanceof ItemBlock && !(b instanceof BlockFlowerPot) ? Block.getBlockFromItem(item) : b;
        return new ItemStack(item, 1, block.getDamageValue(world, x, y, z));
    }

	private boolean addToInventory(ItemStack itemstack)
	{
		boolean flag1 = false;
        int k = 0;
        int invSize = this.getSizeInventory();

        ItemStack itemstack1;

        if (itemstack.isStackable())
        {
            while (itemstack.stackSize > 0 && k < invSize )
            {
                itemstack1 = this.cargoItems[k];

                if (itemstack1 != null && itemstack1.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
                {
                    int l = itemstack1.stackSize + itemstack.stackSize;

                    if (l <= itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize = 0;
                        itemstack1.stackSize = l;
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize -= itemstack.getMaxStackSize() - itemstack1.stackSize;
                        itemstack1.stackSize = itemstack.getMaxStackSize();
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
                itemstack1 = this.cargoItems[k];

                if (itemstack1 == null)
                {
                    this.cargoItems[k] = itemstack.copy();
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

	private boolean moveToPos(BlockVec3 pos, boolean reverse)
	{
		this.noSpeedup = false;
		
		if (reverse != (this.baseFacing < 4))
		{
			if (this.posZ > pos.z + 0.0001D || this.posZ < pos.z - 0.0001D)
			{
				this.moveToPosZ(pos.z, stopForTurn);			
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving Z to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else if (this.posY > pos.y - 0.9999D || this.posY < pos.y - 1.0001D)
			{
				this.moveToPosY(pos.y - 1, stopForTurn);
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving Y to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else if (this.posX > pos.x + 0.0001D || this.posX < pos.x - 0.0001D)
			{
				this.moveToPosX(pos.x, stopForTurn);
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving X to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else return true;
			//got there				
		}
		else
		{
			if (this.posX > pos.x + 0.0001D || this.posX < pos.x - 0.0001D)
			{
				this.moveToPosX(pos.x, stopForTurn);
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving X to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else if (this.posY > pos.y - 0.9999D || this.posY < pos.y - 1.0001D)
			{
				this.moveToPosY(pos.y - 1, stopForTurn);			
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving Y to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else if (this.posZ > pos.z + 0.0001D || this.posZ < pos.z - 0.0001D)
			{
				this.moveToPosZ(pos.z, stopForTurn);			
				if (TEMPDEBUG) System.out.println("At " + posX + "," + posY + "," + posZ + "Moving Z to " + pos.toString() + (stopForTurn ? " : Stop for turn " + this.rotationPitch + "," + this.rotationYaw + " | "  + this.targetPitch + "," + this.targetYaw: ""));
			}
			else return true;
			//got there
		}

		return false;
	}
	
	private void moveToPosX(int x, boolean stopForTurn)
	{
        this.targetPitch = 0;

		if (this.posX > x)
		{
	        if (this.AIstate != AISTATE_DOCKING)
	        	this.targetYaw = 270;
        	this.motionX = -this.speed;
        	//TODO some acceleration and deceleration
        	if (this.motionX * SPEEDUP <= x - this.posX)
        	{
        		this.motionX = x - this.posX;
        		this.noSpeedup = true;
        	}
			this.facingAI = 4;
		}
		else
		{
	        if (this.AIstate != AISTATE_DOCKING)
	        	this.targetYaw = 90;
			this.motionX = this.speed;
        	if (this.motionX * SPEEDUP >= x - this.posX)
        	{
        		this.motionX = x - this.posX;
	    		this.noSpeedup = true;
	    	}
			this.facingAI = 5;
		}

        if (stopForTurn)
        	this.motionX = 0;

		this.motionY = 0;
		this.motionZ = 0;		
	}

	private void moveToPosY(int y, boolean stopForTurn)
	{
		if (this.posY > y)
		{
        	this.targetPitch = -90;
			this.motionY = -this.speed;
        	if (this.motionY * SPEEDUP <= y - this.posY)
        	{
        		this.motionY = y - this.posY;
        		this.noSpeedup = true;
        	}
			this.facingAI = 0;
		}
		else
		{
        	this.targetPitch = 90;
			this.motionY = this.speed;
        	if (this.motionY * SPEEDUP >= y - this.posY)
        	{
        		this.motionY = y - this.posY;
        		this.noSpeedup = true;
        	}
			this.facingAI = 1;
		}

        if (stopForTurn)
        {
        	this.motionY = 0;
        }

		this.motionX = 0;
		this.motionZ = 0;
	}
	
	private void moveToPosZ(int z, boolean stopForTurn)
	{
        this.targetPitch = 0;

		if (this.posZ > z)
		{
	        if (this.AIstate != AISTATE_DOCKING)
	        	this.targetYaw = 180;
        	this.motionZ = -this.speed;
        	//TODO some acceleration and deceleration
        	if (this.motionZ * SPEEDUP <= z - this.posZ)
        	{
        		this.motionZ = z - this.posZ;
        		this.noSpeedup = true;
        	}
			this.facingAI = 2;
		}
		else
		{
	        if (this.AIstate != AISTATE_DOCKING)
	        	this.targetYaw = 0;
			this.motionZ = this.speed;
        	if (this.motionZ * SPEEDUP >= z - this.posZ)
        	{
        		this.motionZ = z - this.posZ;
        		this.noSpeedup = true;
        	}
			this.facingAI = 3;
		}

        if (stopForTurn)
        	this.motionZ = 0;

		this.motionY = 0;
		this.motionX = 0;		
	}

	private boolean checkRotation()
	{
		boolean flag = true;
		//Handle the turns when it changes direction
        if (this.rotationPitch > this.targetPitch + 0.001F || this.rotationPitch < this.targetPitch - 0.001F)
        {
        	if (this.rotationPitch > this.targetPitch + 180)
        		this.rotationPitch -= 360;
        	else
        	if (this.rotationPitch < this.targetPitch - 180)
        		this.rotationPitch += 360;
        		
        	if (this.rotationPitch > this.targetPitch)
        	{
        		this.rotationPitch -= this.rotSpeed;
        		if (this.rotationPitch < this.targetPitch)
        			this.rotationPitch = this.targetPitch;       		
        	}
        	else
        	{
        		this.rotationPitch += this.rotSpeed;
        		if (this.rotationPitch > this.targetPitch)
        			this.rotationPitch = this.targetPitch;       		
        	}
        }

        if (this.rotationYaw > this.targetYaw + 0.001F || this.rotationYaw < this.targetYaw - 0.001F)
        {
        	if (this.rotationYaw > this.targetYaw + 180)
        		this.rotationYaw -= 360;
        	else
        	if (this.rotationYaw < this.targetYaw - 180)
        		this.rotationYaw += 360;
        		
        	if (this.rotationYaw > this.targetYaw)
        	{
        		this.rotationYaw -= this.rotSpeed;
        		if (this.rotationYaw < this.targetYaw)
        			this.rotationYaw = this.targetYaw;       		
        	}
        	else
        	{
        		this.rotationYaw += this.rotSpeed;
        		if (this.rotationYaw > this.targetYaw)
        			this.rotationYaw = this.targetYaw;       		
        	}
        	flag = false;
        }
		
		return flag;
	}

	/** x y z should be the mid-point of the 4 base blocks
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param facing
	 * @param base
	 * @return
	 */
	public static boolean spawnMinerAtBase(World world, int x, int y, int z, int facing, BlockVec3 base, EntityPlayerMP player)
	{
        if (world.isRemote) return true;
		final EntityAstroMiner miner = new EntityAstroMiner(world, new ItemStack[EntityAstroMiner.INV_SIZE], 0);
		miner.setPlayer(player);
        miner.waypointBase = new BlockVec3(x, y - 1, z).modifyPositionFromSide(ForgeDirection.getOrientation(facing), 1);
        miner.setPosition(miner.waypointBase.x, miner.waypointBase.y, miner.waypointBase.z);
        miner.baseFacing = facing;
        miner.facingAI = facing;
        miner.lastFacing = facing;
        miner.motionX = 0;
        miner.motionY = 0;
        miner.motionZ = 0;
        miner.targetPitch = 0;
        switch (facing)
        {
        case 2: 
            miner.targetYaw = 180;
            break;
        case 3: 
            miner.targetYaw = 0;
            break;
        case 4: 
            miner.targetYaw = 270;
            break;
        case 5: 
            miner.targetYaw = 90;
            break;
        }
        miner.rotationPitch = miner.targetPitch;
        miner.rotationYaw = miner.targetYaw;
        miner.setBoundingBoxForFacing();
        miner.AIstate = AISTATE_ATBASE;
        miner.posBase = base;

        //Clear blocks, and test to see if its movement area in front of the base is blocked
        if (miner.prepareMove(12, 0))
        {
           	miner.kill();
           	return false;
        }
        if (miner.prepareMove(12, 1))
        {
           	miner.kill();
           	return false;
        }
        if (miner.prepareMove(12, 2))
        {
           	miner.kill();
           	return false;
        }

    	world.spawnEntityInWorld(miner);
    	miner.flagLink = true;
    	return true;
	}
	
    public void setPlayer(EntityPlayerMP player)
    {
    	this.playerMP = player;
    	this.playerUUID = player.getUniqueID();	
	}

	private void setBoundingBoxForFacing()
    {
    	float xsize = cWIDTH;
    	float ysize = cWIDTH;
    	float zsize = cWIDTH;
    	switch (this.facing)
        {
        case 0:
        case 1:
        	ysize = cLENGTH;
        	break;
        case 2:
        case 3:
        	ysize = cHEIGHT;
        	zsize = cLENGTH;
        	break;
        case 4:
        case 5:
        	ysize = cHEIGHT;
        	xsize = cLENGTH;
        	break;
        }
        this.width = Math.max(xsize, zsize);
        this.height = ysize;
        this.boundingBox.minX = this.posX - xsize / 2D;
        this.boundingBox.minY = this.posY + 1D - ysize / 2D;
        this.boundingBox.minZ = this.posZ - zsize / 2D;
        this.boundingBox.maxX = this.posX + xsize / 2D;
        this.boundingBox.maxY = this.posY + 1D + ysize / 2D;
        this.boundingBox.maxZ = this.posZ + zsize / 2D;
	}

	@Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (this.isDead || par1DamageSource.equals(DamageSource.cactus))
        {
            return true;
        }

        if (!this.worldObj.isRemote)
        {
        	Entity e = par1DamageSource.getEntity(); 
        	//Invulnerable to mobs
        	if (this.isEntityInvulnerable() || (e instanceof EntityLivingBase && !(e instanceof EntityPlayer)))
            {
                return false;
            }
            else
            {
                this.setBeenAttacked();
//                this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
//                this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf((int) (this.dataWatcher.getWatchableObjectInt(this.currentDamage) + par2 * 10)));
                this.shipDamage += par2 * 10;

                if (e instanceof EntityPlayer)
                {
                    if (((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode) this.shipDamage = 100;
                    else this.shipDamage += par2 * 21;
//                    this.dataWatcher.updateObject(this.currentDamage, 100);
                }

                if (this.shipDamage > 90 && !this.worldObj.isRemote)
                {
                    this.kill();
                    this.dropShipAsItem();
                    return true;
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return par1Entity.boundingBox;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
    	return this.boundingBox;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }
    
    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    @Override
	public void performHurtAnimation()
	{
//	    this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
//	    this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) * 5));
	}

    public float getDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setDamage(float p_70492_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float rotYaw, float rotPitch)
    {
    	this.minecartX = x;
    	this.minecartY = y;
    	this.minecartZ = z;
    	super.setLocationAndAngles(x, y, z, rotYaw, rotPitch);
    }
    
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
    {
        this.minecartX = p_70056_1_;
        this.minecartY = p_70056_3_;
        this.minecartZ = p_70056_5_;
        this.minecartYaw = p_70056_7_;
        this.minecartPitch = p_70056_8_;
        this.turnProgress = 0;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.velocityX = this.motionX = p_70016_1_;
        this.velocityY = this.motionY = p_70016_3_;
        this.velocityZ = this.motionZ = p_70016_5_;
        this.turnProgress = 0;
    }
    @Override
    protected void setSize(float p_70105_1_, float p_70105_2_)
    {
    	this.setBoundingBoxForFacing();
    }

    @Override
    public void setPosition(double p_70107_1_, double p_70107_3_, double p_70107_5_)
    {
        this.boundingBox.addCoord(p_70107_1_ - this.posX, p_70107_3_ - this.posY, p_70107_5_ - this.posZ);
        this.posX = p_70107_1_;
        this.posY = p_70107_3_;
        this.posZ = p_70107_5_;
    }

    @Override
    protected void kill()
    {
        this.setDead();
    	if (posBase != null)
    	{
    		TileEntity tileEntity = posBase.getTileEntity(this.worldObj);
			if (tileEntity instanceof TileEntityMinerBase)
			{
				((TileEntityMinerBase) tileEntity).unlinkMiner();
			}
    	}
    }

    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        ItemStack rocket = new ItemStack(AsteroidsItems.astroMiner, 1, 0);
        droppedItems.add(rocket);
        for (int i = 0; i < this.cargoItems.length; i++)
        {
        	if (this.cargoItems[i] != null) droppedItems.add(this.cargoItems[i]);
        	this.cargoItems[i] = null;
        }
        return droppedItems;
    }

    public void dropShipAsItem()
    {
        if (this.worldObj.isRemote)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped(new ArrayList<ItemStack>()))
        {
            EntityItem entityItem = this.entityDropItem(item, 0);

            if (item.hasTagCompound())
            {
                entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
    	final NBTTagList var2 = nbt.getTagList("Items", 10);
        this.cargoItems = new ItemStack[this.INV_SIZE];

        if (var2 != null)
        {
	        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
	        {
	            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
	            final int var5 = var4.getByte("Slot") & 255;
	
	            if (var5 < this.cargoItems.length)
	            {
	                this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
	            }
	        }
        }
        
        if (nbt.hasKey("Energy")) this.energyLevel = nbt.getInteger("Energy");
        if (nbt.hasKey("BaseX"))
        {
        	this.posBase = new BlockVec3(nbt.getInteger("BaseX"), nbt.getInteger("BaseY"), nbt.getInteger("BaseZ"));
        	this.flagLink = true;
        }
        if (nbt.hasKey("TargetX")) this.posTarget = new BlockVec3(nbt.getInteger("TargetX"), nbt.getInteger("TargetY"), nbt.getInteger("TargetZ"));
        if (nbt.hasKey("WBaseX")) this.waypointBase = new BlockVec3(nbt.getInteger("WBaseX"), nbt.getInteger("WBaseY"), nbt.getInteger("WBaseZ"));
        if (nbt.hasKey("BaseFacing")) this.baseFacing = nbt.getInteger("BaseFacing");
        if (nbt.hasKey("AIState")) this.AIstate = nbt.getInteger("AIState");       
        if (nbt.hasKey("Facing")) this.facingAI = nbt.getInteger("Facing");
        this.lastFacing = -1;
        if (nbt.hasKey("WayPoints"))
        {
        	this.wayPoints.clear();
        	final NBTTagList wpList = nbt.getTagList("WayPoints", 10);
        	for (int j = 0; j < wpList.tagCount(); j++)
        	{
	            NBTTagCompound bvTag = wpList.getCompoundTagAt(j);
	            this.wayPoints.add(BlockVec3.readFromNBT(bvTag));
        	}
        }
        if (nbt.hasKey("MinePoints"))
        {
        	this.minePoints.clear();
        	final NBTTagList mpList = nbt.getTagList("MinePoints", 10);
        	for (int j = 0; j < mpList.tagCount(); j++)
        	{
	            NBTTagCompound bvTag = mpList.getCompoundTagAt(j);
	            this.minePoints.add(BlockVec3.readFromNBT(bvTag));
        	}
        }
        if (nbt.hasKey("MinePointCurrent"))
        	this.minePointCurrent = BlockVec3.readFromNBT(nbt.getCompoundTag("MinePointCurrent"));
        else
        	this.minePointCurrent = null;
        if (nbt.hasKey("playerUUIDMost", 4) && nbt.hasKey("playerUUIDLeast", 4))
        {
            this.playerUUID = new UUID(nbt.getLong("playerUUIDMost"), nbt.getLong("playerUUIDLeast"));
        }
        else
        {
        	System.out.println("[Galacticraft] Please break and replace any AstroMiner placed in the world prior to build 3.0.11.317.");
        	this.playerUUID = null;
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = new NBTTagList();

        if (this.cargoItems != null)
        {
	        for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
	        {
	            if (this.cargoItems[var3] != null)
	            {
	                final NBTTagCompound var4 = new NBTTagCompound();
	                var4.setByte("Slot", (byte) var3);
	                this.cargoItems[var3].writeToNBT(var4);
	                var2.appendTag(var4);
	            }
	        }
        }

        nbt.setTag("Items", var2);
        nbt.setInteger("Energy", this.energyLevel);
        if (this.posBase != null)
        {
	        nbt.setInteger("BaseX", this.posBase.x);
	        nbt.setInteger("BaseY", this.posBase.y);
	        nbt.setInteger("BaseZ", this.posBase.z);
        }
        if (this.posTarget != null)
        {
	        nbt.setInteger("TargetX", this.posTarget.x);
	        nbt.setInteger("TargetY", this.posTarget.y);
	        nbt.setInteger("TargetZ", this.posTarget.z);
        }
        if (this.waypointBase != null)
        {
	        nbt.setInteger("WBaseX", this.waypointBase.x);
	        nbt.setInteger("WBaseY", this.waypointBase.y);
	        nbt.setInteger("WBaseZ", this.waypointBase.z);
        }
        nbt.setInteger("BaseFacing", this.baseFacing);
        nbt.setInteger("AIState", this.AIstate);
        nbt.setInteger("Facing", this.facingAI);
        if (this.wayPoints.size() > 0)
        {
        	NBTTagList wpList = new NBTTagList();
        	for (int j = 0; j < this.wayPoints.size(); j++)
        	{
	            wpList.appendTag(this.wayPoints.get(j).writeToNBT(new NBTTagCompound()));
        	}
        	nbt.setTag("WayPoints", wpList);
        }
        if (this.minePoints.size() > 0)
        {
        	NBTTagList mpList = new NBTTagList();
        	for (int j = 0; j < this.minePoints.size(); j++)
        	{
	            mpList.appendTag(this.minePoints.get(j).writeToNBT(new NBTTagCompound()));
        	}
        	nbt.setTag("MinePoints", mpList);
        }
        if (this.minePointCurrent != null)
        {
        	nbt.setTag("MinePointCurrent", this.minePointCurrent.writeToNBT(new NBTTagCompound()));
        }
        if (this.playerUUID != null)
        {
	        nbt.setLong("playerUUIDMost", this.playerUUID.getMostSignificantBits());
	        nbt.setLong("playerUUIDLeast", this.playerUUID.getLeastSignificantBits());
        }        
    }
}

