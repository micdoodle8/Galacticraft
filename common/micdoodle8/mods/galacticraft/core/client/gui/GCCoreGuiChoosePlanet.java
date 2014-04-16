package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import micdoodle8.mods.galacticraft.api.event.client.GCCoreEventChoosePlanetGui.Init;
import micdoodle8.mods.galacticraft.api.recipe.SpaceStationRecipe;
import micdoodle8.mods.galacticraft.core.GCLog;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * GCCoreGuiChoosePlanet.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreGuiChoosePlanet extends GuiScreen
{
	private long spaceTimer = 0;

	public static RenderItem drawItems = new RenderItem();

	private GCCoreGuiChoosePlanetSlot planetSlots;

	public int selectedSlot;

	private String[] destinations;

	public EntityPlayer playerToSend;

	public GuiSmallButton sendButton;

	public GuiSmallButton createSpaceStationButton;
	public GuiSmallButton renameSpaceStationButton;
	private String renameText = "";
	public long timeBackspacePressed;
	public int cursorPulse;
	public int backspacePressed;
	public boolean isTextFocused = false;

	private static final ResourceLocation backgroundTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/stars.png");
	private static final ResourceLocation blackTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/black.png");

	public GCCoreGuiChoosePlanet(EntityPlayer player, String[] listOfDestinations)
	{
		this.playerToSend = player;
		this.destinations = listOfDestinations;
	}

	public void updateDimensionList(String[] listOfDestinations)
	{
		this.destinations = listOfDestinations;
	}

	public FontRenderer getFontRenderer()
	{
		return this.fontRenderer;
	}

	@Override
	protected void keyTyped(char keyChar, int keyID)
	{
		if (!this.isTextFocused)
		{
			return;
		}

		if (keyID == Keyboard.KEY_BACK)
		{
			if (this.renameText.length() > 0)
			{
				this.renameText = this.renameText.substring(0, this.renameText.length() - 1);
				this.timeBackspacePressed = System.currentTimeMillis();
			}
		}
		else if (keyChar == 22)
		{
			String pastestring = GuiScreen.getClipboardString();

			if (pastestring == null)
			{
				pastestring = "";
			}

			if (this.isValid(this.renameText + pastestring))
			{
				this.renameText = this.renameText + pastestring;
				this.renameText = this.renameText.replace('$', ' ');
				this.renameText = this.renameText.replace('.', ' ');
			}
		}
		else if (this.isValid(this.renameText + keyChar) && keyID != Keyboard.KEY_PERIOD && keyChar != '$')
		{
			this.renameText = this.renameText + keyChar;
		}
	}

	public boolean isValid(String string)
	{
		return ChatAllowedCharacters.allowedCharacters.indexOf(string.charAt(string.length() - 1)) >= 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		if (!(this.planetSlots == null))
		{
			this.planetSlots.func_77207_a(2, 10, 10, 10);
		}

		this.planetSlots = new GCCoreGuiChoosePlanetSlot(this);

		this.buttonList.clear();
		this.buttonList.add(new GCCoreGuiTexturedButton(0, this.width - 28, 5, 22, 22, new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/gui/map_button.png"), 22, 22));
		this.buttonList.add(this.sendButton = new GuiSmallButton(1, this.width - 110, this.height - 26, 105, 20, StatCollector.translateToLocal("gui.button.sendtodim.name")));

		if (this.createSpaceStationButton == null)
		{
			this.buttonList.add(this.createSpaceStationButton = new GuiSmallButton(2, this.width / 2 - 60, 4, 120, 20, StatCollector.translateToLocal("gui.button.createsstation.name")));
			this.createSpaceStationButton.enabled = true;
		}
		else
		{
			this.createSpaceStationButton.xPosition = this.width / 2 - 60;
			this.buttonList.add(this.createSpaceStationButton);
		}

		if (this.renameSpaceStationButton != null && this.destinations[this.selectedSlot].contains("$"))
		{
			this.renameSpaceStationButton = new GuiSmallButton(3, this.width - 200, this.height - 26, 80, 20, StatCollector.translateToLocal("gui.button.rename.name"));
			this.buttonList.add(this.renameSpaceStationButton);
		}

		Init event = new Init(new ArrayList<GuiButton>());
		MinecraftForge.EVENT_BUS.post(event);
		this.buttonList.addAll(event.buttonList);

		this.planetSlots.registerScrollButtons(2, 3);
	}

	@Override
	public void updateScreen()
	{
		this.spaceTimer++;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	protected void mouseClicked(int px, int py, int par3)
	{
		final int startX = 20;
		final int startY = this.height - 26;
		final int width = this.width - 210 - startX;
		final int height = 20;

		if (px >= startX && px < startX + width && py >= startY && py < startY + height)
		{
			Gui.drawRect(startX, startY, startX + width, startY + height, 0xffA0A0A0);
			this.isTextFocused = true;
		}
		else
		{
			this.isTextFocused = false;
		}

		super.mouseClicked(px, py, par3);
	}

	private void drawPanorama2(float par1)
	{
		final Tessellator var4 = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		final byte var5 = 1;

		for (int var6 = 0; var6 < var5 * var5; ++var6)
		{
			GL11.glPushMatrix();
			final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 128.0F;
			final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 128.0F;
			final float var9 = 0.0F;
			GL11.glTranslatef(var7, var8, var9 + 0.5F);
			GL11.glScalef(7F, 7F, 7F);

			for (int var10 = 0; var10 < 9; ++var10)
			{
				GL11.glPushMatrix();

				if (var10 == 1)
				{
					GL11.glTranslatef(1.96F, 0.0F, 0.0F);
				}

				if (var10 == 2)
				{
					GL11.glTranslatef(-1.96F, 0.0F, 0.0F);
				}

				if (var10 == 3)
				{
					GL11.glTranslatef(0.0F, 1.96F, 0.0F);
				}

				if (var10 == 4)
				{
					GL11.glTranslatef(0.0F, -1.96F, 0.0F);
				}

				if (var10 == 5)
				{
					GL11.glTranslatef(-1.96F, -1.96F, 0.0F);
				}

				if (var10 == 6)
				{
					GL11.glTranslatef(-1.96F, 1.96F, 0.0F);
				}

				if (var10 == 7)
				{
					GL11.glTranslatef(1.96F, -1.96F, 0.0F);
				}

				if (var10 == 8)
				{
					GL11.glTranslatef(1.96F, 1.96F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(GCCoreGuiChoosePlanet.backgroundTexture);
				var4.startDrawingQuads();
				var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
				var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
				var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
				var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
				var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
				var4.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
		}

		var4.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void drawPanorama(float par1)
	{
		final Tessellator var4 = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		final byte var5 = 1;

		for (int var6 = 0; var6 < var5 * var5; ++var6)
		{
			GL11.glPushMatrix();
			final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
			final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
			final float var9 = 0.0F;
			GL11.glTranslatef(var7, var8, var9);
			GL11.glRotatef(MathHelper.sin((this.spaceTimer * 2 + par1) / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-(this.spaceTimer * 2 + par1) * 0.005F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(41, 0, 0, 1);

			for (int var10 = 0; var10 < 6; ++var10)
			{
				GL11.glPushMatrix();

				if (var10 == 1)
				{
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 2)
				{
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 3)
				{
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 4)
				{
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var10 == 5)
				{
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(GCCoreGuiChoosePlanet.backgroundTexture);
				var4.startDrawingQuads();
				var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
				var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + 1, 0.0F + 1);
				var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - 1, 0.0F + 1);
				var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - 1, 1.0F - 1);
				var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + 1, 1.0F - 1);
				var4.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
		}

		var4.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox()
	{
		this.mc.getTextureManager().bindTexture(GCCoreGuiChoosePlanet.backgroundTexture);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, false);
		GL11.glPushMatrix();
		GL11.glPopMatrix();
		GL11.glColorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	public void renderSkybox(float par1)
	{
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F, 0.0F, 1.0F);
		this.drawPanorama(par1);
		this.drawPanorama2(par1);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.rotateAndBlurSkybox();
		final Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		final float var5 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
		final float var6 = this.height * var5 / 256.0F;
		final float var7 = this.width * var5 / 256.0F;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		final int var8 = this.width;
		final int var9 = this.height;
		var4.addVertexWithUV(0.0D, var9, this.zLevel, 0.5F - var6, 0.5F + var7);
		var4.addVertexWithUV(var8, var9, this.zLevel, 0.5F - var6, 0.5F - var7);
		var4.addVertexWithUV(var8, 0.0D, this.zLevel, 0.5F + var6, 0.5F - var7);
		var4.addVertexWithUV(0.0D, 0.0D, this.zLevel, 0.5F + var6, 0.5F + var7);
		var4.draw();
		GL11.glPopMatrix();
		GL11.glColorMask(true, true, true, true);
	}

	protected void drawItemStackTooltip(HashMap<Integer, ToolTipEntry> itemList, int par2, int par3)
	{
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		if (!itemList.isEmpty())
		{
			int k = 0;
			int l;
			int i1;

			for (l = 0; l < itemList.size(); ++l)
			{
				for (ItemStringPair pair : itemList.get(l).itemStringPairs)
				{
					i1 = this.fontRenderer.getStringWidth(pair.description);

					if (i1 > k)
					{
						k = i1 + (itemList.isEmpty() ? 0 : 50);
					}
				}
			}

			l = par2 + 12;
			i1 = par3 - 12;
			int j1 = 14;

			if (itemList.size() > 1)
			{
				j1 += 2 + (itemList.size() - 1) * (itemList.isEmpty() ? 10 : 16);
			}

			if ((this.height - 500) / 2 + i1 + j1 + 6 > this.height)
			{
				i1 = this.height - j1 - (this.height - 500) / 2 - 6;
			}

			this.zLevel = 300.0F;
			GCCoreGuiChoosePlanet.drawItems.zLevel = 300.0F;
			final int k1 = -267386864;

			this.drawGradientRect(l - 3, i1 - 4, l + k + 3, i1 - 3, k1, k1);
			this.drawGradientRect(l - 3, i1 + j1 + 3, l + k + 3, i1 + j1 + 4, k1, k1);
			this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 + j1 + 3, k1, k1);
			this.drawGradientRect(l - 4, i1 - 3, l - 3, i1 + j1 + 3, k1, k1);
			this.drawGradientRect(l + k + 3, i1 - 3, l + k + 4, i1 + j1 + 3, k1, k1);
			final int l1 = 1347420415;
			final int i2 = (l1 & 16711422) >> 1 | l1 & -16777216;
			this.drawGradientRect(l - 3, i1 - 3 + 1, l - 3 + 1, i1 + j1 + 3 - 1, l1, i2);
			this.drawGradientRect(l + k + 2, i1 - 3 + 1, l + k + 3, i1 + j1 + 3 - 1, l1, i2);
			this.drawGradientRect(l - 3, i1 - 3, l + k + 3, i1 - 3 + 1, l1, l1);
			this.drawGradientRect(l - 3, i1 + j1 + 2, l + k + 3, i1 + j1 + 3, i2, i2);

			int stringY = i1 + (itemList.isEmpty() ? 0 : 5);

			for (int j2 = 0; j2 < itemList.size(); ++j2)
			{
				ToolTipEntry entry = itemList.get(j2);

				if (!entry.itemStringPairs.isEmpty())
				{
					int count = (int) (this.spaceTimer / 20 % entry.itemStringPairs.size());
					ItemStringPair pair = entry.itemStringPairs.get(count);

					int red = GCCoreUtil.convertTo32BitColor(255, 255, 10, 10);
					int green = GCCoreUtil.convertTo32BitColor(255, 10, 10, 255);

					String s = pair.description;

					this.fontRenderer.drawString(s, l + (itemList.isEmpty() ? 0 : 19), stringY, itemList.get(j2).isValid ? green : red);

					stringY += itemList.size() > 0 ? 16 : 14;
				}
			}

			stringY = i1 + (itemList.isEmpty() ? 0 : 5);

			for (int j2 = 0; j2 < itemList.size(); ++j2)
			{
				int count = (int) (this.spaceTimer / 20 % itemList.get(j2).itemStringPairs.size());
				ItemStringPair pair = itemList.get(j2).itemStringPairs.get(count);

				GCCoreGuiChoosePlanet.drawItems.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, pair.stack, l, stringY - 4);

				stringY += itemList.size() > 0 ? 16 : 14;
			}

			this.zLevel = 0.0F;
			GCCoreGuiChoosePlanet.drawItems.zLevel = 0.0F;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		String str = null;

		this.cursorPulse++;

		if (this.timeBackspacePressed > 0)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && this.renameText.length() > 0)
			{
				if (System.currentTimeMillis() - this.timeBackspacePressed > 200 / (1 + this.backspacePressed * 0.3F))
				{
					this.renameText = this.renameText.substring(0, this.renameText.length() - 1);
					this.timeBackspacePressed = System.currentTimeMillis();
					this.backspacePressed++;
				}
			}
			else
			{
				this.timeBackspacePressed = 0;
				this.backspacePressed = 0;
			}
		}

		if (this.renameSpaceStationButton == null && this.destinations[this.selectedSlot].contains("$"))
		{
			this.renameSpaceStationButton = new GuiSmallButton(3, this.width - 200, this.height - 26, 80, 20, "Rename");
			this.buttonList.add(this.renameSpaceStationButton);
		}
		else if (this.renameSpaceStationButton != null && !this.destinations[this.selectedSlot].contains("$"))
		{
			this.buttonList.remove(this.renameSpaceStationButton);
			this.renameSpaceStationButton = null;
		}

		if (this.createSpaceStationButton != null)
		{
			this.createSpaceStationButton.enabled = WorldUtil.getSpaceStationRecipe(this.getDimensionIdFromSlot()) != null;
		}

		this.planetSlots.drawScreen(par1, par2, par3);
		super.drawScreen(par1, par2, par3);

		String dest = this.destinations[this.selectedSlot].toLowerCase();

		if (dest.contains("*"))
		{
			dest = dest.replace("*", "");
		}

		str = StatCollector.translateToLocal("gui.choosePlanet.desc." + dest);

		// if
		// (this.destinations[this.selectedSlot].toLowerCase().equals("overworld"))
		// {
		// str =
		// StatCollector.translateToLocal("gui.choosePlanet.desc.overworld");
		// }

		if (str != null && !str.contains("space station"))
		{
			final String[] strArray = str.split("#");

			final int j = 260 / strArray.length + 1;

			for (int i = 0; i < strArray.length; i++)
			{
				if (strArray[i].contains("*"))
				{
					strArray[i] = strArray[i].replace("*", "");
					this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16716305);
				}
				else
				{
					this.drawCenteredString(this.fontRenderer, strArray[i], 50 + i * j, this.height - 20, 16777215);
				}
			}
		}

		if (this.destinations[this.selectedSlot].contains("$"))
		{
			final int startX = 20;
			final int startY = this.height - 26;
			final int width = this.width - 210 - startX;
			final int height = 20;
			Gui.drawRect(startX, startY, startX + width, startY + height, 0xffA0A0A0);
			Gui.drawRect(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xFF000000);
			this.drawString(this.fontRenderer, this.renameText + (this.cursorPulse / 24 % 2 == 0 && this.isTextFocused ? "_" : ""), startX + 4, startY + 5, 0xe0e0e0);
		}

		if (this.createSpaceStationButton != null)
		{
			PlayerUtil.getPlayerBaseClientFromPlayer(this.playerToSend, false);

			if (par1 >= this.createSpaceStationButton.xPosition && par2 >= this.createSpaceStationButton.yPosition && par1 < this.createSpaceStationButton.xPosition + 120 && par2 < this.createSpaceStationButton.yPosition + 20)
			{
				if (this.playerAlreadyCreatedDimension())
				{
					HashMap<Integer, ToolTipEntry> itemList = new HashMap<Integer, ToolTipEntry>();
					ArrayList<ItemStringPair> pairList0 = new ArrayList<ItemStringPair>();
					ArrayList<ItemStringPair> pairList1 = new ArrayList<ItemStringPair>();
					pairList0.add(new ItemStringPair(null, StatCollector.translateToLocal("gui.chooseplanet.alreadycreated1.name")));
					pairList1.add(new ItemStringPair(null, "        " + StatCollector.translateToLocal("gui.chooseplanet.alreadycreated2.name")));
					itemList.put(0, new ToolTipEntry(false, pairList0));
					itemList.put(1, new ToolTipEntry(false, pairList1));
					this.drawItemStackTooltip(itemList, this.createSpaceStationButton.xPosition + 115, this.createSpaceStationButton.yPosition + 15);
				}
				else if (this.canCreateSpaceStation() && WorldUtil.getSpaceStationRecipe(this.getDimensionIdFromSlot()) != null)
				{
					HashMap<Integer, ToolTipEntry> itemList = new HashMap<Integer, ToolTipEntry>();
					final SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.getDimensionIdFromSlot());

					final HashMap<Object, Integer> required = new HashMap<Object, Integer>();
					required.putAll(recipe.getInput());

					final Iterator<?> req = recipe.getInput().keySet().iterator();
					int itemSlot = 0;

					while (req.hasNext())
					{
						final Object next = req.next();

						final int amountRequired = required.get(next);
						int amountInInv = 0;

						for (int x = 0; x < this.playerToSend.inventory.getSizeInventory(); x++)
						{
							final ItemStack slot = this.playerToSend.inventory.getStackInSlot(x);

							if (slot != null)
							{
								if (next instanceof ItemStack)
								{
									if (SpaceStationRecipe.checkItemEquals((ItemStack) next, slot))
									{
										amountInInv += slot.stackSize;
									}
								}
								else if (next instanceof ArrayList)
								{
									for (final ItemStack item2 : (ArrayList<ItemStack>) next)
									{
										if (SpaceStationRecipe.checkItemEquals(item2, slot))
										{
											amountInInv += slot.stackSize;
										}
									}
								}
							}
						}

						ArrayList<ItemStack> inputList = new ArrayList();

						if (next instanceof ItemStack)
						{
							inputList.add((ItemStack) next);
						}
						else if (next instanceof ArrayList)
						{
							inputList.addAll((ArrayList) next);
						}

						ArrayList<ItemStringPair> stringList = new ArrayList<ItemStringPair>();

						for (ItemStack inputStack : inputList)
						{
							String display = inputStack.getDisplayName() + " " + amountInInv + "/" + amountRequired;
							stringList.add(new ItemStringPair(inputStack, display));
						}

						itemList.put(itemSlot, new ToolTipEntry(amountInInv >= amountRequired, stringList));
						itemSlot++;
					}

					this.drawItemStackTooltip(itemList, this.createSpaceStationButton.xPosition + 115, this.createSpaceStationButton.yPosition + 15);
				}
				else
				{
					this.createSpaceStationButton.enabled = false;
					HashMap<Integer, ToolTipEntry> itemList = new HashMap<Integer, ToolTipEntry>();
					ArrayList<ItemStringPair> pairList0 = new ArrayList<ItemStringPair>();
					ArrayList<ItemStringPair> pairList1 = new ArrayList<ItemStringPair>();
					pairList0.add(new ItemStringPair(null, StatCollector.translateToLocal("gui.chooseplanet.cannotcreate1.name")));
					pairList1.add(new ItemStringPair(null, "     " + StatCollector.translateToLocal("gui.chooseplanet.cannotcreate2.name")));
					itemList.put(0, new ToolTipEntry(false, pairList0));
					itemList.put(1, new ToolTipEntry(false, pairList1));
					this.drawItemStackTooltip(itemList, this.createSpaceStationButton.xPosition + 115, this.createSpaceStationButton.yPosition + 15);
				}
			}
		}
	}

	public static class ToolTipEntry
	{
		private final boolean isValid;

		public ToolTipEntry(boolean isValid, List<ItemStringPair> itemStringPairs)
		{
			this.isValid = isValid;
			this.itemStringPairs = itemStringPairs;
		}

		private final List<ItemStringPair> itemStringPairs;

		public boolean isValid()
		{
			return this.isValid;
		}

		public List<ItemStringPair> getItemStringPairs()
		{
			return this.itemStringPairs;
		}
	}

	public static class ItemStringPair
	{
		private final ItemStack stack;
		private final String description;

		public ItemStringPair(ItemStack stack, String description)
		{
			this.stack = stack;
			this.description = description;
		}

		public ItemStack getStack()
		{
			return this.stack;
		}

		public String getDescription()
		{
			return this.description;
		}
	}

	public void drawBlackBackground()
	{
		final ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		final int var6 = var5.getScaledWidth();
		final int var7 = var5.getScaledHeight();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.mc.getTextureManager().bindTexture(GCCoreGuiChoosePlanet.blackTexture);
		final Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, var7, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV(var6, var7, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV(var6, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		switch (par1GuiButton.id)
		{
		case 0:
			FMLClientHandler.instance().getClient().displayGuiScreen(new GCCoreGuiGalaxyMap(this.playerToSend, this.destinations));
			break;
		case 1:
			if (par1GuiButton.enabled)
			{
				final String dimension = this.destinations[this.selectedSlot];
				final Object[] toSend = { dimension };
				if (dimension.contains("$"))
				{
					this.mc.gameSettings.thirdPersonView = 0;
				}
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.TELEPORT_ENTITY, toSend));
				return;
			}
			else
			{
				GCLog.severe("Severe problem when trying to teleport " + this.playerToSend.username);
			}
			break;
		case 2:
			final SpaceStationRecipe recipe = WorldUtil.getSpaceStationRecipe(this.getDimensionIdFromSlot());
			if (recipe != null && par1GuiButton != null && par1GuiButton.enabled && recipe.matches(this.playerToSend, false))
			{
				final Object[] toSend = { this.getDimensionIdFromSlot() };
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.BIND_SPACE_STATION_ID, toSend));
				par1GuiButton.enabled = false;
				return;
			}
			break;
		case 3:
			if (par1GuiButton != null && par1GuiButton.equals(this.renameSpaceStationButton))
			{
				PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.RENAME_SPACE_STATION, new Object[] { this.renameText, this.getDimensionIdFromSlot() }));
				this.renameText = "";
			}
			break;
		}
	}

	private int getDimensionIdFromSlot()
	{
		String dimension = this.destinations[this.selectedSlot];
		dimension = dimension.replace("*", "");
		final WorldProvider provider = WorldUtil.getProviderForName(dimension);

		if (provider == null)
		{
			throw new NullPointerException("Could not find world provider for dimension: " + dimension);
		}

		return provider.dimensionId;
	}

	public boolean isValidDestination(int i)
	{
		final String str = this.destinations[i];

		if (str.contains("*"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean canCreateSpaceStation()
	{
		if (ClientProxyCore.clientSpaceStationID == 0)
		{
			return true;
		}
		else if (ClientProxyCore.clientSpaceStationID == -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean playerAlreadyCreatedDimension()
	{
		if (ClientProxyCore.clientSpaceStationID != 0 && ClientProxyCore.clientSpaceStationID != -1)
		{
			return true;
		}

		return false;
	}

	public boolean hasSpacestation(int i)
	{
		final String str = this.destinations[i];

		if (str.contains("$"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	static EntityPlayer getPlayerToSend(GCCoreGuiChoosePlanet par0GuiLanguage)
	{
		return par0GuiLanguage.playerToSend;
	}

	static String[] getDestinations(GCCoreGuiChoosePlanet par0GuiLanguage)
	{
		return par0GuiLanguage.destinations;
	}

	static GuiSmallButton getSendButton(GCCoreGuiChoosePlanet par0GuiLanguage)
	{
		return par0GuiLanguage.sendButton;
	}

	static GuiSmallButton getCreateSpaceStationButton(GCCoreGuiChoosePlanet par0GuiLanguage)
	{
		return par0GuiLanguage.createSpaceStationButton;
	}

	static int setSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage, int par1)
	{
		return par0GuiLanguage.selectedSlot = par1;
	}

	static int getSelectedDimension(GCCoreGuiChoosePlanet par0GuiLanguage)
	{
		return par0GuiLanguage.selectedSlot;
	}
}
