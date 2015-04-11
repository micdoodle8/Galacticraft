package micdoodle8.mods.galacticraft.api.prefab.entity;

import cpw.mods.fml.common.FMLCommonHandler;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.entity.IWorldTransferCallback;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityTieredRocket extends EntityAutoRocket implements IRocketType, IDockable, IInventory, IWorldTransferCallback, ICameraZoomEntity
{
    public EnumRocketType rocketType;
    public float rumble;
    public int launchCooldown;
    private ArrayList<BlockVec3> preGenList = new ArrayList();
    private Iterator<BlockVec3> preGenIterator = null;
    private MinecraftServer mcserver = FMLCommonHandler.instance().getMinecraftServerInstance();
    
    public EntityTieredRocket(World par1World)
    {
        super(par1World);
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
    }

    public EntityTieredRocket(World world, double posX, double posY, double posZ)
    {
        super(world, posX, posY, posZ);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();

	//TODO reimplement once Resonant Engine comes out of alpha, bug DarkGuardsman for info
        //if (Loader.isModLoaded("ICBM|Explosion"))
        //{
        //    try
        //    {
        //        Class.forName("icbm.api.RadarRegistry").getMethod("register", Entity.class).invoke(null, this);
        //    }
        //    catch (Exception e)
        //    {
        //        e.printStackTrace();
        //    }
        //}
    }

    @Override
    public void setDead()
    {
        super.setDead();

	//TODO reimplement once Resonant Engine comes out of alpha, bug Dark for info
        //if (Loader.isModLoaded("ICBM|Explosion"))
        //{
        //    try
        //    {
        //        Class.forName("icbm.api.RadarRegistry").getMethod("unregister", Entity.class).invoke(null, this);
        //    }
        //    catch (Exception e)
        //    {
        //        e.printStackTrace();
        //    }
        //}
    }

    public void igniteCheckingCooldown()
    {
        if (!this.worldObj.isRemote && this.launchCooldown <= 0)
        {
            this.initiatePlanetsPreGen(this.chunkCoordX, this.chunkCoordZ);

            this.ignite();
        }
    }

    private void initiatePlanetsPreGen(int cx, int cz)
    {
        this.preGenList.clear();

        //Pre-generate terrain on all possible destination planets if the destination is not being controlled by a Launch Controller
        //(note: this does NOT include the Moon!)

        //This generates with a chunk radius of 12: so for 2 planets that's 1250 chunks to pregen
        //It starts at the centre and generates in circles radiating out in case it doesn't have time to finish
        //These will be done: 2 chunks per tick during IGNITE phase (so 800 chunks during the 20 second launch countdown)
        //then the ones that are left 1 chunk per tick during flight (normally flight will last more than 450 ticks)
        //If the server is at less than 20tps then maybe some of the outermost chunks won't be pre-generated but that's probably OK
        if (this.destinationFrequency == -1)
        {
            ArrayList<Integer> toPreGen = new ArrayList();
            for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
            {
                if (planet.getDimensionID() == this.dimension)
                {
                    continue;
                }
                if (planet.getReachable() && planet.getTierRequirement() <= this.getRocketTier())
                {
                    toPreGen.add(planet.getDimensionID());
                }
            }

            if (toPreGen.size() > 0)
            {
                for (Integer dimID : toPreGen)
                {
                    this.preGenList.add(new BlockVec3(cx, dimID, cz));
                    if (ConfigManagerCore.enableDebug)
                    {
                        GCLog.info("Starting terrain pregen for dimension " + dimID + " at " + (cx * 16 + 8) + ", " + (cz * 16 + 8));
                    }
                }
                for (int r = 1; r < 12; r++)
                {
                    int xmin = cx - r;
                    int xmax = cx + r;
                    int zmin = cz - r;
                    int zmax = cz + r;
                    for (int i = -r; i < r; i++)
                    {
                        for (Integer dimID : toPreGen)
                        {
                            this.preGenList.add(new BlockVec3(xmin, dimID, cz + i));
                            this.preGenList.add(new BlockVec3(xmax, dimID, cz - i));
                            this.preGenList.add(new BlockVec3(cx - i, dimID, zmin));
                            this.preGenList.add(new BlockVec3(cx + i, dimID, zmax));
                        }
                    }
                }
                this.preGenIterator = this.preGenList.iterator();
            }
        }
        else
        {
            this.preGenIterator = null;
        }
    }

    @Override
    public void onUpdate()
    {
        if (this.getWaitForPlayer())
        {
            if (this.riddenByEntity != null)
            {
                if (this.ticks >= 40)
                {
                    if (!this.worldObj.isRemote)
                    {
                        Entity e = this.riddenByEntity;
                        e.mountEntity(null);
                        e.mountEntity(this);
                        if (ConfigManagerCore.enableDebug) GCLog.info("Remounting player in rocket.");
                    }

                    this.setWaitForPlayer(false);
                    this.motionY = -0.5D;
                }
                else
                {
                    this.motionX = this.motionY = this.motionZ = 0.0D;
                    this.riddenByEntity.motionX = this.riddenByEntity.motionY = this.riddenByEntity.motionZ = 0;
                }
            }
            else
            {
                this.motionX = this.motionY = this.motionZ = 0.0D;
            }
        }

        super.onUpdate();

        if (this.landing)
        {
            this.rotationPitch = this.rotationYaw = 0;
        }

        if (!this.worldObj.isRemote)
        {
            if (this.launchCooldown > 0)
            {
                this.launchCooldown--;
            }

            if (this.preGenIterator != null)
            {
                if (this.preGenIterator.hasNext())
                {
                    BlockVec3 coords = this.preGenIterator.next();
                    World w = this.mcserver.worldServerForDimension(coords.y);
                    w.getChunkFromChunkCoords(coords.x, coords.z);
                    //Pregen a second chunk if still on launchpad (low strain on server)
                    if (this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal() && this.preGenIterator.hasNext())
                    {
                        coords = this.preGenIterator.next();
                        w = this.mcserver.worldServerForDimension(coords.y);
                        w.getChunkFromChunkCoords(coords.x, coords.z);
                    }
                }
                else
                {
                    this.preGenIterator = null;
                }
            }
        }

        if (!this.worldObj.isRemote && this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
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

        if (this.rumble > 0)
        {
            this.rumble--;
        }
        else if (this.rumble < 0)
        {
            this.rumble++;
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.posX += this.rumble / (37F - 5F * this.getRocketTier());
            this.riddenByEntity.posZ += this.rumble / (37F - 5F * this.getRocketTier());
        }

        if (this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() || this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.performHurtAnimation();

            this.rumble = (float) this.rand.nextInt(3) - 3;
        }

        if (!this.worldObj.isRemote)
        {
            this.lastLastMotionY = this.lastMotionY;
            this.lastMotionY = this.motionY;
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        this.rocketType = EnumRocketType.values()[buffer.readInt()];
        super.decodePacketdata(buffer);

        if (buffer.readBoolean())
        {
            this.posX = buffer.readDouble() / 8000.0D;
            this.posY = buffer.readDouble() / 8000.0D;
            this.posZ = buffer.readDouble() / 8000.0D;
        }
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
        super.getNetworkedData(list);

        boolean sendPosUpdates = this.ticks < 25 || this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal() || this.landing;
        list.add(sendPosUpdates);

        if (sendPosUpdates)
        {
            list.add(this.posX * 8000.0D);
            list.add(this.posY * 8000.0D);
            list.add(this.posZ * 8000.0D);
        }
    }

    @Override
    public void onReachAtmosphere()
    {
        //Launch controlled
        if (this.destinationFrequency != -1)
        {
            if (this.worldObj.isRemote)
            {
            	//reset the sounds on the client
            	this.stopRocketSound();
            	this.rocketSoundUpdater = null;
                return;
            }

            this.setTarget(true, this.destinationFrequency);

            if (this.targetVec != null)
            {
                if (this.targetDimension != this.worldObj.provider.dimensionId)
                {
                    WorldProvider targetDim = WorldUtil.getProviderForDimension(this.targetDimension);                   
                    if (targetDim != null)
                    {
                    	WorldServer worldServer = mcserver.worldServerForDimension(this.targetDimension);
                        if (worldServer != null)
                        {
	                        boolean dimensionAllowed = this.targetDimension == 0;
	
	                        if (targetDim instanceof IGalacticraftWorldProvider)
	                        {
	                        	if (((IGalacticraftWorldProvider) targetDim).canSpaceshipTierPass(this.getRocketTier()))
	                        		dimensionAllowed = true;
	                        	else
	                        		dimensionAllowed = false;
                        	}
	                        else
	                        //No rocket flight to non-Galacticraft dimensions other than the Overworld allowed unless config
	                        if (this.targetDimension > 1 || this.targetDimension < -1)
	                        {
	                            try {
		                        	Class<?> marsConfig = Class.forName("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars");
		                        	if (marsConfig.getField("launchControllerAllDims").getBoolean(null))
		                        		dimensionAllowed = true;
	                            } catch (Exception e) { e.printStackTrace(); }
	                        }
	
	                    	if (dimensionAllowed)
	                    	{
	                            if (this.riddenByEntity != null)
	                            {
	                                WorldUtil.transferEntityToDimension(this.riddenByEntity, this.targetDimension, worldServer, false, this);
	                            }
	
	                            return;
	                    	}
                        }
                    }
                    //No return - in this situation continue into regular take-off
                }
                else
                {
                	//Same dimension controlled rocket flight
                	this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
                    if (this.riddenByEntity != null)
                    {
                        this.setWaitForPlayer(true);
                        if (ConfigManagerCore.enableDebug) GCLog.info("Rocket repositioned, waiting for player");
                    }
                    this.landing = true;
                    return;
                }
            }
            else
            {
                this.setDead();
                return;
            }
        }

        //Not launch controlled
        if (this.riddenByEntity != null && !this.worldObj.isRemote)
        {
            if (this.riddenByEntity instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) this.riddenByEntity;

                this.onTeleport(player);
                GCPlayerStats stats = GCPlayerStats.get(player);
                WorldUtil.toCelestialSelection(player, stats, this.getRocketTier());

                if (!this.isDead)
                {
                    this.setDead();
                }
            }
        }
    }

    @Override
    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel() && Math.abs(this.lastLastMotionY) < 4;
    }

    public void onTeleport(EntityPlayerMP player)
    {
    }

    @Override
    protected void onRocketLand(int x, int y, int z)
    {
        super.onRocketLand(x, y, z);
        this.launchCooldown = 40;
    }

    @Override
    public void onLaunch()
    {
        super.onLaunch();
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return true;
    }

    @Override
    public boolean interactFirst(EntityPlayer par1EntityPlayer)
    {
        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
        {
            return false;
        }

        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayerMP)
        {
            if (!this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, new Object[] { }), (EntityPlayerMP) par1EntityPlayer);
                GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) par1EntityPlayer);
                stats.chatCooldown = 0;
                par1EntityPlayer.mountEntity(null);
            }

            return true;
        }
        else if (par1EntityPlayer instanceof EntityPlayerMP)
        {
            if (!this.worldObj.isRemote)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_DISPLAY_ROCKET_CONTROLS, new Object[] { }), (EntityPlayerMP) par1EntityPlayer);
                GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) par1EntityPlayer);
                stats.chatCooldown = 0;
                par1EntityPlayer.mountEntity(this);
            }

            return true;
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
        if (this.rocketType == null) return 2;
    	return this.rocketType.getInventorySpace();
    }

    @Override
    public void onWorldTransferred(World world)
    {
        if (this.targetVec != null)
        {
            this.setPosition(this.targetVec.x + 0.5F, this.targetVec.y + 800, this.targetVec.z + 0.5F);
            this.landing = true;
            this.setWaitForPlayer(true);
            this.motionX = this.motionY = this.motionZ = 0.0D;
        }
        else
        {
            this.setDead();
        }
    }

    @Override
    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

    public float getRotateOffset()
    {
        return -1.5F;
    }

    @Override
    public boolean isPlayerRocket()
    {
        return true;
    }
}
