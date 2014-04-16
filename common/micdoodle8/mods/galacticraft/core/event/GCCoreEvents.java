package micdoodle8.mods.galacticraft.core.event;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.item.IKeyItem;
import micdoodle8.mods.galacticraft.api.item.IKeyable;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.FlipPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicEvent.Unlock;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.GalacticraftCore.SleepCancelledEvent;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerServer.EnumPacketServer;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSchematicList;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.block.Block;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.audio.SoundPoolProtocolHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreEvents.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreEvents
{
	@ForgeSubscribe
	public void onWorldSave(Save event)
	{
		ChunkLoadingCallback.save((WorldServer) event.world);
	}

	@ForgeSubscribe
	public void onChunkDataLoad(ChunkDataEvent.Load event)
	{
		ChunkLoadingCallback.load((WorldServer) event.world);
	}

	@ForgeSubscribe
	public void onWorldLoad(Load event)
	{
		if (!event.world.isRemote)
		{
			ChunkLoadingCallback.load((WorldServer) event.world);
		}
	}

	@ForgeSubscribe
	public void onEntityFall(LivingFallEvent event)
	{
		if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
		{
			event.distance *= ((IGalacticraftWorldProvider) event.entityLiving.worldObj.provider).getFallDamageModifier();
		}
	}

	@ForgeSubscribe
	public void onPlayerClicked(PlayerInteractEvent event)
	{
		final ItemStack heldStack = event.entityPlayer.inventory.getCurrentItem();

		final TileEntity tileClicked = event.entityPlayer.worldObj.getBlockTileEntity(event.x, event.y, event.z);
		final int idClicked = event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z);

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
					if (idClicked != Block.tnt.blockID && !OxygenUtil.isAABBInBreathableAirBlock(event.entityLiving.worldObj, new Vector3(event.x, event.y, event.z), new Vector3(event.x + 1, event.y + 1, event.z + 1)))
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

	@ForgeSubscribe
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

				event.entityLiving.attackEntityFrom(GCCoreDamageSource.oxygenSuffocation, 1);

				GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(event.entityLiving);
				MinecraftForge.EVENT_BUS.post(suffocationEventPost);
			}
		}
	}

	@ForgeSubscribe
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

		int bcOilID1 = -1;
		int bcOilID2 = -1;
		Item bcOilBucket = null;

		try
		{
			if ((buildCraftClass = Class.forName("buildcraft.BuildCraftEnergy")) != null)
			{
				for (final Field f : buildCraftClass.getFields())
				{
					if (f.getName().equals("oilMoving"))
					{
						final Block block = (Block) f.get(null);

						bcOilID1 = block.blockID;
					}
					else if (f.getName().equals("oilStill"))
					{
						final Block block = (Block) f.get(null);

						bcOilID2 = block.blockID;
					}
					else if (f.getName().equals("bucketOil"))
					{
						final Item item = (Item) f.get(null);

						bcOilBucket = item;
					}
				}
			}
		}
		catch (final Throwable cnfe)
		{

		}

		final int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

		if ((blockID == bcOilID1 || blockID == bcOilID2 || blockID == GCCoreBlocks.crudeOilStill.blockID) && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0 && bcOilBucket != null)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);

			return new ItemStack(bcOilBucket);
		}
		else
		{
			return null;
		}
	}

	@ForgeSubscribe
	public void populate(PopulateChunkEvent.Post event)
	{
		final boolean doGen = TerrainGen.populate(event.chunkProvider, event.world, event.rand, event.chunkX, event.chunkX, event.hasVillageGenerated, PopulateChunkEvent.Populate.EventType.CUSTOM);
		boolean doGen2 = false;

		for (Integer dim : GCCoreConfigManager.externalOilGen)
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

		GCCoreEvents.doPopulate(event.world, event.rand, worldX + event.rand.nextInt(16), worldZ + event.rand.nextInt(16));
	}

	public static void doPopulate(World world, Random rand, int x, int z)
	{
		final BiomeGenBase biomegenbase = world.getBiomeGenForCoords(x + 16, z + 16);

		if (biomegenbase.biomeID == BiomeGenBase.sky.biomeID || biomegenbase.biomeID == BiomeGenBase.hell.biomeID)
		{
			return;
		}

		final double randMod = Math.min(0.5D, 0.2D * GCCoreConfigManager.oilGenFactor);

		final boolean flag1 = rand.nextDouble() <= randMod;
		final boolean flag2 = rand.nextDouble() <= randMod;

		if (flag1 || flag2)
		{
			final int cx = x, cy = 20 + rand.nextInt(11), cz = z;

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
							world.setBlock(bx + cx, by + cy, bz + cz, GCCoreBlocks.crudeOilStill.blockID, 0, 2);
						}
					}
				}
			}
		}
	}

	@ForgeSubscribe
	public void schematicUnlocked(Unlock event)
	{
		GCCorePlayerMP player = (GCCorePlayerMP) event.player;

		if (!player.getUnlockedSchematics().contains(event.page))
		{
			player.getUnlockedSchematics().add(event.page);
			Collections.sort(player.getUnlockedSchematics());

			if (player != null && player.playerNetServerHandler != null)
			{
				player.playerNetServerHandler.sendPacketToPlayer(GCCorePacketSchematicList.buildSchematicListPacket(player.getUnlockedSchematics()));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void schematicFlipEvent(FlipPage event)
	{
		ISchematicPage page = null;

		switch (event.direction)
		{
		case 1:
			page = GCCoreEvents.getNextSchematic(event.index);
			break;
		case -1:
			page = GCCoreEvents.getLastSchematic(event.index);
			break;
		}

		if (page != null)
		{
			PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketServer.OPEN_SCHEMATIC_PAGE, new Object[] { page.getPageID() }));
			FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, page.getGuiID(), FMLClientHandler.instance().getClient().thePlayer.worldObj, (int) FMLClientHandler.instance().getClient().thePlayer.posX, (int) FMLClientHandler.instance().getClient().thePlayer.posY, (int) FMLClientHandler.instance().getClient().thePlayer.posZ);
		}
	}

	@SideOnly(Side.CLIENT)
	private static ISchematicPage getNextSchematic(int currentIndex)
	{
		final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

		GCCorePlayerSP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);

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

		GCCorePlayerSP player = PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer, false);

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

	@ForgeSubscribe
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.entityLiving instanceof GCCorePlayerMP)
		{
			if (!event.entityLiving.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			{
				for (int i = 0; i < ((GCCorePlayerMP) event.entityLiving).getExtendedInventory().getSizeInventory(); i++)
				{
					ItemStack stack = ((GCCorePlayerMP) event.entityLiving).getExtendedInventory().getStackInSlot(i);

					if (stack != null)
					{
						((GCCorePlayerMP) event.entityLiving).dropPlayerItemWithRandomChoice(stack, true);
						((GCCorePlayerMP) event.entityLiving).getExtendedInventory().setInventorySlotContents(i, null);
					}
				}
			}
		}
	}

	// @SideOnly(Side.CLIENT)
	// @ForgeSubscribe
	// public void onMinecraftLoaded(MinecraftLoadedEvent event)
	// {
	// ;
	// }

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onSoundLoad(SoundLoadEvent event)
	{
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/scaryscape.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip1.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip2.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip3.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip4.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip5.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip6.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip7.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "ambience/singledrip8.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "entity/bossdeath.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "entity/bosslaugh.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "entity/bossliving.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "entity/slime_death.ogg");
		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.ASSET_PREFIX + "music/mars_JC.ogg"));
		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.ASSET_PREFIX + "music/mimas_JC.ogg"));
		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.ASSET_PREFIX + "music/orbit_JC.ogg"));
		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.ASSET_PREFIX + "music/scary_ambience.ogg"));
		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, GalacticraftCore.ASSET_PREFIX + "music/spacerace_JC.ogg"));
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "player/closeairlock.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "player/openairlock.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "player/parachute.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "player/unlockchest.ogg");
		event.manager.addSound(GalacticraftCore.ASSET_PREFIX + "shuttle/shuttle.ogg");
	}

	@SideOnly(Side.CLIENT)
	private SoundPoolEntry func_110654_c(SoundPool pool, String par1Str)
	{
		try
		{
			ResourceLocation resourcelocation = new ResourceLocation(par1Str);
			String s1 = String.format("%s:%s:%s/%s", new Object[] { "mcsounddomain", resourcelocation.getResourceDomain(), "sound", resourcelocation.getResourcePath() });
			SoundPoolProtocolHandler soundpoolprotocolhandler = new SoundPoolProtocolHandler(pool);
			return new SoundPoolEntry(par1Str, new URL((URL) null, s1, soundpoolprotocolhandler));
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onLeaveBedButtonClicked(SleepCancelledEvent event)
	{
		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

		if (player instanceof GCCorePlayerSP)
		{
			((GCCorePlayerSP) player).wakeUpPlayer(false, true, true, true);
		}
	}

	private List<SoundPlayEntry> soundPlayList = new ArrayList<SoundPlayEntry>();

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onSoundPlayed(PlaySoundEvent event)
	{
		EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;

		PlayerGearData gearData = null;

		for (PlayerGearData gearData2 : ClientProxyCore.playerItemData)
		{
			if (gearData2.getPlayer().username.equals(player.username))
			{
				gearData = gearData2;
				break;
			}
		}

		if (player.worldObj.provider instanceof IGalacticraftWorldProvider && (gearData == null || gearData.getFrequencyModule() == -1))
		{
			for (int i = 0; i < this.soundPlayList.size(); i++)
			{
				SoundPlayEntry entry = this.soundPlayList.get(i);

				if (entry.name.equals(event.name) && entry.x == event.x && entry.y == event.y && entry.z == event.z && entry.volume == event.volume)
				{
					this.soundPlayList.remove(i);
					event.result = event.source;
					return;
				}
			}

			float newVolume = event.volume / Math.max(0.01F, ((IGalacticraftWorldProvider) player.worldObj.provider).getSoundVolReductionAmount());

			this.soundPlayList.add(new SoundPlayEntry(event.name, event.x, event.y, event.z, newVolume));
			event.manager.playSound(event.name, event.x, event.y, event.z, newVolume, event.pitch);
			event.result = null;
			return;
		}

		event.result = event.source;
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
}
