package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GCEntityOtherPlayerMP extends RemoteClientPlayerEntity
{
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public GCEntityOtherPlayerMP(ClientWorld world, GameProfile profile)
    {
        super(world, profile);
    }

    @Override
    public ResourceLocation getLocationCape()
    {
        if (this.getRidingEntity() instanceof EntitySpaceshipBase)
        {
            // Don't draw any cape if riding a rocket (the cape renders outside the rocket model!)
            return null;
        }

        ResourceLocation vanillaCape = super.getLocationCape();

        if (!this.checkedCape)
        {
            NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getGameProfile().getId().toString().replace("-", ""));
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes.get() || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public int getBrightnessForRender()
//    {
//        double height = this.posY + (double) this.getEyeHeight();
//        if (height > 255D)
//        {
//            height = 255D;
//        }
//        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
//        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
//    }

    @Override
    public boolean isSneaking()
    {
        if (EventHandlerClient.sneakRenderOverride && !(this.world.getDimension() instanceof IZeroGDimension))
        {
            if (this.onGround && this.inventory.getCurrentItem() != null && this.inventory.getCurrentItem().getItem() instanceof IHoldableItem && !(this.getRidingEntity() instanceof ICameraZoomEntity))
            {
                IHoldableItem holdableItem = (IHoldableItem) this.inventory.getCurrentItem().getItem();

                if (holdableItem.shouldCrouch(this))
                {
                    return true;
                }
            }
        }
        return super.isSneaking();
    }
}
