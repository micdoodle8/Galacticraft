package codechicken.lib.render.particle;

import codechicken.lib.render.DigIconParticle;
import codechicken.lib.texture.IWorldBlockTextureProvider;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Created by covers1624 on 21/11/2016.
 * TODO, we need to support landing particles and Maybe some ASM to add this to all blocks that CCL supports.
 */
public class CustomParticleHandler {

    public static void addHitEffects(IBlockState state, World world, RayTraceResult trace, ParticleManager particleManager, IWorldBlockTextureProvider provider) {
        TextureAtlasSprite sprite = provider.getTexture(trace.sideHit, state, BlockRenderLayer.SOLID, world, trace.getBlockPos());
        Cuboid6 cuboid = new Cuboid6(state.getBoundingBox(world, trace.getBlockPos())).add(trace.getBlockPos());
        addBlockHitEffects(world, cuboid, trace.sideHit, sprite, particleManager);
    }

    public static void addDestroyEffects(World world, BlockPos pos, ParticleManager particleManager, IWorldBlockTextureProvider provider) {
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[6];
        IBlockState state = world.getBlockState(pos);
        for (EnumFacing face : EnumFacing.VALUES) {
            sprites[face.ordinal()] = provider.getTexture(face, state, BlockRenderLayer.SOLID, world, pos);
        }
        Cuboid6 cuboid = new Cuboid6(state.getBoundingBox(world, pos)).add(pos);
        addBlockDestroyEffects(world, cuboid, sprites, particleManager);
    }

    public static void addBlockHitEffects(World world, Cuboid6 bounds, EnumFacing side, TextureAtlasSprite icon, ParticleManager particleManager) {
        float border = 0.1F;
        Vector3 diff = bounds.max.copy().subtract(bounds.min).add(-2 * border);
        diff.x *= world.rand.nextDouble();
        diff.y *= world.rand.nextDouble();
        diff.z *= world.rand.nextDouble();
        Vector3 pos = diff.add(bounds.min).add(border);

        if (side == EnumFacing.DOWN) {
            diff.y = bounds.min.y - border;
        }
        if (side == EnumFacing.UP) {
            diff.y = bounds.max.y + border;
        }
        if (side == EnumFacing.NORTH) {
            diff.z = bounds.min.z - border;
        }
        if (side == EnumFacing.SOUTH) {
            diff.z = bounds.max.z + border;
        }
        if (side == EnumFacing.WEST) {
            diff.x = bounds.min.x - border;
        }
        if (side == EnumFacing.EAST) {
            diff.x = bounds.max.x + border;
        }

        particleManager.addEffect(new DigIconParticle(world, pos.x, pos.y, pos.z, 0, 0, 0, icon).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
    }

    public static void addBlockDestroyEffects(World world, Cuboid6 bounds, TextureAtlasSprite[] icons, ParticleManager particleManager) {
        Vector3 diff = bounds.max.copy().subtract(bounds.min);
        Vector3 center = bounds.min.copy().add(bounds.max).multiply(0.5);
        Vector3 density = diff.copy().multiply(4).celi();

        for (int i = 0; i < density.x; ++i) {
            for (int j = 0; j < density.y; ++j) {
                for (int k = 0; k < density.z; ++k) {
                    double x = bounds.min.x + (i + 0.5) * diff.x / density.x;
                    double y = bounds.min.y + (j + 0.5) * diff.y / density.y;
                    double z = bounds.min.z + (k + 0.5) * diff.z / density.z;
                    particleManager.addEffect(new DigIconParticle(world, x, y, z, x - center.x, y - center.y, z - center.z, icons[world.rand.nextInt(icons.length)]));
                }
            }
        }
    }

}
