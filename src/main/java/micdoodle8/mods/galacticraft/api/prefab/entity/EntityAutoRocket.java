package micdoodle8.mods.galacticraft.api.prefab.entity;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IEntityNoisy;
import micdoodle8.mods.galacticraft.api.entity.ILandable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.sounds.SoundUpdaterRocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.event.EventLandingPadRemoval;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityAutoRocket extends EntitySpaceshipBase implements ILandable, IInventory, IPacketReceiver, IEntityNoisy
{
    public FluidTank fuelTank = new FluidTank(this.getFuelTankCapacity() * ConfigManagerCore.rocketFuelFactor);
    public int destinationFrequency = -1;
    public BlockVec3 targetVec;
    public int targetDimension;
    protected ItemStack[] cargoItems;
    private IFuelDock landingPad;
    public boolean landing;
    public EnumAutoLaunch autoLaunchSetting;

    public int autoLaunchCountdown;
    public String statusMessage;
    public String statusColour;
    public int statusMessageCooldown;
    public int lastStatusMessageCooldown;
    public boolean statusValid;
    protected double lastMotionY;
    protected double lastLastMotionY;
    private boolean waitForPlayer;
    protected IUpdatePlayerListBox rocketSoundUpdater;
    private boolean rocketSoundToStop = false;

    public EntityAutoRocket(World world)
    {
        super(world);
        this.yOffset = 0;
    }

    public EntityAutoRocket(World world, double posX, double posY, double posZ)
    {
        this(world);
        this.setSize(0.98F, 2F);
        this.yOffset = 0;
        this.setPosition(posX, posY, posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
    }

    public abstract int getFuelTankCapacity();

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
                    this.statusMessage = StatCollector.translateToLocal("gui.message.frequency.name") + "#" + StatCollector.translateToLocal("gui.message.notSet.name");
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
            this.statusMessage = StatCollector.translateToLocal("gui.message.notEnough.name") + "#" + StatCollector.translateToLocal("gui.message.fuel.name");
            this.statusColour = "\u00a7c";
            return false;
        }

        this.destinationFrequency = -1;
        return false;
    }

    public boolean setFrequency()
    {
        if (!GalacticraftCore.isPlanetsLoaded)
        {
            return false;
        }

        for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
        {
            for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
            {
                for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                {
                    TileEntity tile = this.worldObj.getTileEntity(x, y, z);

                    if (tile instanceof IFuelDock)
                    {
                        IFuelDock dock = (IFuelDock) tile;

                        try
                        {
                            TileEntity launchController = null;
                            Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");

                            for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
                            {
                                try
                                {
                                    controllerClass.cast(connectedTile);
                                }
                                catch (ClassCastException e)
                                {
                                    continue;
                                }

                                launchController = (TileEntity) connectedTile;
                                if (launchController != null)
                                {
                                    TileEntity tile2 = launchController.getWorldObj().getTileEntity(launchController.xCoord, launchController.yCoord, launchController.zCoord);

                                    try
                                    {
                                        controllerClass.cast(tile2);
                                    }
                                    catch (ClassCastException e)
                                    {
                                        launchController = null;
                                        continue;
                                    }

                                    launchController = tile2;
                                }

                                if (launchController != null)
                                {
                                    break;
                                }
                            }

                            if (launchController != null)
                            {
                                Boolean b = (Boolean) controllerClass.getMethod("validFrequency").invoke(launchController);

                                if (b != null && b)
                                {
                                    int controllerFrequency = controllerClass.getField("destFrequency").getInt(launchController);
                                    boolean foundPad = this.setTarget(false, controllerFrequency);

                                    if (foundPad)
                                    {
                                        this.destinationFrequency = controllerFrequency;
                                        return true;
                                    }
                                }
                            }
                        }
                        catch (ClassCastException e)
                        {
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        this.destinationFrequency = -1;
        return false;
    }

    protected boolean setTarget(boolean doSet, int destFreq)
    {
    	if (!GalacticraftCore.isPlanetsLoaded || FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().worldServers == null)
        {
            return false;
        }

        WorldServer[] servers = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

        for (int i = 0; i < servers.length; i++)
        {
            WorldServer world = servers[i];

            try
            {
                Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");
                
                for (TileEntity tile : new ArrayList<TileEntity>(world.loadedTileEntityList))
                {
                	if (tile != null)
                	{
                		tile = world.getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
                		if (tile == null) continue;

                		try
                		{
                			controllerClass.cast(tile);
                		}
                		catch (ClassCastException e)
                		{
                			continue;
                		}

                		int controllerFrequency = controllerClass.getField("frequency").getInt(tile);

                		if (destFreq == controllerFrequency)
                		{
                			boolean targetSet = false;

                			blockLoop:
                				for (int x = -2; x <= 2; x++)
                				{
                					for (int z = -2; z <= 2; z++)
                					{
                						Block block = world.getBlock(tile.xCoord + x, tile.yCoord, tile.zCoord + z);

                						if (block instanceof BlockLandingPadFull)
                						{
                							if (doSet)
                							{
                								this.targetVec = new BlockVec3(tile.xCoord + x, tile.yCoord, tile.zCoord + z);
                							}

                							targetSet = true;
                							break blockLoop;
                						}
                					}
                				}

                			if (doSet)
                			{
                				this.targetDimension = tile.getWorldObj().provider.dimensionId;
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
                this.autoLaunchCountdown--;

                if (this.autoLaunchCountdown <= 0)
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
                if (this.ticks % 25 == 0)
                {
                    if (this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
                    {
                        for (ILandingPadAttachable tile : this.getLandingPad().getConnectedTiles())
                        {
                            if (this.worldObj.getTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) != null)
                            {
                                try
                                {
                                    Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");

                                    try
                                    {
                                        controllerClass.cast(this.worldObj.getTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord));
                                    }
                                    catch (ClassCastException e)
                                    {
                                        continue;
                                    }

                                    if (this.worldObj.isBlockIndirectlyGettingPowered(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord))
                                    {
                                        this.autoLaunch();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
            {
                if (this.landing && this.targetVec != null && this.worldObj.getTileEntity(this.targetVec.x, this.targetVec.y, this.targetVec.z) instanceof IFuelDock)
                {
                	this.motionY = Math.max(-2.0F, (this.posY - this.getOnPadYOffset() - 0.4D - this.targetVec.y) / -70.0D);
                	
                	if (this.boundingBox.minY - this.targetVec.y < 0.5F)
	                {
	                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
	                    {
	                        for (int y = MathHelper.floor_double(this.boundingBox.minY - this.getOnPadYOffset() - 0.45D) - 1; y <= MathHelper.floor_double(this.boundingBox.maxY) + 1; y++)
	                        {
	                            for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
	                            {
	                                TileEntity tile = this.worldObj.getTileEntity(x, y, z);
	
	                                if (tile instanceof IFuelDock)
	                                {
	                                    this.failRocket();
	                                }
	                            }
	                        }
	                    }
	                }
                }
            }

            if (this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
            {
                for (ILandingPadAttachable tile : this.getLandingPad().getConnectedTiles())
                {
                    if (this.worldObj.getTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) != null && this.worldObj.getTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) instanceof TileEntityFuelLoader)
                    {
                        if (tile instanceof TileEntityFuelLoader && ((TileEntityFuelLoader) tile).getEnergyStoredGC() > 0)
                        {
                            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
                            {
                                this.setPad(null);
                            }
                        }
                    }
                }
            }

            this.lastStatusMessageCooldown = this.statusMessageCooldown;          
        }
        
        if (this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() || this.getLaunched())
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
        		this.stopRocketSound();
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return false;
    }

    private void autoLaunch()
    {
        this.ignite();
        this.autoLaunchSetting = null;
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
            if (this.isPlayerRocket())
            {
                super.ignite();
                return true;
            }

            return false;
        }
    }

    @Override
    public void ignite()
    {
        this.igniteWithResult();
    }

    public abstract boolean isPlayerRocket();

    public void landEntity(int x, int y, int z)
    {
        TileEntity tile = this.worldObj.getTileEntity(x, y, z);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            if (this.isDockValid(dock))
            {
                if (!this.worldObj.isRemote)
                {
                    //Drop any existing rocket on the landing pad
                	if (dock.getDockedEntity() instanceof EntitySpaceshipBase)
                    {
                    	((EntitySpaceshipBase)dock.getDockedEntity()).dropShipAsItem();
                    	((EntitySpaceshipBase)dock.getDockedEntity()).setDead();
                    }
                	
                    this.setPad(dock);
                }

                this.onRocketLand(x, y, z);
            }
        }
    }

    public void updateControllerSettings(IFuelDock dock)
    {
        HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

        try
        {
            Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController");

            for (ILandingPadAttachable connectedTile : connectedTiles)
            {
                if (connectedTile != null)
                {
                    TileEntity updatedTile = this.worldObj.getTileEntity(((TileEntity) connectedTile).xCoord, ((TileEntity) connectedTile).yCoord, ((TileEntity) connectedTile).zCoord);

                    try
                    {
                        controllerClass.cast(updatedTile);
                    }
                    catch (ClassCastException e)
                    {
                        continue;
                    }

                    controllerClass.getField("attachedDock").set(updatedTile, dock);

                    Boolean autoLaunchEnabled = controllerClass.getField("launchSchedulingEnabled").getBoolean(updatedTile);

                    if (autoLaunchEnabled)
                    {
                        this.autoLaunchSetting = EnumAutoLaunch.values()[controllerClass.getField("launchDropdownSelection").getInt(updatedTile)];

                        switch (this.autoLaunchSetting)
                        {
                        case INSTANT:
                            //Small countdown to give player a moment to jump out of the rocket
                            this.autoLaunchCountdown = 12;
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
                    else
                    {
                        this.autoLaunchSetting = null;
                    }

                    break;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onRocketLand(int x, int y, int z)
    {
        this.setPositionAndRotation(x + 0.5, y + 0.4D + this.getOnPadYOffset(), z + 0.5, this.rotationYaw, 0.0F);
        this.stopRocketSound();
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
        super.decodePacketdata(buffer);
        this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, buffer.readInt()));
        this.landing = buffer.readBoolean();
        this.destinationFrequency = buffer.readInt();

        if (buffer.readBoolean())
        {
            this.targetVec = new BlockVec3(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        this.motionX = buffer.readDouble() / 8000.0D;
        this.motionY = buffer.readDouble() / 8000.0D;
        this.motionZ = buffer.readDouble() / 8000.0D;
        this.lastMotionY = buffer.readDouble() / 8000.0D;
        this.lastLastMotionY = buffer.readDouble() / 8000.0D;

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
	        					 e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.difficultySetting.getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).theItemInWorldManager.getGameType().getID());
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
	       		 				e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.difficultySetting.getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).theItemInWorldManager.getGameType().getID());
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
    public void handlePacketData(Side side, EntityPlayer player)
    {
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        super.getNetworkedData(list);

        list.add(this.fuelTank.getFluidAmount());
        list.add(this.landing);
        list.add(this.destinationFrequency);
        list.add(this.targetVec != null);

        if (this.targetVec != null)
        {
            list.add(this.targetVec.x);
            list.add(this.targetVec.y);
            list.add(this.targetVec.z);
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
        if (this.shouldCancelExplosion())
        {
            for (int i = -3; i <= 3; i++)
            {
                if (this.landing && this.targetVec != null && this.worldObj.getTileEntity((int) Math.floor(this.posX), (int) Math.floor(this.posY + i), (int) Math.floor(this.posZ)) instanceof IFuelDock && this.posY - this.targetVec.y < 5)
                {
                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                    {
                        for (int y = MathHelper.floor_double(this.posY - 3.0D); y <= MathHelper.floor_double(this.posY) + 1; y++)
                        {
                            for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                            {
                                TileEntity tile = this.worldObj.getTileEntity(x, y, z);

                                if (tile instanceof IFuelDock)
                                {
                                    this.landEntity(x, y, z);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
        {
            super.failRocket();
        }
    }

    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel();
    }

    public boolean hasValidFuel()
    {
        return this.fuelTank.getFluidAmount() > 0;
    }

    @Override
    public void onLaunch()
    {
        if (!(this.worldObj.provider instanceof WorldProviderSurface || this.worldObj.provider instanceof IGalacticraftWorldProvider))
        {
            for (int i = ConfigManagerCore.disableRocketLaunchDimensions.length - 1; i >= 0; i--)
            {
                if (ConfigManagerCore.disableRocketLaunchDimensions[i] == this.worldObj.provider.dimensionId)
                {
                    //No rocket flight in the Nether, the End etc
                    this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
                    this.timeUntilLaunch = 0;
                    if (!this.worldObj.isRemote && this.riddenByEntity instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP) this.riddenByEntity).addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.rocket.warning.nogyroscope")));
                    }
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
                stats = GCPlayerStats.get((EntityPlayerMP) this.riddenByEntity);

                if (!(this.worldObj.provider instanceof IOrbitDimension))
                {
	                stats.coordsTeleportedFromX = this.riddenByEntity.posX;
	                stats.coordsTeleportedFromZ = this.riddenByEntity.posZ;
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
                        final Block block = this.worldObj.getBlock(x, y, z);

                        if (block != null && block instanceof BlockLandingPadFull)
                        {
                            if (amountRemoved < 9)
                            {
                                EventLandingPadRemoval event = new EventLandingPadRemoval(this.worldObj, x, y, z);
                                MinecraftForge.EVENT_BUS.post(event);

                                if (event.allow)
                                {
                                    this.worldObj.setBlockToAir(x, y, z);
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
                stats.launchpadStack = amountRemoved == 9 ? new ItemStack(GCBlocks.landingPad, 9, 0) : null;
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
            nbt.setDouble("targetTileX", this.targetVec.x);
            nbt.setDouble("targetTileY", this.targetVec.y);
            nbt.setDouble("targetTileZ", this.targetVec.z);
        }

        nbt.setBoolean("WaitingForPlayer", this.getWaitForPlayer());
        nbt.setBoolean("Landing", this.landing);
        nbt.setInteger("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
        nbt.setInteger("TimeUntilAutoLaunch", this.autoLaunchCountdown);
        nbt.setInteger("DestinationFrequency", this.destinationFrequency);
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
            this.targetVec = new BlockVec3(MathHelper.floor_double(nbt.getDouble("targetTileX")), MathHelper.floor_double(nbt.getDouble("targetTileY")), MathHelper.floor_double(nbt.getDouble("targetTileZ")));
        }

        this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));
        this.landing = nbt.getBoolean("Landing");
        int autoLaunchValue = nbt.getInteger("AutoLaunchSetting");
        this.autoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
        this.autoLaunchCountdown = nbt.getInteger("TimeUntilAutoLaunch");
        this.destinationFrequency = nbt.getInteger("DestinationFrequency");
    }

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
        if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("fuel"))
        {
            return this.fuelTank.fill(liquid, doFill);
        }

        return 0;
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
	    	this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
	        this.landing = false;
	        this.targetVec = null;
	        if (GalacticraftCore.isPlanetsLoaded)
	        {
	            this.updateControllerSettings(pad);
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
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.spaceship.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void openInventory()
    {
    }

    @Override
    public void closeInventory()
    {
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
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
        if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
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
        CARGO_IS_UNLOADED(0, "cargoUnloaded"),
        CARGO_IS_FULL(1, "cargoFull"),
        ROCKET_IS_FUELED(2, "fullyFueled"),
        INSTANT(3, "instant"),
        TIME_10_SECONDS(4, "tenSec"),
        TIME_30_SECONDS(5, "thirtySec"),
        TIME_1_MINUTE(6, "oneMin"),
        REDSTONE_SIGNAL(7, "redstoneSig");

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

    @SideOnly(Side.CLIENT)
    public IUpdatePlayerListBox getSoundUpdater()
    {
    	return this.rocketSoundUpdater;
    }
    
    @SideOnly(Side.CLIENT)
    public ISound setSoundUpdater(EntityPlayerSP player)
    {
    	this.rocketSoundUpdater = new SoundUpdaterRocket(player, this);
    	return (ISound) this.rocketSoundUpdater;
    }
}
