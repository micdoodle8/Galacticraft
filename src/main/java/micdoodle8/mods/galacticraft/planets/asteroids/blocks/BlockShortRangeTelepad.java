package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockShortRangeTelepad extends BlockContainer
{
	protected BlockShortRangeTelepad(String assetName)
	{
		super(Material.iron);
		this.blockHardness = 3.0F;
		this.setBlockName(assetName);
		this.setBlockTextureName("stone");
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityShortRangeTelepad();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
		super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, list, entity);
	}

	@Override
	public void onBlockPlacedBy(World world, int x0, int y0, int z0, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x0, y0, z0, entityLiving, itemStack);

		TileEntity tile = world.getTileEntity(x0, y0, z0);

        final TileEntity var8 = world.getTileEntity(x0, y0, z0);

        boolean validSpot = true;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        Block blockAt = world.getBlock(x0 + x, y0 + y, z0 + z);

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
            world.setBlockToAir(x0, y0, z0);

            if (!world.isRemote && entityLiving instanceof EntityPlayer)
            {
                ((EntityPlayer) entityLiving).addChatMessage(new ChatComponentText(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
            }

            return;
        }

		if (tile instanceof IMultiBlock)
		{
			((IMultiBlock) tile).onCreate(new BlockVec3(x0, y0, z0));
		}

	}

	@Override
	public void breakBlock(World world, int x0, int y0, int z0, Block var5, int var6)
	{
		final TileEntity tileAt = world.getTileEntity(x0, y0, z0);

        int fakeBlockCount = 0;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (world.getBlock(x0 + x, y0 + y, z0 + z) == GCBlocks.fakeBlock)
                        {
                            fakeBlockCount++;
                        }
                    }
                }
            }
        }

		if (fakeBlockCount > 0 && tileAt instanceof IMultiBlock)
		{
			((IMultiBlock) tileAt).onDestroy(tileAt);
		}

		super.breakBlock(world, x0, y0, z0, var5, var6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				world.spawnParticle("portal", x + 0.2 + rand.nextDouble() * 0.6, y + 0.1, z + 0.2 + rand.nextDouble() * 0.6, 0.0, 1.75, 0.0);
			}

			world.spawnParticle("portal", x + 0.0 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble(), 0.0, -2.95, 0.0);
			world.spawnParticle("portal", x + 0.8 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble(), 0.0, -2.95, 0.0);
			world.spawnParticle("portal", x + rand.nextDouble(), y + 2.9, z + 0.2 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
			world.spawnParticle("portal", x + rand.nextDouble(), y + 2.9, z + 0.8 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
		}
	}
}
