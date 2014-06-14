package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBeamReceiver extends BlockTileGC
{
	public BlockBeamReceiver(String assetName)
	{
		super(Material.iron);
		this.setBlockName(assetName);
		this.setBlockTextureName("stone");
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return AsteroidsModule.asteroidsTab;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
    	TileEntityBeamReceiver thisTile = (TileEntityBeamReceiver)world.getTileEntity(x, y, z);
    	thisTile.setFacing(ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z)));
	}
	
	private int getMetadataFromAngle(World world, int x, int y, int z, int side)
	{
		TileEntityBeamReceiver thisTile = (TileEntityBeamReceiver)world.getTileEntity(x, y, z);
		ForgeDirection direction = ForgeDirection.getOrientation(side).getOpposite();

		TileEntity tileAt = world.getTileEntity(x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ);
		
		if (tileAt != null && tileAt instanceof EnergyStorageTile)
		{
			if (((EnergyStorageTile) tileAt).getModeFromDirection(direction.getOpposite()) != null)
			{
				return direction.ordinal();
			}
			else
			{
				return -1;
			}
		}
		
		for (ForgeDirection adjacentDir : ForgeDirection.VALID_DIRECTIONS)
		{
			tileAt = world.getTileEntity(x + adjacentDir.offsetX, y + adjacentDir.offsetY, z + adjacentDir.offsetZ);

			if (tileAt != null && tileAt instanceof EnergyStorageTile && ((EnergyStorageTile) tileAt).getModeFromDirection(adjacentDir.getOpposite()) != null)
			{
				return adjacentDir.ordinal();
			}
		}
		
		return -1;
	}

    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
    	return getMetadataFromAngle(world, x, y, z, side);
    }
    
	@Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
		if (getMetadataFromAngle(world, x, y, z, side) != -1)
		{
			return true;
		}
		
		if (world.isRemote && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			this.sendIncorrectSideMessage();
		}
		
    	return false;
    }
	
	@SideOnly(Side.CLIENT)
	private void sendIncorrectSideMessage()
	{
		FMLClientHandler.instance().getClient().thePlayer.addChatMessage(new ChatComponentText(EnumColor.RED + "Cannot attach receiver here!"));
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return 0;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		return new TileEntityBeamReceiver();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}
}
