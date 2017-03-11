package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
//import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPainter extends TileEntity
{
    public void setAllGlassColors(ItemStack itemStack)
    {
        Item item = itemStack.getItem();
        int color = 0x333333;
        if (item == Items.dye)
        {
            color = ItemDye.dyeColors[itemStack.getItemDamage()];
        }
        else if (item instanceof ItemBlock)
        {
            Block b = ((ItemBlock)item).getBlock();
            IBlockState bs = b.getStateFromMeta(itemStack.getItemDamage());
            MapColor mc = b.getMapColor(bs);
            color = mc.colorValue;
        }

        color = ColorUtil.lighten(color, 0.03F);
        
        //For proof of concept and testing
        GCBlocks.spaceGlassClear.setColor(color);
        GCBlocks.spaceGlassVanilla.setColor(color);
        GCBlocks.spaceGlassStrong.setColor(color);
        
        //TODO: anyway to force all chunks to rebuild?
    }
}
