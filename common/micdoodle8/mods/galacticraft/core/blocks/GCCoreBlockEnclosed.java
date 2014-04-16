package micdoodle8.mods.galacticraft.core.blocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.core.GCCoreCompatibilityManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockEnclosed.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockEnclosed extends BlockContainer implements IPartialSealableBlock, ITileEntityProvider
{
	private Icon[] enclosedIcons;

	public enum EnumEnclosedBlock
	{
		TE_CONDUIT(0, 2, null, "enclosed_te_conduit"),
		OXYGEN_PIPE(1, -1, null, "enclosed_oxygen_pipe"),
		IC2_COPPER_CABLE(2, 0, null, "enclosed_copper_cable"),
		IC2_GOLD_CABLE(3, 3, null, "enclosed_gold_cable"),
		IC2_HV_CABLE(4, 6, null, "enclosed_hv_cable"),
		IC2_GLASS_FIBRE_CABLE(5, 9, null, "enclosed_glassfibre_cable"),
		IC2_LV_CABLE(6, 10, null, "enclosed_lv_cable"),
		BC_ITEM_STONEPIPE(7, -1, "PipeItemsStone", "enclosed_itempipe_stone"),
		BC_ITEM_COBBLESTONEPIPE(
				8,
				-1,
				"PipeItemsCobblestone",
				"enclosed_itempipe_cobblestone"),
		BC_FLUIDS_STONEPIPE(
				9,
				-1,
				"PipeFluidsStone",
				"enclosed_liquidpipe_stone"),
		BC_FLUIDS_COBBLESTONEPIPE(
				10,
				-1,
				"PipeFluidsCobblestone",
				"enclosed_liquidpipe_cobblestone"),
		BC_POWER_STONEPIPE(11, -1, "PipePowerStone", "enclosed_powerpipe_stone"),
		BC_POWER_GOLDPIPE(12, -1, "PipePowerGold", "enclosed_powerpipe_gold"),
		ME_CABLE(13, -1, null, "enclosed_me_cable"),
		ALUMINUM_WIRE(14, -1, null, "enclosed_aluminum_wire"),
		ALUMINUM_WIRE_HEAVY(15, -1, null, "enclosed_heavy_aluminum_wire");

		int metadata;
		int subMeta;
		String pipeClass;
		String texture;

		EnumEnclosedBlock(int metadata, int subMeta, String pipeClass, String texture)
		{
			this.metadata = metadata;
			this.subMeta = subMeta;
			this.pipeClass = pipeClass;
			this.texture = texture;
		}

		public int getMetadata()
		{
			return this.metadata;
		}

		public int getSubMetaValue()
		{
			return this.subMeta;
		}

		public String getPipeClass()
		{
			return this.pipeClass;
		}

		public String getTexture()
		{
			return this.texture;
		}
	}

	public static EnumEnclosedBlock getTypeFromMeta(int metadata)
	{
		for (EnumEnclosedBlock type : EnumEnclosedBlock.values())
		{
			if (type.getMetadata() == metadata)
			{
				return type;
			}
		}

		return null;
	}

	public GCCoreBlockEnclosed(int id, String assetName)
	{
		super(id, Material.cloth);
		this.setResistance(0.2F);
		this.setHardness(0.4f);
		this.setStepSound(Block.soundStoneFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata()));
		par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata()));
		par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.OXYGEN_PIPE.getMetadata()));

		if (GCCoreCompatibilityManager.isTELoaded())
		{
			// par3List.add(new ItemStack(par1, 1,
			// EnumEnclosedBlock.TE_CONDUIT.getMetadata()));
		}

		if (GCCoreCompatibilityManager.isIc2Loaded())
		{
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_COPPER_CABLE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GOLD_CABLE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_HV_CABLE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_GLASS_FIBRE_CABLE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.IC2_LV_CABLE.getMetadata()));
		}

		if (GCCoreCompatibilityManager.isBCraftLoaded())
		{
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_ITEM_COBBLESTONEPIPE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_ITEM_STONEPIPE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_FLUIDS_COBBLESTONEPIPE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_FLUIDS_STONEPIPE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_POWER_STONEPIPE.getMetadata()));
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata()));
		}

		if (GCCoreCompatibilityManager.isAppEngLoaded())
		{
			par3List.add(new ItemStack(par1, 1, EnumEnclosedBlock.ME_CABLE.getMetadata()));
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return par2 >= this.enclosedIcons.length ? this.blockIcon : this.enclosedIcons[par2];
	}

	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.enclosedIcons = new Icon[16];

		for (int i = 0; i < EnumEnclosedBlock.values().length; i++)
		{
			this.enclosedIcons[EnumEnclosedBlock.values()[i].getMetadata()] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + EnumEnclosedBlock.values()[i].getTexture());
		}

		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "" + EnumEnclosedBlock.OXYGEN_PIPE.getTexture());
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);

		int metadata = world.getBlockMetadata(x, y, z);
		final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
		{

		}
		else if (metadata > 0 && metadata <= EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
		{
			if (tileEntity instanceof INetworkConnection)
			{
				((INetworkConnection) tileEntity).refresh();
			}
		}
		else if (metadata <= EnumEnclosedBlock.IC2_LV_CABLE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isIc2Loaded())
			{
				try
				{
					Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");

					if (clazz != null && clazz.isInstance(tileEntity))
					{
						try
						{
							Method method = clazz.getMethod("onNeighborBlockChange");
							method.invoke(tileEntity);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
		{
		}
		else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
		{
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refresh();
			}
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refresh();
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		super.onNeighborBlockChange(world, x, y, z, blockID);

		int metadata = world.getBlockMetadata(x, y, z);
		final TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
		{

		}
		else if (metadata > 0 && metadata <= EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
		{
			if (tileEntity instanceof INetworkConnection)
			{
				((INetworkConnection) tileEntity).refresh();
			}
		}
		else if (metadata <= EnumEnclosedBlock.IC2_LV_CABLE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isIc2Loaded())
			{
				try
				{
					Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");

					if (clazz != null && clazz.isInstance(tileEntity))
					{
						try
						{
							Method method = clazz.getMethod("onNeighborBlockChange");
							method.invoke(tileEntity);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isBCraftLoaded())
			{
				try
				{
					Class<?> clazzPipe = Class.forName("buildcraft.transport.Pipe");
					Class<?> clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
					Class<?> clazzPipeBlock = Class.forName("buildcraft.transport.BlockGenericPipe");

					Method getPipe = null;

					for (Method m : clazzPipeBlock.getDeclaredMethods())
					{
						if (m.getName().equals("getPipe"))
						{
							getPipe = m;
						}
					}

					Object pipe = getPipe.invoke(null, world, x, y, z);
					Method isValid = clazzPipeBlock.getMethod("isValid", clazzPipe);
					Boolean valid = (Boolean) isValid.invoke(null, pipe);

					if (valid)
					{
						Method schedule = clazzPipeTile.getMethod("scheduleNeighborChange");
						Object container = clazzPipe.getField("container").get(pipe);
						schedule.invoke(container);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isAppEngLoaded())
			{
				world.markBlockForUpdate(x, y, z);
			}
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refresh();
			}
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
		{
			if (tileEntity instanceof IConductor)
			{
				((IConductor) tileEntity).refresh();
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		if (metadata == EnumEnclosedBlock.TE_CONDUIT.getMetadata())
		{

		}
		else if (metadata > 0 && metadata <= EnumEnclosedBlock.OXYGEN_PIPE.getMetadata())
		{
			return new GCCoreTileEntityOxygenPipe();
		}
		else if (metadata <= EnumEnclosedBlock.IC2_LV_CABLE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isIc2Loaded())
			{
				try
				{
					Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
					Constructor<?>[] constructors = clazz.getDeclaredConstructors();
					Constructor<?> constructor = null;

					for (Constructor<?> constructor2 : constructors)
					{
						constructor = constructor2;

						if (constructor.getGenericParameterTypes().length == 1)
						{
							break;
						}
					}

					constructor.setAccessible(true);

					return (TileEntity) constructor.newInstance((short) GCCoreBlockEnclosed.getTypeFromMeta(metadata).getSubMetaValue());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.BC_POWER_GOLDPIPE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isBCraftLoaded())
			{
				try
				{
					Class<?> clazz = Class.forName("buildcraft.transport.TileGenericPipe");
					Constructor<?>[] constructors = clazz.getDeclaredConstructors();
					Constructor<?> constructor = null;

					for (Constructor<?> constructor2 : constructors)
					{
						constructor = constructor2;

						if (constructor.getGenericParameterTypes().length == 0)
						{
							break;
						}
					}

					constructor.setAccessible(true);

					return (TileEntity) constructor.newInstance();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.ME_CABLE.getMetadata())
		{
			if (GCCoreCompatibilityManager.isAppEngLoaded())
			{
				try
				{
					Class<?> clazz = Class.forName("appeng.me.tile.TileCable");
					return (TileEntity) clazz.newInstance();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE.getMetadata())
		{
			return new GCCoreTileEntityAluminumWire();
		}
		else if (metadata <= EnumEnclosedBlock.ALUMINUM_WIRE_HEAVY.getMetadata())
		{
			return new GCCoreTileEntityAluminumWire(0.025F, 400.0F);
		}

		return null;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		return true;
	}
}
