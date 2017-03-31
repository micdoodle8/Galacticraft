package micdoodle8.mods.galacticraft.core.world.gen;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.Random;

public class StructureComponentVillageStartPiece extends StructureComponentVillageWell
{
    public BiomeProvider biomeProvider;
    public int terrainType;
    public StructureVillagePieceWeightMoon structVillagePieceWeight;
    public ArrayList<StructureVillagePieceWeightMoon> structureVillageWeightedPieceList;
    public ArrayList<Object> field_74932_i = new ArrayList<Object>();
    public ArrayList<Object> field_74930_j = new ArrayList<Object>();

    public StructureComponentVillageStartPiece()
    {
    }

    public StructureComponentVillageStartPiece(BiomeProvider biomeProvider, int par2, Random par3Random, int par4, int par5, ArrayList<StructureVillagePieceWeightMoon> par6ArrayList, int par7)
    {
        super(null, 0, par3Random, par4, par5);
        this.biomeProvider = biomeProvider;
        this.structureVillageWeightedPieceList = par6ArrayList;
        this.terrainType = par7;
        this.startPiece = this;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound nbt)
    {
        super.writeStructureToNBT(nbt);

        nbt.setInteger("TerrainType", this.terrainType);
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
    {
        super.readStructureFromNBT(nbt, manager);

        this.terrainType = nbt.getInteger("TerrainType");
    }

    public BiomeProvider getBiomeProvider()
    {
        return this.biomeProvider;
    }
}
