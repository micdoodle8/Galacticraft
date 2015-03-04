package micdoodle8.mods.galacticraft.core.entities.player;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IAtmosphericGas;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.EntityParachest;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTelemetry;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.WorldProviderAsteroids;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class GCPlayerHandler
{
    private static final int OXYGENHEIGHTLIMIT = 450;
    private boolean isClient = FMLCommonHandler.instance().getEffectiveSide().isClient();
	private ConcurrentHashMap<UUID, GCPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, GCPlayerStats>();
	private Field ftc;

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
        if (event.entity instanceof EntityClientPlayerMP && GCPlayerStatsClient.get((EntityClientPlayerMP) event.entity) == null)
        {
            GCPlayerStatsClient.register((EntityClientPlayerMP) event.entity);
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

        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, new Object[] { }), player);
        int repeatCount = stats.buildFlags >> 9;
        if (repeatCount < 3)
        	stats.buildFlags &= 1536;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, new Object[] { stats.buildFlags }), player);
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

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            this.onPlayerUpdate((EntityPlayerMP) event.entityLiving);
        }
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
        //

        if (GCPlayer.frequencyModuleInSlot != GCPlayer.lastFrequencyModuleInSlot || forceSend)
        {
            if (FMLCommonHandler.instance().getMinecraftServerInstance() != null)
            {
	        	if (GCPlayer.frequencyModuleInSlot == null)
	            {
	                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_FREQUENCY_MODULE);
	                TileEntityTelemetry.frequencyModulePlayer(GCPlayer.lastFrequencyModuleInSlot, null);
	            }
	            else if (GCPlayer.frequencyModuleInSlot.getItem() == GCItems.basicItem && GCPlayer.frequencyModuleInSlot.getItemDamage() == 19 && GCPlayer.lastFrequencyModuleInSlot == null)
	            {
	                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_FREQUENCY_MODULE);
	                TileEntityTelemetry.frequencyModulePlayer(GCPlayer.frequencyModuleInSlot, player);
	            }
            }

            GCPlayer.lastFrequencyModuleInSlot = GCPlayer.frequencyModuleInSlot;
        }

        //

        if (GCPlayer.maskInSlot != GCPlayer.lastMaskInSlot || forceSend)
        {
            if (GCPlayer.maskInSlot == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVEMASK);
            }
            else if (GCPlayer.maskInSlot.getItem() == GCItems.oxMask && (GCPlayer.lastMaskInSlot == null || forceSend))
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDMASK);
            }

            GCPlayer.lastMaskInSlot = GCPlayer.maskInSlot;
        }

        //

        if (GCPlayer.gearInSlot != GCPlayer.lastGearInSlot || forceSend)
        {
            if (GCPlayer.gearInSlot == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVEGEAR);
            }
            else if (GCPlayer.gearInSlot.getItem() == GCItems.oxygenGear && (GCPlayer.lastGearInSlot == null || forceSend))
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDGEAR);
            }

            GCPlayer.lastGearInSlot = GCPlayer.gearInSlot;
        }

        //

        if (GCPlayer.tankInSlot1 != GCPlayer.lastTankInSlot1 || forceSend)
        {
            if (GCPlayer.tankInSlot1 == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_LEFT_TANK);
            }
            else if (GCPlayer.lastTankInSlot1 == null || forceSend)
            {
                if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankLight)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTGREENTANK);
                }
                else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankMedium)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTORANGETANK);
                }
                else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankHeavy)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTREDTANK);
                }
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (GCPlayer.tankInSlot1.getItem() != GCPlayer.lastTankInSlot1.getItem())
            {
                if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankLight)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTGREENTANK);
                }
                else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankMedium)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTORANGETANK);
                }
                else if (GCPlayer.tankInSlot1.getItem() == GCItems.oxTankHeavy)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDLEFTREDTANK);
                }
            }

            GCPlayer.lastTankInSlot1 = GCPlayer.tankInSlot1;
        }

        //

        if (GCPlayer.tankInSlot2 != GCPlayer.lastTankInSlot2 || forceSend)
        {
            if (GCPlayer.tankInSlot2 == null)
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_RIGHT_TANK);
            }
            else if (GCPlayer.lastTankInSlot2 == null || forceSend)
            {
                if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankLight)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTGREENTANK);
                }
                else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankMedium)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTORANGETANK);
                }
                else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankHeavy)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTREDTANK);
                }
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (GCPlayer.tankInSlot2.getItem() != GCPlayer.lastTankInSlot2.getItem())
            {
                if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankLight)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTGREENTANK);
                }
                else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankMedium)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTORANGETANK);
                }
                else if (GCPlayer.tankInSlot2.getItem() == GCItems.oxTankHeavy)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADDRIGHTREDTANK);
                }
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
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_PARACHUTE);
                }
            }
            else if (GCPlayer.lastParachuteInSlot == null || forceSend)
            {
                if (GCPlayer.usingParachute)
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_PARACHUTE, GCPlayer.parachuteInSlot.getItemDamage());
                }
            }
            else if (GCPlayer.parachuteInSlot.getItemDamage() != GCPlayer.lastParachuteInSlot.getItemDamage())
            {
                GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_PARACHUTE, GCPlayer.parachuteInSlot.getItemDamage());
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
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_THERMAL_HELMET);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalHelmetInSlot == null || forceSend))
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_THERMAL_HELMET);
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
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_THERMAL_CHESTPLATE);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalChestplateInSlot == null || forceSend))
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_THERMAL_CHESTPLATE);
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
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_THERMAL_LEGGINGS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalLeggingsInSlot == null || forceSend))
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_THERMAL_LEGGINGS);
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
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_THERMAL_BOOTS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (GCPlayer.lastThermalBootsInSlot == null || forceSend))
                {
                    GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_THERMAL_BOOTS);
                }
            }

            GCPlayer.lastThermalBootsInSlot = GCPlayer.thermalBootsInSlot;
        }
    }

    protected void checkThermalStatus(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        ItemStack thermalPaddingHelm = playerStats.extendedInventory.getStackInSlot(6);
        ItemStack thermalPaddingChestplate = playerStats.extendedInventory.getStackInSlot(7);
        ItemStack thermalPaddingLeggings = playerStats.extendedInventory.getStackInSlot(8);
        ItemStack thermalPaddingBoots = playerStats.extendedInventory.getStackInSlot(9);

        if (player.worldObj.provider instanceof IGalacticraftWorldProvider && !player.capabilities.isCreativeMode)
        {
            IGalacticraftWorldProvider provider = (IGalacticraftWorldProvider) player.worldObj.provider;
            float thermalLevelMod = provider.getThermalLevelModifier();

            if (thermalLevelMod != 0)
            {
                int thermalLevelCooldownBase = (int) Math.floor(1 / (thermalLevelMod * (thermalLevelMod > 0 ? 1 : -1)) * 200);
                int thermalLevelTickCooldown = thermalLevelCooldownBase;

                if (GalacticraftCore.isPlanetsLoaded)
                {
                    if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
                    {
                        int last = playerStats.thermalLevel;

                        if (playerStats.thermalLevel < 0)
                        {
                            playerStats.thermalLevel += 1;
                        }
                        else if (playerStats.thermalLevel > 0)
                        {
                            playerStats.thermalLevel -= 1;
                        }

                        if (playerStats.thermalLevel != last)
                        {
                            this.sendThermalLevelPacket(player, playerStats);
                        }

                        // Player is wearing all required thermal padding items
                        return;
                    }

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
                }

                if ((player.ticksExisted - 1) % thermalLevelTickCooldown == 0)
                {
                    int last = playerStats.thermalLevel;
                    playerStats.thermalLevel = (int) Math.min(Math.max(playerStats.thermalLevel + thermalLevelMod, -22), 22);

                    if (playerStats.thermalLevel != last)
                    {
                        this.sendThermalLevelPacket(player, playerStats);
                    }

                    if (Math.abs(playerStats.thermalLevel) >= 22)
                    {
                        player.attackEntityFrom(DamageSourceGC.thermal, 1.5F); // TODO New thermal damage source
                    }
                }

                if (playerStats.thermalLevel < -15)
                {
                    player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 5, 2, true));
                }

                if (playerStats.thermalLevel > 15)
                {
                    player.addPotionEffect(new PotionEffect(Potion.confusion.id, 5, 2, true));

                }
            }
        }
    }

    protected void checkOxygen(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        final ItemStack tankInSlot = playerStats.extendedInventory.getStackInSlot(2);
        final ItemStack tankInSlot2 = playerStats.extendedInventory.getStackInSlot(3);

        final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

        if ((player.dimension == 0 || player.worldObj.provider instanceof IGalacticraftWorldProvider) && (!(player.dimension == 0 || ((IGalacticraftWorldProvider) player.worldObj.provider).hasBreathableAtmosphere()) || player.posY > GCPlayerHandler.OXYGENHEIGHTLIMIT) && !player.capabilities.isCreativeMode && !(player.ridingEntity instanceof EntityLanderBase) && !(player.ridingEntity instanceof EntityAutoRocket))
        {
        	if (tankInSlot == null)
            {
                playerStats.airRemaining = 0;
            }

            if (tankInSlot2 == null)
            {
                playerStats.airRemaining2 = 0;
            }

            if (drainSpacing > 0)
            {
                if ((player.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(player) && !playerStats.usingPlanetSelectionGui)
                {
                    if (tankInSlot != null && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() > 0)
                    {
                        tankInSlot.damageItem(1, player);
                    }

                    if (tankInSlot2 != null && tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage() > 0)
                    {
                        tankInSlot2.damageItem(1, player);
                    }
                }

                if (tankInSlot != null)
                {
                    playerStats.airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
                }

                if (tankInSlot2 != null)
                {
                    playerStats.airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
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
                            playerStats.airRemaining = Math.max(playerStats.airRemaining - 1, 0);
                        }

                        if (playerStats.airRemaining2 > 0)
                        {
                            playerStats.airRemaining2 = Math.max(playerStats.airRemaining2 - 1, 0);
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

            if (!playerStats.oxygenSetupValid && !player.worldObj.isRemote && player.isEntityAlive())
            {
                if (playerStats.damageCounter == 0)
                {
                    playerStats.damageCounter = ConfigManagerCore.suffocationCooldown;

                    GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(player);
                    MinecraftForge.EVENT_BUS.post(suffocationEvent);

                    if (!suffocationEvent.isCanceled())
                    {
                        player.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage);

                        GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(player);
                        MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                    }
                }
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
            if (((IGalacticraftWorldProvider) world.provider).getMeteorFrequency() > 0)
            {
                final int f = (int) (((IGalacticraftWorldProvider) world.provider).getMeteorFrequency() * 1000D);

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
        boolean noAtmosphericCombustion = player.worldObj.provider instanceof IGalacticraftWorldProvider && (!((IGalacticraftWorldProvider) player.worldObj.provider).isGasPresent(IAtmosphericGas.OXYGEN) || ((IGalacticraftWorldProvider) player.worldObj.provider).isGasPresent(IAtmosphericGas.CO2));
        if (noAtmosphericCombustion && theCurrentItem != null)
        {
            final int var1 = theCurrentItem.stackSize;
            final int var2 = theCurrentItem.getItemDamage();

            if (player.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(Blocks.torch))
            {
                final ItemStack stack = new ItemStack(GCBlocks.unlitTorch, var1, 0);
                player.inventory.mainInventory[player.inventory.currentItem] = stack;
            }
        }
        else if (!noAtmosphericCombustion && theCurrentItem != null)
        {
            final int var1 = theCurrentItem.stackSize;
            final int var2 = theCurrentItem.getItemDamage();

            if (player.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(GCBlocks.unlitTorch))
            {
                final ItemStack stack = new ItemStack(Blocks.torch, var1, 0);
                player.inventory.mainInventory[player.inventory.currentItem] = stack;
            }
        }
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

            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.ADD_PARACHUTE, subtype);
        }
        else
        {
            GCPlayerHandler.sendGearUpdatePacket(player, EnumModelPacket.REMOVE_PARACHUTE);
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
            if (player.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCBlocks.blockMoon)
            {
                // And is the correct metadata (moon turf)
                if (player.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
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
                        TickHandlerServer.addFootprint(chunkKey, new Footprint(player.worldObj.provider.dimensionId, pos, rotation), player.worldObj.provider.dimensionId);

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

            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), player);
        }
    }

    public static class ThermalArmorEvent extends cpw.mods.fml.common.eventhandler.Event
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
        HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(playerStats.spaceshipTier), player);

        String temp = "";
        int count = 0;

        for (Entry<String, Integer> entry : map.entrySet())
        {
            temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        if (!temp.equals(playerStats.savedPlanetList) || (player.ticksExisted % 5 == 0))
        {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { player.getGameProfile().getName(), temp }), player);
            playerStats.savedPlanetList = new String(temp);
        }
    }

    protected void sendAirRemainingPacket(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        final float f1 = Float.valueOf(playerStats.tankInSlot1 == null ? 0.0F : playerStats.tankInSlot1.getMaxDamage() / 90.0F);
        final float f2 = Float.valueOf(playerStats.tankInSlot2 == null ? 0.0F : playerStats.tankInSlot2.getMaxDamage() / 90.0F);
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, new Object[] { MathHelper.floor_float(playerStats.airRemaining / f1), MathHelper.floor_float(playerStats.airRemaining2 / f2), player.getGameProfile().getName() }), player);
    }

    protected void sendThermalLevelPacket(EntityPlayerMP player, GCPlayerStats playerStats)
    {
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_THERMAL_LEVEL, new Object[] { playerStats.thermalLevel }), player);
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacket gearType)
    {
    	MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
    	if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, player.getGameProfile().getName()) != null)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, new Object[] { player.getGameProfile().getName(), gearType.ordinal(), -1 }), new TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 50.0D));
        }
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacket gearType, int subtype)
    {
    	MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
    	if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, player.getGameProfile().getName()) != null)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, new Object[] { player.getGameProfile().getName(), gearType.ordinal(), subtype }), new TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 50.0D));
        }
    }

    public static enum EnumModelPacket
    {
        ADDMASK,
        REMOVEMASK,
        ADDGEAR,
        REMOVEGEAR,
        ADDLEFTREDTANK,
        ADDLEFTORANGETANK,
        ADDLEFTGREENTANK,
        REMOVE_LEFT_TANK,
        ADDRIGHTREDTANK,
        ADDRIGHTORANGETANK,
        ADDRIGHTGREENTANK,
        REMOVE_RIGHT_TANK,
        ADD_PARACHUTE,
        REMOVE_PARACHUTE,
        ADD_FREQUENCY_MODULE,
        REMOVE_FREQUENCY_MODULE,
        ADD_THERMAL_HELMET,
        ADD_THERMAL_CHESTPLATE,
        ADD_THERMAL_LEGGINGS,
        ADD_THERMAL_BOOTS,
        REMOVE_THERMAL_HELMET,
        REMOVE_THERMAL_CHESTPLATE,
        REMOVE_THERMAL_LEGGINGS,
        REMOVE_THERMAL_BOOTS
    }

    private void onPlayerUpdate(EntityPlayerMP player)
    {
    	int tick = player.ticksExisted - 1;

        if (tick == 25)
        {
            if (!GCPlayerStats.get(player).openedSpaceRaceManager)
            {
                SpaceRace race = SpaceRaceManager.getSpaceRaceFromPlayer(player.getGameProfile().getName());

                if (race == null || race.getTeamName().equals(SpaceRace.DEFAULT_NAME))
                {
                    GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_SPACE_RACE_GUI, new Object[] { }), player);
                }

                GCPlayerStats.get(player).openedSpaceRaceManager = true;
            }
        }

        //This will speed things up a little
        final GCPlayerStats GCPlayer = GCPlayerStats.get(player);
        final boolean isInGCDimension = player.worldObj.provider instanceof IGalacticraftWorldProvider;

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
                this.sendAirRemainingPacket(player, GCPlayer);
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
                if (player.getBedLocation(player.worldObj.provider.dimensionId) == null)
                {
                    int i = 30000000;
                    int j = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posX + 0.5D)));
                    int k = Math.min(256, Math.max(0, MathHelper.floor_double(player.posY + 1.5D)));
                    int l = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posZ + 0.5D)));
                    ChunkCoordinates coords = new ChunkCoordinates(j, k, l);
                    player.setSpawnChunk(coords, true, player.worldObj.provider.dimensionId);
                }

                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, new Object[] { }), player);
            }

            if (player.worldObj.provider instanceof WorldProviderOrbit)
            {
                player.fallDistance = 0.0F;
                try {
                	if (ftc == null)
                	{
                		ftc = player.playerNetServerHandler.getClass().getField("floatingTickCount");
            			ftc.setAccessible(true);
                	}
                	//Prevent kicks for flying
					ftc.setInt(player.playerNetServerHandler, 0);
				} catch (Exception e) { }
                if (GCPlayer.newInOrbit)
                {
                	((WorldProviderOrbit) player.worldObj.provider).sendPacketsToClient(player);
                	GCPlayer.newInOrbit = false;
                }
            }
            else
            {
            	GCPlayer.newInOrbit = true;
            	
                if (GalacticraftCore.isPlanetsLoaded && player.worldObj.provider instanceof WorldProviderAsteroids)
                {
                    player.fallDistance = 0.0F;
                    try {
                    	if (ftc == null)
                    	{
                    		ftc = player.playerNetServerHandler.getClass().getField("floatingTickCount");
                			ftc.setAccessible(true);
                    	}
                    	//Prevent kicks for flying
    					ftc.setInt(player.playerNetServerHandler, 0);
    				} catch (Exception e) { }
                }
            }
        }
        else
        	GCPlayer.newInOrbit = true;


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

        if (isInGCDimension && (GCPlayer.oxygenSetupValid != GCPlayer.lastOxygenSetupValid || tick % 100 == 0))
        {
            GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, new Object[] { GCPlayer.oxygenSetupValid }), player);
        }

        this.throwMeteors(player);

        this.updateSchematics(player, GCPlayer);

        if (tick % 250 == 0 && GCPlayer.frequencyModuleInSlot == null && !GCPlayer.receivedSoundWarning && isInGCDimension && player.onGround && tick > 0 && ((IGalacticraftWorldProvider)player.worldObj.provider).getSoundVolReductionAmount() > 1.0F)
        {
            player.addChatMessage(new ChatComponentText(EnumColor.YELLOW + GCCoreUtil.translate("gui.frequencymodule.warning0") + " " + EnumColor.AQUA + GCItems.basicItem.getItemStackDisplayName(new ItemStack(GCItems.basicItem, 1, 19)) + EnumColor.YELLOW + " " + GCCoreUtil.translate("gui.frequencymodule.warning1")));
            GCPlayer.receivedSoundWarning = true;
        }

        GCPlayer.lastOxygenSetupValid = GCPlayer.oxygenSetupValid;
        GCPlayer.lastUnlockedSchematics = GCPlayer.unlockedSchematics;
        GCPlayer.lastOnGround = player.onGround;
    }
}
