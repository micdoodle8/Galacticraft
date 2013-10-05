package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreItemBlockBase extends ItemBlock
{
    public GCCoreItemBlockBase(int i)
    {
        super(i);
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
        case 0:
        {
            name = "copperblock";
            break;
        }
        case 1:
        {
            name = "aluminiumblock";
            break;
        }
        case 2:
        {
            name = "titaniumblock";
            break;
        }
        case 3:
        {
            name = "decoblock1";
            break;
        }
        case 4:
        {
            name = "decoblock2";
            break;
        }
        case 5:
        {
            name = "oreCopper";
            break;
        }
        case 6:
        {
            name = "oreTin";
            break;
        }
        case 7:
        {
            name = "oreAluminum";
            break;
        }
        case 8:
        {
            name = "oreSilicon";
            break;
        }
        default:
            name = "null";
        }

        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + "." + name;
    }

    @Override
    public String getUnlocalizedName()
    {
        return Block.blocksList[this.getBlockID()].getUnlocalizedName() + ".0";
    }
}
