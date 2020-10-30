package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicTier1Rocket;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicTier1Rocket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicRocketT1 extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicRocketT1.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsCore.NASA_WORKBENCH_ROCKET;
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return null;  //This null is OK, it's used only as a flag by the calling code in SchematicRegistry
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenManager.IScreenFactory<ContainerSchematicTier1Rocket, GuiSchematicTier1Rocket> getResultScreen(PlayerEntity player, BlockPos pos)
    {
        return GuiSchematicTier1Rocket::new;
    }

    @Override
    public SimpleNamedContainerProvider getContainerProvider(PlayerEntity player)
    {
        return new SimpleNamedContainerProvider((w, p, pl) -> new ContainerSchematicTier1Rocket(w, p), new TranslationTextComponent("container.schematic_add"));
    }
}
