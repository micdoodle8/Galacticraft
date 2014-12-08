package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class ItemRendererWalkway implements IItemRenderer
{
    private static final ResourceLocation textureMain = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/blocks/walkway.png");
    private static final ResourceLocation textureWire = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/aluminumWire.png");
    private static final ResourceLocation texturePipe = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/pipe_oxygen_white.png");
    public static IModelCustom modelWalkway;

    public ItemRendererWalkway()
    {
        modelWalkway = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/walkway.obj"));
    }

    private void renderWalkway(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
    {
        GL11.glPushMatrix();
        this.transform(type);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);

        FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureMain);
        modelWalkway.renderPart("Walkway");

        if (item.getItem() == Item.getItemFromBlock(AsteroidBlocks.blockWalkway))
        {
            modelWalkway.renderPart("WalkwayBase");
        }
        else if (item.getItem() == Item.getItemFromBlock(AsteroidBlocks.blockWalkwayWire))
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureWire);
            modelWalkway.renderPart("Wire");
        }
        else if (item.getItem() == Item.getItemFromBlock(AsteroidBlocks.blockWalkwayOxygenPipe))
        {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(texturePipe);
            modelWalkway.renderPart("Pipe");
        }


        GL11.glPopMatrix();
    }

    public void transform(ItemRenderType type)
    {
        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(0.6F, 0.2F, 0.6F);
            GL11.glRotatef(185, 1, 0, 0);
            GL11.glRotatef(40, 0, 1, 0);
            GL11.glRotatef(0, 0, 0, 1);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glScalef(1.6F, 1.6F, 1.6F);
            GL11.glTranslatef(-0.1F, 0.4F, 0.35F);
            GL11.glRotatef(180, 0, 0, 1);
        }

        GL11.glScalef(-0.2F, -0.2F, 0.2F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glTranslatef(0.0F, 4.0F, 0.1F);
                GL11.glScalef(2.1F, 2.1F, 2.1F);
                GL11.glRotatef(180, 0, 0, 1);
                GL11.glRotatef(-90, 0, 1, 0);
            }
            else
            {
                GL11.glRotatef(180, 0, 0, 1);
                GL11.glScalef(1.8F, 1.8F, 1.8F);
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
            this.renderWalkway(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case EQUIPPED_FIRST_PERSON:
            this.renderWalkway(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case INVENTORY:
            this.renderWalkway(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        case ENTITY:
            this.renderWalkway(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
            break;
        default:
            break;
        }
    }

}
