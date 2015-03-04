package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAsteroids extends ItemBlock
{
    public ItemBlockAsteroids(Block block)
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
        case 0:
        {
            name = "asteroid0";
            break;
        }
        case 1:
        {
            name = "asteroid1";
            break;
        }
        case 2:
        {
            name = "asteroid2";
            break;
        }
        case 3:
        {
            name = "oreAluminum";
            break;
        }
        case 4:
        {
            name = "oreIlmenite";
            break;
        }
        case 5:
        {
            name = "oreIron";
            break;
        }
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
