package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.GCParticles;
import micdoodle8.mods.galacticraft.core.client.fx.EntityParticleData;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
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
import java.util.Random;

public class EntityTier2Rocket extends EntityTieredRocket
{
    public EntityTier2Rocket(EntityType<? extends EntityTier2Rocket> type, World worldIn)
    {
        super(type, worldIn);
//        this.setSize(1.2F, 4.5F);
    }

    public static EntityTier2Rocket createEntityTier2Rocket(World world, double x, double y, double z, EnumRocketType rocketType)
    {
        EntityTier2Rocket rocket = new EntityTier2Rocket(MarsEntities.ROCKET_T2.get(), world);
        rocket.setPosition(x, y, z);
        rocket.prevPosX = x;
        rocket.prevPosY = y;
        rocket.prevPosZ = z;
        rocket.rocketType = rocketType;
        rocket.stacks = NonNullList.withSize(rocket.getSizeInventory(), ItemStack.EMPTY);
//        rocket.setSize(1.2F, 4.5F);
        return rocket;
    }

    public EntityTier2Rocket createEntityTier2Rocket(World par1World, double par2, double par4, double par6, boolean reversed, EnumRocketType rocketType, NonNullList<ItemStack> inv)
    {
        EntityTier2Rocket rocket = createEntityTier2Rocket(par1World, par2, par4, par6, rocketType);
        this.stacks = inv;
        return rocket;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    public static Item getItemFromType(EnumRocketType rocketType)
    {
        switch (rocketType)
        {
            default:
            case DEFAULT:
                return MarsItems.rocketTierTwo;
            case INVENTORY27:
                return MarsItems.rocketTierTwoCargo1;
            case INVENTORY36:
                return MarsItems.rocketTierTwoCargo2;
            case INVENTORY54:
                return MarsItems.rocketTierTwoCargo3;
            case PREFUELED:
                return MarsItems.rocketTierTwoCreative;
        }
    }

    public static EnumRocketType getTypeFromItem(Item item)
    {
        if (item == MarsItems.rocketTierTwo)
        {
            return EnumRocketType.DEFAULT;
        }
        if (item == MarsItems.rocketTierTwoCargo1)
        {
            return EnumRocketType.INVENTORY27;
        }
        if (item == MarsItems.rocketTierTwoCargo2)
        {
            return EnumRocketType.INVENTORY36;
        }
        if (item == MarsItems.rocketTierTwoCargo3)
        {
            return EnumRocketType.INVENTORY54;
        }
        if (item == MarsItems.rocketTierTwoCreative)
        {
            return EnumRocketType.PREFUELED;
        }
        return null;
    }

    @Override
    public double getYOffset()
    {
        return 1.5F;
    }

    @Override
    protected void registerData()
    {
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(getItemFromType(rocketType));
    }

    @Override
    public double getMountedYOffset()
    {
        return 1.6D;
    }

    @Override
    public float getRotateOffset()
    {
        return 1.25F;
    }

    @Override
    public double getOnPadYOffset()
    {
        return -0.2D;
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
                    d = Math.min(d * 1.2, 1.8);
                }
                else
                {
                    d = Math.min(d, 1.2);
                }

                if (d != 0.0)
                {
//                    this.motionY = -d * 2.0D * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);
                    this.setMotion(this.getMotion().x, -d * 2.0D * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D), this.getMotion().z);
                }
            }
            else
            {
//                this.motionY -= 0.008D;
                this.setMotion(this.getMotion().x, this.getMotion().y - 0.008D, this.getMotion().z);
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

            if (this.timeSinceLaunch % MathHelper.floor(2 * (1 / multiplier)) == 0)
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
//                this.motionY -= ;
                this.setMotion(this.getMotion().x, this.getMotion().y - Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20, this.getMotion().z);
            }
        }
    }

    @Override
    public void onTeleport(ServerPlayerEntity player)
    {
        ServerPlayerEntity playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(playerBase);

            if (this.stacks == null || this.stacks.size() == 0)
            {
                stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            }
            else
            {
                stats.setRocketStacks(this.stacks);
            }

//            stats.setRocketType(this.rocketType.getIndex());
            stats.setRocketItem(getItemFromType(getRocketType()));
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (this.isAlive())
        {
            double sinPitch = Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
            double x1 = 2.9 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double z1 = 2.9 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double y1 = 2.9 * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);
            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.getPosY() - this.targetVec.getY();
                modifier = Math.min(Math.max(modifier, 80.0), 200.0);
                x1 *= modifier / 100.0D;
                y1 *= modifier / 100.0D;
                z1 *= modifier / 100.0D;
            }

            final double y = this.prevPosY + (this.getPosY() - this.prevPosY) + y1 - this.getMotion().y + (!this.getLaunched() ? 2.5D : 1D);

            final double x2 = this.getPosX() + x1 - this.getMotion().x;
            final double z2 = this.getPosZ() + z1 - this.getMotion().z;
            final double x3 = x2 + x1 / 2D;
            final double y3 = y + y1 / 2D;
            final double z3 = z2 + z1 / 2D;

            if (this.ticksExisted % 2 == 0 && !this.getLaunched())
            {
                return;
            }

//            String flame = this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE;

            LivingEntity riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof LivingEntity) ? null : (LivingEntity) this.getPassengers().get(0);
            EntityParticleData particleData = new EntityParticleData(this.getLaunched() ? GCParticles.LAUNCH_FLAME_LAUNCHED : GCParticles.LAUNCH_FLAME_IDLE, riddenByEntity.getUniqueID());
            Random random = this.rand;
            this.world.addParticle(particleData, x2 + 0.4 - random.nextDouble() / 10D, y, z2 + 0.4 - random.nextDouble() / 10D, x1, y1, z1);
            this.world.addParticle(particleData, x2 - 0.4 + random.nextDouble() / 10D, y, z2 + 0.4 - random.nextDouble() / 10D, x1, y1, z1);
            this.world.addParticle(particleData, x2 - 0.4 + random.nextDouble() / 10D, y, z2 - 0.4 + random.nextDouble() / 10D, x1, y1, z1);
            this.world.addParticle(particleData, x2 + 0.4 - random.nextDouble() / 10D, y, z2 - 0.4 + random.nextDouble() / 10D, x1, y1, z1);
            this.world.addParticle(particleData, x2, y, z2, x1, y1, z1);
            this.world.addParticle(particleData, x2 + 0.4, y, z2, x1, y1, z1);
            this.world.addParticle(particleData, x2 - 0.4, y, z2, x1, y1, z1);
            this.world.addParticle(particleData, x2, y, z2 + 0.4D, x1, y1, z1);
            this.world.addParticle(particleData, x2, y, z2 - 0.4D, x1, y1, z1);
            //Larger flameball for T2 - positioned behind the smaller one
            double a = 4D;
            double bx = x1 + 0.5D / a;
            double bz = z1 + 0.5D / a;
            this.world.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 + 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 + 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 + 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 + 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 - 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 - 0.4, z3 + 0.2 - random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.2 + random.nextDouble() / 6D, y3 - 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.2 - random.nextDouble() / 6D, y3 - 0.4, z3 - 0.2 + random.nextDouble() / 6D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 + 0.7 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 + 0.7 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + 0.7 - random.nextDouble() / 8D, y3, z3 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - 0.7 + random.nextDouble() / 8D, y3, z3 - random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 + random.nextDouble() / 8D, y3, z3 + 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
            this.world.addParticle(particleData, x3 - random.nextDouble() / 8D, y3, z3 - 0.7 + random.nextDouble() / 8D, bx - random.nextDouble() / a, y1, bz - random.nextDouble() / a);
//            this.world.addParticle("blueflame", x2 - 0.8, y, z2), motionVec, none);
//            this.world.addParticle("blueflame", x2 + 0.8, y, z2), motionVec, none);
//            this.world.addParticle("blueflame", x2, y, z2 - 0.8), motionVec, none);
//            this.world.addParticle("blueflame", x2, y, z2 + 0.8), motionVec, none);
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity par1EntityPlayer)
    {
        return this.isAlive() && par1EntityPlayer.getDistanceSq(this) <= 64.0D;
    }

//    @Override
//    protected void writeEntityToNBT(CompoundNBT par1NBTTagCompound)
//    {
//        super.writeEntityToNBT(par1NBTTagCompound);
//    }
//
//    @Override
//    protected void readEntityFromNBT(CompoundNBT par1NBTTagCompound)
//    {
//        super.readEntityFromNBT(par1NBTTagCompound);
//    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityLandingPad;
    }

    @Override
    public int getRocketTier()
    {
        return 2;
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 1500;
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
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
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        super.getItemsDropped(droppedItems);
        ItemStack rocket = new ItemStack(getItemFromType(rocketType));
        rocket.setTag(new CompoundNBT());
        rocket.getTag().putInt("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

    @Override
    public float getRenderOffsetY()
    {
        return -0.1F;
    }
}
