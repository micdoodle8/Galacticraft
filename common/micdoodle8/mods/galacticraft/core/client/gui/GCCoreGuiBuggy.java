package micdoodle8.mods.galacticraft.core.client.gui;

import mekanism.api.EnumColor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GCCoreGuiBuggy extends GuiContainer
{
    private final IInventory upperChestInventory;
    private final int type;

    public GCCoreGuiBuggy(IInventory par1IInventory, IInventory par2IInventory, int type)
    {
        super(new GCCoreContainerBuggy(par1IInventory, par2IInventory, type));
        this.upperChestInventory = par1IInventory;
        this.allowUserInput = false;
        this.type = type;
        this.ySize = 145 + this.type * 36;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.fuel.name"), 8, 2 + 3, 4210752);

        this.fontRenderer.drawString(StatCollector.translateToLocal(this.upperChestInventory.getInvName()), 8, this.type == 0 ? 50 : 39, 4210752);

        if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof GCCoreEntityBuggy)
        {
            this.fontRenderer.drawString(LanguageRegistry.instance().getStringLocalization("gui.message.fuel.name") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((GCCoreEntityBuggy) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.code : percentage > 40.0D ? EnumColor.ORANGE.code : EnumColor.RED.code;
            final String str = percentage + "% " + LanguageRegistry.instance().getStringLocalization("gui.message.full.name");
            this.fontRenderer.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        String var4 = "/mods/galacticraftcore/textures/gui/buggy_" + this.type * 18 + ".png";

        this.mc.renderEngine.bindTexture(var4);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);

        if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof GCCoreEntityBuggy)
        {
            final int fuelLevel = ((GCCoreEntityBuggy) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(38);

            this.drawTexturedModalRect((this.width - this.xSize) / 2 + 72, (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
