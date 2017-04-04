package micdoodle8.mods.galacticraft.planets.asteroids.world.gen.base;

import com.google.common.collect.Lists;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.List;
import java.util.Random;

public class BaseStart extends BaseDeck
{
    public List<StructureComponent> attachedComponents = Lists.newArrayList();
    public List<StructureBoundingBox> componentBounds = Lists.newArrayList();

    public BaseStart()
    {
    }

    public BaseStart(BaseConfiguration configuration, Random rand, int blockPosX, int blockPosZ)
    {
        super(configuration, rand, blockPosX, configuration.getYPosition(), blockPosZ, 1, EnumFacing.Plane.HORIZONTAL.random(rand));
    }

    @Override
    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand)
    {
        attachedComponents.clear();
        componentBounds.clear();
        componentBounds.add(this.boundingBox);
        listIn.clear();
        listIn.add(this);
        List<Piece> rooms = getRooms(this, rand);
        for(Piece next : rooms)
        {
            listIn.add(next);
            attachedComponents.add(next);
            componentBounds.add(next.getBoundingBox());
        }

        //TODO  applyAsteroidDamage();

        super.buildComponent(componentIn, listIn, rand);
    }
}