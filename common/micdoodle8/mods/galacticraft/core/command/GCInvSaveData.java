package micdoodle8.mods.galacticraft.core.command;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldSavedData;

public class GCInvSaveData extends WorldSavedData
{
	public GCInvSaveData(String par1Str)
	{
		super(par1Str);
	}

	@Override
	public void readFromNBT(NBTTagCompound filedata)
	{
		for (Object obj : filedata.getTags())
		{
			if (obj instanceof NBTTagList)
			{
				NBTTagList entry = (NBTTagList) obj;
				String name = entry.getName();
				ItemStack[] saveinv = new ItemStack[6];
				if (entry.tagCount() > 0)
				{
					for (Object obj1 : entry.tagList)
					{
						if (obj1 instanceof NBTTagCompound)
						{
							NBTTagCompound dat = (NBTTagCompound) obj1;
							int i = (dat.getByte("Slot")) & 7;
							if (i >= 6)
							{
								System.out.println("GCInv error retrieving savefile: slot was outside range 0-5");
								return;
							}
							saveinv[i] = ItemStack.loadItemStackFromNBT(dat);
						}
					}
				}
				GCCoreCommandGCInv.savedata.put(name.toLowerCase(), saveinv);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound toSave)
	{
		for (String name : GCCoreCommandGCInv.savedata.keySet())
		{
			NBTTagList par1NBTTagList = new NBTTagList();
			ItemStack[] saveinv = GCCoreCommandGCInv.savedata.get(name);

			for (int i = 0; i < 6; i++)
			{
				if (saveinv[i] != null)
				{
					NBTTagCompound nbttagcompound = new NBTTagCompound();
					nbttagcompound.setByte("Slot", (byte) (i + 200));
					saveinv[i].writeToNBT(nbttagcompound);
					par1NBTTagList.appendTag(nbttagcompound);
				}
			}
			toSave.setTag(name, par1NBTTagList);
		}
	}
}
