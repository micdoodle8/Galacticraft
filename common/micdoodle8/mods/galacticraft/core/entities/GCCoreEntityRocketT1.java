package micdoodle8.mods.galacticraft.core.entities;

import icbm.api.IMissile;
import icbm.api.IMissileLockable;
import icbm.api.RadarRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import micdoodle8.mods.galacticraft.API.IFuelDock;
import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IRocketType;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchFlameFX;
import micdoodle8.mods.galacticraft.core.client.fx.GCCoreEntityLaunchSmokeFX;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.moon.GCMoonConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreEntityRocketT1 extends EntitySpaceshipBase implements IInventory, IMissileLockable, IRocketType
{
    private final int tankCapacity = 2000;
    public LiquidTank spaceshipFuelTank = new LiquidTank(this.tankCapacity);

    protected ItemStack[] cargoItems = new ItemStack[27];

    public IUpdatePlayerListBox rocketSoundUpdater;

    private int type;

    private IFuelDock landingPad;

    public int canisterToTankRatio = this.tankCapacity / GCCoreItems.fuelCanister.getMaxDamage();
    public double canisterToLiquidStackRatio = LiquidContainerRegistry.BUCKET_VOLUME * 2.0D / GCCoreItems.fuelCanister.getMaxDamage();

    public GCCoreEntityRocketT1(World par1World)
    {
        super(par1World);
    }

    @Override
    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.spaceshipFuelTank.getLiquid() == null ? 0 : this.spaceshipFuelTank.getLiquid().amount;

        return (int) (fuelLevel * i / 2000);
    }

    public GCCoreEntityRocketT1(World par1World, double par2, double par4, double par6, int type)
    {
        super(par1World);
        this.setPosition(par2, par4 + this.yOffset, par6);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = par2;
        this.prevPosY = par4;
        this.prevPosZ = par6;
    }

    public GCCoreEntityRocketT1(World par1World, double par2, double par4, double par6, boolean reversed, int type, ItemStack[] inv)
    {
        this(par1World, par2, par4, par6, type);
        this.cargoItems = inv;
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(25, new Integer(0));
        this.setSpaceshipType(this.type);
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

        if ((this.getLaunched() == 1 || this.rand.nextInt(i) == 0) && !GCCoreConfigManager.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            {
                this.spawnParticles(this.getLaunched() == 1);
            }
        }

        if (this.rocketSoundUpdater != null && (this.ignite == 1 || this.getLaunched() == 1))
        {
            this.rocketSoundUpdater.update();
        }

        if (this.launched && this.hasValidFuel())
        {
            double d = this.timeSinceLaunch / 250;

            d = Math.min(d, 1);

            if (d != 0.0)
            {
                this.motionY = -d * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
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
        else if (!this.hasValidFuel() && this.getLaunched() == 1 && !this.worldObj.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }

        if (!this.worldObj.isRemote && this.ticks % 3 == 0)
        {
            PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 50);
        }
    }

    public Packet getDescriptionPacket()
    {
        final Packet p = GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.spaceshipFuelTank.getLiquid() == null ? 0 : this.spaceshipFuelTank.getLiquid().amount, this.launched, this.ignite);
        return p;
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            if (this.worldObj.isRemote)
            {
                this.spaceshipFuelTank.setLiquid(new LiquidStack(GCCoreItems.fuel.itemID, dataStream.readInt(), 0));
                this.launched = dataStream.readBoolean();
                this.ignite = dataStream.readInt();
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasValidFuel()
    {
        return !(this.spaceshipFuelTank.getLiquid() == null || this.spaceshipFuelTank.getLiquid().amount == 0);
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
            playerBase.rocketStacks = this.cargoItems;
            playerBase.rocketType = this.getSpaceshipType();
            final int liquid = this.spaceshipFuelTank.getLiquid() == null ? 0 : this.spaceshipFuelTank.getLiquid().amount / MathHelper.floor_double(this.canisterToLiquidStackRatio == 0 ? 1 : this.canisterToLiquidStackRatio);
            playerBase.fuelDamage = Math.max(Math.min(GCCoreItems.fuelCanister.getMaxDamage() - liquid, GCCoreItems.fuelCanister.getMaxDamage()), 1);
        }
    }

    protected void spawnParticles(boolean launched)
    {
        final double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        final double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
        double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D) + (this.getReversed() == 1 ? 10D : 0D);

        if (this.getLaunched() == 0)
        {
            y1 -= 2D;
        }

        final double y = this.prevPosY + (this.posY - this.prevPosY);

        if (!this.isDead)
        {
            this.spawnParticle("launchflame", this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ + 0.4 - this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX - 0.4 + this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX + 0.4 - this.rand.nextDouble() / 10 + x1, y - 0.0D + y1, this.posZ - 0.4 + this.rand.nextDouble() / 10 + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX + 0.4 + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX - 0.4 + x1, y - 0.0D + y1, this.posZ + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ + 0.4D + z1, x1, y1, z1, this.getLaunched() == 1);
            this.spawnParticle("launchflame", this.posX + x1, y - 0.0D + y1, this.posZ - 0.4D + z1, x1, y1, z1, this.getLaunched() == 1);
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
        return 27;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("Type", this.getSpaceshipType());

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

        if (this.spaceshipFuelTank.getLiquid() != null)
        {
            par1NBTTagCompound.setTag("fuelTank", this.spaceshipFuelTank.writeToNBT(new NBTTagCompound()));
        }

        // if (this.spaceshipFuelTank.getLiquid() != null)
        // {
        // NBTTagCompound nbt = new NBTTagCompound();
        // nbt.setInteger("Amount", this.spaceshipFuelTank.getLiquid().amount);
        // nbt.setShort("Id", (short)this.spaceshipFuelTank.getLiquid().itemID);
        // nbt.setShort("Meta",
        // (short)this.spaceshipFuelTank.getLiquid().itemMeta);
        // nbt.setString("LiquidName", "Fuel");
        // if (this.spaceshipFuelTank.getLiquid().extra != null)
        // {
        // nbt.setTag("extra", this.spaceshipFuelTank.getLiquid().extra);
        // }
        //
        // par1NBTTagCompound.setTag("fuelTank", nbt);
        // }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        this.setSpaceshipType(par1NBTTagCompound.getInteger("Type"));

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

    public void setSpaceshipType(int par1)
    {
        this.dataWatcher.updateObject(25, par1);
    }

    public int getSpaceshipType()
    {
        return this.dataWatcher.getWatchableObjectInt(25);
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

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b)
    {
        this.spawnParticle(var1, var2, var4, var6, var8, var10, var12, 0.0D, 0.0D, 0.0D, b);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b)
    {
        final Minecraft mc = FMLClientHandler.instance().getClient();

        if (mc != null && mc.renderViewEntity != null && mc.effectRenderer != null)
        {
            final double var16 = mc.renderViewEntity.posX - var2;
            final double var17 = mc.renderViewEntity.posY - var4;
            final double var19 = mc.renderViewEntity.posZ - var6;
            final double var22 = 64.0D;

            if (var1.equals("whitesmoke"))
            {
                final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1.0F, b);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("whitesmokelarge"))
            {
                final EntityFX fx = new GCCoreEntityLaunchSmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F, b);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("launchflame"))
            {
                final EntityFX fx = new GCCoreEntityLaunchFlameFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 1F);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
            else if (var1.equals("distancesmoke") && var16 * var16 + var17 * var17 + var19 * var19 < var22 * var22 * 1.7)
            {
                final EntityFX fx = new EntitySmokeFX(mc.theWorld, var2, var4, var6, var8, var10, var12, 2.5F);
                if (fx != null)
                {
                    mc.effectRenderer.addEffect(fx);
                }
            }
        }
    }

    @Override
    public Entity[] getSpaceshipParts()
    {
        return null;
    }

    @Override
    public HashSet<Integer> getPossiblePlanets()
    {
        final HashSet<Integer> dimensions = new HashSet<Integer>();
        dimensions.add(0);
        dimensions.add(GCMoonConfigManager.dimensionIDMoon);
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
        items.add(new ItemStack(GCCoreItems.spaceship, 1, this.getSpaceshipType()));

        for (final ItemStack item : this.cargoItems)
        {
            if (item != null)
            {
                items.add(item);
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
    public int addFuel(LiquidStack liquid, int amount, boolean doFill)
    {
        final LiquidStack liquidInTank = this.spaceshipFuelTank.getLiquid();

        if (liquid != null && LiquidDictionary.findLiquidName(liquid).equals("Fuel"))
        {
            if (liquidInTank == null || liquidInTank.amount + liquid.amount <= this.spaceshipFuelTank.getCapacity())
            {
                return this.spaceshipFuelTank.fill(liquid, doFill);
            }
        }

        return 0;
    }

    @Override
    public LiquidStack removeFuel(LiquidStack liquid, int amount)
    {
        if (liquid == null)
        {
            return this.spaceshipFuelTank.drain(amount, true);
        }

        return null;
    }

    @Override
    public void onPadDestroyed()
    {
        if (!this.isDead && !this.launched)
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
    public int getType()
    {
        return this.getSpaceshipType();
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof GCCoreTileEntityLandingPad;
    }
}
