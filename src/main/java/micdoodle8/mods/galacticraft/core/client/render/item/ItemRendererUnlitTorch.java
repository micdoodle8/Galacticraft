//package micdoodle8.mods.galacticraft.core.client.render.item;
//
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.client.IItemRenderer;
//import org.lwjgl.opengl.GL11;
//
//public class ItemRendererUnlitTorch implements IItemRenderer
//{
//    @Override
//    public boolean handleRenderType(ItemStack item, ItemRenderType type)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
//    {
//        switch (helper)
//        {
//        case INVENTORY_BLOCK:
//            return true;
//        default:
//            return false;
//        }
//    }
//
//    @Override
//    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
//    {
//        switch (type)
//        {
//        case INVENTORY:
//            this.renderTorchInInv();
//        default:
//        }
//    }
//
//    public void renderTorchInInv()
//    {
//        GL11.glPushMatrix();
//
//        GL11.glPopMatrix();
//    }
//}
