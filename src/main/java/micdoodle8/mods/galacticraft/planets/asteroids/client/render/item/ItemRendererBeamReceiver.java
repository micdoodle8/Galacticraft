package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityBeamReceiverRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ItemRendererBeamReceiver implements IItemRenderer
{
    private void renderBeamReceiver(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        this.transform(type);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReceiverRenderer.receiverTexture);
        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Main");
        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Ring");

        GL11.glColor3f(0.6F, 0.3F, 0.0F);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Receiver");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }

    public void transform(ItemRenderType type)
    {
        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(1.2F, 0.5F, 1.15F);
            GL11.glRotatef(185, 1, 0, 0);
            GL11.glRotatef(40, 0, 1, 0);
            GL11.glRotatef(-70, 0, 0, 1);
            GL11.glScalef(3.2F, 3.2F, 3.2F);
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            GL11.glTranslatef(-0.1F, 1.0F, 0.35F);
        }

        GL11.glScalef(-0.4F, -0.4F, 0.4F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glTranslatef(0.0F, 2.45F, -0.8F);
                GL11.glScalef(3.0F, 3.0F, 3.0F);
                GL11.glRotatef(180, 0, 0, 1);
                GL11.glRotatef(-90, 0, 1, 0);
            }
            else
            {
                GL11.glTranslatef(0, -3.9F, 0);
                GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
                GL11.glScalef(4.0F, 4.0F, 4.0F);
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
            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case EQUIPPED_FIRST_PERSON:
            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case INVENTORY:
            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case ENTITY:
            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        default:
            break;
        }
    }

}
