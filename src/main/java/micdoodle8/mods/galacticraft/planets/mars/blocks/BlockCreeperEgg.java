package micdoodle8.mods.galacticraft.planets.mars.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockCreeperEgg extends BlockDragonEgg implements ItemBlockDesc.IBlockShiftDesc
{
	public BlockCreeperEgg()
	{
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = iconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "creeperEgg");
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return 27;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		return false;
	}

	@Override
	public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
	{

	}

	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
	{
		if (!world.isRemote)
		{
			EntityEvolvedCreeper creeper = new EntityEvolvedCreeper(world);
			creeper.setPosition(x + 0.5, y + 3, z + 0.5);
            creeper.setChild(true);
			world.spawnEntityInWorld(creeper);
		}

		world.setBlockToAir(x, y, z);
		this.onBlockDestroyedByExplosion(world, x, y, z, explosion);
	}
	
	@Override
    	public boolean canDropFromExplosion(Explosion explose)
    	{
        	return false;
    	}

    	//Can only be harvested with a Sticky Desh Pickaxe
    	@Override
    	public boolean canHarvestBlock(EntityPlayer player, int metadata)
    	{
        	ItemStack stack = player.inventory.getCurrentItem();
        	if (stack == null)
        	{
            		return player.canHarvestBlock(this);
        	}
        	return stack.getItem() == MarsItems.deshPickSlime;
    	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World p_149737_2_, int p_149737_3_, int p_149737_4_, int p_149737_5_)
    	{
        	ItemStack stack = player.inventory.getCurrentItem();

        	if (stack != null && stack.getItem() == MarsItems.deshPickSlime)
        	{
        		return 0.2F;
        	}
    		return ForgeHooks.blockStrength(this, player, p_149737_2_, p_149737_3_, p_149737_4_, p_149737_5_);
    	}

    @SideOnly(Side.CLIENT)
    	@Override
    	public Item getItem(World par1World, int par2, int par3, int par4)
    	{
        	return Item.getItemFromBlock(this);
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
