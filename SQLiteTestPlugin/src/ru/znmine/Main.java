package ru.znmine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin  {

	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;
	public Map<Player, Double> PlayerMoney = new HashMap<Player, Double>();
	public Map<Player, Boolean> PlayersInGame = new HashMap<Player, Boolean>();
	public IconMenu menu;

	public void onEnable() {
		File file = new File(getDataFolder(), "znmine.db");
		sqlite = new SQLite(file);
		sqlite.open();
		//sqlite.execute("create table if not exists Players ( playername primary key , op , privileges , money , ip , chatlog , commands , level );");
		menu = new IconMenu("My Fancy Menu", 9,
				new IconMenu.OptionClickEventHandler() {
					@Override
					public void onOptionClick(IconMenu.OptionClickEvent event) {
						event.getPlayer().sendMessage("You have chosen " + event.getName());
						event.setWillClose(true);
					}
				}, this)
				.setOption(3, new ItemStack(Material.APPLE, 1), "Food",
						"The food is delicious")
				.setOption(4, new ItemStack(Material.IRON_SWORD, 1), "Weapon",
						"Weapons are for awesome people")
				.setOption(5, new ItemStack(Material.EMERALD, 1), "Money",
						"Money brings happiness");
		//getServer().getPluginManager().registerEvents(menu, this);
		getServer().getPluginManager().registerEvents(new EvntPlay(this), this);

	}

	public void onDisable() {
		sqlite.close();
	}

}