package ru.znmine;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin  {

	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;
	public IconMenu menu;

	public void onEnable() {
		File file = new File(getDataFolder(), "znmine.db");
		sqlite = new SQLite(file);
		sqlite.open();
		EvntPlay ОбработчикСобытий =  new EvntPlay(this);
		//sqlite.execute("create table if not exists Players ( playername primary key , op , privileges , money , ip , chatlog , commands , level );");
		menu = new IconMenu("Player Menu", 
				new IconMenu.OptionClickEventHandler() {
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						
						//event.getPlayer().performCommand("spawn");
						event.getPlayer().sendMessage("You have chosen " + event.getName());
						event.setWillClose(true);
						Player p = event.getPlayer();
						Inventory inventory = Bukkit.createInventory(p, InventoryType.ENCHANTING);
						p.openInventory(inventory);
						//p.openEnchanting(new InventoryView(), true);
					}
				}, this, ОбработчикСобытий);

		
		
//		
//		menu = new IconMenu("My Fancy Menu", 9,
//				new IconMenu.OptionClickEventHandler() {
//					@Override
//					public void onOptionClick(IconMenu.OptionClickEvent event) {
//						
//						//event.getPlayer().performCommand("spawn");
//						event.getPlayer().sendMessage("You have chosen " + event.getName());
//						event.setWillClose(true);
//						Player p = event.getPlayer();
//						Inventory inventory = Bukkit.createInventory(p, InventoryType.ENCHANTING);
//						p.openInventory(inventory);
//						//p.openEnchanting(new InventoryView(), true);
//					}
//				}, this)
//				.setOption(3, new ItemStack(Material.APPLE, 1), "Food",	        "The food is delicious")
//				.setOption(4, new ItemStack(Material.IRON_SWORD, 1), "Weapon",	"Weapons are for awesome people")
//				.setOption(5, new ItemStack(Material.DIAMOND, 1), "Weapon",	    "Weapons are for awesome people")
//				.setOption(6, new ItemStack(Material.EMERALD, 1), "Money",	    "Money brings happiness");
		getServer().getPluginManager().registerEvents(ОбработчикСобытий, this);
		getServer().getPluginManager().registerEvents(menu, this);

	}

	public void onDisable() {
		sqlite.close();
	}

}