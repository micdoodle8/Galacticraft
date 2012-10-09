package micdoodle8.mods.galacticraft;

import micdoodle8.mods.galacticraft.client.ClientProxy;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class GCItemJetpack extends ItemArmor
{
	public boolean active;
	
	public GCItemJetpack(int par1, EnumArmorMaterial material, int par3, int par4) 
	{
		super(par1, material, par3, par4);
		this.setMaxStackSize(1);
		this.setMaxDamage(256);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) 
    {
    	if (entity instanceof EntityPlayer)
    	{
    		EntityPlayer player = (EntityPlayer) entity;
    		
    		if (ClientProxy.jetpackCooldown <= 0)
    		{
    			itemstack.damageItem(1, player);
    			ClientProxy.jetpackCooldown = 100;
    		}
    	}
    	
    	if (this.active)
    	{
    	}
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/client/items/core.png";
	}
}
