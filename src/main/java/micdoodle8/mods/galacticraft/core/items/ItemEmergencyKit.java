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
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);
        if (player instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get(player);
            
            for (int i = 0; i < SIZE; i++)
            {
                ItemStack newGear = getContents(i);
                if (newGear.getItem() instanceof IClickableItem)
                {
                    newGear = ((IClickableItem)newGear.getItem()).onItemRightClick(newGear, worldIn, player);
                }
                if (newGear.getCount() >= 1)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, newGear, 0);
                }
            }

            itemStack.setCount(0);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
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
        case 6: return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.HEALING);
        case 7: return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.LONG_NIGHT_VISION);
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