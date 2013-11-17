package com.droppages.Skepter.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.droppages.Skepter.SQL.Builder;

public abstract class Database
{
  protected Logger log;
  protected final String PREFIX;
  protected final String DATABASE_PREFIX;
  protected DatabaseNames driver;
  protected Connection connection;
  protected Map<PreparedStatement, StatementEnum> preparedStatements = new HashMap<PreparedStatement, StatementEnum>();
  protected int lastUpdate;

  public Database(Logger log, String prefix, String dp)
    throws DatabaseException
  {
    if (log == null)
      throw new DatabaseException("Logger cannot be null.");
    if ((prefix == null) || (prefix.length() == 0)) {
      throw new DatabaseException("Plugin prefix cannot be null or empty.");
    }
    this.log = log;
    this.PREFIX = prefix;
    this.DATABASE_PREFIX = dp;
  }

  protected final String prefix(String message)
  {
    return this.PREFIX + this.DATABASE_PREFIX + message;
  }

  public final void writeInfo(String toWrite)
  {
    if (toWrite != null)
      this.log.info(prefix(toWrite));
  }

  public final void writeError(String toWrite, boolean severe)
  {
    if (toWrite != null)
      if (severe)
        this.log.severe(prefix(toWrite));
      else
        this.log.warning(prefix(toWrite));
  }

  protected abstract boolean initialize();

  public final DatabaseNames getDriver()
  {
    return getNames();
  }

  public final DatabaseNames getNames()
  {
    return this.driver;
  }

  public abstract boolean open();

  public final boolean close()
  {
    if (this.connection != null) {
      try {
        this.connection.close();
        return true;
      } catch (SQLException e) {
        writeError("Could not close connection, SQLException: " + e.getMessage(), true);
        return false;
      }
    }
    writeError("Could not close connection, it is null.", true);
    return false;
  }

  public final boolean isConnected()
  {
    return isOpen();
  }

  public final Connection getConnection()
  {
    return this.connection;
  }

  public final boolean isOpen()
  {
    if (this.connection != null)
      try {
        if (this.connection.isValid(1))
          return true; 
      } catch (SQLException localSQLException) {
      }
    return false;
  }

  public final boolean isOpen(int seconds) {
    if (this.connection != null)
      try {
        if (this.connection.isValid(seconds))
          return true; 
      } catch (SQLException localSQLException) {
      }
    return false;
  }

  public final int getLastUpdateCount()
  {
    return this.lastUpdate;
  }

  protected abstract void queryValidation(StatementEnum paramStatementEnum)
    throws SQLException;

  public final ResultSet query(String query)
    throws SQLException
  {
    queryValidation(getStatement(query));
    Statement statement = getConnection().createStatement();
    if (statement.execute(query)) {
      return statement.getResultSet();
    }
    int uc = statement.getUpdateCount();
    this.lastUpdate = uc;
    return getConnection().createStatement().executeQuery("SELECT " + uc);
  }

  protected final ResultSet query(PreparedStatement ps, StatementEnum statement)
    throws SQLException
  {
    queryValidation(statement);
    if (ps.execute()) {
      return ps.getResultSet();
    }
    int uc = ps.getUpdateCount();
    this.lastUpdate = uc;
    return this.connection.createStatement().executeQuery("SELECT " + uc);
  }

  public final ResultSet query(PreparedStatement ps)
    throws SQLException
  {
    ResultSet output = query(ps, (StatementEnum)this.preparedStatements.get(ps));
    this.preparedStatements.remove(ps);
    return output;
  }

  public final PreparedStatement prepare(String query)
    throws SQLException
  {
    StatementEnum s = getStatement(query);
    PreparedStatement ps = this.connection.prepareStatement(query);
    this.preparedStatements.put(ps, s);
    return ps;
  }

  public ArrayList<Long> insert(String query)
    throws SQLException
  {
    ArrayList<Long> keys = new ArrayList<Long>();

    PreparedStatement ps = this.connection.prepareStatement(query, 1);
    this.lastUpdate = ps.executeUpdate();

    ResultSet key = ps.getGeneratedKeys();
    if (key.next())
      keys.add(Long.valueOf(key.getLong(1)));
    return keys;
  }

  public ArrayList<Long> insert(PreparedStatement ps) throws SQLException {
    this.lastUpdate = ps.executeUpdate();
    this.preparedStatements.remove(ps);

    ArrayList<Long> keys = new ArrayList<Long>();
    ResultSet key = ps.getGeneratedKeys();
    if (key.next())
      keys.add(Long.valueOf(key.getLong(1)));
    return keys;
  }

  public final ResultSet query(Builder builder)
    throws SQLException
  {
    return query(builder.toString());
  }

  public abstract StatementEnum getStatement(String paramString)
    throws SQLException;

  public boolean createTable(String query)
  {
    return createTable(query);
  }

  public boolean checkTable(String table)
  {
    return isTable(table);
  }

  public boolean wipeTable(String table)
  {
    return delete(table);
  }
  
  public abstract boolean isTable(String paramString);

  public abstract boolean delete(String paramString);
}