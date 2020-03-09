package micdoodle8.mods.galacticraft.core.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvanced;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
                List<EnumFacing> list = Arrays.asList(values.toArray(new EnumFacing[0]));
                if (list.size() > 0)
                {
                    int i = list.indexOf(state.getValue(property));
                    if (i + 1 < list.size())
                    {
                        world.setBlockState(pos, state.withProperty(property, list.get(i + 1)));
                    } else
                    {
                        world.setBlockState(pos, state.withProperty(property, list.get(0)));
                    }
                    ItemStack stack = player.getHeldItem(hand).copy();
                    stack.damageItem(1, player);

                    player.setHeldItem(hand, stack);
                    return EnumActionResult.SUCCESS;
                }
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
