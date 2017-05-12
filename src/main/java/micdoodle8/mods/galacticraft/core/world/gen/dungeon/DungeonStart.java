package micdoodle8.mods.galacticraft.core.world.gen.dungeon;

import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class DungeonStart extends RoomEntrance
{
    public List<StructureComponent> attachedComponents = Lists.newArrayList();
    public List<StructureBoundingBox> componentBounds = Lists.newArrayList();

    public DungeonStart()
    {
    }

    public DungeonStart(World world, DungeonConfiguration configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(world, configuration, rand, blockPosX, blockPosZ);
    }

    @Override
    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
    {
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
        return this.checkIntersection(new StructureBoundingBox(blockX, blockZ, blockX + sizeX, blockZ + sizeZ));
    }

    public boolean checkIntersection(StructureBoundingBox bounds)
    {
        for (int i = 0; i < componentBounds.size() - 1; ++i)
        {
            StructureBoundingBox boundingBox = componentBounds.get(i);
            if (boundingBox.intersectsWith(bounds))
            {
                return true;
            }
        }

        return false;
    }
}