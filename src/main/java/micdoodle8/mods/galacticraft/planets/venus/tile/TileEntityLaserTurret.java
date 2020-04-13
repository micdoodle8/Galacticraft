package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineTiered;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.venus.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockLaserTurret;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.miccore.Annotations.NetworkedField;

import java.util.*;

public class TileEntityLaserTurret extends TileBaseElectricBlockWithInventory implements IMultiBlock, ISidedInventory, IMachineSides
{
    private final float RANGE = 15.0F;
    private final float METEOR_RANGE = 90.0F;
    private List<Entity> tracked = Lists.newArrayList();
    private List<String> players = Lists.newArrayList(); // Can be whitelist or blacklist
    private List<ResourceLocation> entities = Lists.newArrayList(); // Can be whitelist or blacklist
    private boolean initialisedMulti = false;
    private AxisAlignedBB renderAABB;

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean active = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int targettedEntity = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int chargeLevel = 0;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean blacklistMode = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetMeteors = true;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean alwaysIgnoreSpaceRace = true;
    @NetworkedField(targetSide = Side.CLIENT)
    public int priorityClosest = 1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int priorityLowestHealth = 2;
    @NetworkedField(targetSide = Side.CLIENT)
    public int priorityHighestHealth = 3;


    private UUID ownerUUID = null;
    private String ownerName = null;
    private SpaceRace ownerSpaceRace = null;

    @SideOnly(Side.CLIENT)
    public float pitch;
    @SideOnly(Side.CLIENT)
    public float yaw;
    @SideOnly(Side.CLIENT)
    public float targetPitch;
    @SideOnly(Side.CLIENT)
    public float targetYaw;
    public int timeSinceShot = -1;  //Cannot initialise client-only fields (causes a server crash on constructing the object)

    public TileEntityLaserTurret()
    {
        super("tile.laser_turret.name");
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void validate()
    {
        super.validate();

        this.setOwnerUUID(this.ownerUUID);
    }

    public void setOwnerSpaceRace(SpaceRace spaceRace)
    {
        this.ownerSpaceRace = spaceRace;
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.world.isRemote)
        {
            networkedList.add(players.size());
            networkedList.addAll(players);
            networkedList.add(entities.size());
            for (ResourceLocation res : entities)
            {
                networkedList.add(res.toString());
            }
            networkedList.add(ownerUUID != null);
            if (ownerUUID != null)
            {
                networkedList.add(ownerUUID.getMostSignificantBits());
                networkedList.add(ownerUUID.getLeastSignificantBits());
            }
        }
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.world.isRemote)
        {
            players.clear();
            int playerSize = dataStream.readInt();
            for (int i = 0; i < playerSize; ++i)
            {
                players.add(ByteBufUtils.readUTF8String(dataStream));
            }
            entities.clear();
            int entitySize = dataStream.readInt();
            for (int i = 0; i < entitySize; ++i)
            {
                entities.add(new ResourceLocation(ByteBufUtils.readUTF8String(dataStream)));
            }
            if (dataStream.readBoolean())
            {
                ownerUUID = new UUID(dataStream.readLong(), dataStream.readLong());
            }
        }
    }

    public void addPlayer(String player)
    {
        players.add(player);
    }

    public void addEntity(ResourceLocation entity)
    {
        entities.add(entity);
    }

    public void removePlayer(String player)
    {
        for (int i = 0; i < players.size(); ++i)
        {
            if (players.get(i).equals(player))
            {
                players.remove(i--);
            }
        }
    }

    public void removeEntity(ResourceLocation entity)
    {
        for (int i = 0; i < entities.size(); ++i)
        {
            if (entities.get(i).equals(entity))
            {
                entities.remove(i--);
            }
        }
    }

    public List<String> getPlayers()
    {
        return players;
    }

    public List<ResourceLocation> getEntities()
    {
        return entities;
    }

    @Override
    public double getPacketRange()
    {
        return 25.0D;
    }

    public void trackEntity(Entity entity)
    {
        if (!tracked.contains(entity))
            tracked.add(entity);
    }

    private Entity updateTarget()
    {
        List<EntityEntrySortable> list = Lists.newArrayList();
        if (storage.getEnergyStoredGC() > 1000 && !this.getDisabled(0) && !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
        {
            for (int i = 0; i < tracked.size(); ++i)
            {
                Entity e = tracked.get(i);
                if (e.isDead)
                {
                    tracked.remove(i--);
                }
                else
                {
                    boolean shouldTarget = !this.blacklistMode;
                    if (e instanceof EntityPlayer)
                    {
                        EntityPlayer toTargetPlayer = (EntityPlayer) e;
                        if (this.alwaysIgnoreSpaceRace && (toTargetPlayer.getUniqueID().equals(this.ownerUUID) || this.ownerSpaceRace != null && this.ownerSpaceRace.getPlayerNames().contains(toTargetPlayer.getName())))
                        {
                            shouldTarget = false;
                        }
                        else
                        {
                            for (String player : players)
                            {
                                if (player.equalsIgnoreCase(e.getName()))
                                {
                                    shouldTarget = !shouldTarget;
                                }
                            }
                        }
                    }
                    else if(e instanceof EntityTameable && ((EntityTameable) e).getOwnerId() != null)
                    {
                        if ((((EntityTameable) e).getOwnerId().equals(this.ownerUUID)) || (this.alwaysIgnoreSpaceRace && this.ownerSpaceRace != null && ((EntityTameable) e).getOwner() != null && this.ownerSpaceRace.getPlayerNames().contains(((EntityTameable) e).getOwner().getName()))) {
                            shouldTarget = false;
                        }
                    }
                    else
                    {
                        ResourceLocation location = EntityList.getKey(e.getClass());
                        if (location != null)
                        {
                            for (ResourceLocation entity : entities)
                            {
                                if (location.equals(entity))
                                {
                                    shouldTarget = !shouldTarget;
                                }
                            }
                        }
                    }
                    if (shouldTarget)
                    {
                        Vector3 vec = new Vector3(e.posX, e.posY + e.getEyeHeight(), e.posZ);
                        vec.translate(new Vector3(-(pos.getX() + 0.5F), -(pos.getY() + 1.78F), -(pos.getZ() + 0.5F)));
                        Vector3 vecNoHeight = vec.clone();
                        vecNoHeight.y = 0;
                        // Make sure target is within range and not directly below turret:
                        if ((vec.getMagnitudeSquared() < RANGE * RANGE || (targetMeteors && e instanceof EntityMeteor && vecNoHeight.getMagnitudeSquared() < METEOR_RANGE * METEOR_RANGE)) && Math.asin(vec.clone().normalize().y) > -Math.PI / 3.0)
                        {
                            if (e instanceof EntityLivingBase)
                            {
                                list.add(new EntityEntrySortable((EntityLivingBase)e, vec.getMagnitude()));
                            }
                            else if (targetMeteors && e instanceof EntityMeteor)
                            {
                                return e;
                            }
                        }
                    }
                }
            }
        }

        list.sort((o1, o2) ->
        {
            if (priorityClosest < priorityHighestHealth && priorityClosest < priorityLowestHealth)
            {
                if (priorityLowestHealth < priorityHighestHealth)
                {
                    return ComparisonChain.start().compare(o1.distance, o2.distance).compare(o1.entity.getHealth(), o2.entity.getHealth()).result();
                }
                else
                {
                    return ComparisonChain.start().compare(o1.distance, o2.distance).compare(o2.entity.getHealth(), o1.entity.getHealth()).result();
                }
            }
            else if (priorityHighestHealth < priorityLowestHealth && priorityHighestHealth < priorityClosest)
            {
                return ComparisonChain.start().compare(o2.entity.getHealth(), o1.entity.getHealth()).compare(o1.distance, o2.distance).result();
            }
            else if (priorityLowestHealth < priorityHighestHealth && priorityLowestHealth < priorityClosest)
            {
                return ComparisonChain.start().compare(o1.entity.getHealth(), o2.entity.getHealth()).compare(o1.distance, o2.distance).result();
            }
            return 0;
        });

        for (EntityEntrySortable entry : list)
        {
            Entity entity = entry.entity;
            Vec3d start = new Vec3d(pos.getX() + 0.5F, pos.getY() + 1.78F, pos.getZ() + 0.5F);
            Vec3d end = new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
            start = start.add(end.add(start.scale(-1)).normalize()); // Start at block in front of laser facing direction

            RayTraceResult res = this.world.rayTraceBlocks(start, end, false, true, true);
            if (res == null || res.typeOfHit != RayTraceResult.Type.BLOCK)
            {
                return entity;
            }
        }

        return null;
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.initialisedMulti)
        {
            this.initialisedMulti = this.initialiseMultiTiles(this.getPos(), this.world);
        }

        if (!world.isRemote)
        {
            if (storage.getEnergyStoredGC() > 1000 && !this.getDisabled(0) && !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
            {
                if (chargeLevel < 60)
                {
                    chargeLevel++;
                }
            }
            else
            {
                this.chargeLevel = 0;
            }

            if (ticks % 20 == 0)
            {
                if (storage.getEnergyStoredGC() > 1000 && !this.getDisabled(0) && !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
                {
                    for (Entity e : world.loadedEntityList)
                    {
                        if (e instanceof EntityLivingBase || e instanceof ILaserTrackableFast)
                        {
                            this.trackEntity(e);
                        }
                    }
                }
                else
                {
                    this.tracked.clear();
                }

                Entity toTarget = updateTarget();

                if (toTarget != null && this.chargeLevel > 0)
                {
                    active = true;
                    targettedEntity = toTarget.getEntityId();
                }
                else
                {
                    active = false;
                    targettedEntity = -1;
                }
            }

            if (chargeLevel >= 60 && targettedEntity != -1)
            {
                Entity toTarget = this.world.getEntityByID(this.targettedEntity);
                if (toTarget != null)
                {
                    if (toTarget instanceof EntityLivingBase)
                    {
                        EntityLivingBase entityLiving = (EntityLivingBase) toTarget;
                        entityLiving.attackEntityFrom(DamageSourceGC.laserTurret, 1.5F);
                    }
                    else if (toTarget instanceof EntityMeteor)
                    {
                        toTarget.setDead();
                    }
                    this.world.playSound(null, getPos().up(), GCSounds.laserShoot, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    storage.setEnergyStored(storage.getEnergyStoredGC() - 1000);
                    chargeLevel = 0;
                }
            }
            else if (chargeLevel == 22)
            {
                this.world.playSound(null, getPos().up(), GCSounds.laserCharge, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
        else
        {
            // Client side only
            if (chargeLevel > 0 && chargeLevel < 60)
            {
                chargeLevel++;
            }

            if (chargeLevel < 5)
            {
                timeSinceShot = 0;
            }

            timeSinceShot++;

            if (active && targettedEntity != -1)
            {
                Entity entity = world.getEntityByID(targettedEntity);
                if (entity != null && !entity.isDead)
                {
                    Vector3 vec = new Vector3(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
                    vec.translate(new Vector3(-(pos.getX() + 0.5F), -(pos.getY() + 1.78F), -(pos.getZ() + 0.5F))).normalize();
                    targetPitch = (float) (Math.asin(vec.y) * (180.0F / Math.PI));
                    targetYaw = (float) (Math.atan2(vec.x, vec.z) * (180.0F / Math.PI)) + 90.0F;

                    while (targetYaw > 360.0F)
                    {
                        targetYaw -= 360.0F;
                    }

                    while (targetYaw < 0.0F)
                    {
                        targetYaw += 360.0F;
                    }

                    if (targetPitch > 90.0F)
                    {
                        targetPitch = 90.0F;
                    }
                    else if (targetPitch < -60.0F)
                    {
                        targetPitch = -60.0F;
                    }
                }
            }
            else
            {
                targetPitch = -45.0F;
            }

            float diffY = targetYaw - yaw;
            if (targetYaw > 270.0F && yaw < 90.0F)
            {
                yaw = yaw + (targetYaw - (yaw + 360.0F)) / 3.0F;
            }
            else if (targetYaw < 90.0F && yaw > 270.0F)
            {
                yaw = yaw + ((targetYaw + 360.0F) - yaw) / 3.0F;
            }
            else
            {
                yaw = yaw + diffY / 10.0F;
            }

            float diffP = targetPitch - pitch;
            pitch += diffP / 3.0F;

            while (yaw > 360.0F)
            {
                yaw -= 360.0F;
            }

            while (yaw < 0.0F)
            {
                yaw += 360.0F;
            }

            if (pitch > 90.0F)
            {
                pitch = 90.0F;
            }
            else if (pitch < -60.0F)
            {
                pitch = -60.0F;
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides

        NBTTagList playersTag = nbt.getTagList("PlayerList", 10);
        for (int i = 0; i < playersTag.tagCount(); i++)
        {
            NBTTagCompound tagAt = playersTag.getCompoundTagAt(i);
            this.players.add(tagAt.getString("PlayerName"));
        }

        NBTTagList entitiesTag = nbt.getTagList("EntitiesList", 10);
        for (int i = 0; i < entitiesTag.tagCount(); i++)
        {
            NBTTagCompound tagAt = entitiesTag.getCompoundTagAt(i);
            this.entities.add(new ResourceLocation(tagAt.getString("EntityRes")));
        }

        this.active = nbt.getBoolean("active");
        this.targettedEntity = nbt.getInteger("targettedEntity");
        this.chargeLevel = nbt.getInteger("chargeLevel");
        this.blacklistMode = nbt.getBoolean("blacklistMode");
        this.targetMeteors = nbt.getBoolean("targetMeteors");
        this.alwaysIgnoreSpaceRace = nbt.getBoolean("alwaysIgnoreSpaceRace");
        this.priorityClosest = nbt.getInteger("priorityClosest");
        this.priorityLowestHealth = nbt.getInteger("priorityLowestHealth");
        this.priorityHighestHealth = nbt.getInteger("priorityHighestHealth");

        this.ownerName = nbt.hasKey("ownerName") ? nbt.getString("ownerName") : null;
        this.ownerUUID = nbt.getUniqueId("ownerUUID");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        NBTTagList playersTag = new NBTTagList();
        for (String player : this.players)
        {
            NBTTagCompound tagComp = new NBTTagCompound();
            tagComp.setString("PlayerName", player);
            playersTag.appendTag(tagComp);
        }

        nbt.setTag("PlayerList", playersTag);

        NBTTagList entitiesTag = new NBTTagList();
        for (ResourceLocation entity : this.entities)
        {
            NBTTagCompound tagComp = new NBTTagCompound();
            tagComp.setString("EntityRes", entity.toString());
            entitiesTag.appendTag(tagComp);
        }

        nbt.setTag("EntitiesList", entitiesTag);

        nbt.setBoolean("active", this.active);
        nbt.setInteger("targettedEntity", this.targettedEntity);
        nbt.setInteger("chargeLevel", this.chargeLevel);
        nbt.setBoolean("blacklistMode", this.blacklistMode);
        nbt.setBoolean("targetMeteors", this.targetMeteors);
        nbt.setBoolean("alwaysIgnoreSpaceRace", this.alwaysIgnoreSpaceRace);
        nbt.setInteger("priorityClosest", this.priorityClosest);
        nbt.setInteger("priorityLowestHealth", this.priorityLowestHealth);
        nbt.setInteger("priorityHighestHealth", this.priorityHighestHealth);

        if (this.ownerName != null)
        {
            nbt.setString("ownerName", this.ownerName);
        }
        nbt.setUniqueId("ownerUUID", this.ownerUUID);

        return nbt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 2, getPos().getZ() + 1);
        }
        return this.renderAABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockLaserTurret)
        {
            return state.getValue(BlockLaserTurret.FACING);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return EnumFacing.UP;
        case BOTTOM:
            return EnumFacing.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    public void setOwnerUUID(UUID uniqueID)
    {
        this.ownerUUID = uniqueID;
        if (uniqueID != null)
        {
            EntityPlayer player = this.world.getPlayerEntityByUUID(uniqueID);
            if (player != null)
            {
                this.ownerName = player.getName();
            }
        }
        for (SpaceRace race : SpaceRaceManager.getSpaceRaces())
        {
            if (this.ownerName != null && race.getPlayerNames().contains(this.ownerName))
            {
                this.setOwnerSpaceRace(race);
            }
        }
    }

    public UUID getOwnerUUID()
    {
        return this.ownerUUID;
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        entityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(placedPosition, positions);
        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(world, positions, placedPosition, this.getMultiType());
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock)
            {
                BlockMulti.EnumBlockMultiType type = stateAt.getValue(BlockMulti.MULTI_TYPE);
                if (type == BlockMulti.EnumBlockMultiType.LASER_TURRET)
                {
                    if (this.world.isRemote)
                    {
                        FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, VenusBlocks.laserTurret.getDefaultState());
                    }

                    this.world.setBlockToAir(pos);
                }
            }
        }

        this.world.destroyBlock(getPos(), true);
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.world.getHeight() - 1;
        int y = placedPosition.getY() + 1;
        if (y > buildHeight)
        {
            return;
        }
        positions.add(new BlockPos(placedPosition.getX(), y, placedPosition.getZ()));
    }

    @Override
    public BlockMulti.EnumBlockMultiType getMultiType()
    {
        return BlockMulti.EnumBlockMultiType.LASER_TURRET;
    }

    protected boolean initialiseMultiTiles(BlockPos pos, World world)
    {
        //Client can create its own fake blocks and tiles - no need for networking in 1.8+
        if (world.isRemote) this.onCreate(world, pos);

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityMulti)
            {
                ((TileEntityMulti) tile).mainBlockPosition = pos;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[] { MachineSide.ELECTRIC_IN };
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[] { Face.LEFT };
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (machineSides == null)
        {
            this.initialiseSides();
        }

        return machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return BlockMachineTiered.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation

    private class EntityEntrySortable
    {
        private EntityLivingBase entity;
        private double distance;

        public EntityEntrySortable(EntityLivingBase entity, double distance)
        {
            this.entity = entity;
            this.distance = distance;
        }

        public EntityLivingBase getEntity()
        {
            return entity;
        }

        public void setEntity(EntityLivingBase entity)
        {
            this.entity = entity;
        }

        public double getDistance()
        {
            return distance;
        }

        public void setDistance(double distance)
        {
            this.distance = distance;
        }
    }
}
