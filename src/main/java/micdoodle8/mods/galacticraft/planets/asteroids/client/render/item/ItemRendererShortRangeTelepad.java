//package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;
//
//import net.minecraftforge.fml.client.FMLClientHandler;
//import micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile.TileEntityShortRangeTelepadRenderer;
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.client.IItemRenderer;
//import org.lwjgl.opengl.GL11;
//
//public class ItemRendererShortRangeTelepad implements IItemRenderer
//{
//    private void renderBeamReceiver(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
//    {
//        GL11.glPushMatrix();
//        this.transform(type);
//
//        GL11.glDisable(GL11.GL_BLEND);
//
//        GL11.glColor3f(1.0F, 1.0F, 1.0F);
//
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture);
//
//        if (type == ItemRenderType.INVENTORY)
//        {
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Base");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Top");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture0);
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxNegz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxNegz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxNegz");
//
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxMidz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxMidz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxMidz");
//
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxPosz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxPosz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxPosz");
//        }
//        else
//        {
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Base");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Top");
//
//            FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture0);
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxNegz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxNegz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxNegz");
//
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxMidz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxMidz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxMidz");
//
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxPosz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxPosz");
//            TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxPosz");
//        }
//
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glPopMatrix();
//    }
//
//    public void transform(ItemRenderType type)
//    {
//        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
//
//        if (type == ItemRenderType.EQUIPPED)
//        {
//            GL11.glTranslatef(0.55F, 0.45F, 0.6F);
//            GL11.glRotatef(185, 1, 0, 0);
//            GL11.glRotatef(40, 0, 1, 0);
//            GL11.glRotatef(0, 0, 0, 1);
//            GL11.glScalef(0.6F, 0.6F, 0.6F);
//        }
//
//        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
//        {
//            GL11.glRotatef(180.0F, 0.9F, 0F, 0F);
//            GL11.glScalef(1.0F, 1.0F, 1.0F);
//            GL11.glTranslatef(1.0F, -0.6F, -0.5F);
//        }
//
//        GL11.glScalef(-0.4F, -0.4F, 0.4F);
//
//        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
//        {
//            if (type == ItemRenderType.INVENTORY)
//            {
//                GL11.glTranslatef(0.0F, 1.6F, -0.0F);
//                GL11.glScalef(0.5F, 0.5F, 0.5F);
//                GL11.glRotatef(10, 1, 0, 1);
//                GL11.glRotatef(180, 0, 0, 1);
//                GL11.glRotatef(-100, 0, 1, 0);
//            }
//            else
//            {
//                GL11.glRotatef(180.0F, 1F, 0F, 0F);
//            }
//
//            GL11.glScalef(1.3F, 1.3F, 1.3F);
//        }
//
//        //		GL11.glRotatef(30, 1, 0, 0);
//        //		GL11.glScalef(-1F, -1F, 1);
//        //		GL11.glTranslatef(-0.4F, 0.0F, 0.0F);
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
//            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case EQUIPPED_FIRST_PERSON:
//            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case INVENTORY:
//            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        case ENTITY:
//            this.renderBeamReceiver(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
//            break;
//        default:
//            break;
//        }
//    }
//
//}
