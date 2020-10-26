package micdoodle8.mods.galacticraft.planets.venus.client;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.client.CloudRenderer;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.venus.dimension.DimensionVenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Iterator;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class TickHandlerClientVenus
{
    private final Map<BlockPos, Integer> lightning = Maps.newHashMap();

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;
        final ClientPlayerEntity playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

//        if (event.phase == TickEvent.Phase.END)
//        {
//        }
    }

    @SubscribeEvent
    public void renderLightning(ClientProxyCore.EventSpecialRender event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;
        if (player != null && !ConfigManagerPlanets.disableAmbientLightning.get())
        {
            Iterator<Map.Entry<BlockPos, Integer>> it = lightning.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry<BlockPos, Integer> entry = it.next();
                long seed = entry.getValue() / 10 + entry.getKey().getX() + entry.getKey().getZ();
                FakeLightningBoltRenderer.renderBolt(seed, entry.getKey().getX() - ClientProxyCore.playerPosX, entry.getKey().getY() - ClientProxyCore.playerPosY, entry.getKey().getZ() - ClientProxyCore.playerPosZ);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();

        final ClientWorld world = minecraft.world;

        if (world != null)
        {
            if (world.getDimension() instanceof DimensionVenus)
            {
//                if (world.getDimension().getSkyRenderer() == null)
//                {
//                    world.getDimension().setSkyRenderer(new SkyProviderVenus((IGalacticraftDimension) world.getDimension()));
//                } TODO Sky renderers

                if (world.getDimension().getCloudRenderer() == null)
                {
                    world.getDimension().setCloudRenderer(new CloudRenderer());
                }

//                if (world.getDimension().getWeatherRenderer() == null)
//                {
//                    world.getDimension().setWeatherRenderer(new WeatherRendererVenus());
//                } TODO Weather renderers
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientPlayerEntity player = minecraft.player;

        if (player == event.player)
        {
            if (!ConfigManagerPlanets.disableAmbientLightning.get())
            {
                Iterator<Map.Entry<BlockPos, Integer>> it = lightning.entrySet().iterator();
                while (it.hasNext())
                {
                    Map.Entry<BlockPos, Integer> entry = it.next();
                    int val = entry.getValue();
                    if (val - 1 <= 0)
                    {
                        it.remove();
                    }
                    else
                    {
                        entry.setValue(val - 1);
                    }
                }

                if (player.getRNG().nextInt(300 + (int) (800F * minecraft.world.rainingStrength)) == 0 && minecraft.world.getDimension() instanceof DimensionVenus)
                {
                    double freq = player.getRNG().nextDouble() * Math.PI * 2.0F;
                    double dist = 180.0F;
                    double dX = dist * Math.cos(freq);
                    double dZ = dist * Math.sin(freq);
                    double posX = player.getPosX() + dX;
                    double posY = 70;
                    double posZ = player.getPosZ() + dZ;
                    minecraft.world.playSound(player, posX, posY, posZ, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 500.0F + player.getRNG().nextFloat() * 500F, 1.0F + player.getRNG().nextFloat() * 0.2F);
                    lightning.put(new BlockPos(posX, posY, posZ), 20);
                }
            }
        }
    }
}
