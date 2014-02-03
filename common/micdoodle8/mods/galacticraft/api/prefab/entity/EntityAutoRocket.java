package micdoodle8.mods.galacticraft.api.prefab.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import micdoodle8.mods.galacticraft.core.entities.player.GCCorePlayerMP;
import micdoodle8.mods.galacticraft.core.event.GCCoreLandingPadRemovalEvent;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityAutoRocket extends EntitySpaceshipBase implements IDockable, IInventory
{
	public FluidTank fuelTank = new FluidTank(this.getFuelTankCapacity());
	public int destinationFrequency = -1;
	public Vector3 targetVec;
	public int targetDimension;
	protected ItemStack[] cargoItems;
	private IFuelDock landingPad;
	public boolean landing;
	public EnumAutoLaunch autoLaunchSetting;
	private static boolean marsLoaded = Loader.isModLoaded("GalacticraftMars");

	public int autoLaunchCountdown;
	public String statusMessage;
	public int statusMessageCooldown;
	public int lastStatusMessageCooldown;
	public boolean statusValid;
	protected double lastMotionY;
	protected double lastLastMotionY;
	private boolean waitForPlayer;

	public EntityAutoRocket(World world)
	{
		super(world);
		this.yOffset = 0;
	}

	public EntityAutoRocket(World world, double posX, double posY, double posZ)
	{
		this(world);
		this.setSize(0.98F, 2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(posX, posY, posZ);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
	}

	public abstract int getFuelTankCapacity();

	public boolean checkLaunchValidity()
	{
		this.statusMessageCooldown = 40;

		if (this.hasValidFuel())
		{
			if (this.launchPhase == EnumLaunchPhase.UNIGNITED.getPhase() && !this.worldObj.isRemote)
			{
				if (!this.setFrequency())
				{
					this.destinationFrequency = -1;
					this.statusMessage = "\u00a7cFrequency#\u00a7cNot Set";
					return false;
				}
				else
				{
					this.statusMessage = "\u00a7aSuccess";
					return true;
				}
			}
		}
		else
		{
			this.destinationFrequency = -1;
			this.statusMessage = "\u00a7cNot Enough#\u00a7cFuel";
			return false;
		}

		this.destinationFrequency = -1;
		return false;
	}

	public boolean setFrequency()
	{
		if (!EntityAutoRocket.marsLoaded)
		{
			return false;
		}

		for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
		{
			for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
			{
				for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
				{
					TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

					if (tile instanceof IFuelDock)
					{
						IFuelDock dock = (IFuelDock) tile;

						try
						{
							TileEntity launchController = null;
							Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController");

							for (ILandingPadAttachable connectedTile : dock.getConnectedTiles())
							{
								try
								{
									controllerClass.cast(connectedTile);
								}
								catch (ClassCastException e)
								{
									continue;
								}

								launchController = (TileEntity) connectedTile;

								if (launchController != null)
								{
									TileEntity tile2 = launchController.worldObj.getBlockTileEntity(launchController.xCoord, launchController.yCoord, launchController.zCoord);

									try
									{
										controllerClass.cast(tile2);
									}
									catch (ClassCastException e)
									{
										launchController = null;
										continue;
									}

									launchController = tile2;
								}

								if (launchController != null)
								{
									break;
								}
							}

							if (launchController != null)
							{
								Boolean b = (Boolean) controllerClass.getMethod("validFrequency").invoke(launchController);

								if (b != null && b)
								{
									int controllerFrequency = controllerClass.getField("destFrequency").getInt(launchController);
									boolean foundPad = this.setTarget(false, controllerFrequency);

									if (foundPad)
									{
										this.destinationFrequency = controllerFrequency;
										return true;
									}
								}
							}
						}
						catch (ClassCastException e)
						{
							;
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}

		this.destinationFrequency = -1;
		return false;
	}

	protected boolean setTarget(boolean doSet, int destFreq)
	{
		if (!EntityAutoRocket.marsLoaded || FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().worldServers == null)
		{
			return false;
		}

		for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().worldServers.length; i++)
		{
			WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[i];

			for (int j = 0; j < world.loadedTileEntityList.size(); j++)
			{
				TileEntity tile = (TileEntity) world.loadedTileEntityList.get(j);

				if (tile != null)
				{
					tile = world.getBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);

					try
					{
						Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController");

						try
						{
							controllerClass.cast(tile);
						}
						catch (ClassCastException e)
						{
							continue;
						}

						TileEntity launchController = tile;
						int controllerFrequency = controllerClass.getField("frequency").getInt(tile);

						if (destFreq == controllerFrequency)
						{
							boolean targetSet = false;

							blockLoop:
							for (int x = -2; x <= 2; x++)
							{
								for (int z = -2; z <= 2; z++)
								{
									int blockID = world.getBlockId(launchController.xCoord + x, launchController.yCoord, launchController.zCoord + z);

									if (blockID > 0 && Block.blocksList[blockID] instanceof GCCoreBlockLandingPadFull)
									{
										if (doSet)
										{
											this.targetVec = new Vector3(launchController.xCoord + x, launchController.yCoord, launchController.zCoord + z);
										}

										targetSet = true;
										break blockLoop;
									}
								}
							}

							if (doSet)
							{
								this.targetDimension = launchController.worldObj.provider.dimensionId;
							}

							if (!targetSet)
							{
								if (doSet)
								{
									this.targetVec = null;
								}

								return false;
							}
							else
							{
								return true;
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return false;
	}

	@Override
	public int getScaledFuelLevel(int scale)
	{
		if (this.getFuelTankCapacity() <= 0)
		{
			return 0;
		}

		return this.fuelTank.getFluidAmount() * scale / this.getFuelTankCapacity();
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			if (this.statusMessageCooldown > 0)
			{
				this.statusMessageCooldown--;
			}

			if (this.statusMessageCooldown == 0 && this.lastStatusMessageCooldown > 0 && this.statusValid)
			{
				this.autoLaunch();
			}

			if (this.autoLaunchCountdown > 0)
			{
				this.autoLaunchCountdown--;

				if (this.autoLaunchCountdown <= 0)
				{
					this.autoLaunch();
				}
			}

			if (this.autoLaunchSetting == EnumAutoLaunch.ROCKET_IS_FUELED && this.fuelTank.getFluidAmount() == this.fuelTank.getCapacity())
			{
				this.autoLaunch();
			}

			if (EntityAutoRocket.marsLoaded && this.autoLaunchSetting == EnumAutoLaunch.REDSTONE_SIGNAL)
			{
				if (this.ticks % 5 == 0)
				{
					if (this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
					{
						for (ILandingPadAttachable tile : this.getLandingPad().getConnectedTiles())
						{
							if (this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) != null)
							{
								try
								{
									Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController");

									try
									{
										controllerClass.cast(this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord));
									}
									catch (ClassCastException e)
									{
										continue;
									}

									if (this.worldObj.isBlockIndirectlyGettingPowered(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord))
									{
										this.autoLaunch();
									}
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}

			if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase() && this.hasValidFuel())
			{
				if (this.landing && this.targetVec != null && this.worldObj.getBlockTileEntity(this.targetVec.intX(), this.targetVec.intY(), this.targetVec.intZ()) instanceof IFuelDock && this.posY - this.targetVec.y < 5)
				{
					for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
					{
						for (int y = MathHelper.floor_double(this.posY - 1D); y <= MathHelper.floor_double(this.posY) + 1; y++)
						{
							for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
							{
								TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

								if (tile instanceof IFuelDock)
								{
									this.failRocket();
								}
							}
						}
					}
				}
			}

			if (this.getLandingPad() != null && this.getLandingPad().getConnectedTiles() != null)
			{
				for (ILandingPadAttachable tile : this.getLandingPad().getConnectedTiles())
				{
					if (this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) != null && this.worldObj.getBlockTileEntity(((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord) instanceof GCCoreTileEntityFuelLoader)
					{
						if (tile instanceof GCCoreTileEntityFuelLoader && ((GCCoreTileEntityFuelLoader) tile).getEnergyStored() > 0)
						{
							if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
							{
								this.setPad(null);
							}
						}
					}
				}
			}

			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, 60, this.worldObj.provider.dimensionId, GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList<Object>())));

			this.lastStatusMessageCooldown = this.statusMessageCooldown;
		}
	}

	@Override
	protected boolean shouldMoveClientSide()
	{
		return false;
	}

	private void autoLaunch()
	{
		this.ignite();
		this.autoLaunchSetting = null;
	}

	public boolean igniteWithResult()
	{
		if (this.setFrequency())
		{
			super.ignite();
			return true;
		}
		else
		{
			if (this.isPlayerRocket())
			{
				super.ignite();
			}

			return false;
		}
	}

	@Override
	public void ignite()
	{
		this.igniteWithResult();
	}

	public abstract boolean isPlayerRocket();

	protected void landRocket(int x, int y, int z)
	{
		TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

		if (tile instanceof IFuelDock)
		{
			IFuelDock dock = (IFuelDock) tile;

			if (this.isDockValid(dock))
			{
				if (!this.worldObj.isRemote)
				{
					this.launchPhase = EnumLaunchPhase.UNIGNITED.getPhase();
					this.landing = false;
					this.targetVec = null;
					this.setPad(dock);

					if (EntityAutoRocket.marsLoaded)
					{
						HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

						try
						{
							Class<?> controllerClass = Class.forName("micdoodle8.mods.galacticraft.mars.tile.GCMarsTileEntityLaunchController");

							for (ILandingPadAttachable connectedTile : connectedTiles)
							{
								if (connectedTile != null)
								{
									TileEntity updatedTile = this.worldObj.getBlockTileEntity(((TileEntity) connectedTile).xCoord, ((TileEntity) connectedTile).yCoord, ((TileEntity) connectedTile).zCoord);

									try
									{
										controllerClass.cast(updatedTile);
									}
									catch (ClassCastException e)
									{
										continue;
									}

									Boolean autoLaunchEnabled = controllerClass.getField("launchSchedulingEnabled").getBoolean(updatedTile);

									if (autoLaunchEnabled)
									{
										this.autoLaunchSetting = EnumAutoLaunch.values()[controllerClass.getField("launchDropdownSelection").getInt(updatedTile)];
									}

									if (this.autoLaunchSetting != null)
									{
										switch (this.autoLaunchSetting)
										{
										case INSTANT:
											this.autoLaunch();
											break;
										case TIME_10_SECONDS:
											this.autoLaunchCountdown = 200;
											break;
										case TIME_30_SECONDS:
											this.autoLaunchCountdown = 600;
											break;
										case TIME_1_MINUTE:
											this.autoLaunchCountdown = 1200;
											break;
										default:
											break;
										}
									}

									break;
								}
							}
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}

				this.onRocketLand(x, y, z);
				return;
			}
		}
	}

	protected void onRocketLand(int x, int y, int z)
	{
		this.setPositionAndRotation(x + 0.5, y + 0.3D, z + 0.5, this.rotationYaw, 0.0F);
	}

	@Override
	public void readNetworkedData(ByteArrayDataInput dataStream)
	{
		super.readNetworkedData(dataStream);
		this.fuelTank.setFluid(new FluidStack(GalacticraftCore.fluidFuel, dataStream.readInt()));
		this.landing = dataStream.readBoolean();
		this.destinationFrequency = dataStream.readInt();

		if (dataStream.readBoolean())
		{
			this.targetVec = new Vector3(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
		}

		this.motionX = dataStream.readDouble() / 8000.0D;
		this.motionY = dataStream.readDouble() / 8000.0D;
		this.motionZ = dataStream.readDouble() / 8000.0D;
		this.lastMotionY = dataStream.readDouble() / 8000.0D;
		this.lastLastMotionY = dataStream.readDouble() / 8000.0D;

		if (this.cargoItems == null)
		{
			this.cargoItems = new ItemStack[this.getSizeInventory()];
		}

		this.setWaitForPlayer(dataStream.readBoolean());

		this.statusMessage = dataStream.readUTF();
		this.statusMessage = this.statusMessage.equals("") ? null : this.statusMessage;
		this.statusMessageCooldown = dataStream.readInt();
		this.lastStatusMessageCooldown = dataStream.readInt();
		this.statusValid = dataStream.readBoolean();
	}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> list)
	{
		super.getNetworkedData(list);

		list.add(this.fuelTank.getFluidAmount());
		list.add(this.landing);
		list.add(this.destinationFrequency);
		list.add(this.targetVec != null);

		if (this.targetVec != null)
		{
			list.add(this.targetVec.x);
			list.add(this.targetVec.y);
			list.add(this.targetVec.z);
		}

		list.add(this.motionX * 8000.0D);
		list.add(this.motionY * 8000.0D);
		list.add(this.motionZ * 8000.0D);
		list.add(this.lastMotionY * 8000.0D);
		list.add(this.lastLastMotionY * 8000.0D);

		list.add(this.getWaitForPlayer());

		list.add(this.statusMessage != null ? this.statusMessage : "");
		list.add(this.statusMessageCooldown);
		list.add(this.lastStatusMessageCooldown);
		list.add(this.statusValid);

		return list;
	}

	@Override
	protected void failRocket()
	{
		if (this.shouldCancelExplosion())
		{
			for (int i = -3; i <= 3; i++)
			{
				if (this.landing && this.targetVec != null && this.worldObj.getBlockTileEntity((int) Math.floor(this.posX), (int) Math.floor(this.posY + i), (int) Math.floor(this.posZ)) instanceof IFuelDock && this.posY - this.targetVec.y < 5)
				{
					for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
					{
						for (int y = MathHelper.floor_double(this.posY - 3.0D); y <= MathHelper.floor_double(this.posY) + 1; y++)
						{
							for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
							{
								TileEntity tile = this.worldObj.getBlockTileEntity(x, y, z);

								if (tile instanceof IFuelDock)
								{
									this.landRocket(x, y, z);
									return;
								}
							}
						}
					}
				}
			}
		}

		if (this.launchPhase == EnumLaunchPhase.LAUNCHED.getPhase())
		{
			super.failRocket();
		}
	}

	protected boolean shouldCancelExplosion()
	{
		return this.hasValidFuel();
	}

	public boolean hasValidFuel()
	{
		return this.fuelTank.getFluidAmount() > 0;
	}

	@Override
	public void onLaunch()
	{
		super.onLaunch();

		if (!this.worldObj.isRemote)
		{
			if (!(this.worldObj.provider instanceof IOrbitDimension) && this.riddenByEntity != null && this.riddenByEntity instanceof GCCorePlayerMP)
			{
				((GCCorePlayerMP) this.riddenByEntity).setCoordsTeleportedFromX(this.riddenByEntity.posX);
				((GCCorePlayerMP) this.riddenByEntity).setCoordsTeleportedFromZ(this.riddenByEntity.posZ);
			}

			int amountRemoved = 0;

			for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
			{
				for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
				{
					for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
					{
						final int id = this.worldObj.getBlockId(x, y, z);
						final Block block = Block.blocksList[id];

						if (block != null && block instanceof GCCoreBlockLandingPadFull)
						{
							if (amountRemoved < 9)
							{
								GCCoreLandingPadRemovalEvent event = new GCCoreLandingPadRemovalEvent(this.worldObj, x, y, z);
								MinecraftForge.EVENT_BUS.post(event);

								if (event.allow)
								{
									this.worldObj.setBlockToAir(x, y, z);
									amountRemoved = 9;
								}
							}
						}
					}
				}
			}

			this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		if (this.fuelTank.getFluid() != null)
		{
			nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
		}

		if (this.getSizeInventory() > 0)
		{
			final NBTTagList var2 = new NBTTagList();

			for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
			{
				if (this.cargoItems[var3] != null)
				{
					final NBTTagCompound var4 = new NBTTagCompound();
					var4.setByte("Slot", (byte) var3);
					this.cargoItems[var3].writeToNBT(var4);
					var2.appendTag(var4);
				}
			}

			nbt.setTag("Items", var2);
		}

		nbt.setBoolean("TargetValid", this.targetVec != null);

		if (this.targetVec != null)
		{
			nbt.setDouble("targetTileX", this.targetVec.x);
			nbt.setDouble("targetTileY", this.targetVec.y);
			nbt.setDouble("targetTileZ", this.targetVec.z);
		}

		nbt.setBoolean("WaitingForPlayer", this.getWaitForPlayer());
		nbt.setBoolean("Landing", this.landing);
		nbt.setInteger("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
		nbt.setInteger("TimeUntilAutoLaunch", this.autoLaunchCountdown);
		nbt.setInteger("DestinationFrequency", this.destinationFrequency);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		if (nbt.hasKey("fuelTank"))
		{
			this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
		}

		if (this.getSizeInventory() > 0)
		{
			final NBTTagList var2 = nbt.getTagList("Items");
			this.cargoItems = new ItemStack[this.getSizeInventory()];

			for (int var3 = 0; var3 < var2.tagCount(); ++var3)
			{
				final NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
				final int var5 = var4.getByte("Slot") & 255;

				if (var5 >= 0 && var5 < this.cargoItems.length)
				{
					this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
				}
			}
		}

		if (nbt.getBoolean("TargetValid") && nbt.hasKey("targetTileX"))
		{
			this.targetVec = new Vector3(nbt.getDouble("targetTileX"), nbt.getDouble("targetTileY"), nbt.getDouble("targetTileZ"));
		}

		this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));
		this.landing = nbt.getBoolean("Landing");
		int autoLaunchValue = nbt.getInteger("AutoLaunchSetting");
		this.autoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
		this.autoLaunchCountdown = nbt.getInteger("TimeUntilAutoLaunch");
		this.destinationFrequency = nbt.getInteger("DestinationFrequency");
	}

	@Override
	public int addFuel(FluidStack liquid, boolean doFill)
	{
		if (liquid != null && FluidRegistry.getFluidName(liquid).equalsIgnoreCase("fuel"))
		{
			return this.fuelTank.fill(liquid, doFill);
		}

		return 0;
	}

	@Override
	public FluidStack removeFuel(int amount)
	{
		return this.fuelTank.drain(amount, true);
	}

	@Override
	public void setPad(IFuelDock pad)
	{
		this.landingPad = pad;
	}

	@Override
	public IFuelDock getLandingPad()
	{
		return this.landingPad;
	}

	@Override
	public int getMaxFuel()
	{
		return this.fuelTank.getCapacity();
	}

	@Override
	public boolean isDockValid(IFuelDock dock)
	{
		return dock instanceof GCCoreTileEntityLandingPad;
	}

	@Override
	public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
	{
		if (this.getSizeInventory() <= 3)
		{
			if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
			{
				this.autoLaunch();
			}

			return EnumCargoLoadingState.NOINVENTORY;
		}

		int count = 0;

		for (count = 0; count < this.cargoItems.length - 2; count++)
		{
			ItemStack stackAt = this.cargoItems[count];

			if (stackAt != null && stackAt.itemID == stack.itemID && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
			{
				if (doAdd)
				{
					this.cargoItems[count].stackSize += stack.stackSize;
				}

				return EnumCargoLoadingState.SUCCESS;
			}
		}

		for (count = 0; count < this.cargoItems.length - 2; count++)
		{
			ItemStack stackAt = this.cargoItems[count];

			if (stackAt == null)
			{
				if (doAdd)
				{
					this.cargoItems[count] = stack;
				}

				return EnumCargoLoadingState.SUCCESS;
			}
		}

		if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
		{
			this.autoLaunch();
		}

		return EnumCargoLoadingState.FULL;
	}

	@Override
	public RemovalResult removeCargo(boolean doRemove)
	{
		for (int i = 0; i < this.cargoItems.length - 2; i++)
		{
			ItemStack stackAt = this.cargoItems[i];

			if (stackAt != null)
			{
				if (doRemove && --this.cargoItems[i].stackSize <= 0)
				{
					this.cargoItems[i] = null;
				}

				return new RemovalResult(EnumCargoLoadingState.SUCCESS, new ItemStack(stackAt.itemID, 1, stackAt.getItemDamage()));
			}
		}

		if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_UNLOADED)
		{
			this.autoLaunch();
		}

		return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
	}

	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return this.cargoItems[par1];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.cargoItems[par1] != null)
		{
			ItemStack var3;

			if (this.cargoItems[par1].stackSize <= par2)
			{
				var3 = this.cargoItems[par1];
				this.cargoItems[par1] = null;
				return var3;
			}
			else
			{
				var3 = this.cargoItems[par1].splitStack(par2);

				if (this.cargoItems[par1].stackSize == 0)
				{
					this.cargoItems[par1] = null;
				}

				return var3;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.cargoItems[par1] != null)
		{
			final ItemStack var2 = this.cargoItems[par1];
			this.cargoItems[par1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.cargoItems[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return StatCollector.translateToLocal("container.spaceship.name");
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return this.isDead ? false : entityplayer.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public void onPadDestroyed()
	{
		if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.getPhase())
		{
			this.dropShipAsItem();
			this.setDead();
		}
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
	{
		if (this.cargoItems != null)
		{
			for (final ItemStack item : this.cargoItems)
			{
				if (item != null)
				{
					droppedItemList.add(item);
				}
			}
		}

		return droppedItemList;
	}

	public boolean getWaitForPlayer()
	{
		return this.waitForPlayer;
	}

	public void setWaitForPlayer(boolean waitForPlayer)
	{
		this.waitForPlayer = waitForPlayer;
	}

	public static enum EnumAutoLaunch
	{
		CARGO_IS_UNLOADED(0, "Cargo is Unloaded"),
		CARGO_IS_FULL(1, "Cargo is Full"),
		ROCKET_IS_FUELED(2, "Fully Fueled"),
		INSTANT(3, "Instantly"),
		TIME_10_SECONDS(4, "10 Seconds"),
		TIME_30_SECONDS(5, "30 Seconds"),
		TIME_1_MINUTE(6, "1 Minute"),
		REDSTONE_SIGNAL(7, "Redstone Signal");

		private final int index;
		private String title;

		private EnumAutoLaunch(int index, String title)
		{
			this.index = index;
			this.title = title;
		}

		public int getIndex()
		{
			return this.index;
		}

		public String getTitle()
		{
			return this.title;
		}
	}
}
