package ru.znmine;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class СостояниеИгрока {
	public ItemStack ВещьМеню;
	public String ИмяВещьМеню;
	public boolean ВМиниигре;
	public double Рублей;
	public ArrayList<ItemStack> МенюИгрока;

	
	public СостояниеИгрока(String ИмяВещьМеню, String ВтороеИмя){
		 ВещьМеню = new ItemStack(Material.WATCH, 1);
		 this.ИмяВещьМеню = ИмяВещьМеню;
		 ItemMeta im = ВещьМеню.getItemMeta();
		 im.setDisplayName(ИмяВещьМеню);
		 im.setLore(Arrays.asList(ВтороеИмя));
		 ВещьМеню.setItemMeta(im);
	}
	
    public СостояниеИгрока ДобавитьЭлементМеню(String name, String... info) {
    	ItemStack i = new ItemStack(МенюИгрока.size() + 10, 1);
    	МенюИгрока.add(setItemNameAndLore(i, name, info));
    	return this;
    }
   
   public СостояниеИгрока УдалитьПоследнийЭлементМеню(Player p){
   	МенюИгрока.remove(МенюИгрока.size());
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
