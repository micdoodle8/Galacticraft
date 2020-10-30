package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicInput;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicAdd extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicAddSchematic.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsCore.NASA_WORKBENCH_NEW_SCHEMATIC;
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return null;  //This null is OK, it's used only as a flag by SchematicRegistry calling code
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenManager.IScreenFactory<ContainerSchematic, GuiSchematicInput> getResultScreen(PlayerEntity player, BlockPos pos)
    {
        return GuiSchematicInput::new;
    }

    @Override
    public SimpleNamedContainerProvider getContainerProvider(PlayerEntity player)
    {
        return new SimpleNamedContainerProvider((w, p, pl) -> new ContainerSchematic(w, p), new TranslationTextComponent("container.schematic_add"));
    }
}
