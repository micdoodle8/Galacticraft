package micdoodle8.mods.galacticraft.planets.asteroids.world.gen;

import net.minecraft.block.Block;

import java.util.ArrayList;

public class SpecialAsteroidBlock
{

    public Block block;
    public byte meta;
    public int probability;
    public double thickness; //Arbitrary scale from 0 to 1;
    public int index;
    public static ArrayList<SpecialAsteroidBlock> register = new ArrayList<>();

    public SpecialAsteroidBlock(Block block, byte meta, int probability, double thickness)
    {
        this.block = block;
        this.meta = meta;
        this.probability = probability;
        this.thickness = thickness;
        this.index = register.size();
        register.add(this);
    }

}