package micdoodle8.mods.galacticraft.planets.mars.client.gui;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSchematicCargoRocket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiSchematicCargoRocket extends GuiContainer implements ISchematicResultPage
{
	private static final ResourceLocation cargoRocketTexture = new ResourceLocation(MarsModule.ASSET_DOMAIN, "textures/gui/schematic_rocket_cargo.png");

	private int pageIndex;

	public GuiSchematicCargoRocket(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
	{
		super(new ContainerSchematicCargoRocket(par1InventoryPlayer, x, y, z));
		this.ySize = 220;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 30 + 27 - 12, 40, 20, "Back"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 130, this.height / 2 - 30 + 27 + 12, 40, 20, "Next"));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				SchematicRegistry.flipToLastPage(this.pageIndex);
				break;
			case 1:
				SchematicRegistry.flipToNextPage(this.pageIndex);
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString("Cargo Rocket", 7, -20 + 27, 4210752);
		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 220 - 104 + 2 + 9, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiSchematicCargoRocket.cargoRocketTexture);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void setPageIndex(int index)
	{
		this.pageIndex = index;
	}
}
