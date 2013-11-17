package com.droppages.Skepter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.droppages.Skepter.SQL.SQLite;

public class Main extends JavaPlugin implements Listener 
{
	/* List of useful SQLite Commands:
	 * INSERT INTO table_name(name, name, name) VALUES('v1', 'v2', 'v3');
	 * SELECT name FROM table_name WHERE name='v1';
	 * DELETE FROM table_name WHERE name='v1';
	 * UPDATE table_name SET name='v1', name='v2' WHERE name='v3';
	 */
	
	Logger log = Logger.getLogger("Minecraft");
	public SQLite sqlite;
	PluginDescriptionFile description = getDescription();
	String filename = "databasefile";

    public void onEnable() {
    	sqlConnection();
    	try {
			sqlTableCheck();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        getServer().getPluginManager().registerEvents(this, this);
        ResultSet result = null;
        try {
			result = sqlite.query("SELECT health FROM table_name WHERE playername='Skepter';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        System.out.println(resultToStringArray(result, "health"));
        
    }

	public void onDisable() {
		sqlite.close();
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws SQLException {
		Player player = (Player) event.getPlayer();	
		String query = "INSERT INTO table_name(playername, gamemode, health) VALUES('" + player.getName() + "', '" + player.getGameMode().ordinal() + "', '" + player.getHealth() + "');";
		sqlite.query(query);
		return;
	}
	
	
	public void sqlConnection() {
		sqlite = new SQLite(log, "SQLiteTestPlugin", getDataFolder().getAbsolutePath(), "datafilename");
		try {
		sqlite.open();
		    } catch (Exception e) {
		        log.info(e.getMessage());
		        log.info("Shutting Plugin down to prevent data failure - Report this to the author of the plugin");
		        getPluginLoader().disablePlugin(this);
		    }
		}
	
	public void sqlTableCheck() throws SQLException {
	    if(sqlite.checkTable("table_name")){
	  return;
	    }else{
	  sqlite.query("CREATE TABLE table_name (id INT PRIMARY KEY, playername VARCHAR(50),  gamemode VARCHAR(50), health VARCHAR(50));");
	 
	        getLogger().info("table_name has been created");
	    }
	}
	
	@SuppressWarnings("unused")
	public static String resultToStringArray(ResultSet result, String data) {
		 String[] arr = null;
	        try {
				while (result.next()) {
				    String em = result.getString(data);
				   arr = em.split("\n");
				   for (int i =0; i < arr.length; i++){
				       return arr[i];
				   }
				}
			} catch (SQLException e) {
				System.out.println("There was an error reading from the database");
			}
			return null;
	}
}