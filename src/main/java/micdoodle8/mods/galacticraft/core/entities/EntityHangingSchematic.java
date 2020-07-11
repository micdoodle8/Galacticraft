package micdoodle8.mods.galacticraft.core.entities;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityHangingSchematic extends HangingEntity
{
    public int schematic;
    private boolean sendToClient;
    private int tickCounter1;

    protected EntityHangingSchematic(EntityType<? extends EntityHangingSchematic> type, World world)
    {
        super(type, world);
    }

    public EntityHangingSchematic(EntityType<? extends EntityHangingSchematic> type, World world, BlockPos pos, Direction facing, int schematicType)
    {
        super(type, world);
        this.schematic = schematicType;
        this.updateFacingWithBoundingBox(facing);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return new SSpawnObjectPacket(this);
    }

    @Override
    public void tick()
    {
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();

        if (this.sendToClient)
        {
            this.sendToClient = false;
            this.sendToClient(this.world, this.hangingPosition);
        }

        if (this.tickCounter1++ == 10)
        {
            this.tickCounter1 = 0;

            if (!this.world.isRemote && this.isAlive() && !this.onValidSurface())
            {
                this.remove();
                this.onBroken(null);
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT tagCompound)
    {
        tagCompound.putInt("schem", this.schematic);
        super.writeAdditional(tagCompound);
    }

    @Override
    public void readAdditional(CompoundNBT tag)
    {
        this.schematic = tag.getInt("schem");
        super.readAdditional(tag);
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
        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))
        {
            if (brokenEntity instanceof PlayerEntity)
            {
                PlayerEntity entityplayer = (PlayerEntity) brokenEntity;

                if (entityplayer.abilities.isCreativeMode)
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
        BlockPos blockpos = this.hangingPosition.add(x - this.getPosX(), y - this.getPosY(), z - this.getPosZ());
        this.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
    }

    public void sendToClient(World worldIn, BlockPos blockpos)
    {
        DimensionType dimID = GCCoreUtil.getDimensionType(worldIn);
        GCCoreUtil.sendToAllAround(new PacketSimple(EnumSimplePacket.C_SPAWN_HANGING_SCHEMATIC, dimID, new Object[]{blockpos, this.getEntityId(), this.facingDirection.ordinal(), this.schematic}), worldIn, dimID, blockpos, 150D);
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
