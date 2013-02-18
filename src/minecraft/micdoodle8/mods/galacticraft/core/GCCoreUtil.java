package micdoodle8.mods.galacticraft.core;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import micdoodle8.mods.galacticraft.API.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import micdoodle8.mods.galacticraft.core.client.GCCorePlayerBaseClient;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenGear;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenMask;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemOxygenTank;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemParachute;
import micdoodle8.mods.galacticraft.core.items.GCCoreItems;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryRocketBench;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.moon.blocks.GCMoonBlocks;
import micdoodle8.mods.galacticraft.moon.items.GCMoonItems;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;

public class GCCoreUtil
{
	public static void addCraftingRecipes()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			" YV",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', GCCoreItems.aluminumCanister,
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketEngine, 1), new Object[] {
			"VY ",
			"XWX",
			"XZX",
			'V', Block.stoneButton,
			'W', GCCoreItems.aluminumCanister,
			'X', GCCoreItems.heavyPlating,
			'Y', Item.flintAndSteel,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.heavyPlating, 2), new Object[] {
			"XYZ",
			"XYZ",
			"XYZ",
			'X', "ingotTitanium",
			'Y', "ingotCopper",
			'Z', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketNoseCone, 1), new Object[] {
			" Y ",
			" X ",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', Block.torchRedstoneActive
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.rocketFins, 1), new Object[] {
			" Y ",
			"XYX",
			"X X",
			'X', GCCoreItems.heavyPlating,
			'Y', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.landingPad, 9), new Object[] {
			"YYY",
			"XXX",
			'X', Block.blockSteel,
			'Y', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.airDistributor, 1), new Object[] {
			"WXW",
			"YZY",
			"WXW",
			'W', "ingotAluminium",
			'X', GCCoreItems.airFan,
			'Y', GCCoreItems.airVent,
			'Z', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.blockAirCollector, 1), new Object[] {
			"WWW",
			"YXZ",
			"WVW",
			'W', GCCoreItems.oxygenConcentrator,
			'W', "ingotTitanium",
			'X', GCCoreItems.aluminumCanister,
			'Y', GCCoreItems.airFan,
			'Z', GCCoreItems.airVent
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.rocketBench, 1), new Object[] {
			"XXX",
			"YZY",
			"YYY",
			'X', "ingotAluminium",
			'Y', Block.planks,
			'Z', Block.workbench
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.oxygenPipe, 1), new Object[] {
			"XXX",
			"   ",
			"XXX",
			'X', Block.thinGlass
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.lightOxygenTankFull, 1), new Object[] {
			"Z",
			"X",
			"Y",
			'X', GCCoreItems.aluminumCanister,
			'Y', "ingotCopper",
			'Z', new ItemStack(Block.cloth, 1, 5)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.medOxygenTankFull, 1), new Object[] {
			"ZZ",
			"XX",
			"YY",
			'X', GCCoreItems.aluminumCanister,
			'Y', "ingotAluminium",
			'Z', new ItemStack(Block.cloth, 1, 1)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.heavyOxygenTankFull, 1), new Object[] {
			"ZZZ",
			"XXX",
			"YYY",
			'X', GCCoreItems.aluminumCanister,
			'Y', "ingotTitanium",
			'Z', new ItemStack(Block.cloth, 1, 14)
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.sensorGlasses, 1), new Object[] {
			"YYY",
			"Y Y",
			"XYX",
			'X', GCCoreItems.sensorLens,
			'Y', GCMoonItems.meteoricIronIngot
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.sensorLens, 1), new Object[] {
			"ZYZ",
			"YXY",
			"ZYZ",
			'X', Block.thinGlass,
			'Y', GCMoonItems.meteoricIronIngot,
			'Z', Item.redstone
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.aluminumCanister, 1), new Object[] {
			"X X",
			"X X",
			"XXX",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.airVent, 1), new Object[] {
			"XX",
			"XX",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.airFan, 1), new Object[] {
			"Z Z",
			" Y ",
			"ZXZ",
			'X', Item.redstone,
			'Y', "ingotTitanium",
			'Z', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenMask, 1), new Object[] {
			"XXX",
			"XYX",
			"XXX",
			'X', Block.thinGlass,
			'Y', Item.helmetSteel
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenConcentrator, 1), new Object[] {
			"ZWZ",
			"WYW",
			"ZXZ",
			'W', "ingotAluminium",
			'X', GCCoreItems.airVent,
			'Y', GCCoreItems.aluminumCanister,
			'Z', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumPickaxe, 1), new Object[] {
			"YYY",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			"YY ",
			"YX ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumAxe, 1), new Object[] {
			" YY",
			" XY",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			" YY",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHoe, 1), new Object[] {
			"YY ",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumSpade, 1), new Object[] {
			" Y ",
			" X ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumSword, 1), new Object[] {
			" Y ",
			" Y ",
			" X ",
			'Y', "ingotTitanium",
			'X', Item.stick
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumBoots, 1), new Object[] {
			"X X",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumChestplate, 1), new Object[] {
			"X X",
			"XXX",
			"XXX",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumLeggings, 1), new Object[] {
			"XXX",
			"X X",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.titaniumHelmet, 1), new Object[] {
			"XXX",
			"X X",
			'X', "ingotTitanium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.canvas, 1), new Object[] {
			" XY",
			"XXX",
			"YX ",
			'Y', Item.stick, 'X', Item.silk
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.parachute, 1, 0), new Object[] {
			"XXX",
			"Y Y",
			" Y ",
			'X', GCCoreItems.canvas, 'Y', Item.silk
		}));
		
        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.parachute, 1, GCCoreItemParachute.getParachuteDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.parachute, 1, 0)});
        }
        
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.flagPole, 2, 0), new Object[] {
			"X",
			"X",
			"X",
			'X', "ingotAluminium"
		}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.flag, 1, 16), new Object[] {
			"XYY",
			"XYY",
			"X  ",
			'X', GCCoreItems.flagPole, 'Y', GCCoreItems.canvas
		}));
		
        for (int var2 = 0; var2 < 16; ++var2)
        {
        	CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.flag, 1, GCCoreItemFlag.getFlagDamageValueFromDye(var2)), new Object[] {new ItemStack(Item.dyePowder, 1, var2), new ItemStack(GCCoreItems.flag, 1, 16)});
        }
        
        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(GCCoreItems.rocketFuelBucket, 1), new Object[] {new ItemStack(Item.fireballCharge, 1), new ItemStack(Item.gunpowder, 1), new ItemStack(Item.bucketEmpty, 1)});

		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreItems.oxygenGear), new Object[] {
			" Y ",
			"YXY",
			"Y Y",
			'X', GCCoreItems.oxygenConcentrator, 'Y', GCCoreBlocks.oxygenPipe
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 0), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotCopper"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 1), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 2), new Object[] {
			"XXX",
			"XXX",
			"XXX",
			'X', "ingotTitanium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 3), new Object[] {
			"   ",
			" XY",
			"   ",
			'X', Block.stone, 'Y', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.decorationBlocks, 1, 4), new Object[] {
			"   ",
			" X ",
			" Y ",
			'X', Block.stone, 'Y', "ingotAluminium"
		}));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(GCCoreBlocks.airLockFrame, 2), new Object[] {
			"XXX",
			"YVY",
			"ZWZ",
			'V', GCCoreBlocks.oxygenPipe, 'W', Item.redstone, 'X', GCMoonItems.meteoricIronIngot, 'Y', "ingotTitanium", 'Z', GCCoreItems.oxygenConcentrator
		}));
	}
	
	public static void addSmeltingRecipes()
	{
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 0, new ItemStack(GCCoreItems.ingotCopper), 0.1F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 1, new ItemStack(GCCoreItems.ingotAluminum), 0.3F);
		FurnaceRecipes.smelting().addSmelting(GCCoreBlocks.blockOres.blockID, 2, new ItemStack(GCCoreItems.ingotTitanium), 1.0F);
	}
	
	public static Packet250CustomPayload createRealObjPacket(String channel, int packetID, Object[] input)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream data = null;
        
		try
		{
			data = new ObjectOutputStream(bytes);
		}
		catch (final IOException e1)
		{
			e1.printStackTrace();
		}
		
        try
        {
        	if (data != null)
        	{
                data.write(packetID);

                if (input != null)
                {
                    for (final Object obj : input)
                    {
                        GCCoreUtil.writeObjectToStream(obj, data);
                    }
                }
        	}
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }
	
	public static Packet250CustomPayload createPacket(String channel, int packetID, Object[] input)
    {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream(bytes);
        try
        {
            data.write(packetID);

            if (input != null)
            {
                for (final Object obj : input)
                {
                    GCCoreUtil.writeObjectToStream(obj, data);
                }
            }

        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        final Packet250CustomPayload packet = new Packet250CustomPayload();
        packet.channel = channel;
        packet.data = bytes.toByteArray();
        packet.length = packet.data.length;

        return packet;
    }

    public static Object[] readPacketData(DataInputStream data, Class[] packetDataTypes)
    {
        final List result = new ArrayList<Object>();

        try
        {
            for (final Class curClass : packetDataTypes)
            {
                result.add(GCCoreUtil.readObjectFromStream(data, curClass));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }

    public static Object[] readRealObjPacketData(ObjectInputStream data, Class[] packetDataTypes)
    {
        final List result = new ArrayList<Object>();

        try
        {
            for (final Class curClass : packetDataTypes)
            {
                result.add(GCCoreUtil.readObjectFromStream(data, curClass));
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result.toArray();
    }
    
    private static void writeObjectToStream(Object obj, DataOutputStream data) throws IOException
    {
        final Class objClass = obj.getClass();

        if (objClass.equals(Boolean.class))
        {
            data.writeBoolean((Boolean) obj);
        }
        else if (objClass.equals(Byte.class))
        {
            data.writeByte((Byte) obj);
        }
        else if (objClass.equals(Integer.class))
        {
            data.writeInt((Integer) obj);
        }
        else if (objClass.equals(String.class))
        {
            data.writeUTF((String) obj);
        }
        else if (objClass.equals(Double.class))
        {
            data.writeDouble((Double) obj);
        }
        else if (objClass.equals(Float.class))
        {
            data.writeFloat((Float) obj);
        }
        else if (objClass.equals(Long.class))
        {
            data.writeLong((Long) obj);
        }
        else if (objClass.equals(Short.class))
        {
            data.writeShort((Short) obj);
        }
    }

    private static Object readObjectFromStream(DataInputStream data, Class curClass) throws IOException
    {
        if (curClass.equals(Boolean.class))
        {
            return data.readBoolean();
        }
        else if (curClass.equals(Byte.class))
        {
            return data.readByte();
        }
        else if (curClass.equals(Integer.class))
        {
            return data.readInt();
        }
        else if (curClass.equals(String.class))
        {
            return data.readUTF();
        }
        else if (curClass.equals(Double.class))
        {
            return data.readDouble();
        }
        else if (curClass.equals(Float.class))
        {
            return data.readFloat();
        }
        else if (curClass.equals(Long.class))
        {
            return data.readLong();
        }
        else if (curClass.equals(Short.class))
        {
            return data.readShort();
        }

        return null;
    }
    
    private static void writeObjectToStream(Object obj, ObjectOutputStream data) throws IOException
    {
        final Class objClass = obj.getClass();

        if (objClass.equals(GCCorePlayerBase.class))
        {
            data.writeObject(obj);
        }
    }

    private static Object readObjectFromStream(ObjectInputStream data, Class curClass) throws IOException
    {
    	if (curClass.equals(GCCorePlayerBase.class))
        {
        	try
        	{
				return data.readObject();
			}
        	catch (final ClassNotFoundException e)
        	{
				e.printStackTrace();
			}
        }

        return null;
    }

    public static int readPacketID(ObjectInputStream data)
    {
        int result = -1;

        try
        {
            result = data.read();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public static int readPacketID(DataInputStream data)
    {
        int result = -1;

        try
        {
            result = data.read();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }

        return result;
    }
    
    public static int getDrainSpacing(ItemStack tank)
    {
		if (tank == null)
		{
			return 0;
		}
		
		if (tank.getItem() instanceof GCCoreItemOxygenTank)
		{
			return 180;
		}

		return 0;
    }
    
    public static GCCoreExplosion createNewExplosion(World world, Entity entity, double x, double y, double z, float size, boolean flaming)
    {
        final GCCoreExplosion explosion = new GCCoreExplosion(world, entity, x, y, z, size);
        explosion.isFlaming = flaming;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
    
    public static List<ItemStack> useless = new ArrayList();
    public static List<ItemStack> common = new ArrayList();
    public static List<ItemStack> uncommon = new ArrayList();
    public static List<ItemStack> rare = new ArrayList();
    public static List<ItemStack> ultrarare = new ArrayList();

    public static boolean generateChestContents(World var1, Random var2, int var3, int var4, int var5)
    {
        boolean var6 = true;
        var1.setBlockWithNotify(var3, var4, var5, Block.chest.blockID);
        int var7;

        for (var7 = 0; var7 < 4; ++var7)
        {
            var6 &= GCCoreUtil.addItemToChest(var1, var2, var3, var4, var5, GCCoreUtil.getCommonItem(var2));
        }

        for (var7 = 0; var7 < 2; ++var7)
        {
            var6 &= GCCoreUtil.addItemToChest(var1, var2, var3, var4, var5, GCCoreUtil.getUncommonItem(var2));
        }

        for (var7 = 0; var7 < 1; ++var7)
        {
            var6 &= GCCoreUtil.addItemToChest(var1, var2, var3, var4, var5, GCCoreUtil.getRareItem(var2));
        }

        return var6;
    }

    public static ItemStack getCommonItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? GCCoreUtil.getRandomItemFromList(GCCoreUtil.useless, var1) : GCCoreUtil.getRandomItemFromList(GCCoreUtil.common, var1);
    }

    public static ItemStack getUncommonItem(Random var1)
    {
        return GCCoreUtil.getRandomItemFromList(GCCoreUtil.uncommon, var1);
    }

    public static ItemStack getRareItem(Random var1)
    {
        return var1.nextInt(4) == 0 ? GCCoreUtil.getRandomItemFromList(GCCoreUtil.ultrarare, var1) : GCCoreUtil.getRandomItemFromList(GCCoreUtil.rare, var1);
    }
    
    public static ItemStack getRandomItemFromList(List list, Random rand)
    {
    	return (ItemStack) list.get(rand.nextInt(list.size()));
    }

    protected static boolean addItemToChest(World var1, Random var2, int var3, int var4, int var5, ItemStack var6)
    {
        final TileEntityChest var7 = (TileEntityChest)var1.getBlockTileEntity(var3, var4, var5);

        if (var7 != null)
        {
            final int var8 = GCCoreUtil.findRandomInventorySlot(var7, var2);

            if (var8 != -1)
            {
                var7.setInventorySlotContents(var8, var6);
                return true;
            }
        }

        return false;
    }

    protected static int findRandomInventorySlot(TileEntityChest var1, Random var2)
    {
        for (int var3 = 0; var3 < 100; ++var3)
        {
            final int var4 = var2.nextInt(var1.getSizeInventory());

            if (var1.getStackInSlot(var4) == null)
            {
                return var4;
            }
        }

        return -1;
    }
	
	public static WorldProvider getProviderForName(String par1String)
	{
		final Integer[] var1 = DimensionManager.getStaticDimensionIDs();
		
		for (final Integer element : var1) {
			if (WorldProvider.getProviderForDimension(element).getDimensionName() != null && WorldProvider.getProviderForDimension(element).getDimensionName().equals(par1String))
			{
				return WorldProvider.getProviderForDimension(element);
			}
		}
		
		return null;
	}
	
	public static int getAmountOfPossibleProviders(Integer[] ids)
	{
		int amount = 0;
		
		for (final Integer id : ids) {
    		if (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider || WorldProvider.getProviderForDimension(id).dimensionId == 0)
    		{
    			amount++;
    		}
		}
		
		return amount;
	}
	
	public static HashMap getArrayOfPossibleDimensions(Integer[] ids)
	{
		final HashMap map = new HashMap();
		
		for (final Integer id : ids) {
    		if (WorldProvider.getProviderForDimension(id) != null && (WorldProvider.getProviderForDimension(id) instanceof IGalacticraftWorldProvider || WorldProvider.getProviderForDimension(id).dimensionId == 0))
    		{
    			map.put(WorldProvider.getProviderForDimension(id).getDimensionName(), WorldProvider.getProviderForDimension(id).dimensionId);
    		}
		}
		
		for (int j = 0; j < GalacticraftCore.subMods.size(); j++)
		{
			if (!GalacticraftCore.subMods.get(j).reachableDestination())
			{
				map.put(GalacticraftCore.subMods.get(j).getDimensionName() + "*", 0);
			}
		}
		
		return map;
	}
	
	public static double getSpaceshipFailChance(EntityPlayer player)
	{
		final Double level = Double.valueOf(player.experienceLevel);
		
		if (level <= 50.0D)
		{
			return 12.5D - level / 4.0D;
		}
		else
		{
			return 0.0;
		}
	}
	
	public static float calculateMarsAngleFromOtherPlanet(long par1, float par3)
	{
        final int var4 = (int)(par1 % 48000L);
        float var5 = (var4 + par3) / 48000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        final float var6 = var5;
        var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
	}
	
	public static float calculateEarthAngleFromOtherPlanet(long par1, float par3)
	{
        final int var4 = (int)(par1 % 48000L);
        float var5 = (var4 + par3) / 48000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        final float var6 = var5;
        var5 = 1.0F - (float)((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
	}
	
	public static ItemStack findMatchingSpaceshipRecipe(GCCoreInventoryRocketBench inventoryRocketBench)
	{
		final ItemStack[] slots = new ItemStack[18];
		
		for (int i = 0; i < 18; i++)
		{
			slots[i] = inventoryRocketBench.getStackInSlot(i + 1);
		}
		
		if (slots[0] != null && slots[1] != null && slots[2] != null && slots[3] != null && slots[4] != null && slots[5] != null && slots[6] != null && slots[7] != null && slots[8] != null && slots[9] != null && slots[10] != null && slots[11] != null && slots[12] != null && slots[13] != null)
		{
			if (slots[0].getItem().itemID == GCCoreItems.rocketNoseCone.itemID)
			{
				int platesInPlace = 0;
				
				for (int i = 1; i < 9; i++)
				{
					if (slots[i].getItem().itemID == GCCoreItems.heavyPlating.itemID)
					{
						platesInPlace++;
					}
				}
				
				if (platesInPlace == 8)
				{
					if (slots[9].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[10].getItem().itemID == GCCoreItems.rocketFins.itemID)
					{
						if (slots[12].getItem().itemID == GCCoreItems.rocketFins.itemID && slots[13].getItem().itemID == GCCoreItems.rocketFins.itemID)
						{
							if (slots[11].getItem().itemID == GCCoreItems.rocketEngine.itemID)
							{
								return new ItemStack(GCCoreItems.spaceship);
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	public static int convertTo32BitColor(int a, int r, int b, int g)
	{
        a = a << 24;
        r = r << 16;
        g = g << 8;
        
        return a | r | g | b;
	}
	
	public static List getPlayersOnPlanet(IMapPlanet planet)
	{
		final List list = new ArrayList();
		
		for (final WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
			if (world != null && world.provider instanceof IGalacticraftWorldProvider)
			{
				if (planet.getSlotRenderer().getPlanetName().toLowerCase().equals(world.provider.getDimensionName().toLowerCase()))
				{
					for (int j = 0; j < world.getLoadedEntityList().size(); j++)
					{
						if (world.getLoadedEntityList().get(j) != null && world.getLoadedEntityList().get(j) instanceof EntityPlayer)
						{
							list.add(((EntityPlayer)world.getLoadedEntityList().get(j)).username);
						}
					}
				}
			}
		}
		
		return list;
	}
	
	public static boolean hasValidOxygenSetup(EntityPlayer player)
	{
		boolean missingComponent = false;
		
		final GCCoreInventoryTankRefill inventory = GCCoreUtil.getPlayerBaseServerFromPlayer(player).playerTankInventory;
		
		if (inventory.getStackInSlot(0) == null || !GCCoreUtil.isItemValid(0, inventory.getStackInSlot(0)))
		{
			missingComponent = true;
		}
		
		if (inventory.getStackInSlot(1) == null || !GCCoreUtil.isItemValid(1, inventory.getStackInSlot(1)))
		{
			missingComponent = true;
		}
		
		if ((inventory.getStackInSlot(2) == null || !GCCoreUtil.isItemValid(2, inventory.getStackInSlot(2))) && (inventory.getStackInSlot(3) == null || !GCCoreUtil.isItemValid(3, inventory.getStackInSlot(3))))
		{
			missingComponent = true;
		}
		
        if (missingComponent)
        {
    		return false;
        }
        else
        {
        	return true;
        }
	}
	
	public static boolean hasValidOxygenSetup(GCCorePlayerBase player)
	{
		boolean missingComponent = false;
		
		final GCCoreInventoryTankRefill inventory = player.playerTankInventory;
		
		if (inventory.getStackInSlot(0) == null || !GCCoreUtil.isItemValid(0, inventory.getStackInSlot(0)))
		{
			missingComponent = true;
		}
		
		if (inventory.getStackInSlot(1) == null || !GCCoreUtil.isItemValid(1, inventory.getStackInSlot(1)))
		{
			missingComponent = true;
		}

		if ((inventory.getStackInSlot(2) == null || !GCCoreUtil.isItemValid(2, inventory.getStackInSlot(2))) && (inventory.getStackInSlot(3) == null || !GCCoreUtil.isItemValid(3, inventory.getStackInSlot(3))))
		{
			missingComponent = true;
		}
		
		if (missingComponent)
        {
    		return false;
        }
        else
        {
        	return true;
        }
	}
	
	public static boolean isItemValid(int slotIndex, ItemStack stack)
	{
		switch (slotIndex)
		{
		case 0:
			return stack.getItem() instanceof GCCoreItemOxygenMask;
		case 1:
			return stack.getItem() instanceof GCCoreItemOxygenGear;
		case 2:
			return GCCoreUtil.getDrainSpacing(stack) > 0;
		case 3:
			return GCCoreUtil.getDrainSpacing(stack) > 0;
		}
		
		return false;
	}
	
	public static boolean shouldDisplayTankGui(GuiScreen gui)
	{
		if (FMLClientHandler.instance().getClient().gameSettings.hideGUI)
		{
			return false;
		}
		
		if (gui == null)
		{
			return true;
		}
		
		if (gui instanceof GCCoreGuiTankRefill)
		{
			return true;
		}
		
		if (gui instanceof GuiInventory)
		{
			return true;
		}
		
		if (gui instanceof GuiChat)
		{
			return true;
		}
		
		return false;
	}
	
	public static GCCorePlayerBase getPlayerBaseServerFromPlayer(EntityPlayer player)
	{
		if (GalacticraftCore.playersServer.size() == 0)
		{
			new EmptyStackException().printStackTrace();
		}
		
	    Iterator it = GalacticraftCore.playersServer.entrySet().iterator();
	    
	    while (it.hasNext()) 
	    {
	        Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerBase) entry.getValue();
	        }

	        it.remove();
	    }
        
        return null;
	}
	
	public static GCCorePlayerBaseClient getPlayerBaseClientFromPlayer(EntityPlayer player)
	{
		if (GalacticraftCore.playersClient.size() == 0)
		{
			new EmptyStackException().printStackTrace();
		}
		
	    Iterator it = GalacticraftCore.playersClient.entrySet().iterator();
	    
	    while (it.hasNext()) 
	    {
	        Map.Entry entry = (Map.Entry)it.next();

	        if (entry.getKey().equals(player.username))
	        {
	        	return (GCCorePlayerBaseClient) entry.getValue();
	        }

	        it.remove();
	    }
        
        return null;
	}
}
