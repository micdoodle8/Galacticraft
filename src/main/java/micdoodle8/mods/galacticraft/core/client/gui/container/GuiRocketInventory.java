package micdoodle8.mods.galacticraft.core.client.gui.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiRocketInventory extends GuiContainerGC
{
    private static ResourceLocation[] rocketTextures = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiRocketInventory.rocketTextures[i] = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/rocket_" + i * 18 + ".png");
        }
    }

    private final IInventory upperChestInventory;
    private final EnumRocketType rocketType;

    public GuiRocketInventory(IInventory par1IInventory, IInventory par2IInventory, EnumRocketType rocketType)
    {
        super(new ContainerRocketInventory(par1IInventory, par2IInventory, rocketType));
        this.upperChestInventory = par1IInventory;
        this.allowUserInput = false;
        this.ySize = rocketType.getInventorySpace() <= 3 ? 132 : 145 + rocketType.getInventorySpace() * 2;
        this.rocketType = rocketType;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuelTank.desc.0"));
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuelTank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + (((EntityTieredRocket) this.mc.thePlayer.ridingEntity).rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.ySize) / 2 + 6, 36, 40, fuelTankDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.fuel.name"), 8, 2 + 3, 4210752);

        this.fontRendererObj.drawString(GCCoreUtil.translate(this.upperChestInventory.getInventoryName()), 8, 34 + 2 + 3, 4210752);

        if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof EntitySpaceshipBase)
        {
            this.fontRendererObj.drawString(GCCoreUtil.translate("gui.message.fuel.name") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((EntitySpaceshipBase) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
            final String str = percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
            this.fontRendererObj.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.mc.getTextureManager().bindTexture(GuiRocketInventory.rocketTextures[(this.rocketType.getInventorySpace() - 2) / 18]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);

        if (this.mc.thePlayer != null && this.mc.thePlayer.ridingEntity != null && this.mc.thePlayer.ridingEntity instanceof EntitySpaceshipBase)
        {
            final int fuelLevel = ((EntitySpaceshipBase) this.mc.thePlayer.ridingEntity).getScaledFuelLevel(38);

            this.drawTexturedModalRect((this.width - this.xSize) / 2 + (((EntityTieredRocket) this.mc.thePlayer.ridingEntity).rocketType.getInventorySpace() == 2 ? 71 : 72), (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
