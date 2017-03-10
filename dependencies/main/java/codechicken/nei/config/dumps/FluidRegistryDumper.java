package codechicken.nei.config.dumps;

import codechicken.nei.config.DataDumper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 9/01/2017.
 */
public class FluidRegistryDumper extends DataDumper {

    public FluidRegistryDumper() {
        super("tools.dump.fluid");
    }

    @Override
    public String[] header() {
        return new String[] {"Name", "ID", "Unlocalized Name", "Luminosity", "Density", "Temperature", "Viscosity", "Is Gas", "Rarity", "Block", "Still Texture", "Flowing Texture", "Fill Sound", "Empty Sound", "Class"};
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        Map<String, Fluid> registeredFluids = FluidRegistry.getRegisteredFluids();
        Map<Fluid, Integer> fluidIDMap = FluidRegistry.getRegisteredFluidIDs();
        List<String[]> dumps = new LinkedList<String[]>();
        for (Entry<String, Fluid> fluidEntry : registeredFluids.entrySet()) {
            Fluid fluid = fluidEntry.getValue();
            int id = fluidIDMap.get(fluid);
            dumps.add(new String[] {fluidEntry.getKey(), Integer.toString(id), fluid.getUnlocalizedName(), String.valueOf(fluid.getLuminosity()), String.valueOf(fluid.getDensity()), String.valueOf(fluid.getTemperature()), String.valueOf(fluid.isGaseous()), fluid.getRarity().toString(), fluid.getBlock().getRegistryName().toString(), fluid.getStill().toString(), fluid.getFlowing().toString(), fluid.getFillSound().getRegistryName().toString(), fluid.getEmptySound().getRegistryName().toString(), fluid.getClass().getCanonicalName()});
        }
        return dumps;
    }

    @Override
    public int modeCount() {
        return 1;
    }
}
