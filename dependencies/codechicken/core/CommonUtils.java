package codechicken.core;

import java.io.File;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommonUtils
{
    private static File minecraftDir;
    
    public static boolean isClient()
    {
        return FMLCommonHandler.instance().getSide().isClient();
    }
    
    public static File getWorldSaveLocation(World world, int dimension)
    {
        File basesave = DimensionManager.getCurrentSaveRootDirectory();
        if(dimension != 0)
            return new File(basesave, world.provider.getSaveFolder());
        
        return basesave;
    }
    
    public static String getWorldName(World world)
    {
        return world.getWorldInfo().getWorldName();
    }
    
    public static int getDimension(World world)
    {
        return world.provider.dimensionId;
    }
    
    public static File getModsFolder()
    {
        return new File(getMinecraftDir(), "mods");
    }
    
    public static File getMinecraftDir()
    {
        if(minecraftDir == null)
            minecraftDir = ReflectionManager.getField(Loader.class, File.class, Loader.instance(), "minecraftDir");
        
        return minecraftDir;
    }        

    public static String getRelativePath(File parent, File child)
    {
        if(parent.isFile() || !child.getPath().startsWith(parent.getPath()))
            return null;
        
        return child.getPath().substring(parent.getPath().length() + 1);
    }
    
    public static int getFreeBlockID(int preferred)
    {
        for(int i = preferred; i < 255; i++)
            if(Block.blocksList[i] == null)
                return i;
        for(int i = preferred - 1; i > 0; i--)
            if(Block.blocksList[i] == null)
                return i;
        return -1;
    }
    
    public static <T> T[] subArray(T[] args, int i)
    {
        if(i > args.length)            
            return (T[]) Array.newInstance(args.getClass().getComponentType(), 0);
        
        T[] narray = (T[]) Array.newInstance(args.getClass().getComponentType(), args.length-i);
        System.arraycopy(args, i, narray, 0, narray.length);
        return narray;
    }
    
    private static byte[] charWidth = new byte[]{4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6};
    
    public static int getCharWidth(char c)
    {
        if (c == 167)
            return -1;
        
        int charIndex = ChatAllowedCharacters.allowedCharacters.indexOf(c);
        if(charIndex + 32 > charWidth.length || charIndex < 0)
            return 0;
        
        return charWidth[charIndex + 32];
    }
    
    public static int getStringWidth(String s)
    {
        if (s == null)
            return 0;
        
        int width = 0;
        boolean var3 = false;

        for (int charIndex = 0; charIndex < s.length(); ++charIndex)
        {
            char c = s.charAt(charIndex);
            int charWidth = getCharWidth(c);

            if (charWidth < 0 && charIndex < s.length() - 1)
            {
                ++charIndex;
                c = s.charAt(charIndex);

                if (c != 108 && c != 76)
                {
                    if (c == 114 || c == 82)
                    {
                        var3 = false;
                    }
                }
                else
                {
                    var3 = true;
                }

                charWidth = getCharWidth(c);
            }

            width += charWidth;

            if (var3)
            {
                ++width;
            }
        }

        return width;
    }
    
    public static List<String> splitChat(String message)
    {
        LinkedList<String> splitNotice = new LinkedList<String>();
        String[] splits = message.split(" ");
        String partial = "";
        int colour = 7;
        for(int i = 0; i < splits.length; i++)
        {
            String next = partial.length() == 0 ? splits[i] : partial+" "+splits[i];
            if(getStringWidth(next) > 377)
            {
                splitNotice.add(colourPrefix(colour)+partial);
                for(int charPos = 0; charPos < partial.length(); charPos++)
                {
                    for(; partial.length() > charPos + 1 && partial.charAt(charPos) == '\247'; charPos++)
                    {
                        char c = partial.toLowerCase().charAt(charPos + 1);
                        if(c == 'k')
                        {
                            continue;
                        }
                        colour = "0123456789abcdef".indexOf(c);
                        if(colour < 0 || colour > 15)
                        {
                            colour = 15;
                        }
                    }
                }
                
                partial = splits[i];
                                
                continue;
            }
            partial = next;
        }
        splitNotice.add(colourPrefix(colour)+partial);
        
        return splitNotice;
    }
    
    public static String colourPrefix(int colour)
    {
        if(colour == -1)
            return "";
        return "\247"+"0123456789abcdef".charAt(colour);
    }
    
    public static boolean isBlock(int ID)
    {
        return ID < Block.blocksList.length && Block.blocksList[ID] != null && Block.blocksList[ID].blockID != 0;
    }

    public static ModContainer findModContainer(String modID)
    {
        for(ModContainer mc : Loader.instance().getModList())
            if(modID.equals(mc.getModId()))
                return mc;
        
        return null;
    }

    public static ItemStack consumeItem(ItemStack stack)
    {
        if(stack.getItem().hasContainerItem())
            return stack.getItem().getContainerItemStack(stack);
        
        if(stack.stackSize == 1)
            return null;
        
        stack.stackSize--;        
        return stack;
    }

    public static String filterText(String s)
    {
        return ChatAllowedCharacters.filerAllowedCharacters(s.replaceAll("\247.", ""));
    }
}
