package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorItemGC extends ArmorItem implements ISortable
{
    public ArmorItemGC(EquipmentSlotType slot, Item.Properties builder)
    {
        super(EnumArmorGC.ARMOR_STEEL, slot, builder);
//        this.setUnlocalizedName("steel_" + assetSuffix);
        //this.setTextureName(Constants.TEXTURE_PREFIX + "steel_" + assetSuffix);
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        if (this.getArmorMaterial() == EnumArmorGC.ARMOR_STEEL)
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
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.ARMOR;
    }

//    @Override
//    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
//    {
//        return repair.getItem() == GCItems.basicItem && repair.getDamage() == 9;
//    }
}
