package com.droppages.Skepter.SQL;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.droppages.Skepter.SQL.FileManager;

public class SQLite extends Database
{
  private FileManager delegate = DatabaseCreator.filename();

  public SQLite(Logger log, String prefix, String directory, String filename)
  {
    super(log, prefix, "[SQLite] ");
    this.delegate.setFile(directory, filename);
    this.driver = DatabaseNames.SQLite;
  }

  public SQLite(Logger log, String prefix, String directory, String filename, String extension) {
    super(log, prefix, "[SQLite] ");
    this.delegate.setFile(directory, filename, extension);
    this.driver = DatabaseNames.SQLite;
  }

  public SQLite(Logger log, String prefix)
  {
    super(log, prefix, "[SQLite] ");
    this.delegate.setFile();
    this.driver = DatabaseNames.SQLite;
  }

  private File getFile() {
    return this.delegate.getFile();
  }

  protected boolean initialize() {
    try {
      Class.forName("org.sqlite.JDBC");
      return true;
    } catch (ClassNotFoundException e) {
      writeError("Class not found in initialize(): " + e, true);
    }return false;
  }

  public boolean open()
  {
    if (initialize()) {
      try {
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + (getFile() == null ? ":memory:" : getFile().getAbsolutePath()));
        return true;
      } catch (SQLException e) {
        writeError("Could not establish an SQLite connection, SQLException: " + e.getMessage(), true);
        return false;
      }
    }
    return false;
  }

  protected void queryValidation(StatementEnum statement) throws SQLException
  {
  }

  public Statements getStatement(String query) throws SQLException
  {
    String[] statement = query.trim().split(" ", 2);
    try {
      Statements converted = Statements.valueOf(statement[0].toUpperCase());
      return converted; } catch (IllegalArgumentException e) {
    }
    throw new SQLException("Unknown statement: \"" + statement[0] + "\".");
  }

  public boolean isTable(String table)
  {
    DatabaseMetaData md = null;
    try {
      md = this.connection.getMetaData();
      ResultSet tables = md.getTables(null, null, table, null);
      if (tables.next()) {
        tables.close();
        return true;
      }
      tables.close();
      return false;
    }
    catch (SQLException e) {
      writeError("Could not check if table \"" + table + "\" exists, SQLException: " + e.getMessage(), true);
    }return false;
  }

  public boolean delete(String table)
  {
    Statement statement = null;
    String query = null;
    try {
      if (!isTable(table)) {
        writeError("Table \"" + table + "\" does not exist.", true);
        return false;
      }
      statement = this.connection.createStatement();
      query = "DELETE FROM " + table + ";";
      statement.execute(query);
      statement.close();
      return true;
    } catch (SQLException e) {
      if ((!e.getMessage().toLowerCase().contains("locking")) && (!e.getMessage().toLowerCase().contains("locked")) && 
        (!e.toString().contains("not return ResultSet")))
        writeError("Error in wipeTable() query: " + e, false); 
    }
    return false;
  }

  @Deprecated
  public ResultSet retry(String query)
  {
    try
    {
      return getConnection().createStatement().executeQuery(query);
    } catch (SQLException e) {
      if ((e.getMessage().toLowerCase().contains("locking")) || (e.getMessage().toLowerCase().contains("locked")))
        writeError("Please close your previous ResultSet to run the query: \n\t" + query, false);
      else {
        writeError("SQLException in retry(): " + e.getMessage(), false);
      }
    }
    return null;
  }
	
	public ArrayList<String> resultToArray(ResultSet result, String data) {
    	ArrayList<String> arr = new ArrayList<String>();
        try {
			while (result.next()) {
			   arr.add(result.getString(data));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	try {
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return arr;
	}
	
	public String resultToString(ResultSet result, String data) {
		try {
			return result.getString(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  return null;
	}
  
  private static enum Statements implements StatementEnum {
    SELECT("SELECT"), 

    INSERT("INSERT"), 

    UPDATE("UPDATE"), 

    DELETE("DELETE"), 

    REPLACE("REPLACE"), 

    CREATE("CREATE"), 

    ALTER("ALTER"), 

    DROP("DROP"), 

    ANALYZE("ANALYZE"), 

    ATTACH("ATTACH"), 

    BEGIN("BEGIN"), 

    DETACH("DETACH"), 

    END("END"), 

    EXPLAIN("EXPLAIN"), 

    INDEXED("INDEXED"), 

    PRAGMA("PRAGMA"), 

    REINDEX("REINDEX"), 

    RELEASE("RELEASE"), 

    SAVEPOINT("SAVEPOINT"), 

    VACUUM("VACUUM"), 

    LINE_COMMENT("--"), 
    BLOCK_COMMENT("/*");

    private String string;

    private Statements(String string) { 
    	this.string = string; 
    }

    public String toString() {
      return this.string;
    }
  }
}