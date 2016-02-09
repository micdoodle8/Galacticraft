package micdoodle8.mods.galacticraft.core.nei;

import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class GCNEIHighlightHandler implements IHighlightHandler
{
    @Override
    public List<String> handleTextData(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout)
    {
        String name = null;
        try
        {
            String s = GuiContainerManager.itemDisplayNameShort(stack);
            if (s != null && !s.endsWith("Unnamed"))
            {
                name = s;
            }

            if (name != null)
            {
                currenttip.add(name);
            }
        }
        catch (Exception e)
        {
        }

        if (stack.getItem() == Items.redstone)
        {
            int md = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
            String s = "" + md;
            if (s.length() < 2)
            {
                s = " " + s;
            }
            currenttip.set(currenttip.size() - 1, name + " " + s);
        }

        return currenttip;
    }

    @Override
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop)
    {
        int x = mop.blockX;
        int y = mop.blockY;
        int z = mop.blockZ;
        Block b = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 8 && b == GCBlocks.basicBlock)
        {
            return new ItemStack(GCBlocks.basicBlock, 1, 8);
        }
        if (meta == 2 && b == GCBlocks.blockMoon)
        {
            return new ItemStack(GCBlocks.blockMoon, 1, 2);
        }
        if (b == GCBlocks.fakeBlock && meta == 1)
        {
            return new ItemStack(GCBlocks.spaceStationBase, 1, 0);
        }
        if (b == GCBlocks.fakeBlock && meta == 2)
        {
            return new ItemStack(GCBlocks.landingPad, 1, 0);
        }
        if (b == GCBlocks.fakeBlock && meta == 6)
        {
            return new ItemStack(GCBlocks.landingPad, 1, 1);
        }
        return null;
    }
}
