package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.API.ISchematicPage;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreSchematicMoonBuggy implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return GCCoreConfigManager.idSchematicMoonBuggy;
    }

    @Override
    public int getGuiID()
    {
        return GCCoreConfigManager.idGuiBuggyCraftingBench;
    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(GCCoreItems.schematic, 1, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getTitle()
    {
        return "Moon Buggy";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getResultScreen(EntityPlayer player, int x, int y, int z)
    {
        return new GCCoreGuiSchematicBuggy(player.inventory);
    }

    @Override
    public Container getResultContainer(EntityPlayer player, int x, int y, int z)
    {
        return new GCCoreContainerBuggyBench(player.inventory, x, y, z);
    }

    @Override
    public int compareTo(ISchematicPage o)
    {
        if (this.getPageID() > o.getPageID())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
