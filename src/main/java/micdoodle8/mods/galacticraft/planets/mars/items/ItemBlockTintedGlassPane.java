package micdoodle8.mods.galacticraft.planets.mars.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class ItemBlockTintedGlassPane extends ItemBlock
{
    public ItemBlockTintedGlassPane(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        return this.field_150939_a.getIcon(0, par1);
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return this.getBlock().getUnlocalizedName() + "." + ItemDye.dyeColors[~itemstack.getItemDamage() & 15];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.getBlock().getUnlocalizedName() + ".0";
    }
}
