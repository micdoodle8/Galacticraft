package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.event.GCCoreLandingPadRemovalEvent;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumClientPacket;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.Loader;

public abstract class EntityTieredRocket extends EntitySpaceshipBase implements IRocketType, IDockable, IInventory
{
    public FluidTank spaceshipFuelTank = new FluidTank(this.getFuelTankCapacity());
    public EnumRocketType rocketType;
    public float rumble;

    public EntityTieredRocket(World par1World)
    {
        super(par1World);
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
    }

    public abstract int getFuelTankCapacity();

    @Override
    public int getScaledFuelLevel(int scale)
    {
        final double fuelLevel = this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount;

        return (int) (fuelLevel * scale / this.getFuelTankCapacity());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();

        if (Loader.isModLoaded("ICBM|Explosion"))
        {
            try
            {
                Class.forName("icbm.api.RadarRegistry").getMethod("register", Entity.class).invoke(null, this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setDead()
    {
        super.setDead();

        if (Loader.isModLoaded("ICBM|Explosion"))
        {
            try
            {
                Class.forName("icbm.api.RadarRegistry").getMethod("unregister", Entity.class).invoke(null, this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote && this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
        {
            for (ILandingPadAttachable tile : this.getLandingPad().getConnectedTiles())
            {
                if (this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) != null && this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) instanceof GCCoreTileEntityFuelLoader)
                {
                    if (tile instanceof GCCoreTileEntityFuelLoader && ((GCCoreTileEntityFuelLoader) tile).getEnergyStored() > 0)
                    {
                        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
                        {
                            this.setPad(null);
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

        if (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.performHurtAnimation();

            this.rumble = (float) this.rand.nextInt(3) - 3;
        }
    }

    @Override
    public void readNetworkedData(ByteArrayDataInput dataStream)
    {
        super.readNetworkedData(dataStream);
        this.spaceshipFuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, dataStream.readInt()));
        this.rocketType = EnumRocketType.values()[dataStream.readInt()];
    }

    @Override
    public ArrayList<Object> getNetworkedData(ArrayList<Object> list)
    {
        super.getNetworkedData(list);
        list.add(this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount);
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        return list;
    }

    public boolean hasValidFuel()
    {
        return !(this.spaceshipFuelTank.getFluid() == null || this.spaceshipFuelTank.getFluid().amount == 0);
    }

    @Override
    public void onReachAtmoshpere()
    {
        this.teleport();
    }

    public void teleport()
    {
        if (this.riddenByEntity != null)
        {
            if (this.riddenByEntity instanceof GCCorePlayerMP)
            {
                GCCorePlayerMP player = (GCCorePlayerMP) this.riddenByEntity;

                HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(this.getRocketTier()), player);

                String temp = "";
                int count = 0;

                for (Entry<String, Integer> entry : map.entrySet())
                {
                    temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "." : ""));
                    count++;
                }

                player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.UPDATE_DIMENSION_LIST, new Object[] { player.username, temp }));
                player.setSpaceshipTier(this.getRocketTier());
                player.setUsingPlanetGui();

                this.onTeleport(player);
                player.mountEntity(this);

                if (!this.isDead)
                {
                    this.setDead();
                }
            }
        }
    }

    public void onTeleport(EntityPlayerMP player)
    {
        ;
    }

    @Override
    public void onLaunch()
    {
        super.onLaunch();

        if (!this.worldObj.isRemote)
        {
            if (!(this.worldObj.provider instanceof IOrbitDimension) && this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
            {
                ((GCCorePlayerMP) this.riddenByEntity).setCoordsTeleportedFromX(this.riddenByEntity.posX);
                ((GCCorePlayerMP) this.riddenByEntity).setCoordsTeleportedFromZ(this.riddenByEntity.posZ);
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
                                GCCoreLandingPadRemovalEvent event = new GCCoreLandingPadRemovalEvent(this.worldObj, x, y, z);
                                MinecraftForge.EVENT_BUS.post(event);

                                if (event.allow)
                                {
                                    this.worldObj.setBlockToAir(x, y, z);
                                    amountRemoved = 9;
                                }
                            }
                        }
                    }
                }
            }

            this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer)
    {
        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
        {
            return false;
        }

        if (this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
        {
            if (!this.worldObj.isRemote)
            {
                final Object[] toSend = { ((EntityPlayerMP) this.riddenByEntity).username };
                ((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.RESET_THIRD_PERSON, toSend));
                final Object[] toSend2 = { 0 };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.ZOOM_CAMERA, toSend2));
                ((GCCorePlayerMP) par1EntityPlayer).setChatCooldown(0);
                par1EntityPlayer.mountEntity(null);
            }

            return true;
        }
        else if (par1EntityPlayer instanceof GCCorePlayerMP)
        {
            if (!this.worldObj.isRemote)
            {
                final Object[] toSend = { par1EntityPlayer.username };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.MOUNT_ROCKET, toSend));
                final Object[] toSend2 = { 1 };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumClientPacket.ZOOM_CAMERA, toSend2));
                ((GCCorePlayerMP) par1EntityPlayer).setChatCooldown(0);
                par1EntityPlayer.mountEntity(this);
            }

            return true;
        }

        return false;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("Type", this.rocketType.getIndex());

        if (this.spaceshipFuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.spaceshipFuelTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);
        this.rocketType = EnumRocketType.values()[nbt.getInteger("Type")];

        if (nbt.hasKey("fuelTank"))
        {
            this.spaceshipFuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public EnumRocketType getType()
    {
        return this.rocketType;
    }

    @Override
    public int getSizeInventory()
    {
        return this.rocketType.getInventorySpace();
    }
}
