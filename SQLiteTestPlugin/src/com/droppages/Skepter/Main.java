package com.droppages.Skepter;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin implements Listener 
{
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