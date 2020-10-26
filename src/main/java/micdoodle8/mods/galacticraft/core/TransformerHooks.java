package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.item.IArmorGravity;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.IWeatherProvider;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.dimension.DimensionMoon;
import micdoodle8.mods.galacticraft.core.entities.player.EnumGravity;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.PLAYER_Y_OFFSET;
import static micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.submergedTextures;

/**
 * These methods are called from vanilla minecraft through bytecode injection done in MicdoodleCore
 * <p>
 * See https://github.com/micdoodle8/MicdoodleCore
 */
public class TransformerHooks
{
//    private static final List<IWorldGenerator> otherModGeneratorsWhitelist = new LinkedList<>(); TODO Overworld gen
//    private static final IWorldGenerator generatorTCAuraNodes = null;
    private static final Method generateTCAuraNodes = null;
    private static final boolean generatorsInitialised = false;
    public static List<Block> spawnListAE2_GC = new LinkedList<>();
    public static ThreadLocal<BufferBuilder> renderBuilder = new ThreadLocal<>();
    private static int rainSoundCounter = 0;
    private static final Random random = new Random();

    public static double getGravityForEntity(Entity entity)
    {
        if (entity.world.getDimension() instanceof IGalacticraftDimension)
        {
            if (entity instanceof ChickenEntity && !OxygenUtil.isAABBInBreathableAirBlock(entity.world, entity.getBoundingBox()))
            {
                return 0.08D;
            }

            final IGalacticraftDimension customProvider = (IGalacticraftDimension) entity.world.getDimension();
            if (entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;
                if (player.inventory != null)
                {
                    int armorModLowGrav = 100;
                    int armorModHighGrav = 100;
                    for (ItemStack armorPiece : player.getArmorInventoryList())
                    {
                        if (armorPiece != null && armorPiece.getItem() instanceof IArmorGravity)
                        {
                            armorModLowGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfLow(player);
                            armorModHighGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfHigh(player);
                        }
                    }
                    if (armorModLowGrav > 100)
                    {
                        armorModLowGrav = 100;
                    }
                    if (armorModHighGrav > 100)
                    {
                        armorModHighGrav = 100;
                    }
                    if (armorModLowGrav < 0)
                    {
                        armorModLowGrav = 0;
                    }
                    if (armorModHighGrav < 0)
                    {
                        armorModHighGrav = 0;
                    }
                    if (customProvider.getGravity() > 0)
                    {
                        return 0.08D - (customProvider.getGravity() * armorModLowGrav) / 100;
                    }
                    return 0.08D - (customProvider.getGravity() * armorModHighGrav) / 100;
                }
            }
            return 0.08D - customProvider.getGravity();
        }
        else if (entity instanceof IAntiGrav)
        {
            return 0;
        }
        else
        {
            return 0.08D;
        }
    }

    public static double getItemGravity(ItemEntity e)
    {
        if (e.world.getDimension() instanceof IGalacticraftDimension)
        {
            final IGalacticraftDimension customProvider = (IGalacticraftDimension) e.world.getDimension();
            return Math.max(0.002D, 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D);
        }
        else
        {
            return 0.03999999910593033D;
        }
    }

    public static float getArrowGravity(AbstractArrowEntity e)
    {
        if (e.world.getDimension() instanceof IGalacticraftDimension)
        {
            return ((IGalacticraftDimension) e.world.getDimension()).getArrowGravity();
        }
        else
        {
            return 0.05F;
        }
    }

    public static float getRainStrength(World world, float partialTicks)
    {
        if (world.isRemote)
        {
//            if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld)
//            {
//                return 0.0F;
//            }  TODO Sky rendering
        }

        return world.prevRainingStrength + (world.rainingStrength - world.prevRainingStrength) * partialTicks;
    }

    public static void otherModGenerate(int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider)
    {
//        if (world.getDimension() instanceof DimensionSpaceStation) TODO Other mod gen in space dimensions
//        {
//            return;
//        }
//
//        if (!(world.getDimension() instanceof IGalacticraftWorldProvider) || ConfigManagerCore.enableOtherModsFeatures.get())
//        {
//            try {
//                net.minecraftforge.fml.common.registry.GameRegistry.generateWorld(chunkX, chunkZ, world, chunkGenerator, chunkProvider);
//            } catch (Exception e)
//            {
//                GCLog.severe("Error in another mod's worldgen.  This is *NOT* a Galacticraft bug, report it to the other mod please.");
//                GCLog.severe("Details:- Dimension:" + GCCoreUtil.getDimensionID(world) + "  Chunk cx,cz:" + chunkX + "," + chunkZ + "  Seed:" + world.getSeed());
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        if (!generatorsInitialised)
//        {
//            generatorsInitialised = true;
//
//            if (ConfigManagerCore.whitelistCoFHCoreGen.get())
//            {
//                addWorldGenForName("CoFHCore custom oregen", "cofh.cofhworld.init.WorldHandler");
//            }
//            addWorldGenForName("GalacticGreg oregen", "bloodasp.galacticgreg.GT_Worldgenerator_Space");
//            addWorldGenForName("Dense Ores oregen", "com.rwtema.denseores.WorldGenOres");
//            addWorldGenForName("AE2 meteorites worldgen", "appeng.worldgen.MeteoriteWorldGen");
//
//            try
//            {
//                Class genThaumCraft = Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator");
//                if (genThaumCraft != null && ConfigManagerCore.enableThaumCraftNodes.get())
//                {
//                    final Field regField = GameRegistry.class.getDeclaredField("worldGenerators");
//                    regField.setAccessible(true);
//                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
//                    for (IWorldGenerator gen : registeredGenerators)
//                    {
//                        if (genThaumCraft.isInstance(gen))
//                        {
//                            generatorTCAuraNodes = gen;
//                            break;
//                        }
//                    }
//                    if (generatorTCAuraNodes != null)
//                    {
//                        generateTCAuraNodes = genThaumCraft.getDeclaredMethod("generateWildNodes", World.class, Random.class, int.class, int.class, boolean.class, boolean.class);
//                        generateTCAuraNodes.setAccessible(true);
//                        GCLog.info("Whitelisting ThaumCraft aura node generation on planets.");
//                    }
//                }
//            }
//            catch (Exception e)
//            {
//            }
//        }
//
//        if (otherModGeneratorsWhitelist.size() > 0 || generateTCAuraNodes != null)
//        {
//            try
//            {
//                long worldSeed = world.getSeed();
//                Random fmlRandom = new Random(worldSeed);
//                long xSeed = fmlRandom.nextLong() >> 2 + 1L;
//                long zSeed = fmlRandom.nextLong() >> 2 + 1L;
//                long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
//                fmlRandom.setSeed(chunkSeed);
//
//                for (IWorldGenerator gen : otherModGeneratorsWhitelist)
//                {
//                    gen.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
//                }
//                if (generateTCAuraNodes != null)
//                {
//                    generateTCAuraNodes.invoke(generatorTCAuraNodes, world, fmlRandom, chunkX, chunkZ, false, true);
//                }
//            }
//            catch (Exception e)
//            {
//                GCLog.severe("Error in another mod's worldgen.  This is *NOT* a Galacticraft bug, report it to the other mod please.");
//                e.printStackTrace();
//            }
//        }
    }

    private static void addWorldGenForName(String logString, String name)
    {
        try
        {
            Class target = Class.forName(name);
            if (target != null)
            {
                final Field regField = GameRegistry.class.getDeclaredField("worldGenerators");
                regField.setAccessible(true);
//                Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
//                for (IWorldGenerator gen : registeredGenerators)
//                {
//                    if (target.isInstance(gen))
//                    {
//                        otherModGeneratorsWhitelist.add(gen);
//                        GCLog.info("Whitelisting " + logString + " on planets.");
//                        return;
//                    }
//                } TODO Overworld gen
            }
        }
        catch (Exception e)
        {
        }
    }

    /*
     * Used to supplement the hard-coded blocklist in AE2's MeteoritePlacer class
     */
    public static boolean addAE2MeteorSpawn(Object o, Block b)
    {
        if (o instanceof Collection<?>)
        {
            ((Collection<Block>) o).add(b);
            ((Collection<Block>) o).addAll(spawnListAE2_GC);
            return true;
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getWorldBrightness(ClientWorld world)
    {
        if (world.getDimension() instanceof DimensionMoon)
        {
            float f1 = world.getCelestialAngle(1.0F);
            float f2 = 1.0F - (MathHelper.cos(f1 * Constants.twoPI) * 2.0F + 0.2F);

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            f2 = 1.0F - f2;
            return f2 * 0.8F;
        }

        return world.getSunBrightness(1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static float getColorRed(World world)
    {
        return WorldUtil.getWorldColor(world).x;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getColorGreen(World world)
    {
        return WorldUtil.getWorldColor(world).y;
    }

    @OnlyIn(Dist.CLIENT)
    public static float getColorBlue(World world)
    {
        return WorldUtil.getWorldColor(world).z;
    }

    @OnlyIn(Dist.CLIENT)
    public static Vec3d getFogColorHook(ClientWorld world)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
//        if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld)
//        {
//            float var20 = ((float) (player.getPosY()) - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F;
//            var20 = MathHelper.sqrt(var20);
//            final float var21 = Math.max(1.0F - var20 * 40.0F, 0.0F);
//
//            Vec3d vec = world.getFogColor(1.0F);
//
//            return new Vec3d(vec.x * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.y * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.z * Math.max(1.0F - var20 * 1.29F, 0.0F));
//        } TODO Sky rendering

        return world.getFogColor(1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static Vec3d getSkyColorHook(ClientWorld world)
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
//        if (world.getDimension().getSkyRenderer() instanceof SkyProviderOverworld || (player != null && player.getPosY() > Constants.OVERWORLD_CLOUD_HEIGHT && player.getRidingEntity() instanceof EntitySpaceshipBase))
//        {
//            float f1 = world.getCelestialAngle(1.0F);
//            float f2 = MathHelper.cos(f1 * Constants.twoPI) * 2.0F + 0.5F;
//
//            if (f2 < 0.0F)
//            {
//                f2 = 0.0F;
//            }
//
//            if (f2 > 1.0F)
//            {
//                f2 = 1.0F;
//            }
//
//            int i = MathHelper.floor(player.getPosX());
//            int j = MathHelper.floor(player.getPosY());
//            int k = MathHelper.floor(player.getPosZ());
//            BlockPos pos = new BlockPos(i, j, k);
//            int l = ForgeHooksClient.getSkyBlendColour(world, pos);
//            float f4 = (float) (l >> 16 & 255) / 255.0F;
//            float f5 = (float) (l >> 8 & 255) / 255.0F;
//            float f6 = (float) (l & 255) / 255.0F;
//            f4 *= f2;
//            f5 *= f2;
//            f6 *= f2;
//
//            if (player.getPosY() <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//            {
//                Vec3d vec = world.getSkyColor(Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getBlockPos(), 1.0F);
//                double blend = (player.getPosY() - Constants.OVERWORLD_CLOUD_HEIGHT) / (Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT - Constants.OVERWORLD_CLOUD_HEIGHT);
//                double ablend = 1 - blend;
//                return new Vec3d(f4 * blend + vec.x * ablend, f5 * blend + vec.y * ablend, f6 * blend + vec.z * ablend);
//            }
//            else
//            {
//                double blend = Math.min(1.0D, (player.getPosY() - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 300.0D);
//                double ablend = 1.0D - blend;
//                blend /= 255.0D;
//                return new Vec3d(f4 * ablend + blend * 31.0D, f5 * ablend + blend * 8.0D, f6 * ablend + blend * 99.0D);
//            }
//        } TODO Sky rendering

        return world.getSkyColor(Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getBlockPos(), 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static double getRenderPosY(Entity viewEntity, double regular)
    {
        if (viewEntity.getPosY() >= 256)
        {
            return 255.0F;
        }
        else
        {
            return regular + viewEntity.getEyeHeight();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean shouldRenderFire(Entity entity)
    {
        if (entity.world == null || !(entity.world.getDimension() instanceof IGalacticraftDimension))
        {
            return entity.isBurning();
        }

        if (!(entity instanceof LivingEntity) && !(entity instanceof AbstractArrowEntity))
        {
            return entity.isBurning();
        }

        if (entity.isBurning())
        {
            if (OxygenUtil.noAtmosphericCombustion(entity.world.getDimension()))
            {
                return OxygenUtil.isAABBInBreathableAirBlock(entity.world, entity.getBoundingBox());
            }
            else
            {
                return true;
            }
            //Disable fire on Galacticraft worlds with no oxygen
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static void orientCamera(float partialTicks)
    {
        ClientPlayerEntity player = ClientProxyCore.mc.player;
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        Entity viewEntity = ClientProxyCore.mc.getRenderViewEntity();

        if (player.getRidingEntity() instanceof ICameraZoomEntity && ClientProxyCore.mc.gameSettings.thirdPersonView == 0)
        {
            Entity entity = player.getRidingEntity();
            float offset = ((ICameraZoomEntity) entity).getRotateOffset();
            if (offset > -10F)
            {
                offset += PLAYER_Y_OFFSET;
                GL11.glTranslatef(0, -offset, 0);
                float anglePitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float angleYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
                GL11.glRotatef(-anglePitch, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(angleYaw, 0.0F, 1.0F, 0.0F);

                GL11.glTranslatef(0, offset, 0);
            }
        }

        if (viewEntity instanceof LivingEntity && viewEntity.world.getDimension() instanceof IZeroGDimension && !((LivingEntity) viewEntity).isSleeping())
        {
            float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;
            float yaw = viewEntity.prevRotationYaw + (viewEntity.rotationYaw - viewEntity.prevRotationYaw) * partialTicks + 180.0F;
            float eyeHeightChange = viewEntity.getWidth() / 2.0F;

//            GL11.glTranslatef(0.0F, -f1, 0.0F);
            GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.1F);

            EnumGravity gDir = stats.getGdir();
            GL11.glRotatef(180.0F * gDir.getThetaX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F * gDir.getThetaZ(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityZ(), 0.0F, 0.0F, 1.0F);

//        	GL11.glTranslatef(sneakY * gDir.getSneakVecX(), sneakY * gDir.getSneakVecY(), sneakY * gDir.getSneakVecZ());

            GL11.glTranslatef(eyeHeightChange * gDir.getEyeVecX(), eyeHeightChange * gDir.getEyeVecY(), eyeHeightChange * gDir.getEyeVecZ());

            if (stats.getGravityTurnRate() < 1.0F)
            {
                GL11.glRotatef(90.0F * (stats.getGravityTurnRatePrev() + (stats.getGravityTurnRate() - stats.getGravityTurnRatePrev()) * partialTicks), stats.getGravityTurnVecX(), stats.getGravityTurnVecY(), stats.getGravityTurnVecZ());
            }
        }

        //omit this for interesting 3P views
//        GL11.glTranslatef(0.0F, 0.0F, -0.1F);
//        GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glTranslatef(0.0F, f1, 0.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderLiquidOverlays(float partialTicks)
    {
        boolean within = false;
        for (Map.Entry<Fluid, ResourceLocation> entry : submergedTextures.entrySet())
        {
            if (FluidUtil.isInsideOfFluid(ClientProxyCore.mc.player, entry.getKey()))
            {
                within = true;
                ClientProxyCore.mc.getTextureManager().bindTexture(entry.getValue());
                break;
            }
        }

        if (!within)
        {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        float f1 = ClientProxyCore.mc.player.getBrightness() / 3.0F;
        GL11.glColor4f(f1, f1, f1, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f2 = 4.0F;
        float f3 = -1.1F;
        float f4 = 1.1F;
        float f5 = -1.1F;
        float f6 = 1.1F;
        float f7 = -0.25F;
        float f8 = -ClientProxyCore.mc.player.rotationYaw / 64.0F;
        float f9 = ClientProxyCore.mc.player.rotationPitch / 64.0F;
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(f3, f5, f7).tex(f2 + f8, f2 + f9).endVertex();
        worldRenderer.pos(f4, f5, f7).tex(0.0F + f8, f2 + f9).endVertex();
        worldRenderer.pos(f4, f6, f7).tex(0.0F + f8, 0.0F + f9).endVertex();
        worldRenderer.pos(f3, f6, f7).tex(f2 + f8, 0.0F + f9).endVertex();
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderFootprints(float partialTicks)
    {
        FootprintRenderer.renderFootprints(ClientProxyCore.mc.player, partialTicks);
        MinecraftForge.EVENT_BUS.post(new ClientProxyCore.EventSpecialRender(partialTicks));
//        BubbleRenderer.renderBubbles(ClientProxyCore.mc.player, partialTicks); TODO Bubble rendering
    }

    @OnlyIn(Dist.CLIENT)
    public static double getCameraZoom(double previous)
    {
        if (ConfigManagerCore.disableVehicleCameraChanges.get())
        {
            return previous;
        }

        PlayerEntity player = Minecraft.getInstance().player;
        if (player.getRidingEntity() != null && player.getRidingEntity() instanceof ICameraZoomEntity)
        {
            return ((ICameraZoomEntity) player.getRidingEntity()).getCameraZoom();
        }

        return previous;
    }

    public static double armorDamageHook(LivingEntity entity)
    {
        if (entity instanceof PlayerEntity && GalacticraftCore.isPlanetsLoaded)
        {
            GCPlayerStats stats = GCPlayerStats.get(entity);
            if (stats != null)
            {
                ItemStack shield = stats.getShieldControllerInSlot();
//                if (shield != null && !shield.isEmpty() && shield.getItem() == VenusItems.basicItem && shield.getDamage() == 0)
//                {
//                    return 0D;
//                } TODO Planets
            }
        }
        return 1D;
    }

    public static void setCurrentBuffer(BufferBuilder buffer)
    {
        renderBuilder.set(buffer);
    }

    public static boolean isGrating(boolean orig, Block b)
    {
        return false; // TODO Grating
//        return orig || (b instanceof BlockGrating && b != GCBlocks.grating && MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT);
    }

    public static float armorDamageHookF(LivingEntity entity)
    {
        return (float) armorDamageHook(entity);
    }

    @OnlyIn(Dist.CLIENT)
    public static int addRainParticles(int result, int rendererUpdateCount)
    {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = mc.world;
        if (result == 0 || !(world.getDimension() instanceof IWeatherProvider))
        {
            // Either no rain or it's a vanilla dimension
            return result;
        }
        IWeatherProvider moddedProvider = ((IWeatherProvider) world.getDimension());
        float f = mc.world.getRainStrength(1.0F);
        if (!mc.gameSettings.fancyGraphics)
        {
            f /= 2.0F;
        }

        if (f != 0.0F)
        {
            random.setSeed((long) rendererUpdateCount * 312987231L);
            IWorldReader iworldreader = mc.world;
            BlockPos blockpos = new BlockPos(mc.gameRenderer.getActiveRenderInfo().getProjectedView());
            int i = 10;
            double xx = 0.0D;
            double yy = 0.0D;
            double zz = 0.0D;
            double x = 0.0D;
            double y = 0.0D;
            double z = 0.0D;
            int j = 0;
            int k = (int) (100.0F * f * f);
            if (mc.gameSettings.particles == ParticleStatus.DECREASED)
            {
                k >>= 1;
            }
            else if (mc.gameSettings.particles == ParticleStatus.MINIMAL)
            {
                k = 0;
            }

            for (int l = 0; l < k; ++l)
            {
                BlockPos blockpos1 = iworldreader.getHeight(Heightmap.Type.MOTION_BLOCKING, blockpos.add(random.nextInt(10) - random.nextInt(10), 0, random.nextInt(10) - random.nextInt(10)));
                Biome biome = iworldreader.getBiome(blockpos1);
                BlockPos blockpos2 = blockpos1.down();
                if (blockpos1.getY() <= blockpos.getY() + 10 && blockpos1.getY() >= blockpos.getY() - 10 && biome.getPrecipitation() == Biome.RainType.RAIN && biome.getTemperature(blockpos1) >= 0.15F)
                {
                    double d3 = random.nextDouble();
                    double d4 = random.nextDouble();
                    BlockState blockstate = iworldreader.getBlockState(blockpos2);
                    IFluidState ifluidstate = iworldreader.getFluidState(blockpos1);
                    VoxelShape voxelshape = blockstate.getCollisionShape(iworldreader, blockpos2);
                    double d7 = voxelshape.max(Direction.Axis.Y, d3, d4);
                    double d8 = ifluidstate.getActualHeight(iworldreader, blockpos1);
                    double d5;
                    double d6;
                    if (d7 >= d8)
                    {
                        d5 = d7;
                        d6 = voxelshape.min(Direction.Axis.Y, d3, d4);
                    }
                    else
                    {
                        d5 = 0.0D;
                        d6 = 0.0D;
                    }

                    if (d5 > -Double.MAX_VALUE)
                    {
                        if (!ifluidstate.isTagged(FluidTags.LAVA) && blockstate.getBlock() != Blocks.MAGMA_BLOCK && (blockstate.getBlock() != Blocks.CAMPFIRE || !blockstate.get(CampfireBlock.LIT)))
                        {
                            ++j;
                            x = (double) blockpos2.getX() + d3;
                            y = (double) ((float) blockpos2.getY() + 0.1F) + d5;
                            z = (double) blockpos2.getZ() + d4;
                            if (random.nextInt(j) == 0)
                            {
                                xx = x + d3;
                                yy = y - 1.0D;
                                zz = z + d4;
                            }

                            mc.world.addParticle(moddedProvider.getParticle(mc.world, x, y, z), x, y, z, 0.0D, 0.0D, 0.0D);
                        }
                        else
                        {
                            mc.world.addParticle(ParticleTypes.SMOKE, (double) blockpos1.getX() + d3, (double) ((float) blockpos1.getY() + 0.1F) - d6, (double) blockpos1.getZ() + d4, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            }

            if (j > 0 && random.nextInt(moddedProvider.getSoundInterval(f)) < rainSoundCounter++)
            {
                rainSoundCounter = 0;

                moddedProvider.weatherSounds(j, mc, world, blockpos, xx, yy, zz, random);
            }
        }

        // Bypass vanilla code after returning from this
        return 0;
    }
}
