package micdoodle8.mods.galacticraft.core.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.InventoryExtended;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class CommandGCInv extends CommandBase
{
	protected static final Map<String, ItemStack[]> savedata = new HashMap<String, ItemStack[]>();
	private static final Set<String> dontload = new HashSet<String>();
	private static boolean firstuse = true;
	private static World world0;
	private static GCInvSaveData savefile;

	@Override
	public String getCommandUsage(ICommandSender var1)
	{
		return "/" + this.getCommandName() + " [save/restore/drop/clear] playername";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
	{
		return true;
	}

	@Override
	public String getCommandName()
	{
		return "gcinv";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if (CommandGCInv.firstuse)
		{
			CommandGCInv.firstuse = false;
			CommandGCInv.initialise();
		}

		if (astring.length == 2)
		{
			try
			{
				GCEntityPlayerMP thePlayer = null;
				thePlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(astring[1], true);
				if (thePlayer != null && !thePlayer.isDead && thePlayer.worldObj != null)
				{

					if (astring[0].equalsIgnoreCase("drop"))
					{
						InventoryExtended gcInventory = thePlayer.getExtendedInventory();
						gcInventory.dropExtendedItems(thePlayer);
					}
					else

					if (astring[0].equalsIgnoreCase("save"))
					{
						InventoryExtended gcInventory = thePlayer.getExtendedInventory();
						ItemStack[] saveinv = new ItemStack[gcInventory.getSizeInventory()];
						for (int i = 0; i < gcInventory.getSizeInventory(); i++)
						{
							saveinv[i] = gcInventory.getStackInSlot(i);
							gcInventory.setInventorySlotContents(i, null);
						}

						CommandGCInv.savedata.put(astring[1].toLowerCase(), saveinv);
						CommandGCInv.dontload.add(astring[1].toLowerCase());
						CommandGCInv.writefile();
						System.out.println("[GCInv] Saving and clearing GC inventory slots of " + thePlayer.getGameProfile().getName());
					}
					else

					if (astring[0].equalsIgnoreCase("restore"))
					{
						ItemStack[] saveinv = CommandGCInv.savedata.get(astring[1].toLowerCase());
						CommandGCInv.dontload.remove(astring[1].toLowerCase());
						if (saveinv == null)
						{
							System.out.println("[GCInv] Tried to restore but player " + thePlayer.getGameProfile().getName() + " had no saved GC inventory items.");
							return;
						}

						CommandGCInv.doLoad(thePlayer);
					}
					else

					if (astring[0].equalsIgnoreCase("clear"))
					{
						InventoryExtended gcInventory = thePlayer.getExtendedInventory();
						for (int i = 0; i < gcInventory.getSizeInventory(); i++)
						{
							gcInventory.setInventorySlotContents(i, null);
						}
					}
					else
					{
						throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
					}
				}
				else
				{
					// Special rule for 'restore' command if player not found -
					// look to see if the player is offline (i.e. had a saved
					// inventory already)
					if (astring[0].equalsIgnoreCase("restore"))
					{
						ItemStack[] saveinv = CommandGCInv.savedata.get(astring[1].toLowerCase());
						if (saveinv != null)
						{
							System.out.println("[GCInv] Restore command for offline player " + astring[1] + ", setting to restore GCInv on next login.");
							CommandGCInv.dontload.remove(astring[1].toLowerCase()); // Now
																							// it
																							// can
																							// autoload
																							// on
																							// next
																							// player
																							// logon
							return;
						}
					}

					// No player found, and not a 'restore' command
					if (astring[0].equalsIgnoreCase("clear") || astring[0].equalsIgnoreCase("save") || astring[0].equalsIgnoreCase("drop"))
					{
						System.out.println("GCInv command: player " + astring[1] + " not found.");
					}
					else
					{
						throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
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
			throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(icommandsender), new Object[0]);
		}
	}

	public static void doLoad(GCEntityPlayerMP thePlayer)
	{
		String theName = thePlayer.getGameProfile().getName().toLowerCase();
		if (!CommandGCInv.dontload.contains(theName)) // This is a simple
															// flag: if the
															// playername is in
															// dontload then no
															// restore command
															// has yet been run.
		// Dontload resets to nothing on server restart so that all will
		// auto-restore on a server restart.
		{
			ItemStack[] saveinv = CommandGCInv.savedata.get(theName);
			InventoryExtended gcInventory = thePlayer.getExtendedInventory();
			for (int i = 0; i < gcInventory.getSizeInventory(); i++)
			{
				gcInventory.setInventorySlotContents(i, saveinv[i]);
			}
			CommandGCInv.savedata.remove(theName);
			CommandGCInv.writefile();
			System.out.println("[GCInv] Restored GC inventory slots of " + thePlayer.getGameProfile().getName());

		}
		else
		{
			System.out.println("[GCInv] Player " + thePlayer.getGameProfile().getName() + " was spawned without restoring the GCInv save.  Run /gcinv restore playername to restore it.");
		}
	}

	private static void writefile()
	{
		CommandGCInv.savefile.writeToNBT(new NBTTagCompound());
		CommandGCInv.savefile.markDirty();
	}

	private static void initialise()
	{
		CommandGCInv.world0 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
		CommandGCInv.savefile = (GCInvSaveData) CommandGCInv.world0.loadItemData(GCInvSaveData.class, "GCInv_savefile");
		if (CommandGCInv.savefile == null)
		{
			CommandGCInv.savefile = new GCInvSaveData("GCInv_savefile");
			CommandGCInv.world0.setItemData("GCInv_savefile", CommandGCInv.savefile);
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
