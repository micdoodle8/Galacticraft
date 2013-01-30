package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GCMarsItemJetpack extends ItemArmor
{
	public boolean active;
	
	public GCMarsItemJetpack(int par1, EnumArmorMaterial material, int par3, int par4) 
	{
		super(par1, material, par3, par4);
		this.setMaxStackSize(1);
		this.setMaxDamage(256);
		this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}
	
	@Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) 
    {
    	if (entity instanceof EntityPlayer)
    	{
    		final EntityPlayer player = (EntityPlayer) entity;
    		
    		if (GalacticraftCore.tick % 100 == 0)
    		{
    			if (!player.capabilities.isCreativeMode)
    			{
        			player.inventory.consumeInventoryItem(Item.coal.itemID);
    			}
    		}
    	}
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/mars/client/items/mars.png";
	}
	
	public void setActive()
	{
		this.active = true;
	}
}
