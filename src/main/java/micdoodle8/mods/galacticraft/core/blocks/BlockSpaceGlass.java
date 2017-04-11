package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.JavaUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockSpaceGlass extends Block implements IPartialSealableBlock, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum MODEL = PropertyEnum.create("modeltype", GlassModel.class);
    public static final PropertyEnum ROTATION  = PropertyEnum.create("rot", GlassRotation.class);
    //public static final PropertyInteger PLACING  = PropertyInteger.create("placing", 0, 2);
    //This will define whether originally placed by the player facing NS - EW - or UD

    private final GlassType type;
    private final GlassFrame frame; //frameValue corresponds to the damage of the placing item
    private int color = 0xFFFFFF;
    private final Block baseBlock;
    private boolean isClient; 
    private static Class clazz = Blocks.WATER.getClass().getSuperclass();
    
    public BlockSpaceGlass(String assetName, GlassType newType, GlassFrame newFrame, Block base)
    {
        super(Material.GLASS);
        this.isClient = GalacticraftCore.proxy.getClass().getName().equals("micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore");
        this.type = newType;
        this.frame = newFrame;
        this.baseBlock = base == null ? this : base;
        this.color = frame.getDefaultColor();
        this.setUnlocalizedName(assetName);
        this.setSoundType(SoundType.GLASS);
        this.isBlockContainer = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(MODEL, GlassModel.STANDARD_PANE).withProperty(ROTATION, GlassRotation.N));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {MODEL, ROTATION});
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        //The plain block variety produces items carrying all variants as damage values
        //Other block varieties have no corresponding ItemBlock (see registration in GCBlocks)
        if (this.frame == GlassFrame.PLAIN)
        {
            for (int i = 0; i < GlassFrame.values().length; i++)
                list.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.DECORATION;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int damage, EntityLivingBase placer)
    {
        //This allows the different item damage values to place different instances of the block - the instances of the block are constructed in GCBlocks
        if (damage >= GlassFrame.values().length) damage = 0;
        switch (GlassFrame.values()[damage])
        {
        case TIN_DECO:
        {
            switch (this.type)
            {
            case STRONG:
                return GCBlocks.spaceGlassTinStrong.getDefaultState();
            case VANILLA:
                return GCBlocks.spaceGlassTinVanilla.getDefaultState();
            default:
                return GCBlocks.spaceGlassTinClear.getDefaultState();
            }
        }
        case PLAIN:
        default:
            return this.getDefaultState();
        }
        //TODO: Add PLACING direction
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.frame.ordinal();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this.baseBlock);
        //TODO: override getItemDropped() to return null if we make a broken Space Glass variant...
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this.getItemDropped(state, null, fortune), 1, this.frame.ordinal()));
        return ret;
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing direction)
    {
        return direction.ordinal() > 1;
    }

    @Override
    public Material getMaterial(IBlockState state)
    {
        if (this.isClient && JavaUtil.instance.isCalledBySpecific(clazz))
        {
            return Material.WATER;
        }
        return this.blockMaterial;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
         return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean canRenderInLayer(BlockRenderLayer layer)
    {
        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID;
    }
    
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        IBlockState above = worldIn.getBlockState(pos.up());
//        IBlockState below = worldIn.getBlockState(pos.down());
//        IBlockState north = worldIn.getBlockState(pos.north());
//        IBlockState south = worldIn.getBlockState(pos.south());
//        IBlockState west = worldIn.getBlockState(pos.west());
//        IBlockState east = worldIn.getBlockState(pos.east());
//
//        boolean connectN = this.canPaneConnectToBlock(north, state);
//        boolean connectS = this.canPaneConnectToBlock(south, state);
//        boolean connectW = this.canPaneConnectToBlock(west, state);
//        boolean connectE = this.canPaneConnectToBlock(east, state);
//
//        boolean plateD = this.buildSolidSide(below, state);
//        boolean plateU = this.buildSolidSide(above, state);
//        boolean plateN = this.buildSolidSide(north, state);
//        boolean plateS = this.buildSolidSide(south, state);
//        boolean plateW = this.buildSolidSide(west, state);
//        boolean plateE = this.buildSolidSide(east, state);
//
//        //No plate on the sides which glass is facing
//        if (connectN || connectS)
//        {
//            plateW = plateE = false;
//        }
//        if (connectW || connectE)
//        {
//            plateN = plateS = false;
//        }
//
//        //Singleton
//        if (!connectE && !connectW && !connectN && !connectS)
//        {
//            boolean prefEW = false;
//            if (below.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(below, worldIn, pos.down()) || isPreferenceEW(below, worldIn, pos.down()));
//            }
//            else if (above.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(above, worldIn, pos.up()) || isPreferenceEW(above, worldIn, pos.up()));
//            }
//
//            if (this.isPreferenceEW(state, worldIn, pos))
//                prefEW = true;
//
//            if (prefEW)
//            {
//                plateN = plateS = false;
//            }
//            else
//            {
//                plateW = plateE = false;
//            }
//        }
//
//        float posGlass = 0.375F;
//        float posBase = 0.225F;
//        float xMin = plateU || plateD || plateN || plateS ? posBase : posGlass;
//        float xMax = plateU || plateD || plateN || plateS ? 1F - posBase : 1F - posGlass;
//        float yMin = 0F; //plateE || plateW || plateN || plateS ? posBase : 0.375F;
//        float yMax = 1F; //plateE || plateW || plateN || plateS ? 0.775F : 0.625F;
//        float zMin = plateU || plateD || plateE || plateW ? posBase : posGlass;
//        float zMax = plateU || plateD || plateE || plateW ? 1F - posBase : 1F - posGlass;
//
//        if (plateW || connectW) xMin = 0F;
//        if (plateE || connectE) xMax = 1F;
//        if (plateN || connectN) zMin = 0F;
//        if (plateS || connectS) zMax = 1F;
//
//        //Special for corner diagonals
//        if ((connectW ^ connectE) && (connectN ^ connectS) && !(plateU && plateD))
//        {
//            float diag = 0.25F;
//            if (connectW)
//            {
//                xMin = diag - 0.01F;
//                xMax = diag;
//            }
//            else
//            {
//                xMin = 1F - diag;
//                xMax = 1.01F - diag;
//            }
//            if (connectN)
//            {
//                zMin = diag - 0.01F;
//                zMax = diag;
//            }
//            else
//            {
//                zMin = 1F - diag;
//                zMax = 1.01F - diag;
//            }
//        }
//
//        this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }
//
//    @Override
//    public void setBlockBoundsForItemRender()
//    {
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//TODO:  Set up predefined AABBs
//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        IBlockState state = worldIn.getBlockState(pos);
//        IBlockState above = worldIn.getBlockState(pos.up());
//        IBlockState below = worldIn.getBlockState(pos.down());
//        IBlockState north = worldIn.getBlockState(pos.north());
//        IBlockState south = worldIn.getBlockState(pos.south());
//        IBlockState west = worldIn.getBlockState(pos.west());
//        IBlockState east = worldIn.getBlockState(pos.east());
//
//        boolean connectedN = this.canPaneConnectToBlock(north, state);
//        boolean connectedS = this.canPaneConnectToBlock(south, state);
//        boolean connectedW = this.canPaneConnectToBlock(west, state);
//        boolean connectedE = this.canPaneConnectToBlock(east, state);
//
//        boolean plateD = this.buildSolidSide(below, state);
//        boolean plateU = this.buildSolidSide(above, state);
//        boolean plateN = this.buildSolidSide(north, state);
//        boolean plateS = this.buildSolidSide(south, state);
//        boolean plateW = this.buildSolidSide(west, state);
//        boolean plateE = this.buildSolidSide(east, state);
//
//        //No plate on the sides which glass is facing
//        if (connectedN || connectedS)
//        {
//            plateW = plateE = false;
//        }
//        if (connectedW || connectedE)
//        {
//            plateN = plateS = false;
//        }
//
//        //Singleton
//        if (!connectedE && !connectedW && !connectedN && !connectedS)
//        {
//            boolean prefEW = false;
//            if (below.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(below, worldIn, pos.down()) || isPreferenceEW(below, worldIn, pos.down()));
//            }
//            else if (above.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(above, worldIn, pos.up()) || isPreferenceEW(above, worldIn, pos.up()));
//            }
//
//            if (this.isPreferenceEW(state, worldIn, pos))
//                prefEW = true;
//
//            if (prefEW)
//            {
//                plateN = plateS = false;
//            }
//            else
//            {
//                plateW = plateE = false;
//            }
//        }
//
//        float f, f1, f2, f3;
//        int solids = (plateD ? 1 : 0) + (plateU ? 1 : 0) + (plateN ? 1 : 0) + (plateS ? 1 : 0) + (plateW ? 1 : 0) + (plateE ? 1 : 0);
//        if (solids > 2 || (plateU && plateD) || (plateW && plateE) || (plateN && plateS))
//        {
//            //Widen to the frame width, if frames on opposite sides
//            f = 0.25F;
//            f1 = 0.75F;
//            f2 = 0.25F;
//            f3 = 0.75F;
//        }
//        else {
//            //The glass width
//            f = 0.375F;
//            f1 = 0.625F;
//            f2 = 0.375F;
//            f3 = 0.625F;
//        }


    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing dir)
    {
        IBlockState thisBlock = world.getBlockState(pos);
        if ((dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH) && this.isConnectedEW(thisBlock, world, pos)) return false;
        if ((dir == EnumFacing.WEST || dir == EnumFacing.EAST) && this.isConnectedNS(thisBlock, world, pos)) return false;
        IBlockState otherBlock = world.getBlockState(pos.offset(dir.getOpposite()));
        return this.buildSolidSide(otherBlock, thisBlock);
    }

    protected boolean isConnectedEW(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState west = worldIn.getBlockState(pos.west());
        IBlockState east = worldIn.getBlockState(pos.east());    
        return this.canPaneConnectToBlock(west, state) || this.canPaneConnectToBlock(east, state);
    }

    protected boolean isConnectedNS(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState north = worldIn.getBlockState(pos.north());
        IBlockState south = worldIn.getBlockState(pos.south());    
        return this.canPaneConnectToBlock(north, state) || this.canPaneConnectToBlock(south, state);
    }

    protected boolean isPreferenceEW(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState north = worldIn.getBlockState(pos.north());
        IBlockState south = worldIn.getBlockState(pos.south());
        IBlockState west = worldIn.getBlockState(pos.west());
        IBlockState east = worldIn.getBlockState(pos.east());
        int solidsEW = (west.getBlock().isSideSolid(worldIn.getBlockState(pos.west()), worldIn, pos.west(), EnumFacing.EAST) ? 1 : 0) + (east.getBlock().isSideSolid(worldIn.getBlockState(pos.east()), worldIn, pos.east(), EnumFacing.WEST) ? 1 : 0);
        int solidsNS = (north.getBlock().isSideSolid(worldIn.getBlockState(pos.north()), worldIn, pos.north(), EnumFacing.SOUTH) ? 1 : 0) + (south.getBlock().isSideSolid(worldIn.getBlockState(pos.south()), worldIn, pos.south(), EnumFacing.NORTH) ? 1 : 0);
        return solidsEW > solidsNS;
    }
    
    protected boolean canPaneConnectToBlock(IBlockState off, IBlockState blockState)
    {
        return off.getBlock() == this;
    }

    protected boolean buildSolidSide(IBlockState off, IBlockState blockState)
    {
        return !(off.getBlock() == this);
    }

    protected boolean buildSolidSideUD(BlockPos pos, EnumFacing testUD, IBlockAccess worldIn, IBlockState blockState)
    {
        BlockPos offPos = pos.offset(testUD, 1);
        IBlockState off = worldIn.getBlockState(offPos);
        if (off.getBlock() != this) return true;
        int connThis = (isConnectedEW(blockState, worldIn, pos) ? 2 : 0) + (isConnectedNS(blockState, worldIn, pos) ? 1 : 0);
        int connOther = (isConnectedEW(off, worldIn, offPos) ? 2 : 0) + (isConnectedNS(off, worldIn, offPos) ? 1 : 0);
        if (connThis == 0 || connOther == 0)
            return false;
        if (connThis != connOther)
            return true;
        return false;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState above = worldIn.getBlockState(pos.up());
        IBlockState below = worldIn.getBlockState(pos.down());
        IBlockState north = worldIn.getBlockState(pos.north());
        IBlockState south = worldIn.getBlockState(pos.south());
        IBlockState west = worldIn.getBlockState(pos.west());
        IBlockState east = worldIn.getBlockState(pos.east());
        
        boolean connectN = this.canPaneConnectToBlock(north, state);
        boolean connectS = this.canPaneConnectToBlock(south, state);
        boolean connectW = this.canPaneConnectToBlock(west, state);
        boolean connectE = this.canPaneConnectToBlock(east, state);
        
        boolean plateD = buildSolidSideUD(pos, EnumFacing.DOWN, worldIn, state);
        boolean plateU = buildSolidSideUD(pos, EnumFacing.UP, worldIn, state);
        boolean plateN = this.buildSolidSide(north, state);
        boolean plateS = this.buildSolidSide(south, state);
        boolean plateW = this.buildSolidSide(west, state);
        boolean plateE = this.buildSolidSide(east, state);

        int connections = (connectN ? 1 : 0) + (connectS ? 1 : 0) + (connectW ? 1 : 0) + (connectE ? 1 : 0);
        GlassRotation rot = GlassRotation.N;
        int cornerPiece = 0;
        
        if (connections == 4)
            return getModel(state, 4, rot, plateD, false, false, plateU, 0);

        if (connections == 3)
        {
            if (!connectE) rot = GlassRotation.S;
            else if (!connectN) rot = GlassRotation.E;
            else if (!connectS) rot = GlassRotation.W;

            return getModel(state, 3, rot, plateD, false, false, plateU, 0);
        }

        if (connections == 0)
        {
            if (below.getBlock() == this)
            {
                if (this.isConnectedEW(below, worldIn, pos.down()) || this.isPreferenceEW(below, worldIn, pos.down()))
                    rot = GlassRotation.E;
            }
            else if (above.getBlock() == this)
            {
                if (this.isConnectedEW(above, worldIn, pos.up()) || this.isPreferenceEW(above, worldIn, pos.up()))
                    rot = GlassRotation.E;
            }

            if (this.isPreferenceEW(state, worldIn, pos))
                rot = GlassRotation.E;
                
            return getModel(state, 1, rot, plateD, plateS, plateN, plateU, 0);
        }
        
//        EnumFacing testUD = EnumFacing.UP;
//        if (plateU && !plateD)           
//        {
//            testUD = EnumFacing.DOWN;
//        }
//        if (plateD && !plateU)           
//        {
//            testUD = EnumFacing.UP;
//        }

        // Two glass connections in a flat plane - this also covers connected flat on one side only
        if (connectW && connectE || connections == 1 && (connectW || connectE))
        {
            rot = GlassRotation.E;
            if (connectW && !plateU && buildSolidSideUD(pos.west(), EnumFacing.UP, worldIn, west))
            {
                cornerPiece++;
            }
            if (connectW && !plateD && buildSolidSideUD(pos.west(), EnumFacing.DOWN, worldIn, west))
            {
                cornerPiece+=5;
            }
            if (connectE && !plateU && buildSolidSideUD(pos.east(), EnumFacing.UP, worldIn, east))
            {
                cornerPiece++;
                if (connectW) rot = GlassRotation.W;
            }
            if (connectE && !plateD && buildSolidSideUD(pos.east(), EnumFacing.DOWN, worldIn, east))
            {
                cornerPiece+=5;
                if (connectW) rot = GlassRotation.W;
            }
            return getModel(state, 1, rot, plateD, plateW, plateE, plateU, cornerPiece);
        }

        if (connectN && connectS || connections == 1)
        {
            if (connectN && !plateU && buildSolidSideUD(pos.north(), EnumFacing.UP, worldIn, north))
            {
                cornerPiece++;
                if (connectN && connectS) rot = GlassRotation.S;
            }
            if (connectN && !plateD && buildSolidSideUD(pos.north(), EnumFacing.DOWN, worldIn, north))
            {
                cornerPiece+=5;
                if (connectN && connectS) rot = GlassRotation.S;
            }
            if (connectS && !plateU && buildSolidSideUD(pos.south(), EnumFacing.UP, worldIn, south))
            {
                cornerPiece++;
            }
            if (connectS && !plateD && buildSolidSideUD(pos.south(), EnumFacing.DOWN, worldIn, south))
            {
                cornerPiece+=5;
            }
            return getModel(state, 1, rot, plateD, plateS, plateN, plateU, cornerPiece);
        }

        //It must be two glass connections on a corner

        if (connectW && connectS) rot = GlassRotation.S;
        else if (connectS && connectE) rot = GlassRotation.E;
        else if (connectN && connectW) rot = GlassRotation.W;
        
        return getModel(state, 2, rot, plateD, false, false, plateU, 0);
    }

    private IBlockState getModel(IBlockState state, int model, GlassRotation rot, boolean plateD, boolean plateL, boolean plateR, boolean plateU, int cornerPiece)
    {
        int x = 0;
        int y = rot.ordinal();
        if (y % 2 == 1) y = y ^ 2;
        int solids = (plateD ? 1 : 0) + (plateU ? 1 : 0) + (plateR ? 1 : 0) + (plateL ? 1 : 0);
        switch (model)
        {
        case 1:
            if (solids == 4)
                return state.withProperty(MODEL, GlassModel.STANDARD_S4).withProperty(ROTATION, rot.get(y, 0));

            if (solids == 3)
            {
                if (!plateD) x = ( y % 2 == 1) ? 3 : 1; 
                else if (!plateR) { x = 2; y = y ^ 2; }
                else if (!plateU) x = ( y % 2 == 1) ? 1 :3;
                return state.withProperty(MODEL, GlassModel.STANDARD_S3).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateD && plateU)
                return state.withProperty(MODEL, GlassModel.STANDARD_S2A).withProperty(ROTATION, rot.get(y, 0));

            if (plateL && plateR)
                return state.withProperty(MODEL, GlassModel.STANDARD_S2A).withProperty(ROTATION, rot.get(y, 1));

            if (plateD && (plateR || plateL))
            {
                return state.withProperty(MODEL, cornerPiece > 0 ? GlassModel.STANDARD_S2B : GlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 0));
            }

            if (plateU && (plateR || plateL))
            {
                return state.withProperty(MODEL, cornerPiece > 0 ? GlassModel.STANDARD_S2B : GlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 2));
            }

            if (plateU || plateD)
                return state.withProperty(MODEL, getCornerModel(cornerPiece)).withProperty(ROTATION, rot.get(y, (plateU || cornerPiece > 4) ? 2 : 0));

            if (plateR || plateL)
                return state.withProperty(MODEL, getCornerModel(cornerPiece)).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 1 + ( cornerPiece > 4 ? 2 : 0)));
            
            return state.withProperty(MODEL, GlassModel.STANDARD_PANE).withProperty(ROTATION, rot.get(y, 0));
        case 2:
            if (plateU && plateD)
                return state.withProperty(MODEL, GlassModel.CORNER_S2).withProperty(ROTATION, rot.get(y, 0));
            if (plateD)
                return state.withProperty(MODEL, GlassModel.CORNER_S).withProperty(ROTATION, rot.get(y, 0));
            if (plateU)
                return state.withProperty(MODEL, GlassModel.CORNER_S).withProperty(ROTATION, rot.get((y + 3) % 4, 2));
            
            return state.withProperty(MODEL, GlassModel.CORNER).withProperty(ROTATION, rot.get(y, 0));
        case 3:
            if (plateD && plateU)
                return state.withProperty(MODEL, GlassModel.T_JUNCTION_S2).withProperty(ROTATION, rot.get(y, 0));
            if (plateD)
                return state.withProperty(MODEL, GlassModel.T_JUNCTION_S).withProperty(ROTATION, rot.get(y, 0));
            if (plateU)
                return state.withProperty(MODEL, GlassModel.T_JUNCTION_S).withProperty(ROTATION, rot.get(y ^ 2, 2));
                
            return state.withProperty(MODEL, GlassModel.T_JUNCTION).withProperty(ROTATION, rot.get(y, x));
        case 4:
            if (plateD && plateU)
                return state.withProperty(MODEL, GlassModel.CROSSROADS_S2).withProperty(ROTATION, rot.get(y, 0));
            if (plateD)
                return state.withProperty(MODEL, GlassModel.CROSSROADS_S).withProperty(ROTATION, rot.get(y, 0));
            if (plateU)
                return state.withProperty(MODEL, GlassModel.CROSSROADS_S).withProperty(ROTATION, rot.get(y ^ 2, 2));
        default:
            return state.withProperty(MODEL, GlassModel.CROSSROADS).withProperty(ROTATION, rot.get(y, x));
        }
    }
    
    private Comparable getCornerModel(int cornerPiece)
    {
        if ((cornerPiece & 2) == 2)
            return GlassModel.STANDARD_S1BB;
        return cornerPiece > 0 ? GlassModel.STANDARD_S1B : GlassModel.STANDARD_S1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    public int setColor(int newColor)
    {
        if (newColor >= 0 && this.color != newColor)
        {
            this.color = newColor;
            return 1;
        }
        return 0;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (this.type)
        {
        case CLEAR:
            return GCCoreUtil.translate("tile.space_glass.clear.description");
        case VANILLA:
            return GCCoreUtil.translate("tile.space_glass.description");
        case STRONG:
        default:
            return GCCoreUtil.translate("tile.space_glass.strong.description");
        }
        
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    public enum GlassModel implements IStringSerializable
    {
        STANDARD_PANE("standard"),
        CORNER("corner"),
        T_JUNCTION("joint"),
        CROSSROADS("joinx"),
        STANDARD_S1("standards1"),
        STANDARD_S1B("standards1b"),
        STANDARD_S1BB("standards1bb"),
        STANDARD_S2("standards2"),
        STANDARD_S2A("standards2a"),
        STANDARD_S2B("standards2b"),
        STANDARD_S3("standards3"),
        STANDARD_S4("standards4"),
        CORNER_S("corner_s"),
        CORNER_S2("corner_s2"),
        T_JUNCTION_S("joint_s"),
        T_JUNCTION_S2("joint_s2"),
        CROSSROADS_S("joinx_s"),
        CROSSROADS_S2("joinx_s2");

        private final String name;

        private GlassModel(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public enum GlassRotation implements IStringSerializable
    {
        N("0"),
        E("y"),
        S("yy"),
        W("yyy"),
        UN("u"),
        UE("uyyy"),
        US("uyy"),
        UW("uy"),
        UUN("uuyy"),
        UUE("uuyyy"),
        UUS("uu"),
        UUW("uuy"),
        DN("d"),
        DE("dy"),
        DS("dyy"),
        DW("dyyy");

        private final String name;

        private GlassRotation(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        private static GlassRotation get(int i)
        {
            return GlassRotation.values()[i];
        }

        private static GlassRotation get(int y, int x)
        {
            int i = y + x *4;
            return GlassRotation.values()[i];
        }
}

    public enum GlassFrame implements IStringSerializable
    {
        PLAIN("plain", 0xfafaf7),
        TIN_DECO("tin_deco", 0xFFFFFF);
        //TODO - more frame textures can be added in future - maybe only on Asteroids?

        private final String name;
        private final int defaultColor;
        
        private GlassFrame (String name, int color)
        {
            this.name = name;
            this.defaultColor = color;
        }

        @Override
        public String getName()
        {
            return this.name;
        }

        public int getDefaultColor()
        {
            return this.defaultColor;
        }
    }

    public enum GlassType implements IStringSerializable
    {
        VANILLA("vanilla"),
        CLEAR("clear"),
        STRONG("strong");

        private final String name;

        private GlassType (String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}

