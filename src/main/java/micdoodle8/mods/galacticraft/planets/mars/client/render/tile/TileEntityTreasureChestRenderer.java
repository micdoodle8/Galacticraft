package micdoodle8.mods.galacticraft.planets.mars.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer<T extends TileEntityTreasureChest & IChestLid> extends ChestTileEntityRenderer<T>
{
//    private static final ResourceLocation treasureChestTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/treasure.png");

//    private final ModelTreasureChest chestModel = new ModelTreasureChest();
    public ModelRenderer[] keyParts = new ModelRenderer[6];

    public TileEntityTreasureChestRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        this.keyParts[4] = new ModelRenderer(64, 64, 50, 43);
        this.keyParts[4].addBox(7F, 2F, -0.5F, 3, 1, 1);
        this.keyParts[4].setRotationPoint(0F, 0F, 0F);
        this.keyParts[4].setTextureSize(64, 64);
        this.keyParts[4].mirror = true;
        this.keyParts[3] = new ModelRenderer(64, 64, 39, 43);
        this.keyParts[3].addBox(6F, 1F, -0.5F, 4, 1, 1);
        this.keyParts[3].setRotationPoint(0F, 0F, 0F);
        this.keyParts[3].setTextureSize(64, 64);
        this.keyParts[3].mirror = true;
        this.keyParts[2] = new ModelRenderer(64, 64, 14, 43);
        this.keyParts[2].addBox(-0.5F, 0F, -0.5F, 11, 1, 1);
        this.keyParts[2].setRotationPoint(0F, 0F, 0F);
        this.keyParts[2].setTextureSize(64, 64);
        this.keyParts[2].mirror = true;
        this.keyParts[1] = new ModelRenderer(64, 64, 9, 43);
        this.keyParts[1].addBox(-1.5F, -0.5F, -0.5F, 1, 2, 1);
        this.keyParts[1].setRotationPoint(0F, 0F, 0F);
        this.keyParts[1].setTextureSize(64, 64);
        this.keyParts[1].mirror = true;
        this.keyParts[0] = new ModelRenderer(64, 64, 0, 43);
        this.keyParts[0].addBox(-4.5F, -1F, -0.5F, 3, 3, 1);
        this.keyParts[0].setRotationPoint(0F, 0F, 0F);
        this.keyParts[0].setTextureSize(64, 64);
        this.keyParts[0].mirror = true;
        this.keyParts[5] = new ModelRenderer(64, 64, 0, 0).setTextureSize(64, 64);
        this.keyParts[5].addBox(-2.0F, -2.05F, -15.1F, 4, 4, 1, 0.0F);
        this.keyParts[5].rotationPointX = 8.0F;
        this.keyParts[5].rotationPointY = 7.0F;
        this.keyParts[5].rotationPointZ = 15.0F;
    }

    @Override
    public void render(T chest, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        World world = chest.getWorld();
        boolean flag = world != null;
        BlockState blockstate = flag ? chest.getBlockState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.has(ChestBlock.TYPE) ? blockstate.get(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockstate.getBlock();

        matrixStackIn.push();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.scale(1.0F, -1.0F, -1.0F);
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        float f = chest.getBlockState().get(ChestBlock.FACING).getHorizontalAngle();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
        matrixStackIn.translate(-0.5F, -0.5F, -0.5F);
        Material material = this.getMaterial(chest, chesttype);
        IVertexBuilder ivertexbuilder = material.getBuffer(bufferIn, RenderType::getEntityCutout);
        for (ModelRenderer renderer : keyParts)
        {
            renderer.render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        matrixStackIn.pop();
        super.render(chest, partialTicks, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}