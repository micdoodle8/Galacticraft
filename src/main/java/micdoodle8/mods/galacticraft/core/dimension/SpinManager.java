package micdoodle8.mods.galacticraft.core.dimension;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpinThruster;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SpinManager
{
    private static final float GFORCE = 9.81F / 400F; //gravity in metres per tick squared

    private OrbitSpinSaveData savefile;
    public boolean doSpinning = true;
    public float angularVelocityRadians = 0F;
    public float skyAngularVelocity = (float) (this.angularVelocityRadians * 180 / Math.PI);
    private float angularVelocityTarget = 0F;
    private float angularVelocityAccel = 0F;
    public double spinCentreX;
    public double spinCentreZ;
    private float momentOfInertia;
    private float massCentreX;
    private float massCentreZ;
    public int ssBoundsMaxX;
    public int ssBoundsMinX;
    public int ssBoundsMaxY;
    public int ssBoundsMinY;
    public int ssBoundsMaxZ;
    public int ssBoundsMinZ;

    private LinkedList<BlockVec3> thrustersPlus = new LinkedList();
    private LinkedList<BlockVec3> thrustersMinus = new LinkedList();
    private BlockVec3 oneSSBlock;
    //private HashSet<BlockVec3> stationBlocks = new HashSet();

    private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();

    private float artificialG;
    //Used to make continuous particles + thrust sounds at the spin thrusters in this dimension
    //If false, make particles + sounds occasionally in small bursts, just for fun (micro attitude changes)
    //see: BlockSpinThruster.randomDisplayTick()
    public boolean thrustersFiring = false;
    private boolean dataNotLoaded = true;
    private List<Entity> loadedEntities = new LinkedList();
    private double pPrevMotionX = 0D;
    public double pPrevMotionY = 0D;
    private double pPrevMotionZ = 0D;

    private WorldProviderSpaceStation worldProvider;
    private boolean clientSide = true;

    public SpinManager(WorldProviderSpaceStation provider)
    {
        this.worldProvider = provider;
    }

    /**
     * Called from WorldProviderSpaceStation when registering the worldObj
     */
    public void registerServerSide()
    {
        if (!this.worldProvider.worldObj.isRemote)
        {
            this.clientSide = false;
        }
    }

    public float getSpinRate()
    {
        return this.skyAngularVelocity;
    }

    /**
     * Sets the spin rate for the dimension in radians per tick For example,
     * 0.031415 would be 1/200 revolution per tick So that would be 1 revolution
     * every 10 seconds
     */
    public void setSpinRate(float angle)
    {
        this.angularVelocityRadians = angle;
        this.skyAngularVelocity = angle * 180F / 3.1415927F;

        if (this.clientSide)
        {
            this.updateSkyProviderSpinRate();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateSkyProviderSpinRate()
    {
    	worldProvider.setSpinDeltaPerTick(this.skyAngularVelocity);
    }

    public void setSpinRate(float angle, boolean firing)
    {
        this.angularVelocityRadians = angle;
        this.skyAngularVelocity = angle * 180F / 3.1415927F;
    	worldProvider.setSpinDeltaPerTick(this.skyAngularVelocity);
        this.thrustersFiring = firing;
    }

    public void setSpinCentre(double x, double z)
    {
        this.spinCentreX = x;
        this.spinCentreZ = z;
        if (this.clientSide)
        {
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("Clientside update to spin centre: " + x + "," + z);
            }
        }
    }

    public void setSpinBox(int mx, int xx, int my, int yy, int mz, int zz)
    {
        this.ssBoundsMinX = mx;
        this.ssBoundsMaxX = xx;
        this.ssBoundsMinY = my;
        this.ssBoundsMaxY = yy;
        this.ssBoundsMinZ = mz;
        this.ssBoundsMaxZ = zz;
    }

    public void addThruster(BlockVec3 thruster, boolean positive)
    {
        if (positive)
        {
            this.thrustersPlus.add(thruster);
            this.thrustersMinus.remove(thruster);
        }
        else
        {
            this.thrustersPlus.remove(thruster);
            this.thrustersMinus.add(thruster);
        }
    }

    public void removeThruster(BlockVec3 thruster, boolean positive)
    {
        if (positive)
        {
            this.thrustersPlus.remove(thruster);
        }
        else
        {
            this.thrustersMinus.remove(thruster);
        }
    }

    /**
     * This will check all blocks which are in contact with each other to find
     * the shape of the spacestation. It also finds the centre of mass (to
     * rotate around) and the moment of inertia (how easy/hard this is to
     * rotate).
     * <p/>
     * If placingThruster is true, it will return false if the thruster (at
     * baseBlock) is not in contact with the "existing" spacestation - so the
     * player cannot place thrusters on outlying disconnected blocks and expect
     * them to have an effect.
     * <p/>
     * Note: this check will briefly load, server-side, all chunks which have
     * spacestation blocks in them or 1 block adjacent to those.
     *
     * @param baseBlock
     * @return
     */
    public boolean checkSS(BlockVec3 baseBlock, boolean placingThruster)
    {
        World worldObj = this.worldProvider.worldObj;
    	if (this.oneSSBlock == null || this.oneSSBlock.getBlockID(worldObj) instanceof BlockAir)
        {
            if (baseBlock != null)
            {
                this.oneSSBlock = baseBlock.clone();
            }
            else
            {
                this.oneSSBlock = new BlockVec3(0, 64, 0);
            }
        }

        // Find contiguous blocks using an algorithm like the oxygen sealer one
        List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
        List<BlockVec3> nextLayer = new LinkedList<BlockVec3>();
        final List<BlockVec3> foundThrusters = new LinkedList<BlockVec3>();

        this.checked.clear();
        currentLayer.add(this.oneSSBlock.clone());
        this.checked.add(this.oneSSBlock.clone());
        Block bStart = this.oneSSBlock.getBlockID(worldObj);
        if (bStart instanceof BlockSpinThruster)
        {
            foundThrusters.add(this.oneSSBlock);
        }

        float thismass = 0.1F; //Mass of a thruster
        float thismassCentreX = 0.1F * this.oneSSBlock.x;
        float thismassCentreY = 0.1F * this.oneSSBlock.y;
        float thismassCentreZ = 0.1F * this.oneSSBlock.z;
        float thismoment = 0F;
        int thisssBoundsMaxX = this.oneSSBlock.x;
        int thisssBoundsMinX = this.oneSSBlock.x;
        int thisssBoundsMaxY = this.oneSSBlock.y;
        int thisssBoundsMinY = this.oneSSBlock.y;
        int thisssBoundsMaxZ = this.oneSSBlock.z;
        int thisssBoundsMinZ = this.oneSSBlock.z;

        while (currentLayer.size() > 0)
        {
            int bits;
        	for (BlockVec3 vec : currentLayer)
            {
                bits = vec.sideDoneBits;
        		if (vec.x < thisssBoundsMinX)
                {
                    thisssBoundsMinX = vec.x;
                }
                if (vec.y < thisssBoundsMinY)
                {
                    thisssBoundsMinY = vec.y;
                }
                if (vec.z < thisssBoundsMinZ)
                {
                    thisssBoundsMinZ = vec.z;
                }
                if (vec.x > thisssBoundsMaxX)
                {
                    thisssBoundsMaxX = vec.x;
                }
                if (vec.y > thisssBoundsMaxY)
                {
                    thisssBoundsMaxY = vec.y;
                }
                if (vec.z > thisssBoundsMaxZ)
                {
                    thisssBoundsMaxZ = vec.z;
                }

                for (int side = 0; side < 6; side++)
                {
                    if ((bits & (1 << side)) == 1)
                    {
                        continue;
                    }
                    BlockVec3 sideVec = vec.newVecSide(side);

                    if (!this.checked.contains(sideVec))
                    {
                        this.checked.add(sideVec);
                        Block b = sideVec.getBlockID(worldObj);
                        if (b != null && !b.isAir(worldObj, sideVec.x, sideVec.y, sideVec.z))
                        {
                            nextLayer.add(sideVec);
                            if (bStart.isAir(worldObj, this.oneSSBlock.x, this.oneSSBlock.y, this.oneSSBlock.z))
                            {
                                this.oneSSBlock = sideVec.clone();
                                bStart = b;
                            }
                            float m = 1.0F;
                            //Liquids have a mass of 1, stone, metal blocks etc will be heavier
                            if (!(b instanceof BlockLiquid))
                            {
                                //For most blocks, hardness gives a good idea of mass
                                m = b.getBlockHardness(worldObj, sideVec.x, sideVec.y, sideVec.z);
                                if (m < 0.1F)
                                {
                                    m = 0.1F;
                                }
                                else if (m > 30F)
                                {
                                    m = 30F;
                                }
                                //Wood items have a high hardness compared with their presumed mass
                                if (b.getMaterial() == Material.wood)
                                {
                                    m /= 4;
                                }

                                //TODO: higher mass for future Galacticraft hi-density item like neutronium
                                //Maybe also check for things in other mods by name: lead, uranium blocks?
                            }
                            thismassCentreX += m * sideVec.x;
                            thismassCentreY += m * sideVec.y;
                            thismassCentreZ += m * sideVec.z;
                            thismass += m;
                            thismoment += m * (sideVec.x * sideVec.x + sideVec.z * sideVec.z);
                            if (b instanceof BlockSpinThruster && !RedstoneUtil.isBlockReceivingRedstone(worldObj, sideVec.x, sideVec.y, sideVec.z))
                            {
                                foundThrusters.add(sideVec);
                            }
                        }
                    }
                }
            }

            currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }

        if (placingThruster && !this.checked.contains(baseBlock))
        {
            if (foundThrusters.size() > 0)
            {
                //The thruster was not placed on the existing contiguous space station: it must be.
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Thruster placed on wrong part of space station: base at " + this.oneSSBlock.x + "," + this.oneSSBlock.y + "," + this.oneSSBlock.z + " - baseBlock was " + baseBlock.x + "," + baseBlock.y + "," + baseBlock.z + " - found " + foundThrusters.size());
                }
                return false;
            }

            //No thruster on the original space station - so assume the player made new station and start check again
            //This offers players a reset option: just remove all thrusters from original station then starting adding to new one
            //(This first check prevents an infinite loop)
            if (!this.oneSSBlock.equals(baseBlock))
            {
                this.oneSSBlock = baseBlock.clone();
                if (this.oneSSBlock.getBlockID(worldObj).getMaterial() != Material.air)
                {
                    return this.checkSS(baseBlock, true);
                }
            }

            return false;

        }

        // Update thruster lists based on what was found
        this.thrustersPlus.clear();
        this.thrustersMinus.clear();
        for (BlockVec3 thruster : foundThrusters)
        {
        	int facing = thruster.getBlockMetadata(worldObj) & 8;
            if (facing == 0)
            {
                this.thrustersPlus.add(thruster.clone());
            }
            else
            {
                this.thrustersMinus.add(thruster.clone());
            }
        }

        // Calculate centre of mass
        float mass = thismass;

        this.massCentreX = thismassCentreX / thismass + 0.5F;
        float massCentreY = thismassCentreY / thismass + 0.5F;
        this.massCentreZ = thismassCentreZ / thismass + 0.5F;
        //System.out.println("(X,Z) = "+this.massCentreX+","+this.massCentreZ);

        this.setSpinCentre(this.massCentreX, this.massCentreZ);

        //The boundary is at the outer edges of the blocks
        this.ssBoundsMaxX = thisssBoundsMaxX + 1;
        this.ssBoundsMinX = thisssBoundsMinX;
        this.ssBoundsMaxY = thisssBoundsMaxY + 1;
        this.ssBoundsMinY = thisssBoundsMinY;
        this.ssBoundsMaxZ = thisssBoundsMaxZ + 1;
        this.ssBoundsMinZ = thisssBoundsMinZ;

        // Calculate momentOfInertia
        thismoment -= this.massCentreX * this.massCentreX * mass;
        thismoment -= this.massCentreZ * this.massCentreZ * mass;
        this.momentOfInertia = thismoment;

        //TODO
        // TODO defy gravity
        // TODO break blocks which are outside SS (not in checked)
        // TODO prevent spin if there is a huge number of blocks outside SS

        GCLog.debug("MoI = " + this.momentOfInertia + " CoMx = " + this.massCentreX + " CoMz = " + this.massCentreZ);

        //Send packets to clients in this dimension
        List<Object> objList = new ArrayList<Object>();
        objList.add(Double.valueOf(this.spinCentreX));
        objList.add(Double.valueOf(this.spinCentreZ));
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), this.worldProvider.dimensionId);

        objList = new ArrayList<Object>();
        objList.add(Integer.valueOf(this.ssBoundsMinX));
        objList.add(Integer.valueOf(this.ssBoundsMaxX));
        objList.add(Integer.valueOf(this.ssBoundsMinY));
        objList.add(Integer.valueOf(this.ssBoundsMaxY));
        objList.add(Integer.valueOf(this.ssBoundsMinZ));
        objList.add(Integer.valueOf(this.ssBoundsMaxZ));
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), this.worldProvider.dimensionId);

        this.updateSpinSpeed();

        return true;
    }

    public void updateSpinSpeed()
    {
        if (this.momentOfInertia > 0F)
        {
            float netTorque = 0F;
            int countThrusters = 0;
            int countThrustersReverse = 0;

            for (BlockVec3 thruster : this.thrustersPlus)
            {
                float xx = thruster.x - this.massCentreX;
                float zz = thruster.z - this.massCentreZ;
                netTorque += MathHelper.sqrt_float(xx * xx + zz * zz);
                countThrusters++;
            }
            for (BlockVec3 thruster : this.thrustersMinus)
            {
                float xx = thruster.x - this.massCentreX;
                float zz = thruster.z - this.massCentreZ;
                netTorque -= MathHelper.sqrt_float(xx * xx + zz * zz);
                countThrustersReverse++;
            }

            if (countThrusters == countThrustersReverse)
            {
                this.angularVelocityAccel = 0.000004F;
                this.angularVelocityTarget = 0F;
            }
            else
            {
                countThrusters += countThrustersReverse;
                if (countThrusters > 4)
                {
                    countThrusters = 4;
                }

                float maxRx = Math.max(this.ssBoundsMaxX - this.massCentreX, this.massCentreX - this.ssBoundsMinX);
                float maxRz = Math.max(this.ssBoundsMaxZ - this.massCentreZ, this.massCentreZ - this.ssBoundsMinZ);
                float maxR = Math.max(maxRx, maxRz);
                this.angularVelocityTarget = MathHelper.sqrt_float(GFORCE / maxR) / 2;
                //The divide by 2 is not scientific but is a Minecraft factor as everything happens more quickly
                float spinCap = 0.00125F * countThrusters;

                //TODO: increase this above 20F in release versions so everything happens more slowly
                this.angularVelocityAccel = netTorque / this.momentOfInertia / 20F;
                if (this.angularVelocityAccel < 0)
                {
                    this.angularVelocityAccel = -this.angularVelocityAccel;
                    this.angularVelocityTarget = -this.angularVelocityTarget;
                    if (this.angularVelocityTarget < -spinCap)
                    {
                        this.angularVelocityTarget = -spinCap;
                    }
                }
                else
                    //Do not make it spin too fast or players might get dizzy
                    //Also make it so players need minimum 4 thrusters for best spin
                    if (this.angularVelocityTarget > spinCap)
                    {
                        this.angularVelocityTarget = spinCap;
                    }

                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("MaxR = " + maxR + " Angular vel = " + this.angularVelocityTarget + " Angular accel = " + this.angularVelocityAccel);
                }
            }
        }

        if (!this.clientSide)
        {
            //Save the updated data for the world
            if (this.savefile == null)
            {
                this.savefile = OrbitSpinSaveData.initWorldData(this.worldProvider.worldObj);
                this.dataNotLoaded = false;
            }
            else
            {
                this.writeToNBT(this.savefile.datacompound);
                this.savefile.markDirty();
            }
        }
    }

    public void updateSpin()
    {
    	if (!this.clientSide)
    	{
            if (this.dataNotLoaded)
            {
                this.savefile = OrbitSpinSaveData.initWorldData(this.worldProvider.worldObj);
                this.readFromNBT(this.savefile.datacompound);
                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Loading data from save: " + this.savefile.datacompound.getFloat("omegaSky"));
                }
                this.dataNotLoaded = false;
            }

            if (this.doSpinning)
            {
                boolean updateNeeded = true;
                if (this.angularVelocityTarget < this.angularVelocityRadians)
                {
                    float newAngle = this.angularVelocityRadians - this.angularVelocityAccel;
                    if (newAngle < this.angularVelocityTarget)
                    {
                        newAngle = this.angularVelocityTarget;
                    }
                    this.setSpinRate(newAngle);
                    this.thrustersFiring = true;
                }
                else if (this.angularVelocityTarget > this.angularVelocityRadians)
                {
                    float newAngle = this.angularVelocityRadians + this.angularVelocityAccel;
                    if (newAngle > this.angularVelocityTarget)
                    {
                        newAngle = this.angularVelocityTarget;
                    }
                    this.setSpinRate(newAngle);
                    this.thrustersFiring = true;
                }
                else if (this.thrustersFiring)
                {
                    this.thrustersFiring = false;
                }
                else
                {
                    updateNeeded = false;
                }

                if (updateNeeded)
                {
                    this.writeToNBT(this.savefile.datacompound);
                    this.savefile.markDirty();
                    List<Object> objList = new ArrayList<Object>();
                    objList.add(Float.valueOf(this.angularVelocityRadians));
                    objList.add(Boolean.valueOf(this.thrustersFiring));
                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), this.worldProvider.dimensionId);
                }

                //Update entity positions if in freefall
                World worldObj = this.worldProvider.worldObj;
                this.loadedEntities.clear();
                this.loadedEntities.addAll(worldObj.loadedEntityList);
                for (Entity e : this.loadedEntities)
                {
                    if ((e instanceof EntityItem || e instanceof EntityLivingBase && !(e instanceof EntityPlayer) || e instanceof EntityTNTPrimed || e instanceof EntityFallingBlock) && !e.onGround)
                    {
                        boolean freefall = true;
                        if (e.boundingBox.maxX >= this.ssBoundsMinX && e.boundingBox.minX <= this.ssBoundsMaxX && e.boundingBox.maxY >= this.ssBoundsMinY && e.boundingBox.minY <= this.ssBoundsMaxY && e.boundingBox.maxZ >= this.ssBoundsMinZ && e.boundingBox.minZ <= this.ssBoundsMaxZ)
                        {
                            //Entity is somewhere within the space station boundaries

                            //Check if the entity's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
                            //If so, it's assumed the entity has something close enough to catch onto, so is not in freefall
                            //Note: breatheable air here means the entity is definitely not in freefall
                            int xmx = MathHelper.floor_double(e.boundingBox.maxX + 0.2D);
                            int ym = MathHelper.floor_double(e.boundingBox.minY - 0.1D);
                            int yy = MathHelper.floor_double(e.boundingBox.maxY + 0.1D);
                            int zm = MathHelper.floor_double(e.boundingBox.minZ - 0.2D);
                            int zz = MathHelper.floor_double(e.boundingBox.maxZ + 0.2D);
                            BLOCKCHECK:
                            for (int x = MathHelper.floor_double(e.boundingBox.minX - 0.2D); x <= xmx; x++)
                            {
                                for (int y = ym; y <= yy; y++)
                                {
                                    for (int z = zm; z <= zz; z++)
                                    {
                                        if (worldObj.blockExists(x, y, z) && Blocks.air != worldObj.getBlock(x, y, z))
                                        {
                                            freefall = false;
                                            break BLOCKCHECK;
                                        }
                                    }
                                }
                            }
                        }

                        if (freefall)
                        {
                            //Do the rotation
                            if (this.angularVelocityRadians != 0F)
                            {
                                float angle;
                                final double xx = e.posX - this.spinCentreX;
                                final double zz = e.posZ - this.spinCentreZ;
                                double arc = Math.sqrt(xx * xx + zz * zz);
                                if (xx == 0D)
                                {
                                    angle = zz > 0 ? 3.1415926535F / 2 : -3.1415926535F / 2;
                                }
                                else
                                {
                                    angle = (float) Math.atan(zz / xx);
                                }
                                if (xx < 0D)
                                {
                                    angle += 3.1415926535F;
                                }
                                angle += this.angularVelocityRadians / 3F;
                                arc = arc * this.angularVelocityRadians;
                                final double offsetX = -arc * MathHelper.sin(angle);
                                final double offsetZ = arc * MathHelper.cos(angle);
                                e.posX += offsetX;
                                e.posZ += offsetZ;
                                e.lastTickPosX += offsetX;
                                e.lastTickPosZ += offsetZ;

                                //Rotated into an unloaded chunk (probably also drifted out to there): byebye
                                if (!worldObj.blockExists(MathHelper.floor_double(e.posX), 64, MathHelper.floor_double(e.posZ)))
                                {
                                    e.setDead();
                                }

                                e.boundingBox.offset(offsetX, 0.0D, offsetZ);
                                //TODO check for block collisions here - if so move the entity appropriately and apply fall damage
                                //Moving the entity = slide along / down
                                e.rotationYaw += this.skyAngularVelocity;
                                while (e.rotationYaw > 360F)
                                {
                                    e.rotationYaw -= 360F;
                                }
                            }

                            //Undo deceleration
                            if (e instanceof EntityLivingBase)
                            {
                                e.motionX /= 0.91F;
                                e.motionZ /= 0.91F;
                                if (e instanceof EntityFlying)
                                {
                                    e.motionY /= 0.91F;
                                }
                                else
                                {
                                    e.motionY /= 0.9800000190734863D;
                                }
                            }
                            else if (e instanceof EntityFallingBlock)
                            {
                                e.motionY /= 0.9800000190734863D;
                                //e.motionY += 0.03999999910593033D;
                                //e.posY += 0.03999999910593033D;
                                //e.lastTickPosY += 0.03999999910593033D;
                            }
                            else
                            {
                                e.motionX /= 0.9800000190734863D;
                                e.motionY /= 0.9800000190734863D;
                                e.motionZ /= 0.9800000190734863D;
                            }
                        }
                    }
                }
            }
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public boolean updatePlayerForSpin(EntityPlayerSP p, float partialTicks)
    {
        float angleDelta = partialTicks * this.angularVelocityRadians;
        if (this.doSpinning && angleDelta != 0F)
        {
            //TODO maybe need to test to make sure xx and zz are not too large (outside sight range of SS)
            //TODO think about server + network load (loading/unloading chunks) when movement is rapid
            //Maybe reduce chunkloading radius?
            
            boolean doGravity = false;
            float angle;
            final double xx = p.posX - this.spinCentreX;
            final double zz = p.posZ - this.spinCentreZ;
            double arc = Math.sqrt(xx * xx + zz * zz);
            if (xx == 0D)
            {
                angle = zz > 0 ? 3.1415926535F / 2 : -3.1415926535F / 2;
            }
            else
            {
                angle = (float) Math.atan(zz / xx);
            }
            if (xx < 0D)
            {
                angle += 3.1415926535F;
            }
            angle += angleDelta / 3F;
            arc = arc * angleDelta;
            double offsetX = -arc * MathHelper.sin(angle);
            double offsetZ = arc * MathHelper.cos(angle);

            //Check for block collisions here - if so move the player appropriately
            //First check that there are no existing collisions where the player is now (TODO: bounce the player away)
            if (p.worldObj.getCollidingBoundingBoxes(p, p.boundingBox).size() == 0)
            {
                //Now check for collisions in the new direction and if there are some, try reducing the movement
                int collisions = 0;
                do
                {
                    List<AxisAlignedBB> list = p.worldObj.getCollidingBoundingBoxes(p, p.boundingBox.addCoord(offsetX, 0.0D, offsetZ));
                    collisions = list.size();
                    if (collisions > 0)
                    {
                        if (!doGravity)
                        {
                            p.motionX += -offsetX;
                            p.motionZ += -offsetZ;
                        }
                        offsetX /= 2D;
                        offsetZ /= 2D;
                        if (offsetX < 0.01D && offsetX > -0.01D)
                        {
                            offsetX = 0D;
                        }
                        if (offsetZ < 0.01D && offsetZ > -0.01D)
                        {
                            offsetZ = 0D;
                        }
                        doGravity = true;
                    }
                }
                while (collisions > 0);

                p.posX += offsetX;
                p.posZ += offsetZ;
                p.boundingBox.offset(offsetX, 0.0D, offsetZ);
            }
            
            p.rotationYaw += this.skyAngularVelocity * partialTicks;
            p.renderYawOffset += this.skyAngularVelocity * partialTicks;
            while (p.rotationYaw > 360F)
            {
                p.rotationYaw -= 360F;
            }
            while (p.rotationYaw < 0F)
            {
                p.rotationYaw += 360F;
            }

            return doGravity;
        }
        
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void applyCentrifugalForce(EntityPlayerSP p)
    {
        int quadrant = 0;
        double xd = p.posX - this.spinCentreX;
        double zd = p.posZ - this.spinCentreZ;
        double accel = Math.sqrt(xd * xd + zd * zd) * this.angularVelocityRadians * this.angularVelocityRadians * 4D;

        if (xd < 0)
        {
            if (xd < -Math.abs(zd))
            {
                quadrant = 2;
            }
            else
            {
                quadrant = zd < 0 ? 3 : 1;
            }
        }
        else if (xd > Math.abs(zd))
        {
            quadrant = 0;
        }
        else
        {
            quadrant = zd < 0 ? 3 : 1;
        }

        switch (quadrant)
        {
        case 0:
            p.motionX += accel;
            break;
        case 1:
            p.motionZ += accel;
            break;
        case 2:
            p.motionX -= accel;
            break;
        case 3:
        default:
            p.motionZ -= accel;
        }
    }
    
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.doSpinning = true;//nbt.getBoolean("doSpinning");
        this.angularVelocityRadians = nbt.getFloat("omegaRad");
        this.skyAngularVelocity = nbt.getFloat("omegaSky");
        this.angularVelocityTarget = nbt.getFloat("omegaTarget");
        this.angularVelocityAccel = nbt.getFloat("omegaAcc");

        NBTTagCompound oneBlock = (NBTTagCompound) nbt.getTag("oneBlock");
        if (oneBlock != null)
        {
            this.oneSSBlock = BlockVec3.readFromNBT(oneBlock);
        }
        else
        {
            this.oneSSBlock = null;
        }

        //A lot of the data can be refreshed by checkSS
        this.checkSS(this.oneSSBlock, false);

        //Send packets to clients in this dimension
        List<Object> objList = new ArrayList<Object>();
        objList.add(Float.valueOf(this.angularVelocityRadians));
        objList.add(Boolean.valueOf(this.thrustersFiring));
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), this.worldProvider.dimensionId);

        objList = new ArrayList<Object>();
        objList.add(Double.valueOf(this.spinCentreX));
        objList.add(Double.valueOf(this.spinCentreZ));
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), this.worldProvider.dimensionId);

        objList = new ArrayList<Object>();
        objList.add(Integer.valueOf(this.ssBoundsMinX));
        objList.add(Integer.valueOf(this.ssBoundsMaxX));
        objList.add(Integer.valueOf(this.ssBoundsMinY));
        objList.add(Integer.valueOf(this.ssBoundsMaxY));
        objList.add(Integer.valueOf(this.ssBoundsMinZ));
        objList.add(Integer.valueOf(this.ssBoundsMaxZ));
        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), this.worldProvider.dimensionId);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("doSpinning", this.doSpinning);
        nbt.setFloat("omegaRad", this.angularVelocityRadians);
        nbt.setFloat("omegaSky", this.skyAngularVelocity);
        nbt.setFloat("omegaTarget", this.angularVelocityTarget);
        nbt.setFloat("omegaAcc", this.angularVelocityAccel);
        if (this.oneSSBlock != null)
        {
            NBTTagCompound oneBlock = new NBTTagCompound();
            this.oneSSBlock.writeToNBT(oneBlock);
            nbt.setTag("oneBlock", oneBlock);
        }
    }

    /**
     * Call this when player first login/transfer to this dimension
     * <p/>
     * TODO how can this code be called by other mods / plugins with teleports
     * (e.g. Bukkit)? See WorldUtil.teleportEntity()
     *
     * @param player
     */
    public void sendPacketsToClient(EntityPlayerMP player)
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(Float.valueOf(this.angularVelocityRadians));
        objList.add(Boolean.valueOf(this.thrustersFiring));
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), player);

        objList = new ArrayList<Object>();
        objList.add(Double.valueOf(this.spinCentreX));
        objList.add(Double.valueOf(this.spinCentreZ));
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), player);

        objList = new ArrayList<Object>();
        objList.add(Integer.valueOf(this.ssBoundsMinX));
        objList.add(Integer.valueOf(this.ssBoundsMaxX));
        objList.add(Integer.valueOf(this.ssBoundsMinY));
        objList.add(Integer.valueOf(this.ssBoundsMaxY));
        objList.add(Integer.valueOf(this.ssBoundsMinZ));
        objList.add(Integer.valueOf(this.ssBoundsMaxZ));
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), player);
    }
}
