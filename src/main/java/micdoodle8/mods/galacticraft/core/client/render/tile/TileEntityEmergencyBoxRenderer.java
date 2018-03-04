package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockEmergencyBox;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEmergencyBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class TileEntityEmergencyBoxRenderer extends TileEntitySpecialRenderer<TileEntityEmergencyBox>
{
    private static final float MASKSCALE = 3F;
    
    public class Flap extends ModelBase
    {
        ModelRenderer model;
        protected float angle;

        public Flap()
        {
            this.angle = 0.0F;
            this.textureWidth = 32;
            this.textureHeight = 32;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-6F, -6F, 0F, 12, 6, 1);
            this.model.setRotationPoint(0F, 6F, -7F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        private void setRotation(ModelRenderer model, float x, float y, float z)
        {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

        public void render()
        {
            this.setRotation(this.model, angle/Constants.RADIANS_TO_DEGREES, 0F, 0F);
            this.model.render(1F/16F);
        }
    }

    public class Plinth extends ModelBase
    {
        ModelRenderer model;

        public Plinth()
        {
            this.textureWidth = 16;
            this.textureHeight = 16;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-6F, -7F, -6F, 12, 1, 12);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        public void render(float height)
        {
            this.model.setRotationPoint(0F, height, 0F);
            this.model.render(1F/16F);
        }
    }

    public class Mask extends ModelBase
    {
        ModelRenderer model;

        public Mask()
        {
            this.textureWidth = 128;
            this.textureHeight = 64;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.addBox(-8.0F, -4F, -8.0F, 16, 16, 16, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        public void render(float height)
        {
            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
            this.model.render(1F/16F/MASKSCALE);
        }
    }

    public class Tank extends ModelBase
    {
        ModelRenderer model;

        public Tank()
        {
            this.textureWidth = 128;
            this.textureHeight = 64;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.setTextureOffset(4, 0);   // Green tank
            this.model.addBox(-1.5F, 0F, -1.5F, 3, 7, 3, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        public void render(float height)
        {
            this.model.setRotationPoint(0F, height * MASKSCALE, 0F);
            this.model.render(1F/16F/MASKSCALE);
        }
    }

    public class Pack extends ModelBase
    {
        ModelRenderer model;

        public Pack()
        {
            this.textureWidth = 256;
            this.textureHeight = 256;
            this.model = new ModelRenderer(this, 0, 0);
            this.model.setTextureOffset(50, 50);
            this.model.addBox(-6F, -11F, -10F, 12, 1, 20, 1.0F);
            this.model.setRotationPoint(0F, 0F, 0F);
            this.model.setTextureSize(this.textureWidth, this.textureHeight);
            this.model.mirror = true;
        }

        public void render(float height)
        {
            this.model.setRotationPoint(0F, height * 2F, 0F);
            this.model.render(1F/32F);
        }
    }

    private static final ResourceLocation boxTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/blocks/emergency_box.png");
    private static final ResourceLocation flapTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/blocks/emergency_box_flap.png");
    private static final ResourceLocation packTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/parachute/red.png");
    private static final ResourceLocation oxygenMaskTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/oxygen.png");
    private static final ResourceLocation oxygenTankTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/player.png");
    private Flap flapA = new Flap();
    private Flap flapB = new Flap();
    private Flap flapC = new Flap();
    private Flap flapD = new Flap();
    private Plinth plat = new Plinth();
    private Mask mask = new Mask();
    private Tank tank = new Tank();
    private Pack pack = new Pack();

    @Override
    public void renderTileEntityAt(TileEntityEmergencyBox tileEntity, double d, double d1, double d2, float f, int par9)
    {
        IBlockState b = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (!(b.getBlock() instanceof BlockEmergencyBox)) return;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
        
        flapA.angle = tileEntity.getAngleA(f);
        flapB.angle = tileEntity.getAngleB(f);
        flapC.angle = tileEntity.getAngleC(f);
        flapD.angle = tileEntity.getAngleD(f);
        float height = Math.max(Math.max(flapA.angle, flapB.angle), Math.max(flapC.angle, flapD.angle)) / 90F;
        
        if (height > 0F && b.getValue(BlockEmergencyBox.KIT))
        {
            GlStateManager.pushMatrix();
            this.bindTexture(packTexture);
            this.pack.render(height);
            GlStateManager.rotate(180F, 1F, 0F, 0F);
            this.bindTexture(oxygenMaskTexture);
            GlStateManager.translate(0.0F, 0.0F, -0.07F);
            this.mask.render(-height);
            this.bindTexture(oxygenTankTexture);
            GlStateManager.translate(0.1F, 0.11F, 0.3F);
            this.tank.render(-height);
            GlStateManager.translate(-0.2F, 0F, 0F);
            this.tank.render(-height);
            GlStateManager.popMatrix();
        }

        this.bindTexture(boxTexture);
        this.plat.render(height);
        this.bindTexture(flapTexture);
        this.flapA.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapB.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapC.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapD.render();
        GlStateManager.rotate(180F, 1F, 0F, 0F);
        this.flapB.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapA.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapD.render();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        this.flapC.render();
        GlStateManager.popMatrix();
    }
}
