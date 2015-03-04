package micdoodle8.mods.galacticraft.planets.asteroids.items;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockWalkway extends ItemBlockDesc
{
    public ItemBlockWalkway(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean advanced)
    {
        if (itemStack.getItem() == Item.getItemFromBlock(AsteroidBlocks.blockWalkwayWire))
        {
            list.add(EnumColor.AQUA + GCBlocks.aluminumWire.getLocalizedName());
        }
        else if (itemStack.getItem() == Item.getItemFromBlock(AsteroidBlocks.blockWalkwayOxygenPipe))
        {
            list.add(EnumColor.AQUA + GCBlocks.oxygenPipe.getLocalizedName());
        }

        super.addInformation(itemStack, entityPlayer, list, advanced);
    }
}
