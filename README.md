SQLiteTestPlugin
================

Source for Skepter's SQLite API

Simply put, my SQLite API is designed to help people who require SQLite for databases when creating bukkit plugins. It is created for support for that only, but should be compatible with other services as required.

# The following is only for BUKKIT
To start off (with eclipse), download the files (com.droppages.Skepter.SQL) and add it to your JavaProject. From there, you can continue your project. It is crutial to add these lines of code into your Main class:

/*
 * Of course, everything can be adjusted where necessary.
 */
public SQLite sqlite;

public void sqlConnection() {
//The second argument MUST be the name of your plugin
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


//This goes in your onEnable();
sqlConnection();
    	try {
			sqlTableCheck();
		} catch (SQLException e) {
			e.printStackTrace();
		}


//This goes in your onDisable();
sqlite.close();
