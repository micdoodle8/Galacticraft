package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemOxygenTank extends Item implements ISortableItem, IClickableItem
{
    public ItemOxygenTank(int tier, String assetName)
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(tier * 900);
        this.setUnlocalizedName(assetName);
//        this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
        this.setNoRepair();
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            list.add(new ItemStack(this, 1, 0));
            list.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
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
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(GCCoreUtil.translate("gui.tank.oxygen_remaining") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
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
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player)
    {
        GCPlayerStats stats = GCPlayerStats.get(player);
        ItemStack gear = stats.getExtendedInventory().getStackInSlot(2);
        ItemStack gear1 = stats.getExtendedInventory().getStackInSlot(3);

        if (gear.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(2, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }
        else if (gear1.isEmpty())
        {
            stats.getExtendedInventory().setInventorySlotContents(3, itemStack.copy());
            itemStack = ItemStack.EMPTY;
        }
        
        return itemStack;
    }
}