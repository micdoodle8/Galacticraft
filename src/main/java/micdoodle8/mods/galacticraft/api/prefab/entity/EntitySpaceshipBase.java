package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.entity.IIgnoreShift;
import micdoodle8.mods.galacticraft.api.entity.ITelemetry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.advancement.GCTriggers;
import micdoodle8.mods.galacticraft.core.client.screen.GameScreenText;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;

import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntitySpaceshipBase extends Entity implements IPacketReceiver, IIgnoreShift, ITelemetry
{
    public enum EnumLaunchPhase
    {
        UNIGNITED,
        IGNITED,
        LAUNCHED,
        LANDING
    }

    public int launchPhase;

    protected long ticks = 0;
    protected double dragAir;
    public int timeUntilLaunch;
    public float timeSinceLaunch;
    public float rollAmplitude;
    public float shipDamage;
    private final ArrayList<BlockVec3Dim> telemetryList = new ArrayList<BlockVec3Dim>();
    private boolean addToTelemetry = false;
    public FluidTank fuelTank = new FluidTank(this.getFuelTankCapacity());
    private double syncAdjustX = 0D;
    private double syncAdjustY = 0D;
    private double syncAdjustZ = 0D;
    private boolean syncAdjustFlag = false;
    public static final Predicate<Entity> ROCKET_SELECTOR = e -> e instanceof EntitySpaceshipBase && e.isAlive();

    public EntitySpaceshipBase(EntityType<? extends EntitySpaceshipBase> type, World worldIn)
    {
        super(type, worldIn);
        this.launchPhase = EnumLaunchPhase.UNIGNITED.ordinal();
        this.preventEntitySpawning = true;
        this.ignoreFrustumCheck = true;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getBoundingBox().getAverageEdgeLength();

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
        if (!this.world.isRemote && this.isAlive())
        {
            Entity e = par1DamageSource.getTrueSource();
            boolean flag = e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode;
            if (this.isInvulnerableTo(par1DamageSource) || this.getPosY() > 300 || (e instanceof LivingEntity && !(e instanceof PlayerEntity)))
            {
                return false;
            }
            else
            {
                this.rollAmplitude = 10;
                this.markVelocityChanged();
                this.shipDamage += par2 * 10;

                if (e instanceof PlayerEntity && ((PlayerEntity) e).abilities.isCreativeMode)
                {
                    this.shipDamage = 100;
                }

                if (flag || this.shipDamage > 90 && !this.world.isRemote)
                {
                    this.removePassengers();

                    if (flag)
                    {
                        this.remove();
                    }
                    else
                    {
                        this.remove();
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
            ItemEntity entityItem = this.entityDropItem(item, 0);

            if (item.hasTag())
            {
                entityItem.getItem().setTag(item.getTag().copy());
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
        return this.isAlive();
    }

    @Override
    public boolean shouldRiderSit()
    {
        return false;
    }

    @Override
    public void tick()
    {
        this.ticks++;

        super.tick();

        if (this.addToTelemetry)
        {
            this.addToTelemetry = false;
            for (BlockVec3Dim vec : new ArrayList<BlockVec3Dim>(this.telemetryList))
            {
                TileEntity t1 = vec.getTileEntityNoLoad();
                if (t1 instanceof TileEntityTelemetry && !t1.isRemoved())
                {
                    if (((TileEntityTelemetry) t1).linkedEntity == this)
                    {
                        ((TileEntityTelemetry) t1).addTrackedEntity(this);
                    }
                }
            }
        }

        for (Entity e : this.getPassengers())
        {
            e.fallDistance = 0.0F;
        }

        if (this.getPosY() > (this.world.getDimension().getType() instanceof IExitHeight ? ((IExitHeight) this.world.getDimension().getType()).getYCoordinateToTeleport() : 500) && this.launchPhase != EnumLaunchPhase.LANDING.ordinal())
        {
            this.onReachAtmosphere();
//            if (this.world.isRemote)
//            	this.getPosY() = 1 + (this.world.getDimension() instanceof IExitHeight ? ((IExitHeight) this.world.getDimension()).getYCoordinateToTeleport() : 1200);
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
            if (this.getPosY() < 0.0D)
            {
                this.remove();
            }
            else if (this.getPosY() > (this.world.getDimension() instanceof IExitHeight ? ((IExitHeight) this.world.getDimension()).getYCoordinateToTeleport() : 1200) + (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() ? 355 : 100))
            {
                for (Entity e : this.getPassengers())
                {
                    if (e instanceof ServerPlayerEntity)
                    {
                        GCPlayerStats stats = GCPlayerStats.get(e);
                        if (stats.isUsingPlanetSelectionGui())
                        {
                            this.remove();
                        }
                    }
                    else
                    {
                        this.remove();
                    }
                }
            }

            if (this.timeSinceLaunch > 50 && this.onGround)
            {
                this.failRocket();
            }
        }

        if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal())
        {
            this.timeUntilLaunch = this.getPreLaunchWait();
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
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

        AxisAlignedBB box = this.getBoundingBox().grow(0.2D);

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

        double motionX = -(50 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * Math.sin(this.rotationPitch * 0.01 / Constants.RADIANS_TO_DEGREES_D));
        double motionY = getMotion().y;
        double motionZ = -(50 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * Math.sin(this.rotationPitch * 0.01 / Constants.RADIANS_TO_DEGREES_D));

        if (this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal())
        {
            motionX = motionY = motionZ = 0.0;
        }

        setMotion(motionX, motionY, motionZ);

        if (this.world.isRemote)
        {
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());

            if (this.shouldMoveClientSide())
            {
                this.move(MoverType.SELF, this.getMotion());
            }
        }
        else
        {
            this.move(MoverType.SELF, this.getMotion());
        }

        this.setRotation(this.rotationYaw, this.rotationPitch);

        if (this.world.isRemote)
        {
            this.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        }

        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();

        if (!this.world.isRemote && this.ticks % 3 == 0)
        {
            GalacticraftCore.packetPipeline.sendToDimension(new PacketDynamic(this), this.world.getDimension().getType());
            // PacketDispatcher.sendPacketToAllInDimension(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
            // this, this.getNetworkedData(new ArrayList())),
            // this.world.getDimension().getType());
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
        {
            if (getPassengers().size() >= 1) //When the screen changes to the map, the player is not riding the rocket anymore.
            {
                if (getPassengers().get(0) instanceof ServerPlayerEntity)
                {
                    GCTriggers.LAUNCH_ROCKET.trigger(((ServerPlayerEntity) getPassengers().get(0)));
                }
            }
        }
    }

    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    public abstract boolean hasValidFuel();

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (!this.world.isRemote)
        {
            new Exception().printStackTrace();
        }
        int newLaunchPhase = buffer.readInt();
        if (this.launchPhase != newLaunchPhase)
        {
            this.setLaunchPhase(EnumLaunchPhase.values()[newLaunchPhase]);
            if (newLaunchPhase == EnumLaunchPhase.LANDING.ordinal())
            {
                this.syncAdjustFlag = true;
            }
        }
        else if (this.hasValidFuel())
        {
            double motionX = this.getMotion().x;
            double motionY = this.getMotion().y;
            double motionZ = this.getMotion().z;
            if (Math.abs(this.syncAdjustX - this.getPosX()) > 0.2D)
            {
                motionX += (this.syncAdjustX - this.getPosX()) / 40D;
            }
            if (Math.abs(this.syncAdjustY - this.getPosY()) > 0.2D)
            {
                motionY += (this.syncAdjustY - this.getPosY()) / 40D;
            }
            if (Math.abs(this.syncAdjustZ - this.getPosZ()) > 0.2D)
            {
                motionZ += (this.syncAdjustZ - this.getPosZ()) / 40D;
            }
            setMotion(motionX, motionY, motionZ);
        }
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

        if (!ConfigManagerCore.disableSpaceshipGrief.get())
        {
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 5, Explosion.Mode.NONE);
        }

        this.remove();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setRotation(yaw, pitch);
        if (this.syncAdjustFlag && this.world.isBlockLoaded(new BlockPos(x, 255D, z)) && this.hasValidFuel())
        {
            PlayerEntity p = Minecraft.getInstance().player;
            double dx = x - p.getPosX();
            double dz = z - p.getPosZ();
            if (dx * dx + dz * dz < 1024)
            {
                if (this.world.getEntityByID(this.getEntityId()) == null)
                {
                    try
                    {
                        ((ClientWorld) this.world).addEntity(this.getEntityId(), this);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                this.setRawPosition(x, y, z);

                int cx = MathHelper.floor(x / 16.0D);
                int cz = MathHelper.floor(z / 16.0D);

                if (!this.addedToChunk || this.chunkCoordX != cx || this.chunkCoordZ != cz)
                {
                    if (this.addedToChunk && this.world.isBlockLoaded(new BlockPos(this.chunkCoordX << 4, 255, this.chunkCoordZ << 4)))
                    {
                        this.world.getChunk(this.chunkCoordX, this.chunkCoordZ).removeEntityAtIndex(this, this.chunkCoordY);
                    }

                    this.addedToChunk = true;
                    this.world.getChunk(cx, cz).addEntity(this);
                }

                this.syncAdjustX = 0D;
                this.syncAdjustY = 0D;
                this.syncAdjustZ = 0D;
                this.syncAdjustFlag = false;
            }
        }
        else
        {
            this.syncAdjustX = x;
            this.syncAdjustY = y;
            this.syncAdjustZ = z;
        }
    }


    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        nbt.putInt("launchPhase", this.launchPhase + 1);
        nbt.putInt("timeUntilLaunch", this.timeUntilLaunch);
        if (telemetryList.size() > 0)
        {
            ListNBT teleNBTList = new ListNBT();
            for (BlockVec3Dim vec : new ArrayList<BlockVec3Dim>(this.telemetryList))
            {
                CompoundNBT tag = new CompoundNBT();
                vec.writeToNBT(tag);
                teleNBTList.add(tag);
            }
            nbt.put("telemetryList", teleNBTList);
        }
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        this.timeUntilLaunch = nbt.getInt("timeUntilLaunch");

        boolean hasOldTags = false;

        // Backwards compatibility:
        if (nbt.keySet().contains("launched"))
        {
            hasOldTags = true;

            boolean launched = nbt.getBoolean("launched");

            if (launched)
            {
                this.setLaunchPhase(EnumLaunchPhase.LAUNCHED);
            }
        }

        // Backwards compatibility:
        if (nbt.keySet().contains("ignite"))
        {
            hasOldTags = true;

            int ignite = nbt.getInt("ignite");

            if (ignite == 1)
            {
                this.setLaunchPhase(EnumLaunchPhase.IGNITED);
            }
        }

        // Backwards compatibility:
        if (hasOldTags)
        {
            if (this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal() && this.launchPhase != EnumLaunchPhase.IGNITED.ordinal())
            {
                this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
            }
        }
        else
        {
            this.setLaunchPhase(EnumLaunchPhase.values()[nbt.getInt("launchPhase") - 1]);
        }

        //Update all Telemetry Units which are still tracking this rocket
        this.telemetryList.clear();
        if (nbt.contains("telemetryList"))
        {
            ListNBT teleNBT = nbt.getList("telemetryList", 10);
            if (teleNBT.size() > 0)
            {
                for (int j = teleNBT.size() - 1; j >= 0; j--)
                {
                    CompoundNBT tag1 = teleNBT.getCompound(j);
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
        return this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal();
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

    @Override
    public boolean canRiderInteract()
    {
        return true;
    }

    public ResourceLocation getSpaceshipGui()
    {
        return GalacticraftRegistry.getResouceLocationForDimension(this.world.getDimension().getClass());
    }

    public void setLaunchPhase(EnumLaunchPhase phase)
    {
        this.launchPhase = phase.ordinal();
    }

    @Override
    public boolean shouldIgnoreShiftExit()
    {
        return this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal();
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
            if (t1 instanceof TileEntityTelemetry && !t1.isRemoved())
            {
                if (((TileEntityTelemetry) t1).linkedEntity == this)
                {
                    returnList.add((TileEntityTelemetry) t1);
                }
            }
        }
        return returnList;
    }

    @Override
    public void transmitData(int[] data)
    {
        data[0] = this.timeUntilLaunch;
        data[1] = (int) this.getPosY();
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
        str[4] = GCCoreUtil.translate("gui.message.fuel") + ": " + data[3] + "%";
    }

    @Override
    public void adjustDisplay(int[] data)
    {
        GL11.glRotatef(data[4], -1, 0, 0);
        GL11.glTranslatef(0, this.getHeight() / 4, 0);
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

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.getPosY() + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.getPosX(), height, this.getPosZ());
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }
}