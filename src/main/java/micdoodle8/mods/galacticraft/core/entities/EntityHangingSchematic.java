package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHangingSchematic extends EntityHanging
{
    public int schematic;
    private boolean sendToClient;

    public EntityHangingSchematic(World worldIn)
    {
        super(worldIn);
    }

    public EntityHangingSchematic(World worldIn, BlockPos pos, EnumFacing facing, int meta)
    {
        super(worldIn, pos);
        this.schematic = meta;
        this.updateFacingWithBoundingBox(facing);
    }

    private int tickCounter1;
    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if (this.sendToClient)
        {
            this.sendToClient = false;
            this.sendToClient(this.world, this.hangingPosition);
        }

        if (this.tickCounter1++ == 10)
        {
            this.tickCounter1 = 0;

            if (!this.world.isRemote && !this.isDead && !this.onValidSurface())
            {
                this.setDead();
                this.onBroken((Entity)null);
            }
        }
    }
    
    @Override
    public void setDead()
    {
        super.setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("schem", this.schematic);
        super.writeEntityToNBT(tagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        this.schematic = tag.getInteger("schem");
        super.readEntityFromNBT(tag);
        this.setSendToClient();
    }

    @Override
    public int getWidthPixels()
    {
        return 32;
    }

    @Override
    public int getHeightPixels()
    {
        return 32;
    }

    @Override
    public void onBroken(Entity brokenEntity)
    {
        if (this.world.getGameRules().getBoolean("doEntityDrops"))
        {
            if (brokenEntity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)brokenEntity;

                if (entityplayer.capabilities.isCreativeMode)
                {
                    return;
                }
            }

            this.entityDropItem(SchematicRegistry.getSchematicItem(this.schematic), 0.0F);
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return SchematicRegistry.getSchematicItem(this.schematic);
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
        this.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
    }

    public void sendToClient(World worldIn, BlockPos blockpos)
    {
        int dimID = GCCoreUtil.getDimensionID(worldIn);
        GCCoreUtil.sendToAllAround(new PacketSimple(EnumSimplePacket.C_SPAWN_HANGING_SCHEMATIC, dimID, new Object[] { blockpos, this.getEntityId(), this.facingDirection.ordinal(), this.schematic }), worldIn, dimID, blockpos, 150D);
    }
    
    public void setSendToClient()
    {
        this.sendToClient = true;
    }

    @Override
    public void playPlaceSound()
    {
        this.playSound(SoundEvents.ENTITY_PAINTING_PLACE, 1.0F, 1.0F);
    }
}
