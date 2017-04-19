package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.ITileClientUpdates;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class TileEntityArclamp extends TileEntity implements ITickable, ITileClientUpdates
{
    private int ticks = 0;
    private int sideRear = 0;
    public int facing = 0;
    private HashSet<BlockVec3> airToRestore = new HashSet();
    private intBucket[] buckets;
    private boolean isActive = false;
    private AxisAlignedBB thisAABB;
    private AxisAlignedBB renderAABB;
    private Vec3d thisPos;
    private int facingSide = 0;
    
    @Override
    public void update()
    {
        if (this.worldObj.isRemote)
        {
            return;
        }

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
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 20, this.getPos().getY() - 8, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 1:
                    this.sideRear = side; //Up
                    this.facingSide = this.facing + 2;
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 8, this.getPos().getZ() + 20);
                    break;
                case 2:
                    this.sideRear = side; //North
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 7 - this.facing;
                    }
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 8, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 3:
                    this.sideRear = side; //South
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide += 2;
                    }
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 8);
                    break;
                case 4:
                    this.sideRear = side; //West
                    this.facingSide = this.facing;
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 8, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 5:
                    this.sideRear = side; //East
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 5 - this.facing;
                    }
                    this.thisAABB = new AxisAlignedBB(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 8, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                default:
                    return;
                }
            }

            if (initialLight || this.ticks % 100 == 0)
            {
                this.lightArea();
            }

            if (this.worldObj.rand.nextInt(20) == 0)
            {
                List<Entity> moblist = this.worldObj.getEntitiesInAABBexcluding(null, this.thisAABB, IMob.MOB_SELECTOR);

                if (!moblist.isEmpty())
                {
                    for (Entity entry : moblist)
                    {
                        if (!(entry instanceof EntityCreature))
                        {
                            continue;
                        }
                        EntityCreature e = (EntityCreature) entry;
                        //Check whether the mob can actually *see* the arclamp tile
                        //if (this.worldObj.func_147447_a(thisPos, Vec3d.createVectorHelper(e.posX, e.posY, e.posZ), true, true, false) != null) continue;

                        Vec3d vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(e, 16, 7, this.thisPos);
                        if (vecNewTarget == null)
                        {
                            continue;
                        }
                        PathNavigate nav = e.getNavigator();
                        if (nav == null)
                        {
                            continue;
                        }
                        Vec3d vecOldTarget = null;
                        if (nav.getPath() != null && !nav.getPath().isFinished())
                        {
                            vecOldTarget = nav.getPath().getPosition(e);
                        }
                        double distanceNew = vecNewTarget.distanceTo(new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()));

                        if (distanceNew > e.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()))
                        {
                            if (vecOldTarget == null || distanceNew > vecOldTarget.squareDistanceTo(new Vec3d(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ())))
                            {
                                e.getNavigator().tryMoveToXYZ(vecNewTarget.xCoord, vecNewTarget.yCoord, vecNewTarget.zCoord, 0.3D);
                                //System.out.println("Debug: Arclamp repelling entity: "+e.getClass().getSimpleName());
                            }
                        }
                    }
                }
            }
        }

        this.ticks++;
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
        }
        else
        {
            this.isActive = true;
            if (this.buckets == null)
            {
                this.buckets = new intBucket[256];
                this.checkedInit();
            }
        }
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            this.revertAir();
        }
        this.isActive = false;
        super.invalidate();
    }

    public void lightArea()
    {
        Block air = Blocks.AIR;
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block brightAir = GCBlocks.brightAir;
        Block brightBreatheableAir = GCBlocks.brightBreatheableAir;
        this.checkedClear();
        LinkedList<BlockVec3> currentLayer = new LinkedList();
        LinkedList<BlockVec3> nextLayer = new LinkedList();
        BlockVec3 thisvec = new BlockVec3(this);
        currentLayer.add(thisvec);
        World world = this.worldObj;
        int sideskip1 = this.sideRear;
        int sideskip2 = this.facingSide ^ 1;
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
        for (int i = 0; i < 5; i++)
        {
            inFront = inFront.newVecSide(this.facingSide).newVecSide(sideskip1 ^ 1);
            IBlockState state = inFront.getBlockStateSafe_noChunkLoad(world);
            if (state.getBlock().getLightOpacity(state) < 15)
            {
                currentLayer.add(inFront);
            }
        }

        int side, bits;

        for (int count = 0; count < 14; count++)
        {
            for (BlockVec3 vec : currentLayer)
            {
                side = 0;
                bits = vec.sideDoneBits;
                boolean allAir = true;
                do
                {
                    //Skip the side which this was entered from
                    //and never go 'backwards'
                    if ((bits & (1 << side)) == 0)
                    {
                        if (!checkedContains(vec, side))
                        {
                            BlockVec3 sideVec = vec.newVecSide(side);
                            checkedAdd(sideVec);

                            IBlockState state = sideVec.getBlockStateSafe_noChunkLoad(world);
                            if (state.getBlock() instanceof BlockAir)
                            {
                                if (side != sideskip1 && side != sideskip2)
                                {
                                    nextLayer.add(sideVec);
                                }
                            }
                            else
                            {
                                allAir = false;
                                if (state.getBlock().getLightOpacity(state, world, sideVec.toBlockPos()) == 0)
                                {
                                    if (side != sideskip1 && side != sideskip2)
                                    {
                                        nextLayer.add(sideVec);
                                    }
                                }
                            }
                        }
                    }
                    side++;
                }
                while (side < 6);

                boolean dirty = false;
                if (!allAir)
                {
                    IBlockState state = vec.getBlockStateSafe_noChunkLoad(world);
                    if (Blocks.AIR == state.getBlock())
                    {
                        world.setBlockState(vec.toBlockPos(), brightAir.getDefaultState(), 2);
                        this.airToRestore.add(vec);
                        dirty = true;
                    }
                    else if (state.getBlock() == breatheableAirID)
                    {
                        world.setBlockState(vec.toBlockPos(), brightBreatheableAir.getDefaultState(), 2);
                        this.airToRestore.add(vec);
                        dirty = true;
                    }
                }
                if (dirty) this.markDirty();
            }
            currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
            if (currentLayer.size() == 0)
            {
                break;
            }
        }
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
                this.worldObj.setBlockState(vec.toBlockPos(), Blocks.AIR.getDefaultState(), 2);
            }
            else if (b.getBlock() == brightBreatheableAir)
            {
                this.worldObj.setBlockState(vec.toBlockPos(), GCBlocks.breatheableAir.getDefaultState(), 2);
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

    private void checkedInit()
    {
        for (int i = 0; i < 256; i++)
        {
            this.buckets[i] = new intBucket();
        }
    }

    private void checkedClear()
    {
        for (int i = 0; i < 256; i++)
        {
            this.buckets[i].clear();
        }
    }

    public class intBucket
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
}
