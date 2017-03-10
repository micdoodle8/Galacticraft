package codechicken.lib.model.bakery;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

import java.util.LinkedList;

/**
 * Created by covers1624 on 8/3/2016.
 * TODO, Move the back end of this to the BakingVertexBuffer wrapped through the CCL render pipe.
 */
@Deprecated// Use CCModel.generateBlock(0, new Cuboid6(<bounds>)).apply<rotation and such.>
public class BlockModelBakery {

    public boolean flipTexture;
    public boolean renderFromInside = false;

    public double renderMinX;
    public double renderMaxX;
    public double renderMinY;
    public double renderMaxY;
    public double renderMinZ;
    public double renderMaxZ;

    public int uvRotateEast, uvRotateWest, uvRotateSouth, uvRotateNorth, uvRotateTop, uvRotateBottom;

    public Colour colourTopLeft = new ColourRGBA(0xFFFFFFFF);
    public Colour colourBottomLeft = new ColourRGBA(0xFFFFFFFF);
    public Colour colourBottomRight = new ColourRGBA(0xFFFFFFFF);
    public Colour colourTopRight = new ColourRGBA(0xFFFFFFFF);

    private CCQuadBakery quadBakery;
    private LinkedList<BakedQuad> quadList = new LinkedList<BakedQuad>();

    public BlockModelBakery() {
        this(DefaultVertexFormats.BLOCK);
    }

    public BlockModelBakery(VertexFormat format) {
        quadBakery = new CCQuadBakery(format, null);
        reset();
    }

    public void reset() {
        quadList.clear();
        colourTopLeft = new ColourRGBA(0xFFFFFFFF);
        colourBottomLeft = new ColourRGBA(0xFFFFFFFF);
        colourBottomRight = new ColourRGBA(0xFFFFFFFF);
        colourTopRight = new ColourRGBA(0xFFFFFFFF);

        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;

        flipTexture = false;
        renderFromInside = false;

        setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }

    public LinkedList<BakedQuad> finishBaking() {
        return new LinkedList<BakedQuad>(quadList);
    }

    public CCQuadBakery getQuadBakery() {
        return quadBakery;
    }

    public void setRenderBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

        this.renderMinX = minX;
        this.renderMaxX = maxX;
        this.renderMinY = minY;
        this.renderMaxY = maxY;
        this.renderMinZ = minZ;
        this.renderMaxZ = maxZ;

    }

    public void bakeFace(TextureAtlasSprite sprite, EnumFacing face) {
        switch (face) {

            case DOWN:
                bakeFaceYNeg(sprite);
                break;
            case UP:
                bakeFaceYPos(sprite);
                break;
            case NORTH:
                bakeFaceZNeg(sprite);
                break;
            case SOUTH:
                bakeFaceZPos(sprite);
                break;
            case WEST:
                bakeFaceXNeg(sprite);
                break;
            case EAST:
                bakeFaceXPos(sprite);
                break;
        }
    }

    public void bakeFaceYNeg(TextureAtlasSprite sprite) {
        bakeFaceYNeg(0, 0, 0, sprite);
    }

    public void bakeFaceYNeg(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) sprite.getInterpolatedV(this.renderMinZ * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(this.renderMaxZ * 16.0D);
        double d7;
        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateBottom == 2) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateBottom == 1) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateBottom == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMinX;
        double d12 = posX + this.renderMaxX;
        double d13 = posY + this.renderMinY;
        double d14 = posZ + this.renderMinZ;
        double d15 = posZ + this.renderMaxZ;

        if (this.renderFromInside) {
            d11 = posX + this.renderMaxX;
            d12 = posX + this.renderMinX;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d11, d13, d15, d8, d10);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d11, d13, d14, d3, d5);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d12, d13, d14, d7, d9);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d12, d13, d15, d4, d6);

        quadList.addAll(quadBakery.finishBaking());
    }

    public void bakeFaceYPos(TextureAtlasSprite sprite) {
        bakeFaceYPos(0, 0, 0, sprite);
    }

    public void bakeFaceYPos(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) sprite.getInterpolatedV(this.renderMinZ * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(this.renderMaxZ * 16.0D);
        double d7;

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateTop == 1) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateTop == 2) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateTop == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMinX;
        double d12 = posX + this.renderMaxX;
        double d13 = posY + this.renderMaxY;
        double d14 = posZ + this.renderMinZ;
        double d15 = posZ + this.renderMaxZ;

        if (this.renderFromInside) {
            d11 = posX + this.renderMaxX;
            d12 = posX + this.renderMinX;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d12, d13, d15, d4, d6);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d12, d13, d14, d7, d9);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d11, d13, d14, d3, d5);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d11, d13, d15, d8, d10);

        quadList.addAll(quadBakery.finishBaking());
    }

    public void bakeFaceZNeg(TextureAtlasSprite sprite) {
        bakeFaceZNeg(0, 0, 0, sprite);
    }

    public void bakeFaceZNeg(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxX * 16.0D);

        double d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateEast == 2) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinY * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateEast == 1) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxX * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateEast == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMinX;
        double d12 = posX + this.renderMaxX;
        double d13 = posY + this.renderMinY;
        double d14 = posY + this.renderMaxY;
        double d15 = posZ + this.renderMinZ;

        if (this.renderFromInside) {
            d11 = posX + this.renderMaxX;
            d12 = posX + this.renderMinX;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d11, d14, d15, d7, d9);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d12, d14, d15, d3, d5);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d12, d13, d15, d8, d10);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d11, d13, d15, d4, d6);

        quadList.addAll(quadBakery.finishBaking());
    }

    public void bakeFaceZPos(TextureAtlasSprite sprite) {
        bakeFaceZPos(0, 0, 0, sprite);
    }

    public void bakeFaceZPos(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinX * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxX * 16.0D);
        double d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinX < 0.0D || this.renderMaxX > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateWest == 1) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateWest == 2) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateWest == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMinX;
        double d12 = posX + this.renderMaxX;
        double d13 = posY + this.renderMinY;
        double d14 = posY + this.renderMaxY;
        double d15 = posZ + this.renderMaxZ;

        if (this.renderFromInside) {
            d11 = posX + this.renderMaxX;
            d12 = posX + this.renderMinX;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d11, d14, d15, d3, d5);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d11, d13, d15, d8, d10);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d12, d13, d15, d4, d6);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d12, d14, d15, d7, d9);

        quadList.addAll(quadBakery.finishBaking());
    }

    public void bakeFaceXNeg(TextureAtlasSprite sprite) {
        bakeFaceXNeg(0, 0, 0, sprite);
    }

    public void bakeFaceXNeg(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinZ * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxZ * 16.0D);
        double d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateNorth == 1) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateNorth == 2) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMinZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateNorth == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMinX;
        double d12 = posY + this.renderMinY;
        double d13 = posY + this.renderMaxY;
        double d14 = posZ + this.renderMinZ;
        double d15 = posZ + this.renderMaxZ;

        if (this.renderFromInside) {
            d14 = posZ + this.renderMaxZ;
            d15 = posZ + this.renderMinZ;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d11, d13, d15, d7, d9);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d11, d13, d14, d3, d5);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d11, d12, d14, d8, d10);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d11, d12, d15, d4, d6);

        quadList.addAll(quadBakery.finishBaking());
    }

    public void bakeFaceXPos(TextureAtlasSprite sprite) {
        bakeFaceXPos(0, 0, 0, sprite);
    }

    public void bakeFaceXPos(double posX, double posY, double posZ, TextureAtlasSprite sprite) {
        quadBakery.startBakingQuads().setSprite(sprite);

        double d3 = (double) sprite.getInterpolatedU(this.renderMinZ * 16.0D);
        double d4 = (double) sprite.getInterpolatedU(this.renderMaxZ * 16.0D);

        double d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxY * 16.0D);
        double d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMinY * 16.0D);
        double d7;

        if (this.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (this.renderMinZ < 0.0D || this.renderMaxZ > 1.0D) {
            d3 = (double) sprite.getMinU();
            d4 = (double) sprite.getMaxU();
        }

        if (this.renderMinY < 0.0D || this.renderMaxY > 1.0D) {
            d5 = (double) sprite.getMinV();
            d6 = (double) sprite.getMaxV();
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        if (this.uvRotateSouth == 2) {
            d3 = (double) sprite.getInterpolatedU(this.renderMinY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateSouth == 1) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxY * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMinY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateSouth == 3) {
            d3 = (double) sprite.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d4 = (double) sprite.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) sprite.getInterpolatedV(this.renderMaxY * 16.0D);
            d6 = (double) sprite.getInterpolatedV(this.renderMinY * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }

        double d11 = posX + this.renderMaxX;
        double d12 = posY + this.renderMinY;
        double d13 = posY + this.renderMaxY;
        double d14 = posZ + this.renderMinZ;
        double d15 = posZ + this.renderMaxZ;

        if (this.renderFromInside) {
            d14 = posZ + this.renderMaxZ;
            d15 = posZ + this.renderMinZ;
        }

        quadBakery.setColour(colourTopLeft).addVertexWithUV(d11, d12, d15, d8, d10);
        quadBakery.setColour(colourBottomLeft).addVertexWithUV(d11, d12, d14, d4, d6);
        quadBakery.setColour(colourBottomRight).addVertexWithUV(d11, d13, d14, d7, d9);
        quadBakery.setColour(colourTopRight).addVertexWithUV(d11, d13, d15, d3, d5);

        quadList.addAll(quadBakery.finishBaking());
    }
}
