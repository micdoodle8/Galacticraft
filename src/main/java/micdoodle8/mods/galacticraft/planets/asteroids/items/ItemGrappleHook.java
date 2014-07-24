package micdoodle8.mods.galacticraft.planets.asteroids.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class ItemGrappleHook extends ItemBow
{
	public ItemGrappleHook(String assetName)
	{
		super();
		this.setUnlocalizedName(assetName);
		this.setMaxStackSize(16);
		this.setTextureName("arrow");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public CreativeTabs getCreativeTab()
	{
		return GalacticraftCore.galacticraftItemsTab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
	{
        EntityGrapple grapple = new EntityGrapple(par2World, par3EntityPlayer, 2.0F);

        par3EntityPlayer.inventory.consumeInventoryItem(AsteroidsItems.grapple);
        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(grapple);
        }
	}

	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		return par1ItemStack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
	{
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("arrow");
    }
}