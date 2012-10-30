//package micdoodle8.mods.galacticraft.API;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//
//import cpw.mods.fml.common.FMLLog;
//
//public class PropertyFile 
//{
//	@SuppressWarnings("resource")
//	public String[] readFile(File file)
//	{
//		String[] result = new String[1];
//		
//		try 
//		{
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			
//			String currentLine;
//			
//			try 
//			{
//				while ((currentLine = reader.readLine()) != null)
//				{
//					if (currentLine != null && !currentLine.startsWith("#") && !currentLine.isEmpty() && !currentLine.startsWith("﻿"))
//					{
//						FMLLog.info("" + currentLine);
//						
//						String[] tmp = new String[result.length + 1];
//						
//			            for (int x = 0; x < result.length; x++)
//			            {
//			            	tmp[x] = result[x];
//			            }
//			            
//			            tmp[result.length - 1] = currentLine;
//			            
//			            result = tmp;
//					}
//				}
//				
//				return result;
//			} 
//			catch (IOException e) 
//			{
//				e.printStackTrace();
//			}
//			finally
//			{
//				reader.close();
//			}
//		} 
//		catch (FileNotFoundException e) 
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		}
//		catch (Exception e) 
//		{
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
//}
