package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.api.entity.ICameraZoomEntity;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.model.ModelBipedGC;
import micdoodle8.mods.galacticraft.core.client.model.ModelPlayerGC;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.*;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

/**
 * This renders the thermal armor (unless RenderPlayerAPI is installed).
 * The thermal armor render is done after the corresponding body part of the player is drawn.
 * This ALSO patches RenderPlayer so that it uses ModelPlayerGC in place of ModelPlayer to draw the player.
 * <p>
 * Finally, this also adds a hook into rotateCorpse so as to fire a RotatePlayerEvent - used by the Cryogenic Chamber
 *
 * @author User
 */
public class RenderPlayerGC extends RenderPlayer
{
    public static ResourceLocation thermalPaddingTexture0;
    public static ResourceLocation thermalPaddingTexture1;
    public static ResourceLocation thermalPaddingTexture1_T2;
    public static ResourceLocation heatShieldTexture;
    public static boolean flagThermalOverride = false;
    private static Boolean isSmartRenderLoaded = null;

    public RenderPlayerGC()
    {
        this(false);
    }

    public RenderPlayerGC(boolean smallArms)
    {
        super(FMLClientHandler.instance().getClient().getRenderManager(), smallArms);

        // The following code removes the vanilla armor and item layer renderers and replaces them with the Galacticraft ones
        int itemLayerIndex = -1;
        int armorLayerIndex = -1;
        for (int i = 0; i < this.layerRenderers.size(); i++)
        {
            LayerRenderer layer = this.layerRenderers.get(i); 
            if (layer instanceof LayerHeldItem)
            {
                itemLayerIndex = i;
            }
            if (layer instanceof LayerBipedArmor)
            {
                armorLayerIndex = i;
            }
        }
        if (itemLayerIndex >= 0 && !ConfigManagerCore.disableVehicleCameraChanges)
        {
            this.layerRenderers.set(itemLayerIndex, new LayerHeldItemGC(this));
        }
        if (armorLayerIndex >= 0)
        {
            LayerRenderer playerArmor = new LayerBipedArmor(this)
            {
                @Override
                protected void initArmor()
                {
                    this.field_177189_c = new ModelBipedGC(0.5F);
                    this.field_177186_d = new ModelBipedGC(1.0F);
                }
            };
            this.layerRenderers.set(armorLayerIndex, playerArmor);
        }

        this.mainModel = new ModelPlayerGC(0.0F, smallArms);
        this.addLayer(new LayerOxygenTanks(this));
        this.addLayer(new LayerOxygenGear(this));
        this.addLayer(new LayerOxygenMask(this));
        this.addLayer(new LayerOxygenParachute(this));
        this.addLayer(new LayerFrequencyModule(this));

        if (GalacticraftCore.isPlanetsLoaded)
        {
            this.addLayer(new LayerThermalPadding(this));

            RenderPlayerGC.thermalPaddingTexture0 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_0.png");
            RenderPlayerGC.thermalPaddingTexture1 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_1.png");
            RenderPlayerGC.thermalPaddingTexture1_T2 = new ResourceLocation("galacticraftplanets", "textures/misc/thermal_padding_t2_1.png");
            RenderPlayerGC.heatShieldTexture = new ResourceLocation("galacticraftplanets", "textures/misc/shield.png");

            this.addLayer(new LayerShield(this));
        }
    }

    @Override
    protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);

        if (entitylivingbaseIn.isEntityAlive() && entitylivingbaseIn.isPlayerSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(entitylivingbaseIn);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
                super.preRenderCallback(entitylivingbaseIn, partialTickTime);
            }
            else if (event.shouldRotate == null || event.shouldRotate)
            {
                entitylivingbaseIn.rotationYawHead = 0;
                entitylivingbaseIn.prevRotationYawHead = 0;
                GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            }
        }
    }

    @Override
    protected void rotateCorpse(AbstractClientPlayer abstractClientPlayer, float par2, float par3, float par4)
    {
        if (abstractClientPlayer.isEntityAlive() && abstractClientPlayer.isPlayerSleeping())
        {
            RotatePlayerEvent event = new RotatePlayerEvent(abstractClientPlayer);
            MinecraftForge.EVENT_BUS.post(event);

            if (!event.vanillaOverride)
            {
                super.rotateCorpse(abstractClientPlayer, par2, par3, par4);
            }
            else if (event.shouldRotate == null)
            {
                GL11.glRotatef(abstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            }
            else if (event.shouldRotate)
            {
                float rotation = 0.0F;

                if (abstractClientPlayer.playerLocation != null)
                {
                    IBlockState bed = abstractClientPlayer.worldObj.getBlockState(abstractClientPlayer.playerLocation);

                    if (bed.getBlock().isBed(abstractClientPlayer.worldObj, abstractClientPlayer.playerLocation, abstractClientPlayer))
                    {
                        if (bed.getBlock() == GCBlocks.fakeBlock && bed.getValue(BlockMulti.MULTI_TYPE) == BlockMulti.EnumBlockMultiType.CRYO_CHAMBER)
                        {
                            TileEntity tile = event.entityPlayer.worldObj.getTileEntity(abstractClientPlayer.playerLocation);
                            if (tile instanceof TileEntityMulti)
                            {
                                bed = event.entityPlayer.worldObj.getBlockState(((TileEntityMulti) tile).mainBlockPosition);
                            }
                        }

                        if (bed.getBlock() == MarsBlocks.machine && bed.getValue(BlockMachineMars.TYPE) == BlockMachineMars.EnumMachineType.CRYOGENIC_CHAMBER)
                        {
                            switch (bed.getValue(BlockMachineMars.FACING))
                            {
                            case NORTH:
                                rotation = 0.0F;
                                break;
                            case EAST:
                                rotation = 270.0F;
                                break;
                            case SOUTH:
                                rotation = 180.0F;
                                break;
                            case WEST:
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
                final EntityPlayer player = (EntityPlayer) abstractClientPlayer;

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
            super.rotateCorpse(abstractClientPlayer, par2, par3, par4);
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
