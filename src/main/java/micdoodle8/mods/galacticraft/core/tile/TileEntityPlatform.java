package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform.EnumCorner;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileEntityPlatform extends TileEntity implements ITickable
{
    private static final int MAXRANGE = 16; 
    private int corner = 0;
    private AxisAlignedBB detection = null;
    private boolean noCollide;
    private boolean moving;
    private boolean lightOn = false;
    private int colorState = 0;   //0 = green  1 = red
    private int colorTicks = 0;
    private AxisAlignedBB renderAABB;
    private int lightA;
    private int lightB;
    private int deltaY;
    private boolean firstTickCheck;
    
    public TileEntityPlatform()
    {
    }

    public TileEntityPlatform(int meta)
    {
        this.corner = (meta > 4 || meta < 0) ? 0 : meta;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.corner = nbt.getInteger("co");
        if (this.corner != 0)
        {
            this.firstTickCheck = true;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("co", this.corner);
        return nbt;
    }

    @Override
    public void update()
    {
        if (this.firstTickCheck && !this.world.isRemote)
        {
            this.firstTickCheck = !this.checkIntact();
        }
            
        if (this.corner == 0 && !this.world.isRemote)
        {
            final List<TileEntityPlatform> adjacentPlatforms = new LinkedList<>();
            final int thisX = this.getPos().getX();
            final int thisY = this.getPos().getY();
            final int thisZ = this.getPos().getZ();

            for (int x = - 1; x < 1; x++)
            {
                for (int z = - 1; z < 1; z++)
                {
                    BlockPos pos = new BlockPos(x + thisX, thisY, z + thisZ);
                    final TileEntity tile = this.world.isBlockLoaded(pos, false) ? this.world.getTileEntity(pos) : null;

                    if (tile instanceof TileEntityPlatform && !tile.isInvalid() && ((TileEntityPlatform)tile).corner == 0)
                    {
                        final TileEntity tileUp = this.world.getTileEntity(pos.up());
                        final TileEntity tileDown = this.world.getTileEntity(pos.down());
                        if (!(tileUp instanceof TileEntityPlatform) && !(tileDown instanceof TileEntityPlatform))
                        {
                           adjacentPlatforms.add((TileEntityPlatform) tile);
                        }
                    }
                }
            }

            if (adjacentPlatforms.size() == 4)
            {
                int index = 1;
                for (final TileEntityPlatform tile : adjacentPlatforms)
                {
                    tile.setWhole(index++);;
                }
            }
        }
        else if (this.world.isRemote)
        {
            this.updateClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateClient()
    {
        this.lightOn  = false;
        if (this.colorTicks > 0)
        {
            if (--this.colorTicks == 0)
            {
                this.colorState = 0;
            }
        }

        IBlockState b = this.world.getBlockState(this.getPos());
        if (b.getBlock() == GCBlocks.platform && b.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
        {
            //Scan area for player entities and light up
            if (this.detection == null)
            {
                this.detection = new AxisAlignedBB(this.getPos().getX() + 0.9D, this.getPos().getY() + 0.75D, this.getPos().getZ() + 0.9D, this.getPos().getX() + 1.1D, this.getPos().getY() + 1.85D, this.getPos().getZ() + 1.1D);
            }
            final List<Entity> list = this.world.getEntitiesWithinAABB(EntityPlayer.class, detection);

            if (list.size() > 0)
            {
                // Light up the platform
                this.lightOn = true;

                // If this player is within the box
                EntityPlayerSP p = FMLClientHandler.instance().getClientPlayerEntity();
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(p);
                if (list.contains(p) && !stats.getPlatformControlled() && p.getRidingEntity() == null)
                {
                    if (p.movementInput.sneak)
                    {
                        int canDescend = this.checkNextPlatform(-1);
                        if (canDescend == -1)
                        {
                            this.colorState = 1;
                            this.colorTicks = 16;
                        }
                        else if (canDescend > 0)
                        {
                            TileEntity te = this.world.getTileEntity(this.pos.down(canDescend));
                            if (te instanceof TileEntityPlatform)
                            {
                                TileEntityPlatform tep = (TileEntityPlatform) te;
                                stats.startPlatformAscent(this, tep, this.pos.getY() - canDescend + (this.world.provider instanceof IZeroGDimension ? 0.97D : (double) BlockPlatform.HEIGHT));
                                this.startMove(tep);
                                tep.startMove(this);
                            }
                        }
                    }
                    else if (p.movementInput.jump)
                    {
                        int canAscend = this.checkNextPlatform(1);
                        if (canAscend == -1)
                        {
                            this.colorState = 1;
                            this.colorTicks = 16;
                        }
                        else if (canAscend > 0)
                        {
                            TileEntity te = this.world.getTileEntity(this.pos.up(canAscend));
                            if (te instanceof TileEntityPlatform)
                            {
                                p.motionY = 0D;
                                TileEntityPlatform tep = (TileEntityPlatform) te;
                                stats.startPlatformAscent(tep, this, this.pos.getY() + canAscend + BlockPlatform.HEIGHT + 0.01D);
                                this.startMove(tep);
                                tep.startMove(this);
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void startMove(TileEntityPlatform te)
    {
        this.moving = true;
        this.lightA = this.getBlendedLight();
        this.lightB = te.getBlendedLight();
        this.deltaY = te.getPos().getY() - this.getPos().getY();
    }

    /**
     * @return  0 for no platform, range for good platform, -1 for blocked platform
     */
    private int checkNextPlatform(int dir)
    {
        final int thisX = this.getPos().getX();
        final int thisY = this.getPos().getY();
        final int thisZ = this.getPos().getZ();
        int maxY = thisY + MAXRANGE * dir;
        if (maxY > 255) maxY = 255;
        if (maxY < 0) maxY = 0;

        for (int y = thisY + dir; y != maxY; y+= dir)
        {
            int c1 = this.checkCorner(new BlockPos(thisX, y, thisZ), BlockPlatform.EnumCorner.NW);
            if (c1 >= 2) return c1 - 3;
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ), BlockPlatform.EnumCorner.NE) * 4;
            if (c1 >= 8) return c1 - 3;
            c1 += this.checkCorner(new BlockPos(thisX, y, thisZ + 1), BlockPlatform.EnumCorner.SW) * 16;
            if (c1 >= 32) return c1 - 3;
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ + 1), BlockPlatform.EnumCorner.SE) * 64;
            if (c1 >= 128) return c1 - 3;
            // Good platform on all four corners
            if (c1 == 0) continue;
            if (this.motionObstructed(thisY + 1, y - thisY))
            {
                return -1;
            }
            return (y - thisY) * dir;
        }
        return 0;
    }

    /**
     * @return  0 for air, 1 for good platform, 2 for blocked platform, 3 for block
     */
    private int checkCorner(BlockPos blockPos, EnumCorner corner)
    {
        IBlockState b = this.world.getBlockState(blockPos);
        if (b.getBlock() instanceof BlockAir)
        {
            return 0;
        }
        if (b.getBlock() == GCBlocks.platform && b.getValue(BlockPlatform.CORNER) == corner)
        {
            return (this.world.getBlockState(blockPos.up(1)).causesSuffocation() || this.world.getBlockState(blockPos.up(2)).causesSuffocation()) ? 2 : 1;
        }
        if (b.causesSuffocation() || b.getBlock().isFullBlock(b))
        {
            return 3;
        }
        return 0;
    }

    private void setWhole(int index)
    {
        this.corner = index;
        this.world.setBlockState(this.getPos(), GCBlocks.platform.getDefaultState().withProperty(BlockPlatform.CORNER, EnumCorner.byMetadata(index)));
    }

    public void onDestroy(TileEntity callingBlock)
    {
        if (this.corner > 0)
        {
            resetBlocks();
        }
        this.world.destroyBlock(this.pos, true);
    }
    
    private void resetBlocks()
    {
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(this.pos, positions);

        for (BlockPos pos : positions)
        {
            if (this.world.isBlockLoaded(pos, false) && this.world.getBlockState(pos).getBlock() == GCBlocks.platform)
            {
                final TileEntity tile = this.world.getTileEntity(pos);
                if (tile instanceof TileEntityPlatform)
                {
                    ((TileEntityPlatform) tile).setWhole(0);
                }
            }
        }
    }

    public void getPositions(BlockPos blockPos, List<BlockPos> positions)
    {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        switch (this.corner)
        {
        case 0:
            break;
        case 1:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x + 1, y, z + 1));
            break;
        case 2:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x + 1, y, z - 1));
            break;
        case 3:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x - 1, y, z + 1));
            break;
        case 4:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x - 1, y, z - 1));
            break;
        }
    }

    private boolean checkIntact()
    {
        IBlockState bs = this.world.getBlockState(this.pos); 
        if (bs.getBlock() != GCBlocks.platform || ((BlockPlatform.EnumCorner)bs.getValue(BlockPlatform.CORNER)).getMeta() != this.corner)
        {
            this.resetBlocks();
            return false;
        }
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        int count = 0;
        switch (this.corner)
        {
        case 0:
            count = 3;
            break;
        case 1:
            count += checkState(new BlockPos(x + 1, y, z), 3);
            count += checkState(new BlockPos(x, y, z + 1), 2);
            count += checkState(new BlockPos(x + 1, y, z + 1), 4);
            break;
        case 2:
            count += checkState(new BlockPos(x + 1, y, z), 4);
            count += checkState(new BlockPos(x, y, z - 1), 1);
            count += checkState(new BlockPos(x + 1, y, z - 1), 3);
            break;
        case 3:
            count += checkState(new BlockPos(x - 1, y, z), 1);
            count += checkState(new BlockPos(x, y, z + 1), 4);
            count += checkState(new BlockPos(x - 1, y, z + 1), 2);
            break;
        case 4:
            count += checkState(new BlockPos(x - 1, y, z), 2);
            count += checkState(new BlockPos(x, y, z - 1), 3);
            count += checkState(new BlockPos(x - 1, y, z - 1), 1);
            break;
        }
        
        if (count >= 3)
        {
            return count == 3;
        }
        
        this.resetBlocks();
        return true;
    }

    private int checkState(BlockPos blockPos, int meta)
    {
        if (!this.world.isBlockLoaded(blockPos, false))
            return 4;

        IBlockState bs = this.world.getBlockState(blockPos); 
        if (bs.getBlock() == GCBlocks.platform && ((BlockPlatform.EnumCorner)bs.getValue(BlockPlatform.CORNER)).getMeta() == meta)
        {
            final TileEntity tile = this.world.getTileEntity(blockPos);
            if (tile instanceof TileEntityPlatform)
            {
                ((TileEntityPlatform) tile).corner = meta;
                ((TileEntityPlatform) tile).firstTickCheck = false;
                return 1;
            }
        }

        return 0;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    public boolean noCollide()
    {
        return this.noCollide;
    }

    public void markNoCollide(int y, boolean b)
    {
        TileEntity te;
        final int x = this.getPos().getX();
        final int z = this.getPos().getZ();
        y += this.getPos().getY();
        te = this.world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.world.getTileEntity(new BlockPos(x + 1, y, z));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.world.getTileEntity(new BlockPos(x, y, z + 1));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
        te = this.world.getTileEntity(new BlockPos(x + 1, y, z + 1));
        if (te instanceof TileEntityPlatform)
        {
            ((TileEntityPlatform) te).noCollide = b;
        }
    }

    public boolean lightEnabled()
    {
        return this.lightOn;
    }

    public int lightColor()
    {
        return this.colorState;
    }
    
    public boolean isMoving()
    {
        return this.moving;
    }

    public void stopMoving()
    {
        this.moving = false;
    }

    @SideOnly(Side.CLIENT)
    public float getYOffset(float partialTicks)
    {
        if (this.moving)
        {
            EntityPlayerSP p = FMLClientHandler.instance().getClientPlayerEntity();
            float playerY = (float)(p.lastTickPosY + (p.posY - p.lastTickPosY) * (double)partialTicks);
            return (playerY - this.pos.getY() - BlockPlatform.HEIGHT);
        }
        else
            return 0.0F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(this.pos.add(-1, -18, -1), this.pos.add(1, 18, 1));
        }
        return this.renderAABB;
    }

    @SideOnly(Side.CLIENT)
    public int getBlendedLight()
    {
        int j = 0, k = 0;
        int light = this.world.getCombinedLight(this.getPos().up(), 0);
        j += light % 65536;
        k += light / 65536;
        light = this.world.getCombinedLight(this.getPos().add(1, 1, 0), 0);
        j += light % 65536;
        k += light / 65536;
        light = this.world.getCombinedLight(this.getPos().add(0, 1, 1), 0);
        j += light % 65536;
        k += light / 65536;
        light = this.world.getCombinedLight(this.getPos().add(1, 1, 1), 0);
        j += light % 65536;
        k += light / 65536;
        return j / 4 + k * 16384; 
    }

    @SideOnly(Side.CLIENT)
    public float getMeanLightX(float yOffset)
    {
        float a = (float)(this.lightA % 65536);
        float b = (float)(this.lightB % 65536);
        float f = yOffset / deltaY; 
        return (1 - f) * a + f * b;  
    }

    @SideOnly(Side.CLIENT)
    public float getMeanLightZ(float yOffset)
    {
        float a = (float)(this.lightA / 65536);
        float b = (float)(this.lightB / 65536);
        float f = yOffset / deltaY;
        return (1 - f) * a + f * b;  
    }

    @SideOnly(Side.CLIENT)
    public boolean motionObstructed(double y, double velocityY)
    {
        EntityPlayerSP p = FMLClientHandler.instance().getClientPlayerEntity();
        int x = this.pos.getX() + 1;
        int z = this.pos.getZ() + 1;
        double size = 9/16D;
        double height = p.height + velocityY;
        double depth = velocityY < 0D ? 0.179D : 0D;
        AxisAlignedBB bb = new AxisAlignedBB(x - size, y - depth, z - size, x + size, y + height, z + size);
        BlockPlatform.ignoreCollisionTests = true;
        boolean obstructed = !this.world.getCollisionBoxes(p, bb).isEmpty(); 
        BlockPlatform.ignoreCollisionTests = false;
        return obstructed;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}
