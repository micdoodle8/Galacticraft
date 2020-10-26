package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class EntityCargoRocket extends EntityAutoRocket implements IRocketType, IInventory, IWorldTransferCallback
{
    public EnumRocketType rocketType;
    public float rumble;

    public EntityCargoRocket(EntityType<? extends EntityCargoRocket> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(0.98F, 2F);
    }

    public static EntityCargoRocket createEntityCargoRocket(World world, double x, double y, double z, EnumRocketType rocketType)
    {
        EntityCargoRocket rocket = new EntityCargoRocket(MarsEntities.CARGO_ROCKET, world);
        rocket.setPosition(x, y, z);
        rocket.prevPosX = x;
        rocket.prevPosY = y;
        rocket.prevPosZ = z;
        rocket.rocketType = rocketType;
        rocket.stacks = NonNullList.withSize(rocket.getSizeInventory(), ItemStack.EMPTY);
//        rocket.setSize(0.98F, 2F);
        return rocket;
    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
        default:
        case DEFAULT:
        case INVENTORY27:
            return MarsItems.rocketCargo1;
        case INVENTORY36:
            return MarsItems.rocketCargo2;
        case INVENTORY54:
            return MarsItems.rocketCargo3;
        case PREFUELED:
            return MarsItems.rocketCargoCreative;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == MarsItems.rocketCargo1)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == MarsItems.rocketCargo2)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == MarsItems.rocketCargo3)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == MarsItems.rocketCargoCreative)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 2000;
    }

    public float getCargoFilledAmount()
    {
        float weight = 1;

        for (ItemStack stack : this.stacks)
        {
            if (stack != null && !stack.isEmpty())
            {
                weight += 0.1D;
            }
        }

        return weight;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
//        return new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
        return new ItemStack(getItemFromType(rocketType));
    }

    @Override
    public void tick()
    {
        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            double motionScalar = this.timeSinceLaunch / 250;

            motionScalar = Math.min(motionScalar, 1);

            double modifier = this.getCargoFilledAmount();
            motionScalar *= 5.0D / modifier;

            if (this.launchPhase != EnumLaunchPhase.LANDING.ordinal())
            {
                if (motionScalar != 0.0)
                {
//                    this.motionY = ;
                    this.setMotion(this.getMotion().x, -motionScalar * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D), this.getMotion().z);
                }
            }

            double multiplier = 1.0D;

            if (this.world.getDimension() instanceof IGalacticraftDimension)
            {
                multiplier = ((IGalacticraftDimension) this.world.getDimension()).getFuelUsageMultiplier();

                if (multiplier <= 0)
                {
                    multiplier = 1;
                }
            }

            if (this.timeSinceLaunch % MathHelper.floor(3 * (1 / multiplier)) == 0)
            {
                this.removeFuel(1);
                if (!this.hasValidFuel())
                {
                    this.stopRocketSound();
                }
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched())
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
//                this.motionY -= ;
                this.setMotion(this.getMotion().x, this.getMotion().y - Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20, this.getMotion().z);
            }
        }

        super.tick();

        if (this.rumble > 0)
        {
            this.rumble--;
        }

        if (this.rumble < 0)
        {
            this.rumble++;
        }

        if (this.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
        {
            this.performHurtAnimation();

            this.rumble = (float) this.rand.nextInt(3) - 3;
        }

        int i;

        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
            i = 1;
        }

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.rand.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles.get() && this.hasValidFuel())
        {
            if (this.world.isRemote)
            {
                this.spawnParticles(this.getLaunched());
            }
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    protected void spawnParticles(boolean launched)
    {
        double sinPitch = Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
        double x1 = 2 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
        double z1 = 2 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
        double y1 = 2 * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);

        if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
        {
            double modifier = this.getPosY() - this.targetVec.getY();
            modifier = Math.max(modifier, 1.0);
            x1 *= modifier / 60.0D;
            y1 *= modifier / 60.0D;
            z1 *= modifier / 60.0D;
        }

        final double y = this.prevPosY + (this.getPosY() - this.prevPosY) - 0.4;

        if (this.isAlive())
        {
            LivingEntity riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            EntityParticleData particleData = new EntityParticleData(this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE, riddenByEntity.getUniqueID());
            this.world.addParticle(particleData, this.getPosX() + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.getPosZ() + 0.2 - this.rand.nextDouble() / 10 + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.getPosZ() + 0.2 - this.rand.nextDouble() / 10 + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.getPosZ() - 0.2 + this.rand.nextDouble() / 10 + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.getPosZ() - 0.2 + this.rand.nextDouble() / 10 + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() + x1, y, this.getPosZ() + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() + 0.2 + x1, y, this.getPosZ() + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() - 0.2 + x1, y, this.getPosZ() + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() + x1, y, this.getPosZ() + 0.2D + z1, x1, y1, z1);
            this.world.addParticle(particleData, this.getPosX() + x1, y, this.getPosZ() - 0.2D + z1, x1, y1, z1);
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.rocketType = EnumRocketType.values()[buffer.readInt()];
        super.decodePacketdata(buffer);
        this.setRawPosition(buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D, buffer.readDouble() / 8000.0D);
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.world.isRemote)
        {
            return;
        }
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        super.getNetworkedData(list);
        list.add(this.getPosX() * 8000.0D);
        list.add(this.getPosY() * 8000.0D);
        list.add(this.getPosZ() * 8000.0D);
    }

    @Override
    public void onReachAtmosphere()
    {
        if (this.world.isRemote)
        {
            //stop the sounds on the client - but do not reset, the rocket may start again
            this.stopRocketSound();
            return;
        }

        GCLog.debug("[Serverside] Cargo rocket reached space, heading to " + this.destinationFrequency);
        this.setTarget(true, this.destinationFrequency);

        if (this.targetVec != null)
        {
            GCLog.debug("Destination location = " + this.targetVec.toString());
            if (this.targetDimension != GCCoreUtil.getDimensionType(this.world))
            {
                GCLog.debug("Destination is in different dimension: " + this.targetDimension);
                Dimension targetDim = WorldUtil.getProviderForDimensionServer(this.targetDimension);
                if (targetDim != null && targetDim.getWorld() instanceof ServerWorld)
                {
                    GCLog.debug("Loaded destination dimension " + this.targetDimension);
                    this.setPosition(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                    Entity e = WorldUtil.transferEntityToDimension(this, this.targetDimension, (ServerWorld) targetDim.getWorld(), false, null);

                    if (e instanceof EntityCargoRocket)
                    {
                        GCLog.debug("Cargo rocket arrived at destination dimension, going into landing mode.");
                        e.setPosition(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                        ((EntityCargoRocket) e).setLaunchPhase(EnumLaunchPhase.LANDING);
                        //No setDead() following successful transferEntityToDimension() - see javadoc on that
                    }
                    else
                    {
                        GCLog.info("Error: failed to recreate the cargo rocket in landing mode on target planet.");
                        e.remove();
                        this.remove();
                    }
                    return;
                }
                GCLog.info("Error: the server failed to load the dimension the cargo rocket is supposed to land in. Destroying rocket!");
                this.remove();
                return;
            }
            else
            {
                GCLog.debug("Cargo rocket going into landing mode in same destination.");
                this.setPosition(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                this.setLaunchPhase(EnumLaunchPhase.LANDING);
                return;
            }
        }
        else
        {
            GCLog.info("Error: the cargo rocket failed to find a valid landing spot when it reached space.");
            this.remove();
        }
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand)
    {
        if (!this.world.isRemote && player instanceof ServerPlayerEntity)
        {
//            MarsUtil.openCargoRocketInventory((ServerPlayerEntity) player, this); TODO guis
        }

        return false;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        if (world.isRemote)
        {
            return;
        }
        nbt.putInt("Type", this.rocketType.getIndex());

        super.writeAdditional(nbt);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        this.rocketType = EnumRocketType.values()[nbt.getInt("Type")];

        super.readAdditional(nbt);
    }

    @Override
    public EnumRocketType getRocketType()
    {
        return this.rocketType;
    }

    @Override
    public int getSizeInventory()
    {
        if (this.rocketType == null)
        {
            return 0;
        }
        return this.rocketType.getInventorySpace();
    }

    @Override
    public void onWorldTransferred(World world)
    {
        if (this.targetVec != null)
        {
            this.setPosition(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
            this.setLaunchPhase(EnumLaunchPhase.LANDING);
        }
        else
        {
            this.remove();
        }
    }

    @Override
    public int getRocketTier()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getPreLaunchWait()
    {
        return 20;
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
    {
        super.getItemsDropped(droppedItemList);
//        ItemStack rocket = new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
        ItemStack rocket = new ItemStack(getItemFromType(rocketType));
        rocket.setTag(new CompoundNBT());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItemList.add(rocket);
        return droppedItemList;
    }

    @Override
    public boolean isPlayerRocket()
    {
        return false;
    }

    @Override
    public double getOnPadYOffset()
    {
        return -0.05D;
    }

    @Override
    public float getRenderOffsetY()
    {
        return -0.1F;
    }
}
