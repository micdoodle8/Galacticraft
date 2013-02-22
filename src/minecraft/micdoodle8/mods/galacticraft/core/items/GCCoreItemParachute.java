package micdoodle8.mods.galacticraft.core.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemParachute extends GCCoreItem
{
	public static final String[] names = {
		"plain", // 0
		"black", // 1
		"blue", // 2
		"green", // 3
		"brown", // 4
		"darkblue", // 5
		"darkgray", // 6
		"darkgreen", // 7
		"gray", // 8
		"magenta", // 9
		"orange", // 10
		"pink", // 11
		"purple", // 12
		"red", // 13
		"teal", // 14
		"yellow"}; // 15
	
	public GCCoreItemParachute(int par1)
	{
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
    	for (int i = 0; i < GCCoreItemParachute.names.length; i++)
    	{
            par3List.add(new ItemStack(par1, 1, i));
    	}
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
	public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= GCCoreItemParachute.names.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + GCCoreItemParachute.names[var2];
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getIconFromDamage(int par1)
    {
    	switch (par1)
    	{
    	case 0: // plain
    		return 49;
    	case 1: // black
    		return 34;
    	case 2: // blue
    		return 46;
    	case 3: // green
    		return 44;
    	case 4: // brown
    		return 37;
    	case 5: // dark blue
    		return 38;
    	case 6: // dark gray
    		return 42;
    	case 7: // dark green
    		return 36;
    	case 8: // gray
    		return 41;
    	case 9: // magenta
    		return 47;
    	case 10: // orange
    		return 48;
    	case 11: // pink
    		return 43;
    	case 12: // purple
    		return 39;
    	case 13: // red
    		return 35;
    	case 14: // teal
    		return 40;
    	case 15: // yellow
    		return 45;
    	}
    	
    	return 0;
    }
    
    public static int getParachuteDamageValueFromDye(int meta)
    {
    	switch (meta)
    	{
    	case 0:
    		return 1;
    	case 1:
    		return 13;
    	case 2:
    		return 7;
    	case 3:
    		return 4;
    	case 4:
    		return 5;
    	case 5:
    		return 12;
    	case 6:
    		return 14;
    	case 7:
    		return 8;
    	case 8:
    		return 6;
    	case 9:
    		return 11;
    	case 10:
    		return 3;
    	case 11:
    		return 15;
    	case 12:
    		return 2;
    	case 13:
    		return 9;
    	case 14:
    		return 10;
    	case 15:
    		return 0;
    	}
    	
    	return -1;
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
    		GCCorePlayerBaseClient playerBaseCl = GCCoreUtil.getPlayerBaseClientFromPlayer(par2EntityPlayer);
    		
    		if (playerBaseCl.getUseTutorialText())
    		{
            	par3List.add("Press " + Keyboard.getKeyName(ClientProxyCore.GCKeyHandler.tankRefill.keyCode) + " to access");
            	par3List.add("     Galacticraft Inventory");
    		}
    	}
    }
}
