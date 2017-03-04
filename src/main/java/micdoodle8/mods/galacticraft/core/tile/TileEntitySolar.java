package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.IDisableableMachine;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

public class TileEntitySolar extends TileBaseUniversalElectricalSource implements IMultiBlock, IPacketReceiver, IDisableableMachine, IInventory, ISidedInventory, IConnector
{
    @NetworkedField(targetSide = Side.CLIENT)
    public int solarStrength = 0;
    public float targetAngle;
    public float currentAngle;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;
    private ItemStack[] containingItems = new ItemStack[1];
    public static final int MAX_GENERATE_WATTS = 200;
    @NetworkedField(targetSide = Side.CLIENT)
    public int generateWatts = 0;

    private boolean initialised = false;

    public TileEntitySolar()
    {
        this(1);
    }

    /*
     * @param tier: 1 = Basic Solar  2 = Advanced Solar
     */
    public TileEntitySolar(int tier)
    {
        this.storage.setMaxExtract(TileEntitySolar.MAX_GENERATE_WATTS);
        this.storage.setMaxReceive(TileEntitySolar.MAX_GENERATE_WATTS);
        if (tier == 2)
        {
            this.storage.setCapacity(30000);
        }
        this.setTierGC(tier);
        this.initialised = true;
    }

    @Override
    public void update()
    {
        // TODO: Find a more efficient way to fix this
        //          Broken since 1.8 and this is an inefficient fix
        for (int y = 1; y < 3; y++)
        {
            final BlockPos vecToAdd = new BlockPos(getPos().getX(), getPos().getY() + y, getPos().getZ());

            TileEntity tile = this.worldObj.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityMulti)
            {
                BlockPos pos = ((TileEntityMulti) tile).mainBlockPosition;
                if (pos == null || !pos.equals(getPos()))
                {
                    ((TileEntityMulti) tile).mainBlockPosition = getPos();
                }
            }
        }

        if (!this.initialised)
        {
            int metadata = this.getBlockMetadata();
            if (metadata >= BlockSolar.ADVANCED_METADATA)
            {
                this.storage.setCapacity(30000);
                this.setTierGC(2);
            }
            this.initialised = true;
        }

        this.receiveEnergyGC(null, this.generateWatts, false);

        super.update();

        if (!this.worldObj.isRemote)
        {
            this.recharge(this.containingItems[0]);

            if (this.disableCooldown > 0)
            {
                this.disableCooldown--;
            }

            if (!this.getDisabled(0) && this.ticks % 20 == 0)
            {
                this.solarStrength = 0;

                if (this.worldObj.isDaytime() && (this.worldObj.provider instanceof IGalacticraftWorldProvider || !this.worldObj.isRaining() && !this.worldObj.isThundering()))
                {
                    double distance = 100.0D;
                    double sinA = -Math.sin((this.currentAngle - 77.5D) * Math.PI / 180.0D);
                    double cosA = Math.abs(Math.cos((this.currentAngle - 77.5D) * Math.PI / 180.0D));

                    for (int x = -1; x <= 1; x++)
                    {
                        for (int z = -1; z <= 1; z++)
                        {
                            if (this.tierGC == 1)
                            {
                                if (this.worldObj.canBlockSeeSky(this.getPos().add(x, 2, z)))
                                {
                                    boolean valid = true;

                                    for (int y = this.getPos().getY() + 3; y < 256; y++)
                                    {
                                        Block block = this.worldObj.getBlockState(new BlockPos(this.getPos().getX() + x, y, this.getPos().getZ() + z)).getBlock();

                                        if (block.isOpaqueCube())
                                        {
                                            valid = false;
                                            break;
                                        }
                                    }

                                    if (valid)
                                    {
                                        this.solarStrength++;
                                    }
                                }
                            }
                            else
                            {
                                boolean valid = true;

                                BlockVec3 blockVec = new BlockVec3(this).translate(x, 3, z);
                                for (double d = 0.0D; d < distance; d++)
                                {
                                    BlockVec3 blockAt = blockVec.clone().translate((int) (d * sinA), (int) (d * cosA), 0);
                                    Block block = blockAt.getBlock(this.worldObj);

                                    if (block.isOpaqueCube())
                                    {
                                        valid = false;
                                        break;
                                    }
                                }

                                if (valid)
                                {
                                    this.solarStrength++;
                                }
                            }
                        }
                    }
                }
            }
        }

        float angle = this.worldObj.getCelestialAngle(1.0F) - 0.7845194F < 0 ? 1.0F - 0.7845194F : -0.7845194F;
        float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

        celestialAngle %= 360;

        if (this.tierGC == 1)
        {
            if (!this.worldObj.isDaytime() || this.worldObj.isRaining() || this.worldObj.isThundering())
            {
                this.targetAngle = 77.5F + 180.0F;
            }
            else
            {
                this.targetAngle = 77.5F;
            }
        }
        else
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

        if (!this.worldObj.isRemote)
        {
            if (this.getGenerate() > 0.0F)
            {
                this.generateWatts = Math.min(Math.max(this.getGenerate(), 0), TileEntitySolar.MAX_GENERATE_WATTS);
            }
            else
            {
                this.generateWatts = 0;
            }
        }

        this.produce();
    }

    public int getGenerate()
    {
        if (this.getDisabled(0))
        {
            return 0;
        }

        float angle = this.worldObj.getCelestialAngle(1.0F) - 0.784690560F < 0 ? 1.0F - 0.784690560F : -0.784690560F;
        float celestialAngle = (this.worldObj.getCelestialAngle(1.0F) + angle) * 360.0F;

        celestialAngle %= 360;

        float difference = (180.0F - Math.abs(this.currentAngle % 180 - celestialAngle)) / 180.0F;

        return MathHelper.floor_float(0.01F * difference * difference * (this.solarStrength * (Math.abs(difference) * 500.0F)) * this.getSolarBoost());
    }

    public float getSolarBoost()
    {
        return (float) (this.worldObj.provider instanceof ISolarLevel ? ((ISolarLevel) this.worldObj.provider).getSolarEnergyMultiplier() : 1.0F);
    }

//    @Override TODO
//    public boolean onActivated(EntityPlayer entityPlayer)
//    {
//        return this.getBlockType().onBlockActivated(this.worldObj, this.getPos(), this.worldObj.getBlockState(getPos()), entityPlayer, EnumFacing.DOWN, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
//    }

//    @Override
//    public boolean canUpdate()
//    {
//        return true;
//    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        int buildHeight = this.worldObj.getHeight() - 1;

        if (placedPosition.getY() + 1 > buildHeight)
        {
            return;
        }
        final BlockPos vecStrut = new BlockPos(placedPosition.getX(), placedPosition.getY() + 1, placedPosition.getZ());
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecStrut, placedPosition, 0);

        if (placedPosition.getY() + 2 > buildHeight)
        {
            return;
        }
        for (int x = 0; x < 1; ++x)
        {
            for (int z = 0; z < 1; ++z)
            {
                final BlockPos vecToAdd = new BlockPos(placedPosition.getX() + x, placedPosition.getY() + 2, placedPosition.getZ() + z);

                ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, vecToAdd, placedPosition, (this.getTierGC() == 1) ? 4 : 0);
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        for (int y = 1; y <= 2; y++)
        {
            for (int x = -1; x < 2; x++)
            {
                for (int z = -1; z < 2; z++)
                {
                    BlockPos pos = getPos().add((y == 2 ? x : 0), y, (y == 2 ? z : 0));
                    IBlockState stateAt = this.worldObj.getBlockState(pos);
                    IBlockState stateBelow = this.worldObj.getBlockState(pos.down());

                    if (stateAt.getBlock() == GCBlocks.fakeBlock)
                    {
                        BlockMulti.EnumBlockMultiType type = (BlockMulti.EnumBlockMultiType) stateAt.getValue(BlockMulti.MULTI_TYPE);
                        if ((type == BlockMulti.EnumBlockMultiType.SOLAR_PANEL_0 || type == BlockMulti.EnumBlockMultiType.SOLAR_PANEL_1) &&
                                ((x == 0 && z == 0) || (stateBelow.getBlock().isAir(this.worldObj, pos.down()))))
                        {
                            if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                            {
                                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, GCBlocks.solarPanel.getDefaultState());
                            }

                            this.worldObj.setBlockToAir(pos);
                        }
                    }
                }
            }
        }

        this.worldObj.destroyBlock(getPos(), true);
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
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

	/*@Override
    public float getRequest(EnumFacing direction)
	{
		return 0;
	}
	*/

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    public EnumFacing getFront()
    {
        return ((EnumFacing) this.worldObj.getBlockState(getPos()).getValue(BlockSolar.FACING));
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.of(getFront());
    }

    @Override
    public EnumFacing getElectricalOutputDirectionMain()
    {
        return getFront();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate(this.tierGC == 1 ? "container.solarbasic.name" : "container.solaradvanced.name");
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
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
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

    @Override
    public boolean canConnect(EnumFacing direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return direction == this.getElectricalOutputDirectionMain();
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
    public IChatComponent getDisplayName()
    {
        return null;
    }
}
