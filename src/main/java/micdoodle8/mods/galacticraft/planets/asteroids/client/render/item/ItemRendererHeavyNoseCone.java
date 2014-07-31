package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.util.VersionUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRendererHeavyNoseCone implements IItemRenderer
{
	private void renderHeavyNoseCone(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);

		for (int i = 0; i < 2; i++)
		{
			GL11.glPushMatrix();

			if (i == 1)
			{
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(FMLClientHandler.instance().getClientPlayerEntity().getGameProfile().getName());
                Vector3 color = null;

                if (race != null)
                {
                    color = race.getTeamColor();
                }

                if (color == null)
                {
                    color = new Vector3(1, 1, 1);
                }

				GL11.glColor4f(color.floatX(), color.floatY(), color.floatZ(), 1.0F);
			}

			IIcon iicon = FMLClientHandler.instance().getClientPlayerEntity().getItemIcon(item, i);

			if (iicon == null)
			{
				GL11.glPopMatrix();
				return;
			}

			FMLClientHandler.instance().getClient().getTextureManager().bindTexture(FMLClientHandler.instance().getClient().getTextureManager().getResourceLocation(item.getItemSpriteNumber()));
            VersionUtil.setMipMap(false, false);
			Tessellator tessellator = Tessellator.instance;
			float f = iicon.getMinU();
			float f1 = iicon.getMaxU();
			float f2 = iicon.getMinV();
			float f3 = iicon.getMaxV();
			float f4 = 0.0F;
			float f5 = 1.0F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(1.0F, -1.0F, 1.0F);
			float f6 = 16.0F;
			GL11.glScalef(f6, f6, f6);
			GL11.glTranslatef(-f4, -f5, 0.0F);
			ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(), iicon.getIconHeight(), 0.0625F);
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
	}

	/** IItemRenderer implementation **/

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
		case ENTITY:
			return false;
		case EQUIPPED:
			return false;
		case EQUIPPED_FIRST_PERSON:
			return false;
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		switch (helper)
		{
		case INVENTORY_BLOCK:
			return false;
		default:
			return false;
		}
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case INVENTORY:
			this.renderHeavyNoseCone(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		default:
			break;
		}
	}

}
