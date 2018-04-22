package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBuggy extends GuiContainerGC
{
    private static ResourceLocation[] sealerTexture = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiBuggy.sealerTexture[i] = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/buggy_" + i * 18 + ".png");
        }
    }

    private final IInventory upperChestInventory;
    private final int type;

    public GuiBuggy(IInventory par1IInventory, IInventory par2IInventory, int type)
    {
        super(new ContainerBuggy(par1IInventory, par2IInventory, type, FMLClientHandler.instance().getClient().player));
        this.upperChestInventory = par1IInventory;
        this.allowUserInput = false;
        this.type = type;
        this.ySize = 145 + this.type * 36;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
        oxygenDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 71, (this.height - this.ySize) / 2 + 6, 36, 40, oxygenDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.fuel.name"), 8, 2 + 3, 4210752);

        this.fontRenderer.drawString(GCCoreUtil.translate(this.upperChestInventory.getName()), 8, this.type == 0 ? 50 : 39, 4210752);

        if (this.mc.player != null && this.mc.player.getRidingEntity() != null && this.mc.player.getRidingEntity() instanceof EntityBuggy)
        {
            this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.fuel.name") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((EntityBuggy) this.mc.player.getRidingEntity()).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
            final String str = percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
            this.fontRenderer.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.getTextureManager().bindTexture(GuiBuggy.sealerTexture[this.type]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);

        if (this.mc.player != null && this.mc.player.getRidingEntity() != null && this.mc.player.getRidingEntity() instanceof EntityBuggy)
        {
            final int fuelLevel = ((EntityBuggy) this.mc.player.getRidingEntity()).getScaledFuelLevel(38);

            this.drawTexturedModalRect((this.width - this.xSize) / 2 + 72, (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
