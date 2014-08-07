package micdoodle8.mods.galacticraft.core.dimension;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IExitHeight;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpinThruster;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOrbit;
import micdoodle8.mods.galacticraft.core.entities.player.GCEntityClientPlayerMP;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderOrbit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class WorldProviderOrbit extends WorldProviderSpace implements IOrbitDimension, ISolarLevel, IExitHeight
{
	public int spaceStationDimensionID;

	private static final float GFORCE = 9.81F / 400F; //gravity in metres per tick squared

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
	private float massCentreZ;
	private int ssBoundsMaxX;
	private int ssBoundsMinX;
	private int ssBoundsMaxY;
	private int ssBoundsMinY;
	private int ssBoundsMaxZ;
	private int ssBoundsMinZ;

	private LinkedList<BlockVec3> thrustersPlus = new LinkedList();
	private LinkedList<BlockVec3> thrustersMinus = new LinkedList();
	private BlockVec3 oneSSBlock;
	//private HashSet<BlockVec3> stationBlocks = new HashSet();

	private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();

	private float artificialG;
	//Used to make continuous particles + thrust sounds at the spin thrusters in this dimension
	//If false, make particles + sounds occasionally in small bursts, just for fun (micro attitude changes)
	//see: BlockSpinThruster.randomDisplayTick()
	public boolean thrustersFiring = false;
	private boolean dataNotLoaded = true;
	private List<Entity> loadedEntities = new LinkedList();
	private double pPrevMotionX = 0D;
	private double pPrevMotionY = 0D;
	private double pPrevMotionZ = 0D;
	private int pjumpticks = 0;

	@Override
	public void setDimension(int var1)
	{
		this.spaceStationDimensionID = var1;
        super.setDimension(var1);
	}

//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderOrbit(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//	}

//	@Override
//	protected void generateLightBrightnessTable()
//	{
//		final float var1 = 0.0F;
//
//		for (int var2 = 0; var2 <= 15; ++var2)
//		{
//			final float var3 = 1.0F - var2 / 15.0F;
//			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
//		}
//	}

//	@Override
//	public float[] calcSunriseSunsetColors(float var1, float var2)
//	{
//		return null;
//	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public Vec3 getFogColor(float var1, float var2)
//	{
//		return Vec3.createVectorHelper((double) 0F / 255F, (double) 0F / 255F, (double) 0F / 255F);
//	}

//	@Override
//	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks)
//	{
//		return Vec3.createVectorHelper(0, 0, 0);
//	}

//	@Override
//	public float calculateCelestialAngle(long par1, float par3)
//	{
//		final int var4 = (int) (par1 % 24000L);
//		float var5 = (var4 + par3) / 24000.0F - 0.25F;
//
//		if (var5 < 0.0F)
//		{
//			++var5;
//		}
//
//		if (var5 > 1.0F)
//		{
//			--var5;
//		}
//
//		final float var6 = var5;
//		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
//		var5 = var6 + (var5 - var6) / 3.0F;
//		return var5;
//	}


    @Override
    public CelestialBody getCelestialBody()
    {
        return GalacticraftCore.satelliteSpaceStation;
    }

    @Override
    public Vector3 getFogColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getSkyColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public boolean canRainOrSnow()
    {
        return false;
    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 24000L;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public Class<? extends IChunkProvider> getChunkProviderClass()
    {
        return ChunkProviderOrbit.class;
    }

    @Override
    public Class<? extends WorldChunkManager> getWorldChunkManagerClass()
    {
        return null;
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

	@Override
	public void updateWeather()
	{
        super.updateWeather();

		if (!this.worldObj.isRemote)
		{
			if (this.dataNotLoaded)
			{
				this.savefile = OrbitSpinSaveData.initWorldData(this.worldObj);
				this.readFromNBT(this.savefile.datacompound);
				if (ConfigManagerCore.enableDebug)
					System.out.println("Loading data from save: " + this.savefile.datacompound.getFloat("omegaSky"));
				this.dataNotLoaded = false;
			}

			if (this.doSpinning)
			{
				boolean updateNeeded = true;
				if (this.angularVelocityTarget < this.angularVelocityRadians)
				{
					float newAngle = this.angularVelocityRadians - this.angularVelocityAccel;
					if (newAngle < this.angularVelocityTarget)
					{
						newAngle = this.angularVelocityTarget;
					}
					this.setSpinRate(newAngle);
					this.thrustersFiring = true;
				}
				else if (this.angularVelocityTarget > this.angularVelocityRadians)
				{
					float newAngle = this.angularVelocityRadians + this.angularVelocityAccel;
					if (newAngle > this.angularVelocityTarget)
					{
						newAngle = this.angularVelocityTarget;
					}
					this.setSpinRate(newAngle);
					this.thrustersFiring = true;
				}
				else if (this.thrustersFiring)
				{
					this.thrustersFiring = false;
				}
				else
				{
					updateNeeded = false;
				}

				if (updateNeeded)
				{
					this.writeToNBT(this.savefile.datacompound);
					this.savefile.markDirty();

					List<Object> objList = new ArrayList<Object>();
					objList.add(Float.valueOf(this.angularVelocityRadians));
					objList.add(Boolean.valueOf(this.thrustersFiring));
					GalacticraftCore.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_STATION_SPIN, objList), this.spaceStationDimensionID);
				}

				//Update entity positions if in freefall
				this.loadedEntities.clear();
				this.loadedEntities.addAll(this.worldObj.loadedEntityList);
				for (Entity e : this.loadedEntities)
				{
					if ((e instanceof EntityItem || e instanceof EntityLivingBase && !(e instanceof EntityPlayer) || e instanceof EntityTNTPrimed || e instanceof EntityFallingBlock) && !e.onGround)
					{
						boolean freefall = true;
						if (e.boundingBox.maxX >= this.ssBoundsMinX && e.boundingBox.minX <= this.ssBoundsMaxX && e.boundingBox.maxY >= this.ssBoundsMinY && e.boundingBox.minY <= this.ssBoundsMaxY && e.boundingBox.maxZ >= this.ssBoundsMinZ && e.boundingBox.minZ <= this.ssBoundsMaxZ)
						{
							//Entity is somewhere within the space station boundaries

							//Check if the entity's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
							//If so, it's assumed the entity has something close enough to catch onto, so is not in freefall
							//Note: breatheable air here means the entity is definitely not in freefall
							int xmx = MathHelper.floor_double(e.boundingBox.maxX + 0.2D);
							int ym = MathHelper.floor_double(e.boundingBox.minY - 0.1D);
							int yy = MathHelper.floor_double(e.boundingBox.maxY + 0.1D);
							int zm = MathHelper.floor_double(e.boundingBox.minZ - 0.2D);
							int zz = MathHelper.floor_double(e.boundingBox.maxZ + 0.2D);
							BLOCKCHECK:
							for (int x = MathHelper.floor_double(e.boundingBox.minX - 0.2D); x <= xmx; x++)
							{
								for (int y = ym; y <= yy; y++)
								{
									for (int z = zm; z <= zz; z++)
									{
										if (this.worldObj.blockExists(x, y, z) && this.worldObj.getBlock(x, y, z) != Blocks.air)
										{
											freefall = false;
											break BLOCKCHECK;
										}
									}
								}
							}
						}

						if (freefall)
						{
							//Do the rotation
							if (this.angularVelocityRadians != 0F)
							{
								float angle;
								final double xx = e.posX - this.spinCentreX;
								final double zz = e.posZ - this.spinCentreZ;
								double arc = Math.sqrt(xx * xx + zz * zz);
								if (xx == 0D)
								{
									angle = zz > 0 ? 3.141592536F / 2 : -3.141592536F / 2;
								}
								else
								{
									angle = (float) Math.atan(zz / xx);
								}
								if (xx < 0D)
								{
									angle += 3.141592536F;
								}
								angle += this.angularVelocityRadians / 3F;
								arc = arc * this.angularVelocityRadians;
								final double offsetX = -arc * MathHelper.sin(angle);
								final double offsetZ = arc * MathHelper.cos(angle);
								e.posX += offsetX;
								e.posZ += offsetZ;
								e.lastTickPosX += offsetX;
								e.lastTickPosZ += offsetZ;

								//Rotated into an unloaded chunk (probably also drifted out to there): byebye
								if (!this.worldObj.blockExists(MathHelper.floor_double(e.posX), 64, MathHelper.floor_double(e.posZ)))
								{
									e.setDead();
								}

								e.boundingBox.offset(offsetX, 0.0D, offsetZ);
								//TODO check for block collisions here - if so move the entity appropriately and apply fall damage
								//Moving the entity = slide along / down		
								e.rotationYaw += this.skyAngularVelocity;
								while (e.rotationYaw > 360F)
								{
									e.rotationYaw -= 360F;
								}
							}

							//Undo deceleration
							if (e instanceof EntityLivingBase)
							{
								e.motionX /= 0.91F;
								e.motionZ /= 0.91F;
								if (e instanceof EntityFlying)
								{
									e.motionY /= 0.91F;
								}
								else if (e instanceof EntityFallingBlock)
								{
									e.motionY /= 0.9800000190734863D;
									//e.motionY += 0.03999999910593033D;
									//e.posY += 0.03999999910593033D;
									//e.lastTickPosY += 0.03999999910593033D;
								}
								else
								{
									e.motionY /= 0.9800000190734863D;
								}
							}
							else
							{
								e.motionX /= 0.9800000190734863D;
								e.motionY /= 0.9800000190734863D;
								e.motionZ /= 0.9800000190734863D;
							}
						}
					}
				}
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

//	@Override
//	public boolean canRespawnHere()
//	{
//		return !ConfigManagerCore.forceOverworldRespawn;
//	}
//
//	@Override
//	public String getWelcomeMessage()
//	{
//		return "Entering Earth Orbit";
//	}
//
//	@Override
//	public String getDepartMessage()
//	{
//		return "Leaving Earth Orbit";
//	}

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

//	@Override
//	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canDoLightning(Chunk chunk)
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canDoRainSnowIce(Chunk chunk)
//	{
//		return false;
//	}

	@Override
	public float getGravity()
	{
		return 0.075F;//0.073F;
	}

	@Override
	public boolean hasBreathableAtmosphere()
	{
		return false;
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

    @SideOnly(Side.CLIENT)
	public void spinUpdate(GCEntityClientPlayerMP p)
	{
		boolean freefall = true;

		//This is an "on the ground" check
		int playerFeetOnY = (int) (p.boundingBox.minY - 0.001D);
		Block b = this.worldObj.getBlock(MathHelper.floor_double(p.posX), playerFeetOnY, MathHelper.floor_double(p.posX));
		double blockYmax = b.getBlockBoundsMaxY() + playerFeetOnY;
		if (b != Blocks.air && p.boundingBox.minY - blockYmax < 0.001D)
		{
			freefall = false;
			p.onGround = true;
			p.posY -= p.boundingBox.minY - blockYmax; 
		}
 		else if (this.pjumpticks > 0)
 		{
 			freefall = false;
 		}
 		else if (p.boundingBox.maxX >= this.ssBoundsMinX && p.boundingBox.minX <= this.ssBoundsMaxX && p.boundingBox.maxY >= this.ssBoundsMinY && p.boundingBox.minY <= this.ssBoundsMaxY && p.boundingBox.maxZ >= this.ssBoundsMinZ && p.boundingBox.minZ <= this.ssBoundsMaxZ)
		//Player is somewhere within the space station boundaries
		{
			//Check if the player's bounding box is in the same block coordinates as any non-vacuum block (including torches etc)
			//If so, it's assumed the player has something close enough to grab onto, so is not in freefall
			//Note: breatheable air here means the player is definitely not in freefall
			int xmx = MathHelper.floor_double(p.boundingBox.maxX);
			int ym = MathHelper.floor_double(p.boundingBox.minY);
			int yy = MathHelper.floor_double(p.boundingBox.maxY);
			int zm = MathHelper.floor_double(p.boundingBox.minZ);
			int zz = MathHelper.floor_double(p.boundingBox.maxZ);
			BLOCKCHECK:
			for (int x = MathHelper.floor_double(p.boundingBox.minX); x <= xmx; x++)
			{
				for (int y = ym; y <= yy; y++)
				{
					for (int z = zm; z <= zz; z++)
					{
						if (this.worldObj.getBlock(x, y, z) != Blocks.air)
						{
							freefall = false;
							break BLOCKCHECK;
						}
					}
				}
			}
		}

		/*
		if (freefall)
		{
			//If that check didn't produce a result, see if the player is inside the walls
			//TODO: could apply special weightless movement here like Coriolis force - the player is inside the walls,  not touching them, and in a vacuum
			int quadrant = 0;
			double xd = p.posX - this.spinCentreX;
			double zd = p.posZ - this.spinCentreZ;
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
		}*/

		boolean doGravity = true;
		if (freefall)
		{
			doGravity = false;
			this.pjumpticks = 0;
			//Do spinning
			if (this.doSpinning && this.angularVelocityRadians != 0F)
			{
				//TODO maybe need to test to make sure xx and zz are not too large (outside sight range of SS)
				//TODO think about server + network load (loading/unloading chunks) when movement is rapid
				//Maybe reduce chunkloading radius?
				float angle;
				final double xx = p.posX - this.spinCentreX;
				final double zz = p.posZ - this.spinCentreZ;
				double arc = Math.sqrt(xx * xx + zz * zz);
				if (xx == 0D)
				{
					angle = zz > 0 ? 3.141592536F / 2 : -3.141592536F / 2;
				}
				else
				{
					angle = (float) Math.atan(zz / xx);
				}
				if (xx < 0D)
				{
					angle += 3.141592536F;
				}
				angle += this.angularVelocityRadians / 3F;
				arc = arc * this.angularVelocityRadians;
				double offsetX = -arc * MathHelper.sin(angle);
				double offsetZ = arc * MathHelper.cos(angle);

				//Check for block collisions here - if so move the player appropriately
				//First check that there are no existing collisions where the player is now (TODO: bounce the player away)
				if (this.worldObj.getCollidingBoundingBoxes(p, p.boundingBox).size() == 0)
				{
					//Now check for collisions in the new direction and if there are some, try reducing the movement
					int collisions = 0;
					do
					{
						List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(p, p.boundingBox.addCoord(offsetX, 0.0D, offsetZ));
						collisions = list.size();
						if (collisions > 0)
						{
							if (!doGravity)
							{
								p.motionX += -offsetX;
								p.motionZ += -offsetZ;
							}
							offsetX /= 2D;
							offsetZ /= 2D;
							if (offsetX < 0.01D && offsetX > -0.01D)
							{
								offsetX = 0D;
							}
							if (offsetZ < 0.01D && offsetZ > -0.01D)
							{
								offsetZ = 0D;
							}
							doGravity = true;

						}
					}
					while (collisions > 0);

					p.posX += offsetX;
					p.posZ += offsetZ;
					p.boundingBox.offset(offsetX, 0.0D, offsetZ);
				}

				p.rotationYaw += this.skyAngularVelocity;
				while (p.rotationYaw > 360F)
				{
					p.rotationYaw -= 360F;
				}

				/*				//Just started freefall - give some impulse
								if (!p.inFreefall && p.inFreefallFirstCheck)
								{
									p.motionX += offsetX * 0.91F;
									p.motionZ += offsetZ * 0.91F;
								}*/
			}

			//Reverse effects of deceleration
			p.motionX /= 0.91F;
			p.motionZ /= 0.91F;
			p.motionY /= 0.9800000190734863D;

			//Do freefall motion
			if (!p.capabilities.isCreativeMode)
			{
				double dx = p.motionX - this.pPrevMotionX;
				double dy = p.motionY - this.pPrevMotionY;
				double dz = p.motionZ - this.pPrevMotionZ;
				double dyaw = p.rotationYaw - p.prevRotationYaw;
				p.rotationYaw -= dyaw * 0.8D;

				//if (p.capabilities.isFlying)
				///Undo whatever vanilla tried to do to our y motion
				p.motionY -= dy;
				p.motionX -= dx;
				p.motionZ -= dz;

				if (p.movementInput.moveForward != 0)
				{
					p.motionX -= p.movementInput.moveForward * MathHelper.sin(p.rotationYaw / 57.29578F) / 400F;
					p.motionZ += p.movementInput.moveForward * MathHelper.cos(p.rotationYaw / 57.29578F) / 400F;
				}

				if (p.movementInput.sneak)
				{
					p.motionY -= 0.0016D;
				}

				if (p.movementInput.jump)
				{
					p.motionY += 0.0016D;
				}

				if (p.motionX > 0.7F)
				{
					p.motionX = 0.7F;
				}
				if (p.motionX < -0.7F)
				{
					p.motionX = -0.7F;
				}
				if (p.motionY > 0.7F)
				{
					p.motionY = 0.7F;
				}
				if (p.motionY < -0.7F)
				{
					p.motionY = -0.7F;
				}
				if (p.motionZ > 0.7F)
				{
					p.motionZ = 0.7F;
				}
				if (p.motionZ < -0.7F)
				{
					p.motionZ = -0.7F;
				}
			}
			else
			{
				//Half the normal acceleration in Creative mode
				double dx = p.motionX - this.pPrevMotionX;
				double dy = p.motionY - this.pPrevMotionY;
				double dz = p.motionZ - this.pPrevMotionZ;
				p.motionX -= dx / 2;
				p.motionY -= dy / 2;
				p.motionZ -= dz / 2;

				if (p.motionX > 1.2F)
				{
					p.motionX = 1.2F;
				}
				if (p.motionX < -1.2F)
				{
					p.motionX = -1.2F;
				}
				if (p.motionY > 0.5F)
				{
					p.motionY = 0.5F;
				}
				if (p.motionY < -0.5F)
				{
					p.motionY = -0.5F;
				}
				if (p.motionZ > 1.2F)
				{
					p.motionZ = 1.2F;
				}
				if (p.motionZ < -1.2F)
				{
					p.motionZ = -1.2F;
				}
			}
			//TODO: Think about endless drift?
			//Player may run out of oxygen - that will kill the player eventually if can't get back to SS
			//Maybe player needs a 'suicide' button if floating helplessly in space and with no tether
			//Could auto-kill + respawn the player if floats too far away (config option whether to lose items or not)
			//But we want players to be able to enjoy the view of the spinning space station from the outside
			//Arm and leg movements could start tumbling the player?
		}
		else
		//Not freefall - within arm's length of something or jumping
		{
			if (p.motionY != 0) p.motionY = this.pPrevMotionY;
			if (p.movementInput.jump)
			{
				if (p.onGround)
				{
					p.motionY += 0.05D;
					this.pjumpticks = 5;
					p.onGround = false;
				}
				else
				{
					p.motionY += 0.01D * (this.pjumpticks + 1);
				}
			}
			else if (p.movementInput.sneak)
			{
				if (!p.onGround)
				{
					p.motionY -= 0.01D;
				}
				this.pjumpticks = 0;
			}

			this.pjumpticks--;
		}

		//Artificial gravity
		if (doGravity)
		{
			int quadrant = 0;
			double xd = p.posX - this.spinCentreX;
			double zd = p.posZ - this.spinCentreZ;
			double accel = Math.sqrt(xd * xd + zd * zd) * this.angularVelocityRadians * this.angularVelocityRadians * 4D;

			if (xd < 0)
			{
				if (xd < -Math.abs(zd))
				{
					quadrant = 2;
				}
				else
				{
					quadrant = zd < 0 ? 3 : 1;
				}
			}
			else if (xd > Math.abs(zd))
			{
				quadrant = 0;
			}
			else
			{
				quadrant = zd < 0 ? 3 : 1;
			}

			switch (quadrant)
			{
			case 0:
				p.motionX += accel;
				break;
			case 1:
				p.motionZ += accel;
				break;
			case 2:
				p.motionX -= accel;
				break;
			case 3:
			default:
				p.motionZ -= accel;
			}
		}

		p.inFreefall = freefall;
		p.inFreefallFirstCheck = true;
		this.pPrevMotionX = p.motionX;
		this.pPrevMotionY = p.motionY;
		this.pPrevMotionZ = p.motionZ;
	}

	public float getSpinRate()
	{
		return this.skyAngularVelocity;
	}

	/**
	 * Sets the spin rate for the dimension in radians per tick For example,
	 * 0.031415 would be 1/200 revolution per tick So that would be 1 revolution
	 * every 10 seconds
	 */
	public void setSpinRate(float angle)
	{
		this.angularVelocityRadians = angle;
		this.skyAngularVelocity = angle * 180F / 3.1415927F;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            this.updateSkyProviderSpinRate();
        }
	}

    @SideOnly(Side.CLIENT)
    private void updateSkyProviderSpinRate()
    {
        IRenderHandler sky = this.getSkyRenderer();
        if (sky instanceof SkyProviderOrbit)
        {
            ((SkyProviderOrbit) sky).spinDeltaPerTick = this.skyAngularVelocity;
        }
    }

	public void setSpinRate(float angle, boolean firing)
	{
		this.angularVelocityRadians = angle;
		this.skyAngularVelocity = angle * 180F / 3.1415927F;
		IRenderHandler sky = this.getSkyRenderer();
		if (sky instanceof SkyProviderOrbit)
		{
			((SkyProviderOrbit) sky).spinDeltaPerTick = this.skyAngularVelocity;
		}
		this.thrustersFiring = firing;
	}

	public void setSpinCentre(double x, double z)
	{
		this.spinCentreX = x;
		this.spinCentreZ = z;
		if (this.worldObj.isRemote)
		{
			if (ConfigManagerCore.enableDebug)
				System.out.println("Clientside update to spin centre: " + x + "," + z);
		}
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
			this.thrustersPlus.add(thruster);
			this.thrustersMinus.remove(thruster);
		}
		else
		{
			this.thrustersPlus.remove(thruster);
			this.thrustersMinus.add(thruster);
		}
	}

	public void removeThruster(BlockVec3 thruster, boolean positive)
	{
		if (positive)
		{
			this.thrustersPlus.remove(thruster);
		}
		else
		{
			this.thrustersMinus.remove(thruster);
		}
	}

	/**
	 * This will check all blocks which are in contact with each other to find
	 * the shape of the spacestation. It also finds the centre of mass (to
	 * rotate around) and the moment of inertia (how easy/hard this is to
	 * rotate).
	 * 
	 * If placingThruster is true, it will return false if the thruster (at
	 * baseBlock) is not in contact with the "existing" spacestation - so the
	 * player cannot place thrusters on outlying disconnected blocks and expect
	 * them to have an effect.
	 * 
	 * Note: this check will briefly load, server-side, all chunks which have
	 * spacestation blocks in them or 1 block adjacent to those.
	 * 
	 * @param baseBlock
	 * @return
	 */
	public boolean checkSS(BlockVec3 baseBlock, boolean placingThruster)
	{
		if (this.oneSSBlock == null || this.oneSSBlock.getBlockID(this.worldObj) == Blocks.air)
		{
			if (baseBlock != null)
			{
				this.oneSSBlock = baseBlock.clone();
			}
			else
			{
				this.oneSSBlock = new BlockVec3(0, 64, 0);
			}
		}

		// Find contiguous blocks using an algorithm like the oxygen sealer one
		List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
		List<BlockVec3> nextLayer = new LinkedList<BlockVec3>();
		final List<BlockVec3> foundThrusters = new LinkedList<BlockVec3>();

		this.checked.clear();
		currentLayer.add(this.oneSSBlock.clone());
		this.checked.add(this.oneSSBlock.clone());
		Block bStart = this.oneSSBlock.getBlockID(this.worldObj);
		if (bStart instanceof BlockSpinThruster)
		{
			foundThrusters.add(this.oneSSBlock);
		}

		float thismass = 0.1F; //Mass of a thruster
		float thismassCentreX = 0.1F * this.oneSSBlock.x;
		float thismassCentreY = 0.1F * this.oneSSBlock.y;
		float thismassCentreZ = 0.1F * this.oneSSBlock.z;
		float thismoment = 0F;
		int thisssBoundsMaxX = this.oneSSBlock.x;
		int thisssBoundsMinX = this.oneSSBlock.x;
		int thisssBoundsMaxY = this.oneSSBlock.y;
		int thisssBoundsMinY = this.oneSSBlock.y;
		int thisssBoundsMaxZ = this.oneSSBlock.z;
		int thisssBoundsMinZ = this.oneSSBlock.z;

		while (currentLayer.size() > 0)
		{
			for (BlockVec3 vec : currentLayer)
			{
				if (vec.x < thisssBoundsMinX)
				{
					thisssBoundsMinX = vec.x;
				}
				if (vec.y < thisssBoundsMinY)
				{
					thisssBoundsMinY = vec.y;
				}
				if (vec.z < thisssBoundsMinZ)
				{
					thisssBoundsMinZ = vec.z;
				}
				if (vec.x > thisssBoundsMaxX)
				{
					thisssBoundsMaxX = vec.x;
				}
				if (vec.y > thisssBoundsMaxY)
				{
					thisssBoundsMaxY = vec.y;
				}
				if (vec.z > thisssBoundsMaxZ)
				{
					thisssBoundsMaxZ = vec.z;
				}

				for (int side = 0; side < 6; side++)
				{
					if (vec.sideDone[side])
					{
						continue;
					}
					BlockVec3 sideVec = vec.newVecSide(side);

					if (!this.checked.contains(sideVec))
					{
						this.checked.add(sideVec);
						Block b = sideVec.getBlockID(this.worldObj);
						if (!(b instanceof BlockAir) && b != null)
						{
							nextLayer.add(sideVec);
							if (bStart == Blocks.air)
							{
								this.oneSSBlock = sideVec.clone();
								bStart = b;
							}
							float m = 1.0F;
							//Liquids have a mass of 1, stone, metal blocks etc will be heavier 
							if (!(b instanceof BlockLiquid))
							{
								//For most blocks, hardness gives a good idea of mass
								m = b.getBlockHardness(this.worldObj, sideVec.x, sideVec.y, sideVec.z);
								if (m < 0.1F)
								{
									m = 0.1F;
								}
								else if (m > 30F)
								{
									m = 30F;
								}
								//Wood items have a high hardness compared with their presumed mass 
								if (b.getMaterial() == Material.wood)
								{
									m /= 4;
								}

								//TODO: higher mass for future Galacticraft hi-density item like neutronium
								//Maybe also check for things in other mods by name: lead, uranium blocks? 
							}
							thismassCentreX += m * sideVec.x;
							thismassCentreY += m * sideVec.y;
							thismassCentreZ += m * sideVec.z;
							thismass += m;
							thismoment += m * (sideVec.x * sideVec.x + sideVec.z * sideVec.z);
							if (b instanceof BlockSpinThruster)
							{
								foundThrusters.add(sideVec);
							}
						}
					}
				}
			}

			currentLayer = nextLayer;
			nextLayer = new LinkedList<BlockVec3>();
		}

		if (placingThruster && !this.checked.contains(baseBlock))
		{
			if (foundThrusters.size() > 0)
			{
				//The thruster was not placed on the existing contiguous space station: it must be.
				if (ConfigManagerCore.enableDebug)
					System.out.println("Returning false: oneSSBlock was " + this.oneSSBlock.x + "," + this.oneSSBlock.y + "," + this.oneSSBlock.z + " - baseBlock was " + baseBlock.x + "," + baseBlock.y + "," + baseBlock.z + " - found " + foundThrusters.size());
				return false;
			}

			//No thruster on the original space station - so assume the player made new station and start check again
			//This offers players a reset option: just remove all thrusters from original station then starting adding to new one
			//(This first check prevents an infinite loop)
			if (!this.oneSSBlock.equals(baseBlock))
			{
				this.oneSSBlock = baseBlock.clone();
				if (this.oneSSBlock.getBlockID(this.worldObj) != Blocks.air)
				{
					return this.checkSS(baseBlock, true);
				}
			}

			return false;

		}

		// Update thruster lists based on what was found
		this.thrustersPlus.clear();
		this.thrustersMinus.clear();
		for (BlockVec3 thruster : foundThrusters)
		{
			int facing = thruster.getBlockMetadata(this.worldObj) & 8;
			if (facing == 0)
			{
				this.thrustersPlus.add(thruster.clone());
			}
			else
			{
				this.thrustersMinus.add(thruster.clone());
			}
		}

		// Calculate centre of mass
		float mass = thismass;

		this.massCentreX = thismassCentreX / thismass + 0.5F;
		float massCentreY = thismassCentreY / thismass + 0.5F;
		this.massCentreZ = thismassCentreZ / thismass + 0.5F;
		//System.out.println("(X,Z) = "+this.massCentreX+","+this.massCentreZ);

		this.setSpinCentre(this.massCentreX, this.massCentreZ);

		//The boundary is at the outer edges of the blocks
		this.ssBoundsMaxX = thisssBoundsMaxX + 1;
		this.ssBoundsMinX = thisssBoundsMinX;
		this.ssBoundsMaxY = thisssBoundsMaxY + 1;
		this.ssBoundsMinY = thisssBoundsMinY;
		this.ssBoundsMaxZ = thisssBoundsMaxZ + 1;
		this.ssBoundsMinZ = thisssBoundsMinZ;

		// Calculate momentOfInertia
		thismoment -= this.massCentreX * this.massCentreX * mass;
		thismoment -= this.massCentreZ * this.massCentreZ * mass;
		this.momentOfInertia = thismoment;

		//TODO
		// TODO defy gravity
		// TODO break blocks which are outside SS (not in checked)
		// TODO prevent spin if there is a huge number of blocks outside SS

		if (ConfigManagerCore.enableDebug)
			System.out.println("MoI = " + this.momentOfInertia + " CoMx = " + this.massCentreX + " CoMz = " + this.massCentreZ);

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

		this.updateSpinSpeed();

		return true;
	}

	public void updateSpinSpeed()
	{
		if (this.momentOfInertia > 0F)
		{
			float netTorque = 0F;
			int countThrusters = 0;

			for (BlockVec3 thruster : this.thrustersPlus)
			{
				float xx = thruster.x - this.massCentreX;
				float zz = thruster.z - this.massCentreZ;
				netTorque += MathHelper.sqrt_float(xx * xx + zz * zz);
				countThrusters++;
			}
			for (BlockVec3 thruster : this.thrustersMinus)
			{
				float xx = thruster.x - this.massCentreX;
				float zz = thruster.z - this.massCentreZ;
				netTorque -= MathHelper.sqrt_float(xx * xx + zz * zz);
				countThrusters++;
			}

			if (countThrusters == 0)
			{
				this.angularVelocityAccel = 0.001F;
				this.angularVelocityTarget = 0F;
			}
			else
			{
				if (countThrusters > 4)
				{
					countThrusters = 4;
				}

				float maxRx = Math.max(this.ssBoundsMaxX - this.massCentreX, this.massCentreX - this.ssBoundsMinX);
				float maxRz = Math.max(this.ssBoundsMaxZ - this.massCentreZ, this.massCentreZ - this.ssBoundsMinZ);
				float maxR = Math.max(maxRx, maxRz);
				this.angularVelocityTarget = MathHelper.sqrt_float(WorldProviderOrbit.GFORCE / maxR) / 2;
				//The divide by 2 is not scientific but is a Minecraft factor as everything happens more quickly
				float spinCap = 0.00125F * countThrusters;

				//TODO: increase this above 20F in release versions so everything happens more slowly 
				this.angularVelocityAccel = netTorque / this.momentOfInertia / 20F;
				if (this.angularVelocityAccel < 0)
				{
					this.angularVelocityAccel = -this.angularVelocityAccel;
					this.angularVelocityTarget = -this.angularVelocityTarget;
					if (this.angularVelocityTarget < -spinCap)
					{
						this.angularVelocityTarget = -spinCap;
					}
				}
				else
				//Do not make it spin too fast or players might get dizzy
				//Also make it so players need minimum 4 thrusters for best spin
				if (this.angularVelocityTarget > spinCap)
				{
					this.angularVelocityTarget = spinCap;
				}

				if (ConfigManagerCore.enableDebug)
					System.out.println("MaxR = " + maxR + " Angular vel = " + this.angularVelocityTarget + " Angular accel = " + this.angularVelocityAccel);
			}
		}

		if (!this.worldObj.isRemote)
		{
			//Save the updated data for the world
			if (this.savefile == null)
			{
				this.savefile = OrbitSpinSaveData.initWorldData(this.worldObj);
				this.dataNotLoaded = false;
			}
			else
			{
				this.writeToNBT(this.savefile.datacompound);
				this.savefile.markDirty();
			}
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		this.doSpinning = true;//nbt.getBoolean("doSpinning");
		this.angularVelocityRadians = nbt.getFloat("omegaRad");
		this.skyAngularVelocity = nbt.getFloat("omegaSky");
		this.angularVelocityTarget = nbt.getFloat("omegaTarget");
		this.angularVelocityAccel = nbt.getFloat("omegaAcc");

		NBTTagCompound oneBlock = (NBTTagCompound) nbt.getTag("oneBlock");
		if (oneBlock != null)
		{
			this.oneSSBlock = BlockVec3.readFromNBT(oneBlock);
		}
		else
		{
			this.oneSSBlock = null;
		}

		//A lot of the data can be refreshed by checkSS
		this.checkSS(this.oneSSBlock, false);

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
		if (this.oneSSBlock != null)
		{
			NBTTagCompound oneBlock = new NBTTagCompound();
			this.oneSSBlock.writeToNBT(oneBlock);
			nbt.setTag("oneBlock", oneBlock);
		}
	}

	/**
	 * Call this when player first login/transfer to this dimension
	 * 
	 * TODO how can this code be called by other mods / plugins with teleports
	 * (e.g. Bukkit)? See WorldUtil.teleportEntity()
	 * 
	 * @param player
	 */
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

	@Override
	public float getThermalLevelModifier()
	{
		return 0;
	}

    @Override
    public float getWindLevel()
    {
        return 0.1F;
    }

	//TODO Occasional call to checkSS to update centre of mass etc (in case the player has been building)
}
