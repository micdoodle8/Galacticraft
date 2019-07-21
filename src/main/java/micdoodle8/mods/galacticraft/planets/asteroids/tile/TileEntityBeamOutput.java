package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.power.ILaserNode;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;

public abstract class TileEntityBeamOutput extends TileEntityAdvanced implements ILaserNode
{
    public LinkedList<ILaserNode> nodeList = new LinkedList<ILaserNode>();
    @NetworkedField(targetSide = Side.CLIENT)
    public BlockPos targetVec = new BlockPos(-1, -1, -1);
    public float pitch;
    public float yaw;
    private BlockPos preLoadTarget = null;
    private BlockPos lastTargetVec = new BlockPos(-1, -1, -1);

    public TileEntityBeamOutput(String tileName)
    {
        super(tileName);
    }

    @Override
    public void update()
    {
        if (this.preLoadTarget != null)
        {
            TileEntity tileAtTarget = this.world.getTileEntity(this.preLoadTarget);

            if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
            {
                this.setTarget((ILaserNode) tileAtTarget);
                this.preLoadTarget = null;
            }
        }

        super.update();

        if (!this.targetVec.equals(this.lastTargetVec))
        {
            this.markDirty();
        }

        this.lastTargetVec = this.targetVec;

        if (this.world.isRemote)
        {
            this.updateOrientation();
        }
        else if (this.targetVec.getX() == -1 && this.targetVec.getY() == -1 && this.targetVec.getZ() == -1)
        {
            this.initiateReflector();
        }

    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.invalidateReflector();
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public void onLoad()
    {
    }

    @Override
    public void onChunkUnload()
    {
        this.invalidateReflector();
    }

    public void invalidateReflector()
    {
        for (ILaserNode node : this.nodeList)
        {
            node.removeNode(this);
        }

        this.nodeList.clear();
    }

    public void initiateReflector()
    {
        this.nodeList.clear();

        int chunkXMin = this.getPos().getX() - 15 >> 4;
        int chunkZMin = this.getPos().getZ() - 15 >> 4;
        int chunkXMax = this.getPos().getX() + 15 >> 4;
        int chunkZMax = this.getPos().getZ() + 15 >> 4;

        for (int cX = chunkXMin; cX <= chunkXMax; cX++)
        {
            for (int cZ = chunkZMin; cZ <= chunkZMax; cZ++)
            {
                if (this.world.getChunkProvider().getLoadedChunk(cX, cZ) != null)
                {
                    Chunk chunk = this.world.getChunkFromChunkCoords(cX, cZ);

                    for (Object obj : chunk.getTileEntityMap().values())
                    {
                        if (obj != this && obj instanceof ILaserNode)
                        {
                            BlockVec3 deltaPos = new BlockVec3(this).subtract(new BlockVec3(((ILaserNode) obj).getTile()));

                            if (deltaPos.x < 16 && deltaPos.y < 16 && deltaPos.z < 16)
                            {
                                ILaserNode laserNode = (ILaserNode) obj;

                                if (this.canConnectTo(laserNode) && laserNode.canConnectTo(this))
                                {
                                    this.addNode(laserNode);
                                    laserNode.addNode(this);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.setTarget(this.nodeList.peekFirst());
    }

    @Override
    public void addNode(ILaserNode node)
    {
        int index = -1;

        for (int i = 0; i < this.nodeList.size(); i++)
        {
            if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
            {
                index = i;
                break;
            }
        }

        if (index != -1)
        {
            this.nodeList.set(index, node);
            return;
        }

        if (this.nodeList.isEmpty())
        {
            this.nodeList.add(node);
        }
        else
        {
            int nodeCompare = this.nodeList.get(0).compareTo(node, new BlockVec3(this));

            if (nodeCompare <= 0)
            {
                this.nodeList.addFirst(node);
                return;
            }

            nodeCompare = this.nodeList.get(this.nodeList.size() - 1).compareTo(node, new BlockVec3(this));

            if (nodeCompare >= 0)
            {
                this.nodeList.addLast(node);
                return;
            }

            index = 1;

            while (index < this.nodeList.size())
            {
                index++;
            }

            this.nodeList.add(index, node);
        }
    }

    @Override
    public void removeNode(ILaserNode node)
    {
        int index = -1;

        for (int i = 0; i < this.nodeList.size(); i++)
        {
            if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(node.getTile())))
            {
                index = i;
                break;
            }
        }

        if (new BlockVec3(node.getTile()).equals(new BlockVec3(this.targetVec)))
        {
            if (index == 0)
            {
                if (this.nodeList.size() > 1)
                {
                    this.setTarget(this.nodeList.get(index + 1));
                }
                else
                {
                    this.setTarget(null);
                }
            }
            else if (index > 0)
            {
                this.setTarget(this.nodeList.get(index - 1));
            }
            else
            {
                this.setTarget(null);
            }
        }

        if (index != -1)
        {
            this.nodeList.remove(index);
        }
    }

    public void updateOrientation()
    {
        if (this.getTarget() != null)
        {
            Vector3 direction = Vector3.subtract(this.getOutputPoint(false), this.getTarget().getInputPoint()).normalize();
            this.pitch = (float) -Vector3.getAngle(new Vector3(-direction.x, -direction.y, -direction.z), new Vector3(0, 1, 0)) * Constants.RADIANS_TO_DEGREES + 90;
            this.yaw = (float) -(Math.atan2(direction.z, direction.x) * Constants.RADIANS_TO_DEGREES) + 90;
        }
    }

    @Override
    public TileEntity getTile()
    {
        return this;
    }

    @Override
    public int compareTo(ILaserNode otherNode, BlockVec3 origin)
    {
        int thisDistance = new BlockVec3(this).subtract(origin).getMagnitudeSquared();
        int otherDistance = new BlockVec3(otherNode.getTile()).subtract(origin).getMagnitudeSquared();

        if (thisDistance < otherDistance)
        {
            return 1;
        }
        else if (thisDistance > otherDistance)
        {
            return -1;
        }

        return 0;
    }

    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.nodeList.size() > 1)
        {
            int index = -1;

            if (this.getTarget() != null)
            {
                for (int i = 0; i < this.nodeList.size(); i++)
                {
                    if (new BlockVec3(this.nodeList.get(i).getTile()).equals(new BlockVec3(this.getTarget().getTile())))
                    {
                        index = i;
                        break;
                    }
                }
            }

            if (index == -1)
            {
                // This shouldn't happen, but just in case...
                this.initiateReflector();
            }
            else
            {
                index++;
                index %= this.nodeList.size();
                this.setTarget(this.nodeList.get(index));
                return true;
            }
        }

        return false;
    }

    @Override
    public ILaserNode getTarget()
    {
        if (this.targetVec.getX() != -1 || this.targetVec.getY() != -1 || this.targetVec.getZ() != -1)
        {
            TileEntity tileAtTarget = this.world.getTileEntity(this.targetVec);

            if (tileAtTarget != null && tileAtTarget instanceof ILaserNode)
            {
                return (ILaserNode) tileAtTarget;
            }

            return null;
        }

        return null;
    }

    public void setTarget(ILaserNode target)
    {
        if (target != null)
        {
            this.targetVec = target.getTile().getPos();
        }
        else
        {
            this.targetVec = new BlockPos(-1, -1, -1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.getBoolean("HasTarget"))
        {
            this.preLoadTarget = new BlockPos(nbt.getInteger("TargetX"), nbt.getInteger("TargetY"), nbt.getInteger("TargetZ"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        nbt.setBoolean("HasTarget", this.getTarget() != null);

        if (this.getTarget() != null)
        {
            nbt.setInteger("TargetX", this.getTarget().getTile().getPos().getX());
            nbt.setInteger("TargetY", this.getTarget().getTile().getPos().getY());
            nbt.setInteger("TargetZ", this.getTarget().getTile().getPos().getZ());
        }

        return nbt;
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
}
