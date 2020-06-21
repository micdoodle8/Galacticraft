package micdoodle8.mods.galacticraft.core.tile;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockBrightLamp;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

//ITileClientUpdates for changing in facing;  IPacketReceiver for initial transfer of NBT Data (airToRestore)
public class TileEntityArclamp extends TileEntity implements ITickableTileEntity, ITileClientUpdates, IPacketReceiver
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.arcLamp)
    public static TileEntityType<TileEntityArclamp> TYPE;

    private static final int LIGHTRANGE = 14;
    private int ticks = 0;
    private Direction sideRear = Direction.byIndex(0);
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
    private Vec3d thisPos;
    private Direction facingSide = Direction.byIndex(0);

    public TileEntityArclamp()
    {
        super(TYPE);
    }

    static
    {
        bucketsServer = new intBucket[256];
        bucketsClient = new intBucket[256];
        checkedInit(bucketsServer);
        checkedInit(bucketsClient);
    }
    
    @Override
    public void tick()
    {
        boolean initialLight = false;
//        if (this.updateClientFlag)
//        {
//            this.updateAllInDimension();
//            this.updateClientFlag = false;
//        }

        if (RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
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
                Direction facing = this.getBlockState().get(BlockBrightLamp.FACING);
                this.sideRear = facing;
                switch (facing)
                {
                case DOWN:
                    this.facingSide = Direction.byIndex(this.facing + 2);
                    break;
                case UP:
                    this.facingSide = Direction.byIndex(this.facing + 2);
                    break;
                case NORTH:
                    this.facingSide = Direction.byIndex(this.facing);
                    if (this.facing > 1)
                    {
                        this.facingSide = Direction.byIndex(7 - this.facing);
                    }
                    break;
                case SOUTH:
                    this.facingSide = Direction.byIndex(this.facing);
                    if (this.facing > 1)
                    {
                        this.facingSide = Direction.byIndex(this.facingSide.getIndex() + 2);
                    }
                    break;
                case WEST:
                    this.facingSide = Direction.byIndex(this.facing);
                    break;
                case EAST:
                    this.facingSide = Direction.byIndex(this.facing);
                    if (this.facing > 1)
                    {
                        this.facingSide = Direction.byIndex(5 - this.facing);
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

            if (!this.world.isRemote && this.world.rand.nextInt(20) == 0)
            {
                List<Entity> moblist = this.world.getEntitiesInAABBexcluding(null, this.thisAABB, (entity) -> entity instanceof IMob);

                if (!moblist.isEmpty())
                {
                    Vec3d thisVec3 = new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                    for (Entity entry : moblist)
                    {
                        if (!(entry instanceof CreatureEntity))
                        {
                            continue;
                        }
                        CreatureEntity mob = (CreatureEntity) entry;
                        //Check whether the mob can actually *see* the arclamp tile
                        //if (this.world.func_147447_a(thisPos, new Vec3(entry.posX, entry.posY, entry.posZ), true, true, false) != null) continue;

                        PathNavigator nav = mob.getNavigator();
                        if (nav == null)
                        {
                            continue;
                        }
                        
                        Vec3d vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(mob, 28, 11, this.thisPos);
                        if (vecNewTarget == null)
                        {
                            continue;
                        }

                        double distanceNew = vecNewTarget.squareDistanceTo(thisVec3);
                        double distanceCurrent = thisVec3.squareDistanceTo(new Vec3d(mob.posX, mob.posY, mob.posZ));
                        if (distanceNew > distanceCurrent)
                        {
                            Vec3d vecOldTarget = null;
                            if (nav.getPath() != null && !nav.getPath().isFinished())
                            {
                                vecOldTarget = nav.getPath().getPosition(mob);
                            }
                            if (vecOldTarget == null || distanceCurrent > vecOldTarget.squareDistanceTo(thisVec3))
                            {
                                nav.tryMoveToXYZ(vecNewTarget.x, vecNewTarget.y, vecNewTarget.z, 1.3D);
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
            rangeForSide[i] = (i == this.sideRear.getIndex()) ? 2 : (i == (this.facingSide.getIndex() ^ 1)) ? 4 : 25;
        }
        return new AxisAlignedBB(x - rangeForSide[4], y - rangeForSide[0], z - rangeForSide[2], x + rangeForSide[5], y + rangeForSide[1], z + rangeForSide[3]);
    }

    @Override
    public void onLoad()
    {
        this.thisPos = new Vec3d(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D);
        this.ticks = 0;
        this.thisAABB = null;
        if (this.world.isRemote)
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
    public void remove()
    {
        if (this.lightUpdateBlockList == null)
        {
            if (this.world.isRemote)
            {
                this.buckets = bucketsClient;
                this.usingBuckets = usingBucketsClient;
                if (lightUpdateBlockListClient == null)
                {
                    lightUpdateBlockListClient = new int[SIZELIST];
                }
                this.lightUpdateBlockList = lightUpdateBlockListClient;
                this.usingLightList = usingLightListClient;
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
            }
        }
        this.revertAir();
        this.isActive = false;
        super.remove();
    }

    public void lightArea()
    {
        if (this.usingBuckets.getAndSet(true) || this.usingLightList.getAndSet(true))
        {
            return;
        }
//        long time1 = System.nanoTime();
        int index = 0;
        Block air = Blocks.AIR;
        Block breatheableAirID = GCBlocks.breatheableAir;
        BlockState brightAir = GCBlocks.brightAir.getDefaultState();
        BlockState brightBreatheableAir = GCBlocks.brightBreatheableAir.getDefaultState();
        boolean dirty = false;
        checkedClear();
        HashSet<BlockVec3> airToRevert = new HashSet<>();
        airToRevert.addAll(airToRestore);
        LinkedList<BlockVec3> airNew = new LinkedList<>();
        LinkedList<BlockVec3> currentLayer = new LinkedList<>();
        LinkedList<BlockVec3> nextLayer = new LinkedList<>();
        BlockVec3 thisvec = new BlockVec3(this);
        currentLayer.add(thisvec);
        World world = this.world;
        int sideskip1 = this.sideRear.getIndex();
        int sideskip2 = this.facingSide.getIndex() ^ 1;
        int side, bits;
        for (int i = 0; i < 6; i++)
        {
            if (i != sideskip1 && i != sideskip2 && i != (sideskip1 ^ 1) && i != (sideskip2 ^ 1))
            {
                BlockVec3 onEitherSide = thisvec.newVecSide(i);
                BlockState state = onEitherSide.getBlockStateSafe_noChunkLoad(world);
                if (state != null && state.getOpacity(world, onEitherSide.toBlockPos()) < 15)
                {
                    currentLayer.add(onEitherSide);
                }
            }
        }
        BlockVec3 inFront = new BlockVec3(this);
        for (int i = 0; i < 4; i++)
        {
            inFront = inFront.newVecSide(this.facingSide.getIndex());
            BlockState state = inFront.getBlockStateSafe_noChunkLoad(world);
            if (state != null && state.getOpacity(world, inFront.toBlockPos()) == 15)
            {
                break;
            }
            inFront = inFront.newVecSide(sideskip1 ^ 1);
            state = inFront.getBlockStateSafe_noChunkLoad(world);
            if (state != null && state.getOpacity(world, inFront.toBlockPos()) < 15)
            {
                currentLayer.add(inFront);
            }
            else
            {
                break;
            }
        }

        inFront = new BlockVec3(this).newVecSide(this.facingSide.getIndex());

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
                    case DOWN:
                        offset = inFront.y - vec.y;
                        break;
                    case UP:
                        offset = vec.y - inFront.y;
                        break;
                    case NORTH:
                        offset = inFront.z - vec.z;
                        break;
                    case SOUTH:
                        offset = vec.z - inFront.z;
                        break;
                    case WEST:
                        offset = inFront.x - vec.x;
                        break;
                    case EAST:
                        offset = vec.x - inFront.x;
                        break;
                    }
                    int offset2 = 0;
                    switch (this.sideRear.getOpposite())
                    {
                    case DOWN:
                        offset2 = inFront.y - vec.y;
                        break;
                    case UP:
                        offset2 = vec.y - inFront.y;
                        break;
                    case NORTH:
                        offset2 = inFront.z - vec.z;
                        break;
                    case SOUTH:
                        offset2 = vec.z - inFront.z;
                        break;
                    case WEST:
                        offset2 = inFront.x - vec.x;
                        break;
                    case EAST:
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

                        BlockState bs = sideVec.getBlockStateSafe_noChunkLoad(world);
                        if (bs == null)
                        {
                            side++;
                            continue;
                        }
                        Block b = bs.getBlock();
                        if (b instanceof AirBlock)
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
                                if (toAdd && b != null && b.getOpacity(bs, world, sideVec.toBlockPos()) == 0)
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
                    Block id = vec.getBlockStateSafe_noChunkLoad(world).getBlock();
                    if (Blocks.AIR == id)
                    {
                        this.brightenAir(world, vec, brightAir);
                        index = this.checkLightPartA(LightType.BLOCK, vec.toBlockPos(), index);
                        dirty = true;
                    }
                    else if (id == breatheableAirID)
                    {
                        this.brightenAir(world, vec, brightBreatheableAir);
                        index = this.checkLightPartA(LightType.BLOCK, vec.toBlockPos(), index);
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
            this.checkLightPartB(LightType.BLOCK, index);
        }
        
        //Look for any holdover bright blocks which are no longer lit (e.g. because the Arc Lamp became blocked in a tunnel)
        airToRevert.removeAll(airNew);
        index = 0;
        dirty = false;
        for (Object obj : airToRevert)
        {
            BlockVec3 vec = (BlockVec3) obj;
            this.setDarkerAir(vec);
            index = this.checkLightPartA(LightType.BLOCK, vec.toBlockPos(), index);
            this.airToRestore.remove(vec);
            dirty = true;
        }

        if (dirty)
        {
            this.markDirty();
            this.checkLightPartB(LightType.BLOCK, index);
        }

//        long time3 = System.nanoTime();
//        float total = (time3 - time1) / 1000000.0F;
//        GCLog.info("   Total Time taken: " + String.format("%.2f", total) + "ms");

        this.usingBuckets.set(false);
        this.usingLightList.set(false);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        this.facing = nbt.getInt("Facing");
        if (GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER)
        {
            this.airToRestore.clear();
            ListNBT airBlocks = nbt.getList("AirBlocks", 10);
            if (airBlocks.size() > 0)
            {
                for (int j = airBlocks.size() - 1; j >= 0; j--)
                {
                    CompoundNBT tag1 = airBlocks.getCompound(j);
                    if (tag1 != null)
                    {
                        this.airToRestore.add(BlockVec3.read(tag1));
                    }
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);

        nbt.putInt("Facing", this.facing);

        ListNBT airBlocks = new ListNBT();

        for (BlockVec3 vec : this.airToRestore)
        {
            CompoundNBT tag = new CompoundNBT();
            vec.write(tag);
            airBlocks.add(tag);
        }
        nbt.put("AirBlocks", airBlocks);
        return nbt;
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

    private void brightenAir(World world, BlockVec3 vec, BlockState newState)
    {
        BlockPos blockpos = vec.toBlockPos();
        Chunk chunk = this.world.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.world.isRemote && oldState != null) this.world.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        //No block tick on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
        this.airToRestore.add(vec);
    }
    
    private void setDarkerAir(BlockVec3 vec)
    {
        BlockPos blockpos = vec.toBlockPos();
        Block b = this.world.getBlockState(blockpos).getBlock();
        BlockState newState;
        if (b == GCBlocks.brightAir)
        {
            newState = Blocks.AIR.getDefaultState();
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
        
        Chunk chunk = this.world.getChunkAt(blockpos);
        BlockState oldState = chunk.setBlockState(blockpos, newState, false);
        if (this.world.isRemote && oldState != null) this.world.markAndNotifyBlock(blockpos, chunk, oldState, newState, 2);
        //No block tick on server - not necessary for changing air to air (also must not trigger a sealer edge check!)
    }

    private void revertAir()
    {
        if (this.airToRestore.isEmpty())
            return;
        
        if (this.usingLightList == null)
        {
            this.usingLightList = this.world.isRemote ? usingLightListClient : usingLightListServer;   
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
                index = this.checkLightPartA(LightType.BLOCK, vec.toBlockPos(), index);
            }
            this.checkLightPartB(LightType.BLOCK, index);
            this.usingLightList.set(false);
        }
        this.airToRestore.clear();
        this.checkLightFor(LightType.BLOCK, this.pos);
    }

    public boolean checkLightFor(LightType lightType, BlockPos bp)
    {
        if (!this.world.isAreaLoaded(bp, 17))
        {
            return false;
        }

        World world = this.world;
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

                        range = MathHelper.abs(xx - testx) + MathHelper.abs(yy - testy) + MathHelper.abs(zz - testz);
                        if (range < 17)
                        {
                            GCCoreUtil.getPositionsAdjoining(xx, yy, zz, result);
                            for (BlockPos vec : result)
                            {
                                savedLight = world.getLightFor(lightType, vec);
                                if (savedLight == 0) continue;  //eliminate backtracking
                                BlockState bs = world.getBlockState(vec);
                                opacity = bs.getOpacity(world, vec);
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
//                if (world.isRemote) world.notifyLightSet(blockpos); TODO tick to new lighting manager system
                if (world.isRemote) world.getChunkProvider().getLightManager().checkBlock(blockpos);

                if (rawLight > savedLight)
                {
                    range = MathHelper.abs(xx - testx) + MathHelper.abs(yy - testy) + MathHelper.abs(zz - testz);
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
//                world.notifyLightSet(blockpos); TODO tick to new lighting manager system
                world.getChunkProvider().getLightManager().checkBlock(blockpos);
            }
        }

        return true;
    }

    public int checkLightPartA(LightType lightType, BlockPos bp, int indexIn)
    {
        if (indexIn >= SIZELIST)
        {
            return indexIn;
        }

        World world = this.world;
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
                range = MathHelper.abs(xx - testx) + MathHelper.abs(yy - testy) + MathHelper.abs(zz - testz);
                if (range < 17)
                {
                    arraylight = value >> 21 & 15;
                    GCCoreUtil.getPositionsAdjoiningLoaded(xx, yy, zz, neighbours, world);
                    for (BlockPos vec : neighbours)
                    {
                        savedLight = world.getLightFor(lightType, vec);
                        if (savedLight == 0) continue;  //eliminate backtracking
                        BlockState bs = world.getBlockState(vec);
                        opacity = bs.getOpacity(world, vec);
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

    public boolean checkLightPartB(LightType lightType, int index)
    {
        World world = this.world;
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
//                if (world.isRemote) world.notifyLightSet(blockpos); TODO tick to new lighting manager system
                if (world.isRemote) world.getChunkProvider().getLightManager().checkBlock(blockpos);

                if (rawLight > savedLight)
                {
                    range = MathHelper.abs(xx - testx) + MathHelper.abs(yy - testy) + MathHelper.abs(zz - testz);
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
//                world.notifyLightSet(blockpos); TODO tick to new lighting manager system
                world.getChunkProvider().getLightManager().checkBlock(blockpos);
            }
        }
        return true;
    }
    
/**
 * From vanilla.  This is buggy, gets confused if two low opacity blocks adjacent (e.g. redstone wire, stairs)
 * if those blocks are receiving similar light levels from another source
 */
    private int getRawLight(BlockPos pos, LightType lightType)
    {
        BlockState bs = this.world.getBlockState(pos);
        Block block = bs.getBlock();
        int blockLight = block.getLightValue(bs, this.world, pos);
        int light = lightType == LightType.SKY ? 0 : blockLight;

        if (light < 14)
        {
            int opacity = bs.getOpacity(this.world, pos);
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
                int neighbourLight = this.world.getLightFor(lightType, blockpos) - opacity;  //Easily picks up neighbour lighting if opacity is low...
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
    
    private void setLightFor_preChecked(LightType type, BlockPos pos, int lightValue)
    {
//        this.world.getChunk(pos.getX() >> 4, pos.getZ() >> 4).getWorldLightManager().setLightFor(type, pos, lightValue); TODO Set light
    }

    public boolean getEnabled()
    {
        return !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos());
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

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
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(1, 1, 1));
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
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
