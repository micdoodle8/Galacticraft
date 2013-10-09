package codechicken.nei;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.forge.GuiContainerManager;

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

        if(stack.getItem() == Item.redstone)
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
