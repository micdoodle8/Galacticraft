package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import javax.annotation.Nullable;

public class ItemBuggy extends Item implements IHoldableItem, ISortableItem
{
    public ItemBuggy(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
        //this.setTextureName("arrow");
        this.setMaxStackSize(1);
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (tab == GalacticraftCore.galacticraftItemsTab || tab == CreativeTabs.SEARCH)
        {
            for (int i = 0; i < 4; i++)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        ItemStack itemstack = playerIn.getHeldItem(hand);
        final float var4 = 1.0F;
        final float var5 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * var4;
        final float var6 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * var4;
        final double var7 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * var4;
        final double var9 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * var4 + 1.62D - playerIn.getYOffset();
        final double var11 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * var4;
        final Vec3d var13 = new Vec3d(var7, var9, var11);
        final float var14 = MathHelper.cos(-var6 / Constants.RADIANS_TO_DEGREES - (float) Math.PI);
        final float var15 = MathHelper.sin(-var6 / Constants.RADIANS_TO_DEGREES - (float) Math.PI);
        final float var16 = -MathHelper.cos(-var5 / Constants.RADIANS_TO_DEGREES);
        final float var17 = MathHelper.sin(-var5 / Constants.RADIANS_TO_DEGREES);
        final float var18 = var15 * var16;
        final float var20 = var14 * var16;
        final double var21 = 5.0D;
        final Vec3d var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
        final RayTraceResult var24 = worldIn.rayTraceBlocks(var13, var23, true);

        if (var24 == null)
        {
            return new ActionResult<>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            final Vec3d var25 = playerIn.getLook(var4);
            boolean var26 = false;
            final float var27 = 1.0F;
            final List<?> var28 = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getEntityBoundingBox().grow(var25.x * var21, var25.y * var21, var25.z * var21).expand(var27, var27, var27));
            int var29;

            for (var29 = 0; var29 < var28.size(); ++var29)
            {
                final Entity var30 = (Entity) var28.get(var29);

                if (var30.canBeCollidedWith())
                {
                    final float var31 = var30.getCollisionBorderSize();
                    final AxisAlignedBB var32 = var30.getEntityBoundingBox().expand(var31, var31, var31);

                    if (var32.contains(var13))
                    {
                        var26 = true;
                    }
                }
            }

            if (var26)
            {
                return new ActionResult<>(EnumActionResult.PASS, itemstack);
            }
            else
            {
                if (var24.typeOfHit == RayTraceResult.Type.BLOCK)
                {
                    var29 = var24.getBlockPos().getX();
                    int var33 = var24.getBlockPos().getY();
                    final int var34 = var24.getBlockPos().getZ();

                    if (worldIn.getBlockState(new BlockPos(var29, var33, var34)).getBlock() == Blocks.SNOW)
                    {
                        --var33;
                    }

                    final EntityBuggy var35 = new EntityBuggy(worldIn, var29 + 0.5F, var33 + 1.0F, var34 + 0.5F, itemstack.getItemDamage());

                    if (!worldIn.getCollisionBoxes(var35, var35.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
                    {
                        return new ActionResult<>(EnumActionResult.PASS, itemstack);
                    }

                    if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BuggyFuel"))
                    {
                        var35.buggyFuelTank.setFluid(new FluidStack(GCFluids.fluidFuel, itemstack.getTagCompound().getInteger("BuggyFuel")));
                    }

                    if (!worldIn.isRemote)
                    {
                        worldIn.spawnEntity(var35);
                    }

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                    }
                }

                return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (par1ItemStack.getItemDamage() != 0)
        {
            tooltip.add(GCCoreUtil.translate("gui.buggy.storage_space") + ": " + par1ItemStack.getItemDamage() * 18);
        }

        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("BuggyFuel"))
        {
            tooltip.add(GCCoreUtil.translate("gui.message.fuel.name") + ": " + par1ItemStack.getTagCompound().getInteger("BuggyFuel") + " / " + EntityBuggy.tankCapacity);
        }
    }

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
    public boolean shouldCrouch(EntityPlayer player)
    {
        return true;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}
