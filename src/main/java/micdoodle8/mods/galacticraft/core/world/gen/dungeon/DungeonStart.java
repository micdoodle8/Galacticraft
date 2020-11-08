package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

import static micdoodle8.mods.galacticraft.core.world.gen.GCFeatures.CMOON_DUNGEON_START;

public class DungeonStart extends EntranceCrater
{
    public List<StructurePiece> attachedComponents = Lists.newArrayList();
    public List<MutableBoundingBox> componentBounds = Lists.newArrayList();

    //    public DungeonStart(IStructurePieceType type)
//    {
//        super(type);
//    }
//
    public DungeonStart(World world, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(CMOON_DUNGEON_START, world, configuration, rand, blockPosX, blockPosZ);
    }

    public DungeonStart(TemplateManager templateManager, CompoundNBT nbt)
    {
        super(CMOON_DUNGEON_START, nbt);
    }

    @Override
    public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand)
    {
        System.out.println((boundingBox.maxX + boundingBox.minX) / 2 + " " + 100 + " " + (boundingBox.maxZ + boundingBox.minZ) / 2);
        boolean validAttempt = false;
        final int maxAttempts = 10;
        int attempts = 0;
        while (!validAttempt && attempts < maxAttempts)
        {
            attachedComponents.clear();
            componentBounds.clear();
            componentBounds.add(this.boundingBox);
            listIn.clear();
            listIn.add(this);
            Piece next = getNextPiece(this, rand);
            while (next != null)
            {
                listIn.add(next);
                attachedComponents.add(next);
                componentBounds.add(next.getBoundingBox());

                next = next.getNextPiece(this, rand);
            }
            if (attachedComponents.size() >= 3 && attachedComponents.get(attachedComponents.size() - 1) instanceof RoomTreasure &&
                    attachedComponents.get(attachedComponents.size() - 3) instanceof RoomBoss)
            {
                validAttempt = true;
            }
            attempts++;
        }

        GCLog.debug("Dungeon generation took " + attempts + " attempt(s)");

        if (!validAttempt)
        {
            int xPos = this.boundingBox.minX + (this.boundingBox.maxX - this.boundingBox.minX) / 2;
            int zPos = this.boundingBox.minZ + (this.boundingBox.maxZ - this.boundingBox.minZ) / 2;
            System.err.println("Could not find valid dungeon layout! This is a bug, please report it, including your world seed (/seed) and dungeon location (" + xPos + ", " + zPos + ")");
        }

        super.buildComponent(componentIn, listIn, rand);
    }

    public boolean checkIntersection(int blockX, int blockZ, int sizeX, int sizeZ)
    {
        return this.checkIntersection(new MutableBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ));
    }

    public boolean checkIntersection(MutableBoundingBox bounds)
    {
        for (int i = 0; i < componentBounds.size() - 1; ++i)
        {
            MutableBoundingBox boundingBox = componentBounds.get(i);
            if (boundingBox.intersectsWith(bounds))
            {
                return true;
            }
        }

        return false;
    }
}