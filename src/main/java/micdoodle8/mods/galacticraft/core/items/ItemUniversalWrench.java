package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import net.minecraft.block.BlockState;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ItemUniversalWrench extends Item implements ISortable
{
    public ItemUniversalWrench(Item.Properties properties)
    {
        super(properties);
//        this.setUnlocalizedName(assetName);
//        this.setMaxStackSize(1);
//        this.setMaxDamage(256);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

//    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
//    public boolean canWrench(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace)
//    {
//        return true;
//    }
//
//    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
//    public void wrenchUsed(PlayerEntity player, Hand hand, ItemStack wrench, RayTraceResult rayTrace)
//    {
//        ItemStack stack = player.inventory.getCurrentItem();
//
//        if (!stack.isEmpty())
//        {
//            stack.damageItem(1, player);
//
//            if (stack.getDamage() >= stack.getMaxDamage())
//            {
//                stack.shrink(1);
//            }
//
//            if (stack.getCount() <= 0)
//            {
//                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
//            }
//        }
//    }

    public void wrenchUsed(PlayerEntity entityPlayer, BlockPos pos)
    {

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

//    @Override
//    public boolean doesSneakBypassUse(ItemStack stack, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return true;
//    }


    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, PlayerEntity player)
    {
        return true;
    }

    @Override
    public void onCreated(ItemStack stack, World world, PlayerEntity player)
    {
        if (world.isRemote && player instanceof ClientPlayerEntity)
        {
            ClientProxyCore.playerClientHandler.onBuild(3, (ClientPlayerEntity) player);
        }
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stackIn, ItemUseContext context)
    {
        if (context.getWorld().isRemote || context.getPlayer().isSneaking())
        {
            return ActionResultType.PASS;
        }

        BlockState state = context.getWorld().getBlockState(context.getPos());

        if (state.getBlock() instanceof BlockAdvanced)
        {
            if (((BlockAdvanced) state.getBlock()).onUseWrench(context.getWorld(), context.getPos(), context.getPlayer(), context.getHand(), context.getItem(), new BlockRayTraceResult(context.getHitVec(), context.getFace(), context.getPos(), context.isInside())) == ActionResultType.SUCCESS)
            {
                return ActionResultType.SUCCESS;
            }
        }

        for (IProperty<?> entry : state.getProperties())
        {
            if (entry instanceof DirectionProperty && entry.getName().equals("facing") && state.get(entry) instanceof Direction)
            {
                DirectionProperty property = (DirectionProperty) entry;
                Collection<Direction> values = property.getAllowedValues();
                if (values.size() > 0)
                {
                    boolean done = false;
                    Direction currentFacing = state.get(property);

                    // Special case: horizontal facings should be rotated around the Y axis - this includes most of GC's own blocks
                    if (values.size() == 4 && !values.contains(Direction.UP) && !values.contains(Direction.DOWN))
                    {
                        Direction newFacing = currentFacing.rotateY();
                        if (values.contains(newFacing))
                        {
                            context.getWorld().setBlockState(context.getPos(), state.with(property, newFacing));
                            done = true;
                        }
                    }
                    if (!done)
                    {
                        // General case: rotation will follow the order in FACING (may be a bit jumpy)
                        List<Direction> list = Arrays.asList(values.toArray(new Direction[0]));
                        int i = list.indexOf(currentFacing) + 1;
                        Direction newFacing = list.get(i >= list.size() ? 0 : i);
                        context.getWorld().setBlockState(context.getPos(), state.with(property, newFacing));
                    }

                    ItemStack stack = context.getPlayer().getHeldItem(context.getHand()).copy();
                    stack.damageItem(1, context.getPlayer(), (entity) ->
                    {
                    });
                    context.getPlayer().setHeldItem(context.getHand(), stack);

                    TileEntity tile = context.getWorld().getTileEntity(context.getPos());
//                    if (tile instanceof TileBaseUniversalElectrical)
//                        ((TileBaseUniversalElectrical) tile).updateFacing(); TODO Ic2 support

                    return ActionResultType.SUCCESS;
                }
                return ActionResultType.PASS;
            }
        }

        return ActionResultType.PASS;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.TOOLS;
    }
}
