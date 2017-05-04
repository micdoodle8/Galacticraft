package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
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
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
    private HashSet<BlockVec3> airToRestore = new HashSet();
    private static intBucket[] buckets;
    private static AtomicBoolean usingBuckets = new AtomicBoolean();
    private boolean isActive = false;
    private AxisAlignedBB thisAABB;
    private AxisAlignedBB renderAABB;
    private Vec3d thisPos;
    private int facingSide = 0;

    static
    {
        buckets = new intBucket[256];
        checkedInit();
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
        else if (!this.isActive)
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

            if (!this.worldObj.isRemote && this.worldObj.rand.nextInt(10) == 0)
            {
                List<Entity> moblist = this.worldObj.getEntitiesInAABBexcluding(null, this.thisAABB, IMob.MOB_SELECTOR);

                if (!moblist.isEmpty())
                {
                    Vec3d thisVec3 = new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
                    for (Entity entry : moblist)
                    {
                        if (!(entry instanceof EntityCreature))
                        {
                            continue;
                        }
                        EntityCreature mob = (EntityCreature) entry;
                        //Check whether the mob can actually *see* the arclamp tile
                        //if (this.worldObj.func_147447_a(thisPos, Vec3d.createVectorHelper(e.posX, e.posY, e.posZ), true, true, false) != null) continue;

                        PathNavigate nav = mob.getNavigator();
                        if (nav == null)
                        {
                            continue;
                        }
                        
                        Vec3d vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(mob, 28, 11, this.thisPos);
                        if (vecNewTarget == null)
                        {
                            continue;
                        }

                        double distanceNew = vecNewTarget.distanceTo(thisVec3);
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
        return new AxisAlignedBB(x - rangeForSide[4], y - rangeForSide[0], z - rangeForSide[2], x + rangeForSide[5], y + rangeForSide[1], z + rangeForSide[3]);
    }

    @Override
    public void validate()
    {
        super.validate();
        this.thisPos = new Vec3d(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D);
        this.ticks = 0;
        this.thisAABB = null;
        if (this.worldObj.isRemote)
        {
            this.clientValidate();
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamic(this));
        }
        else
        {
            this.isActive = true;
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
        if (usingBuckets.getAndSet(true))
        {
            return;
        }
        Block air = Blocks.AIR;
        Block breatheableAirID = GCBlocks.breatheableAir;
        IBlockState brightAir = GCBlocks.brightAir.getDefaultState();
        IBlockState brightBreatheableAir = GCBlocks.brightBreatheableAir.getDefaultState();
        boolean dirty = false;
        checkedClear();
        HashSet airToRevert = new HashSet();
        airToRevert.addAll(airToRestore);
        LinkedList airNew = new LinkedList();
        LinkedList<BlockVec3> currentLayer = new LinkedList();
        LinkedList<BlockVec3> nextLayer = new LinkedList();
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
                IBlockState state = onEitherSide.getBlockStateSafe_noChunkLoad(world);
                if (state.getBlock().getLightOpacity(state) < 15)
                {
                    currentLayer.add(onEitherSide);
                }
            }
        }
        BlockVec3 inFront = new BlockVec3(this);
        for (int i = 0; i < 4; i++)
        {
            inFront = inFront.newVecSide(this.facingSide);
            IBlockState state = inFront.getBlockStateSafe_noChunkLoad(world);
            if (state.getBlock().getLightOpacity(state) == 15)
            {
                break;
            }
            inFront = inFront.newVecSide(sideskip1 ^ 1);
            state = inFront.getBlockStateSafe_noChunkLoad(world);
            if (state.getBlock().getLightOpacity(state) < 15)
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

                        IBlockState bs = sideVec.getBlockStateSafe_noChunkLoad(world);
                        Block b = bs.getBlock();
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
                                if (toAdd && b != null && b.getLightOpacity(bs, world, sideVec.toBlockPos()) == 0)
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
                        dirty = true;
                    }
                    else if (id == breatheableAirID)
                    {
                        this.brightenAir(world, vec, brightBreatheableAir);
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

        if (dirty) this.markDirty();
        
        //Look for any holdover bright blocks which are no longer lit (e.g. because the Arc Lamp became blocked in a tunnel)
        airToRevert.removeAll(airNew);
        for (Object obj : airToRevert)
        {
            BlockVec3 vec = (BlockVec3) obj;
            Block b = vec.getBlockState(this.worldObj).getBlock();
            if (b == GCBlocks.brightAir)
            {
                this.worldObj.setBlockState(vec.toBlockPos(), Blocks.AIR.getDefaultState(), 2);
            }
            else if (b == GCBlocks.brightBreatheableAir)
            {
                this.worldObj.setBlockState(vec.toBlockPos(), GCBlocks.breatheableAir.getDefaultState(), 2);
            }
            this.airToRestore.remove(vec);
        }
        usingBuckets.set(false);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.facing = nbt.getInteger("Facing");

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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
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

    private void brightenAir(World world, BlockVec3 vec, IBlockState brighterAir)
    {
//        brighterAir = Blocks.lever.getDefaultState();
        world.setBlockState(vec.toBlockPos(), brighterAir, 4);
        this.airToRestore.add(vec);
    }
    
    private void revertAir()
    {
        Block brightAir = GCBlocks.brightAir;
        Block brightBreatheableAir = GCBlocks.brightBreatheableAir;
        for (BlockVec3 vec : this.airToRestore)
        {
            IBlockState b = vec.getBlockState(this.worldObj);
            if (b.getBlock() == brightAir)
            {
                this.worldObj.setBlockState(vec.toBlockPos(), Blocks.AIR.getDefaultState(), 4);
            }
            else if (b.getBlock() == brightBreatheableAir)
            {
                this.worldObj.setBlockState(vec.toBlockPos(), GCBlocks.breatheableAir.getDefaultState(), 4);
                //No block update - not necessary for changing air to air, also must not trigger a sealer edge check
            }
        }
        this.airToRestore.clear();
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

    private static void checkedInit()
    {
        for (int i = 0; i < 256; i++)
        {
            buckets[i] = new intBucket();
        }
    }

    private static void checkedClear()
    {
        for (int i = 0; i < 256; i++)
        {
            buckets[i].clear();
        }
    }

    public static class intBucket
    {
        private int maxSize = 24;  //default size
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
