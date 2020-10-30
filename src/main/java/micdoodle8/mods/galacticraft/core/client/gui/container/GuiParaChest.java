package micdoodle8.mods.galacticraft.core.client.gui.container;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.IScaleableFuelLevel;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

public class GuiParaChest extends GuiContainerGC<ContainerParaChest>
{
    private static final ResourceLocation[] parachestTexture = new ResourceLocation[4];

    static
    {
        for (int i = 0; i < 4; i++)
        {
            GuiParaChest.parachestTexture[i] = new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/chest_" + i * 18 + ".png");
        }
    }

    private final PlayerInventory playerInv;
    private final TileEntityParaChest parachest;

    private int inventorySlots = 0;

    public GuiParaChest(ContainerParaChest container, PlayerInventory playerInv, ITextComponent title)
    {
        super(container, playerInv, title);
//        super(new ContainerParaChest(playerInv, parachest, Minecraft.getInstance().player), playerInv, new TranslationTextComponent("container.para_chest"));
        this.playerInv = playerInv;
        this.parachest = container.getParaChest();
        this.passEvents = false;
        this.inventorySlots = parachest.getSizeInventory();
        this.ySize = 146 + this.inventorySlots * 2;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.font.drawString(this.title.getFormattedText(), 8, 6, 4210752);
        this.font.drawString(this.title.getFormattedText(), 8, this.ySize - 103 + (this.inventorySlots == 3 ? 2 : 4), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GuiParaChest.parachestTexture[(this.inventorySlots - 3) / 18]);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(k, l, 0, 0, this.xSize, this.ySize);

        if (this.parachest instanceof IScaleableFuelLevel)
        {
            int fuelLevel = ((IScaleableFuelLevel) this.parachest).getScaledFuelLevel(28);
            this.blit(k + 17, l + (this.inventorySlots == 3 ? 40 : 42) - fuelLevel + this.inventorySlots * 2, 176, 28 - fuelLevel, 34, fuelLevel);
        }
    }
}
