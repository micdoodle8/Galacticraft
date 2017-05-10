package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.IZeroGDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.wrappers.PlayerGearData;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Loader;

/**
 * This renders the thermal armor (unless RenderPlayerAPI is installed).
 * The thermal armor render is done after the corresponding body part of the player is drawn.
 * This ALSO patches RenderPlayer so that it uses ModelPlayerGC in place of ModelPlayer to draw the player.
 * 
 * Finally, this also adds a hook into rotateCorpse so as to fire a RotatePlayerEvent - used by the Cryogenic Chamber
 * 
 * @author User
 *
 */
public class RenderPlayerGC extends RenderPlayer
{
    public static ModelBiped modelThermalPadding;
    public static ModelBiped modelThermalPaddingHelmet;
    private static ResourceLocation thermalPaddingTexture0;
    private static ResourceLocation thermalPaddingTexture1;
    public static boolean flagThermalOverride = false;
    private static Boolean isSmartRenderLoaded = null;
    
    static
    {
        modelThermalPadding = new ModelPlayerGC(0.25F);
        modelThermalPaddingHelmet = new ModelPlayerGC(0.9F);
    }

    public RenderPlayerGC()
    {
        super();
        this.mainModel = new ModelPlayerGC(0.0F);
        this.modelBipedMain = (ModelPlayerGC) this.mainModel;
        this.modelArmorChestplate = new ModelPlayerGC(1.0F);
        this.modelArmor = new ModelPlayerGC(0.5F);

        if (GalacticraftCore.isPlanetsLoaded)
        {
            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftasteroids", "textures/misc/thermalPadding_1.png");
        }
    }

    public static void renderModelS(RendererLivingEntity inst, EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	if (inst instanceof RenderPlayer)
    	{
    		RenderPlayer thisInst = (RenderPlayer)inst;
    		
    		if (isSmartRenderLoaded == null)
    		{
    			isSmartRenderLoaded = Loader.isModLoaded("SmartRender");
    		}
    		
            if (RenderPlayerGC.thermalPaddingTexture0 != null && !isSmartRenderLoaded)
            {
                PlayerGearData gearData = ClientProxyCore.playerItemData.get(par1EntityLivingBase.getCommandSenderName());

                if (gearData != null && !RenderPlayerGC.flagThermalOverride)
                {
                    ModelBiped modelBiped;

                    for (int i = 0; i < 4; ++i)
                    {
                        if (i == 0)
                        {
                            modelBiped = modelThermalPaddingHelmet;
                        }
                        else
                        {
                            modelBiped = modelThermalPadding;
                        }

                        int padding = gearData.getThermalPadding(i);

                        //Padding sub-type 0 is standard Thermal Armor.  See PacketSimple handling of C_UPDATE_GEAR_SLOT for how the sub-type gets set
                        if (padding == 0 && !par1EntityLivingBase.isInvisible())
                        {
                            GL11.glColor4f(1, 1, 1, 1);
                            Minecraft.getMinecraft().renderEngine.bindTexture(RenderPlayerGC.thermalPaddingTexture1);
                            modelBiped.bipedHead.showModel = i == 0;
                            modelBiped.bipedHeadwear.showModel = i == 0;
                            modelBiped.bipedBody.showModel = i == 1 || i == 2;
                            modelBiped.bipedRightArm.showModel = i == 1;
                            modelBiped.bipedLeftArm.showModel = i == 1;
                            modelBiped.bipedRightLeg.showModel = i == 2 || i == 3;
                            modelBiped.bipedLeftLeg.showModel = i == 2 || i == 3;
                            
                            modelBiped.onGround = thisInst.mainModel.onGround;
                            modelBiped.isRiding = thisInst.mainModel.isRiding;
                            modelBiped.isChild = thisInst.mainModel.isChild;
                            if (thisInst.mainModel instanceof ModelBiped)
                            {
                                modelBiped.heldItemLeft = ((ModelBiped) thisInst.mainModel).heldItemLeft;
                                modelBiped.heldItemRight = ((ModelBiped) thisInst.mainModel).heldItemRight;
                                modelBiped.isSneak = ((ModelBiped) thisInst.mainModel).isSneak;
                                modelBiped.aimedBow = ((ModelBiped) thisInst.mainModel).aimedBow;
                            }
                            modelBiped.setLivingAnimations(par1EntityLivingBase, par2, par3, 0.0F);
                            modelBiped.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);

                            // Start alpha render
                            GL11.glDisable(GL11.GL_LIGHTING);
                            Minecraft.getMinecraft().renderEngine.bindTexture(RenderPlayerGC.thermalPaddingTexture0);
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
    }

    @Override
    protected void rotateCorpse(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4)
    {
        if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(par1AbstractClientPlayer);
            MinecraftForge.EVENT_BUS.post(event);
            
            if (!event.vanillaOverride)
            {
                super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
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
          	if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
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
          	super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
        }
        
        if (par1AbstractClientPlayer.isSneaking() && par1AbstractClientPlayer.worldObj.provider instanceof IZeroGDimension)
        {
            GL11.glTranslatef(0F, -0.1F, 0F);
        }
    }

    public static class RotatePlayerEvent extends PlayerEvent
    {
        public Boolean shouldRotate = null;
        public boolean vanillaOverride = false;

        public RotatePlayerEvent(AbstractClientPlayer player)
        {
            super(player);
        }
    }
}
