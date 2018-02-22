package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

//import calclavia.api.icbm.IMissile;

public class EntityTier1Rocket extends EntityTieredRocket
{
    public EntityTier1Rocket(World par1World)
    {
        super(par1World);
        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
    }

    public EntityTier1Rocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
    {
        super(par1World, par2, par4, par6);
        this.rocketType = rocketType;
        this.cargoItems = new ItemStack[this.getSizeInventory()];
        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
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
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(GCItems.rocketTier1, 1, this.rocketType.getIndex());
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

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
            if (this.worldObj.isRemote)
            {
                this.spawnParticles(this.getLaunched());
            }
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
            {
                double d = this.timeSinceLaunch / 150;

                if (this.worldObj.provider instanceof IGalacticraftWorldProvider && ((IGalacticraftWorldProvider) this.worldObj.provider).hasNoAtmosphere())
                {
                    d = Math.min(d * 1.2, 1.6);
                }
                else
                {
                    d = Math.min(d, 1);
                }

                if (d != 0.0)
                {
                    this.motionY = -d * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);
                }
            }
            else
            {
                this.motionY -= 0.008D;
            }

            double multiplier = 1.0D;

            if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
            {
                multiplier = ((IGalacticraftWorldProvider) this.worldObj.provider).getFuelUsageMultiplier();

                if (multiplier <= 0)
                {
                    multiplier = 1;
                }
            }

            if (this.timeSinceLaunch % MathHelper.floor_double(3 * (1 / multiplier)) == 0)
            {
                this.removeFuel(1);
                if (!this.hasValidFuel())
                {
                    this.stopRocketSound();
                }
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }
    }

    @Override
    public void onTeleport(EntityPlayerMP player)
    {
        final EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);

            if (this.cargoItems == null || this.cargoItems.length == 0)
            {
                stats.setRocketStacks(new ItemStack[2]);
            }
            else
            {
                stats.setRocketStacks(this.cargoItems);
            }

            stats.setRocketType(this.rocketType.getIndex());
            stats.setRocketItem(GCItems.rocketTier1);
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (!this.isDead)
        {
            double sinPitch = Math.sin(this.rotationPitch / Constants.RADIANS_TO_DEGREES_D);
            double x1 = 2 * Math.cos(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double z1 = 2 * Math.sin(this.rotationYaw / Constants.RADIANS_TO_DEGREES_D) * sinPitch;
            double y1 = 2 * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);

            if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null)
            {
                double modifier = this.posY - this.targetVec.getY();
                modifier = Math.min(Math.max(modifier, 120.0), 300.0);
                x1 *= modifier / 100.0D;
                y1 *= modifier / 100.0D;
                z1 *= modifier / 100.0D;
            }

            double y = this.prevPosY + (this.posY - this.prevPosY) + y1 - this.motionY + 1.2D;

            final double x2 = this.posX + x1 - this.motionX;
            final double z2 = this.posZ + z1 - this.motionZ;

            EntityLivingBase riddenByEntity = this.riddenByEntity instanceof EntityLivingBase ? (EntityLivingBase) this.riddenByEntity : null;

            if (this.getLaunched())
            {
                Vector3 motionVec = new Vector3(x1, y1, z1);
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4, y, z2), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4, y, z2), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2 + 0.4D), motionVec, new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2 - 0.4D), motionVec, new Object[] { riddenByEntity });

            }
            else if (this.ticksExisted % 2 == 0)
            {
                y += 1.2D;
                GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(x1 + 0.7, y1 - 1D, z1 + 0.7), new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(x1 - 0.7, y1 - 1D, z1 + 0.7), new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(x1 - 0.7, y1 - 1D, z1 - 0.7), new Object[] { riddenByEntity });
                GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(x1 + 0.7, y1 - 1D, z1 - 0.7), new Object[] { riddenByEntity });
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return !this.isDead && par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
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
        ItemStack rocket = new ItemStack(GCItems.rocketTier1, 1, this.rocketType.getIndex());
        rocket.setTagCompound(new NBTTagCompound());
        rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

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
        return 1000;
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
