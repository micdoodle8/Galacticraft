package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

public class ModelBipedGC extends ModelBiped
{
    public ModelBipedGC(float var1)
    {
        super(var1);
    }

    public static void setRotationAngles(ModelBiped biped, float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        final EntityPlayer player = (EntityPlayer) par7Entity;
        final ItemStack currentItemStack = player.inventory.getCurrentItem();

        if (!par7Entity.onGround && par7Entity.world.provider instanceof IGalacticraftWorldProvider && par7Entity.getRidingEntity() == null && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
        {
            float speedModifier = 0.1162F * 2;

            float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
            float rightMod = biped.rightArmPose == ArmPose.ITEM ? 1 : 2;
            biped.bipedRightArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * rightMod * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            biped.bipedRightArm.rotateAngleX += -angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX += angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
            biped.bipedLeftLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2 + (float) Math.PI) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
        }

        PlayerGearData gearData = ModelBipedGC.getGearData(player);

        if (gearData != null)
        {
            if (gearData.getParachute() != null)
            {
                // Parachute is equipped
                biped.bipedLeftArm.rotateAngleX += (float) Math.PI;
                biped.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
                biped.bipedRightArm.rotateAngleX += (float) Math.PI;
                biped.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
            }
        }

        ItemStack heldItemStack = null;

        for (EnumHand hand : EnumHand.values())
        {
            ItemStack item = player.getHeldItem(hand);
            if (item != null && item.getItem() instanceof IHoldableItem)
            {
                heldItemStack = item;
            }
        }

        if (heldItemStack != null)
        {
            Item heldItem = heldItemStack.getItem();
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

                biped.bipedLeftArm.rotateAngleX = angle.floatX();
                biped.bipedLeftArm.rotateAngleY = angle.floatY();
                biped.bipedLeftArm.rotateAngleZ = angle.floatZ();
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
                    angle = new Vector3((float) Math.PI + 0.3F, 0.0F, (float) -Math.PI / 10.0F);
                }

                biped.bipedRightArm.rotateAngleX = angle.floatX();
                biped.bipedRightArm.rotateAngleY = angle.floatY();
                biped.bipedRightArm.rotateAngleZ = angle.floatZ();
            }

            if (player.onGround && holdableItem.shouldCrouch(player))
            {
                GlStateManager.translate(0.0F, player.isSneaking() ? 0.0F : 0.1F, 0.0F);
                biped.bipedBody.rotateAngleX = 0.5F;
                biped.bipedRightLeg.rotationPointZ = 4.0F;
                biped.bipedLeftLeg.rotationPointZ = 4.0F;
                biped.bipedRightLeg.rotationPointY = 9.0F;
                biped.bipedLeftLeg.rotationPointY = 9.0F;
                biped.bipedHead.rotationPointY = 1.0F;
                biped.bipedHeadwear.rotationPointY = 1.0F;
            }
        }

        final List<?> l = player.world.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

        for (int i = 0; i < l.size(); i++)
        {
            final Entity e = (Entity) l.get(i);

            if (e instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) e;

                if (!ship.getPassengers().isEmpty() && !ship.getPassengers().contains(player) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
                {
                    biped.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
                    biped.bipedRightArm.rotateAngleX = (float) Math.PI;
                    break;
                }
            }
        }

        if (player.isPlayerSleeping() && GalacticraftCore.isPlanetsLoaded)
        {
            RenderPlayerGC.RotatePlayerEvent event = new RenderPlayerGC.RotatePlayerEvent((AbstractClientPlayer) player);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.vanillaOverride && (event.shouldRotate == null || event.shouldRotate))
            {
                biped.bipedHead.rotateAngleX = (float) (20.0F - Math.sin(player.ticksExisted / 10.0F) / 7.0F);
                biped.bipedHead.rotateAngleY = 0.0F;
                biped.bipedHead.rotateAngleZ = 0.0F;
                biped.bipedLeftArm.rotateAngleX = 0.0F;
                biped.bipedLeftArm.rotateAngleY = 0.0F;
                biped.bipedLeftArm.rotateAngleZ = 0.0F;
                biped.bipedRightArm.rotateAngleX = 0.0F;
                biped.bipedRightArm.rotateAngleY = 0.0F;
                biped.bipedRightArm.rotateAngleZ = 0.0F;
            }
        }

        if (biped instanceof ModelPlayer)
        {
            copyModelAngles(biped.bipedLeftLeg, ((ModelPlayer) biped).bipedLeftLegwear);
            copyModelAngles(biped.bipedRightLeg, ((ModelPlayer) biped).bipedRightLegwear);
            copyModelAngles(biped.bipedLeftArm, ((ModelPlayer) biped).bipedLeftArmwear);
            copyModelAngles(biped.bipedRightArm, ((ModelPlayer) biped).bipedRightArmwear);
            copyModelAngles(biped.bipedBody, ((ModelPlayer) biped).bipedBodyWear);
            copyModelAngles(biped.bipedHead, ((ModelPlayer) biped).bipedHeadwear);
        }
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        ModelBipedGC.setRotationAngles(this, par1, par2, par3, par4, par5, par6, par7Entity);
    }

    public static PlayerGearData getGearData(EntityPlayer player)
    {
        PlayerGearData gearData = ClientProxyCore.playerItemData.get(player.getName());

        if (gearData == null)
        {
            String id = player.getGameProfile().getName();

            if (!ClientProxyCore.gearDataRequests.contains(id))
            {
                GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, GCCoreUtil.getDimensionID(player.world), new Object[] { id }));
                ClientProxyCore.gearDataRequests.add(id);
            }
        }

        return gearData;
    }
}
