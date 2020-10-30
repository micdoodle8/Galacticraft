package micdoodle8.mods.galacticraft.planets.venus.tile;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.entity.ILaserTrackableFast;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Annotations;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.client.sounds.GCSounds;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRace;
import micdoodle8.mods.galacticraft.core.dimension.SpaceRaceManager;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.entities.EntityMeteor;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFake;
import micdoodle8.mods.galacticraft.core.util.DamageSourceGC;
import micdoodle8.mods.galacticraft.core.util.RedstoneUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.GuiIdsPlanets;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlockNames;
import micdoodle8.mods.galacticraft.planets.venus.blocks.VenusBlocks;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerLaserTurret;
import micdoodle8.mods.galacticraft.planets.venus.inventory.ContainerSolarArrayController;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.LogicalSide;

import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

public class TileEntityLaserTurret extends TileBaseElectricBlockWithInventory implements IMultiBlock, ISidedInventory, IMachineSides, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + VenusBlockNames.laserTurret)
    public static TileEntityType<TileEntityLaserTurret> TYPE;

    private final float RANGE = 15.0F;
    private final float METEOR_RANGE = 90.0F;
    private final List<Entity> tracked = Lists.newArrayList();
    private final List<String> players = Lists.newArrayList(); // Can be whitelist or blacklist
    private final List<ResourceLocation> entities = Lists.newArrayList(); // Can be whitelist or blacklist
    private boolean initialisedMulti = false;
    private AxisAlignedBB renderAABB;

    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean active = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int targettedEntity = -1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int chargeLevel = 0;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean blacklistMode = false;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean targetMeteors = true;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean alwaysIgnoreSpaceRace = true;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int priorityClosest = 1;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int priorityLowestHealth = 2;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public int priorityHighestHealth = 3;


    private UUID ownerUUID = null;
    private String ownerName = null;
    private SpaceRace ownerSpaceRace = null;

    @OnlyIn(Dist.CLIENT)
    public float pitch;
    @OnlyIn(Dist.CLIENT)
    public float yaw;
    @OnlyIn(Dist.CLIENT)
    public float targetPitch;
    @OnlyIn(Dist.CLIENT)
    public float targetYaw;
    public int timeSinceShot = -1;  //Cannot initialise client-only fields (causes a server crash on constructing the object)

    public TileEntityLaserTurret()
    {
        super(TYPE);
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
                players.add(NetworkUtil.readUTF8String(dataStream));
            }
            entities.clear();
            int entitySize = dataStream.readInt();
            for (int i = 0; i < entitySize; ++i)
            {
                entities.add(new ResourceLocation(NetworkUtil.readUTF8String(dataStream)));
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
        {
            tracked.add(entity);
        }
    }

    private Entity updateTarget()
    {
        List<EntityEntrySortable> list = Lists.newArrayList();
        if (storage.getEnergyStoredGC() > 1000 && !this.getDisabled(0) && !RedstoneUtil.isBlockReceivingRedstone(this.world, this.getPos()))
        {
            for (int i = 0; i < tracked.size(); ++i)
            {
                Entity e = tracked.get(i);
                if (!e.isAlive())
                {
                    tracked.remove(i--);
                }
                else
                {
                    boolean shouldTarget = !this.blacklistMode;
                    if (e instanceof PlayerEntity)
                    {
                        PlayerEntity toTargetPlayer = (PlayerEntity) e;
                        if (this.alwaysIgnoreSpaceRace && (toTargetPlayer.getUniqueID().equals(this.ownerUUID) || this.ownerSpaceRace != null && this.ownerSpaceRace.getPlayerNames().contains(toTargetPlayer.getName())))
                        {
                            shouldTarget = false;
                        }
                        else
                        {
                            for (String player : players)
                            {
                                if (player.equalsIgnoreCase(e.getName().getString()))
                                {
                                    shouldTarget = !shouldTarget;
                                }
                            }
                        }
                    }
                    else if (e instanceof TameableEntity && ((TameableEntity) e).getOwnerId() != null)
                    {
                        if ((((TameableEntity) e).getOwnerId().equals(this.ownerUUID)) || (this.alwaysIgnoreSpaceRace && this.ownerSpaceRace != null && ((TameableEntity) e).getOwner() != null && this.ownerSpaceRace.getPlayerNames().contains(((TameableEntity) e).getOwner().getName())))
                        {
                            shouldTarget = false;
                        }
                    }
                    else
                    {
                        ResourceLocation location = ForgeRegistries.ENTITIES.getKey(e.getType());
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
                        Vector3 vec = new Vector3(e);
                        vec.y += e.getEyeHeight();
                        vec.translate(new Vector3(-(pos.getX() + 0.5F), -(pos.getY() + 1.78F), -(pos.getZ() + 0.5F)));
                        Vector3 vecNoHeight = vec.clone();
                        vecNoHeight.y = 0;
                        // Make sure target is within range and not directly below turret:
                        if ((vec.getMagnitudeSquared() < RANGE * RANGE || (targetMeteors && e instanceof EntityMeteor && vecNoHeight.getMagnitudeSquared() < METEOR_RANGE * METEOR_RANGE)) && Math.asin(vec.clone().normalize().y) > -Math.PI / 3.0)
                        {
                            if (e instanceof LivingEntity)
                            {
                                list.add(new EntityEntrySortable((LivingEntity) e, vec.getMagnitude()));
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
            Vec3d end = new Vec3d(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());
            start = start.add(end.add(start.scale(-1)).normalize()); // Start at block in front of laser facing direction

            RayTraceResult res = this.world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entity));
//            RayTraceResult res = this.world.rayTraceBlocks(start, end, false, true, true);
            if (res.getType() != RayTraceResult.Type.BLOCK)
            {
                return entity;
            }
        }

        return null;
    }

    @Override
    public void tick()
    {
        super.tick();

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
                    ((ServerWorld) world).getEntities().filter((e) -> e instanceof LivingEntity || e instanceof ILaserTrackableFast).forEach((e) -> trackEntity(e));
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
                    if (toTarget instanceof LivingEntity)
                    {
                        LivingEntity entityLiving = (LivingEntity) toTarget;
                        entityLiving.attackEntityFrom(DamageSourceGC.laserTurret, 1.5F);
                    }
                    else if (toTarget instanceof EntityMeteor)
                    {
                        toTarget.remove();
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
            // Client LogicalSide only
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
                if (entity != null && entity.isAlive())
                {
                    Vector3 vec = new Vector3(entity);
                    vec.y += entity.getEyeHeight();
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
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides

        ListNBT playersTag = nbt.getList("PlayerList", 10);
        for (int i = 0; i < playersTag.size(); i++)
        {
            CompoundNBT tagAt = playersTag.getCompound(i);
            this.players.add(tagAt.getString("PlayerName"));
        }

        ListNBT entitiesTag = nbt.getList("EntitiesList", 10);
        for (int i = 0; i < entitiesTag.size(); i++)
        {
            CompoundNBT tagAt = entitiesTag.getCompound(i);
            this.entities.add(new ResourceLocation(tagAt.getString("EntityRes")));
        }

        this.active = nbt.getBoolean("active");
        this.targettedEntity = nbt.getInt("targettedEntity");
        this.chargeLevel = nbt.getInt("chargeLevel");
        this.blacklistMode = nbt.getBoolean("blacklistMode");
        this.targetMeteors = nbt.getBoolean("targetMeteors");
        this.alwaysIgnoreSpaceRace = nbt.getBoolean("alwaysIgnoreSpaceRace");
        this.priorityClosest = nbt.getInt("priorityClosest");
        this.priorityLowestHealth = nbt.getInt("priorityLowestHealth");
        this.priorityHighestHealth = nbt.getInt("priorityHighestHealth");

        this.ownerName = nbt.contains("ownerName") ? nbt.getString("ownerName") : null;
        this.ownerUUID = nbt.getUniqueId("ownerUUID");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        ListNBT playersTag = new ListNBT();
        for (String player : this.players)
        {
            CompoundNBT tagComp = new CompoundNBT();
            tagComp.putString("PlayerName", player);
            playersTag.add(tagComp);
        }

        nbt.put("PlayerList", playersTag);

        ListNBT entitiesTag = new ListNBT();
        for (ResourceLocation entity : this.entities)
        {
            CompoundNBT tagComp = new CompoundNBT();
            tagComp.putString("EntityRes", entity.toString());
            entitiesTag.add(tagComp);
        }

        nbt.put("EntitiesList", entitiesTag);

        nbt.putBoolean("active", this.active);
        nbt.putInt("targettedEntity", this.targettedEntity);
        nbt.putInt("chargeLevel", this.chargeLevel);
        nbt.putBoolean("blacklistMode", this.blacklistMode);
        nbt.putBoolean("targetMeteors", this.targetMeteors);
        nbt.putBoolean("alwaysIgnoreSpaceRace", this.alwaysIgnoreSpaceRace);
        nbt.putInt("priorityClosest", this.priorityClosest);
        nbt.putInt("priorityLowestHealth", this.priorityLowestHealth);
        nbt.putInt("priorityHighestHealth", this.priorityHighestHealth);

        if (this.ownerName != null)
        {
            nbt.putString("ownerName", this.ownerName);
        }
        nbt.putUniqueId("ownerUUID", this.ownerUUID);

        return nbt;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX() + 1, getPos().getY() + 2, getPos().getZ() + 1);
        }
        return this.renderAABB;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, Direction par3)
    {
        return this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && ItemElectricBase.isElectricItem(itemstack.getItem());
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[]{0};
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return false;
//    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockLaserTurret)
        {
            return state.get(BlockLaserTurret.FACING);
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
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
            PlayerEntity player = this.world.getPlayerByUuid(uniqueID);
            if (player != null)
            {
                this.ownerName = player.getName().getFormattedText();
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
    public ActionResultType onActivated(PlayerEntity entityPlayer)
    {
//        entityPlayer.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_VENUS, world, pos.getX(), pos.getY(), pos.getZ()); TODO Guis
        return ActionResultType.SUCCESS;
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
            BlockState stateAt = this.world.getBlockState(pos);

            if (stateAt.getBlock() == GCBlocks.fakeBlock)
            {
                BlockMulti.EnumBlockMultiType type = stateAt.get(BlockMulti.MULTI_TYPE);
                if (type == BlockMulti.EnumBlockMultiType.LASER_TURRET)
                {
                    if (this.world.isRemote)
                    {
                        Minecraft.getInstance().particles.addBlockDestroyEffects(pos, VenusBlocks.laserTurret.getDefaultState());
                    }

                    this.world.removeBlock(pos, false);
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
        if (world.isRemote)
        {
            this.onCreate(world, pos);
        }

        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(pos, positions);
        boolean result = true;
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityFake)
            {
                ((TileEntityFake) tile).mainBlockPosition = pos;
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
        return new MachineSide[]{MachineSide.ELECTRIC_IN};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.LEFT};
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
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation

    private class EntityEntrySortable
    {
        private LivingEntity entity;
        private double distance;

        public EntityEntrySortable(LivingEntity entity, double distance)
        {
            this.entity = entity;
            this.distance = distance;
        }

        public LivingEntity getEntity()
        {
            return entity;
        }

        public void setEntity(LivingEntity entity)
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

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerLaserTurret(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.laser_turret");
    }
}
