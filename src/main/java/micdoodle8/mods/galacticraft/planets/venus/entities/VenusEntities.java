package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusEntities
{
//    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_PLANETS);

    public static final EntityType<EntityEntryPodVenus> ENTRY_POD = EntityType.Builder.create(EntityEntryPodVenus::new, EntityClassification.MISC)
            .size(1.5F, 3.0F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityJuicer> JUICER = EntityType.Builder.create(EntityJuicer::new, EntityClassification.MISC)
            .size(0.95F, 0.6F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntitySpiderQueen> SPIDER_QUEEN = EntityType.Builder.create(EntitySpiderQueen::new, EntityClassification.MISC)
            .size(1.4F, 0.9F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityWebShot> WEB_SHOT = EntityType.Builder.create(EntityWebShot::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

//    public static final RegistryObject<EntityType<EntityEntryPodVenus>> ENTRY_POD = register(VenusEntityNames.ENTRY_POD_VENUS, VenusEntities::entryPod);
//    public static final RegistryObject<EntityType<EntityJuicer>> JUICER = register(VenusEntityNames.JUICER, VenusEntities::juicer);
//    public static final RegistryObject<EntityType<EntitySpiderQueen>> SPIDER_QUEEN = register(VenusEntityNames.SPIDER_QUEEN, VenusEntities::spiderQueen);
//    public static final RegistryObject<EntityType<EntityWebShot>> WEB_SHOT = register(VenusEntityNames.WEB_SHOT, VenusEntities::webShot);

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, ENTRY_POD, VenusEntityNames.ENTRY_POD_VENUS);
        register(r, JUICER, VenusEntityNames.JUICER);
        register(r, SPIDER_QUEEN, VenusEntityNames.SPIDER_QUEEN);
        register(r, WEB_SHOT, VenusEntityNames.WEB_SHOT);
    }
}
