//package micdoodle8.mods.galacticraft.core.command;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import micdoodle8.mods.galacticraft.core.dimension.SpaceStationWorldData;
//import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.command.CommandSource;
//import net.minecraft.command.Commands;
//import net.minecraft.command.arguments.EntityArgument;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.StringTextComponent;
//
//import java.util.List;
//import java.util.Map;
//
//public class CommandSpaceStationAddOwner
//{
//    private static final int DEFAULT_RANGE = 200000;
//
//    public static void register(CommandDispatcher<CommandSource> dispatcher) {
//        dispatcher.register(
//                Commands.literal("ssinvite")
//                        .requires(s -> s.hasPermissionLevel(0))
//                        .then(Commands.argument("player", EntityArgument.player())
//                                .executes(CommandSpaceStationAddOwner::run))
//        );
//    }
//
//    private static int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException
//    {
//        PlayerEntity player = EntityArgument.getPlayer(ctx, "player");
//        GCPlayerStats stats = GCPlayerStats.get(player);
//        CommandSource source = ctx.getSource();
//
//        if (stats.getSpaceStationDimensionData().isEmpty())
//        {
//            source.sendErrorMessage(new StringTextComponent(GCCoreUtil.translate("commands.ssinvite.not_found")));
//        }
//        else
//        {
//            for (Map.Entry<Integer, Integer> ownedStations : stats.getSpaceStationDimensionData().entrySet())
//            {
//                final SpaceStationWorldData data = SpaceStationWorldData.getStationData(player.world, ownedStations.getValue(), player);
//
////                if (var3.equalsIgnoreCase("+all"))
////                {
////                    data.setAllowedAll(true);
////                    playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_true")));
////                    return;
////                }
////                if (var3.equalsIgnoreCase("-all"))
////                {
////                    data.setAllowedAll(false);
////                    playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_false", var3)));
////                    return;
////                } TODO
//
//                if (!data.getAllowedPlayers().contains(player.getUniqueID()))
//                {
//                    data.getAllowedPlayers().add(player.getUniqueID());
//                    data.markDirty();
//                }
//            }
//        }
//
//        return 1;
//    }
//
////    @Override
////    public String getUsage(ICommandSender var1)
////    {
////        return "/" + this.getName() + " [ <player> | +all | -all ]";
////    }
////
////    @Override
////    public int getRequiredPermissionLevel()
////    {
////        return 0;
////    }
////
////    @Override
////    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
////    {
////        return true;
////    }
////
////    @Override
////    public String getName()
////    {
////        return "ssinvite";
////    }
////
////    @Override
////    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
////    {
////        String var3 = null;
////        EntityPlayerMP playerBase = null;
////
////        if (args.length > 0)
////        {
////            var3 = args[0];
////
////            try
////            {
////                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
////
////                if (playerBase != null)
////                {
////                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
////
////                    if (stats.getSpaceStationDimensionData().isEmpty())
////                    {
////                        throw new WrongUsageException(GCCoreUtil.translate("commands.ssinvite.not_found"), new Object[0]);
////                    }
////                    else
////                    {
////                        for (Map.Entry<Integer, Integer> ownedStations : stats.getSpaceStationDimensionData().entrySet())
////                        {
////                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.world, ownedStations.getValue(), playerBase);
////
////                            if (var3.equalsIgnoreCase("+all"))
////                            {
////                                data.setAllowedAll(true);
////                                playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_true")));
////                                return;
////                            }
////                            if (var3.equalsIgnoreCase("-all"))
////                            {
////                                data.setAllowedAll(false);
////                                playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.allow_all_false", var3)));
////                                return;
////                            }
////
////                            if (!data.getAllowedPlayers().contains(var3))
////                            {
////                                data.getAllowedPlayers().add(var3);
////                                data.markDirty();
////                            }
////                        }
////                    }
////
////                    final EntityPlayerMP playerToAdd = PlayerUtil.getPlayerBaseServerFromPlayerUsername(var3, true);
////
////                    if (playerToAdd != null)
////                    {
////                        playerToAdd.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.added", PlayerUtil.getName(playerBase))));
////                    }
////                }
////            }
////            catch (final Exception var6)
////            {
////                throw new CommandException(var6.getMessage(), new Object[0]);
////            }
////
////        }
////        else
////        {
////            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.ssinvite.wrong_usage", this.getUsage(sender)), new Object[0]);
////        }
////
////        if (playerBase != null)
////        {
////            playerBase.sendMessage(new TextComponentString(GCCoreUtil.translateWithFormat("gui.spacestation.addsuccess", var3)));
////        }
////    }
////
////    @Override
////    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
////    {
////        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : null;
////    }
////
////    @Override
////    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
////    {
////        return par2 == 0;
////    }
//}
