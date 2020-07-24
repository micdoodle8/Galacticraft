package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class EntityTier1Rocket extends EntityTieredRocket
{
    public static final int FUEL_CAPACITY = 1000 * ConfigManagerCore.rocketFuelFactor;

    public EntityTier1Rocket(EntityType<? extends EntityTier1Rocket> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    //    public EntityTier1Rocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
//    {
//        super(par1World, par2, par4, par6);
//        this.rocketType = rocketType;
//        this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
//        this.setSize(1.2F, 3.5F);
////        this.yOffset = 1.5F;
//    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
            default:
            case DEFAULT:
                return GCItems.rocketTierOne;
            case INVENTORY27:
                return GCItems.rocketTierOneCargo1;
            case INVENTORY36:
                return GCItems.rocketTierOneCargo2;
            case INVENTORY54:
                return GCItems.rocketTierOneCargo3;
            case PREFUELED:
                return GCItems.rocketTierOneCreative;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == GCItems.rocketTierOne)
        {
            return EnumRocketType.DEFAULT;
        }
        if (item == GCItems.rocketTierOneCargo1)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == GCItems.rocketTierOneCargo2)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == GCItems.rocketTierOneCargo3)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == GCItems.rocketTierOneCreative)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public double getMountedYOffset()
    {
        return 0.3D;
    }

    @Override
    public float getRotateOffset()
    {
        return -1.5F;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(getItemFromType(getRocketType()), 1);
    }

    @Override
    public void tick()
    {
        super.tick();

        int i;

        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
            i = 1;
        }

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.rand.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (this.world.isRemote)
            {
                this.spawnParticles(this.getLaunched());
            }
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
            {
                double d = this.timeSinceLaunch / 150;

                if (this.world.getDimension() instanceof IGalacticraftDimension && ((IGalacticraftDimension) this.world.getDimension()).hasNoAtmosphere())
                {
                    d = Math.min(d * 1.2, 1.6);
                }
                else
                {
                    d = Math.min(d, 1);
                }

                if (d != 0.0)
                {
                    setMotion(getMotion().x, -d * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D), getMotion().z);
                }
            }
            else
            {
                this.setMotion(this.getMotion().add(0.0, -0.008, 0.0));
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
        else if (!this.hasValidFuel() && this.getLaunched() && !this.world.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.setMotion(this.getMotion().add(0.0, -(Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20), 0.0));
//                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }
    }

    @Override
    public void onTeleport(ServerPlayerEntity player)
    {
        final ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (this.stacks == null || this.stacks.isEmpty())
            {
                stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            }
            else
            {
                stats.setRocketStacks(this.stacks);
            }

            stats.setRocketItem(getItemFromType(getRocketType()));
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (this.isAlive())
        {
            double sinPitch = Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
            double x1 = 2 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double z1 = 2 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double y1 = 2 * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);

            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.getPosY() - this.targetVec.getY();
                modifier = Math.min(Math.max(modifier, 120.0), 300.0);
                x1 *= modifier / 100.0D;
                y1 *= modifier / 100.0D;
                z1 *= modifier / 100.0D;
            }

            double y = this.prevPosY + (this.getPosY() - this.prevPosY) + y1 - this.getMotion().y + 1.2D;

            final double x2 = this.getPosX() + x1 - this.getMotion().x;
            final double z2 = this.getPosZ() + z1 - this.getMotion().z;

            LivingEntity riddenByEntity = !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof LivingEntity ? (LivingEntity) this.getPassengers().get(0) : null;

            if (this.getLaunched())
            {
//                Vector3 motionVec = new Vector3((float)x1, (float)y1, (float)z1);
//                Object[] rider = new Object[] { riddenByEntity };
                EntityParticleData particleData = new EntityParticleData(GCParticles.LAUNCH_FLAME_LAUNCHED, riddenByEntity.getUniqueID());
                this.world.addParticle(particleData, x2 + 0.4 - this.rand.nextDouble() / 10D, y, z2 + 0.4 - this.rand.nextDouble() / 10D, x1, y1, z1);
                this.world.addParticle(particleData, x2 - 0.4 + this.rand.nextDouble() / 10D, y, z2 + 0.4 - this.rand.nextDouble() / 10D, x1, y1, z1);
                this.world.addParticle(particleData, x2 - 0.4 + this.rand.nextDouble() / 10D, y, z2 - 0.4 + this.rand.nextDouble() / 10D, x1, y1, z1);
                this.world.addParticle(particleData, x2 + 0.4 - this.rand.nextDouble() / 10D, y, z2 - 0.4 + this.rand.nextDouble() / 10D, x1, y1, z1);
                this.world.addParticle(particleData, x2, y, z2, x1, y1, z1);
                this.world.addParticle(particleData, x2 + 0.4, y, z2, x1, y1, z1);
                this.world.addParticle(particleData, x2, y, z2 + 0.4D, x1, y1, z1);
                this.world.addParticle(particleData, x2, y, z2 - 0.4D, x1, y1, z1);

            }
            else if (this.ticksExisted % 2 == 0)
            {
                Object[] rider = new Object[]{riddenByEntity};
                y += 0.6D;
                EntityParticleData particleData = new EntityParticleData(GCParticles.LAUNCH_FLAME_LAUNCHED, riddenByEntity.getUniqueID());
                this.world.addParticle(particleData, x2 + 0.4 - this.rand.nextDouble() / 10D, y, z2 + 0.4 - this.rand.nextDouble() / 10D, this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25);
                this.world.addParticle(particleData, x2 - 0.4 + this.rand.nextDouble() / 10D, y, z2 + 0.4 - this.rand.nextDouble() / 10D, this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25);
                this.world.addParticle(particleData, x2 - 0.4 + this.rand.nextDouble() / 10D, y, z2 - 0.4 + this.rand.nextDouble() / 10D, this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25);
                this.world.addParticle(particleData, x2 + 0.4 - this.rand.nextDouble() / 10D, y, z2 - 0.4 + this.rand.nextDouble() / 10D, this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25);
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity par1EntityPlayer)
    {
        return this.isAlive() && par1EntityPlayer.getDistanceSq(this) <= 64.0D;
    }

    @Override
    public void writeAdditional(CompoundNBT nbt)
    {
        super.writeAdditional(nbt);
    }

    @Override
    public void readAdditional(CompoundNBT nbt)
    {
        super.readAdditional(nbt);
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        super.getItemsDropped(droppedItems);
        ItemStack rocket = new ItemStack(getItemFromType(getRocketType()), 1);
        rocket.setTag(new CompoundNBT());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityLandingPad;
    }

    @Override
    public int getRocketTier()
    {
        return 1;
    }

    @Override
    public int getFuelTankCapacity()
    {
        return FUEL_CAPACITY;
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public double getOnPadYOffset()
    {
        return 0.0D;
    }
}
