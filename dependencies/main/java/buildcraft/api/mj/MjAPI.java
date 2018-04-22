package buildcraft.api.mj;

import java.text.DecimalFormat;

import javax.annotation.Nonnull;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;

import buildcraft.api.core.CapabilitiesHelper;

public class MjAPI {

    // ################################
    //
    // Useful constants (Public API)
    //
    // ################################

    /** A single minecraft joule, in micro joules (the power system base unit) */
    public static final long ONE_MINECRAFT_JOULE = getMjValue();
    /** The same as {@link #ONE_MINECRAFT_JOULE}, but a shorter field name */
    public static final long MJ = ONE_MINECRAFT_JOULE;

    /** The decimal format used to display values of MJ to the player. Note that this */
    public static final DecimalFormat MJ_DISPLAY_FORMAT = new DecimalFormat("#,##0.##");

    public static IMjEffectManager EFFECT_MANAGER = NullaryEffectManager.INSTANCE;

    // ###############
    //
    // Helpful methods
    //
    // ###############

    /** Formats a given MJ value to a player-oriented string. Note that this does not append "MJ" to the value. */
    public static String formatMj(long microMj) {
        return formatMjInternal(microMj / (double) MJ);
    }

    private static String formatMjInternal(double val) {
        return MJ_DISPLAY_FORMAT.format(val);
    }

    // ########################################
    //
    // Null based classes
    //
    // ########################################

    public enum NullaryEffectManager implements IMjEffectManager {
        INSTANCE;
        @Override
        public void createPowerLossEffect(World world, Vec3d center, long microJoulesLost) {}

        @Override
        public void createPowerLossEffect(World world, Vec3d center, EnumFacing direction, long microJoulesLost) {}

        @Override
        public void createPowerLossEffect(World world, Vec3d center, Vec3d direction, long microJoulesLost) {}
    }
    // @formatter:on

    // ###############
    //
    // Capabilities
    //
    // ###############

    @Nonnull
    public static final Capability<IMjConnector> CAP_CONNECTOR;

    @Nonnull
    public static final Capability<IMjReceiver> CAP_RECEIVER;

    @Nonnull
    public static final Capability<IMjRedstoneReceiver> CAP_REDSTONE_RECEIVER;

    @Nonnull
    public static final Capability<IMjReadable> CAP_READABLE;

    @Nonnull
    public static final Capability<IMjPassiveProvider> CAP_PASSIVE_PROVIDER;

    static {
        CAP_CONNECTOR = CapabilitiesHelper.registerCapability(IMjConnector.class);
        CAP_RECEIVER = CapabilitiesHelper.registerCapability(IMjReceiver.class);
        CAP_REDSTONE_RECEIVER = CapabilitiesHelper.registerCapability(IMjRedstoneReceiver.class);
        CAP_READABLE = CapabilitiesHelper.registerCapability(IMjReadable.class);
        CAP_PASSIVE_PROVIDER = CapabilitiesHelper.registerCapability(IMjPassiveProvider.class);
    }

    private static long getMjValue() {
        return 1_000_000L;
    }
}
