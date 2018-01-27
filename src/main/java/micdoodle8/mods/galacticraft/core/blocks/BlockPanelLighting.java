package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.item.IPaintable;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PropertyObject;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPanelLighting extends BlockAdvancedTile implements ISortableBlock, IShiftDescription, IPaintable
{
    public static final PropertyEnum<PanelType> TYPE = PropertyEnum.create("type", PanelType.class);
    public static final PropertyObject<IBlockState> BASE_STATE = new PropertyObject<>("held_state", IBlockState.class);
    
    public static int color = 0xf0f0e0;
    
    public enum PanelType implements IStringSerializable
    {
        SQUARE("square", 11),
        SPOTS("spots", 7),
        LINEAR("linear", 9),
        SF("sf", 2),
        SFDIAG("sfdiag", 2);
        //IF ADDING TO THIS ENUM, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS!!!!!!

        private final String name;
        private final int light;

        private PanelType (String name, int lightValue)
        {
            this.name = name;
            this.light = lightValue;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        public int getLight()
        {
            return this.light;
        }
    }
    public static final int PANELTYPES_LENGTH = PanelType.values().length;
    private static IBlockState[] superState = new IBlockState[PANELTYPES_LENGTH]; // - only used clientSide
    
    public BlockPanelLighting(String assetName)
    {
        super(Material.iron);
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState());
    }

    @Override
    public BlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { TYPE }, new IUnlistedProperty[] { BASE_STATE });
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (int i = 0; i < PANELTYPES_LENGTH; i++)
        {
            list.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
        result.add(new ItemStack(this.getItemDropped(state, null, fortune), 1, this.getMetaFromState(state)));
        return result;
    }
    
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int damage, EntityLivingBase placer)
    {
        return this.getStateFromMeta(damage);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int damage = stack.getItemDamage();
        if (damage >= PANELTYPES_LENGTH) damage = 0;
        TileEntity tile = worldIn.getTileEntity(pos); 
        if (tile instanceof TileEntityPanelLight && placer instanceof EntityPlayer)
        {
            ((TileEntityPanelLight) tile).initialise(damage, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer), (EntityPlayer) placer, worldIn.isRemote, ((BlockPanelLighting)state.getBlock()).superState[damage]);
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos); 
        if (!(tile instanceof TileEntityPanelLight))
        {
            return false;
        }
        IBlockState bs = world.getBlockState(pos);
        if (!(bs.getBlock() instanceof BlockPanelLighting))
        {
            return false;
        }
        PanelType type = (PanelType) bs.getValue(BlockPanelLighting.TYPE);
        TileEntityPanelLight tilegood = (TileEntityPanelLight)tile;
        int metadata = tilegood.meta;
        if (metadata < 8 && (type == PanelType.LINEAR || type == PanelType.SF)  || metadata < 24 && type == PanelType.SFDIAG)
        {
            tilegood.meta += 8;
            return true;
        }

        int metaDir = ((metadata & 7) + 1) % 6;
        //DOWN->UP->NORTH->*EAST*->*SOUTH*->WEST
        //0->1 1->2 2->5 3->4 4->0 5->3 
        if (metaDir == 3) //after north
        {
            metaDir = 5;
        }
        else if (metaDir == 0)
        {
            metaDir = 3;
        }
        else if (metaDir == 5)
        {
            metaDir = 0;
        }
            
        tilegood.meta = metaDir;
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityPanelLight();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate("tile.panel_lighting.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.DECORATION;
    }

    @Override
    public boolean canRenderInLayer(EnumWorldBlockLayer layer)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        if (meta >= PANELTYPES_LENGTH) meta = 0;
        return this.getDefaultState().withProperty(TYPE, PanelType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((PanelType) state.getValue(BlockPanelLighting.TYPE)).ordinal();
    }
  
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos); 
        if (tile instanceof TileEntityPanelLight)
        {
            state = ((IExtendedBlockState) state).withProperty(BASE_STATE, ((TileEntityPanelLight)tile).getBaseBlock());
        }
        
        return state;
    }
    
    @Override
    public int getLightValue(IBlockAccess world, BlockPos pos)
    {
        IBlockState bs = world.getBlockState(pos);
        if (!(bs.getBlock() instanceof BlockPanelLighting))
        {
            return 0;
        }
        if (world instanceof World && RedstoneUtil.isBlockReceivingRedstone((World) world, pos))
        {
            return 0;
        }
        return ((PanelType) bs.getValue(BlockPanelLighting.TYPE)).getLight();
    }
    
    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
    }

    @SideOnly(value=Side.CLIENT)
    public static void updateClient(int type, IBlockState state)
    {
        if (type >= PANELTYPES_LENGTH) type = 0;
        superState[type] = state;
    }

    @SideOnly(value=Side.CLIENT)
    public static void updateClient(List<Object> data)
    {
        IBlockState state;
        for (int i = 0; i < PANELTYPES_LENGTH; i++)
        {
            state = TileEntityPanelLight.readBlockState((String) data.get(i + i + 1), (Integer) data.get(i + i + 2));
            if (state.getBlock() == Blocks.air)
            {
                state = null;
            }
            superState[i] = state;
        }
        color = (Integer) data.get(2 * PANELTYPES_LENGTH + 1);
    }

    public static void getNetworkedData(Object[] result, IBlockState[] panel_lighting)
    {
        //IF CHANGING THIS, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS
        Block block;
        IBlockState bs;
        for (int i = 0; i < PANELTYPES_LENGTH; i++)
        {
            bs = panel_lighting[i];
            if (bs == null) bs = Blocks.air.getDefaultState();
            block = bs.getBlock();
            result[i + i + 1] = ((ResourceLocation)Block.blockRegistry.getNameForObject(block)).toString();
            result[i + i + 2] = block.getMetaFromState(bs);
        }
    }

    @Override
    public int setColor(int color, EntityPlayer p, Side side)
    {
        if (side == Side.CLIENT)
        {
            BlockPanelLighting.color = ColorUtil.lighten(ColorUtil.lightenFully(color, 255), 0.1F);
        }
        else
        {
            GCPlayerStats stats = GCPlayerStats.get(p);
            stats.setPanelLightingColor(color);
        }
        return 1;
    }
}
