package micdoodle8.mods.galacticraft.core.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreInventoryExtended;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;

public class GCCoreCommandGCInv extends CommandBase
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
		if (GCCoreCommandGCInv.firstuse)
		{
			GCCoreCommandGCInv.firstuse = false;
			GCCoreCommandGCInv.initialise();
		}

		if (astring.length == 2)
		{
			try
			{
				GCCorePlayerMP thePlayer = null;
				thePlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(astring[1], true);
				if (thePlayer != null && !thePlayer.isDead && thePlayer.worldObj != null)
				{

					if (astring[0].equalsIgnoreCase("drop"))
					{
						GCCoreInventoryExtended gcInventory = thePlayer.getExtendedInventory();
						gcInventory.dropExtendedItems(thePlayer);
					}
					else

					if (astring[0].equalsIgnoreCase("save"))
					{
						GCCoreInventoryExtended gcInventory = thePlayer.getExtendedInventory();
						ItemStack[] saveinv = new ItemStack[gcInventory.getSizeInventory()];
						for (int i = 0; i < gcInventory.getSizeInventory(); i++)
						{
							saveinv[i] = gcInventory.getStackInSlot(i);
							gcInventory.setInventorySlotContents(i, null);
						}

						GCCoreCommandGCInv.savedata.put(astring[1].toLowerCase(), saveinv);
						GCCoreCommandGCInv.dontload.add(astring[1].toLowerCase());
						GCCoreCommandGCInv.writefile();
						System.out.println("[GCInv] Saving and clearing GC inventory slots of " + thePlayer.username);
					}
					else

					if (astring[0].equalsIgnoreCase("restore"))
					{
						ItemStack[] saveinv = GCCoreCommandGCInv.savedata.get(astring[1].toLowerCase());
						GCCoreCommandGCInv.dontload.remove(astring[1].toLowerCase());
						if (saveinv == null)
						{
							System.out.println("[GCInv] Tried to restore but player " + thePlayer.username + " had no saved GC inventory items.");
							return;
						}

						GCCoreCommandGCInv.doLoad(thePlayer);
					}
					else

					if (astring[0].equalsIgnoreCase("clear"))
					{
						GCCoreInventoryExtended gcInventory = thePlayer.getExtendedInventory();
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
						ItemStack[] saveinv = GCCoreCommandGCInv.savedata.get(astring[1].toLowerCase());
						if (saveinv != null)
						{
							System.out.println("[GCInv] Restore command for offline player " + astring[1] + ", setting to restore GCInv on next login.");
							GCCoreCommandGCInv.dontload.remove(astring[1].toLowerCase()); // Now
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

	public static void doLoad(GCCorePlayerMP thePlayer)
	{
		String theName = thePlayer.username.toLowerCase();
		if (!GCCoreCommandGCInv.dontload.contains(theName)) // This is a simple
															// flag: if the
															// playername is in
															// dontload then no
															// restore command
															// has yet been run.
		// Dontload resets to nothing on server restart so that all will
		// auto-restore on a server restart.
		{
			ItemStack[] saveinv = GCCoreCommandGCInv.savedata.get(theName);
			GCCoreInventoryExtended gcInventory = thePlayer.getExtendedInventory();
			for (int i = 0; i < gcInventory.getSizeInventory(); i++)
			{
				gcInventory.setInventorySlotContents(i, saveinv[i]);
			}
			GCCoreCommandGCInv.savedata.remove(theName);
			GCCoreCommandGCInv.writefile();
			System.out.println("[GCInv] Restored GC inventory slots of " + thePlayer.username);

		}
		else
		{
			System.out.println("[GCInv] Player " + thePlayer.username + " was spawned without restoring the GCInv save.  Run /gcinv restore playername to restore it.");
		}
	}

	private static void writefile()
	{
		GCCoreCommandGCInv.savefile.writeToNBT(new NBTTagCompound());
		GCCoreCommandGCInv.savefile.markDirty();
	}

	private static void initialise()
	{
		GCCoreCommandGCInv.world0 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(0);
		GCCoreCommandGCInv.savefile = (GCInvSaveData) GCCoreCommandGCInv.world0.loadItemData(GCInvSaveData.class, "GCInv_savefile");
		if (GCCoreCommandGCInv.savefile == null)
		{
			GCCoreCommandGCInv.savefile = new GCInvSaveData("GCInv_savefile");
			GCCoreCommandGCInv.world0.setItemData("GCInv_savefile", GCCoreCommandGCInv.savefile);
		}
	}

	public static ItemStack[] getSaveData(String p)
	{
		if (GCCoreCommandGCInv.firstuse)
		{
			GCCoreCommandGCInv.firstuse = false;
			GCCoreCommandGCInv.initialise();
		}

		return GCCoreCommandGCInv.savedata.get(p);
	}
}
