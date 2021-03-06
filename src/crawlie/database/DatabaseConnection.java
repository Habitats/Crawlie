package crawlie.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import crawlie.Config;

/**
 * Database connection class. This is the only class that should communicate directly with the database.
 *
 * Tested with sqlite only, however, it should work with mysql as well
 *
 * @author Patrick
 */
public class DatabaseConnection {

  private final String jdbcDriver;
  private final String url;
  private final String user;
  private final String password;
  private final String title;
  private final String table;

  private Connection conn;

  public DatabaseConnection() {
    jdbcDriver = Config.getInstance().getJdbcDriver();
    url = Config.getInstance().getDatabaseUrl();
    user = Config.getInstance().getDatabaseUser();
    title = Config.getInstance().getDatabaseTitle();
    table = Config.getInstance().getDatabaseTable();
    password = Config.getInstance().getDatabasePassword();
  }

  public void createCleanDb() {
    // create the initial db
    try {
      Statement st = conn.createStatement();
      st.execute(String.format("drop table if exists %s", title));
      st.execute(String.format("create table %s (%s);", title, table));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void initialize() {

    try {
      Class.forName(jdbcDriver);
      if (user.length() > 0 && password.length() > 0) {
        conn = DriverManager.getConnection(url, user, password);
      } else {
        conn = DriverManager.getConnection(url);
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public boolean execute(String sql) throws SQLException {
    Statement st = conn.createStatement();
    return st.execute(sql);
  }

  public ResultSet makeSingleQuery(String sql) throws SQLException {
    Statement st = conn.createStatement();
    return st.executeQuery(sql);
  }

  public int makeUpdate(String sql) throws SQLException {
    Statement st = conn.createStatement();
    return st.executeUpdate(sql);
  }

  public PreparedStatement makeBatchUpdate(String sql) throws SQLException {
    PreparedStatement st = conn.prepareStatement(sql);
    return st;
  }

  public void closeConnection() throws SQLException {
    conn.close();
  }

}
