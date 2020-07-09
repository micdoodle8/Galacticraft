package micdoodle8.mods.galacticraft.planets.venus.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityInventory;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

public class TileEntityCrashedProbe extends TileEntityInventory
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.crashedProbe)
    public static TileEntityType<TileEntityCrashedProbe> TYPE;

    private boolean hasCoreToDrop;
    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    public TileEntityCrashedProbe()
    {
        super(TYPE);
        inventory = NonNullList.withSize(6, ItemStack.EMPTY);
    }

//    protected void fillWithLoot(PlayerEntity player)
//    {
//        if (this.lootTable != null)
//        {
//            LootTable loottable = this.world.getLootTableManager().getLootTableFromLocation(this.lootTable);
//            this.lootTable = null;
//            Random random;
//
//            if (this.lootTableSeed == 0L)
//            {
//                random = new Random();
//            }
//            else
//            {
//                random = new Random(this.lootTableSeed);
//            }
//
//            LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld) this.world);
//
//            if (player != null)
//            {
//                lootcontext$builder.withLuck(player.getLuck());
//            }
//
//            loottable.fillInventory(this, random, lootcontext$builder.build());
//        }
//    }

    @Override
    protected boolean handleInventory()
    {
        return false;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        if (nbt.contains("ctd"))
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
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putBoolean("ctd", this.hasCoreToDrop);

        if (!this.checkLootAndWrite(nbt))
        {
            ItemStackHelper.saveAllItems(nbt, this.getInventory());
        }
        return nbt;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
//    	this.fillWithLoot(null);
    	return super.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
//        this.fillWithLoot(null);
        return super.decrStackSize(index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
//        this.fillWithLoot(null);
        return super.removeStackFromSlot(index);
    }

    @Override
    public boolean isEmpty()
    {
//        this.fillWithLoot(null);
        return super.isEmpty();
    }

    @Override
    public void clear()
    {
//        this.fillWithLoot(null);
    }

//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate)
//    {
//        return oldState.getBlock() != newSate.getBlock();
//    }

    public void setDropCore()
    {
        this.hasCoreToDrop = true;
    }

    public boolean getDropCore()
    {
        return this.hasCoreToDrop;
    }

    protected boolean checkLootAndRead(CompoundNBT compound)
    {
        if (compound.contains("LootTable", 8))
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

    protected boolean checkLootAndWrite(CompoundNBT compound)
    {
        if (this.lootTable != null)
        {
            compound.putString("LootTable", this.lootTable.toString());

            if (this.lootTableSeed != 0L)
            {
                compound.putLong("LootTableSeed", this.lootTableSeed);
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
