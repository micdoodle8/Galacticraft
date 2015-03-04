package micdoodle8.mods.galacticraft.planets.asteroids.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;

@SideOnly(Side.CLIENT)
public class FluidTexturesGC
{
    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(new FluidTexturesGC());
    }


    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event)
    {
        if (event.map.getTextureType() == 0)
        {
            AsteroidsModule.fluidMethaneGas.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/MethaneGas"));
            AsteroidsModule.fluidAtmosphericGases.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/AtmosphericGases"));
            AsteroidsModule.fluidLiquidMethane.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/LiquidMethane"));
            AsteroidsModule.fluidLiquidOxygen.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/LiquidOxygen"));
            AsteroidsModule.fluidOxygenGas.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/OxygenGas"));
            AsteroidsModule.fluidLiquidNitrogen.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/LiquidNitrogen"));
            AsteroidsModule.fluidLiquidArgon.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/LiquidArgon"));
            AsteroidsModule.fluidNitrogenGas.setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/NitrogenGas"));
            FluidRegistry.getFluid("hydrogen").setIcons(event.map.registerIcon(AsteroidsModule.ASSET_PREFIX + ":fluids/HydrogenGas"));
        }
    }

}
