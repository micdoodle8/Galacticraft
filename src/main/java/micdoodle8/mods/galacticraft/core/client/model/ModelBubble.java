//package micdoodle8.mods.galacticraft.core.client.model;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.AdvancedModelLoader;
//import net.minecraftforge.client.model.IModelCustom;
//
//public class ModelBubble extends ModelBase
//{
//    IModelCustom sphere;
//
//    public ModelBubble()
//    {
//        this(0.0F);
//    }
//
//    public ModelBubble(float par1)
//    {
//        this.sphere = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/sphere.obj"));
//    }
//
//    @Override
//    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
//    {
//        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
//        this.sphere.renderAll();
//    }
//
//    @Override
//    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
//    {
//
//    }
//}
