package micdoodle8.mods.galacticraft.core.items;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemBuggyMaterial extends Item
{
    public static final String[] names =
    { "wheel", // 0
    "seat", // 1
    "storage" }; // 2

    protected Icon[] icons = new Icon[256];

    public GCCoreItemBuggyMaterial(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        int i = 0;

        for (final String name : GCCoreItemBuggyMaterial.names)
        {
            this.icons[i++] = iconRegister.registerIcon("galacticraftcore:buggy_" + name + GalacticraftCore.TEXTURE_SUFFIX);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return "item." + "buggymat." + GCCoreItemBuggyMaterial.names[itemStack.getItemDamage()];
    }

    @Override
    public Icon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }

    @Override
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < GCCoreItemBuggyMaterial.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
}
