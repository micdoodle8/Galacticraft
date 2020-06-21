package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiBuggy extends GuiContainerGC<ContainerBuggy>
{
    private final ResourceLocation textureLoc;

    public GuiBuggy(ContainerBuggy containerBuggy, PlayerInventory playerInv, ITextComponent title)
    {
        super(containerBuggy, playerInv, title);
        this.passEvents = false;
        this.textureLoc = containerBuggy.buggyType.getTextureLoc();
        this.ySize = 145 + containerBuggy.buggyType.ordinal() * 36;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
        oxygenDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 71, (this.height - this.ySize) / 2 + 6, 36, 40, oxygenDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(GCCoreUtil.translate("gui.message.fuel.name"), 8, 2 + 3, 4210752);

        this.font.drawString(this.title.getFormattedText(), 8, this.container.buggyType == EntityBuggy.BuggyType.NO_INVENTORY ? 50 : 39, 4210752);

        if (this.minecraft.player != null && this.minecraft.player.getRidingEntity() != null && this.minecraft.player.getRidingEntity() instanceof EntityBuggy)
        {
            this.font.drawString(GCCoreUtil.translate("gui.message.fuel.name") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((EntityBuggy) this.minecraft.player.getRidingEntity()).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
            final String str = percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
            this.font.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.getTextureManager().bindTexture(textureLoc);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, 176, this.ySize);

        if (this.minecraft.player != null && this.minecraft.player.getRidingEntity() != null && this.minecraft.player.getRidingEntity() instanceof EntityBuggy)
        {
            final int fuelLevel = ((EntityBuggy) this.minecraft.player.getRidingEntity()).getScaledFuelLevel(38);

            this.blit((this.width - this.xSize) / 2 + 72, (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
