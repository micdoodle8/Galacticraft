package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockFluid.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockFluid extends BlockFluidClassic
{
	private Icon stillIcon;
	private Icon flowingIcon;
	private final String fluidName;
	private final Fluid fluid;

	public GCCoreBlockFluid(int id, Fluid fluid, String assetName)
	{
		super(id, fluid, Material.water);
		this.setRenderPass(1);
		this.fluidName = assetName;
		this.fluid = fluid;

		if (assetName.equals("oil"))
		{
			this.needsRandomTick = true;
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
		return par1 == 0 ? this.stillIcon : this.flowingIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.stillIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + this.fluidName + "_still");
		this.flowingIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + this.fluidName + "_flow");
		this.fluid.setStillIcon(this.stillIcon);
		this.fluid.setFlowingIcon(this.flowingIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		super.randomDisplayTick(world, x, y, z, rand);

		if (this.fluidName.equals("oil") && rand.nextInt(1200) == 0)
		{
			world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
		}
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlockMaterial(x, y, z).isLiquid())
		{
			return false;
		}

		return super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z)
	{
		if (world.getBlockMaterial(x, y, z).isLiquid())
		{
			return false;
		}

		return super.displaceIfPossible(world, x, y, z);
	}

	public Icon getStillIcon()
	{
		return this.stillIcon;
	}

	public Icon getFlowingIcon()
	{
		return this.flowingIcon;
	}
}
