package com.droppages.Skepter.SQL;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.droppages.Skepter.SQL.DatabaseNames;

public enum Parameter
{
  PREFIX(new DatabaseNames[] { DatabaseNames.Other }), 
  LOCATION(new DatabaseNames[] { DatabaseNames.SQLite }), 
  FILENAME(new DatabaseNames[] { DatabaseNames.SQLite });

  private Set<DatabaseNames> types;
  private static Map<DatabaseNames, Integer> count;

  static { count = new EnumMap<DatabaseNames, Integer>(DatabaseNames.class); }

  private Parameter(DatabaseNames[] type)
  {
    this.types = new HashSet<DatabaseNames>();
    for (int i = 0; i < type.length; i++) {
      this.types.add(type[i]);
      updateCount(type[i]);
    }
  }

  public boolean validParam(DatabaseNames check) {
    if (this.types.contains(DatabaseNames.Other)) {
      return true;
    }
    return this.types.contains(check);
  }

  private static void updateCount(DatabaseNames type)
  {
    Integer nb = (Integer)count.get(type);
    if (nb == null)
      nb = Integer.valueOf(1);
    else
      nb = Integer.valueOf(nb.intValue() + 1);
    count.put(type, nb);
  }

  public static int getCount(DatabaseNames type) {
    int nb = ((Integer)count.get(DatabaseNames.Other)).intValue() + ((Integer)count.get(type)).intValue();
    return nb;
  }
}