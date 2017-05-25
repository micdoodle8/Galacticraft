package micdoodle8.mods.galacticraft.api.entity;

public interface ICameraZoomEntity
{
    float getCameraZoom();

    boolean defaultThirdPerson();
    
    /**Used for the ICameraZoomEntities which can rotate pitch and yaw (e.g. TieredRocket, Moon Lander)
    *  Return a value less than -10F to signal a non-rotatable entity (e.g. Landing Balloons, Entry Pod)
    */
    float getRotateOffset();
}
