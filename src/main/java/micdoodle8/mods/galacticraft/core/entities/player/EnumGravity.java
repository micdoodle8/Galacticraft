package micdoodle8.mods.galacticraft.core.entities.player;

public enum EnumGravity
{
    down(0, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F),
    up(1, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, -1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 0.0F),
    west(2, 0.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.0F, 0.0F, -1.0F, 1.0F, 0.0F),
    east(3, 0.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.5F, -1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F),
    south(4, 1.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.5F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 1.0F),
    north(5, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, -0.5F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, -1.0F);

    public int intValue;
    public float pitchGravityX;
    public float pitchGravityY;
    public float yawGravityX;
    public float yawGravityY;
    public float yawGravityZ;
    public float thetaX;
    public float thetaZ;
    public float sneakVecX;
    public float sneakVecY;
    public float sneakVecZ;
    public float eyeVecX;
    public float eyeVecY;
    public float eyeVecZ;
    public static EnumGravity[] GDirections = { down, up, west, east, south, north };

    private EnumGravity(int value, float pitchX, float pitchY, float yawX, float yawY, float yawZ, float thetaX, float thetaZ, float sneakX, float sneakY, float sneakZ, float eyeX, float eyeY, float eyeZ)
    {
        this.intValue = value;
        this.pitchGravityX = pitchX;
        this.pitchGravityY = pitchY;
        this.yawGravityX = yawX;
        this.yawGravityY = yawY;
        this.yawGravityZ = yawZ;
        this.thetaX = thetaX;
        this.thetaZ = thetaZ;
        this.sneakVecX = sneakX;
        this.sneakVecY = sneakY;
        this.sneakVecZ = sneakZ;
        this.eyeVecX = eyeX;
        this.eyeVecY = eyeY;
        this.eyeVecZ = eyeZ;
    }
}