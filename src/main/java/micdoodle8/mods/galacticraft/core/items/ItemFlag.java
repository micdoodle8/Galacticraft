package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
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
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        final int useTime = this.getMaxItemUseDuration(par1ItemStack) - par4;

        boolean placed = false;

        final MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);

        float var7 = useTime / 20.0F;
        var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

        if (var7 > 1.0F)
        {
            var7 = 1.0F;
        }

        if (var7 == 1.0F && var12 != null && var12.typeOfHit == MovingObjectType.BLOCK)
        {
            final BlockPos pos = var12.getBlockPos();

            if (!par2World.isRemote)
            {
                final EntityFlag flag = new EntityFlag(par2World, pos.getX() + 0.5F, pos.getY() + 1.0F, pos.getZ() + 0.5F, (int) (par3EntityPlayer.rotationYaw - 90));

                if (par2World.getEntitiesWithinAABB(EntityFlag.class, AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 3, pos.getZ() + 1)).isEmpty())
                {
                    par2World.spawnEntityInWorld(flag);
                    flag.setType(par1ItemStack.getItemDamage());
                    flag.setOwner(PlayerUtil.getName(par3EntityPlayer));
                    par2World.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), Block.soundTypeMetal.getBreakSound(), Block.soundTypeMetal.getVolume(), Block.soundTypeMetal.getFrequency() + 2.0F);
                    placed = true;
                }
                else
                {
                    par3EntityPlayer.addChatMessage(new ChatComponentText(GCCoreUtil.translate("gui.flag.already_placed")));
                }
            }

            if (placed)
            {
                final int var2 = this.getInventorySlotContainItem(par3EntityPlayer, par1ItemStack);

                if (var2 >= 0 && !par3EntityPlayer.capabilities.isCreativeMode)
                {
                    if (--par3EntityPlayer.inventory.mainInventory[var2].stackSize <= 0)
                    {
                        par3EntityPlayer.inventory.mainInventory[var2] = null;
                    }
                }
            }
        }
    }

    private int getInventorySlotContainItem(EntityPlayer player, ItemStack stack)
    {
        for (int var2 = 0; var2 < player.inventory.mainInventory.length; ++var2)
        {
            if (player.inventory.mainInventory[var2] != null && player.inventory.mainInventory[var2].isItemEqual(stack))
            {
                return var2;
            }
        }

        return -1;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        return stack;
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
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));

        return par1ItemStack;
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
