package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PropertyObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPanelLighting extends BlockAdvancedTile implements ISortableBlock, IShiftDescription
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ROT = PropertyBool.create("rot");
    public static final PropertyObject<IBlockState> BASE_STATE = new PropertyObject<>("held_state", IBlockState.class);
    
    public enum PanelType implements IStringSerializable
    {
        SQUARE("square", 11),
        SPOTS("spots", 7),
        LINEAR("linear", 9),
        SF("sf", 2);

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

    public final PanelType type;
    private IBlockState superState; // - only used clientSide
    
    public BlockPanelLighting(String assetName, PanelType newType)
    {
        super(Material.iron);
        this.type = newType;
        this.setHardness(1.0F);
        this.setStepSound(Block.soundTypeMetal);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState());
    }

    @Override
    public BlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { FACING, ROT }, new IUnlistedProperty[] { BASE_STATE });
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        if (this.type == PanelType.SQUARE)
        {
            for (int i = 0; i < PanelType.values().length; i++)
            {
                list.add(new ItemStack(itemIn, 1, i));
            }
        }
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        return this.type.ordinal();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.panelLighting0);
        //TODO: override getItemDropped() to return null if we make a broken Space Glass variant...
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
        result.add(new ItemStack(this.getItemDropped(state, null, fortune), 1, this.type.ordinal()));
        return result;
    }
    
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int damage, EntityLivingBase placer)
    {
        //This allows the different item damage values to place different instances of the block - the instances of the block are constructed in GCBlocks
        if (damage >= PanelType.values().length) damage = 0;
        switch (PanelType.values()[damage])
        {
        case SQUARE:
        default:
            return GCBlocks.panelLighting0.getDefaultState();
        case SPOTS:
            return GCBlocks.panelLighting1.getDefaultState();
        case LINEAR:
            return GCBlocks.panelLighting2.getDefaultState();
        case SF:
            return GCBlocks.panelLighting3.getDefaultState();
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, BlockCrafting.getFacingFromEntity(worldIn, pos, placer)), 2);
        int damage = stack.getItemDamage();
        if (damage >= PanelType.values().length) damage = 0;
        TileEntity tile = worldIn.getTileEntity(pos); 
        if (tile instanceof TileEntityPanelLight && placer instanceof EntityPlayer)
        {
            ((TileEntityPanelLight) tile).initialise(damage, (EntityPlayer) placer, worldIn.isRemote, ((BlockPanelLighting)state.getBlock()).superState);
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this)
        {
            return false;
        }
        int metadata = this.getMetaFromState(state);
        if (metadata < 8 && (type == PanelType.LINEAR || type == PanelType.SF))
        {
            world.setBlockState(pos, this.getStateFromMeta(metadata + 8), 3);
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
            
        world.setBlockState(pos, this.getStateFromMeta(metaDir), 3);
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
        EnumFacing enumfacing = EnumFacing.getFront(meta & 7);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(ROT, meta > 7);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getIndex() + (state.getValue(ROT) ? 8 : 0);
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
        return this.type.getLight();
    }

    @SideOnly(value=Side.CLIENT)
    public static void updateClient(int type, IBlockState state)
    {
        switch(type)
        {
        case 0:
            ((BlockPanelLighting)GCBlocks.panelLighting0).superState = state;
            return;
        case 1:
            ((BlockPanelLighting)GCBlocks.panelLighting1).superState = state;
            return;
        case 2:
            ((BlockPanelLighting)GCBlocks.panelLighting2).superState = state;
            return;
        case 3:
            ((BlockPanelLighting)GCBlocks.panelLighting3).superState = state;
            return;
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void updateClient(List<Object> data)
    {
        //IF CHANGING THIS, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS
        IBlockState state;
        state = TileEntityPanelLight.readBlockState((String) data.get(1), (Integer) data.get(2));
        ((BlockPanelLighting)GCBlocks.panelLighting0).superState = state;
        state = TileEntityPanelLight.readBlockState((String) data.get(3), (Integer) data.get(4));
        ((BlockPanelLighting)GCBlocks.panelLighting1).superState = state;
        state = TileEntityPanelLight.readBlockState((String) data.get(5), (Integer) data.get(6));
        ((BlockPanelLighting)GCBlocks.panelLighting2).superState = state;
        state = TileEntityPanelLight.readBlockState((String) data.get(7), (Integer) data.get(8));
        ((BlockPanelLighting)GCBlocks.panelLighting3).superState = state;
    }

    public static void getNetworkedData(Object[] result, IBlockState[] panel_lighting)
    {
        //IF CHANGING THIS, MAKE SURE TO CHANGE DEFINITION OF PacketSimple.C_UPDATE_STATS
        for (int i = 0; i < 4; i++)
        {
            IBlockState bs = panel_lighting[i];
            if (bs == null) bs = Blocks.air.getDefaultState();
            Block block = bs.getBlock();
            result[i + i + 1] = ((ResourceLocation)Block.blockRegistry.getNameForObject(block)).toString();
            result[i + i + 2] = block.getMetaFromState(bs);
        }
    }
}
