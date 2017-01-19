package micdoodle8.mods.galacticraft.planets.venus.items;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemVolcanicPickaxe extends ItemPickaxe implements ISortableItem, IShiftDescription
{
    public ItemVolcanicPickaxe(String assetName)
    {
        super(VenusItems.TOOL_VOLCANIC);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean advanced)
    {
        if (this.showDescription(stack.getItemDamage()))
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            {
                info.addAll(FMLClientHandler.instance().getClient().fontRendererObj.listFormattedStringToWidth(this.getShiftDescription(stack.getItemDamage()), 150));
            }
            else
            {
                info.add(GCCoreUtil.translateWithFormat("item_desc.shift.name", GameSettings.getKeyDisplayString(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.getKeyCode())));
            }
        }
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
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.TOOLS;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
        boolean ret = super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);

        if (!(playerIn instanceof EntityPlayer))
        {
            return ret;
        }

        EnumFacing facing = playerIn.getHorizontalFacing();

        if (playerIn.rotationPitch < -45.0F)
        {
            facing = EnumFacing.UP;
        }
        else if (playerIn.rotationPitch > 45.0F)
        {
            facing = EnumFacing.DOWN;
        }

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1 && stack.stackSize > 0; ++j)
            {
                if (i == 0 && j == 0)
                {
                    continue;
                }

                BlockPos pos1;
                if (facing.getAxis() == EnumFacing.Axis.Y)
                {
                    pos1 = pos.add(i, 0, j);
                }
                else if (facing.getAxis() == EnumFacing.Axis.X)
                {
                    pos1 = pos.add(0, i, j);
                }
                else
                {
                    pos1 = pos.add(i, j, 0);
                }
                IBlockState state = worldIn.getBlockState(pos1);
                if (worldIn.getBlockState(pos).equals(state))
                {
                    worldIn.setBlockState(pos1, Blocks.air.getDefaultState());
                    state.getBlock().harvestBlock(worldIn, (EntityPlayer) playerIn, pos1, state, worldIn.getTileEntity(pos1));
                    stack.damageItem(1, playerIn);
                }
            }
        }

        return ret;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate("item.volcanic_pickaxe.description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }
}
