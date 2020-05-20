package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorAsteroids extends ArmorItem implements ISortableItem
{
    public ItemArmorAsteroids(EquipmentSlotType armorIndex, String assetSuffix)
    {
        super(AsteroidsItems.ARMOR_TITANIUM, 0, armorIndex);
        this.setUnlocalizedName("titanium_" + assetSuffix);
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + "titanium_" + assetSuffix);
    }

    @Override
    public ItemGroup getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        if (this.getArmorMaterial() == AsteroidsItems.ARMOR_TITANIUM)
        {
            if (stack.getItem() == AsteroidsItems.titaniumHelmet)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/titanium_1.png";
            }
            else if (stack.getItem() == AsteroidsItems.titaniumChestplate || stack.getItem() == AsteroidsItems.titaniumBoots)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/titanium_2.png";
            }
            else if (stack.getItem() == AsteroidsItems.titaniumLeggings)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/titanium_3.png";
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
        return repair.getItem() == AsteroidsItems.basicItem && repair.getItemDamage() == 6;
    }
}
