package micdoodle8.mods.galacticraft.planets.asteroids.schematic;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.gui.GuiSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.inventory.ContainerSchematicTier3Rocket;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicTier3Rocket implements ISchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerPlanets.idSchematicRocketT3.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsPlanets.NASA_WORKBENCH_TIER_3_ROCKET + Constants.MOD_ID_PLANETS.hashCode();
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(MarsItems.schematicRocketT3);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenManager.IScreenFactory<ContainerSchematicTier3Rocket, GuiSchematicTier3Rocket> getResultScreen(PlayerEntity player, BlockPos pos)
    {
        return GuiSchematicTier3Rocket::new;
    }

    @Override
    public SimpleNamedContainerProvider getContainerProvider(PlayerEntity player)
    {
        return new SimpleNamedContainerProvider((w, p, pl) -> new ContainerSchematicTier3Rocket(w, p), new TranslationTextComponent("container.schematic_tier3_rocket"));
    }

    @Override
    public int compareTo(ISchematicPage o)
    {
        if (this.getPageID() > o.getPageID())
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
