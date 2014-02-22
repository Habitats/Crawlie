package crawlie;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseController {
	private DatabaseConnection conn;

	public DatabaseController() {
		String cfgPath = "crawlie.cfg";
		conn = new DatabaseConnection(cfgPath);

		conn.initialize();
		conn.createCleanDb();
	}

	public void addPage(AnalyzedPage page) {

		try {
			conn.execute(String.format("insert into crawlie values (%s,%s,%s,%s)", page.TITLE, page.URL, page.DOMAIN, page.SOURCE));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addPages(PageQueue pages) {
		String insertQuery = "insert into crawlie (title,url,domain,source) values (?,?,?,?)";
		PreparedStatement ps;
		try {
			ps = conn.makeBatchUpdate(insertQuery);

			for (Page page : pages) {
				try {
					ps.setString(1, ((AnalyzedPage) page).TITLE);
					ps.setString(2, ((AnalyzedPage) page).URL);
					ps.setString(3, ((AnalyzedPage) page).DOMAIN);
					ps.setString(4, ((AnalyzedPage) page).SOURCE.toString());
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
