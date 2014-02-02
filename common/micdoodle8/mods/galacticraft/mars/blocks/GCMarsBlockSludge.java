package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySludgeling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsBlockSludge.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsBlockSludge extends BlockFluidClassic
{
	@SideOnly(Side.CLIENT)
	Icon stillIcon;
	@SideOnly(Side.CLIENT)
	Icon flowingIcon;

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!world.isRemote)
		{
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying || entity instanceof GCMarsEntitySludgeling)
			{
				return;
			}

			int range = 5;
			List<?> l = world.getEntitiesWithinAABB(GCMarsEntitySludgeling.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range));

			if (l.size() < 3)
			{
				GCMarsEntitySludgeling sludgeling = new GCMarsEntitySludgeling(world);
				sludgeling.setPosition(x + world.rand.nextInt(5) - 2, y, z + world.rand.nextInt(5) - 2);
				world.spawnEntityInWorld(sludgeling);
			}
		}

		super.onEntityCollidedWithBlock(world, x, y, z, entity);
	}

	public GCMarsBlockSludge(int par1, Material par2Material)
	{
		super(par1, GalacticraftMars.SLUDGE, par2Material);
		this.setQuantaPerBlock(9);
		this.setRenderPass(1);
		this.setLightValue(1.0F);
		this.needsRandomTick = true;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMars.galacticraftMarsTab;
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
		this.stillIcon = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "sludge");
		this.flowingIcon = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "sludge_flow");
		GalacticraftMars.SLUDGE.setStillIcon(this.stillIcon);
		GalacticraftMars.SLUDGE.setFlowingIcon(this.flowingIcon);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		super.randomDisplayTick(world, x, y, z, rand);

		if (rand.nextInt(1200) == 0)
		{
			world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
		}
	}
}
