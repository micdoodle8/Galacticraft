package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.lang.reflect.Method;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockAdvanced extends Block
{
    public BlockAdvanced(Material material)
    {
        super(material);
        this.setHardness(0.6f);
        this.setResistance(2.5F);
        //A default blast resistance for GC machines and tiles, similar to a bookshelf
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.useWrench(worldIn, pos, playerIn, side, hitX, hitY, hitZ))
        {
            return true;
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, side, hitX, hitY, hitZ))
            {
                return true;
            }
        }

        return this.onMachineActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    protected boolean useWrench(World worldIn, BlockPos pos, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /**
         * Check if the player is holding a wrench or an electric item. If so,
         * call the wrench event.
         */
        if (this.isUsableWrench(playerIn, playerIn.inventory.getCurrentItem(), pos))
        {
            this.damageWrench(playerIn, playerIn.inventory.getCurrentItem(), pos);

            if (playerIn.isSneaking())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, side, hitX, hitY, hitZ))
                {
                    playerIn.swingItem();
                    return true;
                }
            }

            if (this.onUseWrench(worldIn, pos, playerIn, side, hitX, hitY, hitZ))
            {
                playerIn.swingItem();
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
    public boolean isUsableWrench(EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (entityPlayer != null && itemStack != null)
        {
            Item item = itemStack.getItem();
            if (item == GCItems.wrench) return true;
            
            Class<? extends Item> wrenchClass = item.getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodCanWrench = wrenchClass.getMethod("canWrench", EntityPlayer.class, BlockPos.class);
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
                    return itemStack.getItemDamage() < itemStack.getMaxDamage();
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
    public boolean damageWrench(EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (this.isUsableWrench(entityPlayer, itemStack, pos))
        {
            Class<? extends Item> wrenchClass = itemStack.getItem().getClass();

            /**
             * Buildcraft
             */
            try
            {
                Method methodWrenchUsed = wrenchClass.getMethod("wrenchUsed", EntityPlayer.class, BlockPos.class);
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
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
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
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when the machine is being wrenched by a player while sneaking.
     *
     * @return True if something happens
     */
    public boolean onSneakMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine
     *
     * @return True if some happens
     */
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine while sneaking. Only
     * works with the UE wrench.
     *
     * @return True if some happens
     */
    public boolean onSneakUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return this.onUseWrench(world, pos, entityPlayer, side, hitX, hitY, hitZ);
    }
    
    public void rotate6Ways(World world, BlockPos pos)
    {
        int metadata = this.getMetaFromState(world.getBlockState(pos));
        int metaDir = ((metadata & 7) + 1) % 6;
        //DOWN->UP->NORTH->*EAST*->*SOUTH*->WEST
        //0->1 1->2 2->5 3->4 4->0 5->3 
        if (metaDir == 3) //after north
        {
            metaDir = 5;
        }
        else if (metaDir == 0)
        {
            metaDir = 3;
        }
        else if (metaDir == 5)
        {
            metaDir = 0;
        }
            
        world.setBlockState(pos, this.getStateFromMeta(metaDir), 3);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return this.isNormalCube(world, pos);
    }
}
