package micdoodle8.mods.galacticraft.mars.client.render.item;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockMachine;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCMarsItemRendererMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemRendererMachine implements IItemRenderer
{
	private static final ResourceLocation chamberTexture0 = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/chamber_dark.png");
	private static final ResourceLocation chamberTexture1 = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/chamber2_dark.png");

	private IModelCustom model;

	public GCMarsItemRendererMachine(IModelCustom model)
	{
		this.model = model;
	}

	private void renderCryogenicChamber(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();

		this.transform(type);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCMarsItemRendererMachine.chamberTexture0);
		this.model.renderPart("Main_Cylinder");
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCMarsItemRendererMachine.chamberTexture1);
		this.model.renderPart("Shield_Torus");
		GL11.glPopMatrix();
	}

	public void transform(ItemRenderType type)
	{
		final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

		if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotatef(70, 1.0F, 0, 0);
			GL11.glRotatef(-10, 0.0F, 1, 0);
			GL11.glRotatef(50, 0.0F, 1, 1);
			GL11.glScalef(5.2F, 5.2F, 5.2F);
			GL11.glTranslatef(0.25F, 1.2F, 0F);

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
			{
				GL11.glScalef(0.0F, 0.0F, 0.0F);
			}
		}

		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslatef(0.0F, -0.9F, 0.0F);
			GL11.glRotatef(0, 0, 0, 1);
			GL11.glRotatef(45, 0, 1, 0);
			GL11.glRotatef(90, 1, 0, 0);
			GL11.glTranslatef(5.5F, 7.0F, -8.5F);
			GL11.glScalef(8.2F, 8.2F, 8.2F);

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
			{
				GL11.glScalef(0.0F, 0.0F, 0.0F);
			}
		}

		GL11.glScalef(-0.4F, -0.4F, 0.4F);

		if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
		{
			GL11.glTranslatef(0, -0.55F, 0);

			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glTranslatef(0, -1.2F, 0);
				GL11.glScalef(0.7F, 0.6F, 0.7F);
				GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
			}
			else
			{
				GL11.glTranslatef(0, -3.9F, 0);
				GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
			}

			GL11.glScalef(1.3F, 1.3F, 1.3F);
		}
	}

	/** IItemRenderer implementation **/

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		if (item.getItemDamage() >= GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA && item.getItemDamage() < GCMarsBlockMachine.LAUNCH_CONTROLLER_METADATA)
		{
			switch (type)
			{
			case ENTITY:
				return true;
			case EQUIPPED:
				return true;
			case EQUIPPED_FIRST_PERSON:
				return true;
			case INVENTORY:
				return true;
			default:
				return false;
			}
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		if (item.getItemDamage() >= GCMarsBlockMachine.CRYOGENIC_CHAMBER_METADATA && item.getItemDamage() < GCMarsBlockMachine.LAUNCH_CONTROLLER_METADATA)
		{
			switch (type)
			{
			case EQUIPPED:
				this.renderCryogenicChamber(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			case EQUIPPED_FIRST_PERSON:
				this.renderCryogenicChamber(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			case INVENTORY:
				this.renderCryogenicChamber(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			case ENTITY:
				this.renderCryogenicChamber(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			default:
				break;
			}
		}
	}

}
