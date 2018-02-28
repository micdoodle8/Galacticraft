package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockShortRangeTelepad extends BlockTileGC implements IShiftDescription, ISortableBlock
{
    protected BlockShortRangeTelepad(String assetName)
    {
        super(Material.iron);
        this.blockHardness = 3.0F;
        this.setUnlocalizedName(assetName);
        this.setStepSound(Block.soundTypeMetal);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public int getRenderType()
    {
        return -1;
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
        return new TileEntityShortRangeTelepad();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        boolean validSpot = true;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        Block blockAt = worldIn.getBlockState(pos.add(x, y, z)).getBlock();

                        if (!blockAt.getMaterial().isReplaceable())
                        {
                            validSpot = false;
                        }
                    }
                }
            }
        }

        if (!validSpot)
        {
            worldIn.setBlockToAir(pos);

            if (placer instanceof EntityPlayer)
            {
                if (!worldIn.isRemote)
                {
                    ((EntityPlayer) placer).addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                }
                ((EntityPlayer) placer).inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(this), 1, 0));
            }

            return;
        }

        if (tile instanceof TileEntityShortRangeTelepad)
        {
            ((TileEntityShortRangeTelepad) tile).onCreate(worldIn, pos);
            ((TileEntityShortRangeTelepad) tile).setOwner(PlayerUtil.getName(((EntityPlayer) placer)));
        }
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return ((IMultiBlock) worldIn.getTileEntity(pos)).onActivated(playerIn);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity tileAt = worldIn.getTileEntity(pos);

        int fakeBlockCount = 0;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() == AsteroidBlocks.fakeTelepad)
                        {
                            fakeBlockCount++;
                        }
                    }
                }
            }
        }

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            if (fakeBlockCount > 0)
            {
                ((TileEntityShortRangeTelepad) tileAt).onDestroy(tileAt);
            }
            ShortRangeTelepadHandler.removeShortRangeTeleporter((TileEntityShortRangeTelepad) tileAt);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        final TileEntity tileAt = worldIn.getTileEntity(pos);

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            TileEntityShortRangeTelepad telepad = (TileEntityShortRangeTelepad) tileAt;
            float teleportTimeScaled = Math.min(1.0F, telepad.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
            float f;
            float r;
            float g;
            float b;

            for (int i = 0; i < 6; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    f = rand.nextFloat() * 0.6F + 0.4F;
                    r = f * 0.3F;
                    g = f * (0.3F + (teleportTimeScaled * 0.7F));
                    b = f * (1.0F - (teleportTimeScaled * 0.7F));
                    GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(pos.getX() + 0.2 + rand.nextDouble() * 0.6, pos.getY() + 0.1, pos.getZ() + 0.2 + rand.nextDouble() * 0.6), new Vector3(0.0, 1.4, 0.0), telepad, false);
                }

                f = rand.nextFloat() * 0.6F + 0.4F;
                r = f * 0.3F;
                g = f * (0.3F + (teleportTimeScaled * 0.7F));
                b = f * (1.0F - (teleportTimeScaled * 0.7F));
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(pos.getX() + 0.0 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble()), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(pos.getX() + 0.8 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble()), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.2 + rand.nextDouble() * 0.2), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.8 + rand.nextDouble() * 0.2), new Vector3(0.0, -2.95, 0.0), telepad, true);
            }
        }
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
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}
