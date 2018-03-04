package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//ITileClientUpdates for changing in facing;  IPacketReceiver for initial transfer of NBT Data (airToRestore)
public class TileEntityArclamp extends TileEntity implements ITickable, ITileClientUpdates, IPacketReceiver
{
    private static final int LIGHTRANGE = 14;
    private int ticks = 0;
    private int sideRear = 0;
    public int facing = 0;
    private HashSet<BlockVec3> airToRestore = new HashSet<>();
    private intBucket[] buckets;
    private static intBucket[] bucketsServer;
    private static intBucket[] bucketsClient;
    private static AtomicBoolean usingBucketsServer = new AtomicBoolean();
    private static AtomicBoolean usingBucketsClient = new AtomicBoolean();
    private AtomicBoolean usingBuckets;
    private static final int SIZELIST = 65536;
    private int[] lightUpdateBlockList;
    private static int[] lightUpdateBlockListServer = null;
    private static int[] lightUpdateBlockListClient = null;
    private static AtomicBoolean usingLightListServer = new AtomicBoolean();
    private static AtomicBoolean usingLightListClient = new AtomicBoolean();
    private AtomicBoolean usingLightList;
    private boolean isActive = false;
    private AxisAlignedBB thisAABB;
    private AxisAlignedBB renderAABB;
    private Vec3 thisPos;
    private int facingSide = 0;

    static
    {
        bucketsServer = new intBucket[256];
        bucketsClient = new intBucket[256];
        checkedInit(bucketsServer);
        checkedInit(bucketsClient);
    }
    
    @Override
    public void update()
    {
        boolean initialLight = false;
//        if (this.updateClientFlag)
//        {
//            this.updateAllInDimension();
//            this.updateClientFlag = false;
//        }

        if (RedstoneUtil.isBlockReceivingRedstone(this.worldObj, this.getPos()))
        {
            if (this.isActive)
            {
                this.isActive = false;
                this.revertAir();
                this.markDirty();
            }
        }
        else if (!this.isActive && this.pos.getX() >= -30000000 + 32 && this.pos.getZ() >= -30000000 + 32 && this.pos.getX() < 30000000 - 32 && this.pos.getZ() < 30000000 -32 )
        {
            this.isActive = true;
            initialLight = true;
        }

        if (this.isActive)
        {
            //Test for first tick after placement
            if (this.thisAABB == null)
            {
                initialLight = true;
                int side = this.getBlockMetadata();
                switch (side)
                {
                case 0:
                    this.sideRear = side; //Down
                    this.facingSide = this.facing + 2;
                    break;
                case 1:
                    this.sideRear = side; //Up
                    this.facingSide = this.facing + 2;
                    break;
                case 2:
                    this.sideRear = side; //North
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 7 - this.facing;
                    }
                    break;
                case 3:
                    this.sideRear = side; //South
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide += 2;
                    }
                    break;
                case 4:
                    this.sideRear = side; //West
                    this.facingSide = this.facing;
                    break;
                case 5:
                    this.sideRear = side; //East
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 5 - this.facing;
                    }
                    break;
                default:
                }
                
                this.thisAABB = getAABBforSideAndFacing();
            }

            if (initialLight || this.ticks % 100 == 0)
            {
                this.lightArea();
            }

            if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(20) == 0)
            {
                List<Entity> moblist = this.worldObj.getEntitiesInAABBexcluding(null, this.thisAABB, IMob.mobSelector);

                if (!moblist.isEmpty())
                {
                    Vec3 thisVec3 = new Vec3(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                    for (Entity entry : moblist)
                    {
                        if (!(entry instanceof EntityCreature))
                        {
                            continue;
                        }
                        EntityCreature mob = (EntityCreature) entry;
                        //Check whether the mob can actually *see* the arclamp tile
                        //if (this.worldObj.func_147447_a(thisPos, new Vec3(entry.posX, entry.posY, entry.posZ), true, true, false) != null) continue;

                        PathNavigate nav = mob.getNavigator();
                        if (nav == null)
                        {
                            continue;
                        }
                        
                        Vec3 vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(mob, 28, 11, this.thisPos);
                        if (vecNewTarget == null)
                        {
                            continue;
                        }
                        double distanceNew = vecNewTarget.squareDistanceTo(thisVec3);
                        double distanceCurrent = thisVec3.squareDistanceTo(new Vec3(mob.posX, mob.posY, mob.posZ));
                        if (distanceNew > distanceCurrent)
                        {
                            Vec3 vecOldTarget = null;
                            if (nav.getPath() != null && !nav.getPath().isFinished())
                            {
                                vecOldTarget = nav.getPath().getPosition(mob);
                            }
                            if (vecOldTarget == null || distanceCurrent > vecOldTarget.squareDistanceTo(thisVec3))
                            {
                                nav.tryMoveToXYZ(vecNewTarget.xCoord, vecNewTarget.yCoord, vecNewTarget.zCoord, 1.3D);
                            }
                        }
                    }
                }
            }
        }

        this.ticks++;
    }

    private AxisAlignedBB getAABBforSideAndFacing()
    {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        int rangeForSide[] = new int[6];
        for (int i = 0; i < 6; i++)
        {
            rangeForSide[i] = (i == this.sideRear) ? 2 : (i == (this.facingSide ^ 1)) ? 4 : 25;
        }
        return AxisAlignedBB.fromBounds(x - rangeForSide[4], y - rangeForSide[0], z - rangeForSide[2], x + rangeForSide[5], y + rangeForSide[1], z + rangeForSide[3]);
    }

    @Override
    public void onLoad()
    {
        this.thisPos = new Vec3(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D);
        this.ticks = 0;
        this.thisAABB = null;
        if (this.worldObj.isRemote)
        {
            this.buckets = bucketsClient;
            this.usingBuckets = usingBucketsClient;
            if (lightUpdateBlockListClient == null)
            {
                lightUpdateBlockListClient = new int[SIZELIST];
            }
            this.lightUpdateBlockList = lightUpdateBlockListClient;
            this.usingLightList = usingLightListClient;
            this.clientOnLoad();
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
        else
        {
            this.buckets = bucketsServer;
            this.usingBuckets = usingBucketsServer;
            if (lightUpdateBlockListServer == null)
            {
                lightUpdateBlockListServer = new int[SIZELIST];
            }
            this.lightUpdateBlockList = lightUpdateBlockListServer;
            this.usingLightList = usingLightListServer;
            this.isActive = this.pos.getX() >= -30000000 + 32 && this.pos.getZ() >= -30000000 + 32 && this.pos.getX() < 30000000 - 32 && this.pos.getZ() < 30000000 -32;
        }
    }

    @Override
    public void invalidate()
    {
        this.revertAir();
        this.isActive = false;
        super.invalidate();
    }

    public void lightArea()
    {
        if (this.usingBuckets.getAndSet(true) || this.usingLightList.getAndSet(true))
        {
            return;
        }
//        long time1 = System.nanoTime();
        int index = 0;
        Block air = Blocks.air;
        Block breatheableAirID = GCBlocks.breatheableAir;
        IBlockState brightAir = GCBlocks.brightAir.getDefaultState();
        IBlockState brightBreatheableAir = GCBlocks.brightBreatheableAir.getDefaultState();
        boolean dirty = false;
        checkedClear();
        HashSet<BlockVec3> airToRevert = new HashSet<>();
        airToRevert.addAll(airToRestore);
        LinkedList<BlockVec3> airNew = new LinkedList<>();
        LinkedList<BlockVec3> currentLayer = new LinkedList<>();
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        BlockVec3 thisvec = new BlockVec3(this);
        currentLayer.add(thisvec);
        World world = this.worldObj;
        int sideskip1 = this.sideRear;
        int sideskip2 = this.facingSide ^ 1;
        int side, bits;
        for (int i = 0; i < 6; i++)
        {
            if (i != sideskip1 && i != sideskip2 && i != (sideskip1 ^ 1) && i != (sideskip2 ^ 1))
            {
                BlockVec3 onEitherSide = thisvec.newVecSide(i);
                Block b = onEitherSide.getBlockIDsafe_noChunkLoad(world);
                if (b != null && b.getLightOpacity() < 15)
                {
                    currentLayer.add(onEitherSide);
                }
            }
        }
        BlockVec3 inFront = new BlockVec3(this);
        for (int i = 0; i < 4; i++)
        {
            inFront = inFront.newVecSide(this.facingSide);
            Block b = inFront.getBlockIDsafe_noChunkLoad(world);
            if (b == null || b.getLightOpacity() == 15)
            {
                break;
            }
            inFront = inFront.newVecSide(sideskip1 ^ 1);
            b = inFront.getBlockIDsafe_noChunkLoad(world);
            if (b != null && b.getLightOpacity() < 15)
            {
                currentLayer.add(inFront);
            }
            else
            {
                break;
            }
        }

        inFront = new BlockVec3(this).newVecSide(this.facingSide);

        for (int count = 0; count < LIGHTRANGE; count++)
        {
            for (BlockVec3 vec : currentLayer)
            {
                //Shape the arc lamp lighted area to more of a cone in front of it
                if (count > 1)
                {
                    int offset = 0;
                    switch (this.facingSide)
                    {
                    case 0:
                        offset = inFront.y - vec.y;
                        break;
                    case 1:
                        offset = vec.y - inFront.y;
                        break;
                    case 2:
                        offset = inFront.z - vec.z;
                        break;
                    case 3:
                        offset = vec.z - inFront.z;
                        break;
                    case 4:
                        offset = inFront.x - vec.x;
                        break;
                    case 5:
                        offset = vec.x - inFront.x;
                        break;
                    }
                    int offset2 = 0;
                    switch (this.sideRear ^ 1)
                    {
                    case 0:
                        offset2 = inFront.y - vec.y;
                        break;
                    case 1:
                        offset2 = vec.y - inFront.y;
                        break;
                    case 2:
                        offset2 = inFront.z - vec.z;
                        break;
                    case 3:
                        offset2 = vec.z - inFront.z;
                        break;
                    case 4:
                        offset2 = inFront.x - vec.x;
                        break;
                    case 5:
                        offset2 = vec.x - inFront.x;
                        break;
                    }
                    if (offset2 - 2 > offset) offset = offset2 - 2;
                    if (Math.abs(vec.x - inFront.x) > offset + 2) continue;
                    if (Math.abs(vec.y - inFront.y) > offset + 2) continue; 
                    if (Math.abs(vec.z - inFront.z) > offset + 2) continue; 
                }
                
                //Now process each layer outwards from the source, finding new blocks to light (similar to ThreadFindSeal)
                //This is high performance code using our own custom HashSet (that's intBucket)
                side = 0;
                bits = vec.sideDoneBits;
                boolean doShine = false;
                do
                {
                    //Skip the side which this was entered from
                    //and never go 'backwards'
                    if ((bits & (1 << side)) == 0)
                    {
                        BlockVec3 sideVec = vec.newVecSide(side);
                        boolean toAdd = false;
                        if (!checkedContains(vec, side))
                        {
                            checkedAdd(sideVec);
                            toAdd = true;
                        }

                        Block b = sideVec.getBlockIDsafe_noChunkLoad(world);
                        if (b instanceof BlockAir)
                        {
                            if (toAdd && side != sideskip1 && side != sideskip2)
                            {
                                nextLayer.add(sideVec);
                            }
                        }
                        else
                        {
                            doShine = true;
                            //Glass blocks go through to the next layer as well
                            if (side != sideskip1 && side != sideskip2)
                            {
                                if (toAdd && b != null && b.getLightOpacity(world, sideVec.toBlockPos()) == 0)
                                {
                                    nextLayer.add(sideVec);
                                }
                            }
                        }
                    }
                    side++;
                }
                while (side < 6);

                if (doShine)
                {
                    airNew.add(vec);
                    Block id = vec.getBlockIDsafe_noChunkLoad(world);
                    if (Blocks.air == id)
                    {
                        this.brightenAir(world, vec, brightAir);
                        index = this.checkLightPartA(EnumSkyBlock.BLOCK, vec.toBlockPos(), index);
                        dirty = true;
                    }
                    else if (id == breatheableAirID)
                    {
                        this.brightenAir(world, vec, brightBreatheableAir);
                        index = this.checkLightPartA(EnumSkyBlock.BLOCK, vec.toBlockPos(), index);
                        dirty = true;
                    }
                }
            }
            if (nextLayer.size() == 0)
            {
                break;
            }
            currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }

        if (dirty)
        {
            this.markDirty();
            this.checkLightPartB(EnumSkyBlock.BLOCK, index);
        }
        
        //Look for any holdover bright blocks which are no longer lit (e.g. because the Arc Lamp became blocked in a tunnel)
        airToRevert.removeAll(airNew);
        index = 0;
        dirty = false;
        for (Object obj : airToRevert)
        {
            BlockVec3 vec = (BlockVec3) obj;
            this.setDarkerAir(vec);
            index = this.checkLightPartA(EnumSkyBlock.BLOCK, vec.toBlockPos(), index);
            this.airToRestore.remove(vec);
            dirty = true;
        }

        if (dirty)
        {
            this.markDirty();
            this.checkLightPartB(EnumSkyBlock.BLOCK, index);
        }

//        long time3 = System.nanoTime();
//        float total = (time3 - time1) / 1000000.0F;
//        GCLog.info("   Total Time taken: " + String.format("%.2f", total) + "ms");

        this.usingBuckets.set(false);
        this.usingLightList.set(false);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.facing = nbt.getInteger("Facing");
        if (GCCoreUtil.getEffectiveSide() == Side.SERVER)
        {
            this.airToRestore.clear();
            NBTTagList airBlocks = nbt.getTagList("AirBlocks", 10);
            if (airBlocks.tagCount() > 0)
            {
                for (int j = airBlocks.tagCount() - 1; j >= 0; j--)
                {
                    NBTTagCompound tag1 = airBlocks.getCompoundTagAt(j);
                    if (tag1 != null)
                    {
                        this.airToRestore.add(BlockVec3.readFromNBT(tag1));
                    }
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setInteger("Facing", this.facing);

        NBTTagList airBlocks = new NBTTagList();

        for (BlockVec3 vec : this.airToRestore)
        {
            NBTTagCompound tag = new NBTTagCompound();
            vec.writeToNBT(tag);
            airBlocks.appendTag(tag);
        }
        nbt.setTag("AirBlocks", airBlocks);
    }

    public void facingChanged()
    {
        this.facing -= 2;
        if (this.facing < 0)
        {
            this.facing = 1 - this.facing;
            //facing sequence: 0 - 3 - 1 - 2
        }

        this.updateAllInDimension();
        this.thisAABB = null;
        this.revertAir();
        this.markDirty();
        this.ticks = 91;
    }

    private void brightenAir(World world, BlockVec3 vec, IBlockState newState)
    {
        BlockPos blockpos = vec.toBlockPos();
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
        IBlockState oldState = chunk.setBlockState(blockpos, newState);
        if (this.worldObj.isRemote && oldState != null) this.worldObj.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        //No block update on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
        this.airToRestore.add(vec);
    }
    
    private void setDarkerAir(BlockVec3 vec)
    {
        BlockPos blockpos = vec.toBlockPos();
        Block b = this.worldObj.getBlockState(blockpos).getBlock();
        IBlockState newState;
        if (b == GCBlocks.brightAir)
        {
            newState = Blocks.air.getDefaultState();
        }
        else if (b == GCBlocks.brightBreatheableAir)
        {
            newState = GCBlocks.breatheableAir.getDefaultState();
        }
        else
        {
            return;
        }
            
//      Roughly similar to:  this.worldObj.setBlockState(pos, newState, (this.worldObj.isRemote) ? 2 : 0);
        
        Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
        IBlockState oldState = chunk.setBlockState(blockpos, newState);
        if (this.worldObj.isRemote && oldState != null) this.worldObj.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        //No block update on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
    }

    private void revertAir()
    {
        if (this.airToRestore.isEmpty())
            return;
        
        if (this.usingLightList == null)
        {
            this.usingLightList = this.worldObj.isRemote ? usingLightListClient : usingLightListServer;   
        }

        int index = 0;
        for (BlockVec3 vec : this.airToRestore)
        {
            this.setDarkerAir(vec);
        }
        if (!this.usingLightList.getAndSet(true))
        {
            for (BlockVec3 vec : this.airToRestore)
            {
                index = this.checkLightPartA(EnumSkyBlock.BLOCK, vec.toBlockPos(), index);
            }
            this.checkLightPartB(EnumSkyBlock.BLOCK, index);
            this.usingLightList.set(false);
        }
        this.airToRestore.clear();
        this.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
    }

    public boolean checkLightFor(EnumSkyBlock lightType, BlockPos bp)
    {
        if (!this.worldObj.isAreaLoaded(bp, 17, false))
        {
            return false;
        }

        World world = this.worldObj;
        BlockPos blockpos;
        int i = 0;
        int index = 0;
        int savedLight = world.getLightFor(lightType, bp);
        int rawLight = this.getRawLight(bp, lightType);
        int testx = bp.getX();
        int testy = bp.getY();
        int testz = bp.getZ();
        int x = testx - 32;
        int y = testy - 32;
        int z = testz - 32;
        int value, opacity, arraylight;
        int xx, yy, zz, range;
        LinkedList<BlockPos> result = new LinkedList<>();

        if (rawLight > savedLight)  //Light switched on
        {
            lightUpdateBlockList[index++] = 133152; //32, 32, 32 = the 0 position
        }
        else if (rawLight < savedLight)  //Light switched off ?
        {
            lightUpdateBlockList[index++] = 133152 | savedLight << 18;

            while (i < index)   //This becomes CRAZY LARGE
            {
                value = lightUpdateBlockList[i++];
                xx = (value & 63) + x;
                yy = (value >> 6 & 63) + y;
                zz = (value >> 12 & 63) + z;
                arraylight = value >> 18 & 15;
                    
                if (arraylight > 0)
                {
                    blockpos = new BlockPos(xx, yy, zz);
                    if (world.getLightFor(lightType, blockpos) == arraylight)   //Only gonna happen once (definitely will happen the first iteration)
                    {
                        this.setLightFor_preChecked(lightType, blockpos, 0);  //= flagdone

                        range = MathHelper.abs_int(xx - testx) + MathHelper.abs_int(yy - testy) + MathHelper.abs_int(zz - testz);
                        if (range < 17)
                        {
                            GCCoreUtil.getPositionsAdjoining(xx, yy, zz, result);
                            for (BlockPos vec : result)
                            {
                                savedLight = world.getLightFor(lightType, vec);
                                if (savedLight == 0) continue;  //eliminate backtracking
                                opacity = world.getBlockState(vec).getBlock().getLightOpacity();
                                if (opacity <= 0) opacity = 1;
                                //Tack positions onto the list as long as it looks like lit from here. i.e. saved light is adjacent light - opacity!
                                //There will be some errors due to coincidence / equality of light levels from 2 sources
                                if (savedLight == arraylight - opacity && index < SIZELIST)
                                {
                                    lightUpdateBlockList[index++] = vec.getX() - x + (((savedLight << 6) + vec.getZ() - z << 6) + vec.getY() - y << 6);
                                }
                            }
                        }
                    }
                }
            }

            i = 0;
        }

        while (i < index)
        {
            value = lightUpdateBlockList[i++];
            xx = (value & 63) + x;
            yy = (value >> 6 & 63) + y;
            zz = (value >> 12 & 63) + z;
            blockpos = new BlockPos(xx, yy, zz);
            savedLight = world.getLightFor(lightType, blockpos);
            rawLight = this.getRawLight(blockpos, lightType);

            if (rawLight != savedLight)
            {
                this.setLightFor_preChecked(lightType, blockpos, rawLight);   //<-------the light setting
                if (world.isRemote) world.notifyLightSet(blockpos);

                if (rawLight > savedLight)
                {
                    range = MathHelper.abs_int(xx - testx) + MathHelper.abs_int(yy - testy) + MathHelper.abs_int(zz - testz);
                    if (range < 17 && index < SIZELIST - 6)
                    {
                        GCCoreUtil.getPositionsAdjoining(xx, yy, zz, result);
                        for (BlockPos vec : result)
                        {
                            if (world.getLightFor(lightType, vec) < rawLight - 1)  //-1 here because opacity can't be less than 1 in getRawLight
                            {
                                //Tack even more positions on to the end of the list - this propagates each time we find a light source block... including going back over old positions
                                lightUpdateBlockList[index++] = vec.getX() - x + ((vec.getZ() - z << 6) + vec.getY() - y << 6);
                            }
                        }
                    }
                }
            }
            else if (world.isRemote && savedLight != ((value >> 21) & 15))
            {
                world.notifyLightSet(blockpos);
            }
        }

        return true;
    }

    public int checkLightPartA(EnumSkyBlock lightType, BlockPos bp, int indexIn)
    {
        if (indexIn >= SIZELIST)
        {
            return indexIn;
        }

        World world = this.worldObj;
        BlockPos blockpos;
        int i = indexIn;
        int iNextStart = 0;
        int index = indexIn;
        int savedLight = world.getLightFor(lightType, bp);
        int rawLight = this.getRawLight(bp, lightType);
        int x = this.pos.getX() - 64;
        int y = this.pos.getY() - 64;
        int z = this.pos.getZ() - 64;
        int testx = bp.getX();
        int testy = bp.getY();
        int testz = bp.getZ();
        int value, opacity, arraylight;
        int xx, yy, zz, range;
        LinkedList<BlockPos> neighbours = new LinkedList<>();

        if (rawLight > savedLight)  //Light switched on
        {
            lightUpdateBlockList[index++] = ((testz - z << 7) + testy - y << 7) + testx - x;
        }
        else if (rawLight < savedLight)  //Light switched off:  savedLight cannot be 0 here
        {
            lightUpdateBlockList[index++] = ((testz - z << 7) + testy - y << 7) + testx - x | savedLight << 21;
            this.setLightFor_preChecked(lightType, bp, 0);

            while (i < index)
            {
                value = lightUpdateBlockList[i++];
                xx = (value & 127) + x;
                yy = (value >> 7 & 127) + y;
                zz = (value >> 14 & 127) + z;
                range = MathHelper.abs_int(xx - testx) + MathHelper.abs_int(yy - testy) + MathHelper.abs_int(zz - testz);
                if (range < 17)
                {
                    arraylight = value >> 21 & 15;
                    GCCoreUtil.getPositionsAdjoiningLoaded(xx, yy, zz, neighbours, world);
                    for (BlockPos vec : neighbours)
                    {
                        savedLight = world.getLightFor(lightType, vec);
                        if (savedLight == 0) continue;  //eliminate backtracking
                        opacity = world.getBlockState(vec).getBlock().getLightOpacity();
                        if (opacity <= 0) opacity = 1;
                        //Tack positions onto the list as long as it looks like lit from here. i.e. saved light is adjacent light - opacity!
                        //There will be some errors due to coincidence / equality of light levels from 2 sources
                        if (savedLight == arraylight - opacity && index < SIZELIST)
                        {
                            lightUpdateBlockList[index++] = vec.getX() - x + (((savedLight << 7) + vec.getZ() - z << 7) + vec.getY() - y << 7);
                            this.setLightFor_preChecked(lightType, vec, 0);  //darken everything ready for re-light
                        }
                    }
                }
            }

            i = indexIn;
        }
        return index;
    }

    public boolean checkLightPartB(EnumSkyBlock lightType, int index)
    {
        World world = this.worldObj;
        BlockPos blockpos;
        int i = 0;
        int savedLight, rawLight;
        int testx = this.pos.getX();
        int testy = this.pos.getY();
        int testz = this.pos.getZ();
        int x = testx - 64;
        int y = testy - 64;
        int z = testz - 64;
        int value;
        int xx, yy, zz, range;
        LinkedList<BlockPos> neighbours = new LinkedList<>();

        while (i < index)
        {
            value = lightUpdateBlockList[i++];
            xx = (value & 127) + x;
            yy = (value >> 7 & 127) + y;
            zz = (value >> 14 & 127) + z;
            blockpos = new BlockPos(xx, yy, zz);
            savedLight = world.getLightFor(lightType, blockpos);
            rawLight = this.getRawLight(blockpos, lightType);

            if (rawLight != savedLight)
            {
                this.setLightFor_preChecked(lightType, blockpos, rawLight);   //<-------the light setting
                if (world.isRemote) world.notifyLightSet(blockpos);

                if (rawLight > savedLight)
                {
                    range = MathHelper.abs_int(xx - testx) + MathHelper.abs_int(yy - testy) + MathHelper.abs_int(zz - testz);
                    if (range < 34 && index < SIZELIST - 6)
                    {
                        GCCoreUtil.getPositionsAdjoiningLoaded(xx, yy, zz, neighbours, world);
                        for (BlockPos vec : neighbours)
                        {
                            if (world.getLightFor(lightType, vec) < rawLight - 1)  //-1 here because opacity can't be less than 1 in getRawLight
                            {
                                //Tack even more positions on to the end of the list - this propagates each time we find a light source block... including going back over old positions
                                lightUpdateBlockList[index++] = vec.getX() - x + ((vec.getZ() - z << 7) + vec.getY() - y << 7);
                            }
                        }
                    }
                }
            }
            else if (world.isRemote && savedLight != ((value >> 21) & 15))
            {
                world.notifyLightSet(blockpos);
            }
        }
        return true;
    }
    
/**
 * From vanilla.  This is buggy, gets confused if two low opacity blocks adjacent (e.g. redstone wire, stairs)
 * if those blocks are receiving similar light levels from another source
 */
    private int getRawLight(BlockPos pos, EnumSkyBlock lightType)
    {
        Block block = this.worldObj.getBlockState(pos).getBlock();
        int blockLight = block.getLightValue(this.worldObj, pos);
        int light = lightType == EnumSkyBlock.SKY ? 0 : blockLight;

        if (light < 14)
        {
            int opacity = block.getLightOpacity(this.worldObj, pos);
            if (opacity < 1)
            {
                opacity = 1;
            }
            else if (opacity >= 15)
            {
                if (blockLight > 0)
                {
                    opacity = 1;
                }
                else
                {
                    return 0;
                }
            }

            for (BlockPos blockpos : GCCoreUtil.getPositionsAdjoining(pos))
            {
                int neighbourLight = this.worldObj.getLightFor(lightType, blockpos) - opacity;  //Easily picks up neighbour lighting if opacity is low...
                if (neighbourLight > light)
                {
                    if (neighbourLight >= 14)
                    {
                        return neighbourLight;
                    }
                    light = neighbourLight;
                }
            }
        }

        return light;
    }
    
    private void setLightFor_preChecked(EnumSkyBlock type, BlockPos pos, int lightValue)
    {
        this.worldObj.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).setLightFor(type, pos, lightValue);
    }

    public boolean getEnabled()
    {
        return !RedstoneUtil.isBlockReceivingRedstone(this.worldObj, this.getPos());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }


    private void checkedAdd(BlockVec3 vec)
    {
        int dx = this.pos.getX() - vec.x;
        int dz = this.pos.getZ() - vec.z;
        if (dx < -8191 || dx > 8192) return;
        if (dz < -8191 || dz > 8192) return;
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        bucket.add(vec.y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
    }

    /**
     * Currently unused - the sided implementation is used instead
     */
    private boolean checkedContains(BlockVec3 vec)
    {
        int dx = this.pos.getX() - vec.x;
        int dz = this.pos.getZ() - vec.z;
        if (dx < -8191 || dx > 8192) return true;
        if (dz < -8191 || dz > 8192) return true;
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        return bucket.contains(vec.y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
    }

    private boolean checkedContains(BlockVec3 vec, int side)
    {
        int y = vec.y;
        int dx = this.pos.getX() - vec.x;
        int dz = this.pos.getZ() - vec.z;
        switch (side)
        {
        case 0:
            y--;
            if (y < 0) return true;
            break;
        case 1:
            y++;
            if (y > 255) return true;
            break;
        case 2:
            dz++;
            break;
        case 3:
            dz--;
            break;
        case 4:
            dx++;
            break;
        case 5:
            dx--;
        }
        if (dx < -8191 || dx > 8192) return true;
        if (dz < -8191 || dz > 8192) return true;
        intBucket bucket = buckets[((dx & 15) << 4) + (dz & 15)];
        return bucket.contains(y + ((dx & 0x3FF0) + ((dz & 0x3FF0) << 10) << 4));
    }

    private static void checkedInit(intBucket[] buckets)
    {
        for (int i = 0; i < 256; i++)
        {
            buckets[i] = new intBucket();
        }
    }

    private void checkedClear()
    {
        for (int i = 0; i < 256; i++)
        {
            this.buckets[i].clear();
        }
    }

    public static class intBucket
    {
        private int maxSize = 12;  //default size
        private int size = 0;
        private int[] table = new int[maxSize];
        
        public void add(int i)
        {
            if (this.contains(i))
                return;
            
            if (size >= maxSize)
            {
                int[] newTable = new int[maxSize + maxSize];
                System.arraycopy(table, 0, newTable, 0, maxSize);
                table = newTable;
                maxSize += maxSize;
            }
            table[size] = i;
            size++;
        }

        public boolean contains(int test)
        {
            for (int i = size - 1; i >= 0; i--)
            {
                if (table[i] == test)
                    return true;
            }
            return false;
        }
        
        public void clear()
        {
            size = 0;
        }
        
        public int size()
        {
            return size;
        }
          
        public int[] contents()
        {
            return table;
        }
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        data[0] = this.facing;
    }

    @Override
    public void updateClient(List<Object> data)
    {
        this.facing = (Integer) data.get(1);
        this.revertAir();
        this.thisAABB = null;
        this.ticks = 86;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
        }
        return this.renderAABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        for (BlockVec3 vec : this.airToRestore)
        {
            sendData.add(vec);
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        while (buffer.readableBytes() >= 12)
        {
            int x = buffer.readInt();
            int y = buffer.readInt();
            int z = buffer.readInt();
            this.airToRestore.add(new BlockVec3(x, y, z));
        }
    }
}
