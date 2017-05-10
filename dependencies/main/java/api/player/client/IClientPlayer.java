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

package api.player.client;

public interface IClientPlayer
{
	ClientPlayerBase getClientPlayerBase(String baseId);

	java.util.Set<String> getClientPlayerBaseIds();

	Object dynamic(String key, Object[] parameters);

	void realAddExhaustion(float paramFloat);

	void superAddExhaustion(float paramFloat);

	void localAddExhaustion(float paramFloat);

	void realAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	void superAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	void localAddMovementStat(double paramDouble1, double paramDouble2, double paramDouble3);

	void realAddStat(net.minecraft.stats.StatBase paramStatBase, int paramInt);

	void superAddStat(net.minecraft.stats.StatBase paramStatBase, int paramInt);

	void localAddStat(net.minecraft.stats.StatBase paramStatBase, int paramInt);

	boolean realAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	boolean superAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	boolean localAttackEntityFrom(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void realAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	void superAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	void localAttackTargetEntityWithCurrentItem(net.minecraft.entity.Entity paramEntity);

	boolean realCanBreatheUnderwater();

	boolean superCanBreatheUnderwater();

	boolean localCanBreatheUnderwater();

	boolean realCanHarvestBlock(net.minecraft.block.Block paramBlock);

	boolean superCanHarvestBlock(net.minecraft.block.Block paramBlock);

	boolean localCanHarvestBlock(net.minecraft.block.Block paramBlock);

	boolean realCanPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack);

	boolean superCanPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack);

	boolean localCanPlayerEdit(int paramInt1, int paramInt2, int paramInt3, int paramInt4, net.minecraft.item.ItemStack paramItemStack);

	boolean realCanTriggerWalking();

	boolean superCanTriggerWalking();

	boolean localCanTriggerWalking();

	void realCloseScreen();

	void superCloseScreen();

	void localCloseScreen();

	void realDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void superDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void localDamageEntity(net.minecraft.util.DamageSource paramDamageSource, float paramFloat);

	void realDisplayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand);

	void superDisplayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand);

	void localDisplayGUIBrewingStand(net.minecraft.tileentity.TileEntityBrewingStand paramTileEntityBrewingStand);

	void realDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	void superDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	void localDisplayGUIChest(net.minecraft.inventory.IInventory paramIInventory);

	void realDisplayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser);

	void superDisplayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser);

	void localDisplayGUIDispenser(net.minecraft.tileentity.TileEntityDispenser paramTileEntityDispenser);

	void realDisplayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity);

	void superDisplayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity);

	void localDisplayGUIEditSign(net.minecraft.tileentity.TileEntity paramTileEntity);

	void realDisplayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString);

	void superDisplayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString);

	void localDisplayGUIEnchantment(int paramInt1, int paramInt2, int paramInt3, String paramString);

	void realDisplayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace);

	void superDisplayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace);

	void localDisplayGUIFurnace(net.minecraft.tileentity.TileEntityFurnace paramTileEntityFurnace);

	void realDisplayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3);

	void superDisplayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3);

	void localDisplayGUIWorkbench(int paramInt1, int paramInt2, int paramInt3);

	net.minecraft.entity.item.EntityItem realDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem superDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem localDropOneItem(boolean paramBoolean);

	net.minecraft.entity.item.EntityItem realDropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	net.minecraft.entity.item.EntityItem superDropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	net.minecraft.entity.item.EntityItem localDropPlayerItem(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean);

	net.minecraft.entity.item.EntityItem realDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2);

	net.minecraft.entity.item.EntityItem superDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2);

	net.minecraft.entity.item.EntityItem localDropPlayerItemWithRandomChoice(net.minecraft.item.ItemStack paramItemStack, boolean paramBoolean1, boolean paramBoolean2);

	void realFall(float paramFloat);

	void superFall(float paramFloat);

	void localFall(float paramFloat);

	float realGetAIMoveSpeed();

	float superGetAIMoveSpeed();

	float localGetAIMoveSpeed();

	float realGetBedOrientationInDegrees();

	float superGetBedOrientationInDegrees();

	float localGetBedOrientationInDegrees();

	float realGetBrightness(float paramFloat);

	float superGetBrightness(float paramFloat);

	float localGetBrightness(float paramFloat);

	int realGetBrightnessForRender(float paramFloat);

	int superGetBrightnessForRender(float paramFloat);

	int localGetBrightnessForRender(float paramFloat);

	float realGetCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean);

	float superGetCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean);

	float localGetCurrentPlayerStrVsBlock(net.minecraft.block.Block paramBlock, boolean paramBoolean);

	float realGetCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt);

	float superGetCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt);

	float localGetCurrentPlayerStrVsBlockForge(net.minecraft.block.Block paramBlock, boolean paramBoolean, int paramInt);

	double realGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	double superGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	double localGetDistanceSq(double paramDouble1, double paramDouble2, double paramDouble3);

	double realGetDistanceSqToEntity(net.minecraft.entity.Entity paramEntity);

	double superGetDistanceSqToEntity(net.minecraft.entity.Entity paramEntity);

	double localGetDistanceSqToEntity(net.minecraft.entity.Entity paramEntity);

	float realGetFOVMultiplier();

	float localGetFOVMultiplier();

	java.lang.String realGetHurtSound();

	java.lang.String superGetHurtSound();

	java.lang.String localGetHurtSound();

	net.minecraft.util.IIcon realGetItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt);

	net.minecraft.util.IIcon superGetItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt);

	net.minecraft.util.IIcon localGetItemIcon(net.minecraft.item.ItemStack paramItemStack, int paramInt);

	int realGetSleepTimer();

	int superGetSleepTimer();

	int localGetSleepTimer();

	boolean realHandleLavaMovement();

	boolean superHandleLavaMovement();

	boolean localHandleLavaMovement();

	boolean realHandleWaterMovement();

	boolean superHandleWaterMovement();

	boolean localHandleWaterMovement();

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

	boolean realIsSprinting();

	boolean superIsSprinting();

	boolean localIsSprinting();

	void realJump();

	void superJump();

	void localJump();

	void realKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	void superKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	void localKnockBack(net.minecraft.entity.Entity paramEntity, float paramFloat, double paramDouble1, double paramDouble2);

	void realMoveEntity(double paramDouble1, double paramDouble2, double paramDouble3);

	void superMoveEntity(double paramDouble1, double paramDouble2, double paramDouble3);

	void localMoveEntity(double paramDouble1, double paramDouble2, double paramDouble3);

	void realMoveEntityWithHeading(float paramFloat1, float paramFloat2);

	void superMoveEntityWithHeading(float paramFloat1, float paramFloat2);

	void localMoveEntityWithHeading(float paramFloat1, float paramFloat2);

	void realMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3);

	void superMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3);

	void localMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3);

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

	void realPlayStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock);

	void superPlayStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock);

	void localPlayStepSound(int paramInt1, int paramInt2, int paramInt3, net.minecraft.block.Block paramBlock);

	boolean realPushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3);

	boolean superPushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3);

	boolean localPushOutOfBlocks(double paramDouble1, double paramDouble2, double paramDouble3);

	net.minecraft.util.MovingObjectPosition realRayTrace(double paramDouble, float paramFloat);

	net.minecraft.util.MovingObjectPosition superRayTrace(double paramDouble, float paramFloat);

	net.minecraft.util.MovingObjectPosition localRayTrace(double paramDouble, float paramFloat);

	void realReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void superReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void localReadEntityFromNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void realRespawnPlayer();

	void superRespawnPlayer();

	void localRespawnPlayer();

	void realSetDead();

	void superSetDead();

	void localSetDead();

	void realSetPlayerSPHealth(float paramFloat);

	void localSetPlayerSPHealth(float paramFloat);

	void realSetPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void superSetPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void localSetPositionAndRotation(double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2);

	void realSetSneaking(boolean paramBoolean);

	void superSetSneaking(boolean paramBoolean);

	void localSetSneaking(boolean paramBoolean);

	void realSetSprinting(boolean paramBoolean);

	void superSetSprinting(boolean paramBoolean);

	void localSetSprinting(boolean paramBoolean);

	net.minecraft.entity.player.EntityPlayer.EnumStatus realSleepInBedAt(int paramInt1, int paramInt2, int paramInt3);

	net.minecraft.entity.player.EntityPlayer.EnumStatus superSleepInBedAt(int paramInt1, int paramInt2, int paramInt3);

	net.minecraft.entity.player.EntityPlayer.EnumStatus localSleepInBedAt(int paramInt1, int paramInt2, int paramInt3);

	void realSwingItem();

	void superSwingItem();

	void localSwingItem();

	void realUpdateEntityActionState();

	void superUpdateEntityActionState();

	void localUpdateEntityActionState();

	void realUpdateRidden();

	void superUpdateRidden();

	void localUpdateRidden();

	void realWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void superWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void localWakeUpPlayer(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);

	void realWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void superWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	void localWriteEntityToNBT(net.minecraft.nbt.NBTTagCompound paramNBTTagCompound);

	boolean getAddedToChunkField();

	void setAddedToChunkField(boolean addedToChunk);

	int getArrowHitTimerField();

	void setArrowHitTimerField(int arrowHitTimer);

	int getAttackTimeField();

	void setAttackTimeField(int attackTime);

	float getAttackedAtYawField();

	void setAttackedAtYawField(float attackedAtYaw);

	net.minecraft.entity.player.EntityPlayer getAttackingPlayerField();

	void setAttackingPlayerField(net.minecraft.entity.player.EntityPlayer attackingPlayer);

	net.minecraft.util.AxisAlignedBB getBoundingBoxField();

	float getCameraPitchField();

	void setCameraPitchField(float cameraPitch);

	float getCameraYawField();

	void setCameraYawField(float cameraYaw);

	net.minecraft.entity.player.PlayerCapabilities getCapabilitiesField();

	void setCapabilitiesField(net.minecraft.entity.player.PlayerCapabilities capabilities);

	int getChunkCoordXField();

	void setChunkCoordXField(int chunkCoordX);

	int getChunkCoordYField();

	void setChunkCoordYField(int chunkCoordY);

	int getChunkCoordZField();

	void setChunkCoordZField(int chunkCoordZ);

	net.minecraft.entity.DataWatcher getDataWatcherField();

	void setDataWatcherField(net.minecraft.entity.DataWatcher dataWatcher);

	boolean getDeadField();

	void setDeadField(boolean dead);

	int getDeathTimeField();

	void setDeathTimeField(int deathTime);

	int getDimensionField();

	void setDimensionField(int dimension);

	float getDistanceWalkedModifiedField();

	void setDistanceWalkedModifiedField(float distanceWalkedModified);

	float getDistanceWalkedOnStepModifiedField();

	void setDistanceWalkedOnStepModifiedField(float distanceWalkedOnStepModified);

	int getEntityAgeField();

	void setEntityAgeField(int entityAge);

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

	float getField_110154_aXField();

	void setField_110154_aXField(float field_110154_aX);

	boolean getField_70135_KField();

	void setField_70135_KField(boolean field_70135_K);

	float getField_70741_aBField();

	void setField_70741_aBField(float field_70741_aB);

	float getField_70763_axField();

	void setField_70763_axField(float field_70763_ax);

	float getField_70764_awField();

	void setField_70764_awField(float field_70764_aw);

	float getField_70768_auField();

	void setField_70768_auField(float field_70768_au);

	float getField_70769_aoField();

	void setField_70769_aoField(float field_70769_ao);

	float getField_70770_apField();

	void setField_70770_apField(float field_70770_ap);

	float getField_71079_bUField();

	void setField_71079_bUField(float field_71079_bU);

	float getField_71082_cxField();

	void setField_71082_cxField(float field_71082_cx);

	double getField_71085_bRField();

	void setField_71085_bRField(double field_71085_bR);

	float getField_71089_bVField();

	void setField_71089_bVField(float field_71089_bV);

	double getField_71091_bMField();

	void setField_71091_bMField(double field_71091_bM);

	double getField_71094_bPField();

	void setField_71094_bPField(double field_71094_bP);

	double getField_71095_bQField();

	void setField_71095_bQField(double field_71095_bQ);

	double getField_71096_bNField();

	void setField_71096_bNField(double field_71096_bN);

	double getField_71097_bOField();

	void setField_71097_bOField(double field_71097_bO);

	net.minecraft.util.MouseFilter getField_71160_ciField();

	void setField_71160_ciField(net.minecraft.util.MouseFilter field_71160_ci);

	net.minecraft.util.MouseFilter getField_71161_cjField();

	void setField_71161_cjField(net.minecraft.util.MouseFilter field_71161_cj);

	net.minecraft.util.MouseFilter getField_71162_chField();

	void setField_71162_chField(net.minecraft.util.MouseFilter field_71162_ch);

	int getFireResistanceField();

	void setFireResistanceField(int fireResistance);

	net.minecraft.entity.projectile.EntityFishHook getFishEntityField();

	void setFishEntityField(net.minecraft.entity.projectile.EntityFishHook fishEntity);

	int getFlyToggleTimerField();

	void setFlyToggleTimerField(int flyToggleTimer);

	net.minecraft.util.FoodStats getFoodStatsField();

	void setFoodStatsField(net.minecraft.util.FoodStats foodStats);

	boolean getForceSpawnField();

	void setForceSpawnField(boolean forceSpawn);

	float getHeightField();

	void setHeightField(float height);

	float getHorseJumpPowerField();

	void setHorseJumpPowerField(float horseJumpPower);

	int getHorseJumpPowerCounterField();

	void setHorseJumpPowerCounterField(int horseJumpPowerCounter);

	int getHurtResistantTimeField();

	void setHurtResistantTimeField(int hurtResistantTime);

	int getHurtTimeField();

	void setHurtTimeField(int hurtTime);

	boolean getIgnoreFrustumCheckField();

	void setIgnoreFrustumCheckField(boolean ignoreFrustumCheck);

	boolean getInPortalField();

	void setInPortalField(boolean inPortal);

	boolean getInWaterField();

	void setInWaterField(boolean inWater);

	net.minecraft.entity.player.InventoryPlayer getInventoryField();

	void setInventoryField(net.minecraft.entity.player.InventoryPlayer inventory);

	net.minecraft.inventory.Container getInventoryContainerField();

	void setInventoryContainerField(net.minecraft.inventory.Container inventoryContainer);

	boolean getIsAirBorneField();

	void setIsAirBorneField(boolean isAirBorne);

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

	float getLastDamageField();

	void setLastDamageField(float lastDamage);

	double getLastTickPosXField();

	void setLastTickPosXField(double lastTickPosX);

	double getLastTickPosYField();

	void setLastTickPosYField(double lastTickPosY);

	double getLastTickPosZField();

	void setLastTickPosZField(double lastTickPosZ);

	float getLimbSwingField();

	void setLimbSwingField(float limbSwing);

	float getLimbSwingAmountField();

	void setLimbSwingAmountField(float limbSwingAmount);

	int getMaxHurtResistantTimeField();

	void setMaxHurtResistantTimeField(int maxHurtResistantTime);

	int getMaxHurtTimeField();

	void setMaxHurtTimeField(int maxHurtTime);

	net.minecraft.client.Minecraft getMcField();

	void setMcField(net.minecraft.client.Minecraft mc);

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

	net.minecraft.util.MovementInput getMovementInputField();

	void setMovementInputField(net.minecraft.util.MovementInput movementInput);

	net.minecraft.entity.Entity.EnumEntitySize getMyEntitySizeField();

	void setMyEntitySizeField(net.minecraft.entity.Entity.EnumEntitySize myEntitySize);

	int getNewPosRotationIncrementsField();

	void setNewPosRotationIncrementsField(int newPosRotationIncrements);

	double getNewPosXField();

	void setNewPosXField(double newPosX);

	double getNewPosYField();

	void setNewPosYField(double newPosY);

	double getNewPosZField();

	void setNewPosZField(double newPosZ);

	double getNewRotationPitchField();

	void setNewRotationPitchField(double newRotationPitch);

	double getNewRotationYawField();

	void setNewRotationYawField(double newRotationYaw);

	boolean getNoClipField();

	void setNoClipField(boolean noClip);

	boolean getOnGroundField();

	void setOnGroundField(boolean onGround);

	net.minecraft.inventory.Container getOpenContainerField();

	void setOpenContainerField(net.minecraft.inventory.Container openContainer);

	net.minecraft.util.ChunkCoordinates getPlayerLocationField();

	void setPlayerLocationField(net.minecraft.util.ChunkCoordinates playerLocation);

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

	float getPrevDistanceWalkedModifiedField();

	void setPrevDistanceWalkedModifiedField(float prevDistanceWalkedModified);

	float getPrevHealthField();

	void setPrevHealthField(float prevHealth);

	float getPrevLimbSwingAmountField();

	void setPrevLimbSwingAmountField(float prevLimbSwingAmount);

	double getPrevPosXField();

	void setPrevPosXField(double prevPosX);

	double getPrevPosYField();

	void setPrevPosYField(double prevPosY);

	double getPrevPosZField();

	void setPrevPosZField(double prevPosZ);

	float getPrevRenderArmPitchField();

	void setPrevRenderArmPitchField(float prevRenderArmPitch);

	float getPrevRenderArmYawField();

	void setPrevRenderArmYawField(float prevRenderArmYaw);

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

	float getPrevTimeInPortalField();

	void setPrevTimeInPortalField(float prevTimeInPortal);

	boolean getPreventEntitySpawningField();

	void setPreventEntitySpawningField(boolean preventEntitySpawning);

	java.util.Random getRandField();

	void setRandField(java.util.Random rand);

	float getRandomYawVelocityField();

	void setRandomYawVelocityField(float randomYawVelocity);

	int getRecentlyHitField();

	void setRecentlyHitField(int recentlyHit);

	float getRenderArmPitchField();

	void setRenderArmPitchField(float renderArmPitch);

	float getRenderArmYawField();

	void setRenderArmYawField(float renderArmYaw);

	double getRenderDistanceWeightField();

	void setRenderDistanceWeightField(double renderDistanceWeight);

	float getRenderYawOffsetField();

	void setRenderYawOffsetField(float renderYawOffset);

	net.minecraft.entity.Entity getRiddenByEntityField();

	void setRiddenByEntityField(net.minecraft.entity.Entity riddenByEntity);

	net.minecraft.entity.Entity getRidingEntityField();

	void setRidingEntityField(net.minecraft.entity.Entity ridingEntity);

	float getRotationPitchField();

	void setRotationPitchField(float rotationPitch);

	float getRotationYawField();

	void setRotationYawField(float rotationYaw);

	float getRotationYawHeadField();

	void setRotationYawHeadField(float rotationYawHead);

	int getScoreValueField();

	void setScoreValueField(int scoreValue);

	int getServerPosXField();

	void setServerPosXField(int serverPosX);

	int getServerPosYField();

	void setServerPosYField(int serverPosY);

	int getServerPosZField();

	void setServerPosZField(int serverPosZ);

	boolean getSleepingField();

	void setSleepingField(boolean sleeping);

	float getSpeedInAirField();

	void setSpeedInAirField(float speedInAir);

	float getSpeedOnGroundField();

	void setSpeedOnGroundField(float speedOnGround);

	int getSprintToggleTimerField();

	void setSprintToggleTimerField(int sprintToggleTimer);

	int getSprintingTicksLeftField();

	void setSprintingTicksLeftField(int sprintingTicksLeft);

	float getStepHeightField();

	void setStepHeightField(float stepHeight);

	float getSwingProgressField();

	void setSwingProgressField(float swingProgress);

	int getSwingProgressIntField();

	void setSwingProgressIntField(int swingProgressInt);

	int getTeleportDirectionField();

	void setTeleportDirectionField(int teleportDirection);

	int getTicksExistedField();

	void setTicksExistedField(int ticksExisted);

	float getTimeInPortalField();

	void setTimeInPortalField(float timeInPortal);

	int getTimeUntilPortalField();

	void setTimeUntilPortalField(int timeUntilPortal);

	boolean getVelocityChangedField();

	void setVelocityChangedField(boolean velocityChanged);

	float getWidthField();

	void setWidthField(float width);

	net.minecraft.world.World getWorldObjField();

	void setWorldObjField(net.minecraft.world.World worldObj);

	int getXpCooldownField();

	void setXpCooldownField(int xpCooldown);

	float getYOffsetField();

	void setYOffsetField(float yOffset);

	float getYSizeField();

	void setYSizeField(float ySize);

}
