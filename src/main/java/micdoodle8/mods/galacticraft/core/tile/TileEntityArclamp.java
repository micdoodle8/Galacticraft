package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class TileEntityArclamp extends TileEntity
{
    private int ticks = 0;
    private int sideRear = 0;
    public int facing = 0;
    private HashSet<BlockVec3> airToRestore = new HashSet();
    private boolean isActive = false;
    private AxisAlignedBB thisAABB;
    private Vec3 thisPos;
    private int facingSide = 0;
	public boolean updateClientFlag;

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        boolean firstTick = false;
        if (this.updateClientFlag)
        {
        	GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_ARCLAMP_FACING, new Object[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.facing } ), this.worldObj.provider.dimensionId);
        	this.updateClientFlag = false;
        }

        if (!this.worldObj.isRemote && this.isActive)
        {
            if (this.thisAABB == null)
            {
                firstTick = true;
                int side = this.getBlockMetadata();
                switch (side)
                {
                case 0:
                    this.sideRear = side; //Down
                    this.facingSide = this.facing + 2;
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 20, this.getPos().getY() - 8, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 1:
                    this.sideRear = side; //Up
                    this.facingSide = this.facing + 2;
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 8, this.getPos().getZ() + 20);
                    break;
                case 2:
                    this.sideRear = side; //North
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 7 - this.facing;
                    }
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 8, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 3:
                    this.sideRear = side; //South
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide += 2;
                    }
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 8);
                    break;
                case 4:
                    this.sideRear = side; //West
                    this.facingSide = this.facing;
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 8, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 20, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                case 5:
                    this.sideRear = side; //East
                    this.facingSide = this.facing;
                    if (this.facing > 1)
                    {
                        this.facingSide = 5 - this.facing;
                    }
                    this.thisAABB = AxisAlignedBB.fromBounds(this.getPos().getX() - 20, this.getPos().getY() - 20, this.getPos().getZ() - 20, this.getPos().getX() + 8, this.getPos().getY() + 20, this.getPos().getZ() + 20);
                    break;
                default:
                    return;
                }
            }

            if (firstTick || this.ticks % 100 == 0)
            {
                this.lightArea();
            }

            if (this.worldObj.rand.nextInt(20) == 0)
            {
                List<Entity> moblist = this.worldObj.getEntitiesWithinAABBExcludingEntity(null, this.thisAABB, IMob.mobSelector);

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
                        //if (this.worldObj.func_147447_a(thisPos, Vec3.createVectorHelper(e.posX, e.posY, e.posZ), true, true, false) != null) continue;

                        Vec3 vecNewTarget = RandomPositionGenerator.findRandomTargetBlockAwayFrom(e, 16, 7, this.thisPos);
                        if (vecNewTarget == null)
                        {
                            continue;
                        }
                        PathNavigate nav = e.getNavigator();
                        if (nav == null)
                        {
                            continue;
                        }
                        Vec3 vecOldTarget = null;
                        if (nav.getPath() != null && !nav.getPath().isFinished())
                        {
                            vecOldTarget = nav.getPath().getPosition(e);
                        }
                        double distanceNew = vecNewTarget.squareDistanceTo(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());

                        if (distanceNew > e.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()))
                        {
                            if (vecOldTarget == null || distanceNew > vecOldTarget.squareDistanceTo(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()))
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
        this.thisPos = Vec3.createVectorHelper(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D);
        this.ticks = 0;
        this.thisAABB = null;
        this.isActive = true;
        if (this.worldObj.isRemote)
        {
        	GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_ARCLAMP_FACING, new Object[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ() } ));
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
    }

    public void lightArea()
    {
        Block air = Blocks.air;
        Block breatheableAirID = GCBlocks.breatheableAir;
        Block brightAir = GCBlocks.brightAir;
        Block brightBreatheableAir = GCBlocks.brightBreatheableAir;
        HashSet<BlockVec3> checked = new HashSet();
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
                Block b = onEitherSide.getBlockIDsafe_noChunkLoad(world);
                if (b != null && b.getLightOpacity() < 15)
                {
                    currentLayer.add(onEitherSide);
                }
            }
        }
        BlockVec3 inFront = new BlockVec3(this);
        for (int i = 0; i < 5; i++)
        {
            inFront = inFront.newVecSide(this.facingSide).newVecSide(sideskip1 ^ 1);
            Block b = inFront.getBlockIDsafe_noChunkLoad(world);
            if (b != null && b.getLightOpacity() < 15)
            {
                currentLayer.add(inFront);
            }
        }

        for (int count = 0; count < 14; count++)
        {
            int side;
            for (BlockVec3 vec : currentLayer)
            {
                side = 0;
                boolean allAir = true;
                do
                {
                    //Skip the side which this was entered from
                    //and never go 'backwards'
                    if (!vec.sideDone[side])
                    {
                        BlockVec3 sideVec = vec.newVecSide(side);

                        if (!checked.contains(sideVec))
                        {
                            checked.add(sideVec);

                            Block b = sideVec.getBlockIDsafe_noChunkLoad(world);
                            if (b.isAir(world, sideVec.x, sideVec.y, sideVec.z))
                            {
                                if (side != sideskip1 && side != sideskip2)
                                {
                                    nextLayer.add(sideVec);
                                }
                            }
                            else
                            {
                                allAir = false;
                                if (b != null && b.getLightOpacity(world, sideVec.x, sideVec.y, sideVec.z) == 0)
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

                if (!allAir)
                {
                    Block id = vec.getBlockIDsafe_noChunkLoad(world);
                    if (id.isAir(world, vec.x, vec.y, vec.z))
                    {
                        if (Blocks.air == id)
                        {
                            world.setBlock(vec.x, vec.y, vec.z, brightAir, 0, 2);
                            this.airToRestore.add(vec);
                            this.markDirty();
                        }
                        else if (id == breatheableAirID)
                        {
                            world.setBlock(vec.x, vec.y, vec.z, brightBreatheableAir, 0, 2);
                            this.airToRestore.add(vec);
                            this.markDirty();
                        }
                    }
                }
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
        this.updateClientFlag = true;

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

        GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_ARCLAMP_FACING, new Object[] { this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.facing } ), this.worldObj.provider.dimensionId);
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
            Block b = vec.getBlock(this.worldObj);
            if (b == brightAir)
            {
                this.worldObj.setBlock(vec.x, vec.y, vec.z, Blocks.air, 0, 2);
            }
            else if (b == brightBreatheableAir)
            {
                this.worldObj.setBlock(vec.x, vec.y, vec.z, GCBlocks.breatheableAir, 0, 2);
                //No block update - not necessary for changing air to air, also must not trigger a sealer edge check
            }
        }
        this.airToRestore.clear();
    }
}
