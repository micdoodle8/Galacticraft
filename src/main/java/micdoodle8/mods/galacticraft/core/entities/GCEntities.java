package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GCEntities
{
//    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_CORE);

    public static final EntityType<EntityEvolvedSpider> EVOLVED_SPIDER = EntityType.Builder.create(EntityEvolvedSpider::new, EntityClassification.MONSTER)
            .size(1.5F, 1.0F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedZombie> EVOLVED_ZOMBIE = EntityType.Builder.create(EntityEvolvedZombie::new, EntityClassification.MONSTER)
            .size(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedCreeper> EVOLVED_CREEPER = EntityType.Builder.create(EntityEvolvedCreeper::new, EntityClassification.MONSTER)
            .size(0.7F, 2.2F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedSkeleton> EVOLVED_SKELETON = EntityType.Builder.create(EntityEvolvedSkeleton::new, EntityClassification.MONSTER)
            .size(0.6F, 1.99F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedEnderman> EVOLVED_ENDERMAN = EntityType.Builder.create(EntityEvolvedEnderman::new, EntityClassification.MONSTER)
            .size(0.6F, 2.9F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityEvolvedWitch> EVOLVED_WITCH = EntityType.Builder.create(EntityEvolvedWitch::new, EntityClassification.MONSTER)
            .size(0.6F, 1.95F)
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntitySkeletonBoss> SKELETON_BOSS = EntityType.Builder.create(EntitySkeletonBoss::new, EntityClassification.MONSTER)
            .size(1.5F, 4.0F)
            .immuneToFire()
            .setUpdateInterval(10)
            .setTrackingRange(64)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    //    public static final EntityType<EntityAlienVillager> ALIEN_VILLAGER = register(GCEntityNames.alienVillager, GCEntities::alienVillager);
    public static final EntityType<EntityTier1Rocket> ROCKET_T1 = EntityType.Builder.create(EntityTier1Rocket::new, EntityClassification.MISC)
            .size(1.2F, 3.5F)
            .setUpdateInterval(1)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteor> METEOR = EntityType.Builder.create(EntityMeteor::new, EntityClassification.MISC)
            .size(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteor> METEOR_HUGE = EntityType.Builder.create(EntityMeteor::new, EntityClassification.MISC)
            .size(6.0F, 6.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityBuggy> BUGGY = EntityType.Builder.create(EntityBuggy::new, EntityClassification.MISC)
            .size(1.4F, 0.6F)
            .immuneToFire()
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityFlag> FLAG = EntityType.Builder.<EntityFlag>create(EntityFlag::new, EntityClassification.MISC)
            .size(0.4F, 3F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityParachest> PARA_CHEST = EntityType.Builder.<EntityParachest>create(EntityParachest::new, EntityClassification.MISC)
            .size(1.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityLander> LANDER = EntityType.Builder.<EntityLander>create(EntityLander::new, EntityClassification.MISC)
            .size(3.0F, 4.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityMeteorChunk> METEOR_CHUNK = EntityType.Builder.<EntityMeteorChunk>create(EntityMeteorChunk::new, EntityClassification.MISC)
            .size(0.25F, 0.25F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityCelestialFake> CELESTIAL_FAKE = EntityType.Builder.<EntityCelestialFake>create(EntityCelestialFake::new, EntityClassification.MISC)
            .size(3.0F, 1.0F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");
    public static final EntityType<EntityHangingSchematic> HANGING_SCHEMATIC = EntityType.Builder.<EntityHangingSchematic>create(EntityHangingSchematic::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .setUpdateInterval(5)
            .setTrackingRange(150)
            .setShouldReceiveVelocityUpdates(true)
            .build("");

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        register(r, EVOLVED_SPIDER, GCEntityNames.evolvedSpider);
        register(r, EVOLVED_ZOMBIE, GCEntityNames.evolvedZombie);
        register(r, EVOLVED_CREEPER, GCEntityNames.evolvedCreeper);
        register(r, EVOLVED_SKELETON, GCEntityNames.evolvedSkeleton);
        register(r, EVOLVED_ENDERMAN, GCEntityNames.evolvedEnderman);
        register(r, EVOLVED_WITCH, GCEntityNames.evolvedWitch);
        register(r, SKELETON_BOSS, GCEntityNames.skeletonBoss);
        //    register(r, ALIEN_VILLAGER, GCEntityNames.alienVillager);
        register(r, ROCKET_T1, GCEntityNames.rocketTier1);
        register(r, METEOR, GCEntityNames.meteor);
        register(r, METEOR_HUGE, GCEntityNames.meteorHuge);
        register(r, BUGGY, GCEntityNames.buggy);
        register(r, FLAG, GCEntityNames.flag);
        register(r, PARA_CHEST, GCEntityNames.parachest);
        register(r, LANDER, GCEntityNames.lander);
        register(r, METEOR_CHUNK, GCEntityNames.meteorChunk);
        register(r, CELESTIAL_FAKE, GCEntityNames.celestialFake);
        register(r, HANGING_SCHEMATIC, GCEntityNames.hangingSchematic);
    }
}
