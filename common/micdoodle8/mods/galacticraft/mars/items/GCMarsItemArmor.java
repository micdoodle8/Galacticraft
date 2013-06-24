package micdoodle8.mods.galacticraft.mars.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IArmorTextureProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsItemArmor extends ItemArmor implements IArmorTextureProvider
{
    public boolean attachedMask;
    private final EnumArmorMaterial material;

    public GCMarsItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4, boolean breathable)
    {
        super(par1, par2EnumArmorMaterial, par3, par4);
        this.material = par2EnumArmorMaterial;
        this.attachedMask = breathable;
    }

    @Override
    public String getArmorTextureFile(ItemStack itemstack)
    {
        if (this.material == GCMarsItems.ARMORDESH)
        {
            if (itemstack.getItem().itemID == GCMarsItems.deshHelmet.itemID)
            {
                return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_1.png";
            }
            else if (itemstack.getItem().itemID == GCMarsItems.deshChestplate.itemID || itemstack.getItem().itemID == GCMarsItems.deshBoots.itemID)
            {
                return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_2.png";
            }
            else if (itemstack.getItem().itemID == GCMarsItems.deshLeggings.itemID)
            {
                return "/micdoodle8/mods/galacticraft/mars/client/armor/desh_3.png";
            }
        }

        return null;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon(this.getUnlocalizedName().replace("item.", "galacticraftmars:"));
    }
}
