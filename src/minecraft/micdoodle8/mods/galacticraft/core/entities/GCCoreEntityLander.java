package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLanderFlameFX;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketEntityUpdate;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreEntityLander extends GCCoreEntityControllable implements IInventory
{
    public int currentDamage;
    public int timeSinceHit;
    public int rockDirection;
    public double ySpeed;
    public double startingYSpeed;
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
    public GCCorePlayerMP playerSpawnedIn;
    private final float MAX_PITCH_ROTATION = 20.0F;

    public GCCoreEntityLander(World var1)
    {
        super(var1);
        this.setSize(3.0F, 4.5F);
        this.yOffset = 2.5F;
        this.currentDamage = 18;
        this.timeSinceHit = 19;
        this.rockDirection = 20;
        this.ySpeed = 0.0D;
        this.startingYSpeed = -5.0D;
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

    public GCCoreEntityLander(GCCorePlayerMP player)
    {
    	this(player.worldObj, player.posX, player.posY, player.posZ);
    	this.playerSpawnedIn = player;
    	this.chestContents = player.rocketStacks;
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
	public void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround)
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
			if (onGround)
				this.onGround = onGround;
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
        if (this.isDead || var1.equals(DamageSource.cactus) || !this.onGround)
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
                	if (this.riddenByEntity instanceof EntityPlayerMP)
                	{
                	  	final Object[] toSend2 = {0};
                    	((EntityPlayerMP) riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
                    }
                	
                    this.riddenByEntity.mountEntity(this);
                    
                    return false;
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

    public void dropLanderAsItem()
    {
    	if (this.getItemsDropped() == null)
    	{
    		return;
    	}

        for(final ItemStack item : this.getItemsDropped())
        {
        	if (item != null)
        	{
                this.entityDropItem(item, 0);
        	}
        }
    }

    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        
        for (ItemStack stack : this.chestContents)
        {
        	items.add(stack);
        }

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
        
        if (!this.worldObj.isRemote && this.ticksExisted < 20 && this.playerSpawnedIn != null && this.riddenByEntity == null)
        {
        	this.playerSpawnedIn.mountEntity(this);
        }

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
        
    	if (this.motionY != 0 && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
    	{
        	final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        	final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        	double y1 = -5.0D;
        	
        	final double y = this.prevPosY + (this.posY - this.prevPosY);
        	
        	Vector3 thisVec = new Vector3(this);

    		Map<Vector3, Vector3> particleMap = new HashMap<Vector3, Vector3>();
    		float angle1 = (float) ((this.rotationYaw - 40.0F) * Math.PI / 180.0F);
    		float angle2 = (float) ((this.rotationYaw + 40.0F) * Math.PI / 180.0F);
    		float angle3 = (float) ((this.rotationYaw + 180 - 40.0F) * Math.PI / 180.0F);
    		float angle4 = (float) ((this.rotationYaw + 180 + 40.0F) * Math.PI / 180.0F);
    		float pitch = (float) Math.sin(this.rotationPitch * Math.PI / 180.0F);
    		particleMap.put((new Vector3(this)).add(new Vector3(0.4 * Math.cos(angle1) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle1) * Math.cos(pitch))), new Vector3(x1, y1, z1));
    		particleMap.put((new Vector3(this)).add(new Vector3(0.4 * Math.cos(angle2) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle2) * Math.cos(pitch))), new Vector3(x1, y1, z1));
    		particleMap.put((new Vector3(this)).add(new Vector3(0.4 * Math.cos(angle3) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle3) * Math.cos(pitch))), new Vector3(x1, y1, z1));
    		particleMap.put((new Vector3(this)).add(new Vector3(0.4 * Math.cos(angle4) * Math.cos(pitch), -1.5, 0.4 * Math.sin(angle4) * Math.cos(pitch))), new Vector3(x1, y1, z1));
    		this.spawnParticles(particleMap);
    	}
        
        if (this.worldObj.isRemote)
        {
        	if (this.onGround)
        	{
        		this.rotationPitch = 0.0F;
        	}
        	else
        	{
        		if (this.rotationPitch >= MAX_PITCH_ROTATION)
        		{
        			this.rotationPitch = MAX_PITCH_ROTATION;
        		}
        		else if (this.rotationPitch <= -MAX_PITCH_ROTATION)
        		{
        			this.rotationPitch = -MAX_PITCH_ROTATION;
        		}
        	}
        }

        this.ySpeed *= 0.98D;

        this.startingYSpeed += this.ySpeed * this.accel;

        this.startingYSpeed = this.startingYSpeed < -5.0F ? -5.0F : (this.startingYSpeed > -0.5F ? -0.5F : this.startingYSpeed);

        if (this.worldObj.isRemote)
        {
            this.motionY = this.startingYSpeed;
        }

        this.motionX = -(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
        this.motionZ = -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));

		if (this.worldObj.isRemote)
		{
			if (!this.onGround)
			{
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
			}
			else
			{
				this.moveEntity(0.0D, 0.0D, 0.0D);
			}
		}
		
		if (!this.worldObj.isRemote)
		{
		}
		
		if (!this.worldObj.isRemote)
		{
			if (this.onGround && !this.lastOnGround && Math.abs(this.lastMotionY) > 2.0D)
			{
				if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
				{
            	  	final Object[] toSend2 = {0};
                	((EntityPlayerMP) riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
                	
					this.riddenByEntity.mountEntity(this);
				}
				
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 12, true);
				
				this.setDead();
			}
		}

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
    
    public void spawnParticles(Map<Vector3, Vector3> points)
    {
    	for (Entry<Vector3, Vector3> vec : points.entrySet())
    	{
    		Vector3 posVec = vec.getKey();
    		Vector3 motionVec = vec.getValue();
    		
    		this.spawnParticle("launchflame", posVec.x, posVec.y, posVec.z, motionVec.x, motionVec.y, motionVec.z, true);
    	}
    }

	@SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
	{
		this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
	}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double var16 = mc.renderViewEntity.posX - var2;
            final double var17 = mc.renderViewEntity.posY - var4;
            final double var19 = mc.renderViewEntity.posZ - var6;
            Object var21 = null;
            final double var22 = 64.0D;

            if (var1.equals("launchflame"))
            {
        		final EntityFX fx = new GCCoreEntityLanderFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12);
        		if (fx != null)
        		{
        			mc.effectRenderer.addEffect(fx);
        		}
            }
        }
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
        else if (this.riddenByEntity == null && this.onGround)
        {
        	var1.displayGUIChest(this);
            return true;
        }
        else if (var1 instanceof EntityPlayerMP)
        {
    	  	final Object[] toSend2 = {0};
        	((EntityPlayerMP) var1).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
            var1.mountEntity(this);
            return true;
        }
        else
        {
        	return true;
        }
    }

	@Override
	public boolean pressKey(int key)
	{
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
				this.ySpeed += 0.05F;
				return true;
			}
			case 5 : //LShift
			{
				this.ySpeed -= 0.005F;
				return true;
			}
		}

		return false;
	}
}
