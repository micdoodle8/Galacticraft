package micdoodle8.mods.galacticraft.core.entities;

import icbm.api.IMissileLockable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import micdoodle8.mods.galacticraft.API.IDockable;
import micdoodle8.mods.galacticraft.API.IExitHeight;
import micdoodle8.mods.galacticraft.API.IOrbitDimension;
import micdoodle8.mods.galacticraft.API.ISpaceship;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GCCoreDamageSource;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPad;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import com.google.common.io.ByteArrayDataInput;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntitySpaceshipBase extends Entity implements ISpaceship, IPacketReceiver, IMissileLockable, IDockable, IInventory
{
    public static enum EnumLaunchPhase
    {
        UNIGNITED(1),
        IGNITED(2),
        LAUNCHED(3);
        
        private int phase;
        
        private EnumLaunchPhase(int phase)
        {
            this.phase = phase;
        }
        
        public int getPhase()
        {
            return this.phase;
        }
    }
    
    public int launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();
    
    protected long ticks = 0;
    protected double dragAir;
    public int timeUntilLaunch;
    public float timeSinceLaunch;
    public float rumble;
    public float rollAmplitude;
    public float shipDamage;

    public EntitySpaceshipBase(World par1World)
    {
        super(par1World);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 4F);
        this.yOffset = this.height / 2.0F;
        this.ignoreFrustumCheck = true;
        this.renderDistanceWeight = 5.0D;
    }

    public abstract int getMaxFuel();

    public abstract int getScaledFuelLevel(int i);

    public abstract boolean hasValidFuel();

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity)
    {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }

    @Override
    public void setDead()
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
        {
            final Object[] toSend2 = { 0 };
            ((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
        }

        super.setDead();
    }

    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.isEntityInvulnerable() || this.posY > 300)
            {
                return false;
            }
            else
            {
                this.rollAmplitude = 10;
                this.setBeenAttacked();
                this.shipDamage += par2 * 10;

                if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer) par1DamageSource.getEntity()).capabilities.isCreativeMode)
                {
                    this.shipDamage = 100;
                }

                if (this.shipDamage > 90 && !this.worldObj.isRemote)
                {
                    if (this.riddenByEntity != null)
                    {
                        final Object[] toSend2 = { 0 };
                        ((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));

                        this.riddenByEntity.mountEntity(this);
                    }

                    this.setDead();
                    this.dropShipAsItem();
                    return true;
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void dropShipAsItem()
    {
        if (this.getItemsDropped() == null || this.worldObj.isRemote)
        {
            return;
        }

        for (final ItemStack item : this.getItemsDropped())
        {
            this.entityDropItem(item, 0);
        }
    }

    public abstract List<ItemStack> getItemsDropped();

    @Override
    public void performHurtAnimation()
    {
        this.rollAmplitude = 5;
        this.shipDamage += this.shipDamage * 10;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public boolean shouldRiderSit()
    {
        return false;
    }

    @Override
    public void onUpdate()
    {
        if (this.ticks >= Long.MAX_VALUE)
        {
            this.ticks = 1;
        }

        this.ticks++;

        super.onUpdate();

        if (!this.worldObj.isRemote && this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
        {
            for (final TileEntity tile : this.getLandingPad().getConnectedTiles())
            {
                if (this.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) == null || !(this.worldObj.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) instanceof GCCoreTileEntityFuelLoader))
                {

                }
                else
                {
                    if (tile instanceof GCCoreTileEntityFuelLoader && ((GCCoreTileEntityFuelLoader) tile).wattsReceived > 0)
                    {
                        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
                        {
                            this.setPad(null);
                        }
                    }
                }
            }
        }

        if (this.rumble > 0)
        {
            this.rumble--;
        }

        if (this.rumble < 0)
        {
            this.rumble++;
        }

        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.posX += this.rumble / 30F;
            this.riddenByEntity.posZ += this.rumble / 30F;
        }

        if (this.posY > (this.worldObj.provider instanceof IExitHeight ? ((IExitHeight) this.worldObj.provider).getYCoordinateToTeleport() : 1200))
        {
            this.teleport();
        }

        if (this.rollAmplitude > 0)
        {
            this.rollAmplitude--;
        }
        
        if (this.shipDamage > 0)
        {
            this.shipDamage--;
        }

        if (this.posY < 0.0D || this.posY > (this.worldObj.provider instanceof IExitHeight ? ((IExitHeight) this.worldObj.provider).getYCoordinateToTeleport() : 1200) + 10)
        {
            this.kill();
        }

        if (this.launchPhase == EnumLaunchPhase.UNIGNITED.getPhase())
        {
            this.timeUntilLaunch = this.getPreLaunchWait();
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.timeSinceLaunch++;
        }
        else
        {
            this.timeSinceLaunch = 0;
        }

        if (this.timeUntilLaunch > 0 && this.launchPhase == EnumLaunchPhase.IGNITED.getPhase())
        {
            this.timeUntilLaunch--;
        }

        AxisAlignedBB box = null;

        box = this.boundingBox.expand(0.2D, 0.2D, 0.2D);

        final List var15 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

        if (var15 != null && !var15.isEmpty())
        {
            for (int var52 = 0; var52 < var15.size(); ++var52)
            {
                final Entity var17 = (Entity) var15.get(var52);

                if (var17 != this.riddenByEntity)
                {
                    var17.applyEntityCollision(this);
                }
            }
        }

        if (this.timeUntilLaunch == 0 && this.launchPhase == EnumLaunchPhase.IGNITED.getPhase())
        {
            this.launchPhase = EnumLaunchPhase.LAUNCHED.getPhase();
            this.onLaunch();

            if (!this.worldObj.isRemote)
            {
                if (!(this.worldObj.provider instanceof IOrbitDimension) && this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
                {
                    ((GCCorePlayerMP) this.riddenByEntity).coordsTeleportedFromX = this.riddenByEntity.posX;
                    ((GCCorePlayerMP) this.riddenByEntity).coordsTeleportedFromZ = this.riddenByEntity.posZ;
                }

                int amountRemoved = 0;

                for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                {
                    for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
                    {
                        for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                        {
                            final int id = this.worldObj.getBlockId(x, y, z);
                            final Block block = Block.blocksList[id];

                            if (block != null && block instanceof GCCoreBlockLandingPadFull)
                            {
                                if (amountRemoved < 9)
                                {
                                    this.worldObj.setBlockToAir(x, y, z);
                                    amountRemoved = 9;
                                }
                            }

                            if (block != null && block instanceof GCCoreBlockLandingPad)
                            {
                                if (amountRemoved < 9)
                                {
                                    this.worldObj.setBlockToAir(x, y, z);
                                    amountRemoved++;
                                }
                            }
                        }
                    }
                }

                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
        }

        if (this.launchPhase == EnumLaunchPhase.IGNITED.getPhase() || this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.performHurtAnimation();

            this.rumble = (float) this.rand.nextInt(3) - 3;
        }

        if (this.rotationPitch > 90)
        {
            this.rotationPitch = 90;
        }

        if (this.rotationPitch < -90)
        {
            this.rotationPitch = -90;
        }

        this.motionX = -(50 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));
        this.motionZ = -(50 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * 0.01 * Math.PI / 180.0D));

        if (this.timeSinceLaunch > 50 && this.onGround)
        {
            this.failRocket();
        }

        if (this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
        {
            this.motionX = this.motionY = this.motionZ = 0.0F;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.setRotation(this.rotationYaw, this.rotationPitch);

        if (this.worldObj.isRemote)
        {
            this.setPosition(this.posX, this.posY, this.posZ);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if (!this.worldObj.isRemote && this.ticks % 3 == 0)
        {
            PacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList())), this.worldObj, new Vector3(this), 50);
        }
    }

    @Override
    public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
    {
        try
        {
            if (this.worldObj.isRemote)
            {
                this.readNetworkedData(dataStream);
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void readNetworkedData(ByteArrayDataInput dataStream)
    {
        this.launchPhase = dataStream.readInt();
        this.timeSinceLaunch = dataStream.readFloat();
        this.timeUntilLaunch = dataStream.readInt();
    }
    
    public ArrayList getNetworkedData(ArrayList list)
    {
        list.add(this.launchPhase);
        list.add(this.timeSinceLaunch);
        list.add(this.timeUntilLaunch);
        return list;
    }

    public void turnYaw(float f)
    {
        this.rotationYaw += f;
    }

    public void turnPitch(float f)
    {
        this.rotationPitch += f;
    }

    private void failRocket()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.attackEntityFrom(GCCoreDamageSource.spaceshipCrash, (int) (4.0D * 20 + 1.0D));
        }

        if (!GCCoreConfigManager.disableSpaceshipGrief)
        {
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 5, true);
        }

        this.setDead();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setRotation(par7, par8);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("launchPhase", this.launchPhase);
        nbt.setInteger("timeUntilLaunch", this.timeUntilLaunch);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.timeUntilLaunch = nbt.getInteger("timeUntilLaunch");
        
        boolean hasOldTags = false;
        
        // Backwards compatibility:
        if (nbt.getTags().contains("launched"))
        {
            hasOldTags = true;
            
            boolean launched = nbt.getBoolean("launched");
            
            if (launched)
            {
                this.launchPhase = EnumLaunchPhase.LAUNCHED.getPhase();
            }
        }

        // Backwards compatibility:
        if (nbt.getTags().contains("ignite"))
        {
            hasOldTags = true;
            
            int ignite = nbt.getInteger("ignite");
            
            if (ignite == 1)
            {
                this.launchPhase = EnumLaunchPhase.IGNITED.getPhase();
            }
        }

        // Backwards compatibility:
        if (hasOldTags)
        {
            if (this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase() && this.launchPhase != EnumLaunchPhase.IGNITED.getPhase())
            {
                this.launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();
            }
        }
        else
        {
            this.launchPhase = nbt.getInteger("launchPhase");
        }
    }
    
    public boolean getLaunched()
    {
        return this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase();
    }

    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote)
        {
            par1EntityPlayer.mountEntity(this);

            if (this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
            {
                final Object[] toSend = { ((EntityPlayerMP) this.riddenByEntity).username };
                ((EntityPlayerMP) this.riddenByEntity).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 8, toSend));
                final Object[] toSend2 = { 1 };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
                ((GCCorePlayerMP) par1EntityPlayer).chatCooldown = 0;
            }
            else if (par1EntityPlayer instanceof GCCorePlayerMP)
            {
                final Object[] toSend = { par1EntityPlayer.username };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 13, toSend));
                final Object[] toSend2 = { 0 };
                ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 22, toSend2));
                ((GCCorePlayerMP) par1EntityPlayer).chatCooldown = 0;
            }

            return true;
        }

        return false;
    }

    public boolean canBeRidden()
    {
        return false;
    }

    public void ignite()
    {
        this.launchPhase = EnumLaunchPhase.IGNITED.getPhase();
    }

    @Override
    public double getMountedYOffset()
    {
        return -1.0D;
    }

    public void teleport()
    {
        if (this.riddenByEntity != null)
        {
            if (this.riddenByEntity instanceof EntityPlayerMP)
            {
                final GCCorePlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer((EntityPlayerMP) this.riddenByEntity);

                final EntityPlayerMP entityplayermp = (EntityPlayerMP) this.riddenByEntity;

                final Integer[] ids = WorldUtil.getArrayOfPossibleDimensions();

                final Set set = WorldUtil.getArrayOfPossibleDimensions(ids, playerBase).entrySet();
                final Iterator i = set.iterator();

                String temp = "";

                for (int k = 0; i.hasNext(); k++)
                {
                    final Map.Entry entry = (Map.Entry) i.next();
                    temp = k == 0 ? temp.concat(String.valueOf(entry.getKey())) : temp.concat("." + String.valueOf(entry.getKey()));
                }

                final Object[] toSend = { entityplayermp.username, temp };
                FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(entityplayermp.username).playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 2, toSend));

                if (playerBase != null)
                {
                    playerBase.setUsingPlanetGui();
                }

                this.onTeleport(entityplayermp);

                if (this.riddenByEntity != null)
                {
                    this.riddenByEntity.mountEntity(this);
                }

                if (!this.isDead)
                {
                    this.setDead();
                }
            }
        }
    }

    public void onLaunch()
    {
    }

    public void onTeleport(EntityPlayerMP player)
    {
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
    }
}
