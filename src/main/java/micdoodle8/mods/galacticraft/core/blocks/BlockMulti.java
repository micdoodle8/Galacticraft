package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BlockMulti extends BlockAdvanced implements IPartialSealableBlock, ITileEntityProvider
{
    public static final PropertyEnum<EnumBlockMultiType> MULTI_TYPE = PropertyEnum.create("type", EnumBlockMultiType.class);
    public static final PropertyInteger RENDER_TYPE = PropertyInteger.create("renderType", 0, 7);

    public enum EnumBlockMultiType implements IStringSerializable
    {
        SOLAR_PANEL_0(0, "solar"),
        SPACE_STATION_BASE(1, "ss_base"),
        ROCKET_PAD(2, "rocket_pad"),
        NASA_WORKBENCH(3, "nasa_workbench"),
        SOLAR_PANEL_1(4, "solar_panel"),
        CRYO_CHAMBER(5, "cryo_chamber"),
        BUGGY_FUEL_PAD(6, "buggy_pad"),
        MINER_BASE(7, "miner_base"),  //UNUSED
        DISH_LARGE(8, "dish_large");

        private final int meta;
        private final String name;

        EnumBlockMultiType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlockMultiType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMulti(String assetName)
    {
        super(GCBlocks.machine);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
        this.setResistance(1000000000000000.0F);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        int meta = getMetaFromState(worldIn.getBlockState(pos));

        if (meta == 2 || meta == 6)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
        }
        else if (meta == 0 || meta == 4)
        {
            boolean midPole = worldIn.getBlockState(pos.up()).getBlock() == this;
            boolean topPole = worldIn.getBlockState(pos.down()).getBlock() == this;
            if (topPole || midPole)
                this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, midPole ? 1.0F : 0.6F, 0.7F);
            else
                this.setBlockBounds(0.0F, 0.2F, 0.0F, 1.0F, 0.8F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        int meta = getMetaFromState(state);

        if (meta == 2 || meta == 6)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        else if (meta == 0 || meta == 4)
        {
            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        /*else if (meta == 7)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
        }*/
        else
        {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public boolean canDropFromExplosion(Explosion par1Explosion)
    {
        return false;
    }

    public void makeFakeBlock(World worldObj, BlockPos pos, BlockPos mainBlock, int meta)
    {
        worldObj.setBlockState(pos, GCBlocks.fakeBlock.getStateFromMeta(meta), meta == 5 ? 3 : 0);
        worldObj.setTileEntity(pos, new TileEntityMulti(mainBlock));
    }

    public void makeFakeBlock(World worldObj, Collection<BlockPos> posList, BlockPos mainBlock, EnumBlockMultiType type)
    {
        for (BlockPos pos : posList)
        {
            worldObj.setBlockState(pos, this.getDefaultState().withProperty(MULTI_TYPE, type), type == EnumBlockMultiType.CRYO_CHAMBER ? 3 : 0);
            worldObj.setTileEntity(pos, new TileEntityMulti(mainBlock));
        }
    }

    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getBlockHardness(worldIn, pos);
            }
        }

        return this.blockHardness;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        int metadata = getMetaFromState(worldIn.getBlockState(pos));

        //Landing pad and refueling pad
        if (metadata == 2 || metadata == 6)
        {
            return direction == EnumFacing.DOWN;
        }

        //Basic solar panel fixed top
        if (metadata == 4)
        {
            return direction == EnumFacing.UP;
        }

        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityMulti)
        {
            ((TileEntityMulti) tileEntity).onBlockRemoval();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityMulti tileEntity = (TileEntityMulti) worldIn.getTileEntity(pos);
        if (tileEntity == null)
        {
            return false;
        }
        return tileEntity.onBlockActivated(worldIn, pos, playerIn);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityMulti tileEntity = (TileEntityMulti) world.getTileEntity(pos);
        return tileEntity.onBlockWrenched(world, pos, entityPlayer, side, hitX, hitY, hitZ);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return null;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                Block mainBlockID = world.getBlockState(mainBlockPosition).getBlock();

                if (Blocks.air != mainBlockID)
                {
                    return mainBlockID.getPickBlock(target, world, mainBlockPosition, player);
                }
            }
        }

        return null;
    }

    @Override
    public EnumFacing getBedDirection(IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

	        if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return world.getBlockState(mainBlockPosition).getBlock().getBedDirection(world, mainBlockPosition);
            }
        }

        return EnumFacing.UP; // TODO
    }

    @Override
    public boolean isBed(IBlockAccess world, BlockPos pos, Entity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return world.getBlockState(mainBlockPosition).getBlock().isBed(world, mainBlockPosition, player);
            }
        }

        return super.isBed(world, pos, player);
    }

    @Override
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

        if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
        {
            world.getBlockState(mainBlockPosition).getBlock().setBedOccupied(world, mainBlockPosition, player, occupied);
        }
        else
        {
            super.setBedOccupied(world, pos, player, occupied);
        }
    }

    @Override
    public BlockPos getBedSpawnPosition(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        if (!(world instanceof World))
        {
            return null;
        }
        int tries = 3;
        World worldIn = (World) world;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;
        IBlockState cryoChamber = worldIn.getBlockState(mainBlockPosition);
        EnumFacing enumfacing = EnumFacing.NORTH;
        if (GalacticraftCore.isPlanetsLoaded && cryoChamber.getBlock() == MarsBlocks.machine)
        {
            enumfacing = (EnumFacing)cryoChamber.getValue(BlockMachineMars.FACING);
        }
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - enumfacing.getFrontOffsetX() * l - 1;
            int j1 = k - enumfacing.getFrontOffsetZ() * l - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (hasRoomForPlayer(worldIn, blockpos))
                    {
                        if (tries <= 0)
                        {
                            return blockpos;
                        }

                        --tries;
                    }
                }
            }
        }

        return null;
    }

    private static boolean hasRoomForPlayer(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !worldIn.getBlockState(pos).getBlock().getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
    {
        TileEntity tileEntity = worldObj.getTileEntity(target.getBlockPos());

        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(target.getBlockPos()))
            {
                effectRenderer.addBlockHitEffects(mainBlockPosition, target);
            }
        }

        return super.addHitEffects(worldObj, target, effectRenderer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.EffectRenderer effectRenderer)
    {
        return super.addDestroyEffects(world, pos, effectRenderer);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(MULTI_TYPE, EnumBlockMultiType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(MULTI_TYPE).getMeta();
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, MULTI_TYPE, RENDER_TYPE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        EnumBlockMultiType type = state.getValue(MULTI_TYPE);
        int renderType = 0;

        switch (type)
        {
        case CRYO_CHAMBER:
            IBlockState stateAbove = worldIn.getBlockState(pos.up());
            TileEntityMulti tile = (TileEntityMulti) worldIn.getTileEntity(pos);
            if (stateAbove.getBlock() == this && (stateAbove.getValue(MULTI_TYPE)) == EnumBlockMultiType.CRYO_CHAMBER)
            {
                renderType = 0;
            }
            else
            {
                renderType = 4;
            }
            if (tile != null && tile.mainBlockPosition != null && GalacticraftCore.isPlanetsLoaded)
            {
                IBlockState stateMain = worldIn.getBlockState(tile.mainBlockPosition);
                if (stateMain.getBlock() == MarsBlocks.machine && stateMain.getValue(BlockMachineMars.TYPE) == BlockMachineMars.EnumMachineType.CRYOGENIC_CHAMBER)
                {
                    EnumFacing dir = stateMain.getValue(BlockMachineMars.FACING);
                    renderType += dir.getHorizontalIndex();
                }
            }
            break;
        default:
            break;
        }

        return state.withProperty(RENDER_TYPE, renderType);
    }
    
    public static void onPlacement(World worldIn, BlockPos pos,EntityLivingBase placer, Block callingBlock)
    {
        final TileEntity tile = worldIn.getTileEntity(pos);

        if (tile instanceof IMultiBlock)
        {
            boolean validSpot = true;
            List<BlockPos> toCheck = new LinkedList<>();
            ((IMultiBlock) tile).getPositions(pos, toCheck);
            for (BlockPos toTest : toCheck)
            {
                IBlockState blockAt = worldIn.getBlockState(toTest);
                if (!blockAt.getBlock().isReplaceable(worldIn, toTest))
                {
                    validSpot = false;
                    break;
                }
            }

            if (!validSpot)
            {
                worldIn.setBlockToAir(pos);

                if (!worldIn.isRemote && placer instanceof EntityPlayerMP)
                {
                    EntityPlayerMP player = (EntityPlayerMP) placer;
                    player.addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                    if (!player.capabilities.isCreativeMode)
                    {
                        final ItemStack nasaWorkbench = new ItemStack(callingBlock, 1, 0);
                        final EntityItem entityitem = player.dropPlayerItemWithRandomChoice(nasaWorkbench, false);
                        entityitem.setPickupDelay(0);
                        entityitem.setOwner(player.getName());
                    }
                }

                return;
            }
            
            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }
    }
}
