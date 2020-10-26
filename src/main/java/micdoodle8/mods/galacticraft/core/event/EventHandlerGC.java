package micdoodle8.mods.galacticraft.core.event;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.item.IKeyItem;
import micdoodle8.mods.galacticraft.api.item.IKeyable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.FlipPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.Unlock;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCEntities;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.*;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.LocatableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DesertBiome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LifecycleEventProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.ILifecycleEvent;

import java.lang.reflect.Field;
import java.util.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandlerGC
{
    public static Map<Block, Item> bucketList = new HashMap<Block, Item>(4, 1F);
    public static boolean bedActivated;

    @SubscribeEvent
    public void playerJoinWorld(EntityJoinWorldEvent event)
    {
        TickHandlerServer.markWorldNeedsUpdate(GCCoreUtil.getDimensionType(event.getWorld()));
    }

    @SubscribeEvent
    public void onRocketLaunch(EntitySpaceshipBase.RocketLaunchEvent event)
    {
//        if (!event.getEntity().world.isRemote && event.getEntity().world.getDimension().dimensionId == 0)
//        {
//            if (event.rocket.riddenByEntity instanceof EntityPlayerMP)
//            {
//                TickHandlerServer.playersRequestingMapData.add((EntityPlayerMP) event.rocket.riddenByEntity);
//            }
//        }
    }

//    @SubscribeEvent
//    public void onConfigChanged(ConfigChangedEvent event)
//    {
//        if (event.getModID().equals(Constants.MOD_ID_CORE))
//        {
////            ConfigManagerCore.syncConfig.get()(false); TODO Config sync?
//        }
//    }

//    @SubscribeEvent
//    public void onWorldSave(Save event)
//    {
//        ChunkLoadingCallback.save((ServerWorld) event.getWorld());
//    }
//
//    @SubscribeEvent
//    public void onChunkDataLoad(ChunkDataEvent.Load event)
//    {
//        ChunkLoadingCallback.load((ServerWorld) event.getWorld());
//    }
//
//    @SubscribeEvent
//    public void onWorldLoad(Load event)
//    {
//        if (!event.getWorld().isRemote())
//        {
//            ChunkLoadingCallback.load((ServerWorld) event.getWorld());
//        }
//    } TODO Chunkloading

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent event)
    {
        if (event.getSource().damageType.equals(DamageSource.ON_FIRE.damageType))
        {
            if (OxygenUtil.noAtmosphericCombustion(event.getEntityLiving().world.getDimension()))
            {
                if (OxygenUtil.isAABBInBreathableAirBlock(event.getEntityLiving().world, event.getEntityLiving().getBoundingBox()))
                {
                    return;
                }

                if (event.getEntityLiving().world instanceof ServerWorld)
                {
                    ((ServerWorld) event.getEntityLiving().world).spawnParticle(ParticleTypes.SMOKE, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + event.getEntityLiving().getBoundingBox().maxY - event.getEntityLiving().getBoundingBox().minY, event.getEntityLiving().getPosZ(), 50, 0.0, 0.05, 0.0, 0.001);
                }

                event.getEntityLiving().extinguish();
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event)
    {
        if (event.getEntityLiving() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.getRidingEntity() instanceof EntityAutoRocket || player.getRidingEntity() instanceof EntityLanderBase)
            {
                event.setDistance(0.0F);
                event.setCanceled(true);
                return;
            }
        }

        if (event.getEntityLiving().world.getDimension() instanceof IGalacticraftDimension)
        {
            event.setDistance(event.getDistance() * ((IGalacticraftDimension) event.getEntityLiving().world.getDimension()).getFallDamageModifier());
        }
    }

    @SubscribeEvent
    public void blockBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        PlayerEntity p = event.getPlayer();
        if (!p.onGround && p.world.getDimension() instanceof IZeroGDimension && !ConfigManagerCore.hardMode.get() && event.getOriginalSpeed() < 5.0F)
        {
            event.setNewSpeed(event.getOriginalSpeed() * 5.0F);
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickedBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        //Skip events triggered from Thaumcraft Golems and other non-players
        if (event.getPlayer() == null || event.getPlayer().inventory == null || event.getPos() == null || (event.getPos().getX() == 0 && event.getPos().getY() == 0 && event.getPos().getZ() == 0))
        {
            return;
        }

        final World worldObj = event.getPlayer().world;
        if (worldObj == null)
        {
            return;
        }

        final ItemStack heldStack = event.getPlayer().inventory.getCurrentItem();
        final TileEntity tileClicked = worldObj.getTileEntity(event.getPos());

        if (!heldStack.isEmpty())
        {
            if (tileClicked != null && tileClicked instanceof IKeyable)
            {
                event.setCanceled(!((IKeyable) tileClicked).canBreak() && !event.getPlayer().abilities.isCreativeMode);
            }
        }
        else if (tileClicked != null && tileClicked instanceof IKeyable)
        {
            event.setCanceled(!((IKeyable) tileClicked).canBreak() && !event.getPlayer().abilities.isCreativeMode);
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickedBlock(PlayerInteractEvent.RightClickBlock event)
    {
        //Skip events triggered from Thaumcraft Golems and other non-players
        if (event.getPlayer() == null || event.getPlayer().inventory == null || event.getPos() == null || (event.getPos().getX() == 0 && event.getPos().getY() == 0 && event.getPos().getZ() == 0))
        {
            return;
        }

        final World worldObj = event.getPlayer().world;
        if (worldObj == null)
        {
            return;
        }

        final Block idClicked = worldObj.getBlockState(event.getPos()).getBlock();

        if (idClicked instanceof BedBlock && worldObj.dimension instanceof IGalacticraftDimension && !worldObj.isRemote && !((IGalacticraftDimension) worldObj.dimension).hasBreathableAtmosphere())
        {
            if (GalacticraftCore.isPlanetsLoaded)
            {
                GCPlayerStats stats = GCPlayerStats.get(event.getPlayer());
                if (!stats.hasReceivedBedWarning())
                {
                    event.getPlayer().sendMessage(new StringTextComponent(GCCoreUtil.translate("gui.bed_fail.message")));
                    stats.setReceivedBedWarning(true);
                }
            }

//            if (worldObj.dimension instanceof DimensionSpaceStation)
//            {
//                //On space stations simply block the bed activation => no explosion
//                event.setCanceled(true);
//                return;
//            } TODO


            //Optionally prevent beds from exploding - depends on canRespawnHere() in the WorldProvider interacting with this
            EventHandlerGC.bedActivated = true;
            if (worldObj.dimension.canRespawnHere() && !EventHandlerGC.bedActivated)
            {
                EventHandlerGC.bedActivated = true;

                //On planets allow the bed to be used to designate a player spawn point
                DimensionType type = GCCoreUtil.getDimensionType(event.getWorld());
                event.getPlayer().setSpawnPoint(event.getPos(), event.getPlayer().isSpawnForced(type), false, type);
            }
            else
            {
                EventHandlerGC.bedActivated = false;
            }
        }

        final ItemStack heldStack = event.getPlayer().inventory.getCurrentItem();
        final TileEntity tileClicked = worldObj.getTileEntity(event.getPos());

        if (!heldStack.isEmpty())
        {
            if (tileClicked != null && tileClicked instanceof IKeyable)
            {
                if (heldStack.getItem() instanceof IKeyItem)
                {
                    if (((IKeyItem) heldStack.getItem()).getTier(heldStack) == -1 || ((IKeyable) tileClicked).getTierOfKeyRequired() == -1 || ((IKeyItem) heldStack.getItem()).getTier(heldStack) == ((IKeyable) tileClicked).getTierOfKeyRequired())
                    {
                        event.setCanceled(((IKeyable) tileClicked).onValidKeyActivated(event.getPlayer(), heldStack, event.getFace()));
                    }
                    else if (!event.getPlayer().isSneaking())
                    {
                        event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.getPlayer(), event.getFace()));
                    }
                }
                else
                {
                    event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.getPlayer(), event.getFace()));
                }
            }

            if (heldStack.getItem() instanceof FlintAndSteelItem || heldStack.getItem() instanceof FireChargeItem)
            {
                if (!worldObj.isRemote)
                {
                    if (idClicked != Blocks.TNT && OxygenUtil.noAtmosphericCombustion(event.getPlayer().world.getDimension()) && !OxygenUtil.isAABBInBreathableAirBlock(event.getEntityLiving().world, new AxisAlignedBB(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getPos().getX() + 1, event.getPos().getY() + 2, event.getPos().getZ() + 1)))
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
        else if (tileClicked != null && tileClicked instanceof IKeyable)
        {
            event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.getPlayer(), event.getFace()));
        }
    }

    @SubscribeEvent
    public void entityLivingEvent(LivingEvent.LivingUpdateEvent event)
    {
        final LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof ServerPlayerEntity)
        {
            GalacticraftCore.handler.onPlayerUpdate((ServerPlayerEntity) entityLiving);
            if (GalacticraftCore.isPlanetsLoaded)
            {
                AsteroidsModule.playerHandler.onPlayerUpdate((ServerPlayerEntity) entityLiving);
            }
            return;
        }

        if (entityLiving.ticksExisted % ConfigManagerCore.suffocationCooldown.get() == 0)
        {
            if (entityLiving.world.getDimension() instanceof IGalacticraftDimension)
            {
                if (!(entityLiving instanceof PlayerEntity) && (!(entityLiving instanceof IEntityBreathable) || !((IEntityBreathable) entityLiving).canBreath()) && !((IGalacticraftDimension) entityLiving.world.getDimension()).hasBreathableAtmosphere())
                {
                    if (!OxygenUtil.isAABBInBreathableAirBlock(entityLiving))
                    {
                        GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEvent);

                        if (suffocationEvent.isCanceled())
                        {
                            return;
                        }

                        entityLiving.attackEntityFrom(DamageSourceGC.oxygenSuffocation, Math.max(ConfigManagerCore.suffocationDamage.get() / 2, 1));

                        GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void entityUpdateCancelInFreefall(EntityEvent.CanUpdate event)
    {
        if (event.getEntity() instanceof EntityMeteor)
        {
            event.setCanUpdate(true);
            return;
        }

        if (event.getEntity().world.getDimension() instanceof IZeroGDimension)
        {
            if (((IZeroGDimension) event.getEntity().world.getDimension()).inFreefall(event.getEntity()))
            {
                event.setCanUpdate(true);
//                event.entity.move(event.entity.motionX, event.entity.motionY, event.entity.motionZ);
            }
        }
    }

//    private ItemStack fillBucket(World world, RayTraceResult position)
//    {
//        BlockState state = world.getBlockState(new BlockPos(position.getHitVec()));
//        Block block = state.getBlock();
//
//        Item bucket = bucketList.get(block);
//
//        if (bucket != null && block.getMetaFromState(state) == 0)
//        {
//            world.setBlockToAir(position.getBlockPos());
//            return new ItemStack(bucket);
//        }
//
//        return null;
//    } TODO ?

//    @SubscribeEvent
//    public void onBucketFill(FillBucketEvent event)
//    {
//        if (event.getEmptyBucket() == null || !(event.getEmptyBucket().getItem() instanceof BucketItem))
//        {
//            return;
//        }
//        RayTraceResult pos = event.getTarget();
//        if (pos == null)
//        {
//            return;
//        }
//
//        ItemStack ret = fillBucket(event.getWorld(), pos);
//
//        if (ret == null) //Intentional null check - see fillBucket()
//        {
//            return;
//        }
//
//        event.setFilledBucket(ret);
//        event.setResult(Result.ALLOW);
//    } TODO ?

//    @SubscribeEvent
//    public void populate(PopulateChunkEvent.Post event)
//    {
//        final boolean doGen = TerrainGen.populate(event.getGenerator(), event.getWorld(), event.getRand(), event.getChunkX(), event.getChunkZ(), event.isHasVillageGenerated(), PopulateChunkEvent.Populate.EventType.CUSTOM);
//
//        if (!doGen)
//        {
//            return;
//        }
//
//        final int worldX = event.getChunkX() << 4;
//        final int worldZ = event.getChunkZ() << 4;
//
//        EventHandlerGC.generateOil(event.getWorld(), event.getRand(), worldX + 15, worldZ + 15, false);
//    } TODO Oil gen

    public static boolean oilPresent(IWorld world, Random rand, int x, int z, BlockVec3 pos)
    {
        boolean doGen2 = false;

//        for (Integer dim : ConfigManagerCore.externalOilGen.get()) TODO Disable oil gen config
//        {
//            if (dim == GCCoreUtil.getDimensionType(world).getId())
//            {
//                doGen2 = true;
//                break;
//            }
//        }

        if (!doGen2)
        {
            return false;
        }

        final Biome biome = world.getBiome(new BlockPos(x + 8, 0, z + 8));

        if (/*biome == Biomes.SKY || */biome == Biomes.NETHER)
        {
            return false;
        }

        rand.setSeed(world.getSeed());
        long i1 = rand.nextInt() / 2L * 2L + 1L;
        long j1 = rand.nextInt() / 2L * 2L + 1L;
        rand.setSeed(x * i1 + z * j1 ^ world.getSeed());

        double randMod = Math.min(0.2D, 0.05D * ConfigManagerCore.oilGenFactor.get());

        if (biome.getDepth() >= 0.45F)
        {
            randMod /= 2;
        }
        if (biome.getDepth() < -0.5F)
        {
            randMod *= 1.8;
        }
        if (biome instanceof DesertBiome)
        {
            randMod *= 1.8;
        }

        final boolean flag1 = rand.nextDouble() <= randMod;
        final boolean flag2 = rand.nextDouble() <= randMod;

        if (flag1 || flag2)
        {
            pos.y = 17 + rand.nextInt(10) + rand.nextInt(5);
            pos.x = x + 8 - rand.nextInt(16);  //do not change without thinking about chunk loading, see notes in generateOil()
            pos.z = z + 8 - rand.nextInt(16);  //do not change without thinking about chunk loading, see notes in generateOil()
            return true;
        }

        return false;
    }

    /**
     * xx, zz are the central position of 4 chunks: the chunk currently being populated + 1 in the x,z plane
     * We must not stray more than 1 chunk away from this position, that's 16 blocks
     */
    public static void generateOil(IWorld world, Random rand, int xx, int zz, boolean testFirst)
    {
        BlockVec3 pos = new BlockVec3();
        if (oilPresent(world, rand, xx, zz, pos))
        {
            int x = pos.x;
            int cy = pos.y;
            int z = pos.z;
            int r = 3 + rand.nextInt(5);

            //The method loads blocks in the range (x - r - 1) to (x + r + 1) - whatever the randoms, all these positions must be inside the +/-1 chunk range
            //This can be minimum xx - 7 - 7 - 1, that's OK!
            //This can be maximum xx + 8 + 7 + 1, that's also OK!

            if (testFirst && checkOilPresent(world, x, cy, z, r))
            {
                return;
            }

            final int r2 = r * r;

            BlockState crudeOil = GCFluids.OIL.getBlock().getDefaultState();
            for (int bx = -r; bx <= r; bx++)
            {
                for (int by = -r + 2; by <= r - 2; by++)
                {
                    int xySquared = bx * bx + by * by * 3;
                    for (int bz = -r; bz <= r; bz++)
                    {
                        if (xySquared + bz * bz <= r2)
                        {
                            if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x - 1, by + cy, bz + z)))
                            {
                                continue;
                            }
                            if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x + 1, by + cy, bz + z)))
                            {
                                continue;
                            }
                            if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy - 1, bz + z)))
                            {
                                continue;
                            }
                            if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy, bz + z - 1)))
                            {
                                continue;
                            }
                            if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy, bz + z + 1)))
                            {
                                continue;
                            }
                            if (EventHandlerGC.checkBlockAbove(world, new BlockPos(bx + x, by + cy + 1, bz + z)))
                            {
                                continue;
                            }

                            world.setBlockState(new BlockPos(bx + x, by + cy, bz + z), crudeOil, 2);
                        }
                    }
                }
            }
        }
    }

    private static boolean checkOilPresent(IWorld world, int x, int cy, int z, int r)
    {
        final int r2 = r * r;

        for (int bx = -r; bx <= r; bx++)
        {
            for (int by = -r + 2; by <= r - 2; by++)
            {
                int xySquared = bx * bx + by * by * 3;
                for (int bz = -r; bz <= r; bz++)
                {
                    if (xySquared + bz * bz <= r2)
                    {
                        if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x - 1, by + cy, bz + z)))
                        {
                            continue;
                        }
                        if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x + 1, by + cy, bz + z)))
                        {
                            continue;
                        }
                        if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy - 1, bz + z)))
                        {
                            continue;
                        }
                        if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy, bz + z - 1)))
                        {
                            continue;
                        }
                        if (EventHandlerGC.checkBlock(world, new BlockPos(bx + x, by + cy, bz + z + 1)))
                        {
                            continue;
                        }
                        if (EventHandlerGC.checkBlockAbove(world, new BlockPos(bx + x, by + cy + 1, bz + z)))
                        {
                            continue;
                        }

                        if (world.getBlockState(new BlockPos(bx + x, by + cy, bz + z)).getBlock() == GCFluids.OIL.getBlock())
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static void retrogenOil(IWorld world, IChunk chunk)
    {
        int cx = chunk.getPos().x;
        int cz = chunk.getPos().z;

        generateOil(world, new Random(), cx << 4, cz << 4, true);
    }

    private static boolean checkBlock(IWorld w, BlockPos pos)
    {
        BlockState state = w.getBlockState(pos);
        Block b = state.getBlock();
        if (b.getMaterial(state) == Material.AIR)
        {
            return true;
        }
        return /*b instanceof FlowingFluidBlock && */b != GCFluids.OIL.getBlock();
    }

    private static boolean checkBlockAbove(IWorld w, BlockPos pos)
    {
        Block b = w.getBlockState(pos).getBlock();
        if (b instanceof SandBlock)
        {
            return true;
        }
        return b instanceof GravelBlock;
    }

    @SubscribeEvent
    public void schematicUnlocked(Unlock event)
    {
        GCPlayerStats stats = GCPlayerStats.get(event.player);

        if (!stats.getUnlockedSchematics().contains(event.page))
        {
            stats.getUnlockedSchematics().add(event.page);
            Collections.sort(stats.getUnlockedSchematics());

            if (event.player != null && event.player.connection != null)
            {
                Integer[] iArray = new Integer[stats.getUnlockedSchematics().size()];

                for (int i = 0; i < iArray.length; i++)
                {
                    ISchematicPage page = stats.getUnlockedSchematics().get(i);
                    iArray[i] = page == null ? -2 : page.getPageID();
                }

                List<Object> objList = new ArrayList<>();
                objList.add(iArray);

                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, GCCoreUtil.getDimensionType(event.player.world), objList), event.player);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void schematicFlipEvent(FlipPage event)
    {
        ISchematicPage page = null;

        switch (event.direction)
        {
        case 1:
            page = EventHandlerGC.getNextSchematic(event.index);
            break;
        case -1:
            page = EventHandlerGC.getLastSchematic(event.index);
            break;
        }

        if (page != null)
        {
            Screen cs = event.currentGui;
//            int benchX = (int) Minecraft.getInstance().player.getPosX();
//            int benchY = (int) Minecraft.getInstance().player.getPosY();
//            int benchZ = (int) Minecraft.getInstance().player.getPosZ();
//            if (cs instanceof GuiPositionedContainer)
//            {
//                benchX = ((GuiPositionedContainer)cs).getX();
//                benchY = ((GuiPositionedContainer)cs).getY();
//                benchZ = ((GuiPositionedContainer)cs).getZ();
//            }
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_SCHEMATIC_PAGE, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[]{page.getPageID()}));
//            Minecraft.getInstance().player.openGui(GalacticraftCore.instance, page.getGuiID(), Minecraft.getInstance().player.world, benchX, benchY, benchZ); TODO Gui
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static ISchematicPage getNextSchematic(int currentIndex)
    {
        ClientPlayerEntity player = PlayerUtil.getPlayerBaseClientFromPlayer(Minecraft.getInstance().player, false);
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        final int size = stats.getUnlockedSchematics().size();
        final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>(size, 1F);
        for (int i = 0; i < size; i++)
        {
            idList.put(i, stats.getUnlockedSchematics().get(i).getPageID());
        }

        final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
        final Iterator<Integer> iterator = keys.iterator();

        for (int count = 0; count < keys.size(); count++)
        {
            final int i = iterator.next();
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

            if (page.getPageID() == currentIndex)
            {
                if (count + 1 < stats.getUnlockedSchematics().size())
                {
                    return stats.getUnlockedSchematics().get(count + 1);
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    private static ISchematicPage getLastSchematic(int currentIndex)
    {
        ClientPlayerEntity player = PlayerUtil.getPlayerBaseClientFromPlayer(Minecraft.getInstance().player, false);
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        final int size = stats.getUnlockedSchematics().size();
        final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>(size, 1F);
        for (int i = 0; i < size; i++)
        {
            idList.put(i, stats.getUnlockedSchematics().get(i).getPageID());
        }

        final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
        final Iterator<Integer> iterator = keys.iterator();

        for (int count = 0; count < keys.size(); count++)
        {
            final int i = iterator.next();
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

            if (page.getPageID() == currentIndex)
            {
                if (count - 1 >= 0)
                {
                    return stats.getUnlockedSchematics().get(count - 1);
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

//    @SubscribeEvent
//    public void onPlayerDeath(PlayerDropsEvent event)
//    {
//        if (event.getEntityLiving() instanceof ServerPlayerEntity)
//        {
//            GCPlayerStats stats = GCPlayerStats.get(event.getPlayer());
//            if (!event.getPlayer().world.getGameRules().getBoolean("keepInventory"))
//            {
//                event.getPlayer().captureDrops = true;
//                for (int i = stats.getExtendedInventory().getSizeInventory() - 1; i >= 0; i--)
//                {
//                    ItemStack stack = stats.getExtendedInventory().getStackInSlot(i);
//
//                    if (!stack.isEmpty())
//                    {
//                        event.getPlayer().dropItem(stack, true, false);
//                        stats.getExtendedInventory().setInventorySlotContents(i, ItemStack.EMPTY);
//                    }
//                }
//                event.getEntityLiving().captureDrops = false;
//            }
//        }
//    } TODO Drop extended inv

    // @OnlyIn(Dist.CLIENT)
    // @SubscribeEvent
    // public void onMinecraftLoaded(MinecraftLoadedEvent event)
    // {
    // ;
    // }

    //	@OnlyIn(Dist.CLIENT)
    //	@SubscribeEvent
    //	public void onSoundLoad(SoundLoadEvent event)
    //	{
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/scaryscape.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip1.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip2.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip3.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip4.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip5.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip6.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip7.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip8.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bossdeath.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bosslaugh.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bossliving.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/slime_death.ogg");
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/mars_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/mimas_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/orbit_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/scary_ambience.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/spacerace_JC.ogg"));
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/closeairlock.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/openairlock.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/parachute.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/unlockchest.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "shuttle/shuttle.ogg");
    //	}
    //
    //	@OnlyIn(Dist.CLIENT)
    //	private SoundPoolEntry func_110654_c(SoundPool pool, String par1Str)
    //	{
    //		try
    //		{
    //			ResourceLocation resourcelocation = new ResourceLocation(par1Str);
    //			String s1 = String.format("%s:%s:%s/%s", new Object[] { "mcsounddomain", resourcelocation.getResourceDomain(), "sound", resourcelocation.getResourcePath() });
    //			SoundPoolProtocolHandler soundpoolprotocolhandler = new SoundPoolProtocolHandler(pool);
    //			return new SoundPoolEntry(par1Str, new URL((URL) null, s1, soundpoolprotocolhandler));
    //		}
    //		catch (MalformedURLException e)
    //		{
    //			e.printStackTrace();
    //		}
    //
    //		return null;
    //	} TODO Fix sounds

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onLeaveBedButtonClicked(SleepCancelledEvent event)
    {
        PlayerEntity player = Minecraft.getInstance().player;

        BlockPos c = player.getBedLocation(player.world.dimension.getType());

        if (c != null)
        {
            EventWakePlayer event0 = new EventWakePlayer(player, c, true, true, false, true);
            MinecraftForge.EVENT_BUS.post(event0);
//            player.wakeUpPlayer(true, true, false);
            player.wakeUp();

//            if (player.world.isRemote && GalacticraftCore.isPlanetsLoaded)
//            {
//                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(PacketSimpleMars.EnumSimplePacketMars.S_WAKE_PLAYER, GCCoreUtil.getDimensionID(player.world), new Object[] {}));
//            } TODO Cryo chamber
        }
    }

    @SubscribeEvent
    public void onZombieSummonAid(ZombieEvent.SummonAidEvent event)
    {
        if (event.getEntity() instanceof EntityEvolvedZombie)
        {
            event.setCustomSummonedAid(new EntityEvolvedZombie(GCEntities.EVOLVED_ZOMBIE, event.getWorld()));

            if (((LivingEntity) event.getEntity()).getRNG().nextFloat() < ((EntityEvolvedZombie) event.getEntity()).getAttribute(((EntityEvolvedZombie) event.getEntity()).getReinforcementsAttribute()).getValue())
            {
                event.setResult(Event.Result.ALLOW);
            }
            else
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void overrideSkyColor(EntityViewRenderEvent.FogColors event)
    {
        ClientPlayerEntity entity = Minecraft.getInstance().player;
        ClientWorld worldclient = Minecraft.getInstance().world;
        //Disable any night vision effects on the sky, if the planet has no atmosphere
        if (entity != null && entity.isPotionActive(Effects.NIGHT_VISION))
        {
            if (worldclient.dimension instanceof IGalacticraftDimension && ((IGalacticraftDimension) worldclient.dimension).hasNoAtmosphere() && !((IGalacticraftDimension) worldclient.dimension).hasBreathableAtmosphere())
            {
                Vec3d vec = worldclient.getFogColor(1.0F);
                event.setRed((float) vec.x);
                event.setGreen((float) vec.y);
                event.setBlue((float) vec.z);
            }
//            else if (worldclient.dimension.getSkyRenderer() instanceof SkyProviderOverworld && entity.getPosY() > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
//            {
//                Vec3d vec = TransformerHooks.getFogColorHook(entity.world);
//                event.setRed((float) vec.x);
//                event.setGreen((float) vec.y);
//                event.setBlue((float) vec.z);
//            } TODO Sky rendering
        }
    }

    private final List<SoundPlayEntry> soundPlayList = new ArrayList<SoundPlayEntry>();

    private static Field volumeField;
    private static Field pitchField;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onSoundPlayed(PlaySoundEvent event)
    {
        //The event.result starts off equal to event.sound, but could have been altered or set to null by another mod
        if (event.getResultSound() == null)
        {
            return;
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player != null && player.world != null && player.world.getDimension() instanceof IGalacticraftDimension)
        {
            //Only modify standard game sounds, not music
            if (event.getResultSound().getAttenuationType() != ISound.AttenuationType.NONE)
            {
                PlayerGearData gearData = ClientProxyCore.playerItemData.get(PlayerUtil.getName(player));

                float x = event.getResultSound().getX();
                float y = event.getResultSound().getY();
                float z = event.getResultSound().getZ();

                if (gearData == null || gearData.getFrequencyModule() == -1)
                {
                    // If the player doesn't have a frequency module, and the player isn't in an oxygenated environment
                    // Note: this is a very simplistic approach, and nowhere near realistic, but required for performance reasons
                    AxisAlignedBB bb = new AxisAlignedBB(x - 0.0015D, y - 0.0015D, z - 0.0015D, x + 0.0015D, y + 0.0015D, z + 0.0015D);
                    boolean playerInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player);
                    boolean soundInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player.world, bb);
                    if ((!playerInAtmosphere || !soundInAtmosphere))
                    {
                        float volume = 1.0F;
                        float pitch = 1.0F;

                        if (volumeField == null)
                        {
                            try
                            {
                                volumeField = LocatableSound.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "volume" : "field_147662_b"); // TODO Obfuscated environment support
                                volumeField.setAccessible(true);
                                pitchField = LocatableSound.class.getDeclaredField(GCCoreUtil.isDeobfuscated() ? "pitch" : "field_147663_c"); // TODO Obfuscated environment support
                                pitchField.setAccessible(true);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        if (volumeField != null && pitchField != null)
                        {
                            try
                            {
                                volume = event.getSound() instanceof LocatableSound ? (float) volumeField.get(event.getSound()) : 1.0F;
                                pitch = event.getSound() instanceof LocatableSound ? (float) pitchField.get(event.getSound()) : 1.0F;
                            }
                            catch (IllegalAccessException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        //First check for duplicate firing of PlaySoundEvent17 on this handler's own playing of a reduced volume sound (see below)
                        for (int i = 0; i < this.soundPlayList.size(); i++)
                        {
                            SoundPlayEntry entry = this.soundPlayList.get(i);

                            if (entry.name.equals(event.getName()) && entry.x == x && entry.y == y && entry.z == z && entry.volume == volume)
                            {
                                this.soundPlayList.remove(i);
                                return;
                            }
                        }

                        //If it's not a duplicate: play the same sound but at reduced volume
                        float newVolume = volume / Math.max(0.01F, ((IGalacticraftDimension) player.world.getDimension()).getSoundVolReductionAmount());

//                        this.soundPlayList.add(new SoundPlayEntry(event.getName(), x, y, z, newVolume));
//                        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(event.getResultSound().getSoundLocation());
//                        if (soundEvent != null)
//                        {
//                            ISound newSound = SimpleSound.record(soundEvent, SoundCategory.NEUTRAL, newVolume, pitch, x, y, z);
//                            event.getManager().playSound(newSound);
//                            event.setResultSound(null);
//                        }
//                        else
//                        {
//                            GCLog.severe("Sound event null! " + event.getName() + " " + event.getResultSound().getSoundLocation());
//                        } TODO Sounds
                    }
                }
            }
        }
    }

    private static class SoundPlayEntry
    {
        private final String name;
        private final float x;
        private final float y;
        private final float z;
        private final float volume;

        private SoundPlayEntry(String name, float x, float y, float z, float volume)
        {
            this.name = name;
            this.volume = volume;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class SleepCancelledEvent extends Event
    {
    }

    public static class OrientCameraEvent extends Event
    {
    }

}
