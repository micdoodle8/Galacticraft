package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class GCCoreItemOxygenTank extends GCCoreItem
{
	public GCCoreItemOxygenTank(int par1)
	{
		super(par1);
		this.setMaxStackSize(1);
	}

    @Override
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
		return EnumRarity.uncommon;
    }

	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
	{
		par2List.add("- Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));

    	if (player != null && player.worldObj.isRemote)
    	{
    		final GCCorePlayerBaseClient playerBaseCl = PlayerUtil.getPlayerBaseClientFromPlayer(player);

    		if (playerBaseCl != null && playerBaseCl.getUseTutorialText())
    		{
    			par2List.add("- Press " + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.tankRefill.keyCode) + " to access");
            	par2List.add("     Galacticraft Inventory");
    		}
    	}
    }
}
