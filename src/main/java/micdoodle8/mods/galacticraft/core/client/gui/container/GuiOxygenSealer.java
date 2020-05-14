package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.fluid.OxygenPressureProtocol;
import micdoodle8.mods.galacticraft.core.inventory.ContainerOxygenSealer;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiOxygenSealer extends GuiContainerGC
{
    private static final ResourceLocation sealerTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/oxygen_sealer.png");

    private final TileEntityOxygenSealer sealer;
    private GuiButton buttonDisable;

    private GuiElementInfoRegion oxygenInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 24, 56, 9, new ArrayList<String>(), this.width, this.height, this);
    private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion((this.width - this.xSize) / 2 + 112, (this.height - this.ySize) / 2 + 37, 56, 9, new ArrayList<String>(), this.width, this.height, this);

    public GuiOxygenSealer(InventoryPlayer par1InventoryPlayer, TileEntityOxygenSealer par2TileEntityAirDistributor)
    {
        super(new ContainerOxygenSealer(par1InventoryPlayer, par2TileEntityAirDistributor));
        this.sealer = par2TileEntityAirDistributor;
        this.ySize = 200;
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.mc.world), new Object[] { this.sealer.getPos(), 0 }));
            break;
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        List<String> batterySlotDesc = new ArrayList<String>();
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
        batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 32, (this.height - this.ySize) / 2 + 26, 18, 18, batterySlotDesc, this.width, this.height, this));
        List<String> oxygenSlotDesc = new ArrayList<String>();
        oxygenSlotDesc.add(GCCoreUtil.translate("gui.oxygen_slot.desc.0"));
        oxygenSlotDesc.add(GCCoreUtil.translate("gui.oxygen_slot.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 9, (this.height - this.ySize) / 2 + 26, 18, 18, oxygenSlotDesc, this.width, this.height, this));
        List<String> ambientThermalDesc = new ArrayList<String>();
        ambientThermalDesc.add(GCCoreUtil.translate("gui.thermal_slot.desc.0"));
        ambientThermalDesc.add(GCCoreUtil.translate("gui.thermal_slot.desc.1"));
        ambientThermalDesc.add(GCCoreUtil.translate("gui.thermal_slot.desc.2"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 55, (this.height - this.ySize) / 2 + 26, 18, 18, ambientThermalDesc, this.width, this.height, this));
        List<String> oxygenDesc = new ArrayList<String>();
        oxygenDesc.add(GCCoreUtil.translate("gui.oxygen_storage.desc.0"));
        oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getOxygenStored()) + " / " + (int) Math.floor(this.sealer.getMaxOxygenStored())));
        this.oxygenInfoRegion.tooltipStrings = oxygenDesc;
        this.oxygenInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.oxygenInfoRegion.yPosition = (this.height - this.ySize) / 2 + 23;
        this.oxygenInfoRegion.parentWidth = this.width;
        this.oxygenInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.oxygenInfoRegion);
        List<String> electricityDesc = new ArrayList<String>();
        electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
        electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
        this.electricInfoRegion.tooltipStrings = electricityDesc;
        this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 112;
        this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 36;
        this.electricInfoRegion.parentWidth = this.width;
        this.electricInfoRegion.parentHeight = this.height;
        this.infoRegions.add(this.electricInfoRegion);
        this.buttonList.add(this.buttonDisable = new GuiButton(0, this.width / 2 - 38, this.height / 2 - 30 + 21, 76, 20, GCCoreUtil.translate("gui.button.enableseal.name")));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString(this.sealer.getName(), 8, 10, 4210752);
        GCCoreUtil.drawStringRightAligned(GCCoreUtil.translate("gui.message.in.name") + ":", 99, 26, 4210752, this.fontRenderer);
        GCCoreUtil.drawStringRightAligned(GCCoreUtil.translate("gui.message.in.name") + ":", 99, 38, 4210752, this.fontRenderer);
        String status = GCCoreUtil.translate("gui.message.status.name") + ": " + this.getStatus();
        this.buttonDisable.enabled = this.sealer.disableCooldown == 0;
        this.buttonDisable.displayString = this.sealer.disabled ? GCCoreUtil.translate("gui.button.enableseal.name") : GCCoreUtil.translate("gui.button.disableseal.name");
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 50, 4210752);
        int adjustedOxygenPerTick =  (int) (this.sealer.oxygenPerTick * 20);
        if (this.sealer.disabled || this.sealer.getEnergyStoredGC() < this.sealer.storage.getMaxExtract()) adjustedOxygenPerTick = 0;
        status = GCCoreUtil.translate("gui.oxygen_use.desc") + ": " + adjustedOxygenPerTick + GCCoreUtil.translate("gui.per_second");
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 60, 4210752);
        status = GCCoreUtil.translate("gui.message.thermal_status.name") + ": " + this.getThermalStatus();
        this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.sealer.ueWattsPerTick * 20, ElectricUnit.WATT);
        //		this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 70, 4210752);
        //		status = ElectricityDisplay.getDisplay(this.sealer.getVoltage(), ElectricUnit.VOLTAGE);
        //		this.fontRenderer.drawString(status, this.xSize / 2 - this.fontRenderer.getStringWidth(status) / 2, 80, 4210752);
        this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, this.ySize - 90 + 3, 4210752);
    }

    private String getThermalStatus()
    {
        IBlockState stateAbove = this.sealer.getWorld().getBlockState(this.sealer.getPos().up());
        Block blockAbove = stateAbove.getBlock();
        int metadata = blockAbove.getMetaFromState(stateAbove);

        if (blockAbove == GCBlocks.breatheableAir || blockAbove == GCBlocks.brightBreatheableAir)
        {
            if (metadata == 1)
            {
                return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.on.name");
            }
        }

        if (this.sealer.thermalControlEnabled())
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.not_available.name");
        }

        return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.off.name");
    }

    private String getStatus()
    {
        BlockPos blockPosAbove = this.sealer.getPos().up();
        Block blockAbove = this.sealer.getWorld().getBlockState(blockPosAbove).getBlock();
        IBlockState state = this.sealer.getWorld().getBlockState(blockPosAbove);

        if (!(blockAbove.isAir(state, this.sealer.getWorld(), blockPosAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.sealer.getWorld(), state, blockPosAbove, EnumFacing.UP))
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.sealerblocked.name");
        }

//        if (RedstoneUtil.isBlockReceivingRedstone(this.sealer.getWorldObj(), this.sealer.getPos()))
//        {
//            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.off.name");
//        }

        if (this.sealer.disabled)
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        if (this.sealer.getEnergyStoredGC() == 0)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingpower.name");
        }

        if (this.sealer.getEnergyStoredGC() < this.sealer.storage.getMaxExtract())
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.missingpower.name");
        }

        if (this.sealer.getOxygenStored() < 1)
        {
            return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.missingoxygen.name");
        }

        if (this.sealer.calculatingSealed)
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.checking_seal.name") + "...";
        }

        int threadCooldown = this.sealer.getScaledThreadCooldown(25);

        if (threadCooldown < 15)
        {
            if (threadCooldown < 4)
            {
                String elipsis = "";
                for (int i = 0; i < (23 - threadCooldown) % 4; i++)
                {
                    elipsis += ".";
                }

                return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.check_starting.name") + elipsis;
            }
            else
            {
                return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.check_pending.name");
            }
        }
        else
        {
            if (!this.sealer.sealed)
            {
                return EnumColor.DARK_RED + GCCoreUtil.translate("gui.status.unsealed.name");
            }
            else
            {
                return EnumColor.DARK_GREEN + GCCoreUtil.translate("gui.status.sealed.name");
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiOxygenSealer.sealerTexture);
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, this.ySize);

        if (this.sealer != null)
        {
            List<String> oxygenDesc = new ArrayList<String>();
            oxygenDesc.add(GCCoreUtil.translate("gui.oxygen_storage.desc.0"));
            oxygenDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.oxygen_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getOxygenStored()) + " / " + (int) Math.floor(this.sealer.getMaxOxygenStored())));
            this.oxygenInfoRegion.tooltipStrings = oxygenDesc;

            List<String> electricityDesc = new ArrayList<String>();
            electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
            EnergyDisplayHelper.getEnergyDisplayTooltip(this.sealer.getEnergyStoredGC(), this.sealer.getMaxEnergyStoredGC(), electricityDesc);
//			electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + ": " + ((int) Math.floor(this.sealer.getEnergyStoredGC()) + " / " + (int) Math.floor(this.sealer.getMaxEnergyStoredGC())));
            this.electricInfoRegion.tooltipStrings = electricityDesc;

            int scale = this.sealer.getCappedScaledOxygenLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 24, 197, 7, Math.min(scale, 54), 7);
            scale = this.sealer.getScaledElecticalLevel(54);
            this.drawTexturedModalRect(var5 + 113, var6 + 37, 197, 0, Math.min(scale, 54), 7);
            scale = 25 - this.sealer.getScaledThreadCooldown(25);
            this.drawTexturedModalRect(var5 + 148, var6 + 60, 176, 14, 10, 27);
            if (scale != 0)
            {
                this.drawTexturedModalRect(var5 + 149, var6 + 61 + scale, 186, 14, 8, 25 - scale);
            }

            if (this.sealer.getEnergyStoredGC() > 0)
            {
                this.drawTexturedModalRect(var5 + 99, var6 + 36, 176, 0, 11, 10);
            }

            if (this.sealer.getOxygenStored() > 0)
            {
                this.drawTexturedModalRect(var5 + 100, var6 + 23, 187, 0, 10, 10);
            }
        }
    }
}
