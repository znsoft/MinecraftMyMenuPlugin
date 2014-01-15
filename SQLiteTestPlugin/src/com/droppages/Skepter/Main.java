package com.droppages.Skepter;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin {
	
	/* Things to note when using this plugin:
	 * [1] Make sure you define the file, and create a new SQLite with the file
	 * [2] Make sure to open the connection after creating the SQLite
	 * [3] Do whatever you want
	 * [4] Close the SQLite at the end on the onDisable();
	 */
	public Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;

    public void onEnable() {
    	log.info("[SQLite Test Plugin start]");
    	File file = new File(getDataFolder(), "file.db");
    	sqlite = new SQLite(file);
    	sqlite.open();
		sqlite.execute("CREATE TABLE IF NOT EXISTS PlayerTimes (id INT PRIMARY KEY, playername VARCHAR(50), op BOOLEAN);");
		sqlite.execute("INSERT INTO PlayerTimes(playername, op) VALUES('Skepter', 'true');");
    }

	public void onDisable() {
		sqlite.close();
    }
}