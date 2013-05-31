package micdoodle8.mods.galacticraft.core.items;

import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemOxygenTank extends Item
{
    protected Icon[] icons = new Icon[256];

    public GCCoreItemOxygenTank(int par1)
    {
        super(par1);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, this.getMaxDamage()));
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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List par2List, boolean b)
    {
        par2List.add("- Air Remaining: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        this.icons[0] = iconRegister.registerIcon("galacticraftcore:oxygen_tank_light" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[1] = iconRegister.registerIcon("galacticraftcore:oxygen_tank_medium" + GalacticraftCore.TEXTURE_SUFFIX);
        this.icons[2] = iconRegister.registerIcon("galacticraftcore:oxygen_tank_heavy" + GalacticraftCore.TEXTURE_SUFFIX);
        this.itemIcon = iconRegister.registerIcon("galacticraftcore:extractor_1" + GalacticraftCore.TEXTURE_SUFFIX);
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        if (this.itemID == GCCoreItems.heavyOxygenTank.itemID)
        {
            return this.icons[2];
        }

        if (this.itemID == GCCoreItems.medOxygenTank.itemID)
        {
            return this.icons[1];
        }

        if (this.itemID == GCCoreItems.lightOxygenTank.itemID)
        {
            return this.icons[0];
        }

        return this.icons[0];
    }
}
