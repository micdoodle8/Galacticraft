package micdoodle8.mods.galacticraft.core.dimension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.command.CommandGCInv;
import micdoodle8.mods.galacticraft.core.command.GCInvSaveData;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.oxygen.ThreadFindSeal;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreWorldProviderSpaceStation.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8, radfast
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class WorldProviderOrbit extends WorldProvider implements IOrbitDimension, ISolarLevel, IExitHeight
{
	public int spaceStationDimensionID;
	
	private static final float GFORCE = 9.81F / 400F;   //gravity in metres per tick squared

	private OrbitSpinSaveData savefile;
	public boolean doSpinning = true;
	private float angularVelocityRadians = 0F;
	private float skyAngularVelocity = (float) (this.angularVelocityRadians * 180 / Math.PI);
	private float angularVelocityTarget = 0F;
	private float angularVelocityAccel = 0F;
	private double spinCentreX;
	private double spinCentreZ;
	private float momentOfInertia;
	private float massCentreX;
	private float massCentreY;
	private float massCentreZ;
	private float mass;
	private int ssBoundsMaxX;
	private int ssBoundsMinX;
	private int ssBoundsMaxY;
	private int ssBoundsMinY;
	private int ssBoundsMaxZ;
	private int ssBoundsMinZ;
	
	private HashSet<BlockVec3> thrustersPlus = new HashSet();
	private HashSet<BlockVec3> thrustersMinus = new HashSet();
	private BlockVec3 oneSSBlock;
	//private HashSet<BlockVec3> stationBlocks = new HashSet();

	private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();
	private List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
	private List<BlockVec3> nextLayer;
	
	private float artificialG;		
	//Used to make continuous particles + thrust sounds at the spin thrusters in this dimension
	//If false, make particles + sounds occasionally in small bursts, just for fun (micro attitude changes)
	//see: BlockSpinThruster.randomDisplayTick()
	public boolean thrustersFiring = false;
	private boolean dataNotLoaded = true;
	
	@Override
	public void setDimension(int var1)
	{
		this.spaceStationDimensionID = var1;
		this.dimensionId = var1;
		super.setDimension(var1);
	}

	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new ChunkProviderOrbit(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
	}

	@Override
	protected void generateLightBrightnessTable()
	{
		final float var1 = 0.0F;

		for (int var2 = 0; var2 <= 15; ++var2)
		{
			final float var3 = 1.0F - var2 / 15.0F;
			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
		}
	}

	@Override
	public float[] calcSunriseSunsetColors(float var1, float var2)
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getFogColor(float var1, float var2)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool((double) 0F / 255F, (double) 0F / 255F, (double) 0F / 255F);
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
	{
		return this.worldObj.getWorldVec3Pool().getVecFromPool(0, 0, 0);
	}

	@Override
	public float calculateCelestialAngle(long par1, float par3)
	{
		final int var4 = (int) (par1 % 24000L);
		float var5 = (var4 + par3) / 24000.0F - 0.25F;

		if (var5 < 0.0F)
		{
			++var5;
		}

		if (var5 > 1.0F)
		{
			--var5;
		}

		final float var6 = var5;
		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
		var5 = var6 + (var5 - var6) / 3.0F;
		return var5;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1)
	{
		final float var2 = this.worldObj.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

		if (var3 < 0.0F)
		{
			var3 = 0.0F;
		}

		if (var3 > 1.0F)
		{
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F + 0.3F;
	}

	public float calculatePhobosAngle(long par1, float par3)
	{
		return this.calculateCelestialAngle(par1, par3) * 3000;
	}

	public float calculateDeimosAngle(long par1, float par3)
	{
		return this.calculatePhobosAngle(par1, par3) * 0.0000000001F;
	}

	@Override
	public void updateWeather()
	{
		this.worldObj.getWorldInfo().setRainTime(0);
		this.worldObj.getWorldInfo().setRaining(false);
		this.worldObj.getWorldInfo().setThunderTime(0);
		this.worldObj.getWorldInfo().setThundering(false);
		
		if (!this.worldObj.isRemote)
		{
			if (this.dataNotLoaded)
			{
				this.savefile = OrbitSpinSaveData.initWorldData(this.worldObj);
				this.readFromNBT(this.savefile.datacompound);
				System.out.println("Loading data from save: "+this.savefile.datacompound.getFloat("omegaSky"));
				this.dataNotLoaded = false;
				
				//TODO: send packet to clients in this dimension
			}
			
			boolean updateNeeded = true;
			if (this.angularVelocityTarget < this.angularVelocityRadians)
			{
				float newAngle = this.angularVelocityRadians - this.angularVelocityAccel;
				if (newAngle < this.angularVelocityTarget) newAngle = this.angularVelocityTarget;
				setSpinRate(newAngle);
				this.thrustersFiring = true;
			} else if (this.angularVelocityTarget > this.angularVelocityRadians)
			{
				float newAngle = this.angularVelocityRadians + this.angularVelocityAccel;
				if (newAngle > this.angularVelocityTarget) newAngle = this.angularVelocityTarget;
				setSpinRate(newAngle);
				this.thrustersFiring = true;
			} else
			if (this.thrustersFiring)
			{
				this.thrustersFiring = false;
			} else
				updateNeeded = false;
	
			if (updateNeeded)
			{
				this.writeToNBT(this.savefile.datacompound);
				this.savefile.markDirty();
				
				List<Object> objList = new ArrayList<Object>();
				objList.add(Float.valueOf(this.angularVelocityRadians));
				objList.add(Boolean.valueOf(this.thrustersFiring));
				GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), this.spaceStationDimensionID);
			}
		}
	}

	@Override
	public boolean isSkyColored()
	{
		return false;
	}

	@Override
	public double getHorizon()
	{
		return 44.0D;
	}

	@Override
	public int getAverageGroundLevel()
	{
		return 44;
	}

	@Override
	public boolean isSurfaceWorld()
	{
		return true;
	}

	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2)
	{
		return true;
	}

	@Override
	public boolean canRespawnHere()
	{
		return !ConfigManagerCore.forceOverworldRespawn;
	}

	@Override
	public String getWelcomeMessage()
	{
		return "Entering Earth Orbit";
	}

	@Override
	public String getDepartMessage()
	{
		return "Leaving Earth Orbit";
	}

	@Override
	public String getDimensionName()
	{
		return "Space Station " + this.spaceStationDimensionID;
	}

//	@Override
//	public boolean canSnowAt(int x, int y, int z)
//	{
//		return false;
//	} TODO Fix no snow

	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
	{
		return false;
	}

	@Override
	public boolean canDoLightning(Chunk chunk)
	{
		return false;
	}

	@Override
	public boolean canDoRainSnowIce(Chunk chunk)
	{
		return false;
	}

	@Override
	public float getGravity()
	{
		return 0.078F;//0.073F;
	}

	@Override
	public double getMeteorFrequency()
	{
		return 0;
	}

	@Override
	public double getFuelUsageMultiplier()
	{
		return 0.5D;
	}

	@Override
	public String getPlanetToOrbit()
	{
		return "Overworld";
	}

	@Override
	public int getYCoordToTeleportToPlanet()
	{
		return 30;
	}

	@Override
	public String getSaveFolder()
	{
		return "DIM_SPACESTATION" + this.spaceStationDimensionID;
	}

	@Override
	public double getSolarEnergyMultiplier()
	{
		return ConfigManagerCore.spaceStationEnergyScalar;
	}

	@Override
	public double getYCoordinateToTeleport()
	{
		return 1200;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier)
	{
		return tier > 0;
	}

	@Override
	public float getFallDamageModifier()
	{
		return 0.4F;
	}

	@Override
	public float getSoundVolReductionAmount()
	{
		return 50.0F;
	}
	
	public void spinUpdate(GCEntityClientPlayerMP p)
	{
		boolean freefall = true;
		if (p.boundingBox.maxX >= this.ssBoundsMinX && p.boundingBox.minX <= this.ssBoundsMaxX && p.boundingBox.maxY >= this.ssBoundsMinY && p.boundingBox.minY <= this.ssBoundsMaxY && p.boundingBox.maxZ >= this.ssBoundsMinZ && p.boundingBox.minZ <= this.ssBoundsMaxZ)
		{
			//Player is somewhere within the space station boundaries
			//TODO is there a better check for feet are touching ground?  this doesn't seem to work
			if (p.onGround)
				freefall = false;
			else
			{
				//First check if the players bounding box is in any non-vacuum block (including torches etc)
				//If so, it's assumed the player has something close enough to grab onto, so is not in freefall
				//Note: breatheable air here means the player is definitely not in freefall
				BLOCKCHECK:
				for (int x = MathHelper.floor_double(p.boundingBox.minX); x<=MathHelper.floor_double(p.boundingBox.maxX); x++)
					for (int y = MathHelper.floor_double(p.boundingBox.minY); y<=MathHelper.floor_double(p.boundingBox.maxY); y++)
						for (int z = MathHelper.floor_double(p.boundingBox.minZ); z<=MathHelper.floor_double(p.boundingBox.maxZ); z++)
						{
							if (this.worldObj.getBlock(x, y, z) != Blocks.air)
							{
								freefall = false;
								break BLOCKCHECK;
							}
						}
			}

			if (freefall)
			{
				//If that check didn't produce a result, see if the player is inside the walls
				//TODO: could apply special weightless movement here - the player is inside the walls,  not touching them, and in a vacuum
				int quadrant = 0;
				double xd = p.posX - this.massCentreX;
				double zd = p.posZ - this.massCentreZ;
				if (xd<0)
				{
					if (xd<-Math.abs(zd))
					{
						quadrant = 2;
					} else
						quadrant = (zd<0) ? 3 : 1;
				} else
					if (xd>Math.abs(zd))
					{
						quadrant = 0;
					} else
						quadrant = (zd<0) ? 3 : 1;
				
				int ymin = MathHelper.floor_double(p.boundingBox.minY)-1;
				int ymax = MathHelper.floor_double(p.boundingBox.maxY);
				int xmin, xmax, zmin, zmax;

				switch (quadrant)
				{
				case 0:
					xmin = MathHelper.floor_double(p.boundingBox.maxX);
					xmax = this.ssBoundsMaxX - 1;
					zmin = MathHelper.floor_double(p.boundingBox.minZ)-1;
					zmax = MathHelper.floor_double(p.boundingBox.maxZ)+1;
					break;
				case 1:
					xmin = MathHelper.floor_double(p.boundingBox.minX)-1;
					xmax = MathHelper.floor_double(p.boundingBox.maxX)+1;
					zmin = MathHelper.floor_double(p.boundingBox.maxZ);
					zmax = this.ssBoundsMaxZ - 1;
					break;
				case 2:
					zmin = MathHelper.floor_double(p.boundingBox.minZ)-1;
					zmax = MathHelper.floor_double(p.boundingBox.maxZ)+1;
					xmin = this.ssBoundsMinX;
					xmax = MathHelper.floor_double(p.boundingBox.minX);
					break;
				case 3:
				default:
					xmin = MathHelper.floor_double(p.boundingBox.minX)-1;
					xmax = MathHelper.floor_double(p.boundingBox.maxX)+1;
					zmin = this.ssBoundsMinZ;
					zmax = MathHelper.floor_double(p.boundingBox.minZ);
					break;
				}
				
				//This block search could cost a lot of CPU (but client side) - maybe optimise later
				BLOCKCHECK0:
				for(int x = xmin; x <= xmax; x++)
					for (int z = zmin; z <= zmax; z++)
						for (int y = ymin; y <= ymax; y++)
							if (this.worldObj.getBlock(x, y, z) != Blocks.air)
							{
								freefall = false;
								break BLOCKCHECK0;
							}
			}
		}
		if (freefall)
		{
			//TODO because player is in free-fall here maybe disable some movement or special flight mode?
			//Could start tumbling the player
			//Player may run out of oxygen - that will kill the player eventually if can't get back to SS
			//Maybe player needs a 'suicide' button if floating helplessly in space and with no tether
			//Could auto-kill + respawn the player if floats too far away (config option whether to lose items or not)
			//But we want players to be able to enjoy the view of the spinning space station from the outside
			
			//TODO maybe need to test to make sure xx and zz are not too large (outside sight range of SS)
			//TODO think about server + network load (loading/unloading chunks) when movement is rapid
			//Maybe reduce chunkloading radius?
			float angle;
			double xx = p.posX-this.spinCentreX;
			double zz = p.posZ-this.spinCentreZ;
			double arc = Math.sqrt(xx*xx + zz*zz);
			if (xx == 0D) angle = (zz > 0) ? 3.141592536F/2 : -3.141592536F/2;
			else angle = (float)Math.atan(zz/xx);
			if (xx < 0D) angle += 3.141592536F;
			angle += this.angularVelocityRadians/3F;
			arc = arc*this.angularVelocityRadians;
			double offsetX = - arc * MathHelper.sin(angle);
			double offsetZ = arc * MathHelper.cos(angle);
			p.posX += offsetX;
			p.posZ += offsetZ;
			p.boundingBox.offset(offsetX, 0.0D, offsetZ);
			//TODO check for block collisions here - if so move the player appropriately and apply fall damage
			//Moving the player = slide along / down
			
			p.rotationYaw += this.skyAngularVelocity;
			while (p.rotationYaw > 360F) p.rotationYaw -= 360F;	
		}
	}
	
	public float getSpinRate()
	{
		return this.skyAngularVelocity;
	}
	
	/**
	 * Sets the spin rate for the dimension in radians per tick 
	 * For example, 0.031415 would be 1/200 revolution per tick
	 * So that would be 1 revolution every 10 seconds 
	 */
	public void setSpinRate(float angle)
	{
		this.angularVelocityRadians = angle;
		this.skyAngularVelocity = angle * 180F / 3.1415927F;
		IRenderHandler sky = this.getSkyRenderer();
		if (sky instanceof SkyProviderOrbit) ((SkyProviderOrbit)sky).spinDeltaPerTick = this.skyAngularVelocity;
	}
	
	public void setSpinRate(float angle, boolean firing)
	{
		this.angularVelocityRadians = angle;
		this.skyAngularVelocity = angle * 180F / 3.1415927F;
		IRenderHandler sky = this.getSkyRenderer();
		if (sky instanceof SkyProviderOrbit) ((SkyProviderOrbit)sky).spinDeltaPerTick = this.skyAngularVelocity;
		this.thrustersFiring = firing;
	}
	
	public void setSpinCentre(double x, double z)
	{
		this.spinCentreX = x;
		this.spinCentreZ = z;
	}
	
	public void setSpinBox(int mx, int xx, int my, int yy, int mz, int zz)
	{
		this.ssBoundsMinX = mx;
		this.ssBoundsMaxX = xx;
		this.ssBoundsMinY = my;
		this.ssBoundsMaxY = yy;
		this.ssBoundsMinZ = mz;
		this.ssBoundsMaxZ = zz;
	}

	public void addThruster(BlockVec3 thruster, boolean positive)
	{
		if (positive)
		{
			thrustersPlus.add(thruster);
			thrustersMinus.remove(thruster);
		}
		else
		{
			thrustersPlus.remove(thruster);
			thrustersMinus.add(thruster);
		}
	}

	public void removeThruster(BlockVec3 thruster, boolean positive)
	{
		if (positive)
		{
			thrustersPlus.remove(thruster);
		}
		else
		{
			thrustersMinus.remove(thruster);
		}
	}

	public boolean checkSS(BlockVec3 baseBlock)
	{
		// Find contiguous blocks using an algorithm like the oxygen sealer one
		this.currentLayer.clear();
		this.checked.clear();
		this.currentLayer.add(baseBlock.clone());
		this.checked.add(baseBlock.clone());
		this.nextLayer = new LinkedList<BlockVec3>();

		float thismass = 0.1F;  //Mass of a thruster
		float thismassCentreX = 0.1F * baseBlock.x;
		float thismassCentreY = 0.1F * baseBlock.y;
		float thismassCentreZ = 0.1F * baseBlock.z;
		float thismoment = 0F;
		int thisssBoundsMaxX = baseBlock.x;
		int thisssBoundsMinX = baseBlock.x;
		int thisssBoundsMaxY = baseBlock.y;
		int thisssBoundsMinY = baseBlock.y;
		int thisssBoundsMaxZ = baseBlock.z;
		int thisssBoundsMinZ = baseBlock.z;
		
		while (this.currentLayer.size() > 0)
		{
			for (BlockVec3 vec : this.currentLayer)
			{
				if (vec.x < thisssBoundsMinX) thisssBoundsMinX = vec.x;
				if (vec.y < thisssBoundsMinY) thisssBoundsMinY = vec.y;
				if (vec.z < thisssBoundsMinZ) thisssBoundsMinZ = vec.z;
				if (vec.x > thisssBoundsMaxX) thisssBoundsMaxX = vec.x;
				if (vec.y > thisssBoundsMaxY) thisssBoundsMaxY = vec.y;
				if (vec.z > thisssBoundsMaxZ) thisssBoundsMaxZ = vec.z;
				
				for (int side = 0; side < 6; side++)
				{
					if (vec.sideDone[side]) continue;
					BlockVec3 sideVec = vec.newVecSide(side);

					Block b = sideVec.getBlockID(this.worldObj);
					if (b != Blocks.air && b != GCBlocks.breatheableAir && b != null)
					{
						if (!this.checked.contains(sideVec))
						{
							this.checked.add(sideVec);
							this.nextLayer.add(sideVec);
							float m = 1.0F;
							//Liquids have a mass of 1, stone, metal blocks etc will be heavier 
							if (!(b instanceof BlockLiquid))
							{
								//For most blocks, hardness gives a good idea of mass
								m = b.getBlockHardness(this.worldObj, sideVec.x, sideVec.y, sideVec.z);
								if (m < 0.1F) m = 0.1F;
								else if (m > 30F) m = 30F;
								//Wood items have a high hardness compared with their presumed mass 
								if (b.getMaterial() == Material.wood) m /= 4;
								
								//TODO: higher mass for future Galacticraft hi-density item like neutronium
								//Maybe also check for things in other mods by name: lead, uranium blocks? 
							}
							thismassCentreX += m * sideVec.x;
							thismassCentreY += m * sideVec.y;
							thismassCentreZ += m * sideVec.z;
							thismass += m;
							thismoment += m * (sideVec.x * sideVec.x + sideVec.z * sideVec.z); 
						}
					}
				}
			}
			
			this.currentLayer = this.nextLayer;
			this.nextLayer = new LinkedList<BlockVec3>();
		}

		if (this.oneSSBlock == null)
			this.oneSSBlock = baseBlock.clone();
		
		if (!this.checked.contains(oneSSBlock))
		{
			//The thruster was not placed on the existing contiguous spacestation: it must be.
			this.thrustersPlus.remove(baseBlock);
			this.thrustersMinus.remove(baseBlock);
			System.out.println("Returning false: oneSSBlock was "+oneSSBlock.x+","+oneSSBlock.y+","+oneSSBlock.z);
			return false;
		}

		//TODO: Offer players a station reset option so they can move it way out?  Auto-reset when there are no thrusters?

		// Calculate centre of mass
		this.mass = thismass;
		
		this.massCentreX = thismassCentreX / thismass + 0.5F;
		this.massCentreY = thismassCentreY / thismass + 0.5F;
		this.massCentreZ = thismassCentreZ / thismass + 0.5F;
		System.out.println("(X,Z) = "+this.massCentreX+","+this.massCentreZ);

		setSpinCentre(this.massCentreX, this.massCentreZ);
		
		//The boundary is at the outer edges of the blocks
		this.ssBoundsMaxX = thisssBoundsMaxX+1;
		this.ssBoundsMinX = thisssBoundsMinX;
		this.ssBoundsMaxY = thisssBoundsMaxY+1;
		this.ssBoundsMinY = thisssBoundsMinY;
		this.ssBoundsMaxZ = thisssBoundsMaxZ+1;
		this.ssBoundsMinZ = thisssBoundsMinZ;
		
		// Calculate momentOfInertia
		thismoment -= this.massCentreX * this.massCentreX * this.mass;
		thismoment -= this.massCentreZ * this.massCentreZ * this.mass;
		this.momentOfInertia = thismoment;
				
		//TODO
		// TODO defy gravity
		// TODO break blocks which are outside SS (not in checked)
		// TODO prevent spin if there is a huge number of blocks outside SS
		
		System.out.println("MoI = "+this.momentOfInertia+" CoMx = "+this.massCentreX+" CoMz = "+this.massCentreZ);

		//Send packets to clients in this dimension			
		List<Object> objList = new ArrayList<Object>();
		objList.add(Double.valueOf(this.spinCentreX));
		objList.add(Double.valueOf(this.spinCentreZ));
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), this.spaceStationDimensionID);

		objList = new ArrayList<Object>();
		objList.add(Integer.valueOf(this.ssBoundsMinX));
		objList.add(Integer.valueOf(this.ssBoundsMaxX));
		objList.add(Integer.valueOf(this.ssBoundsMinY));
		objList.add(Integer.valueOf(this.ssBoundsMaxY));
		objList.add(Integer.valueOf(this.ssBoundsMinZ));
		objList.add(Integer.valueOf(this.ssBoundsMaxZ));
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), this.spaceStationDimensionID);

		return true;
	}

	public void updateSpinSpeed()
	{
		if (this.momentOfInertia > 0F)
		{
			float netTorque = 0F;
			int countThrusters = 0;
			
			for(BlockVec3 thruster : thrustersPlus)
			{
				//TODO check the block is still a thruster
				float xx = (float)thruster.x - this.massCentreX;
				float zz = (float)thruster.z - this.massCentreZ;
				netTorque+= MathHelper.sqrt_float(xx*xx+zz*zz);
				countThrusters++;
			}
			for(BlockVec3 thruster : thrustersMinus)
			{
				//TODO check the block is still a thruster
				float xx = thruster.x - this.massCentreX;
				float zz = thruster.z - this.massCentreZ;
				netTorque-= MathHelper.sqrt_float(xx*xx+zz*zz);
				countThrusters++;
			}
			
			if (countThrusters == 0)
			{
				this.angularVelocityAccel = 0.001F;
				this.angularVelocityTarget = 0F;
			} else
				{
				if (countThrusters > 4)
					countThrusters = 4;
				
				//TODO: increase this above 20F in release versions so everything happens more slowly 
				this.angularVelocityAccel = netTorque / this.momentOfInertia / 20F;
				if (this.angularVelocityAccel < 0) this.angularVelocityAccel = -this.angularVelocityAccel;
				
				float maxRx = Math.max(this.ssBoundsMaxX - this.massCentreX, this.massCentreX - this.ssBoundsMinX);
				float maxRz = Math.max(this.ssBoundsMaxZ - this.massCentreZ, this.massCentreZ - this.ssBoundsMinZ);
				float maxR = Math.max(maxRx, maxRz);
				this.angularVelocityTarget = MathHelper.sqrt_float(WorldProviderOrbit.GFORCE / maxR);
				float spinCap = 0.00125F * countThrusters;
				//Do not make it spin too fast or players might get dizzy
				//Also make it so players need minimum 4 thrusters for best spin
				if (this.angularVelocityTarget > spinCap) this.angularVelocityTarget = spinCap;
				if (this.angularVelocityTarget < -spinCap) this.angularVelocityTarget = -spinCap;
				System.out.println("MaxR = "+maxR+" Angular vel = "+this.angularVelocityTarget+" Angular accel = "+this.angularVelocityAccel);
			}
		}

		if (!this.worldObj.isRemote)
		{
			//Save the updated data for the world
			if (this.savefile == null)
			{
				this.savefile = OrbitSpinSaveData.initWorldData(this.worldObj);
				dataNotLoaded = false;	
			} else
			{
				this.writeToNBT(this.savefile.datacompound);
				System.out.println(this.savefile.datacompound.getFloat("omegaSky"));
				this.savefile.markDirty();
			}
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.doSpinning = nbt.getBoolean("doSpinning");
		this.angularVelocityRadians = nbt.getFloat("omegaRad");
		this.skyAngularVelocity = nbt.getFloat("omegaSky");
		this.angularVelocityTarget = nbt.getFloat("omegaTarget");
		this.angularVelocityAccel = nbt.getFloat("omegaAcc");
		this.momentOfInertia = nbt.getFloat("MOI");
		this.massCentreX = nbt.getFloat("centreX");
		this.massCentreY = nbt.getFloat("centreY");
		this.massCentreZ = nbt.getFloat("centreZ");
		this.mass = nbt.getFloat("mass");
		this.ssBoundsMaxX = nbt.getInteger("maxX");
		this.ssBoundsMinX = nbt.getInteger("minX");
		this.ssBoundsMaxY = nbt.getInteger("maxY");
		this.ssBoundsMinY = nbt.getInteger("minY");
		this.ssBoundsMaxZ = nbt.getInteger("maxZ");
		this.ssBoundsMinZ = nbt.getInteger("minZ");

		this.thrustersPlus.clear();
		NBTTagList tPlus = nbt.getTagList("tPlus", 10);
		if (tPlus.tagCount() > 0)
		{
			for (int j = 0; j < tPlus.tagCount(); j++)
			{
				NBTTagCompound tag1 = tPlus.getCompoundTagAt(j);
				
				if (tag1 instanceof NBTTagCompound)
				{
					this.thrustersPlus.add(BlockVec3.readFromNBT((NBTTagCompound) tag1));
				}
			}
		}

		this.thrustersMinus.clear();
		NBTTagList tMinus = nbt.getTagList("tMinus", 10);
		if (tMinus.tagCount() > 0)
		{
			for (int j = 0; j < tMinus.tagCount(); j++)
			{
				NBTTagCompound tag1 = tMinus.getCompoundTagAt(j);
				
				if (tag1 instanceof NBTTagCompound)
				{
					this.thrustersMinus.add(BlockVec3.readFromNBT((NBTTagCompound) tag1));
				}
			}
		}

		NBTTagCompound oneBlock = (NBTTagCompound) nbt.getTag("oneBlock");
		if (oneBlock!=null)
			this.oneSSBlock = BlockVec3.readFromNBT(oneBlock);
		else
			this.oneSSBlock = null;

		setSpinCentre(this.massCentreX, this.massCentreZ);
		
		//Send packets to clients in this dimension
		List<Object> objList = new ArrayList<Object>();
		objList.add(Float.valueOf(this.angularVelocityRadians));
		objList.add(Boolean.valueOf(this.thrustersFiring));
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), this.spaceStationDimensionID);
		
		objList = new ArrayList<Object>();
		objList.add(Double.valueOf(this.spinCentreX));
		objList.add(Double.valueOf(this.spinCentreZ));
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), this.spaceStationDimensionID);

		objList = new ArrayList<Object>();
		objList.add(Integer.valueOf(this.ssBoundsMinX));
		objList.add(Integer.valueOf(this.ssBoundsMaxX));
		objList.add(Integer.valueOf(this.ssBoundsMinY));
		objList.add(Integer.valueOf(this.ssBoundsMaxY));
		objList.add(Integer.valueOf(this.ssBoundsMinZ));
		objList.add(Integer.valueOf(this.ssBoundsMaxZ));
		GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), this.spaceStationDimensionID);
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		 nbt.setBoolean("doSpinning", this.doSpinning);
		 nbt.setFloat("omegaRad", this.angularVelocityRadians);
		 nbt.setFloat("omegaSky", this.skyAngularVelocity);
		 nbt.setFloat("omegaTarget", this.angularVelocityTarget);
		 nbt.setFloat("omegaAcc", this.angularVelocityAccel);
		 nbt.setFloat("MOI", this.momentOfInertia);
		 nbt.setFloat("centreX", this.massCentreX);
		 nbt.setFloat("centreY", this.massCentreY);
		 nbt.setFloat("centreZ", this.massCentreZ);
		 nbt.setFloat("mass", this.mass);
		 nbt.setInteger("maxX", this.ssBoundsMaxX);
		 nbt.setInteger("minX", this.ssBoundsMinX);
		 nbt.setInteger("maxY", this.ssBoundsMaxY);
		 nbt.setInteger("minY", this.ssBoundsMinY);
		 nbt.setInteger("maxZ", this.ssBoundsMaxZ);
		 nbt.setInteger("minZ", this.ssBoundsMinZ);
		
		NBTTagList tPlus = new NBTTagList();
		for(BlockVec3 thruster : this.thrustersPlus)
		{
			NBTTagCompound thrust = new NBTTagCompound();
			thruster.writeToNBT(thrust);
			tPlus.appendTag(thrust);
		}
		nbt.setTag("tPlus", tPlus);

		NBTTagList tMinus = new NBTTagList();
		for(BlockVec3 thruster : this.thrustersMinus)
		{
			NBTTagCompound thrust = new NBTTagCompound();
			thruster.writeToNBT(thrust);
			tMinus.appendTag(thrust);
		}
		nbt.setTag("tMinus", tMinus);

		if (this.oneSSBlock != null)
		{
			NBTTagCompound oneBlock = new NBTTagCompound();
			this.oneSSBlock.writeToNBT(oneBlock);
			nbt.setTag("oneBlock", oneBlock);
		}
	}

	//TODO will this still work with other mods / plugins teleports (e.g. Bukkit)? See WorldUtil.teleportEntity()
	//Call this when client first login/transfer to this dimension
	public void sendPacketsToClient(EntityPlayerMP player)
	{
		List<Object> objList = new ArrayList<Object>();
		objList.add(Float.valueOf(this.angularVelocityRadians));
		objList.add(Boolean.valueOf(this.thrustersFiring));
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), player);
		
		objList = new ArrayList<Object>();
		objList.add(Double.valueOf(this.spinCentreX));
		objList.add(Double.valueOf(this.spinCentreZ));
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_DATA, objList), player);

		objList = new ArrayList<Object>();
		objList.add(Integer.valueOf(this.ssBoundsMinX));
		objList.add(Integer.valueOf(this.ssBoundsMaxX));
		objList.add(Integer.valueOf(this.ssBoundsMinY));
		objList.add(Integer.valueOf(this.ssBoundsMaxY));
		objList.add(Integer.valueOf(this.ssBoundsMinZ));
		objList.add(Integer.valueOf(this.ssBoundsMaxZ));
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_BOX, objList), player);
	}

	//TODO Occasional call to checkSS to update centre of mass etc (in case the player has been building)
}
