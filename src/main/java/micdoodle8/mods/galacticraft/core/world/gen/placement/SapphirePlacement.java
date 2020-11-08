package micdoodle8.mods.galacticraft.core.world.gen.placement;

import com.mojang.datafixers.Dynamic;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

public class SapphirePlacement extends SimplePlacement<NoPlacementConfig>
{
   public SapphirePlacement(Function<Dynamic<?>, ? extends NoPlacementConfig> config) {
      super(config);
   }

   public Stream<BlockPos> getPositions(Random random, NoPlacementConfig config, BlockPos pos) {
      int i = 1 + random.nextInt(2);
      return IntStream.range(0, i).mapToObj((i1) -> {
         int j = random.nextInt(16) + pos.getX();
         int k = random.nextInt(16) + pos.getZ();
         int l = random.nextInt(28) + 4;
         return new BlockPos(j, l, k);
      });
   }
}
