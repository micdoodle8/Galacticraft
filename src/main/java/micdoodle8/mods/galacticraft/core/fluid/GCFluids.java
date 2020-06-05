package micdoodle8.mods.galacticraft.core.fluid;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.BucketItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.function.UnaryOperator;

import static net.minecraftforge.fluids.ForgeFlowingFluid.Source;
import static net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;

public class GCFluids
{
    public static final GCFluidRegistry FLUIDS = new GCFluidRegistry();

    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> OIL = registerLiquid("oil", fluidAttributes -> fluidAttributes.color(0xFF111111).density(800).viscosity(1500));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> FUEL = registerLiquid("fuel", fluidAttributes -> fluidAttributes.color(0xFFDBDF16).density(400).viscosity(900));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> OXYGEN = registerLiquid("oxygen", fluidAttributes -> fluidAttributes.color(0xFF6CE2FF).temperature(90).density(1141).viscosity(1141));
    public static final FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> HYDROGEN = registerLiquid("hydrogen", fluidAttributes -> fluidAttributes.color(0xFFFFFFFF).temperature(20).density(70).viscosity(70));

    private static FluidRegistrationEntry<Source, Flowing, FlowingFluidBlock, BucketItem> registerLiquid(String name, UnaryOperator<FluidAttributes.Builder> fluidAttributes) {
        return FLUIDS.register(name, fluidAttributes.apply(FluidAttributes.builder(new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid"),
                new ResourceLocation(Constants.MOD_ID_CORE, "block/liquid/liquid_flow"))));
    }
}
