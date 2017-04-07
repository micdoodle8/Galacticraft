package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.PersistantInventoryCrafting;
import micdoodle8.mods.galacticraft.core.network.PacketDynamicInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class TileEntityCrafting extends TileEntity implements IInventory
{
    public PersistantInventoryCrafting craftMatrix = new PersistantInventoryCrafting();
    public ItemStack[] memory = new ItemStack[9];

    public TileEntityCrafting()
    {
    }

    @Override
    public void validate()
    {
        super.validate();

        if (this.worldObj != null && this.worldObj.isRemote)
        {
            //Request size + contents information from server
            GalacticraftCore.packetPipeline.sendToServer(new PacketDynamicInventory(this));
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public void updateMemory()
    {
        if (CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.getWorld()) == null) return;
        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = this.craftMatrix.getStackInSlot(i);
            if (stack == null)
            {
                this.memory[i] = null;
            }
            else
            {
                ItemStack stack2 = stack.copy();
                stack2.stackSize = 0;
                this.memory[i] = stack2;
            }
        }
    }
    
    public ItemStack getMemory(int i)
    {
        return this.memory[i];
    }

    @Override
    public int getSizeInventory()
    {
        return this.craftMatrix.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.craftMatrix.getStackInSlot(par1);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (par1 >= 0)
        {
            ItemStack result = this.craftMatrix.decrStackSize(par1, par2);
            if (result != null)
            {
                this.updateMemory();
            }
            return result;
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (par1 >= 0)
        {
            return this.craftMatrix.removeStackFromSlot(par1);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (par1 >= 0)
        {
            this.craftMatrix.setInventorySlotContents(par1, par2ItemStack);
            this.updateMemory();
        }
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("container.magneticcrafting.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList contents = nbt.getTagList("Items", 10);
        if (contents != null && contents.tagCount() > 0)
        {
            for (int i = 0; i < contents.tagCount(); ++i)
            {
                NBTTagCompound var4 = contents.getCompoundTagAt(i);
                int slot = var4.getByte("Slot") & 255;

                if (slot < 9)
                {
                    this.craftMatrix.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(var4));
                }
                else if (slot < 18)
                {
                    this.memory[slot - 9] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList var2 = new NBTTagList();
        for (int i = 0; i < 9; ++i)
        {
            if (this.craftMatrix.getStackInSlot(i) != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i));
                this.craftMatrix.getStackInSlot(i).writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        for (int i = 0; i < 9; ++i)
        {
            if (this.memory[i] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) (i + 9));
                this.memory[i].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);
        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }
}
