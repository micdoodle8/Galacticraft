package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlag extends Item implements IHoldableItemCustom, ISortableItem
{
    public int placeProgress;

    public ItemFlag(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return GalacticraftCore.galacticraftItemsTab;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft)
    {
        final int useTime = this.getMaxItemUseDuration(stack) - timeLeft;

        boolean placed = false;

        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        final RayTraceResult var12 = this.rayTrace(worldIn, player, true);

        float var7 = useTime / 20.0F;
        var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }

        if (var7 == 1.0F && var12 != null && var12.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            final BlockPos pos = var12.getBlockPos();

            if (!worldIn.isRemote)
            {
                final EntityFlag flag = new EntityFlag(worldIn, pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F, (int) (entity.rotationYaw - 90));

                if (worldIn.getEntitiesWithinAABB(EntityFlag.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1)).isEmpty())
                {
                    worldIn.spawnEntity(flag);
                    flag.setType(stack.getItemDamage());
                    flag.setOwner(PlayerUtil.getName(player));
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundType.METAL.getBreakSound(), SoundCategory.BLOCKS, SoundType.METAL.getVolume(), SoundType.METAL.getPitch() + 2.0F);
                    placed = true;
                }
                else
                {
                    entity.sendMessage(new TextComponentString(GCCoreUtil.translate("gui.flag.already_placed")));
                }
            }

            if (placed)
            {
                final int var2 = this.getInventorySlotContainItem(player, stack);

                if (var2 >= 0 && !player.capabilities.isCreativeMode)
                {
                    player.inventory.mainInventory.get(var2).shrink(1);
                }
            }
        }
    }

    private int getInventorySlotContainItem(EntityPlayer player, ItemStack stack)
    {
        for (int var2 = 0; var2 < player.inventory.mainInventory.size(); ++var2)
        {
            if (!player.inventory.mainInventory.get(var2).isEmpty() && player.inventory.mainInventory.get(var2).isItemEqual(stack))
            {
                return var2;
            }
        }

        return -1;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
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
        return "item.flag";
    }

    /*@Override
    public IIcon getIconFromDamage(int damage)
    {
        return super.getIconFromDamage(damage);
    }*/

    @Override
    public boolean shouldHoldLeftHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public Vector3 getLeftHandRotation(EntityPlayer player)
    {
        return new Vector3((float) Math.PI + 1.3F, 0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public Vector3 getRightHandRotation(EntityPlayer player)
    {
        return new Vector3((float) Math.PI + 1.3F, -0.5F, (float) Math.PI / 5.0F);
    }

    @Override
    public boolean shouldCrouch(EntityPlayer player)
    {
        return false;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
