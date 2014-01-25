package gregtechmod.api.util;

import gregtechmod.api.GregTech_API;
import gregtechmod.api.enums.GT_Items;
import gregtechmod.api.enums.GT_ToolDictNames;
import gregtechmod.api.events.GT_ScannerEvent;
import gregtechmod.api.interfaces.*;
import gregtechmod.api.items.GT_EnergyArmor_Item;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.RecipeOutput;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * 
 * Just a few Utility Functions I use.
 */
public class GT_Utility {
	public static volatile int VERSION = 407;
	
	public static final List<Character> sNumberedCharacters   = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
	public static final List<Character> sUpperCasedCharacters = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
	public static final List<Character> sLowerCasedCharacters = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z');
	
	public static Field getPublicField(Object aObject, String aField) {
		Field rField = null;
		try {
			rField = aObject.getClass().getDeclaredField(aField);
		} catch (Throwable e) {/*Do nothing*/}
		return rField;
	}
	
	public static Field getField(Object aObject, String aField) {
		Field rField = null;
		try {
			rField = aObject.getClass().getDeclaredField(aField);
			rField.setAccessible(true);
		} catch (Throwable e) {/*Do nothing*/}
		return rField;
	}
	
	public static Field getField(Class aObject, String aField) {
		Field rField = null;
		try {
			rField = aObject.getDeclaredField(aField);
			rField.setAccessible(true);
		} catch (Throwable e) {/*Do nothing*/}
		return rField;
	}
	
	public static Method getMethod(Class aObject, String aMethod, Class<?>... aParameterTypes) {
		Method rMethod = null;
		try {
			rMethod = aObject.getMethod(aMethod, aParameterTypes);
			rMethod.setAccessible(true);
		} catch (Throwable e) {/*Do nothing*/}
		return rMethod;
	}

	public static Method getMethod(Object aObject, String aMethod, Class<?>... aParameterTypes) {
		Method rMethod = null;
		try {
			rMethod = aObject.getClass().getMethod(aMethod, aParameterTypes);
			rMethod.setAccessible(true);
		} catch (Throwable e) {/*Do nothing*/}
		return rMethod;
	}

	public static Field getField(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
		try {
			Field tField = (aObject instanceof Class)?((Class)aObject).getDeclaredField(aField):(aObject instanceof String)?Class.forName((String)aObject).getDeclaredField(aField):aObject.getClass().getDeclaredField(aField);
			if (aPrivate) tField.setAccessible(true);
			return tField;
		} catch (Throwable e) {
			if (aLogErrors) e.printStackTrace(GT_Log.err);
		}
		return null;
	}
	
	public static Object getFieldContent(Object aObject, String aField, boolean aPrivate, boolean aLogErrors) {
		try {
			Field tField = (aObject instanceof Class)?((Class)aObject).getDeclaredField(aField):(aObject instanceof String)?Class.forName((String)aObject).getDeclaredField(aField):aObject.getClass().getDeclaredField(aField);
			if (aPrivate) tField.setAccessible(true);
			return tField.get(aObject instanceof Class || aObject instanceof String ? null : aObject);
		} catch (Throwable e) {
			if (aLogErrors) e.printStackTrace(GT_Log.err);
		}
		return null;
	}
	
	public static Object callPublicMethod(Object aObject, String aMethod, Object... aParameters) {
		return callMethod(aObject, aMethod, false, false, true, aParameters);
	}

	public static Object callPrivateMethod(Object aObject, String aMethod, Object... aParameters) {
		return callMethod(aObject, aMethod, true, false, true, aParameters);
	}
	
	public static Object callMethod(Object aObject, String aMethod, boolean aPrivate, boolean aUseUpperCasedDataTypes, boolean aLogErrors, Object... aParameters) {
		try {
			Class<?>[] tParameterTypes = new Class<?>[aParameters.length];
			for (byte i = 0; i < aParameters.length; i++) {
				if (aParameters[i] instanceof Class) {
					tParameterTypes[i] = (Class)aParameters[i];
					aParameters[i] = null;
				} else {
					tParameterTypes[i] = aParameters[i].getClass();
				}
				if (!aUseUpperCasedDataTypes) {
					if (tParameterTypes[i] == Boolean.class	) tParameterTypes[i] = boolean.class;	else
					if (tParameterTypes[i] == Byte.class	) tParameterTypes[i] = byte.class;		else
					if (tParameterTypes[i] == Short.class	) tParameterTypes[i] = short.class;		else
					if (tParameterTypes[i] == Integer.class	) tParameterTypes[i] = int.class;		else
					if (tParameterTypes[i] == Long.class	) tParameterTypes[i] = long.class;		else
					if (tParameterTypes[i] == Float.class	) tParameterTypes[i] = float.class;		else
					if (tParameterTypes[i] == Double.class	) tParameterTypes[i] = double.class;
				}
			}
			
			Method tMethod = (aObject instanceof Class)?((Class)aObject).getMethod(aMethod, tParameterTypes):aObject.getClass().getMethod(aMethod, tParameterTypes);
			if (aPrivate) tMethod.setAccessible(true);
			return tMethod.invoke(aObject, aParameters);
		} catch (Throwable e) {
			if (aLogErrors) e.printStackTrace(GT_Log.err);
		}
		return null;
	}
	
	public static Object callConstructor(String aClass, int aConstructorIndex, Object aReplacementObject, boolean aLogErrors, Object... aParameters) {
		if (aConstructorIndex < 0) {
			try {
				for (Constructor tConstructor : Class.forName(aClass).getConstructors()) {
					try {
						return tConstructor.newInstance(aParameters);
					} catch (Throwable e) {/*Do nothing*/}
				}
			} catch (Throwable e) {
				if (aLogErrors) e.printStackTrace(GT_Log.err);
			}
		} else {
			try {
				return Class.forName(aClass).getConstructors()[aConstructorIndex].newInstance(aParameters);
			} catch (Throwable e) {
				if (aLogErrors) e.printStackTrace(GT_Log.err);
			}
		}
		return aReplacementObject;
	}
	
	public static String capitalizeString(String aString) {
		if (aString != null && aString.length() > 0) return aString.substring(0, 1).toUpperCase() + aString.substring(1);
		return "";
	}
	
    public static boolean getPotion(EntityLivingBase aPlayer, int aPotionIndex) {
        try  {
        	Field tPotionHashmap = null;
        	
            Field[] var3 = EntityLiving.class.getDeclaredFields();
            int var4 = var3.length;
            
            for (int var5 = 0; var5 < var4; ++var5) {
                Field var6 = var3[var5];
                if (var6.getType() == HashMap.class) {
                    tPotionHashmap = var6;
                    tPotionHashmap.setAccessible(true);
                    break;
                }
            }
            
            if (tPotionHashmap != null) return ((HashMap)tPotionHashmap.get(aPlayer)).get(Integer.valueOf(aPotionIndex)) != null;
        } catch (Throwable e) {
        	if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);
        }
    	return false;
    }
	
    public static String getClassName(Object aObject) {
    	if (aObject == null) return "null";
    	return aObject.getClass().getName().substring(aObject.getClass().getName().lastIndexOf(".")+1);
    }
    
    public static void removePotion(EntityLivingBase aPlayer, int aPotionIndex) {
        try  {
        	Field tPotionHashmap = null;
        	
            Field[] var3 = EntityLiving.class.getDeclaredFields();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field var6 = var3[var5];
                if (var6.getType() == HashMap.class) {
                    tPotionHashmap = var6;
                    tPotionHashmap.setAccessible(true);
                    break;
                }
            }

            if (tPotionHashmap != null) ((HashMap)tPotionHashmap.get(aPlayer)).remove(Integer.valueOf(aPotionIndex));
        } catch (Throwable e) {
        	if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);
        }
    }
	
	public static boolean getFullInvisibility(EntityPlayer aPlayer) {
		try {
			if (aPlayer.isInvisible()) {
				for (int i = 0; i < 4; i++) {
					if (aPlayer.inventory.armorInventory[i] != null) {
						if (aPlayer.inventory.armorInventory[i].getItem() instanceof GT_EnergyArmor_Item) {
							if ((((GT_EnergyArmor_Item)aPlayer.inventory.armorInventory[i].getItem()).mSpecials & 512) != 0) {
								if (GT_ModHandler.canUseElectricItem(aPlayer.inventory.armorInventory[i], 10000)) {
									return true;
								}
							}
						}
					}
				}
			}
		} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
		return false;
	}
	
	public static ItemStack suckOneItemStackAt(World aWorld, double aX, double aY, double aZ, double aL, double aH, double aW) {
		ArrayList<EntityItem> tList = (ArrayList<EntityItem>)aWorld.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(aX, aY, aZ, aX+aL, aY+aH, aZ+aW));
		if (tList.size()>0) {
			aWorld.removeEntity(tList.get(0));
			return tList.get(0).getEntityItem();
		}
		return null;
	}

	public static byte getOppositeSide(int aSide) {
		return (byte)ForgeDirection.getOrientation(aSide).getOpposite().ordinal();
	}
	
	public static byte getTier(int aValue) {
		return (byte)(aValue<=32?1:aValue<=128?2:aValue<=512?3:aValue<=2048?4:aValue<=8192?5:6);
	}
	
	public static void sendChatToPlayer(EntityPlayer aPlayer, String aChatMessage) {
		if (aPlayer != null && aPlayer instanceof EntityPlayerMP && aChatMessage != null) {
			aPlayer.addChatMessage(aChatMessage);
		}
	}
	
	public static boolean isConnectableNonInventoryPipe(Object aTileEntity, int aSide) {
		if (aTileEntity == null) return false;
		try {
//			if (aTileEntity instanceof cofh.api.transport.IItemConduit) return true;
		} catch(Throwable e) {/**/}
		try {
//			if (aTileEntity instanceof buildcraft.api.transport.IPipeTile) return ((buildcraft.api.transport.IPipeTile)aTileEntity).isPipeConnected(ForgeDirection.getOrientation(aSide));
		} catch(Throwable e) {/**/}
		return false;
	}
	
	/**
	 * Moves Stack from Inv-Slot to Inv-Slot, without checking if its even allowed.
	 * @return the Amount of moved Items
	 */
	public static byte moveStackIntoPipe(IInventory aTileEntity1, Object aTileEntity2, int[] aGrabSlots, int aGrabFrom, int aPutTo, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
		if (aTileEntity1 == null || aMaxTargetStackSize <= 0 || aMinTargetStackSize <= 0 || aMinTargetStackSize > aMaxTargetStackSize || aMaxMoveAtOnce <= 0 || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
		if (aTileEntity2 != null) {
			try {
//				if (aTileEntity2 instanceof cofh.api.transport.IItemConduit) {
//					for (int i = 0; i < aGrabSlots.length; i++) {
//						if (listContainsItem(aFilter, aTileEntity1.getStackInSlot(aGrabSlots[i]), true, aInvertFilter)) {
//							if (isAllowedToTakeFromSlot(aTileEntity1, aGrabSlots[i], (byte)aGrabFrom, aTileEntity1.getStackInSlot(aGrabSlots[i]))) {
//								if (Math.max(aMinMoveAtOnce, aMinTargetStackSize) <= aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize) {
//									ItemStack tStack = GT_Utility.copyAmount(Math.min(aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize, Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)), aTileEntity1.getStackInSlot(aGrabSlots[i]));
//									ItemStack rStack = ((cofh.api.transport.IItemConduit)aTileEntity2).insertItem(ForgeDirection.getOrientation(aPutTo), GT_Utility.copy(tStack), false/*true*/);
//									byte tMovedItemCount = (byte)(tStack.stackSize - (rStack == null ? 0 : rStack.stackSize));
//									if (tMovedItemCount >= 1/*Math.max(aMinMoveAtOnce, aMinTargetStackSize)*/) {
//										//((cofh.api.transport.IItemConduit)aTileEntity2).insertItem(ForgeDirection.getOrientation(aPutTo), GT_Utility.copyAmount(tMovedItemCount, tStack), false);
//										aTileEntity1.decrStackSize(aGrabSlots[i], tMovedItemCount);
//										return tMovedItemCount;
//									}
//								}
//							}
//						}
//					}
//					return 0;
//				}
			} catch(Throwable e) {/**/}
			try {
//				if (aTileEntity2 instanceof buildcraft.api.transport.IPipeTile) {
//					for (int i = 0; i < aGrabSlots.length; i++) {
//						if (listContainsItem(aFilter, aTileEntity1.getStackInSlot(aGrabSlots[i]), true, aInvertFilter)) {
//							if (isAllowedToTakeFromSlot(aTileEntity1, aGrabSlots[i], (byte)aGrabFrom, aTileEntity1.getStackInSlot(aGrabSlots[i]))) {
//								if (Math.max(aMinMoveAtOnce, aMinTargetStackSize) <= aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize) {
//									ItemStack tStack = GT_Utility.copyAmount(Math.min(aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize, Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)), aTileEntity1.getStackInSlot(aGrabSlots[i]));
//									byte tMovedItemCount = (byte)((buildcraft.api.transport.IPipeTile)aTileEntity2).injectItem(GT_Utility.copy(tStack), false, ForgeDirection.getOrientation(aPutTo));
//									if (tMovedItemCount >= Math.max(aMinMoveAtOnce, aMinTargetStackSize)) {
//										tMovedItemCount = (byte)(((buildcraft.api.transport.IPipeTile)aTileEntity2).injectItem(GT_Utility.copyAmount(tMovedItemCount, tStack), true, ForgeDirection.getOrientation(aPutTo)));
//										aTileEntity1.decrStackSize(aGrabSlots[i], tMovedItemCount);
//										return tMovedItemCount;
//									}
//								}
//							}
//						}
//					}
//					return 0;
//				}
			} catch(Throwable e) {/**/}
		}
		
		ForgeDirection tDirection = ForgeDirection.getOrientation(aGrabFrom);
		if (aTileEntity1 instanceof TileEntity && tDirection != ForgeDirection.UNKNOWN && tDirection.getOpposite() == ForgeDirection.getOrientation(aPutTo)) {
			int tX = ((TileEntity)aTileEntity1).xCoord + tDirection.offsetX, tY = ((TileEntity)aTileEntity1).yCoord + tDirection.offsetY, tZ = ((TileEntity)aTileEntity1).zCoord + tDirection.offsetZ;
			if (!hasBlockHitBox(((TileEntity)aTileEntity1).worldObj, tX, tY, tZ)) {
				for (int i = 0; i < aGrabSlots.length; i++) {
					if (listContainsItem(aFilter, aTileEntity1.getStackInSlot(aGrabSlots[i]), true, aInvertFilter)) {
						if (isAllowedToTakeFromSlot(aTileEntity1, aGrabSlots[i], (byte)aGrabFrom, aTileEntity1.getStackInSlot(aGrabSlots[i]))) {
							if (Math.max(aMinMoveAtOnce, aMinTargetStackSize) <= aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize) {
								ItemStack tStack = GT_Utility.copyAmount(Math.min(aTileEntity1.getStackInSlot(aGrabSlots[i]).stackSize, Math.min(aMaxMoveAtOnce, aMaxTargetStackSize)), aTileEntity1.getStackInSlot(aGrabSlots[i]));
								EntityItem tEntity = new EntityItem(((TileEntity)aTileEntity1).worldObj, tX+0.5, tY+0.5, tZ+0.5, tStack);
								tEntity.motionX = tEntity.motionY = tEntity.motionZ = 0;
								((TileEntity)aTileEntity1).worldObj.spawnEntityInWorld(tEntity);
								aTileEntity1.decrStackSize(aGrabSlots[i], tStack.stackSize);
								return (byte)tStack.stackSize;
							}
						}
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * Moves Stack from Inv-Slot to Inv-Slot, without checking if its even allowed. (useful for internal Inventory Operations)
	 * @return the Amount of moved Items
	 */
	public static byte moveStackFromSlotAToSlotB(IInventory aTileEntity1, IInventory aTileEntity2, int aGrabFrom, int aPutTo, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
		if (aTileEntity1 == null || aTileEntity2 == null || aMaxTargetStackSize <= 0 || aMinTargetStackSize <= 0 || aMinTargetStackSize > aMaxTargetStackSize || aMaxMoveAtOnce <= 0 || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
		
		ItemStack tStack1 = aTileEntity1.getStackInSlot(aGrabFrom), tStack2 = aTileEntity2.getStackInSlot(aPutTo), tStack3 = null;
		if (tStack1 != null) {
			if (tStack2 != null && !areStacksEqual(tStack1, tStack2)) return 0;
			tStack3 = copy(tStack1);
			aMaxTargetStackSize = (byte)Math.min(aMaxTargetStackSize, Math.min(tStack3.getMaxStackSize(), Math.min(tStack2==null?Integer.MAX_VALUE:tStack2.getMaxStackSize(), aTileEntity2.getInventoryStackLimit())));
			tStack3.stackSize = Math.min(tStack3.stackSize, aMaxTargetStackSize - (tStack2 == null?0:tStack2.stackSize));
			if (tStack3.stackSize > aMaxMoveAtOnce) tStack3.stackSize = aMaxMoveAtOnce;
			if (tStack3.stackSize + (tStack2==null?0:tStack2.stackSize) >= aMinTargetStackSize && tStack3.stackSize >= aMinMoveAtOnce) {
				tStack3 = aTileEntity1.decrStackSize(aGrabFrom, tStack3.stackSize);
				if (tStack3 != null) {
					if (tStack2 == null) {
						aTileEntity2.setInventorySlotContents(aPutTo, copy(tStack3));
					} else {
						tStack2.stackSize += tStack3.stackSize;
					}
					return (byte)tStack3.stackSize;
				}
			}
		}
		return 0;
	}
	
	public static boolean isAllowedToTakeFromSlot(IInventory aTileEntity, int aSlot, byte aSide, ItemStack aStack) {
		if (ForgeDirection.getOrientation(aSide) == ForgeDirection.UNKNOWN) {
			return isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)0, aStack)
				|| isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)1, aStack)
				|| isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)2, aStack)
				|| isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)3, aStack)
				|| isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)4, aStack)
				|| isAllowedToTakeFromSlot(aTileEntity, aSlot, (byte)5, aStack);
		}
		if (aTileEntity instanceof ISidedInventory) return ((ISidedInventory)aTileEntity).canExtractItem(aSlot, aStack, aSide);
		return true;
	}
	
	public static boolean isAllowedToPutIntoSlot(IInventory aTileEntity, int aSlot, byte aSide, ItemStack aStack) {
		if (ForgeDirection.getOrientation(aSide) == ForgeDirection.UNKNOWN) {
			return isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)0, aStack)
				|| isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)1, aStack)
				|| isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)2, aStack)
				|| isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)3, aStack)
				|| isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)4, aStack)
				|| isAllowedToPutIntoSlot(aTileEntity, aSlot, (byte)5, aStack);
		}
		if (aTileEntity instanceof ISidedInventory && !((ISidedInventory)aTileEntity).canInsertItem(aSlot, aStack, aSide)) return false;
		return aTileEntity.isItemValidForSlot(aSlot, aStack);
	}
	
	/**
	 * Moves Stack from Inv-Side to Inv-Side.
	 * @return the Amount of moved Items
	 */
	public static byte moveOneItemStack(Object aTileEntity1, Object aTileEntity2, byte aGrabFrom, byte aPutTo, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
		if (aTileEntity1 != null && aTileEntity1 instanceof IInventory) return moveOneItemStack((IInventory)aTileEntity1, aTileEntity2, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, true);
		return 0;
	}
	
	/**
	 * This is only because I needed an additional Parameter for the Double Chest Check.
	 */
	private static byte moveOneItemStack(IInventory aTileEntity1, Object aTileEntity2, byte aGrabFrom, byte aPutTo, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce, boolean aDoCheckChests) {
		if (aTileEntity1 == null || aMaxTargetStackSize <= 0 || aMinTargetStackSize <= 0 || aMaxMoveAtOnce <= 0 || aMinTargetStackSize > aMaxTargetStackSize || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
		
		int[] tGrabSlots = null;
		if (aTileEntity1 instanceof ISidedInventory) tGrabSlots = ((ISidedInventory)aTileEntity1).getAccessibleSlotsFromSide(aGrabFrom);
		if (tGrabSlots == null) {
			tGrabSlots = new int[aTileEntity1.getSizeInventory()];
			for (int i = 0; i < tGrabSlots.length; i++) tGrabSlots[i] = i;
		}
		
		if (aTileEntity2 != null && aTileEntity2 instanceof IInventory) {
			int[] tPutSlots = null;
			if (aTileEntity2 instanceof ISidedInventory) tPutSlots = ((ISidedInventory)aTileEntity2).getAccessibleSlotsFromSide(aPutTo);
			
			if (tPutSlots == null) {
				tPutSlots = new int[((IInventory)aTileEntity2).getSizeInventory()];
				for (int i = 0; i < tPutSlots.length; i++) tPutSlots[i] = i;
			}
			
			for (int i = 0; i < tGrabSlots.length; i++) {
				for (int j = 0; j < tPutSlots.length; j++) {
					if (listContainsItem(aFilter, aTileEntity1.getStackInSlot(tGrabSlots[i]), true, aInvertFilter)) {
						if (isAllowedToTakeFromSlot(aTileEntity1, tGrabSlots[i], aGrabFrom, aTileEntity1.getStackInSlot(tGrabSlots[i]))) {
							if (isAllowedToPutIntoSlot((IInventory)aTileEntity2, tPutSlots[j], aPutTo, aTileEntity1.getStackInSlot(tGrabSlots[i]))) {
								byte tMovedItemCount = moveStackFromSlotAToSlotB(aTileEntity1, (IInventory)aTileEntity2, tGrabSlots[i], tPutSlots[j], aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce);
								if (tMovedItemCount > 0) return tMovedItemCount;
							}
						}
					}
				}
			}
			
			if (aDoCheckChests && aTileEntity1 instanceof TileEntityChest) {
				TileEntityChest tTileEntity1 = (TileEntityChest)aTileEntity1;
				if (tTileEntity1.adjacentChestChecked) {
					byte tAmount = 0;
					if (tTileEntity1.adjacentChestXNeg != null) {
						tAmount = moveOneItemStack(tTileEntity1.adjacentChestXNeg, aTileEntity2, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity1.adjacentChestZNeg != null) {
						tAmount = moveOneItemStack(tTileEntity1.adjacentChestZNeg, aTileEntity2, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity1.adjacentChestXPos != null) {
						tAmount = moveOneItemStack(tTileEntity1.adjacentChestXPos, aTileEntity2, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity1.adjacentChestZPosition != null) {
						tAmount = moveOneItemStack(tTileEntity1.adjacentChestZPosition, aTileEntity2, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					}
					if (tAmount != 0) return tAmount;
				}
			}
			if (aDoCheckChests && aTileEntity2 instanceof TileEntityChest) {
				TileEntityChest tTileEntity2 = (TileEntityChest)aTileEntity2;
				if (tTileEntity2.adjacentChestChecked) {
					byte tAmount = 0;
					if (tTileEntity2.adjacentChestXNeg != null) {
						tAmount = moveOneItemStack(aTileEntity1, tTileEntity2.adjacentChestXNeg, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity2.adjacentChestZNeg != null) {
						tAmount = moveOneItemStack(aTileEntity1, tTileEntity2.adjacentChestZNeg, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity2.adjacentChestXPos != null) {
						tAmount = moveOneItemStack(aTileEntity1, tTileEntity2.adjacentChestXPos, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					} else if (tTileEntity2.adjacentChestZPosition != null) {
						tAmount = moveOneItemStack(aTileEntity1, tTileEntity2.adjacentChestZPosition, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce, false);
					}
					if (tAmount != 0) return tAmount;
				}
			}
		}
		
		moveStackIntoPipe(aTileEntity1, aTileEntity2, tGrabSlots, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce);
		return 0;
	}
	
	/**
	 * Moves Stack from Inv-Side to Inv-Slot.
	 * @return the Amount of moved Items
	 */
	public static byte moveOneItemStackIntoSlot(Object aTileEntity1, Object aTileEntity2, byte aGrabFrom, int aPutTo, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
		if (aTileEntity1 == null || !(aTileEntity1 instanceof IInventory) || aPutTo < 0 || aMaxTargetStackSize <= 0 || aMinTargetStackSize <= 0 || aMaxMoveAtOnce <= 0 || aMinTargetStackSize > aMaxTargetStackSize || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
		
		int[] tGrabSlots = null;
		if (aTileEntity1 instanceof ISidedInventory) tGrabSlots = ((ISidedInventory)aTileEntity1).getAccessibleSlotsFromSide(aGrabFrom);
		if (tGrabSlots == null) {
			tGrabSlots = new int[((IInventory)aTileEntity1).getSizeInventory()];
			for (int i = 0; i < tGrabSlots.length; i++) tGrabSlots[i] = i;
		}
		
		if (aTileEntity2 != null && aTileEntity2 instanceof IInventory) {
			for (int i = 0; i < tGrabSlots.length; i++) {
				if (listContainsItem(aFilter, ((IInventory)aTileEntity1).getStackInSlot(tGrabSlots[i]), true, aInvertFilter)) {
					if (isAllowedToTakeFromSlot((IInventory)aTileEntity1, tGrabSlots[i], aGrabFrom, ((IInventory)aTileEntity1).getStackInSlot(tGrabSlots[i]))) {
						if (isAllowedToPutIntoSlot((IInventory)aTileEntity2, aPutTo, (byte)6, ((IInventory)aTileEntity1).getStackInSlot(tGrabSlots[i]))) {
							byte tMovedItemCount = moveStackFromSlotAToSlotB((IInventory)aTileEntity1, (IInventory)aTileEntity2, tGrabSlots[i], aPutTo, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce);
							if (tMovedItemCount > 0) return tMovedItemCount;
						}
					}
				}
			}
		}
		
		moveStackIntoPipe(((IInventory)aTileEntity1), aTileEntity2, tGrabSlots, aGrabFrom, aPutTo, aFilter, aInvertFilter, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce);
		return 0;
	}
	
	/**
	 * Moves Stack from Inv-Slot to Inv-Slot.
	 * @return the Amount of moved Items
	 */
	public static byte moveFromSlotToSlot(IInventory aTileEntity1, IInventory aTileEntity2, int aGrabFrom, int aPutTo, List<ItemStack> aFilter, boolean aInvertFilter, byte aMaxTargetStackSize, byte aMinTargetStackSize, byte aMaxMoveAtOnce, byte aMinMoveAtOnce) {
		if (aTileEntity1 == null || aTileEntity2 == null || aGrabFrom < 0 || aPutTo < 0 || aMaxTargetStackSize <= 0 || aMinTargetStackSize <= 0 || aMaxMoveAtOnce <= 0 || aMinTargetStackSize > aMaxTargetStackSize || aMinMoveAtOnce > aMaxMoveAtOnce) return 0;
		if (listContainsItem(aFilter, aTileEntity1.getStackInSlot(aGrabFrom), true, aInvertFilter)) {
			if (isAllowedToTakeFromSlot(aTileEntity1, aGrabFrom, (byte)6, aTileEntity1.getStackInSlot(aGrabFrom))) {
				if (isAllowedToPutIntoSlot(aTileEntity2, aPutTo, (byte)6, aTileEntity1.getStackInSlot(aGrabFrom))) {
					byte tMovedItemCount = moveStackFromSlotAToSlotB(aTileEntity1, aTileEntity2, aGrabFrom, aPutTo, aMaxTargetStackSize, aMinTargetStackSize, aMaxMoveAtOnce, aMinMoveAtOnce);
					if (tMovedItemCount > 0) return tMovedItemCount;
				}
			}
		}
		return 0;
	}
	
	public static boolean listContainsItem(Collection<ItemStack> aList, ItemStack aStack, boolean aTrueIfListEmpty, boolean aInvertFilter) {
		if (aStack == null || aStack.stackSize < 1) return false;
		if (aList == null) return aTrueIfListEmpty;
		while (aList.contains(null)) aList.remove(null);
		if (aList.size() < 1) return aTrueIfListEmpty;
		Iterator<ItemStack> tIterator = aList.iterator();
		ItemStack tStack = null;
		while (tIterator.hasNext()) if ((tStack = tIterator.next())!= null && areStacksEqual(aStack, tStack)) return !aInvertFilter;
		return aInvertFilter;
	}
	
	public static boolean areStacksOrToolsEqual(ItemStack aStack1, ItemStack aStack2) {
		if (aStack1 != null && aStack2 != null && aStack1.getItem() == aStack2.getItem()) {
			if (aStack1.getItem().isDamageable()) return true;
			return ((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null)) && (aStack1.getTagCompound() == null || aStack1.getTagCompound().equals(aStack2.getTagCompound())) && (aStack1.getItemDamage() == aStack2.getItemDamage() || aStack1.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE || aStack2.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE);
		}
		return false;
	}

	public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2) {
		return areStacksEqual(aStack1, aStack2, false);
	}
	
	public static boolean areStacksEqual(ItemStack aStack1, ItemStack aStack2, boolean aIgnoreNBT) {
		return aStack1 != null && aStack2 != null && aStack1.getItem() == aStack2.getItem() && (aIgnoreNBT || ((aStack1.getTagCompound() == null) == (aStack2.getTagCompound() == null)) && (aStack1.getTagCompound() == null || aStack1.getTagCompound().equals(aStack2.getTagCompound()))) && (aStack1.getItemDamage() == aStack2.getItemDamage() || aStack1.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE || aStack2.getItemDamage() == GregTech_API.ITEM_WILDCARD_DAMAGE);
	}
	
	public static String getFluidName(Fluid aFluid, boolean aLocalized) {
		if (aFluid == null) return "";
		String rName = aLocalized?aFluid.getLocalizedName():aFluid.getUnlocalizedName();
		if (rName.contains(".")) return GT_Utility.capitalizeString(rName.replaceAll("fluid.", "").replaceAll("tile.", ""));
		return rName;
	}
	
	public static String getFluidName(FluidStack aFluid, boolean aLocalized) {
		if (aFluid == null) return "";
		return getFluidName(aFluid.getFluid(), aLocalized);
	}
	
    public static ItemStack fillFluidContainer(FluidStack aFluid, ItemStack aStack) {
		if (isStackInvalid(aStack) || aFluid == null) return null;
    	if (aStack.getItem() != null && aStack.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)aStack.getItem()).getFluid(aStack) == null && ((IFluidContainerItem)aStack.getItem()).getCapacity(aStack) <= aFluid.amount) {
			((IFluidContainerItem)aStack.getItem()).fill(aStack = copyAmount(1, aStack), aFluid, true);
			return aStack;
    	}
        return FluidContainerRegistry.fillFluidContainer(aFluid, aStack);
    }
    
    public static boolean containsFluid(ItemStack aStack, FluidStack aFluid) {
		if (isStackInvalid(aStack) || aFluid == null) return false;
    	if (aStack.getItem() != null && aStack.getItem() instanceof IFluidContainerItem) {
			return FluidStack.areFluidStackTagsEqual(aFluid, ((IFluidContainerItem)aStack.getItem()).getFluid(aStack = copyAmount(1, aStack)));
    	}
    	return FluidContainerRegistry.containsFluid(aStack, aFluid);
    }
    
	public static FluidStack getFluidForFilledItem(ItemStack aStack) {
		if (isStackInvalid(aStack)) return null;
		FluidStack rFluid = FluidContainerRegistry.getFluidForFilledItem(aStack);
		if (rFluid == null && aStack.getItem() != null && aStack.getItem() instanceof IFluidContainerItem) {
			rFluid = ((IFluidContainerItem)aStack.getItem()).drain(copyAmount(1, aStack), Integer.MAX_VALUE, true);
		}
		return rFluid;
	}
	
	public static ItemStack getContainerForFilledItem(ItemStack aStack) {
		if (isStackInvalid(aStack)) return null;
		for (FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData())
			if (areStacksEqual(tData.filledContainer, aStack))
				return tData.emptyContainer;
		if (aStack.getItem() != null && aStack.getItem() instanceof IFluidContainerItem) {
			((IFluidContainerItem)aStack.getItem()).drain(aStack = copyAmount(1, aStack), Integer.MAX_VALUE, true);
			return aStack;
		}
		return null;
	}
	
	public static ItemStack getContainerItem(ItemStack aStack) {
		if (isStackInvalid(aStack)) return null;
		
		if (aStack.getItem().hasContainerItem()) return aStack.getItem().getContainerItemStack(aStack);

		if (GT_Items.Cell_Empty.isStackEqual(aStack)) return null;
		if (GT_Items.IC2_Fuel_Can_Filled.isStackEqual(aStack)) return GT_Items.IC2_Fuel_Can_Empty.get(1);
		
		int tCapsuleCount = GT_ModHandler.getCapsuleCellContainerCount(aStack);
		if (tCapsuleCount > 0) return GT_Items.Cell_Empty.get(tCapsuleCount);
		
		if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, GT_ToolDictNames.craftingToolForgeHammer) || GT_OreDictUnificator.isItemStackInstanceOf(aStack, GT_ToolDictNames.craftingToolWireCutter)) {
			return copyMetaData(aStack.getItemDamage() + 1, aStack);
		}
		
		return null;
	}
	
	public static boolean removeSimpleIC2MachineRecipe(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList, ItemStack aOutput) {
		if ((isStackInvalid(aInput) && isStackInvalid(aOutput)) || aRecipeList == null) return false;
		for (Map.Entry<IRecipeInput, RecipeOutput> tEntry : aRecipeList.entrySet()) {
			if (aInput == null || tEntry.getKey().matches(aInput)) {
				List<ItemStack> tList = tEntry.getValue().items;
				if (tList != null) for (ItemStack tOutput : tList) if (aOutput == null || areStacksEqual(tOutput, aOutput)) {
					aRecipeList.remove(tEntry.getKey());
					removeSimpleIC2MachineRecipe(aInput, aRecipeList, aOutput);
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean addSimpleIC2MachineRecipe(ItemStack aInput, Map<IRecipeInput, RecipeOutput> aRecipeList, ItemStack aOutput) {
		if (isStackInvalid(aInput) || isStackInvalid(aOutput) || aRecipeList == null) return false;
		String tOreName = GT_OreDictUnificator.getAssociation(aInput);
		if (isStringValid(tOreName)) {
			aRecipeList.put(new RecipeInputOreDict(tOreName, aInput.stackSize), new RecipeOutput(null, new ItemStack[] {copy(aOutput)}));
		} else {
			aRecipeList.put(new RecipeInputItemStack(copy(aInput), aInput.stackSize), new RecipeOutput(null, new ItemStack[] {copy(aOutput)}));
		}
		return true;
	}
	
	private static int sBookCount = 0;
	
	public static ItemStack getWrittenBook(String aTitle, String aAuthor, String... aPages) {
		if (isStringInvalid(aTitle) || isStringInvalid(aAuthor) || aPages.length <= 0) return null;
		sBookCount++;
		ItemStack rStack = new ItemStack(Item.writtenBook, 1);
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setString("title", GT_LanguageManager.addStringLocalization("Book." + aTitle + ".Name", aTitle));
        tNBT.setString("author", aAuthor);
        NBTTagList tNBTList = new NBTTagList("pages");
        for (byte i = 0; i < aPages.length; i++) {
        	aPages[i] = GT_LanguageManager.addStringLocalization("Book." + aTitle + ".Page" + ((i<10)?"0"+i:i), aPages[i]);
	        if (i < 48) {
	        	if (aPages[i].length() < 256)
	        		tNBTList.appendTag(new NBTTagString("PAGE " + i, aPages[i]));
	        	else
	        		GT_Log.err.println("WARNING: String for written Book too long! -> " + aPages[i]);
	        } else {
        		GT_Log.err.println("WARNING: Too much Pages for written Book! -> " + aTitle);
	        	break;
	        }
        }
		tNBTList.appendTag(new NBTTagString("FINAL PAGE", "Credits to " + aAuthor + " for writing this Book. This was Book Nr. " + sBookCount + " at its creation. Gotta get 'em all!"));
        tNBT.setTag("pages", tNBTList);
        rStack.setTagCompound(tNBT);
        GregTech_API.sBookList.put(aTitle, rStack);
		return copy(rStack);
	}
	
	public static Map<GT_PlayedSound, Integer> sPlayedSoundMap = new HashMap<GT_PlayedSound, Integer>();
	
	public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength) {
		return doSoundAtClient(aSoundName, aTimeUntilNextSound, aSoundStrength, GregTech_API.gregtechmod.getThePlayer());
	}
	
	public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength, Entity aEntity) {
		if (aEntity == null) return false;
		return doSoundAtClient(aSoundName, aTimeUntilNextSound, aSoundStrength, aEntity.posX, aEntity.posY, aEntity.posZ);
	}
	
	public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength, double aX, double aY, double aZ) {
		return doSoundAtClient(aSoundName, aTimeUntilNextSound, aSoundStrength, 0.9F + new Random().nextFloat() * 0.2F, aX, aY, aZ);
	}
	
	public static boolean doSoundAtClient(String aSoundName, int aTimeUntilNextSound, float aSoundStrength, float aSoundModulation, double aX, double aY, double aZ) {
		GT_PlayedSound tSound;
		if (isStringInvalid(aSoundName) || !FMLCommonHandler.instance().getEffectiveSide().isClient() || sPlayedSoundMap.keySet().contains(tSound = new GT_PlayedSound(aSoundName, aX, aY, aZ)) || GregTech_API.gregtechmod.getThePlayer() == null || !GregTech_API.gregtechmod.getThePlayer().worldObj.isRemote) return false;
		GregTech_API.gregtechmod.getThePlayer().worldObj.playSound(aX, aY, aZ, aSoundName, aSoundStrength, aSoundModulation, false);
		sPlayedSoundMap.put(tSound, aTimeUntilNextSound);
		return true;
	}
	
	public static boolean sendSoundToPlayers(World aWorld, String aSoundName, float aSoundStrength, float aSoundModulation, int aX, int aY, int aZ) {
		if (isStringInvalid(aSoundName) || aWorld == null || aWorld.isRemote) return false;
		
		Packet250CustomPayload tPacket = new Packet250CustomPayload();
		tPacket.channel = GregTech_API.SOUND_PACKET_CHANNEL;
        tPacket.isChunkDataPacket = false;
        
        ByteArrayDataOutput tOut = ByteStreams.newDataOutput();

        tOut.writeInt(aX);
        tOut.writeShort(aY);
        tOut.writeInt(aZ);
		
        tOut.writeUTF(aSoundName);
        tOut.writeFloat(aSoundStrength);
        tOut.writeFloat(aSoundModulation < 0 ? 0.9F + aWorld.rand.nextFloat() * 0.2F : aSoundModulation);
        
        tPacket.data = tOut.toByteArray();
        tPacket.length = tPacket.data.length;
        
        sendPacketToAllPlayersInRange(aWorld, tPacket, aX, aZ);
        
		return true;
	}
	
	public static void sendPacketToAllPlayersInRange(World aWorld, Packet aPacket, int aX, int aZ) {
        for (Object tObject : aWorld.playerEntities) {
        	if (tObject instanceof EntityPlayerMP) {
        		EntityPlayerMP tPlayer = (EntityPlayerMP)tObject;
				if (GregTech_API.gregtechmod.allowPacketToBeSent(aPacket, tPlayer)) {
					Chunk tChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
					if (tPlayer.getServerForPlayer().getPlayerManager().isPlayerWatchingChunk(tPlayer, tChunk.xPosition, tChunk.zPosition)) {
						if (GregTech_API.DEBUG_MODE) GT_Log.out.println("sent Packet to " + tPlayer.username);
						tPlayer.playerNetServerHandler.sendPacketToPlayer(aPacket);
					}
	        	}
        	} else {
        		break;
        	}
        }
	}
	
	public static int stackToInt(ItemStack aStack) {
		if (isStackInvalid(aStack)) return 0;
		return aStack.itemID | (aStack.getItemDamage()<<16);
	}
	
	public static ItemStack intToStack(int aStack) {
		int tID = aStack&(~0>>>16), tMeta = aStack>>>16;
		if (tID > 0 && tID < Item.itemsList.length && Item.itemsList[tID] != null) return new ItemStack(tID, 1, tMeta);
		return null;
	}
	
	public static long stacksToLong(ItemStack aStack1, ItemStack aStack2) {
		return stackToInt(aStack1) | (((long)stackToInt(aStack2)) << 32);
	}
	
	public static boolean arrayContains(Object aObject, Object... aObjects) {
		return listContains(aObject, Arrays.asList(aObjects));
	}
	
	public static boolean listContains(Object aObject, Collection aObjects) {
		if (aObjects == null) return false;
		return aObjects.contains(aObject);
	}
	
	public static short getIDOfBlock(Object aBlock) {
		if (isBlockInvalid(aBlock)) return 0;
		return (short)((Block)aBlock).blockID;
	}
	
	public static Block getBlockFromStack(Object aStack) {
		if (isStackInvalid(aStack)) return null;
		if (((ItemStack)aStack).getItem() instanceof ItemBlock) {
			Block rBlock = Block.blocksList[((ItemBlock)((ItemStack)aStack).getItem()).getBlockID()];
			if (isBlockInvalid(rBlock)) return null;
			return rBlock;
		}
		return null;
	}
	
	public static boolean isBlockValid(Object aBlock) {
		return aBlock != null &&  (aBlock instanceof Block) && ((Block)aBlock).blockID != 0;
	}
	
	public static boolean isBlockInvalid(Object aBlock) {
		return aBlock == null || !(aBlock instanceof Block) || ((Block)aBlock).blockID == 0;
	}
	
	public static boolean isStringValid(Object aString) {
		return aString != null && !aString.toString().isEmpty();
	}
	
	public static boolean isStringInvalid(Object aString) {
		return aString == null || aString.toString().isEmpty();
	}
	
	public static boolean isStackValid(Object aStack) {
		return aStack != null &&  (aStack instanceof ItemStack) && ((ItemStack)aStack).getItem() != null && ((ItemStack)aStack).stackSize >= 0;
	}
	
	public static boolean isStackInvalid(Object aStack) {
		return aStack == null || !(aStack instanceof ItemStack) || ((ItemStack)aStack).getItem() == null || ((ItemStack)aStack).stackSize <  0;
	}
	
	public static boolean isDebugItem(ItemStack aStack) {
		return GT_Items.Armor_Cheat.isStackEqual(aStack, true, true) || areStacksEqual(GT_ModHandler.getIC2Item("debug", 1), aStack, true);
	}
	
	public static boolean isItemStackInList(ItemStack aStack, Collection<Integer> aList) {
		if (isStackInvalid(aStack) || aList == null) return false;
		return aList.contains(stackToInt(aStack)) || aList.contains(stackToInt(copyMetaData(GregTech_API.ITEM_WILDCARD_DAMAGE, aStack)));
	}

	public static boolean isOpaqueBlock(World aWorld, int aX, int aY, int aZ) {
		int tID = aWorld.getBlockId(aX, aY, aZ);
		if (tID > 0 && tID < Block.blocksList.length && Block.blocksList[tID] != null) {
			return Block.blocksList[tID].isOpaqueCube();
		}
		return false;
	}
	
	public static boolean isAirBlock(World aWorld, int aX, int aY, int aZ) {
		int tID = aWorld.getBlockId(aX, aY, aZ);
		if (tID > 0 && tID < Block.blocksList.length && Block.blocksList[tID] != null) {
			return Block.blocksList[tID].isAirBlock(aWorld, aX, aY, aZ);
		}
		return true;
	}
	
	public static boolean hasBlockHitBox(World aWorld, int aX, int aY, int aZ) {
		int tID = aWorld.getBlockId(aX, aY, aZ);
		if (tID < Block.blocksList.length && Block.blocksList[tID] != null) {
			return Block.blocksList[tID].getCollisionBoundingBoxFromPool(aWorld, aX, aY, aZ) != null;
		}
		return false;
	}
	
	/**
	 * Converts a Number to a String
	 */
    public static String parseNumberToString(int aNumber) {
    	String tString = "";
    	boolean temp = true, negative = false;
    	
    	if (aNumber<0) {
    		aNumber *= -1;
    		negative = true;
    	}
    	
    	for (int i = 1000000000; i > 0; i /= 10) {
    		int tDigit = (aNumber/i)%10;
			if ( temp && tDigit != 0) temp = false;
			if (!temp) {
				tString += tDigit;
				if (i != 1) for (int j = i; j > 0; j /= 1000) if (j == 1) tString += ",";
			}
    	}
    	
    	if (tString.equals("")) tString = "0";
    	
    	return negative?"-"+tString:tString;
    }
    
	public static ItemStack setStack(Object aSetStack, Object aToStack) {
		if (isStackInvalid(aSetStack) || isStackInvalid(aToStack)) return null;
		((ItemStack)aSetStack).itemID = ((ItemStack)aToStack).itemID;
		((ItemStack)aSetStack).stackSize = ((ItemStack)aToStack).stackSize;
		((ItemStack)aSetStack).setItemDamage(((ItemStack)aToStack).getItemDamage());
		((ItemStack)aSetStack).setTagCompound(((ItemStack)aToStack).getTagCompound());
		return (ItemStack)aSetStack;
	}
	
	public static ItemStack copy(Object... aStacks) {
		for (Object tStack : aStacks) if (isStackValid(tStack)) return ((ItemStack)tStack).copy();
		return null;
	}
	
	public static ItemStack copyAmount(long aAmount, Object... aStacks) {
		ItemStack rStack = copy(aStacks);
		if (isStackInvalid(rStack)) return null;
		if (aAmount > 64) aAmount = 64; else if (aAmount == -1) aAmount = 111; else if (aAmount < 0) aAmount = 0;
		rStack.stackSize = (byte)aAmount;
		return rStack;
	}
	
	public static ItemStack copyMetaData(long aMetaData, Object... aStacks) {
		ItemStack rStack = copy(aStacks);
		if (isStackInvalid(rStack)) return null;
		rStack.setItemDamage((short)aMetaData);
		return rStack;
	}
	
	public static ItemStack copyAmountAndMetaData(long aAmount, long aMetaData, Object... aStacks) {
		ItemStack rStack = copyAmount(aAmount, aStacks);
		if (isStackInvalid(rStack)) return null;
		rStack.setItemDamage((short)aMetaData);
		return rStack;
	}
	
	/**
	 * returns a copy of an ItemStack with its Stacksize being multiplied by aMultiplier
	 */
	public static ItemStack mul(long aMultiplier, Object... aStacks) {
		ItemStack rStack = copy(aStacks);
		if (rStack == null) return null;
		rStack.stackSize *= aMultiplier;
		return rStack;
	}
	
	/**
	 * Loads an ItemStack properly.
	 */
	public static ItemStack loadItem(NBTTagCompound aNBT) {
		ItemStack rStack = ItemStack.loadItemStackFromNBT(aNBT);
		try {
			if (rStack != null && (rStack.getItem().getClass().getName().startsWith("ic2.core.migration"))) {
				rStack.getItem().onUpdate(rStack, GregTech_API.sDummyWorld, null, 0, false);
			}
		} catch(Throwable e) {
			e.printStackTrace(GT_Log.err);
		}
		return GT_OreDictUnificator.get(true, rStack);
	}
	
	/**
	 * Why the fuck do neither Java nor Guava have a Function to do this?
	 */
    public static <K, V extends Comparable> LinkedHashMap<K,V> sortMapByValuesAcending(Map<K,V> aMap) {
        List<Map.Entry<K,V>> tEntrySet = new LinkedList<Map.Entry<K,V>>(aMap.entrySet());
        Collections.sort(tEntrySet, new Comparator<Map.Entry<K,V>>() {@Override public int compare(Entry<K, V> aValue1, Entry<K, V> aValue2) {return aValue1.getValue().compareTo(aValue2.getValue());}});
        LinkedHashMap<K,V> rMap = new LinkedHashMap<K,V>();
        for (Map.Entry<K,V> tEntry : tEntrySet) rMap.put(tEntry.getKey(), tEntry.getValue());
        return rMap;
    }
    
	/**
	 * Why the fuck do neither Java nor Guava have a Function to do this?
	 */
    public static <K, V extends Comparable> LinkedHashMap<K,V> sortMapByValuesDescending(Map<K,V> aMap) {
        List<Map.Entry<K,V>> tEntrySet = new LinkedList<Map.Entry<K,V>>(aMap.entrySet());
        Collections.sort(tEntrySet, new Comparator<Map.Entry<K,V>>() {@Override public int compare(Entry<K, V> aValue1, Entry<K, V> aValue2) {return -aValue1.getValue().compareTo(aValue2.getValue());}});
        LinkedHashMap<K,V> rMap = new LinkedHashMap<K,V>();
        for (Map.Entry<K,V> tEntry : tEntrySet) rMap.put(tEntry.getKey(), tEntry.getValue());
        return rMap;
    }
    
	/**
	 * This checks if the Dimension is really a Dimension and not another Planet or something.
	 * Used for my Teleporter.
	 */
	public static boolean isRealDimension(int aDimensionID) {
		try {
//			if (DimensionManager.getProvider(aDimensionID) instanceof com.xcompwiz.mystcraft.world.WorldProviderMyst) return true;
		} catch (Throwable e) {/*Do nothing*/}
		try {
//			if (DimensionManager.getProvider(aDimensionID) instanceof twilightforest.world.WorldProviderTwilightForest) return true;
		} catch (Throwable e) {/*Do nothing*/}
		return GregTech_API.sDimensionalList.contains(aDimensionID);
	}
	
	public static boolean moveEntityToDimensionAtCoords(Entity aEntity, int aDimension, double aX, double aY, double aZ) {
		WorldServer tTargetWorld = DimensionManager.getWorld(aDimension), tOriginalWorld = DimensionManager.getWorld(aEntity.worldObj.provider.dimensionId);
		if (tTargetWorld != null && tOriginalWorld != null) {
			if (aEntity.ridingEntity != null) aEntity.mountEntity(null);
			if (aEntity.riddenByEntity != null) aEntity.riddenByEntity.mountEntity(null);
			
			if (aEntity instanceof EntityPlayerMP) {
				EntityPlayerMP aPlayer = (EntityPlayerMP)aEntity;
		        aPlayer.dimension = aDimension;
		        aPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(aPlayer.dimension, (byte)aPlayer.worldObj.difficultySetting, tTargetWorld.getWorldInfo().getTerrainType(), tTargetWorld.getHeight(), aPlayer.theItemInWorldManager.getGameType()));
		        tOriginalWorld.removePlayerEntityDangerously(aPlayer);
		        aPlayer.isDead = false;
		        aPlayer.setWorld(tTargetWorld);
		        MinecraftServer.getServer().getConfigurationManager().func_72375_a(aPlayer, tOriginalWorld);
		        aPlayer.playerNetServerHandler.setPlayerLocation(aX+0.5, aY+0.5, aZ+0.5, aPlayer.rotationYaw, aPlayer.rotationPitch);
		        aPlayer.theItemInWorldManager.setWorld(tTargetWorld);
		        MinecraftServer.getServer().getConfigurationManager().updateTimeAndWeatherForPlayer(aPlayer, tTargetWorld);
		        MinecraftServer.getServer().getConfigurationManager().syncPlayerInventory(aPlayer);
		        Iterator tIterator = aPlayer.getActivePotionEffects().iterator();
		        while (tIterator.hasNext()) {
		            PotionEffect potioneffect = (PotionEffect)tIterator.next();
		            aPlayer.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(aPlayer.entityId, potioneffect));
		        }
		        aPlayer.playerNetServerHandler.setPlayerLocation(aX+0.5, aY+0.5, aZ+0.5, aPlayer.rotationYaw, aPlayer.rotationPitch);
		        GameRegistry.onPlayerChangedDimension(aPlayer);
			} else {
				aEntity.setPosition(aX+0.5, aY+0.5, aZ+0.5);
				aEntity.worldObj.removeEntity(aEntity);
				aEntity.dimension = aDimension;
				aEntity.isDead = false;
	            Entity tNewEntity = EntityList.createEntityByName(EntityList.getEntityString(aEntity), tTargetWorld);
	            if (tNewEntity != null) {
	            	tNewEntity.copyDataFrom(aEntity, true);
		            aEntity.setDead();
	            	tNewEntity.isDead = false;
	            	boolean temp = tNewEntity.forceSpawn;
		            tNewEntity.forceSpawn = true;
	            	tTargetWorld.spawnEntityInWorld(tNewEntity);
		            tNewEntity.forceSpawn = temp;
	            	tNewEntity.isDead = false;
	            	aEntity = tNewEntity;
	            }
			}
			
			if (aEntity instanceof EntityLivingBase) {
				((EntityLivingBase)aEntity).setPositionAndUpdate(aX, aY, aZ);
			} else {
				aEntity.setPosition(aX, aY, aZ);
			}
			
            tOriginalWorld.resetUpdateEntityTick();
            tTargetWorld.resetUpdateEntityTick();
			return true;
		}
		return false;
	}
	
	public static synchronized int getCoordinateScan(ArrayList<String> aList, EntityPlayer aPlayer, World aWorld, int aScanLevel, int aX, int aY, int aZ, int aSide, float aClickX, float aClickY, float aClickZ) {
		if (aList == null) return 0;
		
		ArrayList<String> tList = new ArrayList<String>();
		int rEUAmount = 0;
		
		TileEntity tTileEntity = aWorld.getBlockTileEntity(aX, aY, aZ);
	    
	    Block tBlock = Block.blocksList[aWorld.getBlockId(aX, aY, aZ)];
	    
    	tList.add("-----");
	    try {
		    if (tTileEntity != null && tTileEntity instanceof IInventory)
		    	tList.add("Name: " + ((IInventory)tTileEntity).getInvName() + "  ID: " + tBlock.blockID + "  MetaData: " + aWorld.getBlockMetadata(aX, aY, aZ));
		    else
		    	tList.add("Name: " + tBlock.getUnlocalizedName() + "  ID: " + tBlock.blockID + "  MetaData: " + aWorld.getBlockMetadata(aX, aY, aZ));
		    
		    tList.add("Hardness: " + tBlock.getBlockHardness(aWorld, aX, aY, aZ) + "  Blast Resistance: " + tBlock.getExplosionResistance(aPlayer, aWorld, aX, aY, aZ, aPlayer.posX, aPlayer.posY, aPlayer.posZ));
		    if (tBlock.isBeaconBase(aWorld, aX, aY, aZ, aX, aY+1, aZ)) tList.add("Is valid Beacon Pyramid Material");
		} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
	    if (tTileEntity != null) {
			try {if (tTileEntity instanceof IFluidHandler) {
				rEUAmount+=500;
			    FluidTankInfo[] tTanks = ((IFluidHandler)tTileEntity).getTankInfo(ForgeDirection.getOrientation(aSide));
			    if (tTanks != null) for (byte i = 0; i < tTanks.length; i++) {
			    	tList.add("Tank " + i + ": " + (tTanks[i].fluid==null?0:tTanks[i].fluid.amount) + " / " + tTanks[i].capacity + " " + getFluidName(tTanks[i].fluid, true));
			    }
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.reactor.IReactorChamber) {
				rEUAmount+=500;
			    tTileEntity = (TileEntity)(((ic2.api.reactor.IReactorChamber)tTileEntity).getReactor());
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.reactor.IReactor) {
				rEUAmount+=500;
				tList.add("Heat: " + ((ic2.api.reactor.IReactor)tTileEntity).getHeat() + "/" + ((ic2.api.reactor.IReactor)tTileEntity).getMaxHeat()
						+ "  HEM: " + ((ic2.api.reactor.IReactor)tTileEntity).getHeatEffectModifier() + "  Base EU Output: "/* + ((ic2.api.reactor.IReactor)tTileEntity).getOutput()*/);
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.tile.IWrenchable) {
				rEUAmount+=100;
		        tList.add("Facing: " + ((ic2.api.tile.IWrenchable)tTileEntity).getFacing() + " / Chance: " + (((ic2.api.tile.IWrenchable)tTileEntity).getWrenchDropRate()*100) + "%");
			    tList.add(((ic2.api.tile.IWrenchable)tTileEntity).wrenchCanRemove(aPlayer)?"You can remove this with a Wrench":"You can NOT remove this with a Wrench");
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.energy.tile.IEnergyTile) {
				rEUAmount+=200;
			    //aList.add(((ic2.api.energy.tile.IEnergyTile)tTileEntity).isAddedToEnergyNet()?"Added to E-net":"Not added to E-net! Bug?");
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.energy.tile.IEnergySink) {
				rEUAmount+=400;
		        //aList.add("Demanded Energy: " + ((ic2.api.energy.tile.IEnergySink)tTileEntity).demandsEnergy());
		        tList.add("Max Safe Input: " + ((ic2.api.energy.tile.IEnergySink)tTileEntity).getMaxSafeInput());
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.energy.tile.IEnergySource) {
				rEUAmount+=400;
		        //aList.add("Max Energy Output: " + ((ic2.api.energy.tile.IEnergySource)tTileEntity).getMaxEnergyOutput());
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.energy.tile.IEnergyConductor) {
				rEUAmount+=200;
		        tList.add("Conduction Loss: " + ((ic2.api.energy.tile.IEnergyConductor)tTileEntity).getConductionLoss());
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.tile.IEnergyStorage) {
				rEUAmount+=200;
		        tList.add("Contained Energy: " + ((ic2.api.tile.IEnergyStorage)tTileEntity).getStored() + " of " + ((ic2.api.tile.IEnergyStorage)tTileEntity).getCapacity());
		        //aList.add(((ic2.api.tile.IEnergyStorage)tTileEntity).isTeleporterCompatible(ic2.api.Direction.YP)?"Teleporter Compatible":"Not Teleporter Compatible");
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof IUpgradableMachine) {
				rEUAmount+=500;
		    	int tValue = 0;
		    	if (((IUpgradableMachine)tTileEntity).hasMufflerUpgrade()) tList.add("Has Muffler Upgrade");
		    	if (0 < (tValue = ((IUpgradableMachine)tTileEntity).getOverclockerUpgradeCount())) tList.add(tValue	+ " Overclocker Upgrades");
		    	if (0 < (tValue = ((IUpgradableMachine)tTileEntity).getTransformerUpgradeCount())) tList.add(tValue	+ " Transformer Upgrades");
		    	if (0 < (tValue = ((IUpgradableMachine)tTileEntity).getUpgradeStorageVolume()   )) tList.add(tValue	+ " Upgraded EU Capacity");
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof IMachineProgress) {
				rEUAmount+=400;
		    	int tValue = 0;
		    	if (0 < (tValue = ((IMachineProgress)tTileEntity).getMaxProgress())) tList.add("Progress: " + tValue + " / " + ((IMachineProgress)tTileEntity).getProgress());
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ICoverable) {
				rEUAmount+=300;
		    	String tString = ((ICoverable)tTileEntity).getCoverBehaviorAtSide((byte)aSide).getDescription((byte)aSide, ((ICoverable)tTileEntity).getCoverIDAtSide((byte)aSide), ((ICoverable)tTileEntity).getCoverDataAtSide((byte)aSide), (ICoverable)tTileEntity);
		    	if (tString != null && !tString.equals("")) tList.add(tString);
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof IGregTechTileEntity) {
		    	tList.add("Owned by: " + ((IGregTechTileEntity)tTileEntity).getOwnerName());
		    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof IGregTechDeviceInformation && ((IGregTechDeviceInformation)tTileEntity).isGivingInformation()) {
				tList.addAll(Arrays.asList(((IGregTechDeviceInformation)tTileEntity).getInfoData()));
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
			try {if (tTileEntity instanceof ic2.api.crops.ICropTile) {
				if (((ic2.api.crops.ICropTile)tTileEntity).getScanLevel() < 4) {
					rEUAmount+=10000;
					((ic2.api.crops.ICropTile)tTileEntity).setScanLevel((byte)4);
				}
				if (((ic2.api.crops.ICropTile)tTileEntity).getID() >= 0 && ((ic2.api.crops.ICropTile)tTileEntity).getID() < ic2.api.crops.Crops.instance.getCropList().length && ic2.api.crops.Crops.instance.getCropList()[((ic2.api.crops.ICropTile)tTileEntity).getID()] != null) {
					rEUAmount+=1000;
					tList.add("Type -- Crop-Name: " + ic2.api.crops.Crops.instance.getCropList()[((ic2.api.crops.ICropTile)tTileEntity).getID()].name()
			        		+ "  Growth: " + ((ic2.api.crops.ICropTile)tTileEntity).getGrowth()
			        		+ "  Gain: " + ((ic2.api.crops.ICropTile)tTileEntity).getGain()
			        		+ "  Resistance: " + ((ic2.api.crops.ICropTile)tTileEntity).getResistance()
			        		);
			        tList.add("Plant -- Fertilizer: " + ((ic2.api.crops.ICropTile)tTileEntity).getNutrientStorage()
			        		+ "  Water: " + ((ic2.api.crops.ICropTile)tTileEntity).getHydrationStorage()
			        		+ "  Weed-Ex: " + ((ic2.api.crops.ICropTile)tTileEntity).getWeedExStorage()
			        		+ "  Scan-Level: " + ((ic2.api.crops.ICropTile)tTileEntity).getScanLevel()
			        		);
			        tList.add("Environment -- Nutrients: " + ((ic2.api.crops.ICropTile)tTileEntity).getNutrients()
			        		+ "  Humidity: " + ((ic2.api.crops.ICropTile)tTileEntity).getHumidity()
			        		+ "  Air-Quality: " + ((ic2.api.crops.ICropTile)tTileEntity).getAirQuality()
			        		);
			        String tString = "";
			        for (String tAttribute : ic2.api.crops.Crops.instance.getCropList()[((ic2.api.crops.ICropTile)tTileEntity).getID()].attributes()) {
			        	tString += ", " + tAttribute;
			        }
			        tList.add("Attributes:" + tString.replaceFirst(",", ""));
			        tList.add("Discovered by: " + ic2.api.crops.Crops.instance.getCropList()[((ic2.api.crops.ICropTile)tTileEntity).getID()].discoveredBy());
				}
			}} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
    	}
	    try {if (tBlock instanceof IDebugableBlock) {
	    	rEUAmount+=500;
	        ArrayList<String> temp = ((IDebugableBlock)tBlock).getDebugInfo(aPlayer, aX, aY, aZ, 3);
	        if (temp != null) tList.addAll(temp);
	    }} catch(Throwable e) {if (GregTech_API.DEBUG_MODE) e.printStackTrace(GT_Log.err);}
	    
	    GT_ScannerEvent tEvent = new GT_ScannerEvent(aWorld, aPlayer, aX, aY, aZ, (byte)aSide, aScanLevel, tBlock, tTileEntity, tList, aClickX, aClickY, aClickZ);
	    tEvent.mEUCost = rEUAmount;
	    MinecraftForge.EVENT_BUS.post(tEvent);
	    if (!tEvent.isCanceled()) aList.addAll(tList);
		return tEvent.mEUCost;
	}
	
	/**
	 * @return an Array containing the X and the Y Coordinate of the clicked Point, with the top left Corner as Origin, like on the Texture Sheet. return values should always be between 0.0F and 1.0F as long as aX, aY and aZ are valid.
	 */
	public static float[] getClickedFacingCoords(byte aSide, float aX, float aY, float aZ) {
		switch (aSide) {
		case  0: return new float[] {1-aX,   aZ};
		case  1: return new float[] {  aX,   aZ};
		case  2: return new float[] {1-aX, 1-aY};
		case  3: return new float[] {  aX, 1-aY};
		case  4: return new float[] {  aZ, 1-aY};
		case  5: return new float[] {1-aZ, 1-aY};
		default: return new float[] {0.5F, 0.5F};
		}
	}
	
	/**
	 * This Function determines the direction a Block gets when being Wrenched.
	 * returns -1 if invalid. Even though that could never happen.
	 */
	public static byte determineWrenchingSide(byte aSide, float aX, float aY, float aZ) {
		byte tBack = GT_Utility.getOppositeSide(aSide);
		switch (aSide) {
		case  0: case  1:
			if (aX < 0.25) {
				if (aZ < 0.25) return tBack;
				if (aZ > 0.75) return tBack;
				return 4;
			}
			if (aX > 0.75) {
				if (aZ < 0.25) return tBack;
				if (aZ > 0.75) return tBack;
				return 5;
			}
			if (aZ < 0.25) return 2;
			if (aZ > 0.75) return 3;
			return aSide;
		case  2: case  3:
			if (aX < 0.25) {
				if (aY < 0.25) return tBack;
				if (aY > 0.75) return tBack;
				return 4;
			}
			if (aX > 0.75) {
				if (aY < 0.25) return tBack;
				if (aY > 0.75) return tBack;
				return 5;
			}
			if (aY < 0.25) return 0;
			if (aY > 0.75) return 1;
			return aSide;
		case  4: case  5:
			if (aZ < 0.25) {
				if (aY < 0.25) return tBack;
				if (aY > 0.75) return tBack;
				return 2;
			}
			if (aZ > 0.75) {
				if (aY < 0.25) return tBack;
				if (aY > 0.75) return tBack;
				return 3;
			}
			if (aY < 0.25) return 0;
			if (aY > 0.75) return 1;
			return aSide;
		}
		return -1;
	}
}