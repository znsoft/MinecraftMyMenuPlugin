package com.droppages.Skepter.SQL;

public class DatabaseCreator
{
  public static Database createDatabase(DatabaseConfig config)
    throws InvalidConfigurationException
  {
    if (!config.isValid())
      throw new InvalidConfigurationException(
        "The configuration is invalid, you don't have enought parameters for that DB : " + 
        config.getType());
      return new SQLite(config.getLog(), config.getParameter(Parameter.PREFIX), 
      config.getParameter(Parameter.LOCATION), 
      config.getParameter(Parameter.FILENAME));

  }

  public static FileManager filename() {
    return new FileManagerControl();
  }
}