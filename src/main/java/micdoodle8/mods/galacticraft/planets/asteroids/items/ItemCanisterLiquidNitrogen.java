package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.items.ItemCanisterGeneric;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import javax.annotation.Nullable;

public class ItemCanisterLiquidNitrogen extends ItemCanisterGeneric implements ISortable
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemCanisterLiquidNitrogen(Item.Properties builder)
    {
        super(builder);
//        this.setAllowedFluid("liquidnitrogen");
        //this.setTextureName(GalacticraftPlanets.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

//    @Override
//    public String getUnlocalizedName(ItemStack itemStack)
//    {
//        if (itemStack.getMaxDamage() - itemStack.getDamage() == 0)
//        {
//            return "item.empty_gas_canister";
//        }
//
//        if (itemStack.getDamage() == 1)
//        {
//            return "item.canister.liquid_nitrogen.full";
//        }
//
//        return "item.canister.liquid_nitrogen.partial";
//    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getDamage() > 0)
        {
            tooltip.add(new StringTextComponent(GCCoreUtil.translate("item.canister.liquid_nitrogen") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getDamage())));
        }
    }

    private Block canFreeze(Block b)
    {
        if (b == Blocks.WATER)
        {
            return Blocks.ICE;
        }
        if (b == Blocks.LAVA)
        {
            return Blocks.OBSIDIAN;
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand)
    {
        ItemStack itemStack = playerIn.getHeldItem(hand);

        int damage = itemStack.getDamage() + 125;
        if (damage > itemStack.getMaxDamage())
        {
            return new ActionResult<>(ActionResultType.PASS, itemStack);
        }

        RayTraceResult movingobjectposition = Item.rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);

        if (movingobjectposition.getType() == RayTraceResult.Type.MISS)
        {
            return new ActionResult<>(ActionResultType.PASS, itemStack);
        }
        else
        {
            if (movingobjectposition.getType() == RayTraceResult.Type.BLOCK)
            {
                BlockRayTraceResult blockResult = (BlockRayTraceResult) movingobjectposition;
                BlockPos pos = blockResult.getPos();

                if (!worldIn.canMineBlockBody(playerIn, pos))
                {
                    return new ActionResult<>(ActionResultType.PASS, itemStack);
                }

                if (!playerIn.canPlayerEdit(pos, blockResult.getFace(), itemStack))
                {
                    return new ActionResult<>(ActionResultType.PASS, itemStack);
                }

                //Material material = par2World.getBlock(i, j, k).getMaterial();
                BlockState state = worldIn.getBlockState(pos);
                Block b = state.getBlock();
//                int meta = b.getMetaFromState(state);

                Block result = this.canFreeze(b);
                if (result != null)
                {
                    this.setNewDamage(itemStack, damage);
                    worldIn.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.NEUTRAL, 1.0F, Item.random.nextFloat() * 0.4F + 0.8F);
                    worldIn.setBlockState(pos, result.getDefaultState(), 3);
                    return new ActionResult<>(ActionResultType.SUCCESS, itemStack);
                }
            }

            return new ActionResult<>(ActionResultType.PASS, itemStack);
        }
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.CANISTER;
    }
}
