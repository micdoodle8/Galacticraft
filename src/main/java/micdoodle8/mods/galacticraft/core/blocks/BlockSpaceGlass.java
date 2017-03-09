package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpaceGlass extends Block implements IPartialSealableBlock
{
    public static final PropertyEnum TYPE  = PropertyEnum.create("type", GlassType.class);
    public static final PropertyEnum MODEL = PropertyEnum.create("modeltype", GlassModel.class);
    public static final PropertyEnum ROTATION  = PropertyEnum.create("rot", GlassRotation.class);
    //public static final PropertyInteger PLACING  = PropertyInteger.create("placing", 0, 2);
    //This will define whether originally placed by the player facing NS - EW - or UD

    private Vector3 minVector = new Vector3(0.0, 0.32, 0.0);
    private Vector3 maxVector = new Vector3(1.0, 1.0, 1.0);


    public BlockSpaceGlass(String assetName)
    {
        super(Material.glass);
        this.setHardness(0.3F);
        this.setResistance(30F);
        this.setUnlocalizedName(assetName);
        this.setStepSound(Block.soundTypeGlass);
        this.isBlockContainer = true;
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, GlassType.CLEAR).withProperty(MODEL, GlassModel.STANDARD_PANE).withProperty(ROTATION, GlassRotation.N));//.withProperty(ROT_X, EnumGlassRotation.EN).withProperty(ROT_Z, EnumGlassRotation.EN));
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {TYPE, MODEL, ROTATION});
    }
    
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getMetaFromState(state);
    }
    //TODO: override get itemDropped for broken Space Glass...
    
    @Override
    public float getBlockHardness(World worldIn, BlockPos pos)
    {
        GlassType type = (GlassType) worldIn.getBlockState(pos).getValue(TYPE);
        if (type == GlassType.STRONG) return 3.0F;

        return this.blockHardness;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return true;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
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

        boolean plateD = this.buildSolidSide(below, state);
        boolean plateU = this.buildSolidSide(above, state);
        boolean plateN = this.buildSolidSide(north, state);
        boolean plateS = this.buildSolidSide(south, state);
        boolean plateW = this.buildSolidSide(west, state);
        boolean plateE = this.buildSolidSide(east, state);
        
        float xMin = plateU || plateD || plateN || plateS ? 0.125F : 0.375F;
        float xMax = plateU || plateD || plateN || plateS ? 0.875F : 0.625F;
        float yMin = plateE || plateW || plateN || plateS ? 0.125F : 0.375F;
        float yMax = plateE || plateW || plateN || plateS ? 0.875F : 0.625F;
        float zMin = plateU || plateD || plateE || plateW ? 0.125F : 0.375F;
        float zMax = plateU || plateD || plateE || plateW  ? 0.875F : 0.625F;
        
        if (connectW) xMin = 0F;
        if (connectE) xMax = 1F;
        if (connectN) zMin = 0F;
        if (connectS) zMax = 1F;
        
        this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        IBlockState above = worldIn.getBlockState(pos.up());
        IBlockState below = worldIn.getBlockState(pos.down());
        IBlockState north = worldIn.getBlockState(pos.north());
        IBlockState south = worldIn.getBlockState(pos.south());
        IBlockState west = worldIn.getBlockState(pos.west());
        IBlockState east = worldIn.getBlockState(pos.east());
        
        boolean connectedN = this.canPaneConnectToBlock(north, state);
        boolean connectedS = this.canPaneConnectToBlock(south, state);
        boolean connectedW = this.canPaneConnectToBlock(west, state);
        boolean connectedE = this.canPaneConnectToBlock(east, state);

        boolean plateD = this.buildSolidSide(below, state);
        boolean plateU = this.buildSolidSide(above, state);
        boolean plateN = this.buildSolidSide(north, state);
        boolean plateS = this.buildSolidSide(south, state);
        boolean plateW = this.buildSolidSide(west, state);
        boolean plateE = this.buildSolidSide(east, state);
        if (connectedN || connectedS)
        {
            plateW = false;
            plateE = false;
        }
        if (connectedW || connectedE)
        {
            plateN = false;
            plateS = false;
        }
              
        int solids = (plateD ? 1 : 0) + (plateU ? 1 : 0) + (plateN ? 1 : 0) + (plateS ? 1 : 0) + (plateW ? 1 : 0) + (plateE ? 1 : 0);
        if (solids > 2 || (plateU && plateD) || (plateW && plateE) || (plateN && plateS))
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
            
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.375F;
        float f3 = 0.625F;

        if ((!connectedW || !connectedE) && (connectedW || connectedE || connectedN || connectedS))
        {
            if (connectedW)
            {
                f = 0.0F;
            }
            else if (connectedE)
            {
                f1 = 1.0F;
            }
        }
        else
        {
            f = 0.0F;
            f1 = 1.0F;
        }

        if ((!connectedN || !connectedS) && (connectedW || connectedE || connectedN || connectedS))
        {
            if (connectedN)
            {
                f2 = 0.0F;
            }
            else if (connectedS)
            {
                f3 = 1.0F;
            }
        }
        else
        {
            f2 = 0.0F;
            f3 = 1.0F;
        }
        if (plateW) f = 0F;
        if (plateE) f1 = 1F;
        if (plateN) f2 = 0F;
        if (plateS) f3 = 1F;

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing dir)
    {
        IBlockState thisBlock = world.getBlockState(pos);
        if ((dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH) && this.isConnectedEW(thisBlock, world, pos)) return false;
        if ((dir == EnumFacing.WEST || dir == EnumFacing.EAST) && this.isConnectedNS(thisBlock, world, pos)) return false;
        IBlockState otherBlock = world.getBlockState(pos.offset(dir.getOpposite()));
        return this.buildSolidSide(otherBlock, thisBlock);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        //TODO = yes if solid on side
        return false; //worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
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
        int solidsEW = (west.getBlock().isSideSolid(worldIn, pos.west(), EnumFacing.EAST) ? 1 : 0) + (east.getBlock().isSideSolid(worldIn, pos.east(), EnumFacing.WEST) ? 1 : 0); 
        int solidsNS = (north.getBlock().isSideSolid(worldIn, pos.north(), EnumFacing.SOUTH) ? 1 : 0) + (south.getBlock().isSideSolid(worldIn, pos.south(), EnumFacing.NORTH) ? 1 : 0);
        return solidsEW > solidsNS;
    }
    
    protected boolean canPaneConnectToBlock(IBlockState off, IBlockState blockState)
    {
        return off.getBlock() == this && off.getValue(TYPE) == blockState.getValue(TYPE);
    }

    protected boolean buildSolidSide(IBlockState off, IBlockState blockState)
    {
        return off.getBlock() != this;
    }

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
        
        //TODO: if connections < 2 and there is a SpaceGlass block above or below, match its connections
        boolean plateD = this.buildSolidSide(below, state);
        boolean plateU = this.buildSolidSide(above, state);
        boolean plateN = this.buildSolidSide(north, state);
        boolean plateS = this.buildSolidSide(south, state);
        boolean plateW = this.buildSolidSide(west, state);
        boolean plateE = this.buildSolidSide(east, state);

        int connections = (connectN ? 1 : 0) + (connectS ? 1 : 0) + (connectW ? 1 : 0) + (connectE ? 1 : 0);
        GlassRotation rot = GlassRotation.N;

        
        if (connections == 4)
            return getModel(state, 4, rot, plateD, false, false, plateU);

        if (connections == 3)
        {
            if (!connectE) rot = GlassRotation.S;
            else if (!connectN) rot = GlassRotation.E;
            else if (!connectS) rot = GlassRotation.W;

            return getModel(state, 3, rot, plateD, false, false, plateU);
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
                
            return getModel(state, 1, rot, plateD, plateS, plateN, plateU);
        }
        
        if (connections == 1 || (connectN && connectS && !connectW && !connectE))
        {
            if (connections == 1 && (connectW || connectE))
                return getModel(state, 1, GlassRotation.E, plateD, plateW, plateE, plateU);
            
            return getModel(state, 1, rot, plateD, plateS, plateN, plateU);
        }

        if (connectW && connectE)
        {
            return getModel(state, 1, GlassRotation.E, plateD, plateW, plateE, plateU);
        }

        //corner cases
        if (connectW && connectS) rot = GlassRotation.S;
        else if (connectS && connectE) rot = GlassRotation.E;
        else if (connectN && connectW) rot = GlassRotation.W;
        
        return getModel(state, 2, rot, plateD, false, false, plateU);
    }

    private IBlockState getModel(IBlockState state, int model, GlassRotation rot, boolean plateD, boolean plateL, boolean plateR, boolean plateU)
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
                return state.withProperty(MODEL, GlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 0));

            if (plateU && (plateR || plateL))
                return state.withProperty(MODEL, GlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 2));

            if (plateU || plateD)
                return state.withProperty(MODEL, GlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(y, plateU ? 2 : 0));

            if (plateR || plateL)
                return state.withProperty(MODEL, GlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(plateL ? (y ^ 2) : y, 1));
            
            return state.withProperty(MODEL, GlassModel.STANDARD_PANE).withProperty(ROTATION, rot.get(y, 0));
        case 2:
            if (plateU && plateD)
                return state.withProperty(MODEL, GlassModel.CORNER_S2).withProperty(ROTATION, rot.get(y, 0));
            if (plateD)
                return state.withProperty(MODEL, GlassModel.CORNER_S).withProperty(ROTATION, rot.get(y, 0));
            if (plateU)
                return state.withProperty(MODEL, GlassModel.CORNER_S).withProperty(ROTATION, rot.get(y ^ 1, 2));
            
            return state.withProperty(MODEL, GlassModel.CORNER).withProperty(ROTATION, rot.get(y, 0));
        case 3:
            if (plateD)
                return state.withProperty(MODEL, GlassModel.T_JUNCTION_S).withProperty(ROTATION, rot.get(y, 0));
            if (plateU)
                return state.withProperty(MODEL, GlassModel.T_JUNCTION_S).withProperty(ROTATION, rot.get(y ^ 2, 2));
                
            return state.withProperty(MODEL, GlassModel.T_JUNCTION).withProperty(ROTATION, rot.get(y, x));
        case 4:
        default:
            return state.withProperty(MODEL, GlassModel.CROSSROADS).withProperty(ROTATION, rot.get(y, x));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TYPE, GlassType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((GlassType) state.getValue(TYPE)).getMeta();
    }

    public enum GlassModel implements IStringSerializable
    {
        STANDARD_PANE("standard"),
        CORNER("corner"),
        T_JUNCTION("joint"),
        CROSSROADS("joinx"),
        STANDARD_S1("standards1"),
        STANDARD_S2("standards2"),
        STANDARD_S2A("standards2a"),
        STANDARD_S3("standards3"),
        STANDARD_S4("standards4"),
        CORNER_S("corner_s"),
        CORNER_S2("corner_s2"),
        T_JUNCTION_S("joint_s"),
        CROSSROADS_S("joinx_s");

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
        UE("uy"),
        US("uyy"),
        UW("uyyy"),
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

    public enum GlassType implements IStringSerializable
    {
        CLEAR(0, "clear"),
        VANILLA(1, "vanilla"),
        STRONG(2, "strong");

        private final int meta;
        private final String name;

        private GlassType (int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static GlassType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}

