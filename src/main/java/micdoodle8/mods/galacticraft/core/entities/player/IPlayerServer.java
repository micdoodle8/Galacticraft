package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;

public interface IPlayerServer
{
    void updateRiddenPre(ServerPlayerEntity player);

    void updateRiddenPost(ServerPlayerEntity player);

    boolean dismountEntity(ServerPlayerEntity player, Entity par1Entity);

    void move(ServerPlayerEntity player, MoverType type, Vec3d pos);

//    boolean wakeUpPlayer(ServerPlayerEntity player, boolean immediately, boolean updateWorldFlag, boolean setSpawn); TODO Cryo chamber

    float attackEntityFrom(ServerPlayerEntity player, DamageSource par1DamageSource, float par2);

    void knockBack(ServerPlayerEntity player, Entity p_70653_1_, float p_70653_2_, double impulseX, double impulseZ);

    boolean isSpectator(ServerPlayerEntity player);

    void setNoClip(ServerPlayerEntity player, boolean noClip);
}
