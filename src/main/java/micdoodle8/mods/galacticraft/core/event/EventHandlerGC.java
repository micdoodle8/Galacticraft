package micdoodle8.mods.galacticraft.core.event;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStatsClient;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;

import java.util.*;

public class EventHandlerGC
{
    public static Map<Block, Item> bucketList = new HashMap<Block, Item>();
    public static boolean bedActivated;

    @SubscribeEvent
    public void onRocketLaunch(EntitySpaceshipBase.RocketLaunchEvent event)
    {
//        if (!event.entity.worldObj.isRemote && event.entity.worldObj.provider.dimensionId == 0)
//        {
//            if (event.rocket.riddenByEntity instanceof EntityPlayerMP)
//            {
//                TickHandlerServer.playersRequestingMapData.add((EntityPlayerMP) event.rocket.riddenByEntity);
//            }
//        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equals(Constants.MOD_ID_CORE))
        {
            ConfigManagerCore.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onWorldSave(Save event)
    {
        ChunkLoadingCallback.save((WorldServer) event.world);
    }

    @SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event)
    {
        ChunkLoadingCallback.load((WorldServer) event.world);
    }

    @SubscribeEvent
    public void onWorldLoad(Load event)
    {
        if (!event.world.isRemote)
        {
            ChunkLoadingCallback.load((WorldServer) event.world);
        }
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent event)
    {
        if (event.source.damageType.equals(DamageSource.onFire.damageType))
        {
            if (OxygenUtil.noAtmosphericCombustion(event.entityLiving.worldObj.provider))
            {
    	        if (OxygenUtil.isAABBInBreathableAirBlock(event.entityLiving.worldObj, event.entityLiving.boundingBox))
    	        	return;

                if (event.entityLiving.worldObj instanceof WorldServer)
                {
                    ((WorldServer) event.entityLiving.worldObj).func_147487_a("smoke", event.entityLiving.posX, event.entityLiving.posY + event.entityLiving.boundingBox.maxY - event.entityLiving.boundingBox.minY, event.entityLiving.posZ, 50, 0.0, 0.05, 0.0, 0.001);
                }

                event.entityLiving.extinguish();
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (player.ridingEntity instanceof EntityAutoRocket || player.ridingEntity instanceof EntityLanderBase)
            {
                event.distance = 0.0F;
                event.setCanceled(true);
                return;
            }
        }

        if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            event.distance *= ((IGalacticraftWorldProvider) event.entityLiving.worldObj.provider).getFallDamageModifier();
        }
    }

    @SubscribeEvent
    public void onPlayerClicked(PlayerInteractEvent event)
    {
        //Skip events triggered from Thaumcraft Golems and other non-players
    	if (event.entityPlayer == null || event.entityPlayer.inventory == null) return;
        
    	final World worldObj = event.entityPlayer.worldObj;
    	if (worldObj == null) return;
    	
        final Block idClicked = worldObj.getBlock(event.x, event.y, event.z);
        
        if (idClicked == Blocks.bed && worldObj.provider instanceof IGalacticraftWorldProvider && event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && !worldObj.isRemote && !((IGalacticraftWorldProvider)worldObj.provider).hasBreathableAtmosphere())
        {
        	if (GalacticraftCore.isPlanetsLoaded) GCPlayerStats.tryBedWarning((EntityPlayerMP) event.entityPlayer);

        	if (worldObj.provider instanceof WorldProviderOrbit)
        	{        		
            	//On space stations simply block the bed activation => no explosion
            	event.setCanceled(true);
            	return;
        	}
        		

        	//Optionally prevent beds from exploding - depends on canRespawnHere() in the WorldProvider interacting with this
       		EventHandlerGC.bedActivated = true;
       		if (worldObj.provider.canRespawnHere() && !EventHandlerGC.bedActivated)
       		{
           		EventHandlerGC.bedActivated = true;
       			
            	//On planets allow the bed to be used to designate a player spawn point
            	event.entityPlayer.setSpawnChunk(new ChunkCoordinates(event.x, event.y, event.z), false);
       		}
       		else
       			EventHandlerGC.bedActivated = false;
        }

    	final ItemStack heldStack = event.entityPlayer.inventory.getCurrentItem();
        final TileEntity tileClicked = worldObj.getTileEntity(event.x, event.y, event.z);

        if (heldStack != null)
        {
            if (tileClicked != null && tileClicked instanceof IKeyable)
            {
                if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
                {
                    event.setCanceled(!((IKeyable) tileClicked).canBreak() && !event.entityPlayer.capabilities.isCreativeMode);
                    return;
                }
                else if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK))
                {
                    if (heldStack.getItem() instanceof IKeyItem)
                    {
                        if (((IKeyItem) heldStack.getItem()).getTier(heldStack) == -1 || ((IKeyable) tileClicked).getTierOfKeyRequired() == -1 || ((IKeyItem) heldStack.getItem()).getTier(heldStack) == ((IKeyable) tileClicked).getTierOfKeyRequired())
                        {
                            event.setCanceled(((IKeyable) tileClicked).onValidKeyActivated(event.entityPlayer, heldStack, event.face));
                        }
                        else
                        {
                            event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.entityPlayer, event.face));
                        }
                    }
                    else
                    {
                        event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.entityPlayer, event.face));
                    }
                }
            }

            if (heldStack.getItem() instanceof ItemFlintAndSteel || heldStack.getItem() instanceof ItemFireball)
            {
                if (!worldObj.isRemote && event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK))
                {
                    if (idClicked != Blocks.tnt  && OxygenUtil.noAtmosphericCombustion(worldObj.provider) && !OxygenUtil.isAABBInBreathableAirBlock(worldObj, AxisAlignedBB.getBoundingBox(event.x, event.y, event.z, event.x + 1, event.y + 2, event.z + 1)))
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
        else if (tileClicked != null && tileClicked instanceof IKeyable)
        {
            if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK))
            {
                event.setCanceled(!((IKeyable) tileClicked).canBreak() && !event.entityPlayer.capabilities.isCreativeMode);
                return;
            }

            event.setCanceled(((IKeyable) tileClicked).onActivatedWithoutKey(event.entityPlayer, event.face));
        }
    }

    @SubscribeEvent
    public void entityLivingEvent(LivingUpdateEvent event)
    {
        final EntityLivingBase entityLiving = event.entityLiving; 
    	if (entityLiving instanceof EntityPlayerMP)
        {
                GalacticraftCore.handler.onPlayerUpdate((EntityPlayerMP) entityLiving);
                if (GalacticraftCore.isPlanetsLoaded) AsteroidsModule.playerHandler.onPlayerUpdate((EntityPlayerMP) entityLiving);
                return;
        }

        if (entityLiving.ticksExisted % 100 == 0)
        {
            if (entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
            {
                if (!(entityLiving instanceof EntityPlayer) && (!(entityLiving instanceof IEntityBreathable) || !((IEntityBreathable) entityLiving).canBreath()) && !((IGalacticraftWorldProvider)entityLiving.worldObj.provider).hasBreathableAtmosphere())
                {
                    if ((ConfigManagerCore.challengeMobDropsAndSpawning) && entityLiving instanceof EntityEnderman)
                    {
                    	return;
                    }
                    
                	if (!OxygenUtil.isAABBInBreathableAirBlock(entityLiving))
                    {
                        GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEvent);

                        if (suffocationEvent.isCanceled())
                        {
                            return;
                        }

                        entityLiving.attackEntityFrom(DamageSourceGC.oxygenSuffocation, 1);

                        GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                    }
                }
            }
        }
    }

    private ItemStack fillBucket(World world, MovingObjectPosition position)
    {
        Block block = world.getBlock(position.blockX, position.blockY, position.blockZ);

        Item bucket = bucketList.get(block);

        if (bucket != null && world.getBlockMetadata(position.blockX, position.blockY, position.blockZ) == 0)
        {
            world.setBlockToAir(position.blockX, position.blockY, position.blockZ);
            return new ItemStack(bucket);
        }

        return null;
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        MovingObjectPosition pos = event.target;

        ItemStack ret = fillBucket(event.world, pos);

        if (ret == null)
        {
            return;
        }

        event.result = ret;
        event.setResult(Result.ALLOW);
    }

    @SubscribeEvent
    public void populate(PopulateChunkEvent.Post event)
    {
        final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkZ, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);
        
        if (!doGen) return;
        
        final int worldX = event.chunkX << 4;
        final int worldZ = event.chunkZ << 4;

        EventHandlerGC.generateOil(event.world, event.rand, worldX + event.rand.nextInt(16), worldZ + event.rand.nextInt(16), false);
    }

    public static boolean oilPresent(World world, Random rand, int x, int z, BlockVec3 pos)
    {
        boolean doGen2 = false;

        for (Integer dim : ConfigManagerCore.externalOilGen)
        {
            if (dim == world.provider.dimensionId)
            {
                doGen2 = true;
                break;
            }
        }

        if (!doGen2)
            return false;
        
        final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 8, z + 8);

        if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID)
        {
            return false;
        }

        rand.setSeed(world.getSeed());
        long i1 = rand.nextInt() / 2L * 2L + 1L;
        long j1 = rand.nextInt() / 2L * 2L + 1L;
        rand.setSeed(x * i1 + z * j1 ^ world.getSeed());

        double randMod = Math.min(0.2D, 0.08D * ConfigManagerCore.oilGenFactor);

        if (biomegenbase.rootHeight >= 0.45F)
        {
            randMod /= 2;
        }
        if (biomegenbase.rootHeight < -0.5F)
        {
            randMod *= 1.8;
        }
        if (biomegenbase instanceof BiomeGenDesert)
        {
            randMod *= 1.8;
        }

        final boolean flag1 = rand.nextDouble() <= randMod;
        final boolean flag2 = rand.nextDouble() <= randMod;

        if (flag1 || flag2)
        {
            pos.y = 17 + rand.nextInt(10) + rand.nextInt(5);
            pos.x = x + rand.nextInt(16);
            pos.z = z + rand.nextInt(16);
            return true;
        }
        
       	return false;
    }

    public static void generateOil(World world, Random rand, int xx, int zz, boolean testFirst)
    {
    	BlockVec3 pos = new BlockVec3();
    	if (oilPresent(world, rand, xx, zz, pos))
    	{
            int x = pos.x;
            int cy = pos.y;
            int z = pos.z;
            int r = 3 + rand.nextInt(5);
            
    		if (testFirst && checkOilPresent(world, x, cy, z, r)) return;

            final int r2 = r * r;
            
            for (int bx = -r; bx <= r; bx++)
            {
                for (int by = -r + 2; by <= r - 2; by++)
                {
                    for (int bz = -r; bz <= r; bz++)
                    {
                        final int d2 = bx * bx + by * by * 3 + bz * bz;

                        if (d2 <= r2)
                        {
                            if (EventHandlerGC.checkBlock(world, bx + x - 1, by + cy, bz + z))
                                continue;
                            if (EventHandlerGC.checkBlock(world, bx + x + 1, by + cy, bz + z))
                                continue;
                            if (EventHandlerGC.checkBlock(world, bx + x, by + cy - 1, bz + z))
                                continue;
                            if (EventHandlerGC.checkBlock(world, bx + x, by + cy, bz + z - 1))
                                continue;
                            if (EventHandlerGC.checkBlock(world, bx + x, by + cy, bz + z + 1))
                                continue;
                            if (EventHandlerGC.checkBlockAbove(world, bx + x, by + cy + 1, bz + z))
                                continue;

                            world.setBlock(bx + x, by + cy, bz + z, GCBlocks.crudeOil, 0, 2);
                        }
                    }
                }
            }
        }
    }

    private static boolean checkOilPresent(World world, int x, int cy, int z, int r)
    {
        final int r2 = r * r;
        
        for (int bx = -r; bx <= r; bx++)
        {
            for (int by = -r + 2; by <= r - 2; by++)
            {
                for (int bz = -r; bz <= r; bz++)
                {
                    final int d2 = bx * bx + by * by * 3 + bz * bz;

                    if (d2 <= r2)
                    {
                        if (EventHandlerGC.checkBlock(world, bx + x - 1, by + cy, bz + z))
                            continue;
                        if (EventHandlerGC.checkBlock(world, bx + x + 1, by + cy, bz + z))
                            continue;
                        if (EventHandlerGC.checkBlock(world, bx + x, by + cy - 1, bz + z))
                            continue;
                        if (EventHandlerGC.checkBlock(world, bx + x, by + cy, bz + z - 1))
                            continue;
                        if (EventHandlerGC.checkBlock(world, bx + x, by + cy, bz + z + 1))
                            continue;
                        if (EventHandlerGC.checkBlockAbove(world, bx + x, by + cy + 1, bz + z))
                            continue;

                        if (world.getBlock(bx + x, by + cy, bz + z) == GCBlocks.crudeOil)
                        	return true;
                    }
                }
            }
        }
        
        return false;
    }
    
	public static void retrogenOil(World world, Chunk chunk)
	{
		int cx = chunk.xPosition;
		int cz = chunk.zPosition;

		generateOil(world, new Random(), cx << 4, cz << 4, true);
	}

	private static boolean checkBlock(World w, int x, int y, int z)
    {
        Block b = w.getBlock(x, y, z);
        if (b.getMaterial() == Material.air)
        {
            return true;
        }
        return b instanceof BlockLiquid && b != GCBlocks.crudeOil;
    }

    private static boolean checkBlockAbove(World w, int x, int y, int z)
    {
        Block b = w.getBlock(x, y, z);
        if (b instanceof BlockSand)
        {
            return true;
        }
        if (b instanceof BlockGravel)
        {
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void schematicUnlocked(Unlock event)
    {
        GCPlayerStats stats = GCPlayerStats.get(event.player);

        if (!stats.unlockedSchematics.contains(event.page))
        {
            stats.unlockedSchematics.add(event.page);
            Collections.sort(stats.unlockedSchematics);

            if (event.player != null && event.player.playerNetServerHandler != null)
            {
                Integer[] iArray = new Integer[stats.unlockedSchematics.size()];

                for (int i = 0; i < iArray.length; i++)
                {
                    ISchematicPage page = stats.unlockedSchematics.get(i);
                    iArray[i] = page == null ? -2 : page.getPageID();
                }

                List<Object> objList = new ArrayList<Object>();
                objList.add(iArray);

                GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), event.player);
            }
        }
    }

    @SideOnly(Side.CLIENT)
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
            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_SCHEMATIC_PAGE, new Object[] { page.getPageID() }));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, page.getGuiID(), FMLClientHandler.instance().getClient().thePlayer.worldObj, (int) FMLClientHandler.instance().getClient().thePlayer.posX, (int) FMLClientHandler.instance().getClient().thePlayer.posY, (int) FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getNextSchematic(int currentIndex)
    {
        final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

        EntityClientPlayerMP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        for (int i = 0; i < stats.unlockedSchematics.size(); i++)
        {
            idList.put(i, stats.unlockedSchematics.get(i).getPageID());
        }

        final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
        final Iterator<Integer> iterator = keys.iterator();

        for (int count = 0; count < keys.size(); count++)
        {
            final int i = iterator.next();
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

            if (page.getPageID() == currentIndex)
            {
                if (count + 1 < stats.unlockedSchematics.size())
                {
                    return stats.unlockedSchematics.get(count + 1);
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getLastSchematic(int currentIndex)
    {
        final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

        EntityClientPlayerMP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);
        GCPlayerStatsClient stats = GCPlayerStatsClient.get(player);

        for (int i = 0; i < stats.unlockedSchematics.size(); i++)
        {
            idList.put(i, stats.unlockedSchematics.get(i).getPageID());
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
                    return stats.unlockedSchematics.get(count - 1);
                }
                else
                {
                    return null;
                }
            }
        }

        return null;
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event)
    {
        if (event.entityLiving instanceof EntityPlayerMP)
        {
            GCPlayerStats stats = GCPlayerStats.get((EntityPlayerMP) event.entityLiving);
            if (!event.entityLiving.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
            {
            	event.entityLiving.captureDrops = true;
                for (int i = stats.extendedInventory.getSizeInventory() - 1; i >= 0; i--)
                {
                    ItemStack stack = stats.extendedInventory.getStackInSlot(i);

                    if (stack != null)
                    {
                        ((EntityPlayerMP) event.entityLiving).func_146097_a(stack, true, false);
                        stats.extendedInventory.setInventorySlotContents(i, null);
                    }
                }
                event.entityLiving.captureDrops = false;
            }
        }
    }

    // @SideOnly(Side.CLIENT)
    // @SubscribeEvent
    // public void onMinecraftLoaded(MinecraftLoadedEvent event)
    // {
    // ;
    // }

    //	@SideOnly(Side.CLIENT)
    //	@SubscribeEvent
    //	public void onSoundLoad(SoundLoadEvent event)
    //	{
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/scaryscape.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip1.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip2.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip3.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip4.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip5.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip6.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip7.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "ambience/singledrip8.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "entity/bossdeath.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "entity/bosslaugh.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "entity/bossliving.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "entity/slime_death.ogg");
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.TEXTURE_PREFIX + "music/mars_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.TEXTURE_PREFIX + "music/mimas_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.TEXTURE_PREFIX + "music/orbit_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.TEXTURE_PREFIX + "music/scary_ambience.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.TEXTURE_PREFIX + "music/spacerace_JC.ogg"));
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "player/closeairlock.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "player/openairlock.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "player/parachute.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "player/unlockchest.ogg");
    //		event.manager.addSound(GalacticraftCore.TEXTURE_PREFIX + "shuttle/shuttle.ogg");
    //	}
    //
    //	@SideOnly(Side.CLIENT)
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onLeaveBedButtonClicked(SleepCancelledEvent event)
    {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        ChunkCoordinates c = player.playerLocation;

        if (c != null)
        {
            EventWakePlayer event0 = new EventWakePlayer(player, c.posX, c.posY, c.posZ, true, true, false, true);
            MinecraftForge.EVENT_BUS.post(event0);
            player.wakeUpPlayer(true, true, false);
            if (player.worldObj.isRemote && GalacticraftCore.isPlanetsLoaded)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(PacketSimpleMars.EnumSimplePacketMars.S_WAKE_PLAYER, new Object[] {}));
            }
        }
    }

    @SubscribeEvent
    public void onZombieSummonAid(SummonAidEvent event)
    {
        if (event.entity instanceof EntityEvolvedZombie)
        {
            event.customSummonedAid = new EntityEvolvedZombie(event.world);

            if (((EntityLivingBase) event.entity).getRNG().nextFloat() < ((EntityEvolvedZombie) event.entity).getEntityAttribute(((EntityEvolvedZombie) event.entity).getReinforcementsAttribute()).getAttributeValue())
            {
                event.setResult(Result.ALLOW);
            }
            else
            {
                event.setResult(Result.DENY);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void overrideSkyColor(FogColors event)
    {
    	//Disable any night vision effects on the sky, if the planet has no atmosphere
    	if (event.entity.isPotionActive(Potion.nightVision))
    	{
    		WorldClient worldclient = Minecraft.getMinecraft().theWorld;

    		if (worldclient.provider instanceof IGalacticraftWorldProvider && ((IGalacticraftWorldProvider)worldclient.provider).getCelestialBody().atmosphere.size() == 0 && event.block.getMaterial() == Material.air && !((IGalacticraftWorldProvider)worldclient.provider).hasBreathableAtmosphere())
    		{
    			Vec3 vec = worldclient.getFogColor(1.0F);
    			event.red = (float) vec.xCoord;
    			event.green = (float) vec.yCoord;
    			event.blue = (float) vec.zCoord;
    			return;
    		}
    	
	    	if (worldclient.provider.getSkyRenderer() instanceof SkyProviderOverworld && event.entity.posY > Constants.OVERWORLD_SKYPROVIDER_STARTHEIGHT)
	    	{
				Vec3 vec = WorldUtil.getFogColorHook(event.entity.worldObj);
				event.red = (float) vec.xCoord;
				event.green = (float) vec.yCoord;
				event.blue = (float) vec.zCoord;
				return;
	    	}
    	}
    }

    private List<SoundPlayEntry> soundPlayList = new ArrayList<SoundPlayEntry>();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSoundPlayed(PlaySoundEvent17 event)
    {
    	//The event.result starts off equal to event.sound, but could have been altered or set to null by another mod
    	if (event.result == null) return;
    	
    	EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;

    	if (player != null && player.worldObj != null && player.worldObj.provider instanceof IGalacticraftWorldProvider && event != null)
    	{
        	//Only modify standard game sounds, not music
    		if (event.result.getAttenuationType() != ISound.AttenuationType.NONE)
    		{
    			PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getGameProfile().getName());

    			float x = event.result.getXPosF();
    			float y = event.result.getYPosF();
    			float z = event.result.getZPosF();

    			if (gearData == null || gearData.getFrequencyModule() == -1)
    			{
    				// If the player doesn't have a frequency module, and the player isn't in an oxygenated environment
    				// Note: this is a very simplistic approach, and nowhere near realistic, but required for performance reasons
    				AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - 0.0015D, y - 0.0015D, z - 0.0015D, x + 0.0015D, y + 0.0015D, z + 0.0015D);
    				boolean playerInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player);
    				boolean soundInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player.worldObj, bb);
    				if ((!playerInAtmosphere || !soundInAtmosphere))
    				{
    					float volume = event.result.getVolume();

    					//First check for duplicate firing of PlaySoundEvent17 on this handler's own playing of a reduced volume sound (see below) 
    					for (int i = 0; i < this.soundPlayList.size(); i++)
    					{
    						SoundPlayEntry entry = this.soundPlayList.get(i);

    						if (entry.name.equals(event.name) && entry.x == x && entry.y == y && entry.z == z && entry.volume == volume)
    						{
    							this.soundPlayList.remove(i);
    							return;
    						}
    					}

    					//If it's not a duplicate: play the same sound but at reduced volume
    					float newVolume = volume / Math.max(0.01F, ((IGalacticraftWorldProvider) player.worldObj.provider).getSoundVolReductionAmount());

    					this.soundPlayList.add(new SoundPlayEntry(event.name, x, y, z, newVolume));
    					ISound newSound = new PositionedSoundRecord(event.result.getPositionedSoundLocation(), newVolume, event.result.getPitch(), x, y, z);
    					event.manager.playSound(newSound);
    					event.result = null;
    					return;
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
