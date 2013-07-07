package micdoodle8.mods.galacticraft.mars.entities;

import icbm.api.IMissile;
import icbm.api.IMissileLockable;
import icbm.api.RadarRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import micdoodle8.mods.galacticraft.API.entity.IRocketType;
import micdoodle8.mods.galacticraft.API.tile.IFuelDock;
import micdoodle8.mods.galacticraft.API.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.mars.GCMarsConfigManager;
import micdoodle8.mods.galacticraft.mars.items.GCMarsItems;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import universalelectricity.core.vector.Vector3;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreEntityRocketT2 extends EntitySpaceshipBase implements IInventory, IMissileLockable, IRocketType
{
    public final int tankCapacity = 4000;
    public FluidTank spaceshipFuelTank = new FluidTank(this.tankCapacity);

    protected ItemStack[] cargoItems;

    public IUpdatePlayerListBox rocketSoundUpdater;

    private IFuelDock landingPad;

    public int canisterToTankRatio = this.tankCapacity / GCCoreItems.fuelCanister.getMaxDamage();
    public double canisterToFluidStackRatio = FluidContainerRegistry.BUCKET_VOLUME * 2.0D / GCCoreItems.fuelCanister.getMaxDamage();

    public GCCoreEntityRocketT2(World par1World)
    {
        super(par1World);
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount;

        return (int) (fuelLevel * i / 2000);
    }

    public GCCoreEntityRocketT2(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
    {
        super(par1World);
        this.setPosition(par2, par4 + this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
        this.rocketType = rocketType;
        this.cargoItems = new ItemStack[this.getSizeInventory()];
    }

    public GCCoreEntityRocketT2(World par1World, double par2, double par4, double par6, boolean reversed, EnumRocketType rocketType, ItemStack[] inv)
    {
        this(par1World, par2, par4, par6, rocketType);
        this.cargoItems = inv;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        RadarRegistry.register(this);
    }

    @Override
    public void setDead()
    {
        super.setDead();

        RadarRegistry.unregister(this);

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        int i;

        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
            i = 1;
        }

        if ((this.getLaunched() || this.rand.nextInt(i) == 0) && !GCCoreConfigManager.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                this.spawnParticles(this.getLaunched());
            }
        }

        if (this.rocketSoundUpdater != null && (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.getLaunched()))
        {
            this.rocketSoundUpdater.update();
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.hasValidFuel())
        {
            double d = this.timeSinceLaunch / 250;

            d = Math.min(d, 1);

            if (d != 0.0)
            {
                this.motionY = -d * 1.5D * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
            }

            double multiplier = 1.0D;

            if (this.worldObj.provider instanceof IGalacticraftWorldProvider)
            {
                multiplier = ((IGalacticraftWorldProvider) this.worldObj.provider).getFuelUsageMultiplier();

                if (multiplier <= 0)
                {
                    multiplier = 1;
                }
            }

            if (this.timeSinceLaunch % MathHelper.floor_double(3 * (1 / multiplier)) == 0)
            {
                this.removeFuel(null, 1);
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }
    }

    @Override
    public void readNetworkedData(ByteArrayDataInput dataStream)
    {
        super.readNetworkedData(dataStream);
        this.spaceshipFuelTank.setFluid(new FluidStack(GalacticraftCore.FUEL, dataStream.readInt()));

        if (this.cargoItems == null)
        {
            this.cargoItems = new ItemStack[this.getSizeInventory()];
        }
    }

    @Override
    public ArrayList getNetworkedData(ArrayList list)
    {
        super.getNetworkedData(list);
        list.add(this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount);
        return list;
    }

    @Override
    public boolean hasValidFuel()
    {
        return !(this.spaceshipFuelTank.getFluid() == null || this.spaceshipFuelTank.getFluid().amount == 0);
    }

    @Override
    public void onLaunch()
    {

    }

    @Override
    public void onTeleport(EntityPlayerMP player)
    {
        final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);

        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, new Object[] { 0 }));

        if (playerBase != null)
        {
            if (this.cargoItems == null || this.cargoItems.length == 0)
            {
                playerBase.rocketStacks = new ItemStack[3];
            }
            else
            {
                playerBase.rocketStacks = this.cargoItems;
            }

            playerBase.rocketType = this.rocketType.getIndex();
            final int liquid = this.spaceshipFuelTank.getFluid() == null ? 0 : this.spaceshipFuelTank.getFluid().amount / MathHelper.floor_double(this.canisterToFluidStackRatio == 0 ? 1 : this.canisterToFluidStackRatio);
            playerBase.fuelDamage = Math.max(Math.min(GCCoreItems.fuelCanister.getMaxDamage() - liquid, GCCoreItems.fuelCanister.getMaxDamage()), 1);
        }
    }

    protected void spawnParticles(boolean launched)
    {
        final double x1 = 2.9 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double z1 = 2.9 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        double y1 = 2.9 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

        final double y = this.prevPosY + (this.posY - this.prevPosY);

        if (!this.isDead)
        {
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + 0.4 + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX - 0.4 + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ + 0.4D + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ - 0.4D + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("blueflame", this.posX + x1 - 0.8, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("blueflame", this.posX + x1 + 0.8, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("blueflame", this.posX + x1, y - 0.0D + y1, this.posZ + z1 - 0.8, x1, y1, z1, this.getLaunched());
            GalacticraftCore.proxy.spawnParticle("blueflame", this.posX + x1, y - 0.0D + y1, this.posZ + z1 + 0.8, x1, y1, z1, this.getLaunched());
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public int getSizeInventory()
    {
        return this.rocketType.getInventorySpace();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    final NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte) var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            par1NBTTagCompound.setTag("Items", var2);
        }

        if (this.spaceshipFuelTank.getFluid() != null)
        {
            par1NBTTagCompound.setTag("fuelTank", this.spaceshipFuelTank.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
                final int var5 = var4.getByte("Slot") & 255;

                if (var5 >= 0 && var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }

        if (par1NBTTagCompound.hasKey("fuelTank"))
        {
            this.spaceshipFuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.cargoItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
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
        if (this.cargoItems[par1] != null)
        {
            final ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
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
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInvName()
    {
        return "container.spaceship";
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
    }

    @Override
    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public HashSet<Integer> getPossiblePlanets()
    {
        final HashSet<Integer> dimensions = new HashSet<Integer>();
        dimensions.add(0);
        dimensions.add(GCMoonConfigManager.dimensionIDMoon);
        dimensions.add(GCMarsConfigManager.dimensionIDMars);
        return dimensions;
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
    }

    @Override
    public List<ItemStack> getItemsDropped()
    {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(GCMarsItems.spaceship, 1, this.rocketType.getIndex()));

        if (this.cargoItems != null)
        {
            for (final ItemStack item : this.cargoItems)
            {
                if (item != null)
                {
                    items.add(item);
                }
            }
        }

        return items;
    }

    @Override
    public boolean isInvNameLocalized()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getMaxFuel()
    {
        return this.spaceshipFuelTank.getCapacity();
    }

    @Override
    public boolean canLock(IMissile missile)
    {
        return true;
    }

    @Override
    public Vector3 getPredictedPosition(int ticks)
    {
        return new Vector3(this);
    }

    @Override
    public int addFuel(FluidStack liquid, int amount, boolean doFill)
    {
        final FluidStack liquidInTank = this.spaceshipFuelTank.getFluid();

        if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("Fuel"))
        {
            if (liquidInTank == null || liquidInTank.amount + liquid.amount <= this.spaceshipFuelTank.getCapacity())
            {
                return this.spaceshipFuelTank.fill(liquid, doFill);
            }
        }

        return 0;
    }

    @Override
    public FluidStack removeFuel(FluidStack liquid, int amount)
    {
        if (liquid == null)
        {
            return this.spaceshipFuelTank.drain(amount, true);
        }

        return null;
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.rocketType.getInventorySpace() <= 3)
        {
            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.cargoItems.length - 3; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt != null && stackAt.itemID == stack.itemID && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (doAdd)
                {
                    this.cargoItems[count].stackSize += stack.stackSize;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        for (count = 0; count < this.cargoItems.length - 3; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.cargoItems[count] = stack;
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.cargoItems.length - 3; i++)
        {
            ItemStack stackAt = this.cargoItems[i];

            if (stackAt != null)
            {
                if (doRemove && --this.cargoItems[i].stackSize <= 0)
                {
                    this.cargoItems[i] = null;
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.itemID, 1, stackAt.getItemDamage()));
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public void onPadDestroyed()
    {
        if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.dropShipAsItem();
            this.setDead();
        }
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        this.landingPad = pad;
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof GCCoreTileEntityLandingPad;
    }

    @Override
    public void destroyCraft()
    {
        this.setDead();
    }

    @Override
    public int doDamage(int damage)
    {
        this.shipDamage += damage;
        return damage;
    }

    @Override
    public boolean canBeTargeted(Object entity)
    {
        return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.timeSinceLaunch > 50;
    }
    
    @Override
    public int getRocketTier()
    {
        return 2;
    }
}
