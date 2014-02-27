package crawlie;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseController {
  private DatabaseConnection conn;

  public DatabaseController() {
    conn = new DatabaseConnection();

    conn.initialize();
    conn.createCleanDb();
  }

  public void addPage(AnalyzedPage page) {

    try {
      conn.execute(String.format("insert into crawlie values (%s,%s,%s,%s,%s)", page.TITLE,
          page.URL, page.DOMAIN, page.SOURCE, page.getPriority()));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void addPages(AnalyzedPages pages) {
    String insertQuery =
        "insert into crawlie (title,url,domain,source, priority) values (?,?,?,?,?)";
    PreparedStatement ps;
    try {
      ps = conn.makeBatchUpdate(insertQuery);

      for (AnalyzedPage page : pages) {
        try {
          ps.setString(1, page.TITLE);
          ps.setString(2, page.URL);
          ps.setString(3, page.DOMAIN);
          ps.setString(4, page.PARENT);
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
