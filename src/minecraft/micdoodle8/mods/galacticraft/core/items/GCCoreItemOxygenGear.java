package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class GCCoreItemOxygenGear extends GCCoreItem
{
	public GCCoreItemOxygenGear(int par1)
	{
		super(par1);
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
    		GCCorePlayerBaseClient playerBaseCl = PlayerUtil.getPlayerBaseClientFromPlayer(par2EntityPlayer);
    		
    		if (playerBaseCl.getUseTutorialText())
    		{
            	par3List.add("Press " + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.tankRefill.keyCode) + " to access");
            	par3List.add("     Galacticraft Inventory");
    		}
    	}
    }
}
