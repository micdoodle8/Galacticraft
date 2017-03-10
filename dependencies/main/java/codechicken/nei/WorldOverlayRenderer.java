package codechicken.nei;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.render.state.GlStateManagerHelper;
import codechicken.lib.vec.Cuboid6;
import codechicken.nei.KeyManager.IKeyStateTracker;
import codechicken.nei.config.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import static org.lwjgl.opengl.GL11.*;

public class WorldOverlayRenderer implements IKeyStateTracker {
    public static int mobOverlay = 0;
    public static int chunkOverlay = 0;

    public static void reset() {
        mobOverlay = 0;
        chunkOverlay = 0;
    }

    @Override
    public void tickKeyStates() {
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }

        if (KeyBindings.get("nei.options.keys.world.moboverlay").isPressed()) {
            mobOverlay = (mobOverlay + 1) % 2;
        }
        if (KeyBindings.get("nei.options.keys.world.chunkoverlay").isPressed()) {
            chunkOverlay = (chunkOverlay + 1) % 3;
        }
    }

    public static void render(float frame) {
        GlStateManager.pushMatrix();
        GlStateManagerHelper.pushState();
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        RenderUtils.translateToWorldCoords(entity, frame);

        renderChunkBounds(entity);
        renderMobSpawnOverlay(entity);
        GlStateManagerHelper.popState();
        GlStateManager.popMatrix();
    }

    //TODO Improve the performance of this.
    private static void renderMobSpawnOverlay(Entity entity) {
        if (mobOverlay == 0) {
            return;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        glLineWidth(1.5F);
        glBegin(GL_LINES);

        GlStateManager.color(1, 0, 0);

        World world = entity.worldObj;
        int x1 = (int) entity.posX;
        int z1 = (int) entity.posZ;
        int y1 = (int) MathHelper.clip(entity.posY, 16, world.getHeight() - 16);

        for (int x = x1 - 16; x <= x1 + 16; x++) {
            for (int z = z1 - 16; z <= z1 + 16; z++) {
                BlockPos pos = new BlockPos(x, y1, z);
                Chunk chunk = world.getChunkFromBlockCoords(pos);
                Biome biome = world.getBiome(pos);
                if (biome.getSpawnableList(EnumCreatureType.MONSTER).isEmpty() || biome.getSpawningChance() <= 0) {
                    continue;
                }

                for (int y = y1 - 16; y < y1 + 16; y++) {
                    int spawnMode = getSpawnMode(chunk, x, y, z);
                    if (spawnMode == 0) {
                        continue;
                    }

                    if (spawnMode == 1) {
                        GlStateManager.color(1, 1, 0);
                    } else {
                        GlStateManager.color(1, 0, 0);
                    }

                    glVertex3d(x, y + 0.004, z);
                    glVertex3d(x + 1, y + 0.004, z + 1);
                    glVertex3d(x + 1, y + 0.004, z);
                    glVertex3d(x, y + 0.004, z + 1);
                }
            }
        }

        glEnd();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    //private static Entity dummyEntity = new EntityPig(null);
    private static Cuboid6 c = new Cuboid6();

    private static int getSpawnMode(Chunk chunk, int x, int y, int z) {
        World world = chunk.getWorld();
        BlockPos pos = new BlockPos(x, y, z);
        if (!WorldEntitySpawner.canCreatureTypeSpawnAtLocation(SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8) {
            return 0;
        }

        c.set(x + 0.2, y + 0.01, z + 0.2, x + 0.8, y + 1.8, z + 0.8);
        AxisAlignedBB aabb = c.aabb();
        if (!world.checkNoEntityCollision(aabb) || !world.getEntitiesWithinAABBExcludingEntity(null, aabb).isEmpty() || world.containsAnyLiquid(aabb)) {
            return 0;
        }

        if (chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8) {
            return 1;
        }
        return 2;
    }

    private static void renderChunkBounds(Entity entity) {
        if (chunkOverlay == 0) {
            return;
        }

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
        glLineWidth(1.5F);
        glBegin(GL_LINES);

        for (int cx = -4; cx <= 4; cx++) {
            for (int cz = -4; cz <= 4; cz++) {
                double x1 = (entity.chunkCoordX + cx) << 4;
                double z1 = (entity.chunkCoordZ + cz) << 4;
                double x2 = x1 + 16;
                double z2 = z1 + 16;

                double dy = 128;
                double y1 = Math.floor(entity.posY - dy / 2);
                double y2 = y1 + dy;
                if (y1 < 0) {
                    y1 = 0;
                    y2 = dy;
                }

                if (y1 > entity.worldObj.getHeight()) {
                    y2 = entity.worldObj.getHeight();
                    y1 = y2 - dy;
                }

                double dist = Math.pow(1.5, -(cx * cx + cz * cz));

                GlStateManager.color(0.9F, 0, 0, (float) dist);
                if (cx >= 0 && cz >= 0) {
                    glVertex3d(x2, y1, z2);
                    glVertex3d(x2, y2, z2);
                }
                if (cx >= 0 && cz <= 0) {
                    glVertex3d(x2, y1, z1);
                    glVertex3d(x2, y2, z1);
                }
                if (cx <= 0 && cz >= 0) {
                    glVertex3d(x1, y1, z2);
                    glVertex3d(x1, y2, z2);
                }
                if (cx <= 0 && cz <= 0) {
                    glVertex3d(x1, y1, z1);
                    glVertex3d(x1, y2, z1);
                }

                if (chunkOverlay == 2 && cx == 0 && cz == 0) {
                    dy = 32;
                    y1 = Math.floor(entity.posY - dy / 2);
                    y2 = y1 + dy;
                    if (y1 < 0) {
                        y1 = 0;
                        y2 = dy;
                    }

                    if (y1 > entity.worldObj.getHeight()) {
                        y2 = entity.worldObj.getHeight();
                        y1 = y2 - dy;
                    }

                    GlStateManager.color(0, 0.9F, 0, 0.4F);
                    for (double y = (int) y1; y <= y2; y++) {
                        glVertex3d(x2, y, z1);
                        glVertex3d(x2, y, z2);
                        glVertex3d(x1, y, z1);
                        glVertex3d(x1, y, z2);
                        glVertex3d(x1, y, z2);
                        glVertex3d(x2, y, z2);
                        glVertex3d(x1, y, z1);
                        glVertex3d(x2, y, z1);
                    }
                    for (double h = 1; h <= 15; h++) {
                        glVertex3d(x1 + h, y1, z1);
                        glVertex3d(x1 + h, y2, z1);
                        glVertex3d(x1 + h, y1, z2);
                        glVertex3d(x1 + h, y2, z2);
                        glVertex3d(x1, y1, z1 + h);
                        glVertex3d(x1, y2, z1 + h);
                        glVertex3d(x2, y1, z1 + h);
                        glVertex3d(x2, y2, z1 + h);
                    }
                }
            }
        }

        glEnd();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    public static void load() {
        KeyManager.trackers.add(new WorldOverlayRenderer());
    }
}
