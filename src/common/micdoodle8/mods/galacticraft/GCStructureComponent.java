package micdoodle8.mods.galacticraft;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.StructureBoundingBox;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.World;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public abstract class GCStructureComponent extends StructureComponent
{
    public GCStructureComponent(int var1)
    {
        super(var1);
    }

    public static StructureBoundingBox getComponentToAddBoundingBox(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
    {
        switch (var9)
        {
            case 0:
                return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 + var3, var1 + var7 + var4, var2 + var8 + var5);

            case 1:
                return new StructureBoundingBox(var0 - var8 + var5, var1 + var4, var2 + var3, var0 + var5, var1 + var7 + var4, var2 + var6 + var3);

            case 2:
                return new StructureBoundingBox(var0 - var6 - var3, var1 + var4, var2 - var8 - var5, var0 - var3, var1 + var7 + var4, var2 - var5);

            case 3:
                return new StructureBoundingBox(var0 + var5, var1 + var4, var2 - var6, var0 + var8 + var5, var1 + var7 + var4, var2 + var3);

            default:
                return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 + var3, var1 + var7 + var4, var2 + var8 + var5);
        }
    }

    protected void placeSpawnerAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, String var6, StructureBoundingBox var7)
    {
        int var8 = this.getXWithOffset(var3, var5);
        int var9 = this.getYWithOffset(var4);
        int var10 = this.getZWithOffset(var3, var5);

        if (var7.isVecInside(var8, var9, var10) && var1.getBlockId(var8, var9, var10) != Block.mobSpawner.blockID)
        {
            var1.setBlockWithNotify(var8, var9, var10, Block.mobSpawner.blockID);
            TileEntityMobSpawner var11 = (TileEntityMobSpawner)var1.getBlockTileEntity(var8, var9, var10);

            if (var11 != null)
            {
                var11.setMobID(var6);
            }
        }
    }

    protected void placeTreasureAtCurrentPosition(World var1, Random var2, int var3, int var4, int var5, StructureBoundingBox var7)
    {
        int var8 = this.getXWithOffset(var3, var5);
        int var9 = this.getYWithOffset(var4);
        int var10 = this.getZWithOffset(var3, var5);

        if (var7.isVecInside(var8, var9, var10) && var1.getBlockId(var8, var9, var10) != Block.chest.blockID)
        {
            GCUtil.generateChestContents(var1, var2, var8, var9, var10);
        }
    }

    protected int[] offsetTowerCoords(int var1, int var2, int var3, int var4, int var5)
    {
        int var6 = this.getXWithOffset(var1, var3);
        int var7 = this.getYWithOffset(var2);
        int var8 = this.getZWithOffset(var1, var3);
        return var5 == 0 ? new int[] {var6 + 1, var7 - 1, var8 - var4 / 2}: (var5 == 1 ? new int[] {var6 + var4 / 2, var7 - 1, var8 + 1}: (var5 == 2 ? new int[] {var6 - 1, var7 - 1, var8 + var4 / 2}: (var5 == 3 ? new int[] {var6 - var4 / 2, var7 - 1, var8 - 1}: new int[] {var1, var2, var3})));
    }

    public int[] getOffsetAsIfRotated(int[] var1, int var2)
    {
        int var3 = this.getCoordBaseMode();
        int[] var4 = new int[3];
        this.setCoordBaseMode(var2);
        var4[0] = this.getXWithOffset(var1[0], var1[2]);
        var4[1] = this.getYWithOffset(var1[1]);
        var4[2] = this.getZWithOffset(var1[0], var1[2]);
        this.setCoordBaseMode(var3);
        return var4;
    }

    protected int getXWithOffset(int var1, int var2)
    {
        switch (this.getCoordBaseMode())
        {
            case 0:
                return this.boundingBox.minX + var1;

            case 1:
                return this.boundingBox.maxX - var2;

            case 2:
                return this.boundingBox.maxX - var1;

            case 3:
                return this.boundingBox.minX + var2;

            default:
                return var1;
        }
    }

    protected int getZWithOffset(int var1, int var2)
    {
        switch (this.getCoordBaseMode())
        {
            case 0:
                return this.boundingBox.minZ + var2;

            case 1:
                return this.boundingBox.minZ + var1;

            case 2:
                return this.boundingBox.maxZ - var2;

            case 3:
                return this.boundingBox.maxZ - var1;

            default:
                return var2;
        }
    }

    protected int getYWithOffset(int var1)
    {
        return super.getYWithOffset(var1);
    }

    public int getCoordBaseMode()
    {
        return this.coordBaseMode;
    }

    public void setCoordBaseMode(int var1)
    {
        this.coordBaseMode = var1;
    }

    protected int getBlockIdAtCurrentPosition(World var1, int var2, int var3, int var4, StructureBoundingBox var5)
    {
        return super.getBlockIdAtCurrentPosition(var1, var2, var3, var4, var5);
    }

    protected void placeBlockAtCurrentPosition(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7)
    {
        super.placeBlockAtCurrentPosition(var1, var2, var3, var4, var5, var6, var7);
    }
}
