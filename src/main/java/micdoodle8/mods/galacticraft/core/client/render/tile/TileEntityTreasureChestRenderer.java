package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@OnlyIn(Dist.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntityRenderer<TileEntityTreasureChest>
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/treasure.png");

    private final ModelTreasureChest chestModel = new ModelTreasureChest();

    @Override
    public void render(TileEntityTreasureChest chest, double x, double y, double z, float partialTicks, int destroyStage)
    {
        this.bindTexture(treasureChestTexture);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        float f = chest.getBlockState().get(ChestBlock.FACING).getHorizontalAngle();
        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        this.applyLidRotation(chest, partialTicks, chestModel);
        this.chestModel.renderAll(!chest.locked);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void applyLidRotation(TileEntityTreasureChest chest, float partialTicks, ChestModel model) {
        float f = ((IChestLid)chest).getLidAngle(partialTicks);
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        model.getLid().rotateAngleX = -(f * ((float)Math.PI / 2F));
    }
}
