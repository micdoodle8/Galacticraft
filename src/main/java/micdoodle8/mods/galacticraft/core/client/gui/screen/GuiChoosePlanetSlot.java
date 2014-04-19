package micdoodle8.mods.galacticraft.core.client.gui.screen;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.event.client.GCCoreEventChoosePlanetGui.SlotClicked;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreGuiChoosePlanetSlot.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GuiChoosePlanetSlot extends GuiSlot
{
	private final GuiChoosePlanet choosePlanetGui;

	public GuiChoosePlanetSlot(GuiChoosePlanet par1GCGuiChoosePlanet)
	{
		super(FMLClientHandler.instance().getClient(), par1GCGuiChoosePlanet.width, par1GCGuiChoosePlanet.height, 32, par1GCGuiChoosePlanet.height - 32, 20);
		this.choosePlanetGui = par1GCGuiChoosePlanet;
	}

	@Override
	protected int getSize()
	{
		return GuiChoosePlanet.getDestinations(this.choosePlanetGui).length;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4)
	{
		if (var1 < GuiChoosePlanet.getDestinations(this.choosePlanetGui).length)
		{
			GuiChoosePlanet.setSelectedDimension(this.choosePlanetGui, var1);

			if (var1 != this.choosePlanetGui.selectedSlot)
			{
				SlotClicked event = new SlotClicked(new ArrayList<GuiButton>(), this);
				MinecraftForge.EVENT_BUS.post(event);

				this.choosePlanetGui.buttonList.addAll(event.buttonList);
			}
		}

		GuiChoosePlanet.getSendButton(this.choosePlanetGui).displayString = StatCollector.translateToLocal("gui.button.sendtodim.name");
		GuiChoosePlanet.getSendButton(this.choosePlanetGui).enabled = this.choosePlanetGui.isValidDestination(this.choosePlanetGui.selectedSlot);

		GuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).displayString = StatCollector.translateToLocal("gui.button.createsstation.name");
		GuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).enabled = this.choosePlanetGui.canCreateSpaceStation();
	}

	@Override
	protected boolean isSelected(int par1)
	{
		return par1 == GuiChoosePlanet.getSelectedDimension(this.choosePlanetGui);
	}

	@Override
	protected int getContentHeight()
	{
		return this.getSize() * 20;
	}

	@Override
	protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5, int var6, int var7)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1].toLowerCase();

		if (this.isSelected(var1))
		{
			ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
			cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
			cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

			for (CelestialBody celestialBody : cBodyList)
			{
				if (celestialBody != null)
				{
					String str = GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1].toLowerCase();

					if (str.contains("*"))
					{
						str = str.replace("*", "");
					}

					if (str.contains("$"))
					{
						final String[] twoDimensions = str.split("\\$");

						if (twoDimensions.length > 2)
						{
							str = twoDimensions[2];
						}
						else
						{
							str = "";
						}
					}

					if (celestialBody.getLocalizedName().equals(str))
					{
						FMLClientHandler.instance().getClient().renderEngine.bindTexture(celestialBody.getPlanetIcon());
						GuiGalaxyMap.renderPlanet(var1, var2 - 18, var3 + 9, var4 + 3, var5);
					}
				}
			}

			if (this.choosePlanetGui.isValidDestination(var1))
			{
				String str = GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1];

				if (str.contains("$"))
				{
					final String[] strs = str.split("\\$");

					if (strs.length > 2)
					{
						str = strs[2];
					}
					else
					{
						str = "";
					}
				}
				else
				{
					str = StatCollector.translateToLocal("dimension." + str + ".name");
				}

				this.choosePlanetGui.drawCenteredString(GuiChoosePlanet.getFontRenderer(this.choosePlanetGui), str, this.choosePlanetGui.width / 2, var3 + 3, 0xEEEEEE);
			}
			else
			{
				String str = GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1];
				str = str.replace("*", "");

				this.choosePlanetGui.drawCenteredString(GuiChoosePlanet.getFontRenderer(this.choosePlanetGui), str, this.choosePlanetGui.width / 2, var3 + 3, 0xEEEEEE);
			}
		}
		else
		{
			if (this.choosePlanetGui.isValidDestination(var1))
			{
				String str = GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1];

				if (str.contains("$"))
				{
					final String[] strs = str.split("\\$");

					if (strs.length > 2)
					{
						str = strs[2];
					}
					else
					{
						str = "";
					}
				}
				else
				{
					str = StatCollector.translateToLocal("dimension." + str + ".name");
				}

				this.choosePlanetGui.drawCenteredString(GuiChoosePlanet.getFontRenderer(this.choosePlanetGui), str, this.choosePlanetGui.width / 2, var3 + 3, 0xEEEEEE);
			}
			else
			{
				String str = GuiChoosePlanet.getDestinations(this.choosePlanetGui)[var1];
				str = str.replace("*", "");

				this.choosePlanetGui.drawCenteredString(GuiChoosePlanet.getFontRenderer(this.choosePlanetGui), str, this.choosePlanetGui.width / 2, var3 + 3, 0xEEEEEE);
			}
		}

		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	protected void drawBackground()
	{
	}

	@Override
	public void drawContainerBackground(Tessellator par1Tessellator)
	{
		this.choosePlanetGui.drawBlackBackground();
		this.choosePlanetGui.renderSkybox(1);
	}

	public GuiChoosePlanet getParentGui()
	{
		return this.choosePlanetGui;
	}
}