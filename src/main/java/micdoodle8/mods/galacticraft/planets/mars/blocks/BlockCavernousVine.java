package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, ItemBlockDesc.IBlockShiftDesc
{
	@SideOnly(Side.CLIENT)
	private IIcon[] vineIcons;

	public BlockCavernousVine()
	{
		super(Material.vine);
		this.setLightLevel(1.0F);
		this.setTickRandomly(true);
		this.setStepSound(soundTypeGrass);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec3d, Vec3 vec3d1)
	{
		return super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z)
	{
		if (world.setBlockToAir(x, y, z))
		{
			int y2 = y - 1;
			while (world.getBlock(x, y2, z) == this)
			{
				world.setBlockToAir(x, y2, z);
				y2--;
			}

			return true;
		}

		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying)
			{
				return;
			}

			entity.motionY = 0.06F;
			entity.rotationYaw += 0.4F;

			if (!((EntityLivingBase) entity).getActivePotionEffects().contains(Potion.poison))
			{
				((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 5, 20, false));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.vineIcons = new IIcon[3];

		for (int i = 0; i < 3; i++)
		{
			this.vineIcons[i] = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "vine_" + i);
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return this.getVineLight(world, x, y, z);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (meta < 3)
		{
			return this.vineIcons[meta];
		}

		return super.getIcon(side, meta);
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
		return GalacticraftPlanets.getBlockRenderID(this);
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		return ForgeDirection.getOrientation(side) == ForgeDirection.DOWN && this.isBlockSolid(world, x, y + 1, z, side);
	}

	public int getVineLength(IBlockAccess world, int x, int y, int z)
	{
		int vineCount = 0;
		int y2 = y;

		while (world.getBlock(x, y2, z) == MarsBlocks.vine)
		{
			vineCount++;
			y2++;
		}

		return vineCount;
	}

	public int getVineLight(IBlockAccess world, int x, int y, int z)
	{
		int vineCount = 0;
		int y2 = y;

		while (world.getBlock(x, y2, z) == MarsBlocks.vine)
		{
			vineCount += 4;
			y2--;
		}

		return Math.max(19 - vineCount, 0);
	}

	@Override
	public int tickRate(World par1World)
	{
		return 50;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if (!world.isRemote)
		{
			for (int y2 = y - 1; y2 >= y - 2; y2--)
			{
				Block blockID = world.getBlock(x, y2, z);

				if (!blockID.isAir(world, x, y, z))
				{
					return;
				}
			}

			world.setBlock(x, y - 1, z, this, this.getVineLength(world, x, y, z) % 3, 2);
			world.func_147451_t(x, y, z);
		}

	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			// world.scheduleBlockUpdate(x, y, z, this,
			// this.tickRate(world) + world.rand.nextInt(10));
		}
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3)
	{
		return Item.getItemFromBlock(Blocks.air);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
	{
		super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this, 1, 0));
		return ret;
	}

	@Override
	public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
	{
		return true;
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
