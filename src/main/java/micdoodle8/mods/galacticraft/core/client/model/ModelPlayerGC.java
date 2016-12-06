package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModelPlayerGC extends ModelPlayer
{
    public static final ResourceLocation oxygenMaskTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/oxygen.png");
    public static final ResourceLocation playerTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/player.png");

    public ModelPlayerGC(float var1, boolean smallArms)
    {
        super(var1, smallArms);
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        final EntityPlayer player = (EntityPlayer) par7Entity;
        final ItemStack currentItemStack = player.inventory.getCurrentItem();

        if (!par7Entity.onGround && par7Entity.worldObj.provider instanceof IGalacticraftWorldProvider && par7Entity.ridingEntity == null && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
        {
            float speedModifier = 0.1162F * 2;

            float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
            float rightMod = this.heldItemRight != 0 ? 1 : 2;
            this.bipedRightArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * rightMod * par2 * 0.5F;
            this.bipedLeftArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            this.bipedRightArm.rotateAngleX += -angularSwingArm * 4.0F * par2 * 0.5F;
            this.bipedLeftArm.rotateAngleX += angularSwingArm * 4.0F * par2 * 0.5F;
            this.bipedLeftLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
            this.bipedLeftLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2 + (float) Math.PI) * 1.4F * par2;
            this.bipedRightLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            this.bipedRightLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
        }

        PlayerGearData gearData = ModelPlayerGC.getGearData(player);

        if (gearData != null)
        {
            if (gearData.getParachute() != null)
            {
                // Parachute is equipped
                this.bipedLeftArm.rotateAngleX += (float) Math.PI;
                this.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
                this.bipedRightArm.rotateAngleX += (float) Math.PI;
                this.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
            }
        }

        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof IHoldableItem)
        {
            Item heldItem = player.inventory.getCurrentItem().getItem();
            IHoldableItem holdableItem = (IHoldableItem) heldItem;
            IHoldableItemCustom holdableItemCustom = heldItem instanceof IHoldableItemCustom ? (IHoldableItemCustom) heldItem : null;

            if (holdableItem.shouldHoldLeftHandUp(player))
            {
                Vector3 angle = null;

                if (holdableItemCustom != null)
                {
                    angle = holdableItemCustom.getLeftHandRotation(player);
                }

                if (angle == null)
                {
                    angle = new Vector3((float) Math.PI + 0.3F, 0.0F, (float) Math.PI / 10.0F);
                }

                this.bipedLeftArm.rotateAngleX = angle.floatX();
                this.bipedLeftArm.rotateAngleY = angle.floatY();
                this.bipedLeftArm.rotateAngleZ = angle.floatZ();
            }

            if (holdableItem.shouldHoldRightHandUp(player))
            {
                Vector3 angle = null;

                if (holdableItemCustom != null)
                {
                    angle = holdableItemCustom.getRightHandRotation(player);
                }

                if (angle == null)
                {
                    angle = new Vector3((float) Math.PI + 0.3F, 0.0F, (float) Math.PI / 10.0F);
                }

                this.bipedRightArm.rotateAngleX = angle.floatX();
                this.bipedRightArm.rotateAngleY = angle.floatY();
                this.bipedRightArm.rotateAngleZ = angle.floatZ();
            }

            if (player.onGround && holdableItem.shouldCrouch(player))
            {
                this.bipedBody.rotateAngleX = 0.5F;
                this.bipedRightLeg.rotationPointZ = 4.0F;
                this.bipedLeftLeg.rotationPointZ = 4.0F;
                this.bipedRightLeg.rotationPointY = 9.0F;
                this.bipedLeftLeg.rotationPointY = 9.0F;
                this.bipedHead.rotationPointY = 1.0F;
                this.bipedHeadwear.rotationPointY = 1.0F;
            }
        }

        final List<?> l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.fromBounds(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

        for (int i = 0; i < l.size(); i++)
        {
            final Entity e = (Entity) l.get(i);

            if (e instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) e;

                if (ship.riddenByEntity != null && !(ship.riddenByEntity).equals(player) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
                {
                    this.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
                    this.bipedRightArm.rotateAngleX = (float) Math.PI;
                    break;
                }
            }
        }
    }

    public static PlayerGearData getGearData(EntityPlayer player)
    {
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getName());

        if (gearData == null)
        {
            String id = player.getGameProfile().getName();

            if (!ClientProxyCore.gearDataRequests.contains(id))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, player.worldObj.provider.getDimensionId(), new Object[] { id }));
                ClientProxyCore.gearDataRequests.add(id);
            }
        }

        return gearData;
    }
}
