package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.tile.TileEntityInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import java.util.Random;

public class TileEntityCrashedProbe extends TileEntityInventory
{
    private boolean hasCoreToDrop;
    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    public TileEntityCrashedProbe()
    {
        super("container.crashed_probe.name");
        inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    }

    protected void fillWithLoot(EntityPlayer player)
    {
        if (this.lootTable != null)
        {
            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
            this.lootTable = null;
            Random random;

            if (this.lootTableSeed == 0L)
            {
                random = new Random();
            }
            else
            {
                random = new Random(this.lootTableSeed);
            }

            LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);

            if (player != null)
            {
                lootcontext$builder.withLuck(player.getLuck());
            }

            loottable.fillInventory(this, random, lootcontext$builder.build());
        }
    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        if (nbt.hasKey("ctd"))
        {
            this.hasCoreToDrop = nbt.getBoolean("ctd");
        }
        else
        {
            this.hasCoreToDrop = true;   //Legacy compatibility with worlds generated before this key used
        }

        if (!this.checkLootAndRead(nbt))
        {
            this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(nbt, this.getInventory());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("ctd", this.hasCoreToDrop);

        if (!this.checkLootAndWrite(nbt))
        {
            ItemStackHelper.saveAllItems(nbt, this.getInventory());
        }
        return nbt;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
    	this.fillWithLoot(null);
    	return super.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        this.fillWithLoot(null);
        return super.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        this.fillWithLoot(null);
        return super.removeStackFromSlot(index);
    }

    @Override
    public boolean isEmpty()
    {
        this.fillWithLoot(null);
        return super.isEmpty();
    }

    @Override
    public void clear()
    {
        this.fillWithLoot(null);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    public void setDropCore()
    {
        this.hasCoreToDrop = true;
    }

    public boolean getDropCore()
    {
        return this.hasCoreToDrop;
    }

    protected boolean checkLootAndRead(NBTTagCompound compound)
    {
        if (compound.hasKey("LootTable", 8))
        {
            this.lootTable = new ResourceLocation(compound.getString("LootTable"));
            this.lootTableSeed = compound.getLong("LootTableSeed");
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean checkLootAndWrite(NBTTagCompound compound)
    {
        if (this.lootTable != null)
        {
            compound.setString("LootTable", this.lootTable.toString());

            if (this.lootTableSeed != 0L)
            {
                compound.setLong("LootTableSeed", this.lootTableSeed);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void setLootTable(ResourceLocation lootTable, long lootTableSeed)
    {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
    }
}
