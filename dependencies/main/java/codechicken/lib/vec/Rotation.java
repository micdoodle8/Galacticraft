package codechicken.lib.vec;

import codechicken.lib.math.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Rotation extends Transformation
{
    /**
     * Clockwise pi/2 about y looking down
     */
    public static Transformation[] quarterRotations = new Transformation[]{
        new RedundantTransformation(),
        new VariableTransformation(new Matrix4(0, 0,-1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d1 = vec.x; double d2 = vec.z;
                vec.x = -d2; vec.z = d1;
            } @Override public Transformation inverse(){
                return quarterRotations[3];
            }},
        new VariableTransformation(new Matrix4(-1, 0, 0, 0, 0, 1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                vec.x = -vec.x; vec.z = -vec.z;
            } @Override public Transformation inverse(){
                return this;
            }},
        new VariableTransformation(new Matrix4(0, 0, 1, 0, 0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d1 = vec.x; double d2 = vec.z;
                vec.x = d2; vec.z = -d1;
            } @Override public Transformation inverse(){
                return quarterRotations[1];
            }}
        };
    
    public static Transformation[] sideRotations = new Transformation[]{
        new RedundantTransformation(),
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0,-1, 0, 0, 0, 0,-1, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                vec.y = -vec.y; vec.z = -vec.z;
            } @Override public Transformation inverse(){
                return this;
            }},
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d1 = vec.y; double d2 = vec.z;
                vec.y = -d2; vec.z = d1;
            } @Override public Transformation inverse(){
                return sideRotations[3];
            }},
        new VariableTransformation(new Matrix4(1, 0, 0, 0, 0, 0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d1 = vec.y; double d2 = vec.z;
                vec.y = d2; vec.z = -d1;
            } @Override public Transformation inverse(){
                return sideRotations[2];
            }},
        new VariableTransformation(new Matrix4(0, 1, 0, 0,-1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y;
                vec.x = d1; vec.y = -d0;
            } @Override public Transformation inverse(){
                return sideRotations[5];
            }},
        new VariableTransformation(new Matrix4(0,-1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y;
                vec.x = -d1; vec.y = d0;
            } @Override public Transformation inverse(){
                return sideRotations[4];
            }}
        };
    
    public static Vector3[] axes = new Vector3[]{
        new Vector3( 0,-1, 0),
        new Vector3( 0, 1, 0),
        new Vector3( 0, 0,-1),
        new Vector3( 0, 0, 1),
        new Vector3(-1, 0, 0),
        new Vector3( 1, 0, 0)};
    
    public static int[] sideRotMap = new int[]{
        3,4,2,5,
        3,5,2,4,
        1,5,0,4,
        1,4,0,5,
        1,2,0,3,
        1,3,0,2};
    
    public static int[] rotSideMap = new int[]{
        -1,-1, 2, 0, 1, 3,
        -1,-1, 2, 0, 3, 1,
         2, 0,-1,-1, 3, 1,
         2, 0,-1,-1, 1, 3,
         2, 0, 1, 3,-1,-1,
         2, 0, 3, 1,-1,-1};
    
    /**
     * Rotate pi/2 * this offset for [side] about y axis before rotating to the side for the rotation indicies to line up
     */
    public static int[] sideRotOffsets = new int[]{0, 2, 2, 0, 1, 3};

    public static int rotateSide(int s, int r)
    {
        return sideRotMap[s<<2|r];
    }
    
    /**
     * Reverse of rotateSide
     */
    public static int rotationTo(int s1, int s2)
    {
        if((s1&6) == (s2&6))
            throw new IllegalArgumentException("Faces "+s1+" and "+s2+" are opposites");
        return rotSideMap[s1*6+s2];
    }

    /**
     * @param player The placing player, used for obtaining the look vector
     * @param side The side of the block being placed on
     * @return The rotation for the face == side^1
     */
    public static int getSidedRotation(EntityPlayer player, int side)
    {
        Vector3 look = new Vector3(player.getLook(1));
        double max = 0;
        int maxr = 0;
        for(int r = 0; r < 4; r++)
        {
            Vector3 axis = Rotation.axes[rotateSide(side^1, r)];
            double d = look.scalarProject(axis);
            if(d > max)
            {
                max = d;
                maxr = r;
            }
        }
        return maxr;
    }
    
    /**
     * @return The rotation quat for side 0 and rotation 0 to side s with rotation r
     */
    public static Transformation sideOrientation(int s, int r)
    {
        return quarterRotations[(r+sideRotOffsets[s])%4].with(sideRotations[s]);
    }

    /**
     * @param entity The placing entity, used for obtaining the look vector
     * @return The side towards which the entity is most directly looking.
     */
    public static int getSideFromLookAngle(EntityLivingBase entity)
    {
        Vector3 look = new Vector3(entity.getLook(1));
        double max = 0;
        int maxs = 0;
        for(int s = 0; s < 6; s++) {
            double d = look.scalarProject(axes[s]);
            if(d > max) {
                max = d;
                maxs = s;
            }
        }
        return maxs;
    }
    
    public double angle;
    public Vector3 axis;
    
    private Quat quat;
    
    public Rotation(double angle, Vector3 axis)
    {
        this.angle = angle;
        this.axis = axis;
    }
    
    public Rotation(double angle, double x, double y, double z)
    {
        this(angle, new Vector3(x, y, z));
    }
    
    public Rotation(Quat quat)
    {
        this.quat = quat;
        
        angle = Math.acos(quat.s)*2;
        if(angle == 0)
        {
            axis = new Vector3(0, 1, 0);
        }
        else
        {
            double sa = Math.sin(angle*0.5);
            axis = new Vector3(quat.x/sa, quat.y/sa, quat.z/sa);
        }
    }

    @Override
    public void apply(Vector3 vec)
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        
        vec.rotate(quat);
    }
    
    @Override
    public void applyN(Vector3 normal)
    {
        apply(normal);
    }

    @Override
    public void apply(Matrix4 mat)
    {
        mat.rotate(angle, axis);
    }
    
    public Quat toQuat()
    {
        if(quat == null)
            quat = Quat.aroundAxis(axis, angle);
        return quat;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void glApply()
    {
        GL11.glRotatef((float)(angle*MathHelper.todeg), (float)axis.x, (float)axis.y, (float)axis.z);
    }
    
    @Override
    public Transformation inverse()
    {
        return new Rotation(-angle, axis);
    }

    @Override
    public Transformation merge(Transformation next) {
        if(next instanceof Rotation) {
            Rotation r = (Rotation)next;
            if(r.axis.equalsT(axis))
                return new Rotation(angle+r.angle, axis);
            
            return new Rotation(toQuat().copy().multiply(r.toQuat()));
        }
        
        return null;
    }
    
    @Override
    public boolean isRedundant() {
        return MathHelper.between(-1E-5, angle, 1E-5);
    }
    
    @Override
    public String toString()
    {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Rotation("+new BigDecimal(angle, cont)+", "+new BigDecimal(axis.x, cont)+", "+
                new BigDecimal(axis.y, cont)+", "+new BigDecimal(axis.z, cont)+")";
    }
}
