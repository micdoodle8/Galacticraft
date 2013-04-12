package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.network.GCCorePacketEntityUpdate;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GCCoreEntityLander extends GCCoreEntityControllable implements IInventory
{
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public double speed;
    public double actualSpeed = -5.0D;
    float maxSpeed = 0.05F;
    float minSpeed = 0.5F;
    float accel = 0.04F;
    float turnFactor = 2.0F;
    public String texture;
	public float turnProgress = 0;
	private final boolean firstPacketSent = false;
	public float rotationYawBuggy;
    public double boatX;
    public double boatY;
    public double boatZ;
    public double boatYaw;
    public double boatPitch;
	public int boatPosRotationIncrements;
	private boolean lastOnGround = false;
	private double lastMotionY;
	public ItemStack[] chestContents = new ItemStack[27];
    public int numUsingPlayers;
//	private GCCoreEntityLanderChest moduleChest;

    public GCCoreEntityLander(World var1)
    {
        super(var1);
        this.setSize(3.0F, 4.5F);
        this.yOffset = 2.5F;
        this.currentDamage = 18;
        this.timeSinceHit = 19;
        this.rockDirection = 20;
        this.speed = 0.0D;
        this.actualSpeed = -5.0D;
        this.preventEntitySpawning = true;
        this.dataWatcher.addObject(this.currentDamage, new Integer(0));
        this.dataWatcher.addObject(this.timeSinceHit, new Integer(0));
        this.dataWatcher.addObject(this.rockDirection, new Integer(1));
        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
    }

    public GCCoreEntityLander(World var1, double var2, double var4, double var6)
    {
        this(var1);
        this.setPosition(var2, var4 + this.yOffset, var6);
    }

    @Override
	public void setPosition(double par1, double par3, double par5)
    {
        this.posX = par1;
        this.posY = par3;
        this.posZ = par5;
        final float f = this.width / 2.0F;
        final float f1 = this.height;
        this.boundingBox.setBounds(par1 - f, par3 - this.yOffset + this.ySize, par5 - f, par1 + f, par3 - this.yOffset + this.ySize + f1, par5 + f);
    }

    public ModelBase getModel()
    {
        return null;
    }

    @Override
	protected void entityInit() {}

    @Override
	protected boolean canTriggerWalking()
    {
        return false;
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
	public double getMountedYOffset()
    {
        return this.height - 1.0D;
    }

    @Override
	public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
	public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            final double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            final double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0D + 114.8) * -0.5D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

	@Override
	public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ)
	{
		if(this.worldObj.isRemote)
		{
	        this.boatX = x;
	        this.boatY = y;
	        this.boatZ = z;
	        this.boatYaw = yaw;
	        this.boatPitch = pitch;
	        this.motionX = motX;
	        this.motionY = motY;
	        this.motionZ = motZ;
	        this.boatPosRotationIncrements = 5;
		}
		else
		{
			this.setPosition(x, y, z);
			this.setRotation(yaw, pitch);
			this.motionX = motX;
			this.motionY = motY;
			this.motionZ = motZ;
		}
	}

    @Override
	public void performHurtAnimation()
    {
        this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
        this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
        this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) * 5));
    }

    @Override
	public boolean attackEntityFrom(DamageSource var1, int var2)
    {
        if (this.isDead || var1.equals(DamageSource.cactus))
        {
            return true;
        }
        else
        {
            this.dataWatcher.updateObject(this.rockDirection, Integer.valueOf(-this.dataWatcher.getWatchableObjectInt(this.rockDirection)));
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(10));
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) + var2 * 10));
            this.setBeenAttacked();

            if (var1.getEntity() instanceof EntityPlayer && ((EntityPlayer)var1.getEntity()).capabilities.isCreativeMode)
            {
                this.dataWatcher.updateObject(this.currentDamage, 100);
            }

            if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 2)
            {
                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                if (!this.worldObj.isRemote)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity(this);
                    }

                    this.dropLanderAsItem();
                }

                this.setDead();
            }

            return true;
        }
    }

    @Override
	public void setDead()
    {
        this.isDead = true;

//        if (this.moduleChest != null)
//        {
//        	this.moduleChest.setDead();
//        }
    }

    public void dropLanderAsItem()
    {
    	if (this.getItemsDropped() == null)
    	{
    		return;
    	}

        for(final ItemStack item : this.getItemsDropped())
        {
            this.entityDropItem(item, 0);
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();

//        if (this.moduleChest != null)
//        {
//            for (ItemStack stack : this.moduleChest.chestContents)
//            {
//            	if (stack != null)
//            	{
//                	items.add(stack);
//            	}
//            }
//        }

    	return items;
    }

	@Override
    public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i)
    {
		if (this.riddenByEntity != null)
		{
			if(this.riddenByEntity instanceof EntityPlayer && FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity))
			{
			}
			else
			{
	            this.boatPosRotationIncrements = i + 5;
		        this.boatX = d;
		        this.boatY = d1 + (this.riddenByEntity == null ? 1 : 0);
		        this.boatZ = d2;
		        this.boatYaw = f;
		        this.boatPitch = f1;
			}
		}
    }

    @Override
	public void onUpdate()
    {
        super.onUpdate();

        if(this.worldObj.isRemote && (this.riddenByEntity == null || !(this.riddenByEntity instanceof EntityPlayer) || !FMLClientHandler.instance().getClient().thePlayer.equals(this.riddenByEntity)))
        {
            double x;
            double y;
            double var12;
            double z;
            if (this.boatPosRotationIncrements > 0)
            {
                x = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                y = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                z = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw = (float)(this.rotationYaw + var12 / this.boatPosRotationIncrements);
                this.rotationPitch = (float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(x, y, z);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                x = this.posX + this.motionX;
                y = this.posY + this.motionY;
                z = this.posZ + this.motionZ;
        		if (this.riddenByEntity != null)
                this.setPosition(x, y, z);

                if (this.onGround)
                {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
            return;
        }

        if (this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) > 0)
        {
            this.dataWatcher.updateObject(this.timeSinceHit, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.timeSinceHit) - 1));
        }

        if (this.dataWatcher.getWatchableObjectInt(this.currentDamage) > 0)
        {
            this.dataWatcher.updateObject(this.currentDamage, Integer.valueOf(this.dataWatcher.getWatchableObjectInt(this.currentDamage) - 1));
        }

        final byte var20 = 5;
        int var4;

        for (var4 = 0; var4 < var20; ++var4)
        {
        }

        if (this.riddenByEntity == null)
        {
//        	this.yOffset = 5;
        }


        if (this.inWater && this.speed > 0.2D)
        {
            this.worldObj.playSoundEffect((float)this.posX, (float)this.posY, (float)this.posZ, "random.fizz", 0.5F, 2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F);
        }

        this.speed *= 0.98D;

        this.actualSpeed += this.speed * this.accel;

        this.actualSpeed = MathHelper.clamp_float((float) this.actualSpeed, -5.0F, -0.5F);

        this.motionY = this.actualSpeed;

		if (this.worldObj.isRemote)
		{
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
		}

//		if (this.moduleChest != null)
//		{
//            this.moduleChest.posX = this.posX;
//            this.moduleChest.posY = this.posY + 5 + this.yOffset;
//            this.moduleChest.posZ = this.posZ;
//            this.moduleChest.rotationPitch = this.rotationPitch;
//            this.moduleChest.rotationYaw = this.rotationYaw;
//		}
//		else if (this.moduleChest == null)
//		{
//			this.moduleChest = new GCCoreEntityLanderChest(this);
//	        this.worldObj.spawnEntityInWorld(this.moduleChest);
//		}

		// TODO Lander Explosions

//		if (this.worldObj.isRemote && this.onGround && !lastOnGround && this.lastMotionY < -0.7D)
//		{
//			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 17, new Object[] {this.entityId, 5.0F, this.posX, this.posY, this.posZ}));
//		}

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.lastOnGround = this.onGround;
        this.lastMotionY = this.motionY;

		if(this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToServer(GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}

		if(!this.worldObj.isRemote && this.ticksExisted % 5 == 0)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 50, this.dimension, GCCorePacketEntityUpdate.buildUpdatePacket(this));
		}
    }

    @Override
	protected void readEntityFromNBT(NBTTagCompound var1)
    {
        final NBTTagList var2 = var1.getTagList("Items");
        this.chestContents = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.chestContents.length)
            {
            	this.chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
    	final NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.chestContents.length; ++i)
        {
            if (this.chestContents[i] != null)
            {
                final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.chestContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getSizeInventory()
    {
        return this.chestContents.length;
    }

    public ItemStack getStackInSlot(int par1)
    {
        return this.chestContents[par1];
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.chestContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.chestContents[par1].stackSize <= par2)
            {
                itemstack = this.chestContents[par1];
                this.chestContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.chestContents[par1].splitStack(par2);

                if (this.chestContents[par1].stackSize == 0)
                {
                    this.chestContents[par1] = null;
                }

                this.onInventoryChanged();
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.chestContents[par1] != null)
        {
            final ItemStack itemstack = this.chestContents[par1];
            this.chestContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.chestContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    public String getInvName()
    {
        return "container.chest";
    }

    public boolean isInvNameLocalized()
    {
        return false;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return par1EntityPlayer.getDistanceSq(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D) <= 64.0D;
    }


    public void openChest()
    {
        if (this.numUsingPlayers < 0)
        {
            this.numUsingPlayers = 0;
        }

        ++this.numUsingPlayers;
    }

    public void closeChest()
    {
        --this.numUsingPlayers;
    }

    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

	@Override
	public void onInventoryChanged()
	{
	}

    @Override
	public boolean interact(EntityPlayer var1)
    {
        if (this.worldObj.isRemote)
        {
            return true;
        }
        else if (this.riddenByEntity == null && this.isCollidedVertically)
        {
        	var1.displayGUIChest(this);
            return true;
        }
        else
        {
            var1.mountEntity(this);
            return true;
        }
    }

	@Override
	public boolean pressKey(int key)
	{
//    	if(this.worldObj.isRemote || key == 5)
//    	{
//    		PacketDispatcher.sendPacketToServer(GCCorePacketControllableEntity.buildKeyPacket(key));
//    		return true;
//    	}
		switch(key)
		{
			case 0 : //Accelerate
			{
				if (!this.onGround)
				{
					this.rotationPitch -= 0.5F * this.turnFactor;
				}

				return true;
			}
			case 1 : //Deccelerate
			{
				if (!this.onGround)
				{
					this.rotationPitch += 0.5F * this.turnFactor;
				}

				return true;
			}
			case 2 : //Left
			{
				if (!this.onGround)
				{
					this.rotationYaw -= 0.5F * this.turnFactor;
				}

				return true;
			}
			case 3 : //Right
			{
				if (!this.onGround)
				{
					this.rotationYaw += 0.5F * this.turnFactor;
				}

				return true;
			}
			case 4 : //Space
			{
				this.speed += 0.05F;
				return true;
			}
			case 5 : //LShift
			{
				this.speed -= 0.005F;
				return true;
			}
		}

		return false;
	}

//	@Override
//	public void writeSpawnData(ByteArrayDataOutput data)
//	{
//		this.moduleChest = new GCCoreEntityLanderChest(this);
//        this.worldObj.spawnEntityInWorld(this.moduleChest);
//
//		data.writeInt(moduleChest.entityId);
//	}
//
//	@Override
//	public void readSpawnData(ByteArrayDataInput data)
//	{
//		FMLLog.info("done1");
//		if (this.worldObj instanceof WorldClient)
//		{
//			FMLLog.info("done");
//			int entityID = data.readInt();
//			this.moduleChest = new GCCoreEntityLanderChest(this);
//			moduleChest.entityId = entityID;
//	        ((WorldClient)this.worldObj).addEntityToWorld(entityID, moduleChest);
//		}
//	}
}
