package ru.znmine;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Менюшка {
	public ArrayList<ItemStack> МенюИгрока;
	public Менюшка(){
		 МенюИгрока = new ArrayList<ItemStack>();	
	}
	public Менюшка ДобавитьЭлементМеню(String name,String... info ) {
    	ItemStack i = new ItemStack(МенюИгрока.size() + 100, 1);
    	МенюИгрока.add(setItemNameAndLore(i, name, info));
    	return this;
    }
	
	
   
   public Менюшка УдалитьПоследнийЭлементМеню(){
	   
   	МенюИгрока.remove(МенюИгрока.size()-1);
    	return this;
    }
	
   public ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
       ItemMeta im = item.getItemMeta();
           im.setDisplayName(name);
           im.setLore(Arrays.asList(lore));
       item.setItemMeta(im);
       return item;
   }
}
