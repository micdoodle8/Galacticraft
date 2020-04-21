package micdoodle8.mods.galacticraft.core.client.gui.container;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCrafting;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCrafting extends GuiContainerGC
{
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/crafting_table.png");
    TileEntityCrafting tile;
    List<String> memorySlotDesc = new ArrayList<String>();
    
    public GuiCrafting(InventoryPlayer playerInv, TileEntityCrafting tile)
    {
        super(new ContainerCrafting(playerInv, tile));
        this.tile = tile;
    }
    
    @Override
    public void initGui()
    {
        super.initGui();
        ItemStack mem = this.tile.getMemoryHeld();
        boolean memoryStored = !mem.isEmpty();
        memorySlotDesc.add(GCCoreUtil.translate(memoryStored ? "gui.crafting_memory.desc.0" : "gui.crafting_memory.desc.1"));
        this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 123, (this.height - this.ySize) / 2 + 59, 18, 18, memorySlotDesc, this.width, this.height, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRenderer.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        
        ItemStack mem = this.tile.getMemoryHeld();
        boolean memoryStored = !mem.isEmpty();
        memorySlotDesc.clear();
        memorySlotDesc.add(GCCoreUtil.translate(memoryStored ? "gui.crafting_memory.desc.0" : "gui.crafting_memory.desc.1"));
        if (mem != null && !mem.isEmpty()) this.itemRender.renderItemAndEffectIntoGUI(mem, i + 124, j + 59);
    }
}