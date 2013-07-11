package micdoodle8.mods.galacticraft.core;

import java.lang.reflect.Field;
import java.util.Random;
import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import micdoodle8.mods.galacticraft.api.item.IKeyItem;
import micdoodle8.mods.galacticraft.api.item.IKeyable;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

public class GCCoreEvents
{
    @ForgeSubscribe
    public void onEntityFall(LivingFallEvent event)
    {
        if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
        {
            event.setCanceled(true);
            event.setResult(Result.DENY);
            return;
        }

        event.setCanceled(false);
        event.setResult(Result.ALLOW);
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
                if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && idClicked != Block.tnt.blockID)
                {
                    event.setCanceled(true);
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
    public void growTreeBonemeal(BonemealEvent event)
    {
        if (event.world.getBlockId(event.X, event.Y, event.Z) == GCCoreBlocks.sapling.blockID)
        {
            if (!event.world.isRemote && event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider)
            {
                ((BlockSapling) GCCoreBlocks.sapling).growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
                event.setResult(Result.ALLOW);
                return;
            }
        }

        event.setResult(Result.DENY);
    }

    @ForgeSubscribe
    public void entityLivingEvent(LivingUpdateEvent event)
    {
        if (event.entityLiving.worldObj.provider instanceof IGalacticraftWorldProvider && !(event.entityLiving instanceof EntityPlayer) && !OxygenUtil.isAABBInBreathableAirBlock(event.entityLiving))
        {
            if ((!(event.entityLiving instanceof IEntityBreathable) || event.entityLiving instanceof IEntityBreathable && !((IEntityBreathable) event.entityLiving).canBreath()) && event.entityLiving.ticksExisted % 100 == 0)
            {
                event.entityLiving.attackEntityFrom(GCCoreDamageSource.oxygenSuffocation, 1);
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
        Class buildCraftClass = null;

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

        if (!doGen || GCCoreConfigManager.disableOilGen)
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
}
