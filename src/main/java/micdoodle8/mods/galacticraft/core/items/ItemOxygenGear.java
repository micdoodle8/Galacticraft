package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOxygenGear extends Item implements ISortableItem, IClickableItem
{
    public ItemOxygenGear(String assetName)
    {
        super();
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player instanceof EntityPlayerMP)
        {
            if (itemStack.getItem() instanceof IClickableItem)
            {
                itemStack = ((IClickableItem)itemStack.getItem()).onItemRightClick(itemStack, worldIn, player);
            }

            if (itemStack.isEmpty())
            {
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
    
    public ItemStack onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        ItemStack gear = stats.getExtendedInventory().getStackInSlot(1);

        if (gear.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(1, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }
        
        return itemStack;
    }
}