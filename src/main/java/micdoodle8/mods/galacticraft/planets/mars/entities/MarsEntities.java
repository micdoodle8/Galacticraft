package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MarsEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<EntityType<EntityCargoRocket>> CARGO_ROCKET = register(MarsEntityNames.CARGO_ROCKET, MarsEntities::cargoRocket);
    public static final RegistryObject<EntityType<EntityCreeperBoss>> CREEPER_BOSS = register(MarsEntityNames.CREEPER_BOSS, MarsEntities::creeperBoss);
    public static final RegistryObject<EntityType<EntityLandingBalloons>> LANDING_BALLOONS = register(MarsEntityNames.LANDING_BALLOONS, MarsEntities::landingBalloons);
    public static final RegistryObject<EntityType<EntityProjectileTNT>> PROJECTILE_TNT = register(MarsEntityNames.PROJECTILE_TNT, MarsEntities::projectileTNT);
    public static final RegistryObject<EntityType<EntitySlimeling>> SLIMELING = register(MarsEntityNames.SLIMELING, MarsEntities::slimeling);
    public static final RegistryObject<EntityType<EntitySludgeling>> SLUDGELING = register(MarsEntityNames.SLUDGELING, MarsEntities::sludgeling);
    public static final RegistryObject<EntityType<EntityTier2Rocket>> ROCKET_T2 = register(MarsEntityNames.ROCKET_T2, MarsEntities::tier2Rocket);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<EntityCargoRocket> cargoRocket() {
        return EntityType.Builder.create(EntityCargoRocket::new, EntityClassification.MISC)
                .size(0.98F, 2F)
                .immuneToFire()
                .setUpdateInterval(1)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityCreeperBoss> creeperBoss() {
        return EntityType.Builder.create(EntityCreeperBoss::new, EntityClassification.MONSTER)
                .size(2.0F, 7.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityLandingBalloons> landingBalloons() {
        return EntityType.Builder.create(EntityLandingBalloons::new, EntityClassification.MISC)
                .size(2.0F, 2.0F)
                .immuneToFire()
                .setUpdateInterval(5)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityProjectileTNT> projectileTNT() {
        return EntityType.Builder.create(EntityProjectileTNT::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .immuneToFire()
                .setUpdateInterval(1)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntitySlimeling> slimeling() {
        return EntityType.Builder.create(EntitySlimeling::new, EntityClassification.CREATURE)
                .size(0.45F, 0.7F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntitySludgeling> sludgeling() {
        return EntityType.Builder.create(EntitySludgeling::new, EntityClassification.MONSTER)
                .size(0.3F, 0.2F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityTier2Rocket> tier2Rocket() {
        return EntityType.Builder.create(EntityTier2Rocket::new, EntityClassification.MISC)
                .size(1.2F, 4.5F)
                .immuneToFire()
                .setUpdateInterval(1)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }
}
