package micdoodle8.mods.galacticraft.core.client.gui.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public abstract class GuiPositionedContainer<T extends Container> extends GuiContainerGC<T>
{
    private int x;
    private int y;
    private int z;

    public GuiPositionedContainer(T container, PlayerInventory playerInventory, ITextComponent title, BlockPos pos)
    {
        super(container, playerInventory, title);
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }
    
    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }
}
