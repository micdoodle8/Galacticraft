package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenPipe;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOxygenPipe extends BlockTransmitter implements ITileEntityProvider, ItemBlockDesc.IBlockShiftDesc
{
    //private IIcon[] pipeIcons = new IIcon[16];

    public BlockOxygenPipe(String assetName)
    {
        super(Material.glass);
        this.setHardness(0.3F);
        this.setStepSound(Block.soundTypeGlass);
        //this.setBlockTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntityOxygenPipe tile = (TileEntityOxygenPipe) worldIn.getTileEntity(pos);

        if (tile != null && tile.getColor() != 15)
        {
            final float f = 0.7F;
            final double d0 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            final double d1 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
            final double d2 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
            final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(Items.dye, 1, tile.getColor()));
            entityitem.setDefaultPickupDelay();
            worldIn.spawnEntityInWorld(entityitem);
        }

        super.breakBlock(worldIn, pos, state);
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
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1IBlockAccess, int x, int y, int z, int par5)
    {
        BlockVec3 thisVec = new BlockVec3(x, y, z).modifyPositionFromSide(ForgeDirection.getOrientation(par5));
        final Block blockAt = thisVec.getBlock(par1IBlockAccess);

        final TileEntityOxygenPipe tileEntity = (TileEntityOxygenPipe) par1IBlockAccess.getTileEntity(x, y, z);

        if (blockAt == GCBlocks.oxygenPipe && ((TileEntityOxygenPipe) thisVec.getTileEntity(par1IBlockAccess)).getColor() == tileEntity.getColor())
        {
            return this.pipeIcons[15];
        }

        return this.pipeIcons[tileEntity.getColor()];
    }*/

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final TileEntityOxygenPipe tileEntity = (TileEntityOxygenPipe) worldIn.getTileEntity(pos);

        if (!worldIn.isRemote)
        {
            final ItemStack stack = playerIn.inventory.getCurrentItem();

            if (stack != null)
            {
                if (stack.getItem() instanceof ItemDye)
                {
                    final int dyeColor = playerIn.inventory.getCurrentItem().getItemDamage();

                    final byte colorBefore = tileEntity.getColor();

                    tileEntity.setColor((byte) dyeColor);

                    if (colorBefore != (byte) dyeColor && !playerIn.capabilities.isCreativeMode && --playerIn.inventory.getCurrentItem().stackSize == 0)
                    {
                        playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
                    }

                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
                    {
                        final float f = 0.7F;
                        final double d0 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        final double d1 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
                        final double d2 = worldIn.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                        final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(Items.dye, 1, colorBefore));
                        entityitem.setDefaultPickupDelay();
                        worldIn.spawnEntityInWorld(entityitem);
                    }

                    //					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1)); TODO Fix pipe color

                    BlockPos tileVec = tileEntity.getPos();
                    for (final EnumFacing dir : EnumFacing.values())
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

    @Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getBlockRender(this);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.pipeIcons = new IIcon[16];

        for (int count = 0; count < ItemDye.field_150923_a.length; count++)
        {
            this.pipeIcons[count] = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "pipe_oxygen_" + ItemDye.field_150923_a[count]);
        }

        this.blockIcon = this.pipeIcons[15];
    }*/

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
    	return new TileEntityOxygenPipe(); 
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public NetworkType getNetworkType()
    {
        return NetworkType.OXYGEN;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
