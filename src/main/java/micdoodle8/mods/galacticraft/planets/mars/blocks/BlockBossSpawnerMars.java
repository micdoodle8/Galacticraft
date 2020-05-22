package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockBossSpawner;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityDungeonSpawnerMars;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockBossSpawnerMars extends BlockBossSpawner
{
    public BlockBossSpawnerMars(Properties builder)
    {
        super(builder);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDungeonSpawnerMars();
    }
}
