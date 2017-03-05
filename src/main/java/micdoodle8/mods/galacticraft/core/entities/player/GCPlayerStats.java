//package micdoodle8.mods.galacticraft.core.entities.player;
//
//import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
//import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
//import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.GCLog;
//import micdoodle8.mods.galacticraft.core.util.WorldUtil;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTBase;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.TextComponentString;
//import net.minecraft.world.World;
//import net.minecraftforge.common.IExtendedEntityProperties;
//import net.minecraftforge.common.capabilities.Capability;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class GCPlayerStats implements Capability.IStorage<IStatsCapability>
//{
//    public static final ResourceLocation GC_PLAYER_PROP = new ResourceLocation(Constants.ASSET_PREFIX, "player_stats");
//
//
//    public GCPlayerStats(EntityPlayerMP player)
//    {
//        this.player = new WeakReference<EntityPlayerMP>(player);
//    }
//
//    @Override
//    public NBTBase writeNBT(Capability<IStatsCapability> capability, IStatsCapability instance, EnumFacing side)
//    {
//        NBTTagCompound nbt = new NBTTagCompound();
//        instance.saveNBTData(nbt);
//        return nbt;
//    }
//
//    @Override
//    public void readNBT(Capability<IStatsCapability> capability, IStatsCapability instance, EnumFacing side, NBTBase nbt)
//    {
//        instance.loadNBTData((NBTTagCompound) nbt);
//    }
//
//    @Override
//    public void init(Entity entity, World world)
//    {
//    }
//
//    public static void register(EntityPlayerMP player)
//    {
//        player.registerExtendedProperties(GCPlayerStats.GC_PLAYER_PROP, new GCPlayerStats(player));
//    }
//
//    public static GCPlayerStats get(EntityPlayerMP player)
//    {
//        return (GCPlayerStats) player.getExtendedProperties(GCPlayerStats.GC_PLAYER_PROP);
//    }
//
//    public static void tryBedWarning(EntityPlayerMP player)
//    {
//        final GCPlayerStats GCPlayer = GCPlayerStats.get(player);
//        if (!GCPlayer.receivedBedWarning)
//        {
//            player.addChatMessage(new TextComponentString(GCCoreUtil.translate("gui.bed_fail.message")));
//            GCPlayer.receivedBedWarning = true;
//        }
//    }
////
////    public void copyFrom(GCPlayerStats oldData, boolean keepInv)
////    {
////        if (keepInv)
////        {
////            this.extendedInventory.copyInventory(oldData.extendedInventory);
////        }
////
////        this.spaceStationDimensionData = oldData.spaceStationDimensionData;
////        this.unlockedSchematics = oldData.unlockedSchematics;
////        this.receivedSoundWarning = oldData.receivedSoundWarning;
////        this.receivedBedWarning = oldData.receivedBedWarning;
////        this.openedSpaceRaceManager = oldData.openedSpaceRaceManager;
////        this.spaceRaceInviteTeamID = oldData.spaceRaceInviteTeamID;
////        this.buildFlags = oldData.buildFlags;
////        this.astroMinerCount = oldData.astroMinerCount;
////        this.sentFlags = false;
////    }
//
//    public void startAdventure(String worldName)
//    {
//        this.startDimension = worldName;
//    }
//}
