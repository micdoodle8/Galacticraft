//package micdoodle8.mods.galacticraft.core.client.model;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import net.minecraft.client.renderer.GLAllocation;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.client.renderer.model.Model;
//import net.minecraft.client.renderer.model.ModelBox;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//public class ModelRendererGC extends ModelRenderer
//{
//    private boolean compiled;
//    private int displayList;
//
//    public ModelRendererGC(Model par1ModelBase, int par2, int par3)
//    {
//        super(par1ModelBase, par2, par3);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void render(float par1)
//    {
//        if (!this.isHidden)
//        {
//            if (this.showModel)
//            {
//                if (!this.compiled)
//                {
//                    this.compileDisplayList(par1);
//                }
//
//                GL11.glTranslatef(this.offsetX, this.offsetY, this.offsetZ);
//                int i;
//
//                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F)
//                {
//                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F)
//                    {
//                        GL11.glCallList(this.displayList);
//
//                        if (this.childModels != null)
//                        {
//                            for (i = 0; i < this.childModels.size(); ++i)
//                            {
//                                this.childModels.get(i).render(par1);
//                            }
//                        }
//                    }
//                    else
//                    {
//                        GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
//                        GL11.glCallList(this.displayList);
//
//                        if (this.childModels != null)
//                        {
//                            for (i = 0; i < this.childModels.size(); ++i)
//                            {
//                                this.childModels.get(i).render(par1);
//                            }
//                        }
//
//                        GL11.glTranslatef(-this.rotationPointX * par1, -this.rotationPointY * par1, -this.rotationPointZ * par1);
//                    }
//                }
//                else
//                {
//                    GL11.glPushMatrix();
//                    GL11.glTranslatef(this.rotationPointX * par1, this.rotationPointY * par1, this.rotationPointZ * par1);
//
//                    if (this.rotateAngleY != 0.0F)
//                    {
//                        GL11.glRotatef(this.rotateAngleY * Constants.RADIANS_TO_DEGREES, 0.0F, 1.0F, 0.0F);
//                    }
////
//                    if (this.rotateAngleZ != 0.0F)
//                    {
//                        GL11.glRotatef(this.rotateAngleZ * Constants.RADIANS_TO_DEGREES, 0.0F, 0.0F, 1.0F);
//                    }
//
//                    if (this.rotateAngleX != 0.0F)
//                    {
//                        GL11.glRotatef(this.rotateAngleX * Constants.RADIANS_TO_DEGREES, 1.0F, 0.0F, 0.0F);
//                    }
//
//                    GL11.glCallList(this.displayList);
//
//                    if (this.childModels != null)
//                    {
//                        for (i = 0; i < this.childModels.size(); ++i)
//                        {
//                            this.childModels.get(i).render(par1);
//                        }
//                    }
//
//                    GL11.glPopMatrix();
//                }
//
//                GL11.glTranslatef(-this.offsetX, -this.offsetY, -this.offsetZ);
//            }
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private void compileDisplayList(float par1)
//    {
//        this.displayList = GLAllocation.generateDisplayLists(1);
//        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
//        Tessellator tessellator = Tessellator.getInstance();
//
//        for (ModelBox modelBox : this.cubeList)
//        {
//            modelBox.render(tessellator.getBuffer(), par1);
//        }
//
//        GL11.glEndList();
//        this.compiled = true;
//    }
//}
