package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiRocketInventory extends GuiContainerGC<ContainerRocketInventory>
{
    private static final ResourceLocation[] rocketTextures = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiRocketInventory.rocketTextures[i] = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/rocket_" + i * 18 + ".png");
        }
    }

    private final EntityAutoRocket rocket;

    public GuiRocketInventory(ContainerRocketInventory container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerRocketInventory(playerInv, rocket, rocketType, Minecraft.getInstance().player), playerInv, rocket.getName());
        this.passEvents = false;
        this.rocket = container.getRocket();
        this.ySize = rocket.getSizeInventory() <= 3 ? 132 : 145 + rocket.getSizeInventory() * 2;
    }

    @Override
    protected void init()
    {
        super.init();
        List<String> fuelTankDesc = new ArrayList<String>();
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
        fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + (((EntityTieredRocket) this.minecraft.player.getRidingEntity()).rocketType.getInventorySpace() == 2 ? 70 : 71), (this.height - this.ySize) / 2 + 6, 36, 40, fuelTankDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(GCCoreUtil.translate("gui.message.fuel"), 8, 2 + 3, 4210752);

        this.font.drawString(this.title.getFormattedText(), 8, 34 + 2 + 3, 4210752);

        if (this.minecraft.player != null && this.minecraft.player.getRidingEntity() != null && this.minecraft.player.getRidingEntity() instanceof EntitySpaceshipBase)
        {
            this.font.drawString(GCCoreUtil.translate("gui.message.fuel") + ":", 125, 15 + 3, 4210752);
            final double percentage = ((EntitySpaceshipBase) this.minecraft.player.getRidingEntity()).getScaledFuelLevel(100);
            final String color = percentage > 80.0D ? EnumColor.BRIGHT_GREEN.getCode() : percentage > 40.0D ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode();
            final String str = percentage + "% " + GCCoreUtil.translate("gui.message.full");
            this.font.drawString(color + str, 117 - str.length() / 2, 20 + 8, 4210752);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        this.minecraft.getTextureManager().bindTexture(GuiRocketInventory.rocketTextures[(this.rocket.getSizeInventory() - 2) / 18]);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.blit(var5, var6, 0, 0, 176, this.ySize);

        if (this.minecraft.player != null && this.minecraft.player.getRidingEntity() != null && this.minecraft.player.getRidingEntity() instanceof EntitySpaceshipBase)
        {
            final int fuelLevel = ((EntitySpaceshipBase) this.minecraft.player.getRidingEntity()).getScaledFuelLevel(38);

            this.blit((this.width - this.xSize) / 2 + (((EntityTieredRocket) this.minecraft.player.getRidingEntity()).rocketType.getInventorySpace() == 2 ? 71 : 72), (this.height - this.ySize) / 2 + 45 - fuelLevel, 176, 38 - fuelLevel, 42, fuelLevel);
        }
    }
}
