package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiPreLaunchChecklist;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPreLaunchChecklist extends Item implements ISortableItem
{
    public ItemPreLaunchChecklist(Item.Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
//        this.setMaxStackSize(1);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack item, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (!item.isEmpty() && this == GCItems.heavyPlatingTier1)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.tier1.desc")));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
    {
        ItemStack itemStack = playerIn.getHeldItem(hand);

        if (worldIn.isRemote)
        {
            Minecraft.getInstance().displayGuiScreen(new GuiPreLaunchChecklist(WorldUtil.getAllChecklistKeys(), itemStack.getTag().getCompound("checklistData")));
//            playerIn.openGui(GalacticraftCore.instance, GuiIdsCore.PRE_LAUNCH_CHECKLIST, playerIn.world, (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
        }

        return new ActionResult<>(ActionResultType.PASS, itemStack);
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
