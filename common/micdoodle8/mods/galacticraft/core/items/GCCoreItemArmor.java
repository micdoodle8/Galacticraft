package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemArmor extends ItemArmor
{
    public boolean attachedMask;
    private final EnumArmorMaterial material;

    public GCCoreItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable)
    {
        super(par1, par2EnumArmorMaterial, par3, par4);
        this.material = par2EnumArmorMaterial;
        this.attachedMask = breathable;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
        if (this.material == GCCoreItems.steelARMOR)
        {
            if (stack.getItem().itemID == GCCoreItems.steelHelmet.itemID)
            {
                return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_1.png";
            }
            else if (stack.getItem().itemID == GCCoreItems.steelChestplate.itemID || stack.getItem().itemID == GCCoreItems.steelBoots.itemID)
            {
                return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_2.png";
            }
            else if (stack.getItem().itemID == GCCoreItems.steelLeggings.itemID)
            {
                return "/micdoodle8/mods/galacticraft/core/client/armor/titanium_3.png";
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftcore:"));
    }
}
