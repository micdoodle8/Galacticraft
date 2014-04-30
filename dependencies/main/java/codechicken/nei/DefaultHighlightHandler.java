package codechicken.nei;

import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.guihook.GuiContainerManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class DefaultHighlightHandler implements IHighlightHandler
{
    @Override
    public List<String> handleTextData(ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop, List<String> currenttip, ItemInfo.Layout layout)
    {
        String name = null;
        try
        {
            String s = GuiContainerManager.itemDisplayNameShort(stack);
            if(s != null && !s.endsWith("Unnamed"))
                name = s;

            if(name != null)
                currenttip.add(name);
        }
        catch(Exception e)
        {
        }

        if(stack.getItem() == Items.redstone)
        {
            int md = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
            String s = ""+md;
            if(s.length() < 2)
                s=" "+s;
            currenttip.set(currenttip.size()-1, name+" "+s);
        }
        
        return currenttip;
    }

    @Override
    public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop)
    {
        return null;
    }
}
