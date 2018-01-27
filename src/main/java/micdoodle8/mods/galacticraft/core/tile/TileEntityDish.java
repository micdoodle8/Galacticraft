package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class TileEntityDish extends TileBaseUniversalElectrical implements IMultiBlock, IDisableableMachine, IInventoryDefaults, ISidedInventory, IConnector
{
    public float targetAngle;
    public float currentAngle;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;
    private ItemStack[] containingItems = new ItemStack[1];

    private boolean initialised = false;
    private AxisAlignedBB renderAABB;

    public TileEntityDish()
    {
        this.storage.setMaxExtract(100);
        this.setTierGC(1);
    }
    
    public float rotation(float partial)
    {
        return (this.ticks + partial) / 12;
    }

    public float elevation(float partial)
    {
        return (MathHelper.sin(rotation(partial) / 40) + 1.0F) * 22.5F;
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.initialised)
        {
            this.initialised = this.initialiseMultiTiles(this.getPos(), this.worldObj);
        }

        if (!this.worldObj.isRemote)
        {
            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }
        }

        float angle = this.worldObj.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

        celestialAngle %= 360;

        {
            if (celestialAngle > 30 && celestialAngle < 150)
            {
                float difference = this.targetAngle - celestialAngle;

                this.targetAngle -= difference / 20.0F;
            }
            else if (!this.worldObj.isDaytime() || this.worldObj.isRaining() || this.worldObj.isThundering())
            {
                this.targetAngle = 77.5F + 180.0F;
            }
            else if (celestialAngle < 50)
            {
                this.targetAngle = 50;
            }
            else if (celestialAngle > 150)
            {
                this.targetAngle = 150;
            }
        }

        float difference = this.targetAngle - this.currentAngle;

        this.currentAngle += difference / 20.0F;
    }

    protected boolean initialiseMultiTiles(BlockPos pos, World world)
    {
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isRemote) this.onCreate(world, pos);
        
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityMulti)
            {
                ((TileEntityMulti) tile).mainBlockPosition = pos;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        return this.getBlockType().onBlockActivated(this.worldObj, this.getPos(), this.worldObj.getBlockState(this.getPos()), entityPlayer, EnumFacing.DOWN, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return EnumBlockMultiType.DISH_LARGE;
    }
    
    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.worldObj.getHeight() - 1;
        int y = placedPosition.getY();

        if (++y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

        if (++y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

        if (++y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));

        if (++y > buildHeight)
        {
            return;
        }
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }

        if (++y > buildHeight)
        {
            return;
        }
        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }

        if (++y > buildHeight)
        {
            return;
        }
        for (int x = -3; x < 4; x++)
        {
            for (int z = -3; z < 4; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 6) continue;
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }

        if (++y > buildHeight)
        {
            return;
        }
        for (int x = -3; x < 4; x++)
        {
            for (int z = -3; z < 4; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 6) continue;
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }

        if (++y > buildHeight)
        {
            return;
        }
        for (int x = -2; x < 3; x++)
        {
            for (int z = -2; z < 3; z++)
            {
                if (Math.abs(x) + Math.abs(z) == 4) continue;
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }

    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock && (EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE) == EnumBlockMultiType.DISH_LARGE)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
                }
                this.worldObj.setBlockToAir(pos);
            }
        }
        this.worldObj.destroyBlock(thisBlock, true);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.storage.setCapacity(nbt.getFloat("maxEnergy"));
        this.currentAngle = nbt.getFloat("currentAngle");
        this.targetAngle = nbt.getFloat("targetAngle");
        this.setDisabled(0, nbt.getBoolean("disabled"));
        this.disableCooldown = nbt.getInteger("disabledCooldown");

        final NBTTagList var2 = nbt.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.initialised = false;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setFloat("maxEnergy", this.getMaxEnergyStoredGC());
        nbt.setFloat("currentAngle", this.currentAngle);
        nbt.setFloat("targetAngle", this.targetAngle);
        nbt.setInteger("disabledCooldown", this.disableCooldown);
        nbt.setBoolean("disabled", this.getDisabled(0));

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        nbt.setTag("Items", list);
    }

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos.add(-3, 0, -3), pos.add(3, 8, 3));
        }
        return this.renderAABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_MEDIUM;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public String getName()
    {
        //TODO
        return "";
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 20;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    public int getScaledElecticalLevel(int i)
    {
        return (int) Math.floor(this.getEnergyStoredGC() * i / this.getMaxEnergyStoredGC());
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
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
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }
}
