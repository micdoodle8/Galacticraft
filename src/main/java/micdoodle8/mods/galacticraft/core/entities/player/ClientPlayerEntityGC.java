package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.client.EventHandlerClient;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.client.CEntityActionPacket;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.List;

public class ClientPlayerEntityGC extends ClientPlayerEntity
{
    private boolean lastIsFlying;
    private boolean sneakLast;
    private int lastLandingTicks;
    private boolean checkedCape = false;
    private ResourceLocation galacticraftCape = null;

    public ClientPlayerEntityGC(Minecraft mcIn, ClientWorld worldIn, ClientPlayNetHandler netHandler, StatisticsManager statFileWriter, ClientRecipeBook book)
    {
        super(mcIn, worldIn, netHandler, statFileWriter, book);
    }

//    @Override
//    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn)
//    {
////        if (!ClientProxyCore.playerClientHandler.wakeUpPlayer(this, immediately, updateWorldFlag, setSpawn))
////        {
////            super.wakeUpPlayer(immediately, updateWorldFlag, setSpawn);
////        } TODO Cryo chamber
//    }

    @Override
    public void wakeUp()
    {
        super.wakeUp();
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return ClientProxyCore.playerClientHandler.isEntityInsideOpaqueBlock(this, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public boolean isServerWorld()
    {
        return true;
    }


    @Override
    public void livingTick()
    {
        ClientProxyCore.playerClientHandler.onTickPre(this);
        try
        {
            if (this.world.getDimension() instanceof IZeroGDimension)
            {

                //  from: ClientPlayerEntity
                ++this.sprintingTicksLeft;
                if (this.sprintToggleTimer > 0) {
                    --this.sprintToggleTimer;
                }

                this.prevTimeInPortal = this.timeInPortal;
                if (this.inPortal) {
                    if (this.mc.currentScreen != null && !this.mc.currentScreen.isPauseScreen()) {
                        if (this.mc.currentScreen instanceof ContainerScreen) {
                            this.closeScreen();
                        }

                        this.mc.displayGuiScreen((Screen)null);
                    }

                    if (this.timeInPortal == 0.0F) {
                        this.mc.getSoundHandler().play(SimpleSound.master(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F));
                    }

                    this.timeInPortal += 0.0125F;
                    if (this.timeInPortal >= 1.0F) {
                        this.timeInPortal = 1.0F;
                    }

                    this.inPortal = false;
                } else if (this.isPotionActive(Effects.NAUSEA) && this.getActivePotionEffect(Effects.NAUSEA).getDuration() > 60) {
                    this.timeInPortal += 0.006666667F;
                    if (this.timeInPortal > 1.0F) {
                        this.timeInPortal = 1.0F;
                    }
                } else {
                    if (this.timeInPortal > 0.0F) {
                        this.timeInPortal -= 0.05F;
                    }

                    if (this.timeInPortal < 0.0F) {
                        this.timeInPortal = 0.0F;
                    }
                }

                this.decrementTimeUntilPortal();

                boolean flag = this.movementInput.jump;
                boolean flag1 = this.movementInput.sneaking;
                boolean flag2 = this.movingForward();
                this.movementInput.func_225607_a_(this.func_228354_I_());
                net.minecraftforge.client.ForgeHooksClient.onInputUpdate(this, this.movementInput);
                this.mc.getTutorial().handleMovement(this.movementInput);
                if (this.isHandActive() && !this.isPassenger()) {
                    this.movementInput.moveStrafe *= 0.2F;
                    this.movementInput.moveForward *= 0.2F;
                    this.sprintToggleTimer = 0;
                }

                //CUSTOM-------------------
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
                if (stats.getLandingTicks() > 0)
                {
                    this.movementInput.moveStrafe *= 0.5F;
                    this.movementInput.moveForward *= 0.5F;
                }
                //-----------END CUSTOM

                // Omit auto-jump in zero-g
                boolean flag3 = false;

                net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent event = new net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent(this);
                if (!this.noClip && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) {
                    this.pushOutOfBlocks(this.getPosX() - (double)this.getWidth() * 0.35D, event.getMinY(), this.getPosZ() + (double)this.getWidth() * 0.35D);
                    this.pushOutOfBlocks(this.getPosX() - (double)this.getWidth() * 0.35D, event.getMinY(), this.getPosZ() - (double)this.getWidth() * 0.35D);
                    this.pushOutOfBlocks(this.getPosX() + (double)this.getWidth() * 0.35D, event.getMinY(), this.getPosZ() - (double)this.getWidth() * 0.35D);
                    this.pushOutOfBlocks(this.getPosX() + (double)this.getWidth() * 0.35D, event.getMinY(), this.getPosZ() + (double)this.getWidth() * 0.35D);
                }

                boolean flag4 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.abilities.allowFlying;
                if ((this.onGround || this.canSwim()) && !flag1 && !flag2 && this.movingForward() && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(Effects.BLINDNESS)) {
                    if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                        this.sprintToggleTimer = 7;
                    } else {
                        this.setSprinting(true);
                    }
                }

                if (!this.isSprinting() && (!this.isInWater() || this.canSwim()) && this.movingForward() && flag4 && !this.isHandActive() && !this.isPotionActive(Effects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                    this.setSprinting(true);
                }

                if (this.isSprinting()) {
                    boolean flag5 = !this.movementInput.func_223135_b() || !flag4;
                    boolean flag6 = flag5 || this.collidedHorizontally || this.isInWater() && !this.canSwim();
                    if (this.isSwimming()) {
                        if (!this.onGround && !this.movementInput.sneaking && flag5 || !this.isInWater()) {
                            this.setSprinting(false);
                        }
                    } else if (flag6) {
                        this.setSprinting(false);
                    }
                }

                boolean flag7 = false;
                if (this.abilities.allowFlying) {
                    if (this.mc.playerController.isSpectatorMode()) {
                        if (!this.abilities.isFlying) {
                            this.abilities.isFlying = true;
                            flag7 = true;
                            this.sendPlayerAbilities();
                        }
                    } else if (!flag && this.movementInput.jump && !flag3) {
                        if (this.flyToggleTimer == 0) {
                            this.flyToggleTimer = 7;
                        } else if (!this.isSwimming()) {
                            this.abilities.isFlying = !this.abilities.isFlying;
                            flag7 = true;
                            this.sendPlayerAbilities();
                            this.flyToggleTimer = 0;
                        }
                    }
                }

                if (this.movementInput.jump && !flag7 && !flag && !this.abilities.isFlying && !this.isPassenger() && !this.isOnLadder()) {
                    ItemStack itemstack = this.getItemStackFromSlot(EquipmentSlotType.CHEST);
                    if (itemstack.getItem() == Items.ELYTRA && ElytraItem.isUsable(itemstack) && this.tryToStartFallFlying()) {
                        this.connection.sendPacket(new CEntityActionPacket(this, CEntityActionPacket.Action.START_FALL_FLYING));
                    }
                }

                // Omit elytra update in zero-g
                if (this.isInWater() && this.movementInput.sneaking) {
                    this.handleFluidSneak();
                }

                if (this.areEyesInFluid(FluidTags.WATER)) {
                    int i = this.isSpectator() ? 10 : 1;
                    this.counterInWater = MathHelper.clamp(this.counterInWater + i, 0, 600);
                } else if (this.counterInWater > 0) {
                    this.areEyesInFluid(FluidTags.WATER);
                    this.counterInWater = MathHelper.clamp(this.counterInWater - 10, 0, 600);
                }

                if (this.abilities.isFlying && this.isCurrentViewEntity()) {
                    int j = 0;
                    if (this.movementInput.sneaking) {
                        --j;
                    }

                    if (this.movementInput.jump) {
                        ++j;
                    }

                    if (j != 0) {
                        this.setMotion(this.getMotion().add(0.0D, (double)((float)j * this.abilities.getFlySpeed() * 3.0F), 0.0D));
                    }
                }

                //Omit horse jumping - no horse jumping in space!

                // -from: PlayerEntity

                //Omit fly toggle timer

                if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
                    if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                        this.heal(1.0F);
                    }

                    if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                        this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
                    }
                }

                this.inventory.tick();
                this.prevCameraYaw = this.cameraYaw;

                //  from: LivingEntity
                if (this.jumpTicks > 0) {
                    --this.jumpTicks;
                }

                if (this.canPassengerSteer()) {
                    this.newPosRotationIncrements = 0;
                    this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
                }

                if (this.newPosRotationIncrements > 0) {
                    double d0 = this.getPosX() + (this.interpTargetX - this.getPosX()) / (double)this.newPosRotationIncrements;
                    double d2 = this.getPosY() + (this.interpTargetY - this.getPosY()) / (double)this.newPosRotationIncrements;
                    double d4 = this.getPosZ() + (this.interpTargetZ - this.getPosZ()) / (double)this.newPosRotationIncrements;
                    double d6 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + d6 / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(d0, d2, d4);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                } else if (!this.isServerWorld()) {
                    this.setMotion(this.getMotion().scale(0.98D));
                }

                if (this.interpTicksHead > 0) {
                    this.rotationYawHead = (float)((double)this.rotationYawHead + MathHelper.wrapDegrees(this.interpTargetHeadYaw - (double)this.rotationYawHead) / (double)this.interpTicksHead);
                    --this.interpTicksHead;
                }

                Vec3d vec3d = this.getMotion();
                double d1 = vec3d.x;
                double d3 = vec3d.y;
                double d5 = vec3d.z;
                if (Math.abs(vec3d.x) < 0.003D) {
                    d1 = 0.0D;
                }

                if (Math.abs(vec3d.y) < 0.003D) {
                    d3 = 0.0D;
                }

                if (Math.abs(vec3d.z) < 0.003D) {
                    d5 = 0.0D;
                }

                this.setMotion(d1, d3, d5);
                this.world.getProfiler().startSection("ai");
                if (this.isMovementBlocked()) {
                    this.isJumping = false;
                    this.moveStrafing = 0.0F;
                    this.moveForward = 0.0F;
                } else if (this.isServerWorld()) {
                    this.world.getProfiler().startSection("newAi");
                    this.updateEntityActionState();
                    this.world.getProfiler().endSection();
                }

                this.world.getProfiler().endSection();
                this.world.getProfiler().startSection("jump");
                if (this.isJumping) {
                    if (!(this.submergedHeight > 0.0D) || this.onGround && !(this.submergedHeight > 0.4D)) {
                        if (this.isInLava()) {
                            this.handleFluidJump(FluidTags.LAVA);
                        } else if ((this.onGround || this.submergedHeight > 0.0D && this.submergedHeight <= 0.4D) && this.jumpTicks == 0) {
                            this.jump();
                            this.jumpTicks = 10;
                        }
                    } else {
                        this.handleFluidJump(FluidTags.WATER);
                    }
                } else {
                    this.jumpTicks = 0;
                }

                this.world.getProfiler().endSection();
                this.world.getProfiler().startSection("travel");
                this.moveStrafing *= 0.98F;
                this.moveForward *= 0.98F;

                // CUSTOM--------------
                AxisAlignedBB aABB = this.getBoundingBox();
                if ((aABB.minY % 1D) == 0.5D)
                {
                    this.setBoundingBox(aABB.offset(0D, 0.00001D, 0D));
                }
                //-----------END CUSTOM

                // Omit elytra in zero-g

                AxisAlignedBB axisalignedbb = this.getBoundingBox();
                this.travel(new Vec3d((double)this.moveStrafing, (double)this.moveVertical, (double)this.moveForward));
                this.world.getProfiler().endSection();
                this.world.getProfiler().startSection("push");
                if (this.spinAttackDuration > 0) {
                    --this.spinAttackDuration;
                    this.updateSpinAttack(axisalignedbb, this.getBoundingBox());
                }

                this.collideWithNearbyEntities();
                this.world.getProfiler().endSection();

                IAttributeInstance iattributeinstance = this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                if (!this.world.isRemote) {
                    iattributeinstance.setBaseValue((double)this.abilities.getWalkSpeed());
                }

//                this.jumpMovementFactor = 0.02F;
//                if (this.isSprinting()) {
//                    this.jumpMovementFactor = (float)((double)this.jumpMovementFactor + 0.005999999865889549D);
//                }

                this.setAIMoveSpeed((float)iattributeinstance.getValue());
                float f;
                if (this.onGround && !(this.getHealth() <= 0.0F) && !this.isSwimming()) {
                    f = Math.min(0.1F, MathHelper.sqrt(horizontalMag(this.getMotion())));
                } else {
                    f = 0.0F;
                }

                this.cameraYaw += (f - this.cameraYaw) * 0.4F;
                if (this.getHealth() > 0.0F && !this.isSpectator()) {
                    AxisAlignedBB axisalignedbb1;
                    if (this.isPassenger() && !this.getRidingEntity().removed) {
                        axisalignedbb1 = this.getBoundingBox().union(this.getRidingEntity().getBoundingBox()).grow(1.0D, 0.0D, 1.0D);
                    } else {
                        axisalignedbb1 = this.getBoundingBox().grow(1.0D, 0.5D, 1.0D);
                    }

                    List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb1);

                    for(int i = 0; i < list.size(); ++i) {
                        Entity entity = list.get(i);
                        if (!entity.removed) {
                            entity.onCollideWithPlayer(this);
                        }
                    }
                }

                this.playShoulderEntityAmbientSound(this.getLeftShoulderEntity());
                this.playShoulderEntityAmbientSound(this.getRightShoulderEntity());
                if (!this.world.isRemote && (this.fallDistance > 0.5F || this.isInWater()) || this.abilities.isFlying || this.isSleeping()) {
                    this.spawnShoulderEntities();
                }

                //  from: ClientPlayerEntity
                //(modified CUSTOM)
                if (this.lastIsFlying != this.abilities.isFlying)
                {
                    this.lastIsFlying = this.abilities.isFlying;
                    this.sendPlayerAbilities();
                }
            }
            else
            {
                super.livingTick();
            }
        }
        catch (RuntimeException e)
        {
            LogManager.getLogger().error("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onTickPost(this);
    }

    @Override
    public boolean isElytraFlying() {
        return !(this.world.getDimension() instanceof IZeroGDimension) && super.isElytraFlying();
    }

    private boolean movingForward() {
        double d0 = 0.8D;
        return this.canSwim() ? this.movementInput.func_223135_b() : (double)this.movementInput.moveForward >= 0.8D;
    }

    private void playShoulderEntityAmbientSound(@Nullable CompoundNBT p_192028_1_) {
        if (p_192028_1_ != null && !p_192028_1_.contains("Silent") || !p_192028_1_.getBoolean("Silent")) {
            String s = p_192028_1_.getString("id");
            EntityType.byKey(s).filter((p_213830_0_) -> {
                return p_213830_0_ == EntityType.PARROT;
            }).ifPresent((p_213834_1_) -> {
                ParrotEntity.playAmbientSound(this.world, this);
            });
        }

    }

    @Override
    public void move(MoverType type, Vec3d pos)
    {
        super.move(type, pos);
        ClientProxyCore.playerClientHandler.move(this, type, pos);
    }

//    @Override
//    public void onUpdate()
//    {
//        ClientProxyCore.playerClientHandler.onUpdate(this);
//        super.onUpdate();
//    }

    @Override
    public boolean isSneaking()
    {
        if (this.world.getDimension() instanceof IZeroGDimension)
        {
            ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.SneakOverride(this);
            MinecraftForge.EVENT_BUS.post(zeroGEvent);
            if (zeroGEvent.isCanceled())
            {
                return super.isSneaking();
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
            if (stats.getLandingTicks() > 0)
            {
                if (this.lastLandingTicks == 0)
                {
                    this.lastLandingTicks = stats.getLandingTicks();
                }

                this.sneakLast = stats.getLandingTicks() < this.lastLandingTicks;
                return sneakLast;
            }
            else
            {
                this.lastLandingTicks = 0;
            }
//            if (stats.getFreefallHandler().pjumpticks > 0)
//            {
//                this.sneakLast = true;
//                return true;
//            } TODO Freefall
            if (EventHandlerClient.sneakRenderOverride)
            {
                if (this.movementInput != null && this.movementInput.sneaking != this.sneakLast)
                {
                    return false;
                }
                //                if (stats.freefallHandler.testFreefall(this)) return false;
//                if (stats.isInFreefall() || stats.getFreefallHandler().onWall)
//                {
//                    this.sneakLast = false;
//                    return false;
//                } TODO Freefall
            }
            this.sneakLast = this.movementInput != null && this.movementInput.sneaking;
        }
        else
        {
            this.sneakLast = false;
            if (EventHandlerClient.sneakRenderOverride && this.onGround && this.inventory.getCurrentItem() != null && this.inventory.getCurrentItem().getItem() instanceof IHoldableItem && !(this.getRidingEntity() instanceof ICameraZoomEntity))
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

//    @Override
//    public float getEyeHeight(Pose p_213307_1_)
//    {
//        float f = eyeHeight;
//
//        if (this.isPlayerSleeping())
//        {
//            return 0.2F;
//        }
//
//        float ySize = 0.0F;
//        if (this.world.getDimension() instanceof IZeroGDimension)
//        {
//            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
//            if (stats.getLandingTicks() > 0)
//            {
//                ySize = stats.getLandingYOffset()[stats.getLandingTicks()];
//                if (this.movementInput.sneak && ySize < 0.08F)
//                {
//                    ySize = 0.08F;
//                }
//            }
////            else if (stats.getFreefallHandler().pjumpticks > 0)
////            {
////                ySize = 0.01F * stats.getFreefallHandler().pjumpticks;
////            }
////            else if (this.isSneaking() && !stats.isInFreefall() && !stats.getFreefallHandler().onWall)
////            {
////                ySize = 0.08F;
////            } TODO Freefall
//        }
//        else if (this.isSneaking() && this.movementInput != null && this.movementInput.sneak)
//        {
//            ySize = 0.08F;
//        }
//
//        return f - ySize;
//    }

    @Nullable
    @Override
    public Direction getBedDirection()
    {
        return ClientProxyCore.playerClientHandler.getBedDirection(this, super.getBedDirection());
    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void setVelocity(double xx, double yy, double zz)
//    {
//    	if (this.world.getDimension() instanceof WorldProviderOrbit)
//    	{
//    		((WorldProviderOrbit)this.world.getDimension()).setVelocityClient(this, xx, yy, zz);
//    	}
//    	super.setVelocity(xx, yy, zz);
//    }
//

    /*@Override
    public void setInPortal()
    {
    	if (!(this.world.getDimension() instanceof IGalacticraftWorldProvider))
    	{
    		super.setInPortal();
    	}
    } TODO Fix disable of portal */

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
}
