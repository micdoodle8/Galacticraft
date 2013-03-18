package micdoodle8.mods.galacticraft.core.entities;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.API.IOrbitDimension;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPad;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketDimensionList;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntitySpaceshipBase extends Entity implements ISpaceship
{
	protected long ticks = 0;
	
    protected double dragAir;

    protected int ignite;
    public int timeUntilLaunch;
    public boolean launched;

    public float timeSinceLaunch;
    
    public int fuel;
    
    private GCCoreTileEntityLandingPad landingPad;

    public float rumble;

    public EntitySpaceshipBase(World par1World)
    {
        super(par1World);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
        this.ignoreFrustumCheck = true;
    }
    
    public void setLandingPad(GCCoreTileEntityLandingPad pad)
    {
    	this.landingPad = pad;
    }
    
    public GCCoreTileEntityLandingPad getLandingPad()
    {
    	return this.landingPad;
    }
    
    public abstract int getMaxFuel();

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
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.isEntityInvulnerable() || this.posY > 300)
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + par2 * 10);

                if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode)
                {
                    this.setDamage(100);
                }

                if (this.getDamage() > 90 && !this.worldObj.isRemote)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity(this);
                    }

                    this.setDead();
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

    public void dropShipAsItem()
    {
    	if (this.getItemsDropped() == null || this.worldObj.isRemote)
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
    	return null;
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
		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;
		
    	super.onUpdate();
    	
    	if (!this.worldObj.isRemote && this.ticks % 20 == 0 && this.getLandingPad() != null && this.getLandingPad().connectedTiles != null)
    	{
    		for (TileEntity tile : this.getLandingPad().connectedTiles)
    		{
    			if (this.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) == null || !(this.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) instanceof GCCoreTileEntityFuelLoader))
    			{
    				
    			}
    			else
    			{
        			if (tile instanceof GCCoreTileEntityFuelLoader && ((GCCoreTileEntityFuelLoader) tile).wattsReceived > 0)
        			{
        				GCCoreTileEntityFuelLoader loader = (GCCoreTileEntityFuelLoader) tile;
        				
        				if (!this.launched && loader.getStackInSlot(1) != null)
        				{
        					if (this.fuel < this.getMaxFuel())
        					{
        						loader.transferFuelToSpaceship(this);
        					}
        				}
        				else if (this.launched)
        				{
        					this.setLandingPad(null);
        				}
        			}
    			}
    		}
    	}

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

        if (this.posY < -64.0D || this.posY > this.getYCoordToTeleport() + 10)
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

        box = this.boundingBox.expand(0.2D, 0.2D, 0.2D);

        final List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var52 = 0; var52 < var15.size(); ++var52)
            {
                final Entity var17 = (Entity)var15.get(var52);

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
        	this.onLaunch();

        	if (!this.worldObj.isRemote)
        	{
        		if (!(this.worldObj.provider instanceof IOrbitDimension))
        		{
        	        ((GCCorePlayerBase) this.riddenByEntity).coordsTeleportedFromX = this.riddenByEntity.posX;
        	        ((GCCorePlayerBase) this.riddenByEntity).coordsTeleportedFromZ = this.riddenByEntity.posZ;
        		}
        		
        		int amountRemoved = 0;

        		for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
        		{
            		for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
            		{
                		for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                		{
                			final int id = this.worldObj.getBlockId(x, y, z);
                			final Block block = Block.blocksList[id];
                			
                			if (block != null && block instanceof GCCoreBlockLandingPadFull)
                			{
                				if (amountRemoved < 9)
                				{
                    				this.worldObj.func_94571_i(x, y, z);
                    				amountRemoved = 9;
                				}
                			}

                			if (block != null && block instanceof GCCoreBlockLandingPad)
                			{
                    			if (amountRemoved < 9)
                    			{
                    				this.worldObj.func_94571_i(x, y, z);
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

        if (this.rotationPitch > 90)
        {
        	this.rotationPitch = 90;
        }

        if (this.rotationPitch < -90)
        {
        	this.rotationPitch = -90;
        }

        this.motionX = -(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
        this.motionZ = -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));

        if (this.timeSinceLaunch > 50 && this.onGround)
        {
        	this.failRocket();
        }

        if (this.getLaunched() != 1)
        {
        	this.motionX = this.motionY = this.motionZ = 0.0F;
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

		if (!this.worldObj.isRemote && this.ticks % 3 == 0)
		{
			Packet packet = this.getDescriptionPacket();
			
			if (packet != null)
			{
				PacketDispatcher.sendPacketToAllPlayers(packet);
			}
		}
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
            this.riddenByEntity.attackEntityFrom(GalacticraftCore.spaceshipCrash, (int)(4.0D * 20 + 1.0D));
    	}

    	if (!GCCoreConfigManager.disableSpaceshipGrief)
    	{
      		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
    	}

  		this.setDead();
    }

    @Override
	@SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
//    	super.setPositionAndRotation2(par1, par3, par5, par7, par8, par9);
    	this.setRotation(par7, par8);
    }

    @Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	par1NBTTagCompound.setBoolean("launched", this.launched);
    	par1NBTTagCompound.setInteger("timeUntilLaunch", this.timeUntilLaunch);
    	par1NBTTagCompound.setInteger("ignite", this.ignite);
    	par1NBTTagCompound.setInteger("fuel", this.fuel);
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
		this.fuel = par1NBTTagCompound.getInteger("fuel");
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
            	((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 8, toSend));
            }
            else if (par1EntityPlayer instanceof EntityPlayerMP)
            {
        	  	final Object[] toSend = {par1EntityPlayer.username};
            	((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 13, toSend));
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
		        GCCorePlayerBase playerBase = PlayerUtil.getPlayerBaseServerFromPlayer((EntityPlayerMP)this.riddenByEntity);
		        
		        if (playerBase != null && playerBase.spaceStationDimensionID == -1)
		        {
		        	WorldUtil.bindSpaceStationToNewDimension(this.worldObj, playerBase);
		        }
		        else if (playerBase != null)
		        {
		        	WorldUtil.createSpaceStation(this.worldObj, playerBase.spaceStationDimensionID, playerBase);
		        }
		        
        		final EntityPlayerMP entityplayermp = (EntityPlayerMP)this.riddenByEntity;

				final Integer[] ids = DimensionManager.getStaticDimensionIDs();

		    	final Set set = WorldUtil.getArrayOfPossibleDimensions(ids, playerBase).entrySet();
		    	final Iterator i = set.iterator();

		    	String temp = "";

		    	for (int k = 0; i.hasNext(); k++)
		    	{
		    		final Map.Entry entry = (Map.Entry)i.next();
		    		temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
		    	}

		    	final Object[] toSend = {entityplayermp.username, temp};
		        FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(entityplayermp.username).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 2, toSend));

		        if (playerBase != null)
		        {
		        	playerBase.setUsingPlanetGui();
		        }

		        this.onTeleport(entityplayermp);

				if (this.riddenByEntity != null)
				{
            		this.riddenByEntity.mountEntity(this);
				}
            }
    	}
    }
    
    public void checkValidLaunchPadBroken()
    {
		int amountStillValid = 0;

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
        				amountStillValid++;
        			}
        		}
    		}
		}
		
		if (amountStillValid < 9 && !this.launched && !this.isDead)
		{
            this.setDead();
			this.dropShipAsItem();
		}
    }

	public Packet getDescriptionPacket()
	{
		Object[] toSend = {this.fuel};
		return PacketUtil.createPacket(GalacticraftCore.CHANNEL, 15, toSend);
	}
    
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, DataInputStream data)
    {
		try
		{
			if (this.worldObj.isRemote)
			{
				this.fuel = data.readInt();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

    public void onLaunch() {}

    public void onTeleport(EntityPlayerMP player) {}

	@SideOnly(Side.CLIENT)
	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {}
}
