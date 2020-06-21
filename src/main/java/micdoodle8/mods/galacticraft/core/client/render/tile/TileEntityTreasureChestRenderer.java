package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
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
        Direction var9;

        if (!chest.hasWorld())
        {
            var9 = Direction.DOWN;
        }
        else
        {
            var9 = chest.getBlockState().get(BlockTier1TreasureChest.FACING);
        }

        ModelTreasureChest var14 = null;
        ModelTreasureChestLarge var14b = null;

        var14 = this.chestModel;
        this.bindTexture(TileEntityTreasureChestRenderer.treasureChestTexture);

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        short var11 = 0;

        if (var9 == Direction.NORTH)
        {
            var11 = 180;
        }

        if (var9 == Direction.SOUTH)
        {
            var11 = 0;
        }

        if (var9 == Direction.WEST)
        {
            var11 = 90;
        }

        if (var9 == Direction.EAST)
        {
            var11 = -90;
        }

        GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTicks;

        float var13;

        var12 = 1.0F - var12;
        var12 = 1.0F - var12 * var12 * var12;

        if (var14 != null)
        {
            var14.getLid().rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
            var14.renderAll(!chest.locked);
        }

        
        //Note: currently var14b is always null - no large Treasure Chest model
//        if (var14b != null)
//        {
//            var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
//            var14b.renderAll(!chest.locked);
//        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
