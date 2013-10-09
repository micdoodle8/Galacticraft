package codechicken.core.featurehack;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import static net.minecraftforge.common.ForgeDirection.UP;

public class TweakTransformerHelper
{
    public static void quenchFireTick(World world, int x, int y, int z, Random rand)
    {
        Block base = Block.blocksList[world.getBlockId(x, y - 1, z)];
        boolean supported = (base != null && base.isFireSource(world, x, y - 1, z, world.getBlockMetadata(x, y - 1, z), UP));
        
        if (!Block.fire.canPlaceBlockAt(world, x, y, z))
            world.setBlock(x, y, z, 0);
        else if (!supported && world.isRaining() && (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x - 1, y, z) || world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1) || world.canLightningStrikeAt(x, y, z + 1)))
            world.setBlockToAir(x, y, z);
        else
        {
            int meta = world.getBlockMetadata(x, y, z);
            if(meta < 15)
                world.setBlockMetadataWithNotify(x, y, z, meta+rand.nextInt(3)/2, 0);
            
            world.scheduleBlockUpdate(x, y, z, Block.fire.blockID, Block.fire.tickRate(world) + rand.nextInt(10));
            
            if(!supported && !Block.fire.canBlockCatchFire(world, x, y, z, UP) && meta == 15 && rand.nextInt(4) == 0)
                world.setBlock(x, y, z, 0);
        }
    }
    
    public static boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        return block == null || block.isBlockReplaceable(world, x, y, z);
    }
}
