//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.blocks.BlockScreen;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GLAllocation;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//import java.nio.FloatBuffer;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityScreenRenderer extends TileEntityRenderer<TileEntityScreen>
//{
//    public static final ResourceLocation blockTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/blocks/screen_side.png");
//    private final TextureManager textureManager = Minecraft.getInstance().textureManager;
//    private static final FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
//
//    private final float yPlane = 0.91F;
//    float frame = 0.098F;
//
//    @Override
//    public void render(TileEntityScreen screen, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        GL11.glPushMatrix();
//        // Texture file
//        this.textureManager.bindTexture(TileEntityScreenRenderer.blockTexture);
//        GL11.glTranslatef((float) x, (float) y, (float) z);
//
//        Direction dir = screen.getBlockState().get(BlockScreen.FACING);
////        boolean screenData = (meta >= 8);
//
//        switch (dir)
//        {
//        case DOWN:
//            GL11.glRotatef(180, 1, 0, 0);
//            GL11.glTranslatef(0, -1.0F, -1.0F);
//            break;
//        case UP:
//            break;
//        case NORTH:
//            GL11.glTranslatef(0.0F, 0.0F, -0.87F);
//            GL11.glRotatef(90, 1.0F, 0, 0);
//            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
//            break;
//        case SOUTH:
//            GL11.glTranslatef(0.0F, 0.0F, 0.87F);
//            GL11.glRotatef(90, -1.0F, 0, 0);
//            GL11.glTranslatef(1.0F, -1.0F, 1.0F);
//            GL11.glRotatef(180, 0, -1.0F, 0);
//            break;
//        case WEST:
//            GL11.glTranslatef(-0.87F, 0.0F, 0.0F);
//            GL11.glRotatef(90, 0, 0, -1.0F);
//            GL11.glTranslatef(-1.0F, 0.0F, 1.0F);
//            GL11.glRotatef(90, 0, 1.0F, 0);
//            break;
//        case EAST:
//            GL11.glTranslatef(0.87F, 0.0F, 0.0F);
//            GL11.glRotatef(90, 0, 0, 1.0F);
//            GL11.glTranslatef(1.0F, -1.0F, 0.0F);
//            GL11.glRotatef(90, 0, -1.0F, 0);
//            break;
//        default:
//            break;
//        }
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        GL11.glTranslatef(-screen.screenOffsetx, this.yPlane, -screen.screenOffsetz);
//        GL11.glRotatef(90, 1F, 0F, 0F);
//        boolean cornerblock = false;
//        if (screen.connectionsLeft == 0 || screen.connectionsRight == 0)
//        {
//            cornerblock = (screen.connectionsUp == 0 || screen.connectionsDown == 0);
//        }
//        int totalLR = screen.connectionsLeft + screen.connectionsRight;
//        int totalUD = screen.connectionsUp + screen.connectionsDown;
//        if (totalLR > 1 && totalUD > 1 && !cornerblock)
//        {
//            //centre block
//            if (screen.connectionsLeft == screen.connectionsRight - (totalLR | 1))
//            {
//                if (screen.connectionsUp == screen.connectionsDown - (totalUD | 1))
//                {
//                    cornerblock = true;
//                }
//            }
//        }
//        GL11.glRotatef(180, 0, 1, 0);
//        GL11.glTranslatef(-screen.screen.getScaleX(), 0.0F, 0.0F);
//        screen.screen.drawScreen(screen.imageType, partialTicks + screen.getWorld().getDayTime(), cornerblock);
//
//        GL11.glPopMatrix();
//    }
//}
