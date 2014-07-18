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
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.FlipPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.Unlock;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.core.entities.EntityLanderBase;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;

import java.lang.reflect.Field;
import java.util.*;

public class EventHandlerGC
{
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.modID.equals(Constants.MOD_ID_CORE))
        {
            ConfigManagerCore.syncConfig();
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
		final ItemStack heldStack = event.entityPlayer.inventory.getCurrentItem();

		final TileEntity tileClicked = event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z);
		final Block idClicked = event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z);

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

			if (event.entityPlayer.worldObj.provider instanceof IGalacticraftWorldProvider && heldStack.getItem() instanceof ItemFlintAndSteel)
			{
				if (!event.entity.worldObj.isRemote && event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK))
				{
					if (idClicked != Blocks.tnt && !OxygenUtil.isAABBInBreathableAirBlock(event.entityLiving.worldObj, new Vector3(event.x, event.y, event.z), new Vector3(event.x + 1, event.y + 2, event.z + 1)))
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
		if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider && !(event.entityLiving instanceof EntityPlayer) && !OxygenUtil.isAABBInBreathableAirBlock(event.entityLiving))
		{
			if ((!(event.entityLiving instanceof IEntityBreathable) || event.entityLiving instanceof IEntityBreathable && !((IEntityBreathable) event.entityLiving).canBreath()) && event.entityLiving.ticksExisted % 100 == 0)
			{
				GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(event.entityLiving);
				MinecraftForge.EVENT_BUS.post(suffocationEvent);

				if (suffocationEvent.isCanceled())
				{
					return;
				}

				event.entityLiving.attackEntityFrom(DamageSourceGC.oxygenSuffocation, 1);

				GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(event.entityLiving);
				MinecraftForge.EVENT_BUS.post(suffocationEventPost);
			}
		}
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		final ItemStack result = this.fillCustomBucket(event.world, event.target);

		if (result == null)
		{
			return;
		}

		event.result = result;
		event.setResult(Result.ALLOW);
	}

	public ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
	{
		Class<?> buildCraftClass = null;

		Block bcOilID1 = null;
		Block bcOilID2 = null;
		Item bcOilBucket = null;

		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (final Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("oilMoving"))
					{
                        bcOilID1 = (Block) f.get(null);
					}
					else if (f.getName().equals("oilStill"))
					{
						bcOilID2 = (Block) f.get(null);
					}
					else if (f.getName().equals("bucketOil"))
					{
						bcOilBucket = (Item) f.get(null);
					}
				}
			}
		}
		catch (final Throwable cnfe)
		{

		}

		final Block blockID = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

		if ((blockID == bcOilID1 || blockID == bcOilID2 || blockID == GCBlocks.crudeOilStill) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0 && bcOilBucket != null)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);

			return new ItemStack(bcOilBucket);
		}
		else
		{
			return null;
		}
	}

	@SubscribeEvent
	public void populate(PopulateChunkEvent.Post event)
	{
		final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkX, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);
		boolean doGen2 = false;

		for (Integer dim : ConfigManagerCore.externalOilGen)
		{
			if (dim == event.world.provider.dimensionId)
			{
				doGen2 = true;
				break;
			}
		}

		if (!doGen || !(event.world.provider instanceof IGalacticraftWorldProvider) && !doGen2)
		{
			return;
		}

		final int worldX = event.chunkX << 4;
		final int worldZ = event.chunkZ << 4;

		EventHandlerGC.doPopulate(event.world, event.rand, worldX + event.rand.nextInt(16), worldZ + event.rand.nextInt(16));
	}

	public static void doPopulate(World world, Random rand, int x, int z)
	{
		final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 16, z + 16);

		if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID)
		{
			return;
		}

		final double randMod = Math.min(0.5D, 0.2D * ConfigManagerCore.oilGenFactor);

		final boolean flag1 = rand.nextDouble() <= randMod;
		final boolean flag2 = rand.nextDouble() <= randMod;

		if (flag1 || flag2)
		{
            int cy = 20 + rand.nextInt(11);

			final int r = 2 + rand.nextInt(2);

			final int r2 = r * r;

			for (int bx = -r; bx <= r; bx++)
			{
				for (int by = -r; by <= r; by++)
				{
					for (int bz = -r; bz <= r; bz++)
					{
						final int d2 = bx * bx + by * by + bz * bz;

						if (d2 <= r2)
						{
							world.setBlock(bx + x, by + cy, bz + z, GCBlocks.crudeOilStill, 0, 2);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void schematicUnlocked(Unlock event)
	{
		GCEntityPlayerMP player = (GCEntityPlayerMP) event.player;

		if (!player.getPlayerStats().unlockedSchematics.contains(event.page))
		{
			player.getPlayerStats().unlockedSchematics.add(event.page);
			Collections.sort(player.getPlayerStats().unlockedSchematics);

			if (player != null && player.playerNetServerHandler != null)
			{
				Integer[] iArray = new Integer[player.getPlayerStats().unlockedSchematics.size()];

				for (int i = 0; i < iArray.length; i++)
				{
					ISchematicPage page = player.getPlayerStats().unlockedSchematics.get(i);
					iArray[i] = page == null ? -2 : page.getPageID();
				}

				List<Object> objList = new ArrayList<Object>();
				objList.add(iArray);

				GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), player);
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

		GCEntityClientPlayerMP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);

		for (int i = 0; i < player.unlockedSchematics.size(); i++)
		{
			idList.put(i, player.unlockedSchematics.get(i).getPageID());
		}

		final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
		final Iterator<Integer> iterator = keys.iterator();

		for (int count = 0; count < keys.size(); count++)
		{
			final int i = iterator.next();
			final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

			if (page.getPageID() == currentIndex)
			{
				if (count + 1 < player.unlockedSchematics.size())
				{
					return player.unlockedSchematics.get(count + 1);
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

		GCEntityClientPlayerMP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);

		for (int i = 0; i < player.unlockedSchematics.size(); i++)
		{
			idList.put(i, player.unlockedSchematics.get(i).getPageID());
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
					return player.unlockedSchematics.get(count - 1);
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
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.entityLiving instanceof GCEntityPlayerMP)
		{
			if (!event.entityLiving.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			{
				for (int i = 0; i < ((GCEntityPlayerMP) event.entityLiving).getPlayerStats().extendedInventory.getSizeInventory(); i++)
				{
					ItemStack stack = ((GCEntityPlayerMP) event.entityLiving).getPlayerStats().extendedInventory.getStackInSlot(i);

					if (stack != null)
					{
						((GCEntityPlayerMP) event.entityLiving).dropPlayerItemWithRandomChoice(stack, true);
						((GCEntityPlayerMP) event.entityLiving).getPlayerStats().extendedInventory.setInventorySlotContents(i, null);
					}
				}
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

		if (player instanceof GCEntityClientPlayerMP)
		{
			((GCEntityClientPlayerMP) player).wakeUpPlayer(false, true, true, true);
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

	//	private List<SoundPlayEntry> soundPlayList = new ArrayList<SoundPlayEntry>();
	//
	//	@SideOnly(Side.CLIENT)
	//	@SubscribeEvent
	//	public void onSoundPlayed(PlaySoundEvent17 event)
	//	{
	//		EntityPlayerSP player = FMLClientHandler.INSTANCE().getClient().thePlayer;
	//
	//		PlayerGearData gearData = null;
	//
	//		for (PlayerGearData gearData2 : ClientProxyCore.playerItemData)
	//		{
	//			if (gearData2.getPlayer().getGameProfile().equals(player.getGameProfile()))
	//			{
	//				gearData = gearData2;
	//				break;
	//			}
	//		}
	//
	//		if (player.worldObj.provider instanceof IGalacticraftWorldProvider && (gearData == null || gearData.getFrequencyModule() == -1))
	//		{
	//			for (int i = 0; i < this.soundPlayList.size(); i++)
	//			{
	//				SoundPlayEntry entry = this.soundPlayList.get(i);
	//
	//				if (entry.name.equals(event.name) && entry.x == event.x && entry.y == event.y && entry.z == event.z && entry.volume == event.volume)
	//				{
	//					this.soundPlayList.remove(i);
	//					event.result = event.source;
	//					return;
	//				}
	//			}
	//
	//			float newVolume = event.volume / Math.max(0.01F, ((IGalacticraftWorldProvider) player.worldObj.provider).getSoundVolReductionAmount());
	//
	//			this.soundPlayList.add(new SoundPlayEntry(event.name, event.x, event.y, event.z, newVolume));
	//			event.manager.playSound(event.name, event.x, event.y, event.z, newVolume, event.pitch);
	//			event.result = null;
	//			return;
	//		}
	//
	//		event.result = event.source;
	//	} TODO Fix sounds

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
