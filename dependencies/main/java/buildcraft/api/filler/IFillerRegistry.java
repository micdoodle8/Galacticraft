package buildcraft.api.filler;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;

import buildcraft.api.statements.IStatement;

public interface IFillerRegistry {
    void addPattern(IFillerPattern pattern);

    /** @return An {@link IFillerPattern} from its {@link IStatement#getUniqueTag()} */
    @Nullable
    IFillerPattern getPattern(String name);

    Collection<IFillerPattern> getPatterns();

    IFilledTemplate createFilledTemplate(BlockPos pos, BlockPos size);
}
