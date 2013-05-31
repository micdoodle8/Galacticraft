package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemPickaxe extends ItemPickaxe
{
    private final EnumToolMaterial material;

    public GCMarsItemPickaxe(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        super(par1, par2EnumToolMaterial);
        this.material = par2EnumToolMaterial;
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        if (this.material == GCMarsItems.TOOLQUANDRIUM)
        {
            return EnumRarity.epic;
        }

        return EnumRarity.rare;
    }
}
