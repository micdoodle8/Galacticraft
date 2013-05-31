package micdoodle8.mods.galacticraft.core.items;

import java.util.List;
import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.BlockSapling;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTextureTile;
import net.minecraft.item.ItemStack;

public class GCCoreItemSapling extends ItemMultiTextureTile
{
    public GCCoreItemSapling(int par1)
    {
        super(par1, GCCoreBlocks.sapling, BlockSapling.WOOD_TYPES);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return null;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(EnumColor.RED + "Requires 4 blocks of water nearby to grow");
    }
}
