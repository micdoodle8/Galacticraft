package micdoodle8.mods.galacticraft.planets.mars.tile;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.planets.ConfigManagerPlanets;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockLaunchController;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlockNames;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.*;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

// import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
// import micdoodle8.mods.galacticraft.core.util.GCLog;
// import java.util.HashMap;
// import java.util.Map;

public class TileEntityLaunchController extends TileBaseElectricBlockWithInventory implements ISidedInventory, ILandingPadAttachable
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsBlockNames.launchController)
    public static TileEntityType<TileEntityLaunchController> TYPE;

    public static final int WATTS_PER_TICK = 1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean launchPadRemovalDisabled = true;
    //    private Ticket chunkLoadTicket;
//    private List<BlockPos> connectedPads = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int frequency = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int destFrequency = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public UUID ownerUUID;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean frequencyValid;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean destFrequencyValid;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int launchDropdownSelection;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean launchSchedulingEnabled;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean controlEnabled;
    public boolean hideTargetDestination = true;
    public boolean requiresClientUpdate;
    public Object attachedDock = null;
    private boolean frequencyCheckNeeded = false;
    //    private static Map<Integer, Long> tickCounts = new HashMap<>();
//    private static Map<Integer, Integer> instanceCounts = new HashMap<>();
    private static final TicketType<TileEntityLaunchController> TICKET_TYPE = TicketType.create(Constants.MOD_ID_PLANETS + ":chunk_loader", Comparator.comparing(TileEntity::getPos));
    public static final int TICKET_DISTANCE = 2;

    @Nullable
    private World prevWorld;
    @Nullable
    private BlockPos prevPos;

    private boolean hasRegistered;
    private boolean isFirstTick = true;

    private final Set<ChunkPos> chunkSet = new ObjectOpenHashSet<>();

    public TileEntityLaunchController()
    {
        super(TYPE);
        this.storage.setMaxExtract(6);
        this.noRedstoneControl = true;
        this.inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            this.controlEnabled = this.launchSchedulingEnabled && this.hasEnoughEnergyToRun && !this.getDisabled(0);

            if (this.frequencyCheckNeeded)
            {
                this.checkDestFrequencyValid();
                this.frequencyCheckNeeded = false;
            }

            if (this.requiresClientUpdate)
            {
                // PacketDispatcher.sendPacketToAllPlayers(this.getPacket());
                // TODO
                this.requiresClientUpdate = false;
            }

            if (this.ticks % 40 == 0)
            {
                this.setFrequency(this.frequency);
                this.setDestinationFrequency(this.destFrequency);
            }

//            if (this.ticks % 20 == 0)
//            {
//                if (this.chunkLoadTicket != null)
//                {
//                    for (int i = 0; i < this.connectedPads.size(); i++)
//                    {
//                        BlockPos coords = this.connectedPads.get(i);
//                        Block block = this.world.getBlockState(coords).getBlock();
//
//                        if (block != GCBlocks.landingPadFull)
//                        {
//                            this.connectedPads.remove(i);
//                            ForgeChunkManager.unforceChunk(this.chunkLoadTicket, new ChunkPos(coords.getX() >> 4, coords.getZ() >> 4));
//                        }
//                    }
//                }
//            }
        }
        else
        {
            if (this.frequency == -1 && this.destFrequency == -1)
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimpleMars(EnumSimplePacketMars.S_UPDATE_ADVANCED_GUI, GCCoreUtil.getDimensionType(this.world), new Object[]{5, this.getPos(), 0}));
            }
        }

        World world = this.getWorld();
        if (world != null && !world.isRemote && world.getChunkProvider() instanceof ServerChunkProvider)
        {
            if (isFirstTick)
            {
                isFirstTick = false;
                if (!ConfigManagerPlanets.launchControllerChunkLoad.get())
                {
                    //If we just loaded but are not actually able to operate
                    // release any tickets we have assigned to us that we loaded with
                    releaseChunkTickets(world, this.getPos());
                }
            }

            if (hasRegistered && prevWorld != null && (prevPos == null || prevWorld != world || prevPos != this.getPos()))
            {
                releaseChunkTickets(prevWorld);
            }

            if (hasRegistered && !ConfigManagerPlanets.launchControllerChunkLoad.get())
            {
                releaseChunkTickets(world);
            }

            if (!hasRegistered)
            {
                registerChunkTickets(world);
            }
        }
    }

    public UUID getOwnerUUID()
    {
        return this.ownerUUID;
    }

    public void setOwnerUUID(UUID uuid)
    {
        this.ownerUUID = uuid;
    }

    @Override
    public void remove()
    {
        super.remove();

//        if (this.chunkLoadTicket != null)
//        {
//            ForgeChunkManager.releaseTicket(this.chunkLoadTicket);
//        }

        if (!world.isRemote() && prevWorld != null)
        {
            releaseChunkTickets(prevWorld);
        }
    }

//    @Override
//    public void onTicketLoaded(Ticket ticket, boolean placed)
//    {
//        if (!this.world.isRemote && ConfigManagerPlanets.launchControllerChunkLoad.get())
//        {
//            if (ticket == null)
//            {
//                return;
//            }
//
//            if (this.chunkLoadTicket == null)
//            {
//                this.chunkLoadTicket = ticket;
//            }
//
//            CompoundNBT nbt = this.chunkLoadTicket.getModData();
//            nbt.putInt("ChunkLoaderTileX", this.getPos().getX());
//            nbt.putInt("ChunkLoaderTileY", this.getPos().getY());
//            nbt.putInt("ChunkLoaderTileZ", this.getPos().getZ());
//
//            for (int x = -2; x <= 2; x++)
//            {
//                for (int z = -2; z <= 2; z++)
//                {
//                    Block blockID = this.world.getBlockState(this.getPos().add(x, 0, z)).getBlock();
//
//                    if (blockID instanceof BlockPadFull)
//                    {
//                        if (this.getPos().getX() + x >> 4 != this.getPos().getX() >> 4 || this.getPos().getZ() + z >> 4 != this.getPos().getZ() >> 4)
//                        {
//                            this.connectedPads.add(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));
//
//                            if (placed)
//                            {
//                                ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.world, this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z, this.getOwnerName());
//                            }
//                            else
//                            {
//                                ChunkLoadingCallback.addToList(this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
//                            }
//                        }
//                    }
//                }
//            }
//
//            ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
//        }
//    }

//    @Override
//    public Ticket getTicket()
//    {
//        return this.chunkLoadTicket;
//    }

//    @Override
//    public BlockPos getCoords()
//    {
//        return new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
//    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);

        this.ownerUUID = UUID.fromString(nbt.getString("OwnerName"));
        this.launchDropdownSelection = nbt.getInt("LaunchSelection");
        this.frequency = nbt.getInt("ControllerFrequency");
        this.destFrequency = nbt.getInt("TargetFrequency");
        this.frequencyCheckNeeded = true;
        this.launchPadRemovalDisabled = nbt.getBoolean("LaunchPadRemovalDisabled");
        this.launchSchedulingEnabled = nbt.getBoolean("LaunchPadSchedulingEnabled");
        this.hideTargetDestination = nbt.getBoolean("HideTargetDestination");
        this.requiresClientUpdate = GCCoreUtil.getEffectiveSide() == LogicalSide.SERVER;
        chunkSet.clear();
        ListNBT list = nbt.getList("LoaderChunkSet", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND);
        for (INBT element : list)
        {
            chunkSet.add(new ChunkPos(((LongNBT) element).getLong()));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putString("OwnerName", this.ownerUUID.toString());
        nbt.putInt("LaunchSelection", this.launchDropdownSelection);
        nbt.putInt("ControllerFrequency", this.frequency);
        nbt.putInt("TargetFrequency", this.destFrequency);
        nbt.putBoolean("LaunchPadRemovalDisabled", this.launchPadRemovalDisabled);
        nbt.putBoolean("LaunchPadSchedulingEnabled", this.launchSchedulingEnabled);
        nbt.putBoolean("HideTargetDestination", this.hideTargetDestination);
        ListNBT list = new ListNBT();
        for (ChunkPos pos : chunkSet)
        {
            list.add(LongNBT.valueOf(pos.asLong()));
        }
        nbt.put("LoaderChunkSet", list);
        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return slotID == 0;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                this.disableCooldown = 10;
                break;
            case 1:
                this.launchSchedulingEnabled = disabled;
                break;
            case 2:
                this.hideTargetDestination = disabled;
                this.disableCooldown = 10;
                break;
            }
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        case 1:
            return this.launchSchedulingEnabled;
        case 2:
            return this.hideTargetDestination;
        }

        return true;
    }

    @Override
    public boolean canAttachToLandingPad(IWorldReader world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        return tile instanceof TileEntityLandingPad;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;

        if (this.frequency >= 0)
        {
            this.frequencyValid = true;
            Iterable<ServerWorld> servers = GCCoreUtil.getWorldServerList(this.world);

            worldLoop:
            for (ServerWorld world : servers)
            {
                for (TileEntity tile2 : new ArrayList<TileEntity>(world.loadedTileEntityList))
                {
                    if (this != tile2)
                    {
                        tile2 = world.getTileEntity(tile2.getPos());
                        if (tile2 == null)
                        {
                            continue;
                        }

                        if (tile2 instanceof TileEntityLaunchController)
                        {
                            TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                            if (launchController2.frequency == this.frequency)
                            {
                                GCLog.debug("Launch Controller frequency conflict at " + tile2.getPos() + " on dim: " + GCCoreUtil.getDimensionType(tile2));
                                this.frequencyValid = false;
                                break worldLoop;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            this.frequencyValid = false;
        }
    }

    public void setDestinationFrequency(int frequency)
    {
        if (frequency != this.destFrequency)
        {
            this.destFrequency = frequency;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void checkDestFrequencyValid()
    {
        if (!this.world.isRemote)
        {
            this.destFrequencyValid = false;
            if (this.destFrequency >= 0)
            {
                Iterable<ServerWorld> servers = GCCoreUtil.getWorldServerList(this.world);
                for (ServerWorld world : servers)
                {
                    for (TileEntity tile2 : new ArrayList<TileEntity>(world.loadedTileEntityList))
                    {
                        if (this != tile2)
                        {
                            tile2 = world.getTileEntity(tile2.getPos());
                            if (tile2 == null)
                            {
                                continue;
                            }

                            if (tile2 instanceof TileEntityLaunchController)
                            {
                                TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                                if (launchController2.frequency == this.destFrequency)
                                {
                                    this.destFrequencyValid = true;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean validFrequency()
    {
        this.checkDestFrequencyValid();
        return !this.getDisabled(0) && this.hasEnoughEnergyToRun && this.frequencyValid && this.destFrequencyValid;
    }

    public void setLaunchDropdownSelection(int newvalue)
    {
        if (newvalue != this.launchDropdownSelection)
        {
            this.launchDropdownSelection = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void setLaunchSchedulingEnabled(boolean newvalue)
    {
        if (newvalue != this.launchSchedulingEnabled)
        {
            this.launchSchedulingEnabled = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void updateRocketOnDockSettings()
    {
        if (this.attachedDock instanceof TileEntityLandingPad)
        {
            TileEntityLandingPad pad = ((TileEntityLandingPad) this.attachedDock);
            IDockable rocket = pad.getDockedEntity();
            if (rocket instanceof EntityAutoRocket)
            {
                ((EntityAutoRocket) rocket).updateControllerSettings(pad);
            }
        }
    }

    public void setAttachedPad(IFuelDock pad)
    {
        this.attachedDock = pad;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        return state.get(BlockLaunchController.FACING);
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    private void releaseChunkTickets(@Nonnull World world)
    {
        releaseChunkTickets(world, prevPos);
    }

    private Set<ChunkPos> getChunkSet()
    {
        int chunkXMin = (pos.getX() - 2) >> 4;
        int chunkXMax = (pos.getX() + 2) >> 4;
        int chunkZMin = (pos.getX() - 2) >> 4;
        int chunkZMax = (pos.getZ() + 2) >> 4;
        Set<ChunkPos> set = new ObjectOpenHashSet<>();
        for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++)
        {
            for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++)
            {
                set.add(new ChunkPos(chunkX, chunkZ));
            }
        }
        return set;
    }

    private void releaseChunkTickets(@Nonnull World world, @Nullable BlockPos pos)
    {
        ServerChunkProvider chunkProvider = (ServerChunkProvider) world.getChunkProvider();
        Iterator<ChunkPos> chunkIt = chunkSet.iterator();
//        ChunkManager manager = ((ServerWorld) world).getChunkProvider().chunkManager;
        while (chunkIt.hasNext())
        {
            ChunkPos chunkPos = chunkIt.next();
//            if (pos != null) {
//                manager.deregisterChunk(chunkPos, pos);
//            }
            chunkProvider.releaseTicket(TICKET_TYPE, chunkPos, TICKET_DISTANCE, this);
            chunkIt.remove();
        }
        this.hasRegistered = false;
        this.prevWorld = null;
    }

    private void registerChunkTickets(@Nonnull World world)
    {
        ServerChunkProvider chunkProvider = (ServerChunkProvider) world.getChunkProvider();
//        ChunkManager manager = ChunkManager.getInstance((ServerWorld) world);

        prevPos = this.getPos();
        prevWorld = world;

        for (ChunkPos chunkPos : this.getChunkSet())
        {
            chunkProvider.registerTicket(TICKET_TYPE, chunkPos, TICKET_DISTANCE, this);
//            manager.registerChunk(chunkPos, prevPos);
            chunkSet.add(chunkPos);
        }

        hasRegistered = true;
    }

    /**
     * Release and re-register tickets, call when chunkset changes
     */
    public void refreshChunkTickets()
    {
        if (prevWorld != null)
        {
            releaseChunkTickets(prevWorld);
        }
        if (!world.isRemote())
        {
            registerChunkTickets(Objects.requireNonNull(world.getWorld()));
        }
    }
}
