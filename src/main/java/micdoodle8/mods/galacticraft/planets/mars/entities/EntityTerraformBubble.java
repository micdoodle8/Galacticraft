package micdoodle8.mods.galacticraft.planets.mars.entities;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.entities.EntityAdvanced;
import micdoodle8.mods.galacticraft.core.entities.IBubble;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityTerraformBubble extends EntityAdvanced implements IBubble
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = true;

    public TileEntityTerraformer terraformer;

    public EntityTerraformBubble(World world, Vector3 mainBlockVec, TileEntityTerraformer terraformer)
    {
        this(world);
        this.posX = mainBlockVec.x + 0.5D;
        this.posY = mainBlockVec.y + 1.0D;
        this.posZ = mainBlockVec.z + 0.5D;
        this.terraformer = terraformer;
    }

    public EntityTerraformBubble(World world)
    {
        super(world);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.ignoreFrustumCheck = true;
        this.renderDistanceWeight = 5.0D;
    }

    //	@Override
    //	protected boolean pushOutOfBlocks(double par1, double par3, double par5)
    //	{
    //		return false;
    //	} TODO Find out if this is still necessary

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
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @Override
    public void onUpdate()
    {
        if (this.terraformer != null)
        {
            final Vector3 vec = new Vector3(this.terraformer);

            this.posX = vec.x + 0.5D;
            this.posY = vec.y + 1.0D;
            this.posZ = vec.z + 0.5D;
        }

        super.onUpdate();

        final TileEntity tileAt = this.worldObj.getTileEntity(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 1.0), MathHelper.floor_double(this.posZ));

        if (tileAt instanceof TileEntityTerraformer)
        {
            this.terraformer = (TileEntityTerraformer) tileAt;
        }

        if (this.terraformer != null && (this.terraformer.terraformBubble == null || this.terraformer.terraformBubble.equals(this)) && !this.worldObj.isRemote)
        {
            this.terraformer.terraformBubble = this;
        }
        else if (!this.worldObj.isRemote)
        {
            this.setDead();
        }

        if (tileAt == null && !this.worldObj.isRemote)
        {
            this.setDead();
        }

        if (this.terraformer != null)
        {
            final Vector3 vec = new Vector3(this.terraformer);

            this.posX = vec.x + 0.5D;
            this.posY = vec.y + 1.0D;
            this.posZ = vec.z + 0.5D;
        }
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return false;
    }

    public void setSize(float bubbleSize)
    {
        if (this.terraformer == null)
        {
            return;
        }

        this.terraformer.size = bubbleSize;
    }

    @Override
    public float getSize()
    {
        if (this.terraformer == null)
        {
            return 0.0F;
        }

        return this.terraformer.size;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        this.setShouldRender(nbt.getBoolean("ShouldRender"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
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
