//package micdoodle8.mods.galacticraft.core.entities.planet;
//
//import micdoodle8.mods.galacticraft.api.IGalaxy;
//import micdoodle8.mods.galacticraft.api.IPlanetSlotRenderer;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.GCCoreSlotRendererOverworld;
//import micdoodle8.mods.galacticraft.core.client.GCCoreSlotRendererSun;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.world.World;
//
//public class GCCoreEntityOverworld extends GCCoreEntityCelestialObject
//{
//	public GCCoreEntityOverworld(World par1World)
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
//		return 15;
//	}
//
//	@Override
//	public float getDistanceFromCenter()
//	{
//		return 1500F;
//	}
//
//	@Override
//	public float getPhaseShift()
//	{
//		return 2160F;
//	}
//
//	@Override
//	public float getStretchValue()
//	{
//		return 1F;
//	}
//
//	@Override
//	public IPlanetSlotRenderer getSlotRenderer()
//	{
//		return new GCCoreSlotRendererOverworld();
//	}
//
//	@Override
//	public IGalaxy getParentGalaxy()
//	{
//		return GalacticraftCore.galaxyMilkyWay;
//	}
// }
