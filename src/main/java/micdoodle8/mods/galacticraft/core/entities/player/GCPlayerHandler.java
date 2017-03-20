package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.item.EnumExtendedInventorySlot;
import micdoodle8.mods.galacticraft.api.item.IItemThermal;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ITeleportType;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderSpaceStation;
import micdoodle8.mods.galacticraft.core.entities.EntityCelestialFake;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.items.ItemParaChute;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.venus.VenusItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class GCPlayerHandler
{
    private static final int OXYGENHEIGHTLIMIT = 450;
    private boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();
    private ConcurrentHashMap<UUID, GCPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, GCPlayerStats>();
    private HashMap<Item, Item> torchItems = new HashMap<Item, Item>();

    public ConcurrentHashMap<UUID, GCPlayerStats> getServerStatList()
    {
        return this.playerStatsMap;
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerLogin((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerLoggedOutEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerLogout((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerRespawn((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerMP && GCPlayerStats.get((EntityPlayerMP) event.entity) == null)
        {
            GCPlayerStats.register((EntityPlayerMP) event.entity);
        }

        if (isClient)
        {
            this.onEntityConstructingClient(event);
        }
    }

    @SideOnly(Side.CLIENT)
    public void onEntityConstructingClient(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayerSP)
        {
            if (GCPlayerStatsClient.get((EntityPlayerSP) event.entity) == null)
            {
                GCPlayerStatsClient.register((EntityPlayerSP) event.entity);
            }

            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
        }
    }

    private void onPlayerLogin(EntityPlayerMP player)
    {
        GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
        if (oldData != null)
        {
            oldData.saveNBTData(player.getEntityData());
        }

        GCPlayerStats stats = GCPlayerStats.get(player);

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, player.worldObj.provider.getDimensionId(), new Object[] {}), player);
        int repeatCount = stats.buildFlags >> 9;
        if (repeatCount < 3)
        {
            stats.buildFlags &= 1536;
        }
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, player.worldObj.provider.getDimensionId(), new Object[] { stats.buildFlags }), player);
        ColorUtil.sendUpdatedColorsToPlayer(stats);
    }

    private void onPlayerLogout(EntityPlayerMP player)
    {
    }

    private void onPlayerRespawn(EntityPlayerMP player)
    {
        GCPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
        GCPlayerStats stats = GCPlayerStats.get(player);

        if (oldData != null)
        {
            stats.copyFrom(oldData, false);
        }

        stats.player = new WeakReference<EntityPlayerMP>(player);
    }

    public static void checkGear(EntityPlayerMP player, GCPlayerStats GCPlayer, boolean forceSend)
    {
        GCPlayer.maskInSlot = GCPlayer.extendedInventory.getStackInSlot(0);
        GCPlayer.gearInSlot = GCPlayer.extendedInventory.getStackInSlot(1);
        GCPlayer.tankInSlot1 = GCPlayer.extendedInventory.getStackInSlot(2);
        GCPlayer.tankInSlot2 = GCPlayer.extendedInventory.getStackInSlot(3);
        GCPlayer.parachuteInSlot = GCPlayer.extendedInventory.getStackInSlot(4);
        GCPlayer.frequencyModuleInSlot = GCPlayer.extendedInventory.getStackInSlot(5);
        GCPlayer.thermalHelmetInSlot = GCPlayer.extendedInventory.getStackInSlot(6);
        GCPlayer.thermalChestplateInSlot = GCPlayer.extendedInventory.getStackInSlot(7);
        GCPlayer.thermalLeggingsInSlot = GCPlayer.extendedInventory.getStackInSlot(8);
        GCPlayer.thermalBootsInSlot = GCPlayer.extendedInventory.getStackInSlot(9);
        GCPlayer.shieldControllerInSlot = GCPlayer.extendedInventory.getStackInSlot(10);
        //

        if (GCPlayer.frequencyModuleInSlot != GCPlayer.lastFrequencyModuleInSlot || forceSend)
        {
            if (FMLCommonHandler.instance().getMinecraftServerInstance() != null)
            {
                if (GCPlayer.frequencyModuleInSlot == null)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.FREQUENCY_MODULE);
                    TileEntityTelemetry.frequencyModulePlayer(GCPlayer.lastFrequencyModuleInSlot, null);
                }
                else if (GCPlayer.lastFrequencyModuleInSlot == null)
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.frequencyModuleInSlot, EnumExtendedInventorySlot.FREQUENCY_MODULE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.FREQUENCY_MODULE, gearID);
                        TileEntityTelemetry.frequencyModulePlayer(GCPlayer.frequencyModuleInSlot, player);
                    }
                }
            }

            GCPlayer.lastFrequencyModuleInSlot = GCPlayer.frequencyModuleInSlot;
        }

        //

        if (GCPlayer.maskInSlot != GCPlayer.lastMaskInSlot || forceSend)
        {
            if (GCPlayer.maskInSlot == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.MASK);
            }
            else if (GCPlayer.lastMaskInSlot == null || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.maskInSlot, EnumExtendedInventorySlot.MASK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.MASK, gearID);
                }
            }

            GCPlayer.lastMaskInSlot = GCPlayer.maskInSlot;
        }

        //

        if (GCPlayer.gearInSlot != GCPlayer.lastGearInSlot || forceSend)
        {
            if (GCPlayer.gearInSlot == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.GEAR);
            }
            else if (GCPlayer.gearInSlot.getItem() == GCItems.oxygenGear && (GCPlayer.lastGearInSlot == null || forceSend))
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.gearInSlot, EnumExtendedInventorySlot.GEAR);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.GEAR, gearID);
                }
            }

            GCPlayer.lastGearInSlot = GCPlayer.gearInSlot;
        }

        //

        if (GCPlayer.tankInSlot1 != GCPlayer.lastTankInSlot1 || forceSend)
        {
            if (GCPlayer.tankInSlot1 == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.LEFT_TANK);
                GCPlayer.airRemaining = 0;
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }
            else if (GCPlayer.lastTankInSlot1 == null || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.tankInSlot1, EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                GCPlayer.airRemaining = GCPlayer.tankInSlot1.getMaxDamage() - GCPlayer.tankInSlot1.getItemDamage();
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (GCPlayer.tankInSlot1.getItem() != GCPlayer.lastTankInSlot1.getItem())
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.tankInSlot1, EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                GCPlayer.airRemaining = GCPlayer.tankInSlot1.getMaxDamage() - GCPlayer.tankInSlot1.getItemDamage();
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }

            GCPlayer.lastTankInSlot1 = GCPlayer.tankInSlot1;
        }

        //

        if (GCPlayer.tankInSlot2 != GCPlayer.lastTankInSlot2 || forceSend)
        {
            if (GCPlayer.tankInSlot2 == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.RIGHT_TANK);
                GCPlayer.airRemaining2 = 0;
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }
            else if (GCPlayer.lastTankInSlot2 == null || forceSend)
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.tankInSlot2, EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                GCPlayer.airRemaining2 = GCPlayer.tankInSlot2.getMaxDamage() - GCPlayer.tankInSlot2.getItemDamage();
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (GCPlayer.tankInSlot2.getItem() != GCPlayer.lastTankInSlot2.getItem())
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.tankInSlot2, EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                GCPlayer.airRemaining2 = GCPlayer.tankInSlot2.getMaxDamage() - GCPlayer.tankInSlot2.getItemDamage();
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
            }

            GCPlayer.lastTankInSlot2 = GCPlayer.tankInSlot2;
        }

        //

        if (GCPlayer.parachuteInSlot != GCPlayer.lastParachuteInSlot || forceSend)
        {
            if (GCPlayer.parachuteInSlot == null)
            {
                if (GCPlayer.usingParachute)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
                }
            }
            else if (GCPlayer.lastParachuteInSlot == null || forceSend)
            {
                if (GCPlayer.usingParachute)
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.parachuteInSlot, EnumExtendedInventorySlot.PARACHUTE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, GCPlayer.parachuteInSlot.getItemDamage());
                    }
                }
            }
            else if (GCPlayer.parachuteInSlot.getItemDamage() != GCPlayer.lastParachuteInSlot.getItemDamage())
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.parachuteInSlot, EnumExtendedInventorySlot.PARACHUTE);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, GCPlayer.parachuteInSlot.getItemDamage());
                }
            }

            GCPlayer.lastParachuteInSlot = GCPlayer.parachuteInSlot;
        }

        //

        if (GCPlayer.thermalHelmetInSlot != GCPlayer.lastThermalHelmetInSlot || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(0, GCPlayer.thermalHelmetInSlot);
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (GCPlayer.thermalHelmetInSlot == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_HELMET);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalHelmetInSlot == null || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.thermalHelmetInSlot, EnumExtendedInventorySlot.THERMAL_HELMET);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_HELMET, gearID);
                    }
                }
            }

            GCPlayer.lastThermalHelmetInSlot = GCPlayer.thermalHelmetInSlot;
        }

        if (GCPlayer.thermalChestplateInSlot != GCPlayer.lastThermalChestplateInSlot || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(1, GCPlayer.thermalChestplateInSlot);
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (GCPlayer.thermalChestplateInSlot == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalChestplateInSlot == null || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.thermalChestplateInSlot, EnumExtendedInventorySlot.THERMAL_CHESTPLATE);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, gearID);
                    }
                }
            }

            GCPlayer.lastThermalChestplateInSlot = GCPlayer.thermalChestplateInSlot;
        }

        if (GCPlayer.thermalLeggingsInSlot != GCPlayer.lastThermalLeggingsInSlot || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(2, GCPlayer.thermalLeggingsInSlot);
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (GCPlayer.thermalLeggingsInSlot == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_LEGGINGS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalLeggingsInSlot == null || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.thermalLeggingsInSlot, EnumExtendedInventorySlot.THERMAL_LEGGINGS);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_LEGGINGS, gearID);
                    }
                }
            }

            GCPlayer.lastThermalLeggingsInSlot = GCPlayer.thermalLeggingsInSlot;
        }

        if (GCPlayer.thermalBootsInSlot != GCPlayer.lastThermalBootsInSlot || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(3, GCPlayer.thermalBootsInSlot);
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (GCPlayer.thermalBootsInSlot == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_BOOTS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalBootsInSlot == null || forceSend))
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.thermalBootsInSlot, EnumExtendedInventorySlot.THERMAL_BOOTS);

                    if (gearID >= 0)
                    {
                        GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_BOOTS, gearID);
                    }
                }
            }

            GCPlayer.lastThermalBootsInSlot = GCPlayer.thermalBootsInSlot;
        }

        if ((GCPlayer.shieldControllerInSlot != GCPlayer.lastShieldControllerInSlot || forceSend) && GalacticraftCore.isPlanetsLoaded)
        {
            if (GCPlayer.shieldControllerInSlot == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.SHIELD_CONTROLLER);
            }
            else if (GCPlayer.shieldControllerInSlot.getItem() == VenusItems.basicItem && (GCPlayer.lastShieldControllerInSlot == null || forceSend))
            {
                int gearID = GalacticraftRegistry.findMatchingGearID(GCPlayer.shieldControllerInSlot, EnumExtendedInventorySlot.SHIELD_CONTROLLER);

                if (gearID >= 0)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.SHIELD_CONTROLLER, gearID);
                }
            }

            GCPlayer.lastShieldControllerInSlot = GCPlayer.shieldControllerInSlot;
        }
    }

    protected void checkThermalStatus(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        if (player.worldObj.provider instanceof IGalacticraftWorldProvider && !player.capabilities.isCreativeMode)
        {
            final ItemStack thermalPaddingHelm = playerStats.extendedInventory.getStackInSlot(6);
            final ItemStack thermalPaddingChestplate = playerStats.extendedInventory.getStackInSlot(7);
            final ItemStack thermalPaddingLeggings = playerStats.extendedInventory.getStackInSlot(8);
            final ItemStack thermalPaddingBoots = playerStats.extendedInventory.getStackInSlot(9);
            float lowestThermalStrength = 0.0F;
            if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
            {
                if (thermalPaddingHelm.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingHelm.getItem()).getThermalStrength();
                }
                if (thermalPaddingChestplate.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingChestplate.getItem()).getThermalStrength();
                }
                if (thermalPaddingLeggings.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingLeggings.getItem()).getThermalStrength();
                }
                if (thermalPaddingBoots.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingBoots.getItem()).getThermalStrength();
                }
                lowestThermalStrength /= 4.0F;
            }

            IGalacticraftWorldProvider provider = (IGalacticraftWorldProvider) player.worldObj.provider;
            float thermalLevelMod = provider.getThermalLevelModifier();
            double absThermalLevelMod = Math.abs(thermalLevelMod);

            if (absThermalLevelMod > 0D)
            {
                int thermalLevelCooldownBase = Math.abs(MathHelper.floor_double(200 / thermalLevelMod));
                int normaliseCooldown = Math.abs(MathHelper.floor_double(150 / lowestThermalStrength));
                int thermalLevelTickCooldown = thermalLevelCooldownBase;
                if (thermalLevelTickCooldown < 1)
                {
                    thermalLevelTickCooldown = 1;   //Prevent divide by zero errors
                }

                if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
                {
                    thermalLevelMod /= Math.max(1.0F, lowestThermalStrength / 2.0F);
                    absThermalLevelMod = Math.abs(thermalLevelMod);
                    normaliseCooldown = MathHelper.floor_double(normaliseCooldown / absThermalLevelMod);
                    if (normaliseCooldown < 1)
                    {
                        normaliseCooldown = 1;   //Prevent divide by zero errors
                    }
                    // Player is wearing all required thermal padding items
                    if ((player.ticksExisted - 1) % normaliseCooldown == 0)
                    {
                        this.normaliseThermalLevel(player, playerStats, 1);
                    }
                }

                if (OxygenUtil.isAABBInBreathableAirBlock(player, true))
                {
                    playerStats.thermalLevelNormalising = true;
                    this.normaliseThermalLevel(player, playerStats, 1);
                    // If player is in ambient thermal area, slowly reset to normal
                    return;
                }

                // For each piece of thermal equipment being used, slow down the the harmful thermal change slightly
                if (thermalPaddingHelm != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingChestplate != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingLeggings != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingBoots != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }

                // Instead of increasing/decreasing the thermal level by a large amount every ~200 ticks, increase/decrease
                //      by a small amount each time (still the same average increase/decrease)
                int thermalLevelTickCooldownSingle = MathHelper.floor_double(thermalLevelTickCooldown / absThermalLevelMod);
                if (thermalLevelTickCooldownSingle < 1)
                {
                    thermalLevelTickCooldownSingle = 1;   //Prevent divide by zero errors
                }

                if ((player.ticksExisted - 1) % thermalLevelTickCooldownSingle == 0)
                {
                    int last = playerStats.thermalLevel;
                    playerStats.thermalLevel = (int) Math.min(Math.max(playerStats.thermalLevel + (thermalLevelMod < 0 ? -1 : 1), -22), 22);

                    if (playerStats.thermalLevel != last)
                    {
                        this.sendThermalLevelPacket(player, playerStats);
                    }
                }

                // If the normalisation is outpacing the freeze/overheat
                playerStats.thermalLevelNormalising = thermalLevelTickCooldownSingle > normaliseCooldown &&
                        thermalPaddingHelm != null &&
                        thermalPaddingChestplate != null &&
                        thermalPaddingLeggings != null &&
                        thermalPaddingBoots != null;

                if (!playerStats.thermalLevelNormalising)
                {
                    if ((player.ticksExisted - 1) % thermalLevelTickCooldown == 0)
                    {
                        if (Math.abs(playerStats.thermalLevel) >= 22)
                        {
                            player.attackEntityFrom(DamageSourceGC.thermal, 1.5F);
                        }
                    }

                    if (playerStats.thermalLevel < -15)
                    {
                        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 5, 2, true, true));
                    }

                    if (playerStats.thermalLevel > 15)
                    {
                        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 5, 2, true, true));
                    }
                }
            }
            else
            //Normalise thermal level if on Space Station or non-modifier planet
            {
                playerStats.thermalLevelNormalising = true;
                this.normaliseThermalLevel(player, playerStats, 2);
            }
        }
        else
        //Normalise thermal level if on Overworld or any non-GC dimension
        {
            playerStats.thermalLevelNormalising = true;
            this.normaliseThermalLevel(player, playerStats, 3);
        }
    }

    public void normaliseThermalLevel(EntityPlayerMP player, GCPlayerStats playerStats, int increment)
    {
        final int last = playerStats.thermalLevel;

        if (playerStats.thermalLevel < 0)
        {
            playerStats.thermalLevel += Math.min(increment, -playerStats.thermalLevel);
        }
        else if (playerStats.thermalLevel > 0)
        {
            playerStats.thermalLevel -= Math.min(increment, playerStats.thermalLevel);
        }

        if (playerStats.thermalLevel != last)
        {
            this.sendThermalLevelPacket(player, playerStats);
        }
    }

    protected void checkShield(EntityPlayerMP playerMP, GCPlayerStats playerStats)
    {
        if (playerMP.ticksExisted % 20 == 0 && playerMP.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            if (((IGalacticraftWorldProvider) playerMP.worldObj.provider).shouldCorrodeArmor())
            {
                ItemStack shieldController = playerStats.extendedInventory.getStackInSlot(10);
                boolean valid = false;

                if (shieldController != null)
                {
                    int gearID = GalacticraftRegistry.findMatchingGearID(shieldController, EnumExtendedInventorySlot.SHIELD_CONTROLLER);

                    if (gearID != -1)
                    {
                        valid = true;
                    }
                }

                if (!valid)
                {
                    ItemStack armor = playerMP.getCurrentArmor((int) (Math.random() * 4));
                    if (armor != null)
                    {
                        armor.damageItem(1, playerMP);
                    }
                }
            }
        }
    }

    protected void checkOxygen(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        if ((player.dimension == 0 || player.worldObj.provider instanceof IGalacticraftWorldProvider) && (!(player.dimension == 0 || ((IGalacticraftWorldProvider) player.worldObj.provider).hasBreathableAtmosphere()) || player.posY > GCPlayerHandler.OXYGENHEIGHTLIMIT) && !player.capabilities.isCreativeMode && !(player.ridingEntity instanceof EntityLanderBase) && !(player.ridingEntity instanceof EntityAutoRocket) && !(player.ridingEntity instanceof EntityCelestialFake))
        {
            final ItemStack tankInSlot = playerStats.extendedInventory.getStackInSlot(2);
            final ItemStack tankInSlot2 = playerStats.extendedInventory.getStackInSlot(3);

            final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

            if (tankInSlot == null)
            {
                playerStats.airRemaining = 0;
            }
            else
            {
                playerStats.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
            }

            if (tankInSlot2 == null)
            {
                playerStats.airRemaining2 = 0;
            }
            else
            {
                playerStats.airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
            }

            if (drainSpacing > 0)
            {
                if ((player.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(player) && !playerStats.usingPlanetSelectionGui)
                {
                    int toTake = 1;
                    //Take 1 oxygen from Tank 1
                    if (playerStats.airRemaining > 0)
                    {
                        tankInSlot.damageItem(1, player);
                        playerStats.airRemaining--;
                        toTake = 0;
                    }

                    //Alternatively, take 1 oxygen from Tank 2
                    if (toTake > 0 && playerStats.airRemaining2 > 0)
                    {
                        tankInSlot2.damageItem(1, player);
                        playerStats.airRemaining2--;
                        toTake = 0;
                    }
                }
            }
            else
            {
                if ((player.ticksExisted - 1) % 60 == 0)
                {
                    if (OxygenUtil.isAABBInBreathableAirBlock(player))
                    {
                        if (playerStats.airRemaining < 90 && tankInSlot != null)
                        {
                            playerStats.airRemaining = Math.min(playerStats.airRemaining + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
                        }

                        if (playerStats.airRemaining2 < 90 && tankInSlot2 != null)
                        {
                            playerStats.airRemaining2 = Math.min(playerStats.airRemaining2 + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
                        }
                    }
                    else
                    {
                        if (playerStats.airRemaining > 0)
                        {
                            playerStats.airRemaining--;
                        }

                        if (playerStats.airRemaining2 > 0)
                        {
                            playerStats.airRemaining2--;
                        }
                    }
                }
            }

            final boolean airEmpty = playerStats.airRemaining <= 0 && playerStats.airRemaining2 <= 0;

            if (player.isOnLadder())
            {
                playerStats.oxygenSetupValid = playerStats.lastOxygenSetupValid;
            }
            else
            {
                playerStats.oxygenSetupValid = !((!OxygenUtil.hasValidOxygenSetup(player) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(player));
            }

            if (!player.worldObj.isRemote && player.isEntityAlive())
            {
            	if (!playerStats.oxygenSetupValid)
            	{
        			GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(player);
        			MinecraftForge.EVENT_BUS.post(suffocationEvent);

        			if (!suffocationEvent.isCanceled())
        			{
                		if (playerStats.damageCounter == 0)
                		{
                			playerStats.damageCounter = ConfigManagerCore.suffocationCooldown;

            				player.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage * (2 + playerStats.incrementalDamage) / 2);
            				if (ConfigManagerCore.hardMode) playerStats.incrementalDamage++;

            				GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(player);
            				MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                		}
        			}
        			else
        				playerStats.oxygenSetupValid = true;
            	}
        		else
        			playerStats.incrementalDamage = 0;
            }
        }
        else if ((player.ticksExisted - 1) % 20 == 0 && !player.capabilities.isCreativeMode && playerStats.airRemaining < 90)
        {
            playerStats.airRemaining += 1;
            playerStats.airRemaining2 += 1;
        }
        else if (player.capabilities.isCreativeMode)
        {
            playerStats.airRemaining = 90;
            playerStats.airRemaining2 = 90;
        }
        else
        {
            playerStats.oxygenSetupValid = true;
        }
    }

    protected void throwMeteors(EntityPlayerMP player)
    {
        World world = player.worldObj;
        if (world.provider instanceof IGalacticraftWorldProvider && !world.isRemote)
        {
            if (((IGalacticraftWorldProvider) world.provider).getMeteorFrequency() > 0 && ConfigManagerCore.meteorSpawnMod > 0.0)
            {
                final int f = (int) (((IGalacticraftWorldProvider) world.provider).getMeteorFrequency() * 1000D * (1.0 / ConfigManagerCore.meteorSpawnMod));

                if (world.rand.nextInt(f) == 0)
                {
                    final EntityPlayer closestPlayer = world.getClosestPlayerToEntity(player, 100);

                    if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
                    {
                        int x, y, z;
                        double motX, motZ;
                        x = world.rand.nextInt(20) - 10;
                        y = world.rand.nextInt(20) + 200;
                        z = world.rand.nextInt(20) - 10;
                        motX = world.rand.nextDouble() * 5;
                        motZ = world.rand.nextDouble() * 5;

                        final EntityMeteor meteor = new EntityMeteor(world, player.posX + x, player.posY + y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

                        if (!world.isRemote)
                        {
                            world.spawnEntityInWorld(meteor);
                        }
                    }
                }

                if (world.rand.nextInt(f * 3) == 0)
                {
                    final EntityPlayer closestPlayer = world.getClosestPlayerToEntity(player, 100);

                    if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
                    {
                        int x, y, z;
                        double motX, motZ;
                        x = world.rand.nextInt(20) - 10;
                        y = world.rand.nextInt(20) + 200;
                        z = world.rand.nextInt(20) - 10;
                        motX = world.rand.nextDouble() * 5;
                        motZ = world.rand.nextDouble() * 5;

                        final EntityMeteor meteor = new EntityMeteor(world, player.posX + x, player.posY + y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

                        if (!world.isRemote)
                        {
                            world.spawnEntityInWorld(meteor);
                        }
                    }
                }
            }
        }
    }

    protected void checkCurrentItem(EntityPlayerMP player)
    {
        ItemStack theCurrentItem = player.inventory.getCurrentItem();
        if (theCurrentItem != null)
        {
            if (OxygenUtil.noAtmosphericCombustion(player.worldObj.provider))
            {
                //Is it a type of overworld torch?
                if (torchItems.containsValue(theCurrentItem.getItem()))
                {
                    Item torchItem = null;
                    //Get space torch for this overworld torch
                    for (Item i : torchItems.keySet())
                    {
                        if (torchItems.get(i) == theCurrentItem.getItem())
                        {
                            torchItem = i;
                            break;
                        }
                    }
                    if (torchItem != null)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = new ItemStack(torchItem, theCurrentItem.stackSize, 0);
                    }
                }
            }
            else
            {
                //Is it a type of space torch?
                if (torchItems.containsKey(theCurrentItem.getItem()))
                {
                    //Get overworld torch for this space torch
                    Item torchItem = torchItems.get(theCurrentItem.getItem());
                    if (torchItem != null)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = new ItemStack(torchItem, theCurrentItem.stackSize, 0);
                    }
                }
            }
        }
    }

    public void registerTorchType(BlockUnlitTorch spaceTorch, Block vanillaTorch)
    {
        //Space Torch registration must be unique; there may be multiple mappings for vanillaTorch
        Item itemSpaceTorch = Item.getItemFromBlock(spaceTorch);
        Item itemVanillaTorch = Item.getItemFromBlock(vanillaTorch);
        torchItems.put(itemSpaceTorch, itemVanillaTorch);
    }

    public static void setUsingParachute(EntityPlayerMP player, GCPlayerStats playerStats, boolean tf)
    {
        playerStats.usingParachute = tf;

        if (tf)
        {
            int subtype = -1;

            if (playerStats.parachuteInSlot != null)
            {
                subtype = playerStats.parachuteInSlot.getItemDamage();
            }

            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, subtype);
        }
        else
        {
            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
        }
    }

    protected static void updateFeet(EntityPlayerMP player, double motionX, double motionZ)
    {
        double motionSqrd = motionX * motionX + motionZ * motionZ;
        if (motionSqrd > 0.001D && !player.capabilities.isFlying)
        {
            int iPosX = MathHelper.floor_double(player.posX);
            int iPosY = MathHelper.floor_double(player.posY) - 1;
            int iPosZ = MathHelper.floor_double(player.posZ);

            // If the block below is the moon block
            IBlockState state = player.worldObj.getBlockState(new BlockPos(iPosX, iPosY, iPosZ));
            if (state.getBlock() == GCBlocks.blockMoon)
            {
                // And is the correct metadata (moon turf)
                if (state.getBlock().getMetaFromState(state) == 5)
                {
                    GCPlayerStats playerStats = GCPlayerStats.get(player);
                    // If it has been long enough since the last step
                    if (playerStats.distanceSinceLastStep > 0.35D)
                    {
                        Vector3 pos = new Vector3(player);
                        // Set the footprint position to the block below and add random number to stop z-fighting
                        pos.y = MathHelper.floor_double(player.posY - 1D) + player.worldObj.rand.nextFloat() / 100.0F;

                        // Adjust footprint to left or right depending on step count
                        switch (playerStats.lastStep)
                        {
                        case 0:
                            float a = (-player.rotationYaw + 90F) / 57.295779513F;
                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25F, 0, MathHelper.cos(a) * 0.25F));
                            break;
                        case 1:
                            a = (-player.rotationYaw - 90F) / 57.295779513F;
                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25, 0, MathHelper.cos(a) * 0.25));
                            break;
                        }

                        float rotation = player.rotationYaw - 180;
                        pos = WorldUtil.getFootprintPosition(player.worldObj, rotation, pos, new BlockVec3(player));

                        long chunkKey = ChunkCoordIntPair.chunkXZ2Int(pos.intX() >> 4, pos.intZ() >> 4);
                        TickHandlerServer.addFootprint(chunkKey, new Footprint(player.worldObj.provider.getDimensionId(), pos, rotation, player.getName()), player.worldObj.provider.getDimensionId());

                        // Increment and cap step counter at 1
                        playerStats.lastStep++;
                        playerStats.lastStep %= 2;
                        playerStats.distanceSinceLastStep = 0;
                    }
                    else
                    {
                        playerStats.distanceSinceLastStep += motionSqrd;
                    }
                }
            }
        }
    }

    protected void updateSchematics(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        SchematicRegistry.addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(0));
        SchematicRegistry.addUnlockedPage(player, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

        Collections.sort(playerStats.unlockedSchematics);

        if (player.playerNetServerHandler != null && (playerStats.unlockedSchematics.size() != playerStats.lastUnlockedSchematics.size() || (player.ticksExisted - 1) % 100 == 0))
        {
            Integer[] iArray = new Integer[playerStats.unlockedSchematics.size()];

            for (int i = 0; i < iArray.length; i++)
            {
                ISchematicPage page = playerStats.unlockedSchematics.get(i);
                iArray[i] = page == null ? -2 : page.getPageID();
            }

            List<Object> objList = new ArrayList<Object>();
            objList.add(iArray);

            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, player.worldObj.provider.getDimensionId(), objList), player);
        }
    }

    public static class ThermalArmorEvent extends Event
    {
        public ArmorAddResult armorResult = ArmorAddResult.NOTHING;
        public final int armorIndex;
        public final ItemStack armorStack;

        public ThermalArmorEvent(int armorIndex, ItemStack armorStack)
        {
            this.armorIndex = armorIndex;
            this.armorStack = armorStack;
        }

        public void setArmorAddResult(ArmorAddResult result)
        {
            this.armorResult = result;
        }

        public enum ArmorAddResult
        {
            ADD,
            REMOVE,
            NOTHING
        }
    }


    protected void sendPlanetList(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        HashMap<String, Integer> map;
        if (player.ticksExisted % 50 == 0)
        //Check for genuine update - e.g. maybe some other player created a space station or changed permissions
        //CAUTION: possible server load due to dimension loading, if any planets or moons were (contrary to GC default) set to hotload
        {
            map = WorldUtil.getArrayOfPossibleDimensions(playerStats.spaceshipTier, player);
        }
        else
        {
            map = WorldUtil.getArrayOfPossibleDimensionsAgain(playerStats.spaceshipTier, player);
        }

        String temp = "";
        int count = 0;

        for (Entry<String, Integer> entry : map.entrySet())
        {
            temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        if (!temp.equals(playerStats.savedPlanetList) || (player.ticksExisted % 100 == 0))
        {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, player.worldObj.provider.getDimensionId(), new Object[] { player.getGameProfile().getName(), temp }), player);
            playerStats.savedPlanetList = temp;
            //GCLog.debug("Sending to " + player.getGameProfile().getName() + ": " + temp);
        }
    }

    protected static void sendAirRemainingPacket(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        final float f1 = playerStats.tankInSlot1 == null ? 0.0F : playerStats.tankInSlot1.getMaxDamage() / 90.0F;
        final float f2 = playerStats.tankInSlot2 == null ? 0.0F : playerStats.tankInSlot2.getMaxDamage() / 90.0F;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, player.worldObj.provider.getDimensionId(), new Object[] { MathHelper.floor_float(playerStats.airRemaining / f1), MathHelper.floor_float(playerStats.airRemaining2 / f2), player.getGameProfile().getName() }), player);
    }

    protected void sendThermalLevelPacket(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_THERMAL_LEVEL, player.worldObj.provider.getDimensionId(), new Object[] { playerStats.thermalLevel, playerStats.thermalLevelNormalising }), player);
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType)
    {
        GCPlayerHandler.sendGearUpdatePacket(player, packetType, gearType, -1);
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType, int gearID)
    {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, player.getGameProfile().getName()) != null)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, player.worldObj.provider.getDimensionId(), new Object[] { player.getGameProfile().getName(), packetType.ordinal(), gearType.ordinal(), gearID }), new TargetPoint(player.worldObj.provider.getDimensionId(), player.posX, player.posY, player.posZ, 50.0D));
        }
    }

    public enum EnumModelPacketType
    {
        ADD,
        REMOVE
    }

    public void onPlayerUpdate(EntityPlayerMP player)
    {
        int tick = player.ticksExisted - 1;

        //This will speed things up a little
        final GCPlayerStats GCPlayer = GCPlayerStats.get(player);

        if ((ConfigManagerCore.challengeMode || ConfigManagerCore.challengeSpawnHandling) && GCPlayer.unlockedSchematics.size() == 0)
        {
            if (GCPlayer.startDimension.length() > 0)
            {
                GCPlayer.startDimension = "";
            }
            else
            {
                //PlayerAPI is installed
                WorldServer worldOld = (WorldServer) player.worldObj;
                try
                {
                    worldOld.getPlayerManager().removePlayer(player);
                }
                catch (Exception e)
                {
                }
                worldOld.playerEntities.remove(player);
                worldOld.updateAllPlayersSleepingFlag();
                worldOld.loadedEntityList.remove(player);
                worldOld.onEntityRemoved(player);
                if (player.addedToChunk && worldOld.getChunkProvider().chunkExists(player.chunkCoordX, player.chunkCoordZ))
                {
                    Chunk chunkOld = worldOld.getChunkFromChunkCoords(player.chunkCoordX, player.chunkCoordZ);
                    chunkOld.removeEntity(player);
                    chunkOld.setChunkModified();
                }

                WorldServer worldNew = WorldUtil.getStartWorld(worldOld);
                int dimID = worldNew.provider.getDimensionId();
                player.dimension = dimID;
                GCLog.debug("DEBUG: Sending respawn packet to player for dim " + dimID);
                player.playerNetServerHandler.sendPacket(new S07PacketRespawn(dimID, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));

                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, player.worldObj.provider.getDimensionId(), new Object[] {}), player);
                }
                worldNew.spawnEntityInWorld(player);
                player.setWorld(worldNew);
            }

            //This is a mini version of the code at WorldUtil.teleportEntity
            final ITeleportType type = GalacticraftRegistry.getTeleportTypeForDimension(player.worldObj.provider.getClass());
            Vector3 spawnPos = type.getPlayerSpawnLocation((WorldServer) player.worldObj, player);
            ChunkCoordIntPair pair = player.worldObj.getChunkFromChunkCoords(spawnPos.intX(), spawnPos.intZ()).getChunkCoordIntPair();
            GCLog.debug("Loading first chunk in new dimension.");
            ((WorldServer) player.worldObj).theChunkProviderServer.loadChunk(pair.chunkXPos, pair.chunkZPos);
            player.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, player.rotationYaw, player.rotationPitch);
            type.setupAdventureSpawn(player);
            type.onSpaceDimensionChanged(player.worldObj, player, false);
            player.setSpawnChunk(new BlockPos(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ()), true, player.worldObj.provider.getDimensionId());
            GCPlayer.newAdventureSpawn = true;
        }
        final boolean isInGCDimension = player.worldObj.provider instanceof IGalacticraftWorldProvider;

        if (tick >= 25)
        {
            if (ConfigManagerCore.enableSpaceRaceManagerPopup && !GCPlayer.openedSpaceRaceManager)
            {
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(player.getGameProfile().getName());

                if (race == null || race.teamName.equals(SpaceRace.DEFAULT_NAME))
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, player.worldObj.provider.getDimensionId(), new Object[] {}), player);
                }

                GCPlayer.openedSpaceRaceManager = true;
            }
            if (!GCPlayer.sentFlags)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, player.worldObj.provider.getDimensionId(), new Object[] { GCPlayer.buildFlags }), player);
                GCPlayer.sentFlags = true;
            }
        }

        if (GCPlayer.cryogenicChamberCooldown > 0)
        {
            GCPlayer.cryogenicChamberCooldown--;
        }

        if (!player.onGround && GCPlayer.lastOnGround)
        {
            GCPlayer.touchedGround = true;
        }

        if (GCPlayer.teleportCooldown > 0)
        {
            GCPlayer.teleportCooldown--;
        }

        if (GCPlayer.chatCooldown > 0)
        {
            GCPlayer.chatCooldown--;
        }

        if (GCPlayer.openPlanetSelectionGuiCooldown > 0)
        {
            GCPlayer.openPlanetSelectionGuiCooldown--;

            if (GCPlayer.openPlanetSelectionGuiCooldown == 1 && !GCPlayer.hasOpenedPlanetSelectionGui)
            {
                WorldUtil.toCelestialSelection(player, GCPlayer, GCPlayer.spaceshipTier);
                GCPlayer.hasOpenedPlanetSelectionGui = true;
            }
        }

        if (GCPlayer.usingParachute)
        {
            if (GCPlayer.lastParachuteInSlot != null)
            {
                player.fallDistance = 0.0F;
            }
            if (player.onGround)
            {
                GCPlayerHandler.setUsingParachute(player, GCPlayer, false);
            }
        }

        this.checkCurrentItem(player);

        if (GCPlayer.usingPlanetSelectionGui)
        {
            //This sends the planets list again periodically (forcing the Celestial Selection screen to open) in case of server/client lag
            //#PACKETSPAM
            this.sendPlanetList(player, GCPlayer);
        }

		/*		if (isInGCDimension || player.usingPlanetSelectionGui)
                {
					player.playerNetServerHandler.ticksForFloatKick = 0;
				}	
		*/
        if (GCPlayer.damageCounter > 0)
        {
            GCPlayer.damageCounter--;
        }

        if (isInGCDimension)
        {
            if (tick % 30 == 0)
            {
                GCPlayerHandler.sendAirRemainingPacket(player, GCPlayer);
                this.sendThermalLevelPacket(player, GCPlayer);
            }

            if (player.ridingEntity instanceof EntityLanderBase)
            {
                GCPlayer.inLander = true;
                GCPlayer.justLanded = false;
            }
            else
            {
                if (GCPlayer.inLander)
                {
                    GCPlayer.justLanded = true;
                }
                GCPlayer.inLander = false;
            }

            if (player.onGround && GCPlayer.justLanded)
            {
                GCPlayer.justLanded = false;

                //Set spawn point here if just descended from a lander for the first time
                if (player.getBedLocation(player.worldObj.provider.getDimensionId()) == null || GCPlayer.newAdventureSpawn)
                {
                    int i = 30000000;
                    int j = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posX + 0.5D)));
                    int k = Math.min(256, Math.max(0, MathHelper.floor_double(player.posY + 1.5D)));
                    int l = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posZ + 0.5D)));
                    BlockPos coords = new BlockPos(j, k, l);
                    player.setSpawnChunk(coords, true, player.worldObj.provider.getDimensionId());
                    GCPlayer.newAdventureSpawn = false;
                }

                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, player.worldObj.provider.getDimensionId(), new Object[] {}), player);
            }

            if (player.worldObj.provider instanceof WorldProviderSpaceStation)
            {
            	this.preventFlyingKicks(player);
                if (GCPlayer.newInOrbit)
                {
                    ((WorldProviderSpaceStation) player.worldObj.provider).getSpinManager().sendPackets(player);
                    GCPlayer.newInOrbit = false;
                }
            }
            else
            {
                GCPlayer.newInOrbit = true;

                if (GalacticraftCore.isPlanetsLoaded && player.worldObj.provider instanceof WorldProviderAsteroids)
                {
                	this.preventFlyingKicks(player);
                }
            }
        }
        else
        {
            GCPlayer.newInOrbit = true;
        }

        checkGear(player, GCPlayer, false);

        if (GCPlayer.chestSpawnCooldown > 0)
        {
            GCPlayer.chestSpawnCooldown--;

            if (GCPlayer.chestSpawnCooldown == 180)
            {
                if (GCPlayer.chestSpawnVector != null)
                {
                    EntityParachest chest = new EntityParachest(player.worldObj, GCPlayer.rocketStacks, GCPlayer.fuelLevel);

                    chest.setPosition(GCPlayer.chestSpawnVector.x, GCPlayer.chestSpawnVector.y, GCPlayer.chestSpawnVector.z);
                    chest.color = GCPlayer.parachuteInSlot == null ? EnumDyeColor.WHITE : ItemParaChute.getDyeEnumFromParachuteDamage(GCPlayer.parachuteInSlot.getItemDamage());

                    if (!player.worldObj.isRemote)
                    {
                        player.worldObj.spawnEntityInWorld(chest);
                    }
                }
            }
        }

        //

        if (GCPlayer.launchAttempts > 0 && player.ridingEntity == null)
        {
            GCPlayer.launchAttempts = 0;
        }

        this.checkThermalStatus(player, GCPlayer);
        this.checkOxygen(player, GCPlayer);
        this.checkShield(player, GCPlayer);

        if (isInGCDimension && (GCPlayer.oxygenSetupValid != GCPlayer.lastOxygenSetupValid || tick % 100 == 0))
        {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, player.worldObj.provider.getDimensionId(), new Object[] { GCPlayer.oxygenSetupValid }), player);
        }

        this.throwMeteors(player);

        this.updateSchematics(player, GCPlayer);

        if (tick % 250 == 0 && GCPlayer.frequencyModuleInSlot == null && !GCPlayer.receivedSoundWarning && isInGCDimension && player.onGround && tick > 0 && ((IGalacticraftWorldProvider) player.worldObj.provider).getSoundVolReductionAmount() > 1.0F)
        {
            String[] string2 = GCCoreUtil.translate("gui.frequencymodule.warning1").split(" ");
            StringBuilder sb = new StringBuilder();
            for (String aString2 : string2)
            {
                sb.append(" ").append(EnumColor.YELLOW).append(aString2);
            }
            player.addChatMessage(new ChatComponentText(EnumColor.YELLOW + GCCoreUtil.translate("gui.frequencymodule.warning0") + " " + EnumColor.AQUA + GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + sb.toString()));
            GCPlayer.receivedSoundWarning = true;
        }

        GCPlayer.lastOxygenSetupValid = GCPlayer.oxygenSetupValid;
        GCPlayer.lastUnlockedSchematics = GCPlayer.unlockedSchematics;
        GCPlayer.lastOnGround = player.onGround;
    }
    
    public void preventFlyingKicks(EntityPlayerMP player)
    {
        player.fallDistance = 0.0F;
        player.playerNetServerHandler.floatingTickCount = 0;
    }
}
