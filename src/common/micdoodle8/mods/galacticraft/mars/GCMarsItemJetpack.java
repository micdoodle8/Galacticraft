package micdoodle8.mods.galacticraft.mars;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumArmorMaterial;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

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
    		EntityPlayer player = (EntityPlayer) entity;
    		
    		if (GalacticraftCore.instance.tick % 100 == 0)
    		{
    			if (!player.capabilities.isCreativeMode)
    			{
        			player.inventory.consumeInventoryItem(Item.coal.shiftedIndex);
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
