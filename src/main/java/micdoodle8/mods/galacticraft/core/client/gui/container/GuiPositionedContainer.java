package micdoodle8.mods.galacticraft.core.client.gui.container;

import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public abstract class GuiPositionedContainer extends GuiContainerGC
{
    private int x;
    private int y;
    private int z;
    
    public GuiPositionedContainer(Container container, BlockPos pos)
    {
        super(container);
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
