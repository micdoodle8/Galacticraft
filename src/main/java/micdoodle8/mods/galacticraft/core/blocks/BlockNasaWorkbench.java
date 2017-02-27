package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockNasaWorkbench extends BlockContainer implements ITileEntityProvider, IShiftDescription, IPartialSealableBlock, ISortableBlock
{
    public BlockNasaWorkbench(String assetName)
    {
        super(Material.IRON);
        this.setBlockBounds(-0.3F, 0.0F, -0.3F, 1.3F, 0.5F, 1.3F);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return new AxisAlignedBB((double) pos.getX() + -0.0F, (double) pos.getY() + 0.0F, (double) pos.getZ() + -0.0F, (double) pos.getX() + 1.0F, (double) pos.getY() + 1.4F, (double) pos.getZ() + 1.0F);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

        final MovingObjectPosition r = super.collisionRayTrace(worldIn, pos, start, end);

        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

        return r;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final TileEntity tile = worldIn.getTileEntity(pos);

        boolean validSpot = true;

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            Block blockAt = worldIn.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();

                            if ((y == 0 || y == 3) && x == 0 && z == 0)
                            {
                                if (!blockAt.getMaterial().isReplaceable())
                                {
                                    validSpot = false;
                                }
                            }
                            else if (y != 0 && y != 3)
                            {
                                if (!blockAt.getMaterial().isReplaceable())
                                {
                                    validSpot = false;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!validSpot)
        {
            worldIn.setBlockToAir(pos);

            if (!worldIn.isRemote && placer instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) placer;
                player.addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                if (!player.capabilities.isCreativeMode)
                {
                    final ItemStack nasaWorkbench = new ItemStack(this, 1, 0);
                    final EntityItem entityitem = player.dropPlayerItemWithRandomChoice(nasaWorkbench, false);
                    entityitem.setPickupDelay(0);
                    entityitem.setOwner(player.getName());
                }
            }

            return;
        }

        if (tile instanceof IMultiBlock)
        {
            ((IMultiBlock) tile).onCreate(worldIn, pos);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        int fakeBlockCount = 0;

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            Block block = worldIn.getBlockState(pos.add(x, y, z)).getBlock();

                            if ((y == 0 || y == 3) && x == 0 && z == 0)
                            {
                                if (block == GCBlocks.fakeBlock)
                                {
                                    fakeBlockCount++;
                                }
                            }
                            else if (y != 0 && y != 3)
                            {
                                if (block == GCBlocks.fakeBlock)
                                {
                                    fakeBlockCount++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (fakeBlockCount >= 11 && var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(GalacticraftCore.instance, SchematicRegistry.getMatchingRecipeForID(0).getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityNasaWorkbench();
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

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
