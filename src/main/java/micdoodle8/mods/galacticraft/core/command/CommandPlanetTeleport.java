package micdoodle8.mods.galacticraft.core.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;

public class CommandPlanetTeleport
{
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalcommandnode = dispatcher.register(Commands.literal("dimensiontp").requires((src) -> src.hasPermissionLevel(2)).executes((ctx) -> {
            ServerPlayerEntity player = ctx.getSource().asPlayer();
            return teleport(ImmutableList.of(player), ctx.getSource().getWorld());
        }).then(Commands.argument("targets", EntityArgument.entities()).executes((ctx) -> {
            return teleport(EntityArgument.getPlayers(ctx, "targets"), ctx.getSource().getWorld());
        })));
    }

    private static int teleport(Collection<ServerPlayerEntity> targets, ServerWorld world)
    {
        for (ServerPlayerEntity target : targets)
        {
//        ServerWorld worldserver = server.getWorld(GCCoreUtil.getDimensionID(server.worlds[0]));
            BlockPos spawnPoint = world.getSpawnPoint();
            GCPlayerStats stats = GCPlayerStats.get(target);
            stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
            stats.setRocketItem(EntityTier1Rocket.getItemFromType(IRocketType.EnumRocketType.DEFAULT));
//        stats.setRocketItem(GCItems.rocketTierOne);
            stats.setFuelLevel(1000);
            stats.setCoordsTeleportedFromX(spawnPoint.getX());
            stats.setCoordsTeleportedFromZ(spawnPoint.getZ());

            try
            {
                WorldUtil.toCelestialSelection(target, stats, Integer.MAX_VALUE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw e;
            }
        }

        return targets.size();
    }

//    @Override
//    public String getUsage(ICommandSender var1)
//    {
//        return "/" + this.getName() + " [<player>]";
//    }
//
//    @Override
//    public int getRequiredPermissionLevel()
//    {
//        return 2;
//    }
//
//    @Override
//    public String getName()
//    {
//        return "dimensiontp";
//    }
//
//    @Override
//    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
//    {
//        ServerPlayerEntity playerBase = null;
//
//        if (args.length < 2)
//        {
//            try
//            {
//                if (args.length == 1)
//                {
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
//                }
//                else
//                {
//                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
//                }
//
//                if (playerBase != null)
//                {
//                    ServerWorld worldserver = server.getWorld(GCCoreUtil.getDimensionID(server.worlds[0]));
//                    BlockPos spawnPoint = worldserver.getSpawnPoint();
//                    GCPlayerStats stats = GCPlayerStats.get(playerBase);
//                    stats.setRocketStacks(NonNullList.withSize(2, ItemStack.EMPTY));
//                    stats.setRocketType(IRocketType.EnumRocketType.DEFAULT);
//                    stats.setRocketItem(GCItems.rocketTierOne);
//                    stats.setFuelLevel(1000);
//                    stats.setCoordsTeleportedFromX(spawnPoint.getX());
//                    stats.setCoordsTeleportedFromZ(spawnPoint.getZ());
//
//                    try
//                    {
//                        WorldUtil.toCelestialSelection(playerBase, stats, Integer.MAX_VALUE);
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                        throw e;
//                    }
//
//                    CommandBase.notifyCommandListener(sender, this, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getName()), "]" });
//                }
//                else
//                {
//                    throw new Exception("Could not find player with name: " + args[0]);
//                }
//            }
//            catch (final Exception var6)
//            {
//                throw new CommandException(var6.getMessage(), new Object[0]);
//            }
//        }
//        else
//        {
//            throw new WrongUsageException(GCCoreUtil.translateWithFormat("commands.dimensiontp.too_many", this.getUsage(sender)), new Object[0]);
//        }
//    }
}
