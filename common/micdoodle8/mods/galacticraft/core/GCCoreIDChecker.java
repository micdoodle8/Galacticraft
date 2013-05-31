//package micdoodle8.mods.galacticraft.core;
//
//import java.util.ArrayList;
//
//import micdoodle8.mods.galacticraft.core.items.GCCorePairedItemID;
//import micdoodle8.mods.galacticraft.core.items.GCCorePairedItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import cpw.mods.fml.common.FMLLog;
//
//public class GCCoreIDChecker
//{
//	public static ArrayList oldIDs = new ArrayList();
//
//	public static short checkID(NBTTagCompound nbt)
//	{
//		short id = nbt.getShort("id");
//
//		for (Object stackPair : oldIDs)
//		{
//			if (stackPair instanceof GCCorePairedItemStack)
//			{
//				if (((GCCorePairedItemStack) stackPair).stack1.itemID == id)
//				{
//					return (short) ((GCCorePairedItemStack) stackPair).stack2.itemID;
//				}
//			}
//			else if (stackPair instanceof GCCorePairedItemID)
//			{
//				if (((GCCorePairedItemID) stackPair).ID1 == id)
//				{
//					return (short) ((GCCorePairedItemID) stackPair).stack2.itemID;
//				}
//			}
//		}
//
//		return id;
//	}
//
//	public static short checkMeta(NBTTagCompound nbt)
//	{
//		short id = nbt.getShort("id");
//		short meta = nbt.getShort("Damage");
//
//		for (Object stackPair : oldIDs)
//		{
//			if (stackPair instanceof GCCorePairedItemStack)
//			{
//				if (((GCCorePairedItemStack) stackPair).stack1.getItemDamage() == meta)
//				{
//					return (short) ((GCCorePairedItemStack) stackPair).stack2.getItemDamage();
//				}
//			}
//			else if (stackPair instanceof GCCorePairedItemID)
//			{
//				if (((GCCorePairedItemID) stackPair).META1 == meta && ((GCCorePairedItemID) stackPair).ID1 == id)
//				{
//					return (short) ((GCCorePairedItemID) stackPair).stack2.getItemDamage();
//				}
//			}
//		}
//
//		return meta;
//	}
// }
