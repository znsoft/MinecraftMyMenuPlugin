package com.droppages.Skepter.SQL;

import java.io.File;

public abstract interface FileManager
{
  public abstract String getDirectory();

  public abstract void setDirectory(String paramString);

  public abstract String getFilename();

  public abstract void setFilename(String paramString);

  public abstract String getExtension();

  public abstract void setExtension(String paramString);

  public abstract File getFile();

  public abstract void setFile();

  public abstract void setFile(String paramString1, String paramString2);

  public abstract void setFile(String paramString1, String paramString2, String paramString3);
}