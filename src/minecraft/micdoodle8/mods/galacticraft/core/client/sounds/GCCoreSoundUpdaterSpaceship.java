package micdoodle8.mods.galacticraft.core.client.sounds;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

public class GCCoreSoundUpdaterSpaceship implements IUpdatePlayerListBox
{
    private final SoundManager theSoundManager;

    /** Minecart which sound is being updated. */
    private final GCCoreEntitySpaceship theMinecart;

    /** The player that is getting the minecart sound updates. */
    private final EntityPlayerSP thePlayer;
    private boolean playerSPRidingMinecart = false;
    private boolean minecartIsDead = false;
    private boolean minecartIsMoving = false;
    private boolean silent = false;
    private float minecartSoundPitch = 0.0F;
    private float minecartMoveSoundVolume = 0.0F;
    private float minecartRideSoundVolume = 0.0F;
    private double minecartSpeed = 0.0D;

    public GCCoreSoundUpdaterSpaceship(SoundManager par1SoundManager, GCCoreEntitySpaceship par2EntityMinecart, EntityPlayerSP par3EntityPlayerSP)
    {
        this.theSoundManager = par1SoundManager;
        this.theMinecart = par2EntityMinecart;
        this.thePlayer = par3EntityPlayerSP;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
	public void update()
    {
        boolean var1 = false;
        final boolean var2 = this.playerSPRidingMinecart;
        final boolean var3 = this.minecartIsDead;
        final boolean var4 = this.minecartIsMoving;
        final float var5 = this.minecartMoveSoundVolume;
        final float var6 = this.minecartSoundPitch;
        final float var7 = this.minecartRideSoundVolume;
        final double var8 = this.minecartSpeed;
        this.playerSPRidingMinecart = this.thePlayer != null && this.theMinecart.riddenByEntity == this.thePlayer;
        this.minecartIsDead = this.theMinecart.isDead;
        this.minecartSpeed = 20;
        this.minecartIsMoving = this.minecartSpeed >= 0.01D;

        if (var2 && !this.playerSPRidingMinecart)
        {
            this.theSoundManager.stopEntitySound(this.thePlayer);
        }

        if (this.minecartIsDead || !this.silent && this.minecartMoveSoundVolume == 0.0F && this.minecartRideSoundVolume == 0.0F)
        {
            if (!var3)
            {
                this.theSoundManager.stopEntitySound(this.theMinecart);

                if (var2 || this.playerSPRidingMinecart)
                {
                    this.theSoundManager.stopEntitySound(this.thePlayer);
                }
            }

            this.silent = true;

            if (this.minecartIsDead)
            {
                return;
            }
        }

        if (this.theSoundManager != null && this.theMinecart != null && this.theMinecart.getReversed() == 0 && this.minecartMoveSoundVolume > 0.0F)
        {
            this.theSoundManager.playEntitySound("shuttle.shuttle", this.theMinecart, this.minecartMoveSoundVolume, this.minecartSoundPitch, false);
            this.silent = false;
            var1 = true;
        }
        else if (this.theSoundManager != null && this.theMinecart != null && this.theMinecart.getReversed() == 1 && this.minecartMoveSoundVolume > 0.0F)
        {
            this.theSoundManager.playEntitySound("shuttle.shuttle", this.theMinecart, this.minecartMoveSoundVolume, 1.0F, false);
            this.silent = false;
            var1 = true;
        }
        
        if (this.theMinecart.getTimeUntilLaunch() <= 400)
        {
        	if (this.theMinecart.getTimeUntilLaunch() < 400)
        	{
                if (this.minecartSoundPitch < 1.0F)
                {
                    this.minecartSoundPitch += 0.0025F;
                }

                if (this.minecartSoundPitch > 1.0F)
                {
                    this.minecartSoundPitch = 1.0F;
                }
        	}

            float var10 = MathHelper.clamp_float((float)this.minecartSpeed, 0.0F, 4.0F) / 4.0F;
            this.minecartRideSoundVolume = 0.0F + var10 * 0.75F;
            var10 = MathHelper.clamp_float(var10 * 2.0F, 0.0F, 1.0F);
            this.minecartMoveSoundVolume = 0.0F + var10 * 0.7F;
        }
        else if (var4)
        {
            this.minecartMoveSoundVolume = 0.0F;
            this.minecartSoundPitch = 0.0F;
            this.minecartRideSoundVolume = 0.0F;
        }

        if (!this.silent)
        {
            if (this.minecartSoundPitch != var6)
            {
                this.theSoundManager.setEntitySoundPitch(this.theMinecart, this.minecartSoundPitch);
            }

            if (this.minecartMoveSoundVolume != var5)
            {
                this.theSoundManager.setEntitySoundVolume(this.theMinecart, this.minecartMoveSoundVolume);
            }

            if (this.minecartRideSoundVolume != var7)
            {
                this.theSoundManager.setEntitySoundVolume(this.thePlayer, this.minecartRideSoundVolume);
            }
        }

        if (!var1)
        {
            this.theSoundManager.updateSoundLocation(this.theMinecart);

            if (this.playerSPRidingMinecart)
            {
                this.theSoundManager.updateSoundLocation(this.thePlayer, this.theMinecart);
            }
        }
    }
}
