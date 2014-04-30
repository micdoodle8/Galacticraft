package codechicken.lib.vec;

public class AxisCycle
{
    public static Transformation[] cycles = new Transformation[]{
        new RedundantTransformation(),
        new VariableTransformation(new Matrix4(0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y; double d2 = vec.z;
                vec.x = d2; vec.y = d0; vec.z = d1;
            } @Override public Transformation inverse(){
                return cycles[2];
            }},
        new VariableTransformation(new Matrix4(0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1)){
            @Override public void apply(Vector3 vec){
                double d0 = vec.x; double d1 = vec.y; double d2 = vec.z;
                vec.x = d1; vec.y = d2; vec.z = d0;
            } @Override public Transformation inverse(){
                return cycles[1];
            }}};
}
