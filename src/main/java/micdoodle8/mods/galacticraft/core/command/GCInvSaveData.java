//package micdoodle8.mods.galacticraft.core.command;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.nbt.ListNBT;
//import net.minecraft.world.storage.WorldSavedData;
//
//public class GCInvSaveData extends WorldSavedData
//{
//    public static final String SAVE_ID = Constants.GCDATAFOLDER + "GCInv_savefile";
//
//    public GCInvSaveData()
//    {
//        super(SAVE_ID);
//    }
//
//    public GCInvSaveData(String name)
//    {
//        super(name);
//    }
//
//    @Override
//    public void readFromNBT(CompoundNBT filedata)
//    {
//        for (Object obj : filedata.getKeySet())
//        {
//            if (obj instanceof ListNBT)
//            {
//                ListNBT entry = (ListNBT) obj;
//                String name = entry.toString(); // TODO See if this is equivilent to 1.6's getName function
//                ItemStack[] saveinv = new ItemStack[6];
//                if (entry.size() > 0)
//                {
//                    for (int j = 0; j < entry.size(); j++)
//                    {
//                        CompoundNBT obj1 = entry.getCompound(j);
//
//                        if (obj1 != null)
//                        {
//                            int i = obj1.getByte("Slot") & 7;
//                            if (i >= 6)
//                            {
//                                System.out.println("GCInv error retrieving savefile: slot was outside range 0-5");
//                                return;
//                            }
//                            saveinv[i] = new ItemStack(obj1);
//                        }
//                    }
//                }
//                CommandGCInv.savedata.put(name.toLowerCase(), saveinv);
//            }
//        }
//    }
//
//    @Override
//    public CompoundNBT writeToNBT(CompoundNBT toSave)
//    {
//        for (String name : CommandGCInv.savedata.keySet())
//        {
//            ListNBT par1NBTTagList = new ListNBT();
//            ItemStack[] saveinv = CommandGCInv.savedata.get(name);
//
//            for (int i = 0; i < 6; i++)
//            {
//                if (saveinv[i] != null)
//                {
//                    CompoundNBT nbttagcompound = new CompoundNBT();
//                    nbttagcompound.setByte("Slot", (byte) (i + 200));
//                    saveinv[i].writeToNBT(nbttagcompound);
//                    par1NBTTagList.add(nbttagcompound);
//                }
//            }
//            toSave.setTag(name, par1NBTTagList);
//        }
//
//        return toSave;
//    }
//}
