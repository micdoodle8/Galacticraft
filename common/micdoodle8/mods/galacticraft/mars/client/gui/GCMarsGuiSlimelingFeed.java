package micdoodle8.mods.galacticraft.mars.client.gui;

import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCMarsGuiSlimelingFeed.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsGuiSlimelingFeed extends GuiScreen
{
	private final int xSize;
	private final int ySize;
	private static final ResourceLocation slimelingPanelGui = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/gui/slimelingPanel1.png");
	private final GCMarsEntitySlimeling slimeling;

	public static RenderItem drawItems = new RenderItem();

	public long timeBackspacePressed;
	public int cursorPulse;
	public int backspacePressed;
	public boolean isTextFocused = false;

	public GuiButton buttonGrowSlimeling;
	public GuiButton buttonBreedSlimeling;
	public GuiButton buttonStrengthenSlimeling;
	public GuiButton buttonHealSlimeling;

	public GCMarsGuiSlimelingFeed(GCMarsEntitySlimeling slimeling)
	{
		this.slimeling = slimeling;
		this.xSize = 138;
		this.ySize = 51;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		final int var6 = (this.height - this.ySize) / 2;
		this.buttonGrowSlimeling = new GuiButton(0, this.width / 2 - 65, var6 - 15, 64, 20, "Grow");
		this.buttonList.add(this.buttonGrowSlimeling);
		this.buttonBreedSlimeling = new GuiButton(1, this.width / 2 + 1, var6 - 15, 64, 20, "Breed");
		this.buttonList.add(this.buttonBreedSlimeling);
		this.buttonStrengthenSlimeling = new GuiButton(2, this.width / 2 - 65, var6 + 7, 64, 20, "Strengthen");
		this.buttonList.add(this.buttonStrengthenSlimeling);
		this.buttonHealSlimeling = new GuiButton(3, this.width / 2 + 1, var6 + 7, 64, 20, "Heal");
		this.buttonList.add(this.buttonHealSlimeling);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			switch (par1GuiButton.id)
			{
			case 0:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 2, "" }));
				break;
			case 1:
				if (this.slimeling.inLove <= 0 && this.mc.thePlayer.username.equals(this.slimeling.getOwnerName()) && this.slimeling.worldObj.isRemote)
				{
					this.slimeling.func_110196_bT();
				}
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 3, "" }));
				break;
			case 2:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 4, "" }));
				break;
			case 3:
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { this.slimeling.entityId, 5, "" }));
				break;
			}

			FMLClientHandler.instance().getClient().displayGuiScreen(null);
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.mc.renderEngine.bindTexture(GCMarsGuiSlimelingFeed.slimelingPanelGui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6 - 20, 0, 0, this.xSize, this.ySize);

		super.drawScreen(par1, par2, par3);

		this.buttonHealSlimeling.enabled = this.slimeling.getHealth() < Math.floor(this.slimeling.getMaxHealth());
		this.buttonGrowSlimeling.enabled = this.slimeling.getScale() < 1.0F;
		this.buttonStrengthenSlimeling.enabled = this.slimeling.getAttackDamage() < 1.0;
		this.buttonBreedSlimeling.enabled = this.slimeling.inLove <= 0 && this.slimeling.getGrowingAge() == 0;
	}
}
