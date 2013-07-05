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

public class GCCoreItemOilCanister extends Item
{
    protected Icon[] icons = new Icon[256];

    public static final String[] names = { "liquidcan_oil_6", // 0
    "liquidcan_oil_5", // 0
    "liquidcan_oil_4", // 1
    "liquidcan_oil_3", // 2
    "liquidcan_oil_2", // 3
    "liquidcan_oil_1", // 4
    "liquidcan_empty" }; // 5

    public GCCoreItemOilCanister(int par1)
    {
        super(par1);
        this.setMaxDamage(61);
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        int i = 0;

        for (final String name : GCCoreItemOilCanister.names)
        {
            this.icons[i++] = iconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "" + name);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getMaxDamage() - itemStack.getItemDamage() == 0)
        {
            return "item.emptyLiquidCanister";
        }

        if (itemStack.getItemDamage() == 1)
        {
            return "item.oilCanister";
        }

        return "item.oilCanisterPartial";
    }

    @Override
    public Icon getIconFromDamage(int par1)
    {
        final int damage = (int) Math.floor(par1 / 10);

        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 61));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
        {
            par3List.add("Oil: " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
        }
    }
}
