package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWireSwitch;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockAluminumWire extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumWireType> WIRE_TYPE = PropertyEnum.create("wireType", EnumWireType.class);

    public enum EnumWireType implements IStringSerializable
    {
        ALUMINUM_WIRE(0, "alu_wire"),
        ALUMINUM_WIRE_HEAVY(1, "alu_wire_heavy"),
        ALUMINUM_WIRE_SWITCHED(2, "alu_wire_switch"),
        ALUMINUM_WIRE_SWITCHED_HEAVY(3, "alu_wire_switch_heavy");

        private final int meta;
        private final String name;

        EnumWireType(int meta, String name)
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
        public String getName()
        {
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

    @Override
    public Vector3 getMinVector(IBlockState state)
    {
        EnumWireType type = (EnumWireType) state.getValue(WIRE_TYPE);
        if (type == EnumWireType.ALUMINUM_WIRE || type == EnumWireType.ALUMINUM_WIRE_SWITCHED)
        {
            return minVectorNormal;
        }
        return minVectorHeavy;
    }

    @Override
    public Vector3 getMaxVector(IBlockState state)
    {
        EnumWireType type = (EnumWireType) state.getValue(WIRE_TYPE);
        if (type == EnumWireType.ALUMINUM_WIRE || type == EnumWireType.ALUMINUM_WIRE_SWITCHED)
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
        case 2:
            tile = new TileEntityAluminumWireSwitch(1);
            break;
        case 3:
            tile = new TileEntityAluminumWireSwitch(2);
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
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
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
        case 2:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_switch.description");
        case 3:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_switch_heavy.description");
        }
        return "";
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WIRE_TYPE, EnumWireType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumWireType) state.getValue(WIRE_TYPE)).getMeta();
    }

    @Override
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
