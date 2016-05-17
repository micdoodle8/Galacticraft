//package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;
//
//import net.minecraft.util.EnumFacing;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
//import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
//import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//
//import org.lwjgl.opengl.GL11;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityBeamReceiverRenderer extends TileEntitySpecialRenderer
//{
//    public static final ResourceLocation receiverTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/beamReceiver.png");
//    public static IModelCustom receiverModel;
//
//    public TileEntityBeamReceiverRenderer()
//    {
//        TileEntityBeamReceiverRenderer.receiverModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/receiver.obj"));
//    }
//
//    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f1, int f2)
//    {
//        TileEntityBeamReceiver tileEntity = (TileEntityBeamReceiver) tile;
//        // Texture file
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReceiverRenderer.receiverTexture);
//
//        GL11.glPushMatrix();
//
//        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
//        GL11.glScalef(0.85F, 0.85F, 0.85F);
//
//        switch (tileEntity.facing)
//        {
//        case DOWN:
//            GL11.glTranslatef(0.7F, -0.15F, 0.0F);
//            GL11.glRotatef(90, 0, 0, 1);
//            break;
//        case UP:
//            GL11.glTranslatef(-0.7F, 1.3F, 0.0F);
//            GL11.glRotatef(-90, 0, 0, 1);
//            break;
//        case EAST:
//            GL11.glTranslatef(0.7F, -0.15F, 0.0F);
//            GL11.glRotatef(180, 0, 1, 0);
//            break;
//        case SOUTH:
//            GL11.glTranslatef(0.0F, -0.15F, 0.7F);
//            GL11.glRotatef(90, 0, 1, 0);
//            break;
//        case WEST:
//            GL11.glTranslatef(-0.7F, -0.15F, 0.0F);
//            GL11.glRotatef(0, 0, 1, 0);
//            break;
//        case NORTH:
//            GL11.glTranslatef(0.0F, -0.15F, -0.7F);
//            GL11.glRotatef(270, 0, 1, 0);
//            break;
//        default:
//            GL11.glPopMatrix();
//            return;
//        }
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Main");
//
//        if (tileEntity.modeReceive == ReceiverMode.RECEIVE.ordinal())
//        {
//            GL11.glColor3f(0.0F, 0.8F, 0.0F);
//        }
//        else if (tileEntity.modeReceive == ReceiverMode.EXTRACT.ordinal())
//        {
//            GL11.glColor3f(0.6F, 0.0F, 0.0F);
//        }
//        else
//        {
//            GL11.glColor3f(0.1F, 0.1F, 0.1F);
//        }
//
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Receiver");
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        float dX = 0.34772F;
//        float dY = 0.75097F;
//        float dZ = 0.0F;
//        GL11.glTranslatef(dX, dY, dZ);
//        if (tileEntity.modeReceive != ReceiverMode.UNDEFINED.ordinal())
//        {
//            GL11.glRotatef(-tileEntity.ticks * 50, 1, 0, 0);
//        }
//        GL11.glTranslatef(-dX, -dY, -dZ);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        TileEntityBeamReceiverRenderer.receiverModel.renderPart("Ring");
//
//        GL11.glPopMatrix();
//    }
//}
