package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRendererGrappleHook implements IItemRenderer
{
    public static final ResourceLocation grappleTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/grapple.png");

    public static IModelCustom modelGrapple;

    public ItemRendererGrappleHook(IModelCustom modelGrapple)
    {
        ItemRendererGrappleHook.modelGrapple = modelGrapple;
    }

    private void renderGrappleGun(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        if (type == ItemRenderType.INVENTORY)
        {
            GL11.glPushMatrix();
            GL11.glScalef(0.7F, 0.75F, 0.5F);
            GL11.glTranslatef(0.5F, -0.2F, -0.5F);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderManager.instance.itemRenderer.renderItem(FMLClientHandler.instance().getClientPlayerEntity(), new ItemStack(Items.string, 1), 0, ItemRenderType.INVENTORY);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        this.transform(type);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(ItemRendererGrappleHook.grappleTexture);
        ItemRendererGrappleHook.modelGrapple.renderAll();
        GL11.glPopMatrix();
    }

    public void transform(ItemRenderType type)
    {
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glRotatef(185, 1, 0, 0);
            GL11.glRotatef(40, 0, 1, 0);
            GL11.glRotatef(-70, 0, 0, 1);
            GL11.glScalef(3.2F, 3.2F, 3.2F);
            GL11.glTranslatef(0.0F, 0.2F, 0.0F);
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glScalef(8.2F, 8.2F, 8.2F);
            GL11.glTranslatef(0.291F, 0.1F, -0.4F);
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glRotatef(136, 0, 1, 0);
            GL11.glRotatef(-5, 0, 0, 1);
        }

        GL11.glScalef(-0.4F, -0.4F, 0.4F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glScalef(1.55F, 1.55F, 1.55F);
                GL11.glRotatef(170, 1, 0, 0);
                GL11.glRotatef(95, 0, 1, 0);
                GL11.glRotatef(0F, 0, 0, 1);
                GL11.glTranslatef(-0.5F, 0.0F, 0.0F);
            }
            else
            {
                GL11.glTranslatef(0, -3.9F, 0);
                GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
            }

            GL11.glScalef(1.3F, 1.3F, 1.3F);
        }

        GL11.glRotatef(30, 1, 0, 0);
        GL11.glScalef(-1F, -1F, 1);
        GL11.glTranslatef(-0.4F, 0.0F, 0.0F);
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
            this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case EQUIPPED_FIRST_PERSON:
            this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case INVENTORY:
            this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case ENTITY:
            this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        default:
            break;
        }
    }

}
