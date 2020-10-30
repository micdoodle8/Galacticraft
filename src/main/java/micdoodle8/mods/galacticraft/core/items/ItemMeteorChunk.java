package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteorChunk;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMeteorChunk extends Item implements ISortable
{
    public static final int METEOR_BURN_TIME = 45 * 20;

    public ItemMeteorChunk(Item.Properties properties)
    {
        super(properties);
//        this.setMaxDamage(0);
//        this.setHasSubtypes(true);
//        this.maxStackSize = 16;
//        this.setCreativeTab(ItemGroup.MATERIALS);
//        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

//    @Override
//    public ItemGroup getCreativeTab()
//    {
//        return GalacticraftCore.galacticraftItemsTab;
//    }


    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if (entityIn instanceof PlayerEntity && stack.getItem() == GCItems.meteorChunkHot && !world.isRemote)
        {
            if (stack.hasTag())
            {
                float meteorBurnTime = stack.getTag().getFloat("MeteorBurnTimeF");

                if (meteorBurnTime >= 0.5F)
                {
                    meteorBurnTime -= 0.5F;
                    stack.getTag().putFloat("MeteorBurnTimeF", meteorBurnTime);
                }
                else
                {
                    ((PlayerEntity) entityIn).inventory.setInventorySlotContents(itemSlot, new ItemStack(GCItems.meteorChunk, stack.getCount()));
//                    stack.setItemDamage(0);
                    stack.setTag(null);
                }
            }
            else
            {
                stack.getOrCreateTag().putFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
            }
        }
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity entityPlayer)
    {
        super.onCreated(stack, world, entityPlayer);

        if (stack.getItem() == GCItems.meteorChunkHot)
        {
            stack.getOrCreateTag().putFloat("MeteorBurnTimeF", ItemMeteorChunk.METEOR_BURN_TIME);
        }
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubItems(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        if (tab == GalacticraftCore.galacticraftItemsTab || tab == ItemGroup.SEARCH)
//        {
//            list.add(new ItemStack(this, 1, 0));
//            list.add(new ItemStack(this, 1, 1));
//        }
//    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (stack.getItem() == GCItems.meteorChunkHot)
        {
            float burnTime = 0.0F;

            if (stack.hasTag())
            {
                float meteorBurnTime = stack.getTag().getFloat("MeteorBurnTimeF");
                burnTime = Math.round(meteorBurnTime / 10.0F) / 2.0F;
            }
            else
            {
                burnTime = 45.0F;
            }

            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.hot_description") + " " + burnTime + GCCoreUtil.translate("gui.seconds")));
        }
    }

    @Override
    public Rarity getRarity(ItemStack par1ItemStack)
    {
        return ClientProxyCore.galacticraftItem;
    }

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        return "item." + ItemMeteorChunk.names[itemStack.getDamage()];
//    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (!player.abilities.isCreativeMode)
        {
            itemStack.shrink(1);
        }

        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 0.0001F / (Item.random.nextFloat() * 0.1F));

        if (!world.isRemote)
        {
            EntityMeteorChunk meteor = new EntityMeteorChunk(world, player, 1.0F);

            if (itemStack.getItem() == GCItems.meteorChunkHot)
            {
                meteor.setFire(20);
                meteor.isHot = true;
            }

            meteor.canBePickedUp = player.abilities.isCreativeMode ? 2 : 1;
            world.addEntity(meteor);
        }

        player.swingArm(hand);

        return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.GENERAL;
    }
}
