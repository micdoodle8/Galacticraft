package buildcraft.api.schematics;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidStack;

import buildcraft.api.core.InvalidInputDataException;

public interface ISchematicBlock {
    void init(SchematicBlockContext context);

    default boolean isAir() {
        return false;
    }

    @Nonnull
    default Set<BlockPos> getRequiredBlockOffsets() {
        return Collections.emptySet();
    }

    @Nonnull
    default List<ItemStack> computeRequiredItems() {
        return Collections.emptyList();
    }

    @Nonnull
    default List<FluidStack> computeRequiredFluids() {
        return Collections.emptyList();
    }

    ISchematicBlock getRotated(Rotation rotation);

    boolean canBuild(World world, BlockPos blockPos);

    default boolean isReadyToBuild(World world, BlockPos blockPos) {
        return true;
    }

    boolean build(World world, BlockPos blockPos);

    boolean buildWithoutChecks(World world, BlockPos blockPos);

    boolean isBuilt(World world, BlockPos blockPos);

    NBTTagCompound serializeNBT();

    /** @throws InvalidInputDataException If the input data wasn't correct or didn't make sense. */
    void deserializeNBT(NBTTagCompound nbt) throws InvalidInputDataException;
}
