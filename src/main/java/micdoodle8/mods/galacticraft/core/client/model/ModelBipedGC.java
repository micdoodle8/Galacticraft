package micdoodle8.mods.galacticraft.core.client.model;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.item.IHoldableItemCustom;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

public class ModelBipedGC
{
    public static void setRotationAngles(ModelBiped biped, float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        if (!(par7Entity instanceof EntityPlayer)) return;
        final EntityPlayer player = (EntityPlayer) par7Entity;
        final ItemStack currentItemStack = player.inventory.getCurrentItem();
        final float floatPI = 3.1415927F;

        if (!par7Entity.onGround && par7Entity.world.provider instanceof IGalacticraftWorldProvider && par7Entity.getRidingEntity() == null && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
        {
            float speedModifier = 0.1162F * 2;

            float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
            float rightMod = biped.rightArmPose == ModelBiped.ArmPose.ITEM ? 1 : 2;
            biped.bipedRightArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + floatPI) * rightMod * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            biped.bipedRightArm.rotateAngleX += -angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX += angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + floatPI) * 1.4F * par2;
            biped.bipedLeftLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2 + floatPI) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
        }

        PlayerGearData gearData = GalacticraftCore.proxy.getGearData(player);

        if (gearData != null)
        {
            if (gearData.getParachute() != null)
            {
                // Parachute is equipped
                biped.bipedLeftArm.rotateAngleX += floatPI;
                biped.bipedLeftArm.rotateAngleZ += floatPI / 10;
                biped.bipedRightArm.rotateAngleX += floatPI;
                biped.bipedRightArm.rotateAngleZ -= floatPI / 10;
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

        if (heldItemStack != null && !(player.getRidingEntity() instanceof ICameraZoomEntity))
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
                    angle = new Vector3(floatPI + 0.3F, 0.0F, floatPI / 10.0F);
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
                    angle = new Vector3(floatPI + 0.3F, 0.0F, (float) -Math.PI / 10.0F);
                }

                biped.bipedRightArm.rotateAngleX = angle.floatX();
                biped.bipedRightArm.rotateAngleY = angle.floatY();
                biped.bipedRightArm.rotateAngleZ = angle.floatZ();
            }
        }

        for (Entity e : player.world.loadedEntityList)
        {
            if (e instanceof EntityTieredRocket && e.getDistanceSq(player) < 200)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) e;

                if (!ship.getPassengers().isEmpty() && !ship.getPassengers().contains(player) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
                {
                    biped.bipedRightArm.rotateAngleZ -= floatPI / 8F + MathHelper.sin(par3 * 0.9F) * 0.2F;
                    biped.bipedRightArm.rotateAngleX = floatPI;
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

        ModelBiped.copyModelAngles(biped.bipedHead, biped.bipedHeadwear);
    }
}
