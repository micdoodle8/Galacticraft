package micdoodle8.mods.galacticraft.moon.wgen;

public class GCMoonStructureVillagePieceWeight
{
    /** The Class object for the represantation of this village piece. */
    public Class villagePieceClass;
    public final int villagePieceWeight;
    public int villagePiecesSpawned;
    public int villagePiecesLimit;

    public GCMoonStructureVillagePieceWeight(Class par1Class, int par2, int par3)
    {
        this.villagePieceClass = par1Class;
        this.villagePieceWeight = par2;
        this.villagePiecesLimit = (int) (par3 / 1.5D);
    }

    public boolean canSpawnMoreVillagePiecesOfType(int par1)
    {
        return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
    }

    public boolean canSpawnMoreVillagePieces()
    {
        return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
    }
}
