package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderFlag;
import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererFlag implements IItemRenderer
{
	private EntityFlag entityFlagDummy = new EntityFlag(FMLClientHandler.instance().getClient().theWorld);
	private ModelFlag modelFlag = new ModelFlag();

	private void renderFlag(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ, Object... data)
	{
		GL11.glPushMatrix();
		long var10 = this.entityFlagDummy.getEntityId() * 493286711L;
		var10 = var10 * var10 * 4392167121L + var10 * 98761L;
		final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

		this.entityFlagDummy.worldObj = FMLClientHandler.instance().getClient().theWorld;
		this.entityFlagDummy.ticksExisted = (int) FMLClientHandler.instance().getWorldClient().getTotalWorldTime();
		this.entityFlagDummy.setType(item.getItemDamage());

		if (type == ItemRenderType.EQUIPPED)
		{
			EntityLivingBase entityHolding = (EntityLivingBase) data[1];

			if (entityHolding instanceof EntityPlayer)
			{
				String playerName = ((EntityPlayer) entityHolding).getGameProfile().getName();

				if (!playerName.equals(this.entityFlagDummy.getOwner()))
				{
					this.entityFlagDummy.setOwner(playerName);
					this.entityFlagDummy.flagData = ClientUtil.updateFlagData(this.entityFlagDummy.getOwner(), true);
				}
			}
		}
		else
		{
			String playerName = FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName();

			if (!playerName.equals(this.entityFlagDummy.getOwner()) || this.entityFlagDummy.ticksExisted % 100 == 0)
			{
				this.entityFlagDummy.setOwner(playerName);
				this.entityFlagDummy.flagData = ClientUtil.updateFlagData(this.entityFlagDummy.getOwner(), true);
			}
		}

		if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glScalef(7F, 7F, 7F);
			GL11.glTranslatef(0.0F, 0.7F, 0.1F);
		}

		if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotatef(170F, 0F, 0F, 1F);
			GL11.glRotatef(-10F, 1F, 0F, 0F);
			GL11.glTranslatef(-0.25F, 1.3F, 0.15F);
			GL11.glRotatef(-145F, 0F, 1F, 0F);
		}

		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			if (FMLClientHandler.instance().getClient().thePlayer.getItemInUseCount() > 0)
			{
				float var13b;
				float var14b;
				var13b = item.getMaxItemUseDuration() - (FMLClientHandler.instance().getClient().thePlayer.getItemInUseCount() + 1.0F);
				var14b = var13b / 20.0F;
				var14b = (var14b * var14b + var14b * 2.0F) / 3.0F;

				if (var14b > 1.0F)
				{
					var14b = 1.0F;
				}

				GL11.glRotatef(MathHelper.sin((var13b - 0.1F) * 0.3F) * 0.01F * (var14b - 0.1F) * 60, 1F, 0F, 0F);

				GL11.glRotatef(var14b * 60F, 1F, 0F, 1F);
				GL11.glTranslatef(0F, -(var14b * 0.2F), 0F);
			}
		}

		GL11.glTranslatef(var12, var13 - 0.1F, var14);
		GL11.glScalef(-0.4F, -0.4F, 0.4F);
		if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
		{
			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glScalef(1.1F, 1.137F, 1.1F);
				GL11.glRotatef(30F, 1F, 0F, 1F);
				GL11.glRotatef(110F, 0F, 1F, 0F);
				GL11.glTranslatef(-0.5F, 0.3F, 0);
			}
			else
			{
				GL11.glTranslatef(0, -0.9F, 0);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
			}

			GL11.glScalef(1.3F, 1.3F, 1.3F);
			GL11.glTranslatef(0, -0.6F, 0);
		}

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderFlag.flagTexture);

		this.modelFlag.render(this.entityFlagDummy, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	/** IItemRenderer implementation **/

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
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

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case EQUIPPED:
			this.renderFlag(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderFlag(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case INVENTORY:
			this.renderFlag(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case ENTITY:
			this.renderFlag(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		default:
		}
	}
}
