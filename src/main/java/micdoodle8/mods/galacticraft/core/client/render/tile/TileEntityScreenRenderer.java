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
//        RenderSystem.pushMatrix();
//        // Texture file
//        this.textureManager.bindTexture(TileEntityScreenRenderer.blockTexture);
//        RenderSystem.translatef((float) x, (float) y, (float) z);
//
//        Direction dir = screen.getBlockState().get(BlockScreen.FACING);
////        boolean screenData = (meta >= 8);
//
//        switch (dir)
//        {
//        case DOWN:
//            RenderSystem.rotatef(180, 1, 0, 0);
//            RenderSystem.translatef(0, -1.0F, -1.0F);
//            break;
//        case UP:
//            break;
//        case NORTH:
//            RenderSystem.translatef(0.0F, 0.0F, -0.87F);
//            RenderSystem.rotatef(90, 1.0F, 0, 0);
//            RenderSystem.translatef(0.0F, 0.0F, -1.0F);
//            break;
//        case SOUTH:
//            RenderSystem.translatef(0.0F, 0.0F, 0.87F);
//            RenderSystem.rotatef(90, -1.0F, 0, 0);
//            RenderSystem.translatef(1.0F, -1.0F, 1.0F);
//            RenderSystem.rotatef(180, 0, -1.0F, 0);
//            break;
//        case WEST:
//            RenderSystem.translatef(-0.87F, 0.0F, 0.0F);
//            RenderSystem.rotatef(90, 0, 0, -1.0F);
//            RenderSystem.translatef(-1.0F, 0.0F, 1.0F);
//            RenderSystem.rotatef(90, 0, 1.0F, 0);
//            break;
//        case EAST:
//            RenderSystem.translatef(0.87F, 0.0F, 0.0F);
//            RenderSystem.rotatef(90, 0, 0, 1.0F);
//            RenderSystem.translatef(1.0F, -1.0F, 0.0F);
//            RenderSystem.rotatef(90, 0, -1.0F, 0);
//            break;
//        default:
//            break;
//        }
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        RenderSystem.translatef(-screen.screenOffsetx, this.yPlane, -screen.screenOffsetz);
//        RenderSystem.rotatef(90, 1F, 0F, 0F);
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
//        RenderSystem.rotatef(180, 0, 1, 0);
//        RenderSystem.translatef(-screen.screen.getScaleX(), 0.0F, 0.0F);
//        screen.screen.drawScreen(screen.imageType, partialTicks + screen.getWorld().getDayTime(), cornerblock);
//
//        RenderSystem.popMatrix();
//    }
//}
