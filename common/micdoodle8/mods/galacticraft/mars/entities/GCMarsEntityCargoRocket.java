package micdoodle8.mods.galacticraft.mars.entities;

import icbm.api.IMissile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.ASMHelper.RuntimeInterface;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.event.GCCoreLandingPadRemovalEvent;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityCargoPad;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController;
import micdoodle8.mods.galacticraft.mars.util.GCMarsUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.core.vector.Vector3;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController.EnumAutoLaunch;

public class GCMarsEntityCargoRocket extends EntitySpaceshipBase implements IRocketType, IDockable, IInventory, IWorldTransferCallback
{
    public FluidTank spaceshipFuelTank = new FluidTank(this.getFuelTankCapacity());
    public EnumRocketType rocketType;
    public float rumble;
    public int destinationFrequency = -1;
    public Vector3 targetVec;
    public int targetDimension;
    protected ItemStack[] cargoItems;
    public IUpdatePlayerListBox rocketSoundUpdater;
    private IFuelDock landingPad;
    public boolean landing;
    public EnumAutoLaunch autoLaunchSetting;
    public EnumAutoLaunch lastAutoLaunchSetting;
    public int autoLaunchCountdown;
    public String statusMessage;
    public int statusMessageCooldown;
    public int lastStatusMessageCooldown;
    public boolean statusValid;

    public GCMarsEntityCargoRocket(World par1World)
    {
        super(par1World);
        this.yOffset = 0;
    }

    public GCMarsEntityCargoRocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
    {
        this(par1World);
        this.setSize(0.98F, 2F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(par2, par4, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.rocketType = rocketType;
        this.cargoItems = new ItemStack[this.getSizeInventory()];
    }

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

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }
    }

    private void autoLaunch()
    {
        this.ignite();
        this.autoLaunchSetting = this.lastAutoLaunchSetting = null;
    }
    
    public boolean checkLaunchValidity()
    {
        this.statusMessageCooldown = 40;
        
        if (this.launchPhase == EnumLaunchPhase.UNIGNITED.getPhase() && !this.worldObj.isRemote && this.spaceshipFuelTank.getFluidAmount() > 0)
        {
            if (!this.setFrequency())
            {
                this.destinationFrequency = -1;
                this.statusMessage = "\u00a7cFrequency#\u00a7cNot Set";
                return false;
            }
            else
            {
                this.statusMessage = "\u00a7aSuccess";
                return true;
            }
        }

        if (this.spaceshipFuelTank.getFluidAmount() <= 0)
        {
            this.destinationFrequency = -1;
            this.statusMessage = "\u00a7cNot Enough#\u00a7cFuel";
            return false;
        }

        this.destinationFrequency = -1;
        return false;
    }

    @Override
    public void onUpdate()
    {        
        if (!this.worldObj.isRemote)
        {
            if (this.statusMessageCooldown > 0)
            {
                this.statusMessageCooldown--;
            }
            
            if (this.statusMessageCooldown == 0 && this.lastStatusMessageCooldown > 0 && this.statusValid)
            {
                this.ignite();
            }
            
            this.lastStatusMessageCooldown = this.statusMessageCooldown;
        }
        
        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.hasValidFuel() && !this.worldObj.isRemote)
        {
            if (this.landing && this.targetVec != null && this.worldObj.getBlockTileEntity(this.targetVec.intX(), this.targetVec.intY(), this.targetVec.intZ()) instanceof IFuelDock && this.posY - this.targetVec.y < 5)
            {
                for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                {
                    for (int y = MathHelper.floor_double(this.posY - 1D); y <= MathHelper.floor_double(this.posY) + 1; y++)
                    {
                        for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                        {
                            TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

                            if (tile instanceof IFuelDock)
                            {
                                this.landRocket(x, y, z);
                            }
                        }
                    }
                }
            }

            double motionScalar = this.timeSinceLaunch / 250;

            motionScalar = Math.min(motionScalar, 1);

            double modifier = this.getCargoFilledAmount();
            motionScalar *= 5.0D / modifier;

            if (!this.landing)
            {
                if (motionScalar != 0.0)
                {
                    this.motionY = -motionScalar * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
                }
            }
            else
            {
                if (this.targetVec != null)
                {
                    this.motionY = (this.posY - this.targetVec.y) / -150.0D;
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
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }

        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.autoLaunchCountdown > 0)
            {
                this.autoLaunchCountdown--;

                if (this.autoLaunchCountdown <= 0)
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.ROCKET_IS_FUELED && this.spaceshipFuelTank.getFluidAmount() == this.spaceshipFuelTank.getCapacity())
            {
                this.autoLaunch();
            }

            if (this.autoLaunchSetting != null && this.lastAutoLaunchSetting == null)
            {
                switch (this.autoLaunchSetting)
                {
                case INSTANT:
                    this.autoLaunch();
                    break;
                case TIME_10_SECONDS:
                    this.autoLaunchCountdown = 200;
                    break;
                case TIME_30_SECONDS:
                    this.autoLaunchCountdown = 600;
                    break;
                case TIME_1_MINUTE:
                    this.autoLaunchCountdown = 1200;
                    break;
                default:
                    break;
                }
            }

            this.lastAutoLaunchSetting = this.autoLaunchSetting;
        }

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

        if (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
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

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() && this.rand.nextInt(i) == 0) && !GCCoreConfigManager.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                this.spawnParticles(this.getLaunched());
            }
        }

        if (this.rocketSoundUpdater != null && (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.getLaunched()))
        {
            this.rocketSoundUpdater.update();
        }

        if (!this.worldObj.isRemote)
        {
            PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 60, this.worldObj.provider.dimensionId, GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList<Object>())));
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return false;
    }

    protected void spawnParticles(boolean launched)
    {
        double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

        if (this.landing && this.targetVec != null)
        {
            double modifier = this.posY - this.targetVec.y;
            modifier = Math.max(modifier, 1.0);
            x1 *= modifier / 60.0D;
            y1 *= modifier / 60.0D;
            z1 *= modifier / 60.0D;
        }

        final double y = this.prevPosY + (this.posY - this.prevPosY) - 0.4;

        if (!this.isDead)
        {
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ + 0.2 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.2 + this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.2 - this.rand.nextDouble() / 10 + x1, y, this.posZ - 0.2 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.2 + x1, y, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.2 + x1, y, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y, this.posZ + 0.2D + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y, this.posZ - 0.2D + z1, x1, y1, z1, this.getLaunched());
        }
    }

    @Override
    public void readNetworkedData(ByteArrayDataInput dataStream)
    {
        super.readNetworkedData(dataStream);
        this.spaceshipFuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, dataStream.readInt()));
        this.rocketType = EnumRocketType.values()[dataStream.readInt()];
        this.landing = dataStream.readBoolean();
        this.destinationFrequency = dataStream.readInt();

        if (dataStream.readBoolean())
        {
            this.targetVec = new Vector3(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
        }

        this.posX = dataStream.readDouble();
        this.posY = dataStream.readDouble();
        this.posZ = dataStream.readDouble();
        this.motionY = dataStream.readDouble();

        if (this.cargoItems == null)
        {
            this.cargoItems = new ItemStack[this.getSizeInventory()];
        }
        
        this.statusMessage = dataStream.readUTF();
        this.statusMessage = (this.statusMessage.equals("") ? null : this.statusMessage);
        this.statusMessageCooldown = dataStream.readInt();
        this.lastStatusMessageCooldown = dataStream.readInt();
        this.statusValid = dataStream.readBoolean();
    }

    @Override
    public ArrayList<Object> getNetworkedData(ArrayList<Object> list)
    {
        super.getNetworkedData(list);
        list.add(this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount);
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        list.add(this.landing);
        list.add(this.destinationFrequency);
        list.add(this.targetVec != null);

        if (this.targetVec != null)
        {
            list.add(this.targetVec.x);
            list.add(this.targetVec.y);
            list.add(this.targetVec.z);
        }

        list.add(this.posX);
        list.add(this.posY);
        list.add(this.posZ);
        list.add(this.motionY);
        
        list.add(this.statusMessage != null ? this.statusMessage : "");
        list.add(this.statusMessageCooldown);
        list.add(this.lastStatusMessageCooldown);
        list.add(this.statusValid);

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
        if (this.worldObj.isRemote)
        {
            return;
        }
        
        this.setTarget(true, this.destinationFrequency);
        
        if (this.targetVec != null)
        {
            if (this.targetDimension != this.worldObj.provider.dimensionId)
            {
                WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(this.targetDimension);
                if (!this.worldObj.isRemote && worldServer != null)
                {
                    WorldUtil.transferEntityToDimension(this, this.targetDimension, worldServer, false);
                }
            }
            else
            {
                this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
                this.landing = true;
            }
        }
        else
        {
            this.setDead();
        }
    }
    
    private boolean setTarget(boolean doSet, int destFreq)
    {
        for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length; i++)
        {
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];

            for (int j = 0; j < world.loadedTileEntityList.size(); j++)
            {
                TileEntity tile = (TileEntity) world.loadedTileEntityList.get(j);

                if (tile != null)
                {
                    tile = world.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);

                    if (tile instanceof GCMarsTileEntityLaunchController)
                    {
                        GCMarsTileEntityLaunchController launchController = (GCMarsTileEntityLaunchController) tile;

                        if (launchController.frequency == destFreq)
                        {
                            boolean targetSet = false;

                            blockLoop:
                                for (int x = -2; x <= 2; x++)
                                {
                                    for (int z = -2; z <= 2; z++)
                                    {
                                        int blockID = world.getBlockId(launchController.xCoord + x, launchController.yCoord, launchController.zCoord + z);
    
                                        if (blockID > 0 && Block.blocksList[blockID] instanceof GCCoreBlockLandingPadFull)
                                        {
                                            if (doSet)
                                            {
                                                this.targetVec = new Vector3(launchController.xCoord + x, launchController.yCoord, launchController.zCoord + z);
                                            }
                                            
                                            targetSet = true;
                                            break blockLoop;
                                        }
                                    }
                                }

                            if (doSet)
                            {
                                this.targetDimension = launchController.worldObj.provider.dimensionId;
                            }

                            if (!targetSet)
                            {
                                if (doSet)
                                {
                                    this.targetVec = null;
                                }
                                
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
    
        return false;
    }

    @Override
    protected void failRocket()
    {
        for (int i = -3; i <= 3; i++)
        {
            if (this.landing && this.targetVec != null && this.worldObj.getBlockTileEntity((int) Math.floor(this.posX), (int) Math.floor(this.posY + i), (int) Math.floor(this.posZ)) instanceof IFuelDock && this.posY - this.targetVec.y < 5)
            {
                for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                {
                    for (int y = MathHelper.floor_double(this.posY - 3.0D); y <= MathHelper.floor_double(this.posY) + 1; y++)
                    {
                        for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                        {
                            TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

                            if (tile instanceof IFuelDock)
                            {
                                this.landRocket(x, y, z);
                                return;
                            }
                        }
                    }
                }
            }
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
        {
            super.failRocket();
        }
    }

    private void landRocket(int x, int y, int z)
    {
        TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            if (this.isDockValid(dock))
            {
                if (!this.worldObj.isRemote)
                {
                    this.launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();
                    this.landing = false;
                    this.setPad(dock);

                    HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

                    for (ILandingPadAttachable connectedTile : connectedTiles)
                    {
                        if (connectedTile != null)
                        {
                            TileEntity updatedTile = this.worldObj.getBlockTileEntity(((TileEntity) connectedTile).xCoord, ((TileEntity) connectedTile).yCoord, ((TileEntity) connectedTile).zCoord);

                            if (updatedTile instanceof GCMarsTileEntityLaunchController)
                            {
                                GCMarsTileEntityLaunchController controller = (GCMarsTileEntityLaunchController) updatedTile;

                                this.autoLaunchSetting = EnumAutoLaunch.values()[controller.launchDropdownSelection];

                                break;
                            }
                        }
                    }
                }

                this.setPosition(this.posX, y + 0.3D, this.posZ);
                return;
            }
        }
    }

    public void onTeleport(EntityPlayerMP player)
    {
        ;
    }
    
    public boolean setFrequency()
    {
        for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
        {
            for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
            {
                for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                {
                    TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

                    if (tile instanceof IFuelDock)
                    {
                        IFuelDock dock = (IFuelDock) tile;

                        GCMarsTileEntityLaunchController launchController = null;

                        for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
                        {
                            if (connectedTile instanceof GCMarsTileEntityLaunchController)
                            {
                                launchController = (GCMarsTileEntityLaunchController) connectedTile;

                                if (launchController != null)
                                {
                                    TileEntity tile2 = launchController.worldObj.getBlockTileEntity(launchController.xCoord, launchController.yCoord, launchController.zCoord);

                                    if (tile2 instanceof GCMarsTileEntityLaunchController)
                                    {
                                        launchController = (GCMarsTileEntityLaunchController) tile2;
                                    }
                                    else
                                    {
                                        launchController = null;
                                    }
                                }

                                if (launchController != null)
                                {
                                    break;
                                }
                            }
                        }

                        if (launchController != null)
                        {
                            if (!launchController.getDisabled(0) && launchController.getEnergyStored() > 0.0F)
                            {
                                if (launchController.frequencyValid && launchController.destFrequencyValid)
                                {
                                    boolean foundPad = this.setTarget(false, launchController.destFrequency);
                                    
                                    if (foundPad)
                                    {
                                        this.destinationFrequency = launchController.destFrequency;
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean igniteWithResult()
    {
        if (this.setFrequency())
        {
            super.ignite();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void ignite()
    {
        this.igniteWithResult();
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
        if (!this.worldObj.isRemote && par1EntityPlayer instanceof EntityPlayerMP)
        {
//            this.ignite();
            GCMarsUtil.openCargoRocketInventory((EntityPlayerMP) par1EntityPlayer, this);
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

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    final NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte) var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            nbt.setTag("Items", var2);
        }

        nbt.setBoolean("TargetValid", this.targetVec != null);

        if (this.targetVec != null)
        {
            nbt.setDouble("targetTileX", this.targetVec.x);
            nbt.setDouble("targetTileY", this.targetVec.y);
            nbt.setDouble("targetTileZ", this.targetVec.z);
        }

        nbt.setBoolean("Landing", this.landing);
        nbt.setInteger("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
        nbt.setInteger("TimeUntilAutoLaunch", this.autoLaunchCountdown);
        nbt.setInteger("DestinationFrequency", this.destinationFrequency);
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

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = nbt.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
                final int var5 = var4.getByte("Slot") & 255;

                if (var5 >= 0 && var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }

        if (nbt.getBoolean("TargetValid") && nbt.hasKey("targetTileX"))
        {
            this.targetVec = new Vector3(nbt.getDouble("targetTileX"), nbt.getDouble("targetTileY"), nbt.getDouble("targetTileZ"));
        }

        this.landing = nbt.getBoolean("Landing");
        int autoLaunchValue = nbt.getInteger("AutoLaunchSetting");
        this.autoLaunchSetting = this.lastAutoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
        this.autoLaunchCountdown = nbt.getInteger("TimeUntilAutoLaunch");
        this.destinationFrequency = nbt.getInteger("DestinationFrequency");
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

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
        if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel"))
        {
            return this.spaceshipFuelTank.fill(liquid, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.spaceshipFuelTank.drain(amount, true);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.rocketType.getInventorySpace() <= 3)
        {
            if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
            {
                this.autoLaunch();
            }

            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.cargoItems.length - 2; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt != null && stackAt.itemID == stack.itemID && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (doAdd)
                {
                    this.cargoItems[count].stackSize += stack.stackSize;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        for (count = 0; count < this.cargoItems.length - 3; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.cargoItems[count] = stack;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
        {
            this.autoLaunch();
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.cargoItems.length - 2; i++)
        {
            ItemStack stackAt = this.cargoItems[i];

            if (stackAt != null)
            {
                if (doRemove && --this.cargoItems[i].stackSize <= 0)
                {
                    this.cargoItems[i] = null;
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.itemID, 1, stackAt.getItemDamage()));
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_UNLOADED)
        {
            this.autoLaunch();
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public void onWorldTransferred(World world)
    {
        if (this.targetVec != null)
        {
            this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
            this.landing = true;
        }
        else
        {
            this.setDead();
        }
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.cargoItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.cargoItems[par1] != null)
        {
            final ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        return LanguageRegistry.instance().getStringLocalization("container.spaceship.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public boolean isInvNameLocalized()
    {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return this.isDead ? false : entityplayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        this.landingPad = pad;
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public void onPadDestroyed()
    {
        if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.dropShipAsItem();
            this.setDead();
        }
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof GCCoreTileEntityLandingPad || dock instanceof GCCoreTileEntityCargoPad;
    }

    @Override
    public int getRocketTier()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxFuel()
    {
        return this.spaceshipFuelTank.getCapacity();
    }

    @Override
    public int getPreLaunchWait()
    {
        return 20;
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(GCMarsItems.spaceship, 1, this.rocketType.getIndex() + 10));

        if (this.cargoItems != null)
        {
            for (final ItemStack item : this.cargoItems)
            {
                if (item != null)
                {
                    items.add(item);
                }
            }
        }

        return items;
    }

    @RuntimeInterface(clazz = "icbm.api.IMissileLockable", modID = "ICBM|Explosion")
    public boolean canLock(IMissile missile)
    {
        return true;
    }

    @RuntimeInterface(clazz = "icbm.api.IMissileLockable", modID = "ICBM|Explosion")
    public Vector3 getPredictedPosition(int ticks)
    {
        return new Vector3(this);
    }

    @RuntimeInterface(clazz = "icbm.api.sentry.IAATarget", modID = "ICBM|Explosion")
    public void destroyCraft()
    {
        this.setDead();
    }

    @RuntimeInterface(clazz = "icbm.api.sentry.IAATarget", modID = "ICBM|Explosion")
    public int doDamage(int damage)
    {
        return (int) (this.shipDamage += damage);
    }

    @RuntimeInterface(clazz = "icbm.api.sentry.IAATarget", modID = "ICBM|Explosion")
    public boolean canBeTargeted(Object entity)
    {
        return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.timeSinceLaunch > 50;
    }
}
