package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemEmergencyKit extends ItemDesc implements ISortableItem
{
    private static final int SIZE = 9;

    public ItemEmergencyKit(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            
            for (int i = 0; i < SIZE; i++)
            {
                ItemStack newGear = getContents(i);
                if (newGear.getItem() instanceof ISortableItem && ((ISortableItem)newGear.getItem()).getCategory(newGear.getItemDamage()) == EnumSortCategoryItem.GEAR)
                {
                    newGear = newGear.getItem().onItemRightClick(newGear, world, player);
                }
                if (newGear.stackSize >= 1)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, newGear, 0);
                }
            }

            itemStack.stackSize = 0;
        }
        return itemStack;
    }
    
    public static ItemStack getContents(int slot)
    {
        switch (slot)
        {
        case 0: return new ItemStack(GCItems.oxMask);
        case 1: return new ItemStack(GCItems.oxygenGear);
        case 2: return new ItemStack(GCItems.oxTankLight);
        case 3: return new ItemStack(GCItems.oxTankLight);
        case 4: return new ItemStack(GCItems.steelPickaxe);
        case 5: return new ItemStack(GCItems.foodItem, 1, 3);
        case 6: return new ItemStack(Items.potionitem, 1, 8197);
        case 7: return new ItemStack(Items.potionitem, 1, 8262);
        case 8: return new ItemStack(GCItems.parachute, 1, 13);
        default: return null;
        }
    }

    public static Object[] getRecipe()
    {
        Object[] result = new Object[]{ "EAB", "CID", "FGH", 'A', null, 'B', null, 'C', null, 'D', null, 'E', null, 'F', null, 'G', null, 'H', null, 'I', null };
        for (int i = 0; i < SIZE; i++)
        {
            result [i * 2 + 4] = getContents(i);
        }        
        return result;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate("item.emergency_kit.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}