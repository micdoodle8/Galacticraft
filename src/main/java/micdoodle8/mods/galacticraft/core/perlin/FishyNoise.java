package micdoodle8.mods.galacticraft.core.perlin;

import java.util.Random;

public class FishyNoise
{

    int[] perm = new int[512];

    public float[][] grad2d = new float[][] { { 1, 0 }, { .9239F, .3827F }, { .707107F, 0.707107F }, { .3827F, .9239F }, { 0, 1 }, { -.3827F, .9239F }, { -.707107F, 0.707107F }, { -.9239F, .3827F }, { -1, 0 }, { -.9239F, -.3827F }, { -.707107F, -0.707107F }, { -.3827F, -.9239F }, { 0, -1 }, { .3827F, -.9239F }, { .707107F, -0.707107F }, { .9239F, -.3827F } };

    public int[][] grad3d = new int[][] { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 }, { 1, 1, 0 }, { -1, 1, 0 }, { 0, -1, 1 }, { 0, -1, -1 } };

    public FishyNoise(long seed)
    {
        final Random rand = new Random(seed);
        for (int i = 0; i < 256; i++)
        {
            this.perm[i] = i; // Fill up the random array with numbers 0-256
        }

        for (int i = 0; i < 256; i++) // Shuffle those numbers for the random
        // effect
        {
            final int j = rand.nextInt(256);
            this.perm[i] = this.perm[i] ^ this.perm[j];
            this.perm[j] = this.perm[i] ^ this.perm[j];
            this.perm[i] = this.perm[i] ^ this.perm[j];
        }

        System.arraycopy(this.perm, 0, this.perm, 256, 256);
    }

    private static float lerp(float x, float y, float n)
    {
        return x + n * (y - x);
    }

    private static int fastFloor(float x)
    {
        return x > 0 ? (int) x : (int) x - 1;
    }

    private static float fade(float n)
    {
        return n * n * n * (n * (n * 6 - 15) + 10);
    }

    private static float dot2(float[] grad2, float x, float y)
    {
        return grad2[0] * x + grad2[1] * y;
    }

    private static float dot3(int[] grad3, float x, float y, float z)
    {
        return grad3[0] * x + grad3[1] * y + grad3[2] * z;
    }

    public float noise2d(float x, float y)
    {
        int largeX = x > 0 ? (int) x : (int) x - 1;
        int largeY = y > 0 ? (int) y : (int) y - 1;
        x -= largeX;
        y -= largeY;
        largeX &= 255;
        largeY &= 255;

        final float u = x * x * x * (x * (x * 6 - 15) + 10);
        final float v = y * y * y * (y * (y * 6 - 15) + 10);

        int randY = this.perm[largeY];
        int randY1 = this.perm[largeY + 1];
        float[] grad2 = this.grad2d[this.perm[largeX + randY] & 15];
        final float grad00 = grad2[0] * x + grad2[1] * y;
        grad2 = this.grad2d[this.perm[largeX + randY1] & 15];
        final float grad01 = grad2[0] * x + grad2[1] * (y - 1);
        grad2 = this.grad2d[this.perm[largeX + 1 + randY1] & 15];
        final float grad11 = grad2[0] * (x - 1) + grad2[1] * (y - 1);
        grad2 = this.grad2d[this.perm[largeX + 1 + randY] & 15];
        final float grad10 = grad2[0] * (x - 1) + grad2[1] * y;

        final float lerpX0 = grad00 + u * (grad10 - grad00);
        return lerpX0 + v * (grad01 + u * (grad11 - grad01) - lerpX0);
    }

    public float noise3d(float x, float y, float z)
    {
        int unitX = x > 0 ? (int) x : (int) x - 1;
        int unitY = y > 0 ? (int) y : (int) y - 1;
        int unitZ = z > 0 ? (int) z : (int) z - 1;

        x -= unitX;
        y -= unitY;
        z -= unitZ;

        unitX = unitX & 255;
        unitY = unitY & 255;
        unitZ = unitZ & 255;

        final float u = x * x * x * (x * (x * 6 - 15) + 10);
        final float v = y * y * y * (y * (y * 6 - 15) + 10);
        final float w = z * z * z * (z * (z * 6 - 15) + 10);

        int randZ = this.perm[unitZ];
        int randZ1 = this.perm[unitZ + 1];
        int randYZ = this.perm[unitY + randZ];
        int randY1Z = this.perm[unitY + 1 + randZ];
        int randYZ1 = this.perm[unitY + randZ1];
        int randY1Z1 = this.perm[unitY + 1 + randZ1];
        final float grad000 = FishyNoise.dot3(this.grad3d[this.perm[unitX + randYZ] & 15], x, y, z);
        final float grad100 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + randYZ] & 15], x - 1, y, z);
        final float grad010 = FishyNoise.dot3(this.grad3d[this.perm[unitX + randY1Z] & 15], x, y - 1, z);
        final float grad110 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + randY1Z] & 15], x - 1, y - 1, z);
        final float grad001 = FishyNoise.dot3(this.grad3d[this.perm[unitX + randYZ1] & 15], x, y, z - 1);
        final float grad101 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + randYZ1] & 15], x - 1, y, z - 1);
        final float grad011 = FishyNoise.dot3(this.grad3d[this.perm[unitX + randY1Z1] & 15], x, y - 1, z - 1);
        final float grad111 = FishyNoise.dot3(this.grad3d[this.perm[unitX + 1 + randY1Z1] & 15], x - 1, y - 1, z - 1);

        return FishyNoise.lerp(FishyNoise.lerp(grad000 + u * (grad100 - grad000), grad010 + u * (grad110 - grad010), v), FishyNoise.lerp(grad001 + u * (grad101 - grad001), grad011 + u * (grad111 - grad011), v), w);
    }

}
