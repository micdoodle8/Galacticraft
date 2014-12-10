package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemArmorGC extends ItemArmor
{
    public ItemArmorGC(int armorIndex, String assetSuffix)
    {
        super(GCItems.ARMOR_STEEL, GalacticraftCore.proxy.getTitaniumArmorRenderIndex(), armorIndex);
        this.setUnlocalizedName("steel_" + assetSuffix);
        this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + "steel_" + assetSuffix);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if (this.getArmorMaterial() == GCItems.ARMOR_STEEL)
        {
            if (stack.getItem() == GCItems.steelHelmet)
            {
                return GalacticraftCore.TEXTURE_PREFIX + "textures/model/armor/steel_1.png";
            }
            else if (stack.getItem() == GCItems.steelChestplate || stack.getItem() == GCItems.steelBoots)
            {
                return GalacticraftCore.TEXTURE_PREFIX + "textures/model/armor/steel_2.png";
            }
            else if (stack.getItem() == GCItems.steelLeggings)
            {
                return GalacticraftCore.TEXTURE_PREFIX + "textures/model/armor/steel_3.png";
            }
        }

        return null;
    }
}
