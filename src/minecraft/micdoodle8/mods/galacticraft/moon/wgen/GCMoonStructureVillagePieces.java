package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class GCMoonStructureVillagePieces
{
    public static ArrayList getStructureVillageWeightedPieceList(Random par0Random, int par1)
    {
        ArrayList var2 = new ArrayList();
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageHouse4_Garden.class, 4, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 4 + par1 * 2)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageChurch.class, 20, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 1 + par1)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageHouse1.class, 20, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 2 + par1)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageWoodHut.class, 3, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 5 + par1 * 3)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageHall.class, 15, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 2 + par1)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageField.class, 20, MathHelper.getRandomIntegerInRange(par0Random, 1 + par1, 4 + par1)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageField2.class, 3, MathHelper.getRandomIntegerInRange(par0Random, 2 + par1, 4 + par1 * 2)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageHouse2.class, 15, MathHelper.getRandomIntegerInRange(par0Random, 0, 1 + par1)));
        var2.add(new GCMoonStructureVillagePieceWeight(GCMoonComponentVillageHouse3.class, 8, MathHelper.getRandomIntegerInRange(par0Random, 0 + par1, 3 + par1 * 2)));
        VillagerRegistry.addExtraVillageComponents(var2, par0Random, par1);

        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            if (((GCMoonStructureVillagePieceWeight)var3.next()).villagePiecesLimit == 0)
            {
                var3.remove();
            }
        }

        return var2;
    }

    private static int func_75079_a(List par0List)
    {
        boolean var1 = false;
        int var2 = 0;
        GCMoonStructureVillagePieceWeight var4;

        for (Iterator var3 = par0List.iterator(); var3.hasNext(); var2 += var4.villagePieceWeight)
        {
            var4 = (GCMoonStructureVillagePieceWeight)var3.next();

            if (var4.villagePiecesLimit > 0 && var4.villagePiecesSpawned < var4.villagePiecesLimit)
            {
                var1 = true;
            }
        }

        return var1 ? var2 : -1;
    }

    private static GCMoonComponentVillage func_75083_a(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, GCMoonStructureVillagePieceWeight par1StructureVillagePieceWeight, List par2List, Random par3Random, int par4, int par5, int par6, int par7, int par8)
    {
        Class var9 = par1StructureVillagePieceWeight.villagePieceClass;
        Object var10 = null;

        if (var9 == GCMoonComponentVillageHouse4_Garden.class)
        {
            var10 = GCMoonComponentVillageHouse4_Garden.func_74912_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageChurch.class)
        {
            var10 = GCMoonComponentVillageChurch.func_74919_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageHouse1.class)
        {
            var10 = GCMoonComponentVillageHouse1.func_74898_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageWoodHut.class)
        {
            var10 = GCMoonComponentVillageWoodHut.func_74908_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageHall.class)
        {
            var10 = GCMoonComponentVillageHall.func_74906_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageField.class)
        {
            var10 = GCMoonComponentVillageField.func_74900_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageField2.class)
        {
            var10 = GCMoonComponentVillageField2.func_74902_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageHouse2.class)
        {
            var10 = GCMoonComponentVillageHouse2.func_74915_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }
        else if (var9 == GCMoonComponentVillageHouse3.class)
        {
            var10 = GCMoonComponentVillageHouse3.func_74921_a(par0ComponentVillageStartPiece, par2List, par3Random, par4, par5, par6, par7, par8);
        }

        return (GCMoonComponentVillage)var10;
    }

    /**
     * attempts to find a next Village Component to be spawned
     */
    private static GCMoonComponentVillage getNextVillageComponent(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        int var8 = func_75079_a(par0ComponentVillageStartPiece.structureVillageWeightedPieceList);

        if (var8 <= 0)
        {
            return null;
        }
        else
        {
            int var9 = 0;

            while (var9 < 5)
            {
                ++var9;
                int var10 = par2Random.nextInt(var8);
                Iterator var11 = par0ComponentVillageStartPiece.structureVillageWeightedPieceList.iterator();

                while (var11.hasNext())
                {
                    GCMoonStructureVillagePieceWeight var12 = (GCMoonStructureVillagePieceWeight)var11.next();
                    var10 -= var12.villagePieceWeight;

                    if (var10 < 0)
                    {
                        if (!var12.canSpawnMoreVillagePiecesOfType(par7) || var12 == par0ComponentVillageStartPiece.structVillagePieceWeight && par0ComponentVillageStartPiece.structureVillageWeightedPieceList.size() > 1)
                        {
                            break;
                        }

                        GCMoonComponentVillage var13 = func_75083_a(par0ComponentVillageStartPiece, var12, par1List, par2Random, par3, par4, par5, par6, par7);

                        if (var13 != null)
                        {
                            ++var12.villagePiecesSpawned;
                            par0ComponentVillageStartPiece.structVillagePieceWeight = var12;

                            if (!var12.canSpawnMoreVillagePieces())
                            {
                                par0ComponentVillageStartPiece.structureVillageWeightedPieceList.remove(var12);
                            }

                            return var13;
                        }
                    }
                }
            }

            StructureBoundingBox var14 = GCMoonComponentVillageTorch.func_74904_a(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6);

            if (var14 != null)
            {
                return new GCMoonComponentVillageTorch(par0ComponentVillageStartPiece, par7, par2Random, var14, par6);
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * attempts to find a next Structure Component to be spawned, private Village function
     */
    private static StructureComponent getNextVillageStructureComponent(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 50)
        {
            return null;
        }
        else if (Math.abs(par3 - par0ComponentVillageStartPiece.getBoundingBox().minX) <= 112 && Math.abs(par5 - par0ComponentVillageStartPiece.getBoundingBox().minZ) <= 112)
        {
            GCMoonComponentVillage var8 = getNextVillageComponent(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7 + 1);

            if (var8 != null)
            {
                int var9 = (var8.getBoundingBox().minX + var8.getBoundingBox().maxX) / 2;
                int var10 = (var8.getBoundingBox().minZ + var8.getBoundingBox().maxZ) / 2;
                int var11 = var8.getBoundingBox().maxX - var8.getBoundingBox().minX;
                int var12 = var8.getBoundingBox().maxZ - var8.getBoundingBox().minZ;
                int var13 = var11 > var12 ? var11 : var12;

//                if (par0ComponentVillageStartPiece.getWorldChunkManager().areBiomesViable(var9, var10, var13 / 2 + 4, GCMoonMapGenVillage.villageSpawnBiomes))
                {
                    par1List.add(var8);
                    par0ComponentVillageStartPiece.field_74932_i.add(var8);
                    return var8;
                }
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    private static StructureComponent getNextComponentVillagePath(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 3 + par0ComponentVillageStartPiece.terrainType)
        {
            return null;
        }
        else if (Math.abs(par3 - par0ComponentVillageStartPiece.getBoundingBox().minX) <= 112 && Math.abs(par5 - par0ComponentVillageStartPiece.getBoundingBox().minZ) <= 112)
        {
            StructureBoundingBox var8 = GCMoonComponentVillagePathGen.func_74933_a(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6);

            if (var8 != null && var8.minY > 10)
            {
                GCMoonComponentVillagePathGen var9 = new GCMoonComponentVillagePathGen(par0ComponentVillageStartPiece, par7, par2Random, var8, par6);
                int var10 = (var9.getBoundingBox().minX + var9.getBoundingBox().maxX) / 2;
                int var11 = (var9.getBoundingBox().minZ + var9.getBoundingBox().maxZ) / 2;
                int var12 = var9.getBoundingBox().maxX - var9.getBoundingBox().minX;
                int var13 = var9.getBoundingBox().maxZ - var9.getBoundingBox().minZ;
                int var14 = var12 > var13 ? var12 : var13;

//                if (par0ComponentVillageStartPiece.getWorldChunkManager().areBiomesViable(var10, var11, var14 / 2 + 4, GCMoonMapGenVillage.villageSpawnBiomes))
                {
                    par1List.add(var9);
                    par0ComponentVillageStartPiece.field_74930_j.add(var9);
                    return var9;
                }
            }

            return null;
        }
        else
        {
            return null;
        }
    }

    /**
     * attempts to find a next Structure Component to be spawned
     */
    static StructureComponent getNextStructureComponent(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextVillageStructureComponent(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static StructureComponent getNextStructureComponentVillagePath(GCMoonComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextComponentVillagePath(par0ComponentVillageStartPiece, par1List, par2Random, par3, par4, par5, par6, par7);
    }
}
