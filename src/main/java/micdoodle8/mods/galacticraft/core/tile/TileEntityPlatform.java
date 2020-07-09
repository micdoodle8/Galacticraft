package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform.EnumCorner;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TileEntityPlatform extends TileEntity implements ITickableTileEntity
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.platform)
    public static TileEntityType<TileEntityPlatform> TYPE;

    private static final int MAXRANGE = 16;
    private EnumCorner corner = EnumCorner.NONE;
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
        super(TYPE);
    }

    public TileEntityPlatform(EnumCorner corner)
    {
        this();
        this.corner = corner;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.corner = EnumCorner.byId(nbt.getInt("co"));
        if (this.corner != EnumCorner.NONE)
        {
            this.firstTickCheck = true;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putInt("co", this.corner.getId());
        return nbt;
    }

    @Override
    public void tick()
    {
        if (this.firstTickCheck && !this.world.isRemote)
        {
            this.firstTickCheck = !this.checkIntact();
        }

        if (this.corner == EnumCorner.NONE && !this.world.isRemote)
        {
            final List<TileEntityPlatform> adjacentPlatforms = new LinkedList<>();
            final int thisX = this.getPos().getX();
            final int thisY = this.getPos().getY();
            final int thisZ = this.getPos().getZ();

            for (int x = -1; x < 1; x++)
            {
                for (int z = -1; z < 1; z++)
                {
                    BlockPos pos = new BlockPos(x + thisX, thisY, z + thisZ);
                    final TileEntity tile = this.world.isBlockLoaded(pos) ? this.world.getTileEntity(pos) : null;

                    if (tile instanceof TileEntityPlatform && !tile.isRemoved() && ((TileEntityPlatform) tile).corner == EnumCorner.NONE)
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
                    tile.setWhole(EnumCorner.byId(index));
                    index++;
                }
            }
        }
        else if (this.world.isRemote)
        {
            this.updateClient();
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void updateClient()
    {
        this.lightOn = false;
        if (this.colorTicks > 0)
        {
            if (--this.colorTicks == 0)
            {
                this.colorState = 0;
            }
        }

        BlockState b = this.world.getBlockState(this.getPos());
        if (b.getBlock() == GCBlocks.platform && b.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
        {
            //Scan area for player entities and light up
            if (this.detection == null)
            {
                this.detection = new AxisAlignedBB(this.getPos().getX() + 0.9D, this.getPos().getY() + 0.75D, this.getPos().getZ() + 0.9D, this.getPos().getX() + 1.1D, this.getPos().getY() + 1.85D, this.getPos().getZ() + 1.1D);
            }
            final List<Entity> list = this.world.getEntitiesWithinAABB(PlayerEntity.class, detection);

            if (list.size() > 0)
            {
                // Light up the platform
                this.lightOn = true;

                // If this player is within the box
                ClientPlayerEntity p = Minecraft.getInstance().player;
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
                                stats.startPlatformAscent(this, tep, this.pos.getY() - canDescend + (this.world.getDimension() instanceof IZeroGDimension ? 0.97D : (double) BlockPlatform.HEIGHT));
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
                                p.setMotion(p.getMotion().x, 0.0, p.getMotion().z);
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

    @OnlyIn(Dist.CLIENT)
    private void startMove(TileEntityPlatform te)
    {
        this.moving = true;
        this.lightA = this.getBlendedLight();
        this.lightB = te.getBlendedLight();
        this.deltaY = te.getPos().getY() - this.getPos().getY();
    }

    /**
     * @return 0 for no platform, range for good platform, -1 for blocked platform
     */
    private int checkNextPlatform(int dir)
    {
        final int thisX = this.getPos().getX();
        final int thisY = this.getPos().getY();
        final int thisZ = this.getPos().getZ();
        int maxY = thisY + MAXRANGE * dir;
        if (maxY > 255)
        {
            maxY = 255;
        }
        if (maxY < 0)
        {
            maxY = 0;
        }

        for (int y = thisY + dir; y != maxY; y += dir)
        {
            int c1 = this.checkCorner(new BlockPos(thisX, y, thisZ), BlockPlatform.EnumCorner.NW);
            if (c1 >= 2)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ), BlockPlatform.EnumCorner.NE) * 4;
            if (c1 >= 8)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX, y, thisZ + 1), BlockPlatform.EnumCorner.SW) * 16;
            if (c1 >= 32)
            {
                return c1 - 3;
            }
            c1 += this.checkCorner(new BlockPos(thisX + 1, y, thisZ + 1), BlockPlatform.EnumCorner.SE) * 64;
            if (c1 >= 128)
            {
                return c1 - 3;
            }
            // Good platform on all four corners
            if (c1 == 0)
            {
                continue;
            }
            if (this.motionObstructed(thisY + 1, y - thisY))
            {
                return -1;
            }
            return (y - thisY) * dir;
        }
        return 0;
    }

    /**
     * @return 0 for air, 1 for good platform, 2 for blocked platform, 3 for block
     */
    private int checkCorner(BlockPos blockPos, EnumCorner corner)
    {
        BlockState b = this.world.getBlockState(blockPos);
        if (b.getBlock() instanceof AirBlock)
        {
            return 0;
        }
        if (b.getBlock() == GCBlocks.platform && b.get(BlockPlatform.CORNER) == corner)
        {
            return (this.world.getBlockState(blockPos.up(1)).causesSuffocation(world, blockPos) || this.world.getBlockState(blockPos.up(2)).causesSuffocation(world, blockPos)) ? 2 : 1;
        }
        if (b.causesSuffocation(world, blockPos) || b.getShape(world, blockPos) == VoxelShapes.fullCube())
        {
            return 3;
        }
        return 0;
    }

    private void setWhole(EnumCorner corner)
    {
        this.corner = corner;
        this.world.setBlockState(this.getPos(), GCBlocks.platform.getDefaultState().with(BlockPlatform.CORNER, corner));
    }

    public void onDestroy(TileEntity callingBlock)
    {
        if (this.corner != EnumCorner.NONE)
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
            if (this.world.isBlockLoaded(pos) && this.world.getBlockState(pos).getBlock() == GCBlocks.platform)
            {
                final TileEntity tile = this.world.getTileEntity(pos);
                if (tile instanceof TileEntityPlatform)
                {
                    ((TileEntityPlatform) tile).setWhole(EnumCorner.NONE);
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
        case NONE:
            break;
        case NW:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x + 1, y, z + 1));
            break;
        case SW:
            positions.add(new BlockPos(x + 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x + 1, y, z - 1));
            break;
        case NE:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z + 1));
            positions.add(new BlockPos(x - 1, y, z + 1));
            break;
        case SE:
            positions.add(new BlockPos(x - 1, y, z));
            positions.add(new BlockPos(x, y, z - 1));
            positions.add(new BlockPos(x - 1, y, z - 1));
            break;
        }
    }

    private boolean checkIntact()
    {
        BlockState bs = this.world.getBlockState(this.pos);
        if (bs.getBlock() != GCBlocks.platform || bs.get(BlockPlatform.CORNER) != this.corner)
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
        case NONE:
            count = 3;
            break;
        case NW:
            count += checkState(new BlockPos(x + 1, y, z), EnumCorner.NE);
            count += checkState(new BlockPos(x, y, z + 1), EnumCorner.SW);
            count += checkState(new BlockPos(x + 1, y, z + 1), EnumCorner.SE);
            break;
        case SW:
            count += checkState(new BlockPos(x + 1, y, z), EnumCorner.SE);
            count += checkState(new BlockPos(x, y, z - 1), EnumCorner.NW);
            count += checkState(new BlockPos(x + 1, y, z - 1), EnumCorner.NE);
            break;
        case NE:
            count += checkState(new BlockPos(x - 1, y, z), EnumCorner.NW);
            count += checkState(new BlockPos(x, y, z + 1), EnumCorner.SE);
            count += checkState(new BlockPos(x - 1, y, z + 1), EnumCorner.SW);
            break;
        case SE:
            count += checkState(new BlockPos(x - 1, y, z), EnumCorner.SW);
            count += checkState(new BlockPos(x, y, z - 1), EnumCorner.NE);
            count += checkState(new BlockPos(x - 1, y, z - 1), EnumCorner.NW);
            break;
        }

        if (count >= 3)
        {
            return count == 3;
        }

        this.resetBlocks();
        return true;
    }

    private int checkState(BlockPos blockPos, EnumCorner corner)
    {
        if (!this.world.isBlockLoaded(blockPos))
        {
            return 4;
        }

        BlockState bs = this.world.getBlockState(blockPos);
        if (bs.getBlock() == GCBlocks.platform && bs.get(BlockPlatform.CORNER) == corner)
        {
            final TileEntity tile = this.world.getTileEntity(blockPos);
            if (tile instanceof TileEntityPlatform)
            {
                ((TileEntityPlatform) tile).corner = corner;
                ((TileEntityPlatform) tile).firstTickCheck = false;
                return 1;
            }
        }

        return 0;
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState)
//    {
//        return oldState.getBlock() != newState.getBlock();
//    }

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

    @OnlyIn(Dist.CLIENT)
    public float getYOffset(float partialTicks)
    {
        if (this.moving)
        {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            float playerY = (float) (p.lastTickPosY + (p.posY - p.lastTickPosY) * (double) partialTicks);
            return (playerY - this.pos.getY() - BlockPlatform.HEIGHT);
        }
        else
        {
            return 0.0F;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(this.pos.add(-1, -18, -1), this.pos.add(1, 18, 1));
        }
        return this.renderAABB;
    }

    @OnlyIn(Dist.CLIENT)
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

    @OnlyIn(Dist.CLIENT)
    public float getMeanLightX(float yOffset)
    {
        float a = (float) (this.lightA % 65536);
        float b = (float) (this.lightB % 65536);
        float f = yOffset / deltaY;
        return (1 - f) * a + f * b;
    }

    @OnlyIn(Dist.CLIENT)
    public float getMeanLightZ(float yOffset)
    {
        float a = (float) (this.lightA / 65536);
        float b = (float) (this.lightB / 65536);
        float f = yOffset / deltaY;
        return (1 - f) * a + f * b;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean motionObstructed(double y, double velocityY)
    {
        ClientPlayerEntity p = Minecraft.getInstance().player;
        int x = this.pos.getX() + 1;
        int z = this.pos.getZ() + 1;
        double size = 9 / 16D;
        double height = p.getHeight() + velocityY;
        double depth = velocityY < 0D ? 0.179D : 0D;
        AxisAlignedBB bb = new AxisAlignedBB(x - size, y - depth, z - size, x + size, y + height, z + size);
        BlockPlatform.ignoreCollisionTests = true;
        boolean obstructed = this.world.getCollisionShapes(p, bb) != VoxelShapes.empty();
        BlockPlatform.ignoreCollisionTests = false;
        return obstructed;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }
}
