package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.LightType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TileEntityMinerBaseRenderer extends TileEntityRenderer<TileEntityMinerBase>
{
    private IBakedModel minerBaseModelBaked;
    private List<BlockVec3> offsets = Lists.newArrayList();

    public TileEntityMinerBaseRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
        offsets.add(new BlockVec3(0, 0, 0));
        offsets.add(new BlockVec3(1, 0, 0));
        offsets.add(new BlockVec3(0, 0, 1));
        offsets.add(new BlockVec3(1, 0, 1));
        offsets.add(new BlockVec3(0, 1, 0));
        offsets.add(new BlockVec3(1, 1, 0));
        offsets.add(new BlockVec3(0, 1, 1));
        offsets.add(new BlockVec3(1, 1, 1));
    }

    private void updateModels()
    {
        minerBaseModelBaked = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/minerbase0.obj"), ImmutableList.of("dock"));
    }

    private int getMinerBaseLight(ILightReader lightReaderIn, BlockPos pos)
    {
        int totalSky = 0;
        int totalBlock = 0;

        for (BlockVec3 offset : offsets)
        {
            BlockPos offsetPos = pos.add(offset.x, offset.y, offset.z);
            totalSky += lightReaderIn.getLightFor(LightType.SKY, offsetPos);
            int block = lightReaderIn.getLightFor(LightType.BLOCK, offsetPos);
            BlockState state = lightReaderIn.getBlockState(offsetPos);
            int k = state.getLightValue(lightReaderIn, offsetPos);
            if (block < k)
            {
                block = k;
            }
            totalBlock += block;
        }

        return (totalSky / offsets.size()) << 20 | (totalBlock / offsets.size()) << 4;
    }

    @Override
    public void render(TileEntityMinerBase tile, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if (!tile.isMaster)
        {
            return;
        }

        int light = getMinerBaseLight(tile.getWorld(), tile.getPos());

//        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        matStack.push();

//        GL11.glTranslatef((float) x + 1F, (float) y + 1F, (float) z + 1F);
//        GL11.glScalef(0.05F, 0.05F, 0.05F);
        matStack.translate(1.0F, 1.0F, 1.0F);
        matStack.scale(0.05F, 0.05F, 0.05F);

        switch (tile.facing)
        {
        case SOUTH:
//            GL11.glRotatef(180F, 0, 1F, 0);
            matStack.rotate(new Quaternion(0.0F, 180.0F, 0.0F, true));
            break;
        case WEST:
//            GL11.glRotatef(90F, 0, 1F, 0);
            matStack.rotate(new Quaternion(0.0F, 90.0F, 0.0F, true));
            break;
        case NORTH:
            break;
        case EAST:
//            GL11.glRotatef(270F, 0, 1F, 0);
            matStack.rotate(new Quaternion(0.0F, 270.0F, 0.0F, true));
            break;
        }

        ClientUtil.drawBakedModel(minerBaseModelBaked, bufferIn, matStack, light);

        matStack.pop();
    }
}
