package micdoodle8.mods.galacticraft.core.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlockDesc
{
    public ItemBlockBase(Block block)
    {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
        case 3:
            name = "decoblock1";
            break;
        case 4:
            name = "decoblock2";
            break;
        case 5:
            name = "oreCopper";
            break;
        case 6:
            name = "oreTin";
            break;
        case 7:
            name = "oreAluminum";
            break;
        case 8:
            name = "oreSilicon";
            break;
        case 9:
            name = "copperBlock";
            break;
        case 10:
            name = "tinBlock";
            break;
        case 11:
            name = "blockAluminum";
            break;
        case 12:
            name = "meteorironBlock";
            break;
        default:
            name = "null";
        }

        return this.field_150939_a.getUnlocalizedName() + "." + name;
    }

    @Override
    public String getUnlocalizedName()
    {
        return this.field_150939_a.getUnlocalizedName() + ".0";
    }
}
