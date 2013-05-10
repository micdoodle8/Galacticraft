//package micdoodle8.mods.galacticraft.core.entities.planet;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import micdoodle8.mods.galacticraft.API.IGalaxy;
//import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.WorldUtil;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.MathHelper;
//import net.minecraft.util.Vec3;
//import net.minecraft.world.World;
//
//import org.lwjgl.Sys;
//
//import cpw.mods.fml.common.FMLLog;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//public abstract class GCCoreEntitySubOrbitingObject extends Entity implements IUpdateable
//{
//	public GCCoreEntityCelestialObject orbitingObject = null;
//    private final Map[] posMaps = this.computePlanetPos(0, 0, (float) (GalacticraftCore.spaceScale * this.getDistanceFromCenter() / 20), 2880);
//	
//	public GCCoreEntitySubOrbitingObject(World par1World) 
//	{
//		super(par1World);
//		this.setPosition(0, 200, 0);
//		this.ignoreFrustumCheck = true;
//	}
//	
//	public abstract void onPlayerCollide(EntityPlayer player);
//	
//	public abstract float getPlanetSize();
//
//	public abstract float getDistanceFromCenter();
//
//	public abstract float getPhaseShift();
//
//	public abstract float getStretchValue();
//
//	public abstract IPlanetSlotRenderer getSlotRenderer();
//
//	public abstract IGalaxy getParentGalaxy();
//
//	@Override
//	protected void entityInit()
//	{
//		if (!WorldUtil.updateableObjects.contains(this))
//		{
//			WorldUtil.updateableObjects.add(this);
//		}
//	}
//
//	@Override
//	protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
//	{
//		if (!WorldUtil.updateableObjects.contains(this))
//		{
//			WorldUtil.updateableObjects.add(this);
//		}
//	}
//
//	@Override
//	protected void writeEntityToNBT(NBTTagCompound nbttagcompound)
//	{
//		
//	}
//
//	@Override
//	public void update()
//	{
//    	this.updatePlanetPos();
//	}
//
//    @Override
//    public void onUpdate()
//    {
//    	this.updatePlanetPos();
//    }
//    
//    private void updatePlanetPos()
//    {
//    	if (orbitingObject == null || this.posMaps == null)
//    	{
//    		return;
//    	}
//    	
//        if (posMaps[0] != null && posMaps[1] != null)
//        {
//        	float x = 0;
//        	float z = 0; 
//        	
//        	Object o1 = posMaps[0].get(MathHelper.floor_float((this.getPhaseShift() + Sys.getTime() / (720F * GalacticraftCore.spaceSpeedScale * this.getStretchValue())) % 2880));
//        	Object o2 = posMaps[1].get(MathHelper.floor_float((this.getPhaseShift() + Sys.getTime() / (720F * GalacticraftCore.spaceSpeedScale * this.getStretchValue())) % 2880));
//        	
//        	if (o1 != null && o2 != null)
//        	{
//            	x = (Float) o1;
//            	z = (Float) o2;
//        	}
//        	
//        	this.rotationYaw -= 0.05F;
//        	
//        	this.posX = x + this.orbitingObject.posX;
//        	this.posZ = z + this.orbitingObject.posZ;
//        	this.posY = 200;
//        }
//        
//        for (Object o : this.worldObj.loadedEntityList)
//        {
//        	Entity e = (Entity)o;
//        	
//        	if (e.getDistanceToEntity(this) == 0.0 && this.entityId < e.entityId && this.getClass().isAssignableFrom(e.getClass()))
//        	{
//        		super.setDead();
//        	}
//        }
//        
//        this.prevPosX = this.posX;
//        this.prevPosY = this.posY;
//        this.prevPosZ = this.posZ;
//    }
//
//    public Map[] computePlanetPos(float cx, float cy, float r, float stretch)
//    {
//    	final Map mapX = new HashMap();
//    	final Map mapY = new HashMap();
//
//    	final float theta = (float) (2 * Math.PI / stretch);
//    	final float c = (float) Math.cos(theta);
//    	final float s = (float) Math.sin(theta);
//    	float t;
//
//    	float x = r;
//    	float y = 0;
//
//    	for(int ii = 0; ii < stretch; ii++)
//    	{
//    		mapX.put(ii, x + cx);
//    		mapY.put(ii, y + cy);
//
//    		t = x;
//    		x = c * x - s * y;
//    		y = s * t + c * y;
//    	}
//
//    	return new Map[] {mapX, mapY};
//    }
//
//    @Override
//    public void applyEntityCollision(Entity par1Entity)
//    {
//    	;
//    }
//
//    @Override
//    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) 
//    {
//    	this.onPlayerCollide(par1EntityPlayer);
//    }
//
//    @Override
//    public boolean canBeCollidedWith()
//    {
//        return true;
//    }
//
//    @Override
//    public boolean canBePushed()
//    {
//        return false;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public boolean isInRangeToRenderVec3D(Vec3 par1Vec3)
//    {
//    	return true;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public boolean isInRangeToRenderDist(double par1)
//    {
//    	return true;
//    }
//
//    @Override
//    protected void kill()
//    {
//    	;
//    }
//
//    @Override
//    public void setDead()
//    {
//    	;
//    }
//
//    @Override
//    public float getCollisionBorderSize()
//    {
//        return 0.5F;
//    }
//
//    @Override
//    protected boolean pushOutOfBlocks(double par1, double par3, double par5)
//    {
//    	return false;
//    }
//
//    @Override
//    public void moveEntity(double par1, double par3, double par5)
//    {
//    	;
//    }
//}
