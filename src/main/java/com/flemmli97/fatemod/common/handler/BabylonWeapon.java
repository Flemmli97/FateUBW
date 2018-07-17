package com.flemmli97.fatemod.common.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.flemmli97.fatemod.common.item.weapon.ClassSpear;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BabylonWeapon {
	
	private static String fileName = "config/fate/babylon.txt";
	private static File file = new File(fileName);
	
	public static void write()
	{
		try
		{
			FileWriter writer = new FileWriter(file);
			BufferedWriter buf = new BufferedWriter(writer);
			
			buf.write("Changing this file will do nothing!");
			buf.newLine();
			
			@SuppressWarnings("deprecation")
			RegistryNamespaced<ResourceLocation, Item> test = net.minecraftforge.fml.common.registry.GameData.getItemRegistry();

			Iterator<Item> it = test.iterator();
			while(it.hasNext())
			{
				Item item = it.next();
				if(item instanceof ItemSword || item instanceof ItemTool || item instanceof ClassSpear)
				{				
					buf.write(item.getRegistryName().toString());
					buf.newLine();				
				}
			}
			
			buf.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}		
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getWeapon()
	{
		Random rand = new Random();
		Item weapon = null;
        String line = null;
        List<String> list = new ArrayList<String>();

        try {
            FileReader read = new FileReader(file);

            BufferedReader buf = new BufferedReader(read);

            buf.readLine();
            while((line = buf.readLine()) != null) {
            	list.add(line);
            }
            String[] regName = list.get(rand.nextInt(list.size())).split(":");
            weapon = GameRegistry.findItem(regName[0], regName[1]);
            buf.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
        	ex.printStackTrace();
        }
        ItemStack stack = new ItemStack(weapon, 1, 0);
        return stack;
	}
}
