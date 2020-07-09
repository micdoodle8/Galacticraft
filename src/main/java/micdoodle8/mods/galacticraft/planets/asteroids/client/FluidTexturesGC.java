package micdoodle8.mods.galacticraft.planets.asteroids.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@OnlyIn(Dist.CLIENT)
public class FluidTexturesGC
{
    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(new FluidTexturesGC());
    }

//    @SubscribeEvent
//    public void onStitch(TextureStitchEvent.Pre event)
//    {
//        if (event.map.getTextureType() == 0)
//        {
//            AsteroidsModule.fluidMethaneGas.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/MethaneGas"));
//            AsteroidsModule.fluidAtmosphericGases.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/AtmosphericGases"));
//            AsteroidsModule.fluidLiquidMethane.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/LiquidMethane"));
//            AsteroidsModule.fluidLiquidOxygen.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/LiquidOxygen"));
//            AsteroidsModule.fluidOxygenGas.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/OxygenGas"));
//            AsteroidsModule.fluidLiquidNitrogen.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/LiquidNitrogen"));
//            AsteroidsModule.fluidLiquidArgon.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/LiquidArgon"));
//            AsteroidsModule.fluidNitrogenGas.setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/NitrogenGas"));
//            FluidRegistry.getFluid("hydrogen").setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/HydrogenGas"));
//            FluidRegistry.getFluid("helium").setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/HeliumGas"));
//            FluidRegistry.getFluid("argon").setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/ArgonGas"));
//            FluidRegistry.getFluid("carbondioxide").setIcons(event.map.registerIcon(GalacticraftPlanets.ASSET_PREFIX + ":fluids/CarbonDioxideGas"));
//        }
//    }
}
