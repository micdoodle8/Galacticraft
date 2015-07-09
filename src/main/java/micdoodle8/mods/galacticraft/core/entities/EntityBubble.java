package micdoodle8.mods.galacticraft.core.entities;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenDistributor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBubble extends EntityAdvanced implements IPacketReceiver, IBubble
{
    @NetworkedField(targetSide = Side.CLIENT)
    public float size;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = true;

    public TileEntityOxygenDistributor distributor;

    public EntityBubble(World world, Vector3 mainBlockVec, TileEntityOxygenDistributor distributor)
    {
        this(world);
        this.posX = mainBlockVec.x + 0.5D;
        this.posY = mainBlockVec.y + 1.0D;
        this.posZ = mainBlockVec.z + 0.5D;
        this.distributor = distributor;
    }

    public EntityBubble(World world)
    {
        super(world);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
    }

    //This is pushOutOfBlocks
    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
    	return false;
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
    public void moveEntity(double par1, double par3, double par5)
    {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_180426_a(double x, double y, double z, float yaw, float pitch, int i, boolean b)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void onEntityUpdate()
    {
        if (this.distributor != null)
        {
            final Vector3 vec = new Vector3(this.distributor);

            this.posX = vec.x + 0.5D;
            this.posY = vec.y + 1.0D;
            this.posZ = vec.z + 0.5D;
        }

        super.onEntityUpdate();

        final TileEntity tileAt = this.worldObj.getTileEntity(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1.0), MathHelper.floor_double(this.posZ)));

        if (tileAt instanceof TileEntityOxygenDistributor)
        {
            this.distributor = (TileEntityOxygenDistributor) tileAt;
        }
        else if (tileAt == null)
        {
            if (this.distributor != null)
            {
                this.distributor.oxygenBubble = null;
            }

            this.distributor = null;

            if (!this.worldObj.isRemote)
            {
                this.setDead();
            }
        }

        if (!this.worldObj.isRemote)
        {
            if (this.distributor != null && (this.distributor.oxygenBubble == null || !this.distributor.oxygenBubble.equals(this)))
            {
                this.distributor.oxygenBubble = this;
            }

            if (this.distributor == null)
            {
                this.setDead();
            }

            if (tileAt == null)
            {
                this.setDead();
            }
        }

        if (!this.worldObj.isRemote && this.distributor != null)
        {
            if (this.distributor.getEnergyStoredGC() > 0.0F && this.distributor.storedOxygen > this.distributor.oxygenPerTick)
            {
                this.size += 0.01F;
            }
            else
            {
                this.size -= 0.1F;
            }

            this.size = Math.min(Math.max(this.size, 0.0F), 10.0F);
        }

        if (this.distributor != null)
        {
            final Vector3 vec = new Vector3(this.distributor);

            this.posX = vec.x + 0.5D;
            this.posY = vec.y + 1.0D;
            this.posZ = vec.z + 0.5D;
        }
    }

    @Override
    public void handlePacketData(Side side, EntityPlayer player)
    {
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    public void setSize(float bubbleSize)
    {
        this.size = bubbleSize;
    }

    @Override
    public float getSize()
    {
        return this.size;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        if (nbt.getKeySet().contains("bubbleSize"))
        {
            this.size = (float) nbt.getDouble("bubbleSize");
        }
        else
        {
            this.size = nbt.getFloat("bubbleSizeF");
        }

        this.setShouldRender(nbt.getBoolean("ShouldRender"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        if (ConfigManagerCore.enableDebug)
        {
            GCLog.info("Terraformer debug: writing bubble size " + size);
        }
        nbt.setFloat("bubbleSizeF", this.size);
        nbt.setBoolean("ShouldRender", this.shouldRender);
    }

    @Override
    public boolean shouldRender()
    {
        return this.shouldRender;
    }

    public void setShouldRender(boolean shouldRender)
    {
        this.shouldRender = shouldRender;
    }

    @Override
    public boolean isNetworkedEntity()
    {
        return true;
    }

    @Override
    public int getPacketCooldown(Side side)
    {
        return 3;
    }

    @Override
    public void onPacketClient(EntityPlayer player)
    {
    }

    @Override
    public void onPacketServer(EntityPlayer player)
    {
    }

    @Override
    public double getPacketRange()
    {
        return 64.0D;
    }
}
