package micdoodle8.mods.galacticraft.planets.asteroids.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NetworkRenderer
{
    public static void renderNetworks(World world, float partialTicks)
    {
        List<TileEntityBeamOutput> nodes = new ArrayList<>();

        for (Object o : new ArrayList<>(world.loadedTileEntityList))
        {
            if (o instanceof TileEntityBeamOutput)
            {
                nodes.add((TileEntityBeamOutput) o);
            }
        }

        if (nodes.isEmpty())
        {
            return;
        }

        Tessellator tess = Tessellator.getInstance();
        ClientPlayerEntity player = Minecraft.getInstance().player;
        double interpPosX = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * partialTicks;

        RenderSystem.disableTexture();

        for (TileEntityBeamOutput tileEntity : nodes)
        {
            if (tileEntity.getTarget() == null)
            {
                continue;
            }

            RenderSystem.pushMatrix();

            Vector3 outputPoint = tileEntity.getOutputPoint(true);
            Vector3 targetInputPoint = tileEntity.getTarget().getInputPoint();

            Vector3 direction = Vector3.subtract(outputPoint, targetInputPoint);
            float directionLength = direction.getMagnitude();

            float posX = (float) (tileEntity.getPos().getX() - interpPosX);
            float posY = (float) (tileEntity.getPos().getY() - interpPosY);
            float posZ = (float) (tileEntity.getPos().getZ() - interpPosZ);
            RenderSystem.translatef(posX, posY, posZ);

            RenderSystem.translatef(outputPoint.floatX() - tileEntity.getPos().getX(), outputPoint.floatY() - tileEntity.getPos().getY(), outputPoint.floatZ() - tileEntity.getPos().getZ());
            RenderSystem.rotatef(tileEntity.yaw + 180, 0, 1, 0);
            RenderSystem.rotatef(-tileEntity.pitch, 1, 0, 0);
            RenderSystem.rotatef(tileEntity.ticks * 10, 0, 0, 1);

            tess.getBuffer().begin(1, DefaultVertexFormats.POSITION_COLOR);

            for (Direction dir : Direction.values())
            {
                tess.getBuffer().pos(dir.getXOffset() / 40.0F, dir.getYOffset() / 40.0F, dir.getZOffset() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
                tess.getBuffer().pos(dir.getXOffset() / 40.0F, dir.getYOffset() / 40.0F, directionLength + dir.getZOffset() / 40.0F).color(tileEntity.getColor().floatX(), tileEntity.getColor().floatY(), tileEntity.getColor().floatZ(), 1.0F).endVertex();
            }

            tess.draw();

            RenderSystem.popMatrix();
        }

        RenderSystem.enableTexture();

        RenderSystem.color4f(1, 1, 1, 1);
    }
}
