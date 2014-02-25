package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.blocks.BlockFluid;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.EntitySludgeling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
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
public class BlockSludge extends BlockFluid
{
	@SideOnly(Side.CLIENT)
	private IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	IIcon flowingIcon;

	public BlockSludge(Fluid fluid, String assetName)
	{
		super(fluid, GalacticraftMars.TEXTURE_PREFIX, assetName);
		this.setRenderPass(1);
		this.setLightLevel(1.0F);
		this.needsRandomTick = true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (!world.isRemote)
		{
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying || entity instanceof EntitySludgeling)
			{
				return;
			}

			int range = 5;
			List<?> l = world.getEntitiesWithinAABB(EntitySludgeling.class, AxisAlignedBB.getBoundingBox(x - range, y - range, z - range, x + range, y + range, z + range));

			if (l.size() < 3)
			{
				EntitySludgeling sludgeling = new EntitySludgeling(world);
				sludgeling.setPosition(x + world.rand.nextInt(5) - 2, y, z + world.rand.nextInt(5) - 2);
				world.spawnEntityInWorld(sludgeling);
			}
		}

		super.onEntityCollidedWithBlock(world, x, y, z, entity);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftMars.galacticraftMarsTab;
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
