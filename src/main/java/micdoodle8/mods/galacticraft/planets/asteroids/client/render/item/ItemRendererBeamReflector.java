package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReflectorRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ItemRendererBeamReflector implements IItemRenderer
{
    private void renderBeamReflector(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        this.transform(type);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReflectorRenderer.reflectorTexture);
        TileEntityBeamReflectorRenderer.reflectorModel.renderAll();
        GL11.glPopMatrix();
    }

    public void transform(ItemRenderType type)
    {
        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glRotatef(185, 1, 0, 0);
            GL11.glRotatef(40, 0, 1, 0);
            GL11.glRotatef(-70, 0, 0, 1);
            GL11.glScalef(3.2F, 3.2F, 3.2F);
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glScalef(8.2F, 8.2F, 8.2F);
            GL11.glTranslatef(0.291F, -0.15F, 0.1F);
            GL11.glRotatef(180, 0, 0, 1);
        }

        GL11.glScalef(-0.4F, -0.4F, 0.4F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glTranslatef(0.0F, 1.45F, 0.0F);
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                GL11.glRotatef(180, 0, 0, 1);
                GL11.glRotatef(180, 0, 1, 0);
            }
            else
            {
                GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
                GL11.glScalef(2F, -2F, 2F);
            }

            GL11.glScalef(1.3F, 1.3F, 1.3F);
        }
    }

    /**
     * IItemRenderer implementation *
     */

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
            this.renderBeamReflector(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case EQUIPPED_FIRST_PERSON:
            this.renderBeamReflector(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case INVENTORY:
            this.renderBeamReflector(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case ENTITY:
            this.renderBeamReflector(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        default:
            break;
        }
    }

}
