package micdoodle8.mods.galacticraft.core.client.render.entities;

import api.player.model.ModelPlayer;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerBaseGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.RenderPlayerGC.RotatePlayerEvent;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class RenderPlayerBaseGC extends RenderPlayerBase
{
    public ModelPlayer modelThermalPadding;
    public ModelPlayer modelThermalPaddingHelmet;
    private static ResourceLocation thermalPaddingTexture0;
    private static ResourceLocation thermalPaddingTexture1;

    /**
     * This is used in place of RenderPlayerGC only if RenderPlayerAPI is installed
     * It renders the thermal armor (also does something connected with rotating a sleeping player) 
     * 
     * @param renderPlayerAPI
     */
    public RenderPlayerBaseGC(RenderPlayerAPI renderPlayerAPI)
    {
        super(renderPlayerAPI);
        this.modelThermalPadding = new ModelPlayer(0.25F);
        this.modelThermalPaddingHelmet = new ModelPlayer(0.9F);

        if (GalacticraftCore.isPlanetsLoaded)
        {
            thermalPaddingTexture0 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_0.png");
            thermalPaddingTexture1 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_1.png");
        }
    }

    @Override
    public void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        super.renderModel(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);

        if (thermalPaddingTexture0 != null)
        {
            PlayerGearData gearData = ClientProxyCore.playerItemData.get(par1EntityLivingBase.getCommandSenderName());

            if (gearData != null && !RenderPlayerGC.flagThermalOverride)
            {
                ModelBiped modelBiped;

                for (int i = 0; i < 4; ++i)
                {
                    if (i == 0)
                    {
                        modelBiped = this.modelThermalPaddingHelmet;
                    }
                    else
                    {
                        modelBiped = this.modelThermalPadding;
                    }

                    int padding = gearData.getThermalPadding(i);
                    
                    //Padding sub-type 0 is standard Thermal Armor.  See PacketSimple handling of C_UPDATE_GEAR_SLOT for how the sub-type gets set
                    if (padding == 0 && !par1EntityLivingBase.isInvisible())
                    {
                        // First draw the thermal armor without any color tinting
                    	GL11.glColor4f(1, 1, 1, 1);
                        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(thermalPaddingTexture1);
                        modelBiped.bipedHead.showModel = i == 0;
                        modelBiped.bipedHeadwear.showModel = i == 0;
                        modelBiped.bipedBody.showModel = i == 1 || i == 2;
                        modelBiped.bipedRightArm.showModel = i == 1;
                        modelBiped.bipedLeftArm.showModel = i == 1;
                        modelBiped.bipedRightLeg.showModel = i == 2 || i == 3;
                        modelBiped.bipedLeftLeg.showModel = i == 2 || i == 3;
                        modelBiped.onGround = this.renderPlayer.modelBipedMain.onGround;
                        modelBiped.isRiding = this.renderPlayer.modelBipedMain.isRiding;
                        modelBiped.isChild = this.renderPlayer.modelBipedMain.isChild;
                        if (this.renderPlayer.modelBipedMain != null)
                        {
                            modelBiped.heldItemLeft = this.renderPlayer.modelBipedMain.heldItemLeft;
                            modelBiped.heldItemRight = this.renderPlayer.modelBipedMain.heldItemRight;
                            modelBiped.isSneak = this.renderPlayer.modelBipedMain.isSneak;
                            modelBiped.aimedBow = this.renderPlayer.modelBipedMain.aimedBow;
                        }
                        modelBiped.setLivingAnimations(par1EntityLivingBase, par2, par3, 0.0F);
                        modelBiped.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);

                        // Then overlay the same again, with color tinting:
                        //Start alpha render
                        GL11.glDisable(GL11.GL_LIGHTING);
                        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(thermalPaddingTexture0);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        float time = par1EntityLivingBase.ticksExisted / 10.0F;
                        float sTime = (float) Math.sin(time) * 0.5F + 0.5F;

                        float r = 0.2F * sTime;
                        float g = 1.0F * sTime;
                        float b = 0.2F * sTime;

                        if (par1EntityLivingBase.worldObj.provider instanceof IGalacticraftWorldProvider)
                        {
                            float modifier = ((IGalacticraftWorldProvider) par1EntityLivingBase.worldObj.provider).getThermalLevelModifier();

                            if (modifier > 0)
                            {
                                b = g;
                                g = r;
                            }
                            else if (modifier < 0)
                            {
                                r = g;
                                g = b;
                            }
                        }

                        GL11.glColor4f(r, g, b, 0.4F * sTime);
                        modelBiped.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
                        GL11.glColor4f(1, 1, 1, 1);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_LIGHTING);
                    }
                }
            }
        }
    }

    @Override
    public void rotatePlayer(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
    {
    	if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
        	RotatePlayerEvent event = new RotatePlayerEvent(par1AbstractClientPlayer);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
            	GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            	GL11.glRotatef(this.getDeathMaxRotation(par1AbstractClientPlayer), 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0, 0, ModelPlayerBaseGC.isSmartMovingLoaded ? 3 : 2);
            }
            else if (event.shouldRotate == null)
            {
                GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            }
            else if (event.shouldRotate)
            {
                float rotation = 0.0F;

                ChunkCoordinates pos = par1AbstractClientPlayer.playerLocation;
                if (pos != null)
                {
                    Block bed = par1AbstractClientPlayer.worldObj.getBlock(pos.posX, pos.posY, pos.posZ);
                    int meta = par1AbstractClientPlayer.worldObj.getBlockMetadata(pos.posX, pos.posY, pos.posZ);

                    if (bed.isBed(par1AbstractClientPlayer.worldObj, pos.posX, pos.posY, pos.posZ, par1AbstractClientPlayer))
                    {
                        if (bed == GCBlocks.fakeBlock && meta == 5)
                        {
                            TileEntity tile = event.entityPlayer.worldObj.getTileEntity(pos.posX, pos.posY, pos.posZ);
                            if (tile instanceof TileEntityMulti)
                            {
                                bed = ((TileEntityMulti) tile).mainBlockPosition.getBlock(event.entityPlayer.worldObj);
                                meta = ((TileEntityMulti) tile).mainBlockPosition.getBlockMetadata(event.entityPlayer.worldObj);
                            }
                        }

                        if (bed == MarsBlocks.machine && (meta & 12) == BlockMachineMars.CRYOGENIC_CHAMBER_METADATA)
                        {
                            switch (meta & 3)
                            {
                            case 3:
                                rotation = 0.0F;
                                break;
                            case 1:
                                rotation = 270.0F;
                                break;
                            case 2:
                                rotation = 180.0F;
                                break;
                            case 0:
                                rotation = 90.0F;
                                break;
                            }
                        }
                    }
                }

                GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
            }
        }
        else
        {
        	if (par1AbstractClientPlayer instanceof EntityPlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
        	{
                final EntityPlayer player = (EntityPlayer)par1AbstractClientPlayer;

                if (player.ridingEntity instanceof ICameraZoomEntity)
                {
                    Entity rocket = player.ridingEntity;
                    float rotateOffset = ((ICameraZoomEntity)rocket).getRotateOffset();
                    if (rotateOffset > -10F)
                    {
                        GL11.glTranslatef(0, -rotateOffset, 0);
                        float anglePitch = rocket.prevRotationPitch;
                        float angleYaw = rocket.prevRotationYaw;
                        GL11.glRotatef(-angleYaw, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(anglePitch, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0, rotateOffset, 0);
                    }
                }
        	}
            super.rotatePlayer(par1AbstractClientPlayer, par2, par3, par4);
        }
    }
}
