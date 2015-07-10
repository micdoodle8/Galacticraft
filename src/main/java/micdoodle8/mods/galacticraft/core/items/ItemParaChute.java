package micdoodle8.mods.galacticraft.core.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemParaChute extends Item
{
    public static final String[] names = { "plain", // 0
            "black", // 1
            "blue", // 2
            "lime", // 3
            "brown", // 4
            "darkblue", // 5
            "darkgray", // 6
            "darkgreen", // 7
            "gray", // 8
            "magenta", // 9
            "orange", // 10
            "pink", // 11
            "purple", // 12
            "red", // 13
            "teal", // 14
            "yellow" }; // 15

//    protected IIcon[] icons;

    public ItemParaChute(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
        //this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < ItemParaChute.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        int i = 0;
        this.icons = new IIcon[ItemParaChute.names.length];

        for (String name : ItemParaChute.names)
        {
            this.icons[i++] = iconRegister.registerIcon(this.getIconString() + "_" + name);
        }
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName() + "_" + ItemParaChute.names[itemStack.getItemDamage()];
    }

    /*@Override
    public IIcon getIconFromDamage(int damage)
    {
        if (this.icons.length > damage)
        {
            return this.icons[damage];
        }

        return super.getIconFromDamage(damage);
    }*/

    // @Override
    // @SideOnly(Side.CLIENT)
    // public int getIconFromDamage(int par1)
    // {
    // switch (par1)
    // {
    // case 0: // plain
    // return 49;
    // case 1: // black
    // return 34;
    // case 2: // blue
    // return 46;
    // case 3: // green
    // return 44;
    // case 4: // brown
    // return 37;
    // case 5: // dark blue
    // return 38;
    // case 6: // dark gray
    // return 42;
    // case 7: // dark green
    // return 36;
    // case 8: // gray
    // return 41;
    // case 9: // magenta
    // return 47;
    // case 10: // orange
    // return 48;
    // case 11: // pink
    // return 43;
    // case 12: // purple
    // return 39;
    // case 13: // red
    // return 35;
    // case 14: // teal
    // return 40;
    // case 15: // yellow
    // return 45;
    // }
    //
    // return 0;
    // }

    public static int getParachuteDamageValueFromDye(int meta)
    {
        switch (meta)
        {
        case 0:
            return 1;
        case 1:
            return 13;
        case 2:
            return 7;
        case 3:
            return 4;
        case 4:
            return 5;
        case 5:
            return 12;
        case 6:
            return 14;
        case 7:
            return 8;
        case 8:
            return 6;
        case 9:
            return 11;
        case 10:
            return 3;
        case 11:
            return 15;
        case 12:
            return 2;
        case 13:
            return 9;
        case 14:
            return 10;
        case 15:
            return 0;
        }

        return -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }
}
