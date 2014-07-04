package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

import java.util.List;
import java.util.Random;

public class BlockSludge extends BlockFluidClassic
{
	@SideOnly(Side.CLIENT)
	IIcon stillIcon;
	@SideOnly(Side.CLIENT)
	IIcon flowingIcon;

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

	public BlockSludge()
	{
		super(MarsModule.SLUDGE, Material.water);
		this.setQuantaPerBlock(9);
		this.setRenderPass(1);
		this.setLightLevel(1.0F);
		this.needsRandomTick = true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		return par1 == 0 ? this.stillIcon : this.flowingIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.stillIcon = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "sludge");
		this.flowingIcon = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "sludge_flow");
		MarsModule.SLUDGE.setStillIcon(this.stillIcon);
		MarsModule.SLUDGE.setFlowingIcon(this.flowingIcon);
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
