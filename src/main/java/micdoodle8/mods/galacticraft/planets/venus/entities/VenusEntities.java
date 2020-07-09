package micdoodle8.mods.galacticraft.planets.venus.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import micdoodle8.mods.galacticraft.planets.mars.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VenusEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, Constants.MOD_ID_PLANETS);

    public static final RegistryObject<EntityType<EntityEntryPodVenus>> ENTRY_POD = register(VenusEntityNames.ENTRY_POD_VENUS, VenusEntities::entryPod);
    public static final RegistryObject<EntityType<EntityJuicer>> JUICER = register(VenusEntityNames.JUICER, VenusEntities::juicer);
    public static final RegistryObject<EntityType<EntitySpiderQueen>> SPIDER_QUEEN = register(VenusEntityNames.SPIDER_QUEEN, VenusEntities::spiderQueen);
    public static final RegistryObject<EntityType<EntityWebShot>> WEB_SHOT = register(VenusEntityNames.WEB_SHOT, VenusEntities::webShot);

    private static <E extends Entity, T extends EntityType<E>> RegistryObject<EntityType<E>> register(final String name, final Supplier<EntityType.Builder<E>> sup)
    {
        return ENTITIES.register(name, () -> sup.get().build(name));
    }

    private static EntityType.Builder<EntityEntryPodVenus> entryPod()
    {
        return EntityType.Builder.create(EntityEntryPodVenus::new, EntityClassification.MISC)
                .size(1.5F, 3.0F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityJuicer> juicer()
    {
        return EntityType.Builder.create(EntityJuicer::new, EntityClassification.MISC)
                .size(0.95F, 0.6F)
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntitySpiderQueen> spiderQueen()
    {
        return EntityType.Builder.create(EntitySpiderQueen::new, EntityClassification.MISC)
                .size(1.4F, 0.9F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }

    private static EntityType.Builder<EntityWebShot> webShot()
    {
        return EntityType.Builder.create(EntityWebShot::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .immuneToFire()
                .setUpdateInterval(10)
                .setTrackingRange(64)
                .setShouldReceiveVelocityUpdates(true);
    }
}
