package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.miccore.Annotations;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace)
    {
        return true;
    }

    @Annotations.RuntimeInterface(clazz = "buildcraft.api.tools.IToolWrench", modID = CompatibilityManager.modidBuildcraft)
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace)
    {
        ItemStack stack = player.inventory.getCurrentItem();

        if (!stack.isEmpty())
        {
            stack.damageItem(1, player);

            if (stack.getItemDamage() >= stack.getMaxDamage())
            {
                stack.shrink(1);
            }

            if (stack.getCount() <= 0)
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
        }
    }

    public void wrenchUsed(EntityPlayer entityPlayer, BlockPos pos)
    {

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

        if (state.getBlock() instanceof BlockAdvanced)
        {
            if (((BlockAdvanced) state.getBlock()).onUseWrench(world, pos, player, hand, player.getHeldItem(hand), side, hitX, hitY, hitZ))
            {
                return EnumActionResult.SUCCESS;
            }
        }

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet())
        {
            IProperty<?> iProperty = entry.getKey();
            if (iProperty instanceof PropertyEnum && iProperty.getName().equals("facing") && state.getValue(iProperty) instanceof EnumFacing)
            {
                PropertyEnum<EnumFacing> property = (PropertyEnum<EnumFacing>) iProperty;
                Collection<EnumFacing> values = property.getAllowedValues();
                if (values.size() > 0)
                {
                    boolean done = false;
                    EnumFacing currentFacing = state.getValue(property);
                    
                    // Special case: horizontal facings should be rotated around the Y axis - this includes most of GC's own blocks
                    if (values.size() == 4 && !values.contains(EnumFacing.UP) && !values.contains(EnumFacing.DOWN))
                    {
                        EnumFacing newFacing = currentFacing.rotateY();
                        if (values.contains(newFacing))
                        {
                            world.setBlockState(pos, state.withProperty(property, newFacing));
                            done = true;
                        }
                    }
                    if (!done)
                    {
                        // General case: rotation will follow the order in FACING (may be a bit jumpy)
                        List<EnumFacing> list = Arrays.asList(values.toArray(new EnumFacing[0]));
                        int i = list.indexOf(currentFacing) + 1;
                        EnumFacing newFacing = list.get(i >= list.size() ? 0 : i);
                        world.setBlockState(pos, state.withProperty(property, newFacing));
                    }

                    ItemStack stack = player.getHeldItem(hand).copy();
                    stack.damageItem(1, player);
                    player.setHeldItem(hand, stack);

                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof TileBaseUniversalElectrical)
                        ((TileBaseUniversalElectrical) tile).updateFacing();

                    return EnumActionResult.SUCCESS;
                }
                return EnumActionResult.PASS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }
}
