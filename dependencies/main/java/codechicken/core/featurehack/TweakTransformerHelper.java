package codechicken.core.featurehack;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import static net.minecraftforge.common.util.ForgeDirection.UP;

public class TweakTransformerHelper
{
    public static void quenchFireTick(World world, int x, int y, int z, Random rand)
    {
        Block base = world.getBlock(x, y - 1, z);
        boolean supported = (base != null && base.isFireSource(world, x, y - 1, z, UP));
        
        if (!Blocks.fire.canPlaceBlockAt(world, x, y, z) ||
                !supported && world.isRaining() && (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x - 1, y, z) || world.canLightningStrikeAt(x + 1, y, z) || world.canLightningStrikeAt(x, y, z - 1) || world.canLightningStrikeAt(x, y, z + 1)))
            world.setBlockToAir(x, y, z);
        else
        {
            int meta = world.getBlockMetadata(x, y, z);
            if(meta < 15)
                world.setBlockMetadataWithNotify(x, y, z, meta+rand.nextInt(3)/2, 0);
            
            world.scheduleBlockUpdate(x, y, z, Blocks.fire, Blocks.fire.tickRate(world) + rand.nextInt(10));
            
            if(!supported && !Blocks.fire.canCatchFire(world, x, y - 1, z, UP) && meta == 15 && rand.nextInt(4) == 0)
                world.setBlockToAir(x, y, z);
        }
    }
    
    public static boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        return block.isAir(world, x, y, z) || block.isReplaceable(world, x, y, z);
    }
}
