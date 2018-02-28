package micdoodle8.mods.galacticraft.api.prefab.entity;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.client.sounds.SoundUpdaterRocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.event.EventLandingPadRemoval;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.block.Block;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityAutoRocket extends EntitySpaceshipBase implements ILandable, IInventoryDefaults, IEntityNoisy
{
    public int destinationFrequency = -1;
    public BlockPos targetVec;
    public int targetDimension;
    protected ItemStack[] cargoItems;
    private IFuelDock landingPad;
    public EnumAutoLaunch autoLaunchSetting;
    public int autoLaunchCountdown;
    private BlockVec3 activeLaunchController;

    public String statusMessage;
    public String statusColour;
    public int statusMessageCooldown;
    public int lastStatusMessageCooldown;
    public boolean statusValid;
    protected double lastMotionY;
    protected double lastLastMotionY;
    private boolean waitForPlayer;
    protected ITickable rocketSoundUpdater;
    private boolean rocketSoundToStop = false;
    private static Class<?> controllerClass = null;

    static
    {
        try
        {
            controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");
        } catch (ClassNotFoundException e) 
        {
            GCLog.info("Galacticraft-Planets' LaunchController not present, rockets will not be launch controlled.");
        }
    }

    public EntityAutoRocket(World world)
    {
        super(world);
        
        if (world != null && world.isRemote)
        {
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
    }

    public EntityAutoRocket(World world, double posX, double posY, double posZ)
    {
        this(world);
        this.setSize(0.98F, 2F);
        this.setPosition(posX, posY, posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
    }

    //Used for Cargo Rockets, client is asking the server what is the status
    public boolean checkLaunchValidity()
    {
        this.statusMessageCooldown = 40;

        if (this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && !this.worldObj.isRemote)
            {
                if (!this.setFrequency())
                {
                    this.destinationFrequency = -1;
                    this.statusMessage = StatCollector.translateToLocal("gui.message.frequency.name") + "#" + StatCollector.translateToLocal("gui.message.not_set.name");
                    this.statusColour = "\u00a7c";
                    return false;
                }
                else
                {
                    this.statusMessage = StatCollector.translateToLocal("gui.message.success.name");
                    this.statusColour = "\u00a7a";
                    return true;
                }
            }
        }
        else
        {
            this.destinationFrequency = -1;
            this.statusMessage = StatCollector.translateToLocal("gui.message.not_enough.name") + "#" + StatCollector.translateToLocal("gui.message.fuel.name");
            this.statusColour = "\u00a7c";
            return false;
        }

        this.destinationFrequency = -1;
        return false;
    }

    public boolean setFrequency()
    {
        if (!GalacticraftCore.isPlanetsLoaded || controllerClass == null)
        {
            return false;
        }

        if (this.activeLaunchController != null)
        {
            TileEntity launchController = this.activeLaunchController.getTileEntity(this.worldObj);
            if (controllerClass.isInstance(launchController))
            {
                try
                {
                    Boolean b = (Boolean) controllerClass.getMethod("validFrequency").invoke(launchController);

                    if (b != null && b)
                    {
                        int controllerFrequency = controllerClass.getField("destFrequency").getInt(launchController);
                        boolean foundPad = this.setTarget(false, controllerFrequency);

                        if (foundPad)
                        {
                            this.destinationFrequency = controllerFrequency;
                            GCLog.debug("Rocket under launch control: going to target frequency " + controllerFrequency);
                            return true;
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        this.destinationFrequency = -1;
        return false;
    }

    protected boolean setTarget(boolean doSet, int destFreq)
    {
        WorldServer[] servers = GCCoreUtil.getWorldServerList(this.worldObj);
        if (!GalacticraftCore.isPlanetsLoaded || controllerClass == null)
        {
            return false;
        }

        for (int i = 0; i < servers.length; i++)
        {
            WorldServer world = servers[i];

            try
            {
                for (TileEntity tile : new ArrayList<TileEntity>(world.loadedTileEntityList))
                {
                    if (!controllerClass.isInstance(tile))
                        continue;

                    tile = world.getTileEntity(tile.getPos());
                    if (!controllerClass.isInstance(tile))
                        continue;

                    int controllerFrequency = controllerClass.getField("frequency").getInt(tile);

                    if (destFreq == controllerFrequency)
                    {
                        boolean targetSet = false;

                        blockLoop:
                            for (int x = -2; x <= 2; x++)
                            {
                                for (int z = -2; z <= 2; z++)
                                {
                                    BlockPos pos = new BlockPos(tile.getPos().add(x, 0, z));
                                    Block block = world.getBlockState(pos).getBlock();

                                    if (block instanceof BlockLandingPadFull)
                                    {
                                        if (doSet)
                                        {
                                            this.targetVec = pos;
                                        }

                                        targetSet = true;
                                        break blockLoop;
                                    }
                                }
                            }

                        if (doSet)
                        {
                            this.targetDimension = tile.getWorld().provider.getDimensionId();
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public int getScaledFuelLevel(int scale)
    {
        if (this.getFuelTankCapacity() <= 0)
        {
            return 0;
        }

        return this.fuelTank.getFluidAmount() * scale / this.getFuelTankCapacity() / ConfigManagerCore.rocketFuelFactor;
    }

    @Override
    public void onUpdate()
    {
        //Workaround for a weird bug (?) in vanilla 1.8.9 where - if client view distance is shorter
        //than the server's chunk loading distance - chunks will unload on the client, but the
        //entity will still be in the WorldClient.loadedEntityList.  This results in an entity which
        //is in the world, not dead and still updating on both server and client, but not in any chunk's
        //chunk.entityLists.  Also, it won't be reloaded into any chunk's entityLists when the chunk comes
        //back into viewing range - not sure why, maybe because it's already in the World.loadedEntityList?
        //Because it's now not in any chunk's entityLists, it cannot be iterated for rendering by RenderGlobal,
        //so it's gone invisible!
        
        //Tracing shows that Chunk.onChunkUnload() is called on the chunk clientside when the chunk goes
        //out of the view distance.  However, even after Chunk.onChunkUnload() - which should remove
        //this entity from the WorldClient.loadedEntityList - this entity stays in the world loadedEntityList.
        //That's why an onUpdate() tick is active for it, still!
        //Weird, huh?
        if (this.worldObj.isRemote && this.addedToChunk)
        {
            Chunk chunk = this.worldObj.getChunkFromChunkCoords(this.chunkCoordX, this.chunkCoordZ);
            int cx = MathHelper.floor_double(this.posX) >> 4;
            int cz = MathHelper.floor_double(this.posZ) >> 4;
            if (chunk.isChunkLoaded && this.chunkCoordX == cx && this.chunkCoordZ == cz)
            {
                boolean thisfound = false;
                ClassInheritanceMultiMap<Entity> mapEntities = chunk.getEntityLists()[this.chunkCoordY];
                for (Entity ent : mapEntities)
                {
                    if (ent == this)
                    {
                        thisfound = true;
                        break;
                    }
                }
                if (!thisfound)
                {
                    chunk.addEntity(this);
                }
            }
        }
        
        if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.hasValidFuel())
        {
            if (this.targetVec != null)
            {
                double yDiff = this.posY - this.getOnPadYOffset() - this.targetVec.getY();
                this.motionY = Math.max(-2.0, (yDiff - 0.04) / -55.0);

                //Some lateral motion in case not exactly on target (happens if rocket was moving laterally during launch)
                double diff = this.posX - this.targetVec.getX() - 0.5D;
                double motX, motZ;
                if (diff > 0D)
                {
                    motX = Math.max(-0.1, diff / -100.0D);
                }
                else if (diff < 0D)
                {
                    motX = Math.min(0.1, diff / -100.0D);
                }
                else motX = 0D;
                diff = this.posZ - this.targetVec.getZ() - 0.5D;
                if (diff > 0D)
                {
                    motZ = Math.max(-0.1, diff / -100.0D);
                }
                else if (diff < 0D)
                {
                    motZ = Math.min(0.1, diff / -100.0D);
                }
                else motZ = 0D;
                if (motZ != 0D || motX != 0D)
                {
                    double angleYaw = Math.atan(motZ / motX);
                    double signed = motX < 0 ? 50D : -50D;
                    double anglePitch = Math.atan(Math.sqrt(motZ * motZ + motX * motX) / signed) * 100D;
                    this.rotationYaw = (float)angleYaw * Constants.RADIANS_TO_DEGREES;
                    this.rotationPitch = (float)anglePitch * Constants.RADIANS_TO_DEGREES;
                }
                else
                    this.rotationPitch = 0F;
                
                if (yDiff > 1D && yDiff < 4D)
                {
                    for (Object o : this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().offset(0D, -3D, 0D), EntitySpaceshipBase.rocketSelector))
                    {
                        if (o instanceof EntitySpaceshipBase)
                        {
                            ((EntitySpaceshipBase)o).dropShipAsItem();
                            ((EntitySpaceshipBase)o).setDead();
                        }
                    }
                }
                if (yDiff < 0.4)
                {
                    int yMin = MathHelper.floor_double(this.getEntityBoundingBox().minY - this.getOnPadYOffset() - 0.45D) - 2;
                    int yMax = MathHelper.floor_double(this.getEntityBoundingBox().maxY) + 1;
                    int zMin = MathHelper.floor_double(this.posZ) - 1;
                    int zMax = MathHelper.floor_double(this.posZ) + 1;
                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            //Doing y as the inner loop may help with cacheing of chunks
                            for (int y = yMin; y <= yMax; y++)
                            {
                                if (this.worldObj.getTileEntity(new BlockPos(x, y, z)) instanceof IFuelDock)
                                {
                                    //Land the rocket on the pad found
                                    this.rotationPitch = 0F;
                                    this.failRocket();
                                }
                            }
                        }
                    }
                }
            }
        }

        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.statusMessageCooldown > 0)
            {
                this.statusMessageCooldown--;
            }

            if (this.statusMessageCooldown == 0 && this.lastStatusMessageCooldown > 0 && this.statusValid)
            {
                this.autoLaunch();
            }

            if (this.autoLaunchCountdown > 0 && (!(this instanceof EntityTieredRocket) || this.riddenByEntity != null))
            {
                if (--this.autoLaunchCountdown <= 0)
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.ROCKET_IS_FUELED && this.fuelTank.getFluidAmount() == this.fuelTank.getCapacity()  && (!(this instanceof EntityTieredRocket) || this.riddenByEntity != null))
            {
                this.autoLaunch();
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.INSTANT)
            {
                if (this.autoLaunchCountdown == 0  && (!(this instanceof EntityTieredRocket) || this.riddenByEntity != null))
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.REDSTONE_SIGNAL)
            {
                if (this.ticks % 11 == 0 && this.activeLaunchController != null)
                {
                    if (RedstoneUtil.isBlockReceivingRedstone(this.worldObj, this.activeLaunchController.toBlockPos()))
                    {
                        this.autoLaunch();
                    }
                }
            }

            if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
            {
                this.setPad(null);
            }
            else
            {
                if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && this.landingPad != null && this.ticks % 17 == 0)
                {
                    this.updateControllerSettings(this.landingPad);
                }
            }

            this.lastStatusMessageCooldown = this.statusMessageCooldown;          
        }

        if (this.launchPhase >= EnumLaunchPhase.IGNITED.ordinal())
        {
            if (this.rocketSoundUpdater != null)
            {
                this.rocketSoundUpdater.update();
                this.rocketSoundToStop = true;
            }
        }
        else
        {
            //Not ignited - either because not yet launched, or because it has landed
            if (this.rocketSoundToStop)
            {
                this.stopRocketSound();
                this.rocketSoundUpdater = null;
            }
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return false;
    }

    //Server side only - this is a Launch Controlled ignition attempt
    private void autoLaunch()
    {
        if (this.autoLaunchSetting != null)
        {
            if (this.activeLaunchController != null)
            {
                TileEntity tile = this.activeLaunchController.getTileEntity(this.worldObj);

                if (controllerClass.isInstance(tile))
                {
                    Boolean autoLaunchEnabled = null;
                    try
                    {
                        autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(tile);
                    } catch (Exception e) { }

                    if (autoLaunchEnabled != null && autoLaunchEnabled)
                    {
                        if (this.fuelTank.getFluidAmount() > this.fuelTank.getCapacity() * 2 / 5)
                            this.ignite();
                        else
                            this.failMessageInsufficientFuel();
                    }
                    else
                    {
                        this.failMessageLaunchController();
                    }
                }            
            }
            this.autoLaunchSetting = null;

            return;
        }
        else
        {
            this.ignite();
        }
    }

    public boolean igniteWithResult()
    {
        if (this.setFrequency())
        {
            super.ignite();
            this.activeLaunchController = null;
            return true;
        }
        else
        {
            if (this.isPlayerRocket())
            {
                super.ignite();
                this.activeLaunchController = null;
                return true;
            }

            this.activeLaunchController = null;
            return false;
        }
    }

    @Override
    public void ignite()
    {
        this.igniteWithResult();
    }

    public abstract boolean isPlayerRocket();

    @Override
    public void landEntity(BlockPos pos)
    {
        TileEntity tile = this.worldObj.getTileEntity(pos);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            if (this.isDockValid(dock))
            {
                if (!this.worldObj.isRemote)
                {
                    //Drop any existing rocket on the landing pad
                	if (dock.getDockedEntity() instanceof EntitySpaceshipBase && dock.getDockedEntity() != this)
                    {
                        ((EntitySpaceshipBase)dock.getDockedEntity()).dropShipAsItem();
                        ((EntitySpaceshipBase)dock.getDockedEntity()).setDead();
                    }

                    this.setPad(dock);
                }

                this.onRocketLand(pos);
            }
        }
    }

    public void updateControllerSettings(IFuelDock dock)
    {
        HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

        try
        {
            for (ILandingPadAttachable updatedTile : connectedTiles)
            {
                if (controllerClass.isInstance(updatedTile))
                {
                    //This includes a check for whether it has enough energy to run (if it doesn't, then a launch would not go to the destination frequency and the rocket would be lost!)
                    Boolean autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(updatedTile);

                    this.activeLaunchController = new BlockVec3((TileEntity) updatedTile);
                    
                    if (autoLaunchEnabled)
                    {
                        this.autoLaunchSetting = EnumAutoLaunch.values()[controllerClass.getField("launchDropdownSelection").getInt(updatedTile)];

                        switch (this.autoLaunchSetting)
                        {
                        case INSTANT:
                            //Small countdown to give player a moment to exit the Launch Controller GUI
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 12) this.autoLaunchCountdown = 12;
                            break;
                            //The other settings set time to count down AFTER player mounts rocket but BEFORE engine ignition
                            //TODO: if autoLaunchCountdown > 0 add some smoke (but not flame) particle effects or other pre-flight test feedback so the player knows something is happening
                        case TIME_10_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 200)
                                this.autoLaunchCountdown = 200;
                            break;
                        case TIME_30_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 600) this.autoLaunchCountdown = 600;
                            break;
                        case TIME_1_MINUTE:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 1200) this.autoLaunchCountdown = 1200;
                            break;
                        default:
                            break;
                        }
                    }
                    else
                    {
                        //This LaunchController is out of power, disabled, invalid target or set not to launch
                        //No auto launch - but maybe another connectedTile will have some launch settings?
                        this.autoLaunchSetting = null;
                        this.autoLaunchCountdown = 0;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onRocketLand(BlockPos pos)
    {
        this.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.4D + this.getOnPadYOffset(), pos.getZ() + 0.5, this.rotationYaw, 0.0F);
        this.stopRocketSound();
        this.rocketSoundUpdater = null;  //Allow sound to be restarted again if it relaunches
    }

    public void stopRocketSound()
    {
        if (this.rocketSoundUpdater != null)
        {
            ((SoundUpdaterRocket) this.rocketSoundUpdater).stopRocketSound();
        }
        this.rocketSoundToStop = false;
    }

    @Override
    public void setDead()
    {
        super.setDead();

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (!this.worldObj.isRemote)
        {
            double clientPosY = buffer.readDouble();
            if (clientPosY != Double.NaN && this.hasValidFuel())
            {
                if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
                {
                    if (clientPosY > this.posY)
                    {
                        this.motionY += (clientPosY - this.posY) / 40D;
                    }
                }
                else if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal())
                {
                    if (clientPosY < this.posY)
                    {
                        this.motionY += (clientPosY - this.posY) / 40D;
                    }
                }
            }
            return;
        }
        int launchPhaseLast = this.launchPhase;
        super.decodePacketdata(buffer);
        this.fuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, buffer.readInt()));
        boolean landingNew = buffer.readBoolean();
        if (landingNew && launchPhaseLast != EnumLaunchPhase.LANDING.ordinal())
        {
            this.rocketSoundUpdater = null;  //Flag TickHandlerClient to restart it
        }
        this.destinationFrequency = buffer.readInt();

        if (buffer.readBoolean())
        {
            this.targetVec = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        double motX = buffer.readDouble() / 8000.0D;
        double motY = buffer.readDouble() / 8000.0D;
        double motZ = buffer.readDouble() / 8000.0D;
        double lastMotY = buffer.readDouble() / 8000.0D;
        double lastLastMotY = buffer.readDouble() / 8000.0D;

        if (!this.hasValidFuel())
        {    
            this.motionX = motX;
            this.motionY = motY;
            this.motionZ = motZ;
            this.lastMotionY = lastMotY;
            this.lastLastMotionY = lastLastMotY;
        }

        if (this.cargoItems == null)
        {
            this.cargoItems = new ItemStack[this.getSizeInventory()];
        }

        this.setWaitForPlayer(buffer.readBoolean());

        this.statusMessage = ByteBufUtils.readUTF8String(buffer);
        this.statusMessage = this.statusMessage.equals("") ? null : this.statusMessage;
        this.statusMessageCooldown = buffer.readInt();
        this.lastStatusMessageCooldown = buffer.readInt();
        this.statusValid = buffer.readBoolean();

        //Update client with correct rider if needed
        if (this.worldObj.isRemote)
        {
            int shouldBeMountedId = buffer.readInt();
            if (this.riddenByEntity == null)
            {
                if (shouldBeMountedId > -1)
                {
                    Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
                    if (e != null)
                    {
                        if (e.dimension != this.dimension)
                        {
                            if (e instanceof EntityPlayer)
                            {
                                e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).theItemInWorldManager.getGameType().getID());
                                e.mountEntity(this);
                            }
                        }
                        else
                            e.mountEntity(this);
                    }
                }
            }
            else if (this.riddenByEntity.getEntityId() != shouldBeMountedId)
            {
                if (shouldBeMountedId == -1)
                {
                    this.riddenByEntity.mountEntity(null);
                }
                else
                {
                    Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
                    if (e != null)
                    {
                        if (e.dimension != this.dimension)
                        {
                            if (e instanceof EntityPlayer)
                            {
                                e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).theItemInWorldManager.getGameType().getID());
                                e.mountEntity(this);
                            }
                        }
                        else
                            e.mountEntity(this);
                    }
                }
            }
        }
        this.statusColour = ByteBufUtils.readUTF8String(buffer);
        if (this.statusColour.equals("")) this.statusColour = null;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        if (this.worldObj.isRemote)
        {
            if (this.riddenByEntity == FMLClientHandler.instance().getClientPlayerEntity() && this.hasValidFuel())
            {
                list.add(this.posY);
            }
            else
            {
                list.add(Double.NaN);
            }
        }
        super.getNetworkedData(list);

        list.add(this.fuelTank.getFluidAmount());
        list.add(this.launchPhase == EnumLaunchPhase.LANDING.ordinal());
        list.add(this.destinationFrequency);
        list.add(this.targetVec != null);

        if (this.targetVec != null)
        {
            list.add(this.targetVec.getX());
            list.add(this.targetVec.getY());
            list.add(this.targetVec.getZ());
        }

        list.add(this.motionX * 8000.0D);
        list.add(this.motionY * 8000.0D);
        list.add(this.motionZ * 8000.0D);
        list.add(this.lastMotionY * 8000.0D);
        list.add(this.lastLastMotionY * 8000.0D);

        list.add(this.getWaitForPlayer());

        list.add(this.statusMessage != null ? this.statusMessage : "");
        list.add(this.statusMessageCooldown);
        list.add(this.lastStatusMessageCooldown);
        list.add(this.statusValid);

        if (!this.worldObj.isRemote)
        {
            list.add(this.riddenByEntity == null ? -1 : this.riddenByEntity.getEntityId());
        }
        list.add(this.statusColour != null ? this.statusColour : "");
    }

    @Override
    protected void failRocket()
    {
        this.stopRocketSound();

        if (this.shouldCancelExplosion())
        {
            //TODO: why looking around when already know the target?
            //TODO: it would be good to land on an alternative neighbouring pad if there is already a rocket on the target pad
            for (int i = -3; i <= 3; i++)
            {
                BlockPos pos = new BlockPos((int) Math.floor(this.posX), (int) Math.floor(this.posY + i), (int) Math.floor(this.posZ));
                if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal() && this.targetVec != null && this.worldObj.getTileEntity(pos) instanceof IFuelDock && this.posY - this.targetVec.getY() < 5)
                {
                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                    {
                        for (int y = MathHelper.floor_double(this.posY - 3.0D); y <= MathHelper.floor_double(this.posY) + 1; y++)
                        {
                            for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                            {
                                BlockPos pos1 = new BlockPos(x, y, z);
                                TileEntity tile = this.worldObj.getTileEntity(pos1);

                                if (tile instanceof IFuelDock)
                                {
                                    this.landEntity(pos1);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal())
        {
            super.failRocket();
        }
    }

    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel();
    }

    @Override
    public boolean hasValidFuel()
    {
        return this.fuelTank.getFluidAmount() > 0;
    }

    public void cancelLaunch()
    {
        this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
        this.timeUntilLaunch = 0;
        if (!this.worldObj.isRemote && this.riddenByEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) this.riddenByEntity).addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.nogyroscope")));
        }
    }

    public void failMessageLaunchController()
    {
        if (!this.worldObj.isRemote && this.riddenByEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) this.riddenByEntity).addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.launchcontroller")));
        }
    }

    public void failMessageInsufficientFuel()
    {
        if (!this.worldObj.isRemote && this.riddenByEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) this.riddenByEntity).addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.fuelinsufficient")));
        }
    }

    @Override
    public void onLaunch()
    {
        if (!(this.worldObj.provider.getDimensionId() == GalacticraftCore.planetOverworld.getDimensionID() || this.worldObj.provider instanceof IGalacticraftWorldProvider))
        {
            if (ConfigManagerCore.disableRocketLaunchAllNonGC)
            {
                this.cancelLaunch();
                return;
            }

            //No rocket flight in the Nether, the End etc
            for (int i = ConfigManagerCore.disableRocketLaunchDimensions.length - 1; i >= 0; i--)
            {
                if (ConfigManagerCore.disableRocketLaunchDimensions[i] == this.worldObj.provider.getDimensionId())
                {
                    this.cancelLaunch();
                    return;
                }
            }

        }

        super.onLaunch();

        if (!this.worldObj.isRemote)
        {
            GCPlayerStats stats = null;

            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
            {
                stats = GCPlayerStats.get(this.riddenByEntity);

                if (!(this.worldObj.provider instanceof IOrbitDimension))
                {
                    stats.setCoordsTeleportedFromX(this.riddenByEntity.posX);
                    stats.setCoordsTeleportedFromZ(this.riddenByEntity.posZ);
                }
            }

            int amountRemoved = 0;

            PADSEARCH:
                for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                {
                    for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
                    {
                        for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            final Block block = this.worldObj.getBlockState(pos).getBlock();

                            if (block != null && block instanceof BlockLandingPadFull)
                            {
                                if (amountRemoved < 9)
                                {
                                    EventLandingPadRemoval event = new EventLandingPadRemoval(this.worldObj, pos);
                                    MinecraftForge.EVENT_BUS.post(event);

                                    if (event.allow)
                                    {
                                        this.worldObj.setBlockToAir(pos);
                                        amountRemoved = 9;
                                    }
                                    break PADSEARCH;
                                }
                            }
                        }
                    }
                }

            //Set the player's launchpad item for return on landing - or null if launchpads not removed
            if (stats != null)
            {
                stats.setLaunchpadStack(amountRemoved == 9 ? new ItemStack(GCBlocks.landingPad, 9, 0) : null);
            }

            this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
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
            nbt.setDouble("targetTileX", this.targetVec.getX());
            nbt.setDouble("targetTileY", this.targetVec.getY());
            nbt.setDouble("targetTileZ", this.targetVec.getZ());
        }

        nbt.setBoolean("WaitingForPlayer", this.getWaitForPlayer());
        nbt.setBoolean("Landing", this.launchPhase == EnumLaunchPhase.LANDING.ordinal());
        nbt.setInteger("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
        nbt.setInteger("TimeUntilAutoLaunch", this.autoLaunchCountdown);
        nbt.setInteger("DestinationFrequency", this.destinationFrequency);
        if (this.activeLaunchController != null) this.activeLaunchController.writeToNBT(nbt,"ALCat");
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = nbt.getTagList("Items", 10);
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                final int var5 = var4.getByte("Slot") & 255;

                if (var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }

        if (nbt.getBoolean("TargetValid") && nbt.hasKey("targetTileX"))
        {
            this.targetVec = new BlockPos(MathHelper.floor_double(nbt.getDouble("targetTileX")), MathHelper.floor_double(nbt.getDouble("targetTileY")), MathHelper.floor_double(nbt.getDouble("targetTileZ")));
        }

        this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));
        int autoLaunchValue = nbt.getInteger("AutoLaunchSetting");
        this.autoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
        this.autoLaunchCountdown = nbt.getInteger("TimeUntilAutoLaunch");
        this.destinationFrequency = nbt.getInteger("DestinationFrequency");
        this.activeLaunchController = BlockVec3.readFromNBT(nbt, "ALCat");
    }

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
        return FluidUtil.fillWithGCFuel(this.fuelTank, liquid, doFill);
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.fuelTank.drain(amount * ConfigManagerCore.rocketFuelFactor, true);
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        //Called either when a rocket lands or when one is placed
        //Can also be called with null param when rocket leaves a pad
        this.landingPad = pad;
        if (pad != null)
        {
            pad.dockEntity(this);
            //NOTE: setPad() is called also when a world or chunk is loaded - if the rocket is Ignited (from NBT save data) do not change those settings
	    	if (!(this.launchPhase == EnumLaunchPhase.IGNITED.ordinal())) 
	    	{ 
	    	    this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
	    	    this.stopRocketSound();
	    	    this.targetVec = null;
	    	    if (GalacticraftCore.isPlanetsLoaded)
	    	    {
	    	        this.updateControllerSettings(pad);
	    	    }
	    	}
        }
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public int getMaxFuel()
    {
        return this.fuelTank.getCapacity();
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return (dock instanceof TileEntityLandingPad);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.getSizeInventory() <= 3)
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

            if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        this.cargoItems[count].stackSize += stack.stackSize;
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.stackSize;
                    int surplus = origSize + stack.stackSize - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        this.cargoItems[count].stackSize = stackAt.getMaxStackSize();
                        this.markDirty();
                    }

                    stack.stackSize = surplus;
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    this.cargoItems[count].stackSize = origSize;
                    if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
                    {
                        this.autoLaunch();
                    }
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.cargoItems.length - 2; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.cargoItems[count] = stack;
                    this.markDirty();
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
                ItemStack resultStack = stackAt.copy();
                resultStack.stackSize = 1;

                if (doRemove && --stackAt.stackSize <= 0)
                {
                    this.cargoItems[i] = null;
                }

                if (doRemove)
                {
                    this.markDirty();
                }
                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_UNLOADED)
        {
            this.autoLaunch();
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (this.cargoItems == null) return null; 

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
    public ItemStack removeStackFromSlot(int par1)
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
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return !this.isDead && entityplayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public void onPadDestroyed()
    {
        if (!this.isDead && this.launchPhase < EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.dropShipAsItem();
            this.setDead();
        }
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
    {
        if (this.cargoItems != null)
        {
            for (final ItemStack item : this.cargoItems)
            {
                if (item != null)
                {
                    droppedItemList.add(item);
                }
            }
        }

        return droppedItemList;
    }

    public boolean getWaitForPlayer()
    {
        return this.waitForPlayer;
    }

    public void setWaitForPlayer(boolean waitForPlayer)
    {
        this.waitForPlayer = waitForPlayer;
    }

    public static enum EnumAutoLaunch
    {
        CARGO_IS_UNLOADED(0, "cargo_unloaded"),
        CARGO_IS_FULL(1, "cargo_full"),
        ROCKET_IS_FUELED(2, "fully_fueled"),
        INSTANT(3, "instant"),
        TIME_10_SECONDS(4, "ten_sec"),
        TIME_30_SECONDS(5, "thirty_sec"),
        TIME_1_MINUTE(6, "one_min"),
        REDSTONE_SIGNAL(7, "redstone_sig");

        private final int index;
        private String title;

        private EnumAutoLaunch(int index, String title)
        {
            this.index = index;
            this.title = title;
        }

        public int getIndex()
        {
            return this.index;
        }

        public String getTitle()
        {
            return GCCoreUtil.translate("gui.message." + this.title + ".name");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITickable getSoundUpdater()
    {
        return this.rocketSoundUpdater;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ISound setSoundUpdater(EntityPlayerSP player)
    {
        this.rocketSoundUpdater = new SoundUpdaterRocket(player, this);
        return (ISound) this.rocketSoundUpdater;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        double d0 = this.posX - x;
        double d1 = this.posY - y;
        double d2 = this.posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return d3 < Constants.RENDERDISTANCE_LONG;
    }
    
    @Override
    public boolean inFlight()
    {
        return this.getLaunched();
    }
}
