package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemArmorAsteroids extends ItemArmor
{
    public ItemArmorAsteroids(int armorIndex, String assetSuffix)
    {
        super(AsteroidsItems.ARMOR_TITANIUM, GalacticraftCore.proxy.getTitaniumArmorRenderIndex(), armorIndex);
        this.setUnlocalizedName("titanium_" + assetSuffix);
        this.setTextureName(AsteroidsModule.TEXTURE_PREFIX + "titanium_" + assetSuffix);
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
                return AsteroidsModule.TEXTURE_PREFIX + "textures/model/armor/titanium_1.png";
            }
            else if (stack.getItem() == AsteroidsItems.titaniumChestplate || stack.getItem() == AsteroidsItems.titaniumBoots)
            {
                return AsteroidsModule.TEXTURE_PREFIX + "textures/model/armor/titanium_2.png";
            }
            else if (stack.getItem() == AsteroidsItems.titaniumLeggings)
            {
                return AsteroidsModule.TEXTURE_PREFIX + "textures/model/armor/titanium_3.png";
            }
        }

        return null;
    }
}
