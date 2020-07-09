package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import java.util.ArrayList;

public class SpecialAsteroidBlock
{

    public BlockState state;
    public int probability;
    public double thickness; //Arbitrary scale from 0 to 1;
    public int index;
    public static ArrayList<SpecialAsteroidBlock> register = new ArrayList<>();

    public SpecialAsteroidBlock(BlockState state, int probability, double thickness)
    {
        this.state = state;
        this.probability = probability;
        this.thickness = thickness;
        this.index = register.size();
        register.add(this);
    }

}