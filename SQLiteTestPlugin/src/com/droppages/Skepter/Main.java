package com.droppages.Skepter;

import java.io.File;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin implements Listener {
	
	/* Things to note when using this plugin:
	 * [1] Make sure you define the file, and create a new SQLite with the file
	 * [2] Make sure to open the connection after creating the SQLite
	 * [3] Do whatever you want
	 * [4] Close the SQLite at the end on the onDisable();
	 */
	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;

    public void onEnable() {
    	Bukkit.getPluginManager().registerEvents(this, this);

    	log.info("SQLite Test Plugin start");
    	File file = new File(getDataFolder(), "file.db");
    	sqlite = new SQLite(file);
    	sqlite.open();
		sqlite.execute("CREATE TABLE IF NOT EXISTS PlayerTimes (id INT PRIMARY KEY, playername , op , privileges , money , ip );");
		sqlite.execute("INSERT INTO PlayerTimes(id , playername, op) VALUES(0,'Skepter', 'true');");
		ResultSet rs = sqlite.executeQuery("select playername from PlayerTimes;");
		try {
			rs.next();
			Object o = rs.getObject(1);
	    	log.info(o.toString());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void onDisable() {
		sqlite.close();
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
	    event.setJoinMessage(event.getPlayer().getName() + " вошел на сервер");
		sqlite.execute("INSERT INTO PlayerTimes(playername) VALUES('"+event.getPlayer().getName().replace('\'',' ')+"');");
		Player[] players = getServer().getOnlinePlayers(); 
		for(Player player : players){ 
			if(player.isOp()){ 
				player.sendMessage(ChatColor.DARK_PURPLE + event.getPlayer().getName() + "[" + event.getPlayer().getAddress().getAddress().getHostAddress() + "]");
				} }

	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
	    event.setQuitMessage(event.getPlayer().getName() + " покинул сервер");
	}
	
}