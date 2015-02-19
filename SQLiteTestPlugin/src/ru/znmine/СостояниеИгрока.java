package ru.znmine;

import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class СостояниеИгрока {
	public ItemStack ВещьМеню;
	public String ИмяВещьМеню;
	public boolean ВМиниигре;
	public double Рублей;
	public ArrayList<МоиИконки> МенюИгрока;

	
	public СостояниеИгрока(String ИмяВещьМеню, String ВтороеИмя){
		 ВещьМеню = new ItemStack(Material.WATCH, 1);
		 this.ИмяВещьМеню = ИмяВещьМеню;
		 ItemMeta im = ВещьМеню.getItemMeta();
		 im.setDisplayName(ИмяВещьМеню);
		 im.setLore(Arrays.asList(ВтороеИмя));
		 ВещьМеню.setItemMeta(im);
	}
	
public class МоиИконки{
	public String Имя;
	public String Команда;
	public ItemStack Иконка;
}	
	
}
