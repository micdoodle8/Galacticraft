package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityHydrogenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAluminumWire extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc, ISortableBlock
{
    public static final PropertyEnum WIRE_TYPE = PropertyEnum.create("wireType", EnumWireType.class);

    public enum EnumWireType implements IStringSerializable
    {
        ALUMINUM_WIRE(0, "alu_wire"),
        ALUMINUM_WIRE_HEAVY(1, "alu_wire_heavy");

        private final int meta;
        private final String name;

        private EnumWireType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumWireType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    private Vector3 minVectorNormal = new Vector3(0.38, 0.38, 0.38);
    private Vector3 minVectorHeavy = new Vector3(0.3, 0.3, 0.3);
    private Vector3 maxVectorNormal = new Vector3(0.62, 0.62, 0.62);
    private Vector3 maxVectorHeavy = new Vector3(0.7, 0.7, 0.7);

    public BlockAluminumWire(String assetName)
    {
        super(Material.cloth);
        this.setStepSound(Block.soundTypeCloth);
        this.setResistance(0.2F);
        this.setBlockBounds(0.4F, 0.4F, 0.4F, 0.6F, 0.6F, 0.6F);
        this.setHardness(0.075F);
        this.setUnlocalizedName(assetName);
    }

    public Vector3 getMinVector(IBlockState state)
    {
        EnumWireType type = (EnumWireType)state.getValue(WIRE_TYPE);
        if (type == EnumWireType.ALUMINUM_WIRE)
        {
            return minVectorNormal;
        }
        return minVectorHeavy;
    }

    @Override
    public Vector3 getMaxVector(IBlockState state)
    {
        EnumWireType type = (EnumWireType)state.getValue(WIRE_TYPE);
        if (type == EnumWireType.ALUMINUM_WIRE)
        {
            return maxVectorNormal;
        }
        return maxVectorHeavy;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

   /* @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        BlockAluminumWire.blockIcons = new IIcon[BlockAluminumWire.names.length];

        for (int i = 0; i < BlockAluminumWire.names.length; i++)
        {
            BlockAluminumWire.blockIcons[i] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + BlockAluminumWire.names[i]);
        }
    }*/

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        switch (meta)
        {
        case 0:
            return BlockAluminumWire.blockIcons[0];
        case 1:
            return BlockAluminumWire.blockIcons[1];
        default:
            return BlockAluminumWire.blockIcons[0];
        }
    }*/

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntity tile;
    	switch (metadata)
        {
        case 0:
            tile = new TileEntityAluminumWire(1);
            break;
        case 1:
            tile = new TileEntityAluminumWire(2);
            break;
        default:
            return null;
        }
    	
    	return tile;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public NetworkType getNetworkType(IBlockState state)
    {
        return NetworkType.POWER;
    }

    @Override
    public String getShiftDescription(int itemDamage)
    {
        switch (itemDamage)
        {
        case 0:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire.description");
        case 1:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_heavy.description");
        }
        return "";
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WIRE_TYPE, EnumWireType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((EnumWireType)state.getValue(WIRE_TYPE)).getMeta();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, WIRE_TYPE, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public boolean showDescription(int itemDamage)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.TRANSMITTER;
    }
}
