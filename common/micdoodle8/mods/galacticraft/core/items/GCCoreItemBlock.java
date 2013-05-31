package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemBlock extends ItemBlock
{
    public GCCoreItemBlock(int par1)
    {
        super(par1);
    }

    @Override
	@SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }
}
