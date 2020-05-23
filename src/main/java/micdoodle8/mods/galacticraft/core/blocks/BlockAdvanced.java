package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.lang.reflect.Method;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockAdvanced extends Block
{
    public BlockAdvanced(Properties builder)
    {
        super(builder);
//        this.setHardness(0.6f);
//        this.setResistance(2.5F);
        //A default blast resistance for GC machines and tiles, similar to a bookshelf
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (hand != Hand.MAIN_HAND)
        {
            return false;
        }

        ItemStack heldItem = playerIn.getHeldItem(hand);

        if (this.useWrench(worldIn, pos, playerIn, hand, heldItem, hit))
        {
            return true;
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, hit))
            {
                return true;
            }
        }

        return this.onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, hit);
    }

    protected boolean useWrench(World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        if (heldItem.getItem() == GCItems.wrench)
        {
            if (playerIn.isSneaking())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, hand, heldItem, hit))
                {
                    playerIn.swingArm(hand);
                    return true;
                }
                return false;
            }

            playerIn.swingArm(hand);
            return true;
        }
        /*
         * Check if the player is holding a wrench or an electric item. If so,
         * call the wrench event.
         */
        if (this.isUsableWrench(playerIn, heldItem, pos))
        {
            this.damageWrench(playerIn, heldItem, pos);

            if (playerIn.isSneaking())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, hand, heldItem, hit))
                {
                    playerIn.swingArm(hand);
                    return true;
                }
            }

            if (this.onUseWrench(worldIn, pos, playerIn, hand, heldItem, hit))
            {
                playerIn.swingArm(hand);
                return true;
            }
        }

        return false;
    }

    /**
     * A function that denotes if an itemStack is a wrench that can be used.
     * Override this for more wrench compatibility. Compatible with Buildcraft
     * and IC2 wrench API via reflection.
     *
     * @return True if it is a wrench.
     */
    public boolean isUsableWrench(PlayerEntity entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (entityPlayer != null && itemStack != null)
        {
            Item item = itemStack.getItem();
            if (item == GCItems.wrench)
                return false;
            
            Class<? extends Item> wrenchClass = item.getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodCanWrench = wrenchClass.getMethod("canWrench", PlayerEntity.class, BlockPos.class);
                return (Boolean) methodCanWrench.invoke(item, entityPlayer, pos);
            }
            catch (NoClassDefFoundError e)
            {
            }
            catch (Exception e)
            {
            }

            if (CompatibilityManager.isIc2Loaded())
            {
            /**
             * Industrialcraft
             */
                if (wrenchClass == CompatibilityManager.classIC2wrench || wrenchClass == CompatibilityManager.classIC2wrenchElectric )
                {
                    return itemStack.getDamage() < itemStack.getMaxDamage();
                }
            }
        }

        return false;
    }

    /**
     * This function damages a wrench. Works with Buildcraft and Industrialcraft
     * wrenches.
     *
     * @return True if damage was successfull.
     */
    public boolean damageWrench(PlayerEntity entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (this.isUsableWrench(entityPlayer, itemStack, pos))
        {
            Class<? extends Item> wrenchClass = itemStack.getItem().getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodWrenchUsed = wrenchClass.getMethod("wrenchUsed", PlayerEntity.class, BlockPos.class);
                methodWrenchUsed.invoke(itemStack.getItem(), entityPlayer, pos);
                return true;
            }
            catch (Exception e)
            {
            }

            /**
             * Industrialcraft
             */
            try
            {
                if (wrenchClass == CompatibilityManager.classIC2wrench || wrenchClass == CompatibilityManager.classIC2wrenchElectric )
                {
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, PlayerEntity.class);
                    methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, entityPlayer);
                    return true;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

    /**
     * Called when the machine is right clicked by the player
     *
     * @return True if something happens
     */
    public boolean onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        return false;
    }

    /**
     * Called when the machine is being wrenched by a player while sneaking.
     *
     * @return True if something happens
     */
    public boolean onSneakMachineActivated(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine
     *
     * @return True if some happens
     */
    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine while sneaking. Only
     * works with the UE wrench.
     *
     * @return True if some happens
     */
    public boolean onSneakUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        return this.onUseWrench(world, pos, entityPlayer, hand, heldItem, hit);
    }
}
