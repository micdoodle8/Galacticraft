package micdoodle8.mods.galacticraft.core.client.gui;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.event.client.GCCoreEventChoosePlanetGui.SlotClicked;
import micdoodle8.mods.galacticraft.api.world.ICelestialBody;
import micdoodle8.mods.galacticraft.api.world.ICelestialBodyRenderer;
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
public class GCCoreGuiChoosePlanetSlot extends GuiSlot
{
	private final GCCoreGuiChoosePlanet choosePlanetGui;

	public GCCoreGuiChoosePlanetSlot(GCCoreGuiChoosePlanet par1GCGuiChoosePlanet)
	{
		super(FMLClientHandler.instance().getClient(), par1GCGuiChoosePlanet.width, par1GCGuiChoosePlanet.height, 32, par1GCGuiChoosePlanet.height - 32, 20);
		this.choosePlanetGui = par1GCGuiChoosePlanet;
	}

	@Override
	protected int getSize()
	{
		return GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui).length;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void elementClicked(int par1, boolean par2)
	{
		if (par1 < GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui).length)
		{
			GCCoreGuiChoosePlanet.setSelectedDimension(this.choosePlanetGui, par1);

			if (par1 != this.choosePlanetGui.selectedSlot)
			{
				SlotClicked event = new SlotClicked(new ArrayList<GuiButton>(), this);
				MinecraftForge.EVENT_BUS.post(event);

				this.choosePlanetGui.buttonList.addAll(event.buttonList);
			}
		}

		GCCoreGuiChoosePlanet.getSendButton(this.choosePlanetGui).displayString = StatCollector.translateToLocal("gui.button.sendtodim.name");
		GCCoreGuiChoosePlanet.getSendButton(this.choosePlanetGui).enabled = this.choosePlanetGui.isValidDestination(this.choosePlanetGui.selectedSlot);

		GCCoreGuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).displayString = StatCollector.translateToLocal("gui.button.createsstation.name");
		GCCoreGuiChoosePlanet.getCreateSpaceStationButton(this.choosePlanetGui).enabled = this.choosePlanetGui.canCreateSpaceStation();
	}

	@Override
	protected boolean isSelected(int par1)
	{
		return par1 == GCCoreGuiChoosePlanet.getSelectedDimension(this.choosePlanetGui);
	}

	@Override
	protected int getContentHeight()
	{
		return this.getSize() * 20;
	}

	@Override
	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator)
	{
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1].toLowerCase();
		if (this.isSelected(par1))
		{
			for (int i = 0; i < GalacticraftRegistry.getCelestialBodies().size(); i++)
			{
				ICelestialBody celestialBody = GalacticraftRegistry.getCelestialBodies().get(i);

				if (celestialBody != null && celestialBody.getMapObject().getSlotRenderer() != null)
				{
					ICelestialBodyRenderer renderer = celestialBody.getMapObject().getSlotRenderer();

					String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1].toLowerCase();

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

					if (renderer.getPlanetName().toLowerCase().equals(str))
					{
						FMLClientHandler.instance().getClient().renderEngine.bindTexture(renderer.getPlanetSprite());

						renderer.renderSlot(par1, par2 - 18, par3 + 9, par4 + 3, par5Tessellator);
					}
				}
			}

			if (this.choosePlanetGui.isValidDestination(par1))
			{
				String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];

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

				this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
			}
			else
			{
				String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];
				str = str.replace("*", "");

				this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
			}
		}
		else
		{
			if (this.choosePlanetGui.isValidDestination(par1))
			{
				String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];

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

				this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
			}
			else
			{
				String str = GCCoreGuiChoosePlanet.getDestinations(this.choosePlanetGui)[par1];
				str = str.replace("*", "");

				this.choosePlanetGui.drawCenteredString(this.choosePlanetGui.getFontRenderer(), str, this.choosePlanetGui.width / 2, par3 + 3, 0xEEEEEE);
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

	public GCCoreGuiChoosePlanet getParentGui()
	{
		return this.choosePlanetGui;
	}
}
