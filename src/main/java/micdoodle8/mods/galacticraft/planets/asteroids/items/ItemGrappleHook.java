package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityGrapple;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemGrappleHook extends BowItem implements ISortableItem
{
    private static NonNullList<ItemStack> stringEntries = null;

    public ItemGrappleHook(Item.Properties properties)
    {
        super(properties);
        this.setUnlocalizedName(assetName);
        this.setMaxStackSize(1);
        //this.setTextureName("arrow");
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entity, int timeLeft)
    {
        if (!(entity instanceof PlayerEntity))
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        boolean canShoot = player.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack string = ItemStack.EMPTY;

        if (stringEntries == null) stringEntries = OreDictionary.getOres("string");

        for (ItemStack itemstack : player.inventory.mainInventory)
        {
            if (!canShoot && OreDictionary.containsMatch(false, stringEntries, itemstack))
            {
                string = itemstack;
                canShoot = true;
            }
        }

        if (canShoot)
        {
            ItemStack pickupString = string == ItemStack.EMPTY ? ItemStack.EMPTY : new ItemStack(string.getItem(), 1, string.getItemDamage(), string.getTagCompound());
            EntityGrapple grapple = new EntityGrapple(worldIn, player, 2.0F, pickupString);

            worldIn.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);

            if (!worldIn.isRemote)
            {
                worldIn.addEntity(grapple);
            }

            stack.damageItem(1, player);
            grapple.canBePickedUp = player.abilities.isCreativeMode ? 2 : 1;

            if (!player.abilities.isCreativeMode)
            {
                string.shrink(1);

                if (string.isEmpty())
                {
                    player.inventory.deleteStack(string);
                }
            }
        }
        else if (worldIn.isRemote)
        {
            player.sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.message.grapple.fail")));
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public UseAction getItemUseAction(ItemStack par1ItemStack)
    {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
