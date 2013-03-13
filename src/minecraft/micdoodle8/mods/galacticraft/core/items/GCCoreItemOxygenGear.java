package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class GCCoreItemOxygenGear extends Item
{
	public GCCoreItemOxygenGear(int par1)
	{
		super(par1);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if (par2EntityPlayer.worldObj.isRemote)
    	{
    		final GCCorePlayerBaseClient playerBaseCl = PlayerUtil.getPlayerBaseClientFromPlayer(par2EntityPlayer);

    		if (playerBaseCl != null && playerBaseCl.getUseTutorialText())
    		{
            	par3List.add("Press " + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.tankRefill.keyCode) + " to access");
            	par3List.add("     Galacticraft Inventory");
    		}
    	}
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister iconRegister)
	{
		this.iconIndex = iconRegister.func_94245_a("galacticraftcore:oxygen_gear");
	}

	@Override
	public Icon getIconFromDamage(int damage)
	{
		return super.getIconFromDamage(damage);
	}
}
