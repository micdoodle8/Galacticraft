package micdoodle8.mods.galacticraft.core.client.render.entities;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelPlayer;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import org.lwjgl.opengl.GL11;

/**
 * GCCoreRenderPlayer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreRenderPlayer extends RenderPlayer
{
	public GCCoreRenderPlayer()
	{
		super();
		this.mainModel = new GCCoreModelPlayer(0.0F);
		this.modelBipedMain = (GCCoreModelPlayer) this.mainModel;
		this.modelArmorChestplate = new GCCoreModelPlayer(1.0F);
		this.modelArmor = new GCCoreModelPlayer(0.5F);
	}

	public ModelBiped getModel()
	{
		return this.modelBipedMain;
	}

	@Override
	protected void renderSpecials(AbstractClientPlayer par1AbstractClientPlayer, float par2)
	{
		RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(par1AbstractClientPlayer, this, par2);
		if (MinecraftForge.EVENT_BUS.post(event))
		{
			return;
		}

		float f1 = 1.0F;
		GL11.glColor3f(f1, f1, f1);
		super.renderArrowsStuckInEntity(par1AbstractClientPlayer, par2);
		ItemStack itemstack = par1AbstractClientPlayer.inventory.armorItemInSlot(3);

		if (itemstack != null && event.renderHelmet)
		{
			GL11.glPushMatrix();
			this.modelBipedMain.bipedHead.postRender(0.0625F);
			float f2;

			if (itemstack != null && itemstack.getItem() instanceof ItemBlock)
			{
				IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
				boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D);

				if (is3D || RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
				{
					f2 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(f2, -f2, -f2);
				}

				this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack, 0);
			}
			else if (itemstack.getItem().itemID == Item.skull.itemID)
			{
				f2 = 1.0625F;
				GL11.glScalef(f2, -f2, -f2);
				String s = "";

				if (itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("SkullOwner"))
				{
					s = itemstack.getTagCompound().getString("SkullOwner");
				}

				TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack.getItemDamage(), s);
			}

			GL11.glPopMatrix();
		}

		boolean flag = par1AbstractClientPlayer.getTextureCape().isTextureUploaded();
		boolean flag1 = !par1AbstractClientPlayer.isInvisible();
		boolean flag2 = !par1AbstractClientPlayer.getHideCape();
		flag = event.renderCape && flag;
		float f6;

		if (flag && flag1 && flag2)
		{
			this.bindTexture(par1AbstractClientPlayer.getLocationCape());
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			double d0 = par1AbstractClientPlayer.field_71091_bM + (par1AbstractClientPlayer.field_71094_bP - par1AbstractClientPlayer.field_71091_bM) * par2 - (par1AbstractClientPlayer.prevPosX + (par1AbstractClientPlayer.posX - par1AbstractClientPlayer.prevPosX) * par2);
			double d1 = par1AbstractClientPlayer.field_71096_bN + (par1AbstractClientPlayer.field_71095_bQ - par1AbstractClientPlayer.field_71096_bN) * par2 - (par1AbstractClientPlayer.prevPosY + (par1AbstractClientPlayer.posY - par1AbstractClientPlayer.prevPosY) * par2);
			double d2 = par1AbstractClientPlayer.field_71097_bO + (par1AbstractClientPlayer.field_71085_bR - par1AbstractClientPlayer.field_71097_bO) * par2 - (par1AbstractClientPlayer.prevPosZ + (par1AbstractClientPlayer.posZ - par1AbstractClientPlayer.prevPosZ) * par2);
			f6 = par1AbstractClientPlayer.prevRenderYawOffset + (par1AbstractClientPlayer.renderYawOffset - par1AbstractClientPlayer.prevRenderYawOffset) * par2;
			double d3 = MathHelper.sin(f6 * (float) Math.PI / 180.0F);
			double d4 = -MathHelper.cos(f6 * (float) Math.PI / 180.0F);
			float f7 = (float) d1 * 10.0F;

			if (f7 < -6.0F)
			{
				f7 = -6.0F;
			}

			if (f7 > 32.0F)
			{
				f7 = 32.0F;
			}

			float f8 = (float) (d0 * d3 + d2 * d4) * 100.0F;
			float f9 = (float) (d0 * d4 - d2 * d3) * 100.0F;

			if (f8 < 0.0F)
			{
				f8 = 0.0F;
			}

			float f10 = par1AbstractClientPlayer.prevCameraYaw + (par1AbstractClientPlayer.cameraYaw - par1AbstractClientPlayer.prevCameraYaw) * par2;
			f7 += MathHelper.sin((par1AbstractClientPlayer.prevDistanceWalkedModified + (par1AbstractClientPlayer.distanceWalkedModified - par1AbstractClientPlayer.prevDistanceWalkedModified) * par2) * 6.0F) * 32.0F * f10;

			if (par1AbstractClientPlayer.isSneaking())
			{
				f7 += 25.0F;
			}

			GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelBipedMain.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

		ItemStack itemstack1 = par1AbstractClientPlayer.inventory.getCurrentItem();

		if (itemstack1 != null && event.renderItem)
		{
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (par1AbstractClientPlayer.fishEntity != null)
			{
				itemstack1 = new ItemStack(Item.stick);
			}

			EnumAction enumaction = null;

			if (par1AbstractClientPlayer.getItemInUseCount() > 0)
			{
				enumaction = itemstack1.getItemUseAction();
			}

			float f11;

			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack1, EQUIPPED);
			boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack1, BLOCK_3D);
			boolean isBlock = itemstack1.itemID < Block.blocksList.length && itemstack1.getItemSpriteNumber() == 0;

			if (is3D || isBlock && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack1.itemID].getRenderType()))
			{
				f11 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				f11 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-f11, -f11, f11);
			}
			else if (itemstack1.itemID == Item.bow.itemID || itemstack1.itemID == GCCoreItems.bowGravity.itemID)
			{
				f11 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(f11, -f11, f11);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			else if (Item.itemsList[itemstack1.itemID].isFull3D())
			{
				f11 = 0.625F;

				if (Item.itemsList[itemstack1.itemID].shouldRotateAroundWhenRendering())
				{
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (par1AbstractClientPlayer.getItemInUseCount() > 0 && enumaction == EnumAction.block)
				{
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(f11, -f11, f11);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			else
			{
				f11 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(f11, f11, f11);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float f12;
			float f13;
			int j;

			if (itemstack1.getItem().requiresMultipleRenderPasses())
			{
				for (j = 0; j < itemstack1.getItem().getRenderPasses(itemstack1.getItemDamage()); ++j)
				{
					int k = itemstack1.getItem().getColorFromItemStack(itemstack1, j);
					f13 = (k >> 16 & 255) / 255.0F;
					f12 = (k >> 8 & 255) / 255.0F;
					f6 = (k & 255) / 255.0F;
					GL11.glColor4f(f13, f12, f6, 1.0F);
					this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, j);
				}
			}
			else
			{
				j = itemstack1.getItem().getColorFromItemStack(itemstack1, 0);
				float f14 = (j >> 16 & 255) / 255.0F;
				f13 = (j >> 8 & 255) / 255.0F;
				f12 = (j & 255) / 255.0F;
				GL11.glColor4f(f14, f13, f12, 1.0F);
				this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, itemstack1, 0);
			}

			GL11.glPopMatrix();
		}

		MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(par1AbstractClientPlayer, this, par2));
	}

	@Override
	protected void rotatePlayer(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
	{
		if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
		{
			RotatePlayerEvent event = new RotatePlayerEvent(par1AbstractClientPlayer);
			MinecraftForge.EVENT_BUS.post(event);

			if (event.shouldRotate == null || event.shouldRotate == true)
			{
				super.rotatePlayer(par1AbstractClientPlayer, par2, par3, par4);
			}
		}
		else
		{
			super.rotatePlayer(par1AbstractClientPlayer, par2, par3, par4);
		}
	}

	public static class RotatePlayerEvent extends PlayerEvent
	{
		public Boolean shouldRotate = null;

		public RotatePlayerEvent(AbstractClientPlayer player)
		{
			super(player);
		}
	}
}
