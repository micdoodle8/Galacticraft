package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFood extends net.minecraft.item.ItemFood implements ISortableItem
{
    public static final String[] names = { "dehydrated_apple", "dehydrated_carrot", "dehydrated_melon", "dehydrated_potato", "cheese_slice", "burger_bun", "beef_patty_raw", "beef_patty_cooked", "cheeseburger", "canned_beef" };

    public ItemFood(String assetName)
    {
        super(2, 0.3F, false);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
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
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() < 4)
        {
            return "item.basic_item.canned_food";
        }
        else
        {
            return "item.food." + ItemFood.names[itemStack.getItemDamage()];
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            for (int i = 0; i < 4; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
            list.add(new ItemStack(this, 1, 9));
            for (int i = 4; i < 9; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (par1ItemStack.getItemDamage() < 4)
        {
            tooltip.add(EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("item.basic_item." + ItemFood.names[par1ItemStack.getItemDamage()] + ".name"));
        }
        else if (par1ItemStack.getItemDamage() == 8)
        {
            tooltip.add(EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("item.food.cheeseburger.desc"));
        }
    }

    @Override
    public int getHealAmount(ItemStack par1ItemStack)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 0:
            return 8;
        case 1:
            return 8;
        case 2:
            return 4;
        case 3:
            return 2;
        case 4:
            return 2;
        case 5:
            return 4;
        case 6:
            return 2;
        case 7:
            return 4;
        case 8:
            return 14;
        case 9:
            return 8;
        default:
            return 0;
        }
    }

    @Override
    public float getSaturationModifier(ItemStack par1ItemStack)
    {
        switch (par1ItemStack.getItemDamage())
        {
        case 0:
            return 0.3F;
        case 1:
            return 0.6F;
        case 2:
            return 0.3F;
        case 3:
            return 0.3F;
        case 4:
            return 0.1F;
        case 5:
            return 0.8F;
        case 6:
            return 0.3F;
        case 7:
            return 0.6F;
        case 8:
            return 1.0F;
        case 9:
            return 0.6F;
        default:
            return 0.0F;
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            ((EntityPlayer) entityLiving).getFoodStats().addStats(this, stack);
        }
        worldIn.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
        if (!worldIn.isRemote && (stack.getItemDamage() < 4 || stack.getItemDamage() == 9))
        {
            entityLiving.entityDropItem(new ItemStack(GCItems.canister, 1, 0), 0.0F);
        }
        stack.shrink(1);
        return stack;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
