package codechicken.lib.model.modelbase;

import codechicken.lib.model.bakery.CCQuad;
import codechicken.lib.render.CCModel;
import codechicken.lib.vec.*;
import codechicken.lib.vec.uv.UV;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.VertexBuffer;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 8/24/2016.
 * A ModelRenderer that is compatible with FastTESR's.
 * TODO See CCModelBase's comment,
 */
public class CCModelRenderer {

    public float textureWidth;
    public float textureHeight;
    private int textureOffsetX;
    private int textureOffsetY;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public boolean mirror;
    public boolean showModel;
    public boolean isHidden;

    private CCModel compiledModel;
    public boolean compiled;

    public List<CCModelBox> cubeList;
    public List<CCModelRenderer> childModels;
    public final String boxName;
    private final CCModelBase baseModel;
    public float offsetX;
    public float offsetY;
    public float offsetZ;

    public CCModelRenderer(CCModelBase model, String boxNameIn) {
        this.textureWidth = 64.0F;
        this.textureHeight = 32.0F;
        this.showModel = true;
        this.cubeList = Lists.newArrayList();
        this.baseModel = model;
        model.boxList.add(this);
        this.boxName = boxNameIn;
        this.setTextureSize(model.textureWidth, model.textureHeight);
    }

    public CCModelRenderer(CCModelBase model) {
        this(model, null);
    }

    public CCModelRenderer(CCModelBase model, int texOffX, int texOffY) {
        this(model);
        this.setTextureOffset(texOffX, texOffY);
    }

    public void addChild(CCModelRenderer renderer) {
        if (this.childModels == null) {
            this.childModels = Lists.newArrayList();
        }

        this.childModels.add(renderer);
    }

    public CCModelRenderer setTextureOffset(int x, int y) {
        this.textureOffsetX = x;
        this.textureOffsetY = y;
        return this;
    }

    public CCModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth) {
        partName = this.boxName + "." + partName;
        UV textureOffset = this.baseModel.getTextureOffset(partName);
        this.setTextureOffset((int) textureOffset.u, (int) textureOffset.v);
        this.cubeList.add((new CCModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F)).setBoxName(partName));
        return this;
    }

    public CCModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth) {
        this.cubeList.add(new CCModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
        return this;
    }

    public CCModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth, boolean mirrored) {
        this.cubeList.add(new CCModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F, mirrored));
        return this;
    }

    public void addBox(float offX, float offY, float offZ, int width, int height, int depth, float scaleFactor) {
        this.cubeList.add(new CCModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, scaleFactor));
    }

    public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPointX = rotationPointXIn;
        this.rotationPointY = rotationPointYIn;
        this.rotationPointZ = rotationPointZIn;
    }

    public void render(float scale, VertexBuffer buffer) {
        //TODO
        //CCRenderState.bind(buffer);
        //boolean startDrawing = !CCRenderState.isDrawing();
        //if (startDrawing) {
        //    CCRenderState.startDrawing(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        //}
        //bake(scale).render();
        //if (startDrawing) {
        //    CCRenderState.draw();
        //}
    }

    public CCModel bake(float scale) {
        if (!this.isHidden) {
            if (this.showModel) {
                if (!this.compiled) {
                    this.compileModel(scale);
                }
                LinkedList<Transformation> transforms = new LinkedList<Transformation>();
                LinkedList<CCModel> finalModelElements = new LinkedList<CCModel>();
                transforms.add(new Translation(offsetX, offsetY, offsetZ));
                //GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                    if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                        //GlStateManager.callList(this.displayList);
                        finalModelElements.add(compiledModel.copy());
                        if (this.childModels != null) {
                            for (CCModelRenderer modelRenderer : childModels) {
                                finalModelElements.add(modelRenderer.bake(scale).copy());
                            }
                        }
                    } else {
                        transforms.add(new Translation(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale));
                        //GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                        //GlStateManager.callList(this.displayList);
                        finalModelElements.add(compiledModel.copy());

                        if (this.childModels != null) {
                            for (CCModelRenderer modelRenderer : childModels) {
                                finalModelElements.add(modelRenderer.bake(scale).copy());
                            }
                        }

                        //GlStateManager.translate(-this.rotationPointX * scale, -this.rotationPointY * scale, -this.rotationPointZ * scale);
                    }
                } else {
                    //GlStateManager.pushMatrix();
                    //GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    transforms.add(new Translation(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale));

                    if (this.rotateAngleZ != 0.0F) {
                        transforms.add(new Rotation(rotateAngleZ, new Vector3(0, 0, 1).normalize()));
                        //GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F) {
                        transforms.add(new Rotation(rotateAngleY, new Vector3(0, 1, 0).normalize()));
                        //GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F) {
                        transforms.add(new Rotation(rotateAngleZ, new Vector3(1, 0, 0).normalize()));
                        //GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                    }
                    finalModelElements.add(compiledModel.copy());
                    //GlStateManager.callList(this.displayList);

                    if (this.childModels != null) {
                        for (CCModelRenderer modelRenderer : childModels) {
                            finalModelElements.add(modelRenderer.bake(scale).copy());
                        }
                    }

                    //GlStateManager.popMatrix();
                }

                //GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
                return CCModel.combine(finalModelElements).apply(new TransformationList(transforms.toArray(new Transformation[transforms.size()])));
            }
        }
        return CCModel.newModel(GL11.GL_QUADS);
    }

    /*public void renderWithRotation(float scale, VertexBuffer buffer) {
        if (!this.isHidden) {
            if (this.showModel) {
                if (!this.compiled) {
                    this.compileModel(scale, buffer.getVertexFormat());
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                }

                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                }

                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                }

                GlStateManager.callList(this.displayList);
                GlStateManager.popMatrix();
            }
        }
    }*/

    //@SideOnly(Side.CLIENT)
    /*public void postRender(float scale, VertexBuffer buffer) {
        if (!this.isHidden) {
            if (this.showModel) {
                if (!this.compiled) {
                    this.compileModel(scale, buffer.getVertexFormat());
                }

                if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                    if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
                        GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    }
                } else {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                    }

                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                    }

                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                    }
                }
            }
        }
    }*/

    private CCModel compileModel(float scale) {
        if (compiled) {
            return compiledModel;
        }

        CCModel model = CCModel.newModel(GL11.GL_QUADS);
        LinkedList<Vertex5> vertices = new LinkedList<Vertex5>();
        for (CCModelBox box : cubeList) {
            for (CCQuad quad : box.getQuads()) {
                Collections.addAll(vertices, quad.vertices);
            }
        }
        model.verts = vertices.toArray(new Vertex5[vertices.size()]);
        model.computeNormals();

        compiledModel = model.copy().apply(new Scale(scale));

        this.compiled = true;
        return compiledModel;
    }

    public CCModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
        this.textureWidth = (float) textureWidthIn;
        this.textureHeight = (float) textureHeightIn;
        return this;
    }
}
