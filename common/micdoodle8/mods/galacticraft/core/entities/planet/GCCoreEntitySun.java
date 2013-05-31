//package micdoodle8.mods.galacticraft.core.entities.planet;
//
//import micdoodle8.mods.galacticraft.API.IGalaxy;
//import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.GCCoreSlotRendererSun;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.world.World;
//
//public class GCCoreEntitySun extends GCCoreEntityCelestialObject
//{
//	public GCCoreEntitySun(World par1World)
//	{
//		super(par1World);
//	}
//
//	@Override
//	public void onPlayerCollide(EntityPlayer player)
//	{
//		player.setFire(10000);
//	}
//
//	@Override
//	public float getPlanetSize()
//	{
//		return 15F * 108F;
//	}
//
//	@Override
//	public float getDistanceFromCenter()
//	{
//		return 0F;
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
//		return 0F;
//	}
//
//	@Override
//	public IPlanetSlotRenderer getSlotRenderer()
//	{
//		return new GCCoreSlotRendererSun();
//	}
//
//	@Override
//	public IGalaxy getParentGalaxy()
//	{
//		return GalacticraftCore.galaxyMilkyWay;
//	}
//}
