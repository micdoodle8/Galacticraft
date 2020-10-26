//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.item.IPaintable;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
//import micdoodle8.mods.galacticraft.core.util.*;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockRenderType;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.*;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.LightType;
//import net.minecraft.world.World;
//import net.minecraftforge.common.property.ExtendedBlockState;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.property.IUnlistedProperty;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockPanelLighting extends BlockAdvancedTile implements ISortable, IShiftDescription, IPaintable
//{
//    public static final EnumProperty<PanelType> TYPE = EnumProperty.create("type", PanelType.class);
//    public static final PropertyObject<BlockState> BASE_STATE = new PropertyObject<>("held_state", BlockState.class);
//
//    public static int color = 0xf0f0e0;
//
//    public enum PanelType implements IStringSerializable
//    {
//        SQUARE("square", 11),
//        SPOTS("spots", 7),
//        LINEAR("linear", 9),
//        SF("sf", 2),
//        SFDIAG("sfdiag", 2);
//        //IF ADDING TO THIS ENUM, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS!!!!!!
//
//        private final String name;
//        private final int light;
//
//        private PanelType (String name, int lightValue)
//        {
//            this.name = name;
//            this.light = lightValue;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//
//        public int getLight()
//        {
//            return this.light;
//        }
//    }
//    public static final int PANELTYPES_LENGTH = PanelType.values().length;
//    private static BlockState[] superState = new BlockState[PANELTYPES_LENGTH]; // - only used clientSide
//
//    public BlockPanelLighting(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return new ExtendedBlockState(this, new IProperty[] { TYPE }, new IUnlistedProperty[] { BASE_STATE });
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (int i = 0; i < PANELTYPES_LENGTH; i++)
//        {
//            list.add(new ItemStack(this, 1, i));
//        }
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return this.getMetaFromState(state);
//    }
//
//    @Override
//    public List<ItemStack> getDrops(IBlockReader world, BlockPos pos, BlockState state, int fortune)
//    {
//        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
//        result.add(new ItemStack(this.getItemDropped(state, null, fortune), 1, this.getMetaFromState(state)));
//        return result;
//    }
//
//    @Override
//    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int damage, LivingEntity placer)
//    {
//        return this.getStateFromMeta(damage);
//    }
//
//    @Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
//    {
//        int damage = stack.getDamage();
//        if (damage >= PANELTYPES_LENGTH) damage = 0;
//        TileEntity tile = worldIn.getTileEntity(pos);
//        if (tile instanceof TileEntityPanelLight && placer instanceof PlayerEntity)
//        {
//            ((TileEntityPanelLight) tile).initialise(damage, Direction.getDirectionFromEntityLiving(pos, placer), (PlayerEntity) placer, worldIn.isRemote, ((BlockPanelLighting)state.getBlock()).superState[damage]);
//        }
//    }
//
//    @Override
//    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        TileEntity tile = world.getTileEntity(pos);
//        if (!(tile instanceof TileEntityPanelLight))
//        {
//            return false;
//        }
//        BlockState bs = world.getBlockState(pos);
//        if (!(bs.getBlock() instanceof BlockPanelLighting))
//        {
//            return false;
//        }
//        PanelType type = (PanelType) bs.getValue(BlockPanelLighting.TYPE);
//        TileEntityPanelLight tilegood = (TileEntityPanelLight)tile;
//        int metadata = tilegood.meta;
//        if (metadata < 8 && (type == PanelType.LINEAR || type == PanelType.SF)  || metadata < 24 && type == PanelType.SFDIAG)
//        {
//            tilegood.meta += 8;
//            return true;
//        }
//
//        int metaDir = ((metadata & 7) + 1) % 6;
//        //DOWN->UP->NORTH->*EAST*->*SOUTH*->WEST
//        //0->1 1->2 2->5 3->4 4->0 5->3
//        if (metaDir == 3) //after north
//        {
//            metaDir = 5;
//        }
//        else if (metaDir == 0)
//        {
//            metaDir = 3;
//        }
//        else if (metaDir == 5)
//        {
//            metaDir = 0;
//        }
//
//        tilegood.meta = metaDir;
//        return true;
//    }
//
//    @Override
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityPanelLight();
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate("tile.panel_lighting.description");
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.DECORATION;
//    }
//
//    @Override
//    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer)
//    {
//        return true;
//    }
//
//    @Override
//    public BlockRenderType getRenderType(BlockState state)
//    {
//        return BlockRenderType.MODEL;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        if (meta >= PANELTYPES_LENGTH) meta = 0;
//        return this.getDefaultState().with(TYPE, PanelType.values()[meta]);
//    }
//
//    @Override
//    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        TileEntity tile = world.getTileEntity(pos);
//        if (tile instanceof TileEntityPanelLight)
//        {
//            state = ((IExtendedBlockState) state).with(BASE_STATE, ((TileEntityPanelLight)tile).getBaseBlock());
//        }
//
//        return state;
//    }
//
//    @Override
//    public int getLightValue(BlockState bs, IBlockReader world, BlockPos pos)
//    {
//        if (!(bs.getBlock() instanceof BlockPanelLighting))
//        {
//            return 0;
//        }
//        if (world instanceof World && RedstoneUtil.isBlockReceivingRedstone((World) world, pos))
//        {
//            return 0;
//        }
//        return ((PanelType) bs.getValue(BlockPanelLighting.TYPE)).getLight();
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        worldIn.checkLightFor(LightType.BLOCK, pos);
//    }
//
//    @SideOnly(value=LogicalSide.CLIENT)
//    public static void updateClient(int type, BlockState state)
//    {
//        if (type >= PANELTYPES_LENGTH) type = 0;
//        superState[type] = state;
//    }
//
//    @SideOnly(value=LogicalSide.CLIENT)
//    public static void updateClient(List<Object> data)
//    {
//        BlockState state;
//        for (int i = 0; i < PANELTYPES_LENGTH; i++)
//        {
//            state = TileEntityPanelLight.readBlockState((String) data.get(i + i + 1), (Integer) data.get(i + i + 2));
//            if (state.getBlock() == Blocks.AIR)
//            {
//                state = null;
//            }
//            superState[i] = state;
//        }
//        color = (Integer) data.get(2 * PANELTYPES_LENGTH + 1);
//    }
//
//    public static void getNetworkedData(Object[] result, BlockState[] panel_lighting)
//    {
//        //IF CHANGING THIS, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS
//        Block block;
//        BlockState bs;
//        for (int i = 0; i < PANELTYPES_LENGTH; i++)
//        {
//            bs = panel_lighting[i];
//            if (bs == null) bs = Blocks.AIR.getDefaultState();
//            block = bs.getBlock();
//            result[i + i + 1] = ((ResourceLocation)Block.REGISTRY.getNameForObject(block)).toString();
//            result[i + i + 2] = block.getMetaFromState(bs);
//        }
//    }
//
//    @Override
//    public int setColor(int color, PlayerEntity p, LogicalSide LogicalSide)
//    {
//        if (LogicalSide == LogicalSide.CLIENT)
//        {
//            BlockPanelLighting.color = ColorUtil.lighten(ColorUtil.lightenFully(color, 255), 0.1F);
//        }
//        else
//        {
//            GCPlayerStats stats = GCPlayerStats.get(p);
//            stats.setPanelLightingColor(color);
//        }
//        return 1;
//    }
//}
