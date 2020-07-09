package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemArmorMars extends ArmorItem implements ISortableItem
{
//    private final ArmorMaterial material;

    public ItemArmorMars(EquipmentSlotType slotType, Item.Properties properties)
    {
        super(EnumArmorMars.ARMOR_DESH, slotType, properties);
//        this.material = par2EnumArmorMaterial;
    }

    /*@Override
    public Item setUnlocalizedName(String par1Str)
    {
//        super.setTextureName(par1Str);
        super.setUnlocalizedName(par1Str);
        return this;
    }*/

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        if (this.getArmorMaterial() == EnumArmorMars.ARMOR_DESH)
        {
            if (stack.getItem() == MarsItems.deshHelmet)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/desh_1.png";
            }
            else if (stack.getItem() == MarsItems.deshChestplate || stack.getItem() == MarsItems.deshBoots)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/desh_2.png";
            }
            else if (stack.getItem() == MarsItems.deshLeggings)
            {
                return GalacticraftPlanets.TEXTURE_PREFIX + "textures/model/armor/desh_3.png";
            }
        }

        return null;
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack stack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.ARMOR;
    }
    
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == MarsItems.ingotDesh;
    }
}
