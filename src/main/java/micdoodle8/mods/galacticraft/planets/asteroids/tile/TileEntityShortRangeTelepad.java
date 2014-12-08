package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import io.netty.buffer.ByteBuf;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlock;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.BlockTelepadFake;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.network.PacketSimpleAsteroids;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityShortRangeTelepad extends TileBaseElectricBlock implements IMultiBlock, IInventory, ISidedInventory
{
    public static enum EnumTelepadSearchResult
    {
        VALID,
        NOT_FOUND,
        TOO_FAR,
        WRONG_DIM
    }

    public static final int MAX_TELEPORT_TIME = 150;
    public static final int TELEPORTER_RANGE = 256;
    public static final int ENERGY_USE_ON_TELEPORT = 2500;

    @NetworkedField(targetSide = Side.CLIENT)
    public int address = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean addressValid = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int targetAddress = -1;
    public EnumTelepadSearchResult targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
    @NetworkedField(targetSide = Side.CLIENT)
    public int teleportTime = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public String owner = "";
    private ItemStack[] containingItems = new ItemStack[1];
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean teleporting;

    public TileEntityShortRangeTelepad()
    {
        super();
        this.storage.setMaxExtract(50);
    }

    public int canTeleportHere()
    {
        if (this.worldObj.isRemote)
        {
            return -1;
        }

        this.setAddress(this.address);
        this.setTargetAddress(this.targetAddress);

        if (!this.addressValid)
        {
            return 1;
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return 2;
        }

        return 0; // GOOD
    }

    @Override
    public void updateEntity()
    {
        if (this.ticks % 40 == 0 && !worldObj.isRemote)
        {
            this.setAddress(this.address);
            this.setTargetAddress(this.targetAddress);
        }

        if (!this.worldObj.isRemote)
        {
            if (this.targetAddressResult == EnumTelepadSearchResult.VALID && (this.ticks % 5 == 0 || teleporting))
            {
                List<EntityLivingBase> containedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1));

                if (containedEntities.size() > 0 && this.getEnergyStoredGC() >= ENERGY_USE_ON_TELEPORT)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    if (entry != null)
                    {
                        teleporting = true;
                    }
                }
                else
                {
                    teleporting = false;
                }
            }

            if (this.teleporting)
            {
                this.teleportTime++;

                if (teleportTime >= MAX_TELEPORT_TIME)
                {
                    ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);

                    BlockVec3 finalPos = (entry == null) ? null : entry.position;

                    if (finalPos != null)
                    {
                        TileEntity tileAt = finalPos.getTileEntity(this.worldObj);
                        List<EntityLivingBase> containedEntities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 2, this.zCoord + 1));

                        if (tileAt != null && tileAt instanceof TileEntityShortRangeTelepad)
                        {
                            TileEntityShortRangeTelepad destTelepad = (TileEntityShortRangeTelepad) tileAt;
                            int teleportResult = destTelepad.canTeleportHere();
                            if (teleportResult == 0)
                            {
                                for (EntityLivingBase e : containedEntities)
                                {
                                    e.setPosition(finalPos.x + 0.5F, finalPos.y + 1.0F, finalPos.z + 0.5F);
                                    GalacticraftCore.packetPipeline.sendToDimension(new PacketSimpleAsteroids(PacketSimpleAsteroids.EnumSimplePacketAsteroids.C_TELEPAD_SEND, new Object[] { finalPos, e.getEntityId() }), this.worldObj.provider.dimensionId);
                                }

                                if (containedEntities.size() > 0)
                                {
                                    this.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                    destTelepad.storage.setEnergyStored(this.storage.getEnergyStoredGC() - ENERGY_USE_ON_TELEPORT);
                                }
                            }
                            else
                            {
                                switch (teleportResult)
                                {
                                case -1:
                                    for (EntityLivingBase e : containedEntities)
                                    {
                                        if (e instanceof EntityPlayer)
                                        {
                                            ((EntityPlayer) e).addChatComponentMessage(new ChatComponentText("Cannot Send client-side")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 1:
                                    for (EntityLivingBase e : containedEntities)
                                    {
                                        if (e instanceof EntityPlayer)
                                        {
                                            ((EntityPlayer) e).addChatComponentMessage(new ChatComponentText("Target address invalid")); // No need for translation, since this should never happen
                                        }
                                    }
                                    break;
                                case 2:
                                    for (EntityLivingBase e : containedEntities)
                                    {
                                        if (e instanceof EntityPlayer)
                                        {
                                            ((EntityPlayer) e).addChatComponentMessage(new ChatComponentText(GCCoreUtil.translate("gui.message.targetNoEnergy.name")));
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                    this.teleportTime = 0;
                    this.teleporting = false;
                }
            }
            else
            {
                this.teleportTime = Math.max(--this.teleportTime, 0);
            }
        }

        super.updateEntity();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        NBTTagList var2 = nbt.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.setAddress(nbt.getInteger("Address"));
        this.targetAddress = nbt.getInteger("TargetAddress");
        this.owner = nbt.getString("Owner");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        nbt.setTag("Items", var2);

        nbt.setInteger("TargetAddress", this.targetAddress);
        nbt.setInteger("Address", this.address);
        nbt.setString("Owner", this.owner);
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        super.addExtraNetworkedData(networkedList);
        networkedList.add(targetAddressResult.ordinal());
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        super.readExtraNetworkedData(dataStream);
        targetAddressResult = EnumTelepadSearchResult.values()[dataStream.readInt()];
    }

    @Override
    public double getPacketRange()
    {
        return 24.0D;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_ASTEROIDS, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    @Override
    public void onCreate(BlockVec3 placedPosition)
    {
        for (int y = 0; y < 3; y += 2)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x + x, placedPosition.y + y, placedPosition.z + z);

                    if (!vecToAdd.equals(placedPosition))
                    {
                        ((BlockTelepadFake) AsteroidBlocks.fakeTelepad).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, y == 0 ? 1 : 0);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        for (int y = 0; y < 3; y += 2)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    this.worldObj.func_147480_a(this.xCoord + x, this.yCoord + y, this.zCoord + z, y == 0 && x == 0 && z == 0);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
    	return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 4, zCoord + 2);
    }

    @Override
    public String getInventoryName()
    {
        return GCCoreUtil.translate("container.shortRangeTelepad.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && par1EntityPlayer.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, int par3)
    {
        return slotID == 0;
    }

    @Override
    public EnumSet<ForgeDirection> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    @Override
    public ForgeDirection getElectricInputDirection()
    {
        return ForgeDirection.getOrientation((this.getBlockMetadata() & 3) + 2);
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            switch (index)
            {
            case 0:
                this.disabled = disabled;
                this.disableCooldown = 10;
                break;
            default:
                break;
            }
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.disabled;
        default:
            break;
        }

        return true;
    }

    public void setAddress(int address)
    {
        if (this.worldObj != null && address != this.address)
        {
            ShortRangeTelepadHandler.removeShortRangeTeleporter(this);
        }

        this.address = address;

        if (this.address >= 0)
        {
            ShortRangeTelepadHandler.TelepadEntry entry = ShortRangeTelepadHandler.getLocationFromAddress(this.address);
            this.addressValid = entry == null || (this.worldObj != null && (entry.dimensionID == this.worldObj.provider.dimensionId && entry.position.x == this.xCoord && entry.position.y == this.yCoord && entry.position.z == this.zCoord));
        }
        else
        {
            this.addressValid = false;
        }

        if (worldObj != null && !worldObj.isRemote)
        {
            ShortRangeTelepadHandler.addShortRangeTelepad(this);
        }
    }

    public TileEntityShortRangeTelepad updateTarget()
    {
        if (this.targetAddress >= 0 && !this.worldObj.isRemote)
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;

            ShortRangeTelepadHandler.TelepadEntry addressResult = ShortRangeTelepadHandler.getLocationFromAddress(this.targetAddress);
            TileEntityShortRangeTelepad foundTelepad = null;

            if (addressResult != null)
            {
                WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(addressResult.dimensionID);

                TileEntity tile2 = addressResult.position.getTileEntity(world);

                if (tile2 == null)
                {
                    FMLLog.severe("Bad TileEntity in Telepad Handler: address(" + this.targetAddress + ") x" + addressResult.position.x + " y" + addressResult.position.y + " z" + addressResult.position.z);
                }
                else
                {
                    if (this != tile2)
                    {
                        if (tile2 instanceof TileEntityShortRangeTelepad)
                        {
                            TileEntityShortRangeTelepad launchController2 = (TileEntityShortRangeTelepad) tile2;

                            if (launchController2.address == this.targetAddress && launchController2.addressValid)
                            {
                                foundTelepad = launchController2;
                            }
                        }
                    }
                }
            }

            if (foundTelepad != null)
            {
                if (this.worldObj.provider.dimensionId == foundTelepad.worldObj.provider.dimensionId)
                {
                    double distance = this.getDistanceFrom(foundTelepad.xCoord + 0.5F, foundTelepad.yCoord + 0.5F, foundTelepad.zCoord + 0.5F);

                    if (distance < TELEPORTER_RANGE * TELEPORTER_RANGE)
                    {
                        this.targetAddressResult = EnumTelepadSearchResult.VALID;
                        return foundTelepad;
                    }
                    else
                    {
                        this.targetAddressResult = EnumTelepadSearchResult.TOO_FAR;
                        return null;
                    }
                }
                else
                {
                    this.targetAddressResult = EnumTelepadSearchResult.WRONG_DIM;
                    return null;
                }
            }
            else
            {
                this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
                return null;
            }
        }
        else
        {
            this.targetAddressResult = EnumTelepadSearchResult.NOT_FOUND;
            return null;
        }
    }

    public void setTargetAddress(int address)
    {
        this.targetAddress = address;
        this.updateTarget();
    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

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
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var2 = this.containingItems[par1];
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

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return this.owner;
    }

    @SideOnly(Side.CLIENT)
    public String getReceivingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalidAddress.name");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.noEnergy.name");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.notEnoughEnergy.name");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.receivingActive.name");
    }

    @SideOnly(Side.CLIENT)
    public String getSendingStatus()
    {
        if (!this.addressValid)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.invalidTargetAddress.name");
        }

        if (this.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.TOO_FAR)
        {
            return EnumColor.RED + GCCoreUtil.translateWithFormat("gui.message.telepadTooFar.name", TELEPORTER_RANGE);
        }

        if (this.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.WRONG_DIM)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepadWrongDim.name");
        }

        if (this.targetAddressResult == TileEntityShortRangeTelepad.EnumTelepadSearchResult.NOT_FOUND)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.telepadNotFound.name");
        }

        if (this.getEnergyStoredGC() <= 0.0F)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.noEnergy.name");
        }

        if (this.getEnergyStoredGC() <= ENERGY_USE_ON_TELEPORT)
        {
            return EnumColor.RED + GCCoreUtil.translate("gui.message.notEnoughEnergy.name");
        }

        if (this.getDisabled(0))
        {
            return EnumColor.ORANGE + GCCoreUtil.translate("gui.status.disabled.name");
        }

        return EnumColor.BRIGHT_GREEN + GCCoreUtil.translate("gui.message.sendingActive.name");
    }

    @SideOnly(Side.CLIENT)
    public Vector3 getParticleColor(Random rand, boolean sending)
    {
        float teleportTimeScaled = Math.min(1.0F, this.teleportTime / (float) TileEntityShortRangeTelepad.MAX_TELEPORT_TIME);
        float f;
        f = rand.nextFloat() * 0.6F + 0.4F;

        if (sending && this.targetAddressResult != EnumTelepadSearchResult.VALID)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (!sending && !this.addressValid)
        {
            return new Vector3(f, f * 0.3F, f * 0.3F);
        }

        if (this.getEnergyStoredGC() < ENERGY_USE_ON_TELEPORT)
        {
            return new Vector3(f, f * 0.6F, f * 0.3F);
        }

        float r = f * 0.3F;
        float g = f * (0.3F + (teleportTimeScaled * 0.7F));
        float b = f * (1.0F - (teleportTimeScaled * 0.7F));

        return new Vector3(r, g, b);
    }
}
