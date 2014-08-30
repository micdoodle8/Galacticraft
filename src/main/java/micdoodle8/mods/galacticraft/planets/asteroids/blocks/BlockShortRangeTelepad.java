package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockShortRangeTelepad extends BlockTileGC implements ItemBlockDesc.IBlockShiftDesc
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
	    ((EntityPlayer) entityLiving).inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(this), 1, 0));
            return;
        }

		if (tile instanceof TileEntityShortRangeTelepad)
		{
			((TileEntityShortRangeTelepad) tile).onCreate(new BlockVec3(x0, y0, z0));
            ((TileEntityShortRangeTelepad) tile).setOwner(((EntityPlayer) entityLiving).getGameProfile().getName());
		}
	}

    @Override
    public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        return ((IMultiBlock) world.getTileEntity(x, y, z)).onActivated(par5EntityPlayer);
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
                        if (world.getBlock(x0 + x, y0 + y, z0 + z) == AsteroidBlocks.fakeTelepad)
                        {
                            fakeBlockCount++;
                        }
                    }
                }
            }
        }

		if (fakeBlockCount > 0 && tileAt instanceof TileEntityShortRangeTelepad)
		{
			((TileEntityShortRangeTelepad) tileAt).onDestroy(tileAt);
            ShortRangeTelepadHandler.removeShortRangeTeleporter((TileEntityShortRangeTelepad) tileAt);
		}

		super.breakBlock(world, x0, y0, z0, var5, var6);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
        final TileEntity tileAt = world.getTileEntity(x, y, z);

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            TileEntityShortRangeTelepad telepad = (TileEntityShortRangeTelepad) tileAt;
            float teleportTimeScaled = Math.min(1.0F, telepad.teleportTime / (float)TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
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
                    GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(x + 0.2 + rand.nextDouble() * 0.6, y + 0.1, z + 0.2 + rand.nextDouble() * 0.6), new Vector3(0.0, 1.4, 0.0), telepad, false);
                }

                f = rand.nextFloat() * 0.6F + 0.4F;
                r = f * 0.3F;
                g = f * (0.3F + (teleportTimeScaled * 0.7F));
                b = f * (1.0F - (teleportTimeScaled * 0.7F));
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(x + 0.0 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble()), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(x + 0.8 + rand.nextDouble() * 0.2, y + 2.9, z + rand.nextDouble()), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(x + rand.nextDouble(), y + 2.9, z + 0.2 + rand.nextDouble() * 0.2), new Vector3(0.0, -2.95, 0.0), telepad, true);
                GalacticraftPlanets.spawnParticle("portalBlue", new Vector3(x + rand.nextDouble(), y + 2.9, z + 0.8 + rand.nextDouble() * 0.2), new Vector3(0.0, -2.95, 0.0), telepad, true);
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
}
