package micdoodle8.mods.galacticraft.planets.asteroids.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AsteroidEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<EntityType<EntityAstroMiner>> ASTRO_MINER = register(AsteroidEntityNames.ASTRO_MINER, AsteroidEntities::astroMiner);
    public static final RegistryObject<EntityType<EntityEntryPod>> ENTRY_POD = register(AsteroidEntityNames.ENTRY_POD, AsteroidEntities::entryPod);
    public static final RegistryObject<EntityType<EntityGrapple>> GRAPPLE = register(AsteroidEntityNames.GRAPPLE, AsteroidEntities::grapple);
    public static final RegistryObject<EntityType<EntitySmallAsteroid>> SMALL_ASTEROID = register(AsteroidEntityNames.SMALL_ASTEROID, AsteroidEntities::smallAsteroid);
    public static final RegistryObject<EntityType<EntityTier3Rocket>> ROCKET_T3 = register(AsteroidEntityNames.ROCKET_T3, AsteroidEntities::rocketT3);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<EntityAstroMiner> astroMiner() {
        return EntityType.Builder.create(EntityAstroMiner::new, EntityClassification.MISC)
                .size(2.6F, 1.8F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEntryPod> entryPod() {
        return EntityType.Builder.create(EntityEntryPod::new, EntityClassification.MISC)
                .size(2.6F, 1.8F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityGrapple> grapple() {
        return EntityType.Builder.create(EntityGrapple::new, EntityClassification.MISC)
                .size(0.75F, 0.75F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntitySmallAsteroid> smallAsteroid() {
        return EntityType.Builder.create(EntitySmallAsteroid::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityTier3Rocket> rocketT3() {
        return EntityType.Builder.create(EntityTier3Rocket::new, EntityClassification.MISC)
                .size(1.8F, 6.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }
}
