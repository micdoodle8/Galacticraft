package micdoodle8.mods.galacticraft.core.command;

import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class CommandGCInv extends CommandBase
{
    protected static final Map<String, ItemStack[]> savedata = new HashMap<String, ItemStack[]>();
    private static final Set<String> dontload = new HashSet<String>();
    private static boolean firstuse = true;
    private static GCInvSaveData savefile;

    @Override
    public String getUsage(ICommandSender var1)
    {
        return "/" + this.getName() + " [save|restore|drop|clear] <playername>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getName()
    {
        return "gcinv";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "save", "restore", "drop", "clear");
        }
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (CommandGCInv.firstuse)
        {
            CommandGCInv.firstuse = false;
            CommandGCInv.initialise();
        }

        if (args.length == 2)
        {
            try
            {
                EntityPlayerMP thePlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[1], true);
                if (thePlayer != null && !thePlayer.isDead && thePlayer.world != null)
                {
                    GCPlayerStats stats = GCPlayerStats.get(thePlayer);

                    if (args[0].equalsIgnoreCase("drop"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        gcInventory.dropExtendedItems(thePlayer);
                    }
                    else if (args[0].equalsIgnoreCase("save"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        ItemStack[] saveinv = new ItemStack[gcInventory.getSizeInventory()];
                        for (int i = 0; i < gcInventory.getSizeInventory(); i++)
                        {
                            saveinv[i] = gcInventory.getStackInSlot(i);
                            gcInventory.setInventorySlotContents(i, ItemStack.EMPTY);
                        }

                        CommandGCInv.savedata.put(args[1].toLowerCase(), saveinv);
                        CommandGCInv.dontload.add(args[1].toLowerCase());
                        CommandGCInv.writefile();
                        System.out.println("[GCInv] Saving and clearing GC inventory slots of " + PlayerUtil.getName(thePlayer));
                    }
                    else if (args[0].equalsIgnoreCase("restore"))
                    {
                        ItemStack[] saveinv = CommandGCInv.savedata.get(args[1].toLowerCase());
                        CommandGCInv.dontload.remove(args[1].toLowerCase());
                        if (saveinv == null)
                        {
                            System.out.println("[GCInv] Tried to restore but player " + PlayerUtil.getName(thePlayer) + " had no saved GC inventory items.");
                            return;
                        }

                        CommandGCInv.doLoad(thePlayer);
                    }
                    else if (args[0].equalsIgnoreCase("clear"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        for (int i = 0; i < gcInventory.getSizeInventory(); i++)
                        {
                            gcInventory.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                    }
                    else
                    {
                        throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getUsage(sender));
                    }
                }
                else
                {
                    // Special rule for 'restore' command if player not found -
                    // look to see if the player is offline (i.e. had a saved
                    // inventory already)
                    if (args[0].equalsIgnoreCase("restore"))
                    {
                        ItemStack[] saveinv = CommandGCInv.savedata.get(args[1].toLowerCase());
                        if (saveinv != null)
                        {
                            System.out.println("[GCInv] Restore command for offline player " + args[1] + ", setting to restore GCInv on next login.");
                            CommandGCInv.dontload.remove(args[1].toLowerCase());
                            // Now it can autoload on next player logon
                            return;
                        }
                    }

                    // No player found, and not a 'restore' command
                    if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("drop"))
                    {
                        System.out.println("GCInv command: player " + args[1] + " not found.");
                    }
                    else
                    {
                        throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getUsage(sender), new Object[0]);
                    }
                }
            }
            catch (final Exception e)
            {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        else
        {
            throw new WrongUsageException("Not enough command arguments! Usage: " + this.getUsage(sender), new Object[0]);
        }
    }

    public static void doLoad(EntityPlayerMP thePlayer)
    {
        String theName = PlayerUtil.getName(thePlayer).toLowerCase();
        if (!CommandGCInv.dontload.contains(theName))
        // This is a simple flag: if the playername is in dontload then no
        // restore command has yet been run.
        // Dontload resets to nothing on server restart so that all will
        // auto-restore on a server restart.
        {
            ItemStack[] saveinv = CommandGCInv.savedata.get(theName);
            InventoryExtended gcInventory = GCPlayerStats.get(thePlayer).getExtendedInventory();
            for (int i = 0; i < gcInventory.getSizeInventory(); i++)
            {
                gcInventory.setInventorySlotContents(i, saveinv[i]);
            }
            CommandGCInv.savedata.remove(theName);
            CommandGCInv.writefile();
            System.out.println("[GCInv] Restored GC inventory slots of " + PlayerUtil.getName(thePlayer));

        }
        else
        {
            System.out.println("[GCInv] Player " + PlayerUtil.getName(thePlayer) + " was spawned without restoring the GCInv save.  Run /gcinv restore playername to restore it.");
        }
    }

    private static void writefile()
    {
        CommandGCInv.savefile.writeToNBT(new NBTTagCompound());
        CommandGCInv.savefile.markDirty();
    }

    private static void initialise()
    {
        World world0 = WorldUtil.getWorldForDimensionServer(0);
        if (world0 == null)
        {
            return;
        }
        CommandGCInv.savefile = (GCInvSaveData) world0.loadData(GCInvSaveData.class, GCInvSaveData.SAVE_ID);
        if (CommandGCInv.savefile == null)
        {
            CommandGCInv.savefile = new GCInvSaveData();
            world0.setData(GCInvSaveData.SAVE_ID, CommandGCInv.savefile);
        }
    }

    public static ItemStack[] getSaveData(String p)
    {
        if (CommandGCInv.firstuse)
        {
            CommandGCInv.firstuse = false;
            CommandGCInv.initialise();
        }

        return CommandGCInv.savedata.get(p);
    }
}
