package codechicken.nei;

import codechicken.core.ClientUtils;
import codechicken.lib.render.TextureUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class SpawnerRenderer implements IItemRenderer
{
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        return true;
    }
    
    public void renderInventoryItem(RenderBlocks render, ItemStack item)
    {
        int meta = item.getItemDamage();
        
        if(meta == 0)
        {
            meta = ItemMobSpawner.idPig;
        }
        String bossName = BossStatus.bossName;
        int bossTimeout = BossStatus.statusBarTime;
        try
        {
            World world = NEIClientUtils.mc().theWorld;
            ItemMobSpawner.loadSpawners(world);
            TextureUtils.bindAtlas(0);
            render.renderBlockAsItem(Blocks.mob_spawner, 0, 1F);
            GL11.glPushMatrix();
            
            Entity entity = ItemMobSpawner.getEntity(meta);
            if(entity != null)
            {
                entity.setWorld(world);
                float f1 = 0.4375F;
                if(entity.getShadowSize() > 1.5)
                {
                    f1 = 0.1F;
                }
                GL11.glRotatef((float) (ClientUtils.getRenderTime()*10), 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-20F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
                GL11.glScalef(f1, f1, f1);
                entity.setLocationAndAngles(0, 0, 0, 0.0F, 0.0F);
                RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 0);
            }
            GL11.glPopMatrix();
    
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
        catch(Exception e)
        {
            if(Tessellator.instance.isDrawing)
                Tessellator.instance.draw();
        }
        BossStatus.bossName = bossName;
        BossStatus.statusBarTime = bossTimeout;
    }
    
    @SuppressWarnings("incomplete-switch")
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        switch(type)
        {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            case INVENTORY:
            case ENTITY:
                renderInventoryItem((RenderBlocks)data[0], item);
            break;
        }
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return true;
    }    
}
