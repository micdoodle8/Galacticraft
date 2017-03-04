package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.network.PacketDynamic;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;

public class EntityParachest extends Entity implements IPacketReceiver
{
    public ItemStack[] cargo;
    public int fuelLevel;
    private boolean placedChest;
    public EnumDyeColor color = EnumDyeColor.WHITE;

    public EntityParachest(World world, ItemStack[] cargo, int fuelLevel)
    {
        this(world);
        this.cargo = cargo.clone();
        this.placedChest = false;
        this.fuelLevel = fuelLevel;
    }

    public EntityParachest(World world)
    {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = nbt.getTagList("Items", 10);
        int size = 56;
        if (nbt.hasKey("CargoLength"))
        {
            size = nbt.getInteger("CargoLength");
        }
        this.cargo = new ItemStack[size];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.cargo.length)
            {
                this.cargo[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.placedChest = nbt.getBoolean("placedChest");
        this.fuelLevel = nbt.getInteger("FuelLevel");

        if (nbt.hasKey("color"))
        {
            this.color = EnumDyeColor.byDyeDamage(nbt.getInteger("color"));
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("CargoLength", this.cargo.length);

        final NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < this.cargo.length; ++var3)
        {
            if (this.cargo[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.cargo[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }
        nbt.setTag("Items", var2);

        nbt.setBoolean("placedChest", this.placedChest);
        nbt.setInteger("FuelLevel", this.fuelLevel);
        nbt.setInteger("color", this.color.getDyeDamage());
    }

    @Override
    public void onUpdate()
    {
        if (!this.placedChest)
        {
            if (this.onGround && !this.worldObj.isRemote)
            {
                for (int i = 0; i < 100; i++)
                {
                    final int x = MathHelper.floor_double(this.posX);
                    final int y = MathHelper.floor_double(this.posY);
                    final int z = MathHelper.floor_double(this.posZ);

                    BlockPos pos = new BlockPos(x, y + i, z);
                    Block block = this.worldObj.getBlockState(pos).getBlock();

                    if (block.getMaterial().isReplaceable())
                    {
                        if (this.placeChest(pos))
                        {
                            this.setDead();
                            return;
                        }
                        else if (this.cargo != null)
                        {
                            for (final ItemStack stack : this.cargo)
                            {
                                final EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
                                this.worldObj.spawnEntityInWorld(e);
                            }

                            return;
                        }
                    }
                }

                if (this.cargo != null)
                {
                    for (final ItemStack stack : this.cargo)
                    {
                        final EntityItem e = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack);
                        this.worldObj.spawnEntityInWorld(e);
                    }
                }
            }
            else
            {
                this.motionY = -0.35;
            }

            this.moveEntity(0, this.motionY, 0);
        }

        if (!this.worldObj.isRemote && this.ticksExisted % 5 == 0)
        {
            GalacticraftCore.packetPipeline.sendToAllAround(new PacketDynamic(this), new NetworkRegistry.TargetPoint(this.worldObj.provider.getDimension(), this.posX, this.posY, this.posZ, 64.0));
        }
    }

    private boolean placeChest(BlockPos pos)
    {
        this.worldObj.setBlockState(pos, GCBlocks.parachest.getDefaultState(), 3);
        final TileEntity te = this.worldObj.getTileEntity(pos);

        if (te instanceof TileEntityParaChest && this.cargo != null)
        {
            final TileEntityParaChest chest = (TileEntityParaChest) te;

            chest.chestContents = new ItemStack[this.cargo.length + 1];
            chest.color = this.color;

            System.arraycopy(this.cargo, 0, chest.chestContents, 0, this.cargo.length);

            chest.fuelTank.fill(FluidRegistry.getFluidStack(GCFluids.fluidFuel.getName().toLowerCase(), this.fuelLevel), true);

            return true;
        }

        this.placedChest = true;

        return true;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> sendData)
    {
        if (!this.worldObj.isRemote)
        {
            sendData.add(this.color.getDyeDamage());
        }
    }

    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        if (this.worldObj.isRemote)
        {
            this.color = EnumDyeColor.byDyeDamage(buffer.readInt());
        }
    }
}
