package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockBossSpawner;
import micdoodle8.mods.galacticraft.planets.venus.tile.TileEntityDungeonSpawnerVenus;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBossSpawnerVenus extends BlockBossSpawner
{
    public BlockBossSpawnerVenus(Properties builder)
    {
        super(builder);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityDungeonSpawnerVenus();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }
}
