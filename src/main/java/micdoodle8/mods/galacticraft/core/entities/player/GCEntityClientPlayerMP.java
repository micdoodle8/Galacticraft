package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.event.ZeroGravityEvent;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class GCEntityClientPlayerMP extends EntityClientPlayerMP
{
    boolean lastIsFlying;
    int lastLandingTicks;
    
    public GCEntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetHandlerPlayClient netHandler, StatFileWriter statFileWriter)
    {
        super(minecraft, world, session, netHandler, statFileWriter);
    }

    @Override
    public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
    {
        if (!ClientProxyCore.playerClientHandler.wakeUpPlayer(this, par1, par2, par3))
        {
            super.wakeUpPlayer(par1, par2, par3);
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock()
    {
        return ClientProxyCore.playerClientHandler.isEntityInsideOpaqueBlock(this, super.isEntityInsideOpaqueBlock());
    }

    @Override
    public void onLivingUpdate()
    {
        ClientProxyCore.playerClientHandler.onLivingUpdatePre(this);
        try {
            if (this.worldObj.provider instanceof IZeroGDimension)
            {
                //  from: EntityPlayerSP
                if (this.sprintingTicksLeft > 0)
                {
                    --this.sprintingTicksLeft;

                    if (this.sprintingTicksLeft == 0)
                    {
                        this.setSprinting(false);
                    }
                }

                if (this.sprintToggleTimer > 0)
                {
                    --this.sprintToggleTimer;
                }

                this.prevTimeInPortal = this.timeInPortal;

                if (this.inPortal)
                {
                    if (this.mc.currentScreen != null)
                    {
                        this.mc.displayGuiScreen((GuiScreen)null);
                    }

                    if (this.timeInPortal == 0.0F)
                    {
                        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
                    }

                    this.timeInPortal += 0.0125F;

                    if (this.timeInPortal >= 1.0F)
                    {
                        this.timeInPortal = 1.0F;
                    }

                    this.inPortal = false;
                }
                else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60)
                {
                    this.timeInPortal += 0.006666667F;

                    if (this.timeInPortal > 1.0F)
                    {
                        this.timeInPortal = 1.0F;
                    }
                }
                else
                {
                    if (this.timeInPortal > 0.0F)
                    {
                        this.timeInPortal -= 0.05F;
                    }

                    if (this.timeInPortal < 0.0F)
                    {
                        this.timeInPortal = 0.0F;
                    }
                }

                if (this.timeUntilPortal > 0)
                {
                    --this.timeUntilPortal;
                }

                boolean flag = this.movementInput.jump;
                float ff = 0.8F;
                boolean flag1 = this.movementInput.moveForward >= ff;
                this.movementInput.updatePlayerMoveState();

                if (this.isUsingItem() && !this.isRiding())
                {
                    this.movementInput.moveStrafe *= 0.2F;
                    this.movementInput.moveForward *= 0.2F;
                    this.sprintToggleTimer = 0;
                }

                //CUSTOM-------------------
                GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
                if (stats.landingTicks > 0)
                {
                    this.ySize = stats.landingYOffset[stats.landingTicks];
                    this.movementInput.moveStrafe *= 0.5F;
                    this.movementInput.moveForward *= 0.5F;
                    if (this.movementInput.sneak && this.ySize < 0.2F)
                    {
                        this.ySize = 0.2F;
                    }
                }
                else if (stats.pjumpticks > 0)
                {
                    this.ySize = 0.01F * stats.pjumpticks;
                }
                else if (this.movementInput.sneak && this.ySize < 0.2F && this.onGround && !stats.inFreefall)
                {
                    this.ySize = 0.2F;
                }
                //-----------END CUSTOM
                    
                this.func_145771_j(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);
                this.func_145771_j(this.posX - (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
                this.func_145771_j(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ - (double)this.width * 0.35D);
                this.func_145771_j(this.posX + (double)this.width * 0.35D, this.boundingBox.minY + 0.5D, this.posZ + (double)this.width * 0.35D);

                boolean flag2 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

                if (this.onGround && !flag1 && this.movementInput.moveForward >= ff && !this.isSprinting() && flag2 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
                {
                    if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.getIsKeyPressed())
                    {
                        this.sprintToggleTimer = 7;
                    }
                    else
                    {
                        this.setSprinting(true);
                    }
                }

                if (!this.isSprinting() && this.movementInput.moveForward >= ff && flag2 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.getIsKeyPressed())
                {
                    this.setSprinting(true);
                }

                if (this.isSprinting() && (this.movementInput.moveForward < ff || this.isCollidedHorizontally || !flag2))
                {
                    this.setSprinting(false);
                }

                //Omit flying toggles - flying will be controlled by freefall status

                if (this.capabilities.isFlying)
                {
                    if (this.movementInput.sneak)
                    {
                        this.motionY -= 0.15D;
                    }

                    if (this.movementInput.jump)
                    {
                        this.motionY += 0.15D;
                    }
                }

                //Omit horse jumping - no horse jumping in space!

                // -from: EntityPlayer

                //Omit fly toggle timer
                if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL && this.getHealth() < this.getMaxHealth() && this.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && this.ticksExisted % 20 * 12 == 0)
                {
                    this.heal(1.0F);
                }

                this.inventory.decrementAnimations();
                this.prevCameraYaw = this.cameraYaw;

                //  from: EntityLivingBase
                if (this.newPosRotationIncrements > 0)
                {
                    double d0 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                    double d1 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                    double d2 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                    double d3 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(d0, d1, d2);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
                if (Math.abs(this.motionX) < 0.005D)
                {
                    this.motionX = 0.0D;
                }

                if (Math.abs(this.motionY) < 0.005D)
                {
                    this.motionY = 0.0D;
                }

                if (Math.abs(this.motionZ) < 0.005D)
                {
                    this.motionZ = 0.0D;
                }

                this.updateEntityActionState();
                this.rotationYawHead = this.rotationYaw;

                if (this.isMovementBlocked())
                {
                    this.isJumping = false;
                    this.moveStrafing = 0.0F;
                    this.moveForward = 0.0F;
                    this.randomYawVelocity = 0.0F;
                }
                this.moveStrafing *= 0.98F;
                this.moveForward *= 0.98F;
                this.randomYawVelocity *= 0.9F;
                if ((this.boundingBox.minY % 1F) == 0.5F) this.boundingBox.minY += 0.00001F;
                this.moveEntityWithHeading(this.moveStrafing, this.moveForward);

                // -from: EntityPlayer
                
                //Omit IAttributeInstance - seems relevant only on server
                
                //Omit        this.jumpMovementFactor = this.speedInAir;
                //(no bounding in space)
                
                float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                float f1 = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;

                if (f > 0.1F)
                {
                    f = 0.1F;
                }

                if (!this.onGround || this.getHealth() <= 0.0F)
                {
                    f = 0.0F;
                }

                if (this.onGround || this.getHealth() <= 0.0F)
                {
                    f1 = 0.0F;
                }

                this.cameraYaw += (f - this.cameraYaw) * 0.4F;
                this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;

                if (this.getHealth() > 0.0F)
                {
                    AxisAlignedBB axisalignedbb = null;

                    if (this.ridingEntity != null && !this.ridingEntity.isDead)
                    {
                        axisalignedbb = this.boundingBox.func_111270_a(this.ridingEntity.boundingBox).expand(1.0D, 0.0D, 1.0D);
                    }
                    else
                    {
                        axisalignedbb = this.boundingBox.expand(1.0D, 0.5D, 1.0D);
                    }

                    List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

                    if (list != null)
                    {
                        for (int i = 0; i < list.size(); ++i)
                        {
                            Entity entity = (Entity)list.get(i);

                            if (!entity.isDead)
                            {
                                entity.onCollideWithPlayer(this);
                            }
                        }
                    }
                }

                //  from: EntityPlayerSP
                //(modified CUSTOM)
                if (this.lastIsFlying != this.capabilities.isFlying)
                {
                    this.lastIsFlying = this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                }
            }
            else
                super.onLivingUpdate();
        } catch (RuntimeException e)
        {
            FMLLog.severe("A mod has crashed while Minecraft was doing a normal player tick update.  See details below.  GCEntityClientPlayerMP is in this because that is the player class name when Galacticraft is installed.  This is =*NOT*= a bug in Galacticraft, please report it to the mod indicated by the first lines of the crash report.");
            throw (e);
        }
        ClientProxyCore.playerClientHandler.onLivingUpdatePost(this);
    }

    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
        super.moveEntity(par1, par3, par5);
        ClientProxyCore.playerClientHandler.moveEntity(this, par1, par3, par5);
    }

    @Override
    public void onUpdate()
    {
        ClientProxyCore.playerClientHandler.onUpdate(this);
        super.onUpdate();
    }

    @Override
    public boolean isSneaking()
    {
        if (this.worldObj.provider instanceof IZeroGDimension)
        {
            ZeroGravityEvent zeroGEvent = new ZeroGravityEvent.SneakOverride(this);
            MinecraftForge.EVENT_BUS.post(zeroGEvent);
            if (zeroGEvent.isCanceled())
            {
                return super.isSneaking();
            }

            GCPlayerStatsClient stats = GCPlayerStatsClient.get(this);
            if (stats.landingTicks > 0)
               {
                if (this.lastLandingTicks == 0)
                    this.lastLandingTicks = stats.landingTicks;
                
                return stats.landingTicks < this.lastLandingTicks;
                }
            else
                this.lastLandingTicks = 0;
            if (stats.pjumpticks > 0) return true;
            if (ClientProxyCore.sneakRenderOverride)
            {
                if (FreefallHandler.testFreefall(this)) return false;
                if (stats.inFreefall) return false;
            }
        }
        return super.isSneaking();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getBedOrientationInDegrees()
    {
        return ClientProxyCore.playerClientHandler.getBedOrientationInDegrees(this, super.getBedOrientationInDegrees());
    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void setVelocity(double xx, double yy, double zz)
//    {
//      if (this.worldObj.provider instanceof WorldProviderOrbit)
//      {
//          ((WorldProviderOrbit)this.worldObj.provider).setVelocityClient(this, xx, yy, zz);   
//      }
//      super.setVelocity(xx, yy, zz);
//    }
//

    @Override
    public void setInPortal()
    {
        if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider))
        {
            super.setInPortal();
        }
    }
}
