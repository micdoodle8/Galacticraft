package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GCCoreItemWrench extends Item
{
    public GCCoreItemWrench(int par1)
    {
		super(par1);
        this.setMaxDamage(64);
        this.maxStackSize = 1;
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
	}

	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int x, int y, int z, int par7, float par8, float par9, float par10)
    {
        if (par7 == 0)
        {
            --y;
        }

        if (par7 == 1)
        {
            ++y;
        }

        if (par7 == 2)
        {
            --z;
        }

        if (par7 == 3)
        {
            ++z;
        }

        if (par7 == 4)
        {
            --x;
        }

        if (par7 == 5)
        {
            ++x;
        }
        
        if (!par2EntityPlayer.canPlayerEdit(x, y, z, par7, par1ItemStack))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
