package micdoodle8.mods.galacticraft.core.client.gui.container;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiParaChest extends GuiContainerGC
{
    private static ResourceLocation[] parachestTexture = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiParaChest.parachestTexture[i] = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/chest_" + i * 18 + ".png");
        }
    }

    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;

    private int inventorySlots = 0;

    public GuiParaChest(IInventory par1IInventory, IInventory par2IInventory)
    {
        super(new ContainerParaChest(par1IInventory, par2IInventory));
        this.upperChestInventory = par1IInventory;
        this.lowerChestInventory = par2IInventory;
        this.allowUserInput = false;
        this.inventorySlots = par2IInventory.getSizeInventory();
        this.ySize = 146 + this.inventorySlots * 2;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRendererObj.drawString(this.lowerChestInventory.getName(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.upperChestInventory.hasCustomName() ? this.upperChestInventory.getName() : GCCoreUtil.translate(this.upperChestInventory.getName()), 8, this.ySize - 103 + (this.inventorySlots == 3 ? 2 : 4), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiParaChest.parachestTexture[(this.inventorySlots - 3) / 18]);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (this.lowerChestInventory instanceof IScaleableFuelLevel)
        {
            int fuelLevel = ((IScaleableFuelLevel) this.lowerChestInventory).getScaledFuelLevel(28);
            this.drawTexturedModalRect(k + 17, l + (this.inventorySlots == 3 ? 40 : 42) - fuelLevel + this.inventorySlots * 2, 176, 28 - fuelLevel, 34, fuelLevel);
        }
    }
}
