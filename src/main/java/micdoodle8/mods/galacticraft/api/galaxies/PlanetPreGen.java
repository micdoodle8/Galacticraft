package micdoodle8.mods.galacticraft.api.galaxies;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import net.minecraft.world.World;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlanetPreGen
{
	public AtomicBoolean sealedFinal = new AtomicBoolean();
	//public static AtomicBoolean anylooping = new AtomicBoolean();
	public AtomicBoolean looping = new AtomicBoolean();

	private World world;
	private int startChunkX;
	private int startChunkZ;
	private Planet callingPlanet;

	public PlanetPreGen(int dimID, int cx, int cz, Planet p)
	{
		this(FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dimID), cx, cz, p);
	}

	@SuppressWarnings("unchecked")
	public PlanetPreGen(World world, int cx, int cz, Planet p)
	{
		this.looping.set(true);

		this.world = world;
		this.startChunkX = cx;
		this.startChunkZ = cz;
		this.callingPlanet = p;

		new ThreadedPreGen();
	}

	//Multi-threaded version of the code for sealer updates (not for edge checks).
	public class ThreadedPreGen extends Thread
	{
		public ThreadedPreGen()
		{
			super("World pre gen thread");

			if (this.isAlive())
			{
				this.interrupt();
			}

			//Run this as a separate thread
			this.start();
		}

		@Override
		public void run()
		{
			PlanetPreGen.this.pregen();
		}
	}

	public void pregen()
	{
		long time1 = System.nanoTime();

		//First pass: generate
		this.world.getChunkFromChunkCoords(this.startChunkX, this.startChunkZ);

		for (int r = 1; r < 12; r++)
		{
			int xmin = this.startChunkX - r;
			int xmax = this.startChunkX + r;
			int zmin = this.startChunkZ - r;
			int zmax = this.startChunkZ + r;
			for (int i = -r; i < r; i++)
			{
				this.world.getChunkFromChunkCoords(xmin, this.startChunkZ + i);
				this.world.getChunkFromChunkCoords(xmax, this.startChunkZ - i);
				this.world.getChunkFromChunkCoords(this.startChunkX - i, zmin);
				this.world.getChunkFromChunkCoords(this.startChunkX + i, zmax);
			}
		}
		
		long time2 = System.nanoTime();
		
		//Second pass: populate?  (no point populating the edges)
		this.world.getChunkFromChunkCoords(this.startChunkX, this.startChunkZ);

		for (int r = 1; r < 11; r++)
		{
			int xmin = this.startChunkX - r;
			int xmax = this.startChunkX + r;
			int zmin = this.startChunkZ - r;
			int zmax = this.startChunkZ + r;
			for (int i = -r; i < r; i++)
			{
				this.world.getChunkFromChunkCoords(xmin, this.startChunkZ + i);
				this.world.getChunkFromChunkCoords(xmax, this.startChunkZ - i);
				this.world.getChunkFromChunkCoords(this.startChunkX - i, zmin);
				this.world.getChunkFromChunkCoords(this.startChunkX + i, zmax);
			}
		}

		if (ConfigManagerCore.enableDebug)
		{
			long time3 = System.nanoTime();
			int timetaken1 = (int) ((time2 - time1) / 1000);
			int timetaken2 = (int) ((time3 - time2) / 1000);
			int timetaken = (int) ((time3 - time1) / 1000);
			FMLLog.info("World pregen completed in "+this.world.provider.getDimensionName()+" at " + this.startChunkX + ", " + this.startChunkZ);
			FMLLog.info("Time taken (ns):  Terrain gen: " + timetaken1 + " Ore gen: " + timetaken2 + " Total: " + timetaken);
		}

		this.looping.set(false);
	}
}
