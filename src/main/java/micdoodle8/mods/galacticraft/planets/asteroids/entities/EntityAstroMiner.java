package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityAstroMiner extends Entity
{
    public ItemStack[] cargo;

    public int energyLevel;
    public float targetYaw;
    public float targetPitch;
    
    public int AIstate;  //0 idle  1 on way to target  2 mining  3 return base  4 at base
    public int timeInCurrentState = 0;
    private BlockVec3 posTarget;
    private BlockVec3 posBase;
    private BlockVec3 waypointBase;
    private int baseFacing;
    private int facing;
    private boolean zFirst;

    private final int baseSafeRadius = 32;
    private final int MAXENERGY = 10000;
    private final double speed = 0.02D;
    private final float rotSpeed = 1.5F;
    public float shipDamage;
    public int currentDamage;
    public int timeSinceHit;
    private boolean flagLink = false;

    private float cLENGTH = 1.4F;
    private float cWIDTH = 0.8F;
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

    public EntityAstroMiner(World world, ItemStack[] cargo, int energy)
    {
        this(world);
        this.cargo = cargo.clone();
        this.energyLevel = energy;
    }

    public EntityAstroMiner(World world)
    {
        super(world);
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
        this.renderDistanceWeight = 5.0D;
        this.setSize(cLENGTH, cWIDTH);
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
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
    	final NBTTagList var2 = nbt.getTagList("Items", 10);
        this.cargo = new ItemStack[27];

        if (var2 != null)
        {
	        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
	        {
	            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
	            final int var5 = var4.getByte("Slot") & 255;
	
	            if (var5 >= 0 && var5 < this.cargo.length)
	            {
	                this.cargo[var5] = ItemStack.loadItemStackFromNBT(var4);
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
        System.out.println("Astro Miner: Successful read from NBT");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = new NBTTagList();

        if (this.cargo != null)
        {
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

        if (this.worldObj.isRemote)
        {
        	//this.checkRotation();
        	if (this.turnProgress > 0)
            {
                double d6 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
                double d7 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
                double d1 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
                double d3 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
                this.rotationYaw = (float)(this.rotationYaw + d3 / this.turnProgress);
                this.rotationPitch = (float)(this.rotationPitch + (this.minecartPitch - this.rotationPitch) / this.turnProgress);
                --this.turnProgress;
                this.setPosition(d6, d7, d1);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        	return;
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
        
    	this.lastTickPosX = this.posX;
    	this.lastTickPosY = this.posY;
    	this.lastTickPosZ = this.posZ;
    	this.prevPosX = this.posX;
    	this.prevPosY = this.posY;
    	this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        //if (!(this.worldObj.isRemote))  //TODO network packet with AI state + move targets
        {
	        this.updateAI();
	    	if (this.energyLevel <= 0)
	    	{
	    		if (this.AIstate != 4)
	    			this.AIstate = 0;
	    	}
	    	else if (this.ticksExisted % 10 == 0) this.energyLevel--;
	    	
	    	switch (this.AIstate)
	    	{
	    	case 0:
	    		//TODO blinking distress light or something
	    		break;
	    	case 1:
	    		this.moveToTarget();
	        	this.prepareMove();
	    		break;
	    	case 2:
	        	this.prepareMove();
	    		this.doMining();
	        	if (this.ticksExisted % 2 == 0) this.energyLevel--;
	    		break;
	    	case 3:
	    		this.moveToBase();
	        	this.prepareMove();
	    		break;
	    	case 4:
	    		this.atBase();
	    		break;
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

	private void atBase()
	{
		TileEntity tileEntity = posBase.getTileEntity(this.worldObj);
		
		if (!(tileEntity instanceof TileEntityMinerBase) || tileEntity.isInvalid())
		{
			System.out.println("Problem with Astro Miner's base");
			this.AIstate = 0;
			return;
			//TODO notify owner in chat that miner can't find base
		}
		
		TileEntityMinerBase minerBase = (TileEntityMinerBase) tileEntity;
		
		// TODO
		// Empty item storage
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
				this.AIstate = 1;
			}
		}
	}

	private boolean findNextTarget()
	{
		//Simple test = 10 forward, 3 down
		this.posTarget = this.posBase.clone().modifyPositionFromSide(ForgeDirection.getOrientation(this.baseFacing), 10);
		this.posTarget.modifyPositionFromSide(ForgeDirection.DOWN, 3);
		System.out.println("Miner target: "+this.posTarget.toString());
		return true;
		// TODO Is target completely mined?  If so, change target	
		// return false;
	}

	private void moveToTarget()
	{
		if (this.posTarget == null)
		{
			AIstate = 0;
			return;
		}
		
		if (this.moveToPos(this.posTarget, false))
		{
			AIstate = 2;
		}
		
		// TODO  marker beacons for things to avoid
		// Overworld: avoid lava source blocks, spawners, chests, mossy cobble, End Portal and Fortress blocks
		// railtrack, levers, redstone dust
		// GC walkways, oxygen pipes, hydrogen pipes, wires
		
		// TODO
		/*
	- move in straight lines (basedir first) until [12?] blocks from target
	- not allowed to move less than 16 blocks closer to base in basedir (to protect own base from being mined by accident - hopefully!)
	- once reached target: waypoint (so that can retrace same route when returning to base later)
		 * 
		 */
	}

	private void moveToBase()
	{
		if (this.waypointBase == null)
		{
			AIstate = 0;
			return;
		}
		
		if (this.moveToPos(this.waypointBase, true))
		{
			AIstate = 4;
		}
		
		// TODO
		// Similar to moveToTarget
		// Goto waypoints for journey home
		// If obstructed: either mine it (v1) or will need AI pathfinding (v2 ...)
		// Can move faster because not generally obstructed at all
		//When it gets there: stop and reverse in!
		
	}

	private void doMining()
	{
		if (energyLevel < 9900)
		{
			AIstate = 3;
			System.out.println("Miner going home: "+this.posBase.toString());
		}
		// TODO
		//Mine blocks around
		//Note timing with moves - can't mine it all at once
		//There are 12 blocks around ... and 12 in front.  One block per tick?  
		//(That means can move at 5/6 block per second when mining, and 1.67 bps when traveling)
		//Once reached a certain distance from TunnelCentre, turn
		//[How exactly handle the turn?  AI turning?  IF POSSIBLE: Move TunnelCentre and rest is auto consequence.]
		
		//Turn pattern:  6 blocks apart in z or x plane   (3, 4) blocks apart in y plane
		//Order of moves so that pattern covers all bases...
		
		//If out of power, set waypoint and return to base
	}

	private void prepareMove()
	{
		// TODO
		//Check not obstructed by something immovable e.g. bedrock
		//[if it is obstructed, figure out what to do ... e.g. return to base, or turn 90 degrees?]
		//Check things to avoid in front of it (see moveToTarget() for list)
		//Can move through liquids including flowing lava
		//Mine out the 12 blocks in front of it in direction of travel when getting close
		//But no mining out in protected zone close to base (may need to do pathfinding if blocks were changed?)		
	}

	private boolean moveToPos(BlockVec3 pos, boolean reverse)
	{
		boolean stopForTurn = !this.checkRotation();
		
		if (zFirst)
		{
			//TODO
		}
		else
		{
			if (reverse)
			{
				if (this.posY != pos.y)
				{
					this.moveToPosY(pos, stopForTurn);			
				}
				else if (this.posX != pos.x)
				{
					this.moveToPosX(pos, stopForTurn);
				}
				else return true;
				//got there				
			}
			else
			{
				if (this.posX != pos.x)
				{
					this.moveToPosX(pos, stopForTurn);
				}
				else if (this.posY != pos.y)
				{
					this.moveToPosY(pos, stopForTurn);			
				}
				else return true;
				//got there
			}
		}

		return false;
	}
	
	private void moveToPosX(BlockVec3 pos, boolean stopForTurn)
	{
        this.targetPitch = 0;

		if (this.posX > pos.x)
		{
	        this.targetYaw = 270;
        	this.motionX = -this.speed;
        	//TODO some acceleration and deceleration
        	if (this.motionX < pos.x - this.posX)
        		this.motionX = pos.x - this.posX;
			this.facing = 4;
			this.setBoundingBoxForFacing();
		}
		else
		{
	        this.targetYaw = 90;
			this.motionX = this.speed;
        	if (this.motionX > pos.x - this.posX)
        		this.motionX = pos.x - this.posX;
			this.facing = 5;
			this.setBoundingBoxForFacing();
		}

        if (stopForTurn)
        	this.motionX = 0;

		this.motionY = 0;
		this.motionZ = 0;		
	}

	private void moveToPosY(BlockVec3 pos, boolean stopForTurn)
	{
		if (this.posY > pos.y)
		{
			this.targetPitch = -90;
			this.motionY = -this.speed;
        	if (this.motionY < pos.y - this.posY)
        		this.motionY = pos.y - this.posY;
			this.facing = 0;
			this.setBoundingBoxForFacing();
		}
		else
		{
			this.targetPitch = 90;
			this.motionY = this.speed;
        	if (this.motionY > pos.y - this.posY)
        		this.motionY = pos.y - this.posY;
			this.facing = 1;
			this.setBoundingBoxForFacing();
		}

        if (stopForTurn)
        {
        	this.motionY = 0;
        }

		this.motionX = 0;
		this.motionZ = 0;
	}
	
	private void moveToPosZ(BlockVec3 pos, boolean stopForTurn)
	{
        this.targetPitch = 0;

		if (this.posZ > pos.z)
		{
	        this.targetYaw = 0;
        	this.motionZ = -this.speed;
        	//TODO some acceleration and deceleration
        	if (this.motionZ < pos.z - this.posZ)
        		this.motionZ = pos.z - this.posZ;
			this.facing = 2;
			this.setBoundingBoxForFacing();
		}
		else
		{
	        this.targetYaw = 180;
			this.motionZ = this.speed;
        	if (this.motionZ > pos.z - this.posZ)
        		this.motionZ = pos.z - this.posZ;
			this.facing = 3;
			this.setBoundingBoxForFacing();
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
        if (this.rotationPitch != this.targetPitch)
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
        	flag = false;
        }

        if (this.rotationYaw != this.targetYaw)
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

	private void updateAI()
	{
		// TODO 
		/* Check whether current task finished
		// If in state 0 - look for target?
		// If in state 1 - if reached target (within [12] blocks), start mining
		// In in state 2
			If nothing left nearby look for new target
			If full or getting low on energy return to base
			Otherwise keep mining
		// If in state 3
		 	- if reached base, state 4
		// If in state 4
		    - when empty and fully charged, look for target
			*/
	}

	//x y z should be the mid-point of the 4 base blocks
	public static void spawnMinerAtBase(World world, int x, int y, int z, int facing, BlockVec3 base)
	{
        if (world.isRemote) return;
		final EntityAstroMiner miner = new EntityAstroMiner(world);
        miner.waypointBase = new BlockVec3(x, y, z).modifyPositionFromSide(ForgeDirection.getOrientation(facing), 2);
        miner.setPosition(miner.waypointBase.x, y, miner.waypointBase.z);
        miner.baseFacing = facing;
        miner.facing = facing;
        miner.motionX = 0;
        miner.motionY = 0;
        miner.motionZ = 0;
        miner.targetPitch = 0;
        miner.setBoundingBoxForFacing();
        switch (facing)
        {
        case 2: 
            miner.targetYaw = 0;
            miner.zFirst = true;
            break;
        case 3: 
            miner.targetYaw = 180;
            miner.zFirst = true;
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
        miner.AIstate = 4;
        miner.posBase = base;

        world.spawnEntityInWorld(miner);
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
        	zsize = cLENGTH;
        	break;
        case 4:
        case 5:
        	xsize = cLENGTH;
        	break;
        }
        this.width = Math.max(xsize, zsize);
        this.height = ysize;
        this.boundingBox.minX = this.posX - xsize / 2;
        this.boundingBox.minY = this.posY - ysize / 2;
        this.boundingBox.minZ = this.posZ - zsize / 2;
        this.boundingBox.maxX = this.posX + xsize / 2;
        this.boundingBox.maxY = this.posY + ysize / 2;
        this.boundingBox.maxZ = this.posZ + zsize / 2;
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
        	if (this.isEntityInvulnerable())
            {
                return false;
            }
            else
            {
                this.setBeenAttacked();
//                this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
//                this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf((int) (this.dataWatcher.getWatchableObjectInt(this.currentDamage) + par2 * 10)));
                this.shipDamage += par2 * 10;

                if (par1DamageSource.getEntity() instanceof EntityPlayer)
                {
                    if (((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode) this.shipDamage = 100;
                    else this.shipDamage += par2 * 21;
//                    this.dataWatcher.updateObject(this.currentDamage, 100);
                }

                if (this.shipDamage > 90 && !this.worldObj.isRemote)
                {
                    this.setDead();
                    //this.dropShipAsItem();
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
    	return null;
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

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
    {
        this.minecartX = p_70056_1_;
        this.minecartY = p_70056_3_;
        this.minecartZ = p_70056_5_;
        this.minecartYaw = p_70056_7_;
        this.minecartPitch = p_70056_8_;
        this.turnProgress = p_70056_9_ + 2;
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
    }
}

