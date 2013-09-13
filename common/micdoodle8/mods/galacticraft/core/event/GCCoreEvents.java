package micdoodle8.mods.galacticraft.core.event;

import java.lang.reflect.Field;
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
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.GalacticraftCore.MinecraftLoadedEvent;
import micdoodle8.mods.galacticraft.core.GalacticraftCore.SleepCancelledEvent;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerSP;
import micdoodle8.mods.galacticraft.core.client.GCCoreThreadDownloadSound;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketSchematicList;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
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
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreEvents
{
    private static GCCoreThreadDownloadSound downloadResourcesThread;
    
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
            PacketDispatcher.sendPacketToServer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 4, new Object[] { page.getPageID() }));
            FMLClientHandler.instance().getClient().thePlayer.openGui(GalacticraftCore.instance, page.getGuiID(), FMLClientHandler.instance().getClient().thePlayer.worldObj, (int) FMLClientHandler.instance().getClient().thePlayer.posX, (int) FMLClientHandler.instance().getClient().thePlayer.posY, (int) FMLClientHandler.instance().getClient().thePlayer.posZ);
        }
    }

    @SideOnly(Side.CLIENT)
    private static ISchematicPage getNextSchematic(int currentIndex)
    {
        final HashMap<Integer, Integer> idList = new HashMap<Integer, Integer>();

        for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
        {
            idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
        }

        final SortedSet<Integer> keys = new TreeSet<Integer>(idList.keySet());
        final Iterator<Integer> iterator = keys.iterator();

        for (int count = 0; count < keys.size(); count++)
        {
            final int i = iterator.next();
            final ISchematicPage page = SchematicRegistry.getMatchingRecipeForID(idList.get(i));

            if (page.getPageID() == currentIndex)
            {
                if (count + 1 < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size())
                {
                    return PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(count + 1);
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

        for (int i = 0; i < PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.size(); i++)
        {
            idList.put(i, PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(i).getPageID());
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
                    return PlayerUtil.getPlayerBaseClientFromPlayer(FMLClientHandler.instance().getClient().thePlayer).unlockedSchematics.get(count - 1);
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
    
    @ForgeSubscribe
    public void onMinecraftLoaded(MinecraftLoadedEvent event)
    {
        final List<String> downloadUrls = new ArrayList<String>();
        
        downloadUrls.add("http://micdoodle8.com/galacticraft/sounds/downloadsounds.xml");
        
        if (Loader.isModLoaded("GalacticraftMars"))
        {
            downloadUrls.add("http://micdoodle8.com/galacticraft/sounds/mars/downloadmarssounds.xml");
        }
        
        try
        {
            if (GCCoreEvents.downloadResourcesThread == null)
            {
                GCCoreEvents.downloadResourcesThread = new GCCoreThreadDownloadSound(FMLClientHandler.instance().getClient().mcDataDir, FMLClientHandler.instance().getClient(), downloadUrls);
                GCCoreEvents.downloadResourcesThread.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
}
