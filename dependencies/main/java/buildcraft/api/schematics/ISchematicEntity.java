package buildcraft.api.schematics;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidStack;

import buildcraft.api.core.InvalidInputDataException;

public interface ISchematicEntity {
    void init(SchematicEntityContext context);

    Vec3d getPos();

    @Nonnull
    default List<ItemStack> computeRequiredItems() {
        return Collections.emptyList();
    }

    @Nonnull
    default List<FluidStack> computeRequiredFluids() {
        return Collections.emptyList();
    }

    ISchematicEntity getRotated(Rotation rotation);

    Entity build(World world, BlockPos basePos);

    Entity buildWithoutChecks(World world, BlockPos basePos);

    NBTTagCompound serializeNBT();

    /** @throws InvalidInputDataException If the input data wasn't correct or didn't make sense. */
    void deserializeNBT(NBTTagCompound nbt) throws InvalidInputDataException;
}
