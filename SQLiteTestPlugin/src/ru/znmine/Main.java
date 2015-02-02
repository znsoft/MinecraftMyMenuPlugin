package ru.znmine;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin implements Listener {

	/*
	 * Things to note when using this plugin: [1] Make sure you define the file,
	 * and create a new SQLite with the file [2] Make sure to open the
	 * connection after creating the SQLite [3] Do whatever you want [4] Close
	 * the SQLite at the end on the onDisable();
	 */
	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;
	public Map<Player, Boolean> pluginEnabled = new HashMap<Player, Boolean>();
	IconMenu menu;

	public void onEnable() {
		//Bukkit.getPluginManager().registerEvents(this, this);
		//log.info("znmine Plugin start");
		File file = new File(getDataFolder(), "znmine.db");
		sqlite = new SQLite(file);
		sqlite.open();
		sqlite.execute("CREATE TABLE IF NOT EXISTS PlayerTimes ( playername PRIMARY KEY, op , privileges , money , ip );");

	}

	public void onDisable() {
		sqlite.close();
	}



}