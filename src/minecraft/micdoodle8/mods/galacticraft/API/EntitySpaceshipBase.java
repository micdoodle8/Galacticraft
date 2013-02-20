package micdoodle8.mods.galacticraft.API;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPad;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntitySpaceshipBase extends Entity implements ISpaceship
{
    protected double dragAir;
    
    protected int ignite;
    public int timeUntilLaunch;
    public boolean launched;
    
    public float timeSinceLaunch;
    
    public float rumble;

    public EntitySpaceshipBase(World par1World)
    {
        super(par1World);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
        this.ignoreFrustumCheck = true;
    }

    @Override
	protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
	protected void entityInit()
    {
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Integer(0));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(0));
        this.dataWatcher.addObject(22, new Integer(0));
        this.dataWatcher.addObject(23, new Integer(0));
        this.dataWatcher.addObject(24, new Integer(0));
    }

    @Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    @Override
	public AxisAlignedBB getBoundingBox()
    {
//        return this.boundingBox;
    	return null;
    }

    @Override
	public boolean canBePushed()
    {
        return false;
    }

    @Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        return true;
    }

    @Override
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(5);
        this.setDamage(this.getDamage() + this.getDamage() * 10);
    }

    @Override
	public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
    @Override
	public boolean shouldRiderSit()
    {
        return false;
    }
    
    @Override
	public void onUpdate()
    {
    	super.onUpdate();

    	if (this.rumble > 0)
    	{
    		this.rumble--;
    	}
    	
    	if (this.rumble < 0)
    	{
    		this.rumble++;
    	}
    	
    	if (this.riddenByEntity != null)
    	{
    		this.riddenByEntity.posX += this.rumble / 30F;
    		this.riddenByEntity.posZ += this.rumble / 30F;
    		
    		final EntityPlayer player = (EntityPlayer) this.riddenByEntity;
    	}
    	
    	if (this.getReversed() == 1)
    	{
    		this.rotationPitch = 180F;
    	}
    	
    	if (this.posY > this.getYCoordToTeleport())
    	{
    		this.teleport();
    	}
    	
    	if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0)
        {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D || this.posY > 500D && this.dataWatcher.getWatchableObjectInt(21) == 0)
        {
            this.kill();
        }
        
        if (this.ignite == 0)
        {
        	this.timeUntilLaunch = this.getPreLaunchWait();
        }
        
        if (this.launched)
        {
        	this.timeSinceLaunch++;
        }
        else
        {
        	this.timeSinceLaunch = 0;
        }
        
        if (!this.worldObj.isRemote)
        {
            this.setTimeSinceLaunch((int)this.timeSinceLaunch);
        }
        
        if (this.timeUntilLaunch > 0 && this.ignite == 1)
        {
        	this.timeUntilLaunch--;
        }

        AxisAlignedBB box = null;
        
        box = boundingBox.expand(0.2D, 0.2D, 0.2D);

        List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var52 = 0; var52 < var15.size(); ++var52)
            {
                Entity var17 = (Entity)var15.get(var52);

                if (var17 != this.riddenByEntity)
                {
                    var17.applyEntityCollision(this);
                }
            }
        }
        
        this.setTimeUntilLaunch(this.timeUntilLaunch);
        
        if (this.timeUntilLaunch == 0 && this.ignite == 1 || this.getReversed() == 1)
        {
        	this.launched = true;
        	this.setLaunched(1);
        	this.ignite = 0;
        	
        	if (!this.worldObj.isRemote)
        	{
        		int amountRemoved = 0;
        		
        		for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
        		{
            		for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
            		{
                		for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                		{
                			final int id = this.worldObj.getBlockId(x, y, z);
                			final Block block = Block.blocksList[id];
                			
                			if (block != null && block instanceof GCCoreBlockLandingPad)
                			{
                    			if (amountRemoved < 9);
                    			{
                    				this.worldObj.setBlock(x, y, z, 0);
                    				amountRemoved++;
                    			}
                			}
                		}
            		}
        		}
        		
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        	}
        }
        
        if (this.ignite == 1 || this.launched)
        {
            this.performHurtAnimation();
            
        	this.rumble = (float) this.rand.nextInt(3) - 3;
        }
        
        this.motionX = -(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
        this.motionZ = -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
        
        if (this.timeSinceLaunch > 50 && this.onGround)
        {
        	this.failRocket();
        }
        
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        
        this.setRotation(this.rotationYaw, this.rotationPitch);

        if (this.worldObj.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }
    
    public void turnYaw (float f)
    {
		this.rotationYaw += f;
    }
    
    public void turnPitch (float f)
    {
		this.rotationPitch += f;
    }
    
    private void failRocket()
    {
    	if (this.riddenByEntity != null)
    	{
            final double var13 = this.riddenByEntity.getDistance(this.posX, this.posY, this.posZ) / 20;
    		this.riddenByEntity.attackEntityFrom(GCCoreDamageSource.spaceshipExplosion, (int)(4.0D * 20 + 1.0D));
    	}
        
  		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
  		
  		this.setDead();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    	this.setRotation(par7, par8);
    }
    
    @Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setBoolean("launched", this.launched);
    	par1NBTTagCompound.setInteger("timeUntilLaunch", this.timeUntilLaunch);
    	par1NBTTagCompound.setInteger("ignite", this.ignite);
    }

    @Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
		this.launched = par1NBTTagCompound.getBoolean("launched");
		if (par1NBTTagCompound.getBoolean("launched"))
		{
			this.setLaunched(1);
		}
		else
		{
			this.setLaunched(0);
		}
		this.timeUntilLaunch = par1NBTTagCompound.getInteger("timeUntilLaunch");
		this.ignite = par1NBTTagCompound.getInteger("ignite");
    }

    @Override
	public boolean interact(EntityPlayer par1EntityPlayer)
    {
    	if (!this.worldObj.isRemote)
    	{
        	par1EntityPlayer.mountEntity(this);
            
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
            {
        	  	final Object[] toSend = {((EntityPlayerMP)this.riddenByEntity).username};
            	((EntityPlayerMP)this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 8, toSend));
            }
            
        	return true;
    	}
        return false;
    }

    public boolean canBeRidden()
    {
        return false;
    }
    
    public void setDamage(int par1)
    {
        this.dataWatcher.updateObject(19, Integer.valueOf(par1));
    }

    public int getDamage()
    {
        return this.dataWatcher.getWatchableObjectInt(19);
    }

    public void setRollingAmplitude(int par1)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(par1));
    }

    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setRollingDirection(int par1)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(par1));
    }

    public int getRollingDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setFailedLaunch(int par1)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(par1));
    }

    public int getFailedLaunch()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    public int getReversed()
    {
        return this.dataWatcher.getWatchableObjectInt(21);
    }
    
    public void setLaunched(int par1)
    {
    	this.dataWatcher.updateObject(22, par1);
    }
    
    public int getLaunched()
    {
    	return this.dataWatcher.getWatchableObjectInt(22);
    }
    
    public void setTimeUntilLaunch(int par1)
    {
    	if (!this.worldObj.isRemote)
    	{
        	this.dataWatcher.updateObject(23, par1);
    	}
    }
    
    public int getTimeUntilLaunch()
    {
    	return this.dataWatcher.getWatchableObjectInt(23);
    }
    
    public void setTimeSinceLaunch(int par1)
    {
    	this.dataWatcher.updateObject(24, par1);
    }
    
    public int getTimeSinceLaunch()
    {
    	return this.dataWatcher.getWatchableObjectInt(24);
    }
    
    public void ignite()
    {
    	this.ignite = 1;
    }
    
    @Override
	public double getMountedYOffset()
    {
        return -1D;
    }
    
    public void teleport()
    {
    	if (this.riddenByEntity != null)
    	{
    		if (this.riddenByEntity instanceof EntityPlayerMP)
            {
        		final EntityPlayerMP entityplayermp = (EntityPlayerMP)this.riddenByEntity;
        		
				final Integer[] ids = DimensionManager.getStaticDimensionIDs();
		    	
		    	final Set set = GCCoreUtil.getArrayOfPossibleDimensions(ids).entrySet();
		    	final Iterator i = set.iterator();
		    	
		    	String temp = "";
		    	
		    	for (int k = 0; i.hasNext(); k++)
		    	{
		    		final Map.Entry entry = (Map.Entry)i.next();
		    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
		    	}
		    	
		    	final Object[] toSend = {entityplayermp.username, temp};
		        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(entityplayermp.username).playerNetServerHandler.sendPacketToPlayer(GCCoreUtil.createPacket("Galacticraft", 2, toSend));
				
		        GCCoreUtil.getPlayerBaseServerFromPlayer(entityplayermp).setUsingPlanetGui();
		        
		        this.onTeleport(entityplayermp);
		        
				if (this.riddenByEntity != null)
				{
            		this.riddenByEntity.mountEntity(this);
				}
            }
    	}
    }
    
    public void onLaunch() {}
    
    public void onTeleport(EntityPlayerMP player) {}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {}
}
