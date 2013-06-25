package micdoodle8.mods.galacticraft.mars.client.render.item;

import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.entities.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;

public class GCMarsItemRendererSpaceshipT2 extends GCCoreItemRendererSpaceship
{
    public GCMarsItemRendererSpaceshipT2(EntitySpaceshipBase spaceship, ModelBase model, String texture)
    {
        super(spaceship, model, texture);
    }

    public void transform(ItemRenderType type)
    {
        final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
        long var10 = this.spaceship.entityId * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        if (type == ItemRenderType.EQUIPPED)
        {
            GL11.glRotatef(70, 1.0F, 0, 0);
            GL11.glRotatef(-10, 0.0F, 1, 0);
            GL11.glRotatef(50, 0.0F, 1, 1);
            GL11.glTranslatef(0F, 2.0F, 0F);
            GL11.glScalef(5.2F, 5.2F, 5.2F);

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
            {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        {
            GL11.glTranslatef(-0.5F, 4.2F, 0F);
            GL11.glRotatef(28, 0.0F, 0, 1);
            GL11.glRotatef(50 + 180, 0.0F, 1, 0);
            GL11.glRotatef(73, 1.0F, 0, 0);
            GL11.glScalef(5.2F, 5.2F, 5.2F);

            if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
            {
                GL11.glScalef(0.0F, 0.0F, 0.0F);
            }
        }

        GL11.glTranslatef(var12, var13 - 0.1F, var14);
        GL11.glScalef(-0.4F, -0.4F, 0.4F);

        if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
        {
            if (type == ItemRenderType.INVENTORY)
            {
                GL11.glRotatef(85F, 1F, 0F, 1F);
                GL11.glRotatef(20F, 1F, 0F, 0F);
                GL11.glScalef(0.9F, 0.9F, 0.9F);
            }
            else
            {
                GL11.glTranslatef(0, -0.9F, 0);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            GL11.glScalef(1.3F, 1.3F, 1.3F);
            GL11.glTranslatef(0, -0.6F, 0);
            GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
        }
    }
}
