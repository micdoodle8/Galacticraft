package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class GCCoreItemOxygenTank extends GCCoreItem
{
	public EnumOxygenTankTier tier;
	
	public GCCoreItemOxygenTank(int par1)
	{
		super(par1);
		this.setMaxStackSize(1);
	}
	
	public GCCoreItemOxygenTank setTankTier(EnumOxygenTankTier tier)
	{
		this.tier = tier;
		return this;
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
		
    	if (player.worldObj.isRemote)
    	{
    		GCCorePlayerBaseClient playerBaseCl = GCCoreUtil.getPlayerBaseClientFromPlayer(player);
    		
    		if (playerBaseCl.getUseTutorialText())
    		{
    			par2List.add("- Press " + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.tankRefill.keyCode) + " to access");
            	par2List.add("     Galacticraft Inventory");
    		}
    	}
    }
}
