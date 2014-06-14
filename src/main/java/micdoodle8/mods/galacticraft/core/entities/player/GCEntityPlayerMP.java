package micdoodle8.mods.galacticraft.core.entities.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.event.EventWakePlayer;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;



public class GCEntityPlayerMP extends EntityPlayerMP
{
    public GCEntityPlayerMP(MinecraftServer server, WorldServer world, GameProfile profile, ItemInWorldManager itemInWorldManager)
	{
		super(server, world, profile, itemInWorldManager);
	}

	@Override
	public void clonePlayer(EntityPlayer oldPlayer, boolean keepInv)
	{
		super.clonePlayer(oldPlayer, keepInv);

		if (oldPlayer instanceof GCEntityPlayerMP)
		{
			this.getPlayerStats().copyFrom(((GCEntityPlayerMP) oldPlayer).getPlayerStats(), keepInv || this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"));
		}
	}

	@Override
    public void moveEntity(double par1, double par3, double par5)
    {
    	super.moveEntity(par1, par3, par5);
    	this.updateFeet(par1, par5);
    }

	private void updateFeet(double motionX, double motionZ)
	{
		double motionSqrd = (motionX * motionX + motionZ * motionZ);
		
		// If the player is on the moon, not airbourne and not riding anything
		if (this.worldObj.provider instanceof WorldProviderMoon && motionSqrd > 0.001D && this.ridingEntity == null)
		{
			int iPosX = MathHelper.floor_double(this.posX);
			int iPosY = MathHelper.floor_double(this.posY)-1;
			int iPosZ = MathHelper.floor_double(this.posZ);
			
			// If the block below is the moon block
			if (this.worldObj.getBlock(iPosX, iPosY, iPosZ) == GCBlocks.blockMoon)
			{
				// And is the correct metadata (moon turf)
				if (this.worldObj.getBlockMetadata(iPosX, iPosY, iPosZ) == 5)
				{
					// If it has been long enough since the last step
					if (this.getPlayerStats().distanceSinceLastStep > 0.35D)
					{
						Vector3 pos = new Vector3(this);
						// Set the footprint position to the block below and add random number to stop z-fighting
						pos.y = MathHelper.floor_double(this.posY - 1D) + this.rand.nextFloat() / 100.0F;
						
						// Adjust footprint to left or right depending on step count
						switch (this.getPlayerStats().lastStep)
						{
						case 0:
							float a = (-this.rotationYaw + 90F)/57.295779513F;
							pos.translate(new Vector3(MathHelper.sin(a) * 0.25F, 0, MathHelper.cos(a) * 0.25F));
							break;
						case 1:
							a = (-this.rotationYaw - 90F)/57.295779513F;
							pos.translate(new Vector3(MathHelper.sin(a) * 0.25, 0, MathHelper.cos(a) * 0.25));
							break;
						}
						
						TickHandlerServer.addFootprint(new Footprint(pos, this.rotationYaw), this.worldObj.provider.dimensionId);
						
						// Increment and cap step counter at 1
						this.getPlayerStats().lastStep++;
						this.getPlayerStats().lastStep %= 2;
						this.getPlayerStats().distanceSinceLastStep = 0;
					}
					else
					{
						this.getPlayerStats().distanceSinceLastStep += motionSqrd;
					}
				}
			}
		}
	}

	protected void checkCurrentItem()
	{
		ItemStack theCurrentItem = this.inventory.getCurrentItem();
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(Blocks.torch))
			{
				final ItemStack stack = new ItemStack(GCBlocks.unlitTorch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
		else if (!(this.worldObj.provider instanceof IGalacticraftWorldProvider) && theCurrentItem != null)
		{
			final int var1 = theCurrentItem.stackSize;
			final int var2 = theCurrentItem.getItemDamage();

			if (this.inventory.getCurrentItem().getItem() == Item.getItemFromBlock(GCBlocks.unlitTorch))
			{
				final ItemStack stack = new ItemStack(Blocks.torch, var1, 0);
				this.inventory.mainInventory[this.inventory.currentItem] = stack;
			}
		}
	}

	protected void sendPlanetList()
	{
		HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(WorldUtil.getPossibleDimensionsForSpaceshipTier(this.getPlayerStats().spaceshipTier), this);

		String temp = "";
		int count = 0;

		for (Entry<String, Integer> entry : map.entrySet())
		{
			temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "." : ""));
			count++;
		}

		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, new Object[] { this.getGameProfile().getName(), temp }), this);
	}

	protected void checkGear()
	{
		this.getPlayerStats().maskInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(0);
		this.getPlayerStats().gearInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(1);
		this.getPlayerStats().tankInSlot1 = this.getPlayerStats().extendedInventory.getStackInSlot(2);
		this.getPlayerStats().tankInSlot2 = this.getPlayerStats().extendedInventory.getStackInSlot(3);
		this.getPlayerStats().parachuteInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(4);
		this.getPlayerStats().frequencyModuleInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(5);

		//

		if (this.getPlayerStats().frequencyModuleInSlot != this.getPlayerStats().lastFrequencyModuleInSlot)
		{
			if (this.getPlayerStats().frequencyModuleInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.REMOVE_FREQUENCY_MODULE.getIndex());
			else if (this.getPlayerStats().frequencyModuleInSlot.getItem() == GCItems.basicItem && this.getPlayerStats().frequencyModuleInSlot.getItemDamage() == 19 && this.getPlayerStats().lastFrequencyModuleInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.ADD_FREQUENCY_MODULE.getIndex());

			this.getPlayerStats().lastFrequencyModuleInSlot = this.getPlayerStats().frequencyModuleInSlot;
		}

		//

		if (this.getPlayerStats().maskInSlot != this.getPlayerStats().lastMaskInSlot)
		{
			if (this.getPlayerStats().maskInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.REMOVEMASK.getIndex());
			else if (this.getPlayerStats().maskInSlot.getItem() == GCItems.oxMask && this.getPlayerStats().lastMaskInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.ADDMASK.getIndex());
			
			this.getPlayerStats().lastMaskInSlot = this.getPlayerStats().maskInSlot;
		}

		//

		if (this.getPlayerStats().gearInSlot != this.getPlayerStats().lastGearInSlot)
		{
			if (this.getPlayerStats().gearInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.REMOVEGEAR.getIndex());
			else if (this.getPlayerStats().gearInSlot.getItem() == GCItems.oxygenGear && this.getPlayerStats().lastGearInSlot == null)
				this.sendGearUpdatePacket(EnumModelPacket.ADDGEAR.getIndex());
			
			this.getPlayerStats().lastGearInSlot = this.getPlayerStats().gearInSlot;
		}

		//

		if (this.getPlayerStats().tankInSlot1 != this.getPlayerStats().lastTankInSlot1)
		{
			if (this.getPlayerStats().tankInSlot1 == null) 
				this.sendGearUpdatePacket(EnumModelPacket.REMOVE_LEFT_TANK.getIndex());
	
			else if (this.getPlayerStats().lastTankInSlot1 == null)
			{
				if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK.getIndex());
				}
			}
			//if the else is reached then both tankInSlot and lastTankInSlot are non-null
			else if (this.getPlayerStats().tankInSlot1.getItem() != this.getPlayerStats().lastTankInSlot1.getItem())
			{
				if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTGREENTANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTORANGETANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot1.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDLEFTREDTANK.getIndex());
				}
			}
			
			this.getPlayerStats().lastTankInSlot1 = this.getPlayerStats().tankInSlot1;
		}

		//

		if (this.getPlayerStats().tankInSlot2 != this.getPlayerStats().lastTankInSlot2)
		{
			if (this.getPlayerStats().tankInSlot2 == null) 
				this.sendGearUpdatePacket(EnumModelPacket.REMOVE_RIGHT_TANK.getIndex());
	
			else if (this.getPlayerStats().lastTankInSlot2 == null)
			{
				if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK.getIndex());
				}
			}
			//if the else is reached then both tankInSlot and lastTankInSlot are non-null
			else if (this.getPlayerStats().tankInSlot2.getItem() != this.getPlayerStats().lastTankInSlot2.getItem())
			{
				if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankLight)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTGREENTANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankMedium)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTORANGETANK.getIndex());
				}
				else if (this.getPlayerStats().tankInSlot2.getItem() == GCItems.oxTankHeavy)
				{
					this.sendGearUpdatePacket(EnumModelPacket.ADDRIGHTREDTANK.getIndex());
				}
			}
			
			this.getPlayerStats().lastTankInSlot2 = this.getPlayerStats().tankInSlot2;
		}

		if (this.getPlayerStats().parachuteInSlot != this.getPlayerStats().lastParachuteInSlot)
		{
			if (this.getPlayerStats().parachuteInSlot == null)
			{
				if (this.getPlayerStats().usingParachute) this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
			}
			else if (this.getPlayerStats().lastParachuteInSlot == null)
			{
				if (this.getPlayerStats().usingParachute) this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex());
			}
			else if (this.getPlayerStats().parachuteInSlot.getItemDamage() != this.getPlayerStats().lastParachuteInSlot.getItemDamage())
			{
				this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex());
			}
			
			this.getPlayerStats().lastParachuteInSlot = this.getPlayerStats().parachuteInSlot;
		}
	}

	protected void checkOxygen()
	{
		final ItemStack tankInSlot = this.getPlayerStats().extendedInventory.getStackInSlot(2);
		final ItemStack tankInSlot2 = this.getPlayerStats().extendedInventory.getStackInSlot(3);

		final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider) this.worldObj.provider).hasBreathableAtmosphere() && !this.capabilities.isCreativeMode)
		{
			if (tankInSlot == null)
			{
				this.getPlayerStats().airRemaining = 0;
			}

			if (tankInSlot2 == null)
			{
				this.getPlayerStats().airRemaining2 = 0;
			}

			if (drainSpacing > 0)
			{
				if ((this.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(this) && !this.getPlayerStats().usingPlanetSelectionGui)
				{
					if (tankInSlot != null && tankInSlot.getMaxDamage() - tankInSlot.getItemDamage() > 0)
					{
						tankInSlot.damageItem(1, this);
					}

					if (tankInSlot2 != null && tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage() > 0)
					{
						tankInSlot2.damageItem(1, this);
					}
				}

				if (tankInSlot != null)
				{
					this.getPlayerStats().airRemaining = tankInSlot.getMaxDamage() - tankInSlot.getItemDamage();
				}

				if (tankInSlot2 != null)
				{
					this.getPlayerStats().airRemaining2 = tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage();
				}
			}
			else
			{
				if ((this.ticksExisted - 1) % 60 == 0)
				{
					if (OxygenUtil.isAABBInBreathableAirBlock(this))
					{
						if (this.getPlayerStats().airRemaining < 90 && tankInSlot != null)
						{
							this.getPlayerStats().airRemaining = Math.min(this.getPlayerStats().airRemaining + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
						}

						if (this.getPlayerStats().airRemaining2 < 90 && tankInSlot2 != null)
						{
							this.getPlayerStats().airRemaining2 = Math.min(this.getPlayerStats().airRemaining2 + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
						}
					}
					else
					{
						if (this.getPlayerStats().airRemaining > 0)
						{
							this.getPlayerStats().airRemaining = Math.max(this.getPlayerStats().airRemaining - 1, 0);
						}

						if (this.getPlayerStats().airRemaining2 > 0)
						{
							this.getPlayerStats().airRemaining2 = Math.max(this.getPlayerStats().airRemaining2 - 1, 0);
						}
					}
				}
			}

			final boolean airEmpty = this.getPlayerStats().airRemaining <= 0 && this.getPlayerStats().airRemaining2 <= 0;

			if (this.isOnLadder())
			{
				this.getPlayerStats().oxygenSetupValid = this.getPlayerStats().lastOxygenSetupValid;
			}
			else if ((!OxygenUtil.hasValidOxygenSetup(this) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(this))
			{
				this.getPlayerStats().oxygenSetupValid = false;
			}
			else
			{
				this.getPlayerStats().oxygenSetupValid = true;
			}

			if (!this.getPlayerStats().oxygenSetupValid && !this.worldObj.isRemote && this.isEntityAlive())
			{
				if (this.getPlayerStats().damageCounter == 0)
				{
					this.getPlayerStats().damageCounter = ConfigManagerCore.suffocationCooldown;

					GCCoreOxygenSuffocationEvent suffocationEvent = new GCCoreOxygenSuffocationEvent.Pre(this);
					MinecraftForge.EVENT_BUS.post(suffocationEvent);

					if (!suffocationEvent.isCanceled())
					{
						this.attackEntityFrom(DamageSourceGC.oxygenSuffocation, ConfigManagerCore.suffocationDamage);

						GCCoreOxygenSuffocationEvent suffocationEventPost = new GCCoreOxygenSuffocationEvent.Post(this);
						MinecraftForge.EVENT_BUS.post(suffocationEventPost);
					}
				}
			}
		}
		else if ((this.ticksExisted - 1) % 20 == 0 && !this.capabilities.isCreativeMode && this.getPlayerStats().airRemaining < 90)
		{
			this.getPlayerStats().airRemaining += 1;
			this.getPlayerStats().airRemaining2 += 1;
		}
		else if (this.capabilities.isCreativeMode)
		{
			this.getPlayerStats().airRemaining = 90;
			this.getPlayerStats().airRemaining2 = 90;
		}
		else
		{
			this.getPlayerStats().oxygenSetupValid = true;
		}
	}

	protected void throwMeteors()
	{
		if (this.worldObj.provider instanceof IGalacticraftWorldProvider && FMLCommonHandler.instance().getEffectiveSide() != Side.CLIENT)
		{
			if (((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency() > 0)
			{
				final int f = (int) (((IGalacticraftWorldProvider) this.worldObj.provider).getMeteorFrequency() * 1000D);

				if (this.worldObj.rand.nextInt(f) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}

				if (this.worldObj.rand.nextInt(f * 3) == 0)
				{
					final EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 100);

					if (closestPlayer == null || closestPlayer.getEntityId() <= this.getEntityId())
					{
						int x, y, z;
						double motX, motZ;
						x = this.worldObj.rand.nextInt(20) - 10;
						y = this.worldObj.rand.nextInt(20) + 200;
						z = this.worldObj.rand.nextInt(20) - 10;
						motX = this.worldObj.rand.nextDouble() * 5;
						motZ = this.worldObj.rand.nextDouble() * 5;

						final EntityMeteor meteor = new EntityMeteor(this.worldObj, this.posX + x, this.posY + y, this.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

						if (!this.worldObj.isRemote)
						{
							this.worldObj.spawnEntityInWorld(meteor);
						}
					}
				}
			}
		}
	}

	protected void updateSchematics()
	{
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(0));
		SchematicRegistry.addUnlockedPage(this, SchematicRegistry.getMatchingRecipeForID(Integer.MAX_VALUE));

		Collections.sort(this.getPlayerStats().unlockedSchematics);

		if (this.playerNetServerHandler != null && (this.getPlayerStats().unlockedSchematics.size() != this.getPlayerStats().lastUnlockedSchematics.size() || (this.ticksExisted - 1) % 100 == 0))
		{
			Integer[] iArray = new Integer[this.getPlayerStats().unlockedSchematics.size()];

			for (int i = 0; i < iArray.length; i++)
			{
				ISchematicPage page = this.getPlayerStats().unlockedSchematics.get(i);
				iArray[i] = page == null ? -2 : page.getPageID();
			}

			List<Object> objList = new ArrayList<Object>();
			objList.add(iArray);

			GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SCHEMATIC_LIST, objList), this);
		}
	}

	protected void sendAirRemainingPacket()
	{
		final float f1 = Float.valueOf(this.getPlayerStats().tankInSlot1 == null ? 0.0F : this.getPlayerStats().tankInSlot1.getMaxDamage() / 90.0F);
		final float f2 = Float.valueOf(this.getPlayerStats().tankInSlot2 == null ? 0.0F : this.getPlayerStats().tankInSlot2.getMaxDamage() / 90.0F);
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, new Object[] { MathHelper.floor_float(this.getPlayerStats().airRemaining / f1), MathHelper.floor_float(this.getPlayerStats().airRemaining2 / f2), this.getGameProfile().getName() }), this);
	}

	protected void sendGearUpdatePacket(int gearType)
	{
		this.sendGearUpdatePacket(gearType, -1);
	}

	private void sendGearUpdatePacket(int gearType, int subtype)
	{
		if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(this.getGameProfile().getName()) != null)
		{
			GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, new Object[] { this.getGameProfile().getName(), gearType, subtype }), new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, 50.0D));
		}
	}

	@Override
	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3)
	{
		this.wakeUpPlayer(par1, par2, par3, false);
	}

	public void wakeUpPlayer(boolean par1, boolean par2, boolean par3, boolean bypass)
	{
		ChunkCoordinates c = this.playerLocation;

		if (c != null)
		{
			EventWakePlayer event = new EventWakePlayer(this, c.posX, c.posY, c.posZ, par1, par2, par3, bypass);
			MinecraftForge.EVENT_BUS.post(event);

			if (bypass || event.result == null || event.result == EnumStatus.OK)
			{
				super.wakeUpPlayer(par1, par2, par3);
			}
		}
	}

	public void setUsingParachute(boolean tf)
	{
		this.getPlayerStats().usingParachute = tf;

		if (tf)
		{
			int subtype = -1;

			if (this.getPlayerStats().parachuteInSlot != null)
			{
				subtype = this.getPlayerStats().parachuteInSlot.getItemDamage();
			}

			this.sendGearUpdatePacket(EnumModelPacket.ADD_PARACHUTE.getIndex(), subtype);
		}
		else
		{
			this.sendGearUpdatePacket(EnumModelPacket.REMOVE_PARACHUTE.getIndex());
		}
	}

	public final GCPlayerStats getPlayerStats()
	{
		return GCPlayerStats.get(this);
	}

	public static enum EnumModelPacket
	{
		ADDMASK(0),
		REMOVEMASK(1),
		ADDGEAR(2),
		REMOVEGEAR(3),
		ADDLEFTREDTANK(4),
		ADDLEFTORANGETANK(5),
		ADDLEFTGREENTANK(6),
		REMOVE_LEFT_TANK(7),
		ADDRIGHTREDTANK(8),
		ADDRIGHTORANGETANK(9),
		ADDRIGHTGREENTANK(10),
		REMOVE_RIGHT_TANK(11),
		ADD_PARACHUTE(12),
		REMOVE_PARACHUTE(13),
		ADD_FREQUENCY_MODULE(14),
		REMOVE_FREQUENCY_MODULE(15);

		private int index;

		private EnumModelPacket(int index)
		{
			this.index = index;
		}

		int getIndex()
		{
			return this.index;
		}
	}
}
