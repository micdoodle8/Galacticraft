package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.entity.ITelemetry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenText;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntitySpaceshipBase extends Entity implements IPacketReceiver, IIgnoreShift, ITelemetry
{
    public static enum EnumLaunchPhase
    {
        UNIGNITED,
        IGNITED,
        LAUNCHED
    }

    public int launchPhase;

    protected long ticks = 0;
    protected double dragAir;
    public int timeUntilLaunch;
    public float timeSinceLaunch;
    public float rollAmplitude;
    public float shipDamage; 
    private ArrayList<BlockVec3Dim> telemetryList = new ArrayList<BlockVec3Dim>();
    private boolean addToTelemetry = false;
    public FluidTank fuelTank = new FluidTank(this.getFuelTankCapacity() * ConfigManagerCore.rocketFuelFactor);
    
    public EntitySpaceshipBase(World par1World)
    {
        super(par1World);
        this.launchPhase = EnumLaunchPhase.UNIGNITED.ordinal();
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 5.0;
        return distance < d0 * d0;
    }

    public abstract int getFuelTankCapacity();

    public abstract int getMaxFuel();

    public abstract int getScaledFuelLevel(int i);

    public abstract int getPreLaunchWait();

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return null;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (!this.world.isRemote && !this.isDead)
        {
			boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;
        	Entity e = par1DamageSource.getEntity(); 
            if (this.isEntityInvulnerable(par1DamageSource) || this.posY > 300 || (e instanceof EntityLivingBase && !(e instanceof EntityPlayer)))
            {
                return false;
            }
            else
            {
                this.rollAmplitude = 10;
                this.setBeenAttacked();
                this.shipDamage += par2 * 10;

                if (e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)
                {
                    this.shipDamage = 100;
                }

                if (flag || this.shipDamage > 90 && !this.world.isRemote)
                {
                    this.removePassengers();

					if (flag)
					{
						this.setDead();
					}
					else
					{
						this.setDead();
						this.dropShipAsItem();
					}
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
        if (this.world.isRemote)
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

    public abstract List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList);

    @Override
    public void performHurtAnimation()
    {
        this.rollAmplitude = 5;
        this.shipDamage += this.shipDamage * 10;
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
        this.ticks++;

        super.onUpdate();

        if (this.addToTelemetry)
        {
        	this.addToTelemetry = false;
			for (BlockVec3Dim vec : new ArrayList<BlockVec3Dim>(this.telemetryList))
			{
				TileEntity t1 = vec.getTileEntityNoLoad();
				if (t1 instanceof TileEntityTelemetry && !t1.isInvalid())
				{
					if (((TileEntityTelemetry)t1).linkedEntity == this)
						((TileEntityTelemetry)t1).addTrackedEntity(this);
				}		
			}
        }

        for (Entity e : this.getPassengers())
        {
            e.fallDistance = 0.0F;
        }

        if (this.posY > (this.world.provider instanceof IExitHeight ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 1200))
        {
            this.onReachAtmosphere();
//            if (this.world.isRemote)
//            	this.posY = 1 + (this.world.provider instanceof IExitHeight ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 1200);
        }

        if (this.rollAmplitude > 0)
        {
            this.rollAmplitude--;
        }

        if (this.shipDamage > 0)
        {
            this.shipDamage--;
        }

        if (!this.world.isRemote)
        {
	        if (this.posY < 0.0D)
	        {
	            this.kill();
	        }
	        else if (this.posY > (this.world.provider instanceof IExitHeight ? ((IExitHeight) this.world.provider).getYCoordinateToTeleport() : 1200) + 100)
	        {
                for (Entity e : this.getPassengers())
                {
                    if (e instanceof EntityPlayerMP)
                    {
                        GCPlayerStats stats = GCPlayerStats.get(e);
                        if (stats.isUsingPlanetSelectionGui())
                        {
                            this.kill();
                        }
                    }
                    else
                        this.kill();
                }

                if (this.timeSinceLaunch > 50 && this.onGround)
                {
                    this.failRocket();
                }
	        }
        }
        
        if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal())
        {
            this.timeUntilLaunch = this.getPreLaunchWait();
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.timeSinceLaunch++;
        }
        else
        {
            this.timeSinceLaunch = 0;
        }

        if (this.timeUntilLaunch > 0 && this.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
        {
            this.timeUntilLaunch--;
        }

        AxisAlignedBB box = this.getEntityBoundingBox().expand(0.2D, 0.2D, 0.2D);

        final List<?> var15 = this.world.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var52 = 0; var52 < var15.size(); ++var52)
            {
                final Entity var17 = (Entity) var15.get(var52);

                if (this.getPassengers().contains(var17))
                {
                    var17.applyEntityCollision(this);
                }
            }
        }

        if (this.timeUntilLaunch == 0 && this.launchPhase == EnumLaunchPhase.IGNITED.ordinal())
        {
            this.setLaunchPhase(EnumLaunchPhase.LAUNCHED);
            this.onLaunch();
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

        if (this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.motionX = this.motionY = this.motionZ = 0.0F;
        }

        if (this.world.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);

            if (this.shouldMoveClientSide())
            {
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            }
        }
        else
        {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }

        this.setRotation(this.rotationYaw, this.rotationPitch);

        if (this.world.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.world.isRemote && this.ticks % 3 == 0)
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.world.provider.getDimension());
            // PacketDispatcher.sendPacketToAllInDimension(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
            // this, this.getNetworkedData(new ArrayList())),
            // this.world.provider.getDimension());
        }
    }

    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (!this.world.isRemote)
        {
            new Exception().printStackTrace();
        }
        this.setLaunchPhase(EnumLaunchPhase.values()[buffer.readInt()]);
        this.timeSinceLaunch = buffer.readFloat();
        this.timeUntilLaunch = buffer.readInt();
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.world.isRemote)
        {
            return;
        }
        list.add(this.launchPhase);
        list.add(this.timeSinceLaunch);
        list.add(this.timeUntilLaunch);
    }

    public void turnYaw(float f)
    {
        this.rotationYaw += f;
    }

    public void turnPitch(float f)
    {
        this.rotationPitch += f;
    }

    protected void failRocket()
    {
        for (Entity passenger : this.getPassengers())
        {
            passenger.attackEntityFrom(DamageSourceGC.spaceshipCrash, (int) (4.0D * 20 + 1.0D));
        }

        if (!ConfigManagerCore.disableSpaceshipGrief)
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
        }

        this.setDead();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setRotation(yaw, pitch);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("launchPhase", this.launchPhase + 1);
        nbt.setInteger("timeUntilLaunch", this.timeUntilLaunch);
        if (telemetryList.size() > 0)
        {
            NBTTagList teleNBTList = new NBTTagList();
            for (BlockVec3Dim vec : new ArrayList<BlockVec3Dim>(this.telemetryList))
            {
                NBTTagCompound tag = new NBTTagCompound();
                vec.writeToNBT(tag);
                teleNBTList.appendTag(tag);
            }
            nbt.setTag("telemetryList", teleNBTList);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.timeUntilLaunch = nbt.getInteger("timeUntilLaunch");

        boolean hasOldTags = false;

        // Backwards compatibility:
        if (nbt.getKeySet().contains("launched"))
        {
            hasOldTags = true;

            boolean launched = nbt.getBoolean("launched");

            if (launched)
            {
                this.setLaunchPhase(EnumLaunchPhase.LAUNCHED);
            }
        }

        // Backwards compatibility:
        if (nbt.getKeySet().contains("ignite"))
        {
            hasOldTags = true;

            int ignite = nbt.getInteger("ignite");

            if (ignite == 1)
            {
                this.setLaunchPhase(EnumLaunchPhase.IGNITED);
            }
        }

        // Backwards compatibility:
        if (hasOldTags)
        {
            if (this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal() && this.launchPhase != EnumLaunchPhase.IGNITED.ordinal())
            {
                this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
            }
        }
        else
        {
            this.setLaunchPhase(EnumLaunchPhase.values()[nbt.getInteger("launchPhase") - 1]);
        }

        //Update all Telemetry Units which are still tracking this rocket
        this.telemetryList.clear();
        if (nbt.hasKey("telemetryList"))
        {
            NBTTagList teleNBT = nbt.getTagList("telemetryList", 10);
            if (teleNBT.tagCount() > 0)
            {
                for (int j = teleNBT.tagCount() - 1; j >= 0; j--)
                {
                    NBTTagCompound tag1 = teleNBT.getCompoundTagAt(j);
                    if (tag1 != null)
                    {
                        this.telemetryList.add(BlockVec3Dim.readFromNBT(tag1));
                    }
                }
                this.addToTelemetry = true;
            }
        }
    }

    public boolean getLaunched()
    {
        return this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal();
    }

    public boolean canBeRidden()
    {
        return false;
    }

    public void ignite()
    {
        this.setLaunchPhase(EnumLaunchPhase.IGNITED);
    }

    @Override
    public double getMountedYOffset()
    {
        return -0.9D;
    }
    
    public double getOnPadYOffset()
    {
    	return 0D;
    }

    public void onLaunch()
    {
        MinecraftForge.EVENT_BUS.post(new RocketLaunchEvent(this));
    }

    public void onReachAtmosphere()
    {
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
    }

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    public ResourceLocation getSpaceshipGui()
    {
        return GalacticraftRegistry.getResouceLocationForDimension(this.world.provider.getClass());
    }

    public void setLaunchPhase(EnumLaunchPhase phase)
    {
        this.launchPhase = phase.ordinal();
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal();
    }

    public static class RocketLaunchEvent extends EntityEvent
    {
        public final EntitySpaceshipBase rocket;

        public RocketLaunchEvent(EntitySpaceshipBase entity)
        {
            super(entity);
            rocket = entity;
        }
    }

	public void addTelemetry(TileEntityTelemetry tile)
	{
		this.telemetryList.add(new BlockVec3Dim(tile));
	}

	public ArrayList<TileEntityTelemetry> getTelemetry()
	{
		ArrayList<TileEntityTelemetry> returnList = new ArrayList<TileEntityTelemetry>();
		for (BlockVec3Dim vec : new ArrayList<BlockVec3Dim>(this.telemetryList))
		{
			TileEntity t1 = vec.getTileEntity();
			if (t1 instanceof TileEntityTelemetry && !t1.isInvalid())
			{
				if (((TileEntityTelemetry)t1).linkedEntity == this)
					returnList.add((TileEntityTelemetry)t1);
			}		
		}
		return returnList;
	}
	
    @Override
    public void transmitData(int[] data)
    {
		data[0] = this.timeUntilLaunch;
		data[1] = (int) this.posY;
		//data[2] is entity speed already set as default by TileEntityTelemetry
		data[3] = this.getScaledFuelLevel(100);
		data[4] = (int) this.rotationPitch;
    }
	
    @Override
    public void receiveData(int[] data, String[] str)
    {
		//Spaceships:
		//  data0 = launch countdown
		//  data1 = height
		//  data2 = speed
		//  data3 = fuel remaining
		//  data4 = pitch angle
		int countdown = data[0];
		str[0] = "";
		str[1] = (countdown == 400) ? GCCoreUtil.translate("gui.rocket.on_launchpad") : ((countdown > 0) ? GCCoreUtil.translate("gui.rocket.countdown") + ": " + countdown / 20 : GCCoreUtil.translate("gui.rocket.launched"));
		str[2] = GCCoreUtil.translate("gui.rocket.height") + ": " + data[1];
		str[3] = GameScreenText.makeSpeedString(data[2]);
		str[4] = GCCoreUtil.translate("gui.message.fuel.name") + ": " + data[3] + "%";
    }

    @Override
    public void adjustDisplay(int[] data)
    {
		GL11.glRotatef(data[4], -1, 0, 0);
		GL11.glTranslatef(0, this.height / 4, 0);
    }

    /**
     * Used in RenderTier1Rocket for standard rendering. Ignore if using a custom renderer.
     *
     * @return Y-axis offset for rendering at correct position
     */
    public float getRenderOffsetY()
    {
        return 1.34F;
    }

    public static final Predicate<Entity> rocketSelector = new Predicate<Entity>()
    {
        @Override
        public boolean apply(Entity e)
        {
            return e instanceof EntitySpaceshipBase && e.isEntityAlive();
        }
    };
}