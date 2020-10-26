package micdoodle8.mods.galacticraft.core.schematic;

import micdoodle8.mods.galacticraft.api.recipe.SchematicPage;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiSchematicBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematicBuggy;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SchematicMoonBuggy extends SchematicPage
{
    @Override
    public int getPageID()
    {
        return ConfigManagerCore.idSchematicMoonBuggy.get();
    }

//    @Override
//    public int getGuiID()
//    {
//        return GuiIdsCore.NASA_WORKBENCH_BUGGY;
//    }

    @Override
    public ItemStack getRequiredItem()
    {
        return new ItemStack(GCItems.schematicBuggy, 1);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ScreenManager.IScreenFactory<ContainerSchematicBuggy, GuiSchematicBuggy> getResultScreen(PlayerEntity player, BlockPos pos)
    {
        return GuiSchematicBuggy::new;
    }

    @Override
    public SimpleNamedContainerProvider getContainerProvider(PlayerEntity player)
    {
        return new SimpleNamedContainerProvider((w, p, pl) -> new ContainerSchematicBuggy(w, p), new TranslationTextComponent("container.buggy.name"));
    }
}
