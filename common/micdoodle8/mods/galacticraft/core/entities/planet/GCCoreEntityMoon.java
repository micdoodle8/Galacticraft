//package micdoodle8.mods.galacticraft.core.entities.planet;
//
//import micdoodle8.mods.galacticraft.API.IGalaxy;
//import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.GCCoreSlotRendererSun;
//import micdoodle8.mods.galacticraft.moon.client.GCMoonSlotRenderer;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.world.World;
//
//public class GCCoreEntityMoon extends GCCoreEntitySubOrbitingObject
//{
//	public GCCoreEntityMoon(World par1World)
//	{
//		super(par1World);
//	}
//
//	@Override
//	public void onPlayerCollide(EntityPlayer player)
//	{
//	}
//
//	@Override
//	public float getPlanetSize()
//	{
//		return 4;
//	}
//
//	@Override
//	public float getDistanceFromCenter()
//	{
//		return 40F;
//	}
//
//	@Override
//	public float getPhaseShift()
//	{
//		return 0;
//	}
//
//	@Override
//	public float getStretchValue()
//	{
//		return 0.01F;
//	}
//
//	@Override
//	public IPlanetSlotRenderer getSlotRenderer()
//	{
//		return new GCMoonSlotRenderer();
//	}
//
//	@Override
//	public IGalaxy getParentGalaxy()
//	{
//		return GalacticraftCore.galaxyMilkyWay;
//	}
// }
