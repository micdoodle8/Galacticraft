package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class ItemRendererAstroMiner implements IItemRenderer
{
    protected IModelCustom modelMiner;
    protected static RenderItem drawItems = new RenderItem();
    protected ResourceLocation texture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/astroMiner_off.png");

    public ItemRendererAstroMiner()
    {
        this.modelMiner = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/astroMiner.obj"));;
    }

    protected void renderMiner(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();

        this.transform(item, type);
        GL11.glScalef(0.06F, 0.06F, 0.06F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.texture);
        this.modelMiner.renderAll();
        GL11.glPopMatrix();
    }

    public void transform(ItemStack itemstack, ItemRenderType type)
    {
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        if (type == ItemRenderType.EQUIPPED)
        {
            float angle1 = 0F;
        	ItemStack s1 = player.inventory.getStackInSlot(0);
            if (s1 != null) angle1 = 32F - 1F * s1.stackSize; 
            float angle2 = 0.0F;
        	ItemStack s2 = player.inventory.getStackInSlot(1);
            if (s2 != null) angle2 = 3.2F - 0.1F * s2.stackSize; 
            	
        	GL11.glRotatef(20F, 0F, 0F, -1.0F);
            GL11.glRotatef(-24F, 0F, 1.0F, 0F);
            GL11.glRotatef(-14F, 1.0F, 0F, 0F);
            GL11.glTranslatef(0.6F, -1.2F, 1.4F);
            GL11.glScalef(4.6F, 4.6F, 4.6F);

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityTier1Rocket)
            {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
             GL11.glTranslatef(-1F, 4.0F, 0F);
            GL11.glRotatef(30F, 0F, 1.0F, 0F);
            GL11.glScalef(5.2F, 5.2F, 5.2F);

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof EntityTier1Rocket)
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
                GL11.glTranslatef(0.0F, 1.6F, -0.4F);
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
