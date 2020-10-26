package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.api.item.IKeyItem;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemKeyVenus extends Item implements IKeyItem, ISortable
{
    public ItemKeyVenus(Item.Properties properties)
    {
        super(properties);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.setUnlocalizedName(name);
    }

////    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return "item.key.t3";
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1));
//        }
//    }

//    @Override
//    public int getMetadata(int par1)
//    {
//        return par1;
//    }

    @Override
    public int getTier(ItemStack keyStack)
    {
        return 3;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.KEYS;
    }
}
