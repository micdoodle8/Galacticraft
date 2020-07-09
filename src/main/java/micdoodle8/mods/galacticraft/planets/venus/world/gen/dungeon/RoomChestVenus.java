package micdoodle8.mods.galacticraft.planets.venus.world.gen.dungeon;

import micdoodle8.mods.galacticraft.api.world.IGalacticraftDimension;
import micdoodle8.mods.galacticraft.core.blocks.BlockTier1TreasureChest;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_CHEST;
import static micdoodle8.mods.galacticraft.planets.venus.world.gen.VenusFeatures.CVENUS_DUNGEON_TREASURE;

public class RoomChestVenus extends RoomEmptyVenus
{
    public RoomChestVenus(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CVENUS_DUNGEON_CHEST, nbt);
    }

    public RoomChestVenus(DungeonConfigurationVenus configuration, Random rand, int blockPosX, int blockPosZ, int sizeX, int sizeY, int sizeZ, Direction entranceDir)
    {
        super(CVENUS_DUNGEON_CHEST, configuration, rand, blockPosX, blockPosZ, sizeX, sizeY, sizeZ, entranceDir);
    }

    @Override
    public boolean addComponentParts(IWorld worldIn, Random random, MutableBoundingBox chunkBox, ChunkPos pos)
    {
        if (super.addComponentParts(worldIn, random, chunkBox, pos))
        {
            int chestX = this.sizeX / 2;
            int chestY = 1;
            int chestZ = this.sizeZ / 2;
            this.setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(BlockTier1TreasureChest.FACING, this.getDirection().getOpposite()), chestX, chestY, chestZ, boundingBox);

            BlockPos blockpos = new BlockPos(this.getXWithOffset(chestX, chestZ), this.getYWithOffset(chestY), this.getZWithOffset(chestX, chestZ));
            ChestTileEntity chest = (ChestTileEntity) worldIn.getTileEntity(blockpos);

            if (chest != null)
            {
                ResourceLocation chesttype = RoomTreasureVenus.VENUSCHEST;
                if (worldIn.getDimension() instanceof IGalacticraftDimension)
                {
                    chesttype = ((IGalacticraftDimension) worldIn.getDimension()).getDungeonChestType();
                }
                chest.setLootTable(chesttype, random.nextLong());
            }

            return true;
        }

        return false;
    }
}