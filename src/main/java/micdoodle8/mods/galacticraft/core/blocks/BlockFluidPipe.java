package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFluidPipe extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public Vector3 minVector = new Vector3(0.35, 0.35, 0.35);
    public Vector3 maxVector = new Vector3(0.65, 0.65, 0.65);

    public static boolean ignoreDrop = false;

    private EnumPipeMode mode;

    public BlockFluidPipe(String assetName, EnumPipeMode mode)
    {
        super(Material.glass);
        this.setHardness(0.3F);
        this.setStepSound(Block.soundTypeGlass);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setUnlocalizedName(assetName);
        this.mode = mode;
    }

    @Override
    public Vector3 getMinVector(IBlockState state)
    {
        return minVector;
    }

    @Override
    public Vector3 getMaxVector(IBlockState state)
    {
        return maxVector;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntityFluidPipe tile = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
        int pipeColor = state.getValue(COLOR).getDyeDamage();

        if (!ignoreDrop && tile != null && pipeColor != 15)
        {
            spawnItem(worldIn, pos, pipeColor);
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.oxygenPipe);
        //Never drop the 'pull' variety of pipe
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        worldIn.notifyLightSet(pos);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        if (this.mode == EnumPipeMode.NORMAL)
        {
            return GalacticraftCore.galacticraftBlocksTab;
        }

        return null;
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getTileEntity(pos);
            tile.switchType();
        }

        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final TileEntityFluidPipe tileEntity = (TileEntityFluidPipe) worldIn.getTileEntity(pos);

        if (super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ))
        {
            return true;
        }

        if (!worldIn.isRemote)
        {
            final ItemStack stack = playerIn.inventory.getCurrentItem();

            if (stack != null)
            {
                if (stack.getItem() instanceof ItemDye)
                {
                    final int dyeColor = playerIn.inventory.getCurrentItem().getItemDamage();

                    final byte colorBefore = tileEntity.getColor(state);

                    tileEntity.onColorUpdate();

                    worldIn.setBlockState(pos, state.withProperty(COLOR, EnumDyeColor.byDyeDamage(dyeColor)));

                    GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_RECOLOR_PIPE, GCCoreUtil.getDimensionID(worldIn), new Object[] { pos }), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(worldIn), pos.getX(), pos.getY(), pos.getZ(), 40.0));

                    if (colorBefore != (byte) dyeColor && !playerIn.capabilities.isCreativeMode && --playerIn.inventory.getCurrentItem().stackSize == 0)
                    {
                        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                    }

                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
                    {
                        spawnItem(worldIn, pos, colorBefore);
                    }

                    //					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1)); TODO Fix pipe color

                    BlockPos tileVec = tileEntity.getPos();
                    for (final EnumFacing dir : EnumFacing.VALUES)
                    {
                        final TileEntity tileAt = worldIn.getTileEntity(tileVec.offset(dir));

                        if (tileAt != null && tileAt instanceof IColorable)
                        {
                            ((IColorable) tileAt).onAdjacentColorChanged(dir);
                        }
                    }

                    return true;
                }
            }

        }

        return false;
    }

    private void spawnItem(World worldIn, BlockPos pos, int colorBefore)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(Items.dye, 1, colorBefore));
        entityitem.setDefaultPickupDelay();
        worldIn.spawnEntityInWorld(entityitem);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return true;
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityFluidPipe();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public NetworkType getNetworkType(IBlockState state)
    {
        return NetworkType.FLUID;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, COLOR, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.TRANSMITTER;
    }

    public EnumPipeMode getMode()
    {
        return mode;
    }

    public enum EnumPipeMode implements IStringSerializable
    {
        NORMAL(0, "normal"),
        PULL(1, "pull");

        private final int meta;
        private final String name;

        EnumPipeMode(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return meta;
        }

        public static EnumPipeMode byMetadata(int ordinal)
        {
            return EnumPipeMode.values()[ordinal];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}
