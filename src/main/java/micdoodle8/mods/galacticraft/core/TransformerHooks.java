package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.api.entity.IAntiGrav;
import micdoodle8.mods.galacticraft.api.item.IArmorGravity;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.client.FootprintRenderer;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderZeroGravity;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Set;

import static micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore.PLAYER_Y_OFFSET;

/**
 * These methods are called from vanilla minecraft through bytecode injection done in MicdoodleCore
 *
 * See https://github.com/micdoodle8/MicdoodleCore
 */
@SuppressWarnings("unused")
public class TransformerHooks
{
    private static IWorldGenerator generatorGCGreg = null;
    private static IWorldGenerator generatorCoFH = null;
    private static IWorldGenerator generatorDenseOres = null;
    private static IWorldGenerator generatorTCAuraNodes = null;
    private static IWorldGenerator generatorAE2meteors = null;
    private static Method generateTCAuraNodes = null;
    private static boolean generatorsInitialised = false;

    public static double getGravityForEntity(Entity entity)
    {
        if (entity.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            if (entity instanceof EntityChicken && !OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.getEntityBoundingBox()))
            {
                return 0.08D;
            }

            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) entity.worldObj.provider;
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.inventory != null)
                {
                    int armorModLowGrav = 100;
                    int armorModHighGrav = 100;
                    for (int i = 0; i < 4; i++)
                    {
                        ItemStack armorPiece = player.getCurrentArmor(i);
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

    public static double getItemGravity(EntityItem e)
    {
        if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            final IGalacticraftWorldProvider customProvider = (IGalacticraftWorldProvider) e.worldObj.provider;
            return Math.max(0.002D, 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D);
        }
        else
        {
            return 0.03999999910593033D;
        }
    }

    public static float getArrowGravity(EntityArrow e)
    {
        if (e.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            return 0.005F;
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
            if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld)
            {
                return 0.0F;
            }
        }

        return world.prevRainingStrength + (world.rainingStrength - world.prevRainingStrength) * partialTicks;
    }

    public static boolean otherModPreventGenerate(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (!(world.provider instanceof IGalacticraftWorldProvider))
        {
            return false;
        }
        if (world.provider instanceof WorldProviderZeroGravity)
        {
            return true;
        }
        if (ConfigManagerCore.enableOtherModsFeatures)
        {
            return false;
        }

        if (!generatorsInitialised)
        {
            generatorsInitialised = true;

            try
            {
                Class GCGreg = Class.forName("bloodasp.galacticgreg.GT_Worldgenerator_Space");
                if (GCGreg != null)
                {
                    final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
                    regField.setAccessible(true);
                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
                    for (IWorldGenerator gen : registeredGenerators)
                    {
                        if (GCGreg.isInstance(gen))
                        {
                            generatorGCGreg = gen;
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }

            try
            {
                Class cofh = Class.forName("cofh.core.world.WorldHandler");
                if (cofh != null && ConfigManagerCore.whitelistCoFHCoreGen)
                {
                    final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
                    regField.setAccessible(true);
                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
                    for (IWorldGenerator gen : registeredGenerators)
                    {
                        if (cofh.isInstance(gen))
                        {
                            generatorCoFH = gen;
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }

            try
            {
                Class denseOres = Class.forName("com.rwtema.denseores.WorldGenOres");
                if (denseOres != null)
                {
                    final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
                    regField.setAccessible(true);
                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
                    for (IWorldGenerator gen : registeredGenerators)
                    {
                        if (denseOres.isInstance(gen))
                        {
                            generatorDenseOres = gen;
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }

            try
            {
                Class ae2meteorPlace = null;
                try
                {
                    ae2meteorPlace = Class.forName("appeng.hooks.MeteoriteWorldGen");
                }
                catch (ClassNotFoundException e)
                {
                }

                if (ae2meteorPlace == null)
                {
                    try
                    {
                        ae2meteorPlace = Class.forName("appeng.worldgen.MeteoriteWorldGen");
                    }
                    catch (ClassNotFoundException e)
                    {
                    }
                }

                if (ae2meteorPlace != null)
                {
                    final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
                    regField.setAccessible(true);
                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
                    for (IWorldGenerator gen : registeredGenerators)
                    {
                        if (ae2meteorPlace.isInstance(gen))
                        {
                            generatorAE2meteors = gen;
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
            }

            try
            {
                Class genThaumCraft = Class.forName("thaumcraft.common.lib.world.ThaumcraftWorldGenerator");
                if (genThaumCraft != null)
                {
                    final Field regField = Class.forName("cpw.mods.fml.common.registry.GameRegistry").getDeclaredField("worldGenerators");
                    regField.setAccessible(true);
                    Set<IWorldGenerator> registeredGenerators = (Set<IWorldGenerator>) regField.get(null);
                    for (IWorldGenerator gen : registeredGenerators)
                    {
                        if (genThaumCraft.isInstance(gen))
                        {
                            generatorTCAuraNodes = gen;
                            break;
                        }
                    }
                    if (generatorTCAuraNodes != null && ConfigManagerCore.enableThaumCraftNodes)
                    {
                        generateTCAuraNodes = genThaumCraft.getDeclaredMethod("generateWildNodes", World.class, Random.class, int.class, int.class, boolean.class, boolean.class);
                        generateTCAuraNodes.setAccessible(true);
                    }
                }

            }
            catch (Exception e)
            {
            }

            if (generatorGCGreg != null)
            {
                System.out.println("Whitelisting GalacticGreg oregen on planets.");
            }
            if (generatorCoFH != null)
            {
                System.out.println("Whitelisting CoFHCore custom oregen on planets.");
            }
            if (generatorDenseOres != null)
            {
                System.out.println("Whitelisting Dense Ores oregen on planets.");
            }
            if (generatorAE2meteors != null)
            {
                System.out.println("Whitelisting AE2 meteorites worldgen on planets.");
            }
            if (generatorTCAuraNodes != null && generateTCAuraNodes != null)
            {
                System.out.println("Whitelisting ThaumCraft aura node generation on planets.");
            }
        }

        if (generatorGCGreg != null || generatorCoFH != null || generatorDenseOres != null || generatorTCAuraNodes != null || generatorAE2meteors != null)
        {
            try
            {
                long worldSeed = world.getSeed();
                Random fmlRandom = new Random(worldSeed);
                long xSeed = fmlRandom.nextLong() >> 2 + 1L;
                long zSeed = fmlRandom.nextLong() >> 2 + 1L;
                long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
                fmlRandom.setSeed(chunkSeed);

                if (generatorCoFH != null)
                {
                    generatorCoFH.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                }
                if (generatorDenseOres != null)
                {
                    generatorDenseOres.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                }
                if (generatorGCGreg != null)
                {
                    generatorGCGreg.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                }
                if (generatorAE2meteors != null)
                {
                    generatorAE2meteors.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
                }
                if (generateTCAuraNodes != null)
                {
                    generateTCAuraNodes.invoke(generatorTCAuraNodes, world, fmlRandom, chunkX, chunkZ, false, true);
                }

            }
            catch (Exception e)
            {
                GCLog.severe("Error in another mod's worldgen.  This is NOT a Galacticraft bug.");
                e.printStackTrace();
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static float getWorldBrightness(WorldClient world)
    {
        if (world.provider instanceof WorldProviderMoon)
        {
            float f1 = world.getCelestialAngle(1.0F);
            float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.2F);

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

    @SideOnly(Side.CLIENT)
    public static float getColorRed(World world)
    {
        return (float) WorldUtil.getWorldColor(world).x;
    }

    @SideOnly(Side.CLIENT)
    public static float getColorGreen(World world)
    {
        return (float) WorldUtil.getWorldColor(world).y;
    }

    @SideOnly(Side.CLIENT)
    public static float getColorBlue(World world)
    {
        return (float) WorldUtil.getWorldColor(world).z;
    }

    @SideOnly(Side.CLIENT)
    public static Vec3 getFogColorHook(World world)
    {
        EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;
        if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld)
        {
            float var20 = ((float) (player.posY) - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 1000.0F;
            var20 = MathHelper.sqrt_float(var20);
            final float var21 = Math.max(1.0F - var20 * 40.0F, 0.0F);

            Vec3 vec = world.getFogColor(1.0F);

            return new Vec3(vec.xCoord * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.yCoord * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.zCoord * Math.max(1.0F - var20 * 1.29F, 0.0F));
        }

        return world.getFogColor(1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static Vec3 getSkyColorHook(World world)
    {
        EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;
        if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld || (player != null && player.posY > Constants.OVERWORLD_CLOUD_HEIGHT && player.ridingEntity instanceof EntitySpaceshipBase))
        {
            float f1 = world.getCelestialAngle(1.0F);
            float f2 = MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            int i = MathHelper.floor_double(player.posX);
            int j = MathHelper.floor_double(player.posY);
            int k = MathHelper.floor_double(player.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            int l = ForgeHooksClient.getSkyBlendColour(world, pos);
            float f4 = (float) (l >> 16 & 255) / 255.0F;
            float f5 = (float) (l >> 8 & 255) / 255.0F;
            float f6 = (float) (l & 255) / 255.0F;
            f4 *= f2;
            f5 *= f2;
            f6 *= f2;

            if (player.posY <= Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
            {
                Vec3 vec = world.getSkyColor(FMLClientHandler.instance().getClient().getRenderViewEntity(), 1.0F);
                double blend = (player.posY - Constants.OVERWORLD_CLOUD_HEIGHT) / (Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT - Constants.OVERWORLD_CLOUD_HEIGHT);
                double ablend = 1 - blend;
                return new Vec3(f4 * blend + vec.xCoord * ablend, f5 * blend + vec.yCoord * ablend, f6 * blend + vec.zCoord * ablend);
            }
            else
            {
                double blend = Math.min(1.0D, (player.posY - Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT) / 300.0D);
                double ablend = 1.0D - blend;
                blend /= 255.0D;
                return new Vec3(f4 * ablend + blend * 31.0D, f5 * ablend + blend * 8.0D, f6 * ablend + blend * 99.0D);
            }
        }

        return world.getSkyColor(FMLClientHandler.instance().getClient().getRenderViewEntity(), 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static double getRenderPosY(Entity viewEntity, double regular)
    {
        if (viewEntity.posY >= 256)
        {
            return 255.0F;
        }
        else
        {
            return regular + viewEntity.getEyeHeight();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean shouldRenderFire(Entity entity)
    {
        if (entity.worldObj == null || !(entity.worldObj.provider instanceof IGalacticraftWorldProvider))
        {
            return entity.isBurning();
        }

        if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityArrow))
        {
            return entity.isBurning();
        }

        if (entity.isBurning())
        {
            if (OxygenUtil.noAtmosphericCombustion(entity.worldObj.provider))
            {
                return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.getEntityBoundingBox());
            }
            else
            {
                return true;
            }
            //Disable fire on Galacticraft worlds with no oxygen
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void orientCamera(float partialTicks)
    {
        EntityPlayerSP player = ClientProxyCore.mc.thePlayer;
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        Entity viewEntity = ClientProxyCore.mc.getRenderViewEntity();

        if (player.ridingEntity instanceof EntityTieredRocket && ClientProxyCore.mc.gameSettings.thirdPersonView == 0)
        {
            EntityTieredRocket entity = (EntityTieredRocket) player.ridingEntity;
            float offset = entity.getRotateOffset() + PLAYER_Y_OFFSET;
            GL11.glTranslatef(0, -offset, 0);
            float anglePitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float angleYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
            GL11.glRotatef(-anglePitch, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(angleYaw, 0.0F, 1.0F, 0.0F);

            GL11.glTranslatef(0, offset, 0);
        }

        if (viewEntity instanceof EntityLivingBase && viewEntity.worldObj.provider instanceof WorldProviderZeroGravity && !((EntityLivingBase)viewEntity).isPlayerSleeping())
        {
            float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;
            float yaw = viewEntity.prevRotationYaw + (viewEntity.rotationYaw - viewEntity.prevRotationYaw) * partialTicks + 180.0F;
            float eyeHeightChange = viewEntity.width / 2.0F;

//            GL11.glTranslatef(0.0F, -f1, 0.0F);
            GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.1F);

            GL11.glRotatef(180.0F * stats.gdir.getThetaX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F * stats.gdir.getThetaZ(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(pitch * stats.gdir.getPitchGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(pitch * stats.gdir.getPitchGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * stats.gdir.getYawGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yaw * stats.gdir.getYawGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * stats.gdir.getYawGravityZ(), 0.0F, 0.0F, 1.0F);

            if (stats.landingTicks > 0)
            {
            	float sneakY;
            	if (stats.landingTicks >= 4) sneakY = (stats.landingTicks >= 5) ? 0.15F : 0.3F;
            	else
            		sneakY = stats.landingTicks * 0.075F;
            	GL11.glTranslatef(sneakY * stats.gdir.getSneakVecX(), sneakY * stats.gdir.getSneakVecY(), sneakY * stats.gdir.getSneakVecZ());
            }

            GL11.glTranslatef(eyeHeightChange * stats.gdir.getEyeVecX(), eyeHeightChange * stats.gdir.getEyeVecY(), eyeHeightChange * stats.gdir.getEyeVecZ());

            if (stats.gravityTurnRate < 1.0F)
            {
                GL11.glRotatef(90.0F * (stats.gravityTurnRatePrev + (stats.gravityTurnRate - stats.gravityTurnRatePrev) * partialTicks), stats.gravityTurnVecX, stats.gravityTurnVecY, stats.gravityTurnVecZ);
            }
        }

        //omit this for interesting 3P views
//        GL11.glTranslatef(0.0F, 0.0F, -0.1F);
//        GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glTranslatef(0.0F, f1, 0.0F);
    }

    @SideOnly(Side.CLIENT)
    public static void renderLiquidOverlays(float partialTicks)
    {
        if (FluidUtil.isInsideOfFluid(ClientProxyCore.mc.thePlayer, GCFluids.fluidOil))
        {
            ClientProxyCore.mc.getTextureManager().bindTexture(ClientProxyCore.underOilTexture);
        }
        else
        {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        float f1 = ClientProxyCore.mc.thePlayer.getBrightness(partialTicks) / 3.0F;
        GL11.glColor4f(f1, f1, f1, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f2 = 4.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -1.0F;
        float f6 = 1.0F;
        float f7 = -0.5F;
        float f8 = -ClientProxyCore.mc.thePlayer.rotationYaw / 64.0F;
        float f9 = ClientProxyCore.mc.thePlayer.rotationPitch / 64.0F;
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
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

    @SideOnly(Side.CLIENT)
    public static void renderFootprints(float partialTicks)
    {
        FootprintRenderer.renderFootprints(ClientProxyCore.mc.thePlayer, partialTicks);
        MinecraftForge.EVENT_BUS.post(new ClientProxyCore.EventSpecialRender(partialTicks));
    }
}
