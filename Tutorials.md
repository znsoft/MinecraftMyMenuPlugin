Tutorials
=========

Once you have followed the instructions in the Readme.md file, you can then use these useful snippets of code to help aid your way around it:

To send a query, use:

    sqlite.query(query);

After sending a query which returns data, you must use this code AFTER you have finished with the data:

    try {
      result.close();
    } catch (SQLException e) {
    	e.printStackTrace();
    }

For example:

    public ArrayList<String> getTopTime() {
      ResultSet result = null;
    	  try {
          result = sqlite.query("SELECT time FROM DeathCountdownData ORDER BY time DESC;");
      } catch (SQLException e) {
      	e.printStackTrace();
      }
      ArrayList<String> r = resultToArray(result, "time");
      	try {
      	result.close();
      } catch (SQLException e) {
      	e.printStackTrace();
      }
    	return r;
    }

When necessary, this may return a ResultSet which (if only 1 line of response) can be turned into a string with:

    sqlite.resultToString(ResultSet result, String data);

To use this, the ResultSet must be the returned result and the data must be the column where the data is.

If the data you want is more than one line of response, you have to use this code here:

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

Which has to be (preferrably) in a main class.


