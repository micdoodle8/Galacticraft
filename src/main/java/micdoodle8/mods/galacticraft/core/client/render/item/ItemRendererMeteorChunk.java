//package micdoodle8.mods.galacticraft.core.client.render.item;
//
//import net.minecraftforge.fml.client.FMLClientHandler;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.IItemRenderer;
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import org.lwjgl.opengl.GL11;
//
//public class ItemRendererMeteorChunk implements IItemRenderer
//{
//    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/meteorChunk.png");
//    private static final ResourceLocation meteorChunkHotTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/meteorChunkHot.png");
//
//    private final IModelCustom meteorChunkModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/meteorChunk.obj"));
//
//    private void renderMeteorChunk(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
//    {
//        GL11.glPushMatrix();
//
//        GL11.glScalef(0.7F, 0.7F, 0.7F);
//
//        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
//        {
//            GL11.glTranslatef(1.4F, 1.0F, 0.0F);
//            GL11.glRotatef(180.0F, 1, 0, 0);
//        }
//
//        if (type == ItemRenderType.EQUIPPED)
//        {
//            GL11.glTranslatef(1.4F, 1.0F, 0.5F);
//        }
//
//        if (item.getItemDamage() == 0)
//        {
//            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(ItemRendererMeteorChunk.meteorChunkTexture);
//        }
//        else
//        {
//            FMLClientHandler.instance().getClient().getTextureManager().bindTexture(ItemRendererMeteorChunk.meteorChunkHotTexture);
//        }
//        this.meteorChunkModel.renderAll();
//
//        GL11.glPopMatrix();
//    }
//
//    /**
//     * IItemRenderer implementation *
//     */
//
//    @Override
//    public boolean handleRenderType(ItemStack item, ItemRenderType type)
//    {
//        switch (type)
//        {
//        case ENTITY:
//            return true;
//        case EQUIPPED:
//            return true;
//        case EQUIPPED_FIRST_PERSON:
//            return true;
//        case INVENTORY:
//            return true;
//        default:
//            return false;
//        }
//    }
//
//    @Override
//    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
//    {
//        return true;
//    }
//
//    @Override
//    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
//    {
//        switch (type)
//        {
//        case EQUIPPED:
//            this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case EQUIPPED_FIRST_PERSON:
//            this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case INVENTORY:
//            this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case ENTITY:
//            this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        default:
//        }
//    }
//
//}
