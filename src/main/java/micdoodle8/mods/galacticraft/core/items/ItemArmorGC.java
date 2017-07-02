package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorGC extends ItemArmor implements ISortableItem
{
    public ItemArmorGC(int armorIndex, String assetSuffix)
    {
        super(GCItems.ARMOR_STEEL, 0, armorIndex);
        this.setUnlocalizedName("steel_" + assetSuffix);
        //this.setTextureName(Constants.TEXTURE_PREFIX + "steel_" + assetSuffix);
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
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_1.png";
            }
            else if (stack.getItem() == GCItems.steelChestplate || stack.getItem() == GCItems.steelBoots)
            {
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_2.png";
            }
            else if (stack.getItem() == GCItems.steelLeggings)
            {
                return Constants.TEXTURE_PREFIX + "textures/model/armor/steel_3.png";
            }
        }

        return null;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.ARMOR;
    }
    
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == GCItems.basicItem && repair.getItemDamage() == 9;
    }
}
