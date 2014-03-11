package crawlie.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import crawlie.pages.AnalyzedPage;
import crawlie.pages.AnalyzedList;

/**
 * Database queries happens here, nowhere else!
 * 
 * @author Patrick
 * 
 */
public class DatabaseController {
  private DatabaseConnection conn;

  public DatabaseController() {
    conn = new DatabaseConnection();

    conn.initialize();
    conn.createCleanDb();
  }

  /**
   * Add a single page to the database
   * 
   * @param page
   */
  public void addPage(AnalyzedPage page) {

    try {
      conn.execute(String.format("insert into crawlie values (%s,%s,%s,%s,%s)", page.title,
          page.url, page.domain, page.source, page.getPriority()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add a list of pages to the database through prepared statemenets. This is way more efficient
   * than doing single calls
   * 
   * @param pages
   */
  public void addPages(AnalyzedList pages) {
    String insertQuery =
        "insert into crawlie (title,url,domain,source, priority) values (?,?,?,?,?)";
    PreparedStatement ps;
    try {
      ps = conn.makeBatchUpdate(insertQuery);

      for (AnalyzedPage page : pages) {
        try {
          ps.setString(1, page.title);
          ps.setString(2, page.url);
          ps.setString(3, page.domain);
          // ps.setString(4, page.parent);
          ps.setInt(5, page.getPriority());
          // ps.setString(4, "" + user.getLastLoggedIn());
          ps.addBatch();
        } catch (SQLException e) {
        }
      }
      ps.executeBatch();
      ps.close();
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
}
