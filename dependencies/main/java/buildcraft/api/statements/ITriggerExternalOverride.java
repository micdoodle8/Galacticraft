package buildcraft.api.statements;

import net.minecraft.util.EnumFacing;

/** This interface can be used by tiles to override external trigger behaviour.
 *
 * Please use wisely. */
public interface ITriggerExternalOverride {
    enum Result {
        TRUE,
        FALSE,
        IGNORE
    }

    Result override(EnumFacing side, IStatementContainer source, ITriggerExternal trigger, IStatementParameter[] parameters);
}
