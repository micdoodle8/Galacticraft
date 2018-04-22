package micdoodle8.mods.galacticraft.core.items;

import buildcraft.api.blocks.CustomRotationHelper;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemUniversalWrench extends Item implements ISortableItem
{
    public ItemUniversalWrench(String assetName)
    {
        super();
        this.setUnlocalizedName(assetName);
        this.setMaxStackSize(1);
        this.setMaxDamage(256);
        //this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
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

    @RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace)
    {
        return true;
    }

    @RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace)
    {
        this.wrenchUsed(player, rayTrace == null ? null : rayTrace.getBlockPos());
    }

    public void wrenchUsed(EntityPlayer entityPlayer, BlockPos pos)
    {
        ItemStack stack = entityPlayer.inventory.getCurrentItem();

        if (!stack.isEmpty())
        {
            stack.damageItem(1, entityPlayer);

            if (stack.getItemDamage() >= stack.getMaxDamage())
            {
                stack.shrink(1);
            }

            if (stack.getCount() <= 0)
            {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return EnumActionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return true;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote && player instanceof EntityPlayerSP)
        {
            ClientProxyCore.playerClientHandler.onBuild(3, (EntityPlayerSP) player);
        }
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if (world.isRemote || player.isSneaking())
        {
            return EnumActionResult.PASS;
        }
        IBlockState state = world.getBlockState(pos);
        Block blockID = state.getBlock();

        if (blockID == Blocks.FURNACE || blockID == Blocks.LIT_FURNACE || blockID == Blocks.DISPENSER || blockID == Blocks.DROPPER)
        {
            int metadata = blockID.getMetaFromState(state);

            world.setBlockState(pos, blockID.getStateFromMeta(EnumFacing.getFront(metadata).rotateY().ordinal()), 3);
            this.wrenchUsed(player, pos);

            return EnumActionResult.SUCCESS;
        }
        else if (blockID == Blocks.HOPPER || blockID == Blocks.PISTON || blockID == Blocks.STICKY_PISTON)
        {
            int metadata = blockID.getMetaFromState(state);
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
            if (blockID == Blocks.HOPPER && metaDir == 1)
            {
                metaDir = 2;
            }
                
            world.setBlockState(pos, blockID.getStateFromMeta((metadata & 8) + metaDir), 3);
            this.wrenchUsed(player, pos);

            return EnumActionResult.SUCCESS;
        }
        else if (CompatibilityManager.modBCraftLoaded)
        {
            state = state.getActualState(world, pos);
            EnumActionResult result = CustomRotationHelper.INSTANCE.attemptRotateBlock(world, pos, state, side);
            if (result == EnumActionResult.SUCCESS) {
                wrenchUsed(player, hand, player.getHeldItem(hand), new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos));
            }
            return result;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }
}
