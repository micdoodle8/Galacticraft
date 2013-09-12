package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class GCCoreLandingPadRemovalEvent extends BlockEvent
{
    public boolean allow = true;
    
    public GCCoreLandingPadRemovalEvent(World world, int x, int y, int z)
    {
        super(x, y, z, world, Block.blocksList[world.getBlockId(x, y, z)], world.getBlockMetadata(x, y, z));
    }
}
