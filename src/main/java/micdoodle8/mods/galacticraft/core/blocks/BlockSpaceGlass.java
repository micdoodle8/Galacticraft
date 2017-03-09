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
    public static final PropertyEnum TYPE  = PropertyEnum.create("type", EnumGlassType.class);
    public static final PropertyEnum MODEL = PropertyEnum.create("modeltype", EnumGlassModel.class);
    public static final PropertyEnum ROTATION  = PropertyEnum.create("rot", EnumGlassRotation.class);
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumGlassType.CLEAR).withProperty(MODEL, EnumGlassModel.STANDARD_PANE).withProperty(ROTATION, EnumGlassRotation.N));//.withProperty(ROT_X, EnumGlassRotation.EN).withProperty(ROT_Z, EnumGlassRotation.EN));
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
        EnumGlassType type = (EnumGlassType) worldIn.getBlockState(pos).getValue(TYPE);
        if (type == EnumGlassType.STRONG) return 3.0F;

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
        IBlockState blockState = worldIn.getBlockState(pos);
        boolean flag = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()), blockState);
        boolean flag1 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()), blockState);
        boolean flag2 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()), blockState);
        boolean flag3 = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()), blockState);

        if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
        {
            if (flag2)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (flag3)
            {
                this.setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
        {
            if (flag)
            {
                this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
            else if (flag1)
            {
                this.setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }
        else
        {
            this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float f = 0.4375F;
        float f1 = 0.5625F;
        float f2 = 0.4375F;
        float f3 = 0.5625F;
        IBlockState blockState = worldIn.getBlockState(pos);
        boolean connectedN = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()), blockState);
        boolean connectedS = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()), blockState);
        boolean connectedW = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()), blockState);
        boolean connectedE = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()), blockState);

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

        this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
    }

//    @Override
//    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing dir)
//    {
//        BlockPos off = pos.offset(dir.getOpposite());
//        Block block = world.getBlockState(off).getBlock();
//        return canPaneConnectToBlock(block) || block.isSideSolid(world, off, dir.getOpposite());
//    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        //TODO = yes if solid on side
        return false; //worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    public boolean canPaneConnectToBlock(IBlockState off, IBlockState blockState)
    {
        return off.getBlock() == this && off.getValue(TYPE) == blockState.getValue(TYPE);
    }

    public boolean buildSolidSide(IBlockState off, IBlockState blockState)
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
        EnumGlassRotation rot = EnumGlassRotation.N;

        
        if (connections == 4)
            return getState(state, 4, rot, plateD, false, false, false);

        if (connections == 3)
        {
            if (!connectE) rot = EnumGlassRotation.S;
            else if (!connectN) rot = EnumGlassRotation.E;
            else if (!connectS) rot = EnumGlassRotation.W;

            return getState(state, 3, rot, plateD, false, false, false);
        }

        if (connections < 2 || (connectN && connectS && !connectW && !connectE))
        {
            if (connections == 1 && (connectW || connectE))
                return getState(state, 1, EnumGlassRotation.E, plateD, plateW, plateE, plateU);

            return getState(state, 1, rot, plateD, plateS, plateN, plateU);
        }

        if (connectW && connectE)
        {
            return getState(state, 1, EnumGlassRotation.E, plateD, plateW, plateE, plateU);
        }

        //corner cases
        if (connectW && connectS) rot = EnumGlassRotation.S;
        else if (connectS && connectE) rot = EnumGlassRotation.E;
        else if (connectN && connectW) rot = EnumGlassRotation.W;
        
        return getState(state, 2, rot, plateD, false, false, plateU);
    }

    private IBlockState getState(IBlockState state, int model, EnumGlassRotation rot, boolean plateD, boolean plateL, boolean plateR, boolean plateU)
    {
        int x = 0;
        int y = rot.ordinal();
        if (y % 2 == 1) y = y ^ 2;
        switch (model)
        {
        case 1:
            if (plateD && plateR)
            {
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateD && plateL)
            {
                y = y ^ 2;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateD)
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(y, x));
            
            if (plateU && plateR)
            {
                x = 2;
                y = y  ^ 2;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateU && plateL)
            {
                x = 2;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S2).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateU)
            {
                x = 2;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateL)
            {
                x = 1;
                y = y ^ 2;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(y, x));
            }

            if (plateR)
            {
                x = 1;
                return state.withProperty(MODEL, EnumGlassModel.STANDARD_S1).withProperty(ROTATION, rot.get(y, x));
            }
            
            return state.withProperty(MODEL, EnumGlassModel.STANDARD_PANE).withProperty(ROTATION, rot.get(y, x));
        case 2:
            return state.withProperty(MODEL, EnumGlassModel.CORNER).withProperty(ROTATION, rot.get(y, x));
        case 3:
            return state.withProperty(MODEL, EnumGlassModel.T_JUNCTION).withProperty(ROTATION, rot.get(y, x));
        case 4:
        default:
            return state.withProperty(MODEL, EnumGlassModel.CROSSROADS).withProperty(ROTATION, rot.get(y, x));
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
        return this.getDefaultState().withProperty(TYPE, EnumGlassType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumGlassType) state.getValue(TYPE)).getMeta();
    }

    public enum EnumGlassModel implements IStringSerializable
    {
        STANDARD_PANE("standard"),
        CORNER("corner"),
        T_JUNCTION("joint"),
        CROSSROADS("joinx"),
        STANDARD_S1("standards1"),
        STANDARD_S2("standards2");

        private final String name;

        private EnumGlassModel(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public enum EnumGlassRotation implements IStringSerializable
    {
        N("0"),
        E("y"),
        S("yy"),
        W("yyy"),
        UN("u"),
        UE("uy"),
        US("uyy"),
        UW("uyyy"),
        UUN("uu"),
        UUE("uuy"),
        UUS("uuyy"),
        UUW("uuyyy"),
        DN("d"),
        DE("dy"),
        DS("dyy"),
        DW("dyyy");

        private final String name;

        private EnumGlassRotation(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        private static EnumGlassRotation get(int i)
        {
            return EnumGlassRotation.values()[i];
        }

        private static EnumGlassRotation get(int y, int x)
        {
            int i = y + x *4;
            return EnumGlassRotation.values()[i];
        }
}

    public enum EnumGlassType implements IStringSerializable
    {
        CLEAR(0, "clear"),
        VANILLA(1, "vanilla"),
        STRONG(2, "strong");

        private final int meta;
        private final String name;

        private EnumGlassType (int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumGlassType byMetadata(int meta)
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

