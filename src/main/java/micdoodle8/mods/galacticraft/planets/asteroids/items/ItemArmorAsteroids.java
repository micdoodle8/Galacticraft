package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorAsteroids extends ItemArmor implements ISortableItem
{
    public ItemArmorAsteroids(int armorIndex, String assetSuffix)
    {
        super(AsteroidsItems.ARMOR_TITANIUM, 0, armorIndex);
        this.setUnlocalizedName("titanium_" + assetSuffix);
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + "titanium_" + assetSuffix);
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
