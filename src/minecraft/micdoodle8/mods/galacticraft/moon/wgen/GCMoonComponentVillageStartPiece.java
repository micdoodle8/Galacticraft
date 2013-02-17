package micdoodle8.mods.galacticraft.moon.wgen;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class GCMoonComponentVillageStartPiece extends GCMoonComponentVillageWell
{
    public final WorldChunkManager worldChunkMngr;

    /** Boolean that determines if the village is in a desert or not. */
    public final boolean inDesert;
    public final BiomeGenBase biome;

    /** World terrain type, 0 for normal, 1 for flap map */
    public final int terrainType;
    public GCMoonStructureVillagePieceWeight structVillagePieceWeight;

    /**
     * Contains List of all spawnable Structure Piece Weights. If no more Pieces of a type can be spawned, they are
     * removed from this list
     */
    public ArrayList structureVillageWeightedPieceList;
    public ArrayList field_74932_i = new ArrayList();
    public ArrayList field_74930_j = new ArrayList();

    public GCMoonComponentVillageStartPiece(WorldChunkManager par1WorldChunkManager, int par2, Random par3Random, int par4, int par5, ArrayList par6ArrayList, int par7)
    {
        super((GCMoonComponentVillageStartPiece)null, 0, par3Random, par4, par5);
        this.worldChunkMngr = par1WorldChunkManager;
        this.structureVillageWeightedPieceList = par6ArrayList;
        this.terrainType = par7;
        final BiomeGenBase var8 = par1WorldChunkManager.getBiomeGenAt(par4, par5);
        this.inDesert = var8 == BiomeGenBase.desert || var8 == BiomeGenBase.desertHills;
        this.biome = var8;
        this.startPiece = this;
    }

    public WorldChunkManager getWorldChunkManager()
    {
        return this.worldChunkMngr;
    }
}
