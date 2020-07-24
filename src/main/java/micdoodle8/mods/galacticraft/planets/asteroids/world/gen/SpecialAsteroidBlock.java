package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import net.minecraft.block.BlockState;

import java.util.ArrayList;

public class SpecialAsteroidBlock
{

    public final BlockState state;
    public final int probability;
    public final double thickness; //Arbitrary scale from 0 to 1;
    public final int index;
    public static final ArrayList<SpecialAsteroidBlock> register = new ArrayList<>();

    public SpecialAsteroidBlock(BlockState state, int probability, double thickness)
    {
        this.state = state;
        this.probability = probability;
        this.thickness = thickness;
        this.index = register.size();
        register.add(this);
    }

}