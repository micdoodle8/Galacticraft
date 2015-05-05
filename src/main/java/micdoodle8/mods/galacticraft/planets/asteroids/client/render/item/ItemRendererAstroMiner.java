package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity.RenderAstroMiner;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ItemRendererAstroMiner implements IItemRenderer
{
    protected IModelCustom modelMiner;
    protected IModelCustom modellasergl;
    protected IModelCustom modellasergr;
    protected static RenderItem drawItems = new RenderItem();
    protected ResourceLocation texture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMiner_off.png");

    public ItemRendererAstroMiner()
    {
        this.modelMiner = RenderAstroMiner.modelObj;
        this.modellasergl = RenderAstroMiner.modellasergl;
        this.modellasergr = RenderAstroMiner.modellasergr;
    }

    protected void renderMiner(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPushMatrix();

        this.transform(item, type);
        GL11.glScalef(0.06F, 0.06F, 0.06F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.texture);
        this.modelMiner.renderAll();
        GL11.glTranslatef(1.875F, 0F, 0F);
	    this.modellasergl.renderAll();
        GL11.glTranslatef(-3.75F, 0F, 0F);
	    this.modellasergr.renderAll();
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void transform(ItemStack itemstack, ItemRenderType type)
    {
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if (type == ItemRenderType.EQUIPPED)
        {
        	//The additional offsets cause this to be held one-handed overhead (avoids clipping)
        	GL11.glRotatef(20F + 5F, 0F, 0F, -1.0F);
            GL11.glRotatef(-30F - 1.6F, 0F, 1.0F, 0F);
            GL11.glRotatef(-14F + 29.4F, 1.0F, 0F, 0F);
            GL11.glTranslatef(0.6F + 0.5F, -1.2F -0.2F, 1.4F -1.08F);
            GL11.glScalef(4.6F, 4.6F, 4.6F);

            if (player != null && player.ridingEntity != null && (player.ridingEntity instanceof EntityAutoRocket || player.ridingEntity instanceof EntityLanderBase))
            {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
             GL11.glTranslatef(-1F, 4.0F, 0F);
            GL11.glRotatef(30F, 0F, 1.0F, 0F);
            GL11.glScalef(5.2F, 5.2F, 5.2F);

            if (player != null && player.ridingEntity != null && (player.ridingEntity instanceof EntityAutoRocket || player.ridingEntity instanceof EntityLanderBase))
            {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }

        GL11.glTranslatef(0F, 0.1F, 0F);
        GL11.glScalef(-0.4F, -0.4F, 0.4F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            GL11.glRotatef(Sys.getTime() / 30F % 360F + 45, 0F, 1F, 0F);
            if (type == ItemRenderType.INVENTORY)
            {
                //GL11.glRotatef(85F, 1F, 0F, 1F);
                //GL11.glRotatef(20F, 1F, 0F, 0F);
                GL11.glScalef(0.7F, 0.7F, 0.7F);
                GL11.glTranslatef(0.0F, 1.6F, -0.2F);
            }
            else
            {
                GL11.glTranslatef(0, -0.9F, 0);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            GL11.glScalef(1.3F, 1.3F, 1.3F);
            GL11.glTranslatef(0, -0.6F, 0.25F);
        }

        GL11.glRotatef(180, 0, 0, 1);
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
            this.renderMiner(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case EQUIPPED_FIRST_PERSON:
            this.renderMiner(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case INVENTORY:
            this.renderMiner(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case ENTITY:
            this.renderMiner(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        default:
        }
    }

}
