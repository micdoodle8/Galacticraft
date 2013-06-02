package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiKnowledgeBook;
import net.minecraft.client.model.ModelBook;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.client.FMLClientHandler;


public class GCCoreItemRendererKnowledgeBook implements IItemRenderer
{
    private ModelBook bookModel = new ModelBook();
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON) || type.equals(ItemRenderType.INVENTORY))
        {
            return true;
        }
        
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return type.equals(ItemRenderType.INVENTORY);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
        if (FMLClientHandler.instance().getClient().currentScreen instanceof GCCoreGuiKnowledgeBook)
        {
            return;
        }
        
        GL11.glPushMatrix();
        GL11.glTranslatef(2.7F, 2.3F, -0.9F);
        GL11.glRotatef(200.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
        
        if (type.equals(ItemRenderType.INVENTORY))
        {
            GL11.glTranslatef(0F, 1.64F, -5.2F);
            GL11.glRotatef(-19.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(3F, 2.2F, 3F);
            GL11.glRotatef(-130, 0F, 1F, 0F);
        }
        else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
        {
            GL11.glScalef(5F, 5F, 5F);
        }
        
        FMLClientHandler.instance().getClient().renderEngine.bindTexture("/micdoodle8/mods/galacticraft/core/client/entities/book.png");
        GL11.glEnable(GL11.GL_CULL_FACE);
        this.bookModel.render((Entity)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
