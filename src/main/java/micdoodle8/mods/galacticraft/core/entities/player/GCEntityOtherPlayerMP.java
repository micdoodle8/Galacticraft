package micdoodle8.mods.galacticraft.core.entities.player;

import com.mojang.authlib.GameProfile;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GCEntityOtherPlayerMP extends EntityOtherPlayerMP
{
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public GCEntityOtherPlayerMP(World par1World, GameProfile profile)
    {
        super(par1World, profile);
    }

    @Override
    public ResourceLocation getLocationCape()
    {
        if (this.ridingEntity instanceof EntitySpaceshipBase)
        {
            // Don't draw any cape if riding a rocket (the cape renders outside the rocket model!)
            return null;
        }
        
        ResourceLocation vanillaCape = super.getLocationCape();

        if (!this.checkedCape)
        {
            NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
            this.galacticraftCape = ClientProxyCore.capeMap.get(networkplayerinfo.getGameProfile().getName());
            this.checkedCape = true;
        }

        if ((ConfigManagerCore.overrideCapes || vanillaCape == null) && galacticraftCape != null)
        {
            return galacticraftCape;
        }

        return vanillaCape;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks)
    {
        double height = this.posY + (double)this.getEyeHeight();
        if (height > 255D) height = 255D;
        BlockPos blockpos = new BlockPos(this.posX, height, this.posZ);
        return this.worldObj.isBlockLoaded(blockpos) ? this.worldObj.getCombinedLight(blockpos, 0) : 0;
    }

    @Override
    public boolean isSneaking()
    {
        if (EventHandlerClient.sneakRenderOverride && !(this.worldObj.provider instanceof IZeroGDimension))
        {
            if (this.onGround && this.inventory.getCurrentItem() != null && this.inventory.getCurrentItem().getItem() instanceof IHoldableItem && !(this.ridingEntity instanceof ICameraZoomEntity))
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
