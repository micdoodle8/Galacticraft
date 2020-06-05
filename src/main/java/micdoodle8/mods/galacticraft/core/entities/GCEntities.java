package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_CORE);

    public static final RegistryObject<EntityType<EntityEvolvedSpider>> EVOLVED_SPIDER = register(GCEntityNames.evolvedSpider, GCEntities::evolvedSpider);
    public static final RegistryObject<EntityType<EntityEvolvedZombie>> EVOLVED_ZOMBIE = register(GCEntityNames.evolvedZombie, GCEntities::evolvedZombie);
    public static final RegistryObject<EntityType<EntityEvolvedCreeper>> EVOLVED_CREEPER = register(GCEntityNames.evolvedCreeper, GCEntities::evolvedCreeper);
    public static final RegistryObject<EntityType<EntityEvolvedSkeleton>> EVOLVED_SKELETON = register(GCEntityNames.evolvedSkeleton, GCEntities::evolvedSkeleton);
    public static final RegistryObject<EntityType<EntityEvolvedEnderman>> EVOLVED_ENDERMAN = register(GCEntityNames.evolvedEnderman, GCEntities::evolvedEnderman);
    public static final RegistryObject<EntityType<EntityEvolvedWitch>> EVOLVED_WITCH = register(GCEntityNames.evolvedWitch, GCEntities::evolvedWitch);
    public static final RegistryObject<EntityType<EntitySkeletonBoss>> SKELETON_BOSS = register(GCEntityNames.skeletonBoss, GCEntities::skeletonBoss);
//    public static final RegistryObject<EntityType<EntityAlienVillager>> ALIEN_VILLAGER = register(GCEntityNames.alienVillager, GCEntities::alienVillager);
    public static final RegistryObject<EntityType<EntityTier1Rocket>> ROCKET_T1 = register(GCEntityNames.rocketTier1, GCEntities::rocketTier1);
    public static final RegistryObject<EntityType<EntityMeteor>> METEOR = register(GCEntityNames.meteor, GCEntities::meteor);
    public static final RegistryObject<EntityType<EntityMeteor>> METEOR_HUGE = register(GCEntityNames.meteorHuge, GCEntities::meteorHuge);
    public static final RegistryObject<EntityType<EntityBuggy>> BUGGY = register(GCEntityNames.buggy, GCEntities::buggy);
    public static final RegistryObject<EntityType<EntityFlag>> FLAG = register(GCEntityNames.flag, GCEntities::flag);
    public static final RegistryObject<EntityType<EntityParachest>> PARA_CHEST = register(GCEntityNames.parachest, GCEntities::parachest);
    public static final RegistryObject<EntityType<EntityLander>> LANDER = register(GCEntityNames.lander, GCEntities::lander);
    public static final RegistryObject<EntityType<EntityMeteorChunk>> METEOR_CHUNK = register(GCEntityNames.meteorChunk, GCEntities::meteorChunk);
    public static final RegistryObject<EntityType<EntityCelestialFake>> CELESTIAL_FAKE = register(GCEntityNames.celestialFake, GCEntities::celestialFake);
    public static final RegistryObject<EntityType<EntityHangingSchematic>> HANGING_SCHEMATIC = register(GCEntityNames.hangingSchematic, GCEntities::hangingSchematic);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup) {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<EntityEvolvedSpider> evolvedSpider() {
        return EntityType.Builder.create(EntityEvolvedSpider::new, EntityClassification.MONSTER)
                .size(1.5F, 1.0F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEvolvedZombie> evolvedZombie() {
        return EntityType.Builder.create(EntityEvolvedZombie::new, EntityClassification.MONSTER)
                .size(0.6F, 1.95F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEvolvedCreeper> evolvedCreeper() {
        return EntityType.Builder.create(EntityEvolvedCreeper::new, EntityClassification.MONSTER)
                .size(0.7F, 2.2F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEvolvedSkeleton> evolvedSkeleton() {
        return EntityType.Builder.create(EntityEvolvedSkeleton::new, EntityClassification.MONSTER)
                .size(0.6F, 1.99F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEvolvedEnderman> evolvedEnderman() {
        return EntityType.Builder.create(EntityEvolvedEnderman::new, EntityClassification.MONSTER)
                .size(0.6F, 2.9F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityEvolvedWitch> evolvedWitch() {
        return EntityType.Builder.create(EntityEvolvedWitch::new, EntityClassification.MONSTER)
                .size(0.6F, 1.95F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntitySkeletonBoss> skeletonBoss() {
        return EntityType.Builder.create(EntitySkeletonBoss::new, EntityClassification.MONSTER)
                .size(1.5F, 4.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

//    private static EntityType.Builder<EntityAlienVillager> alienVillager() {
//        return EntityType.Builder.create(EntityAlienVillager::new, EntityClassification.MISC)
//                .size(0.6F, 1.8F)
//                .setUpdateInterval(10)
//                .setTrackingRange(64)
//                .setShouldReceiveVelocityUpdates(true);
//    } TODO Villagers

    private static EntityType.Builder<EntityTier1Rocket> rocketTier1() {
        return EntityType.Builder.create(EntityTier1Rocket::new, EntityClassification.MISC)
                .size(1.2F, 3.5F)
                .setUpdateInterval(1)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityMeteor> meteor() {
        return EntityType.Builder.create(EntityMeteor::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityMeteor> meteorHuge() {
        return EntityType.Builder.create(EntityMeteor::new, EntityClassification.MISC)
                .size(6.0F, 6.0F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityBuggy> buggy() {
        return EntityType.Builder.create(EntityBuggy::new, EntityClassification.MISC)
                .size(1.4F, 0.6F)
                .immuneToFire()
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityFlag> flag() {
        return EntityType.Builder.<EntityFlag>create(EntityFlag::new, EntityClassification.MISC)
                .size(0.4F, 3F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityParachest> parachest() {
        return EntityType.Builder.<EntityParachest>create(EntityParachest::new, EntityClassification.MISC)
                .size(1.0F, 1.0F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityLander> lander() {
        return EntityType.Builder.<EntityLander>create(EntityLander::new, EntityClassification.MISC)
                .size(3.0F, 4.25F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityMeteorChunk> meteorChunk() {
        return EntityType.Builder.<EntityMeteorChunk>create(EntityMeteorChunk::new, EntityClassification.MISC)
                .size(0.25F, 0.25F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityCelestialFake> celestialFake() {
        return EntityType.Builder.<EntityCelestialFake>create(EntityCelestialFake::new, EntityClassification.MISC)
                .size(3.0F, 1.0F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityHangingSchematic> hangingSchematic() {
        return EntityType.Builder.<EntityHangingSchematic>create(EntityHangingSchematic::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setUpdateInterval(5)
                .setTrackingRange(150)
                .setShouldReceiveVelocityUpdates(true);
    }
}
