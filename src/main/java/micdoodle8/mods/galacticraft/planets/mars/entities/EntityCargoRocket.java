package micdoodle8.mods.galacticraft.planets.mars.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.util.MarsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class EntityCargoRocket extends EntityAutoRocket implements IRocketType, IInventory, IWorldTransferCallback
{
    public EnumRocketType rocketType;
    public float rumble;

    public EntityCargoRocket(World par1World)
    {
        super(par1World);
        this.setSize(0.98F, 2F);
    }

    public EntityCargoRocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
    {
        super(par1World, par2, par4, par6);
        this.rocketType = rocketType;
        this.cargoItems = new ItemStack[this.getSizeInventory()];
        this.setSize(0.98F, 2F);
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 2000;
    }

    public float getCargoFilledAmount()
    {
        float weight = 1;

        for (ItemStack stack : this.cargoItems)
        {
            if (stack != null)
            {
                weight += 0.1D;
            }
        }

        return weight;
    }

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target)
    {
        return new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
    }

    @Override
    public void onUpdate()
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
                    this.motionY = -motionScalar * Math.cos((this.rotationPitch - 180) / Constants.RADIANS_TO_DEGREES_D);
                }
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
        else if (!this.hasValidFuel() && this.getLaunched())
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }

        super.onUpdate();

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

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.rand.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (this.worldObj.isRemote)
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
            double modifier = this.posY - this.targetVec.getY();
            modifier = Math.max(modifier, 1.0);
            x1 *= modifier / 60.0D;
            y1 *= modifier / 60.0D;
            z1 *= modifier / 60.0D;
        }

        final double y = this.prevPosY + (this.posY - this.prevPosY) - 0.4;

        if (!this.isDead)
        {
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + 0.2 + x1, y, this.posZ + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX - 0.2 + x1, y, this.posZ + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ + 0.2D + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
            GalacticraftCore.proxy.spawnParticle(this.getLaunched() ? "launchFlameLaunched" : "launchFlameIdle", new Vector3(this.posX + x1, y, this.posZ - 0.2D + z1), new Vector3(x1, y1, z1), new Object[] { riddenByEntity });
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.rocketType = EnumRocketType.values()[buffer.readInt()];
        super.decodePacketdata(buffer);
        this.posX = buffer.readDouble() / 8000.0D;
        this.posY = buffer.readDouble() / 8000.0D;
        this.posZ = buffer.readDouble() / 8000.0D;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.worldObj.isRemote)
        {
            return;
        }
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        super.getNetworkedData(list);
        list.add(this.posX * 8000.0D);
        list.add(this.posY * 8000.0D);
        list.add(this.posZ * 8000.0D);
    }

    @Override
    public void onReachAtmosphere()
    {
        if (this.worldObj.isRemote)
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
            if (this.targetDimension != GCCoreUtil.getDimensionID(this.worldObj))
            {
                GCLog.debug("Destination is in different dimension: " + this.targetDimension);
                WorldProvider targetDim = WorldUtil.getProviderForDimensionServer(this.targetDimension);
                if (targetDim != null && targetDim.worldObj instanceof WorldServer)
                {
                    GCLog.debug("Loaded destination dimension " + this.targetDimension);
                    this.setPosition(this.targetVec.getX() + 0.5F, this.targetVec.getY() + 800, this.targetVec.getZ() + 0.5F);
                    Entity e = WorldUtil.transferEntityToDimension(this, this.targetDimension, (WorldServer) targetDim.worldObj, false, null);

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
                        e.setDead();
                        this.setDead();
                    }
                    return;
                }
                GCLog.info("Error: the server failed to load the dimension the cargo rocket is supposed to land in. Destroying rocket!");
                this.setDead();
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
            this.setDead();
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && par1EntityPlayer instanceof EntityPlayerMP)
        {
            MarsUtil.openCargoRocketInventory((EntityPlayerMP) par1EntityPlayer, this);
        }

        return false;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("Type", this.rocketType.getIndex());

        super.writeEntityToNBT(nbt);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.rocketType = EnumRocketType.values()[nbt.getInteger("Type")];

        super.readEntityFromNBT(nbt);
    }

    @Override
    public EnumRocketType getType()
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
            this.setDead();
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
        ItemStack rocket = new ItemStack(MarsItems.rocketMars, 1, this.rocketType.getIndex() + 10);
        rocket.setTagCompound(new NBTTagCompound());
        rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
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
