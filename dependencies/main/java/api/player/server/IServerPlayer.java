// ==================================================================
// This file is part of Player API.
//
// Player API is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
//
// Player API is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License and the GNU General Public License along with Player API.
// If not, see <http://www.gnu.org/licenses/>.
// ==================================================================

package api.player.server;

public interface IServerPlayer
{
	ServerPlayerBase getServerPlayerBase(String baseId);

	java.util.Set<String> getServerPlayerBaseIds();

	Object dynamic(String key, Object[] parameters);

	void realAddExhaustion(float paramFloat);

	void superAddExhaustion(float paramFloat);

	void localAddExhaustion(float paramFloat);

	void realAddExperience(int paramInt);

	void superAddExperience(int paramInt);

	void localAddExperience(int paramInt);

	void realAddExperienceLevel(int paramInt);

	void superAddExperienceLevel(int paramInt);

	void localAddExperienceLevel(int paramInt);

	void realAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	void superAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	void localAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	boolean realAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	boolean superAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	boolean localAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void realAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	void superAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	void localAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	boolean realCanBreatheUnderwater();

	boolean superCanBreatheUnderwater();

	boolean localCanBreatheUnderwater();

	boolean realCanHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState);

	boolean superCanHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState);

	boolean localCanHarvestBlock(net.minecraft.block.state.IBlockState paramIBlockState);

	boolean realCanPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack);

	boolean superCanPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack);

	boolean localCanPlayerEdit(net.minecraft.util.math.BlockPos paramBlockPos, net.minecraft.util.EnumFacing paramEnumFacing, net.minecraft.item.ItemStack paramItemStack);

	boolean realCanTriggerWalking();

	boolean superCanTriggerWalking();

	boolean localCanTriggerWalking();

	void realClonePlayer(net.minecraft.entity.player.EntityPlayerMP paramEntityPlayerMP, boolean paramBoolean);

	void localClonePlayer(net.minecraft.entity.player.EntityPlayerMP paramEntityPlayerMP, boolean paramBoolean);

	void realDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void superDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void localDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void realDisplayGui(net.minecraft.world.IInteractionObject paramIInteractionObject);

	void superDisplayGui(net.minecraft.world.IInteractionObject paramIInteractionObject);

	void localDisplayGui(net.minecraft.world.IInteractionObject paramIInteractionObject);

	void realDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	void superDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	void localDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	net.minecraft.entity.item.EntityItem realDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem superDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem localDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem realDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	net.minecraft.entity.item.EntityItem superDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	net.minecraft.entity.item.EntityItem localDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	void realFall(float paramFloat1, float paramFloat2);

	void superFall(float paramFloat1, float paramFloat2);

	void localFall(float paramFloat1, float paramFloat2);

	float realGetAIMoveSpeed();

	float superGetAIMoveSpeed();

	float localGetAIMoveSpeed();

	float realGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos);

	float superGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos);

	float localGetBreakSpeed(net.minecraft.block.state.IBlockState paramIBlockState, net.minecraft.util.math.BlockPos paramBlockPos);

	double realGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	double superGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	double localGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	float realGetBrightness();

	float superGetBrightness();

	float localGetBrightness();

	float realGetEyeHeight();

	float superGetEyeHeight();

	float localGetEyeHeight();

	void realHeal(float paramFloat);

	void superHeal(float paramFloat);

	void localHeal(float paramFloat);

	boolean realIsEntityInsideOpaqueBlock();

	boolean superIsEntityInsideOpaqueBlock();

	boolean localIsEntityInsideOpaqueBlock();

	boolean realIsInWater();

	boolean superIsInWater();

	boolean localIsInWater();

	boolean realIsInsideOfMaterial(net.minecraft.block.material.Material paramMaterial);

	boolean superIsInsideOfMaterial(net.minecraft.block.material.Material paramMaterial);

	boolean localIsInsideOfMaterial(net.minecraft.block.material.Material paramMaterial);

	boolean realIsOnLadder();

	boolean superIsOnLadder();

	boolean localIsOnLadder();

	boolean realIsPlayerSleeping();

	boolean superIsPlayerSleeping();

	boolean localIsPlayerSleeping();

	boolean realIsSneaking();

	boolean superIsSneaking();

	boolean localIsSneaking();

	void realJump();

	void superJump();

	void localJump();

	void realKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	void superKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	void localKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	boolean realMountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean);

	boolean superMountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean);

	boolean localMountEntity(net.minecraft.entity.Entity paramEntity, boolean paramBoolean);

	void realMoveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3);

	void superMoveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3);

	void localMoveEntity(net.minecraft.entity.MoverType paramMoverType, double paramDouble1, double paramDouble2, double paramDouble3);

	void realMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3);

	void superMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3);

	void localMoveEntityWithHeading(float paramFloat1, float paramFloat2, float paramFloat3);

	void realMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

	void superMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

	void localMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);

	void realOnDeath(net.minecraft.util.DamageSource paramDamageSource);

	void superOnDeath(net.minecraft.util.DamageSource paramDamageSource);

	void localOnDeath(net.minecraft.util.DamageSource paramDamageSource);

	void realOnLivingUpdate();

	void superOnLivingUpdate();

	void localOnLivingUpdate();

	void realOnKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	void superOnKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	void localOnKillEntity(net.minecraft.entity.EntityLivingBase paramEntityLivingBase);

	void realOnStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt);

	void superOnStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt);

	void localOnStruckByLightning(net.minecraft.entity.effect.EntityLightningBolt paramEntityLightningBolt);

	void realOnUpdate();

	void superOnUpdate();

	void localOnUpdate();

	void realOnUpdateEntity();

	void localOnUpdateEntity();

	void realReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void superReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void localReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void realSetDead();

	void superSetDead();

	void localSetDead();

	void realSetEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2);

	void localSetEntityActionState(float paramFloat1, float paramFloat2, boolean paramBoolean1, boolean paramBoolean2);

	void realSetPosition(double paramDouble1, double paramDouble2, double paramDouble3);

	void superSetPosition(double paramDouble1, double paramDouble2, double paramDouble3);

	void localSetPosition(double paramDouble1, double paramDouble2, double paramDouble3);

	void realSetSneaking(boolean paramBoolean);

	void superSetSneaking(boolean paramBoolean);

	void localSetSneaking(boolean paramBoolean);

	void realSetSprinting(boolean paramBoolean);

	void superSetSprinting(boolean paramBoolean);

	void localSetSprinting(boolean paramBoolean);

	void realSwingItem(net.minecraft.util.EnumHand paramEnumHand);

	void superSwingItem(net.minecraft.util.EnumHand paramEnumHand);

	void localSwingItem(net.minecraft.util.EnumHand paramEnumHand);

	void realUpdateEntityActionState();

	void superUpdateEntityActionState();

	void localUpdateEntityActionState();

	void realUpdatePotionEffects();

	void superUpdatePotionEffects();

	void localUpdatePotionEffects();

	void realUpdateRidden();

	void superUpdateRidden();

	void localUpdateRidden();

	void realWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void superWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void localWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void realWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void superWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void localWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	net.minecraft.network.datasync.DataParameter getLEFT_SHOULDER_ENTITYField();

	net.minecraft.network.datasync.DataParameter getRIGHT_SHOULDER_ENTITYField();

	net.minecraft.item.ItemStack getActiveItemStackField();

	void setActiveItemStackField(net.minecraft.item.ItemStack activeItemStack);

	int getActiveItemStackUseCountField();

	void setActiveItemStackUseCountField(int activeItemStackUseCount);

	boolean getAddedToChunkField();

	void setAddedToChunkField(boolean addedToChunk);

	net.minecraft.advancements.PlayerAdvancements getAdvancementsField();

	int getArrowHitTimerField();

	void setArrowHitTimerField(int arrowHitTimer);

	float getAttackedAtYawField();

	void setAttackedAtYawField(float attackedAtYaw);

	net.minecraft.entity.player.EntityPlayer getAttackingPlayerField();

	void setAttackingPlayerField(net.minecraft.entity.player.EntityPlayer attackingPlayer);

	java.lang.String getCachedUniqueIdStringField();

	void setCachedUniqueIdStringField(java.lang.String cachedUniqueIdString);

	float getCameraPitchField();

	void setCameraPitchField(float cameraPitch);

	float getCameraYawField();

	void setCameraYawField(float cameraYaw);

	net.minecraft.entity.player.PlayerCapabilities getCapabilitiesField();

	void setCapabilitiesField(net.minecraft.entity.player.PlayerCapabilities capabilities);

	double getChasingPosXField();

	void setChasingPosXField(double chasingPosX);

	double getChasingPosYField();

	void setChasingPosYField(double chasingPosY);

	double getChasingPosZField();

	void setChasingPosZField(double chasingPosZ);

	boolean getChatColoursField();

	void setChatColoursField(boolean chatColours);

	net.minecraft.entity.player.EntityPlayer.EnumChatVisibility getChatVisibilityField();

	void setChatVisibilityField(net.minecraft.entity.player.EntityPlayer.EnumChatVisibility chatVisibility);

	int getChunkCoordXField();

	void setChunkCoordXField(int chunkCoordX);

	int getChunkCoordYField();

	void setChunkCoordYField(int chunkCoordY);

	int getChunkCoordZField();

	void setChunkCoordZField(int chunkCoordZ);

	float getCombinedHealthField();

	void setCombinedHealthField(float combinedHealth);

	int getCurrentWindowIdField();

	void setCurrentWindowIdField(int currentWindowId);

	net.minecraft.network.datasync.EntityDataManager getDataWatcherField();

	void setDataWatcherField(net.minecraft.network.datasync.EntityDataManager dataWatcher);

	boolean getDeadField();

	void setDeadField(boolean dead);

	int getDeathTimeField();

	void setDeathTimeField(int deathTime);

	java.util.List<?> getDestroyedItemsNetCacheField();

	int getDimensionField();

	void setDimensionField(int dimension);

	boolean getDisconnectedField();

	void setDisconnectedField(boolean disconnected);

	float getDistanceWalkedModifiedField();

	void setDistanceWalkedModifiedField(float distanceWalkedModified);

	float getDistanceWalkedOnStepModifiedField();

	void setDistanceWalkedOnStepModifiedField(float distanceWalkedOnStepModified);

	net.minecraft.inventory.InventoryEnderChest getEnderChestField();

	void setEnderChestField(net.minecraft.inventory.InventoryEnderChest enderChest);

	net.minecraft.util.math.Vec3d getEnteredNetherPositionField();

	void setEnteredNetherPositionField(net.minecraft.util.math.Vec3d enteredNetherPosition);

	float getEntityCollisionReductionField();

	void setEntityCollisionReductionField(float entityCollisionReduction);

	java.util.UUID getEntityUniqueIDField();

	void setEntityUniqueIDField(java.util.UUID entityUniqueID);

	float getExperienceField();

	void setExperienceField(float experience);

	int getExperienceLevelField();

	void setExperienceLevelField(int experienceLevel);

	int getExperienceTotalField();

	void setExperienceTotalField(int experienceTotal);

	float getFallDistanceField();

	void setFallDistanceField(float fallDistance);

	boolean getFirstUpdateField();

	void setFirstUpdateField(boolean firstUpdate);

	net.minecraft.entity.projectile.EntityFishHook getFishEntityField();

	void setFishEntityField(net.minecraft.entity.projectile.EntityFishHook fishEntity);

	net.minecraft.network.datasync.DataParameter getFlagsField();

	int getFlyToggleTimerField();

	void setFlyToggleTimerField(int flyToggleTimer);

	net.minecraft.util.FoodStats getFoodStatsField();

	void setFoodStatsField(net.minecraft.util.FoodStats foodStats);

	boolean getForceSpawnField();

	void setForceSpawnField(boolean forceSpawn);

	boolean getGlowingField();

	void setGlowingField(boolean glowing);

	net.minecraft.network.datasync.DataParameter getHandStatesField();

	float getHeightField();

	void setHeightField(float height);

	float getHundredEightyField();

	void setHundredEightyField(float hundredEighty);

	int getHurtResistantTimeField();

	void setHurtResistantTimeField(int hurtResistantTime);

	int getHurtTimeField();

	void setHurtTimeField(int hurtTime);

	int getIdleTimeField();

	void setIdleTimeField(int idleTime);

	boolean getIgnoreFrustumCheckField();

	void setIgnoreFrustumCheckField(boolean ignoreFrustumCheck);

	boolean getInPortalField();

	void setInPortalField(boolean inPortal);

	boolean getInWaterField();

	void setInWaterField(boolean inWater);

	net.minecraft.server.management.PlayerInteractionManager getInteractionManagerField();

	double getInterpTargetXField();

	void setInterpTargetXField(double interpTargetX);

	double getInterpTargetYField();

	void setInterpTargetYField(double interpTargetY);

	double getInterpTargetYawField();

	void setInterpTargetYawField(double interpTargetYaw);

	double getInterpTargetZField();

	void setInterpTargetZField(double interpTargetZ);

	net.minecraft.entity.player.InventoryPlayer getInventoryField();

	void setInventoryField(net.minecraft.entity.player.InventoryPlayer inventory);

	net.minecraft.inventory.Container getInventoryContainerField();

	void setInventoryContainerField(net.minecraft.inventory.Container inventoryContainer);

	boolean getInvulnerableDimensionChangeField();

	void setInvulnerableDimensionChangeField(boolean invulnerableDimensionChange);

	boolean getIsAirBorneField();

	void setIsAirBorneField(boolean isAirBorne);

	boolean getIsChangingQuantityOnlyField();

	void setIsChangingQuantityOnlyField(boolean isChangingQuantityOnly);

	boolean getIsCollidedField();

	void setIsCollidedField(boolean isCollided);

	boolean getIsCollidedHorizontallyField();

	void setIsCollidedHorizontallyField(boolean isCollidedHorizontally);

	boolean getIsCollidedVerticallyField();

	void setIsCollidedVerticallyField(boolean isCollidedVertically);

	boolean getIsDeadField();

	void setIsDeadField(boolean isDead);

	boolean getIsImmuneToFireField();

	void setIsImmuneToFireField(boolean isImmuneToFire);

	boolean getIsInWebField();

	void setIsInWebField(boolean isInWeb);

	boolean getIsJumpingField();

	void setIsJumpingField(boolean isJumping);

	boolean getIsSwingInProgressField();

	void setIsSwingInProgressField(boolean isSwingInProgress);

	float getJumpMovementFactorField();

	void setJumpMovementFactorField(float jumpMovementFactor);

	int getLastAirScoreField();

	void setLastAirScoreField(int lastAirScore);

	int getLastArmorScoreField();

	void setLastArmorScoreField(int lastArmorScore);

	float getLastDamageField();

	void setLastDamageField(float lastDamage);

	int getLastExperienceField();

	void setLastExperienceField(int lastExperience);

	int getLastExperienceScoreField();

	void setLastExperienceScoreField(int lastExperienceScore);

	int getLastFoodLevelField();

	void setLastFoodLevelField(int lastFoodLevel);

	int getLastFoodScoreField();

	void setLastFoodScoreField(int lastFoodScore);

	float getLastHealthField();

	void setLastHealthField(float lastHealth);

	int getLastLevelScoreField();

	void setLastLevelScoreField(int lastLevelScore);

	net.minecraft.util.math.BlockPos getLastPortalPosField();

	void setLastPortalPosField(net.minecraft.util.math.BlockPos lastPortalPos);

	net.minecraft.util.math.Vec3d getLastPortalVecField();

	void setLastPortalVecField(net.minecraft.util.math.Vec3d lastPortalVec);

	double getLastTickPosXField();

	void setLastTickPosXField(double lastTickPosX);

	double getLastTickPosYField();

	void setLastTickPosYField(double lastTickPosY);

	double getLastTickPosZField();

	void setLastTickPosZField(double lastTickPosZ);

	int getLevitatingSinceField();

	void setLevitatingSinceField(int levitatingSince);

	net.minecraft.util.math.Vec3d getLevitationStartPosField();

	void setLevitationStartPosField(net.minecraft.util.math.Vec3d levitationStartPos);

	float getLimbSwingField();

	void setLimbSwingField(float limbSwing);

	float getLimbSwingAmountField();

	void setLimbSwingAmountField(float limbSwingAmount);

	org.apache.logging.log4j.Logger getLoggerField();

	net.minecraft.network.datasync.DataParameter getMainHandField();

	double getManagedPosXField();

	void setManagedPosXField(double managedPosX);

	double getManagedPosZField();

	void setManagedPosZField(double managedPosZ);

	int getMaxHurtResistantTimeField();

	void setMaxHurtResistantTimeField(int maxHurtResistantTime);

	int getMaxHurtTimeField();

	void setMaxHurtTimeField(int maxHurtTime);

	net.minecraft.server.MinecraftServer getMcServerField();

	double getMotionXField();

	void setMotionXField(double motionX);

	double getMotionYField();

	void setMotionYField(double motionY);

	double getMotionZField();

	void setMotionZField(double motionZ);

	float getMoveForwardField();

	void setMoveForwardField(float moveForward);

	float getMoveStrafingField();

	void setMoveStrafingField(float moveStrafing);

	float getMoveVerticalField();

	void setMoveVerticalField(float moveVertical);

	float getMovedDistanceField();

	void setMovedDistanceField(float movedDistance);

	int getNewPosRotationIncrementsField();

	void setNewPosRotationIncrementsField(int newPosRotationIncrements);

	double getNewPosXField();

	void setNewPosXField(double newPosX);

	boolean getNoClipField();

	void setNoClipField(boolean noClip);

	boolean getOnGroundField();

	void setOnGroundField(boolean onGround);

	float getOnGroundSpeedFactorField();

	void setOnGroundSpeedFactorField(float onGroundSpeedFactor);

	net.minecraft.inventory.Container getOpenContainerField();

	void setOpenContainerField(net.minecraft.inventory.Container openContainer);

	int getPingField();

	void setPingField(int ping);

	long getPlayerLastActiveTimeField();

	void setPlayerLastActiveTimeField(long playerLastActiveTime);

	net.minecraft.util.math.BlockPos getPlayerLocationField();

	void setPlayerLocationField(net.minecraft.util.math.BlockPos playerLocation);

	net.minecraft.network.datasync.DataParameter getPlayerModelFlagField();

	net.minecraft.network.NetHandlerPlayServer getPlayerNetServerHandlerField();

	void setPlayerNetServerHandlerField(net.minecraft.network.NetHandlerPlayServer playerNetServerHandler);

	int getPortalCounterField();

	void setPortalCounterField(int portalCounter);

	double getPosXField();

	void setPosXField(double posX);

	double getPosYField();

	void setPosYField(double posY);

	double getPosZField();

	void setPosZField(double posZ);

	float getPrevCameraPitchField();

	void setPrevCameraPitchField(float prevCameraPitch);

	float getPrevCameraYawField();

	void setPrevCameraYawField(float prevCameraYaw);

	double getPrevChasingPosXField();

	void setPrevChasingPosXField(double prevChasingPosX);

	double getPrevChasingPosYField();

	void setPrevChasingPosYField(double prevChasingPosY);

	double getPrevChasingPosZField();

	void setPrevChasingPosZField(double prevChasingPosZ);

	float getPrevDistanceWalkedModifiedField();

	void setPrevDistanceWalkedModifiedField(float prevDistanceWalkedModified);

	float getPrevLimbSwingAmountField();

	void setPrevLimbSwingAmountField(float prevLimbSwingAmount);

	float getPrevMovedDistanceField();

	void setPrevMovedDistanceField(float prevMovedDistance);

	float getPrevOnGroundSpeedFactorField();

	void setPrevOnGroundSpeedFactorField(float prevOnGroundSpeedFactor);

	double getPrevPosXField();

	void setPrevPosXField(double prevPosX);

	double getPrevPosYField();

	void setPrevPosYField(double prevPosY);

	double getPrevPosZField();

	void setPrevPosZField(double prevPosZ);

	float getPrevRenderYawOffsetField();

	void setPrevRenderYawOffsetField(float prevRenderYawOffset);

	float getPrevRotationPitchField();

	void setPrevRotationPitchField(float prevRotationPitch);

	float getPrevRotationYawField();

	void setPrevRotationYawField(float prevRotationYaw);

	float getPrevRotationYawHeadField();

	void setPrevRotationYawHeadField(float prevRotationYawHead);

	float getPrevSwingProgressField();

	void setPrevSwingProgressField(float prevSwingProgress);

	boolean getPreventEntitySpawningField();

	void setPreventEntitySpawningField(boolean preventEntitySpawning);

	boolean getQueuedEndExitField();

	void setQueuedEndExitField(boolean queuedEndExit);

	java.util.Random getRandField();

	void setRandField(java.util.Random rand);

	float getRandomNumberBetweenOneHundredthAndTwoHundredthField();

	void setRandomNumberBetweenOneHundredthAndTwoHundredthField(float randomNumberBetweenOneHundredthAndTwoHundredth);

	float getRandomNumberBetweenZeroAndTwelveThousandThreeHundredNinetyEightField();

	void setRandomNumberBetweenZeroAndTwelveThousandThreeHundredNinetyEightField(float randomNumberBetweenZeroAndTwelveThousandThreeHundredNinetyEight);

	float getRandomYawVelocityField();

	void setRandomYawVelocityField(float randomYawVelocity);

	int getRecentlyHitField();

	void setRecentlyHitField(int recentlyHit);

	net.minecraft.stats.RecipeBookServer getRecipeBookField();

	float getRenderOffsetXField();

	void setRenderOffsetXField(float renderOffsetX);

	float getRenderOffsetYField();

	void setRenderOffsetYField(float renderOffsetY);

	float getRenderOffsetZField();

	void setRenderOffsetZField(float renderOffsetZ);

	float getRenderYawOffsetField();

	void setRenderYawOffsetField(float renderYawOffset);

	int getRespawnInvulnerabilityTicksField();

	void setRespawnInvulnerabilityTicksField(int respawnInvulnerabilityTicks);

	int getRideCooldownField();

	void setRideCooldownField(int rideCooldown);

	float getRotationPitchField();

	void setRotationPitchField(float rotationPitch);

	float getRotationYawField();

	void setRotationYawField(float rotationYaw);

	float getRotationYawHeadField();

	void setRotationYawHeadField(float rotationYawHead);

	int getScoreValueField();

	void setScoreValueField(int scoreValue);

	boolean getSeenCreditsField();

	void setSeenCreditsField(boolean seenCredits);

	long getServerPosXField();

	void setServerPosXField(long serverPosX);

	long getServerPosYField();

	void setServerPosYField(long serverPosY);

	long getServerPosZField();

	void setServerPosZField(long serverPosZ);

	boolean getSleepingField();

	void setSleepingField(boolean sleeping);

	net.minecraft.entity.Entity getSpectatingEntityField();

	void setSpectatingEntityField(net.minecraft.entity.Entity spectatingEntity);

	float getSpeedInAirField();

	void setSpeedInAirField(float speedInAir);

	net.minecraft.stats.StatisticsManagerServer getStatsFileField();

	float getStepHeightField();

	void setStepHeightField(float stepHeight);

	float getSwingProgressField();

	void setSwingProgressField(float swingProgress);

	int getSwingProgressIntField();

	void setSwingProgressIntField(int swingProgressInt);

	net.minecraft.util.EnumHand getSwingingHandField();

	void setSwingingHandField(net.minecraft.util.EnumHand swingingHand);

	net.minecraft.util.EnumFacing getTeleportDirectionField();

	void setTeleportDirectionField(net.minecraft.util.EnumFacing teleportDirection);

	int getTicksElytraFlyingField();

	void setTicksElytraFlyingField(int ticksElytraFlying);

	int getTicksExistedField();

	void setTicksExistedField(int ticksExisted);

	int getTicksSinceLastSwingField();

	void setTicksSinceLastSwingField(int ticksSinceLastSwing);

	int getTimeUntilPortalField();

	void setTimeUntilPortalField(int timeUntilPortal);

	java.lang.String getTranslatorField();

	void setTranslatorField(java.lang.String translator);

	boolean getVelocityChangedField();

	void setVelocityChangedField(boolean velocityChanged);

	boolean getWasHungryField();

	void setWasHungryField(boolean wasHungry);

	float getWidthField();

	void setWidthField(float width);

	net.minecraft.world.World getWorldObjField();

	void setWorldObjField(net.minecraft.world.World worldObj);

	int getXpCooldownField();

	void setXpCooldownField(int xpCooldown);

	int getXpSeedField();

	void setXpSeedField(int xpSeed);

}
