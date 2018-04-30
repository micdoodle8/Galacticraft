package micdoodle8.mods.galacticraft.planets.deepspace.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.deepspace.blocks.BlockDeepStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.util.EnumMap;
import java.util.List;

public class ModelDeepStructural implements IBakedModel
{
    private final EnumMap<EnumFacing, List<BakedQuad>[]> faceQuads;
    private static final float x[] = { 0, 0, 1, 1 };
    private static final float z[] = { 0, 1, 1, 0 };
    private final boolean inv; 

    public ModelDeepStructural (boolean invert)
    {
        this.inv = invert;
        this.faceQuads = Maps.newEnumMap(EnumFacing.class);
        this.setupAllQuads(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "blocks/grey"));
    }

    private void setupAllQuads(ResourceLocation location)
    {
        TextureAtlasSprite topSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        for(EnumFacing side : EnumFacing.VALUES)
        {
            faceQuads.put(side, this.setupQuadsAllY(side, DefaultVertexFormats.ITEM, topSprite));
        }
    }

    private List<BakedQuad>[] setupQuadsAllY(EnumFacing side, VertexFormat format, TextureAtlasSprite topSprite)
    {
        List<BakedQuad>[] result = new List[16]; 
        for (int y = 0; y < 16; y++)
        {
            result[y] = this.setupQuads(side, format, topSprite, y);
        }
        return result;
    }

    private List<BakedQuad> setupQuads(EnumFacing side, VertexFormat format, TextureAtlasSprite topSprite, int y)
    {
        ImmutableList.Builder<BakedQuad> listBuilder = ImmutableList.builder();
        UnpackedBakedQuad.Builder builder;
        builder = new UnpackedBakedQuad.Builder(format);
        builder.setQuadOrientation(side);
        builder.setTexture(topSprite);

        // angle = 45/8 degrees ie. 5.6125 degrees and 1z for 8y
        float za = (y % 8) / 8F;
        float zb = za + 0.125F;
        float zc = y < 8 ? 0F : 1F - zb;
        float zd = y < 8 ? 0F : 1F - za;
        if (this.inv)
        {
            float zza = za;
            za = zb;
            zb = zza;
            zza = zc;
            zc = zd;
            zd = zza;
        }
        float xx, yy, zz;
        for (int i = 0; i < 4; i++)
        {
            switch(side)
            {
            case UP:
                xx = x[i];
                yy = 1.0F;
                zz = z[i] * zb + zc;
                break;
            case DOWN:
                xx = x[3 - i];
                yy = 0.0F;
                zz = z[3 - i] * za + zd;
                break;
            case SOUTH:
                xx = x[3 - i];
                yy = z[3 - i];
                zz = y < 8 ? ((i == 0 || i == 3) ? za : zb) : 1F;
                break;
            case NORTH:
                xx = x[i];
                yy = z[i];
                zz = (i == 0 || i == 3) ? zd : zc;
                break;
            case EAST:
                xx = 1.0F;
                yy = z[i];
                if (y < 8)
                    zz = x[i] * ((i == 0 || i == 3) ? za : zb);
                else
                    zz = 1F - x[3 - i] * ((i == 0 || i == 3) ? za : zb);
                break;
            case WEST:
            default:
                xx = 0.0F;
                yy = z[3 - i];
                if (y < 8)
                    zz = x[3 - i] * ((i == 0 || i == 3) ? za : zb);
                else
                    zz = 1F - x[i] * ((i == 0 || i == 3) ? za : zb);
                break;
            }
            putVertex(format, builder, side, xx, yy, zz,
                    topSprite.getInterpolatedU(x[i] * 16F),
                    topSprite.getInterpolatedV(z[i] * 16F));
        }
        listBuilder.add(builder.build());
        return listBuilder.build();
    }

    private void putVertex(VertexFormat format, UnpackedBakedQuad.Builder builder, EnumFacing side, float x, float y, float z, float u, float v)
    {
        for(int e = 0; e < format.getElementCount(); e++)
        {
            switch(format.getElement(e).getUsage())
            {
            case POSITION:
                float[] data = new float[]{ x, y, z, 1 };
                builder.put(e, data);
                break;
            case COLOR:
                builder.put(e, 1f, 1f, 1f, 1f);
                break;
            case UV:
                if (format.getElement(e).getIndex() == 0)
                {
                    builder.put(e, u, v, 0f, 1f);
                }
                else if (format.getElement(e).getIndex() == 1)
                {    
                    builder.put(e, 0.00732433F, 0.00732433F, 0, 1);
                }
                else builder.put(e);
                break;
            case NORMAL:
                builder.put(e, (float)side.getFrontOffsetX(), (float)side.getFrontOffsetY(), (float)side.getFrontOffsetZ(), 0f);
                break;
            default:
                builder.put(e);
                break;
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        if (MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT && side != null)
        {
            int y = state.getValue(BlockDeepStructure.H);
            return faceQuads.get(side)[y];
        }
        
        return ImmutableList.<BakedQuad>of();
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(GalacticraftPlanets.ASSET_PREFIX + ":blocks/grey");
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }
}
