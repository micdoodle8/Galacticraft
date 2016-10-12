package micdoodle8.mods.galacticraft.planets.mars.items;

import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemSchematicTier2 extends ItemSchematic implements ISchematicItem, ISortableItem
{
    public ItemSchematicTier2()
    {
        super("schematic");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < 3; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (par2EntityPlayer.worldObj.isRemote)
        {
            switch (par1ItemStack.getItemDamage())
            {
            case 0:
                tooltip.add(GCCoreUtil.translate("schematic.rocket_t3.name"));
                break;
            case 1:
                tooltip.add(GCCoreUtil.translate("schematic.cargo_rocket.name"));
                break;
            case 2:
                tooltip.add(GCCoreUtil.translate("schematic.astro_miner.name"));
                break;
            }
        }
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.SCHEMATIC;
    }
}
