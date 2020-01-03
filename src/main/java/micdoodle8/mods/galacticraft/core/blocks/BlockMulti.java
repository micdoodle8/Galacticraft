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
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BlockMulti extends BlockAdvanced implements IPartialSealableBlock, ITileEntityProvider
{
    public static final PropertyEnum<EnumBlockMultiType> MULTI_TYPE = PropertyEnum.create("type", EnumBlockMultiType.class);
    public static final PropertyInteger RENDER_TYPE = PropertyInteger.create("rendertype", 0, 7);

    protected static final AxisAlignedBB AABB_PAD = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
    protected static final AxisAlignedBB AABB_SOLAR = new AxisAlignedBB(0.0F, 0.2F, 0.0F, 1.0F, 0.8F, 1.0F);
    protected static final AxisAlignedBB AABB_SOLAR_POLE = new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.7F, 1.0F, 0.7F);
    protected static final AxisAlignedBB AABB_TURRET = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

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
        DISH_LARGE(8, "dish_large"),
        LASER_TURRET(9, "laser_turret");

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

        private final static EnumBlockMultiType[] values = values();
        public static EnumBlockMultiType byMetadata(int meta)
        {
            return values[meta % values.length];
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
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
        this.setResistance(1000000000000000.0F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch ((EnumBlockMultiType)state.getValue(MULTI_TYPE))
        {
        case SOLAR_PANEL_0:
        case SOLAR_PANEL_1:
            boolean midPole = source.getBlockState(pos.up()).getBlock() == this;
            boolean topPole = source.getBlockState(pos.down()).getBlock() == this;
            if (topPole || midPole)
                return midPole ? AABB_SOLAR_POLE : AABB_SOLAR;
            else
                return AABB_SOLAR; 
        case ROCKET_PAD:
        case BUGGY_FUEL_PAD:
            return AABB_PAD;
        case LASER_TURRET:
            return AABB_TURRET;
        default:
            return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        int meta = getMetaFromState(worldIn.getBlockState(pos));
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//        }
//        else
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//        }
//    }

//    @SuppressWarnings("rawtypes")
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
//    {
//        int meta = getMetaFromState(state);
//
//        if (meta == 2 || meta == 6)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        else if (meta == 0 || meta == 4)
//        {
//            this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, worldIn.getBlockState(pos.up()).getBlock() == this ? 1.0F : 0.6F, 0.7F);
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//        /*else if (meta == 7)
//        {
//            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.38F, 1.0F);
//            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
//        }*/
//        else
//        {
//            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//        }
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }

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
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return worldIn.getBlockState(mainBlockPosition).getBlock().getBlockHardness(blockState, worldIn, pos);
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
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityMulti tileEntity = (TileEntityMulti) worldIn.getTileEntity(pos);
        if (tileEntity == null)
        {
            return false;
        }
        return tileEntity.onBlockActivated(worldIn, pos, playerIn);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityMulti tileEntity = (TileEntityMulti) world.getTileEntity(pos);
        return tileEntity.onBlockWrenched(world, pos, entityPlayer, hand, side, hitX, hitY, hitZ);
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int meta)
    {
        return null;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                IBlockState mainBlockState = world.getBlockState(mainBlockPosition);

                if (Blocks.AIR != mainBlockState.getBlock())
                {
                    return mainBlockState.getBlock().getPickBlock(mainBlockState, target, world, mainBlockPosition, player);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

	        if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                IBlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().getBedDirection(mainState, world, mainBlockPosition);
            }
        }

        return EnumFacing.UP; // TODO
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                IBlockState mainState = world.getBlockState(mainBlockPosition);
                return mainState.getBlock().isBed(state, world, mainBlockPosition, player);
            }
        }

        return super.isBed(state, world, pos, player);
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
    public BlockPos getBedSpawnPosition(IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player)
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
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid();
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager)
    {
        TileEntity tileEntity = worldObj.getTileEntity(target.getBlockPos());

        if (tileEntity instanceof TileEntityMulti)
        {
            BlockPos mainBlockPosition = ((TileEntityMulti) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(target.getBlockPos()))
            {
                manager.addBlockHitEffects(mainBlockPosition, target);
            }
        }

        return super.addHitEffects(state, worldObj, target, manager);
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
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, MULTI_TYPE, RENDER_TYPE);
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
                    player.sendMessage(new TextComponentString(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                    if (!player.capabilities.isCreativeMode)
                    {
                        final ItemStack nasaWorkbench = new ItemStack(callingBlock, 1, 0);
                        final EntityItem entityitem = player.dropItem(nasaWorkbench, false);
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
